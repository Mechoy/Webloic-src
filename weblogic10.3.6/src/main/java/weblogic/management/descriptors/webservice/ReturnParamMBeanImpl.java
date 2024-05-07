package weblogic.management.descriptors.webservice;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;
import weblogic.xml.stream.XMLName;

public class ReturnParamMBeanImpl extends XMLElementMBeanDelegate implements ReturnParamMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_paramName = false;
   private String paramName;
   private boolean isSet_paramType = false;
   private XMLName paramType;
   private boolean isSet_className = false;
   private String className;
   private boolean isSet_location = false;
   private String location;

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

   public XMLName getParamType() {
      return this.paramType;
   }

   public void setParamType(XMLName var1) {
      XMLName var2 = this.paramType;
      this.paramType = var1;
      this.isSet_paramType = var1 != null;
      this.checkChange("paramType", var2, this.paramType);
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

   public String getLocation() {
      return this.location;
   }

   public void setLocation(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.location;
      this.location = var1;
      this.isSet_location = var1 != null;
      this.checkChange("location", var2, this.location);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<return-param");
      if (this.isSet_paramType) {
         var2.append(" xmlns:").append(this.getParamType().getPrefix()).append("=\"").append(this.getParamType().getNamespaceUri()).append("\" type=\"").append(this.getParamType().getQualifiedName()).append("\"");
      }

      if (this.isSet_location) {
         var2.append(" location=\"").append(String.valueOf(this.getLocation())).append("\"");
      }

      if (this.isSet_paramName) {
         var2.append(" name=\"").append(String.valueOf(this.getParamName())).append("\"");
      }

      if (this.isSet_className) {
         var2.append(" class-name=\"").append(String.valueOf(this.getClassName())).append("\"");
      }

      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</return-param>\n");
      return var2.toString();
   }
}
