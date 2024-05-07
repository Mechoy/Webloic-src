package weblogic.wsee.bind.buildtime.internal;

import weblogic.wsee.util.jspgen.JspGenBase;

public abstract class HolderTypeBase extends JspGenBase {
   protected String packageName;
   protected String className;
   protected String valueType;

   public void setPackageName(String var1) {
      this.packageName = var1;
   }

   public void setClassName(String var1) {
      this.className = var1;
   }

   public void setValueType(String var1) {
      this.valueType = var1;
   }
}
