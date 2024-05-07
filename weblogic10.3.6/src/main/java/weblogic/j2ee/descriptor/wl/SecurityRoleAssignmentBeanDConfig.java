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

public class SecurityRoleAssignmentBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private SecurityRoleAssignmentBean beanTreeNode;

   public SecurityRoleAssignmentBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (SecurityRoleAssignmentBean)var2;
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
      return this.getRoleName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setRoleName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("RoleName: ");
      var1.append(this.beanTreeNode.getRoleName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getRoleName() {
      return this.beanTreeNode.getRoleName();
   }

   public void setRoleName(String var1) {
      this.beanTreeNode.setRoleName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RoleName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String[] getPrincipalNames() {
      return this.beanTreeNode.getPrincipalNames();
   }

   public void setPrincipalNames(String[] var1) {
      this.beanTreeNode.setPrincipalNames(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PrincipalNames", (Object)null, (Object)null));
      this.setModified(true);
   }

   public EmptyBean getExternallyDefined() {
      return this.beanTreeNode.getExternallyDefined();
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
