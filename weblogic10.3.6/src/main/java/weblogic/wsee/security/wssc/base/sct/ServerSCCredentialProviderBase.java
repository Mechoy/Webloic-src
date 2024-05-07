package weblogic.wsee.security.wssc.base.sct;

import java.security.Key;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.rpc.handler.MessageContext;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.connection.transport.servlet.HttpTransportUtils;
import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.jaxws.persistence.PersistenceConfig;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.security.wssc.base.faults.WSCFaultException;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wssc.sct.SCTStore;
import weblogic.wsee.security.wssc.sct.SCTokenReference;
import weblogic.wsee.security.wssc.utils.WSSCCompatibilityUtil;
import weblogic.wsee.security.wst.faults.BadRequestException;
import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.wsee.security.wst.faults.WSTFaultUtil;
import weblogic.wsee.security.wst.framework.TrustToken;
import weblogic.wsee.security.wst.framework.TrustTokenProvider;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.security.wst.helpers.TrustTokenHelper;
import weblogic.wsee.server.WsLifeCycleEvent;
import weblogic.wsee.server.WsLifeCycleListenerRegistry;
import weblogic.wsee.util.Guid;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public abstract class ServerSCCredentialProviderBase extends SCCredentialProviderBase implements TrustTokenProvider {
   protected static final boolean verbose = Verbose.isVerbose(ClientSCCredentialProviderBase.class);

   protected ServerSCCredentialProviderBase() {
   }

   protected abstract SCTokenBase newSCToken(SCCredential var1);

   protected abstract WSCFaultException newUnableToRenewException(String var1);

   public Object getCredential(String var1, String var2, ContextHandler var3, Purpose var4, String var5) {
      if (var4 != null && !var4.equals(Purpose.ENCRYPT_RESPONSE)) {
         SecurityTokenContextHandler var6 = getSecurityCtxHandler(var3);
         if (var6 == null) {
            return null;
         } else {
            MessageContext var7 = getMessageContext(var6);
            if (var7 == null) {
               return null;
            } else {
               String var8 = getAppliesToEndpoint(var7);
               if (var8 == null) {
                  return null;
               } else {
                  WSTContext var9 = WSTContext.getWSTContext(var7);
                  var9.setAppliesTo(var8);
                  SCCredential var10 = getSCFromContext(var7);
                  if (var10 != null && var10.getSecret() != null) {
                     checkExpiration(var7, var10, var5);
                     removeDelayedCancelSCToken(var7, var10.getIdentifier());
                     return var10;
                  } else {
                     SCCredential var11 = null;
                     if (var10 != null) {
                        String var12 = getPhysicalStoreNameFromMessageContext(var7);
                        var11 = SCTStore.get(var10.getIdentifier(), var12);
                     }

                     getSession(var7, false);
                     checkExpiration(var7, var11, var5);
                     if (var11 != null) {
                        removeDelayedCancelSCToken(var7, var11.getIdentifier());
                     }

                     return var11;
                  }
               }
            }
         }
      } else {
         return null;
      }
   }

   public TrustToken issueTrustToken(WSTContext var1) throws WSTFaultException {
      SCCredential var2 = createNewSCCredential(var1);
      SCTokenBase var3 = this.newSCToken(var2);
      return var3;
   }

   public TrustToken renewTrustToken(WSTContext var1, TrustToken var2) throws WSTFaultException {
      SCTokenBase var3 = (SCTokenBase)var2;
      SCCredential var4 = var3.getCredential();
      var4.setCreated(var1.getCreated());
      var4.setExpires(var1.getExpires());
      WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_RECV_AFTER_RENEW);
      getSession(var1.getMessageContext(), false);
      String var5 = this.getPhysicalStoreNameFromWSTContext(var1);
      SCTStore.addToServer(var4, !var1.isSessionPersisted(), var5);
      WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_RECV_AFTER_RENEW_SAVE);
      return var3;
   }

   public void cancelTrustToken(WSTContext var1, TrustToken var2) throws WSTFaultException {
      MessageContext var3 = var1.getMessageContext();
      int var4 = WSSCCompatibilityUtil.getWSSCVersion(var3);
      boolean var5 = var4 == 2 || var4 == 3;
      String var6 = var2.getTrustCredential().getIdentifier();
      String var7;
      if (var5) {
         var7 = getPhysicalStoreNameFromMessageContext(var3);
         SCCredential var8 = SCTStore.get(var6, var7);
         setSCToContext(var3, var8);
         String var9 = (String)var3.getProperty("DELAYED_SCTOKEN_CANCEL");
         if (var9 != null) {
            if (var9.equals(var6)) {
               return;
            }

            removeDelayedCancelSCToken(var3, var9);
         }

         var3.setProperty("DELAYED_SCTOKEN_CANCEL", var6);
      }

      var7 = getPhysicalStoreNameFromMessageContext(var3);
      SCTStore.removeFromServer(var6, var7);
      getSession(var1.getMessageContext(), false);
   }

   public SecurityTokenReference createSecurityTokenReference(WSTContext var1, TrustToken var2) throws WSTFaultException {
      if (!(var2 instanceof SCTokenBase)) {
         WSTFaultUtil.raiseFault(new BadRequestException(var2.getValueType() + " is not a supported SCT"));
      }

      try {
         return new SCTokenReference(WSSConstants.REFERENCE_QNAME, (SCTokenBase)var2);
      } catch (WSSecurityException var4) {
         WSTFaultUtil.raiseFault(new BadRequestException(var4.getMessage()));
         return null;
      }
   }

   public TrustToken resolveTrustToken(WSTContext var1, SecurityTokenReference var2) throws WSTFaultException {
      TrustToken var3 = TrustTokenHelper.getTrustCredentialFromSecurityContext(var1, var2);
      if (var3 != null) {
         return var3;
      } else {
         throw this.newUnableToRenewException("Unable to resolve SC Token from STR: " + var2.getReferenceURI());
      }
   }

   private static void removeDelayedCancelSCToken(MessageContext var0, String var1) {
      String var2 = (String)var0.getProperty("DELAYED_SCTOKEN_CANCEL");
      if (var2 != null && var2.equals(var1)) {
         var0.removeProperty("DELAYED_SCTOKEN_CANCEL");

         try {
            String var3 = getPhysicalStoreNameFromMessageContext(var0);
            SCTStore.removeFromServer(var1, var3);
         } catch (Exception var4) {
         }
      }

   }

   private static HttpSession getSession(MessageContext var0, boolean var1) {
      if (var0 == null) {
         return null;
      } else {
         HttpServletRequest var2 = HttpTransportUtils.getHttpServletRequest(var0);
         return var2 != null ? var2.getSession(var1) : null;
      }
   }

   private static SCCredential createNewSCCredential(WSTContext var0) {
      Key var1 = var0.getSymmetricKey();
      if (var1 == null) {
         throw new IllegalArgumentException("SecretKey is not yet generated");
      } else {
         MessageContext var2 = var0.getMessageContext();
         String var3 = null;
         String var4;
         if (!isJaxwsRuntime(var2)) {
            var4 = Guid.generateGuidWithServerName();
         } else {
            WlMessageContext var5 = WlMessageContext.narrow(var2);
            PersistenceConfig.Common var6 = getPersistenceConfigFromMessageContext(var5);
            var3 = getPhysicalStoreName(var6);
            boolean var7 = false;

            try {
               var7 = !var6.getLogicalStoreMBean().getPersistenceStrategy().equals("NETWORK_ACCESSIBLE");
            } catch (IllegalArgumentException var9) {
               if (verbose) {
                  Verbose.log((Object)("Logical Store Not Found Error Message = " + var9.getMessage()));
               }
            }

            if (var7) {
               var4 = getRoutableId(var3);
            } else {
               var4 = Guid.generateGuidWithServerName();
            }
         }

         SCCredential var10 = new SCCredential();
         var10.setIdentifier(var4);
         var10.setSecret(var1);
         var10.setAppliesTo(var0.getAppliesTo());
         var10.setAppliesToElement(var0.getAppliesToElement());
         var10.setCreated(var0.getCreated());
         var10.setExpires(var0.getExpires());
         WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_RECV_RST_BEFORE_SAVE);
         SCTStore.addToServer(var10, !var0.isSessionPersisted(), var3);
         WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_RECV_RST_AFTER_SAVE);
         setSCToContext(var2, var10);
         getSession(var2, true);
         return var10;
      }
   }

   private static String getRoutableId(String var0) {
      return WsUtil.generateRoutableUUID(var0);
   }
}
