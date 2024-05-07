package weblogic.ejb.container.internal;

import java.rmi.AccessException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import javax.ejb.ConcurrentAccessException;
import javax.ejb.EJBException;
import javax.ejb.EJBObject;
import javax.ejb.EnterpriseBean;
import javax.ejb.Handle;
import javax.ejb.NoSuchEJBException;
import javax.ejb.RemoveException;
import javax.transaction.Transaction;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.CachingManager;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.EntityEJBObjectIntf;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb20.interfaces.PrincipalNotFoundException;
import weblogic.ejb20.internal.HandleImpl;
import weblogic.j2ee.MethodInvocationHelper;
import weblogic.logging.Loggable;
import weblogic.rmi.extensions.NotificationListener;
import weblogic.security.service.ContextHandler;
import weblogic.transaction.TxHelper;
import weblogic.utils.StackTraceUtils;

public abstract class EntityEJBObject extends BaseRemoteObject implements EntityEJBObjectIntf, Remote, NotificationListener {
   private Object primaryKey;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = 3796764024524644703L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.EntityEJBObject");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;

   protected Handle getHandleObject() throws RemoteException {
      if (debugLogger.isDebugEnabled()) {
         debug("Getting handle in eo:" + this);
      }

      return new HandleImpl(this, this.primaryKey);
   }

   protected final boolean isIdentical(MethodDescriptor var1, EJBObject var2) throws RemoteException {
      return super.isIdentical(var1, var2) ? this.primaryKey.equals(var2.getPrimaryKey()) : false;
   }

   public void setPrimaryKey(Object var1) {
      boolean var8;
      boolean var10000 = var8 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var9 = null;
      DiagnosticActionState[] var10 = null;
      Object var7 = null;
      if (var10000) {
         Object[] var3 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         DynamicJoinPoint var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var3, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High;
         DiagnosticAction[] var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var13, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         if (!$assertionsDisabled && var1 == null) {
            throw new AssertionError();
         }

         this.primaryKey = var1;
      } finally {
         if (var8) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var9, var10);
         }

      }

   }

   protected final Object getPrimaryKeyObject() {
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
            var3 = EJBRuntimeUtils.createWrapWithTxs(var1, this.primaryKey);
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
               InstrumentationSupport.process(_WLDF$INST_JPFLD_1, var10003, var10003.getActions());
            }

         }
      }

      if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low.isEnabledAndNotDyeFiltered()) {
         DelegatingMonitor var10002 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low;
         InstrumentationSupport.process(_WLDF$INST_JPFLD_1, var10002, var10002.getActions());
      }

      return var10000;
   }

   protected void remove(MethodDescriptor var1) throws RemoteException, RemoveException {
      boolean var21;
      boolean var10000 = var21 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var22 = null;
      DiagnosticActionState[] var23 = null;
      Object var20 = null;
      if (var10000) {
         Object[] var16 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High.isArgumentsCaptureNeeded()) {
            var16 = new Object[]{this, var1};
         }

         DynamicJoinPoint var68 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var16, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High;
         DiagnosticAction[] var10002 = var22 = var10001.getActions();
         InstrumentationSupport.preProcess(var68, var10001, var10002, var23 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         BaseEJBHome var2 = (BaseEJBHome)this.getEJBHome();
         Transaction var3 = null;
         Transaction var4 = null;
         InvocationWrapper var5 = null;
         RemoveException var6 = null;

         try {
            var2.pushEnvironment();
            if (!var1.checkMethodPermissionsRemote(EJBContextHandler.EMPTY)) {
               Loggable var7 = EJBLogger.loginsufficientPermissionToUserLoggable(SecurityHelper.getCurrentPrincipal().getName(), "remove");
               SecurityException var8 = new SecurityException(var7.getMessage());
               throw new AccessException(var8.getMessage(), var8);
            }

            try {
               MethodInvocationHelper.pushMethodObject(var2.getBeanInfo());
               SecurityHelper.pushCallerPrincipal();
               var1.pushRunAsIdentity();
               var5 = EJBRuntimeUtils.createWrapWithTxs(var1, this.primaryKey);
               var4 = var5.getInvokeTx();
               var3 = var5.getCallerTx();
               this.pushInvocationWrapperInThreadLocal(var5);

               try {
                  var2.getBeanManager().remove(var5);
               } catch (InternalException var63) {
                  if (!(var63.getCause() instanceof NoSuchEJBException) && !(var63.getCause() instanceof ConcurrentAccessException)) {
                     throw var63;
                  }

                  throw new InternalException(var63.getMessage(), var63.getCause().getCause());
               }
            } catch (InternalException var64) {
               if (!(var64.detail instanceof RemoveException)) {
                  var2.handleSystemException(var5, var64);
                  throw new AssertionError("Should not reach here");
               }

               var6 = (RemoveException)var64.detail;
            } finally {
               var1.popRunAsIdentity();
               if (((EntityEJBHome)var2).getIsNoObjectActivation()) {
                  ((EntityEJBHome)var2).releaseEO(this.primaryKey);
               }

               try {
                  SecurityHelper.popCallerPrincipal();
               } catch (PrincipalNotFoundException var60) {
                  EJBLogger.logErrorPoppingCallerPrincipal(var60);
               }

            }

            try {
               if (MethodInvocationHelper.popMethodObject(var2.getBeanInfo())) {
                  var2.getBeanManager().handleUncommittedLocalTransaction(var5);
               }
            } catch (InternalException var62) {
               EJBRuntimeUtils.throwRemoteException(var62);
            }

            if (EJBRuntimeUtils.runningInOurTx(var5)) {
               try {
                  if (debugLogger.isDebugEnabled()) {
                     debug("Committing tx: " + var4);
                  }

                  var4.commit();
               } catch (Exception var61) {
                  EJBLogger.logErrorDuringCommit2(var4.toString(), StackTraceUtils.throwable2StackTrace(var61));
                  EJBRuntimeUtils.throwRemoteException("Exception during commit:", var61);
               }
            }

            if (var6 != null) {
               throw var6;
            }
         } finally {
            var2.popEnvironment();

            try {
               EJBRuntimeUtils.resumeCallersTransaction(var3, var4);
            } catch (InternalException var59) {
               EJBRuntimeUtils.throwRemoteException(var59);
            }

         }
      } finally {
         if (var21) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High, var22, var23);
         }

      }

   }

   public void notifyRemoteCallBegin() {
      currentInvocationWrapper.push(new BaseRemoteObject.ThreadLocalObject(true, this));
   }

   protected void pushInvocationWrapperInThreadLocal(InvocationWrapper var1) {
      Object var2 = currentInvocationWrapper.get();
      if (var2 instanceof BaseRemoteObject.ThreadLocalObject) {
         BaseRemoteObject.ThreadLocalObject var3 = (BaseRemoteObject.ThreadLocalObject)var2;
         if (var3.isRemote() && var3.getBaseRemoteObject() == this) {
            currentInvocationWrapper.pop();
            currentInvocationWrapper.push(var1);
            var1.setIsRemoteInvocation();
         }
      }

   }

   protected void popInvocationWrapperInThreadLocalOnError(InvocationWrapper var1) {
   }

   protected void setTxCreateAttributeOnBean(InvocationWrapper var1) {
      WLEnterpriseBean var2 = (WLEnterpriseBean)var1.getBean();
      if (var2 != null && EJBRuntimeUtils.runningInOurTx(var1)) {
         var2.__WL_setCreatorOfTx(var1.isRemoteInvocation());
      } else if (var2 != null) {
         var2.__WL_setCreatorOfTx(false);
      }

   }

   public void notifyRemoteCallEnd() {
      Object var1 = currentInvocationWrapper.get();
      if (var1 != null) {
         if (var1 instanceof InvocationWrapper) {
            InvocationWrapper var2 = (InvocationWrapper)currentInvocationWrapper.pop();
            if ((EJBRuntimeUtils.runningInOurTx(var2) || var2.getInvokeTx() == null) && !var2.hasSystemExceptionOccured()) {
               EnterpriseBean var3 = var2.getBean();
               Class var4 = ((EntityBeanInfo)this.ejbHome.getBeanInfo()).getGeneratedBeanClass();
               if (var3 != null && var3.getClass() == var4 && (((WLEnterpriseBean)var3).__WL_isCreatorOfTx() || var2.getInvokeTx() == null)) {
                  ((WLEnterpriseBean)var3).__WL_setCreatorOfTx(false);
                  this.ejbHome.getBeanManager().releaseBean(var2);
               }
            }
         } else if (var1 instanceof BaseRemoteObject.ThreadLocalObject) {
            BaseRemoteObject.ThreadLocalObject var5 = (BaseRemoteObject.ThreadLocalObject)var1;
            if (var5.isRemote()) {
               currentInvocationWrapper.pop();
            }
         }

      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof EntityEJBObject) {
         boolean var2 = false;

         try {
            var2 = this.isIdentical((EntityEJBObject)var1);
            return var2;
         } catch (RemoteException var4) {
            throw new EJBException(StackTraceUtils.throwable2StackTrace(var4));
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.primaryKey.hashCode();
   }

   public void operationsComplete() {
      weblogic.transaction.Transaction var1 = TxHelper.getTransaction();
      ((CachingManager)this.beanManager).operationsComplete(var1, this.primaryKey);
   }

   private static void debug(String var0) {
      debugLogger.debug("[EntityEJBObject] " + var0);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Preinvoke_After_Low");
      _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Database_Access_Around_High");
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EntityEJBObject.java", "weblogic.ejb.container.internal.EntityEJBObject", "setPrimaryKey", "(Ljava/lang/Object;)V", 64, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EntityEJBObject.java", "weblogic.ejb.container.internal.EntityEJBObject", "__WL_preInvoke", "(Lweblogic/ejb/container/internal/MethodDescriptor;Lweblogic/security/service/ContextHandler;)Lweblogic/ejb/container/internal/InvocationWrapper;", 72, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Preinvoke_After_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EntityEJBObject.java", "weblogic.ejb.container.internal.EntityEJBObject", "remove", "(Lweblogic/ejb/container/internal/MethodDescriptor;)V", 92, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true)})}), (boolean)0);
      $assertionsDisabled = !EntityEJBObject.class.desiredAssertionStatus();
   }
}
