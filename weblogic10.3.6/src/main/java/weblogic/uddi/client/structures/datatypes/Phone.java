package weblogic.uddi.client.structures.datatypes;

public class Phone extends TextNode {
   private String useType;

   public Phone() {
      this.useType = null;
   }

   public Phone(String var1) {
      super(var1);
      this.useType = null;
   }

   public Phone(String var1, String var2) {
      super(var2);
      this.useType = var1;
   }

   public String getUseType() {
      return this.useType;
   }

   public void setUseType(String var1) {
      this.useType = new String(var1);
   }
}
