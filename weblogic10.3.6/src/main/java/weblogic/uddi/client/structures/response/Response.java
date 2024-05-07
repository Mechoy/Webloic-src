package weblogic.uddi.client.structures.response;

public abstract class Response {
   protected String generic = null;
   protected String operator = null;

   public String getOperator() {
      return this.operator;
   }

   public void setOperator(String var1) {
      this.operator = var1;
   }

   public String getGeneric() {
      return this.generic;
   }

   public void setGeneric(String var1) {
      this.generic = var1;
   }
}
