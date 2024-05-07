package weblogic.ejb.container.internal;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.util.Map;
import java.util.Properties;
import javax.resource.ResourceException;
import javax.resource.spi.UnavailableException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;
import weblogic.connector.external.SuspendableEndpointFactory;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.container.manager.MessageDrivenManager;
import weblogic.rmi.extensions.server.ServerHelper;

public final class MessageEndpointFactoryImpl implements MessageEndpointFactory, SuspendableEndpointFactory {
   protected static final DebugLogger debugLogger;
   private final BeanInfo beanInfo;
   private MessageDrivenManager beanManager;
   private boolean ready;
   static final long serialVersionUID = 4759518020353275814L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.MessageEndpointFactoryImpl");
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_After_Inbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Before_Inbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Around_Inbound;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   public void setReady(boolean var1) {
      this.ready = var1;
   }

   public MessageEndpointFactoryImpl(BeanInfo var1) {
      this.beanInfo = var1;
      this.beanManager = (MessageDrivenManager)var1.getBeanManager();
      this.ready = false;
   }

   public MessageEndpoint createEndpoint(XAResource var1) throws UnavailableException {
      Object[] var5;
      DynamicJoinPoint var10000;
      DelegatingMonitor var10001;
      if (_WLDF$INST_FLD_Connector_Before_Inbound.isEnabledAndNotDyeFiltered()) {
         var5 = null;
         if (_WLDF$INST_FLD_Connector_Before_Inbound.isArgumentsCaptureNeeded()) {
            var5 = InstrumentationSupport.toSensitive(2);
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var5, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Inbound;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      boolean var10;
      boolean var18 = var10 = _WLDF$INST_FLD_Connector_Around_Inbound.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var11 = null;
      DiagnosticActionState[] var12 = null;
      Object var9 = null;
      if (var18) {
         var5 = null;
         if (_WLDF$INST_FLD_Connector_Around_Inbound.isArgumentsCaptureNeeded()) {
            var5 = InstrumentationSupport.toSensitive(2);
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var5, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Inbound;
         DiagnosticAction[] var10002 = var11 = var10001.getActions();
         InstrumentationSupport.preProcess(var10000, var10001, var10002, var12 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var15 = false;

      Object var6;
      Object var19;
      try {
         var15 = true;
         if (!this.ready) {
            throw new UnavailableException("MessageEndpointFactory Unavailable.  It is either not ready or suspended by the user.");
         }

         Object var2 = this.beanManager.allocateMDO(var1);
         if (var2 instanceof Remote) {
            try {
               var2 = (MessageEndpoint)ServerHelper.replaceAndResolveRemoteObject((Remote)var2);
            } catch (Exception var16) {
               debug("Error calling ServerHelper.replaceAndResolveRemoteObject(): " + var16);
            }
         }

         var19 = var2;
         var15 = false;
      } finally {
         if (var15) {
            var6 = null;
            if (var10) {
               InstrumentationSupport.postProcess(InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var6), _WLDF$INST_FLD_Connector_Around_Inbound, var11, var12);
            }

            if (_WLDF$INST_FLD_Connector_After_Inbound.isEnabledAndNotDyeFiltered()) {
               DynamicJoinPoint var21 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var6);
               DelegatingMonitor var10003 = _WLDF$INST_FLD_Connector_After_Inbound;
               InstrumentationSupport.process(var21, var10003, var10003.getActions());
            }

         }
      }

      var6 = var19;
      if (var10) {
         InstrumentationSupport.postProcess(InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var6), _WLDF$INST_FLD_Connector_Around_Inbound, var11, var12);
      }

      if (_WLDF$INST_FLD_Connector_After_Inbound.isEnabledAndNotDyeFiltered()) {
         DynamicJoinPoint var20 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var6);
         DelegatingMonitor var22 = _WLDF$INST_FLD_Connector_After_Inbound;
         InstrumentationSupport.process(var20, var22, var22.getActions());
      }

      return (MessageEndpoint)var19;
   }

   public boolean isDeliveryTransacted(Method var1) throws NoSuchMethodException {
      return ((MessageDrivenBeanInfo)this.beanInfo).isDeliveryTransacted(var1);
   }

   public void suspend(Properties var1) throws ResourceException {
      this.setReady(false);
   }

   public void resume(Properties var1) throws ResourceException {
      this.setReady(true);
   }

   public boolean isSuspended() {
      return this.ready;
   }

   public void disconnect() throws ResourceException {
      this.beanManager.onRAUndeploy();
   }

   private static void debug(String var0) {
      debugLogger.debug("[MessageEndpointFactoryImpl] " + var0);
   }

   static {
      _WLDF$INST_FLD_Connector_After_Inbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_After_Inbound");
      _WLDF$INST_FLD_Connector_Before_Inbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Before_Inbound");
      _WLDF$INST_FLD_Connector_Around_Inbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Around_Inbound");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "MessageEndpointFactoryImpl.java", "weblogic.ejb.container.internal.MessageEndpointFactoryImpl", "createEndpoint", "(Ljavax/transaction/xa/XAResource;)Ljavax/resource/spi/endpoint/MessageEndpoint;", 55, (Map)null, (boolean)0);
      debugLogger = EJBDebugService.mdbConnectionLogger;
   }
}
