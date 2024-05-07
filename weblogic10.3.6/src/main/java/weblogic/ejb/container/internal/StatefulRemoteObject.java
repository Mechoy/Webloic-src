package weblogic.ejb.container.internal;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.ejb.Handle;
import javax.ejb.RemoveException;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.manager.StatefulSessionManager;
import weblogic.ejb.spi.BusinessHandle;
import weblogic.ejb.spi.BusinessObject;
import weblogic.rmi.extensions.activation.Activator;
import weblogic.security.service.ContextHandler;

public abstract class StatefulRemoteObject extends BaseRemoteObject {
   protected Object primaryKey;
   private Activator ejbActivator;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = 1009251259173188943L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.StatefulRemoteObject");
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

   protected InvocationWrapper __WL_preInvoke(MethodDescriptor var1, ContextHandler var2) throws RemoteException {
      boolean var13 = false;

      InvocationWrapper var10000;
      try {
         var13 = true;
         if (!$assertionsDisabled && this.primaryKey == null) {
            throw new AssertionError();
         }

         InvocationWrapper var3 = null;

         try {
            if (!this.isImplementsRemote()) {
               var3 = EJBRuntimeUtils.createWrapWithTxsForBus(var1, this.primaryKey);
            } else {
               var3 = EJBRuntimeUtils.createWrapWithTxs(var1, this.primaryKey);
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

   protected void pushInvocationWrapperInThreadLocal(InvocationWrapper var1) {
   }

   protected void notifyRemoteCallBegin() {
   }

   protected Object getPrimaryKeyObject() throws RemoteException {
      throw new weblogic.utils.AssertionError("This shouldn't be called");
   }

   protected BusinessHandle _WL_getBusinessObjectHandle(MethodDescriptor var1) throws RemoteException {
      this.checkMethodPermissions(var1, new EJBContextHandler(var1, new Object[0]));
      if (debugLogger.isDebugEnabled()) {
         debug("Getting handle in eo:" + this);
      }

      String var2 = this.getImplementedBusinessInterfaceName();
      Class var3 = null;
      Object var4 = this;

      try {
         var3 = Class.forName(var2, false, this.getClass().getClassLoader());
      } catch (ClassNotFoundException var8) {
         throw new weblogic.utils.AssertionError("this shouldn't happen!");
      }

      if (!Remote.class.isAssignableFrom(var3)) {
         Ejb3SessionBeanInfo var5 = (Ejb3SessionBeanInfo)this.ejbHome.getBeanInfo();
         Class var6 = var5.getGeneratedRemoteBusinessIntfClass(var3);
         RemoteBusinessIntfProxy var7 = new RemoteBusinessIntfProxy(this, var5.getDeploymentInfo().getApplicationName(), var2, var6.getName());
         var4 = Proxy.newProxyInstance(var3.getClassLoader(), new Class[]{var3, BusinessObject.class}, var7);
      }

      return new BusinessHandleImpl(this.ejbHome, (BusinessObject)var4, this.primaryKey);
   }

   protected Handle getHandleObject() throws RemoteException {
      throw new weblogic.utils.AssertionError("This shouldn't be called");
   }

   protected void remove(MethodDescriptor var1) throws RemoteException, RemoveException {
      throw new weblogic.utils.AssertionError("This shouldn't be called");
   }

   private static void debug(String var0) {
      debugLogger.debug("[StatefulRemoteObject] " + var0);
   }

   public Activator getActivator() {
      return this.ejbActivator;
   }

   public void setActivator(Activator var1) {
      this.ejbActivator = var1;
   }

   public Object getActivationID() {
      return this.getPK();
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
         if (var5 && (var2 == null || EJBRuntimeUtils.isAppException((BeanInfo)this.beanInfo, (Method)var4, var2) && !var6)) {
            this.removeForRemoveAnnotation(var1);
         }
      }

      return var3;
   }

   private void removeForRemoveAnnotation(InvocationWrapper var1) throws Exception {
      ((StatefulSessionManager)this.beanManager).removeForRemoveAnnotation(var1);
   }

   protected void popInvocationWrapperInThreadLocalOnError(InvocationWrapper var1) {
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Before_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Postinvoke_Before_Low");
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Preinvoke_After_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatefulRemoteObject.java", "weblogic.ejb.container.internal.StatefulRemoteObject", "__WL_preInvoke", "(Lweblogic/ejb/container/internal/MethodDescriptor;Lweblogic/security/service/ContextHandler;)Lweblogic/ejb/container/internal/InvocationWrapper;", 42, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Preinvoke_After_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatefulRemoteObject.java", "weblogic.ejb.container.internal.StatefulRemoteObject", "__WL_postInvokeTxRetry", "(Lweblogic/ejb/container/internal/InvocationWrapper;Ljava/lang/Throwable;)Z", 136, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Postinvoke_Before_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true), null})}), (boolean)0);
      $assertionsDisabled = !StatefulRemoteObject.class.desiredAssertionStatus();
   }
}
