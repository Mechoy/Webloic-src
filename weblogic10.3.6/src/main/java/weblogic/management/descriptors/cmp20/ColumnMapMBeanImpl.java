package weblogic.management.descriptors.cmp20;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ColumnMapMBeanImpl extends XMLElementMBeanDelegate implements ColumnMapMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_keyColumn = false;
   private String keyColumn;
   private boolean isSet_foreignKeyColumn = false;
   private String foreignKeyColumn;

   public String getKeyColumn() {
      return this.keyColumn;
   }

   public void setKeyColumn(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.keyColumn;
      this.keyColumn = var1;
      this.isSet_keyColumn = var1 != null;
      this.checkChange("keyColumn", var2, this.keyColumn);
   }

   public String getForeignKeyColumn() {
      return this.foreignKeyColumn;
   }

   public void setForeignKeyColumn(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.foreignKeyColumn;
      this.foreignKeyColumn = var1;
      this.isSet_foreignKeyColumn = var1 != null;
      this.checkChange("foreignKeyColumn", var2, this.foreignKeyColumn);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<column-map");
      var2.append(">\n");
      if (null != this.getForeignKeyColumn()) {
         var2.append(ToXML.indent(var1 + 2)).append("<foreign-key-column>").append(this.getForeignKeyColumn()).append("</foreign-key-column>\n");
      }

      if (null != this.getKeyColumn()) {
         var2.append(ToXML.indent(var1 + 2)).append("<key-column>").append(this.getKeyColumn()).append("</key-column>\n");
      }

      var2.append(ToXML.indent(var1)).append("</column-map>\n");
      return var2.toString();
   }
}
