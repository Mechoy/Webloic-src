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

public class WeblogicWebAppBeanDConfig extends BasicDConfigBeanRoot {
   private static final boolean debug = Debug.isDebug("config");
   private WeblogicWebAppBean beanTreeNode;
   private List resourceDescriptionsDConfig = new ArrayList();
   private List ejbReferenceDescriptionsDConfig = new ArrayList();
   private List messageDestinationDescriptorsDConfig = new ArrayList();
   private List servletDescriptorsDConfig = new ArrayList();

   public WeblogicWebAppBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WeblogicWebAppBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
   }

   public WeblogicWebAppBeanDConfig(DDBeanRoot var1, WebLogicDeploymentConfiguration var2, DescriptorBean var3, String var4, DescriptorSupport var5) throws InvalidModuleException, IOException {
      super(var1, var2, var4, var5);
      this.beanTree = var3;
      this.beanTreeNode = (WeblogicWebAppBean)var3;

      try {
         this.initXpaths();
         this.customInit();
      } catch (ConfigurationException var7) {
         throw new InvalidModuleException(var7.toString());
      }
   }

   public WeblogicWebAppBeanDConfig(DDBeanRoot var1, WebLogicDeploymentConfiguration var2, String var3, DescriptorSupport var4) throws InvalidModuleException, IOException {
      super(var1, var2, var3, var4);

      try {
         this.initXpaths();
         if (debug) {
            Debug.say("Creating new root object");
         }

         this.beanTree = DescriptorParser.getWLSEditableDescriptorBean(WeblogicWebAppBean.class);
         this.beanTreeNode = (WeblogicWebAppBean)this.beanTree;
         this.customInit();
      } catch (ConfigurationException var6) {
         throw new InvalidModuleException(var6.toString());
      }
   }

   private void initXpaths() throws ConfigurationException {
      ArrayList var1 = new ArrayList();
      var1.add(this.getParentXpath(this.applyNamespace("web-app/resource-ref/res-ref-name")));
      var1.add(this.getParentXpath(this.applyNamespace("web-app/ejb-ref/ejb-ref-name")));
      var1.add(this.getParentXpath(this.applyNamespace("web-app/message-destination/message-destination-name")));
      var1.add(this.getParentXpath(this.applyNamespace("web-app/servlet/servlet-name")));
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

            var7 = this.lastElementOf(this.applyNamespace("web-app/resource-ref/res-ref-name"));
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
            EjbReferenceDescriptionBean var12 = null;
            EjbReferenceDescriptionBean[] var13 = this.beanTreeNode.getEjbReferenceDescriptions();
            if (var13 == null) {
               this.beanTreeNode.createEjbReferenceDescription();
               var13 = this.beanTreeNode.getEjbReferenceDescriptions();
            }

            var7 = this.lastElementOf(this.applyNamespace("web-app/ejb-ref/ejb-ref-name"));
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

               var12 = this.beanTreeNode.createEjbReferenceDescription();
               var3 = true;
            }

            var4 = new EjbReferenceDescriptionBeanDConfig(var1, (DescriptorBean)var12, var2);
            ((EjbReferenceDescriptionBeanDConfig)var4).initKeyPropertyValue(var6);
            if (!((BasicDConfigBean)var4).hasCustomInit()) {
               ((BasicDConfigBean)var4).setParentPropertyName("EjbReferenceDescriptions");
            }

            if (debug) {
               Debug.say("dcb dump: " + ((BasicDConfigBean)var4).toString());
            }

            this.ejbReferenceDescriptionsDConfig.add(var4);
         } else if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
            MessageDestinationDescriptorBean var15 = null;
            MessageDestinationDescriptorBean[] var14 = this.beanTreeNode.getMessageDestinationDescriptors();
            if (var14 == null) {
               this.beanTreeNode.createMessageDestinationDescriptor();
               var14 = this.beanTreeNode.getMessageDestinationDescriptors();
            }

            var7 = this.lastElementOf(this.applyNamespace("web-app/message-destination/message-destination-name"));
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

               var15 = this.beanTreeNode.createMessageDestinationDescriptor();
               var3 = true;
            }

            var4 = new MessageDestinationDescriptorBeanDConfig(var1, (DescriptorBean)var15, var2);
            ((MessageDestinationDescriptorBeanDConfig)var4).initKeyPropertyValue(var6);
            if (!((BasicDConfigBean)var4).hasCustomInit()) {
               ((BasicDConfigBean)var4).setParentPropertyName("MessageDestinationDescriptors");
            }

            if (debug) {
               Debug.say("dcb dump: " + ((BasicDConfigBean)var4).toString());
            }

            this.messageDestinationDescriptorsDConfig.add(var4);
         } else if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
            ServletDescriptorBean var17 = null;
            ServletDescriptorBean[] var16 = this.beanTreeNode.getServletDescriptors();
            if (var16 == null) {
               this.beanTreeNode.createServletDescriptor();
               var16 = this.beanTreeNode.getServletDescriptors();
            }

            var7 = this.lastElementOf(this.applyNamespace("web-app/servlet/servlet-name"));
            this.setKeyName(var7);
            var6 = this.getDDKey(var1, var7);
            if (debug) {
               Debug.say("Using keyName: " + var7 + ", key: " + var6);
            }

            for(var11 = 0; var11 < var16.length; ++var11) {
               var17 = var16[var11];
               if (this.isMatch((DescriptorBean)var17, var1, var6)) {
                  break;
               }

               var17 = null;
            }

            if (var17 == null) {
               if (debug) {
                  Debug.say("creating new dcb element");
               }

               var17 = this.beanTreeNode.createServletDescriptor();
               var3 = true;
            }

            var4 = new ServletDescriptorBeanDConfig(var1, (DescriptorBean)var17, var2);
            ((ServletDescriptorBeanDConfig)var4).initKeyPropertyValue(var6);
            if (!((BasicDConfigBean)var4).hasCustomInit()) {
               ((BasicDConfigBean)var4).setParentPropertyName("ServletDescriptors");
            }

            if (debug) {
               Debug.say("dcb dump: " + ((BasicDConfigBean)var4).toString());
            }

            this.servletDescriptorsDConfig.add(var4);
         } else if (debug) {
            Debug.say("Ignoring " + var1.getXpath());

            for(int var18 = 0; var18 < this.xpaths.length; ++var18) {
               Debug.say("xpaths[" + var18 + "]=" + this.xpaths[var18]);
            }
         }

         if (var4 != null) {
            this.addDConfigBean((DConfigBean)var4);
            if (var3) {
               ((BasicDConfigBean)var4).setModified(true);

               Object var19;
               for(var19 = var4; ((BasicDConfigBean)var19).getParent() != null; var19 = ((BasicDConfigBean)var19).getParent()) {
               }

               ((BasicDConfigBeanRoot)var19).registerAsListener(((BasicDConfigBean)var4).getDescriptorBean());
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

   public String[] getDescriptions() {
      return this.beanTreeNode.getDescriptions();
   }

   public void setDescriptions(String[] var1) {
      this.beanTreeNode.setDescriptions(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Descriptions", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String[] getWeblogicVersions() {
      return this.beanTreeNode.getWeblogicVersions();
   }

   public void setWeblogicVersions(String[] var1) {
      this.beanTreeNode.setWeblogicVersions(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "WeblogicVersions", (Object)null, (Object)null));
      this.setModified(true);
   }

   public SecurityRoleAssignmentBean[] getSecurityRoleAssignments() {
      return this.beanTreeNode.getSecurityRoleAssignments();
   }

   public RunAsRoleAssignmentBean[] getRunAsRoleAssignments() {
      return this.beanTreeNode.getRunAsRoleAssignments();
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

   public MessageDestinationDescriptorBeanDConfig[] getMessageDestinationDescriptors() {
      return (MessageDestinationDescriptorBeanDConfig[])((MessageDestinationDescriptorBeanDConfig[])this.messageDestinationDescriptorsDConfig.toArray(new MessageDestinationDescriptorBeanDConfig[0]));
   }

   void addMessageDestinationDescriptorBean(MessageDestinationDescriptorBeanDConfig var1) {
      this.addToList(this.messageDestinationDescriptorsDConfig, "MessageDestinationDescriptorBean", var1);
   }

   void removeMessageDestinationDescriptorBean(MessageDestinationDescriptorBeanDConfig var1) {
      this.removeFromList(this.messageDestinationDescriptorsDConfig, "MessageDestinationDescriptorBean", var1);
   }

   public SessionDescriptorBean[] getSessionDescriptors() {
      return this.beanTreeNode.getSessionDescriptors();
   }

   public JspDescriptorBean[] getJspDescriptors() {
      return this.beanTreeNode.getJspDescriptors();
   }

   public ContainerDescriptorBean[] getContainerDescriptors() {
      return this.beanTreeNode.getContainerDescriptors();
   }

   public String[] getAuthFilters() {
      return this.beanTreeNode.getAuthFilters();
   }

   public void setAuthFilters(String[] var1) {
      this.beanTreeNode.setAuthFilters(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "AuthFilters", (Object)null, (Object)null));
      this.setModified(true);
   }

   public CharsetParamsBean[] getCharsetParams() {
      return this.beanTreeNode.getCharsetParams();
   }

   public VirtualDirectoryMappingBean[] getVirtualDirectoryMappings() {
      return this.beanTreeNode.getVirtualDirectoryMappings();
   }

   public String[] getUrlMatchMaps() {
      return this.beanTreeNode.getUrlMatchMaps();
   }

   public void setUrlMatchMaps(String[] var1) {
      this.beanTreeNode.setUrlMatchMaps(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "UrlMatchMaps", (Object)null, (Object)null));
      this.setModified(true);
   }

   public SecurityPermissionBean[] getSecurityPermissions() {
      return this.beanTreeNode.getSecurityPermissions();
   }

   public String[] getContextRoots() {
      return this.beanTreeNode.getContextRoots();
   }

   public void setContextRoots(String[] var1) {
      this.beanTreeNode.setContextRoots(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ContextRoots", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String[] getWlDispatchPolicies() {
      return this.beanTreeNode.getWlDispatchPolicies();
   }

   public void setWlDispatchPolicies(String[] var1) {
      this.beanTreeNode.setWlDispatchPolicies(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "WlDispatchPolicies", (Object)null, (Object)null));
      this.setModified(true);
   }

   public ServletDescriptorBeanDConfig[] getServletDescriptors() {
      return (ServletDescriptorBeanDConfig[])((ServletDescriptorBeanDConfig[])this.servletDescriptorsDConfig.toArray(new ServletDescriptorBeanDConfig[0]));
   }

   void addServletDescriptorBean(ServletDescriptorBeanDConfig var1) {
      this.addToList(this.servletDescriptorsDConfig, "ServletDescriptorBean", var1);
   }

   void removeServletDescriptorBean(ServletDescriptorBeanDConfig var1) {
      this.removeFromList(this.servletDescriptorsDConfig, "ServletDescriptorBean", var1);
   }

   public WorkManagerBean[] getWorkManagers() {
      return this.beanTreeNode.getWorkManagers();
   }

   public String[] getComponentFactoryClassName() {
      return this.beanTreeNode.getComponentFactoryClassName();
   }

   public void setComponentFactoryClassName(String[] var1) {
      this.beanTreeNode.setComponentFactoryClassName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ComponentFactoryClassName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public LoggingBean[] getLoggings() {
      return this.beanTreeNode.getLoggings();
   }

   public LibraryRefBean[] getLibraryRefs() {
      return this.beanTreeNode.getLibraryRefs();
   }

   public FastSwapBean getFastSwap() {
      return this.beanTreeNode.getFastSwap();
   }

   public CoherenceClusterRefBean getCoherenceClusterRef() {
      return this.beanTreeNode.getCoherenceClusterRef();
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
