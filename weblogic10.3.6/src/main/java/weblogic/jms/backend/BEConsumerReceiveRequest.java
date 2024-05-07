package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSConsumerReceiveResponse;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;
import weblogic.messaging.kernel.KernelRequest;
import weblogic.transaction.internal.TransactionImpl;

public final class BEConsumerReceiveRequest extends Request implements Externalizable {
   static final long serialVersionUID = -4180296985320716407L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   static final int START = 0;
   static final int CONTINUE = 1;
   static final int PAGEIN = 2;
   private long timeout;
   private transient TransactionImpl transaction;
   private transient KernelRequest kernelRequest;

   public BEConsumerReceiveRequest(JMSID var1, long var2) {
      super(var1, 11025);
      this.timeout = var2;
   }

   long getTimeout() {
      return this.timeout;
   }

   void setTransaction(TransactionImpl var1) {
      this.transaction = var1;
   }

   TransactionImpl getTransaction() {
      return this.transaction;
   }

   boolean isTransactional() {
      return this.transaction != null;
   }

   public int remoteSignature() {
      return 35;
   }

   public Response createResponse() {
      return new JMSConsumerReceiveResponse();
   }

   public BEConsumerReceiveRequest() {
   }

   void setKernelRequest(KernelRequest var1) {
      this.kernelRequest = var1;
   }

   KernelRequest getKernelRequest() {
      return this.kernelRequest;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeInt(1);
      super.writeExternal(var1);
      var1.writeLong(this.timeout);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.timeout = var1.readLong();
      }
   }
}
