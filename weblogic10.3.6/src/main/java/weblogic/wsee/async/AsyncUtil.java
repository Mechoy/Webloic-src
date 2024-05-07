package weblogic.wsee.async;

import com.bea.staxb.runtime.internal.ClassLoadingUtils;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Locale;
import javax.naming.InitialContext;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.ejb.spi.DynamicEJBModule;
import weblogic.ejb.spi.DynamicEJBModuleFactory;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.descriptor.RunAsBean;
import weblogic.j2ee.descriptor.SecurityIdentityBean;
import weblogic.j2ee.descriptor.wl.RunAsRoleAssignmentBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.webservice.WebServiceLogger;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.buffer2.api.common.BufferingFeature;
import weblogic.wsee.deploy.VersioningHelper;
import weblogic.wsee.jws.container.Container;
import weblogic.wsee.jws.container.ContainerFactory;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.reliability.WsrmSequenceContext;
import weblogic.wsee.security.WssClientHandler;
import weblogic.wsee.security.WssServerHandler;
import weblogic.wsee.security.WssServerPolicyHandler;
import weblogic.wsee.security.wssp.handlers.PostWssServerPolicyHandler;
import weblogic.wsee.security.wssp.handlers.PreWssClientPolicyHandler;
import weblogic.wsee.server.ServerUtil;
import weblogic.wsee.util.EjbDescriptorCreator;
import weblogic.wsee.util.Guid;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsRegistry;
import weblogic.wsee.ws.dispatch.Dispatcher;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;

/** @deprecated */
public class AsyncUtil {
   private static final boolean verbose = Verbose.isVerbose(AsyncUtil.class);
   public static final String SOAP12_ENVELOPE_NS = "http://www.w3.org/2003/05/soap-envelope";
   public static final String ASYNC_RESPONSE_SERVICE_CONTEXT_PATH = "weblogic.async.contextPath";
   public static final String ASYNC_RESPONSE_SERVICE_SERVICE_URI = "weblogic.async.serviceUri";
   private static AuthenticatedSubject _kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static String getAsyncUri(boolean var0, String var1) {
      String var2 = System.getProperty("weblogic.async.contextPath");
      String var3 = System.getProperty("weblogic.async.serviceUri");
      if (var2 == null) {
         var2 = "_async";
      }

      if (var3 == null) {
         var3 = "AsyncResponseService";
      }

      if (var0) {
         var3 = var3 + "Soap12";
      }

      if (!"http".equalsIgnoreCase(var1)) {
         if ("https".equalsIgnoreCase(var1)) {
            var3 = var3 + "Https";
         } else {
            if (!"jms".equalsIgnoreCase(var1)) {
               throw new JAXRPCException("Unsupported transport: " + var1);
            }

            var3 = var3 + "Jms";
         }
      }

      return "/" + var2 + "/" + var3;
   }

   public static boolean isSoap12(MessageContext var0) {
      if (!(var0 instanceof WlMessageContext)) {
         return false;
      } else {
         WlMessageContext var1 = (WlMessageContext)var0;
         Dispatcher var2 = var1.getDispatcher();
         if (var2 != null) {
            return var2.isSOAP12();
         } else if (!(var0 instanceof SOAPMessageContext)) {
            return false;
         } else {
            SOAPMessageContext var3 = (SOAPMessageContext)var0;

            try {
               return "http://www.w3.org/2003/05/soap-envelope".equals(var3.getMessage().getSOAPPart().getEnvelope().getNamespaceURI());
            } catch (SOAPException var5) {
               throw new JAXRPCException(var5);
            }
         }
      }
   }

   public static String getAsyncResponseMethodName(MessageContext var0) {
      String var1 = (String)var0.getProperty("weblogic.wsee.method.name");
      if (var1 == null) {
         throw new JAXRPCException("No method name property found");
      } else {
         String var2 = null;
         HashMap var3 = (HashMap)var0.getProperty("weblogic.wsee.async.response.map");
         if (var3 != null) {
            var2 = (String)var3.get(var1);
         }

         if (var2 == null) {
            if (var1.length() > 1) {
               var2 = "on" + var1.substring(0, 1).toUpperCase(Locale.ENGLISH) + var1.substring(1, var1.length()) + "AsyncResponse";
            } else {
               var2 = "on" + var1.substring(0, 1).toUpperCase(Locale.ENGLISH) + "AsyncResponse";
            }
         }

         return var2;
      }
   }

   public static String getAsyncFailureMethodName(MessageContext var0) {
      String var1 = (String)var0.getProperty("weblogic.wsee.method.name");
      if (var1 == null) {
         throw new JAXRPCException("No method name property found");
      } else {
         String var2 = null;
         HashMap var3 = (HashMap)var0.getProperty("weblogic.wsee.async.failure.map");
         if (var3 != null) {
            var2 = (String)var3.get(var1);
         }

         if (var2 == null) {
            if (var1.length() > 1) {
               var2 = "on" + var1.substring(0, 1).toUpperCase(Locale.ENGLISH) + var1.substring(1, var1.length()) + "AsyncFailure";
            } else {
               var2 = "on" + var1.substring(0, 1).toUpperCase(Locale.ENGLISH) + "AsyncFailure";
            }
         }

         return var2;
      }
   }

   public static Class[] getAsyncResponseMethodParams(MessageContext var0) {
      String var1 = (String)var0.getProperty("weblogic.wsee.return.type");
      if (var1 == null) {
         throw new JAXRPCException("No return type found");
      } else {
         Class[] var2;
         if (var1.equals("void")) {
            var2 = new Class[]{AsyncPostCallContext.class};
         } else {
            try {
               var2 = new Class[]{AsyncPostCallContext.class, ClassLoadingUtils.loadClass(var1, (ClassLoader)null)};
            } catch (ClassNotFoundException var4) {
               throw new JAXRPCException(var4);
            }
         }

         return var2;
      }
   }

   public static Class[] getAsyncFailureMethodParams() {
      return new Class[]{AsyncPostCallContext.class, Throwable.class};
   }

   public static Container getContainer(MessageContext var0) {
      return ContainerFactory.getContainer(var0);
   }

   public static String getAsyncSelector(String var0) {
      String var1 = new String(var0);
      if (var0.indexOf("/") == 0) {
         var1 = var0.substring(1);
      }

      if (var0.endsWith("/")) {
         var1 = var1.substring(0, var1.length() - 1);
      }

      StringBuffer var2 = new StringBuffer();
      var2.append("(");
      var2.append("ASYNC_URI").append("= '").append(var1).append("'");
      var2.append(" OR ");
      var2.append("ASYNC_URI").append(" = '/").append(var1).append("'");
      var2.append(" OR ");
      var2.append("ASYNC_URI").append(" = '/").append(var1).append("/'");
      var2.append(" OR ");
      var2.append("ASYNC_URI").append(" = '").append(var1).append("/'");
      var2.append(")");
      if (verbose) {
         Verbose.say("Calculated selector for dynamic MDB: " + var2.toString());
      }

      return var2.toString();
   }

   public static void setupDynamicMDB(WsDeploymentContext var0, String var1, String var2, String var3, String var4, int var5) throws WsDeploymentException {
      setupDynamicMDB(var0, var1, var2, var3, var4, var5, false);
   }

   public static void setupDynamicMDB(WsDeploymentContext var0, String var1, String var2, String var3, String var4, int var5, boolean var6) throws WsDeploymentException {
      setupDynamicMDB(var0, var1, var2, var3, var4, var5, var6, "weblogic.wsee.mdb.DispatchPolicy");
   }

   public static void setupDynamicMDB(WsDeploymentContext var0, String var1, String var2, String var3, String var4, int var5, boolean var6, String var7) throws WsDeploymentException {
      try {
         InitialContext var8 = new InitialContext();
         var8.lookup(var2);
      } catch (Exception var13) {
         String var9 = var0.getServiceName();
         if (var0.getServiceURIs() != null && var0.getServiceURIs().length > 0) {
            var9 = calculateServiceTargetURI(var0.getContextPath(), var0.getServiceURIs()[0]);
         }

         WebServiceLogger.logAsyncOperationsNotAvailable(var9, var2);
         return;
      }

      DynamicEJBModule var14 = DynamicEJBModuleFactory.createDynamicEJBModule(Guid.generateGuidStandardChar());
      EjbDescriptorBean var15;
      if (var5 > 0) {
         var15 = EjbDescriptorCreator.createEjbDescriptorBean(Guid.generateGuidStandardChar(), var4, "javax.jms.Queue", var2, var1, var5);
      } else {
         var15 = EjbDescriptorCreator.createEjbDescriptorBean(Guid.generateGuidStandardChar(), var4, "javax.jms.Queue", var2, var1);
      }

      EjbDescriptorCreator.setDispatchPolicy(var15, var7);
      if (var3 != null) {
         SecurityIdentityBean var10 = var15.getEjbJarBean().getEnterpriseBeans().getMessageDrivens()[0].createSecurityIdentity();
         RunAsBean var11 = var10.createRunAs();
         var11.setRoleName("ReliableDeliveryRole");
         RunAsRoleAssignmentBean var12 = var15.getWeblogicEjbJarBean().createRunAsRoleAssignment();
         var12.setRoleName("ReliableDeliveryRole");
         var12.setRunAsPrincipalName(var3);
      }

      var14.setEjbDescriptorBean(var15);
      if (!var14.deployDynamicEJB()) {
         throw new WsDeploymentException("Cannot deploy dynamic MDB");
      } else if (!var14.startDynamicEJB()) {
         var14.undeployDynamicEJB();
         throw new WsDeploymentException("Cannot start dynamic MDB");
      } else {
         if (var6) {
            BufferingFeature.registerQueue(var2, var14);
         } else {
            var0.addDynamicEjb(var14);
         }

      }
   }

   private static WsspVersion getWsspVersion(WsrmSequenceContext var0) {
      if (var0.getWsrmSecurityContext().isSecureWithWssp10Wssc()) {
         return AsyncUtil.WsspVersion.WSSP10;
      } else if (!var0.getWsrmSecurityContext().isSecureWithWssp12Wssc() && !var0.getWsrmSecurityContext().isSecureWithWssp12Wssc13()) {
         throw new JAXRPCException("Not a secure RM sequence");
      } else {
         return AsyncUtil.WsspVersion.WSSP12;
      }
   }

   public static GenericHandler getWssServerHandler(WsrmSequenceContext var0) {
      assert var0 != null;

      WsspVersion var1 = getWsspVersion(var0);
      return (GenericHandler)(var1 == AsyncUtil.WsspVersion.WSSP10 ? new WssServerHandler() : new weblogic.wsee.security.wssp.handlers.WssServerHandler());
   }

   public static GenericHandler getWssServerPolicyHandler(WsrmSequenceContext var0) {
      assert var0 != null;

      WsspVersion var1 = getWsspVersion(var0);
      return (GenericHandler)(var1 == AsyncUtil.WsspVersion.WSSP10 ? new WssServerPolicyHandler() : new PostWssServerPolicyHandler());
   }

   public static GenericHandler getWssClientHandler(WsrmSequenceContext var0) {
      assert var0 != null;

      WsspVersion var1 = getWsspVersion(var0);
      return (GenericHandler)(var1 == AsyncUtil.WsspVersion.WSSP10 ? new WssClientHandler() : new weblogic.wsee.security.wssp.handlers.WssClientHandler());
   }

   public static GenericHandler getWssClientPolicyHandler(WsrmSequenceContext var0) {
      assert var0 != null;

      WsspVersion var1 = getWsspVersion(var0);
      return var1 == AsyncUtil.WsspVersion.WSSP10 ? null : new PreWssClientPolicyHandler();
   }

   public static void setApplicationVersionIdIntoContexts(MessageContext var0, AsyncPostCallContextImpl var1) {
      String var2 = ApplicationVersionUtils.getCurrentVersionId();
      if (var2 != null) {
         String var3 = ApplicationVersionUtils.getApplicationName(ApplicationVersionUtils.getCurrentApplicationId());
         var1.setProperty("weblogic.wsee.async.appname", var3);
         var1.setProperty("weblogic.wsee.async.appversion", var2);
         var0.setProperty("weblogic.wsee.async.appname", var3);
         var0.setProperty("weblogic.wsee.async.appversion", var2);
         VersioningHelper.updateCount(var3, var2, 1);
      }

   }

   public static SavedServiceInfo getSavedServiceInfo(AsyncInvokeState var0, boolean var1) {
      SavedServiceInfo var2 = new SavedServiceInfo();
      var2.apc = var0.getAsyncPostCallContext();
      var2.version = (String)var0.getAsyncPostCallContext().getProperty("weblogic.wsee.async.appversion");
      if (var1) {
         Verbose.log((Object)("Version from ais = " + var2.version));
      }

      var2.uri = (String)var0.getMessageContext().getProperty("weblogic.wsee.enclosing.jws.serviceuri");
      if (var2.uri == null) {
         throw new JAXRPCException("No enclosing JWS service URI provided, cannot complete async operation response phase.");
      } else {
         if (var2.version != null) {
            if (var1) {
               Verbose.log((Object)("Async Response bean - adding version " + var2.version + " to URI " + var2.uri));
            }

            var2.uri = var2.uri + "#" + var2.version;
         } else if (var1) {
            Verbose.log((Object)"Async response bean - no version ");
         }

         var2.wsPort = getPort(var2.uri);
         if (var2.wsPort == null) {
            throw new JAXRPCException("No port found for " + var2.uri);
         } else {
            return var2;
         }
      }
   }

   private static WsPort getPort(String var0) {
      String var1 = WsRegistry.getURL(var0);
      String var2 = WsRegistry.getVersion(var0);
      return WsRegistry.instance().lookup(var1, var2);
   }

   public static EndpointReference getDefaultAsyncResponseServiceEPR(String var0, boolean var1) {
      String var2 = ServerUtil.getServerURL(var0) + getAsyncUri(var1, var0);
      if ("jms".equalsIgnoreCase(var0)) {
         var2 = getJMSUrl(var2, ServerUtil.getMessagingQueueInfo().getQueueName());
      }

      EndpointReference var3 = new EndpointReference(var2);
      return var3;
   }

   private static String getJMSUrl(String var0, String var1) {
      return var0 + "?URI=" + var1 + "&FACTORY=" + ServerUtil.getJmsConnectionFactory();
   }

   public static String getCurrentPrincipal() {
      return SecurityServiceManager.getCurrentSubject(_kernelId).toString();
   }

   public static String calculateServiceTargetURI(String var0, String var1) {
      String var2;
      if (var1 != null && var1.length() != 0 && !var1.equals("/")) {
         var2 = var0 + var1;
      } else if (var0.endsWith("/")) {
         var2 = var0.substring(0, var0.length() - 1);
      } else {
         var2 = var0;
      }

      if (verbose) {
         Verbose.say("&& Calculated service target URI '" + var2 + "' from contextPath '" + var0 + "' and service URI '" + var1 + "'");
      }

      return var2;
   }

   /** @deprecated */
   public static class SavedServiceInfo {
      public String version;
      public String uri;
      public AsyncPostCallContextImpl apc;
      public WsPort wsPort;
   }

   public static enum WsspVersion {
      WSSP10,
      WSSP12;
   }
}
