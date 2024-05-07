package weblogic.uddi.client.structures.datatypes;

public class AccessPoint extends TextNode {
   private String URL_Type;

   public AccessPoint() {
      this.URL_Type = null;
   }

   public AccessPoint(String var1, String var2) {
      super(var1);
      this.URL_Type = var2;
   }

   public void setURLType(String var1) {
      this.URL_Type = var1;
   }

   public String getURLType() {
      return this.URL_Type;
   }
}
