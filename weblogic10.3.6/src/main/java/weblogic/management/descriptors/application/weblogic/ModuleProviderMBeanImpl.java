package weblogic.management.descriptors.application.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ModuleProviderMBeanImpl extends XMLElementMBeanDelegate implements ModuleProviderMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_moduleFactoryClassName = false;
   private String moduleFactoryClassName;

   public String getModuleFactoryClassName() {
      return this.moduleFactoryClassName;
   }

   public void setModuleFactoryClassName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.moduleFactoryClassName;
      this.moduleFactoryClassName = var1;
      this.isSet_moduleFactoryClassName = var1 != null;
      this.checkChange("moduleFactoryClassName", var2, this.moduleFactoryClassName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<module-provider");
      var2.append(">\n");
      if (null != this.getName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<name>").append(this.getName()).append("</name>\n");
      }

      if (null != this.getModuleFactoryClassName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<module-factory-class-name>").append(this.getModuleFactoryClassName()).append("</module-factory-class-name>\n");
      }

      var2.append(ToXML.indent(var1)).append("</module-provider>\n");
      return var2.toString();
   }
}
