package weblogic.management.descriptors.ejb20;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class CMRFieldMBeanImpl extends XMLElementMBeanDelegate implements CMRFieldMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_cmrFieldType = false;
   private String cmrFieldType;
   private boolean isSet_cmrFieldName = false;
   private String cmrFieldName;

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

   public String getCMRFieldType() {
      return this.cmrFieldType;
   }

   public void setCMRFieldType(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.cmrFieldType;
      this.cmrFieldType = var1;
      this.isSet_cmrFieldType = var1 != null;
      this.checkChange("cmrFieldType", var2, this.cmrFieldType);
   }

   public String getCMRFieldName() {
      return this.cmrFieldName;
   }

   public void setCMRFieldName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.cmrFieldName;
      this.cmrFieldName = var1;
      this.isSet_cmrFieldName = var1 != null;
      this.checkChange("cmrFieldName", var2, this.cmrFieldName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<cmr-field");
      var2.append(">\n");
      if (null != this.getDescription()) {
         var2.append(ToXML.indent(var1 + 2)).append("<description>").append("<![CDATA[" + this.getDescription() + "]]>").append("</description>\n");
      }

      if (null != this.getCMRFieldName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<cmr-field-name>").append(this.getCMRFieldName()).append("</cmr-field-name>\n");
      }

      if (null != this.getCMRFieldType()) {
         var2.append(ToXML.indent(var1 + 2)).append("<cmr-field-type>").append(this.getCMRFieldType()).append("</cmr-field-type>\n");
      }

      var2.append(ToXML.indent(var1)).append("</cmr-field>\n");
      return var2.toString();
   }
}
