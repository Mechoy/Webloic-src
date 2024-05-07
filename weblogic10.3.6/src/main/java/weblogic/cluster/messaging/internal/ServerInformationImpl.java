package weblogic.cluster.messaging.internal;

import weblogic.cluster.ClusterMemberInfo;

public class ServerInformationImpl implements ServerInformation {
   static final long serialVersionUID = 5448440092079264186L;
   private final long joinTime;
   private final String serverName;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public ServerInformationImpl(ClusterMemberInfo var1) {
      this.joinTime = var1.joinTime();
      this.serverName = var1.serverName();
   }

   public ServerInformationImpl(String var1) {
      this.joinTime = 0L;
      this.serverName = var1;
   }

   public String getServerName() {
      return this.serverName;
   }

   public long getStartupTime() {
      return this.joinTime;
   }

   public String toString() {
      return "[" + this.serverName + "," + this.joinTime + "]";
   }

   public int compareTo(Object var1) {
      if (!$assertionsDisabled && !(var1 instanceof ServerInformation)) {
         throw new AssertionError();
      } else {
         ServerInformation var2 = (ServerInformation)var1;
         int var3 = (int)(this.joinTime - var2.getStartupTime());
         return var3 != 0 ? var3 : this.serverName.compareTo(var2.getServerName());
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof ServerInformation)) {
         return false;
      } else {
         ServerInformation var2 = (ServerInformation)var1;
         return this.serverName.equals(var2.getServerName()) && this.joinTime == var2.getStartupTime();
      }
   }

   public int hashCode() {
      return (int)((long)this.serverName.hashCode() ^ this.joinTime);
   }

   static {
      $assertionsDisabled = !ServerInformationImpl.class.desiredAssertionStatus();
   }
}
