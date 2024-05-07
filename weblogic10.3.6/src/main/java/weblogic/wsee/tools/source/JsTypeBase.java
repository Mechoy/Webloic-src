package weblogic.wsee.tools.source;

import weblogic.wsee.util.HashCodeBuilder;
import weblogic.wsee.util.ObjectUtil;
import weblogic.xml.schema.binding.internal.NameUtil;

public abstract class JsTypeBase {
   private String type;
   private String partName;

   public String getType() {
      return this.type;
   }

   public void setType(String var1) {
      this.type = var1;
   }

   public String getPartName() {
      return this.partName;
   }

   public void setPartName(String var1) {
      this.partName = var1;
   }

   public static String getJavaName(String var0) {
      String var1 = NameUtil.getJavaName(var0);
      boolean var2 = Character.isLowerCase(var0.charAt(0));
      return var2 ? NameUtil.lowercaseFirstLetter(var1) : var1;
   }

   public int hashCode() {
      HashCodeBuilder var1 = new HashCodeBuilder();
      var1.add(this.type);
      var1.add(this.partName);
      return var1.hashCode();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof JsTypeBase)) {
         return false;
      } else {
         JsTypeBase var2 = (JsTypeBase)var1;
         boolean var3 = ObjectUtil.equals(this.type, var2.type);
         var3 = var3 && ObjectUtil.equals(this.partName, var2.partName);
         return var3;
      }
   }
}
