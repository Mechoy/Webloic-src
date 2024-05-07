package weblogic.management.descriptors.application.weblogic.jdbc;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ConnectionCheckParamsMBeanCustomImpl extends XMLElementMBeanDelegate implements ConnectionCheckParamsMBean {
   private String descrEncoding = null;
   private String descriptorVersion = null;
   private Integer inactiveConnectionTimeoutSeconds;
   private Integer creationRetryFrequencySeconds;
   private Integer reserveTimeoutSeconds;
   private Integer testFrequencySeconds;
   private Boolean checkOnCreateEnabled;
   private Boolean checkOnReleaseEnabled;
   private Boolean checkOnReserveEnabled;
   private String tableName;
   private String initTableName;
   private Integer refreshMinutes;

   public void setEncoding(String var1) {
      this.descrEncoding = var1;
   }

   public void setVersion(String var1) {
      String var2 = this.descriptorVersion;
      this.descriptorVersion = var1;
      this.checkChange("version", var2, var1);
   }

   public void setConnectionCreationRetryFrequencySeconds(int var1) {
      Integer var2 = this.creationRetryFrequencySeconds;
      this.creationRetryFrequencySeconds = new Integer(var1);
      this.checkChange("creationRetryFrequencySeconds", var2, this.creationRetryFrequencySeconds);
   }

   public void setConnectionReserveTimeoutSeconds(int var1) {
      Integer var2 = this.reserveTimeoutSeconds;
      this.reserveTimeoutSeconds = new Integer(var1);
      this.checkChange("connectionReserveTimeoutSeconds", var2, this.reserveTimeoutSeconds);
   }

   public void setTestFrequencySeconds(int var1) {
      Integer var2 = this.testFrequencySeconds;
      this.testFrequencySeconds = new Integer(var1);
      this.checkChange("testFrequencySeconds", var2, this.testFrequencySeconds);
   }

   public void setInactiveConnectionTimeoutSeconds(int var1) {
      Integer var2 = this.inactiveConnectionTimeoutSeconds;
      this.inactiveConnectionTimeoutSeconds = new Integer(var1);
      this.checkChange("inactiveConnectionTimeoutSeconds", var2, this.inactiveConnectionTimeoutSeconds);
   }

   public void setCheckOnCreateEnabled(boolean var1) {
      Boolean var2 = this.checkOnCreateEnabled;
      this.checkOnCreateEnabled = new Boolean(var1);
      this.checkChange("checkOnCreateEnabled", var2, this.checkOnCreateEnabled);
   }

   public void setCheckOnReleaseEnabled(boolean var1) {
      Boolean var2 = this.checkOnReleaseEnabled;
      this.checkOnReleaseEnabled = new Boolean(var1);
      this.checkChange("checkonreleaseenabled", var2, this.checkOnReleaseEnabled);
   }

   public void setCheckOnReserveEnabled(boolean var1) {
      Boolean var2 = this.checkOnReserveEnabled;
      this.checkOnReserveEnabled = new Boolean(var1);
      this.checkChange("checkOnReserveEnabled", var2, this.checkOnReserveEnabled);
   }

   public void setInitSQL(String var1) {
      String var2 = this.initTableName;
      this.initTableName = var1;
      this.checkChange("initSQL", var2, var1);
   }

   public void setTableName(String var1) {
      String var2 = this.tableName;
      this.tableName = var1;
      this.checkChange("tableName", var2, this.tableName);
   }

   public void setRefreshMinutes(int var1) {
      Integer var2 = this.refreshMinutes;
      this.refreshMinutes = new Integer(var1);
      this.checkChange("refreshMinutes", var2, this.refreshMinutes);
   }

   public String getEncoding() {
      return this.descrEncoding;
   }

   public String getVersion() {
      return this.descriptorVersion;
   }

   public int getConnectionCreationRetryFrequencySeconds() {
      return this.creationRetryFrequencySeconds != null ? this.creationRetryFrequencySeconds : 0;
   }

   public int getConnectionReserveTimeoutSeconds() {
      return this.reserveTimeoutSeconds != null ? this.reserveTimeoutSeconds : 10;
   }

   public int getTestFrequencySeconds() {
      return this.testFrequencySeconds != null ? this.testFrequencySeconds : 0;
   }

   public int getInactiveConnectionTimeoutSeconds() {
      return this.inactiveConnectionTimeoutSeconds != null ? this.inactiveConnectionTimeoutSeconds : 0;
   }

   public boolean isCheckOnCreateEnabled() {
      return this.checkOnCreateEnabled != null ? this.checkOnCreateEnabled : false;
   }

   public boolean isCheckOnReleaseEnabled() {
      return this.checkOnReleaseEnabled != null ? this.checkOnReleaseEnabled : false;
   }

   public boolean isCheckOnReserveEnabled() {
      return this.checkOnReserveEnabled != null ? this.checkOnReserveEnabled : false;
   }

   public String getInitSQL() {
      return this.initTableName;
   }

   public String getTableName() {
      return this.tableName;
   }

   public int getRefreshMinutes() {
      return this.refreshMinutes != null ? this.refreshMinutes : 0;
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<connection-check-params");
      var2.append(">\n");
      if (null != this.getTableName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<table-name>").append(this.getTableName()).append("</table-name>\n");
      }

      var2.append(ToXML.indent(var1 + 2)).append("<check-on-reserve-enabled>").append(ToXML.capitalize((new Boolean(this.isCheckOnReserveEnabled())).toString())).append("</check-on-reserve-enabled>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<check-on-release-enabled>").append(ToXML.capitalize((new Boolean(this.isCheckOnReleaseEnabled())).toString())).append("</check-on-release-enabled>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<refresh-minutes>").append(this.getRefreshMinutes()).append("</refresh-minutes>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<check-on-create-enabled>").append(ToXML.capitalize((new Boolean(this.isCheckOnCreateEnabled())).toString())).append("</check-on-create-enabled>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<connection-reserve-timeout-seconds>").append(this.getConnectionReserveTimeoutSeconds()).append("</connection-reserve-timeout-seconds>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<connection-creation-retry-frequency-seconds>").append(this.getConnectionCreationRetryFrequencySeconds()).append("</connection-creation-retry-frequency-seconds>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<inactive-connection-timeout-seconds>").append(this.getInactiveConnectionTimeoutSeconds()).append("</inactive-connection-timeout-seconds>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<test-frequency-seconds>").append(this.getTestFrequencySeconds()).append("</test-frequency-seconds>\n");
      if (null != this.getInitSQL()) {
         var2.append(ToXML.indent(var1 + 2)).append("<init-sql>").append(this.getInitSQL()).append("</init-sql>\n");
      }

      var2.append(ToXML.indent(var1)).append("</connection-check-params>\n");
      return var2.toString();
   }
}
