package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class WeblogicRdbmsRelationBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private WeblogicRdbmsRelationBean beanTreeNode;

   public WeblogicRdbmsRelationBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WeblogicRdbmsRelationBean)var2;
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
      return this.getRelationName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setRelationName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("RelationName: ");
      var1.append(this.beanTreeNode.getRelationName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getRelationName() {
      return this.beanTreeNode.getRelationName();
   }

   public void setRelationName(String var1) {
      this.beanTreeNode.setRelationName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RelationName", (Object)null, (Object)null));
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

   public WeblogicRelationshipRoleBean[] getWeblogicRelationshipRoles() {
      return this.beanTreeNode.getWeblogicRelationshipRoles();
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
