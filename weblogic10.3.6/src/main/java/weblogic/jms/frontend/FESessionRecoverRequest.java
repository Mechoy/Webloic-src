package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSSessionRecoverResponse;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;

public final class FESessionRecoverRequest extends Request implements Externalizable {
   static final long serialVersionUID = 3677451811287600209L;
   private int pipelineGeneration;
   private long lastSequenceNumber;
   private boolean doRollback;
   private transient Request childRequests;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int DO_ROLLBACK_MASK = 256;
   private static final int SEQUENCE_NUMBER_MASK = 512;
   private static final int ALGORITHM_90_MASK = 1024;
   static final int ROLLBACK_START = 0;
   static final int RECOVER_START = 1;
   static final int RECOVER_FINISH = 2;
   static final int ROLLBACK_FINISH = 3;

   public FESessionRecoverRequest(JMSID var1, long var2, boolean var4, int var5) {
      super(var1, 6920);
      this.lastSequenceNumber = var2;
      this.doRollback = var4;
      this.pipelineGeneration = var5;
   }

   final long getLastSequenceNumber() {
      return this.lastSequenceNumber;
   }

   final void setLastSequenceNumber(long var1) {
      this.lastSequenceNumber = var1;
   }

   final boolean doRollback() {
      return this.doRollback;
   }

   final int getPipelineGeneration() {
      return this.pipelineGeneration;
   }

   final Request getChildRequests() {
      return this.childRequests;
   }

   final void setChildRequests(Request var1) {
      this.childRequests = var1;
   }

   public int remoteSignature() {
      return this.doRollback ? 18 : 19;
   }

   public Response createResponse() {
      return new JMSSessionRecoverResponse();
   }

   public FESessionRecoverRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      int var2 = 1 | this.pipelineGeneration;
      if (this.doRollback) {
         var2 |= 256;
      }

      if (this.lastSequenceNumber != 0L) {
         var2 |= 512;
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      if (this.lastSequenceNumber != 0L) {
         var1.writeLong(this.lastSequenceNumber);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         if ((var2 & 512) != 0) {
            this.lastSequenceNumber = var1.readLong();
         }

         this.doRollback = (var2 & 256) != 0;
         this.pipelineGeneration = var2 & 15728640;
      }
   }
}
