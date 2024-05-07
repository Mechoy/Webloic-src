package weblogic.ejb.container.internal;

import java.lang.reflect.Method;
import javax.ejb.EJBException;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.manager.StatefulSessionManager;
import weblogic.logging.Loggable;
import weblogic.security.service.ContextHandler;

public abstract class StatefulLocalObject extends BaseLocalObject {
   protected Object primaryKey;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = 8282085137481415274L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.StatefulLocalObject");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Before_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;

   public void setPrimaryKey(Object var1) {
      if (!$assertionsDisabled && var1 == null) {
         throw new AssertionError();
      } else {
         this.primaryKey = var1;
      }
   }

   public Object getPK() {
      return this.primaryKey;
   }

   protected Object getPrimaryKeyObject() throws EJBException {
      Loggable var1 = EJBLogger.loglocalSessionBeanCannotCallGetPrimaryKeyLoggable();
      throw new EJBException(var1.getMessage());
   }

   protected InvocationWrapper __WL_preInvoke(MethodDescriptor var1, ContextHandler var2) throws EJBException {
      boolean var13 = false;

      InvocationWrapper var10000;
      try {
         var13 = true;
         if (!$assertionsDisabled && this.primaryKey == null) {
            throw new AssertionError();
         }

         InvocationWrapper var3 = null;

         try {
            if (this.isEJB30ClientView()) {
               var3 = EJBRuntimeUtils.createWrapWithTxsForBus(var1, this.primaryKey);
            } else {
               var3 = EJBRuntimeUtils.createWrapWithTxs(var1, this.primaryKey);
            }
         } catch (InternalException var14) {
            EJBRuntimeUtils.throwEJBException(var14);
            throw new weblogic.utils.AssertionError("Should not reach.");
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

   private static void debug(String var0) {
      debugLogger.debug("[StatefulLocalObject] " + var0);
   }

   protected boolean __WL_postInvokeTxRetry(InvocationWrapper var1, Throwable var2) throws Exception {
      if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Before_Low.isEnabledAndNotDyeFiltered()) {
         Object[] var8 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Before_Low.isArgumentsCaptureNeeded()) {
            var8 = new Object[]{this, var1, var2};
         }

         DynamicJoinPoint var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var8, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Before_Low;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      boolean var3 = super.__WL_postInvokeTxRetry(var1, var2);
      if (!var3 && this.beanInfo instanceof Ejb3SessionBeanInfo) {
         Method var4 = var1.getMethodDescriptor().getMethod();
         boolean var5 = ((Ejb3SessionBeanInfo)this.beanInfo).isRemoveMethod(var4);
         boolean var6 = ((Ejb3SessionBeanInfo)this.beanInfo).isRetainifException(var4);
         if (var5 && (var2 == null || EJBRuntimeUtils.isAppException(this.beanInfo, var4, var2) && !var6)) {
            this.removeForRemoveAnnotation(var1);
         }
      }

      return var3;
   }

   private void removeForRemoveAnnotation(InvocationWrapper var1) throws Exception {
      ((StatefulSessionManager)this.beanManager).removeForRemoveAnnotation(var1);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Before_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Postinvoke_Before_Low");
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Preinvoke_After_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatefulLocalObject.java", "weblogic.ejb.container.internal.StatefulLocalObject", "__WL_preInvoke", "(Lweblogic/ejb/container/internal/MethodDescriptor;Lweblogic/security/service/ContextHandler;)Lweblogic/ejb/container/internal/InvocationWrapper;", 59, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Preinvoke_After_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatefulLocalObject.java", "weblogic.ejb.container.internal.StatefulLocalObject", "__WL_postInvokeTxRetry", "(Lweblogic/ejb/container/internal/InvocationWrapper;Ljava/lang/Throwable;)Z", 86, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Postinvoke_Before_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true), null})}), (boolean)0);
      $assertionsDisabled = !StatefulLocalObject.class.desiredAssertionStatus();
   }
}
