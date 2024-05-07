package weblogic.application.internal;

import java.security.AccessController;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ConcurrentModule;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.UpdateListener;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.classloaders.GenericClassLoader;

public final class AppClientModule implements Module, ConcurrentModule {
   private final String uri;
   private AppClientComponentRuntimeMBeanImpl rtmb;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   AppClientModule(String var1) {
      this.uri = var1;
   }

   public String getId() {
      return this.uri;
   }

   public String getType() {
      return WebLogicModuleType.MODULETYPE_CAR;
   }

   public DescriptorBean[] getDescriptors() {
      return new DescriptorBean[0];
   }

   public ComponentRuntimeMBean[] getComponentRuntimeMBeans() {
      return new ComponentRuntimeMBean[]{this.rtmb};
   }

   public void initUsingLoader(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      ApplicationContextInternal var4 = (ApplicationContextInternal)var1;
      String var5 = ManagementService.getRuntimeAccess(kernelId).getServerName() + "_" + var4.getApplicationId() + "_" + this.uri;

      try {
         this.rtmb = new AppClientComponentRuntimeMBeanImpl(var5, this.uri, var4.getRuntime());
      } catch (ManagementException var7) {
         throw new ModuleException(var7);
      }
   }

   public GenericClassLoader init(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.initUsingLoader(var1, var2, var3);
      return var2;
   }

   public void destroy(UpdateListener.Registration var1) throws ModuleException {
      try {
         if (this.rtmb != null) {
            this.rtmb.unregister();
         }

      } catch (ManagementException var3) {
         throw new ModuleException(var3);
      }
   }

   public void prepare() {
   }

   public void activate() {
   }

   public void start() {
   }

   public void deactivate() {
   }

   public void unprepare() {
   }

   public void remove() {
   }

   public void adminToProduction() {
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) {
   }

   public void forceProductionToAdmin() {
   }

   public boolean isParallelEnabled() {
      return true;
   }
}
