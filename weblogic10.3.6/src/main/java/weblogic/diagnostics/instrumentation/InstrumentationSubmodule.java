package weblogic.diagnostics.instrumentation;

import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.descriptor.DescriptorDiff;
import weblogic.diagnostics.descriptor.WLDFInstrumentationBean;
import weblogic.diagnostics.descriptor.WLDFInstrumentationMonitorBean;
import weblogic.diagnostics.descriptor.WLDFResourceBean;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.module.WLDFModuleException;
import weblogic.diagnostics.module.WLDFSubModule;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.management.runtime.RuntimeMBean;

public final class InstrumentationSubmodule implements WLDFSubModule {
   private ApplicationContextInternal appCtx;
   private WLDFResourceBean wldfResource;
   private boolean isSystemResource;

   public static final WLDFSubModule createInstance() {
      return new InstrumentationSubmodule();
   }

   private InstrumentationSubmodule() {
   }

   private String getName() {
      return this.isSystemResource ? "_WL_INTERNAL_SERVER_SCOPE" : this.appCtx.getApplicationId();
   }

   public void init(ApplicationContext var1, WLDFResourceBean var2) throws WLDFModuleException {
      if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_CONFIG.debug("InstrumentationSubmodule initialized for " + var1);
      }

      try {
         this.appCtx = (ApplicationContextInternal)var1;
         this.wldfResource = var2;
         this.isSystemResource = this.appCtx.getSystemResourceMBean() != null;
      } catch (ClassCastException var4) {
         throw new WLDFModuleException("Unexpected application context type", var4);
      }
   }

   public void prepare() throws WLDFModuleException {
      String var1 = this.getName();
      if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_CONFIG.debug("InstrumentationSubmodule prepare for " + var1);
      }

      J2EEApplicationRuntimeMBeanImpl var2 = this.appCtx.getRuntime();
      InstrumentationManager var3 = InstrumentationManager.getInstrumentationManager();
      InstrumentationScope var4 = null;
      if (this.isSystemResource) {
         var4 = var3.findInstrumentationScope(var1);
         if (var4 == null) {
            this.createInstrumentationScope(var1, (J2EEApplicationRuntimeMBeanImpl)null);
         } else {
            var4.registerRuntime((RuntimeMBean)null);
         }

      } else if (!var3.isEnabled()) {
         if (!this.isEmptyInstrumentationDescriptor()) {
            DiagnosticsLogger.logWarnInstrumentationManagerDisabled(var1);
         }

      } else {
         var4 = var3.findInstrumentationScope(var1);
         if (var4 != null) {
            throw new WLDFModuleException("Instrumentation scope " + var1 + " already exists");
         } else {
            this.createInstrumentationScope(var1, var2);
         }
      }
   }

   private boolean isEmptyInstrumentationDescriptor() {
      WLDFInstrumentationBean var1 = this.wldfResource != null ? this.wldfResource.getInstrumentation() : null;
      if (var1 != null) {
         if (var1.isEnabled()) {
            return false;
         }

         WLDFInstrumentationMonitorBean[] var2 = var1.getWLDFInstrumentationMonitors();
         if (var2 != null && var2.length > 0) {
            return false;
         }
      }

      return true;
   }

   private void createInstrumentationScope(String var1, J2EEApplicationRuntimeMBeanImpl var2) throws WLDFModuleException {
      try {
         InstrumentationManager var3 = InstrumentationManager.getInstrumentationManager();
         WLDFInstrumentationBean var4 = this.wldfResource != null ? this.wldfResource.getInstrumentation() : null;
         InstrumentationScope var5 = var3.createInstrumentationScope(var1, var4);
         if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
            InstrumentationDebug.DEBUG_CONFIG.debug("InstrumentationSubmodule createInstrumentationScope for " + var1 + " created scope=" + var5);
         }

         if (var5 != null) {
            DiagnosticsLogger.logInformInstrumentationScopeCreation(var1);
            if (!var5.isEnabled()) {
               DiagnosticsLogger.logWarnInstrumentationScopeDisabled(var1);
            }

            var5.registerRuntime(var2);
         }

      } catch (Exception var6) {
         throw new WLDFModuleException("Failed to create instrumentation module for application " + var1, var6);
      }
   }

   public void activate() throws WLDFModuleException {
   }

   public void deactivate() throws WLDFModuleException {
      if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_CONFIG.debug("InstrumentationSubmodule deactivate for " + this.appCtx);
      }

      String var1 = this.getName();
      InstrumentationManager var2 = InstrumentationManager.getInstrumentationManager();
      InstrumentationScope var3 = var2.findInstrumentationScope(var1);
      if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_CONFIG.debug("InstrumentationSubmodule deactivate for " + var1 + " found scope=" + var3);
      }

      if (var3 != null) {
         var3.setEnabled(false);
         var3.unregisterRuntime();
         var2.deleteInstrumentationScope(var3);
      }

   }

   public void unprepare() throws WLDFModuleException {
      if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_CONFIG.debug("InstrumentationSubmodule unprepare for " + this.appCtx);
      }

      String var1 = this.getName();
      InstrumentationManager var2 = InstrumentationManager.getInstrumentationManager();
      InstrumentationScope var3 = var2.findInstrumentationScope(var1);
      if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_CONFIG.debug("InstrumentationSubmodule unprepare for " + var1 + " found scope=" + var3);
      }

      if (var3 != null) {
         var3.setEnabled(false);
         var3.unregisterRuntime();
         var2.deleteInstrumentationScope(var3);
      }

   }

   public void destroy() throws WLDFModuleException {
   }

   public void prepareUpdate(WLDFResourceBean var1, DescriptorDiff var2) throws WLDFModuleException {
   }

   public void activateUpdate(WLDFResourceBean var1, DescriptorDiff var2) throws WLDFModuleException {
      if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_CONFIG.debug("InstrumentationSubmodule activateUpdate for " + this.appCtx);
      }

      String var3 = this.getName();
      InstrumentationManager var4 = InstrumentationManager.getInstrumentationManager();
      InstrumentationScope var5 = var4.findInstrumentationScope(var3);
      if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_CONFIG.debug("InstrumentationSubmodule activateUpdate for " + var3 + " found scope=" + var5);
      }

      if (var5 != null) {
         WLDFInstrumentationBean var6 = var1.getInstrumentation();
         InstrumentationScope var7 = InstrumentationScope.createInstrumentationScope(var3, var6);
         var5.merge(var7);
      }

   }

   public void rollbackUpdate(WLDFResourceBean var1, DescriptorDiff var2) {
   }
}
