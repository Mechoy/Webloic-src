package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ListIterator;
import javax.transaction.Transaction;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.dispatcher.Response;
import weblogic.messaging.kernel.KernelRequest;

public final class BESessionAcknowledgeRequest extends Request implements Externalizable {
   static final long serialVersionUID = -496965679629368939L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int ACKNOWLEDGE_POLICY_MASK = 65280;
   private static final int ACKNOWLEDGE_POLICY_SHIFT = 8;
   public static final int ACK_IN_PROGRESS = 11000;
   public static final int ACK_COMPLETED = 11001;
   private int acknowledgePolicy;
   private long lastSequenceNumber;
   private transient KernelRequest kernelRequest;
   private transient Transaction transaction;
   private transient ListIterator iterator;

   public BESessionAcknowledgeRequest(JMSID var1, long var2) {
      super(var1, 13072);
      this.lastSequenceNumber = var2;
   }

   long getLastSequenceNumber() {
      return this.lastSequenceNumber;
   }

   public int remoteSignature() {
      return 35;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   void setIterator(ListIterator var1) {
      this.iterator = var1;
   }

   ListIterator getIterator() {
      return this.iterator;
   }

   void setKernelRequest(KernelRequest var1) {
      this.kernelRequest = var1;
   }

   KernelRequest getKernelRequest() {
      return this.kernelRequest;
   }

   Transaction getTransaction() {
      return this.transaction;
   }

   void setTransaction(Transaction var1) {
      this.transaction = var1;
   }

   boolean isTransactional() {
      return this.transaction != null;
   }

   public BESessionAcknowledgeRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 1 | this.acknowledgePolicy << 8;
      var1.writeInt(var2);
      super.writeExternal(var1);
      var1.writeLong(this.lastSequenceNumber);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.lastSequenceNumber = var1.readLong();
         this.acknowledgePolicy = (var2 & '\uff00') >>> 8;
      }
   }
}
