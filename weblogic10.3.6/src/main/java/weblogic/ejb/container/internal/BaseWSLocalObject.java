package weblogic.ejb.container.internal;

import javax.ejb.EJBException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPMessage;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.spi.BaseWSObjectIntf;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.utils.Debug;

public abstract class BaseWSLocalObject extends BaseLocalObject implements BaseWSObjectIntf {
   private int state = 1;
   private boolean sessionCtxHasMessageCtx = false;
   private ContextHandler __wsContext = null;
   private Throwable theException = null;
   private InvocationWrapper __wrap_;
   private MethodDescriptor __md_;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = 8535090582963499445L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.BaseWSLocalObject");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   public String toString() {
      return "[BaseWSLocalObject] ";
   }

   protected final InvocationWrapper __WL_preInvoke(MethodDescriptor var1, ContextHandler var2) throws EJBException {
      boolean var10 = false;

      InvocationWrapper var10000;
      try {
         var10 = true;
         var10000 = this.__WL_preInvoke(var1, var2, (ContextHandler)null, (AuthenticatedSubject)null);
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

   protected final InvocationWrapper __WL_preInvoke(MethodDescriptor var1, ContextHandler var2, ContextHandler var3) throws EJBException {
      return this.__WL_preInvoke(var1, var2, var3, (AuthenticatedSubject)null);
   }

   protected final InvocationWrapper __WL_preInvoke(MethodDescriptor var1, ContextHandler var2, ContextHandler var3, AuthenticatedSubject var4) throws EJBException {
      this.__md_ = var1;
      this.__wsContext = var3;
      if (debugLogger.isDebugEnabled()) {
         debug("[BaseWSLocalObject] preInvoke with md: " + this.__md_ + " on: " + this);
      }

      this.setNextState(2);
      EJBContextHandler var5 = (EJBContextHandler)var2;
      if (this.__wsContext != null) {
         MessageContext var6 = (MessageContext)this.__wsContext.getValue("com.bea.contextelement.wsee.SOAPMessage");
         if (var6 instanceof SOAPMessageContext) {
            SOAPMessage var7 = ((SOAPMessageContext)var6).getMessage();
            var5.setSOAPMessage(var7);
         } else if (debugLogger.isDebugEnabled()) {
            debug(" we expected MessageContext.getValue(ContextElementDictionary.WSEE_SOAPMESSAGE) to return a javax.xml.rpc.handler.soap.SOAPMessageContext, but instead we got: '" + var6.getClass().getName() + "'");
         }
      }

      try {
         this.__wrap_ = EJBRuntimeUtils.createWrapWithTxs(this.__md_);
         super.preInvoke(this.__wrap_, var2, false, var4);
      } catch (Throwable var8) {
         this.setNextStateError(8, var8);
         EJBRuntimeUtils.throwEJBException("service object exception on preInvoke", var8);
      }

      this.setNextState(4);
      return this.__wrap_;
   }

   protected final void __WL_business(MethodDescriptor var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("[BaseWSLocalObject] business method  on: " + this);
      }

      if (!$assertionsDisabled && !var1.getMethod().equals(this.__md_.getMethod())) {
         throw new AssertionError(" business method called for different method than was called for preInvoke ! preInvoke method: '" + this.__md_.toString() + "', business method: '" + var1.toString() + "'");
      } else {
         this.setNextState(16);
         if (this.__wsContext != null) {
            this.setMessageContext();
         }

      }
   }

   private void setMessageContext() {
      WLEnterpriseBean var1 = (WLEnterpriseBean)this.__wrap_.getBean();
      SessionEJBContextImpl var2 = (SessionEJBContextImpl)var1.__WL_getEJBContext();
      var2.setMessageContext((MessageContext)this.__wsContext.getValue("com.bea.contextelement.wsee.SOAPMessage"));
      this.sessionCtxHasMessageCtx = true;
   }

   protected final void __WL_business_success() {
      this.setNextState(32);
   }

   protected final void __WL_business_fail(Throwable var1) {
      this.setNextStateError(64, var1);
   }

   protected final boolean __WL_postInvokeTxRetry() throws Exception {
      this.setNextState(256);
      if (this.sessionCtxHasMessageCtx) {
         WLEnterpriseBean var1 = (WLEnterpriseBean)this.__wrap_.getBean();
         SessionEJBContextImpl var2 = (SessionEJBContextImpl)var1.__WL_getEJBContext();
         var2.setMessageContext((MessageContext)null);
         this.sessionCtxHasMessageCtx = false;
      }

      boolean var3 = super.__WL_postInvokeTxRetry(this.__wrap_, this.theException);
      if (var3 && this.__wsContext != null) {
         this.setMessageContext();
      } else {
         this.__wsContext = null;
      }

      return var3;
   }

   protected final void __WL_wsPostInvoke() throws Exception {
      switch (this.getState()) {
         case 4:
            this.__WL_postInvokeWithoutBusiness();
            return;
         case 256:
            this.__WL_postInvokeCleanup();
            return;
         default:
            Debug.assertion(false, "wsPostInvoke encountered unhandleable state " + this.getState());
      }
   }

   public final void __WL_postInvokeWithoutBusiness() throws Exception {
      if (debugLogger.isDebugEnabled()) {
         debug("[BaseWSLocalObject] postInvokeWithoutBusiness  on: " + this);
      }

      this.setNextState(128);

      try {
         byte var1 = 0;
         super.postInvoke1(var1, this.__wrap_, this.theException);
      } catch (Exception var6) {
         this.theException = var6;
      } finally {
         this.__WL_postInvokeCleanup();
      }

   }

   public final void __WL_postInvokeCleanup() {
      if (debugLogger.isDebugEnabled()) {
         debug("[BaseWSLocalObject] __WL_postInvokeCleanup  on: " + this);
      }

      this.setNextState(512);

      try {
         super.__WL_postInvokeCleanup(this.__wrap_, this.theException);
      } catch (Throwable var2) {
         this.setNextStateError(1024, var2);
         EJBRuntimeUtils.throwEJBException("service object exception on postInvoke", var2);
      }

      this.setNextState(1024);
   }

   public final void __WL_methodComplete() {
      this.initialize();
   }

   public final boolean __WL_encounteredException() {
      return this.theException != null;
   }

   public final boolean __WL_isApplicationException() {
      Debug.assertion(this.__md_ != null, "isAppException invoked outside of service invoke cycle !");
      Debug.assertion(this.theException != null, "isAppException invoked when there was no exception encountered !");
      return EJBRuntimeUtils.isAppException(this.beanInfo, this.__md_.getMethod(), this.theException);
   }

   public final Throwable __WL_getException() {
      return this.theException;
   }

   protected final void __WL_setException(Throwable var1) {
      this.theException = var1;
   }

   private void setNextState(int var1) {
      if (this.allowedState(var1)) {
         this.setState(var1);
      } else {
         Debug.assertion(false, "attempt at illegal state transaction from " + this.getState() + " to " + var1);
      }

   }

   private void setNextStateError(int var1, Throwable var2) {
      this.setNextState(var1);
      this.theException = var2;
   }

   private boolean allowedState(int var1) {
      if ((var1 & this.getState()) != 0) {
         return true;
      } else {
         boolean var2 = false;
         short var3;
         switch (var1) {
            case 1:
               var3 = 1033;
               break;
            case 2:
               var3 = 9;
               break;
            case 4:
               var3 = 2;
               break;
            case 8:
               var3 = 2;
               break;
            case 16:
               var3 = 260;
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
               var3 = 96;
               break;
            case 512:
               var3 = 384;
               break;
            case 1024:
               var3 = 512;
               break;
            default:
               Debug.assertion(false, "unknown state argument to allowedMethod '" + var1 + "'");
               return false;
         }

         return (var3 & this.getState()) != 0;
      }
   }

   private void initialize() {
      this.setNextState(1);
      this.sessionCtxHasMessageCtx = false;
      this.theException = null;
      this.__wrap_ = null;
      this.__md_ = null;
      this.__wsContext = null;
   }

   private void setState(int var1) {
      this.state = var1;
   }

   private int getState() {
      return this.state;
   }

   protected final InvocationWrapper __WL_getWrap() {
      return this.__wrap_;
   }

   private static void debug(String var0) {
      debugLogger.debug("[BaseWSLocalObject] " + var0);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Preinvoke_After_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "BaseWSLocalObject.java", "weblogic.ejb.container.internal.BaseWSLocalObject", "__WL_preInvoke", "(Lweblogic/ejb/container/internal/MethodDescriptor;Lweblogic/security/service/ContextHandler;)Lweblogic/ejb/container/internal/InvocationWrapper;", 65, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Preinvoke_After_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
      $assertionsDisabled = !BaseWSLocalObject.class.desiredAssertionStatus();
   }
}
