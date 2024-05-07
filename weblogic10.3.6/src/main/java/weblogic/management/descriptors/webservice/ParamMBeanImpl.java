package weblogic.management.descriptors.webservice;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;
import weblogic.xml.stream.XMLName;

public class ParamMBeanImpl extends XMLElementMBeanDelegate implements ParamMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_implicit = false;
   private boolean implicit;
   private boolean isSet_contentType = false;
   private String contentType;
   private boolean isSet_paramName = false;
   private String paramName;
   private boolean isSet_paramType = false;
   private XMLName paramType;
   private boolean isSet_paramStyle = false;
   private String paramStyle;
   private boolean isSet_className = false;
   private String className;
   private boolean isSet_location = false;
   private String location;

   public boolean isImplicit() {
      return this.implicit;
   }

   public void setImplicit(boolean var1) {
      boolean var2 = this.implicit;
      this.implicit = var1;
      this.isSet_implicit = true;
      this.checkChange("implicit", var2, this.implicit);
   }

   public String getContentType() {
      return this.contentType;
   }

   public void setContentType(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.contentType;
      this.contentType = var1;
      this.isSet_contentType = var1 != null;
      this.checkChange("contentType", var2, this.contentType);
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

   public XMLName getParamType() {
      return this.paramType;
   }

   public void setParamType(XMLName var1) {
      XMLName var2 = this.paramType;
      this.paramType = var1;
      this.isSet_paramType = var1 != null;
      this.checkChange("paramType", var2, this.paramType);
   }

   public String getParamStyle() {
      return this.paramStyle;
   }

   public void setParamStyle(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.paramStyle;
      this.paramStyle = var1;
      this.isSet_paramStyle = var1 != null;
      this.checkChange("paramStyle", var2, this.paramStyle);
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
      var2.append(ToXML.indent(var1)).append("<param");
      if (this.isSet_implicit) {
         var2.append(" implicit=\"").append(String.valueOf(this.isImplicit())).append("\"");
      }

      if (this.isSet_paramStyle) {
         var2.append(" style=\"").append(String.valueOf(this.getParamStyle())).append("\"");
      }

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

      if (this.isSet_contentType) {
         var2.append(" content-type=\"").append(String.valueOf(this.getContentType())).append("\"");
      }

      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</param>\n");
      return var2.toString();
   }
}
