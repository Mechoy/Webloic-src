package weblogic.management.descriptors.cmp11;

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
   private boolean isSet_createDefaultDBMSTables = false;
   private String createDefaultDBMSTables = "Disabled";
   private boolean isSet_weblogicRDBMSBeans = false;
   private List weblogicRDBMSBeans;
   private boolean isSet_version = false;
   private String version;
   private boolean isSet_databaseType = false;
   private String databaseType;

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

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<weblogic-rdbms-jar");
      var2.append(">\n");
      if (null != this.getWeblogicRDBMSBeans()) {
         for(int var3 = 0; var3 < this.getWeblogicRDBMSBeans().length; ++var3) {
            var2.append(this.getWeblogicRDBMSBeans()[var3].toXML(var1 + 2));
         }
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

      var2.append(ToXML.indent(var1)).append("</weblogic-rdbms-jar>\n");
      return var2.toString();
   }
}
