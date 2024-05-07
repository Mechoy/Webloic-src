package weblogic.ejb.container.internal;

import java.io.Serializable;
import java.lang.reflect.Method;
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
import weblogic.ejb.container.interfaces.BaseEJBLocalObjectIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.replication.ReplicatedBeanManager;
import weblogic.ejb.spi.WLDeploymentException;

public abstract class StatefulEJBLocalHome extends BaseEJBLocalHome {
   private boolean usesBeanManagedTx = false;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = 3366907767775611244L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.StatefulEJBLocalHome");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   public StatefulEJBLocalHome(Class var1) {
      super(var1);
   }

   public void setup(BeanInfo var1, BaseEJBHomeIntf var2, BeanManager var3) throws WLDeploymentException {
      super.setup(var1, var2, var3);
      SessionBeanInfo var4 = (SessionBeanInfo)var1;
      this.usesBeanManagedTx = var4.usesBeanManagedTx();
   }

   protected BaseEJBLocalObjectIntf create(MethodDescriptor var1, Method var2, Object[] var3) throws Exception {
      BaseEJBLocalObjectIntf var26;
      try {
         this.pushEnvironment();
         if (debugLogger.isDebugEnabled()) {
            debug("[StatefulEJBLocalHome] Creating a bean from md: " + var1);
         }

         BaseEJBLocalObjectIntf var4 = null;
         InvocationWrapper var5 = this.preHomeInvoke(var1, new EJBContextHandler(var1, var3));
         Throwable var6 = null;

         try {
            if (!$assertionsDisabled && var5.getInvokeTx() != null) {
               throw new AssertionError();
            }

            BeanManager var25 = this.getBeanManager();
            var4 = (BaseEJBLocalObjectIntf)var25.localCreate(var5, var2, (Method)null, var3);
         } catch (InternalException var20) {
            InternalException var24 = var20;
            var6 = var20.detail;
            if (debugLogger.isDebugEnabled()) {
               debug("Got exception back from manager: ", var20);
            }

            if (EJBRuntimeUtils.isAppException((BeanInfo)this.beanInfo, (Method)var2, var6)) {
               throw (Exception)var6;
            }

            this.handleSystemException(var5, var20);
            throw new weblogic.utils.AssertionError("Should never have reached here");
         } catch (Throwable var21) {
            Throwable var7 = var21;
            var6 = var21;
            this.handleSystemException(var5, var21);
            throw new weblogic.utils.AssertionError("Should never have reached here");
         } finally {
            this.postHomeInvoke(var5, var6);
         }

         var26 = var4;
      } finally {
         this.popEnvironment();
      }

      return var26;
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

         DynamicJoinPoint var14 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var4, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium;
         DiagnosticAction[] var10002 = var10 = var10001.getActions();
         InstrumentationSupport.preProcess(var14, var10001, var10002, var11 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         throw new RemoveException("Cannot remove stateful session beans using EJBLocalHome.remove(Object primaryKey)");
      } finally {
         if (var9) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium, var10, var11);
         }

      }
   }

   public BaseEJBLocalObjectIntf allocateELO(Object var1) {
      StatefulEJBLocalObject var2 = (StatefulEJBLocalObject)this.allocateELO();
      var2.setPrimaryKey(var1);
      var2.setBeanManager(this.getBeanManager());
      var2.setBeanInfo(this.getBeanInfo());
      var2.setIsEJB30ClientView(false);
      return var2;
   }

   public BaseEJBLocalObjectIntf allocateELO() {
      StatefulEJBLocalObject var1 = null;

      try {
         var1 = (StatefulEJBLocalObject)this.eloClass.newInstance();
      } catch (InstantiationException var3) {
         throw new weblogic.utils.AssertionError(var3);
      } catch (IllegalAccessException var4) {
         throw new weblogic.utils.AssertionError(var4);
      }

      var1.setBeanInfo(this.getBeanInfo());
      var1.setEJBLocalHome(this);
      var1.setBeanManager(this.getBeanManager());
      var1.setIsEJB30ClientView(false);
      return var1;
   }

   public void cleanup() {
   }

   public boolean usesBeanManagedTx() {
      return this.usesBeanManagedTx;
   }

   public void becomePrimary(Object var1) {
      ((ReplicatedBeanManager)this.beanManager).becomePrimary(var1);
   }

   public Object createSecondary(Object var1) {
      return ((ReplicatedBeanManager)this.beanManager).createSecondary(var1);
   }

   public void removeSecondary(Object var1) {
      ((ReplicatedBeanManager)this.beanManager).removeSecondary(var1);
   }

   public void updateSecondary(Object var1, Serializable var2) {
      ((ReplicatedBeanManager)this.beanManager).updateSecondary(var1, var2);
   }

   private static void debug(String var0) {
      debugLogger.debug("[StatefulEJBLocalHome] " + var0);
   }

   private static void debug(String var0, Throwable var1) {
      debugLogger.debug("[StatefulEJBLocalHome] " + var0, var1);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Home_Remove_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatefulEJBLocalHome.java", "weblogic.ejb.container.internal.StatefulEJBLocalHome", "remove", "(Lweblogic/ejb/container/internal/MethodDescriptor;Ljava/lang/Object;)V", 124, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Home_Remove_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
      $assertionsDisabled = !StatefulEJBLocalHome.class.desiredAssertionStatus();
   }
}
