package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class RelationshipRoleMapBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private RelationshipRoleMapBean beanTreeNode;

   public RelationshipRoleMapBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (RelationshipRoleMapBean)var2;
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

   public String getForeignKeyTable() {
      return this.beanTreeNode.getForeignKeyTable();
   }

   public void setForeignKeyTable(String var1) {
      this.beanTreeNode.setForeignKeyTable(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ForeignKeyTable", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getPrimaryKeyTable() {
      return this.beanTreeNode.getPrimaryKeyTable();
   }

   public void setPrimaryKeyTable(String var1) {
      this.beanTreeNode.setPrimaryKeyTable(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PrimaryKeyTable", (Object)null, (Object)null));
      this.setModified(true);
   }

   public ColumnMapBean[] getColumnMaps() {
      return this.beanTreeNode.getColumnMaps();
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
