package weblogic.ejb.container.cmp.rdbms;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class RDBMSDependent {
   private String m_name = null;
   private String m_dataSourceName = null;
   private String m_schemaName = null;
   private String m_tableName = null;
   private List m_fieldMap = new LinkedList();
   private List m_primaryKeyField = new LinkedList();
   private List m_fieldGroup = new LinkedList();

   public String toString() {
      return "[RDBMSDependent name:" + this.m_name + " dataSource:" + this.m_dataSourceName + " schema:" + this.m_schemaName + " table:" + this.m_tableName + " fieldMap:" + this.m_fieldMap + " pkField:" + this.m_primaryKeyField + " fieldGroup:" + this.m_fieldGroup + "]";
   }

   public void setName(String var1) {
      this.m_name = var1;
   }

   public String getName() {
      return this.m_name;
   }

   public void setDataSourceName(String var1) {
      this.m_dataSourceName = var1;
   }

   public String getDataSourceName() {
      return this.m_dataSourceName;
   }

   public void setSchemaName(String var1) {
      this.m_schemaName = var1;
   }

   public String schemaName() {
      return this.m_schemaName;
   }

   public void setTableName(String var1) {
      this.m_tableName = var1;
   }

   public String getTableName() {
      return this.m_tableName;
   }

   public void addFieldMap(ObjectLink var1) {
      this.m_fieldMap.add(var1);
   }

   public Iterator getFieldMaps() {
      return this.m_fieldMap.iterator();
   }

   public void addPrimaryKeyField(String var1) {
      this.m_primaryKeyField.add(var1);
   }

   public Iterator getPrimaryKeyFields() {
      return this.m_primaryKeyField.iterator();
   }

   public void addFieldGroup(FieldGroup var1) {
      this.m_fieldGroup.add(var1);
   }

   public Iterator getFieldGroups() {
      return this.m_fieldGroup.iterator();
   }

   public static class FieldGroupField {
      private boolean m_isCMP;
      private String m_name;

      public FieldGroupField(boolean var1, String var2) {
         this.m_isCMP = var1;
         this.m_name = var2;
      }

      public boolean isCMP() {
         return this.m_isCMP;
      }

      public String getName() {
         return this.m_name;
      }

      public String toString() {
         return "[FieldGroupField " + (this.m_isCMP ? "CMP" : "CMR") + ":" + this.m_name + "]";
      }
   }

   public static class FieldGroup {
      private String m_name;
      private List m_fields = new LinkedList();

      public void setName(String var1) {
         this.m_name = var1;
      }

      public String getName() {
         return this.m_name;
      }

      public void addCMPField(String var1) {
         this.m_fields.add(new FieldGroupField(true, var1));
      }

      public Iterator getFields() {
         return this.m_fields.iterator();
      }

      public void addCMRField(String var1) {
         this.m_fields.add(new FieldGroupField(false, var1));
      }

      public String toString() {
         return "[FieldGroup name:" + this.m_name + " fields:" + this.m_fields + "]";
      }
   }
}
