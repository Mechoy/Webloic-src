package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import javax.enterprise.deploy.spi.exceptions.InvalidModuleException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.WebLogicDeploymentConfiguration;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.deploy.api.spi.config.BasicDConfigBeanRoot;
import weblogic.deploy.api.spi.config.DescriptorParser;
import weblogic.deploy.api.spi.config.DescriptorSupport;
import weblogic.descriptor.DescriptorBean;

public class WeblogicEjbJarBeanDConfig extends BasicDConfigBeanRoot {
   private static final boolean debug = Debug.isDebug("config");
   private WeblogicEjbJarBean beanTreeNode;

   public WeblogicEjbJarBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WeblogicEjbJarBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
   }

   public WeblogicEjbJarBeanDConfig(DDBeanRoot var1, WebLogicDeploymentConfiguration var2, DescriptorBean var3, String var4, DescriptorSupport var5) throws InvalidModuleException, IOException {
      super(var1, var2, var4, var5);
      this.beanTree = var3;
      this.beanTreeNode = (WeblogicEjbJarBean)var3;

      try {
         this.initXpaths();
         this.customInit();
      } catch (ConfigurationException var7) {
         throw new InvalidModuleException(var7.toString());
      }
   }

   public WeblogicEjbJarBeanDConfig(DDBeanRoot var1, WebLogicDeploymentConfiguration var2, String var3, DescriptorSupport var4) throws InvalidModuleException, IOException {
      super(var1, var2, var3, var4);

      try {
         this.initXpaths();
         if (debug) {
            Debug.say("Creating new root object");
         }

         this.beanTree = DescriptorParser.getWLSEditableDescriptorBean(WeblogicEjbJarBean.class);
         this.beanTreeNode = (WeblogicEjbJarBean)this.beanTree;
         this.customInit();
      } catch (ConfigurationException var6) {
         throw new InvalidModuleException(var6.toString());
      }
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

   public WeblogicEnterpriseBeanBean[] getWeblogicEnterpriseBeans() {
      return this.beanTreeNode.getWeblogicEnterpriseBeans();
   }

   public SecurityRoleAssignmentBean[] getSecurityRoleAssignments() {
      return this.beanTreeNode.getSecurityRoleAssignments();
   }

   public RunAsRoleAssignmentBean[] getRunAsRoleAssignments() {
      return this.beanTreeNode.getRunAsRoleAssignments();
   }

   public SecurityPermissionBean getSecurityPermission() {
      return this.beanTreeNode.getSecurityPermission();
   }

   public TransactionIsolationBean[] getTransactionIsolations() {
      return this.beanTreeNode.getTransactionIsolations();
   }

   public MessageDestinationDescriptorBean[] getMessageDestinationDescriptors() {
      return this.beanTreeNode.getMessageDestinationDescriptors();
   }

   public IdempotentMethodsBean getIdempotentMethods() {
      return this.beanTreeNode.getIdempotentMethods();
   }

   public RetryMethodsOnRollbackBean[] getRetryMethodsOnRollbacks() {
      return this.beanTreeNode.getRetryMethodsOnRollbacks();
   }

   public boolean isEnableBeanClassRedeploy() {
      return this.beanTreeNode.isEnableBeanClassRedeploy();
   }

   public void setEnableBeanClassRedeploy(boolean var1) {
      this.beanTreeNode.setEnableBeanClassRedeploy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "EnableBeanClassRedeploy", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getTimerImplementation() {
      return this.beanTreeNode.getTimerImplementation();
   }

   public void setTimerImplementation(String var1) {
      this.beanTreeNode.setTimerImplementation(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TimerImplementation", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String[] getDisableWarnings() {
      return this.beanTreeNode.getDisableWarnings();
   }

   public void setDisableWarnings(String[] var1) {
      this.beanTreeNode.setDisableWarnings(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DisableWarnings", (Object)null, (Object)null));
      this.setModified(true);
   }

   public WorkManagerBean[] getWorkManagers() {
      return this.beanTreeNode.getWorkManagers();
   }

   public String getComponentFactoryClassName() {
      return this.beanTreeNode.getComponentFactoryClassName();
   }

   public void setComponentFactoryClassName(String var1) {
      this.beanTreeNode.setComponentFactoryClassName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ComponentFactoryClassName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public WeblogicCompatibilityBean getWeblogicCompatibility() {
      return this.beanTreeNode.getWeblogicCompatibility();
   }

   public String getId() {
      return this.beanTreeNode.getId();
   }

   public void setId(String var1) {
      this.beanTreeNode.setId(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Id", (Object)null, (Object)null));
      this.setModified(true);
   }

   public CoherenceClusterRefBean getCoherenceClusterRef() {
      return this.beanTreeNode.getCoherenceClusterRef();
   }

   public String getVersion() {
      return this.beanTreeNode.getVersion();
   }

   public void setVersion(String var1) {
      this.beanTreeNode.setVersion(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Version", (Object)null, (Object)null));
      this.setModified(true);
   }

   public DConfigBean[] getSecondaryDescriptors() {
      return super.getSecondaryDescriptors();
   }
}
