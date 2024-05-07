package weblogic.jdbc.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class MetaJDBCDriverInfo implements Serializable {
   private static final long serialVersionUID = 184401685166623934L;
   private String dbmsVendor;
   private String dbmsDriverVendor;
   private String driverClassName;
   private String driverType;
   private String urlHelperClassname;
   private String testSQL;
   private String versionString;
   private String installURL;
   private String description;
   private String datasourceTemplateName = null;
   private String jdbcProviderTemplateName = null;
   private List versionList = new ArrayList();
   private Map driverAttributes = new LinkedHashMap(20);
   private Map unknownDriverAttributeKeys = new LinkedHashMap(20);
   private boolean forXA;
   private boolean cert = false;

   MetaJDBCDriverInfo() {
   }

   public void setDbmsVersion(String var1) {
      this.versionString = var1;
      StringTokenizer var2 = new StringTokenizer(var1, ",");

      while(var2.hasMoreTokens()) {
         String var3 = var2.nextToken();
         this.versionList.add(var3);
      }

   }

   public List getDbmsVersionList() {
      return this.versionList;
   }

   public String getDbmsVersion() {
      return this.versionString;
   }

   public void setDbmsVendor(String var1) {
      this.dbmsVendor = var1;
   }

   public String getDbmsVendor() {
      return this.dbmsVendor;
   }

   public void setDriverVendor(String var1) {
      this.dbmsDriverVendor = var1;
   }

   public String getDriverVendor() {
      return this.dbmsDriverVendor;
   }

   public void setDriverClassName(String var1) {
      this.driverClassName = var1;
   }

   public String getDriverClassName() {
      return this.driverClassName;
   }

   public void setURLHelperClassName(String var1) {
      this.urlHelperClassname = var1;
   }

   public String getURLHelperClassName() {
      return this.urlHelperClassname;
   }

   public void setType(String var1) {
      this.driverType = var1;
   }

   public String getType() {
      return this.driverType;
   }

   public void setTestSQL(String var1) {
      this.testSQL = var1;
   }

   public String getTestSQL() {
      return this.testSQL;
   }

   public void setInstallURL(String var1) {
      this.installURL = var1;
   }

   public String getInstallURL() {
      return this.installURL;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setJdbcProviderTemplateName(String var1) {
      this.jdbcProviderTemplateName = var1;
   }

   public String getJdbcProviderTemplateName() {
      return this.jdbcProviderTemplateName;
   }

   public void setDatasourceTemplateName(String var1) {
      this.datasourceTemplateName = var1;
   }

   public String getDatasourceTemplateName() {
      return this.datasourceTemplateName;
   }

   public void setForXA(String var1) {
      this.setForXA(new Boolean(var1));
   }

   public void setForXA(boolean var1) {
      this.forXA = var1;
   }

   public boolean isForXA() {
      return this.forXA;
   }

   public void setCert(String var1) {
      this.setCert(new Boolean(var1));
   }

   public void setCert(boolean var1) {
      this.cert = var1;
   }

   public boolean isCert() {
      return this.cert;
   }

   public void setDriverAttribute(String var1, JDBCDriverAttribute var2) {
      boolean var3 = true;

      for(int var4 = 0; var4 < JDBCDriverInfo.WELL_KNOWN_KEYS.length; ++var4) {
         if (JDBCDriverInfo.WELL_KNOWN_KEYS[var4].equals(var1)) {
            var3 = false;
         }
      }

      if (var3) {
         this.unknownDriverAttributeKeys.put(var1, var2);
      }

      this.driverAttributes.put(var1, var2);
   }

   public HashMap getDriverAttributes() {
      LinkedHashMap var1 = new LinkedHashMap(this.driverAttributes.size());

      String var3;
      JDBCDriverAttribute var4;
      for(Iterator var2 = this.driverAttributes.keySet().iterator(); var2.hasNext(); var1.put(var3, var4)) {
         var3 = (String)var2.next();
         var4 = null;

         try {
            var4 = (JDBCDriverAttribute)((JDBCDriverAttribute)this.driverAttributes.get(var3)).clone();
         } catch (CloneNotSupportedException var6) {
         }
      }

      return var1;
   }

   public Set getUnknownDriverAttributesKeys() {
      return this.unknownDriverAttributeKeys.keySet();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.isCert()) {
         var1.append("*");
      }

      var1.append(this.getDriverVendor());
      var1.append("'s");
      if (!this.getDriverVendor().equals(this.getDbmsVendor())) {
         var1.append(" " + this.getDbmsVendor());
      }

      var1.append(" Driver ");
      if (this.isForXA() || this.getType() != null) {
         var1.append("(");
         if (this.getType() != null) {
            var1.append(this.getType());
            if (this.isForXA()) {
               var1.append(" XA");
            }

            var1.append(") ");
         } else {
            var1.append("XA) ");
         }
      }

      if (this.getDescription() != null) {
         var1.append(this.getDescription() + " ");
      }

      String var2 = this.getDbmsVersion();
      if (var2 != null && !var2.trim().equals("")) {
         var1.append("Versions:");
         var1.append(this.getDbmsVersion());
      }

      return var1.toString();
   }
}
