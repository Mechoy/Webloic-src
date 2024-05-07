package weblogic.ejb.container.internal;

import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import javax.ejb.EJBMetaData;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
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
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.internal.EJBMetaDataImpl;
import weblogic.logging.Loggable;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.utils.AssertionError;

public abstract class StatelessEJBHome extends BaseEJBHome {
   private EJBObject eo;
   private boolean usesBeanManagedTx;
   static final long serialVersionUID = 4093212281770550184L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.StatelessEJBHome");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;

   public StatelessEJBHome(Class var1) {
      super(var1);
   }

   public void setup(BeanInfo var1, BaseEJBHomeIntf var2, BeanManager var3) throws WLDeploymentException {
      super.setup(var1, var2, var3);
      SessionBeanInfo var4 = (SessionBeanInfo)var1;
      this.usesBeanManagedTx = var4.usesBeanManagedTx();
   }

   public void activate() throws WLDeploymentException {
      super.activate();
      if (this.beanInfo.hasDeclaredRemoteHome()) {
         String var1 = this.getIsIdenticalKey().replace('.', '_') + "_EO";
         StatelessEJBObject var2 = null;

         try {
            var2 = (StatelessEJBObject)this.eoClass.newInstance();
            var2.setEJBHome(this);
            var2.setBeanManager(this.getBeanManager());
            var2.setBeanInfo(this.getBeanInfo());
         } catch (Exception var4) {
            throw new AssertionError(var4);
         }

         this.eo = var2;
      }
   }

   public boolean usesBeanManagedTx() {
      return this.usesBeanManagedTx;
   }

   protected EJBMetaData getEJBMetaDataInstance() {
      return new EJBMetaDataImpl(this, this.beanInfo.getHomeInterfaceClass(), this.beanInfo.getRemoteInterfaceClass(), (Class)null, true, true);
   }

   public EJBObject create(MethodDescriptor var1) throws Exception {
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

      EJBObject var17;
      try {
         var14 = true;
         if (!var1.checkMethodPermissionsRemote(EJBContextHandler.EMPTY)) {
            Loggable var2 = EJBLogger.loginsufficientPermissionToUserLoggable(SecurityHelper.getCurrentPrincipal().getName(), "create");
            SecurityException var3 = new SecurityException(var2.getMessage());
            throw new AccessException(var3.getMessage(), var3);
         }

         var17 = this.allocateEO();
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

   public void remove(MethodDescriptor var1, Handle var2) throws RemoteException, RemoveException {
      boolean var12;
      boolean var10000 = var12 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var13 = null;
      DiagnosticActionState[] var14 = null;
      Object var11 = null;
      if (var10000) {
         Object[] var7 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium.isArgumentsCaptureNeeded()) {
            var7 = new Object[]{this, var1, var2};
         }

         DynamicJoinPoint var21 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var7, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium;
         DiagnosticAction[] var10002 = var13 = var10001.getActions();
         InstrumentationSupport.preProcess(var21, var10001, var10002, var14 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         InvocationWrapper var3 = this.preHomeInvoke(var1, new EJBContextHandler(var1, new Object[]{var2}));

         try {
            this.validateHandleFromHome(var2);
         } finally {
            this.postHomeInvoke(var3, (Throwable)null);
         }
      } finally {
         if (var12) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium, var13, var14);
         }

      }

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

         DynamicJoinPoint var15 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var5, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium;
         DiagnosticAction[] var10002 = var11 = var10001.getActions();
         InstrumentationSupport.preProcess(var15, var10001, var10002, var12 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         Loggable var3 = EJBLogger.loginvalidRemoveCallLoggable();
         throw new RemoveException(var3.getMessage());
      } finally {
         if (var10) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium, var11, var12);
         }

      }
   }

   public EJBObject allocateEO(Object var1) {
      throw new AssertionError("No pk for stateless beans");
   }

   public EJBObject allocateEO() {
      return this.eo;
   }

   public void cleanup() {
      if (this.beanInfo.hasDeclaredRemoteHome()) {
         try {
            ServerHelper.unexportObject(this.eo, true, true);
         } catch (NoSuchObjectException var2) {
         }
      }

   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Home_Create_Around_Medium");
      _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Home_Remove_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatelessEJBHome.java", "weblogic.ejb.container.internal.StatelessEJBHome", "create", "(Lweblogic/ejb/container/internal/MethodDescriptor;)Ljavax/ejb/EJBObject;", 124, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Home_Create_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true)})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatelessEJBHome.java", "weblogic.ejb.container.internal.StatelessEJBHome", "remove", "(Lweblogic/ejb/container/internal/MethodDescriptor;Ljavax/ejb/Handle;)V", 138, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Home_Remove_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatelessEJBHome.java", "weblogic.ejb.container.internal.StatelessEJBHome", "remove", "(Lweblogic/ejb/container/internal/MethodDescriptor;Ljava/lang/Object;)V", 153, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Home_Remove_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
   }
}
