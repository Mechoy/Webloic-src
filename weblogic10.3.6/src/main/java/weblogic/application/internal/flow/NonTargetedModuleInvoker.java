package weblogic.application.internal.flow;

import java.util.HashMap;
import java.util.Map;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContext;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.ModuleWrapper;
import weblogic.application.UpdateListener;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.utils.classloaders.GenericClassLoader;

public final class NonTargetedModuleInvoker extends ModuleWrapper implements Module, UpdateListener.Registration {
   private static final boolean DEBUG = false;
   private final Map updateListeners = new HashMap(1);
   private final Module delegate;
   private UpdateListener.Registration updateListenerRegistration = null;

   public NonTargetedModuleInvoker(Module var1) {
      this.delegate = var1;
   }

   public Module getDelegate() {
      return this.delegate;
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
      this.updateListenerRegistration = var3;
      return this.delegate.init(var1, var2, this);
   }

   public void initUsingLoader(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.delegate.initUsingLoader(var1, var2, var3);
   }

   public void destroy(UpdateListener.Registration var1) throws ModuleException {
      this.delegate.destroy(this);
   }

   public void remove() throws ModuleException {
      this.delegate.remove();
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

   public void adminToProduction() {
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) {
   }

   public void forceProductionToAdmin() {
   }

   public void addUpdateListener(UpdateListener var1) {
      OnlyAcceptURIUpdateListener var2 = new OnlyAcceptURIUpdateListener(var1);
      this.updateListeners.put(var1, var2);
      this.updateListenerRegistration.addUpdateListener(var2);
   }

   public void removeUpdateListener(UpdateListener var1) {
      UpdateListener var2 = (UpdateListener)this.updateListeners.remove(var1);
      this.updateListenerRegistration.removeUpdateListener(var2);
   }

   private static class OnlyAcceptURIUpdateListener implements UpdateListener {
      private final UpdateListener delegate;

      OnlyAcceptURIUpdateListener(UpdateListener var1) {
         this.delegate = var1;
      }

      public boolean acceptURI(String var1) {
         return this.delegate.acceptURI(var1);
      }

      public void prepareUpdate(String var1) {
      }

      public void activateUpdate(String var1) {
      }

      public void rollbackUpdate(String var1) {
      }
   }
}
