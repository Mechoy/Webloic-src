package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.deploy.api.spi.config.BasicDConfigBeanRoot;
import weblogic.deploy.api.spi.config.templates.ConfigTemplate;
import weblogic.descriptor.DescriptorBean;

public class WeblogicEnterpriseBeanBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private WeblogicEnterpriseBeanBean beanTreeNode;
   private List resourceDescriptionsDConfig = new ArrayList();

   public WeblogicEnterpriseBeanBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WeblogicEnterpriseBeanBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
      ConfigTemplate.configureEntityDescriptor(this);
      ConfigTemplate.configureMessageDrivenDescriptor(this);
   }

   private void initXpaths() throws ConfigurationException {
      ArrayList var1 = new ArrayList();
      var1.add(this.getParentXpath(this.applyNamespace("resource-ref/res-ref-name")));
      this.xpaths = (String[])((String[])var1.toArray(new String[0]));
   }

   private void customInit() throws ConfigurationException {
   }

   public DConfigBean createDConfigBean(DDBean var1, DConfigBean var2) throws ConfigurationException {
      if (debug) {
         Debug.say("Creating child DCB for <" + var1.getXpath() + ">");
      }

      boolean var3 = false;
      ResourceDescriptionBeanDConfig var4 = null;
      String var5 = var1.getXpath();
      int var8 = 0;
      if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
         ResourceDescriptionBean var9 = null;
         ResourceDescriptionBean[] var10 = this.beanTreeNode.getResourceDescriptions();
         if (var10 == null) {
            this.beanTreeNode.createResourceDescription();
            var10 = this.beanTreeNode.getResourceDescriptions();
         }

         String var7 = this.lastElementOf(this.applyNamespace("resource-ref/res-ref-name"));
         this.setKeyName(var7);
         String var6 = this.getDDKey(var1, var7);
         if (debug) {
            Debug.say("Using keyName: " + var7 + ", key: " + var6);
         }

         for(int var11 = 0; var11 < var10.length; ++var11) {
            var9 = var10[var11];
            if (this.isMatch((DescriptorBean)var9, var1, var6)) {
               break;
            }

            var9 = null;
         }

         if (var9 == null) {
            if (debug) {
               Debug.say("creating new dcb element");
            }

            var9 = this.beanTreeNode.createResourceDescription();
            var3 = true;
         }

         var4 = new ResourceDescriptionBeanDConfig(var1, (DescriptorBean)var9, var2);
         ((ResourceDescriptionBeanDConfig)var4).initKeyPropertyValue(var6);
         if (!var4.hasCustomInit()) {
            var4.setParentPropertyName("ResourceDescriptions");
         }

         if (debug) {
            Debug.say("dcb dump: " + var4.toString());
         }

         this.resourceDescriptionsDConfig.add(var4);
      } else if (debug) {
         Debug.say("Ignoring " + var1.getXpath());

         for(int var12 = 0; var12 < this.xpaths.length; ++var12) {
            Debug.say("xpaths[" + var12 + "]=" + this.xpaths[var12]);
         }
      }

      if (var4 != null) {
         this.addDConfigBean(var4);
         if (var3) {
            var4.setModified(true);

            Object var13;
            for(var13 = var4; ((BasicDConfigBean)var13).getParent() != null; var13 = ((BasicDConfigBean)var13).getParent()) {
            }

            ((BasicDConfigBeanRoot)var13).registerAsListener(var4.getDescriptorBean());
         }

         this.processDCB(var4, var3);
      }

      return var4;
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
      var1.append("JNDIName: ");
      var1.append(this.beanTreeNode.getJNDIName());
      var1.append("\n");
      var1.append("LocalJNDIName: ");
      var1.append(this.beanTreeNode.getLocalJNDIName());
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

   public EntityDescriptorBean getEntityDescriptor() {
      return this.beanTreeNode.getEntityDescriptor();
   }

   public StatelessSessionDescriptorBean getStatelessSessionDescriptor() {
      return this.beanTreeNode.getStatelessSessionDescriptor();
   }

   public StatefulSessionDescriptorBean getStatefulSessionDescriptor() {
      return this.beanTreeNode.getStatefulSessionDescriptor();
   }

   public MessageDrivenDescriptorBean getMessageDrivenDescriptor() {
      return this.beanTreeNode.getMessageDrivenDescriptor();
   }

   public TransactionDescriptorBean getTransactionDescriptor() {
      return this.beanTreeNode.getTransactionDescriptor();
   }

   public IiopSecurityDescriptorBean getIiopSecurityDescriptor() {
      return this.beanTreeNode.getIiopSecurityDescriptor();
   }

   public ResourceDescriptionBeanDConfig[] getResourceDescriptions() {
      return (ResourceDescriptionBeanDConfig[])((ResourceDescriptionBeanDConfig[])this.resourceDescriptionsDConfig.toArray(new ResourceDescriptionBeanDConfig[0]));
   }

   void addResourceDescriptionBean(ResourceDescriptionBeanDConfig var1) {
      this.addToList(this.resourceDescriptionsDConfig, "ResourceDescriptionBean", var1);
   }

   void removeResourceDescriptionBean(ResourceDescriptionBeanDConfig var1) {
      this.removeFromList(this.resourceDescriptionsDConfig, "ResourceDescriptionBean", var1);
   }

   public ResourceEnvDescriptionBean[] getResourceEnvDescriptions() {
      return this.beanTreeNode.getResourceEnvDescriptions();
   }

   public EjbReferenceDescriptionBean[] getEjbReferenceDescriptions() {
      return this.beanTreeNode.getEjbReferenceDescriptions();
   }

   public ServiceReferenceDescriptionBean[] getServiceReferenceDescriptions() {
      return this.beanTreeNode.getServiceReferenceDescriptions();
   }

   public boolean isEnableCallByReference() {
      return this.beanTreeNode.isEnableCallByReference();
   }

   public void setEnableCallByReference(boolean var1) {
      this.beanTreeNode.setEnableCallByReference(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "EnableCallByReference", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getNetworkAccessPoint() {
      return this.beanTreeNode.getNetworkAccessPoint();
   }

   public void setNetworkAccessPoint(String var1) {
      this.beanTreeNode.setNetworkAccessPoint(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "NetworkAccessPoint", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isClientsOnSameServer() {
      return this.beanTreeNode.isClientsOnSameServer();
   }

   public void setClientsOnSameServer(boolean var1) {
      this.beanTreeNode.setClientsOnSameServer(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ClientsOnSameServer", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getRunAsPrincipalName() {
      return this.beanTreeNode.getRunAsPrincipalName();
   }

   public void setRunAsPrincipalName(String var1) {
      this.beanTreeNode.setRunAsPrincipalName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RunAsPrincipalName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getCreateAsPrincipalName() {
      return this.beanTreeNode.getCreateAsPrincipalName();
   }

   public void setCreateAsPrincipalName(String var1) {
      this.beanTreeNode.setCreateAsPrincipalName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CreateAsPrincipalName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getRemoveAsPrincipalName() {
      return this.beanTreeNode.getRemoveAsPrincipalName();
   }

   public void setRemoveAsPrincipalName(String var1) {
      this.beanTreeNode.setRemoveAsPrincipalName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RemoveAsPrincipalName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getPassivateAsPrincipalName() {
      return this.beanTreeNode.getPassivateAsPrincipalName();
   }

   public void setPassivateAsPrincipalName(String var1) {
      this.beanTreeNode.setPassivateAsPrincipalName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PassivateAsPrincipalName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getJNDIName() {
      return this.beanTreeNode.getJNDIName();
   }

   public void setJNDIName(String var1) {
      this.beanTreeNode.setJNDIName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "JNDIName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getLocalJNDIName() {
      return this.beanTreeNode.getLocalJNDIName();
   }

   public void setLocalJNDIName(String var1) {
      this.beanTreeNode.setLocalJNDIName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LocalJNDIName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getDispatchPolicy() {
      return this.beanTreeNode.getDispatchPolicy();
   }

   public void setDispatchPolicy(String var1) {
      this.beanTreeNode.setDispatchPolicy(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DispatchPolicy", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getRemoteClientTimeout() {
      return this.beanTreeNode.getRemoteClientTimeout();
   }

   public void setRemoteClientTimeout(int var1) {
      this.beanTreeNode.setRemoteClientTimeout(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RemoteClientTimeout", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isStickToFirstServer() {
      return this.beanTreeNode.isStickToFirstServer();
   }

   public void setStickToFirstServer(boolean var1) {
      this.beanTreeNode.setStickToFirstServer(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "StickToFirstServer", (Object)null, (Object)null));
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
