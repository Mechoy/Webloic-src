package weblogic.diagnostics.instrumentation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import weblogic.Server;
import weblogic.diagnostics.accessor.DataRecord;
import weblogic.diagnostics.descriptor.WLDFInstrumentationBean;
import weblogic.diagnostics.descriptor.WLDFResourceBean;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.image.ImageManager;
import weblogic.diagnostics.instrumentation.engine.InstrumentationEngineConfiguration;
import weblogic.diagnostics.instrumentation.gathering.DataGatheringManager;
import weblogic.diagnostics.instrumentation.support.DyeInjectionMonitorSupport;
import weblogic.diagnostics.type.FeatureNotAvailableException;
import weblogic.diagnostics.type.UnexpectedExceptionHandler;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.WLDFServerDiagnosticMBean;
import weblogic.management.configuration.WLDFSystemResourceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public final class InstrumentationManager implements PropertyChangeListener {
   private static InstrumentationManager singleton;
   private boolean valid;
   private List instrumentationScopes = new ArrayList();
   private InstrumentationScope serverInstrumentationScope;
   private boolean synchronousEventPersistence;
   private long eventsPersistenceInterval;
   private InstrumentationEventListener[] eventsListeners;
   private Map classLoaderMap = Collections.synchronizedMap(new WeakHashMap());
   private Map serverMonitors = new HashMap();
   private Map<String, DiagnosticMonitorControl> serverManagedMonitors = new HashMap();
   private static final String WEBLOGIC_INSTRUMENTATION_SERVER_MANAGED_SCOPE = "_WL_INTERNAL_SERVER_MANAGED_SCOPE";
   private InstrumentationScope serverManagedScope;
   private InstrumentationStatistics statsForDeletedScopes = new InstrumentationStatistics();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   private InstrumentationManager() {
      this.createServerManagedScope();
      this.eventsListeners = new InstrumentationEventListener[0];
      DiagnosticMonitor[] var1 = this.getAvailableMonitors();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         DiagnosticMonitor var3 = var1[var2];
         if (var3.isServerScopeAllowed()) {
            if (var3.isServerManaged()) {
               if (var3 instanceof DiagnosticMonitorControl) {
                  try {
                     if (var3 instanceof DelegatingMonitorControl) {
                        this.setupActionList((DelegatingMonitorControl)var3);
                     }

                     this.serverManagedScope.addMonitorControl((DiagnosticMonitorControl)var3);
                     this.serverManagedMonitors.put(var3.getType(), (DiagnosticMonitorControl)var3);
                  } catch (DuplicateMonitorException var10) {
                  }
               }
            } else {
               this.serverMonitors.put(var3.getType(), var3);
            }
         }
      }

      InstrumentationLibrary var11 = InstrumentationLibrary.getInstrumentationLibrary();
      if (!var11.isValid()) {
         DiagnosticsLogger.logInvalidInstrumentationLibraryError();
      } else {
         DataGatheringManager.setEventClassNamesInUse(var11.getEventClassNamesInUse());
         ValueRenderingManager.initialize(var11.getValueRenderersByType());
         DiagnosticMonitorControl var12 = (DiagnosticMonitorControl)this.serverMonitors.get("DyeInjection");
         if (var12 != null) {
            InstrumentationEngineConfiguration var4 = var11.getInstrumentationEngineConfiguration();
            Map var5 = var4.getDyeFlagsMap();
            HashSet var6 = new HashSet();
            String[] var7 = var12.getAttributeNames();
            int var8 = var7 != null ? var7.length : 0;

            for(int var9 = 0; var9 < var8; ++var9) {
               var6.add(var7[var9]);
            }

            var6.addAll(var5.keySet());
            var7 = (String[])((String[])var6.toArray(new String[0]));
            var12.setAttributeNames(var7);
            this.valid = true;
         } else {
            DiagnosticsLogger.logMissingDiagnosticMonitor("DyeInjection");
         }

      }
   }

   public static synchronized InstrumentationManager getInstrumentationManager() {
      if (singleton == null) {
         singleton = new InstrumentationManager();
      }

      return singleton;
   }

   public void initialize() {
      try {
         InstrumentationScope var1 = this.getServerInstrumentationScope();
         if (Boolean.getBoolean("weblogic.diagnostics.instrumentation.VerifyEventClasses")) {
            this.verifyEnabledServerManagedMonitorEventClassesLoadable();
         }

         this.updateServerManagedMonitors(DataGatheringManager.getDiagnosticVolume());
      } catch (Exception var2) {
         DiagnosticsLogger.logServerInstrumentationScopeInitializationError(var2);
      }

   }

   public void initializeInstrumentationParameters() {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      ServerMBean var2 = var1.getServer();
      WLDFServerDiagnosticMBean var3 = var2 != null ? var2.getServerDiagnosticConfig() : null;
      if (var3 != null) {
         this.eventsPersistenceInterval = var3.getEventPersistenceInterval();
         this.synchronousEventPersistence = var3.isSynchronousEventPersistenceEnabled();
         EventQueue.getInstance().setTimerInterval(this.eventsPersistenceInterval);
         ImageManager var4 = ImageManager.getInstance();
         var4.registerImageSource("InstrumentationImageSource", new InstrumentationImageSource());
         var3.addPropertyChangeListener(this);
      }
   }

   InstrumentationScope getServerInstrumentationScope() throws Exception {
      InstrumentationScope var1 = null;
      RuntimeAccess var2 = ManagementService.getRuntimeAccess(kernelId);
      String var3 = var2.getServerName();
      DomainMBean var4 = var2.getDomain();
      if (var4 == null) {
         return null;
      } else {
         WLDFSystemResourceMBean[] var5 = var4.getWLDFSystemResources();
         int var6 = var5 != null ? var5.length : 0;

         for(int var7 = 0; var1 == null && var7 < var6; ++var7) {
            WLDFSystemResourceMBean var8 = var5[var7];
            TargetMBean[] var9 = var8.getTargets();
            int var10 = var9 != null ? var9.length : 0;

            for(int var11 = 0; var1 == null && var11 < var10; ++var11) {
               if (var9[var11].getServerNames().contains(var3)) {
                  WLDFResourceBean var12 = var8.getWLDFResource();
                  if (var12 == null) {
                     throw new IllegalArgumentException(DiagnosticsLogger.logWLDFResourceBeanNotFoundLoggable(var8.getName()).getMessage());
                  }

                  WLDFInstrumentationBean var13 = var12.getInstrumentation();
                  if (var13 != null) {
                     var1 = this.createInstrumentationScope("_WL_INTERNAL_SERVER_SCOPE", var13);
                  }
               }
            }
         }

         this.serverInstrumentationScope = var1;
         return var1;
      }
   }

   public DiagnosticMonitorControl getServerMonitor(String var1) {
      return (DiagnosticMonitorControl)this.serverMonitors.get(var1);
   }

   public static void initializeServerScope(ClassLoader var0, byte[] var1) throws Exception {
      ByteArrayInputStream var2 = new ByteArrayInputStream(var1);
      ObjectInputStream var3 = new ObjectInputStream(var2);
      InstrumentationScope var4 = (InstrumentationScope)var3.readObject();
      var3.close();
      InstrumentationManager var5 = getInstrumentationManager();
      var5.addInstrumentationScope("_WL_INTERNAL_SERVER_SCOPE", var4, false);
      var5.associateClassloaderWithScope(var0, var4);
   }

   public boolean isHotswapAvailable() {
      return Server.isRedefineClassesSupported();
   }

   public DiagnosticMonitor[] getAvailableMonitors() {
      DiagnosticMonitorControl[] var1 = InstrumentationLibrary.getInstrumentationLibrary().getAllAvailableMonitors();
      return var1;
   }

   public DiagnosticAction[] getAvailableActions() {
      DiagnosticAction[] var1 = InstrumentationLibrary.getInstrumentationLibrary().getAvailableActions();
      return var1;
   }

   public InstrumentationScope[] getInstrumentationScopes() {
      synchronized(this.instrumentationScopes) {
         InstrumentationScope[] var2 = new InstrumentationScope[this.instrumentationScopes.size()];
         return (InstrumentationScope[])((InstrumentationScope[])this.instrumentationScopes.toArray(var2));
      }
   }

   public InstrumentationScope createInstrumentationScope(String var1) throws ScopeAlreadyExistsException {
      if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_CONFIG.debug("Creating instrumentation scope: " + var1);
      }

      InstrumentationScope var2 = new InstrumentationScope(var1);
      this.addInstrumentationScope(var1, var2);
      return var2;
   }

   public InstrumentationScope createInstrumentationScope(String var1, WLDFInstrumentationBean var2) throws ScopeAlreadyExistsException {
      if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_CONFIG.debug("Creating instrumentation scope: " + var1);
      }

      InstrumentationScope var3 = InstrumentationScope.createInstrumentationScope(var1, var2);
      this.addInstrumentationScope(var1, var3);
      if ("_WL_INTERNAL_SERVER_SCOPE".equals(var1)) {
         this.serverInstrumentationScope = var3;
      }

      return var3;
   }

   private void addInstrumentationScope(String var1, InstrumentationScope var2) throws ScopeAlreadyExistsException {
      this.addInstrumentationScope(var1, var2, true);
   }

   private synchronized void addInstrumentationScope(String var1, InstrumentationScope var2, boolean var3) throws ScopeAlreadyExistsException {
      if (!this.isValid()) {
         var2.setEnabled(false);
      }

      synchronized(this.instrumentationScopes) {
         if (this.findInstrumentationScope(var1) != null) {
            throw new ScopeAlreadyExistsException("Instrumentation scope already exists for " + var1);
         } else {
            if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
               InstrumentationDebug.DEBUG_CONFIG.debug("Adding instrumentation scope: " + var1);
            }

            var2.initialize();
            this.instrumentationScopes.add(var2);
            if (var3) {
               this.installDyeInjectionMonitor(var2);
            }

         }
      }
   }

   private boolean hasEnabledScope() {
      Iterator var1 = this.instrumentationScopes.iterator();

      InstrumentationScope var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (InstrumentationScope)var1.next();
      } while(!var2.isEnabled());

      return true;
   }

   public void installDyeInjectionMonitor(InstrumentationScope var1) {
      InstrumentationScope var2 = this.findInstrumentationScope("_WL_INTERNAL_SERVER_SCOPE");
      Object var3 = null;
      if (var2 != null) {
         var3 = var2.findDiagnosticMonitorControl("DyeInjection");
      }

      boolean var4 = var3 != null ? ((DiagnosticMonitorControl)var3).isEnabled() : false;
      if (this.hasEnabledScope()) {
         if (var3 == null || !var4) {
            var3 = new StandardMonitorControl("DyeInjection");
         }
      } else {
         var3 = null;
      }

      DyeInjectionMonitorSupport.setDyeInjectionMonitor((DiagnosticMonitorControl)var3);
   }

   public void installDyeInjectionMonitor_OLD(InstrumentationScope var1) {
      if (var1.getName().equals("_WL_INTERNAL_SERVER_SCOPE")) {
         DiagnosticMonitorControl var2 = var1.findDiagnosticMonitorControl("DyeInjection");
         if (var2 != null && !var2.isEnabled()) {
            var2 = null;
         }

         DyeInjectionMonitorSupport.setDyeInjectionMonitor(var2);
      }

   }

   public InstrumentationScope findInstrumentationScope(String var1) {
      synchronized(this.instrumentationScopes) {
         Iterator var3 = this.instrumentationScopes.iterator();

         InstrumentationScope var4;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            var4 = (InstrumentationScope)var3.next();
         } while(!var1.equals(var4.getName()));

         return var4;
      }
   }

   public void deleteInstrumentationScope(InstrumentationScope var1) {
      String var2 = var1.getName();
      synchronized(this.instrumentationScopes) {
         if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
            InstrumentationDebug.DEBUG_CONFIG.debug("Deleting instrumentation scope: " + var2);
         }

         this.statsForDeletedScopes.add(var1.getInstrumentationStatistics());
         DiagnosticMonitorControl[] var4 = var1.getMonitorControlsInScope();
         int var5 = var4 != null ? var4.length : 0;
         int var6 = 0;

         while(true) {
            if (var6 >= var5) {
               this.instrumentationScopes.remove(var1);
               break;
            }

            var4[var6].setEnabled(false);
            ++var6;
         }
      }

      if ("_WL_INTERNAL_SERVER_SCOPE".equals(var2)) {
         this.serverInstrumentationScope = null;
         DyeInjectionMonitorSupport.setDyeInjectionMonitor((DiagnosticMonitorControl)null);
      }

   }

   public void setDyeMask(InstrumentationScope var1, long var2) {
      DiagnosticMonitorControl[] var4 = var1.getMonitorControlsInScope();
      int var5 = var4 != null ? var4.length : 0;

      for(int var6 = 0; var6 < var5; ++var6) {
         this.setDyeMask((DiagnosticMonitor)var4[var6], var2);
      }

   }

   public void setDyeMask(DiagnosticMonitor var1, long var2) {
      var1.setDyeMask(var2);
   }

   public void setDyeMask(InstrumentationScope var1, String var2, long var3) {
      DiagnosticMonitorControl[] var5 = var1.getMonitorControlsInScope();
      int var6 = var5 != null ? var5.length : 0;

      for(int var7 = 0; var7 < var6; ++var7) {
         DiagnosticMonitorControl var8 = var5[var7];
         this.setDyeMask((DiagnosticMonitor)var8, var3);
      }

   }

   public void weaveClass(InstrumentationScope var1, String var2) throws FeatureNotAvailableException, ClassNotFoundException, InstrumentationException {
   }

   public void associateClassloaderWithScope(ClassLoader var1, InstrumentationScope var2) {
      this.classLoaderMap.put(var1, var2);
   }

   public InstrumentationScope getAssociatedScope(ClassLoader var1) {
      return (InstrumentationScope)this.classLoaderMap.get(var1);
   }

   private List array2list(Object[] var1) {
      ArrayList var2 = new ArrayList();
      int var3 = var1 != null ? var1.length : 0;

      for(int var4 = 0; var4 < var3; ++var4) {
         var2.add(var1[var4]);
      }

      return var2;
   }

   public void addInstrumentationEventListener(InstrumentationEventListener var1) {
      synchronized(this) {
         List var3 = this.array2list(this.eventsListeners);
         if (!var3.contains(var1)) {
            var3.add(var1);
            InstrumentationEventListener[] var4 = new InstrumentationEventListener[var3.size()];
            this.eventsListeners = (InstrumentationEventListener[])((InstrumentationEventListener[])var3.toArray(var4));
         }

      }
   }

   public void removeInstrumentationEventListener(InstrumentationEventListener var1) {
      synchronized(this) {
         List var3 = this.array2list(this.eventsListeners);
         if (var3.contains(var1)) {
            var3.remove(var1);
            InstrumentationEventListener[] var4 = new InstrumentationEventListener[var3.size()];
            this.eventsListeners = (InstrumentationEventListener[])((InstrumentationEventListener[])var3.toArray(var4));
         }

      }
   }

   public InstrumentationStatistics getInstrumentationStatistics() {
      InstrumentationStatistics var1 = new InstrumentationStatistics();
      var1.add(this.statsForDeletedScopes);
      Iterator var2 = this.instrumentationScopes.iterator();

      while(var2.hasNext()) {
         InstrumentationScope var3 = (InstrumentationScope)var2.next();
         var1.add(var3.getInstrumentationStatistics());
      }

      return var1;
   }

   void propagateInstrumentationEvents(List var1) {
      InstrumentationEventListener[] var2 = this.eventsListeners;
      int var3 = var2 != null ? var2.length : 0;

      for(int var4 = 0; var4 < var3; ++var4) {
         InstrumentationEventListener var5 = var2[var4];

         try {
            Iterator var6 = var1.iterator();

            while(var6.hasNext()) {
               DataRecord var7 = (DataRecord)var6.next();
               var5.handleInstrumentationEvent(var7);
            }
         } catch (Exception var8) {
            UnexpectedExceptionHandler.handle("Could not propagate events to listener " + var5, var8);
         }
      }

   }

   public boolean isValid() {
      return this.valid;
   }

   public boolean isEnabled() {
      return this.valid && this.serverInstrumentationScope != null ? this.serverInstrumentationScope.isEnabled() : false;
   }

   public boolean isSynchronousEventPersistenceEnabled() {
      return this.synchronousEventPersistence;
   }

   public void propertyChange(PropertyChangeEvent var1) {
      this.attributesChanged(var1.getSource());
   }

   private void attributesChanged(Object var1) {
      if (var1 instanceof WLDFServerDiagnosticMBean) {
         WLDFServerDiagnosticMBean var2 = (WLDFServerDiagnosticMBean)var1;
         long var3 = var2.getEventPersistenceInterval();
         if (var3 != this.eventsPersistenceInterval) {
            EventQueue.getInstance().setTimerInterval(var3);
            this.eventsPersistenceInterval = var3;
         }

         this.synchronousEventPersistence = var2.isSynchronousEventPersistenceEnabled();
      }

   }

   Map findAttachedActionTypes() {
      HashMap var1 = new HashMap();
      if (this.serverInstrumentationScope != null) {
         this.serverInstrumentationScope.findAttachedActionTypes(var1);
      }

      if (this.instrumentationScopes != null) {
         Iterator var2 = this.instrumentationScopes.iterator();

         while(var2.hasNext()) {
            InstrumentationScope var3 = (InstrumentationScope)var2.next();
            var3.findAttachedActionTypes(var1);
         }
      }

      return var1;
   }

   static boolean isKernelIdentity() {
      AuthenticatedSubject var0 = SecurityServiceManager.getCurrentSubject(kernelId);
      return SecurityServiceManager.isKernelIdentity(var0);
   }

   public void updateServerManagedMonitors(int var1) {
      if (this.serverManagedMonitors != null && !this.serverManagedMonitors.isEmpty()) {
         boolean var2 = DataGatheringManager.isGatheringEnabled();
         boolean var3 = DataGatheringManager.jfrActionsDisabled();
         Iterator var4 = this.serverManagedMonitors.values().iterator();

         while(true) {
            while(true) {
               DiagnosticMonitorControl var5;
               do {
                  label51:
                  do {
                     for(; var4.hasNext(); var5.setEnabled(false)) {
                        var5 = (DiagnosticMonitorControl)var4.next();
                        if (var2 && DataGatheringManager.convertVolume(var5.getDiagnosticVolume()) <= var1) {
                           if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
                              InstrumentationDebug.DEBUG_CONFIG.debug("monitor enabled: " + var5.getType());
                           }

                           var5.setEnabled(true);
                           continue label51;
                        }

                        if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
                           InstrumentationDebug.DEBUG_CONFIG.debug("monitor disabled: " + var5.getType());
                        }
                     }

                     return;
                  } while(!var3);
               } while(!(var5 instanceof DelegatingMonitorControl));

               DelegatingMonitorControl var6 = (DelegatingMonitorControl)var5;
               DiagnosticAction[] var7 = var6.getActions();
               int var8 = var7.length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  DiagnosticAction var10 = var7[var9];
                  if (var10.getType().equals("FlightRecorderAroundAction") || var10.getType().equals("FlightRecorderStatelessAction")) {
                     try {
                        if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
                           InstrumentationDebug.DEBUG_CONFIG.debug("Removing " + var10.getType() + " from monitor: " + var5.getType());
                        }

                        var6.removeAction(var10);
                        if (var10.requiresArgumentsCapture()) {
                           var6.subvertArgumentsCaptureNeededCheck();
                        }
                     } catch (ActionNotFoundException var12) {
                     }
                     break;
                  }
               }
            }
         }
      }
   }

   private void createServerManagedScope() {
      if (this.serverManagedScope == null) {
         this.serverManagedScope = InstrumentationScope.createInstrumentationScope("_WL_INTERNAL_SERVER_MANAGED_SCOPE", (WLDFInstrumentationBean)null);
      }
   }

   private void setupActionList(DelegatingMonitorControl var1) {
      InstrumentationLibrary var2 = InstrumentationLibrary.getInstrumentationLibrary();
      String[] var3 = var1.getCompatibleActionTypes();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];

         try {
            DiagnosticAction var7 = var2.getDiagnosticAction(var6);
            if (var7 == null) {
               DiagnosticsLogger.logNonExistentActionType(this.serverManagedScope.getName(), var1.getType(), var6);
            } else {
               var1.addAction(var7);
            }
         } catch (DuplicateActionException var8) {
            DiagnosticsLogger.logDuplicateActionInMonitor(this.serverManagedScope.getName(), var1.getType(), var6);
         } catch (IncompatibleActionException var9) {
            DiagnosticsLogger.logIncompatibleAction(this.serverManagedScope.getName(), var1.getType(), var6);
         }
      }

   }

   public DiagnosticMonitorControl getServerManagedMonitorControl(String var1) {
      return var1 != null ? (DiagnosticMonitorControl)this.serverManagedMonitors.get(var1) : null;
   }

   private boolean verifyEnabledServerManagedMonitorEventClassesLoadable() {
      boolean var1 = true;
      if (this.serverManagedMonitors != null && !this.serverManagedMonitors.isEmpty()) {
         Iterator var2 = this.serverManagedMonitors.values().iterator();

         while(true) {
            while(var2.hasNext()) {
               DiagnosticMonitorControl var3 = (DiagnosticMonitorControl)var2.next();
               String var4 = var3.getEventClassName();
               if (var4 == null) {
                  if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
                     InstrumentationDebug.DEBUG_CONFIG.debug("Monitor has null event class name: " + var3.getType());
                  }
               } else {
                  try {
                     Class var5 = Class.forName(var4);
                     if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
                        InstrumentationDebug.DEBUG_CONFIG.debug("Loaded event class for Monitor : " + var3.getType());
                     }
                  } catch (Throwable var6) {
                     var1 = false;
                     if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
                        InstrumentationDebug.DEBUG_CONFIG.debug("Unable to load event class: " + var4 + ", specified by Monitor: " + var3.getType(), var6);
                     }
                  }
               }
            }

            return var1;
         }
      } else {
         return var1;
      }
   }

   public boolean isGatheringExtended() {
      InstrumentationLibrary var1 = InstrumentationLibrary.getInstrumentationLibrary();
      return var1.isGatheringExtended();
   }

   public Map<String, String> getValueRenderersByType() {
      InstrumentationLibrary var1 = InstrumentationLibrary.getInstrumentationLibrary();
      return var1.getValueRenderersByType();
   }
}
