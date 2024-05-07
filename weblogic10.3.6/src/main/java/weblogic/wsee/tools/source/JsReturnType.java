package weblogic.wsee.tools.source;

import javax.xml.namespace.QName;
import weblogic.wsee.util.HashCodeBuilder;
import weblogic.wsee.util.ObjectUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public class JsReturnType extends JsTypeBase {
   private static final boolean verbose = Verbose.isVerbose(JsReturnType.class);
   private QName element;
   private boolean docStyle = true;

   JsReturnType(String var1) {
      this.setType(var1);
   }

   public void setElement(QName var1) {
      this.element = var1;
   }

   public QName getElement() {
      return this.element;
   }

   public void setDocStyle(boolean var1) {
      this.docStyle = var1;
   }

   public boolean isDocStyle() {
      return this.docStyle;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("type", this.getType());
      var1.end();
   }

   public int hashCode() {
      HashCodeBuilder var1 = new HashCodeBuilder();
      var1.add(super.hashCode());
      var1.add(this.element);
      var1.add(this.docStyle);
      return var1.hashCode();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof JsReturnType)) {
         return false;
      } else {
         JsReturnType var2 = (JsReturnType)var1;
         boolean var3 = super.equals(var1);
         var3 = var3 && ObjectUtil.equals(this.element, var2.element);
         var3 = var3 && this.docStyle == var2.docStyle;
         return var3;
      }
   }
}
