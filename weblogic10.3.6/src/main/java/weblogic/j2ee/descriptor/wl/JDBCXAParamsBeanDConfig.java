package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class JDBCXAParamsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private JDBCXAParamsBean beanTreeNode;

   public JDBCXAParamsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (JDBCXAParamsBean)var2;
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

   public boolean isKeepXaConnTillTxComplete() {
      return this.beanTreeNode.isKeepXaConnTillTxComplete();
   }

   public void setKeepXaConnTillTxComplete(boolean var1) {
      this.beanTreeNode.setKeepXaConnTillTxComplete(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "KeepXaConnTillTxComplete", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isNeedTxCtxOnClose() {
      return this.beanTreeNode.isNeedTxCtxOnClose();
   }

   public void setNeedTxCtxOnClose(boolean var1) {
      this.beanTreeNode.setNeedTxCtxOnClose(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "NeedTxCtxOnClose", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isXaEndOnlyOnce() {
      return this.beanTreeNode.isXaEndOnlyOnce();
   }

   public void setXaEndOnlyOnce(boolean var1) {
      this.beanTreeNode.setXaEndOnlyOnce(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "XaEndOnlyOnce", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isNewXaConnForCommit() {
      return this.beanTreeNode.isNewXaConnForCommit();
   }

   public void setNewXaConnForCommit(boolean var1) {
      this.beanTreeNode.setNewXaConnForCommit(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "NewXaConnForCommit", (Object)null, (Object)null));
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

   public boolean isResourceHealthMonitoring() {
      return this.beanTreeNode.isResourceHealthMonitoring();
   }

   public void setResourceHealthMonitoring(boolean var1) {
      this.beanTreeNode.setResourceHealthMonitoring(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ResourceHealthMonitoring", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isRecoverOnlyOnce() {
      return this.beanTreeNode.isRecoverOnlyOnce();
   }

   public void setRecoverOnlyOnce(boolean var1) {
      this.beanTreeNode.setRecoverOnlyOnce(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RecoverOnlyOnce", (Object)null, (Object)null));
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

   public boolean isRollbackLocalTxUponConnClose() {
      return this.beanTreeNode.isRollbackLocalTxUponConnClose();
   }

   public void setRollbackLocalTxUponConnClose(boolean var1) {
      this.beanTreeNode.setRollbackLocalTxUponConnClose(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RollbackLocalTxUponConnClose", (Object)null, (Object)null));
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

   public int getXaRetryIntervalSeconds() {
      return this.beanTreeNode.getXaRetryIntervalSeconds();
   }

   public void setXaRetryIntervalSeconds(int var1) {
      this.beanTreeNode.setXaRetryIntervalSeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "XaRetryIntervalSeconds", (Object)null, (Object)null));
      this.setModified(true);
   }
}
