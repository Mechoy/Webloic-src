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

public class WeblogicApplicationBeanDConfig extends BasicDConfigBeanRoot {
   private static final boolean debug = Debug.isDebug("config");
   private WeblogicApplicationBean beanTreeNode;

   public WeblogicApplicationBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WeblogicApplicationBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
      ConfigTemplate.configureSecurity(this);
   }

   public WeblogicApplicationBeanDConfig(DDBeanRoot var1, WebLogicDeploymentConfiguration var2, DescriptorBean var3, String var4, DescriptorSupport var5) throws InvalidModuleException, IOException {
      super(var1, var2, var4, var5);
      this.beanTree = var3;
      this.beanTreeNode = (WeblogicApplicationBean)var3;

      try {
         this.initXpaths();
         this.customInit();
      } catch (ConfigurationException var7) {
         throw new InvalidModuleException(var7.toString());
      }
   }

   public WeblogicApplicationBeanDConfig(DDBeanRoot var1, WebLogicDeploymentConfiguration var2, String var3, DescriptorSupport var4) throws InvalidModuleException, IOException {
      super(var1, var2, var3, var4);

      try {
         this.initXpaths();
         if (debug) {
            Debug.say("Creating new root object");
         }

         this.beanTree = DescriptorParser.getWLSEditableDescriptorBean(WeblogicApplicationBean.class);
         this.beanTreeNode = (WeblogicApplicationBean)this.beanTree;
         this.customInit();
         ConfigTemplate.configureSecurity(this);
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

   public EjbBean getEjb() {
      return this.beanTreeNode.getEjb();
   }

   public XmlBean getXml() {
      return this.beanTreeNode.getXml();
   }

   public JDBCConnectionPoolBean[] getJDBCConnectionPools() {
      return this.beanTreeNode.getJDBCConnectionPools();
   }

   public SecurityBean getSecurity() {
      return this.beanTreeNode.getSecurity();
   }

   public ApplicationParamBean[] getApplicationParams() {
      return this.beanTreeNode.getApplicationParams();
   }

   public ClassloaderStructureBean getClassloaderStructure() {
      return this.beanTreeNode.getClassloaderStructure();
   }

   public ListenerBean[] getListeners() {
      return this.beanTreeNode.getListeners();
   }

   public SingletonServiceBean[] getSingletonServices() {
      return this.beanTreeNode.getSingletonServices();
   }

   public StartupBean[] getStartups() {
      return this.beanTreeNode.getStartups();
   }

   public ShutdownBean[] getShutdowns() {
      return this.beanTreeNode.getShutdowns();
   }

   public WeblogicModuleBean[] getModules() {
      return this.beanTreeNode.getModules();
   }

   public LibraryRefBean[] getLibraryRefs() {
      return this.beanTreeNode.getLibraryRefs();
   }

   public FairShareRequestClassBean[] getFairShareRequests() {
      return this.beanTreeNode.getFairShareRequests();
   }

   public ResponseTimeRequestClassBean[] getResponseTimeRequests() {
      return this.beanTreeNode.getResponseTimeRequests();
   }

   public ContextRequestClassBean[] getContextRequests() {
      return this.beanTreeNode.getContextRequests();
   }

   public MaxThreadsConstraintBean[] getMaxThreadsConstraints() {
      return this.beanTreeNode.getMaxThreadsConstraints();
   }

   public MinThreadsConstraintBean[] getMinThreadsConstraints() {
      return this.beanTreeNode.getMinThreadsConstraints();
   }

   public CapacityBean[] getCapacities() {
      return this.beanTreeNode.getCapacities();
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

   public ApplicationAdminModeTriggerBean getApplicationAdminModeTrigger() {
      return this.beanTreeNode.getApplicationAdminModeTrigger();
   }

   public SessionDescriptorBean getSessionDescriptor() {
      return this.beanTreeNode.getSessionDescriptor();
   }

   public LibraryContextRootOverrideBean[] getLibraryContextRootOverrides() {
      return this.beanTreeNode.getLibraryContextRootOverrides();
   }

   public PreferApplicationPackagesBean getPreferApplicationPackages() {
      return this.beanTreeNode.getPreferApplicationPackages();
   }

   public PreferApplicationResourcesBean getPreferApplicationResources() {
      return this.beanTreeNode.getPreferApplicationResources();
   }

   public FastSwapBean getFastSwap() {
      return this.beanTreeNode.getFastSwap();
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
