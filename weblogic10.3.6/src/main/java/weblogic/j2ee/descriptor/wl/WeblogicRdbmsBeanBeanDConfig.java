package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class WeblogicRdbmsBeanBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private WeblogicRdbmsBeanBean beanTreeNode;

   public WeblogicRdbmsBeanBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WeblogicRdbmsBeanBean)var2;
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
      return this.getEjbName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setEjbName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("EjbName: ");
      var1.append(this.beanTreeNode.getEjbName());
      var1.append("\n");
      var1.append("DataSourceJNDIName: ");
      var1.append(this.beanTreeNode.getDataSourceJNDIName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getEjbName() {
      return this.beanTreeNode.getEjbName();
   }

   public void setEjbName(String var1) {
      this.beanTreeNode.setEjbName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "EjbName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getDataSourceJNDIName() {
      return this.beanTreeNode.getDataSourceJNDIName();
   }

   public void setDataSourceJNDIName(String var1) {
      this.beanTreeNode.setDataSourceJNDIName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DataSourceJNDIName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public UnknownPrimaryKeyFieldBean getUnknownPrimaryKeyField() {
      return this.beanTreeNode.getUnknownPrimaryKeyField();
   }

   public TableMapBean[] getTableMaps() {
      return this.beanTreeNode.getTableMaps();
   }

   public FieldGroupBean[] getFieldGroups() {
      return this.beanTreeNode.getFieldGroups();
   }

   public RelationshipCachingBean[] getRelationshipCachings() {
      return this.beanTreeNode.getRelationshipCachings();
   }

   public SqlShapeBean[] getSqlShapes() {
      return this.beanTreeNode.getSqlShapes();
   }

   public WeblogicQueryBean[] getWeblogicQueries() {
      return this.beanTreeNode.getWeblogicQueries();
   }

   public String getDelayDatabaseInsertUntil() {
      return this.beanTreeNode.getDelayDatabaseInsertUntil();
   }

   public void setDelayDatabaseInsertUntil(String var1) {
      this.beanTreeNode.setDelayDatabaseInsertUntil(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DelayDatabaseInsertUntil", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isUseSelectForUpdate() {
      return this.beanTreeNode.isUseSelectForUpdate();
   }

   public void setUseSelectForUpdate(boolean var1) {
      this.beanTreeNode.setUseSelectForUpdate(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "UseSelectForUpdate", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getLockOrder() {
      return this.beanTreeNode.getLockOrder();
   }

   public void setLockOrder(int var1) {
      this.beanTreeNode.setLockOrder(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LockOrder", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getInstanceLockOrder() {
      return this.beanTreeNode.getInstanceLockOrder();
   }

   public void setInstanceLockOrder(String var1) {
      this.beanTreeNode.setInstanceLockOrder(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "InstanceLockOrder", (Object)null, (Object)null));
      this.setModified(true);
   }

   public AutomaticKeyGenerationBean getAutomaticKeyGeneration() {
      return this.beanTreeNode.getAutomaticKeyGeneration();
   }

   public boolean isCheckExistsOnMethod() {
      return this.beanTreeNode.isCheckExistsOnMethod();
   }

   public void setCheckExistsOnMethod(boolean var1) {
      this.beanTreeNode.setCheckExistsOnMethod(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CheckExistsOnMethod", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getId() {
      return this.beanTreeNode.getId();
   }

   public void setId(String var1) {
      this.beanTreeNode.setId(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Id", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isClusterInvalidationDisabled() {
      return this.beanTreeNode.isClusterInvalidationDisabled();
   }

   public void setClusterInvalidationDisabled(boolean var1) {
      this.beanTreeNode.setClusterInvalidationDisabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ClusterInvalidationDisabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isUseInnerJoin() {
      return this.beanTreeNode.isUseInnerJoin();
   }

   public void setUseInnerJoin(boolean var1) {
      this.beanTreeNode.setUseInnerJoin(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "UseInnerJoin", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getCategoryCmpField() {
      return this.beanTreeNode.getCategoryCmpField();
   }

   public void setCategoryCmpField(String var1) {
      this.beanTreeNode.setCategoryCmpField(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CategoryCmpField", (Object)null, (Object)null));
      this.setModified(true);
   }
}
