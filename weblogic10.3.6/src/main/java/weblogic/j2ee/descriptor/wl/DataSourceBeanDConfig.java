package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class DataSourceBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private DataSourceBean beanTreeNode;

   public DataSourceBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (DataSourceBean)var2;
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
      return this.getName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Name: ");
      var1.append(this.beanTreeNode.getName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getDescription() {
      return this.beanTreeNode.getDescription();
   }

   public void setDescription(String var1) {
      this.beanTreeNode.setDescription(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Description", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getName() {
      return this.beanTreeNode.getName();
   }

   public void setName(String var1) {
      this.beanTreeNode.setName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Name", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getClassName() {
      return this.beanTreeNode.getClassName();
   }

   public void setClassName(String var1) {
      this.beanTreeNode.setClassName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ClassName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getServerName() {
      return this.beanTreeNode.getServerName();
   }

   public void setServerName(String var1) {
      this.beanTreeNode.setServerName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ServerName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getPortNumber() {
      return this.beanTreeNode.getPortNumber();
   }

   public void setPortNumber(int var1) {
      this.beanTreeNode.setPortNumber(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PortNumber", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getDatabaseName() {
      return this.beanTreeNode.getDatabaseName();
   }

   public void setDatabaseName(String var1) {
      this.beanTreeNode.setDatabaseName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DatabaseName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getUrl() {
      return this.beanTreeNode.getUrl();
   }

   public void setUrl(String var1) {
      this.beanTreeNode.setUrl(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Url", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getUser() {
      return this.beanTreeNode.getUser();
   }

   public void setUser(String var1) {
      this.beanTreeNode.setUser(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "User", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getPassword() {
      return this.beanTreeNode.getPassword();
   }

   public void setPassword(String var1) {
      this.beanTreeNode.setPassword(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Password", (Object)null, (Object)null));
      this.setModified(true);
   }

   public JavaEEPropertyBean[] getProperties() {
      return this.beanTreeNode.getProperties();
   }

   public int getLoginTimeout() {
      return this.beanTreeNode.getLoginTimeout();
   }

   public void setLoginTimeout(int var1) {
      this.beanTreeNode.setLoginTimeout(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LoginTimeout", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isTransactional() {
      return this.beanTreeNode.isTransactional();
   }

   public void setTransactional(boolean var1) {
      this.beanTreeNode.setTransactional(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Transactional", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getIsolationLevel() {
      return this.beanTreeNode.getIsolationLevel();
   }

   public void setIsolationLevel(String var1) {
      this.beanTreeNode.setIsolationLevel(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "IsolationLevel", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getInitialPoolSize() {
      return this.beanTreeNode.getInitialPoolSize();
   }

   public void setInitialPoolSize(int var1) {
      this.beanTreeNode.setInitialPoolSize(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "InitialPoolSize", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getMaxPoolSize() {
      return this.beanTreeNode.getMaxPoolSize();
   }

   public void setMaxPoolSize(int var1) {
      this.beanTreeNode.setMaxPoolSize(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MaxPoolSize", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getMinPoolSize() {
      return this.beanTreeNode.getMinPoolSize();
   }

   public void setMinPoolSize(int var1) {
      this.beanTreeNode.setMinPoolSize(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MinPoolSize", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getMaxIdleTime() {
      return this.beanTreeNode.getMaxIdleTime();
   }

   public void setMaxIdleTime(int var1) {
      this.beanTreeNode.setMaxIdleTime(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MaxIdleTime", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getMaxStatements() {
      return this.beanTreeNode.getMaxStatements();
   }

   public void setMaxStatements(int var1) {
      this.beanTreeNode.setMaxStatements(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MaxStatements", (Object)null, (Object)null));
      this.setModified(true);
   }
}
