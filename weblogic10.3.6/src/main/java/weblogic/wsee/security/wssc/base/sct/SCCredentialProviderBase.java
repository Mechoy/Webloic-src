package weblogic.wsee.security.wssc.base.sct;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.rpc.handler.MessageContext;
import weblogic.j2ee.descriptor.wl.PortInfoBean;
import weblogic.security.service.ContextHandler;
import weblogic.store.PersistentStoreManager;
import weblogic.wsee.jaxws.framework.ConfigUtil;
import weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext;
import weblogic.wsee.jaxws.persistence.PersistenceConfig;
import weblogic.wsee.jaxws.spi.WLSServiceDelegate;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.runtime.WebServicesRuntime;
import weblogic.wsee.security.wssc.faults.FaultVersionHelper;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wst.faults.WSTFaultUtil;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.security.wst.helpers.TrustTokenHelper;
import weblogic.wsee.ws.WsPort;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.provider.CredentialProvider;

public abstract class SCCredentialProviderBase implements CredentialProvider {
   private static final Logger LOGGER = Logger.getLogger(SCCredentialProviderBase.class.getName());

   protected abstract String[] getSCT_VALUE_TYPES();

   public String[] getValueTypes() {
      return this.getSCT_VALUE_TYPES();
   }

   protected static SecurityTokenContextHandler getSecurityCtxHandler(ContextHandler var0) {
      return !(var0 instanceof SecurityTokenContextHandler) ? null : (SecurityTokenContextHandler)var0;
   }

   protected static MessageContext getMessageContext(SecurityTokenContextHandler var0) {
      WSSecurityContext var1 = (WSSecurityContext)var0.getValue("com.bea.contextelement.xml.SecurityInfo");
      if (var1 == null) {
         return null;
      } else {
         MessageContext var2 = var1.getMessageContext();
         return var2;
      }
   }

   static SCCredential checkExpiration(MessageContext var0, SCCredential var1, String var2) {
      if ("true".equals(var0.getProperty("weblogic.wsee.security.wssc.needCheckSCTExpiration")) && var1 != null && TrustTokenHelper.isExpired(var0, var1.getCreated(), var1.getExpires())) {
         WSTFaultUtil.raiseFault(FaultVersionHelper.newRenewNeededException(var2, "SCToken expired: " + var1.getIdentifier()));
      }

      return var1;
   }

   static SCCredential getSCFromContext(MessageContext var0) {
      if (var0 == null) {
         return null;
      } else {
         Map var1 = (Map)var0.getProperty("weblogic.wsee.invoke_properties");
         if (var1 != null) {
            SCCredential var2 = (SCCredential)var1.get("weblogic.wsee.wssc.sct");
            if (var2 != null) {
               return var2;
            }
         }

         return (SCCredential)var0.getProperty("weblogic.wsee.wssc.sct");
      }
   }

   static void setSCToContext(MessageContext var0, SCCredential var1) {
      if (var0 != null) {
         var0.setProperty("weblogic.wsee.wssc.sct", var1);
         Map var2 = (Map)var0.getProperty("weblogic.wsee.invoke_properties");
         if (var2 != null) {
            var2.put("weblogic.wsee.wssc.sct", var1);
         }
      }

   }

   static void removeSCFromContext(MessageContext var0) {
      var0.removeProperty("weblogic.wsee.wssc.sct");
      Map var1 = (Map)var0.getProperty("weblogic.wsee.invoke_properties");
      if (var1 != null) {
         var1.remove("weblogic.wsee.wssc.sct");
      }

   }

   static String getAppliesToEndpoint(MessageContext var0) {
      String var1 = (String)var0.getProperty("weblogic.wsee.connection.end_point_address");
      if (var1 == null) {
         var1 = (String)var0.getProperty("javax.xml.rpc.service.endpoint.address");
      }

      return var1;
   }

   protected String getPhysicalStoreNameFromWSTContext(WSTContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1.getMessageContext());
      return getPhysicalStoreNameFromMessageContext(var2);
   }

   protected static String getPhysicalStoreNameFromMessageContext(MessageContext var0) {
      WlMessageContext var1 = (WlMessageContext)var0;
      String var2 = null;
      boolean var3 = isJaxwsRuntime(var1);
      if (var3) {
         PersistenceConfig.Common var4 = getPersistenceConfigFromMessageContext(var1);
         if (var4 != null) {
            var2 = getPhysicalStoreName(var4);
         }
      }

      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Calculated physical store for current message context as: " + var2);
      }

      return var2;
   }

   protected static PersistenceConfig.Common getPersistenceConfigFromMessageContext(WlMessageContext var0) {
      Object var1 = null;
      PortInfoBean var3;
      if (var0 instanceof SOAPMessageContext) {
         SOAPMessageContext var2 = (SOAPMessageContext)var0;
         if (var2.isClient()) {
            var3 = ConfigUtil.getPortInfoBeanForPort((WLSServiceDelegate)var2.getWSService(), var0.getDispatcher().getPortName().getLocalPart());
            var1 = PersistenceConfig.getClientConfig(var3);
         } else {
            WsPort var6 = null;
            if (var0.getDispatcher() != null) {
               var6 = var0.getDispatcher().getWsPort();
            }

            var1 = PersistenceConfig.getServiceConfig(var6);
         }
      } else if (var0.getProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.SERVICE") instanceof WLSServiceDelegate) {
         WLSServiceDelegate var4 = (WLSServiceDelegate)var0.getProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.SERVICE");
         var3 = ConfigUtil.getPortInfoBeanForPort(var4, var0.getDispatcher().getPortName().getLocalPart());
         var1 = PersistenceConfig.getClientConfig(var3);
      } else if (var0.getDispatcher() != null && var0.getDispatcher().getWsPort() != null) {
         WsPort var5 = var0.getDispatcher().getWsPort();
         var1 = PersistenceConfig.getServiceConfig(var5);
      } else {
         var1 = PersistenceConfig.getServiceConfig((WsPort)null);
      }

      return (PersistenceConfig.Common)var1;
   }

   public static boolean isJaxwsRuntime(MessageContext var0) {
      return Boolean.parseBoolean((String)var0.getProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.JAX_WS_RUNTIME")) || var0.getProperty("weblogic.wsee.jaxws.async.PersistentContext") != null;
   }

   protected static String getPhysicalStoreName(PersistenceConfig.Common var0) {
      String var1 = var0.getLogicalStoreName();
      return getPhysicalStoreFromLogicalStore(var1);
   }

   protected static String getPhysicalStoreFromLogicalStore(String var0) {
      List var2 = WebServicesRuntime.getInstance().getLocalPhysicalStoresForLogicalStore(var0);
      if (var2.size() < 1) {
         String var3 = PersistentStoreManager.getManager().getDefaultStore().getName();
         return var3;
      } else {
         String var1 = (String)var2.get(0);
         return var1;
      }
   }
}
