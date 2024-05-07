package weblogic.cluster.messaging.internal;

import java.net.InetAddress;

public final class ServerConfigurationInformationImpl implements ServerConfigurationInformation {
   private static final long serialVersionUID = -7511929101139272497L;
   private static final boolean DEBUG;
   private final InetAddress address;
   private final int port;
   private final String serverName;
   private final long creationTime;
   private final boolean usingSSL;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public ServerConfigurationInformationImpl(InetAddress var1, int var2, String var3, long var4) {
      this(var1, var2, var3, var4, false);
   }

   public ServerConfigurationInformationImpl(InetAddress var1, int var2, String var3, long var4, boolean var6) {
      this.address = var1;
      this.port = var2;
      this.serverName = var3;
      this.creationTime = var4;
      this.usingSSL = var6;
   }

   public InetAddress getAddress() {
      return this.address;
   }

   public int getPort() {
      return this.port;
   }

   public String getServerName() {
      return this.serverName;
   }

   public long getCreationTime() {
      return this.creationTime;
   }

   public boolean isUsingSSL() {
      return this.usingSSL;
   }

   public int compareTo(Object var1) {
      if (!$assertionsDisabled && !(var1 instanceof ServerConfigurationInformationImpl)) {
         throw new AssertionError();
      } else {
         ServerConfigurationInformationImpl var2 = (ServerConfigurationInformationImpl)var1;
         int var3 = (int)(this.creationTime - var2.creationTime);
         return var3 != 0 ? var3 : this.serverName.compareTo(var2.serverName);
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof ServerConfigurationInformationImpl)) {
         return false;
      } else {
         ServerConfigurationInformationImpl var2 = (ServerConfigurationInformationImpl)var1;
         return this.serverName.equals(var2.serverName);
      }
   }

   public int hashCode() {
      return (int)((long)this.serverName.hashCode() ^ this.creationTime);
   }

   public String toString() {
      return this.address + ":" + this.port + (this.usingSSL ? "(SSL):" : ":") + this.serverName + ":" + this.creationTime;
   }

   static {
      $assertionsDisabled = !ServerConfigurationInformationImpl.class.desiredAssertionStatus();
      DEBUG = Environment.DEBUG;
   }
}
