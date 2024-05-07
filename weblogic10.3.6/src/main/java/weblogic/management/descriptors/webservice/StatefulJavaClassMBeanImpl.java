package weblogic.management.descriptors.webservice;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class StatefulJavaClassMBeanImpl extends XMLElementMBeanDelegate implements StatefulJavaClassMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_componentName = false;
   private String componentName;
   private boolean isSet_className = false;
   private String className;

   public String getComponentName() {
      return this.componentName;
   }

   public void setComponentName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.componentName;
      this.componentName = var1;
      this.isSet_componentName = var1 != null;
      this.checkChange("componentName", var2, this.componentName);
   }

   public String getClassName() {
      return this.className;
   }

   public void setClassName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.className;
      this.className = var1;
      this.isSet_className = var1 != null;
      this.checkChange("className", var2, this.className);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<stateful-java-class");
      if (this.isSet_componentName) {
         var2.append(" name=\"").append(String.valueOf(this.getComponentName())).append("\"");
      }

      if (this.isSet_className) {
         var2.append(" class-name=\"").append(String.valueOf(this.getClassName())).append("\"");
      }

      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</stateful-java-class>\n");
      return var2.toString();
   }
}
