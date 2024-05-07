package weblogic.xml.util.xed;

public class Variable extends Command {
   private String name;

   public Variable() {
   }

   public Variable(String var1) {
      this.name = var1;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public Object evaluate(Context var1) throws StreamEditorException {
      return var1.lookup(this);
   }

   public void assign(Object var1, Context var2) throws StreamEditorException {
      var2.assign(this, var1);
   }

   public String toString() {
      return "$" + this.getName();
   }

   public boolean isAttributeRef() {
      return false;
   }
}
