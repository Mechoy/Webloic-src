package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.dispatcher.Response;

public final class BESessionCloseRequest extends Request implements Externalizable {
   static final long serialVersionUID = 2830831915495832371L;
   private static final int EXTVERSION61 = 1;
   private static final int EXTVERSION92 = 2;
   private static final int VERSION_MASK = 255;
   private static final int HAS_ALLOW_DELAY_CLOSE = 256;
   private static final int EXTVERSION = 2;
   private long lastSequenceNumber;
   private boolean allowDelayClose;

   public BESessionCloseRequest(boolean var1, JMSID var2, long var3) {
      super(var2, 13328);
      this.lastSequenceNumber = var3;
      this.allowDelayClose = var1;
   }

   public final long getLastSequenceNumber() {
      return this.lastSequenceNumber;
   }

   boolean allowDelayClose() {
      return this.allowDelayClose;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public BESessionCloseRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2;
      if (var1 instanceof PeerInfoable && ((PeerInfoable)var1).getPeerInfo().compareTo(PeerInfo.VERSION_920) < 0) {
         var2 = 1;
      } else {
         var2 = 2;
         if (this.allowDelayClose) {
            var2 |= 256;
         }
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      var1.writeLong(this.lastSequenceNumber);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 >= 1 && 2 >= var3) {
         super.readExternal(var1);
         this.lastSequenceNumber = var1.readLong();
         this.allowDelayClose = (256 & var2) != 0;
      } else {
         throw JMSUtilities.versionIOException(var3, 1, 2);
      }
   }
}
