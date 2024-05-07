package weblogic.management.descriptors.application.weblogic.jdbc;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ParameterMBeanImpl extends XMLElementMBeanDelegate implements ParameterMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_paramName = false;
   private String paramName;
   private boolean isSet_paramValue = false;
   private String paramValue;

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.description;
      this.description = var1;
      this.isSet_description = var1 != null;
      this.checkChange("description", var2, this.description);
   }

   public String getParamName() {
      return this.paramName;
   }

   public void setParamName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.paramName;
      this.paramName = var1;
      this.isSet_paramName = var1 != null;
      this.checkChange("paramName", var2, this.paramName);
   }

   public String getParamValue() {
      return this.paramValue;
   }

   public void setParamValue(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.paramValue;
      this.paramValue = var1;
      this.isSet_paramValue = var1 != null;
      this.checkChange("paramValue", var2, this.paramValue);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<parameter");
      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</parameter>\n");
      return var2.toString();
   }
}
