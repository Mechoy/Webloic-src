package weblogic.management.descriptors.cmp20;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class WeblogicRDBMSJarMBeanImpl extends XMLElementMBeanDelegate implements WeblogicRDBMSJarMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_validateDbSchemaWith = false;
   private String validateDbSchemaWith = "TableQuery";
   private boolean isSet_encoding = false;
   private String encoding;
   private boolean isSet_weblogicRDBMSRelations = false;
   private List weblogicRDBMSRelations;
   private boolean isSet_weblogicRDBMSBeans = false;
   private List weblogicRDBMSBeans;
   private boolean isSet_enableBatchOperations = false;
   private boolean enableBatchOperations = true;
   private boolean isSet_orderDatabaseOperations = false;
   private boolean orderDatabaseOperations = true;
   private boolean isSet_createDefaultDBMSTables = false;
   private String createDefaultDBMSTables = "Disabled";
   private boolean isSet_compatibilityBean = false;
   private CompatibilityMBean compatibilityBean;
   private boolean isSet_version = false;
   private String version;
   private boolean isSet_databaseType = false;
   private String databaseType;
   private boolean isSet_defaultDbmsTablesDdl = false;
   private String defaultDbmsTablesDdl;

   public String getValidateDbSchemaWith() {
      return this.validateDbSchemaWith;
   }

   public void setValidateDbSchemaWith(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.validateDbSchemaWith;
      this.validateDbSchemaWith = var1;
      this.isSet_validateDbSchemaWith = var1 != null;
      this.checkChange("validateDbSchemaWith", var2, this.validateDbSchemaWith);
   }

   public String getEncoding() {
      return this.encoding;
   }

   public void setEncoding(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.encoding;
      this.encoding = var1;
      this.isSet_encoding = var1 != null;
      this.checkChange("encoding", var2, this.encoding);
   }

   public WeblogicRDBMSRelationMBean[] getWeblogicRDBMSRelations() {
      if (this.weblogicRDBMSRelations == null) {
         return new WeblogicRDBMSRelationMBean[0];
      } else {
         WeblogicRDBMSRelationMBean[] var1 = new WeblogicRDBMSRelationMBean[this.weblogicRDBMSRelations.size()];
         var1 = (WeblogicRDBMSRelationMBean[])((WeblogicRDBMSRelationMBean[])this.weblogicRDBMSRelations.toArray(var1));
         return var1;
      }
   }

   public void setWeblogicRDBMSRelations(WeblogicRDBMSRelationMBean[] var1) {
      WeblogicRDBMSRelationMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getWeblogicRDBMSRelations();
      }

      this.isSet_weblogicRDBMSRelations = true;
      if (this.weblogicRDBMSRelations == null) {
         this.weblogicRDBMSRelations = Collections.synchronizedList(new ArrayList());
      } else {
         this.weblogicRDBMSRelations.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.weblogicRDBMSRelations.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("WeblogicRDBMSRelations", var2, this.getWeblogicRDBMSRelations());
      }

   }

   public void addWeblogicRDBMSRelation(WeblogicRDBMSRelationMBean var1) {
      this.isSet_weblogicRDBMSRelations = true;
      if (this.weblogicRDBMSRelations == null) {
         this.weblogicRDBMSRelations = Collections.synchronizedList(new ArrayList());
      }

      this.weblogicRDBMSRelations.add(var1);
   }

   public void removeWeblogicRDBMSRelation(WeblogicRDBMSRelationMBean var1) {
      if (this.weblogicRDBMSRelations != null) {
         this.weblogicRDBMSRelations.remove(var1);
      }
   }

   public WeblogicRDBMSBeanMBean[] getWeblogicRDBMSBeans() {
      if (this.weblogicRDBMSBeans == null) {
         return new WeblogicRDBMSBeanMBean[0];
      } else {
         WeblogicRDBMSBeanMBean[] var1 = new WeblogicRDBMSBeanMBean[this.weblogicRDBMSBeans.size()];
         var1 = (WeblogicRDBMSBeanMBean[])((WeblogicRDBMSBeanMBean[])this.weblogicRDBMSBeans.toArray(var1));
         return var1;
      }
   }

   public void setWeblogicRDBMSBeans(WeblogicRDBMSBeanMBean[] var1) {
      WeblogicRDBMSBeanMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getWeblogicRDBMSBeans();
      }

      this.isSet_weblogicRDBMSBeans = true;
      if (this.weblogicRDBMSBeans == null) {
         this.weblogicRDBMSBeans = Collections.synchronizedList(new ArrayList());
      } else {
         this.weblogicRDBMSBeans.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.weblogicRDBMSBeans.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("WeblogicRDBMSBeans", var2, this.getWeblogicRDBMSBeans());
      }

   }

   public void addWeblogicRDBMSBean(WeblogicRDBMSBeanMBean var1) {
      this.isSet_weblogicRDBMSBeans = true;
      if (this.weblogicRDBMSBeans == null) {
         this.weblogicRDBMSBeans = Collections.synchronizedList(new ArrayList());
      }

      this.weblogicRDBMSBeans.add(var1);
   }

   public void removeWeblogicRDBMSBean(WeblogicRDBMSBeanMBean var1) {
      if (this.weblogicRDBMSBeans != null) {
         this.weblogicRDBMSBeans.remove(var1);
      }
   }

   public boolean getEnableBatchOperations() {
      return this.enableBatchOperations;
   }

   public void setEnableBatchOperations(boolean var1) {
      boolean var2 = this.enableBatchOperations;
      this.enableBatchOperations = var1;
      this.isSet_enableBatchOperations = true;
      this.checkChange("enableBatchOperations", var2, this.enableBatchOperations);
   }

   public boolean getOrderDatabaseOperations() {
      return this.orderDatabaseOperations;
   }

   public void setOrderDatabaseOperations(boolean var1) {
      boolean var2 = this.orderDatabaseOperations;
      this.orderDatabaseOperations = var1;
      this.isSet_orderDatabaseOperations = true;
      this.checkChange("orderDatabaseOperations", var2, this.orderDatabaseOperations);
   }

   public String getCreateDefaultDBMSTables() {
      return this.createDefaultDBMSTables;
   }

   public void setCreateDefaultDBMSTables(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.createDefaultDBMSTables;
      this.createDefaultDBMSTables = var1;
      this.isSet_createDefaultDBMSTables = var1 != null;
      this.checkChange("createDefaultDBMSTables", var2, this.createDefaultDBMSTables);
   }

   public CompatibilityMBean getCompatibilityBean() {
      return this.compatibilityBean;
   }

   public void setCompatibilityBean(CompatibilityMBean var1) {
      CompatibilityMBean var2 = this.compatibilityBean;
      this.compatibilityBean = var1;
      this.isSet_compatibilityBean = var1 != null;
      this.checkChange("compatibilityBean", var2, this.compatibilityBean);
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.version;
      this.version = var1;
      this.isSet_version = var1 != null;
      this.checkChange("version", var2, this.version);
   }

   public String getDatabaseType() {
      return this.databaseType;
   }

   public void setDatabaseType(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.databaseType;
      this.databaseType = var1;
      this.isSet_databaseType = var1 != null;
      this.checkChange("databaseType", var2, this.databaseType);
   }

   public String getDefaultDbmsTablesDdl() {
      return this.defaultDbmsTablesDdl;
   }

   public void setDefaultDbmsTablesDdl(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.defaultDbmsTablesDdl;
      this.defaultDbmsTablesDdl = var1;
      this.isSet_defaultDbmsTablesDdl = var1 != null;
      this.checkChange("defaultDbmsTablesDdl", var2, this.defaultDbmsTablesDdl);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<weblogic-rdbms-jar");
      var2.append(">\n");
      int var3;
      if (null != this.getWeblogicRDBMSBeans()) {
         for(var3 = 0; var3 < this.getWeblogicRDBMSBeans().length; ++var3) {
            var2.append(this.getWeblogicRDBMSBeans()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getWeblogicRDBMSRelations()) {
         for(var3 = 0; var3 < this.getWeblogicRDBMSRelations().length; ++var3) {
            var2.append(this.getWeblogicRDBMSRelations()[var3].toXML(var1 + 2));
         }
      }

      if (this.isSet_orderDatabaseOperations || !this.getOrderDatabaseOperations()) {
         var2.append(ToXML.indent(var1 + 2)).append("<order-database-operations>").append(ToXML.capitalize(Boolean.valueOf(this.getOrderDatabaseOperations()).toString())).append("</order-database-operations>\n");
      }

      if (this.isSet_enableBatchOperations || !this.getEnableBatchOperations()) {
         var2.append(ToXML.indent(var1 + 2)).append("<enable-batch-operations>").append(ToXML.capitalize(Boolean.valueOf(this.getEnableBatchOperations()).toString())).append("</enable-batch-operations>\n");
      }

      if ((this.isSet_createDefaultDBMSTables || !"Disabled".equals(this.getCreateDefaultDBMSTables())) && null != this.getCreateDefaultDBMSTables()) {
         var2.append(ToXML.indent(var1 + 2)).append("<create-default-dbms-tables>").append(this.getCreateDefaultDBMSTables()).append("</create-default-dbms-tables>\n");
      }

      if ((this.isSet_validateDbSchemaWith || !"TableQuery".equals(this.getValidateDbSchemaWith())) && null != this.getValidateDbSchemaWith()) {
         var2.append(ToXML.indent(var1 + 2)).append("<validate-db-schema-with>").append(this.getValidateDbSchemaWith()).append("</validate-db-schema-with>\n");
      }

      if (null != this.getDatabaseType()) {
         var2.append(ToXML.indent(var1 + 2)).append("<database-type>").append(this.getDatabaseType()).append("</database-type>\n");
      }

      if (null != this.getDefaultDbmsTablesDdl()) {
         var2.append(ToXML.indent(var1 + 2)).append("<default-dbms-tables-ddl>").append(this.getDefaultDbmsTablesDdl()).append("</default-dbms-tables-ddl>\n");
      }

      if (null != this.getCompatibilityBean()) {
         var2.append(this.getCompatibilityBean().toXML(var1 + 2)).append("\n");
      }

      var2.append(ToXML.indent(var1)).append("</weblogic-rdbms-jar>\n");
      return var2.toString();
   }
}
