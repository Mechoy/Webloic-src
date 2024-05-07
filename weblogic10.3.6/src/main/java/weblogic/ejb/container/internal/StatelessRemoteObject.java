package weblogic.ejb.container.internal;

import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.ejb.EnterpriseBean;
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
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.spi.BusinessHandle;
import weblogic.ejb.spi.BusinessObject;
import weblogic.security.service.ContextHandler;

public abstract class StatelessRemoteObject extends BaseRemoteObject {
   static final long serialVersionUID = 2863313955071375981L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.StatelessRemoteObject");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;

   protected InvocationWrapper __WL_preInvoke(MethodDescriptor var1, ContextHandler var2) throws RemoteException {
      boolean var13 = false;

      InvocationWrapper var10000;
      try {
         var13 = true;
         if (debugLogger.isDebugEnabled()) {
            debug("[StatelessEJBObject] preInvoke with md: " + var1 + " on: " + this);
         }

         InvocationWrapper var3 = null;

         try {
            if (!this.isImplementsRemote()) {
               var3 = EJBRuntimeUtils.createWrapWithTxsForBus(var1);
            } else {
               var3 = EJBRuntimeUtils.createWrapWithTxs(var1);
            }
         } catch (InternalException var14) {
            EJBRuntimeUtils.throwRemoteException(var14);
         }

         var3 = super.preInvoke(var3, var2);
         var10000 = var3;
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

   public boolean postInvoke1(int var1, InvocationWrapper var2, Throwable var3) throws Exception {
      boolean var4 = super.postInvoke1(var1, var2, var3);
      if (var4) {
         this.beanManager.releaseBean(var2);
      }

      return var4;
   }

   public void notifyRemoteCallBegin() {
      currentInvocationWrapper.push(new BaseRemoteObject.ThreadLocalObject(true, this));
   }

   protected void pushInvocationWrapperInThreadLocal(InvocationWrapper var1) {
      Object var2 = currentInvocationWrapper.get();
      if (var2 instanceof BaseRemoteObject.ThreadLocalObject) {
         BaseRemoteObject.ThreadLocalObject var3 = (BaseRemoteObject.ThreadLocalObject)var2;
         if (var3.isRemote() && var3.getBaseRemoteObject() == this) {
            BaseRemoteObject.ThreadLocalObject var4 = (BaseRemoteObject.ThreadLocalObject)currentInvocationWrapper.pop();
            currentInvocationWrapper.push(var1);
         } else {
            currentInvocationWrapper.push(new BaseRemoteObject.ThreadLocalObject(false, this));
         }
      } else {
         currentInvocationWrapper.push(new BaseRemoteObject.ThreadLocalObject(false, this));
      }

   }

   protected void popInvocationWrapperInThreadLocalOnError(InvocationWrapper var1) {
      Object var2 = currentInvocationWrapper.get();
      if (var2 instanceof BaseRemoteObject.ThreadLocalObject) {
         BaseRemoteObject.ThreadLocalObject var3 = (BaseRemoteObject.ThreadLocalObject)var2;
         if (!var3.isRemote()) {
            currentInvocationWrapper.pop();
         }
      }

   }

   public void notifyRemoteCallEnd() {
      Object var1 = currentInvocationWrapper.get();
      if (var1 != null && var1 instanceof InvocationWrapper) {
         InvocationWrapper var6 = (InvocationWrapper)currentInvocationWrapper.pop();
         EnterpriseBean var3 = var6.getBean();
         Class var4 = ((SessionBeanInfo)this.ejbHome.getBeanInfo()).getGeneratedBeanClass();
         boolean var5;
         if (this.beanInfo.isEJB30()) {
            var5 = EJBRuntimeUtils.beanEq(var4, var3, this.beanInfo.getDeploymentInfo().getPitchforkContext());
         } else {
            var5 = var3 != null && var3.getClass() == var4;
         }

         if (!var6.hasSystemExceptionOccured() && var3 != null && var5) {
            this.beanManager.releaseBean(var6);
         }
      } else if (var1 instanceof BaseRemoteObject.ThreadLocalObject) {
         BaseRemoteObject.ThreadLocalObject var2 = (BaseRemoteObject.ThreadLocalObject)var1;
         if (var2.isRemote()) {
            currentInvocationWrapper.pop();
         }
      }

   }

   public void __WL_postInvokeCleanup(InvocationWrapper var1, Throwable var2) throws Exception {
      boolean var13;
      boolean var10000 = var13 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var14 = null;
      DiagnosticActionState[] var15 = null;
      Object var12 = null;
      if (var10000) {
         Object[] var8 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High.isArgumentsCaptureNeeded()) {
            var8 = new Object[]{this, var1, var2};
         }

         DynamicJoinPoint var22 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var8, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High;
         DiagnosticAction[] var10002 = var14 = var10001.getActions();
         InstrumentationSupport.preProcess(var22, var10001, var10002, var15 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         try {
            super.__WL_postInvokeCleanup(var1, var2);
         } finally {
            Object var5 = currentInvocationWrapper.get();
            if (var5 instanceof BaseRemoteObject.ThreadLocalObject) {
               BaseRemoteObject.ThreadLocalObject var6 = (BaseRemoteObject.ThreadLocalObject)currentInvocationWrapper.pop();
               if (!var6.isRemote() && !var1.hasSystemExceptionOccured()) {
                  this.beanManager.releaseBean(var1);
               }
            }

         }
      } finally {
         if (var13) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High, var14, var15);
         }

      }

   }

   protected Object getPrimaryKeyObject() throws RemoteException {
      throw new AssertionError("This shouldn't be called");
   }

   protected BusinessHandle _WL_getBusinessObjectHandle(MethodDescriptor var1) throws RemoteException {
      this.checkMethodPermissions(var1, new EJBContextHandler(var1, new Object[0]));
      if (debugLogger.isDebugEnabled()) {
         debug("Getting business object handle in bo:" + this);
      }

      String var2 = this.getImplementedBusinessInterfaceName();
      Class var3 = null;
      Object var4 = this;

      try {
         var3 = Class.forName(var2, false, this.getClass().getClassLoader());
      } catch (ClassNotFoundException var8) {
         throw new AssertionError("this shouldn't happen!");
      }

      if (!Remote.class.isAssignableFrom(var3)) {
         Ejb3SessionBeanInfo var5 = (Ejb3SessionBeanInfo)this.ejbHome.getBeanInfo();
         Class var6 = var5.getGeneratedRemoteBusinessIntfClass(var3);
         RemoteBusinessIntfProxy var7 = new RemoteBusinessIntfProxy(this, var5.getDeploymentInfo().getApplicationName(), var2, var6.getName());
         var4 = Proxy.newProxyInstance(var3.getClassLoader(), new Class[]{var3, BusinessObject.class}, var7);
      }

      return new BusinessHandleImpl(this.ejbHome, (BusinessObject)var4, var2);
   }

   protected Handle getHandleObject() throws RemoteException {
      throw new AssertionError("This shouldn't be called");
   }

   protected void remove(MethodDescriptor var1) throws RemoteException, RemoveException {
      throw new AssertionError("This shouldn't be called");
   }

   private static void debug(String var0) {
      debugLogger.debug("[StatelessRemoteObject] " + var0);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High");
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Preinvoke_After_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatelessRemoteObject.java", "weblogic.ejb.container.internal.StatelessRemoteObject", "__WL_preInvoke", "(Lweblogic/ejb/container/internal/MethodDescriptor;Lweblogic/security/service/ContextHandler;)Lweblogic/ejb/container/internal/InvocationWrapper;", 24, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Preinvoke_After_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatelessRemoteObject.java", "weblogic.ejb.container.internal.StatelessRemoteObject", "__WL_postInvokeCleanup", "(Lweblogic/ejb/container/internal/InvocationWrapper;Ljava/lang/Throwable;)V", 146, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true), null})}), (boolean)0);
   }
}
