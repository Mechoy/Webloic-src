package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class TableBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private TableBean beanTreeNode;

   public TableBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (TableBean)var2;
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

   public String[] getDbmsColumns() {
      return this.beanTreeNode.getDbmsColumns();
   }

   public void setDbmsColumns(String[] var1) {
      this.beanTreeNode.setDbmsColumns(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DbmsColumns", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getEjbRelationshipRoleName() {
      return this.beanTreeNode.getEjbRelationshipRoleName();
   }

   public void setEjbRelationshipRoleName(String var1) {
      this.beanTreeNode.setEjbRelationshipRoleName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "EjbRelationshipRoleName", (Object)null, (Object)null));
      this.setModified(true);
   }
}
