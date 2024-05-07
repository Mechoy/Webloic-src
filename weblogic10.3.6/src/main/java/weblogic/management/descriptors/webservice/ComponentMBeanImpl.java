package weblogic.management.descriptors.webservice;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ComponentMBeanImpl extends XMLElementMBeanDelegate implements ComponentMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_componentName = false;
   private String componentName;

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

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<component");
      if (this.isSet_componentName) {
         var2.append(" name=\"").append(String.valueOf(this.getComponentName())).append("\"");
      }

      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</component>\n");
      return var2.toString();
   }
}
