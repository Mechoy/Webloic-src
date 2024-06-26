package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class ConnectionCheckParamsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private ConnectionCheckParamsBean beanTreeNode;

   public ConnectionCheckParamsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (ConnectionCheckParamsBean)var2;
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

   public String getTableName() {
      return this.beanTreeNode.getTableName();
   }

   public void setTableName(String var1) {
      this.beanTreeNode.setTableName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TableName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isCheckOnReserveEnabled() {
      return this.beanTreeNode.isCheckOnReserveEnabled();
   }

   public void setCheckOnReserveEnabled(boolean var1) {
      this.beanTreeNode.setCheckOnReserveEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CheckOnReserveEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isCheckOnReleaseEnabled() {
      return this.beanTreeNode.isCheckOnReleaseEnabled();
   }

   public void setCheckOnReleaseEnabled(boolean var1) {
      this.beanTreeNode.setCheckOnReleaseEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CheckOnReleaseEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getRefreshMinutes() {
      return this.beanTreeNode.getRefreshMinutes();
   }

   public void setRefreshMinutes(int var1) {
      this.beanTreeNode.setRefreshMinutes(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RefreshMinutes", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isCheckOnCreateEnabled() {
      return this.beanTreeNode.isCheckOnCreateEnabled();
   }

   public void setCheckOnCreateEnabled(boolean var1) {
      this.beanTreeNode.setCheckOnCreateEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CheckOnCreateEnabled", (Object)null, (Object)null));
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

   public int getConnectionCreationRetryFrequencySeconds() {
      return this.beanTreeNode.getConnectionCreationRetryFrequencySeconds();
   }

   public void setConnectionCreationRetryFrequencySeconds(int var1) {
      this.beanTreeNode.setConnectionCreationRetryFrequencySeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConnectionCreationRetryFrequencySeconds", (Object)null, (Object)null));
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

   public int getTestFrequencySeconds() {
      return this.beanTreeNode.getTestFrequencySeconds();
   }

   public void setTestFrequencySeconds(int var1) {
      this.beanTreeNode.setTestFrequencySeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TestFrequencySeconds", (Object)null, (Object)null));
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
}
