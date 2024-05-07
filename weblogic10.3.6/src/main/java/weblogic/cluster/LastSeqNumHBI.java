package weblogic.cluster;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public final class LastSeqNumHBI implements Externalizable {
   private static final long serialVersionUID = -2753700702225040722L;
   int senderNum;
   long lastSeqNum;
   boolean useHTTPForSD;

   LastSeqNumHBI(int var1, long var2, boolean var4) {
      this.senderNum = var1;
      this.lastSeqNum = var2;
      this.useHTTPForSD = var4;
   }

   public String toString() {
      return "lastSeqNumHBI senderNum:" + this.senderNum + " seqNum:" + this.lastSeqNum;
   }

   public boolean equals(Object var1) {
      try {
         LastSeqNumHBI var2 = (LastSeqNumHBI)var1;
         return var2.senderNum == this.senderNum;
      } catch (ClassCastException var3) {
         return false;
      }
   }

   public int hashCode() {
      return this.senderNum;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeInt(this.senderNum);
      var1.writeLong(this.lastSeqNum);
      var1.writeBoolean(this.useHTTPForSD);
   }

   public void readExternal(ObjectInput var1) throws IOException {
      this.senderNum = var1.readInt();
      this.lastSeqNum = var1.readLong();
      this.useHTTPForSD = var1.readBoolean();
   }

   public LastSeqNumHBI() {
   }
}
