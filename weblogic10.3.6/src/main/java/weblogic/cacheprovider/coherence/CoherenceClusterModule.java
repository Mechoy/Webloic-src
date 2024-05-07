package weblogic.cacheprovider.coherence;

import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContext;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.UpdateListener;
import weblogic.coherence.descriptor.wl.WeblogicCoherenceBean;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.utils.classloaders.GenericClassLoader;

public class CoherenceClusterModule implements Module {
   private final String uri;
   private WeblogicCoherenceBean bean;

   public CoherenceClusterModule(String var1) {
      this.uri = var1;
   }

   public String getId() {
      return this.uri;
   }

   public String getModuleURI() {
      return this.uri;
   }

   public String getType() {
      return WebLogicModuleType.MODULETYPE_COHERENCE_CLUSTER;
   }

   public ComponentRuntimeMBean[] getComponentRuntimeMBeans() {
      return null;
   }

   public DescriptorBean[] getDescriptors() {
      return this.bean != null ? new DescriptorBean[]{(DescriptorBean)this.bean} : new DescriptorBean[0];
   }

   public GenericClassLoader init(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      return null;
   }

   public void initUsingLoader(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.bean = CoherenceClusterDescriptorHelper.createCoherenceDescriptor(this.uri);
   }

   public void prepare() throws ModuleException {
   }

   public void activate() throws ModuleException {
   }

   public void start() throws ModuleException {
   }

   public void deactivate() throws ModuleException {
   }

   public void unprepare() throws ModuleException {
   }

   public void destroy(UpdateListener.Registration var1) throws ModuleException {
   }

   public void remove() throws ModuleException {
   }

   public void adminToProduction() {
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) throws ModuleException {
   }

   public void forceProductionToAdmin() throws ModuleException {
   }
}
