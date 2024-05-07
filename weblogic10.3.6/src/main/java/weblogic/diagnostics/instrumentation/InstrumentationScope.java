package weblogic.diagnostics.instrumentation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import weblogic.diagnostics.context.DiagnosticContextHelper;
import weblogic.diagnostics.descriptor.WLDFInstrumentationBean;
import weblogic.diagnostics.descriptor.WLDFInstrumentationMonitorBean;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.instrumentation.engine.InstrumentationEngineConfiguration;
import weblogic.diagnostics.instrumentation.engine.InstrumentorEngine;
import weblogic.diagnostics.instrumentation.engine.MonitorSpecification;
import weblogic.diagnostics.instrumentation.engine.base.PointcutExpression;
import weblogic.diagnostics.instrumentation.engine.base.PointcutExpression.Factory;
import weblogic.diagnostics.type.UnexpectedExceptionHandler;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.runtime.WLDFRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class InstrumentationScope implements Serializable {
   private String name;
   private String description;
   private List diagnosticMonitorControls;
   private boolean enabled;
   private String[] includes;
   private String[] excludes;
   private transient InstrumentorEngine instrumentorEngine;
   private transient InstrumentationRuntimeMBeanImpl runtimeMbean;
   private WeakHashMap classLoadersMap = new WeakHashMap();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public InstrumentationScope(String var1) {
      this.name = var1;
      this.description = "";
      this.diagnosticMonitorControls = new ArrayList();
   }

   public String getName() {
      return this.name;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public DiagnosticMonitorControl findDiagnosticMonitorControl(String var1) {
      synchronized(this.diagnosticMonitorControls) {
         Iterator var3 = this.diagnosticMonitorControls.iterator();

         DiagnosticMonitorControl var4;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            var4 = (DiagnosticMonitorControl)var3.next();
         } while(!var1.equals(var4.getType()));

         return var4;
      }
   }

   public DiagnosticMonitorControl[] getMonitorControlsInScope() {
      synchronized(this.diagnosticMonitorControls) {
         DiagnosticMonitorControl[] var2 = new DiagnosticMonitorControl[this.diagnosticMonitorControls.size()];
         return (DiagnosticMonitorControl[])((DiagnosticMonitorControl[])this.diagnosticMonitorControls.toArray(var2));
      }
   }

   public void addMonitorControl(DiagnosticMonitorControl var1) throws DuplicateMonitorException {
      synchronized(this.diagnosticMonitorControls) {
         String var3 = var1.getType();
         InstrumentationDebug.DEBUG_CONFIG.debug("Addmin monitor " + var3 + " to scope " + this.getName());
         if (this.findDiagnosticMonitorControl(var3) != null) {
            throw new DuplicateMonitorException("Diagnostic monitor of type " + var3 + " already exists in instrumentation scope " + this.getName());
         } else {
            this.diagnosticMonitorControls.add(var1);
            if (var1.getInstrumentationScope() == null || !"_WL_INTERNAL_SERVER_SCOPE".equals(this.getName())) {
               var1.setInstrumentationScope(this);
            }

         }
      }
   }

   public void removeMonitorControl(DiagnosticMonitorControl var1) throws MonitorNotFoundException {
      synchronized(this.diagnosticMonitorControls) {
         String var3 = var1.getType();
         InstrumentationDebug.DEBUG_CONFIG.debug("Removing monitor " + var3 + " from scope " + this.getName());
         var1 = this.findDiagnosticMonitorControl(var3);
         if (var1 == null) {
            throw new MonitorNotFoundException("Diagnostic monitor of type " + var3 + " does not exist in instrumentation scope " + this.getName());
         } else {
            var1.setEnabled(false);
            var1.setInstrumentationScope((InstrumentationScope)null);
            this.diagnosticMonitorControls.remove(var1);
         }
      }
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
      if (!this.enabled) {
         this.disableAllMonitors();
      }

   }

   public String[] getIncludes() {
      return this.includes;
   }

   public void setIncludes(String[] var1) {
      this.includes = var1;
   }

   private void validateRegexPatterns(String[] var1) throws PatternSyntaxException {
      int var2 = var1 != null ? var1.length : 0;

      for(int var3 = 0; var3 < var2; ++var3) {
         Pattern.compile(var1[var3]);
      }

   }

   public String[] getExcludes() {
      return this.excludes;
   }

   public void setExcludes(String[] var1) {
      this.excludes = var1;
   }

   public InstrumentationStatistics getInstrumentationStatistics() {
      return this.instrumentorEngine.getInstrumentationStatistics();
   }

   byte[] instrumentClass(ClassLoader var1, String var2, byte[] var3) {
      byte[] var4 = var3;
      if (this.isEnabled() && this.instrumentorEngine != null) {
         byte[] var5 = this.instrumentorEngine.instrumentClass(var1, var2, var3);
         if (var5 != null) {
            var4 = var5;
         }
      }

      synchronized(this.classLoadersMap) {
         this.classLoadersMap.put(var1, new WeakReference(var1));
         return var4;
      }
   }

   private void redefineClasses() {
      DiagnosticsLogger.logDiagnosticsClassRedefinition(this.getName());
      this.initialize();
      ArrayList var1 = new ArrayList();
      synchronized(this.classLoadersMap) {
         Iterator var3 = this.classLoadersMap.keySet().iterator();

         while(true) {
            if (!var3.hasNext()) {
               break;
            }

            var1.add(var3.next());
         }
      }

      if (var1.size() > 0) {
         try {
            Class var2 = Class.forName("weblogic.diagnostics.instrumentation.agent.WLDFInstrumentationAgent");
            Class[] var9 = new Class[]{List.class};
            Method var4 = var2.getMethod("redefineClasses", var9);
            Object[] var5 = new Object[]{var1};
            var4.invoke((Object)null, var5);
         } catch (Throwable var7) {
            DiagnosticsLogger.logDiagnosticsClassRedefinitionFailed(this.getName(), var7);
         }
      }

   }

   private static boolean isParent(ClassLoader var0, ClassLoader var1) {
      while(var1 != null) {
         if (var1 == var0) {
            return true;
         }

         var1 = var1.getParent();
      }

      return false;
   }

   void findAttachedActionTypes(Map var1) {
      Iterator var2 = this.diagnosticMonitorControls.iterator();

      while(true) {
         Object var3;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = var2.next();
         } while(!(var3 instanceof DelegatingMonitorControl));

         DelegatingMonitorControl var4 = (DelegatingMonitorControl)var3;
         DiagnosticAction[] var5 = var4.getActions();
         int var6 = var5 != null ? var5.length : 0;

         for(int var7 = 0; var7 < var6; ++var7) {
            String var8 = var5[var7].getType();
            synchronized(var1) {
               String var10 = (String)var1.get(var8);
               var10 = var10 == null ? this.getName() : var10 + "," + this.getName();
               var1.put(var8, var10);
            }
         }
      }
   }

   void initialize() {
      InstrumentationEngineConfiguration var1 = InstrumentationEngineConfiguration.getInstrumentationEngineConfiguration();
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.diagnosticMonitorControls.iterator();

      while(var3.hasNext()) {
         DiagnosticMonitorControl var4 = (DiagnosticMonitorControl)var3.next();
         MonitorSpecification var5 = null;
         if (var4 instanceof CustomMonitorControl) {
            var5 = ((CustomMonitorControl)var4).getMonitorSpecification();
         } else {
            var5 = var1.getMonitorSpecification(var4.getType());
         }

         if (var5 != null) {
            var2.add(var5);
         }
      }

      MonitorSpecification[] var8 = new MonitorSpecification[var2.size()];
      var8 = (MonitorSpecification[])((MonitorSpecification[])var2.toArray(var8));
      boolean var9 = InstrumentationManager.getInstrumentationManager().isHotswapAvailable();
      this.instrumentorEngine = new InstrumentorEngine(this.name, var8, var9);
      this.instrumentorEngine.setDiagnosticMonitors(this.diagnosticMonitorControls);

      try {
         if (this.includes != null) {
            this.instrumentorEngine.setIncludePatterns(this.includes);
         }
      } catch (PatternSyntaxException var7) {
         DiagnosticsLogger.logInvalidInclusionPatternError(this.name);
      }

      try {
         if (this.excludes != null) {
            this.instrumentorEngine.setExcludePatterns(this.excludes);
         }
      } catch (PatternSyntaxException var6) {
         DiagnosticsLogger.logInvalidExclusionPatternError(this.name);
      }

   }

   void registerRuntime(RuntimeMBean var1) {
      try {
         if (this.runtimeMbean == null && ManagementService.getRuntimeAccess(kernelId) != null) {
            ServerRuntimeMBean var2 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
            WLDFRuntimeMBean var3 = null;
            if (var2 != null) {
               var3 = var2.getWLDFRuntime();
            }

            if (var1 == null) {
               var1 = var3;
            }

            this.runtimeMbean = new InstrumentationRuntimeMBeanImpl(this, (RuntimeMBean)var1);
            if (var3 != null) {
               var3.addWLDFInstrumentationRuntime(this.runtimeMbean);
            }
         }
      } catch (ManagementException var4) {
         UnexpectedExceptionHandler.handle("Internal error: failed to create runtime mbean for instrumentation scope " + this.getName(), var4);
      }

   }

   void unregisterRuntime() {
      try {
         if (this.runtimeMbean != null) {
            if (ManagementService.getRuntimeAccess(kernelId) != null) {
               ServerRuntimeMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
               if (var1 != null) {
                  WLDFRuntimeMBean var2 = var1.getWLDFRuntime();
                  var2.removeWLDFInstrumentationRuntime(this.runtimeMbean);
               }
            }

            this.runtimeMbean.unregister();
            this.runtimeMbean = null;
         }
      } catch (ManagementException var3) {
         UnexpectedExceptionHandler.handle("Internal error: failed to unregister runtime mbean for instrumentation scope " + this.getName(), var3);
      }

   }

   public synchronized void merge(InstrumentationScope var1) {
      if (this.name.equals(var1.getName())) {
         boolean var2 = false;
         if (this.enabled != var1.enabled) {
            var2 = true;
         }

         if (!this.isIdenticalList(this.includes, var1.includes)) {
            var2 = true;
         }

         if (!this.isIdenticalList(this.excludes, var1.excludes)) {
            var2 = true;
         }

         DiagnosticMonitorControl[] var3 = this.getMonitorControlsInScope();
         int var4 = var3 != null ? var3.length : 0;

         int var5;
         for(var5 = 0; var5 < var4; ++var5) {
            if (var1.findDiagnosticMonitorControl(var3[var5].getType()) == null) {
               try {
                  this.removeMonitorControl(var3[var5]);
                  var2 = true;
               } catch (Exception var10) {
               }
            }
         }

         var3 = var1.getMonitorControlsInScope();
         var4 = var3 != null ? var3.length : 0;

         for(var5 = 0; var5 < var4; ++var5) {
            DiagnosticMonitorControl var6 = var3[var5];
            DiagnosticMonitorControl var7 = this.findDiagnosticMonitorControl(var6.getType());
            if (var7 != null) {
               if (var7 instanceof CustomMonitorControl && var6 instanceof CustomMonitorControl) {
                  CustomMonitorControl var8 = (CustomMonitorControl)var7;
                  if (var8.isStructurallyDifferent((CustomMonitorControl)var6)) {
                     var2 = true;
                  }
               }

               if (!this.isIdenticalList(var7.getIncludes(), var6.getIncludes())) {
                  var2 = true;
               }

               if (!this.isIdenticalList(var7.getExcludes(), var6.getExcludes())) {
                  var2 = true;
               }

               var7.merge(var6);
            } else {
               try {
                  this.addMonitorControl(var3[var5]);
                  var2 = true;
               } catch (Exception var9) {
               }
            }
         }

         InstrumentationManager var11 = InstrumentationManager.getInstrumentationManager();
         if ("_WL_INTERNAL_SERVER_SCOPE".equals(this.getName())) {
            this.enabled = var1.isEnabled();
            if (!this.enabled) {
               this.disableAllMonitors();
            }

            var11.installDyeInjectionMonitor(this);
         } else if (var2) {
            if (InstrumentationManager.getInstrumentationManager().isHotswapAvailable()) {
               this.enabled = var1.isEnabled();
               this.includes = var1.includes;
               this.excludes = var1.excludes;
               var11.installDyeInjectionMonitor(this);
               this.redefineClasses();
            } else {
               DiagnosticsLogger.logWarnHotswapUnavailable(this.getName());
            }
         }

      }
   }

   private void disableAllMonitors() {
      if (this.diagnosticMonitorControls != null) {
         Iterator var1 = this.diagnosticMonitorControls.iterator();

         while(var1.hasNext()) {
            DiagnosticMonitorControl var2 = (DiagnosticMonitorControl)var1.next();
            var2.setEnabled(false);
         }
      }

   }

   private boolean isIdenticalList(String[] var1, String[] var2) {
      int var3 = var1 != null ? var1.length : 0;
      int var4 = var2 != null ? var2.length : 0;
      if (var3 != var4) {
         return false;
      } else {
         for(int var5 = 0; var5 < var3; ++var5) {
            String var6 = var1[var5];
            boolean var7 = false;

            for(int var8 = 0; !var7 && var8 < var4; ++var8) {
               if (var6.equals(var2[var8])) {
                  var7 = true;
               }
            }

            if (!var7) {
               return false;
            }
         }

         return true;
      }
   }

   public static InstrumentationScope createInstrumentationScope(String var0, WLDFInstrumentationBean var1) {
      InstrumentationScope var2 = new InstrumentationScope(var0);
      if (var1 != null) {
         var2.setEnabled(var1.isEnabled());
         var2.setExcludes(var1.getExcludes());
         var2.setIncludes(var1.getIncludes());
         readMonitors(var2, var1.getWLDFInstrumentationMonitors());
      }

      if (!var2.isEnabled()) {
         var2.disableAllMonitors();
      }

      return var2;
   }

   private static void readMonitors(InstrumentationScope var0, WLDFInstrumentationMonitorBean[] var1) {
      int var2 = var1 != null ? var1.length : 0;
      InstrumentationEngineConfiguration var3 = InstrumentationEngineConfiguration.getInstrumentationEngineConfiguration();

      for(int var4 = 0; var4 < var2; ++var4) {
         WLDFInstrumentationMonitorBean var5 = var1[var4];
         String var6 = var5.getName();
         Object var7 = null;
         MonitorSpecification var8 = var3.getMonitorSpecification(var6);
         if (var8 != null) {
            var7 = createMonitorControl(var0, var5);
         } else {
            var7 = readCustomMonitor(var0, var5);
         }

         if (var7 != null) {
            ((DiagnosticMonitorControl)var7).setEnabled(var5.isEnabled());
            ((DiagnosticMonitorControl)var7).setDyeFilteringEnabled(var5.isDyeFilteringEnabled());
            ((DiagnosticMonitorControl)var7).setDyeMask(DiagnosticContextHelper.parseDyeMask(var5.getDyeMask()));
            ((DiagnosticMonitorControl)var7).setIncludes(var5.getIncludes());
            ((DiagnosticMonitorControl)var7).setExcludes(var5.getExcludes());
            if ("HttpSessionDebug".equals(var6)) {
               ((DiagnosticMonitorControl)var7).argumentsCaptureNeeded = true;
            }

            readProperties((DiagnosticMonitorControl)var7, var5.getProperties());
            if (var7 instanceof DelegatingMonitorControl) {
               readMonitorActions(var0, var5, (DelegatingMonitorControl)var7);
            }

            try {
               var0.addMonitorControl((DiagnosticMonitorControl)var7);
            } catch (DuplicateMonitorException var10) {
               DiagnosticsLogger.logDuplicateMonitorInScope(var0.getName(), ((DiagnosticMonitorControl)var7).getType());
            }
         }
      }

   }

   private static void readProperties(DiagnosticMonitorControl var0, String var1) {
      if (var1 != null) {
         ByteArrayInputStream var2 = null;

         try {
            var2 = new ByteArrayInputStream(var1.getBytes());
            Properties var3 = new Properties();
            var3.load(var2);
            String[] var4 = var0.getAttributeNames();
            int var5 = var4 != null ? var4.length : 0;

            for(int var6 = 0; var6 < var5; ++var6) {
               var0.setAttribute(var4[var6], (String)null);
            }

            Enumeration var19 = var3.propertyNames();

            while(var19.hasMoreElements()) {
               String var7 = (String)var19.nextElement();
               String var8 = var3.getProperty(var7);
               var0.setAttribute(var7, var8);
            }
         } catch (IOException var17) {
            DiagnosticsLogger.logMonitorAttributeError(var0.getName(), var1, var17);
         } finally {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (Exception var16) {
               }
            }

         }

      }
   }

   private static void readMonitorActions(InstrumentationScope var0, WLDFInstrumentationMonitorBean var1, DelegatingMonitorControl var2) {
      InstrumentationLibrary var3 = InstrumentationLibrary.getInstrumentationLibrary();
      String[] var4 = var1.getActions();
      var2.removeAllActions();
      int var5 = var4 != null ? var4.length : 0;

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];

         try {
            DiagnosticAction var8 = var3.getDiagnosticAction(var7);
            if (var8 == null) {
               DiagnosticsLogger.logNonExistentActionType(var0.getName(), var2.getType(), var7);
            } else {
               var2.addAction(var8);
            }
         } catch (DuplicateActionException var9) {
            DiagnosticsLogger.logDuplicateActionInMonitor(var0.getName(), var2.getType(), var7);
         } catch (IncompatibleActionException var10) {
            DiagnosticsLogger.logIncompatibleAction(var0.getName(), var2.getType(), var7);
         }
      }

   }

   private static CustomMonitorControl readCustomMonitor(InstrumentationScope var0, WLDFInstrumentationMonitorBean var1) {
      InstrumentationLibrary var2 = InstrumentationLibrary.getInstrumentationLibrary();
      CustomMonitorControl var3 = null;
      String var4 = var1.getName();
      if ("_WL_INTERNAL_SERVER_SCOPE".equals(var0.getName())) {
         DiagnosticsLogger.logCustomMonitorInServerScopeError(var4);
         return null;
      } else if (!MonitorSpecification.isValidType(var4)) {
         DiagnosticsLogger.logInvalidCharactersInMonitorType(var0.getName(), var4);
         return null;
      } else {
         try {
            int var5 = MonitorSpecification.getLocationType(var1.getLocationType());
            if (var5 == 0) {
               DiagnosticsLogger.logMissingLocationForCustomMonitor(var4, var0.getName());
               return null;
            }

            String var6 = var1.getPointcut();
            PointcutExpression var7 = Factory.parse(var6);
            MonitorSpecification var8 = new MonitorSpecification(var4, var5, var7, true, true);
            var3 = new CustomMonitorControl(var4);
            var3.setLocationType(var5);
            var3.setMonitorSpecification(var8);
            var3.setPointcut(var6);
         } catch (Exception var9) {
            DiagnosticsLogger.logMonitorConfigurationError(var4, var0.getName(), var9);
         }

         return var3;
      }
   }

   private static DiagnosticMonitorControl createMonitorControl(InstrumentationScope var0, WLDFInstrumentationMonitorBean var1) {
      String var2 = var1.getName();
      DiagnosticMonitorControl var3 = null;
      if ("_WL_INTERNAL_SERVER_SCOPE".equals(var0.getName())) {
         InstrumentationManager var4 = InstrumentationManager.getInstrumentationManager();
         var3 = var4.getServerMonitor(var2);
      } else {
         InstrumentationLibrary var5 = InstrumentationLibrary.getInstrumentationLibrary();
         var3 = var5.getDiagnosticMonitorControl(var2);
      }

      if (var3 == null) {
         DiagnosticsLogger.logUnknownMonitorTypeInScope(var0.getName(), var2);
         return null;
      } else {
         if ("_WL_INTERNAL_SERVER_SCOPE".equals(var0.getName())) {
            if (!var3.isServerScopeAllowed()) {
               DiagnosticsLogger.logInvalidMonitorInServerScope(var2);
               var3 = null;
            }
         } else if (!var3.isComponentScopeAllowed()) {
            DiagnosticsLogger.logInvalidMonitorInApplicationScope(var2, var0.getName());
            var3 = null;
         }

         return var3;
      }
   }
}
