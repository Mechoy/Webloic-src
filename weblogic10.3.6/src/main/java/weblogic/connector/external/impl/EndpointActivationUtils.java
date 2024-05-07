package weblogic.connector.external.impl;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterAssociation;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RACollectionManager;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.exception.RAInternalException;
import weblogic.connector.extensions.Suspendable;
import weblogic.connector.external.ActivationSpecFindOrCreateException;
import weblogic.connector.external.ActivationSpecInfo;
import weblogic.connector.external.ElementNotFoundException;
import weblogic.connector.external.EndpointActivationException;
import weblogic.connector.external.InboundInfo;
import weblogic.connector.external.MissingPropertiesException;
import weblogic.connector.external.RAInfo;
import weblogic.connector.external.RequiredConfigPropInfo;
import weblogic.connector.external.ResourceAdapterNotActiveException;
import weblogic.connector.external.ResourceAdapterNotFoundException;
import weblogic.connector.inbound.RAInboundManager;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.management.runtime.MessageDrivenEJBRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.PlatformConstants;

public class EndpointActivationUtils implements weblogic.connector.external.EndpointActivationUtils {
   private static final String CLASS_NAME = "weblogic.connector.external.impl.EndpointActivationUtils";
   private static final EndpointActivationUtils SINGLETON;
   static final long serialVersionUID = -1427661757008357138L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.connector.external.impl.EndpointActivationUtils");
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Endpoint_Deactivate_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_After_Inbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Endpoint_Activate_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Before_Inbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Around_Inbound;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;

   private EndpointActivationUtils() {
   }

   public static weblogic.connector.external.EndpointActivationUtils getAccessor() {
      return SINGLETON;
   }

   public void activateEndpoint(String var1, String var2, String var3, ActivationSpec var4, MessageEndpointFactory var5, MessageDrivenEJBRuntimeMBean var6) throws ResourceAdapterNotFoundException, MissingPropertiesException, EndpointActivationException {
      Object[] var13;
      DynamicJoinPoint var10000;
      DelegatingMonitor var10001;
      if (_WLDF$INST_FLD_Connector_Endpoint_Deactivate_Low.isEnabledAndNotDyeFiltered()) {
         var13 = null;
         if (_WLDF$INST_FLD_Connector_Endpoint_Deactivate_Low.isArgumentsCaptureNeeded()) {
            var13 = new Object[]{this, var1, var2, var3, var4, var5, var6};
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var13, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Endpoint_Deactivate_Low;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Endpoint_Activate_Low.isEnabledAndNotDyeFiltered()) {
         var13 = null;
         if (_WLDF$INST_FLD_Connector_Endpoint_Activate_Low.isArgumentsCaptureNeeded()) {
            var13 = new Object[]{this, var1, var2, var3, var4, var5, var6};
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var13, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Endpoint_Activate_Low;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Before_Inbound.isEnabledAndNotDyeFiltered()) {
         var13 = null;
         if (_WLDF$INST_FLD_Connector_Before_Inbound.isArgumentsCaptureNeeded()) {
            var13 = new Object[]{this, var1, var2, var3, var4, var5, var6};
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var13, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Inbound;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      boolean var18;
      boolean var26 = var18 = _WLDF$INST_FLD_Connector_Around_Inbound.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var19 = null;
      DiagnosticActionState[] var20 = null;
      Object var17 = null;
      if (var26) {
         var13 = null;
         if (_WLDF$INST_FLD_Connector_Around_Inbound.isArgumentsCaptureNeeded()) {
            var13 = new Object[]{this, var1, var2, var3, var4, var5, var6};
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var13, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Inbound;
         DiagnosticAction[] var10002 = var19 = var10001.getActions();
         InstrumentationSupport.preProcess(var10000, var10001, var10002, var20 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         ResourceAdapter var7 = null;
         if (Debug.isRALifecycleEnabled()) {
            Debug.raLifecycle("weblogic.connector.external.impl.EndpointActivationUtils.activateEndpoint called, ejbName = " + var1 + ", jndiName = " + var2 + ", messageListenerType = " + var3 + ", .. )");
         }

         checkStringArg("activateEndPoint()", "ejbName", var1);
         checkStringArg("activateEndPoint()", "jndiName", var2);
         checkStringArg("activateEndpoint()", "messageListenerType", var3);
         checkObjectArg("activateEndpoint()", "activationSpec", var4);
         checkObjectArg("activateEndpoint()", "endpointFactory", var5);
         RAInstanceManager var8 = RACollectionManager.getRAInstanceManager(var2);
         if (var8 == null) {
            String var25 = Debug.getExceptionRANotDeployed(var2);
            throw new ResourceAdapterNotFoundException(var25);
         }

         var7 = var8.getResourceAdapter();
         if (var7 == null) {
            Debug.throwAssertionError("RA bean bound with JNDI name '" + var2 + "' is null");
         }

         this.checkRequiredConfigProperties(var4, var2, var3);
         AuthenticatedSubject var9 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

         String var11;
         try {
            RAInboundManager var10 = var8.getRAInboundManager();
            var11 = var6.getApplicationName() + "_" + var6.getEJBName();
            var10.setupForRecovery(var4, var11);
            var8.getAdapterLayer().endpointActivation(var7, var5, var4, var9);
            var10.addEJB(var3, var1);
            var10.addEndpointFactory(var3, var5, var6);
         } catch (Throwable var23) {
            var11 = var8.getAdapterLayer().toString(var23, var9);
            throw (EndpointActivationException)((EndpointActivationException)(new EndpointActivationException(var11, false)).initCause(var23));
         }
      } finally {
         if (var18) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Connector_Around_Inbound, var19, var20);
         }

         if (_WLDF$INST_FLD_Connector_After_Inbound.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Inbound;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10001, var10001.getActions());
         }

      }

   }

   public void deActivateEndpoint(String var1, String var2, String var3, ActivationSpec var4, MessageEndpointFactory var5, MessageDrivenEJBRuntimeMBean var6) throws EndpointActivationException {
      Object[] var13;
      DynamicJoinPoint var10000;
      DelegatingMonitor var10001;
      if (_WLDF$INST_FLD_Connector_Before_Inbound.isEnabledAndNotDyeFiltered()) {
         var13 = null;
         if (_WLDF$INST_FLD_Connector_Before_Inbound.isArgumentsCaptureNeeded()) {
            var13 = InstrumentationSupport.toSensitive(7);
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var13, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Inbound;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      boolean var18;
      boolean var25 = var18 = _WLDF$INST_FLD_Connector_Around_Inbound.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var19 = null;
      DiagnosticActionState[] var20 = null;
      Object var17 = null;
      if (var25) {
         var13 = null;
         if (_WLDF$INST_FLD_Connector_Around_Inbound.isArgumentsCaptureNeeded()) {
            var13 = InstrumentationSupport.toSensitive(7);
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var13, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Inbound;
         DiagnosticAction[] var10002 = var19 = var10001.getActions();
         InstrumentationSupport.preProcess(var10000, var10001, var10002, var20 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         ResourceAdapter var7 = null;
         if (Debug.isRALifecycleEnabled()) {
            Debug.raLifecycle("weblogic.connector.external.impl.EndpointActivationUtils.deactivateEndpoint called, ejbName = " + var1 + ", jndiName = " + var2 + ", messageListenerType = " + var3 + ", .. )");
         }

         checkStringArg("deActivateEndpoint()", "ejbName", var1);
         checkStringArg("deActivateEndpoint()", "jndiName", var2);
         checkObjectArg("deActivateEndpoint()", "activationSpec", var4);
         checkObjectArg("deActivateEndpoint()", "endpointFactory", var5);
         RAInstanceManager var8 = RACollectionManager.getRAInstanceManager(var2);
         if (var8 != null) {
            var7 = var8.getResourceAdapter();
            if (var7 == null) {
               Debug.throwAssertionError("RA bean bound with JNDI name '" + var2 + "' is null");
            }
         } else {
            Debug.throwAssertionError("Attempt to deactivate RA with JNDI name '" + var2 + "' and RA cannot be found");
         }

         AuthenticatedSubject var9 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

         try {
            var8.getAdapterLayer().endpointDeactivation(var7, var5, var4, var9);
            RAInboundManager var10 = var8.getRAInboundManager();
            if (var10 == null) {
               Debug.throwAssertionError("Attempt to deactivate endpoint of RA with JNDI name = '" + var2 + "' but no InboundManger can be found for that RA");
            }

            var10.removeEJB(var3, var1);
            var10.removeEndpointFactory(var3, var5, var6);
            var10.cleanupForRecovery(var4);
         } catch (Throwable var23) {
            String var11 = var8.getAdapterLayer().toString(var23, var9);
            throw (EndpointActivationException)((EndpointActivationException)(new EndpointActivationException(var11, false)).initCause(var23));
         }
      } finally {
         if (var18) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_Connector_Around_Inbound, var19, var20);
         }

         if (_WLDF$INST_FLD_Connector_After_Inbound.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Inbound;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_1, var10001, var10001.getActions());
         }

      }

   }

   public void suspendInbound(String var1, MessageEndpointFactory var2, Properties var3) throws EndpointActivationException {
      ResourceAdapter var4 = null;
      if (Debug.isRALifecycleEnabled()) {
         Debug.raLifecycle("weblogic.connector.external.impl.EndpointActivationUtils.suspendInbound() called, jndiName = " + var1 + ", endpointFactory = " + var2);
      }

      checkStringArg("deActivateEndpoint()", "jndiName", var1);
      checkObjectArg("deActivateEndpoint()", "endpointFactory", var2);
      RAInstanceManager var5 = RACollectionManager.getRAInstanceManager(var1);
      if (var5 != null) {
         var4 = var5.getResourceAdapter();
         if (var4 == null) {
            Debug.throwAssertionError("RA bean bound with JNDI name '" + var1 + "' is null");
         }
      } else {
         Debug.throwAssertionError("Attempt to suspend inbound of RA with JNDI name '" + var1 + "' and RA cannot be found");
      }

      AuthenticatedSubject var6 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

      try {
         if (var4 instanceof Suspendable && var5.getAdapterLayer().supportsSuspend((Suspendable)var4, 1, var6)) {
            var5.getAdapterLayer().suspendInbound((Suspendable)var4, var2, var3, var6);
         }

         RAInboundManager var7 = var5.getRAInboundManager();
         if (var7 == null) {
            Debug.throwAssertionError("Attempt to suspend inbound for endpoint of RA with JNDI name = '" + var1 + "' but no InboundManger can be found for that RA");
         }

         var7.setEndpointFactorySuspendedState(var2, true);
      } catch (Throwable var9) {
         String var8 = var5.getAdapterLayer().toString(var9, var6);
         throw (EndpointActivationException)((EndpointActivationException)(new EndpointActivationException(var8, false)).initCause(var9));
      }
   }

   public void resumeInbound(String var1, MessageEndpointFactory var2, Properties var3) throws EndpointActivationException {
      ResourceAdapter var4 = null;
      if (Debug.isRALifecycleEnabled()) {
         Debug.raLifecycle("weblogic.connector.external.impl.EndpointActivationUtils.resumeInbound() called, jndiName = " + var1 + ", endpointFactory = " + var2);
      }

      checkStringArg("deActivateEndpoint()", "jndiName", var1);
      checkObjectArg("deActivateEndpoint()", "endpointFactory", var2);
      RAInstanceManager var5 = RACollectionManager.getRAInstanceManager(var1);
      if (var5 != null) {
         var4 = var5.getResourceAdapter();
         if (var4 == null) {
            Debug.throwAssertionError("RA bean bound with JNDI name '" + var1 + "' is null");
         }
      } else {
         Debug.throwAssertionError("Attempt to resume inbound of RA with JNDI name '" + var1 + "' and RA cannot be found");
      }

      AuthenticatedSubject var6 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

      try {
         if (var4 instanceof Suspendable) {
            var5.getAdapterLayer().resumeInbound((Suspendable)var4, var2, var3, var6);
         }

         RAInboundManager var7 = var5.getRAInboundManager();
         if (var7 == null) {
            Debug.throwAssertionError("Attempt to resume inbound for endpoint of RA with JNDI name = '" + var1 + "' but no InboundManger can be found for that RA");
         }

         var7.setEndpointFactorySuspendedState(var2, false);
      } catch (Throwable var9) {
         String var8 = var5.getAdapterLayer().toString(var9, var6);
         throw (EndpointActivationException)((EndpointActivationException)(new EndpointActivationException(var8, false)).initCause(var9));
      }
   }

   private static Object initializeActivationSpec(RAInstanceManager var0, String var1) throws ActivationSpecFindOrCreateException {
      Debug.println("weblogic.connector.external.impl.EndpointActivationUtils", ".initializeActivationSpec( raIM = " + var0 + ", activationSpecClass = " + var1);
      Object var2 = null;
      AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

      try {
         var2 = instantiateActivationSpec(var0, var1, var0.getClassloader());
         var0.getAdapterLayer().setResourceAdapter((ResourceAdapterAssociation)var2, var0.getResourceAdapter(), var3);
      } catch (ActivationSpecFindOrCreateException var6) {
         throw var6;
      } catch (Throwable var7) {
         String var5 = var0.getAdapterLayer().toString(var7, var3);
         throw (ActivationSpecFindOrCreateException)((ActivationSpecFindOrCreateException)(new ActivationSpecFindOrCreateException(var5)).initCause(var7));
      }

      if (var2 == null) {
         String var4 = Debug.getExceptionInitializeActivationSpecFailed(var1);
         throw new ActivationSpecFindOrCreateException(var4);
      } else {
         return var2;
      }
   }

   private static Object instantiateActivationSpec(RAInstanceManager var0, String var1, ClassLoader var2) throws ActivationSpecFindOrCreateException {
      AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

      String var5;
      try {
         Class var4 = Class.forName(var1, true, var2);
         return var0.getAdapterLayer().newInstance(var4, var3);
      } catch (RAInternalException var6) {
         var5 = Debug.getExceptionInstantiateClassFailed(var1, var6.toString());
         throw new ActivationSpecFindOrCreateException(var5, var6);
      } catch (ClassNotFoundException var7) {
         var5 = Debug.getExceptionInstantiateClassFailed(var1, var7.toString());
         throw new ActivationSpecFindOrCreateException(var5, var7);
      } catch (InstantiationException var8) {
         var5 = Debug.getExceptionInstantiateClassFailed(var1, var8.toString());
         throw new ActivationSpecFindOrCreateException(var5, var8);
      } catch (IllegalAccessException var9) {
         var5 = Debug.getExceptionInstantiateClassFailed(var1, var9.toString());
         throw new ActivationSpecFindOrCreateException(var5, var9);
      }
   }

   private static void checkStringArg(String var0, String var1, String var2) {
      if (var2 == null || var2.trim().equals("")) {
         String var3 = var0 + " passed " + var1 + " of '" + var2 + "'";
         Debug.throwAssertionError(var3);
      }

   }

   private static void checkObjectArg(String var0, String var1, Object var2) {
      if (var2 == null) {
         String var3 = var0 + " passed " + var1 + " of 'null'";
         Debug.throwAssertionError(var3);
      }

   }

   public Object getActivationSpec(String var1, String var2) throws ActivationSpecFindOrCreateException, ResourceAdapterNotActiveException, ResourceAdapterNotFoundException {
      ActivationSpecInfo var3 = null;
      Object var4 = null;
      Debug.println("weblogic.connector.external.impl.EndpointActivationUtils", ".getActivationSpec( " + var1 + ", " + var2 + " )");
      checkStringArg("getActivationSpec()", "jndiName", var1);
      checkStringArg("getAcitvationSpec()", "messageListenerType", var2);
      String var5 = null;
      RAInstanceManager var6 = RACollectionManager.getRAInstanceManager(var1);
      if (var6 == null) {
         var5 = Debug.getExceptionRANotDeployed(var1);
         throw new ResourceAdapterNotFoundException(var5);
      } else if (!var6.isActivated()) {
         var5 = Debug.getExceptionRANotActive(var1);
         throw new ResourceAdapterNotActiveException(var5);
      } else {
         RAInfo var7 = var6.getRAInfo();
         var3 = getActivationSpecInfo(var7, var2, var1);
         var4 = initializeActivationSpec(var6, var3.getActivationSpecClass());
         return var4;
      }
   }

   public List getRequiredConfigProperties(String var1, String var2) throws ResourceAdapterNotFoundException, ActivationSpecFindOrCreateException {
      checkStringArg("getRequiredConfigProperties()", "jndiName", var1);
      checkStringArg("getRequiredConfigProperties()", "messageListenerType", var2);
      RAInstanceManager var3 = RACollectionManager.getRAInstanceManager(var1);
      if (var3 == null) {
         String var6 = Debug.getExceptionRANotFound(var1);
         throw new ResourceAdapterNotFoundException(var6);
      } else {
         RAInfo var4 = var3.getRAInfo();
         ActivationSpecInfo var5 = getActivationSpecInfo(var4, var2, var1);
         return var5.getRequiredProps();
      }
   }

   private static ActivationSpecInfo getActivationSpecInfo(RAInfo var0, String var1, String var2) throws ActivationSpecFindOrCreateException {
      Iterator var3 = null;
      ActivationSpecInfo var4 = null;
      boolean var5 = false;

      try {
         var3 = var0.getInboundInfos().iterator();
      } catch (ElementNotFoundException var8) {
         throw new ActivationSpecFindOrCreateException(var8.toString());
      }

      while(var3.hasNext() && !var5) {
         InboundInfo var6 = (InboundInfo)var3.next();
         if (Debug.getVerbose()) {
            Debug.println("weblogic.connector.external.impl.EndpointActivationUtils", ".getActivationSpecInfo( " + var1 + " ) found " + var6.getMsgType());
         }

         if (var6.getMsgType().equals(var1)) {
            var4 = var6.getActivationSpec();
            var5 = true;
         }
      }

      if (var4 == null) {
         String var7 = Debug.getExceptionNoMessageListener(var2, var1);
         throw new ActivationSpecFindOrCreateException(var7);
      } else {
         return var4;
      }
   }

   private void checkRequiredConfigProperties(ActivationSpec var1, String var2, String var3) throws MissingPropertiesException, ResourceAdapterNotFoundException, ActivationSpecFindOrCreateException {
      Vector var4 = new Vector();
      List var5 = this.getRequiredConfigProperties(var2, var3);
      Iterator var6 = var5.iterator();

      String var9;
      while(var6.hasNext()) {
         boolean var7 = false;
         RequiredConfigPropInfo var8 = (RequiredConfigPropInfo)var6.next();
         var9 = var8.getName();
         Method[] var10 = var1.getClass().getMethods();

         for(int var11 = 0; var11 < var10.length; ++var11) {
            if (var10[var11].getName().equalsIgnoreCase("get" + var9)) {
               var7 = true;
               break;
            }
         }

         if (!var7) {
            if (Debug.getVerbose()) {
               Debug.println("weblogic.connector.external.impl.EndpointActivationUtils", ".checkRequiredConfigProperties():  missing property found, '" + var9 + "'");
            }

            var4.add(var9);
         }
      }

      Debug.println("weblogic.connector.external.impl.EndpointActivationUtils", ".checkRequiredConfigProperties found " + var4.size() + " missing properties in the activation spec");
      if (var4.size() > 0) {
         Object[] var12 = var4.toArray();
         String var13 = "";

         for(int var14 = 0; var14 < var12.length; ++var14) {
            if (var14 > 0) {
               var13 = var13 + PlatformConstants.EOL + "  ";
            }

            var13 = var13 + (String)var12[var14];
         }

         Debug.println("weblogic.connector.external.impl.EndpointActivationUtils", ".activateEndpoint call missing required properties in the passed activation spec, " + var13);
         var9 = Debug.getExceptionMissingRequiredProperty(var13);
         throw new MissingPropertiesException(var9);
      }
   }

   static {
      _WLDF$INST_FLD_Connector_Endpoint_Deactivate_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Endpoint_Deactivate_Low");
      _WLDF$INST_FLD_Connector_After_Inbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_After_Inbound");
      _WLDF$INST_FLD_Connector_Endpoint_Activate_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Endpoint_Activate_Low");
      _WLDF$INST_FLD_Connector_Before_Inbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Before_Inbound");
      _WLDF$INST_FLD_Connector_Around_Inbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Around_Inbound");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EndpointActivationUtils.java", "weblogic.connector.external.impl.EndpointActivationUtils", "activateEndpoint", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljavax/resource/spi/ActivationSpec;Ljavax/resource/spi/endpoint/MessageEndpointFactory;Lweblogic/management/runtime/MessageDrivenEJBRuntimeMBean;)V", 87, InstrumentationSupport.makeMap(new String[]{"Connector_Around_Inbound", "Connector_Endpoint_Deactivate_Low", "Connector_Before_Inbound", "Connector_After_Inbound", "Connector_Endpoint_Activate_Low"}, new PointcutHandlingInfo[]{null, InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("ejb", (String)null, false, true), InstrumentationSupport.createValueHandlingInfo("jndi", (String)null, false, true), InstrumentationSupport.createValueHandlingInfo("listener", (String)null, false, true), null, null, null}), null, null, InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("ejb", (String)null, false, true), InstrumentationSupport.createValueHandlingInfo("jndi", (String)null, false, true), InstrumentationSupport.createValueHandlingInfo("listener", (String)null, false, true), null, null, null})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EndpointActivationUtils.java", "weblogic.connector.external.impl.EndpointActivationUtils", "deActivateEndpoint", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljavax/resource/spi/ActivationSpec;Ljavax/resource/spi/endpoint/MessageEndpointFactory;Lweblogic/management/runtime/MessageDrivenEJBRuntimeMBean;)V", 169, (Map)null, (boolean)0);
      SINGLETON = new EndpointActivationUtils();
   }
}
