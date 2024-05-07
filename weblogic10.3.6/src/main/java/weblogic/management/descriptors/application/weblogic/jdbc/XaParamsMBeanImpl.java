package weblogic.management.descriptors.application.weblogic.jdbc;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class XaParamsMBeanImpl extends XMLElementMBeanDelegate implements XaParamsMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_keepLogicalConnOpenOnRelease = false;
   private boolean keepLogicalConnOpenOnRelease;
   private boolean isSet_debugLevel = false;
   private int debugLevel;
   private boolean isSet_recoverOnlyOnceEnabled = false;
   private boolean recoverOnlyOnceEnabled;
   private boolean isSet_xaTransactionTimeout = false;
   private int xaTransactionTimeout;
   private boolean isSet_resourceHealthMonitoringEnabled = false;
   private boolean resourceHealthMonitoringEnabled;
   private boolean isSet_xaSetTransactionTimeout = false;
   private boolean xaSetTransactionTimeout;
   private boolean isSet_rollbackLocalTxUponConnClose = false;
   private boolean rollbackLocalTxUponConnClose;
   private boolean isSet_localTransactionSupported = false;
   private boolean localTransactionSupported;
   private boolean isSet_endOnlyOnceEnabled = false;
   private boolean endOnlyOnceEnabled;
   private boolean isSet_keepConnUntilTxCompleteEnabled = false;
   private boolean keepConnUntilTxCompleteEnabled;
   private boolean isSet_xaRetryIntervalSeconds = false;
   private int xaRetryIntervalSeconds;
   private boolean isSet_txContextOnCloseNeeded = false;
   private boolean txContextOnCloseNeeded;
   private boolean isSet_newConnForCommitEnabled = false;
   private boolean newConnForCommitEnabled;
   private boolean isSet_xaRetryDurationSeconds = false;
   private int xaRetryDurationSeconds;
   private boolean isSet_preparedStatementCacheSize = false;
   private int preparedStatementCacheSize;

   public boolean getKeepLogicalConnOpenOnRelease() {
      return this.keepLogicalConnOpenOnRelease;
   }

   public void setKeepLogicalConnOpenOnRelease(boolean var1) {
      boolean var2 = this.keepLogicalConnOpenOnRelease;
      this.keepLogicalConnOpenOnRelease = var1;
      this.isSet_keepLogicalConnOpenOnRelease = true;
      this.checkChange("keepLogicalConnOpenOnRelease", var2, this.keepLogicalConnOpenOnRelease);
   }

   public int getDebugLevel() {
      return this.debugLevel;
   }

   public void setDebugLevel(int var1) {
      int var2 = this.debugLevel;
      this.debugLevel = var1;
      this.isSet_debugLevel = var1 != -1;
      this.checkChange("debugLevel", var2, this.debugLevel);
   }

   public boolean isRecoverOnlyOnceEnabled() {
      return this.recoverOnlyOnceEnabled;
   }

   public void setRecoverOnlyOnceEnabled(boolean var1) {
      boolean var2 = this.recoverOnlyOnceEnabled;
      this.recoverOnlyOnceEnabled = var1;
      this.isSet_recoverOnlyOnceEnabled = true;
      this.checkChange("recoverOnlyOnceEnabled", var2, this.recoverOnlyOnceEnabled);
   }

   public int getXATransactionTimeout() {
      return this.xaTransactionTimeout;
   }

   public void setXATransactionTimeout(int var1) {
      int var2 = this.xaTransactionTimeout;
      this.xaTransactionTimeout = var1;
      this.isSet_xaTransactionTimeout = var1 != -1;
      this.checkChange("xaTransactionTimeout", var2, this.xaTransactionTimeout);
   }

   public boolean isResourceHealthMonitoringEnabled() {
      return this.resourceHealthMonitoringEnabled;
   }

   public void setResourceHealthMonitoringEnabled(boolean var1) {
      boolean var2 = this.resourceHealthMonitoringEnabled;
      this.resourceHealthMonitoringEnabled = var1;
      this.isSet_resourceHealthMonitoringEnabled = true;
      this.checkChange("resourceHealthMonitoringEnabled", var2, this.resourceHealthMonitoringEnabled);
   }

   public boolean getXASetTransactionTimeout() {
      return this.xaSetTransactionTimeout;
   }

   public void setXASetTransactionTimeout(boolean var1) {
      boolean var2 = this.xaSetTransactionTimeout;
      this.xaSetTransactionTimeout = var1;
      this.isSet_xaSetTransactionTimeout = true;
      this.checkChange("xaSetTransactionTimeout", var2, this.xaSetTransactionTimeout);
   }

   public boolean getRollbackLocalTxUponConnClose() {
      return this.rollbackLocalTxUponConnClose;
   }

   public void setRollbackLocalTxUponConnClose(boolean var1) {
      boolean var2 = this.rollbackLocalTxUponConnClose;
      this.rollbackLocalTxUponConnClose = var1;
      this.isSet_rollbackLocalTxUponConnClose = true;
      this.checkChange("rollbackLocalTxUponConnClose", var2, this.rollbackLocalTxUponConnClose);
   }

   public boolean isLocalTransactionSupported() {
      return this.localTransactionSupported;
   }

   public void setLocalTransactionSupported(boolean var1) {
      boolean var2 = this.localTransactionSupported;
      this.localTransactionSupported = var1;
      this.isSet_localTransactionSupported = true;
      this.checkChange("localTransactionSupported", var2, this.localTransactionSupported);
   }

   public boolean isEndOnlyOnceEnabled() {
      return this.endOnlyOnceEnabled;
   }

   public void setEndOnlyOnceEnabled(boolean var1) {
      boolean var2 = this.endOnlyOnceEnabled;
      this.endOnlyOnceEnabled = var1;
      this.isSet_endOnlyOnceEnabled = true;
      this.checkChange("endOnlyOnceEnabled", var2, this.endOnlyOnceEnabled);
   }

   public boolean isKeepConnUntilTxCompleteEnabled() {
      return this.keepConnUntilTxCompleteEnabled;
   }

   public void setKeepConnUntilTxCompleteEnabled(boolean var1) {
      boolean var2 = this.keepConnUntilTxCompleteEnabled;
      this.keepConnUntilTxCompleteEnabled = var1;
      this.isSet_keepConnUntilTxCompleteEnabled = true;
      this.checkChange("keepConnUntilTxCompleteEnabled", var2, this.keepConnUntilTxCompleteEnabled);
   }

   public int getXARetryIntervalSeconds() {
      return this.xaRetryIntervalSeconds;
   }

   public void setXARetryIntervalSeconds(int var1) {
      int var2 = this.xaRetryIntervalSeconds;
      this.xaRetryIntervalSeconds = var1;
      this.isSet_xaRetryIntervalSeconds = var1 != -1;
      this.checkChange("xaRetryIntervalSeconds", var2, this.xaRetryIntervalSeconds);
   }

   public boolean isTxContextOnCloseNeeded() {
      return this.txContextOnCloseNeeded;
   }

   public void setTxContextOnCloseNeeded(boolean var1) {
      boolean var2 = this.txContextOnCloseNeeded;
      this.txContextOnCloseNeeded = var1;
      this.isSet_txContextOnCloseNeeded = true;
      this.checkChange("txContextOnCloseNeeded", var2, this.txContextOnCloseNeeded);
   }

   public boolean isNewConnForCommitEnabled() {
      return this.newConnForCommitEnabled;
   }

   public void setNewConnForCommitEnabled(boolean var1) {
      boolean var2 = this.newConnForCommitEnabled;
      this.newConnForCommitEnabled = var1;
      this.isSet_newConnForCommitEnabled = true;
      this.checkChange("newConnForCommitEnabled", var2, this.newConnForCommitEnabled);
   }

   public int getXARetryDurationSeconds() {
      return this.xaRetryDurationSeconds;
   }

   public void setXARetryDurationSeconds(int var1) {
      int var2 = this.xaRetryDurationSeconds;
      this.xaRetryDurationSeconds = var1;
      this.isSet_xaRetryDurationSeconds = var1 != -1;
      this.checkChange("xaRetryDurationSeconds", var2, this.xaRetryDurationSeconds);
   }

   public int getPreparedStatementCacheSize() {
      return this.preparedStatementCacheSize;
   }

   public void setPreparedStatementCacheSize(int var1) {
      int var2 = this.preparedStatementCacheSize;
      this.preparedStatementCacheSize = var1;
      this.isSet_preparedStatementCacheSize = var1 != -1;
      this.checkChange("preparedStatementCacheSize", var2, this.preparedStatementCacheSize);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<xa-params");
      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</xa-params>\n");
      return var2.toString();
   }
}
