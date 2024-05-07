package weblogic.diagnostics.instrumentation.rtsupport;

import java.util.HashMap;
import java.util.Map;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.instrumentation.AroundDiagnosticAction;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DiagnosticMonitor;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.DynamicJoinPointImpl;
import weblogic.diagnostics.instrumentation.InstrumentationSupportBase;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.JoinPointImpl;
import weblogic.diagnostics.instrumentation.LocalHolder;
import weblogic.diagnostics.instrumentation.MonitorLocalHolder;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfoImpl;
import weblogic.diagnostics.instrumentation.StatelessDiagnosticAction;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfoImpl;
import weblogic.diagnostics.type.UnexpectedExceptionHandler;
import weblogic.spring.monitoring.actions.AbstractApplicationContextRefreshAction;
import weblogic.spring.monitoring.actions.AbstractBeanFactoryCreateBeanAction;
import weblogic.spring.monitoring.actions.AbstractBeanFactoryGetBeanAction;
import weblogic.spring.monitoring.actions.AbstractBeanFactoryRegisterScopeAction;
import weblogic.spring.monitoring.actions.AbstractPlatformTransactionManagerCommitAction;
import weblogic.spring.monitoring.actions.AbstractPlatformTransactionManagerResumeAction;
import weblogic.spring.monitoring.actions.AbstractPlatformTransactionManagerRollbackAction;
import weblogic.spring.monitoring.actions.AbstractPlatformTransactionManagerSuspendAction;
import weblogic.spring.monitoring.actions.ApplicationContextObtainFreshBeanFactoryAction;
import weblogic.spring.monitoring.actions.DefaultListableBeanFactoryGetBeanNamesForTypeAction;
import weblogic.spring.monitoring.actions.DefaultListableBeanFactoryGetBeansOfTypeAction;

public class SpringInstSupportImpl extends InstrumentationSupportBase {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugSpringStatistics");
   private static boolean legacyUnknownSpringMessageIssued = false;
   private static boolean legacySpringMessageIssued = false;
   private static final ValueHandlingInfo LEGACY_SPRING_INFO = new ValueHandlingInfoImpl((String)null, (String)null, false, false);
   private static Map<String, SpringDelegatingMonitor> monitorMap = new HashMap();

   public static synchronized DiagnosticMonitor getMonitor(Class var0, String var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringInstSupportImpl.getMonitor: clz=" + var0.getName() + " type=" + var1);
      }

      SpringDelegatingMonitor var2 = (SpringDelegatingMonitor)monitorMap.get(var1);
      if (var2 == null) {
         var2 = new SpringDelegatingMonitor(var1);
         Object var3;
         if ("Spring_Around_Internal_Application_Context_Obtain_Fresh_Bean_Factory".equals(var1)) {
            var3 = new ApplicationContextObtainFreshBeanFactoryAction();
         } else if ("Spring_Around_Internal_Abstract_Application_Context_Refresh".equals(var1)) {
            var3 = new AbstractApplicationContextRefreshAction();
         } else if ("Spring_Around_Internal_Abstract_Bean_Factory_Create_Bean".equals(var1)) {
            var3 = new AbstractBeanFactoryCreateBeanAction();
         } else if ("Spring_Around_Internal_Abstract_Bean_Factory_Get_Bean".equals(var1)) {
            var3 = new AbstractBeanFactoryGetBeanAction();
         } else if ("Spring_Around_Internal_Abstract_Bean_Factory_Register_Scope".equals(var1)) {
            var3 = new AbstractBeanFactoryRegisterScopeAction();
         } else if ("Spring_Around_Internal_Abstract_Platform_Transaction_Manager_Commit".equals(var1)) {
            var3 = new AbstractPlatformTransactionManagerCommitAction();
         } else if ("Spring_Around_Internal_Abstract_Platform_Transaction_Manager_Resume".equals(var1)) {
            var3 = new AbstractPlatformTransactionManagerResumeAction();
         } else if ("Spring_Around_Internal_Abstract_Platform_Transaction_Manager_Rollback".equals(var1)) {
            var3 = new AbstractPlatformTransactionManagerRollbackAction();
         } else if ("Spring_Around_Internal_Abstract_Platform_Transaction_Manager_Suspend".equals(var1)) {
            var3 = new AbstractPlatformTransactionManagerSuspendAction();
         } else if ("Spring_Around_Internal_Default_Listable_Bean_Factory_Get_Bean_Names_For_Type".equals(var1)) {
            var3 = new DefaultListableBeanFactoryGetBeanNamesForTypeAction();
         } else {
            if (!"Spring_Around_Internal_Default_Listable_Bean_Factory_Get_Beans_Of_Type".equals(var1)) {
               return null;
            }

            var3 = new DefaultListableBeanFactoryGetBeansOfTypeAction();
         }

         ((AroundDiagnosticAction)var3).setDiagnosticMonitor(var2);
         DiagnosticAction[] var4 = new DiagnosticAction[]{(DiagnosticAction)var3};
         var2.setActions(var4);
         monitorMap.put(var1, var2);
      }

      return var2;
   }

   public static JoinPoint createJoinPoint(Class var0, String var1, String var2, String var3, String var4, int var5) {
      Map var6 = null;
      boolean var7 = false;
      if (var3 != null) {
         if (var3.equals("obtainFreshBeanFactory")) {
            var6 = createSpringLegacyMap("Spring_Around_Internal_Application_Context_Obtain_Fresh_Bean_Factory", true, true, 0);
         } else if (var3.equals("refresh")) {
            var6 = createSpringLegacyMap("Spring_Around_Internal_Abstract_Application_Context_Refresh", false, true, 0);
         } else if (var3.equals("createBean")) {
            var6 = createSpringLegacyMap("Spring_Around_Internal_Abstract_Bean_Factory_Create_Bean", false, true, 0);
         } else if (var3.equals("getBean")) {
            var6 = createSpringLegacyMap("Spring_Around_Internal_Abstract_Bean_Factory_Get_Bean", false, true, 0);
         } else if (var3.equals("registerScope")) {
            var6 = createSpringLegacyMap("Spring_Around_Internal_Abstract_Bean_Factory_Register_Scope", false, true, 2);
         } else if (var3.equals("commit")) {
            var6 = createSpringLegacyMap("Spring_Around_Internal_Abstract_Platform_Transaction_Manager_Commit", false, true, 0);
         } else if (var3.equals("resume")) {
            var6 = createSpringLegacyMap("Spring_Around_Internal_Abstract_Platform_Transaction_Manager_Resume", false, true, 0);
         } else if (var3.equals("rollback")) {
            var6 = createSpringLegacyMap("Spring_Around_Internal_Abstract_Platform_Transaction_Manager_Rollback", false, true, 0);
         } else if (var3.equals("suspend")) {
            var6 = createSpringLegacyMap("Spring_Around_Internal_Abstract_Platform_Transaction_Manager_Suspend", false, true, 0);
         } else if (var3.equals("getBeanNamesForType")) {
            var6 = createSpringLegacyMap("Spring_Around_Internal_Default_Listable_Bean_Factory_Get_Bean_Names_For_Type", false, true, 0);
         } else if (var3.equals("getBeansOfType")) {
            var6 = createSpringLegacyMap("Spring_Around_Internal_Default_Listable_Bean_Factory_Get_Beans_Of_Type", false, true, 0);
         } else if (!legacyUnknownSpringMessageIssued) {
            DiagnosticsLogger.logLegacySpringInstrumentationUnknownMethod(var3);
            legacyUnknownSpringMessageIssued = true;
         }
      }

      if (var6 != null && !legacySpringMessageIssued) {
         DiagnosticsLogger.logLegacySpringInstrumentationCalled();
         legacySpringMessageIssued = true;
      }

      return new JoinPointImpl(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   private static Map<String, PointcutHandlingInfo> createSpringLegacyMap(String var0, boolean var1, boolean var2, int var3) {
      ValueHandlingInfo var4 = var1 ? LEGACY_SPRING_INFO : null;
      ValueHandlingInfo var5 = var2 ? LEGACY_SPRING_INFO : null;
      ValueHandlingInfo[] var6 = null;
      if (var3 > 0) {
         var6 = new ValueHandlingInfo[var3];

         for(int var7 = 0; var7 < var3; ++var7) {
            var6[var7] = LEGACY_SPRING_INFO;
         }
      }

      PointcutHandlingInfoImpl var9 = new PointcutHandlingInfoImpl(var5, var4, var6);
      HashMap var8 = new HashMap();
      var8.put(var0, var9);
      return var8;
   }

   public static JoinPoint createJoinPoint(Class var0, String var1, String var2, String var3, String var4, int var5, Map<String, PointcutHandlingInfo> var6, boolean var7) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Executing SpringInstSupportImpl.createJoinPoint");
      }

      return new JoinPointImpl(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   public static DynamicJoinPoint createDynamicJoinPoint(JoinPoint var0, Object[] var1, Object var2) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Executing SpringInstSupportImpl.createDynamicJoinPoint");
      }

      return new DynamicJoinPointImpl(var0, var1, var2);
   }

   public static void createDynamicJoinPoint(LocalHolder var0) {
      MonitorLocalHolder var1 = var0.monitorHolder[var0.monitorIndex];
      var1.djp = createDynamicJoinPoint((JoinPoint)(var1.djp == null ? var0.jp : var1.djp), var1.captureArgs ? var0.args : null, var0.ret);
   }

   public static PointcutHandlingInfo createPointcutHandlingInfo(ValueHandlingInfo var0, ValueHandlingInfo var1, ValueHandlingInfo[] var2) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Executing SpringInstSupportImpl.createPointcutHandlingInfo");
      }

      return new PointcutHandlingInfoImpl(var0, var1, var2);
   }

   public static ValueHandlingInfo createValueHandlingInfo(String var0, String var1, boolean var2, boolean var3) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Executing SpringInstSupportImpl.createValueHandlingInfo");
      }

      return new ValueHandlingInfoImpl(var0, var1, var2, var3);
   }

   public static Map<String, PointcutHandlingInfo> makeMap(String[] var0, PointcutHandlingInfo[] var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Executing SpringInstSupportImpl.makeMap");
      }

      return PointcutHandlingInfoImpl.makeMap(var0, var1);
   }

   public static void preProcess(LocalHolder var0) {
      applyActionStates(var0);
      MonitorLocalHolder var1 = var0.monitorHolder[var0.monitorIndex];
      preProcess((JoinPoint)(var1.djp == null ? var0.jp : var1.djp), var1.monitor, var1.actions, var1.states);
   }

   public static void preProcess(JoinPoint var0, DiagnosticMonitor var1, DiagnosticAction[] var2, DiagnosticActionState[] var3) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Executing SpringInstSupportImpl.preProcess: mon=" + var1.getName());
      }

      DynamicJoinPointImpl var4 = null;
      if (var0 instanceof DynamicJoinPointImpl) {
         var4 = (DynamicJoinPointImpl)var0;
         var4.setMonitorType(var1.getType());
      }

      int var5 = var2 != null ? var2.length : 0;

      for(int var6 = 0; var6 < var5; ++var6) {
         try {
            AroundDiagnosticAction var7 = (AroundDiagnosticAction)var2[var6];
            var7.preProcess(var0, var3[var6]);
         } catch (Throwable var8) {
            UnexpectedExceptionHandler.handle("Unexpected exception in preProcess,  executing action " + var6, var8);
         }
      }

      if (var4 != null) {
         var4.setMonitorType((String)null);
      }

   }

   public static void postProcess(LocalHolder var0) {
      MonitorLocalHolder var1 = var0.monitorHolder[var0.monitorIndex];
      postProcess((JoinPoint)(var1.djp == null ? var0.jp : var1.djp), var1.monitor, var1.actions, var1.states);
   }

   public static void postProcess(JoinPoint var0, DiagnosticMonitor var1, DiagnosticAction[] var2, DiagnosticActionState[] var3, Throwable var4) {
      postProcess(var0, var1, var2, var3);
   }

   public static void postProcess(JoinPoint var0, DiagnosticMonitor var1, DiagnosticAction[] var2, DiagnosticActionState[] var3) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Executing SpringInstSupportImpl.postProcess: mon=" + var1.getName());
      }

      DynamicJoinPointImpl var4 = null;
      if (var0 instanceof DynamicJoinPointImpl) {
         var4 = (DynamicJoinPointImpl)var0;
         var4.setMonitorType(var1.getType());
      }

      int var5 = var2 != null ? var2.length : 0;

      for(int var6 = 0; var6 < var5; ++var6) {
         try {
            AroundDiagnosticAction var7 = (AroundDiagnosticAction)var2[var6];
            var7.postProcess(var0, var3[var6]);
         } catch (Throwable var8) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Unexpected exception in postProcess,  executing action " + var6, var8);
            }
         }
      }

      if (var4 != null) {
         var4.setMonitorType((String)null);
      }

   }

   public static void process(JoinPoint var0, DiagnosticMonitor var1, DiagnosticAction[] var2, Throwable var3) {
      process(var0, var1, var2);
   }

   public static void process(LocalHolder var0) {
      MonitorLocalHolder var1 = var0.monitorHolder[var0.monitorIndex];
      process((JoinPoint)(var1.djp == null ? var0.jp : var1.djp), var1.monitor, var1.actions);
   }

   public static void process(JoinPoint var0, DiagnosticMonitor var1, DiagnosticAction[] var2) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Executing SpringInstSupportImpl.process: mon=" + var1.getName());
      }

      DynamicJoinPointImpl var3 = null;
      if (var0 instanceof DynamicJoinPointImpl) {
         var3 = (DynamicJoinPointImpl)var0;
         var3.setMonitorType(var1.getType());
      }

      int var4 = var2 != null ? var2.length : 0;

      for(int var5 = 0; var5 < var4; ++var5) {
         try {
            StatelessDiagnosticAction var6 = (StatelessDiagnosticAction)var2[var5];
            var6.process(var0);
         } catch (Throwable var7) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Unexpected exception in process,  executing action " + var5, var7);
            }
         }
      }

      if (var3 != null) {
         var3.setMonitorType((String)null);
      }

   }

   private static class SpringDelegatingMonitor implements DelegatingMonitor {
      private String type;
      private DiagnosticAction[] actions;

      SpringDelegatingMonitor(String var1) {
         this.type = var1;
      }

      public String getAttribute(String var1) {
         return null;
      }

      public String[] getAttributeNames() {
         return null;
      }

      public String getDescription() {
         return null;
      }

      public String getName() {
         return this.type;
      }

      public String getType() {
         return this.type;
      }

      public boolean isArgumentsCaptureNeeded() {
         return true;
      }

      public boolean isComponentScopeAllowed() {
         return true;
      }

      public boolean isEnabled() {
         return true;
      }

      public boolean isEnabledAndNotDyeFiltered() {
         return true;
      }

      public boolean isServerScopeAllowed() {
         return true;
      }

      public void setAttribute(String var1, String var2) {
      }

      public void setDescription(String var1) {
      }

      public void setEnabled(boolean var1) {
      }

      public void setName(String var1) {
      }

      public long getDyeMask() {
         return 0L;
      }

      public boolean isDyeFilteringEnabled() {
         return false;
      }

      public void setDyeFilteringEnabled(boolean var1) {
      }

      public void setDyeMask(long var1) {
      }

      public void addAction(DiagnosticAction var1) {
      }

      public void setActions(DiagnosticAction[] var1) {
         this.actions = var1;
      }

      public DiagnosticAction[] getActions() {
         return this.actions;
      }

      public String[] getCompatibleActionTypes() {
         return null;
      }

      public void removeAction(DiagnosticAction var1) {
      }

      public String[] getIncludes() {
         return null;
      }

      public void setIncludes(String[] var1) {
      }

      public String[] getExcludes() {
         return null;
      }

      public void setExcludes(String[] var1) {
      }

      public String getDiagnosticVolume() {
         return "Off";
      }

      public boolean isServerManaged() {
         return false;
      }

      public void setDiagnosticVolume(String var1) {
      }

      public void setServerManaged(boolean var1) {
      }

      public String getEventClassName() {
         return null;
      }

      public Class getEventClass() {
         return null;
      }

      public void setEventClassName(String var1) {
      }
   }
}
