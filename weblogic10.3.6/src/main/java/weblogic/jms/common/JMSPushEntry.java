package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.dispatcher.JMSDispatcher;

public final class JMSPushEntry implements Externalizable {
   static final long serialVersionUID = -632448292622511345L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int NEXT_MASK = 256;
   private static final int REDELIVERED_MASK = 512;
   private static final int SEQUENCER_ID_MASK = 1024;
   private static final int BACK_END_MASK = 2048;
   private static final int FRONT_END_MASK = 4096;
   private static final int CLIENTRESPONSIBLEFORACKNOWLEDGE = 8192;
   private static final int REDELIVERED_COUNT_FLAG = 16384;
   public static final int PIPELINE_GENERATION_MASK = 15728640;
   public static final int NO_PIPELINE_GENERATION = 0;
   public static final int EXPIRED_GENERATION = 1048576;
   public static final int CLIENT_ACK_GENERATION = 2097152;
   public static final int LEAST_PIPELINE_GENERATION = 4194304;
   public static final int FIRST_PIPELINE_GENERATION = 15728640;
   private static final int DEBUG_PIPELINE_GENERATION_SHIFT = 20;
   private transient JMSPushEntry next;
   private transient JMSPushEntry nextUnacked;
   private transient JMSPushEntry prevUnacked;
   private transient JMSDispatcher dispatcher;
   private transient long messageSize;
   private JMSID sequencerId;
   private JMSID consumerId;
   private long backEndSequenceNumber;
   private long frontEndSequenceNumber;
   private int pipelineGeneration;
   private int deliveryCount;
   private boolean clientResponsibleForAcknowledge;

   public JMSPushEntry(JMSID var1, JMSID var2, long var3, long var5, int var7, int var8) {
      this.sequencerId = var1;
      this.consumerId = var2;
      this.backEndSequenceNumber = var3;
      this.frontEndSequenceNumber = var5;
      this.deliveryCount = var7;
      this.pipelineGeneration = var8;
   }

   public final JMSID getSequencerId() {
      return this.sequencerId;
   }

   public final JMSID getConsumerId() {
      return this.consumerId;
   }

   public final long getFrontEndSequenceNumber() {
      return this.frontEndSequenceNumber;
   }

   public final void setFrontEndSequenceNumber(long var1) {
      this.frontEndSequenceNumber = var1;
   }

   public final long getBackEndSequenceNumber() {
      return this.backEndSequenceNumber;
   }

   public final void setBackEndSequenceNumber(long var1) {
      this.backEndSequenceNumber = var1;
   }

   public int getPipelineGeneration() {
      return this.pipelineGeneration;
   }

   public void setPipelineGeneration(int var1) {
      this.pipelineGeneration = var1;
   }

   public static int nextRecoverGeneration(int var0) {
      if (var0 < 4194304) {
         return var0;
      } else {
         var0 += 1048576;
         if (var0 > 15728640) {
            var0 = 4194304;
         }

         return var0;
      }
   }

   public static int displayRecoverGeneration(int var0) {
      return var0 >> 20;
   }

   public final long getMessageSize() {
      return this.messageSize;
   }

   public int getDeliveryCount() {
      return this.deliveryCount;
   }

   public final void setMessageSize(long var1) {
      this.messageSize = var1;
   }

   public final boolean getClientResponsibleForAcknowledge() {
      return this.clientResponsibleForAcknowledge;
   }

   public final void setClientResponsibleForAcknowledge(boolean var1) {
      this.clientResponsibleForAcknowledge = var1;
   }

   public final JMSPushEntry getNext() {
      return this.next;
   }

   public final void setNext(JMSPushEntry var1) {
      this.next = var1;
   }

   public final JMSPushEntry getNextUnacked() {
      return this.nextUnacked;
   }

   public final void setNextUnacked(JMSPushEntry var1) {
      this.nextUnacked = var1;
   }

   public final JMSPushEntry getPrevUnacked() {
      return this.prevUnacked;
   }

   public final void setPrevUnacked(JMSPushEntry var1) {
      this.prevUnacked = var1;
   }

   public final void setDispatcher(JMSDispatcher var1) {
      this.dispatcher = var1;
   }

   public final JMSDispatcher getDispatcher() {
      return this.dispatcher;
   }

   public JMSPushEntry() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      this.writeExternal(var1, (JMSPushRequest)null);
   }

   public void writeExternal(ObjectOutput var1, JMSPushRequest var2) throws IOException {
      ObjectOutput var4;
      if (var1 instanceof MessageImpl.JMSObjectOutputWrapper) {
         var4 = ((MessageImpl.JMSObjectOutputWrapper)var1).getInnerObjectOutput();
      } else {
         var4 = var1;
      }

      boolean var3;
      if (var4 instanceof PeerInfoable) {
         var3 = PeerInfo.VERSION_DIABLO.compareTo(((PeerInfoable)var4).getPeerInfo()) <= 0;
      } else {
         var3 = false;
      }

      assert var3 || this.pipelineGeneration == 0;

      int var5 = 1;
      JMSPushEntry var6 = this;
      JMSID var7 = null;
      if (var2 == null) {
         var5 |= 6144;
      } else if (var2.getMethodId() == 15620) {
         var5 |= 4096;
      } else {
         var7 = (JMSID)var2.getInvocableId();
         var5 |= 2048;
      }

      do {
         var5 &= -15746817;
         var5 |= this.pipelineGeneration;
         if (var6.next != null) {
            var5 |= 256;
         }

         if (var6.deliveryCount > 1) {
            if (var3) {
               var5 |= 16384;
            } else {
               var5 |= 512;
            }
         }

         if (var6.getClientResponsibleForAcknowledge()) {
            var5 |= 8192;
         }

         if ((var5 & 2048) != 0 && var6.sequencerId != var7) {
            var5 |= 1024;
            var7 = this.sequencerId;
         }

         var1.writeInt(var5);
         if (MessageImpl.debugWire && JMSDebug.JMSDispatcher.isDebugEnabled()) {
            this.debugWire("JMSPushEntry.write", var5, ", atLeastDiabloIOStream=" + var3);
         }

         if ((var5 & 1024) != 0) {
            var6.sequencerId.writeExternal(var1);
         }

         if ((var5 & 2048) != 0) {
            var1.writeLong(var6.backEndSequenceNumber);
         }

         if ((var5 & 4096) != 0) {
            var1.writeLong(var6.frontEndSequenceNumber);
         }

         var6.consumerId.writeExternal(var1);
         if ((var5 & 16384) != 0) {
            var1.writeInt(var6.deliveryCount);
         }

         var6 = var6.next;
      } while(var6 != null);

   }

   private void debugWire(String var1, int var2, String var3) {
      JMSDebug.JMSDispatcher.debug(var1 + " versionInt x" + Integer.toHexString(var2).toUpperCase() + var3);
   }

   public void readExternal(ObjectInput var1) throws ClassNotFoundException, IOException {
      this.readExternal(var1, (JMSPushRequest)null);
   }

   JMSPushEntry readExternal(ObjectInput var1, JMSPushRequest var2) throws ClassNotFoundException, IOException {
      JMSPushEntry var6 = this;
      JMSID var7 = (JMSID)var2.getInvocableId();

      JMSPushEntry var5;
      do {
         int var3 = var1.readInt();
         int var4 = var3 & 255;
         if (MessageImpl.debugWire && JMSDebug.JMSDispatcher.isDebugEnabled()) {
            this.debugWire("JMSPushEntry.read ", var3, ", version=" + var4);
         }

         if (var4 != 1) {
            throw JMSUtilities.versionIOException(var4, 1, 1);
         }

         if ((var3 & 1024) == 0) {
            var6.sequencerId = var7;
         } else {
            var6.sequencerId = new JMSID();
            var6.sequencerId.readExternal(var1);
         }

         if ((var3 & 2048) != 0) {
            var6.backEndSequenceNumber = var1.readLong();
         }

         if ((var3 & 4096) != 0) {
            var6.frontEndSequenceNumber = var1.readLong();
         }

         var6.consumerId = new JMSID();
         var6.consumerId.readExternal(var1);
         var6.setClientResponsibleForAcknowledge((var3 & 8192) != 0);
         this.pipelineGeneration = var3 & 15728640;
         if ((var3 & 16384) != 0) {
            var6.deliveryCount = var1.readInt();
         } else if ((var3 & 512) != 0) {
            var6.deliveryCount = 2;
         } else {
            var6.deliveryCount = 1;
         }

         var5 = var6;
         if ((var3 & 256) != 0) {
            var6.next = new JMSPushEntry();
         }
      } while((var6 = var6.next) != null);

      return var5;
   }
}
