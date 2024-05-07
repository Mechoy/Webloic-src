package weblogic.wsee.security.saml;

import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.security.UsernameAndPassword;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyFinder;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.security.util.BSTCredentialProviderUtil;
import weblogic.wsee.security.wst.binding.BinarySecret;
import weblogic.wsee.security.wst.binding.KeySize;
import weblogic.wsee.security.wst.binding.RequestSecurityTokenResponse;
import weblogic.wsee.security.wst.binding.RequestSecurityTokenResponseCollection;
import weblogic.wsee.security.wst.binding.RequestedProofToken;
import weblogic.wsee.security.wst.binding.RequestedSecurityToken;
import weblogic.wsee.security.wst.binding.TokenType;
import weblogic.wsee.security.wst.faults.InvalidRequestException;
import weblogic.wsee.security.wst.faults.InvalidScopeException;
import weblogic.wsee.security.wst.faults.RequestFailedException;
import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.wsee.security.wst.framework.TrustSoapClient;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.security.wst.framework.WSTCredentialProviderHelper;
import weblogic.wsee.security.wst.helpers.BindingHelper;
import weblogic.wsee.security.wst.helpers.EncryptedKeyInfoBuilder;
import weblogic.wsee.security.wst.helpers.SOAPHelper;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.utils.KeyUtils;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.UsernameTokenImpl;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.X509Credential;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.dom.DOMUtils;

public class SAMLTrustCredentialProvider extends AbstractSAMLCredentialProvider {
   private static boolean verbose = Verbose.isVerbose(SAMLTrustCredentialProvider.class);
   protected static final ContextHandler EMPTY_CONTEXT = new SecurityTokenContextHandler();
   private SAMLCredential samlCredential = null;
   protected static final String[] SAML_VALUE_TYPES = new String[]{"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1", "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID", "http://docs.oasis-open.org/wss/2004/01/oasis-2004-01-saml-token-profile-1.0#SAMLAssertionID", "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0"};

   public Object getCredential(String var1, String var2, ContextHandler var3, Purpose var4) {
      if (null != this.samlCredential) {
         if (verbose) {
            Verbose.log((Object)"return with saved SAML Credential");
         }

         return this.samlCredential;
      } else {
         this.samlCredential = this.getCredentialSTSCSS(var1, var3);
         return this.samlCredential;
      }
   }

   private Object getCredentialLocalCSS(String var1, String var2, ContextHandler var3, Purpose var4) {
      if (!var4.equals(Purpose.IDENTITY) && !var4.equals(Purpose.SIGN)) {
         return null;
      } else if (!(var3 instanceof SecurityTokenContextHandler)) {
         return null;
      } else {
         SecurityTokenContextHandler var5 = new SecurityTokenContextHandler();
         Object var6 = var3.getValue("com.bea.contextelement.saml.CachingRequested");
         if (var6 != null) {
            var5.addContextElement("com.bea.contextelement.saml.CachingRequested", var6);
         }

         Node var7 = (Node)var3.getValue("weblogic.xml.crypto.wss.policy.Claims");
         CSSUtils.processSAMLClaims(false, var5, var7);
         CSSUtils.setupSAMLContextElements(false, var5, var3);
         Object var8 = null;
         if (CSSUtils.isHolderOfKey(false, var5)) {
            var8 = this.getKeyInfoCredential(var1, var2, (SecurityTokenContextHandler)var3, var4);
         }

         try {
            return CSSUtils.getSAMLCredential(false, "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID", var5, var8);
         } catch (WSSecurityException var10) {
            if (verbose) {
               Verbose.log("Exception while acquiring SAML credential", var10);
            }

            return null;
         }
      }
   }

   private SAMLCredential getCredentialSTSCSS(String var1, ContextHandler var2) {
      SecurityTokenContextHandler var3 = getSecurityCtxHandler(var2);
      if (var3 == null) {
         return null;
      } else {
         MessageContext var4 = getMessageContext(var3);
         if (var4 == null) {
            return null;
         } else {
            Node var5 = (Node)var2.getValue("weblogic.xml.crypto.wss.policy.Claims");
            boolean var6 = var1.equals("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0");
            CSSUtils.processSAMLClaims(var6, var3, var5);
            SAMLTokenHandler var7 = new SAMLTokenHandler();
            SAML2TokenHandler var8 = new SAML2TokenHandler();
            WSSecurityContext var9 = WSSecurityContext.getSecurityContext(var4);
            if (var9 != null) {
               var9.setTokenHandler(var7);
               var9.setTokenHandler(var8);
            }

            Element var10 = DOMUtils.getFirstElement(var5, SAMLIssuedTokenHelper.ISSUED_TK_POLICY_QNAME);
            WSTContext var11 = WSTContext.getWSTContext(var4);

            try {
               if (null == var10) {
                  this.intWSTContext(var1, var11, var4, var3);
               } else {
                  this.intWSTContext(var1, var11, var4, var3, var10);
               }
            } catch (PolicyException var16) {
               LogUtils.logWss("Could not load policy for SAML STS: " + var16.getMessage());
               return null;
            } catch (WSSecurityException var17) {
               LogUtils.logWss("Could not create OnBehalfOf token: " + var17.getMessage());
               return null;
            }

            TrustSoapClient var12 = null;

            try {
               var12 = new TrustSoapClient(var11);
               return createCredential(var12, var11, var7, var1);
            } catch (InvalidRequestException var14) {
               LogUtils.logWss("Could not retrieve SAML token through WS-Trust, request was invalid." + var14.getMessage());
            } catch (WSTFaultException var15) {
               var15.printStackTrace();
               LogUtils.logWss("Could not retrieve SAML token through WS-Trust, WS-Trust fault: " + var15.getMessage());
            }

            return null;
         }
      }
   }

   private void intWSTContext(String var1, WSTContext var2, MessageContext var3, SecurityTokenContextHandler var4) throws PolicyException, WSSecurityException {
      String var5 = (String)var4.getValue("weblogic.wsee.security.trust_version");
      if (var5 == null) {
         var5 = (String)var3.getProperty("weblogic.wsee.security.trust_version");
      }

      if (var5 == null) {
         var5 = "http://docs.oasis-open.org/ws-sx/ws-trust/200512";
      }

      String var6 = (String)var3.getProperty("weblogic.wsee.security.trust_soap_version");
      if (var6 != null) {
         var2.setSoapVersion(var6);
      }

      var2.setAction(SAMLSTSHelper.getAction(var5));
      var2.setTokenType(var1);
      var2.setTrustVersion(var5);
      var2.initEndpoints(var3);
      String var7 = var2.getStsUri();
      if (var7 == null || var7.equals(var2.getEndpointUri())) {
         var7 = WSTCredentialProviderHelper.getSTSURIFromConfig(var4, var3, this.getClass().getName());
         if (var7 == null) {
            var7 = var2.getEndpointUri();
         }

         var2.setStsUri(var7);
      }

      this.initPolicy(var4, var2, var3);
      String var8 = (String)var4.getValue("com.bea.contextelement.saml.subject.ConfirmationMethod");
      if (var8 != null) {
         if (var8.contains("bearer")) {
            var2.setKeyType("http://docs.oasis-open.org/ws-sx/ws-trust/200512/Bearer");
         } else if (!"sender-vouches".equals(var8) && !"urn:oasis:names:tc:SAML:2.0:cm:sender-vouches".equals(var8)) {
            if (var8.contains("holder-of-key")) {
               var2.setKeyType("http://docs.oasis-open.org/ws-sx/ws-trust/200512/PublicKey");
            }
         } else if ("metro".equals(var3.getProperty("weblogic.wsee.policy.compat.preference"))) {
            if (verbose) {
               Verbose.log((Object)"Setting keytype to \"http://schemas.oracle.com/ws/2010/03/securitypolicy/ws-sx/ws-trust/SenderVouches\" ");
            }

            var2.setKeyType("http://schemas.oracle.com/ws/2010/03/securitypolicy/ws-sx/ws-trust/SenderVouches");
         }
      }

      String var9 = (String)var3.getProperty("weblogic.wsee.security.trust_key_type");
      if (var9 != null) {
         var2.setKeyType(var9);
      }

      var2.setLifetimePeriod(-1L);
      var2.setKeySize(-1);
      var2.setBinarySecretType("none");
      String var10 = (String)var3.getProperty("weblogic.wsee.security.wst_onbehalfof_user");
      if (var10 != null) {
         UsernameAndPassword var11 = new UsernameAndPassword();
         var11.setUsername(var10);
         UsernameTokenImpl var12 = new UsernameTokenImpl(var11, EMPTY_CONTEXT);
         var2.setOnBehalfOfToken(var12);
      }

   }

   private void intWSTContext(String var1, WSTContext var2, MessageContext var3, SecurityTokenContextHandler var4, Element var5) throws PolicyException, WSSecurityException {
      if (null == var5) {
         this.intWSTContext(var1, var2, var3, var4);
      } else {
         var2.setIssuedTokenClaims(var5);
         var3.setProperty("weblogic.wsee.security.trust_claim", var5);
         SAMLIssuedTokenHelper var6 = new SAMLIssuedTokenHelper(var5);
         String var7 = var6.getTrustVersion();
         if (var7 == null) {
            var7 = (String)var4.getValue("weblogic.wsee.security.trust_version");
         }

         if (var7 == null) {
            var7 = (String)var3.getProperty("weblogic.wsee.security.trust_version");
         }

         if (var7 == null) {
            var7 = "http://docs.oasis-open.org/ws-sx/ws-trust/200512";
         }

         String var8 = (String)var3.getProperty("weblogic.wsee.security.trust_soap_version");
         if (var8 != null) {
            var2.setSoapVersion(var8);
         }

         var2.setAction(SAMLSTSHelper.getAction(var7));
         var2.setTokenType(var1);
         var2.setTrustVersion(var7);
         var2.initEndpoints(var3);
         String var9;
         String var10;
         if (StringUtil.isEmpty((String)var3.getProperty("weblogic.wsee.wst.saml.sts_endpoint_uri"))) {
            var9 = var6.getIssuerAddressUri();
            if (var9 != null) {
               var2.setStsUri(var9);
            } else {
               var10 = var2.getStsUri();
               if (var10 == null || var10.equals(var2.getEndpointUri())) {
                  var10 = WSTCredentialProviderHelper.getSTSURIFromConfig(var4, var3, this.getClass().getName());
                  if (var10 == null) {
                     var10 = var2.getEndpointUri();
                  }

                  var2.setStsUri(var10);
               }
            }
         }

         this.initPolicy(var4, var2, var3);
         var9 = var6.getKeyType();
         if (null == var9) {
            var9 = (String)var3.getProperty("weblogic.wsee.security.trust_key_type");
         }

         if (var9 != null) {
            var2.setKeyType(var9);
         } else {
            var10 = (String)var4.getValue("com.bea.contextelement.saml.subject.ConfirmationMethod");
            if (var10 != null) {
               if (var10.contains("bearer")) {
                  var2.setKeyType("http://docs.oasis-open.org/ws-sx/ws-trust/200512/Bearer");
               } else if (!"sender-vouches".equals(var10) && !"urn:oasis:names:tc:SAML:2.0:cm:sender-vouches".equals(var10)) {
                  if (var10.contains("holder-of-key")) {
                     var2.setKeyType("http://docs.oasis-open.org/ws-sx/ws-trust/200512/PublicKey");
                  }
               } else if ("metro".equals(var3.getProperty("weblogic.wsee.policy.compat.preference"))) {
                  if (verbose) {
                     Verbose.log((Object)"Setting keytype to \"http://schemas.oracle.com/ws/2010/03/securitypolicy/ws-sx/ws-trust/SenderVouches\" ");
                  }

                  var2.setKeyType("http://schemas.oracle.com/ws/2010/03/securitypolicy/ws-sx/ws-trust/SenderVouches");
               }
            }
         }

         var2.setLifetimePeriod(-1L);
         var2.setKeySize(var6.getKeySize());
         if (var9 == null || !var9.endsWith("/SymmetricKey")) {
            var2.setBinarySecretType("none");
         }

         var10 = (String)var3.getProperty("weblogic.wsee.security.wst_onbehalfof_user");
         if (var10 != null) {
            UsernameAndPassword var11 = new UsernameAndPassword();
            var11.setUsername(var10);
            UsernameTokenImpl var12 = new UsernameTokenImpl(var11, EMPTY_CONTEXT);
            var2.setOnBehalfOfToken(var12);
         }

      }
   }

   private void initPolicy(SecurityTokenContextHandler var1, WSTContext var2, MessageContext var3) throws PolicyException {
      NormalizedExpression var4 = WSTCredentialProviderHelper.getSTSPolicyFromConfig(var1, var3, this.getClass().getName());
      if (var3.getProperty("weblogic.wsee.security.wst_bootstrap_policy") != null) {
         Object var5 = var3.getProperty("weblogic.wsee.security.wst_bootstrap_policy");
         if (var5 instanceof NormalizedExpression) {
            var4 = (NormalizedExpression)var5;
         } else if (var5 instanceof InputStream) {
            InputStream var6 = (InputStream)var3.getProperty("weblogic.wsee.security.wst_bootstrap_policy");
            var4 = PolicyFinder.readPolicyFromStream((PolicyServer)null, "SAMLSTSPolicy.xml", var6, true).normalize();
         }
      }

      if (var4 == null) {
         var4 = SAMLSTSHelper.getTrustBootStrapPolicy(var2.getStsUri().toLowerCase(Locale.ENGLISH).startsWith("https"));
         var2.setWssp(false);
      } else {
         var2.setWssp(true);
      }

      var2.setBootstrapPolicy(var4);
   }

   public static SAMLCredential createCredential(TrustSoapClient var0, WSTContext var1, SecurityTokenHandler var2, String var3) throws WSTFaultException {
      SOAPMessage var4 = var0.requestTrustToken();
      Node var5 = SOAPHelper.getRSTBaseNode(var4);
      RequestSecurityTokenResponse var6 = null;
      if ("RequestSecurityTokenResponseCollection".equals(var5.getLocalName())) {
         RequestSecurityTokenResponseCollection var7 = BindingHelper.unmarshalRSTRCNode(var5, var2);
         List var8 = var7.getRequestSecurityTokenResponseCollection();
         if (var8 == null || var8.isEmpty()) {
            throw new WSTFaultException("Empty RequestSecurityTokenResponseCollection.");
         }

         var6 = (RequestSecurityTokenResponse)var8.get(0);
      } else {
         var6 = BindingHelper.unmarshalRSTRNode(var5, var2);
      }

      return getCredentialFromRSTR(var1, var6, var3);
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

   private String getIssueAction(String var1) {
      return var1 + "/Issue";
   }

   static SAMLCredential getFromContext(MessageContext var0) {
      return var0 == null ? null : (SAMLCredential)var0.getProperty("weblogic.wsee.saml.credential");
   }

   static void setToContext(MessageContext var0, SAMLCredential var1) {
      if (var0 != null) {
         var0.setProperty("weblogic.wsee.saml.credential", var1);
         Map var2 = (Map)var0.getProperty("weblogic.wsee.invoke_properties");
         if (var2 != null) {
            var2.put("weblogic.wsee.saml.credential", var1);
         }
      }

   }

   private static SAMLCredential getCredentialFromRSTR(WSTContext var0, RequestSecurityTokenResponse var1, String var2) throws WSTFaultException {
      TokenType var3 = var1.getTokenType();
      if (var3 != null && !var3.getTokenType().equals(var2)) {
         throw new RequestFailedException("Unexpected token type in RSTR: " + var3.getTokenType());
      } else {
         RequestedSecurityToken var4 = var1.getRequestedSecurityToken();
         if (var4 == null) {
            throw new RequestFailedException("RequestedSecurityToken must be specified");
         } else {
            SecurityToken var5 = var4.getSecurityToken();
            if (!(var5 instanceof SAMLToken)) {
               throw new RequestFailedException(var5.getValueType() + " is not a SAML token.");
            } else {
               LogUtils.logWss("From  RequestedSecurityToken, got SAML Token = " + var5.toString() + " id =" + var5.getId());
               SAMLToken var6 = (SAMLToken)var5;
               SAMLCredential var7 = (SAMLCredential)var6.getCredential();
               if (var7.isHolderOfKey()) {
                  String var8 = var0.getKeyType();
                  if (SAMLUtils.isSymmetricKeyType(var8)) {
                     if (null != var0.getSymmetricKey()) {
                        if (verbose) {
                           Verbose.log((Object)"Setting Symmetric Key from Entropy");
                        }

                        var7.setSymmetircKey(var0.getSymmetricKey());
                     } else {
                        if (verbose) {
                           Verbose.log((Object)"Getting Symmetric Key from RSTR");
                        }

                        var7.setSymmetircKey(getSymmetricKeyFromRstr(var1, var0));
                     }
                  } else if (var7.getPrivateKey() == null) {
                     X509Certificate var9 = var7.getX509Cert();
                     if (verbose) {
                        Verbose.log((Object)("setting key for SAML HofK for cert =" + var9.toString()));
                     }

                     X509Credential var10 = BSTCredentialProviderUtil.findX509Credential(var0.getMessageContext(), var9);
                     var7.setPrivateKey(var10.getPrivateKey());
                  }
               }

               return var7;
            }
         }
      }
   }

   private static Key getSymmetricKeyFromRstr(RequestSecurityTokenResponse var0, WSTContext var1) throws RequestFailedException, InvalidScopeException {
      RequestedProofToken var2 = var0.getRequestedProofToken();
      if (var2 == null) {
         throw new RequestFailedException("RequestedProofToken must be specified");
      } else {
         Object var3 = null;

         try {
            try {
               BinarySecret var4 = getBinarySecret(var0);
               String var5 = var4.getType();
               String var6;
               if (var5 != null && var5.endsWith("/Nonce")) {
                  var6 = var1.getSymmetricKeyAlgorithm();
                  if (var6 == null) {
                     var6 = "AES";
                  }

                  int var7 = var1.getKeySize();
                  KeySize var8 = var0.getKeySize();
                  if (var8 != null) {
                     var7 = var8.getSize();
                  }

                  var3 = KeyUtils.generateKey(var1.getRstNonce(), var4.getValue(), var6, var7);
               } else {
                  if (var5 == null || !var5.endsWith("/SymmetricKey")) {
                     throw new RequestFailedException("Not yet supported BinarySecret type: " + var5);
                  }

                  var6 = var1.getSymmetricKeyAlgorithm();
                  if (var6 == null) {
                     var6 = "AES";
                  }

                  var3 = new SecretKeySpec(var4.getValue(), var6);
               }
            } catch (NoSuchAlgorithmException var9) {
               throw new RequestFailedException("Unable to compute key from entropies");
            } catch (InvalidKeyException var10) {
               throw new RequestFailedException("Unable to compute key from entropies");
            }
         } catch (Exception var11) {
            throw new InvalidScopeException(var11.getMessage());
         }

         EncryptedKeyInfoBuilder.debugKey((Key)var3, "Key got from RSTR BinarySecret");
         return (Key)var3;
      }
   }

   private static BinarySecret getBinarySecret(RequestSecurityTokenResponse var0) throws RequestFailedException {
      if (null != var0.getEntropy()) {
         if (verbose) {
            Verbose.log((Object)"Getting BinarySecret from Entropy");
         }

         return var0.getEntropy().getBinarySecret();
      } else if (null != var0.getRequestedProofToken()) {
         if (verbose) {
            Verbose.log((Object)"Getting BinarySecret from RequestedProofToken");
         }

         return var0.getRequestedProofToken().getBinarySecret();
      } else {
         if (verbose) {
            Verbose.log((Object)"BinarySecret not found from either Entropy or  RequestedProofToken");
         }

         throw new RequestFailedException("BinarySecret is expected in Entropy or RequestedProofToken");
      }
   }

   public String[] getValueTypes() {
      return SAML_VALUE_TYPES;
   }
}
