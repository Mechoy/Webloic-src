package weblogic.ejb.container.internal;

import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.ejb.container.interfaces.Invokable;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.interfaces.WLSessionEJBContext;
import weblogic.utils.PlatformConstants;
import weblogic.utils.StackTraceUtils;

public final class SessionRemoteMethodInvoker {
   static final long serialVersionUID = 2667188584244800473L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.SessionRemoteMethodInvoker");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Invoke_Wrapper_Around_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   private SessionRemoteMethodInvoker() {
   }

   public static Object invoke(BaseRemoteObject var0, MethodDescriptor var1, Object[] var2, int var3, String var4, Class<?> var5) throws Throwable {
      boolean var22;
      boolean var10000 = var22 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Invoke_Wrapper_Around_Low.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var23 = null;
      DiagnosticActionState[] var24 = null;
      Object var21 = null;
      DynamicJoinPoint var41;
      if (var10000) {
         Object[] var17 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Invoke_Wrapper_Around_Low.isArgumentsCaptureNeeded()) {
            var17 = new Object[]{var0, var1, var2, InstrumentationSupport.convertToObject(var3), var4, var5};
         }

         var41 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var17, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Invoke_Wrapper_Around_Low;
         DiagnosticAction[] var10002 = var23 = var10001.getActions();
         InstrumentationSupport.preProcess(var41, var10001, var10002, var24 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var30 = false;

      Object var42;
      try {
         var30 = true;
         Object var6 = null;
         Throwable var7 = null;
         InvocationWrapper var8 = var0.__WL_preInvoke(var1, new EJBContextHandler(var1, var2));
         boolean var9 = false;

         while(true) {
            WLEnterpriseBean var10 = (WLEnterpriseBean)var8.getBean();
            int var11 = var10.__WL_getMethodState();
            WLSessionEJBContext var12 = null;

            try {
               AllowedMethodsHelper.pushBean(var10);
               var10.__WL_setMethodState(128);
               if (var5 != null) {
                  var12 = (WLSessionEJBContext)var10.__WL_getEJBContext();
                  var12.setBusinessInterfaceClass(var5);
               }

               var6 = ((Invokable)var0).__WL_invoke(var10, var2, var3);
            } catch (Throwable var37) {
               var7 = var37;
            } finally {
               if (var12 != null) {
                  var12.setBusinessInterfaceClass((Class)null);
               }

               var10.__WL_setMethodState(var11);
               AllowedMethodsHelper.popBean();
            }

            try {
               var9 = var0.__WL_postInvokeTxRetry(var8, var7);
            } catch (Throwable var36) {
               var7 = var36;
               var9 = false;
            }

            if (!var9) {
               try {
                  var0.__WL_postInvokeCleanup(var8, var7);
               } catch (Exception var39) {
                  if (var39 instanceof RemoteException) {
                     throw var39;
                  }

                  ((Invokable)var0).__WL_handleException(var3, var39);
                  throw new UnexpectedException("Unexpected exception in " + var4 + ":" + PlatformConstants.EOL + StackTraceUtils.throwable2StackTrace(var39), var39);
               }

               var42 = var6;
               var30 = false;
               break;
            }
         }
      } finally {
         if (var30) {
            var41 = null;
            if (var22) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Invoke_Wrapper_Around_Low, var23, var24);
            }

         }
      }

      if (var22) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Invoke_Wrapper_Around_Low, var23, var24);
      }

      return var42;
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Invoke_Wrapper_Around_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Invoke_Wrapper_Around_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "SessionRemoteMethodInvoker.java", "weblogic.ejb.container.internal.SessionRemoteMethodInvoker", "invoke", "(Lweblogic/ejb/container/internal/BaseRemoteObject;Lweblogic/ejb/container/internal/MethodDescriptor;[Ljava/lang/Object;ILjava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;", 21, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Invoke_Wrapper_Around_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{null, InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null, null, null, null})}), (boolean)1);
   }
}
