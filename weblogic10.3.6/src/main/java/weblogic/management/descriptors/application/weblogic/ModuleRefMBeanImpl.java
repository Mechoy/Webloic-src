package weblogic.management.descriptors.application.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ModuleRefMBeanImpl extends XMLElementMBeanDelegate implements ModuleRefMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_moduleUri = false;
   private String moduleUri;

   public String getModuleUri() {
      return this.moduleUri;
   }

   public void setModuleUri(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.moduleUri;
      this.moduleUri = var1;
      this.isSet_moduleUri = var1 != null;
      this.checkChange("moduleUri", var2, this.moduleUri);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<module-ref");
      var2.append(">\n");
      if (null != this.getModuleUri()) {
         var2.append(ToXML.indent(var1 + 2)).append("<module-uri>").append(this.getModuleUri()).append("</module-uri>\n");
      }

      var2.append(ToXML.indent(var1)).append("</module-ref>\n");
      return var2.toString();
   }
}
