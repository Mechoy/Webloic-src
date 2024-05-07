package weblogic.ejb.container.internal;

import javax.ejb.EJBException;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.logging.Loggable;
import weblogic.security.service.ContextHandler;
import weblogic.utils.AssertionError;

public abstract class StatelessLocalObject extends BaseLocalObject {
   static final long serialVersionUID = -5011608039847284801L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.StatelessLocalObject");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   protected InvocationWrapper __WL_preInvoke(MethodDescriptor var1, ContextHandler var2) throws EJBException {
      boolean var13 = false;

      InvocationWrapper var10000;
      try {
         var13 = true;
         if (debugLogger.isDebugEnabled()) {
            debug("preInvoke with md: " + var1 + " on: " + this);
         }

         InvocationWrapper var3 = null;

         try {
            if (this.isEJB30ClientView()) {
               var3 = EJBRuntimeUtils.createWrapWithTxsForBus(var1);
            } else {
               var3 = EJBRuntimeUtils.createWrapWithTxs(var1);
            }
         } catch (InternalException var14) {
            EJBRuntimeUtils.throwEJBException(var14);
            throw new AssertionError("Should not reach.");
         }

         var10000 = super.preInvoke(var3, var2);
         var13 = false;
      } finally {
         if (var13) {
            Object var10001 = null;
            if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low.isEnabledAndNotDyeFiltered()) {
               DelegatingMonitor var10003 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low;
               InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10003, var10003.getActions());
            }

         }
      }

      if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low.isEnabledAndNotDyeFiltered()) {
         DelegatingMonitor var10002 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low;
         InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10002, var10002.getActions());
      }

      return var10000;
   }

   protected final Object getPrimaryKeyObject() throws EJBException {
      Loggable var1 = EJBLogger.loglocalSessionBeanCannotCallGetPrimaryKeyLoggable();
      throw new EJBException(var1.getMessage());
   }

   private static void debug(String var0) {
      debugLogger.debug("[StatelessLocalObject] " + var0);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Preinvoke_After_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatelessLocalObject.java", "weblogic.ejb.container.internal.StatelessLocalObject", "__WL_preInvoke", "(Lweblogic/ejb/container/internal/MethodDescriptor;Lweblogic/security/service/ContextHandler;)Lweblogic/ejb/container/internal/InvocationWrapper;", 30, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Preinvoke_After_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
   }
}
