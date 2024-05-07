package weblogic.management.descriptors.ejb11;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class CMPFieldMBeanImpl extends XMLElementMBeanDelegate implements CMPFieldMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_fieldName = false;
   private String fieldName;

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

   public String getFieldName() {
      return this.fieldName;
   }

   public void setFieldName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.fieldName;
      this.fieldName = var1;
      this.isSet_fieldName = var1 != null;
      this.checkChange("fieldName", var2, this.fieldName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<cmp-field");
      var2.append(">\n");
      if (null != this.getDescription()) {
         var2.append(ToXML.indent(var1 + 2)).append("<description>").append("<![CDATA[" + this.getDescription() + "]]>").append("</description>\n");
      }

      if (null != this.getFieldName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<field-name>").append(this.getFieldName()).append("</field-name>\n");
      }

      var2.append(ToXML.indent(var1)).append("</cmp-field>\n");
      return var2.toString();
   }
}
