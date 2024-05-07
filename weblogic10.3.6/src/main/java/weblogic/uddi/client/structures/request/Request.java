package weblogic.uddi.client.structures.request;

public abstract class Request {
   protected String generic = "2.0";
   protected String xmlns = "urn:uddi-org:api_v2";

   public void setGeneric(String var1) {
      this.generic = var1;
   }

   public String getGeneric() {
      return this.generic;
   }

   public void setXMLns(String var1) {
      this.xmlns = var1;
   }

   public String getXMLns() {
      return this.xmlns;
   }
}
