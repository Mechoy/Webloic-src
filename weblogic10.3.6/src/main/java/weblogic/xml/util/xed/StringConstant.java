package weblogic.xml.util.xed;

public class StringConstant extends Variable {
   String value;

   public StringConstant(String var1) {
      this.value = var1;
   }

   public Object evaluate(Context var1) throws StreamEditorException {
      return this.value;
   }

   public String toString() {
      return "'" + this.value + "'";
   }
}
