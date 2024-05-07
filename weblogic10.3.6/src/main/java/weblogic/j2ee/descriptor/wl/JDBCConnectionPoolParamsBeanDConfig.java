package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class JDBCConnectionPoolParamsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private JDBCConnectionPoolParamsBean beanTreeNode;

   public JDBCConnectionPoolParamsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (JDBCConnectionPoolParamsBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
   }

   private void initXpaths() throws ConfigurationException {
      ArrayList var1 = new ArrayList();
      this.xpaths = (String[])((String[])var1.toArray(new String[0]));
   }

   private void customInit() throws ConfigurationException {
   }

   public DConfigBean createDConfigBean(DDBean var1, DConfigBean var2) throws ConfigurationException {
      return null;
   }

   public String keyPropertyValue() {
      return null;
   }

   public void initKeyPropertyValue(String var1) {
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public int getInitialCapacity() {
      return this.beanTreeNode.getInitialCapacity();
   }

   public void setInitialCapacity(int var1) {
      this.beanTreeNode.setInitialCapacity(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "InitialCapacity", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getMaxCapacity() {
      return this.beanTreeNode.getMaxCapacity();
   }

   public void setMaxCapacity(int var1) {
      this.beanTreeNode.setMaxCapacity(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MaxCapacity", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getMinCapacity() {
      return this.beanTreeNode.getMinCapacity();
   }

   public void setMinCapacity(int var1) {
      this.beanTreeNode.setMinCapacity(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MinCapacity", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getCapacityIncrement() {
      return this.beanTreeNode.getCapacityIncrement();
   }

   public void setCapacityIncrement(int var1) {
      this.beanTreeNode.setCapacityIncrement(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CapacityIncrement", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getShrinkFrequencySeconds() {
      return this.beanTreeNode.getShrinkFrequencySeconds();
   }

   public void setShrinkFrequencySeconds(int var1) {
      this.beanTreeNode.setShrinkFrequencySeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ShrinkFrequencySeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getHighestNumWaiters() {
      return this.beanTreeNode.getHighestNumWaiters();
   }

   public void setHighestNumWaiters(int var1) {
      this.beanTreeNode.setHighestNumWaiters(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "HighestNumWaiters", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getConnectionCreationRetryFrequencySeconds() {
      return this.beanTreeNode.getConnectionCreationRetryFrequencySeconds();
   }

   public void setConnectionCreationRetryFrequencySeconds(int var1) {
      this.beanTreeNode.setConnectionCreationRetryFrequencySeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConnectionCreationRetryFrequencySeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getConnectionReserveTimeoutSeconds() {
      return this.beanTreeNode.getConnectionReserveTimeoutSeconds();
   }

   public void setConnectionReserveTimeoutSeconds(int var1) {
      this.beanTreeNode.setConnectionReserveTimeoutSeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConnectionReserveTimeoutSeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getTestFrequencySeconds() {
      return this.beanTreeNode.getTestFrequencySeconds();
   }

   public void setTestFrequencySeconds(int var1) {
      this.beanTreeNode.setTestFrequencySeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TestFrequencySeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isTestConnectionsOnReserve() {
      return this.beanTreeNode.isTestConnectionsOnReserve();
   }

   public void setTestConnectionsOnReserve(boolean var1) {
      this.beanTreeNode.setTestConnectionsOnReserve(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TestConnectionsOnReserve", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getProfileHarvestFrequencySeconds() {
      return this.beanTreeNode.getProfileHarvestFrequencySeconds();
   }

   public void setProfileHarvestFrequencySeconds(int var1) {
      this.beanTreeNode.setProfileHarvestFrequencySeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ProfileHarvestFrequencySeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isIgnoreInUseConnectionsEnabled() {
      return this.beanTreeNode.isIgnoreInUseConnectionsEnabled();
   }

   public void setIgnoreInUseConnectionsEnabled(boolean var1) {
      this.beanTreeNode.setIgnoreInUseConnectionsEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "IgnoreInUseConnectionsEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getInactiveConnectionTimeoutSeconds() {
      return this.beanTreeNode.getInactiveConnectionTimeoutSeconds();
   }

   public void setInactiveConnectionTimeoutSeconds(int var1) {
      this.beanTreeNode.setInactiveConnectionTimeoutSeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "InactiveConnectionTimeoutSeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getTestTableName() {
      return this.beanTreeNode.getTestTableName();
   }

   public void setTestTableName(String var1) {
      this.beanTreeNode.setTestTableName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TestTableName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getLoginDelaySeconds() {
      return this.beanTreeNode.getLoginDelaySeconds();
   }

   public void setLoginDelaySeconds(int var1) {
      this.beanTreeNode.setLoginDelaySeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LoginDelaySeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getInitSql() {
      return this.beanTreeNode.getInitSql();
   }

   public void setInitSql(String var1) {
      this.beanTreeNode.setInitSql(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "InitSql", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getStatementCacheSize() {
      return this.beanTreeNode.getStatementCacheSize();
   }

   public void setStatementCacheSize(int var1) {
      this.beanTreeNode.setStatementCacheSize(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "StatementCacheSize", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getStatementCacheType() {
      return this.beanTreeNode.getStatementCacheType();
   }

   public void setStatementCacheType(String var1) {
      this.beanTreeNode.setStatementCacheType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "StatementCacheType", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isRemoveInfectedConnections() {
      return this.beanTreeNode.isRemoveInfectedConnections();
   }

   public void setRemoveInfectedConnections(boolean var1) {
      this.beanTreeNode.setRemoveInfectedConnections(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RemoveInfectedConnections", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getSecondsToTrustAnIdlePoolConnection() {
      return this.beanTreeNode.getSecondsToTrustAnIdlePoolConnection();
   }

   public void setSecondsToTrustAnIdlePoolConnection(int var1) {
      this.beanTreeNode.setSecondsToTrustAnIdlePoolConnection(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SecondsToTrustAnIdlePoolConnection", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getStatementTimeout() {
      return this.beanTreeNode.getStatementTimeout();
   }

   public void setStatementTimeout(int var1) {
      this.beanTreeNode.setStatementTimeout(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "StatementTimeout", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getProfileType() {
      return this.beanTreeNode.getProfileType();
   }

   public void setProfileType(int var1) {
      this.beanTreeNode.setProfileType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ProfileType", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getJDBCXADebugLevel() {
      return this.beanTreeNode.getJDBCXADebugLevel();
   }

   public void setJDBCXADebugLevel(int var1) {
      this.beanTreeNode.setJDBCXADebugLevel(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "JDBCXADebugLevel", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isCredentialMappingEnabled() {
      return this.beanTreeNode.isCredentialMappingEnabled();
   }

   public void setCredentialMappingEnabled(boolean var1) {
      this.beanTreeNode.setCredentialMappingEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CredentialMappingEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getDriverInterceptor() {
      return this.beanTreeNode.getDriverInterceptor();
   }

   public void setDriverInterceptor(String var1) {
      this.beanTreeNode.setDriverInterceptor(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DriverInterceptor", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isPinnedToThread() {
      return this.beanTreeNode.isPinnedToThread();
   }

   public void setPinnedToThread(boolean var1) {
      this.beanTreeNode.setPinnedToThread(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PinnedToThread", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isIdentityBasedConnectionPoolingEnabled() {
      return this.beanTreeNode.isIdentityBasedConnectionPoolingEnabled();
   }

   public void setIdentityBasedConnectionPoolingEnabled(boolean var1) {
      this.beanTreeNode.setIdentityBasedConnectionPoolingEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "IdentityBasedConnectionPoolingEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isWrapTypes() {
      return this.beanTreeNode.isWrapTypes();
   }

   public void setWrapTypes(boolean var1) {
      this.beanTreeNode.setWrapTypes(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "WrapTypes", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getFatalErrorCodes() {
      return this.beanTreeNode.getFatalErrorCodes();
   }

   public void setFatalErrorCodes(String var1) {
      this.beanTreeNode.setFatalErrorCodes(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "FatalErrorCodes", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getConnectionLabelingCallback() {
      return this.beanTreeNode.getConnectionLabelingCallback();
   }

   public void setConnectionLabelingCallback(String var1) {
      this.beanTreeNode.setConnectionLabelingCallback(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConnectionLabelingCallback", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getConnectionHarvestMaxCount() {
      return this.beanTreeNode.getConnectionHarvestMaxCount();
   }

   public void setConnectionHarvestMaxCount(int var1) {
      this.beanTreeNode.setConnectionHarvestMaxCount(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConnectionHarvestMaxCount", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getConnectionHarvestTriggerCount() {
      return this.beanTreeNode.getConnectionHarvestTriggerCount();
   }

   public void setConnectionHarvestTriggerCount(int var1) {
      this.beanTreeNode.setConnectionHarvestTriggerCount(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConnectionHarvestTriggerCount", (Object)null, (Object)null));
      this.setModified(true);
   }
}
