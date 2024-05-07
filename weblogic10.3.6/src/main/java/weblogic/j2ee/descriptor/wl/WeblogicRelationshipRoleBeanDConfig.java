package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.EmptyBean;

public class WeblogicRelationshipRoleBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private WeblogicRelationshipRoleBean beanTreeNode;

   public WeblogicRelationshipRoleBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WeblogicRelationshipRoleBean)var2;
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
      return this.getRelationshipRoleName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setRelationshipRoleName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("RelationshipRoleName: ");
      var1.append(this.beanTreeNode.getRelationshipRoleName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getRelationshipRoleName() {
      return this.beanTreeNode.getRelationshipRoleName();
   }

   public void setRelationshipRoleName(String var1) {
      this.beanTreeNode.setRelationshipRoleName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RelationshipRoleName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getGroupName() {
      return this.beanTreeNode.getGroupName();
   }

   public void setGroupName(String var1) {
      this.beanTreeNode.setGroupName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "GroupName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public RelationshipRoleMapBean getRelationshipRoleMap() {
      return this.beanTreeNode.getRelationshipRoleMap();
   }

   public EmptyBean getDbCascadeDelete() {
      return this.beanTreeNode.getDbCascadeDelete();
   }

   public boolean getEnableQueryCaching() {
      return this.beanTreeNode.getEnableQueryCaching();
   }

   public void setEnableQueryCaching(boolean var1) {
      this.beanTreeNode.setEnableQueryCaching(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "EnableQueryCaching", (Object)null, (Object)null));
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
