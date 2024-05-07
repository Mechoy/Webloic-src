package weblogic.management.descriptors.cmp20;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class TableMapMBeanImpl extends XMLElementMBeanDelegate implements TableMapMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_optimisticColumn = false;
   private String optimisticColumn;
   private boolean isSet_verifyColumns = false;
   private String verifyColumns;
   private boolean isSet_fieldMaps = false;
   private List fieldMaps;
   private boolean isSet_tableName = false;
   private String tableName;
   private boolean isSet_verifyRows = false;
   private String verifyRows;

   public String getOptimisticColumn() {
      return this.optimisticColumn;
   }

   public void setOptimisticColumn(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.optimisticColumn;
      this.optimisticColumn = var1;
      this.isSet_optimisticColumn = var1 != null;
      this.checkChange("optimisticColumn", var2, this.optimisticColumn);
   }

   public String getVerifyColumns() {
      return this.verifyColumns;
   }

   public void setVerifyColumns(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.verifyColumns;
      this.verifyColumns = var1;
      this.isSet_verifyColumns = var1 != null;
      this.checkChange("verifyColumns", var2, this.verifyColumns);
   }

   public FieldMapMBean[] getFieldMaps() {
      if (this.fieldMaps == null) {
         return new FieldMapMBean[0];
      } else {
         FieldMapMBean[] var1 = new FieldMapMBean[this.fieldMaps.size()];
         var1 = (FieldMapMBean[])((FieldMapMBean[])this.fieldMaps.toArray(var1));
         return var1;
      }
   }

   public void setFieldMaps(FieldMapMBean[] var1) {
      FieldMapMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getFieldMaps();
      }

      this.isSet_fieldMaps = true;
      if (this.fieldMaps == null) {
         this.fieldMaps = Collections.synchronizedList(new ArrayList());
      } else {
         this.fieldMaps.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.fieldMaps.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("FieldMaps", var2, this.getFieldMaps());
      }

   }

   public void addFieldMap(FieldMapMBean var1) {
      this.isSet_fieldMaps = true;
      if (this.fieldMaps == null) {
         this.fieldMaps = Collections.synchronizedList(new ArrayList());
      }

      this.fieldMaps.add(var1);
   }

   public void removeFieldMap(FieldMapMBean var1) {
      if (this.fieldMaps != null) {
         this.fieldMaps.remove(var1);
      }
   }

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

   public String getVerifyRows() {
      return this.verifyRows;
   }

   public void setVerifyRows(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.verifyRows;
      this.verifyRows = var1;
      this.isSet_verifyRows = var1 != null;
      this.checkChange("verifyRows", var2, this.verifyRows);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<table-map");
      var2.append(">\n");
      if (null != this.getTableName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<table-name>").append(this.getTableName()).append("</table-name>\n");
      }

      if (null != this.getFieldMaps()) {
         for(int var3 = 0; var3 < this.getFieldMaps().length; ++var3) {
            var2.append(this.getFieldMaps()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getVerifyRows()) {
         var2.append(ToXML.indent(var1 + 2)).append("<verify-rows>").append(this.getVerifyRows()).append("</verify-rows>\n");
      }

      if (null != this.getVerifyColumns()) {
         var2.append(ToXML.indent(var1 + 2)).append("<verify-columns>").append(this.getVerifyColumns()).append("</verify-columns>\n");
      }

      if (null != this.getOptimisticColumn()) {
         var2.append(ToXML.indent(var1 + 2)).append("<optimistic-column>").append(this.getOptimisticColumn()).append("</optimistic-column>\n");
      }

      var2.append(ToXML.indent(var1)).append("</table-map>\n");
      return var2.toString();
   }
}
