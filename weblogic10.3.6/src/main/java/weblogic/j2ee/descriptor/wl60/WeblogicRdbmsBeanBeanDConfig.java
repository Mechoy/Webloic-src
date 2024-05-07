package weblogic.j2ee.descriptor.wl60;

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
      var1.append("PoolName: ");
      var1.append(this.beanTreeNode.getPoolName());
      var1.append("\n");
      var1.append("DataSourceJndiName: ");
      var1.append(this.beanTreeNode.getDataSourceJndiName());
      var1.append("\n");
      var1.append("TableName: ");
      var1.append(this.beanTreeNode.getTableName());
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

   public String getPoolName() {
      return this.beanTreeNode.getPoolName();
   }

   public void setPoolName(String var1) {
      this.beanTreeNode.setPoolName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PoolName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getDataSourceJndiName() {
      return this.beanTreeNode.getDataSourceJndiName();
   }

   public void setDataSourceJndiName(String var1) {
      this.beanTreeNode.setDataSourceJndiName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DataSourceJndiName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getTableName() {
      return this.beanTreeNode.getTableName();
   }

   public void setTableName(String var1) {
      this.beanTreeNode.setTableName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TableName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public FieldMapBean[] getFieldMaps() {
      return this.beanTreeNode.getFieldMaps();
   }

   public FinderBean[] getFinders() {
      return this.beanTreeNode.getFinders();
   }

   public boolean isEnableTunedUpdates() {
      return this.beanTreeNode.isEnableTunedUpdates();
   }

   public void setEnableTunedUpdates(boolean var1) {
      this.beanTreeNode.setEnableTunedUpdates(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "EnableTunedUpdates", (Object)null, (Object)null));
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
}
