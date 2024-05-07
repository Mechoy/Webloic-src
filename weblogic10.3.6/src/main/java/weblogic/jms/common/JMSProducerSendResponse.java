package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import javax.jms.Message;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.jms.dispatcher.Response;
import weblogic.jms.extensions.AsyncSendResponseInfo;
import weblogic.jms.frontend.FEProducerSendRequest;

public final class JMSProducerSendResponse extends Response implements Externalizable, ProducerSendResponse, AsyncSendResponseInfo {
   static final long serialVersionUID = -2438555459624785305L;
   private static final int VERSION61 = 1;
   private static final int EXTVERSION = 2;
   private static final int VERSION_MASK = 255;
   private static final int DELIVERY_MODE_MASK = 256;
   private static final int PRIORITY_MASK = 512;
   private static final int TIME_TO_LIVE_MASK = 1024;
   private static final int TIME_TO_DELIVER_MASK = 2048;
   private static final int FLOW_CONTROL_MASK = 4096;
   private static final int REDELIVERY_LIMIT_MASK = 8192;
   private static final int UOO90_MEMBER_MASK = 16384;
   private static final int DIABLOSTYLEMESSAGEID_MASK = 32768;
   private int mask;
   private JMSMessageId messageId;
   private int deliveryMode;
   private int priority;
   private long timeToLive;
   private long timeToDeliver;
   private int redeliveryLimit;
   private long flowControlTime;
   private FEProducerSendRequest request;
   private Serializable uooMember;
   private transient Message message;
   private transient long asyncFlowControlTime;

   public JMSProducerSendResponse(JMSMessageId var1) {
      this.messageId = var1;
   }

   public void setMessageId(JMSMessageId var1) {
      this.messageId = var1;
   }

   public JMSMessageId getMessageId() {
      return this.messageId;
   }

   public void setDeliveryMode(int var1) {
      this.mask |= 256;
      this.deliveryMode = var1;
   }

   public int getDeliveryMode() {
      return (this.mask & 256) == 0 ? -1 : this.deliveryMode;
   }

   public void setPriority(int var1) {
      this.mask |= 512;
      this.priority = var1;
   }

   public int getPriority() {
      return (this.mask & 512) == 0 ? -1 : this.priority;
   }

   public void setTimeToLive(long var1) {
      this.mask |= 1024;
      this.timeToLive = var1;
   }

   public long getTimeToLive() {
      return (this.mask & 1024) == 0 ? -1L : this.timeToLive;
   }

   public void setTimeToDeliver(long var1) {
      this.mask |= 2048;
      this.timeToDeliver = var1;
   }

   public long getTimeToDeliver() {
      return (this.mask & 2048) == 0 ? -1L : this.timeToDeliver;
   }

   public void setRedeliveryLimit(int var1) {
      this.mask |= 8192;
      this.redeliveryLimit = var1;
   }

   public void set90StyleMessageId() {
      this.mask |= 32768;
   }

   public boolean get90StyleMessageId() {
      return (this.mask & '耀') != 0;
   }

   public int getRedeliveryLimit() {
      return (this.mask & 8192) == 0 ? 0 : this.redeliveryLimit;
   }

   public void setNeedsFlowControl(boolean var1) {
      if (var1) {
         this.mask |= 4096;
      } else {
         this.mask &= -4097;
      }

   }

   public boolean getNeedsFlowControl() {
      return (this.mask & 4096) != 0;
   }

   public void setFlowControlTime(long var1) {
      this.mask |= 4096;
      this.flowControlTime = var1;
   }

   public long getFlowControlTime() {
      return (this.mask & 4096) == 0 ? -1L : this.flowControlTime;
   }

   public Message getMessage() {
      return this.message;
   }

   public void setMessage(Message var1) {
      this.message = var1;
   }

   public long getAsyncFlowControlTime() {
      return this.asyncFlowControlTime;
   }

   public void setAsyncFlowControlTime(long var1) {
      this.asyncFlowControlTime = var1;
   }

   public void setRequest(FEProducerSendRequest var1) {
      this.request = var1;
   }

   public boolean isDispatchOneWay() {
      return this.request == null ? false : this.request.isNoResponse();
   }

   public void setUOOInfo(Serializable var1) {
      this.uooMember = var1;
      if (var1 == null) {
         this.mask &= -16385;
      } else {
         this.mask |= 16384;
      }

   }

   public Serializable getUOOInfo() {
      return this.uooMember;
   }

   private int getVersion(ObjectOutput var1) throws IOException {
      if (var1 instanceof PeerInfoable) {
         PeerInfo var2 = ((PeerInfoable)var1).getPeerInfo();
         if (var2.compareTo(PeerInfo.VERSION_61) < 0) {
            throw new IOException(JMSClientExceptionLogger.logInvalidResponseLoggable(1, 2, var2.toString()).getMessage());
         }

         if (var2.compareTo(PeerInfo.VERSION_70) < 0) {
            return 1;
         }
      }

      return 2;
   }

   public JMSProducerSendResponse() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      int var2 = this.getVersion(var1);
      super.writeExternal(var1);
      this.mask = this.mask & -256 | var2;
      var1.writeInt(this.mask);
      this.messageId.writeExternal(var1);
      if ((this.mask & 256) != 0) {
         var1.writeInt(this.deliveryMode);
      }

      if ((this.mask & 512) != 0) {
         var1.writeInt(this.priority);
      }

      if ((this.mask & 1024) != 0) {
         var1.writeLong(this.timeToLive);
      }

      if ((this.mask & 2048) != 0) {
         var1.writeLong(this.timeToDeliver);
      }

      if (var2 == 2) {
         if ((this.mask & 8192) != 0) {
            var1.writeInt(this.redeliveryLimit);
         }

         if ((this.mask & 4096) != 0) {
            var1.writeLong(this.flowControlTime);
         }
      }

      if ((this.mask & 16384) != 0) {
         var1.writeObject(this.uooMember);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.mask = var1.readInt();
      int var2 = this.mask & 255;
      if (var2 >= 1 && var2 <= 2) {
         this.messageId = new JMSMessageId();
         this.messageId.readExternal(var1);
         if ((this.mask & 256) != 0) {
            this.deliveryMode = var1.readInt();
         }

         if ((this.mask & 512) != 0) {
            this.priority = var1.readInt();
         }

         if ((this.mask & 1024) != 0) {
            this.timeToLive = var1.readLong();
         }

         if ((this.mask & 2048) != 0) {
            this.timeToDeliver = var1.readLong();
         }

         if ((this.mask & 8192) != 0) {
            this.redeliveryLimit = var1.readInt();
         }

         if ((this.mask & 4096) != 0) {
            this.flowControlTime = var1.readLong();
         }

         if ((this.mask & 16384) != 0) {
            this.uooMember = (Serializable)((Serializable)var1.readObject());
         }

      } else {
         throw JMSUtilities.versionIOException(var2, 1, 2);
      }
   }
}
