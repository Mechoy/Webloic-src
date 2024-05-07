package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class SqlShapeBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private SqlShapeBean beanTreeNode;

   public SqlShapeBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (SqlShapeBean)var2;
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

   public String getDescription() {
      return this.beanTreeNode.getDescription();
   }

   public void setDescription(String var1) {
      this.beanTreeNode.setDescription(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Description", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getSqlShapeName() {
      return this.beanTreeNode.getSqlShapeName();
   }

   public void setSqlShapeName(String var1) {
      this.beanTreeNode.setSqlShapeName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SqlShapeName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public TableBean[] getTables() {
      return this.beanTreeNode.getTables();
   }

   public int getPassThroughColumns() {
      return this.beanTreeNode.getPassThroughColumns();
   }

   public void setPassThroughColumns(int var1) {
      this.beanTreeNode.setPassThroughColumns(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PassThroughColumns", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String[] getEjbRelationNames() {
      return this.beanTreeNode.getEjbRelationNames();
   }

   public void setEjbRelationNames(String[] var1) {
      this.beanTreeNode.setEjbRelationNames(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "EjbRelationNames", (Object)null, (Object)null));
      this.setModified(true);
   }
}
