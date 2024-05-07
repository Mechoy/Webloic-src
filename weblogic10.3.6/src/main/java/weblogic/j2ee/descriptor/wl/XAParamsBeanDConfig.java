package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class XAParamsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private XAParamsBean beanTreeNode;

   public XAParamsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (XAParamsBean)var2;
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

   public int getDebugLevel() {
      return this.beanTreeNode.getDebugLevel();
   }

   public void setDebugLevel(int var1) {
      this.beanTreeNode.setDebugLevel(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DebugLevel", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isKeepConnUntilTxCompleteEnabled() {
      return this.beanTreeNode.isKeepConnUntilTxCompleteEnabled();
   }

   public void setKeepConnUntilTxCompleteEnabled(boolean var1) {
      this.beanTreeNode.setKeepConnUntilTxCompleteEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "KeepConnUntilTxCompleteEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isEndOnlyOnceEnabled() {
      return this.beanTreeNode.isEndOnlyOnceEnabled();
   }

   public void setEndOnlyOnceEnabled(boolean var1) {
      this.beanTreeNode.setEndOnlyOnceEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "EndOnlyOnceEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isRecoverOnlyOnceEnabled() {
      return this.beanTreeNode.isRecoverOnlyOnceEnabled();
   }

   public void setRecoverOnlyOnceEnabled(boolean var1) {
      this.beanTreeNode.setRecoverOnlyOnceEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RecoverOnlyOnceEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isTxContextOnCloseNeeded() {
      return this.beanTreeNode.isTxContextOnCloseNeeded();
   }

   public void setTxContextOnCloseNeeded(boolean var1) {
      this.beanTreeNode.setTxContextOnCloseNeeded(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TxContextOnCloseNeeded", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isNewConnForCommitEnabled() {
      return this.beanTreeNode.isNewConnForCommitEnabled();
   }

   public void setNewConnForCommitEnabled(boolean var1) {
      this.beanTreeNode.setNewConnForCommitEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "NewConnForCommitEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getPreparedStatementCacheSize() {
      return this.beanTreeNode.getPreparedStatementCacheSize();
   }

   public void setPreparedStatementCacheSize(int var1) {
      this.beanTreeNode.setPreparedStatementCacheSize(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PreparedStatementCacheSize", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isKeepLogicalConnOpenOnRelease() {
      return this.beanTreeNode.isKeepLogicalConnOpenOnRelease();
   }

   public void setKeepLogicalConnOpenOnRelease(boolean var1) {
      this.beanTreeNode.setKeepLogicalConnOpenOnRelease(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "KeepLogicalConnOpenOnRelease", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isLocalTransactionSupported() {
      return this.beanTreeNode.isLocalTransactionSupported();
   }

   public void setLocalTransactionSupported(boolean var1) {
      this.beanTreeNode.setLocalTransactionSupported(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LocalTransactionSupported", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isResourceHealthMonitoringEnabled() {
      return this.beanTreeNode.isResourceHealthMonitoringEnabled();
   }

   public void setResourceHealthMonitoringEnabled(boolean var1) {
      this.beanTreeNode.setResourceHealthMonitoringEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ResourceHealthMonitoringEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isXaSetTransactionTimeout() {
      return this.beanTreeNode.isXaSetTransactionTimeout();
   }

   public void setXaSetTransactionTimeout(boolean var1) {
      this.beanTreeNode.setXaSetTransactionTimeout(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "XaSetTransactionTimeout", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getXaTransactionTimeout() {
      return this.beanTreeNode.getXaTransactionTimeout();
   }

   public void setXaTransactionTimeout(int var1) {
      this.beanTreeNode.setXaTransactionTimeout(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "XaTransactionTimeout", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isRollbackLocaltxUponConnclose() {
      return this.beanTreeNode.isRollbackLocaltxUponConnclose();
   }

   public void setRollbackLocaltxUponConnclose(boolean var1) {
      this.beanTreeNode.setRollbackLocaltxUponConnclose(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RollbackLocaltxUponConnclose", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getXaRetryDurationSeconds() {
      return this.beanTreeNode.getXaRetryDurationSeconds();
   }

   public void setXaRetryDurationSeconds(int var1) {
      this.beanTreeNode.setXaRetryDurationSeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "XaRetryDurationSeconds", (Object)null, (Object)null));
      this.setModified(true);
   }
}
