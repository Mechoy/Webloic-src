package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import weblogic.deploy.api.spi.config.templates.ConfigTemplate;
import weblogic.descriptor.DescriptorBean;

public class WeblogicApplicationClientBeanDConfig extends BasicDConfigBeanRoot {
   private static final boolean debug = Debug.isDebug("config");
   private WeblogicApplicationClientBean beanTreeNode;
   private List resourceDescriptionsDConfig = new ArrayList();
   private List resourceEnvDescriptionsDConfig = new ArrayList();
   private List ejbReferenceDescriptionsDConfig = new ArrayList();

   public WeblogicApplicationClientBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WeblogicApplicationClientBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
   }

   public WeblogicApplicationClientBeanDConfig(DDBeanRoot var1, WebLogicDeploymentConfiguration var2, DescriptorBean var3, String var4, DescriptorSupport var5) throws InvalidModuleException, IOException {
      super(var1, var2, var4, var5);
      this.beanTree = var3;
      this.beanTreeNode = (WeblogicApplicationClientBean)var3;

      try {
         this.initXpaths();
         this.customInit();
      } catch (ConfigurationException var7) {
         throw new InvalidModuleException(var7.toString());
      }
   }

   public WeblogicApplicationClientBeanDConfig(DDBeanRoot var1, WebLogicDeploymentConfiguration var2, String var3, DescriptorSupport var4) throws InvalidModuleException, IOException {
      super(var1, var2, var3, var4);

      try {
         this.initXpaths();
         if (debug) {
            Debug.say("Creating new root object");
         }

         this.beanTree = DescriptorParser.getWLSEditableDescriptorBean(WeblogicApplicationClientBean.class);
         this.beanTreeNode = (WeblogicApplicationClientBean)this.beanTree;
         this.customInit();
      } catch (ConfigurationException var6) {
         throw new InvalidModuleException(var6.toString());
      }
   }

   private void initXpaths() throws ConfigurationException {
      ArrayList var1 = new ArrayList();
      var1.add(this.getParentXpath(this.applyNamespace("application-client/resource-ref/res-ref-name")));
      var1.add(this.getParentXpath(this.applyNamespace("application-client/resource-env-ref/resource-env-ref-name")));
      var1.add(this.getParentXpath(this.applyNamespace("application-client/ejb-ref/ejb-ref-name")));
      this.xpaths = (String[])((String[])var1.toArray(new String[0]));
   }

   private void customInit() throws ConfigurationException {
   }

   public DConfigBean createDConfigBean(DDBean var1, DConfigBean var2) throws ConfigurationException {
      if (!ConfigTemplate.requireEjbRefDConfig(var1, var2)) {
         return null;
      } else {
         if (debug) {
            Debug.say("Creating child DCB for <" + var1.getXpath() + ">");
         }

         boolean var3 = false;
         Object var4 = null;
         String var5 = var1.getXpath();
         int var8 = 0;
         String var6;
         String var7;
         int var11;
         if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
            ResourceDescriptionBean var9 = null;
            ResourceDescriptionBean[] var10 = this.beanTreeNode.getResourceDescriptions();
            if (var10 == null) {
               this.beanTreeNode.createResourceDescription();
               var10 = this.beanTreeNode.getResourceDescriptions();
            }

            var7 = this.lastElementOf(this.applyNamespace("application-client/resource-ref/res-ref-name"));
            this.setKeyName(var7);
            var6 = this.getDDKey(var1, var7);
            if (debug) {
               Debug.say("Using keyName: " + var7 + ", key: " + var6);
            }

            for(var11 = 0; var11 < var10.length; ++var11) {
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
            if (!((BasicDConfigBean)var4).hasCustomInit()) {
               ((BasicDConfigBean)var4).setParentPropertyName("ResourceDescriptions");
            }

            if (debug) {
               Debug.say("dcb dump: " + ((BasicDConfigBean)var4).toString());
            }

            this.resourceDescriptionsDConfig.add(var4);
         } else if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
            ResourceEnvDescriptionBean var12 = null;
            ResourceEnvDescriptionBean[] var13 = this.beanTreeNode.getResourceEnvDescriptions();
            if (var13 == null) {
               this.beanTreeNode.createResourceEnvDescription();
               var13 = this.beanTreeNode.getResourceEnvDescriptions();
            }

            var7 = this.lastElementOf(this.applyNamespace("application-client/resource-env-ref/resource-env-ref-name"));
            this.setKeyName(var7);
            var6 = this.getDDKey(var1, var7);
            if (debug) {
               Debug.say("Using keyName: " + var7 + ", key: " + var6);
            }

            for(var11 = 0; var11 < var13.length; ++var11) {
               var12 = var13[var11];
               if (this.isMatch((DescriptorBean)var12, var1, var6)) {
                  break;
               }

               var12 = null;
            }

            if (var12 == null) {
               if (debug) {
                  Debug.say("creating new dcb element");
               }

               var12 = this.beanTreeNode.createResourceEnvDescription();
               var3 = true;
            }

            var4 = new ResourceEnvDescriptionBeanDConfig(var1, (DescriptorBean)var12, var2);
            ((ResourceEnvDescriptionBeanDConfig)var4).initKeyPropertyValue(var6);
            if (!((BasicDConfigBean)var4).hasCustomInit()) {
               ((BasicDConfigBean)var4).setParentPropertyName("ResourceEnvDescriptions");
            }

            if (debug) {
               Debug.say("dcb dump: " + ((BasicDConfigBean)var4).toString());
            }

            this.resourceEnvDescriptionsDConfig.add(var4);
         } else if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
            EjbReferenceDescriptionBean var15 = null;
            EjbReferenceDescriptionBean[] var14 = this.beanTreeNode.getEjbReferenceDescriptions();
            if (var14 == null) {
               this.beanTreeNode.createEjbReferenceDescription();
               var14 = this.beanTreeNode.getEjbReferenceDescriptions();
            }

            var7 = this.lastElementOf(this.applyNamespace("application-client/ejb-ref/ejb-ref-name"));
            this.setKeyName(var7);
            var6 = this.getDDKey(var1, var7);
            if (debug) {
               Debug.say("Using keyName: " + var7 + ", key: " + var6);
            }

            for(var11 = 0; var11 < var14.length; ++var11) {
               var15 = var14[var11];
               if (this.isMatch((DescriptorBean)var15, var1, var6)) {
                  break;
               }

               var15 = null;
            }

            if (var15 == null) {
               if (debug) {
                  Debug.say("creating new dcb element");
               }

               var15 = this.beanTreeNode.createEjbReferenceDescription();
               var3 = true;
            }

            var4 = new EjbReferenceDescriptionBeanDConfig(var1, (DescriptorBean)var15, var2);
            ((EjbReferenceDescriptionBeanDConfig)var4).initKeyPropertyValue(var6);
            if (!((BasicDConfigBean)var4).hasCustomInit()) {
               ((BasicDConfigBean)var4).setParentPropertyName("EjbReferenceDescriptions");
            }

            if (debug) {
               Debug.say("dcb dump: " + ((BasicDConfigBean)var4).toString());
            }

            this.ejbReferenceDescriptionsDConfig.add(var4);
         } else if (debug) {
            Debug.say("Ignoring " + var1.getXpath());

            for(int var16 = 0; var16 < this.xpaths.length; ++var16) {
               Debug.say("xpaths[" + var16 + "]=" + this.xpaths[var16]);
            }
         }

         if (var4 != null) {
            this.addDConfigBean((DConfigBean)var4);
            if (var3) {
               ((BasicDConfigBean)var4).setModified(true);

               Object var17;
               for(var17 = var4; ((BasicDConfigBean)var17).getParent() != null; var17 = ((BasicDConfigBean)var17).getParent()) {
               }

               ((BasicDConfigBeanRoot)var17).registerAsListener(((BasicDConfigBean)var4).getDescriptorBean());
            }

            this.processDCB((BasicDConfigBean)var4, var3);
         }

         return (DConfigBean)var4;
      }
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

   public ResourceDescriptionBeanDConfig[] getResourceDescriptions() {
      return (ResourceDescriptionBeanDConfig[])((ResourceDescriptionBeanDConfig[])this.resourceDescriptionsDConfig.toArray(new ResourceDescriptionBeanDConfig[0]));
   }

   void addResourceDescriptionBean(ResourceDescriptionBeanDConfig var1) {
      this.addToList(this.resourceDescriptionsDConfig, "ResourceDescriptionBean", var1);
   }

   void removeResourceDescriptionBean(ResourceDescriptionBeanDConfig var1) {
      this.removeFromList(this.resourceDescriptionsDConfig, "ResourceDescriptionBean", var1);
   }

   public ResourceEnvDescriptionBeanDConfig[] getResourceEnvDescriptions() {
      return (ResourceEnvDescriptionBeanDConfig[])((ResourceEnvDescriptionBeanDConfig[])this.resourceEnvDescriptionsDConfig.toArray(new ResourceEnvDescriptionBeanDConfig[0]));
   }

   void addResourceEnvDescriptionBean(ResourceEnvDescriptionBeanDConfig var1) {
      this.addToList(this.resourceEnvDescriptionsDConfig, "ResourceEnvDescriptionBean", var1);
   }

   void removeResourceEnvDescriptionBean(ResourceEnvDescriptionBeanDConfig var1) {
      this.removeFromList(this.resourceEnvDescriptionsDConfig, "ResourceEnvDescriptionBean", var1);
   }

   public EjbReferenceDescriptionBeanDConfig[] getEjbReferenceDescriptions() {
      return (EjbReferenceDescriptionBeanDConfig[])((EjbReferenceDescriptionBeanDConfig[])this.ejbReferenceDescriptionsDConfig.toArray(new EjbReferenceDescriptionBeanDConfig[0]));
   }

   void addEjbReferenceDescriptionBean(EjbReferenceDescriptionBeanDConfig var1) {
      this.addToList(this.ejbReferenceDescriptionsDConfig, "EjbReferenceDescriptionBean", var1);
   }

   void removeEjbReferenceDescriptionBean(EjbReferenceDescriptionBeanDConfig var1) {
      this.removeFromList(this.ejbReferenceDescriptionsDConfig, "EjbReferenceDescriptionBean", var1);
   }

   public ServiceReferenceDescriptionBean[] getServiceReferenceDescriptions() {
      return this.beanTreeNode.getServiceReferenceDescriptions();
   }

   public MessageDestinationDescriptorBean[] getMessageDestinationDescriptors() {
      return this.beanTreeNode.getMessageDestinationDescriptors();
   }

   public String getId() {
      return this.beanTreeNode.getId();
   }

   public void setId(String var1) {
      this.beanTreeNode.setId(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Id", (Object)null, (Object)null));
      this.setModified(true);
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
