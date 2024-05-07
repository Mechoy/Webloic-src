package weblogic.application.internal.flow;

import java.util.Iterator;
import java.util.Map;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContext;
import weblogic.application.DeploymentManager;
import weblogic.application.MergedDescriptorModule;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.ModuleListener;
import weblogic.application.ModuleListenerCtx;
import weblogic.application.ModuleListenerCtxImpl;
import weblogic.application.ModuleWrapper;
import weblogic.application.UpdateListener;
import weblogic.application.utils.ExceptionUtils;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;

public final class ModuleListenerInvoker extends ModuleWrapper implements Module, MergedDescriptorModule {
   private final Module delegate;
   private final TargetMBean target;
   private ModuleListener.State state;
   private ModuleListenerCtx ctx;
   private final DeploymentManager deploymentManager;

   ModuleListenerInvoker(Module var1, TargetMBean var2) {
      this.state = ModuleListener.STATE_NEW;
      this.deploymentManager = DeploymentManager.getDeploymentManager();
      this.delegate = var1;
      this.target = var2;
   }

   public String toString() {
      return this.getClass().getName() + "(" + this.delegate.getClass().getName() + ")" + "(" + this.ctx.toString() + ")";
   }

   public Module getDelegate() {
      return this.delegate;
   }

   private void throwModuleException(Throwable var1) throws ModuleException {
      ExceptionUtils.throwModuleException(var1);
   }

   public String getId() {
      return this.delegate.getId();
   }

   public String getType() {
      return this.delegate.getType();
   }

   public ComponentRuntimeMBean[] getComponentRuntimeMBeans() {
      return this.delegate.getComponentRuntimeMBeans();
   }

   public DescriptorBean[] getDescriptors() {
      return this.delegate.getDescriptors();
   }

   public GenericClassLoader init(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.ctx = new ModuleListenerCtxImpl(var1.getApplicationId(), this.getId(), this.target, this.getType());
      return this.delegate.init(var1, var2, var3);
   }

   public void initUsingLoader(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.ctx = new ModuleListenerCtxImpl(var1.getApplicationId(), this.getId(), this.target, this.getType());
      this.delegate.initUsingLoader(var1, var2, var3);
   }

   public void prepare() throws ModuleException {
      try {
         this.begin(ModuleListener.STATE_PREPARED);
         this.delegate.prepare();
         this.end(ModuleListener.STATE_PREPARED);
         this.state = ModuleListener.STATE_PREPARED;
      } catch (Throwable var2) {
         this.failed(ModuleListener.STATE_PREPARED);
         this.throwModuleException(var2);
      }

   }

   public void activate() throws ModuleException {
      try {
         this.begin(ModuleListener.STATE_ADMIN);
         this.delegate.activate();
         this.end(ModuleListener.STATE_ADMIN);
         this.state = ModuleListener.STATE_ADMIN;
      } catch (Throwable var2) {
         this.failed(ModuleListener.STATE_ADMIN);
         this.throwModuleException(var2);
      }

   }

   public void start() throws ModuleException {
      this.delegate.start();
   }

   public void deactivate() throws ModuleException {
      try {
         this.begin(ModuleListener.STATE_PREPARED);
         this.delegate.deactivate();
         this.end(ModuleListener.STATE_PREPARED);
         this.state = ModuleListener.STATE_PREPARED;
      } catch (Throwable var2) {
         this.failed(ModuleListener.STATE_PREPARED);
         this.throwModuleException(var2);
      }

   }

   public void unprepare() throws ModuleException {
      try {
         this.begin(ModuleListener.STATE_NEW);
         this.delegate.unprepare();
         this.end(ModuleListener.STATE_NEW);
         this.state = ModuleListener.STATE_NEW;
      } catch (Throwable var2) {
         this.failed(ModuleListener.STATE_NEW);
         this.throwModuleException(var2);
      }

   }

   public void destroy(UpdateListener.Registration var1) throws ModuleException {
      this.delegate.destroy(var1);
   }

   public void remove() throws ModuleException {
      this.delegate.remove();
   }

   public void adminToProduction() {
      try {
         this.begin(ModuleListener.STATE_ACTIVE);
         this.delegate.adminToProduction();
         this.end(ModuleListener.STATE_ACTIVE);
         this.state = ModuleListener.STATE_ACTIVE;
      } catch (RuntimeException var2) {
         this.failed(ModuleListener.STATE_ACTIVE);
         throw var2;
      } catch (Error var3) {
         this.failed(ModuleListener.STATE_ACTIVE);
         throw var3;
      }
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) throws ModuleException {
      try {
         this.begin(ModuleListener.STATE_ADMIN);
         this.delegate.gracefulProductionToAdmin(var1);
         this.end(ModuleListener.STATE_ADMIN);
         this.state = ModuleListener.STATE_ADMIN;
      } catch (Throwable var3) {
         this.failed(ModuleListener.STATE_ADMIN);
         this.throwModuleException(var3);
      }

   }

   public void forceProductionToAdmin() throws ModuleException {
      try {
         this.begin(ModuleListener.STATE_ADMIN);
         this.delegate.forceProductionToAdmin();
         this.end(ModuleListener.STATE_ADMIN);
         this.state = ModuleListener.STATE_ADMIN;
      } catch (Throwable var2) {
         this.failed(ModuleListener.STATE_ADMIN);
         this.throwModuleException(var2);
      }

   }

   private void begin(ModuleListener.State var1) {
      Iterator var2 = this.deploymentManager.getModuleListeners();

      while(var2.hasNext()) {
         ((ModuleListener)var2.next()).beginTransition(this.ctx, this.state, var1);
      }

   }

   private void end(ModuleListener.State var1) {
      Iterator var2 = this.deploymentManager.getModuleListeners();

      while(var2.hasNext()) {
         ((ModuleListener)var2.next()).endTransition(this.ctx, this.state, var1);
      }

   }

   private void failed(ModuleListener.State var1) {
      Iterator var2 = this.deploymentManager.getModuleListeners();

      while(var2.hasNext()) {
         ((ModuleListener)var2.next()).failedTransition(this.ctx, this.state, var1);
      }

   }

   public Map getDescriptorMappings() {
      return this.delegate instanceof MergedDescriptorModule ? ((MergedDescriptorModule)this.delegate).getDescriptorMappings() : null;
   }

   public void handleMergedFinder(ClassFinder var1) {
      if (this.delegate instanceof MergedDescriptorModule) {
         ((MergedDescriptorModule)this.delegate).handleMergedFinder(var1);
      }

   }
}
