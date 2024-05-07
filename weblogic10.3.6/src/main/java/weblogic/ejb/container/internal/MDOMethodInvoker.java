package weblogic.ejb.container.internal;

import javax.ejb.EJBException;
import javax.ejb.EnterpriseBean;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.ejb.container.interfaces.Invokable;
import weblogic.utils.PlatformConstants;
import weblogic.utils.StackTraceUtils;

public final class MDOMethodInvoker {
   static final long serialVersionUID = 691262971439823636L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.MDOMethodInvoker");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Invoke_Wrapper_Around_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   private MDOMethodInvoker() {
   }

   public static Object invoke(MessageDrivenLocalObject var0, MethodDescriptor var1, Object[] var2, int var3, String var4) throws Throwable {
      boolean var15;
      boolean var10000 = var15 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Invoke_Wrapper_Around_Low.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var16 = null;
      DiagnosticActionState[] var17 = null;
      Object var14 = null;
      DynamicJoinPoint var25;
      if (var10000) {
         Object[] var10 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Invoke_Wrapper_Around_Low.isArgumentsCaptureNeeded()) {
            var10 = new Object[]{var0, var1, var2, InstrumentationSupport.convertToObject(var3), var4};
         }

         var25 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var10, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Invoke_Wrapper_Around_Low;
         DiagnosticAction[] var10002 = var16 = var10001.getActions();
         InstrumentationSupport.preProcess(var25, var10001, var10002, var17 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var21 = false;

      Object var26;
      try {
         var21 = true;
         Object var5 = null;
         boolean var6 = var0.__WL_getState() == 128;
         if (!var6) {
            var0.__WL_preInvoke(var1, new EJBContextHandler(var1, var2));
         } else {
            var0.__WL_superPreInvoke(var1, new EJBContextHandler(var1, var2));
         }

         EnterpriseBean var7 = var0.__WL_getWrap().getBean();
         var0.__WL_business();

         try {
            var5 = ((Invokable)var0).__WL_invoke(var7, var2, var3);
            var0.__WL_business_success();
         } catch (Throwable var22) {
            var0.__WL_business_fail(var22);
         }

         if (!var6) {
            try {
               var0.__WL_mdPostInvoke();
            } catch (Exception var23) {
               if (var23 instanceof EJBException) {
                  throw var23;
               }

               ((Invokable)var0).__WL_handleException(var3, var23);
               throw new EJBException("Unexpected exception in " + var4 + ":" + PlatformConstants.EOL + StackTraceUtils.throwable2StackTrace(var23), var23);
            }
         } else if (var0.__WL_getException() != null) {
            Exception var8 = null;
            if (var0.__WL_getException() instanceof Exception) {
               var8 = (Exception)var0.__WL_getException();
            } else {
               EJBRuntimeUtils.throwEJBException("EJB Application Exception:", var0.__WL_getException());
            }

            if (var8 instanceof EJBException) {
               throw var8;
            }

            ((Invokable)var0).__WL_handleException(var3, var8);
            throw new EJBException("Unexpected exception in " + var4 + ":" + PlatformConstants.EOL + StackTraceUtils.throwable2StackTrace(var8), var8);
         }

         var26 = var5;
         var21 = false;
      } finally {
         if (var21) {
            var25 = null;
            if (var15) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Invoke_Wrapper_Around_Low, var16, var17);
            }

         }
      }

      if (var15) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Invoke_Wrapper_Around_Low, var16, var17);
      }

      return var26;
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Invoke_Wrapper_Around_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Invoke_Wrapper_Around_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "MDOMethodInvoker.java", "weblogic.ejb.container.internal.MDOMethodInvoker", "invoke", "(Lweblogic/ejb/container/internal/MessageDrivenLocalObject;Lweblogic/ejb/container/internal/MethodDescriptor;[Ljava/lang/Object;ILjava/lang/String;)Ljava/lang/Object;", 18, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Invoke_Wrapper_Around_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{null, InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null, null, null})}), (boolean)1);
   }
}
