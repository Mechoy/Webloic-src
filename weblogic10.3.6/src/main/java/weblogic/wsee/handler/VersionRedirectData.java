package weblogic.wsee.handler;

public class VersionRedirectData extends DirectInvokeData {
   private String version;
   private String operation;
   private Object[] args;

   public String getVerion() {
      return this.version;
   }

   public Object[] getArgs() {
      return this.args;
   }

   public void setVersion(String var1) {
      this.version = var1;
   }

   public void setArgs(Object[] var1) {
      this.args = var1;
   }

   public String getOperation() {
      return this.operation;
   }

   public void setOperation(String var1) {
      this.operation = var1;
   }
}
