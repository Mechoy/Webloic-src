package weblogic.ejb.container.internal;

import javax.ejb.EJBException;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.ejb.container.InternalException;
import weblogic.security.service.ContextHandler;

public class TimerDrivenLocalObject extends BaseLocalObject {
   private ClassLoader clSave = null;
   static final long serialVersionUID = 7587830459442812972L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.TimerDrivenLocalObject");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   protected InvocationWrapper __WL_preInvoke(MethodDescriptor var1, ContextHandler var2) throws EJBException {
      try {
         throw new AssertionError("This method should NOT be called");
      } finally {
         Object var10000 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low.isEnabledAndNotDyeFiltered()) {
            DelegatingMonitor var10002 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10002, var10002.getActions());
         }

      }
   }

   protected InvocationWrapper preInvoke(Object var1, MethodDescriptor var2, ContextHandler var3) throws Throwable {
      if (debugLogger.isDebugEnabled()) {
         debug("[TimerDrivenLocalObject] preInvoke with md: " + var2 + " on: " + this);
      }

      SecurityHelper.pushRunAsSubject(SecurityHelper.getAnonymousUser());

      try {
         InvocationWrapper var4 = null;

         try {
            var4 = EJBRuntimeUtils.createWrapWithTxs(var2, var1);
         } catch (InternalException var7) {
            EJBRuntimeUtils.throwEJBException(var7);
            throw new AssertionError("Should not reach.");
         }

         super.preInvoke(var4, var3);
         ClassLoader var5 = this.beanInfo.getModuleClassLoader();
         Thread var6 = Thread.currentThread();
         this.clSave = var6.getContextClassLoader();
         var6.setContextClassLoader(var5);
         return var4;
      } catch (Throwable var8) {
         SecurityHelper.popRunAsSubject();
         throw var8;
      }
   }

   public void postInvoke(InvocationWrapper var1, Throwable var2) throws Exception {
      try {
         super.postInvoke(var1, var2);
      } finally {
         Thread var5 = Thread.currentThread();
         if (this.clSave != null) {
            var5.setContextClassLoader(this.clSave);
         }

         SecurityHelper.popRunAsSubject();
      }

   }

   private static void debug(String var0) {
      debugLogger.debug("[TimerDrivenLocalObject] " + var0);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Preinvoke_After_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "TimerDrivenLocalObject.java", "weblogic.ejb.container.internal.TimerDrivenLocalObject", "__WL_preInvoke", "(Lweblogic/ejb/container/internal/MethodDescriptor;Lweblogic/security/service/ContextHandler;)Lweblogic/ejb/container/internal/InvocationWrapper;", 26, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Preinvoke_After_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
   }
}
