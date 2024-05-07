package weblogic.management.descriptors.cmp11;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class FieldMapMBeanImpl extends XMLElementMBeanDelegate implements FieldMapMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_cmpField = false;
   private String cmpField;
   private boolean isSet_dbmsColumn = false;
   private String dbmsColumn;

   public String getCMPField() {
      return this.cmpField;
   }

   public void setCMPField(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.cmpField;
      this.cmpField = var1;
      this.isSet_cmpField = var1 != null;
      this.checkChange("cmpField", var2, this.cmpField);
   }

   public String getDBMSColumn() {
      return this.dbmsColumn;
   }

   public void setDBMSColumn(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.dbmsColumn;
      this.dbmsColumn = var1;
      this.isSet_dbmsColumn = var1 != null;
      this.checkChange("dbmsColumn", var2, this.dbmsColumn);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<field-map");
      var2.append(">\n");
      if (null != this.getCMPField()) {
         var2.append(ToXML.indent(var1 + 2)).append("<cmp-field>").append(this.getCMPField()).append("</cmp-field>\n");
      }

      if (null != this.getDBMSColumn()) {
         var2.append(ToXML.indent(var1 + 2)).append("<dbms-column>").append(this.getDBMSColumn()).append("</dbms-column>\n");
      }

      var2.append(ToXML.indent(var1)).append("</field-map>\n");
      return var2.toString();
   }
}
