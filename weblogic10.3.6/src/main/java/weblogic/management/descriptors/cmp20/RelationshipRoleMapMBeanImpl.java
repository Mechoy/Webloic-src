package weblogic.management.descriptors.cmp20;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class RelationshipRoleMapMBeanImpl extends XMLElementMBeanDelegate implements RelationshipRoleMapMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_foreignKeyTable = false;
   private String foreignKeyTable;
   private boolean isSet_primaryKeyTable = false;
   private String primaryKeyTable;
   private boolean isSet_columnMaps = false;
   private List columnMaps;

   public String getForeignKeyTable() {
      return this.foreignKeyTable;
   }

   public void setForeignKeyTable(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.foreignKeyTable;
      this.foreignKeyTable = var1;
      this.isSet_foreignKeyTable = var1 != null;
      this.checkChange("foreignKeyTable", var2, this.foreignKeyTable);
   }

   public String getPrimaryKeyTable() {
      return this.primaryKeyTable;
   }

   public void setPrimaryKeyTable(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.primaryKeyTable;
      this.primaryKeyTable = var1;
      this.isSet_primaryKeyTable = var1 != null;
      this.checkChange("primaryKeyTable", var2, this.primaryKeyTable);
   }

   public ColumnMapMBean[] getColumnMaps() {
      if (this.columnMaps == null) {
         return new ColumnMapMBean[0];
      } else {
         ColumnMapMBean[] var1 = new ColumnMapMBean[this.columnMaps.size()];
         var1 = (ColumnMapMBean[])((ColumnMapMBean[])this.columnMaps.toArray(var1));
         return var1;
      }
   }

   public void setColumnMaps(ColumnMapMBean[] var1) {
      ColumnMapMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getColumnMaps();
      }

      this.isSet_columnMaps = true;
      if (this.columnMaps == null) {
         this.columnMaps = Collections.synchronizedList(new ArrayList());
      } else {
         this.columnMaps.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.columnMaps.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("ColumnMaps", var2, this.getColumnMaps());
      }

   }

   public void addColumnMap(ColumnMapMBean var1) {
      this.isSet_columnMaps = true;
      if (this.columnMaps == null) {
         this.columnMaps = Collections.synchronizedList(new ArrayList());
      }

      this.columnMaps.add(var1);
   }

   public void removeColumnMap(ColumnMapMBean var1) {
      if (this.columnMaps != null) {
         this.columnMaps.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<relationship-role-map");
      var2.append(">\n");
      if (null != this.getForeignKeyTable()) {
         var2.append(ToXML.indent(var1 + 2)).append("<foreign-key-table>").append(this.getForeignKeyTable()).append("</foreign-key-table>\n");
      }

      if (null != this.getPrimaryKeyTable()) {
         var2.append(ToXML.indent(var1 + 2)).append("<primary-key-table>").append(this.getPrimaryKeyTable()).append("</primary-key-table>\n");
      }

      if (null != this.getColumnMaps()) {
         for(int var3 = 0; var3 < this.getColumnMaps().length; ++var3) {
            var2.append(this.getColumnMaps()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</relationship-role-map>\n");
      return var2.toString();
   }
}
