package weblogic.xml.util.xed;

public class AttributeReference extends Variable {
   public AttributeReference() {
   }

   public AttributeReference(String var1) {
      super("@" + var1);
   }

   public void setName(String var1) {
      super.setName("@" + var1);
   }

   public Object evaluate(Context var1) throws StreamEditorException {
      return var1.getEventType() != 2 ? "" : super.evaluate(var1);
   }

   public void assign(Object var1, Context var2) throws StreamEditorException {
      if (var2.getEventType() == 2) {
         super.assign(var1, var2);
      }
   }

   public String toString() {
      return "$" + this.getName();
   }

   public boolean isAttributeRef() {
      return "@attributes".equals(this.getName());
   }
}
