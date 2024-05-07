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
import weblogic.deploy.api.spi.config.templates.ConfigTemplate;
import weblogic.descriptor.DescriptorBean;

public class WeblogicConnectorBeanDConfig extends BasicDConfigBeanRoot {
   private static final boolean debug = Debug.isDebug("config");
   private WeblogicConnectorBean beanTreeNode;
   private OutboundResourceAdapterBeanDConfig outboundResourceAdapterDConfig;

   public WeblogicConnectorBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WeblogicConnectorBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
      ConfigTemplate.configureAdminObj(this);
   }

   public WeblogicConnectorBeanDConfig(DDBeanRoot var1, WebLogicDeploymentConfiguration var2, DescriptorBean var3, String var4, DescriptorSupport var5) throws InvalidModuleException, IOException {
      super(var1, var2, var4, var5);
      this.beanTree = var3;
      this.beanTreeNode = (WeblogicConnectorBean)var3;

      try {
         this.initXpaths();
         this.customInit();
      } catch (ConfigurationException var7) {
         throw new InvalidModuleException(var7.toString());
      }
   }

   public WeblogicConnectorBeanDConfig(DDBeanRoot var1, WebLogicDeploymentConfiguration var2, String var3, DescriptorSupport var4) throws InvalidModuleException, IOException {
      super(var1, var2, var3, var4);

      try {
         this.initXpaths();
         if (debug) {
            Debug.say("Creating new root object");
         }

         this.beanTree = DescriptorParser.getWLSEditableDescriptorBean(WeblogicConnectorBean.class);
         this.beanTreeNode = (WeblogicConnectorBean)this.beanTree;
         this.customInit();
         ConfigTemplate.configureAdminObj(this);
      } catch (ConfigurationException var6) {
         throw new InvalidModuleException(var6.toString());
      }
   }

   private void initXpaths() throws ConfigurationException {
      ArrayList var1 = new ArrayList();
      var1.add(this.applyNamespace("connector/resourceadapter/outbound-resourceadapter"));
      this.xpaths = (String[])((String[])var1.toArray(new String[0]));
   }

   private void customInit() throws ConfigurationException {
   }

   public DConfigBean createDConfigBean(DDBean var1, DConfigBean var2) throws ConfigurationException {
      if (debug) {
         Debug.say("Creating child DCB for <" + var1.getXpath() + ">");
      }

      boolean var3 = false;
      OutboundResourceAdapterBeanDConfig var4 = null;
      String var5 = var1.getXpath();
      int var8 = 0;
      if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
         OutboundResourceAdapterBean var9 = this.beanTreeNode.getOutboundResourceAdapter();
         if (var9 == null) {
            if (debug) {
               Debug.say("creating new dcb element");
            }

            var9 = this.beanTreeNode.createOutboundResourceAdapter();
            var3 = true;
         }

         this.outboundResourceAdapterDConfig = new OutboundResourceAdapterBeanDConfig(var1, (DescriptorBean)var9, var2);
         var4 = this.outboundResourceAdapterDConfig;
         if (!var4.hasCustomInit()) {
            var4.setParentPropertyName("OutboundResourceAdapter");
         }
      } else if (debug) {
         Debug.say("Ignoring " + var1.getXpath());

         for(int var10 = 0; var10 < this.xpaths.length; ++var10) {
            Debug.say("xpaths[" + var10 + "]=" + this.xpaths[var10]);
         }
      }

      if (var4 != null) {
         this.addDConfigBean(var4);
         if (var3) {
            var4.setModified(true);

            Object var11;
            for(var11 = var4; ((BasicDConfigBean)var11).getParent() != null; var11 = ((BasicDConfigBean)var11).getParent()) {
            }

            ((BasicDConfigBeanRoot)var11).registerAsListener(var4.getDescriptorBean());
         }

         this.processDCB(var4, var3);
      }

      return var4;
   }

   public String keyPropertyValue() {
      return null;
   }

   public void initKeyPropertyValue(String var1) {
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("JNDIName: ");
      var1.append(this.beanTreeNode.getJNDIName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getNativeLibdir() {
      return this.beanTreeNode.getNativeLibdir();
   }

   public void setNativeLibdir(String var1) {
      this.beanTreeNode.setNativeLibdir(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "NativeLibdir", (Object)null, (Object)null));
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

   public boolean isEnableAccessOutsideApp() {
      return this.beanTreeNode.isEnableAccessOutsideApp();
   }

   public void setEnableAccessOutsideApp(boolean var1) {
      this.beanTreeNode.setEnableAccessOutsideApp(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "EnableAccessOutsideApp", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isEnableGlobalAccessToClasses() {
      return this.beanTreeNode.isEnableGlobalAccessToClasses();
   }

   public void setEnableGlobalAccessToClasses(boolean var1) {
      this.beanTreeNode.setEnableGlobalAccessToClasses(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "EnableGlobalAccessToClasses", (Object)null, (Object)null));
      this.setModified(true);
   }

   public WorkManagerBean getWorkManager() {
      return this.beanTreeNode.getWorkManager();
   }

   public ResourceAdapterSecurityBean getSecurity() {
      return this.beanTreeNode.getSecurity();
   }

   public ConfigPropertiesBean getProperties() {
      return this.beanTreeNode.getProperties();
   }

   public AdminObjectsBean getAdminObjects() {
      return this.beanTreeNode.getAdminObjects();
   }

   public OutboundResourceAdapterBeanDConfig getOutboundResourceAdapter() {
      return this.outboundResourceAdapterDConfig;
   }

   public void setOutboundResourceAdapter(OutboundResourceAdapterBeanDConfig var1) {
      this.outboundResourceAdapterDConfig = var1;
      this.firePropertyChange(new PropertyChangeEvent(this, "OutboundResourceAdapter", (Object)null, (Object)null));
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
