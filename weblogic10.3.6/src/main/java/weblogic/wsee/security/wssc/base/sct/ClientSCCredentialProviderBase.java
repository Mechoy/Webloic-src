package weblogic.wsee.security.wssc.base.sct;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.xml.rpc.Stub;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.sequence.CreateSequencePostSecurityTokenCallback;
import weblogic.wsee.security.wssc.faults.FaultVersionHelper;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wssc.sct.SCTClaims;
import weblogic.wsee.security.wssc.sct.SCTStore;
import weblogic.wsee.security.wssc.sct.SCTVersionHelper;
import weblogic.wsee.security.wst.binding.AppliesTo;
import weblogic.wsee.security.wst.binding.ComputedKey;
import weblogic.wsee.security.wst.binding.KeySize;
import weblogic.wsee.security.wst.binding.Lifetime;
import weblogic.wsee.security.wst.binding.RequestSecurityTokenResponse;
import weblogic.wsee.security.wst.binding.RequestSecurityTokenResponseCollection;
import weblogic.wsee.security.wst.binding.RequestedAttachedReference;
import weblogic.wsee.security.wst.binding.RequestedProofToken;
import weblogic.wsee.security.wst.binding.RequestedSecurityToken;
import weblogic.wsee.security.wst.binding.RequestedUnattachedReference;
import weblogic.wsee.security.wst.binding.TokenType;
import weblogic.wsee.security.wst.faults.InvalidRequestException;
import weblogic.wsee.security.wst.faults.InvalidScopeException;
import weblogic.wsee.security.wst.faults.RequestFailedException;
import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.wsee.security.wst.faults.WSTFaultUtil;
import weblogic.wsee.security.wst.framework.TrustSoapClient;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.security.wst.framework.WSTCredentialProviderHelper;
import weblogic.wsee.security.wst.helpers.BindingHelper;
import weblogic.wsee.security.wst.helpers.SOAPHelper;
import weblogic.wsee.security.wst.helpers.TrustTokenHelper;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.utils.KeyUtils;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public abstract class ClientSCCredentialProviderBase extends SCCredentialProviderBase {
   protected static final boolean verbose = Verbose.isVerbose(ClientSCCredentialProviderBase.class);

   public Object getCredential(String var1, String var2, ContextHandler var3, Purpose var4, SCTokenHandlerBase var5) {
      if (var4 != null && !var4.equals(Purpose.ENCRYPT_RESPONSE)) {
         SCCredential var6 = null;
         SecurityTokenContextHandler var7 = getSecurityCtxHandler(var3);
         if (var7 == null) {
            return null;
         } else {
            MessageContext var8 = getMessageContext(var7);
            if (var8 == null) {
               return null;
            } else if (var4.equals(Purpose.IDENTITY)) {
               var6 = getSCFromContext(var8);
               if (var6 != null && var6.getSecret() == null) {
                  String var12 = getPhysicalStoreNameFromMessageContext(var8);
                  var6 = SCTStore.getFromClient(var6.getIdentifier(), var12);
               }

               return var6;
            } else {
               WSTContext var9 = WSTContext.getWSTContext(var8);
               this.initWSTContext(var9, var8, var7, var5, var1);
               if (this.isProactive(var8)) {
                  var6 = this.requestCredentialWithProactive(var8, var9, var5);
               } else {
                  var6 = this.requestCredential(var8, var9, var5);
               }

               try {
                  CreateSequencePostSecurityTokenCallback.processCallback(var8);
                  return var6;
               } catch (WsrmException var11) {
                  throw new RuntimeException(var11.getMessage());
               }
            }
         }
      } else {
         return null;
      }
   }

   private boolean isProactive(MessageContext var1) {
      return "true".equalsIgnoreCase((String)var1.getProperty("weblogic.wsee.security.wssc.sct.enableSCCredentialProactiveRequestor"));
   }

   private SCCredential requestCredential(MessageContext var1, WSTContext var2, SCTokenHandlerBase var3) {
      SCCredential var4 = getSCFromContext(var1);
      String var5 = getPhysicalStoreNameFromMessageContext(var1);
      if (var4 == null) {
         var4 = createSCCredential(var2, var3);
         setSCToContext(var1, var4);
         SCTStore.addToClient(var4, !var2.isSessionPersisted(), var5);
      } else if (TrustTokenHelper.isExpired(var1, var4.getCreated(), var4.getExpires())) {
         renewCredential(var4, var2, var3);
         setSCToContext(var1, var4);
         SCTStore.addToClient(var4, !var2.isSessionPersisted(), var5);
      }

      return var4;
   }

   private SCCredential requestCredentialWithProactive(MessageContext var1, WSTContext var2, SCTokenHandlerBase var3) {
      SCCredentialProactiveRequestor var5 = SCCredentialProactiveRequestor.getProactiveRequestor(var1);
      String var6 = getPhysicalStoreNameFromMessageContext(var1);
      var5.lock();

      SCCredential var4;
      try {
         var4 = getSCFromContext(var1);
         if (var4 == null) {
            if (verbose) {
               Verbose.log((Object)"SC credential not found in context, needs a new SC credential.");
            }

            var4 = _createSCCredential(var5, var2, var3);
            setSCToContext(var1, var4);
            SCTStore.addToClient(var4, !var2.isSessionPersisted(), var6);
            var5.asyncRenewNext(var4);
         } else if (TrustTokenHelper.isExpired(var1, var4.getCreated(), var4.getExpires())) {
            if (verbose) {
               Verbose.log((Object)"SC credential expires, needs renewal on it.");
            }

            var5.waitOutProactiveRenewal();
            if (TrustTokenHelper.isExpired(var1, var4.getCreated(), var4.getExpires())) {
               _renewCredential(var5, var4, var2, var3);
               if (verbose) {
                  Verbose.log((Object)"Renew SC credential without using proactive requestor");
               }

               synchronized(var5) {
                  setSCToContext(var1, var4);
                  SCTStore.addToClient(var4, !var2.isSessionPersisted(), var6);
               }
            }
         }
      } finally {
         var5.unlock();
      }

      return var4;
   }

   public static void cancelSCToken(Stub var0) {
      Map var1 = (Map)var0._getProperty("weblogic.wsee.invoke_properties");
      boolean var2 = false;
      WSTContext var3 = null;
      if (var1 != null) {
         var3 = (WSTContext)var1.get("weblogic.wsee.security.wst.framework.WSTContext");
         if (var3 != null) {
            var2 = ((SoapMessageContext)var3.getMessageContext()).isSoap12();
         }
      }

      SoapMessageContext var4 = new SoapMessageContext(var2);
      if (var1 != null) {
         Iterator var5 = var1.entrySet().iterator();

         while(var5.hasNext()) {
            Map.Entry var6 = (Map.Entry)var5.next();
            var4.setProperty((String)var6.getKey(), var6.getValue());
         }

         var4.setProperty("weblogic.wsee.invoke_properties", var1);
      }

      cancelSCToken((MessageContext)var4);
   }

   public static void cancelSCToken(BindingProvider var0) {
      Map var1 = var0.getRequestContext();
      boolean var2 = false;
      WSTContext var3 = null;
      if (var1 != null) {
         var3 = (WSTContext)var1.get("weblogic.wsee.security.wst.framework.WSTContext");
         if (var3 != null) {
            var2 = ((SoapMessageContext)var3.getMessageContext()).isSoap12();
         }
      }

      SoapMessageContext var4 = new SoapMessageContext(var2);
      if (var1 != null) {
         Iterator var5 = var1.entrySet().iterator();

         while(var5.hasNext()) {
            Map.Entry var6 = (Map.Entry)var5.next();
            var4.setProperty((String)var6.getKey(), var6.getValue());
         }

         var4.setProperty("weblogic.wsee.invoke_properties", var1);
      }

      cancelSCToken((MessageContext)var4);
   }

   public static void cancelSCToken(MessageContext var0) {
      SCCredential var1 = getSCFromContext(var0);
      if (var1 == null) {
         WSTFaultUtil.raiseFault(new InvalidRequestException("No SCCredential to cancelSCToken"));
      }

      String var2 = var1.getScNamespace();

      assert var2 != null : "Warning ! SCNamespace in SCCredential is NULL, we require it in cancelSCToken(messageContext) to know which version of SCToken to load for cancel !";

      cancelSCToken(var0, var2);
   }

   private static void cancelSCToken(MessageContext var0, String var1) {
      cancelSCToken(var0, SCTVersionHelper.newSCTokenHandler(var1));
   }

   public static void cancelSCToken(MessageContext var0, SCTokenHandlerBase var1) {
      SCCredential var2 = getSCFromContext(var0);
      String var3 = var1.getSCT_RST_CANCEL_ACTION();
      WSTContext var4 = (WSTContext)var0.getProperty("weblogic.wsee.security.wst.framework.WSTContext");
      if (var4 != null) {
         var4.setAction(var3);
      }

      try {
         WSTCredentialProviderHelper.cancelCredential(var0, var2, var1, var1.getSCT_VALUE_TYPE(), var1.getCANNED_POLICY_INCLUDE_SCT_FOR_IDENTITY(), var1.getSCT_RST_CANCEL_ACTION());
      } finally {
         SCCredentialProactiveRequestor.dispose(var0);
      }

      removeSCFromContext(var0);
      String var5 = getPhysicalStoreNameFromMessageContext(var0);
      SCTStore.removeFromClient(var2.getIdentifier(), var5);
   }

   public static void renewSCToken(Stub var0) {
      Map var1 = (Map)var0._getProperty("weblogic.wsee.invoke_properties");
      boolean var2 = false;
      WSTContext var3 = null;
      if (var1 != null) {
         var3 = (WSTContext)var1.get("weblogic.wsee.security.wst.framework.WSTContext");
         if (var3 != null) {
            var2 = ((SoapMessageContext)var3.getMessageContext()).isSoap12();
         }
      }

      SoapMessageContext var4 = new SoapMessageContext(var2);
      if (var1 != null) {
         Iterator var5 = var1.entrySet().iterator();

         while(var5.hasNext()) {
            Map.Entry var6 = (Map.Entry)var5.next();
            var4.setProperty((String)var6.getKey(), var6.getValue());
         }

         var4.setProperty("weblogic.wsee.invoke_properties", var1);
      }

      renewSCToken((MessageContext)var4);
   }

   public static void renewSCToken(BindingProvider var0) {
      Map var1 = var0.getRequestContext();
      boolean var2 = false;
      WSTContext var3 = null;
      if (var1 != null) {
         var3 = (WSTContext)var1.get("weblogic.wsee.security.wst.framework.WSTContext");
         if (var3 != null) {
            var2 = ((SoapMessageContext)var3.getMessageContext()).isSoap12();
         }
      }

      SoapMessageContext var4 = new SoapMessageContext(var2);
      if (var1 != null) {
         Iterator var5 = var1.entrySet().iterator();

         while(var5.hasNext()) {
            Map.Entry var6 = (Map.Entry)var5.next();
            var4.setProperty((String)var6.getKey(), var6.getValue());
         }

         var4.setProperty("weblogic.wsee.invoke_properties", var1);
      }

      renewSCToken((MessageContext)var4);
   }

   public static void renewSCToken(MessageContext var0) {
      SCCredential var1 = getSCFromContext(var0);
      if (var1 == null) {
         WSTFaultUtil.raiseFault(new InvalidRequestException("No SCCredential to renewSCToken"));
      }

      String var2 = var1.getScNamespace();

      assert var2 != null : "Warning ! SCNamespace in SCCredential is NULL, we require it in renewSCToken(messageContext) to know which version of SCToken to load for renewal !";

      WSTContext var3 = (WSTContext)var0.getProperty("weblogic.wsee.security.wst.framework.WSTContext");
      renewCredential(var1, var3, SCTVersionHelper.newSCTokenHandler(var2));
      setSCToContext(var0, var1);
      String var4 = getPhysicalStoreNameFromMessageContext(var0);
      SCTStore.addToClient(var1, !var3.isSessionPersisted(), var4);
   }

   static void _renewCredential(SCCredentialProactiveRequestor var0, SCCredential var1, WSTContext var2, SCTokenHandlerBase var3) {
      SCCredentialProactiveRequestor.Measure var4 = var0.createAndStartMeasure();
      renewCredential(var1, var2, var3);
      var4.terminate();
   }

   static void renewCredential(SCCredential var0, WSTContext var1, SCTokenHandlerBase var2) {
      RequestSecurityTokenResponse var3 = WSTCredentialProviderHelper.renewCredential(var1, var0, var2, var2.getSCT_VALUE_TYPE(), var2.getCANNED_POLICY_INCLUDE_SCT_FOR_IDENTITY(), var2.getSCT_RST_RENEW_ACTION());
      RequestedSecurityToken var4 = var3.getRequestedSecurityToken();
      if (var4 == null) {
         WSTFaultUtil.raiseFault(FaultVersionHelper.newUnableToRenewException(var2.getXMLNS_WSC(), "No RequestedSecurityToken found in renew RSTR"));
      }

      Lifetime var5 = var3.getLifetime();
      if (var5 == null) {
         WSTFaultUtil.raiseFault(FaultVersionHelper.newUnableToRenewException(var2.getXMLNS_WSC(), "Lifetime is missing in renewal RSTR."));
      }

      var0.setCreated(var5.getCreated());
      var0.setExpires(var5.getExpires());
   }

   public static SCCredential createSCCredential(TrustSoapClient var0, WSTContext var1, SCTokenHandlerBase var2) throws WSTFaultException {
      initPolicy((SecurityTokenContextHandler)null, var1);
      SOAPMessage var3 = var0.requestTrustToken();
      Node var4 = SOAPHelper.getRSTBaseNode(var3);
      if (var2.getXMLNS_WSC().equalsIgnoreCase("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512")) {
         RequestSecurityTokenResponseCollection var6 = BindingHelper.unmarshalRSTRCNode(var4, var2);
         return getSCCredentialFromRSTRC(var1, var6, var2);
      } else if (var2.getXMLNS_WSC().equalsIgnoreCase("http://schemas.xmlsoap.org/ws/2005/02/sc")) {
         RequestSecurityTokenResponse var5 = BindingHelper.unmarshalRSTRNode(var4, var2);
         return getSCCredentialFromRSTR(var1, var5, var2);
      } else {
         throw new WSTFaultException(" unable to create SCCredential for WS-SC namespace='" + var2.getXMLNS_WSC() + "'.  Namespace not understood.");
      }
   }

   private static SCCredential _createSCCredential(SCCredentialProactiveRequestor var0, WSTContext var1, SCTokenHandlerBase var2) {
      SCCredentialProactiveRequestor.Measure var3 = var0.createAndStartMeasure();
      SCCredential var4 = createSCCredential(var1, var2);
      var3.terminate();
      return var4;
   }

   private static SCCredential createSCCredential(WSTContext var0, SCTokenHandlerBase var1) {
      try {
         RequestSecurityTokenResponse var2 = WSTCredentialProviderHelper.createCredential((WSTContext)var0, var1);
         return getSCCredentialFromRSTR(var0, var2, var1);
      } catch (WSTFaultException var3) {
         WSTFaultUtil.raiseFault(var3);
         return null;
      }
   }

   private static SCCredential getSCCredentialFromRSTRC(WSTContext var0, RequestSecurityTokenResponseCollection var1, SCTokenHandlerBase var2) throws WSTFaultException {
      List var3 = var1.getRequestSecurityTokenResponseCollection();
      if (var3.size() <= 0) {
         throw new WSTFaultException(" SecureTokenService did not return a WS-SecureConversation token in the returned RequestSecurityTokenResponseCollection ");
      } else {
         RequestSecurityTokenResponse var4 = (RequestSecurityTokenResponse)var3.get(0);
         return getSCCredentialFromRSTR(var0, var4, var2);
      }
   }

   private static SCCredential getSCCredentialFromRSTR(WSTContext var0, RequestSecurityTokenResponse var1, SCTokenHandlerBase var2) throws WSTFaultException {
      SCCredential var3 = var2.newSCCredential();
      TokenType var4 = var1.getTokenType();
      if (var4 != null && !var4.getTokenType().equals(var2.getSCT_VALUE_TYPE())) {
         throw new RequestFailedException("Unexpected token type in RSTR: " + var4.getTokenType());
      } else {
         RequestedSecurityToken var5 = var1.getRequestedSecurityToken();
         if (var5 == null) {
            throw new RequestFailedException("RequestedSecurityToken must be specified");
         } else {
            SecurityToken var6 = var5.getSecurityToken();
            if (!(var6 instanceof SCTokenBase)) {
               throw new RequestFailedException(var6.getValueType() + " is not a SCT");
            } else {
               SCTokenBase var7 = (SCTokenBase)var6;
               SCCredential var8 = var7.getCredential();
               SCCredential.copy(var8, var3);
               RequestedProofToken var9 = var1.getRequestedProofToken();
               if (var9 == null) {
                  throw new RequestFailedException("RequestedProofToken must be specified");
               } else {
                  ComputedKey var10 = var9.getComputedKey();
                  if (var10 == null) {
                     throw new RequestFailedException("ComputedKey is expected in RequestedProofToken");
                  } else {
                     String var11 = var10.getUri();
                     if (var11 != null && !var11.endsWith("/CK/PSHA1")) {
                        throw new InvalidScopeException(var11 + " of ComputedKey is not supported");
                     } else {
                        int var12 = 256;
                        KeySize var13 = var1.getKeySize();
                        if (var13 != null) {
                           var12 = var13.getSize();
                        }

                        Key var14 = null;

                        try {
                           var14 = KeyUtils.generateKey(var0.getRstNonce(), var1.getEntropy().getBinarySecret().getValue(), "AES", var12);
                        } catch (NoSuchAlgorithmException var21) {
                           throw new InvalidScopeException(var21.getMessage());
                        } catch (InvalidKeyException var22) {
                           throw new InvalidScopeException(var22.getMessage());
                        }

                        var3.setSecret(var14);
                        Lifetime var15 = var1.getLifetime();
                        var3.setCreated(var15.getCreated());
                        var3.setExpires(var15.getExpires());
                        AppliesTo var16 = var1.getAppliesTo();
                        if (var16 != null) {
                           var3.setAppliesTo(var16.getEndpointReference());
                           var3.setAppliesToElement(var16.getElement());
                        } else {
                           var3.setAppliesTo(var0.getAppliesTo());
                           var3.setAppliesToElement(var0.getAppliesToElement());
                        }

                        RequestedAttachedReference var17 = var1.getRequestedAttachedReference();
                        if (var17 != null) {
                           SecurityTokenReference var18 = var17.getSecurityTokenReference();
                           SCCredential.SecurityTokenReferenceInfo var19 = var3.newAttachedSecurityTokenReferenceInfo();
                           SCCredential.copyFromSTRToInfo(var18, var19);
                        }

                        RequestedUnattachedReference var23 = var1.getRequestedUnattachedReference();
                        if (var23 != null) {
                           SecurityTokenReference var24 = var23.getSecurityTokenReference();
                           SCCredential.SecurityTokenReferenceInfo var20 = var3.newUnattachedSecurityTokenReferenceInfo();
                           SCCredential.copyFromSTRToInfo(var24, var20);
                        }

                        return var3;
                     }
                  }
               }
            }
         }
      }
   }

   void initWSTContext(WSTContext var1, MessageContext var2, SecurityTokenContextHandler var3, SCTokenHandlerBase var4, String var5) {
      SCTClaims var6 = SCTClaims.newInstance(var3);
      long var7 = var6.getTokenLifetime();
      if (verbose) {
         Verbose.log((Object)("SCT Lifetime from SCTClaims is " + var7));
      }

      if (var7 > 0L) {
         var1.setLifetimePeriod(var7);
      }

      var1.setAction(var4.getSCT_RST_ACTION());
      var1.setTokenType(var4.getSCT_VALUE_TYPE());
      String var9 = (String)var3.getValue("weblogic.wsee.security.trust_version");
      if (var9 == null) {
         if (var5 != null) {
            if (var5.startsWith("http://schemas.xmlsoap.org/ws/2005/02/sc")) {
               var9 = "http://schemas.xmlsoap.org/ws/2005/02/trust";
            } else if (var5.startsWith("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512")) {
               var9 = "http://docs.oasis-open.org/ws-sx/ws-trust/200512";
            } else {
               if (verbose) {
                  Verbose.log((Object)("WARNING !  could not determine WS-Trust namespace URI from tokenType='" + var5 + "', forcing value to '" + "http://docs.oasis-open.org/ws-sx/ws-trust/200512" + "'"));
               }

               var9 = "http://docs.oasis-open.org/ws-sx/ws-trust/200512";
            }
         } else {
            if (verbose) {
               Verbose.log((Object)"WARNING !  could not determine WS-Trust namespace URI from null tokenType.  Forcing value to 'http://docs.oasis-open.org/ws-sx/ws-trust/200512'");
            }

            var9 = "http://docs.oasis-open.org/ws-sx/ws-trust/200512";
         }
      }

      var1.setTrustVersion(var9);
      String var10 = (String)var2.getProperty("weblogic.wsee.security.trust_soap_version");
      if (var10 != null) {
         var1.setSoapVersion(var10);
      }

      var1.initEndpoints(var2);
      String var11 = var1.getStsUri();
      if (var11 == null || var11.equals(var1.getEndpointUri())) {
         String var12 = (String)var2.getProperty("weblogic.wsee.wst.sts_endpoint_uri");
         if (var12 != null && !"".equals(var12)) {
            var11 = var12;
         } else {
            var11 = WSTCredentialProviderHelper.getSTSURIFromConfig(var3, var2, this.getClass().getName());
         }

         if (var11 == null) {
            var11 = var1.getEndpointUri();
         }

         if (verbose) {
            Verbose.log((Object)("Settng the STS endpoint URI to [" + var11 + "]"));
         }

         var1.setStsUri(var11);
      }

      initPolicy(var3, var1);
   }

   private static void initPolicy(SecurityTokenContextHandler var0, WSTContext var1) {
      if (var1.getBootstrapPolicy() == null) {
         NormalizedExpression var2 = null;
         if (var0 != null) {
            var2 = (NormalizedExpression)var0.getValue("weblogic.wsee.security.wst_bootstrap_policy");
         }

         if (var2 == null) {
            var2 = getTrustBootStrapPolicy(var1.getStsUri().toLowerCase(Locale.ENGLISH).startsWith("https"));
            var1.setWssp(false);
         } else {
            try {
               NormalizedExpression var3 = PolicyContext.getEndpointPolicy(var1.getMessageContext());
               var1.setOuterPolicy(var3);
               var1.setWssp(true);
            } catch (PolicyException var4) {
               throw new RuntimeException(var4.getMessage());
            }
         }

         if (null == var2) {
            throw new IllegalArgumentException("Unable to find the security policy for WS-Trust");
         } else {
            var1.setBootstrapPolicy(var2);
         }
      }
   }

   private static NormalizedExpression getTrustBootStrapPolicy(boolean var0) {
      NormalizedExpression var1 = NormalizedExpression.createUnitializedExpression();
      if (!var0) {
         try {
            PolicyServer var2 = new PolicyServer();
            var1 = var2.getPolicy("SecurityTokenService.xml").normalize();
         } catch (PolicyException var3) {
         }
      }

      return var1;
   }

   static void p(String var0) {
      Verbose.log((Object)(" [ClientSCCredentialProviderBase THORICK] " + var0));
   }
}
