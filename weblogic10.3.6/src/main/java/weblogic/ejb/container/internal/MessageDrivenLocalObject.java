package weblogic.ejb.container.internal;

import java.lang.reflect.Method;
import javax.ejb.EJBException;
import javax.resource.ResourceException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.transaction.xa.XAResource;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.container.interfaces.MessageDrivenLocalObjectIntf;
import weblogic.logging.Loggable;
import weblogic.security.service.ContextHandler;
import weblogic.transaction.TxHelper;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;

public class MessageDrivenLocalObject extends BaseLocalObject implements MessageDrivenLocalObjectIntf, MessageEndpoint {
   private XAResource xaResource = null;
   private ClassLoader clSave = null;
   private boolean isDeliveryTransacted = false;
   private Throwable theException = null;
   private InvocationWrapper __wrap_;
   private int state = 1;
   static final long serialVersionUID = 1391242042575229928L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.MessageDrivenLocalObject");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Before_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;

   public MessageDrivenLocalObject() {
      this.initialize();
   }

   public void setXAResource(XAResource var1) {
      this.xaResource = var1;
   }

   protected final InvocationWrapper __WL_preInvoke(MethodDescriptor var1, ContextHandler var2) throws EJBException {
      boolean var10 = false;

      InvocationWrapper var10000;
      try {
         var10 = true;
         var10000 = this.preInvoke(var1, var2, false);
         var10 = false;
      } finally {
         if (var10) {
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

   protected InvocationWrapper preInvoke(MethodDescriptor var1, ContextHandler var2, boolean var3) throws EJBException {
      if (debugLogger.isDebugEnabled()) {
         debug("[MessageDrivenLocalObject] preInvoke with md: " + var1 + " on: " + this);
      }

      this.__WL_setNextState(2);
      boolean var4 = TxHelper.getTransaction() != null;

      try {
         this.__wrap_ = EJBRuntimeUtils.createWrapWithTxs(var1);
         if (!var3) {
            super.preInvoke(this.__wrap_, var2);
         }
      } catch (Throwable var8) {
         this.setNextStateError(8, var8);
         EJBRuntimeUtils.throwEJBException("exception on preInvoke", var8);
         throw new AssertionError("Should not reach.");
      }

      this.isDeliveryTransacted = this.__wrap_.getInvokeTx() != null;
      if (this.xaResource != null && !var4 && this.isDeliveryTransacted) {
         try {
            this.__wrap_.getInvokeTx().enlistResource(this.xaResource);
         } catch (Exception var7) {
            this.setNextStateError(8, var7);
            EJBException var6 = new EJBException(var7);
            var6.initCause(var7);
            throw var6;
         }
      }

      this.setClassloader();
      this.__WL_setNextState(4);
      return this.__wrap_;
   }

   private void setClassloader() {
      ClassLoader var1 = this.beanInfo.getModuleClassLoader();
      Thread var2 = Thread.currentThread();
      this.clSave = var2.getContextClassLoader();
      var2.setContextClassLoader(var1);
   }

   protected final void __WL_superPreInvoke(MethodDescriptor var1, ContextHandler var2) {
      try {
         super.preInvoke(this.__wrap_, var2);
      } catch (Throwable var4) {
         EJBRuntimeUtils.throwEJBException("exception on preInvoke", var4);
         throw new AssertionError("Should not reach.");
      }
   }

   public final void __WL_mdPostInvoke() throws Exception {
      this.__WL_setNextState(4096);
      Thread var1 = Thread.currentThread();
      if (this.clSave != null) {
         var1.setContextClassLoader(this.clSave);
      }

      try {
         super.postInvoke(this.__wrap_, this.theException);
      } finally {
         this.__WL_setNextState(8192);
      }

   }

   protected final void __WL_business() {
      if (this.__WL_getState() == 128) {
         this.__WL_setNextState(256);
      } else {
         this.__WL_setNextState(16);
      }

   }

   protected final void __WL_business_success() {
      if (this.__WL_getState() == 256) {
         this.__WL_setNextState(512);
      } else {
         this.__WL_setNextState(32);
      }

   }

   protected final void __WL_business_fail(Throwable var1) {
      if (this.__WL_getState() == 256) {
         this.setNextStateError(1024, var1);
      } else {
         this.setNextStateError(64, var1);
      }

   }

   private void setNextStateError(int var1, Throwable var2) {
      this.__WL_setNextState(var1);
      this.theException = var2;
   }

   public void beforeDelivery(Method var1) throws NoSuchMethodException, ResourceException {
      try {
         this.isDeliveryTransacted = ((MessageDrivenBeanInfo)this.beanInfo).isDeliveryTransacted(var1);
         if (this.xaResource != null && this.isDeliveryTransacted) {
            MethodDescriptor var2 = ((MessageDrivenBeanInfo)this.beanInfo).getMDBMethodDescriptor(var1);
            Object[] var3 = new Object[var2.getMethodInfo().getMethodParams().length];
            this.preInvoke(var2, new EJBContextHandler(var2, var3), true);
            this.__WL_setNextState(128);
            return;
         }

         this.setClassloader();
         if (debugLogger.isDebugEnabled()) {
            debug("JCA 12.5.6: The beforeDelivery and afterDelivery calls have no effect when the resource adapter does not provide an XAResource instance or when the delivery is not transacted.");
         }
      } finally {
         if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low.isEnabledAndNotDyeFiltered()) {
            DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_1, var10001, var10001.getActions());
         }

      }

   }

   public void afterDelivery() throws ResourceException {
      if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Before_Low.isEnabledAndNotDyeFiltered()) {
         Object[] var5 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Before_Low.isArgumentsCaptureNeeded()) {
            var5 = new Object[]{this};
         }

         DynamicJoinPoint var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var5, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Before_Low;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      if (this.xaResource != null && this.isDeliveryTransacted) {
         this.__WL_setNextState(2048);

         try {
            this.__WL_mdPostInvoke();
         } catch (Exception var6) {
            if (var6 instanceof EJBException) {
               throw (EJBException)var6;
            } else {
               Loggable var2 = EJBLogger.logExceptionAferDeliveryLoggable(StackTraceUtils.throwable2StackTrace(var6));
               EJBException var3 = new EJBException(var2.getMessage(), var6);
               var3.initCause(var6);
               throw var3;
            }
         }
      } else {
         Thread var1 = Thread.currentThread();
         if (this.clSave != null) {
            var1.setContextClassLoader(this.clSave);
         }

         if (debugLogger.isDebugEnabled()) {
            debug("JCA 12.5.6: The beforeDelivery and afterDelivery calls have no effect when the resource adapter does not provide an XAResource instance or when the delivery is not transacted.");
         }

      }
   }

   public void release() {
      if (this.__WL_getState() == 512 || this.__WL_getState() == 1024) {
         try {
            this.afterDelivery();
         } catch (ResourceException var2) {
         }
      }

      this.__WL_setNextState(16384);
   }

   protected final void __WL_setNextState(int var1) throws IllegalStateException {
      if (this.allowedState(var1)) {
         this.setState(var1);
      } else {
         Loggable var2 = EJBLogger.logIllegalStateTransactionLoggable(this.__WL_getState() + "", var1 + "");
         throw new IllegalStateException(var2.getMessage());
      }
   }

   private boolean allowedState(int var1) {
      if ((var1 & this.__WL_getState()) != 0) {
         return true;
      } else {
         boolean var2 = false;
         short var3;
         switch (var1) {
            case 1:
               var3 = 24585;
               break;
            case 2:
               var3 = 24585;
               break;
            case 4:
               var3 = 2;
               break;
            case 8:
               var3 = 2;
               break;
            case 16:
               var3 = 4;
               break;
            case 32:
               var3 = 16;
               break;
            case 64:
               var3 = 16;
               break;
            case 128:
               var3 = 4;
               break;
            case 256:
               var3 = 128;
               break;
            case 512:
               var3 = 256;
               break;
            case 1024:
               var3 = 256;
               break;
            case 2048:
               var3 = 1536;
               break;
            case 4096:
               var3 = 2144;
               break;
            case 8192:
               var3 = 4096;
               break;
            case 16384:
               var3 = 26241;
               break;
            default:
               Debug.assertion(false, "unknown state argument to allowedMethod '" + var1 + "'");
               return false;
         }

         return (var3 & this.__WL_getState()) != 0;
      }
   }

   private void initialize() {
      this.__WL_setNextState(1);
      this.theException = null;
      this.__wrap_ = null;
      this.xaResource = null;
   }

   private void setState(int var1) {
      this.state = var1;
   }

   protected final int __WL_getState() {
      return this.state;
   }

   protected final Throwable __WL_getException() {
      return this.theException;
   }

   protected final void __WL_setException(Throwable var1) {
      this.theException = var1;
   }

   protected final InvocationWrapper __WL_getWrap() {
      return this.__wrap_;
   }

   private static void debug(String var0) {
      debugLogger.debug("[MessageDrivenLocalObject] " + var0);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Preinvoke_After_Low");
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Before_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Postinvoke_Before_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "MessageDrivenLocalObject.java", "weblogic.ejb.container.internal.MessageDrivenLocalObject", "__WL_preInvoke", "(Lweblogic/ejb/container/internal/MethodDescriptor;Lweblogic/security/service/ContextHandler;)Lweblogic/ejb/container/internal/InvocationWrapper;", 47, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Preinvoke_After_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "MessageDrivenLocalObject.java", "weblogic.ejb.container.internal.MessageDrivenLocalObject", "beforeDelivery", "(Ljava/lang/reflect/Method;)V", 158, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Preinvoke_After_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo(InstrumentationSupport.createValueHandlingInfo("this", "weblogic.diagnostics.instrumentation.gathering.ToStringRenderer", false, true), (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("method", (String)null, false, true)})}), (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "MessageDrivenLocalObject.java", "weblogic.ejb.container.internal.MessageDrivenLocalObject", "afterDelivery", "()V", 183, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Postinvoke_Before_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo(InstrumentationSupport.createValueHandlingInfo("this", "weblogic.diagnostics.instrumentation.gathering.ToStringRenderer", false, true), (ValueHandlingInfo)null, (ValueHandlingInfo[])null)}), (boolean)0);
   }
}
