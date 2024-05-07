package weblogic.wsee.tools.source;

import javax.xml.namespace.QName;
import weblogic.wsee.util.HashCodeBuilder;
import weblogic.wsee.util.Verbose;

public class JsClass extends JsBinding {
   private QName bindingName;
   private static final boolean verbose = Verbose.isVerbose(JsClass.class);

   public void setBindingName(QName var1) {
      this.bindingName = var1;
   }

   public QName getBindingName() {
      return this.bindingName;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof JsClass)) {
         return false;
      } else {
         JsClass var2 = (JsClass)var1;
         boolean var3 = super.equals(var1);
         var3 = var3 && this.bindingName.equals(var2.bindingName);
         return var3;
      }
   }

   public int hashCode() {
      HashCodeBuilder var1 = new HashCodeBuilder();
      var1.add(super.hashCode());
      var1.add(this.bindingName);
      return var1.hashCode();
   }
}
