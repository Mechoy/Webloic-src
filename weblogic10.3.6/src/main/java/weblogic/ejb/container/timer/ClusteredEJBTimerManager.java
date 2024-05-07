package weblogic.ejb.container.timer;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.Timer;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.ejb.WLTimerInfo;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.TimerManager;
import weblogic.ejb.container.internal.MethodDescriptor;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.runtime.EJBTimerRuntimeMBean;
import weblogic.utils.classloaders.GenericClassLoader;

public final class ClusteredEJBTimerManager implements TimerManager {
   private static final DebugLogger debugLogger;
   private weblogic.scheduler.ejb.EJBTimerManager jscheduler;
   private BeanInfo bi;
   private BeanManager beanManager;
   private MethodDescriptor md;
   private boolean isTransactional;
   static final long serialVersionUID = 5129692831177551442L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.timer.ClusteredEJBTimerManager");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;
   public static final JoinPoint _WLDF$INST_JPFLD_3;

   public ClusteredEJBTimerManager(BeanManager var1) {
      this.beanManager = var1;
      this.bi = var1.getBeanInfo();
   }

   public void setup(EJBTimerRuntimeMBean var1) throws WLDeploymentException {
      this.initializeMethodDescriptor();
      this.initializeTimerHandler();
   }

   public Timer createTimer(Object var1, Date var2, long var3, Serializable var5, WLTimerInfo var6) {
      boolean var14;
      boolean var10000 = var14 = _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var15 = null;
      DiagnosticActionState[] var16 = null;
      Object var13 = null;
      DelegatingMonitor var10001;
      if (var10000) {
         JoinPoint var20 = _WLDF$INST_JPFLD_0;
         var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High;
         DiagnosticAction[] var10002 = var15 = var10001.getActions();
         InstrumentationSupport.preProcess(var20, var10001, var10002, var16 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var18 = false;

      TimerWrapper var21;
      try {
         var18 = true;
         ClusteredTimerImpl var7 = new ClusteredTimerImpl(var1, var5, this.isTransactional, this.bi);
         var7.initialize(this.jscheduler.schedule(var7, var2, var3));
         var21 = new TimerWrapper(var7);
         var18 = false;
      } finally {
         if (var18) {
            var10001 = null;
            if (var14) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High, var15, var16);
            }

         }
      }

      if (var14) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High, var15, var16);
      }

      return var21;
   }

   public Timer createTimer(Object var1, Date var2, Serializable var3, WLTimerInfo var4) {
      boolean var12;
      boolean var10000 = var12 = _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var13 = null;
      DiagnosticActionState[] var14 = null;
      Object var11 = null;
      DelegatingMonitor var10001;
      if (var10000) {
         JoinPoint var18 = _WLDF$INST_JPFLD_1;
         var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High;
         DiagnosticAction[] var10002 = var13 = var10001.getActions();
         InstrumentationSupport.preProcess(var18, var10001, var10002, var14 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var16 = false;

      TimerWrapper var19;
      try {
         var16 = true;
         ClusteredTimerImpl var5 = new ClusteredTimerImpl(var1, var3, this.isTransactional, this.bi);
         var5.initialize(this.jscheduler.schedule(var5, var2));
         var19 = new TimerWrapper(var5);
         var16 = false;
      } finally {
         if (var16) {
            var10001 = null;
            if (var12) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High, var13, var14);
            }

         }
      }

      if (var12) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High, var13, var14);
      }

      return var19;
   }

   public Timer createTimer(Object var1, long var2, long var4, Serializable var6, WLTimerInfo var7) {
      boolean var15;
      boolean var10000 = var15 = _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var16 = null;
      DiagnosticActionState[] var17 = null;
      Object var14 = null;
      DelegatingMonitor var10001;
      if (var10000) {
         JoinPoint var21 = _WLDF$INST_JPFLD_2;
         var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High;
         DiagnosticAction[] var10002 = var16 = var10001.getActions();
         InstrumentationSupport.preProcess(var21, var10001, var10002, var17 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var19 = false;

      Timer var22;
      try {
         var19 = true;
         Date var8 = new Date(System.currentTimeMillis() + var2);
         var22 = this.createTimer(var1, var8, var4, var6, (WLTimerInfo)null);
         var19 = false;
      } finally {
         if (var19) {
            var10001 = null;
            if (var15) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High, var16, var17);
            }

         }
      }

      if (var15) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High, var16, var17);
      }

      return var22;
   }

   public Timer createTimer(Object var1, long var2, Serializable var4, WLTimerInfo var5) {
      boolean var13;
      boolean var10000 = var13 = _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var14 = null;
      DiagnosticActionState[] var15 = null;
      Object var12 = null;
      DelegatingMonitor var10001;
      if (var10000) {
         JoinPoint var19 = _WLDF$INST_JPFLD_3;
         var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High;
         DiagnosticAction[] var10002 = var14 = var10001.getActions();
         InstrumentationSupport.preProcess(var19, var10001, var10002, var15 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var17 = false;

      Timer var20;
      try {
         var17 = true;
         Date var6 = new Date(System.currentTimeMillis() + var2);
         var20 = this.createTimer(var1, var6, var4, (WLTimerInfo)null);
         var17 = false;
      } finally {
         if (var17) {
            var10001 = null;
            if (var13) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_3, _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High, var14, var15);
            }

         }
      }

      if (var13) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_3, _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High, var14, var15);
      }

      return var20;
   }

   public Collection getTimers(Object var1) {
      return this.getTimers(var1, true);
   }

   public Collection getTimers(Object var1, boolean var2) {
      boolean var3 = this.bi instanceof EntityBeanInfo;
      HashSet var4 = new HashSet();
      weblogic.timers.Timer[] var5 = null;
      if (var3) {
         String var6 = Integer.toString(var1.hashCode());
         var5 = this.jscheduler.getTimers(var6);
      } else {
         var5 = this.jscheduler.getTimers();
      }

      for(int var8 = 0; var8 < var5.length; ++var8) {
         ClusteredTimerImpl var7 = (ClusteredTimerImpl)var5[var8].getListener();
         if (!var3 || var1.equals(var7.getPrimaryKey())) {
            var7.initialize(var5[var8]);
            if (var2) {
               var4.add(new TimerWrapper(var7));
            } else {
               var4.add(var7);
            }
         }
      }

      return var4;
   }

   public void removeTimersForPK(Object var1) {
      Collection var2 = this.getTimers(var1, false);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         ((Timer)var3.next()).cancel();
      }

   }

   public static void removeAllTimers(BeanInfo var0) {
      String var1 = var0.getDeploymentInfo().getApplicationName();
      ApplicationContextInternal var2 = ApplicationAccess.getApplicationAccess().getApplicationContext(var1);
      DomainMBean var3 = var2.getProposedDomain();
      if (var3 != null && var3.lookupApplication(var1) == null) {
         getEJBTimerManager(var0).stop();
      }

   }

   public void perhapsStart() {
      EJBTimerStarter.addTimerManagerStarter(this);
   }

   public void start() {
      this.jscheduler = getEJBTimerManager(this.bi);
   }

   private static weblogic.scheduler.ejb.EJBTimerManager getEJBTimerManager(BeanInfo var0) {
      weblogic.scheduler.ejb.EJBTimerManagerFactory var1 = weblogic.scheduler.ejb.EJBTimerManagerFactory.getInstance();
      GenericClassLoader var2 = (GenericClassLoader)var0.getModuleClassLoader();
      String var3 = var2.getAnnotation().getAnnotationString();
      return var1.create(var0.getIsIdenticalKey(), var3, var0.getDispatchPolicy());
   }

   public void undeploy() {
      this.uninitializeTimerHandler();
   }

   private void initializeMethodDescriptor() {
      this.md = this.bi.getEjbTimeoutMethodDescriptor();
      int var1 = this.md.getTXAttribute();
      this.isTransactional = var1 == 3 || var1 == 1;
      this.md.setTXAttribute(2);
   }

   private void initializeTimerHandler() throws WLDeploymentException {
      DeploymentInfo var1 = this.bi.getDeploymentInfo();
      String var2 = var1.getModuleURI();
      String var3 = this.bi.getEJBName();
      Context var4 = this.beanManager.getEnvironmentContext();
      TimerHandlerImpl var5 = new TimerHandlerImpl(this.beanManager, this.bi, this.md);

      try {
         var4.bind("app/ejb/" + var2 + "#" + var3 + "/timerHandler", var5);
      } catch (NamingException var7) {
         throw new WLDeploymentException("Error initializing TimerHelper:", var7);
      }
   }

   private void uninitializeTimerHandler() {
      DeploymentInfo var1 = this.bi.getDeploymentInfo();
      String var2 = var1.getModuleURI();
      String var3 = this.bi.getEJBName();
      Context var4 = this.beanManager.getEnvironmentContext();

      try {
         var4.unbind("app/ejb/" + var2 + "#" + var3 + "/timerHandler");
      } catch (NamingException var6) {
         if (debugLogger.isDebugEnabled()) {
            debug("Error unbinding TimerHandler", var6);
         }
      }

   }

   public void enableDisabledTimers() {
      throw new AssertionError("The clustered EJB Timer implementation doesn't support the disabling of EJB Timers");
   }

   private static void debug(String var0) {
      debugLogger.debug("[ClusteredEJBTimerManager] " + var0);
   }

   private static void debug(String var0, Throwable var1) {
      debugLogger.debug("[ClusteredEJBTimerManager] " + var0, var1);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Timer_Manager_Around_High = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Timer_Manager_Around_High");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ClusteredEJBTimerManager.java", "weblogic.ejb.container.timer.ClusteredEJBTimerManager", "createTimer", "(Ljava/lang/Object;Ljava/util/Date;JLjava/io/Serializable;Lweblogic/ejb/WLTimerInfo;)Ljavax/ejb/Timer;", 74, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ClusteredEJBTimerManager.java", "weblogic.ejb.container.timer.ClusteredEJBTimerManager", "createTimer", "(Ljava/lang/Object;Ljava/util/Date;Ljava/io/Serializable;Lweblogic/ejb/WLTimerInfo;)Ljavax/ejb/Timer;", 84, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ClusteredEJBTimerManager.java", "weblogic.ejb.container.timer.ClusteredEJBTimerManager", "createTimer", "(Ljava/lang/Object;JJLjava/io/Serializable;Lweblogic/ejb/WLTimerInfo;)Ljavax/ejb/Timer;", 93, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_3 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ClusteredEJBTimerManager.java", "weblogic.ejb.container.timer.ClusteredEJBTimerManager", "createTimer", "(Ljava/lang/Object;JLjava/io/Serializable;Lweblogic/ejb/WLTimerInfo;)Ljavax/ejb/Timer;", 101, (Map)null, (boolean)0);
      debugLogger = EJBDebugService.timerLogger;
   }
}
