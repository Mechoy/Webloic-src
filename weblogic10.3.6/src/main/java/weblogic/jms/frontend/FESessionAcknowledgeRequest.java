package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.dispatcher.Response;

public final class FESessionAcknowledgeRequest extends Request implements Externalizable {
   static final long serialVersionUID = -569536026964306235L;
   private long lastSequenceNumber;
   private int acknowledgePolicy;
   private boolean doCommit;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int ACKNOWLEDGE_POLICY_MASK = 65280;
   private static final int COMMIT_MASK = 65536;
   private static final int SEQUENCE_NUMBER_MASK = 131072;
   private static final int ACKNOWLEDGE_POLICY_SHIFT = 8;
   static final int COMMIT_START = 0;
   static final int ACK_START = 1;
   static final int ACK_FINISH = 2;
   static final int COMMIT_FINISH = 3;

   public FESessionAcknowledgeRequest(JMSID var1, long var2, int var4, boolean var5) {
      super(var1, 6152);
      this.lastSequenceNumber = var2;
      this.acknowledgePolicy = var4;
      this.doCommit = var5;
   }

   long getLastSequenceNumber() {
      return this.lastSequenceNumber;
   }

   boolean doCommit() {
      return this.doCommit;
   }

   public int remoteSignature() {
      return this.doCommit ? 18 : 19;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public FESessionAcknowledgeRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      int var2 = 1 | this.acknowledgePolicy << 8;
      if (this.doCommit) {
         var2 |= 65536;
      }

      if (this.lastSequenceNumber != 0L) {
         var2 |= 131072;
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
         if ((var2 & 131072) != 0) {
            this.lastSequenceNumber = var1.readLong();
         }

         this.acknowledgePolicy = (var2 & '\uff00') >>> 8;
         this.doCommit = (var2 & 65536) != 0;
      }
   }
}
