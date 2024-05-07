package weblogic.corba.j2ee.naming;

final class EndPointInfo {
   private String host = "";
   private int port = -1;
   private int majorVersion = 1;
   private int minorVersion = 2;

   public EndPointInfo() {
   }

   public EndPointInfo(String var1, int var2, int var3, int var4) {
      this.host = var1;
      this.port = var2;
      this.minorVersion = var4;
      this.majorVersion = var3;
   }

   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   public int getMajorVersion() {
      return this.majorVersion;
   }

   public int getMinorVersion() {
      return this.minorVersion;
   }

   public String getAddress() {
      return this.majorVersion + "." + this.minorVersion + "@" + this.host + (this.port > 0 ? ":" + this.port : "");
   }

   public String toString() {
      return " Host: " + this.host + " Port: " + Integer.toString(this.port) + " Version: " + Integer.toString(this.majorVersion) + "." + Integer.toString(this.minorVersion);
   }
}
