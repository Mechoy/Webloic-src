package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StreamCorruptedException;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.dispatcher.Response;

public final class JMSPushRequest extends Request implements Externalizable {
   static final long serialVersionUID = -7576284721569682185L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int MESSAGE_MASK = 65280;
   private static final int BACK_END_MASK = 65536;
   private static final int NEXT_MASK = 131072;
   private static final int MESSAGE_MASK_SHIFT = 8;
   private MessageImpl message;
   private JMSPushEntry firstPushEntry;
   private JMSPushEntry lastPushEntry;
   private int compressionThreshold = Integer.MAX_VALUE;

   public JMSPushRequest(int var1, JMSID var2, MessageImpl var3) {
      super(var2, 15616 | var1);
      this.message = var3;
   }

   public JMSPushRequest(int var1, JMSID var2, MessageImpl var3, JMSPushEntry var4) {
      super(var2, 15616 | var1);
      this.message = var3;
      this.firstPushEntry = var4;
      if (var4 != null) {
         do {
            this.lastPushEntry = var4;
            var4 = var4.getNext();
         } while(var4 != null);
      }

   }

   public JMSPushRequest(JMSPushRequest var1) {
      super((JMSID)var1.invocableId, var1.methodId);
      this.message = var1.message;
      this.firstPushEntry = var1.firstPushEntry;
      this.lastPushEntry = var1.lastPushEntry;
      this.next = var1.next;
   }

   public final int getCompressionThreshold() {
      return this.compressionThreshold;
   }

   public final void setCompressionThreshold(int var1) {
      this.compressionThreshold = var1;
   }

   public MessageImpl getMessage() {
      return this.message;
   }

   public void setMessage(MessageImpl var1) {
      this.message = var1;
   }

   public void setInvocableType(int var1) {
      this.methodId = this.methodId & 16776960 | var1;
   }

   public void addPushEntry(JMSPushEntry var1) {
      var1.setNext((JMSPushEntry)null);
      if (this.firstPushEntry == null) {
         this.firstPushEntry = var1;
      } else {
         this.lastPushEntry.setNext(var1);
      }

      this.lastPushEntry = var1;
   }

   public JMSPushEntry removePushEntry() {
      JMSPushEntry var1 = this.firstPushEntry;
      if (var1 != null) {
         this.firstPushEntry = var1.getNext();
         if (this.firstPushEntry == null) {
            this.lastPushEntry = null;
         }
      }

      return var1;
   }

   public long getBackEndSequenceNumber() {
      return this.firstPushEntry.getBackEndSequenceNumber();
   }

   public long getFrontEndSequenceNumber() {
      return this.firstPushEntry.getFrontEndSequenceNumber();
   }

   public void setFirstPushEntry(JMSPushEntry var1) {
      this.firstPushEntry = var1;
   }

   public JMSPushEntry getFirstPushEntry() {
      return this.firstPushEntry;
   }

   public void setLastPushEntry(JMSPushEntry var1) {
      this.lastPushEntry = var1;
   }

   public JMSPushEntry getLastPushEntry() {
      return this.lastPushEntry;
   }

   public final JMSPushEntry getPushEntries() {
      return this.firstPushEntry;
   }

   public final void setPushEntries(JMSPushEntry var1) {
      if ((this.firstPushEntry = var1) == null) {
         this.lastPushEntry = null;
      } else {
         for(this.lastPushEntry = var1; this.lastPushEntry.getNext() != null; this.lastPushEntry = this.lastPushEntry.getNext()) {
         }
      }

   }

   public int remoteSignature() {
      return 64;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public JMSPushRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      JMSPushRequest var3 = this;

      do {
         int var2 = var3.message.getType() << 8;
         if (var3.getNext() != null) {
            var2 |= 131072;
         }

         var1.writeInt(var2 | 1);
         if (MessageImpl.debugWire && JMSDebug.JMSDispatcher.isDebugEnabled()) {
            debugWireInfo("JMSPushEntry.write", var2, (Throwable)null, ", msgType 0x" + Integer.toHexString(var3.message.getType()).toUpperCase() + (this.compressionThreshold == Integer.MAX_VALUE ? ", compressionThreshold==MAX_VALUE" : ", compressionThreshold=" + this.compressionThreshold));
         }

         super.writeExternal(var1);
         if (this.compressionThreshold == Integer.MAX_VALUE) {
            var3.message.writeExternal(var1);
         } else {
            var3.message.writeExternal(MessageImpl.createJMSObjectOutputWrapper(var1, this.compressionThreshold, true));
         }

         var3.firstPushEntry.writeExternal(var1, this);
         var3 = (JMSPushRequest)var3.getNext();
      } while(var3 != null);

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      JMSPushRequest var4 = this;

      do {
         int var2 = var1.readInt();
         int var3 = var2 & 255;
         if (var3 != 1) {
            StreamCorruptedException var8 = JMSUtilities.versionIOException(var3, 1, 1);
            debugWireInfo("JMSPushEntry.read ", var2, var8, " threw");
            throw var8;
         }

         try {
            super.readExternal(var1);
         } catch (IOException var6) {
            debugWireInfo("JMSPushEntry.read ", var2, var6, " threw");
            throw var6;
         } catch (ClassNotFoundException var7) {
            debugWireInfo("JMSPushEntry.read ", var2, var7, " threw");
            throw var7;
         }

         byte var5 = (byte)((var2 & '\uff00') >>> 8);
         if (MessageImpl.debugWire && JMSDebug.JMSDispatcher.isDebugEnabled()) {
            debugWireInfo("JMSPushEntry.read ", var2, (Throwable)null, ", msgType 0x" + Integer.toHexString(var5).toUpperCase());
         }

         var4.message = MessageImpl.createMessageImpl(var5);
         var4.message.readExternal(var1);
         var4.firstPushEntry = new JMSPushEntry();
         var4.lastPushEntry = var4.firstPushEntry.readExternal(var1, var4);
         if ((var2 & 131072) != 0) {
            var4.setNext(new JMSPushRequest());
         }
      } while((var4 = (JMSPushRequest)var4.getNext()) != null);

   }

   private static void debugWireInfo(String var0, int var1, Throwable var2, String var3) {
      if (MessageImpl.debugWire && JMSDebug.JMSDispatcher.isDebugEnabled()) {
         if (var2 != null) {
            JMSDebug.JMSDispatcher.debug(var0 + " versionInt 0x" + Integer.toHexString(var1).toUpperCase() + var3, var2);
         } else {
            JMSDebug.JMSDispatcher.debug(var0 + " versionInt 0x" + Integer.toHexString(var1).toUpperCase() + var3);
         }
      }

   }
}
