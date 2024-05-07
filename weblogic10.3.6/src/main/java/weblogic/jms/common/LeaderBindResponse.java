package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.dispatcher.Response;

public final class LeaderBindResponse extends Response implements Externalizable, Comparable {
   static final long serialVersionUID = -2408521791630650078L;
   private static final byte EXTVERSION1 = 1;
   private static final byte EXTVERSION2 = 2;
   private static final int VERSION_MASK = 255;
   private static final int GO_AHEAD_AND_BIND = 256;
   private static final int HAS_REASON = 512;
   private boolean goAheadAndBind;
   private JMSID leaderJMSID;
   private long leaderSequenceNumber;
   private String reasonForRejection;

   public LeaderBindResponse(boolean var1, JMSID var2, long var3, String var5) {
      this.goAheadAndBind = var1;
      this.leaderJMSID = var2;
      this.leaderSequenceNumber = var3;
      this.reasonForRejection = var5;
   }

   public LeaderBindResponse(boolean var1, JMSID var2, long var3) {
      this(var1, var2, var3, (String)null);
   }

   public JMSID getLeaderJMSID() {
      return this.leaderJMSID;
   }

   public long getLeaderSequenceNumber() {
      return this.leaderSequenceNumber;
   }

   public boolean doBind() {
      return this.goAheadAndBind;
   }

   public String getReasonForRejection() {
      return !this.goAheadAndBind && this.reasonForRejection == null ? "Unknown reason for rejection" : this.reasonForRejection;
   }

   public int compareTo(Object var1) {
      LeaderBindResponse var2 = (LeaderBindResponse)var1;
      int var3 = var2.leaderJMSID.compareTo(this.leaderJMSID);
      if (var3 < 0) {
         return -1;
      } else if (var3 > 0) {
         return 1;
      } else if (var2.leaderSequenceNumber < this.leaderSequenceNumber) {
         return -1;
      } else {
         return var2.leaderSequenceNumber > this.leaderSequenceNumber ? 1 : 0;
      }
   }

   public boolean equals(Object var1) {
      LeaderBindResponse var2 = (LeaderBindResponse)var1;
      if (!var2.leaderJMSID.equals(this.leaderJMSID)) {
         return false;
      } else {
         return var2.leaderSequenceNumber == this.leaderSequenceNumber;
      }
   }

   public String toString() {
      return new String("LeaderBindResponse(" + this.goAheadAndBind + ":" + this.leaderJMSID.toString() + ":" + this.leaderSequenceNumber + ")");
   }

   public int hashCode() {
      return this.leaderJMSID.hashCode() ^ (int)(this.leaderSequenceNumber & -1L);
   }

   public LeaderBindResponse() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      byte var2 = 2;
      if (var1 instanceof PeerInfoable) {
         PeerInfo var3 = ((PeerInfoable)var1).getPeerInfo();
         if (var3.getMajor() < 9) {
            var2 = 1;
         }
      }

      int var4 = var2;
      if (this.goAheadAndBind) {
         var4 = var2 | 256;
      }

      if (this.reasonForRejection != null) {
         var4 |= 512;
      }

      var1.writeInt(var4);
      super.writeExternal(var1);
      this.leaderJMSID.writeExternal(var1);
      var1.writeLong(this.leaderSequenceNumber);
      if (var2 >= 2 && this.reasonForRejection != null) {
         var1.writeUTF(this.reasonForRejection);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 >= 1 && var3 <= 2) {
         this.goAheadAndBind = (var2 & 256) != 0;
         super.readExternal(var1);
         this.leaderJMSID = new JMSID();
         this.leaderJMSID.readExternal(var1);
         this.leaderSequenceNumber = var1.readLong();
         if (var3 >= 2 && (var2 & 512) != 0) {
            this.reasonForRejection = var1.readUTF();
         }

      } else {
         throw JMSUtilities.versionIOException(var3, 1, 2);
      }
   }
}
