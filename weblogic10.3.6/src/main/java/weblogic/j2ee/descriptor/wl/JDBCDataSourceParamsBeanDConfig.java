package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class JDBCDataSourceParamsBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private JDBCDataSourceParamsBean beanTreeNode;

   public JDBCDataSourceParamsBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (JDBCDataSourceParamsBean)var2;
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

   public String[] getJNDINames() {
      return this.beanTreeNode.getJNDINames();
   }

   public void setJNDINames(String[] var1) {
      this.beanTreeNode.setJNDINames(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "JNDINames", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getScope() {
      return this.beanTreeNode.getScope();
   }

   public void setScope(String var1) {
      this.beanTreeNode.setScope(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Scope", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isRowPrefetch() {
      return this.beanTreeNode.isRowPrefetch();
   }

   public void setRowPrefetch(boolean var1) {
      this.beanTreeNode.setRowPrefetch(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RowPrefetch", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getRowPrefetchSize() {
      return this.beanTreeNode.getRowPrefetchSize();
   }

   public void setRowPrefetchSize(int var1) {
      this.beanTreeNode.setRowPrefetchSize(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RowPrefetchSize", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getStreamChunkSize() {
      return this.beanTreeNode.getStreamChunkSize();
   }

   public void setStreamChunkSize(int var1) {
      this.beanTreeNode.setStreamChunkSize(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "StreamChunkSize", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getAlgorithmType() {
      return this.beanTreeNode.getAlgorithmType();
   }

   public void setAlgorithmType(String var1) {
      this.beanTreeNode.setAlgorithmType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "AlgorithmType", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getDataSourceList() {
      return this.beanTreeNode.getDataSourceList();
   }

   public void setDataSourceList(String var1) {
      this.beanTreeNode.setDataSourceList(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DataSourceList", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getConnectionPoolFailoverCallbackHandler() {
      return this.beanTreeNode.getConnectionPoolFailoverCallbackHandler();
   }

   public void setConnectionPoolFailoverCallbackHandler(String var1) {
      this.beanTreeNode.setConnectionPoolFailoverCallbackHandler(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ConnectionPoolFailoverCallbackHandler", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isFailoverRequestIfBusy() {
      return this.beanTreeNode.isFailoverRequestIfBusy();
   }

   public void setFailoverRequestIfBusy(boolean var1) {
      this.beanTreeNode.setFailoverRequestIfBusy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "FailoverRequestIfBusy", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getGlobalTransactionsProtocol() {
      return this.beanTreeNode.getGlobalTransactionsProtocol();
   }

   public void setGlobalTransactionsProtocol(String var1) {
      this.beanTreeNode.setGlobalTransactionsProtocol(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "GlobalTransactionsProtocol", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isKeepConnAfterLocalTx() {
      return this.beanTreeNode.isKeepConnAfterLocalTx();
   }

   public void setKeepConnAfterLocalTx(boolean var1) {
      this.beanTreeNode.setKeepConnAfterLocalTx(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "KeepConnAfterLocalTx", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isKeepConnAfterGlobalTx() {
      return this.beanTreeNode.isKeepConnAfterGlobalTx();
   }

   public void setKeepConnAfterGlobalTx(boolean var1) {
      this.beanTreeNode.setKeepConnAfterGlobalTx(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "KeepConnAfterGlobalTx", (Object)null, (Object)null));
      this.setModified(true);
   }
}
