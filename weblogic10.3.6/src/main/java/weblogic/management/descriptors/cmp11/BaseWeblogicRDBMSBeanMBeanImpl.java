package weblogic.management.descriptors.cmp11;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class BaseWeblogicRDBMSBeanMBeanImpl extends XMLElementMBeanDelegate implements BaseWeblogicRDBMSBeanMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_tableName = false;
   private String tableName;
   private boolean isSet_ejbName = false;
   private String ejbName;
   private boolean isSet_dataSourceName = false;
   private String dataSourceName;

   public String getTableName() {
      return this.tableName;
   }

   public void setTableName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.tableName;
      this.tableName = var1;
      this.isSet_tableName = var1 != null;
      this.checkChange("tableName", var2, this.tableName);
   }

   public String getEJBName() {
      return this.ejbName;
   }

   public void setEJBName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.ejbName;
      this.ejbName = var1;
      this.isSet_ejbName = var1 != null;
      this.checkChange("ejbName", var2, this.ejbName);
   }

   public String getDataSourceName() {
      return this.dataSourceName;
   }

   public void setDataSourceName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.dataSourceName;
      this.dataSourceName = var1;
      this.isSet_dataSourceName = var1 != null;
      this.checkChange("dataSourceName", var2, this.dataSourceName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<base-weblogic-rdbms-bean");
      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</base-weblogic-rdbms-bean>\n");
      return var2.toString();
   }
}
