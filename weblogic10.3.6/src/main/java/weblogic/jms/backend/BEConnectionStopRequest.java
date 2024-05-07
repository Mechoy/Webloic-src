package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.dispatcher.Response;

public final class BEConnectionStopRequest extends Request implements Externalizable {
   static final long serialVersionUID = -908887813148387129L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int STOP_FOR_SUSPEND = 256;
   private long startStopSequenceNumber;
   private boolean stopForSuspend;

   public BEConnectionStopRequest(JMSID var1, long var2, boolean var4) {
      super(var1, 9743);
      this.startStopSequenceNumber = var2;
      this.stopForSuspend = var4;
   }

   public final long getStartStopSequenceNumber() {
      return this.startStopSequenceNumber;
   }

   public final boolean isStopForSuspend() {
      return this.stopForSuspend;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public BEConnectionStopRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 1;
      if (this.stopForSuspend) {
         var2 |= 256;
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      var1.writeLong(this.startStopSequenceNumber);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.startStopSequenceNumber = var1.readLong();
         if ((var2 & 256) != 0) {
            this.stopForSuspend = true;
         }

      }
   }
}
