package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.messaging.dispatcher.DispatcherId;

public final class ConsumerReconnectInfo implements Externalizable, Cloneable {
   static final long serialVersionUID = -2345606540693435552L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int _HAS_CLIENT_JMSID = 256;
   private static final int _HAS_SERVER_ID = 512;
   private static final int _HAS_CLIENT_DISPATCHER = 1024;
   private static final int _HAS_SERVER_DISPATCHER = 2048;
   private static final int _HAS_LAST_EXPOSED_MSG_ID = 4096;
   private static final int _HAS_DELAY_SERVER_CLOSE = 8192;
   private static final int _HAS_INVOKABLE_ID = 16384;
   private static final int _HAS_LAST_ACK_MSG_ID = 32768;
   private JMSID clientJMSID;
   private JMSID serverDestId;
   private JMSID invokableID;
   private DispatcherId clientDispatcherId;
   private DispatcherId serverDispatcherId;
   private long delayServerClose;
   private JMSMessageId lastExposedMsgId;
   private JMSMessageId lastAckMsgId;

   public DispatcherId getClientDispatcherId() {
      return this.clientDispatcherId;
   }

   public void setClientDispatcherId(DispatcherId var1) {
      this.clientDispatcherId = var1;
   }

   public JMSID getClientJMSID() {
      return this.clientJMSID;
   }

   public void setClientJMSID(JMSID var1) {
      this.clientJMSID = var1;
   }

   public long getDelayServerClose() {
      return this.delayServerClose;
   }

   public void setDelayServerClose(long var1) {
      this.delayServerClose = var1;
   }

   public JMSID getInvokableID() {
      return this.invokableID;
   }

   public void setInvokableID(JMSID var1) {
      this.invokableID = var1;
   }

   public JMSMessageId getLastAckMsgId() {
      return this.lastAckMsgId;
   }

   public void setLastAckMsgId(JMSMessageId var1) {
      this.lastAckMsgId = var1;
   }

   public JMSMessageId getLastExposedMsgId() {
      return this.lastExposedMsgId;
   }

   public void setLastExposedMsgId(JMSMessageId var1) {
      this.lastExposedMsgId = var1;
   }

   public JMSID getServerDestId() {
      return this.serverDestId;
   }

   public void setServerDestId(JMSID var1) {
      this.serverDestId = var1;
   }

   public DispatcherId getServerDispatcherId() {
      return this.serverDispatcherId;
   }

   public void setServerDispatcherId(DispatcherId var1) {
      this.serverDispatcherId = var1;
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public ConsumerReconnectInfo getClone() {
      try {
         return (ConsumerReconnectInfo)this.clone();
      } catch (CloneNotSupportedException var2) {
         throw new AssertionError(var2);
      }
   }

   public String toString() {
      return "(ConsumerReconnectInfo <clientDispatcherId " + this.clientDispatcherId + "> <clientJMSID " + this.clientJMSID + "> <lastExposedMsgId " + this.lastExposedMsgId + "> <lastAckMsgId " + this.lastAckMsgId + "> <serverDestId " + this.serverDestId + "> <serverDispatcherId " + this.serverDispatcherId + "> <delayServerClose " + this.delayServerClose + ">)";
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 1;
      if (this.clientJMSID != null) {
         var2 |= 256;
      }

      if (this.serverDestId != null) {
         var2 |= 512;
      }

      if (this.clientDispatcherId != null) {
         var2 |= 1024;
      }

      if (this.serverDispatcherId != null) {
         var2 |= 2048;
      }

      if (this.lastExposedMsgId != null) {
         var2 |= 4096;
      }

      if (this.lastAckMsgId != null) {
         var2 |= 32768;
      }

      if (this.delayServerClose != 0L) {
         var2 |= 8192;
      }

      if (this.invokableID != null) {
         var2 |= 16384;
      }

      var1.writeInt(var2);
      if (this.delayServerClose != 0L) {
         var1.writeLong(this.delayServerClose);
      }

      if (this.lastAckMsgId != null) {
         this.lastAckMsgId.writeExternal(var1);
      }

      if (this.lastExposedMsgId != null) {
         this.lastExposedMsgId.writeExternal(var1);
      }

      if (this.clientJMSID != null) {
         this.clientJMSID.writeExternal(var1);
      }

      if (this.serverDestId != null) {
         this.serverDestId.writeExternal(var1);
      }

      if (this.clientDispatcherId != null) {
         this.clientDispatcherId.writeExternal(var1);
      }

      if (this.serverDispatcherId != null) {
         this.serverDispatcherId.writeExternal(var1);
      }

      if (this.invokableID != null) {
         this.invokableID.writeExternal(var1);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         if ((var2 & 8192) != 0) {
            this.delayServerClose = var1.readLong();
         }

         if ((var2 & '耀') != 0) {
            this.lastAckMsgId = new JMSMessageId();
            this.lastAckMsgId.readExternal(var1);
         }

         if ((var2 & 4096) != 0) {
            this.lastExposedMsgId = new JMSMessageId();
            this.lastExposedMsgId.readExternal(var1);
         }

         if ((var2 & 256) != 0) {
            this.clientJMSID = new JMSID();
            this.clientJMSID.readExternal(var1);
         }

         if ((var2 & 512) != 0) {
            this.serverDestId = new JMSID();
            this.serverDestId.readExternal(var1);
         }

         if ((var2 & 1024) != 0) {
            this.clientDispatcherId = new DispatcherId();
            this.clientDispatcherId.readExternal(var1);
         }

         if ((var2 & 2048) != 0) {
            this.serverDispatcherId = new DispatcherId();
            this.serverDispatcherId.readExternal(var1);
         }

         if ((var2 & 16384) != 0) {
            this.invokableID = new JMSID();
            this.invokableID.readExternal(var1);
         }

      }
   }
}
