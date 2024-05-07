package weblogic.cluster;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.rmi.spi.HostID;
import weblogic.utils.StackTraceUtils;

public final class NAKHBI implements Externalizable {
   private static final long serialVersionUID = 1807772120988360177L;
   HostID memID;
   int senderNum;
   long seqNum;
   int fragNum;
   String serverVersion;

   NAKHBI(HostID var1, int var2, long var3, int var5) {
      this.memID = var1;
      this.senderNum = var2;
      this.seqNum = var3;
      this.fragNum = var5;
   }

   public String toString() {
      return "NAKHBI server:" + this.memID + " senderNum:" + this.senderNum + " seqNum:" + this.seqNum + " fragNum:" + this.fragNum;
   }

   public boolean equals(Object var1) {
      try {
         NAKHBI var2 = (NAKHBI)var1;
         return this.memID.equals(var2.memID) && this.senderNum == var2.senderNum;
      } catch (ClassCastException var3) {
         return false;
      }
   }

   public int hashCode() {
      return this.senderNum;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      WLObjectOutput var2 = (WLObjectOutput)var1;
      var2.writeObjectWL(this.memID);
      var2.writeInt(this.senderNum);
      var2.writeLong(this.seqNum);
      var2.writeInt(this.fragNum);
      var2.writeString(UpgradeUtils.getInstance().getLocalServerVersion());
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      WLObjectInput var2 = (WLObjectInput)var1;
      this.memID = (HostID)var2.readObjectWL();
      this.senderNum = var2.readInt();
      this.seqNum = var2.readLong();
      this.fragNum = var2.readInt();

      try {
         this.serverVersion = var2.readString();
      } catch (IOException var4) {
         if (ClusterDebugLogger.isDebugEnabled()) {
            ClusterDebugLogger.debug("[UPGRADE] serverVerion not available in NAKBHI!" + StackTraceUtils.throwable2StackTrace(var4));
         }
      }

   }

   public NAKHBI() {
   }
}
