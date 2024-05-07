package weblogic.diagnostics.instrumentation;

import java.io.File;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.diagnostics.context.DiagnosticContextHelper;
import weblogic.diagnostics.context.InvalidDyeException;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.instrumentation.engine.InstrumentationEngineConfiguration;
import weblogic.diagnostics.instrumentation.engine.MonitorSpecification;
import weblogic.diagnostics.instrumentation.engine.base.InstrumentationEngineConstants;
import weblogic.diagnostics.type.UnexpectedExceptionHandler;
import weblogic.kernel.Kernel;
import weblogic.management.DomainDir;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.jars.ManifestManager;

public final class InstrumentationLibrary implements InstrumentationEngineConstants {
   private static InstrumentationLibrary singleton;
   private HashMap monitorMap = new HashMap();
   private HashMap serverManagedMonitorMap = new HashMap();
   private HashMap actionMap = new HashMap();
   private ClassLoader actionsClassLoader;
   private List statelessActionTypes = new ArrayList();
   private List aroundActionTypes = new ArrayList();
   private List<String> eventClassNamesInUse = new ArrayList();
   private Collection pointcutList = new ArrayList();
   private InstrumentationEngineConfiguration engineConf;
   private boolean valid;
   private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   private InstrumentationLibrary() {
   }

   public static synchronized InstrumentationLibrary getInstrumentationLibrary() {
      if (singleton == null) {
         InstrumentationLibrary var0 = new InstrumentationLibrary();
         var0.loadInstrumentationEngineConfiguration();
         singleton = var0;
      }

      return singleton;
   }

   public boolean isValid() {
      return this.valid;
   }

   public InstrumentationEngineConfiguration getInstrumentationEngineConfiguration() {
      return this.engineConf;
   }

   private void loadInstrumentationEngineConfiguration() {
      this.actionsClassLoader = Thread.currentThread().getContextClassLoader();
      if (this.actionsClassLoader == null) {
         this.actionsClassLoader = this.getClass().getClassLoader();
      }

      String var1 = null;
      if (Kernel.isServer()) {
         String var2 = null;
         RuntimeAccess var3 = ManagementService.getRuntimeAccess(kernelID);
         if (var3 != null) {
            var2 = var3.getServerName();
         }

         if (var2 == null || var2.equals("")) {
            var2 = System.getProperty("weblogic.Name", "myserver");
         }

         if ("".equals(var2)) {
            var2 = null;
         }

         if (var2 != null) {
            File var4 = new File(DomainDir.getPathRelativeServersCacheDir(var2, "diagnostics"));
            var4 = new File(var4, "InstrumentationEngineConfig.ser");
            var1 = var4.getAbsolutePath();
         }
      }

      this.engineConf = InstrumentationEngineConfiguration.getInstrumentationEngineConfiguration(var1);
      if (this.engineConf.isValid()) {
         Map var20 = this.engineConf.getDyeFlagsMap();
         Iterator var18 = var20.keySet().iterator();

         while(var18.hasNext()) {
            String var21 = (String)var18.next();
            int var5 = (Integer)var20.get(var21);

            try {
               DiagnosticContextHelper.registerDye(var21, var5);
            } catch (InvalidDyeException var17) {
               if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
                  InstrumentationDebug.DEBUG_CONFIG.debug("Failed to register dye: ", var17);
               }

               DiagnosticsLogger.logDyeRegistrationFailureError(var21, var5);
            }
         }

         String[] var19 = this.engineConf.getActionTypes();
         String[] var22 = this.engineConf.getGroupActionTypes("StatelessActions");
         String[] var23 = this.engineConf.getGroupActionTypes("AroundActions");
         int var6 = var19 != null ? var19.length : 0;

         String var9;
         DiagnosticAction var10;
         for(int var7 = 0; var7 < var6; ++var7) {
            String var8 = var19[var7];
            var9 = this.engineConf.getActionClassName(var8);

            try {
               var10 = (DiagnosticAction)Class.forName(var9).newInstance();
               this.actionMap.put(var8, var9);
               if (this.isInList(var8, var22)) {
                  this.statelessActionTypes.add(var8);
               } else if (this.isInList(var8, var23)) {
                  this.aroundActionTypes.add(var8);
               }
            } catch (Exception var16) {
               UnexpectedExceptionHandler.handle("Unknown diagnostic action " + var9, var16);
            }
         }

         Iterator var24 = this.engineConf.getAllMonitorSpecifications();

         while(var24.hasNext()) {
            MonitorSpecification var25 = (MonitorSpecification)var24.next();
            var9 = var25.getType();
            var10 = null;
            Object var26;
            if (var25.isStandardMonitor()) {
               var26 = new StandardMonitorControl(var9);
            } else {
               DelegatingMonitorControl var11 = new DelegatingMonitorControl(var9);
               String[] var12 = var25.getActionTypes();
               int var13 = var12 != null ? var12.length : 0;
               ArrayList var14 = new ArrayList();

               for(int var15 = 0; var15 < var13; ++var15) {
                  if (this.actionMap.get(var12[var15]) != null) {
                     var14.add(var12[var15]);
                  }
               }

               var12 = (String[])((String[])var14.toArray(new String[0]));
               var11.setCompatibleActionTypes(var12);
               var11.setLocationType(var25.getLocation());
               var26 = var11;
            }

            ((DiagnosticMonitorControl)var26).setAttributeNames(var25.getAttributeNames());
            ((DiagnosticMonitorControl)var26).setServerScopeAllowed(var25.isServerScoped());
            ((DiagnosticMonitorControl)var26).setComponentScopeAllowed(var25.isApplicationScoped());
            ((DiagnosticMonitorControl)var26).setServerManaged(var25.isServerManaged());
            ((DiagnosticMonitorControl)var26).setDiagnosticVolume(var25.getDiagnosticVolume());
            ((DiagnosticMonitorControl)var26).setEventClassName(var25.getEventClassName());
            if (var25.isServerManaged()) {
               String var27 = var25.getEventClassName();
               if (var27 != null && !this.eventClassNamesInUse.contains(var27)) {
                  this.eventClassNamesInUse.add(var27);
               }

               this.serverManagedMonitorMap.put(var9, var26);
            } else {
               this.monitorMap.put(var9, var26);
            }
         }

         this.readCustomActions();
         this.valid = true;
      }
   }

   private boolean isInList(String var1, String[] var2) {
      int var3 = var2 != null ? var2.length : 0;

      for(int var4 = 0; var4 < var3; ++var4) {
         if (var1.equals(var2[var4])) {
            return true;
         }
      }

      return false;
   }

   public DiagnosticMonitorControl[] getAvailableMonitors() {
      int var1 = this.monitorMap.size();
      DiagnosticMonitorControl[] var2 = new DiagnosticMonitorControl[var1];
      int var3 = 0;

      String var5;
      for(Iterator var4 = this.monitorMap.keySet().iterator(); var4.hasNext(); var2[var3++] = this.getDiagnosticMonitorControl(var5)) {
         var5 = (String)var4.next();
      }

      return var2;
   }

   public DiagnosticMonitorControl[] getAllAvailableMonitors() {
      int var1 = this.monitorMap.size() + this.serverManagedMonitorMap.size();
      DiagnosticMonitorControl[] var2 = new DiagnosticMonitorControl[var1];
      int var3 = 0;

      Iterator var4;
      String var5;
      for(var4 = this.monitorMap.keySet().iterator(); var4.hasNext(); var2[var3++] = this.getDiagnosticMonitorControl(var5)) {
         var5 = (String)var4.next();
      }

      for(var4 = this.serverManagedMonitorMap.keySet().iterator(); var4.hasNext(); var2[var3++] = this.getDiagnosticMonitorControl(var5)) {
         var5 = (String)var4.next();
      }

      return var2;
   }

   public DiagnosticMonitorControl getDiagnosticMonitorControl(String var1) {
      DiagnosticMonitorControl var2 = (DiagnosticMonitorControl)this.monitorMap.get(var1);
      if (var2 == null) {
         var2 = (DiagnosticMonitorControl)this.serverManagedMonitorMap.get(var1);
         if (var2 == null) {
            return null;
         }
      }

      Object var3 = null;
      if (var2 instanceof CustomMonitorControl) {
         var3 = new CustomMonitorControl((CustomMonitorControl)var2);
      } else if (var2 instanceof DelegatingMonitorControl) {
         var3 = new DelegatingMonitorControl((DelegatingMonitorControl)var2);
      } else if (var2 instanceof StandardMonitorControl) {
         var3 = new StandardMonitorControl((StandardMonitorControl)var2);
      }

      return (DiagnosticMonitorControl)var3;
   }

   public DiagnosticAction[] getAvailableActions() {
      int var1 = this.actionMap.size();
      DiagnosticAction[] var2 = new DiagnosticAction[var1];
      int var3 = 0;

      String var5;
      for(Iterator var4 = this.actionMap.keySet().iterator(); var4.hasNext(); var2[var3++] = this.getDiagnosticAction(var5)) {
         var5 = (String)var4.next();
      }

      return var2;
   }

   public DiagnosticAction getDiagnosticAction(String var1) {
      String var2 = (String)this.actionMap.get(var1);
      if (var2 == null) {
         return null;
      } else {
         DiagnosticAction var3 = null;

         try {
            var3 = (DiagnosticAction)this.actionsClassLoader.loadClass(var2).newInstance();
         } catch (Exception var5) {
            UnexpectedExceptionHandler.handle("Unknown diagnostic action type " + var1, var5);
         }

         var3.setType(var1);
         return var3;
      }
   }

   public String[] getActionTypes() {
      ArrayList var1 = new ArrayList(this.statelessActionTypes);
      var1.addAll(this.aroundActionTypes);
      String[] var2 = new String[var1.size()];
      return (String[])((String[])var1.toArray(var2));
   }

   public String[] getStatelessDiagnosticActionTypes() {
      String[] var1 = new String[this.statelessActionTypes.size()];
      return (String[])((String[])this.statelessActionTypes.toArray(var1));
   }

   public String[] getAroundDiagnosticActionTypes() {
      String[] var1 = new String[this.aroundActionTypes.size()];
      return (String[])((String[])this.aroundActionTypes.toArray(var1));
   }

   public String getActionClassname(String var1) {
      return (String)this.actionMap.get(var1);
   }

   private void readCustomActions() {
      ArrayList var1 = ManifestManager.getServices(DiagnosticAction.class);
      Iterator var2 = var1.iterator();

      while(true) {
         String var4;
         String var5;
         while(true) {
            DiagnosticAction var3;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               var3 = (DiagnosticAction)var2.next();
               var4 = var3.getType();
               var5 = var3.getClass().getName();
            } while(this.isAlreadyRegistered(var4, var5));

            if (var3 instanceof StatelessDiagnosticAction) {
               this.statelessActionTypes.add(var4);
               break;
            }

            if (var3 instanceof AroundDiagnosticAction) {
               this.aroundActionTypes.add(var4);
               break;
            }
         }

         this.actionMap.put(var4, var5);
         if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
            InstrumentationDebug.DEBUG_CONFIG.debug("Found custom action class: " + var5);
         }
      }
   }

   private boolean isAlreadyRegistered(String var1, String var2) {
      Iterator var3 = this.actionMap.keySet().iterator();

      String var4;
      String var5;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         var4 = (String)var3.next();
         var5 = (String)this.actionMap.get(var4);
      } while(!var4.equals(var1) && !var5.equals(var2));

      if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_CONFIG.debug("Action type " + var1 + " or action class " + var2 + " is already in use");
      }

      return true;
   }

   public boolean isGatheringExtended() {
      String[] var1 = this.engineConf.getGroupActionTypes("DataGatheringStatelessActions");
      String[] var2 = this.engineConf.getGroupActionTypes("DataGatheringAroundActions");
      int var3 = var1.length + var2.length;
      return var3 > 2;
   }

   public Map<String, String> getValueRenderersByType() {
      return this.engineConf.getValueRenderersByType();
   }

   public List<String> getEventClassNamesInUse() {
      return this.eventClassNamesInUse;
   }
}
