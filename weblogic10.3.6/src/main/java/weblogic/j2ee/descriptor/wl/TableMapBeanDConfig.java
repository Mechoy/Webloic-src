package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class TableMapBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private TableMapBean beanTreeNode;

   public TableMapBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (TableMapBean)var2;
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
      return this.getTableName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setTableName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("TableName: ");
      var1.append(this.beanTreeNode.getTableName());
      var1.append("\n");
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

   public FieldMapBean[] getFieldMaps() {
      return this.beanTreeNode.getFieldMaps();
   }

   public String getVerifyRows() {
      return this.beanTreeNode.getVerifyRows();
   }

   public void setVerifyRows(String var1) {
      this.beanTreeNode.setVerifyRows(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "VerifyRows", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getVerifyColumns() {
      return this.beanTreeNode.getVerifyColumns();
   }

   public void setVerifyColumns(String var1) {
      this.beanTreeNode.setVerifyColumns(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "VerifyColumns", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getOptimisticColumn() {
      return this.beanTreeNode.getOptimisticColumn();
   }

   public void setOptimisticColumn(String var1) {
      this.beanTreeNode.setOptimisticColumn(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "OptimisticColumn", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isTriggerUpdatesOptimisticColumn() {
      return this.beanTreeNode.isTriggerUpdatesOptimisticColumn();
   }

   public void setTriggerUpdatesOptimisticColumn(boolean var1) {
      this.beanTreeNode.setTriggerUpdatesOptimisticColumn(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TriggerUpdatesOptimisticColumn", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getVersionColumnInitialValue() {
      return this.beanTreeNode.getVersionColumnInitialValue();
   }

   public void setVersionColumnInitialValue(int var1) {
      this.beanTreeNode.setVersionColumnInitialValue(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "VersionColumnInitialValue", (Object)null, (Object)null));
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
