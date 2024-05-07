package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.dispatcher.Response;

public final class LeaderBindFailedRequest extends Request implements Externalizable {
   static final long serialVersionUID = 1241072754999225509L;
   private static final byte EXTVERSION1 = 1;
   private static final byte EXTVERSION2 = 2;
   private static final int VERSION_MASK = 255;
   private String jndiName;
   private JMSID leaderID;
   private long sequenceNumber = -1L;

   public LeaderBindFailedRequest(String var1, JMSID var2, long var3) {
      super((JMSID)null, 16661);
      this.jndiName = var1;
      this.leaderID = var2;
      this.sequenceNumber = var3;
   }

   public int remoteSignature() {
      return 64;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public final String getJNDIName() {
      return this.jndiName;
   }

   public final long getSequenceNumber() {
      return this.sequenceNumber;
   }

   public final JMSID getLeaderID() {
      return this.leaderID;
   }

   public String toString() {
      return new String("LeaderBindFailedRequest(" + this.jndiName + ")");
   }

   public LeaderBindFailedRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      byte var2 = 2;
      if (var1 instanceof PeerInfoable) {
         PeerInfo var3 = ((PeerInfoable)var1).getPeerInfo();
         if (var3.getMajor() < 9) {
            var2 = 1;
         }
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      var1.writeUTF(this.jndiName);
      if (var2 >= 2) {
         this.leaderID.writeExternal(var1);
         var1.writeLong(this.sequenceNumber);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 >= 1 && var3 <= 2) {
         super.readExternal(var1);
         this.jndiName = var1.readUTF();
         if (var3 >= 2) {
            this.leaderID = new JMSID();
            this.leaderID.readExternal(var1);
            this.sequenceNumber = var1.readLong();
         }

      } else {
         throw JMSUtilities.versionIOException(var3, 1, 2);
      }
   }
}
