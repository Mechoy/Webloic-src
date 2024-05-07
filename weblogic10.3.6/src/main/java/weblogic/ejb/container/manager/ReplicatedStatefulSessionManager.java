package weblogic.ejb.container.manager;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJBException;
import javax.ejb.EJBObject;
import javax.naming.Context;
import weblogic.cluster.replication.ROID;
import weblogic.cluster.replication.ROInfo;
import weblogic.cluster.replication.Replicatable;
import weblogic.cluster.replication.ReplicationManager;
import weblogic.cluster.replication.ReplicationServices;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BaseEJBLocalHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBLocalObjectIntf;
import weblogic.ejb.container.interfaces.BaseEJBRemoteHomeIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.interfaces.StatefulEJBObjectIntf;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb.container.internal.InvocationWrapper;
import weblogic.ejb.container.internal.StatefulEJBHome;
import weblogic.ejb.container.internal.StatefulEJBHomeImpl;
import weblogic.ejb.container.internal.StatefulEJBLocalHome;
import weblogic.ejb.container.internal.StatefulRemoteObject;
import weblogic.ejb.container.replication.ReplicatedBean;
import weblogic.ejb.container.replication.ReplicatedBeanManager;
import weblogic.ejb.container.replication.ReplicatedEJB3ViewBean;
import weblogic.ejb.container.swap.ReplicatedMemorySwap;
import weblogic.ejb.spi.EJBCache;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.kernel.Kernel;
import weblogic.logging.LogOutputStream;
import weblogic.management.configuration.ServerMBean;
import weblogic.rmi.cluster.PrimarySecondaryRemoteObject;
import weblogic.rmi.extensions.activation.Activator;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.utils.StackTraceUtils;

public final class ReplicatedStatefulSessionManager extends StatefulSessionManager implements ReplicatedBeanManager {
   private boolean inCluster;
   private ReplicationServices repServ = ReplicationManager.services();
   private Map primariesEO = Collections.synchronizedMap(new HashMap());
   private StatefulEJBHome remoteHome = null;
   private StatefulEJBLocalHome localHome = null;
   private LogOutputStream log = new LogOutputStream("EJB");
   static final long serialVersionUID = -4002103232338924108L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.manager.ReplicatedStatefulSessionManager");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;
   public static final JoinPoint _WLDF$INST_JPFLD_3;
   public static final JoinPoint _WLDF$INST_JPFLD_4;

   public ReplicatedStatefulSessionManager(EJBComponentRuntimeMBeanImpl var1) {
      super(var1);
   }

   public void setup(BaseEJBRemoteHomeIntf var1, BaseEJBLocalHomeIntf var2, BeanInfo var3, Context var4, EJBCache var5) throws WLDeploymentException {
      super.setup(var1, var2, var3, var4, var5);
      this.remoteHome = (StatefulEJBHome)var1;
      this.localHome = (StatefulEJBLocalHome)var2;
      this.inCluster = ((ServerMBean)Kernel.getConfig()).getCluster() != null;
      if (this.inCluster) {
         this.swapper = new ReplicatedMemorySwap();
         this.swapper.setup(var3, this, var3.getClassLoader());
         this.serializeCalls = ((SessionBeanInfo)var3).statefulSessionSerializesConcurrentCalls();
      }

   }

   private StatefulRemoteObject registerReplicatedBO(Object var1, Class var2, Class var3) throws InternalException {
      StatefulRemoteObject var4 = null;
      ReplicatedMemorySwap var5 = null;
      String var6 = this.ejbHome.getJNDINameAsString();
      if (var6 == null) {
         var6 = this.getBeanInfo().getIsIdenticalKey();
      }

      if (this.inCluster) {
         try {
            var5 = (ReplicatedMemorySwap)this.swapper;
            ROInfo var7 = null;
            if (var1 == null) {
               var7 = this.repServ.register(new ReplicatedEJB3ViewBean(var6, var3));
               var1 = var7.getROID();
            } else {
               var7 = this.repServ.add((ROID)var1, new ReplicatedEJB3ViewBean(var6, var3));
            }

            var4 = (StatefulRemoteObject)((StatefulEJBHomeImpl)this.remoteHome).allocateBI(var1, var2, var3, (Activator)null);
            Object var8 = var7.getSecondaryROInfo();
            PrimarySecondaryRemoteObject var9 = new PrimarySecondaryRemoteObject((Remote)var4, (Remote)var8);
            var5.savePrimaryRO(var1, var9);
         } catch (RemoteException var10) {
            EJBRuntimeUtils.throwInternalException("Exception while registering replicated bean.", var10);
         }
      } else {
         if (var1 == null) {
            var1 = this.keyGenerator.nextKey();
         }

         var4 = (StatefulRemoteObject)((StatefulEJBHomeImpl)this.remoteHome).allocateBI(var1, var2, var3, (Activator)null);
      }

      var4.setPrimaryKey(var1);
      return var4;
   }

   public StatefulRemoteObject remoteCreateForBI(Object var1, Class var2, Activator var3, Class var4) throws InternalException {
      boolean var16;
      boolean var10000 = var16 = _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var17 = null;
      DiagnosticActionState[] var18 = null;
      Object var15 = null;
      DelegatingMonitor var10001;
      if (var10000) {
         JoinPoint var26 = _WLDF$INST_JPFLD_0;
         var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High;
         DiagnosticAction[] var10002 = var17 = var10001.getActions();
         InstrumentationSupport.preProcess(var26, var10001, var10002, var18 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var22 = false;

      StatefulRemoteObject var27;
      label136: {
         try {
            var22 = true;
            StatefulRemoteObject var5 = null;

            try {
               var5 = this.registerReplicatedBO(var1, var2, var4);
               if (var1 != null) {
                  var27 = var5;
                  var22 = false;
                  break label136;
               }

               var1 = var5.getPK();
               EJBObject var6 = null;
               BaseEJBLocalObjectIntf var7 = null;
               InvocationWrapper var8 = EJBRuntimeUtils.createWrap();
               if (!this.inCluster && this.beanInfo.hasDeclaredRemoteHome()) {
                  var6 = this.remoteHome.allocateEO(var1);
               }

               if (this.beanInfo.hasDeclaredLocalHome()) {
                  var7 = this.localHome.allocateELO(var1);
               }

               try {
                  super.create(var6, var7, var1, var8, (Method)null, (Method)null, (Object[])null);
               } catch (Exception var23) {
                  if (this.inCluster) {
                     ((ReplicatedMemorySwap)this.swapper).remove(var1);
                  }

                  EJBRuntimeUtils.throwInternalException("Exception in create", var23);
               }
            } catch (Exception var24) {
               EJBRuntimeUtils.throwInternalException("Exception in remote create", var24);
            }

            var27 = var5;
            var22 = false;
         } finally {
            if (var22) {
               var10001 = null;
               if (var16) {
                  InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High, var17, var18);
               }

            }
         }

         if (var16) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High, var17, var18);
         }

         return var27;
      }

      if (var16) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High, var17, var18);
      }

      return var27;
   }

   public EJBObject remoteCreate(InvocationWrapper var1, Method var2, Method var3, Object[] var4) throws InternalException {
      Object var5 = null;
      StatefulEJBObjectIntf var6 = null;
      BaseEJBLocalObjectIntf var7 = null;

      try {
         ReplicatedMemorySwap var8 = null;
         if (this.inCluster) {
            var8 = (ReplicatedMemorySwap)this.swapper;
            ROInfo var9 = this.repServ.register(new ReplicatedBean(this.ejbHome.getJNDINameAsString()));
            var5 = var9.getROID();
            var6 = (StatefulEJBObjectIntf)this.remoteHome.allocateEO(var5);
            EJBObject var10 = (EJBObject)var9.getSecondaryROInfo();
            PrimarySecondaryRemoteObject var11 = new PrimarySecondaryRemoteObject(var6, var10);
            var8.savePrimaryRO(var5, var11);
         } else {
            var5 = this.keyGenerator.nextKey();
            var6 = (StatefulEJBObjectIntf)this.remoteHome.allocateEO(var5);
            new PrimarySecondaryRemoteObject(var6, (Remote)null);
         }

         var6.setPrimaryKey(var5);
         if (this.localHome != null) {
            var7 = this.localHome.allocateELO(var5);
         }

         try {
            super.create(var6, var7, var5, var1, var2, var3, var4);
         } catch (Exception var12) {
            if (this.inCluster) {
               var8.remove(var5);
            }

            EJBRuntimeUtils.throwInternalException("Exception in create", var12);
         }
      } catch (Exception var13) {
         EJBRuntimeUtils.throwInternalException("Exception in remote create", var13);
      }

      return var6;
   }

   public void remove(InvocationWrapper var1) throws InternalException {
      if (this.inCluster) {
         this.getBean(var1.getPrimaryKey());
         this.swapper.remove(var1.getPrimaryKey());
      }

      super.remove(var1);
   }

   public void removeForRemoveAnnotation(InvocationWrapper var1) throws InternalException {
      if (this.inCluster) {
         this.getBean(var1.getPrimaryKey());
         this.swapper.remove(var1.getPrimaryKey());
      }

      super.removeForRemoveAnnotation(var1);
   }

   public void becomePrimary(Object var1) {
      boolean var8;
      boolean var10000 = var8 = _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var9 = null;
      DiagnosticActionState[] var10 = null;
      Object var7 = null;
      if (var10000) {
         JoinPoint var13 = _WLDF$INST_JPFLD_1;
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High;
         DiagnosticAction[] var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var13, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         if (debugLogger.isDebugEnabled()) {
            debug("** becomePrimary called on key: " + var1);
         }

         ((ReplicatedMemorySwap)this.swapper).becomePrimary(var1);
      } finally {
         if (var8) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High, var9, var10);
         }

      }

   }

   public Remote createSecondary(Object var1) {
      boolean var10;
      boolean var10000 = var10 = _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var11 = null;
      DiagnosticActionState[] var12 = null;
      Object var9 = null;
      DelegatingMonitor var10001;
      if (var10000) {
         JoinPoint var18 = _WLDF$INST_JPFLD_2;
         var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High;
         DiagnosticAction[] var10002 = var11 = var10001.getActions();
         InstrumentationSupport.preProcess(var18, var10001, var10002, var12 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var15 = false;

      Remote var19;
      try {
         var15 = true;
         if (debugLogger.isDebugEnabled()) {
            debug(" ** createSecondary " + var1);
         }

         EJBObject var2 = this.remoteHome.allocateEO(var1);

         try {
            ((ReplicatedMemorySwap)this.swapper).saveSecondaryRO(var1, new PrimarySecondaryRemoteObject(var2, (Remote)null));
            Remote var3 = ServerHelper.exportObject(var2, "");
            if (debugLogger.isDebugEnabled()) {
               debug(" createSecondary remote: " + var3.getClass().getName());
            }

            var19 = var3;
            var15 = false;
         } catch (RemoteException var16) {
            EJBLogger.logFailedToCreateCopy(this.ejbHome.getDisplayName(), StackTraceUtils.throwable2StackTrace(var16));
            throw new EJBException(var16);
         }
      } finally {
         if (var15) {
            var10001 = null;
            if (var10) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High, var11, var12);
            }

         }
      }

      if (var10) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High, var11, var12);
      }

      return var19;
   }

   public Remote createSecondaryForBI(Object var1, Class var2) {
      if (debugLogger.isDebugEnabled()) {
         debug(" ** createSecondary " + var1);
      }

      Ejb3SessionBeanInfo var3 = (Ejb3SessionBeanInfo)this.beanInfo;
      Class var4 = var3.getGeneratedRemoteBusinessImplClass(var2);
      Remote var5 = ((StatefulEJBHomeImpl)this.remoteHome).allocateBI(var1, var4, var2, (Activator)null);

      try {
         ((ReplicatedMemorySwap)this.swapper).saveSecondaryRO(var1, new PrimarySecondaryRemoteObject(var5, (Remote)null));
         Remote var6 = ServerHelper.exportObject(var5, "");
         if (debugLogger.isDebugEnabled()) {
            debug(" createSecondary remote: " + var6.getClass().getName());
         }

         return var6;
      } catch (RemoteException var7) {
         EJBLogger.logFailedToCreateCopy(this.ejbHome.getDisplayName(), StackTraceUtils.throwable2StackTrace(var7));
         throw new EJBException(var7);
      }
   }

   public void removeSecondary(Object var1) {
      boolean var8;
      boolean var10000 = var8 = _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var9 = null;
      DiagnosticActionState[] var10 = null;
      Object var7 = null;
      if (var10000) {
         JoinPoint var13 = _WLDF$INST_JPFLD_3;
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High;
         DiagnosticAction[] var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var13, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         if (debugLogger.isDebugEnabled()) {
            debug("*** removeSecondary with key: " + var1);
         }

         this.swapper.remove(var1);
      } finally {
         if (var8) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_3, _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High, var9, var10);
         }

      }

   }

   public void updateSecondary(Object var1, Serializable var2) {
      boolean var10;
      boolean var10000 = var10 = _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var11 = null;
      DiagnosticActionState[] var12 = null;
      Object var9 = null;
      if (var10000) {
         JoinPoint var17 = _WLDF$INST_JPFLD_4;
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High;
         DiagnosticAction[] var10002 = var11 = var10001.getActions();
         InstrumentationSupport.preProcess(var17, var10001, var10002, var12 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         if (debugLogger.isDebugEnabled()) {
            debug("*** updateSecondary with key: " + var1);
         }

         try {
            ((ReplicatedMemorySwap)this.swapper).receiveUpdate(var1, var2);
         } catch (Exception var15) {
            EJBLogger.logFailedToUpdateSecondaryCopy(StackTraceUtils.throwable2StackTrace(var15));
         }
      } finally {
         if (var10) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_4, _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High, var11, var12);
         }

      }

   }

   public void destroyInstance(InvocationWrapper var1, Throwable var2) {
      super.destroyInstance(var1, var2);
      if (this.inCluster) {
         this.repServ.unregister((ROID)var1.getPrimaryKey(), Replicatable.DEFAULT_KEY);
      }

   }

   public StatefulEJBObjectIntf registerReplicatedObject(Object var1) throws InternalException {
      StatefulEJBObjectIntf var2 = null;
      ReplicatedMemorySwap var3 = null;

      try {
         var3 = (ReplicatedMemorySwap)this.swapper;
         ROInfo var4 = null;
         if (var1 == null) {
            var4 = this.repServ.register(new ReplicatedBean(this.ejbHome.getJNDINameAsString()));
         } else {
            var4 = this.repServ.add((ROID)var1, new ReplicatedBean(this.ejbHome.getJNDINameAsString()));
         }

         ROID var8 = var4.getROID();
         var2 = (StatefulEJBObjectIntf)this.remoteHome.allocateEO(var8);
         EJBObject var5 = (EJBObject)var4.getSecondaryROInfo();
         PrimarySecondaryRemoteObject var6 = new PrimarySecondaryRemoteObject(var2, var5);
         var3.savePrimaryRO(var8, var6);
         var2.setPrimaryKey(var8);
      } catch (RemoteException var7) {
         EJBRuntimeUtils.throwInternalException("Exception while registering replicated bean.", var7);
      }

      return var2;
   }

   public boolean isInCluster() {
      return this.inCluster;
   }

   private static void debug(String var0) {
      debugLogger.debug("[ReplicatedStatefulSessionManager] " + var0);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Replicated_Session_Manager_Around_High = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Replicated_Session_Manager_Around_High");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ReplicatedStatefulSessionManager.java", "weblogic.ejb.container.manager.ReplicatedStatefulSessionManager", "remoteCreateForBI", "(Ljava/lang/Object;Ljava/lang/Class;Lweblogic/rmi/extensions/activation/Activator;Ljava/lang/Class;)Lweblogic/ejb/container/internal/StatefulRemoteObject;", 151, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ReplicatedStatefulSessionManager.java", "weblogic.ejb.container.manager.ReplicatedStatefulSessionManager", "becomePrimary", "(Ljava/lang/Object;)V", 289, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ReplicatedStatefulSessionManager.java", "weblogic.ejb.container.manager.ReplicatedStatefulSessionManager", "createSecondary", "(Ljava/lang/Object;)Ljava/rmi/Remote;", 296, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_3 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ReplicatedStatefulSessionManager.java", "weblogic.ejb.container.manager.ReplicatedStatefulSessionManager", "removeSecondary", "(Ljava/lang/Object;)V", 350, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_4 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ReplicatedStatefulSessionManager.java", "weblogic.ejb.container.manager.ReplicatedStatefulSessionManager", "updateSecondary", "(Ljava/lang/Object;Ljava/io/Serializable;)V", 359, (Map)null, (boolean)0);
   }
}
