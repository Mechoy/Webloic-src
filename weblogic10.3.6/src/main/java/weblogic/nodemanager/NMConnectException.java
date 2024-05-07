package weblogic.nodemanager;

public class NMConnectException extends NMException {
   private String host;
   private int port;

   public NMConnectException(String var1) {
      this(var1, "", 0);
   }

   public NMConnectException(String var1, String var2, int var3) {
      super(var1);
      this.host = var2;
      this.port = var3;
   }

   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   public void setHost(String var1) {
      this.host = var1;
   }

   public void setPort(int var1) {
      this.port = var1;
   }
}
