package weblogic.ejb.container.internal;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.ConcurrentAccessException;
import javax.ejb.EJBMetaData;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.NoSuchEJBException;
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
import weblogic.ejb.container.interfaces.BaseEJBHomeIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.interfaces.StatefulEJBObjectIntf;
import weblogic.ejb.container.replication.ReplicatedBeanManager;
import weblogic.ejb.container.replication.ReplicatedHome;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.internal.EJBMetaDataImpl;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.rmi.extensions.activation.Activatable;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.collections.SoftHashMap;

public abstract class StatefulEJBHome extends BaseEJBHome implements ReplicatedHome {
   private boolean isInMemoryReplication = false;
   private boolean isNoObjectActivation = false;
   private boolean usesBeanManagedTx = false;
   private EJBActivator ejbActivator = new EJBActivator(this);
   private Map eoMap = Collections.synchronizedMap(new SoftHashMap());
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = 3228386374483738647L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.StatefulEJBHome");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;

   public StatefulEJBHome(Class var1) {
      super(var1);
   }

   public void setup(BeanInfo var1, BaseEJBHomeIntf var2, BeanManager var3) throws WLDeploymentException {
      super.setup(var1, var2, var3);
      SessionBeanInfo var4 = (SessionBeanInfo)var1;
      this.usesBeanManagedTx = var4.usesBeanManagedTx();
      this.isInMemoryReplication = var4.getReplicationType() == 2;
      if (this.eoClass != null) {
         this.isNoObjectActivation = !Activatable.class.isAssignableFrom(this.eoClass);
      }

   }

   protected EJBMetaData getEJBMetaDataInstance() {
      return new EJBMetaDataImpl(this, this.beanInfo.getHomeInterfaceClass(), this.beanInfo.getRemoteInterfaceClass(), (Class)null, true, false);
   }

   protected EJBObject create(MethodDescriptor var1, Method var2, Object[] var3) throws Exception {
      EJBObject var4 = null;

      try {
         this.pushEnvironment();
         if (debugLogger.isDebugEnabled()) {
            debug("[StatefulEJBHome] Creating a bean from md: " + var1);
         }

         InvocationWrapper var5 = this.preHomeInvoke(var1, new EJBContextHandler(var1, var3));
         Throwable var6 = null;

         try {
            if (!$assertionsDisabled && var5.getInvokeTx() != null) {
               throw new AssertionError();
            }

            BeanManager var7 = this.getBeanManager();
            var4 = var7.remoteCreate(var5, var2, (Method)null, var3);
         } catch (InternalException var20) {
            var6 = var20.detail;
            if (debugLogger.isDebugEnabled()) {
               debug("Got exception back from manager: " + var20.getClass().getName());
            }

            if (EJBRuntimeUtils.isAppException((BeanInfo)this.beanInfo, (Method)var2, var6)) {
               throw (Exception)var6;
            }

            this.handleSystemException(var5, var20);
            throw new weblogic.utils.AssertionError("Should never have reached here");
         } catch (Throwable var21) {
            var6 = var21;
            this.handleSystemException(var5, var21);
            throw new weblogic.utils.AssertionError("Should never reach here");
         } finally {
            this.postHomeInvoke(var5, var6);
         }
      } finally {
         this.popEnvironment();
      }

      return var4;
   }

   public void remove(MethodDescriptor var1, Handle var2) throws RemoteException, RemoveException {
      boolean var19;
      boolean var10000 = var19 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var20 = null;
      DiagnosticActionState[] var21 = null;
      Object var18 = null;
      if (var10000) {
         Object[] var14 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium.isArgumentsCaptureNeeded()) {
            var14 = new Object[]{this, var1, var2};
         }

         DynamicJoinPoint var46 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var14, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium;
         DiagnosticAction[] var10002 = var20 = var10001.getActions();
         InstrumentationSupport.preProcess(var46, var10001, var10002, var21 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         try {
            this.pushEnvironment();
            this.validateHandleFromHome(var2);
            StatefulEJBObjectIntf var3 = (StatefulEJBObjectIntf)PortableRemoteObject.narrow(var2.getEJBObject(), StatefulEJBObjectIntf.class);
            Object var4 = var3.getPK();
            InvocationWrapper var5 = this.preHomeInvoke(var1, new EJBContextHandler(var1, new Object[]{var2}));
            Throwable var6 = null;

            try {
               var5.setPrimaryKey(var4);
               Object var7 = null;

               try {
                  this.getBeanManager().remove(var5);
               } catch (InternalException var40) {
                  if (!(var40.getCause() instanceof NoSuchEJBException) && !(var40.getCause() instanceof ConcurrentAccessException)) {
                     throw var40;
                  }

                  throw new InternalException(var40.getMessage(), var40.getCause().getCause());
               }
            } catch (InternalException var41) {
               var6 = var41.detail;
               if (EJBRuntimeUtils.isAppException((BeanInfo)this.beanInfo, (Method)var1.getMethod(), var6)) {
                  if (var6 instanceof RemoveException) {
                     throw (RemoveException)var6;
                  }

                  throw new weblogic.utils.AssertionError("Invalid Exception thrown from remove: " + StackTraceUtils.throwable2StackTrace(var6));
               }

               this.handleSystemException(var5, var41);
               throw new weblogic.utils.AssertionError("Should never have reached here");
            } catch (Throwable var42) {
               var6 = var42;
               this.handleSystemException(var5, var42);
               throw new weblogic.utils.AssertionError("Should never reach here");
            } finally {
               if (this.getIsNoObjectActivation() || this.getIsInMemoryReplication()) {
                  this.releaseEO(var4);
                  if (this instanceof StatefulEJBHomeImpl) {
                     ((StatefulEJBHomeImpl)this).releaseBOs(var4);
                  }
               }

               this.postHomeInvoke(var5, var6);
            }
         } finally {
            this.popEnvironment();
         }
      } finally {
         if (var19) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium, var20, var21);
         }

      }

   }

   public void remove(MethodDescriptor var1, Object var2) throws RemoveException {
      boolean var9;
      boolean var10000 = var9 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var10 = null;
      DiagnosticActionState[] var11 = null;
      Object var8 = null;
      if (var10000) {
         Object[] var4 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium.isArgumentsCaptureNeeded()) {
            var4 = new Object[]{this, var1, var2};
         }

         DynamicJoinPoint var14 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var4, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium;
         DiagnosticAction[] var10002 = var10 = var10001.getActions();
         InstrumentationSupport.preProcess(var14, var10001, var10002, var11 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         throw new RemoveException("Cannot remove stateful session beans using EJBHome.remove(Object primaryKey)");
      } finally {
         if (var9) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium, var10, var11);
         }

      }
   }

   public EJBObject allocateEO() {
      StatefulEJBObject var1 = null;

      try {
         var1 = (StatefulEJBObject)this.eoClass.newInstance();
      } catch (InstantiationException var3) {
         throw new weblogic.utils.AssertionError(var3);
      } catch (IllegalAccessException var4) {
         throw new weblogic.utils.AssertionError(var4);
      }

      var1.setEJBHome(this);
      var1.setBeanManager(this.getBeanManager());
      var1.setBeanInfo(this.getBeanInfo());
      return var1;
   }

   public EJBObject getEJBObject(Object var1) {
      return (StatefulEJBObject)this.eoMap.get(var1);
   }

   public EJBObject allocateEO(Object var1) {
      StatefulEJBObject var2;
      if (!this.getIsNoObjectActivation() && !this.getIsInMemoryReplication()) {
         var2 = (StatefulEJBObject)this.allocateEO();
         var2.setPrimaryKey(var1);
         var2.setBeanManager(this.getBeanManager());
         var2.setBeanInfo(this.getBeanInfo());
         var2.setActivator(this.ejbActivator);
         return var2;
      } else {
         var2 = (StatefulEJBObject)this.eoMap.get(var1);
         if (var2 == null) {
            var2 = (StatefulEJBObject)this.allocateEO();
            var2.setPrimaryKey(var1);
            var2.setBeanManager(this.getBeanManager());
            var2.setBeanInfo(this.getBeanInfo());
            this.eoMap.put(var1, var2);
         }

         return var2;
      }
   }

   public void releaseEO(Object var1) {
      this.eoMap.remove(var1);
   }

   public void cleanup() {
      if (this.getIsNoObjectActivation() || this.getIsInMemoryReplication()) {
         Collection var1 = this.eoMap.values();
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            EJBObject var3 = (EJBObject)var2.next();
            this.unexportEO(var3);
         }
      }

      if (this.beanInfo.hasDeclaredRemoteHome()) {
         this.unexportEJBActivator(this.ejbActivator, this.eoClass);
      }

   }

   public boolean usesBeanManagedTx() {
      return this.usesBeanManagedTx;
   }

   public boolean getIsInMemoryReplication() {
      return this.isInMemoryReplication;
   }

   public boolean getIsNoObjectActivation() {
      return this.isNoObjectActivation;
   }

   public void becomePrimary(Object var1) throws RemoteException {
      ((ReplicatedBeanManager)this.beanManager).becomePrimary(var1);
   }

   public Object createSecondary(Object var1) throws RemoteException {
      return ((ReplicatedBeanManager)this.beanManager).createSecondary(var1);
   }

   public Object createSecondaryForBI(Object var1, String var2) throws RemoteException {
      throw new weblogic.utils.AssertionError("this method should not be invoked");
   }

   public void removeSecondary(Object var1) throws RemoteException {
      ((ReplicatedBeanManager)this.beanManager).removeSecondary(var1);
      StatefulEJBObject var2 = (StatefulEJBObject)this.eoMap.remove(var1);
      if (var2 != null) {
         this.unexportEO(var2, false);
      }

   }

   public void updateSecondary(Object var1, Serializable var2) throws RemoteException {
      ((ReplicatedBeanManager)this.beanManager).updateSecondary(var1, var2);
   }

   private static void debug(String var0) {
      debugLogger.debug("[StatefulEJBHome] " + var0);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Home_Remove_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatefulEJBHome.java", "weblogic.ejb.container.internal.StatefulEJBHome", "remove", "(Lweblogic/ejb/container/internal/MethodDescriptor;Ljavax/ejb/Handle;)V", 165, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Home_Remove_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatefulEJBHome.java", "weblogic.ejb.container.internal.StatefulEJBHome", "remove", "(Lweblogic/ejb/container/internal/MethodDescriptor;Ljava/lang/Object;)V", 231, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Home_Remove_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
      $assertionsDisabled = !StatefulEJBHome.class.desiredAssertionStatus();
   }
}
