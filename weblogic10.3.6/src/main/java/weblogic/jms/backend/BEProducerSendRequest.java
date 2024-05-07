package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.CompletionListener;
import weblogic.common.CompletionRequest;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSProducerSendResponse;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;
import weblogic.messaging.kernel.KernelRequest;
import weblogic.messaging.kernel.Sequence;
import weblogic.messaging.path.helper.KeyString;
import weblogic.work.InheritableThreadContext;

public final class BEProducerSendRequest extends Request implements Externalizable, CompletionListener {
   static final long serialVersionUID = -3794283731017311093L;
   private static final int VERSION61 = 1;
   private static final int VERSION81 = 2;
   private static final int VERSION902 = 4;
   private static final int EXTVERSION = 4;
   private static final int VERSION_MASK = 255;
   private static final int MESSAGE_TYPE_MASK = 65280;
   private static final int CONNECTION_ID_MASK = 65536;
   private static final int TIMEOUT_MASK = 131072;
   private static final int PRODUCER_ID_MASK = 262144;
   public static final int CHECK_UNIT_OF_ORDER = 2097152;
   private static final int MESSAGE_TYPE_SHIFT = 8;
   private long sendTimeout;
   static final int SEND_RUNNING_WITHOUT_BLOCKING = 500;
   static final int SEND_BLOCKED_WAITING_FOR_QUOTA = 501;
   static final int SEND_QUOTA_GRANTED = 502;
   static final int SEND_TIMED_OUT_WAITING_FOR_QUOTA = 503;
   static final int SEND_UNIT_OF_ORDER_PATH_SERVICE = 504;
   private MessageImpl message;
   private JMSID connectionId;
   private int checkUnitOfOrder;
   private JMSID producerId = null;
   private transient KeyString uooKey;
   private transient BEUOOMember uooMember;
   private transient CompletionRequest completionRequest;
   private transient KernelRequest kernelSendRequest;
   private transient BEUOOState.State uooState;
   private transient InheritableThreadContext context;
   private transient Sequence sequence;

   public BEProducerSendRequest(JMSID var1, MessageImpl var2, JMSID var3, long var4, JMSID var6) {
      super(var1, 12052);
      this.producerId = var6;
      this.message = var2;
      this.connectionId = var3;
      this.sendTimeout = var4;
   }

   MessageImpl getMessage() {
      return this.message;
   }

   JMSID getConnectionId() {
      return this.connectionId;
   }

   JMSID getProducerId() {
      return this.producerId;
   }

   public int remoteSignature() {
      return 35;
   }

   public Response createResponse() {
      return new JMSProducerSendResponse();
   }

   public void setCheckUOO(int var1) {
      this.checkUnitOfOrder = var1;
   }

   int getCheckUOO() {
      return this.checkUnitOfOrder;
   }

   public void setUOOInfo(KeyString var1, BEUOOMember var2, CompletionRequest var3) {
      this.uooKey = var1;
      this.uooMember = var2;
      this.completionRequest = var3;
   }

   public KeyString getUOOKey() {
      return this.uooKey;
   }

   public BEUOOMember getUOOMember() {
      return this.uooMember;
   }

   public CompletionRequest getCompletionRequest() {
      return this.completionRequest;
   }

   public void onCompletion(CompletionRequest var1, Object var2) {
      this.resumeExecution(false);
   }

   public void onException(CompletionRequest var1, Throwable var2) {
      this.resumeRequest(var2, false);
   }

   Sequence getSequence() {
      return this.sequence;
   }

   void setSequence(Sequence var1) {
      this.sequence = var1;
   }

   public BEProducerSendRequest() {
   }

   private byte getVersion(Object var1) throws IOException {
      if (var1 instanceof PeerInfoable) {
         PeerInfo var2 = ((PeerInfoable)var1).getPeerInfo();
         if (var2.compareTo(PeerInfo.VERSION_61) < 0) {
            throw JMSUtilities.versionIOException(0, 1, 2);
         }

         if (var2.compareTo(PeerInfo.VERSION_81) < 0) {
            return 1;
         }

         if (var2.compareTo(PeerInfo.VERSION_901) < 0) {
            return 2;
         }
      }

      return 4;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      byte var2 = this.getVersion(var1);
      int var3 = var2;
      byte var4 = this.message.getType();
      if (var4 != 0) {
         var3 = var2 | var4 << 8;
      }

      if (this.connectionId != null) {
         var3 |= 65536;
      }

      if (this.producerId != null) {
         var3 |= 262144;
      }

      if (var2 >= 2 && this.sendTimeout != 10L) {
         var3 |= 131072;
      }

      var3 |= this.checkUnitOfOrder;
      var1.writeInt(var3);
      super.writeExternal(var1);
      this.message.writeExternal(var1);
      if (this.connectionId != null) {
         this.connectionId.writeExternal(var1);
      }

      if ((var3 & 131072) != 0) {
         var1.writeLong(this.sendTimeout);
      }

      if ((var3 & 262144) != 0) {
         this.producerId.writeExternal(var1);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 4) {
         throw JMSUtilities.versionIOException(var3, 4, 4);
      } else {
         super.readExternal(var1);
         byte var4 = (byte)((var2 & '\uff00') >> 8);
         this.message = MessageImpl.createMessageImpl(var4);
         this.message.readExternal(var1);
         if ((var2 & 65536) != 0) {
            this.connectionId = new JMSID();
            this.connectionId.readExternal(var1);
         }

         if ((var2 & 131072) != 0) {
            this.sendTimeout = var1.readLong();
         } else {
            this.sendTimeout = 10L;
         }

         this.checkUnitOfOrder = var2 & 2097152;
         if ((var2 & 262144) != 0) {
            this.producerId = new JMSID();
            this.producerId.readExternal(var1);
         }

      }
   }

   long getSendTimeout() {
      return this.sendTimeout;
   }

   void setKernelRequest(KernelRequest var1) {
      this.kernelSendRequest = var1;
   }

   KernelRequest getKernelRequest() {
      return this.kernelSendRequest;
   }

   BEUOOState.State getUooState() {
      return this.uooState;
   }

   void setUooState(BEUOOState.State var1) {
      this.uooState = var1;
   }

   void restoreResources(boolean var1) {
      BEUOOState.State var2 = this.uooState;
      if (var2 != null) {
         synchronized(this) {
            if (!this.hasResults()) {
               return;
            }
         }

         var2.removeReference(this, var1);
      }
   }

   void rememberThreadContext() {
      if (this.context == null) {
         this.context = InheritableThreadContext.getContext();
      }

   }

   public void run() {
      if (this.context != null) {
         this.context.push();
      }

      try {
         super.run();
      } finally {
         if (this.context != null) {
            this.context.pop();
         }

      }

   }

   JMSProducerSendResponse setupSendResponse() {
      JMSProducerSendResponse var1 = new JMSProducerSendResponse(this.message.getId());
      this.setResult(var1);
      return var1;
   }
}
