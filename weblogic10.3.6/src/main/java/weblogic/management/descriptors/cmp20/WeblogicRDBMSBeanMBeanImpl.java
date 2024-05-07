package weblogic.management.descriptors.cmp20;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class WeblogicRDBMSBeanMBeanImpl extends XMLElementMBeanDelegate implements WeblogicRDBMSBeanMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_automaticKeyGeneration = false;
   private AutomaticKeyGenerationMBean automaticKeyGeneration;
   private boolean isSet_dataSourceName = false;
   private String dataSourceName;
   private boolean isSet_instanceLockOrder = false;
   private String instanceLockOrder = "AccessOrder";
   private boolean isSet_relationshipCachings = false;
   private List relationshipCachings;
   private boolean isSet_delayDatabaseInsertUntil = false;
   private String delayDatabaseInsertUntil = "ejbPostCreate";
   private boolean isSet_weblogicQueries = false;
   private List weblogicQueries;
   private boolean isSet_tableMaps = false;
   private List tableMaps;
   private boolean isSet_fieldGroups = false;
   private List fieldGroups;
   private boolean isSet_useSelectForUpdate = false;
   private boolean useSelectForUpdate = false;
   private boolean isSet_checkExistsOnMethod = false;
   private boolean checkExistsOnMethod = true;
   private boolean isSet_tableName = false;
   private String tableName;
   private boolean isSet_ejbName = false;
   private String ejbName;
   private boolean isSet_lockOrder = false;
   private int lockOrder = 0;

   public AutomaticKeyGenerationMBean getAutomaticKeyGeneration() {
      return this.automaticKeyGeneration;
   }

   public void setAutomaticKeyGeneration(AutomaticKeyGenerationMBean var1) {
      AutomaticKeyGenerationMBean var2 = this.automaticKeyGeneration;
      this.automaticKeyGeneration = var1;
      this.isSet_automaticKeyGeneration = var1 != null;
      this.checkChange("automaticKeyGeneration", var2, this.automaticKeyGeneration);
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

   public String getInstanceLockOrder() {
      return this.instanceLockOrder;
   }

   public void setInstanceLockOrder(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.instanceLockOrder;
      this.instanceLockOrder = var1;
      this.isSet_instanceLockOrder = var1 != null;
      this.checkChange("instanceLockOrder", var2, this.instanceLockOrder);
   }

   public RelationshipCachingMBean[] getRelationshipCachings() {
      if (this.relationshipCachings == null) {
         return new RelationshipCachingMBean[0];
      } else {
         RelationshipCachingMBean[] var1 = new RelationshipCachingMBean[this.relationshipCachings.size()];
         var1 = (RelationshipCachingMBean[])((RelationshipCachingMBean[])this.relationshipCachings.toArray(var1));
         return var1;
      }
   }

   public void setRelationshipCachings(RelationshipCachingMBean[] var1) {
      RelationshipCachingMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getRelationshipCachings();
      }

      this.isSet_relationshipCachings = true;
      if (this.relationshipCachings == null) {
         this.relationshipCachings = Collections.synchronizedList(new ArrayList());
      } else {
         this.relationshipCachings.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.relationshipCachings.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("RelationshipCachings", var2, this.getRelationshipCachings());
      }

   }

   public void addRelationshipCaching(RelationshipCachingMBean var1) {
      this.isSet_relationshipCachings = true;
      if (this.relationshipCachings == null) {
         this.relationshipCachings = Collections.synchronizedList(new ArrayList());
      }

      this.relationshipCachings.add(var1);
   }

   public void removeRelationshipCaching(RelationshipCachingMBean var1) {
      if (this.relationshipCachings != null) {
         this.relationshipCachings.remove(var1);
      }
   }

   public String getDelayDatabaseInsertUntil() {
      return this.delayDatabaseInsertUntil;
   }

   public void setDelayDatabaseInsertUntil(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.delayDatabaseInsertUntil;
      this.delayDatabaseInsertUntil = var1;
      this.isSet_delayDatabaseInsertUntil = var1 != null;
      this.checkChange("delayDatabaseInsertUntil", var2, this.delayDatabaseInsertUntil);
   }

   public WeblogicQueryMBean[] getWeblogicQueries() {
      if (this.weblogicQueries == null) {
         return new WeblogicQueryMBean[0];
      } else {
         WeblogicQueryMBean[] var1 = new WeblogicQueryMBean[this.weblogicQueries.size()];
         var1 = (WeblogicQueryMBean[])((WeblogicQueryMBean[])this.weblogicQueries.toArray(var1));
         return var1;
      }
   }

   public void setWeblogicQueries(WeblogicQueryMBean[] var1) {
      WeblogicQueryMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getWeblogicQueries();
      }

      this.isSet_weblogicQueries = true;
      if (this.weblogicQueries == null) {
         this.weblogicQueries = Collections.synchronizedList(new ArrayList());
      } else {
         this.weblogicQueries.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.weblogicQueries.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("WeblogicQueries", var2, this.getWeblogicQueries());
      }

   }

   public void addWeblogicQuery(WeblogicQueryMBean var1) {
      this.isSet_weblogicQueries = true;
      if (this.weblogicQueries == null) {
         this.weblogicQueries = Collections.synchronizedList(new ArrayList());
      }

      this.weblogicQueries.add(var1);
   }

   public void removeWeblogicQuery(WeblogicQueryMBean var1) {
      if (this.weblogicQueries != null) {
         this.weblogicQueries.remove(var1);
      }
   }

   public TableMapMBean[] getTableMaps() {
      if (this.tableMaps == null) {
         return new TableMapMBean[0];
      } else {
         TableMapMBean[] var1 = new TableMapMBean[this.tableMaps.size()];
         var1 = (TableMapMBean[])((TableMapMBean[])this.tableMaps.toArray(var1));
         return var1;
      }
   }

   public void setTableMaps(TableMapMBean[] var1) {
      TableMapMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getTableMaps();
      }

      this.isSet_tableMaps = true;
      if (this.tableMaps == null) {
         this.tableMaps = Collections.synchronizedList(new ArrayList());
      } else {
         this.tableMaps.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.tableMaps.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("TableMaps", var2, this.getTableMaps());
      }

   }

   public void addTableMap(TableMapMBean var1) {
      this.isSet_tableMaps = true;
      if (this.tableMaps == null) {
         this.tableMaps = Collections.synchronizedList(new ArrayList());
      }

      this.tableMaps.add(var1);
   }

   public void removeTableMap(TableMapMBean var1) {
      if (this.tableMaps != null) {
         this.tableMaps.remove(var1);
      }
   }

   public FieldGroupMBean[] getFieldGroups() {
      if (this.fieldGroups == null) {
         return new FieldGroupMBean[0];
      } else {
         FieldGroupMBean[] var1 = new FieldGroupMBean[this.fieldGroups.size()];
         var1 = (FieldGroupMBean[])((FieldGroupMBean[])this.fieldGroups.toArray(var1));
         return var1;
      }
   }

   public void setFieldGroups(FieldGroupMBean[] var1) {
      FieldGroupMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getFieldGroups();
      }

      this.isSet_fieldGroups = true;
      if (this.fieldGroups == null) {
         this.fieldGroups = Collections.synchronizedList(new ArrayList());
      } else {
         this.fieldGroups.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.fieldGroups.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("FieldGroups", var2, this.getFieldGroups());
      }

   }

   public void addFieldGroup(FieldGroupMBean var1) {
      this.isSet_fieldGroups = true;
      if (this.fieldGroups == null) {
         this.fieldGroups = Collections.synchronizedList(new ArrayList());
      }

      this.fieldGroups.add(var1);
   }

   public void removeFieldGroup(FieldGroupMBean var1) {
      if (this.fieldGroups != null) {
         this.fieldGroups.remove(var1);
      }
   }

   public boolean getUseSelectForUpdate() {
      return this.useSelectForUpdate;
   }

   public void setUseSelectForUpdate(boolean var1) {
      boolean var2 = this.useSelectForUpdate;
      this.useSelectForUpdate = var1;
      this.isSet_useSelectForUpdate = true;
      this.checkChange("useSelectForUpdate", var2, this.useSelectForUpdate);
   }

   public boolean getCheckExistsOnMethod() {
      return this.checkExistsOnMethod;
   }

   public void setCheckExistsOnMethod(boolean var1) {
      boolean var2 = this.checkExistsOnMethod;
      this.checkExistsOnMethod = var1;
      this.isSet_checkExistsOnMethod = true;
      this.checkChange("checkExistsOnMethod", var2, this.checkExistsOnMethod);
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

   public int getLockOrder() {
      return this.lockOrder;
   }

   public void setLockOrder(int var1) {
      int var2 = this.lockOrder;
      this.lockOrder = var1;
      this.isSet_lockOrder = var1 != -1;
      this.checkChange("lockOrder", var2, this.lockOrder);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<weblogic-rdbms-bean");
      var2.append(">\n");
      if (null != this.getEJBName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<ejb-name>").append(this.getEJBName()).append("</ejb-name>\n");
      }

      if (null != this.getDataSourceName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<data-source-name>").append(this.getDataSourceName()).append("</data-source-name>\n");
      }

      int var3;
      if (null != this.getTableMaps()) {
         for(var3 = 0; var3 < this.getTableMaps().length; ++var3) {
            var2.append(this.getTableMaps()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getFieldGroups()) {
         for(var3 = 0; var3 < this.getFieldGroups().length; ++var3) {
            var2.append(this.getFieldGroups()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getRelationshipCachings()) {
         for(var3 = 0; var3 < this.getRelationshipCachings().length; ++var3) {
            var2.append(this.getRelationshipCachings()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getWeblogicQueries()) {
         for(var3 = 0; var3 < this.getWeblogicQueries().length; ++var3) {
            var2.append(this.getWeblogicQueries()[var3].toXML(var1 + 2));
         }
      }

      if ((this.isSet_delayDatabaseInsertUntil || !"ejbPostCreate".equals(this.getDelayDatabaseInsertUntil())) && null != this.getDelayDatabaseInsertUntil()) {
         var2.append(ToXML.indent(var1 + 2)).append("<delay-database-insert-until>").append(this.getDelayDatabaseInsertUntil()).append("</delay-database-insert-until>\n");
      }

      if (this.isSet_useSelectForUpdate || this.getUseSelectForUpdate()) {
         var2.append(ToXML.indent(var1 + 2)).append("<use-select-for-update>").append(ToXML.capitalize(Boolean.valueOf(this.getUseSelectForUpdate()).toString())).append("</use-select-for-update>\n");
      }

      if (this.isSet_lockOrder || 0 != this.getLockOrder()) {
         var2.append(ToXML.indent(var1 + 2)).append("<lock-order>").append(this.getLockOrder()).append("</lock-order>\n");
      }

      if ((this.isSet_instanceLockOrder || !"AccessOrder".equals(this.getInstanceLockOrder())) && null != this.getInstanceLockOrder()) {
         var2.append(ToXML.indent(var1 + 2)).append("<instance-lock-order>").append(this.getInstanceLockOrder()).append("</instance-lock-order>\n");
      }

      if (null != this.getAutomaticKeyGeneration()) {
         var2.append(this.getAutomaticKeyGeneration().toXML(var1 + 2)).append("\n");
      }

      if (this.isSet_checkExistsOnMethod || !this.getCheckExistsOnMethod()) {
         var2.append(ToXML.indent(var1 + 2)).append("<check-exists-on-method>").append(ToXML.capitalize(Boolean.valueOf(this.getCheckExistsOnMethod()).toString())).append("</check-exists-on-method>\n");
      }

      var2.append(ToXML.indent(var1)).append("</weblogic-rdbms-bean>\n");
      return var2.toString();
   }
}
