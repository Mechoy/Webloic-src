package weblogic.uddi.client.structures.datatypes;

public abstract class TextNode {
   protected String value;

   public TextNode() {
      this.value = null;
   }

   public TextNode(String var1) {
      this.value = new String(var1);
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String var1) {
      this.value = var1;
   }
}
