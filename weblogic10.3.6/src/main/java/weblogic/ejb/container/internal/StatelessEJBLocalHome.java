package weblogic.ejb.container.internal;

import javax.ejb.AccessLocalException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.BaseEJBHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBLocalObjectIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.logging.Loggable;
import weblogic.utils.AssertionError;

public abstract class StatelessEJBLocalHome extends BaseEJBLocalHome {
   private StatelessEJBLocalObject elo;
   private boolean usesBeanManagedTx;
   static final long serialVersionUID = 6841887735091877327L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.StatelessEJBLocalHome");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;

   public StatelessEJBLocalHome(Class var1) {
      super(var1);
   }

   public void setup(BeanInfo var1, BaseEJBHomeIntf var2, BeanManager var3) throws WLDeploymentException {
      super.setup(var1, var2, var3);
      SessionBeanInfo var4 = (SessionBeanInfo)var1;
      if (var4.hasDeclaredLocalHome()) {
         try {
            this.elo = (StatelessEJBLocalObject)this.eloClass.newInstance();
            this.elo.setBeanInfo(this.getBeanInfo());
            this.elo.setEJBLocalHome(this);
            this.elo.setBeanManager(this.getBeanManager());
            this.elo.setIsEJB30ClientView(false);
         } catch (Exception var6) {
            throw new AssertionError(var6);
         }
      }

      this.usesBeanManagedTx = var4.usesBeanManagedTx();
   }

   public boolean usesBeanManagedTx() {
      return this.usesBeanManagedTx;
   }

   public BaseEJBLocalObjectIntf create(MethodDescriptor var1) throws EJBException {
      boolean var10;
      boolean var10000 = var10 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var11 = null;
      DiagnosticActionState[] var12 = null;
      Object var9 = null;
      DynamicJoinPoint var16;
      if (var10000) {
         Object[] var5 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium.isArgumentsCaptureNeeded()) {
            var5 = new Object[]{this, var1};
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var5, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium;
         DiagnosticAction[] var10002 = var11 = var10001.getActions();
         InstrumentationSupport.preProcess(var16, var10001, var10002, var12 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var14 = false;

      BaseEJBLocalObjectIntf var17;
      try {
         var14 = true;
         if (!var1.checkMethodPermissionsLocal(EJBContextHandler.EMPTY)) {
            Loggable var2 = EJBLogger.loginsufficientPermissionToUserLoggable(SecurityHelper.getCurrentPrincipal().getName(), "create");
            SecurityException var3 = new SecurityException(var2.getMessage());
            throw new AccessLocalException(var3.getMessage(), var3);
         }

         var17 = this.allocateELO();
         var14 = false;
      } finally {
         if (var14) {
            var16 = null;
            if (var10) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium, var11, var12);
            }

         }
      }

      if (var10) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium, var11, var12);
      }

      return var17;
   }

   public void remove(MethodDescriptor var1, Object var2) throws RemoveException {
      boolean var10;
      boolean var10000 = var10 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var11 = null;
      DiagnosticActionState[] var12 = null;
      Object var9 = null;
      if (var10000) {
         Object[] var5 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium.isArgumentsCaptureNeeded()) {
            var5 = new Object[]{this, var1, var2};
         }

         DynamicJoinPoint var15 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var5, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium;
         DiagnosticAction[] var10002 = var11 = var10001.getActions();
         InstrumentationSupport.preProcess(var15, var10001, var10002, var12 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         Loggable var3 = EJBLogger.loginvalidRemoveCallLoggable();
         throw new RemoveException(var3.getMessage());
      } finally {
         if (var10) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium, var11, var12);
         }

      }
   }

   public BaseEJBLocalObjectIntf allocateELO(Object var1) {
      throw new AssertionError("No pk for stateless beans");
   }

   public BaseEJBLocalObjectIntf allocateELO() {
      return this.elo;
   }

   public void cleanup() {
   }

   public void undeploy() {
      super.undeploy();
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Home_Remove_Around_Medium");
      _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Home_Create_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatelessEJBLocalHome.java", "weblogic.ejb.container.internal.StatelessEJBLocalHome", "create", "(Lweblogic/ejb/container/internal/MethodDescriptor;)Lweblogic/ejb/container/interfaces/BaseEJBLocalObjectIntf;", 60, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Home_Create_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true)})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatelessEJBLocalHome.java", "weblogic.ejb.container.internal.StatelessEJBLocalHome", "remove", "(Lweblogic/ejb/container/internal/MethodDescriptor;Ljava/lang/Object;)V", 72, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Home_Remove_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
   }
}
