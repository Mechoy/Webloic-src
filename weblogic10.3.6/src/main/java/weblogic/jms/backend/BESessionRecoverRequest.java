package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSSessionRecoverResponse;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.common.Sequencer;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;

public final class BESessionRecoverRequest extends Request implements Externalizable {
   static final long serialVersionUID = -7100249057361747189L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int ALGORITHM_90 = 1024;
   private long lastSequenceNumber;
   private int pipelineGeneration;
   private transient Sequencer sequencer;

   public BESessionRecoverRequest(JMSID var1, long var2, Sequencer var4, int var5) {
      super(var1, 13840);
      this.lastSequenceNumber = var2;
      this.sequencer = var4;
      this.pipelineGeneration = var5;
   }

   final long getLastSequenceNumber() {
      return this.lastSequenceNumber;
   }

   public final Sequencer getSequencer() {
      return this.sequencer;
   }

   final int getPipelineGeneration() {
      return this.pipelineGeneration;
   }

   public int remoteSignature() {
      return 17;
   }

   public Response createResponse() {
      return new JMSSessionRecoverResponse();
   }

   public BESessionRecoverRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 1 | this.pipelineGeneration;
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
         this.pipelineGeneration = var2 & 15728640;
      }
   }
}
