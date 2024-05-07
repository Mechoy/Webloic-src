package weblogic.management.descriptors.application.weblogic.jdbc;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ConnectionCheckParamsMBeanImpl extends XMLElementMBeanDelegate implements ConnectionCheckParamsMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_refreshMinutes = false;
   private int refreshMinutes;
   private boolean isSet_checkOnReserveEnabled = false;
   private boolean checkOnReserveEnabled;
   private boolean isSet_tableName = false;
   private String tableName;
   private boolean isSet_inactiveConnectionTimeoutSeconds = false;
   private int inactiveConnectionTimeoutSeconds;
   private boolean isSet_connectionCreationRetryFrequencySeconds = false;
   private int connectionCreationRetryFrequencySeconds;
   private boolean isSet_initSQL = false;
   private String initSQL;
   private boolean isSet_connectionReserveTimeoutSeconds = false;
   private int connectionReserveTimeoutSeconds;
   private boolean isSet_checkOnCreateEnabled = false;
   private boolean checkOnCreateEnabled;
   private boolean isSet_testFrequencySeconds = false;
   private int testFrequencySeconds;
   private boolean isSet_checkOnReleaseEnabled = false;
   private boolean checkOnReleaseEnabled;

   public int getRefreshMinutes() {
      return this.refreshMinutes;
   }

   public void setRefreshMinutes(int var1) {
      int var2 = this.refreshMinutes;
      this.refreshMinutes = var1;
      this.isSet_refreshMinutes = var1 != -1;
      this.checkChange("refreshMinutes", var2, this.refreshMinutes);
   }

   public boolean isCheckOnReserveEnabled() {
      return this.checkOnReserveEnabled;
   }

   public void setCheckOnReserveEnabled(boolean var1) {
      boolean var2 = this.checkOnReserveEnabled;
      this.checkOnReserveEnabled = var1;
      this.isSet_checkOnReserveEnabled = true;
      this.checkChange("checkOnReserveEnabled", var2, this.checkOnReserveEnabled);
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

   public int getInactiveConnectionTimeoutSeconds() {
      return this.inactiveConnectionTimeoutSeconds;
   }

   public void setInactiveConnectionTimeoutSeconds(int var1) {
      int var2 = this.inactiveConnectionTimeoutSeconds;
      this.inactiveConnectionTimeoutSeconds = var1;
      this.isSet_inactiveConnectionTimeoutSeconds = var1 != -1;
      this.checkChange("inactiveConnectionTimeoutSeconds", var2, this.inactiveConnectionTimeoutSeconds);
   }

   public int getConnectionCreationRetryFrequencySeconds() {
      return this.connectionCreationRetryFrequencySeconds;
   }

   public void setConnectionCreationRetryFrequencySeconds(int var1) {
      int var2 = this.connectionCreationRetryFrequencySeconds;
      this.connectionCreationRetryFrequencySeconds = var1;
      this.isSet_connectionCreationRetryFrequencySeconds = var1 != -1;
      this.checkChange("connectionCreationRetryFrequencySeconds", var2, this.connectionCreationRetryFrequencySeconds);
   }

   public String getInitSQL() {
      return this.initSQL;
   }

   public void setInitSQL(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.initSQL;
      this.initSQL = var1;
      this.isSet_initSQL = var1 != null;
      this.checkChange("initSQL", var2, this.initSQL);
   }

   public int getConnectionReserveTimeoutSeconds() {
      return this.connectionReserveTimeoutSeconds;
   }

   public void setConnectionReserveTimeoutSeconds(int var1) {
      int var2 = this.connectionReserveTimeoutSeconds;
      this.connectionReserveTimeoutSeconds = var1;
      this.isSet_connectionReserveTimeoutSeconds = var1 != -1;
      this.checkChange("connectionReserveTimeoutSeconds", var2, this.connectionReserveTimeoutSeconds);
   }

   public boolean isCheckOnCreateEnabled() {
      return this.checkOnCreateEnabled;
   }

   public void setCheckOnCreateEnabled(boolean var1) {
      boolean var2 = this.checkOnCreateEnabled;
      this.checkOnCreateEnabled = var1;
      this.isSet_checkOnCreateEnabled = true;
      this.checkChange("checkOnCreateEnabled", var2, this.checkOnCreateEnabled);
   }

   public int getTestFrequencySeconds() {
      return this.testFrequencySeconds;
   }

   public void setTestFrequencySeconds(int var1) {
      int var2 = this.testFrequencySeconds;
      this.testFrequencySeconds = var1;
      this.isSet_testFrequencySeconds = var1 != -1;
      this.checkChange("testFrequencySeconds", var2, this.testFrequencySeconds);
   }

   public boolean isCheckOnReleaseEnabled() {
      return this.checkOnReleaseEnabled;
   }

   public void setCheckOnReleaseEnabled(boolean var1) {
      boolean var2 = this.checkOnReleaseEnabled;
      this.checkOnReleaseEnabled = var1;
      this.isSet_checkOnReleaseEnabled = true;
      this.checkChange("checkOnReleaseEnabled", var2, this.checkOnReleaseEnabled);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<connection-check-params");
      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</connection-check-params>\n");
      return var2.toString();
   }
}
