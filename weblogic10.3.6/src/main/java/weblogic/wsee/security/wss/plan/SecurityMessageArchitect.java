package weblogic.wsee.security.wss.plan;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.security.policy.EncryptionTarget;
import weblogic.wsee.security.policy.SecurityToken;
import weblogic.wsee.security.saml.SAML2Constants;
import weblogic.wsee.security.saml.SAMLConstants;
import weblogic.wsee.security.saml.SAMLToken;
import weblogic.wsee.security.saml.SAMLUtils;
import weblogic.wsee.security.wss.SecurityPolicyException;
import weblogic.wsee.security.wss.plan.helper.SOAPSecurityHeaderHelper;
import weblogic.wsee.security.wss.plan.helper.TokenTypeHelper;
import weblogic.wsee.security.wss.policy.EncryptionPolicy;
import weblogic.wsee.security.wss.policy.GeneralPolicy;
import weblogic.wsee.security.wss.policy.IdentityPolicy;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wss.policy.SignaturePolicy;
import weblogic.wsee.security.wss.policy.TimestampPolicy;
import weblogic.wsee.security.wssc.v200502.WSCConstants;
import weblogic.wsee.security.wssp.QNameExpr;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.dsig.api.spec.DigestMethodParameterSpec;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.SecurityTokenHelper;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.crypto.wss11.internal.SecurityBuilder;
import weblogic.xml.crypto.wss11.internal.SecurityBuilderImpl;
import weblogic.xml.crypto.wss11.internal.SecurityImpl;
import weblogic.xml.crypto.wss11.internal.SignatureConfirmation;
import weblogic.xml.crypto.wss11.internal.WSS11Context;
import weblogic.xml.crypto.wss11.internal.enckey.EncryptedKeyToken;
import weblogic.xml.security.wsu.WSUConstants;

public class SecurityMessageArchitect {
   private static final boolean verbose = Verbose.isVerbose(SecurityMessageArchitect.class);
   private static final boolean debug = false;
   private static final String XMLNS_TRUST_13 = "http://docs.oasis-open.org/ws-sx/ws-trust/200512";
   private static final String XMLNS_TRUST_DEFAULT = "http://docs.oasis-open.org/ws-sx/ws-trust/200512";
   private SecurityTokenContextHandler ctxHandler;
   private SecurityPolicyBlueprint blueprint;
   private SOAPMessageContext soapMessageCtx;
   private WSS11Context securityCtx;
   private SecurityBuilder secBuilder;

   public SecurityMessageArchitect(WSS11Context var1) {
      this.securityCtx = var1;
   }

   public SecurityMessageArchitect(SOAPMessageContext var1, WSS11Context var2) {
      if (null != var1 && var1.getMessage() != null) {
         this.soapMessageCtx = var1;
         this.securityCtx = var2;
         this.ctxHandler = new SecurityTokenContextHandler(var2);
      } else {
         throw new IllegalArgumentException("Null Soap message context");
      }
   }

   public void buildWssMessage(SOAPMessageContext var1, SecurityPolicyBlueprint var2) throws PolicyException, WSSecurityException, SecurityPolicyException, MarshalException, XMLEncryptionException {
      if (null != var1 && var1.getMessage() != null) {
         this.soapMessageCtx = var1;
         this.ctxHandler = new SecurityTokenContextHandler(this.securityCtx);
         this.buildWssMessage(var2);
      } else {
         throw new IllegalArgumentException("Null Soap message context");
      }
   }

   private void buildWssMessage(SecurityPolicyBlueprint var1) throws PolicyException, WSSecurityException, SecurityPolicyException, MarshalException, XMLEncryptionException {
      this.blueprint = var1;
      this.secBuilder = this.blueprint.getSecurityBuilder();
      this.init();
      this.constructMessage();
      if (verbose) {
         Verbose.log((Object)"SOAP Security Message is constructed");
      }

   }

   private void constructMessage() throws PolicyException, WSSecurityException, SecurityPolicyException, MarshalException, XMLEncryptionException {
      boolean var1 = this.blueprint.isRequest();
      boolean var2 = false;
      SignatureConfirmation[] var3 = null;
      if (verbose) {
         if (this.blueprint.getPolicyAlternative() != null) {
            NormalizedExpression var4 = NormalizedExpression.createEmptyExpression();
            var4.addAlternative(this.blueprint.getPolicyAlternative());
            if (null == var4) {
               Verbose.log((Object)"Policy NormalizedExpression is null");
            } else {
               System.out.println(var4.toString());
            }
         } else {
            Verbose.log((Object)"Policy Alternative is null");
         }
      }

      boolean var5 = "LaxTimestampFirst".equals(this.blueprint.getGeneralPolicy().getLayout());
      if (this.doAction(2) && !var5) {
         if (verbose) {
            Verbose.log((Object)"Constructing message age (1)...");
         }

         this.processMessageAge(this.blueprint.getTimestampPolicy(), var5);
      }

      if (this.doAction(1)) {
         if (verbose) {
            Verbose.log((Object)"Constructing message authentication identity ...");
         }

         this.processIdentity(this.blueprint.getIdentityPolicy());
      }

      if (this.doAction(128)) {
         if (!this.blueprint.getGeneralPolicy().isRequireSignatureConfirmation()) {
            throw new IllegalStateException("Check why");
         }

         if (verbose) {
            Verbose.log((Object)"Constructing signature confirmation ...");
         }

         var3 = this.processSignatureConfirmation(this.blueprint.getGeneralPolicy());
      }

      if (this.doAction(2) && var5) {
         if (verbose) {
            Verbose.log((Object)"Constructing message age (2)...");
         }

         this.processMessageAge(this.blueprint.getTimestampPolicy(), var5);
      }

      if (this.doAction(4)) {
         if (verbose) {
            Verbose.log((Object)"Adding toekns to the message ...");
         }

         this.addEndorseToken(this.blueprint.getEndorsingPolicy(), this.blueprint.getSigningPolicy());
      }

      if (this.doAction(256)) {
         this.resolveSignatureList(var3);
         this.resolveEncryptionList(var3);
      }

      if (this.doAction(8448)) {
         if (verbose) {
            Verbose.log((Object)("Constructing signature and encryption with Endorsing togther for the Encrypt Signature case ..., request =" + var1));
         }

         if (this.doAction(1024)) {
            this.resolveSignatureElementSignatureList(var3);
         }

         this.processIntegrityAndConfidentialityAndEndorsing(this.blueprint.getSigningPolicy(), this.blueprint.getEncryptionPolicy(), this.blueprint.getEndorsingPolicy(), this.blueprint.getBuildingPlan(), var1);
      } else {
         if (this.doAction(256)) {
            if (verbose) {
               Verbose.log((Object)("Constructing signature and encryption ..., request =" + var1));
            }

            if (!this.isEncryptBeforeSigning() || this.isEncryptBeforeSigning() && (!this.blueprint.getEncryptionPolicy().isEncryptionRequired() || !this.blueprint.getSigningPolicy().isSignatureRequired())) {
               this.processIntegrityAndConfidentiality(this.blueprint.getSigningPolicy(), this.blueprint.getEncryptionPolicy(), var1);
            } else {
               SecurityBuilderImpl.setEncryptBeforeSign(this.ctxHandler, true);
               this.processIntegrityAndConfidentiality(this.blueprint.getSigningPolicy(), this.blueprint.getEncryptionPolicy(), var1);
               SecurityBuilderImpl.setEncryptBeforeSign(this.ctxHandler, false);
            }
         } else if (this.isEncryptBeforeSigning()) {
            SecurityBuilderImpl.setEncryptBeforeSign(this.ctxHandler, true);
            if (this.doAction(8)) {
               if (verbose) {
                  Verbose.log((Object)"Constructing encryption  (1)...");
               }

               this.resolveEncryptionList(var3);
               this.processConfidentiality(this.blueprint.getEncryptionPolicy(), var1);
            }

            if (this.doAction(16)) {
               if (verbose) {
                  Verbose.log((Object)"Constructing signature (1)...");
               }

               this.resolveSignatureList(var3);
               if (this.blueprint.isX509AuthConditional()) {
               }

               this.processIntegrity(this.blueprint.getSigningPolicy(), var1);
            }

            SecurityBuilderImpl.setEncryptBeforeSign(this.ctxHandler, false);
         } else {
            if (this.doAction(16)) {
               if (verbose) {
                  Verbose.log((Object)"Constructing signature (2) ...");
               }

               this.resolveSignatureList(var3);
               if (this.blueprint.isX509AuthConditional()) {
               }

               this.processIntegrity(this.blueprint.getSigningPolicy(), var1);
               if (this.doAction(1024)) {
                  if (verbose) {
                     Verbose.log((Object)"Endorsing support token (1) ...");
                  }

                  this.resolveSignatureElementSignatureList(var3);
                  this.ctxHandler.addContextElement("weblogic.wsee.security.need_to_move_timestamp", new Boolean(true));
                  this.processIntegrity(this.blueprint.getEndorsingPolicy(), var1);
                  var2 = true;
               }
            }

            if (this.doAction(8)) {
               if (verbose) {
                  Verbose.log((Object)"Constructing encryption (2) ...");
               }

               this.resolveEncryptionList(var3);
               this.processConfidentiality(this.blueprint.getEncryptionPolicy(), var1);
            }
         }

         if (!var2 && this.doAction(1024)) {
            if (verbose) {
               Verbose.log((Object)"Endorsing support token (2) ...");
            }

            if (this.isEncryptBeforeSigning()) {
               this.ctxHandler.addContextElement("weblogic.wsee.security.need_to_move_timestamp", new Boolean(true));
            }

            this.resolveSignatureElementSignatureList(var3);
            this.processIntegrity(this.blueprint.getEndorsingPolicy(), var1);
         }

      }
   }

   private boolean doAction(int var1) {
      int var2 = this.blueprint.getBuildingPlan();
      return (var2 & var1) == var1;
   }

   private boolean isEncryptBeforeSigning() {
      return this.blueprint.getGeneralPolicy().isEncryptBeforeSigning();
   }

   private void init() throws SecurityPolicyArchitectureException {
      this.blueprint.verifyPolicy(this.soapMessageCtx);
      this.secBuilder.setLayout(this.blueprint.getGeneralPolicy().getLayout());
      if (this.blueprint.getGeneralPolicy().isWss11()) {
         this.secBuilder.setWSSVersion("http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd");
      } else {
         this.secBuilder.setWSSVersion("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
      }

      if (this.blueprint.getGeneralPolicy().isRequireSignatureConfirmation()) {
         this.blueprint.getGeneralPolicy().setSignatureValues((String[])this.securityCtx.getPreviousMessageSignatureValues().get(0));
      }

      if (this.blueprint.getGeneralPolicy().hasTrustOptions()) {
         if (this.blueprint.getGeneralPolicy().getTrustOptions().isWst13()) {
            this.ctxHandler.addContextElement("weblogic.wsee.security.trust_version", "http://docs.oasis-open.org/ws-sx/ws-trust/200512");
         } else if (this.blueprint.getGeneralPolicy().getTrustOptions().isWst10()) {
            this.ctxHandler.addContextElement("weblogic.wsee.security.trust_version", "http://schemas.xmlsoap.org/ws/2005/02/trust");
         } else {
            if (verbose) {
               Verbose.log((Object)"Unsupported Trust version found, set to default Trust version with NS=http://docs.oasis-open.org/ws-sx/ws-trust/200512");
            }

            this.ctxHandler.addContextElement("weblogic.wsee.security.trust_version", "http://docs.oasis-open.org/ws-sx/ws-trust/200512");
         }
      }

      Map var1 = this.blueprint.encryptionPolicy.getNodeMap();
      if (var1.containsKey("EncryptSignature") && null != this.blueprint.getEndorsingPolicy()) {
         Map var2 = this.blueprint.getEndorsingPolicy().getSigningNodeMap();
         if (var2 != null && var2.containsKey("EndoseSignature")) {
            this.ctxHandler.addContextElement("weblogic.wsee.security.endorse_signature_encrypt_signature", new Boolean(true));
         }
      }

   }

   private void resolveSignatureList(SignatureConfirmation[] var1) throws SecurityPolicyException, WSSecurityException {
      SignaturePolicy var2 = this.blueprint.getSigningPolicy();
      this.resolveSignatureList(var2, var1);
   }

   private void resolveSignatureList(SignaturePolicy var1, SignatureConfirmation[] var2) throws SecurityPolicyException, WSSecurityException {
      Map var3 = var1.getSigningNodeMap();
      SOAPMessage var4 = this.soapMessageCtx.getMessage();

      try {
         SOAPSecurityHeaderHelper var5 = new SOAPSecurityHeaderHelper(this.soapMessageCtx);
         if (var3.containsKey("Body")) {
            var1.addSignatureNode("Body", var4.getSOAPBody());
         }

         Element var6;
         Map var7;
         if (var3.containsKey("UserNameToken")) {
            var6 = var5.getUsernameTokenElement();
            if (var6 != null) {
               var1.addSignatureNode("UserNameToken", var6);
            } else if (SecurityImpl.isEncryptBeforeSign(this.ctxHandler)) {
               var7 = (Map)this.ctxHandler.getValue("weblogic.wsee.security.encrypted_element.map");
               if (null != var7) {
                  var6 = (Element)var7.get(WSSConstants.UNT_QNAME);
                  if (var6 != null) {
                     var1.addSignatureNode("UserNameToken", var6);
                  }
               }
            }
         }

         if (var3.containsKey("SamlToken")) {
            var6 = var5.getSaml11Or20TokenElement();
            if (var6 != null) {
               if (this.blueprint.getGeneralPolicy().isCompatMSFT()) {
                  var1.addSignatureNode("SamlToken", var6);
               } else {
                  SAMLToken var13 = (SAMLToken)this.securityCtx.getToken(var6);
                  Reference var8 = this.secBuilder.createSTRReference(var13, var1.getDigestMethod(), (List)null, true);
                  String var9 = var8.getURI();
                  if (var9.startsWith("#")) {
                     var9 = var9.substring(1);
                  }

                  SecurityTokenReference var10 = this.securityCtx.getSTR(var9);
                  var10.marshal((Element)var6.getParentNode(), var6.getNextSibling(), this.securityCtx.getNamespaces());
                  var1.addSignatureReference("SamlToken", var8);
               }
            } else if (SecurityImpl.isEncryptBeforeSign(this.ctxHandler)) {
               var7 = (Map)this.ctxHandler.getValue("weblogic.wsee.security.encrypted_element.map");
               if (null != var7) {
                  var6 = (Element)var7.get(SAML2Constants.SAML2_ASST_QNAME);
                  if (var6 == null) {
                     var6 = (Element)var7.get(SAMLConstants.SAML_ASST_QNAME);
                  }

                  if (var6 != null) {
                     var1.addSignatureNode("SamlToken", var6);
                  }
               }
            }
         }

         Element var14;
         if (var3.containsKey("SecureConversationTokenToken")) {
            var14 = var5.getScToken13Element();
            QName var15;
            if (var14 == null) {
               var14 = var5.getScTokenElement();
               var15 = WSCConstants.SCT_QNAME;
            } else {
               var15 = weblogic.wsee.security.wssc.v13.WSCConstants.SCT_QNAME;
            }

            if (var14 != null) {
               var1.addSignatureNode("SecureConversationTokenToken", var14);
            } else if (SecurityImpl.isEncryptBeforeSign(this.ctxHandler)) {
               Map var16 = (Map)this.ctxHandler.getValue("weblogic.wsee.security.encrypted_element.map");
               if (null != var16) {
                  var14 = (Element)var16.get(var15);
                  if (var14 != null) {
                     var1.addSignatureNode("SecureConversationTokenToken", var14);
                  }
               }
            }
         }

         if (var3.containsKey("Header") && null == var3.get("Header")) {
            List var17 = SOAPSecurityHeaderHelper.getNonSecurityElements(var4, (QNameExpr)null);
            if (null != var17 && var17.size() > 0) {
               var1.addSignatureNodeListToReference(var17);
            }
         }

         if (var1.isSignatureRequired() || var3.containsKey("TimeStamp")) {
            var14 = var5.getTimestampElement();
            if (null != var14) {
               var1.addSignatureNode("TimeStamp", var14);
            }
         }

         if (var1.isSignatureRequired() && var2 != null && var2.length > 0) {
            for(int var18 = 0; var18 < var2.length; ++var18) {
               var1.addSignatureNode("Signature" + var18, var2[var18].getSignatureConfirmationNode());
            }
         }

         var1.addSignatureNodeListToReference(this.soapMessageCtx);
      } catch (SOAPException var11) {
         Verbose.logException(var11);
         throw new WSSecurityException(var11.getMessage(), var11);
      } catch (weblogic.xml.dom.marshal.MarshalException var12) {
         Verbose.logException(var12);
         throw new WSSecurityException(var12.getMessage(), var12);
      }
   }

   private void resolveEncryptionList(SignatureConfirmation[] var1) throws SecurityPolicyException, WSSecurityException {
      EncryptionPolicy var2 = this.blueprint.getEncryptionPolicy();
      Map var3 = var2.getNodeMap();
      SOAPMessage var4 = this.soapMessageCtx.getMessage();

      try {
         SOAPSecurityHeaderHelper var5 = new SOAPSecurityHeaderHelper(this.soapMessageCtx);
         if (var3.containsKey("Body")) {
            if (var4.getSOAPBody().hasChildNodes()) {
               var2.addNode("Body", var4.getSOAPBody());
            } else if (verbose) {
               Verbose.log((Object)"No body encryption due to body is empty");
            }
         }

         if (var3.containsKey("EncryptSignature")) {
            if ((this.blueprint.getBuildingPlan() & 8448) != 8448) {
               List var6 = var5.getSignatrueElements();
               if (null != var6 && var6.size() != 0) {
                  for(int var7 = 0; var7 < var6.size(); ++var7) {
                     var2.addNode("EncryptSignature" + var7, (Node)var6.get(var7));
                  }
               } else {
                  if (this.blueprint.isRequest()) {
                     throw new SecurityPolicyException("Missing signature element for encryption");
                  }

                  if (verbose) {
                     Verbose.log((Object)"No no signature requirement on response, no encrypt signature ...");
                  }
               }
            }

            if (var1 != null && var1.length > 0) {
               for(int var9 = 0; var9 < var1.length; ++var9) {
                  Element var11 = (Element)var1[var9].getSignatureConfirmationNode();
                  var2.addNode("Signature" + var9, var11);
               }
            }
         }

         if (var3.containsKey("UserNameToken")) {
            var2.addNode("UserNameToken", var5.getUsernameTokenElement());
         }

         if (var3.containsKey("SamlToken")) {
            Element var10 = var5.getSaml11Or20TokenElement();
            if (null != var10) {
               var2.addNode("SamlToken", var10);
            } else {
               Verbose.log((Object)"Unable to find SAML Token to Encrypt!");
            }
         }

         if (var3.containsKey("Header")) {
         }

         var2.addEncryptionNodeList(this.soapMessageCtx);
      } catch (SOAPException var8) {
         Verbose.logException(var8);
         throw new WSSecurityException(var8.getMessage(), var8);
      }
   }

   private void resolveSignatureElementSignatureList(SignatureConfirmation[] var1) throws WSSecurityException, SecurityPolicyException {
      SignaturePolicy var2 = this.blueprint.getEndorsingPolicy();
      if (this.blueprint.hasTransportSecuirity()) {
         if (verbose) {
            Verbose.log((Object)"Endorsing supporting token + transport security caes....");
         }

         this.resolveSignatureList(var2, var1);
      } else {
         Map var3 = var2.getSigningNodeMap();

         try {
            if (var3.containsKey("EndoseSignature")) {
               SOAPSecurityHeaderHelper var4 = new SOAPSecurityHeaderHelper(this.soapMessageCtx);
               Element var5 = var4.getSignatrueElement();
               boolean var6 = false;
               if (var5 == null && this.isEndosingEncryptSignature()) {
                  var5 = var4.getDummyElement("weblogic.wsee.security.signature_node");
                  QName var7 = new QName(WSUConstants.WSU_URI, "Id", "wsu");
                  DOMUtils.addAttribute(var5, var7, "weblogic.wsee.security.signature_node");
                  var6 = true;
               }

               if (var5 == null) {
                  throw new WSSecurityException("Missing signature element for Endorsing");
               }

               ArrayList var9 = new ArrayList();
               var9.add(var5);
               var2.setNewSignatureNodeListToReference(var9);
               if (var6) {
                  var4.removeDummyElement(var5);
               }
            }
         } catch (Exception var8) {
            Verbose.logException(var8);
            if (var8 instanceof WSSecurityException) {
               throw (WSSecurityException)var8;
            }

            throw new WSSecurityException(var8.getMessage(), var8);
         }
      }

      var2.setIncludeSigningTokens(var2.signedSecurityTokens());
   }

   private void processMessageAge(TimestampPolicy var1, boolean var2) throws WSSecurityException, MarshalException {
      if (var1.isIncludeTimestamp()) {
         this.doProcessMessageAge(var1, var2);
      } else if (verbose) {
         Verbose.log((Object)"Timestamp is not required.");
      }

   }

   private void doProcessMessageAge(TimestampPolicy var1, boolean var2) throws WSSecurityException, MarshalException {
      short var3 = var1.getMessageAgeSeconds();
      if (var2) {
         this.ctxHandler.addContextElement("weblogic.wsee.security.timestamp_first", (new Boolean(var2)).toString());
      }

      if (!this.secBuilder.addTimestamp(var3, this.ctxHandler)) {
         throw new WSSecurityException("Unable to add a Timestamp to the message");
      } else {
         if (verbose) {
            Verbose.log((Object)("Added timestamp(maxAgesSecs=" + var3 + ")..."));
         }

      }
   }

   private void addEndorseToken(SignaturePolicy var1, SignaturePolicy var2) throws WSSecurityException, MarshalException {
      if (!var1.isSignatureRequired()) {
         if (verbose) {
            Verbose.log((Object)"No need to add support token reference.");
         }

      } else {
         this.doAddEndorseToken(var1, var2);
      }
   }

   private void doAddEndorseToken(SignaturePolicy var1, SignaturePolicy var2) throws WSSecurityException, MarshalException {
      List var3 = var1.getValidSignatureTokens();
      boolean var4 = false;
      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         SecurityToken var6 = (SecurityToken)var5.next();
         this.setTokenIssuer(var6);
         if (var6.getDerivedFromTokenType() != null) {
            this.addDerivedFromToken(var6, true);
            weblogic.xml.crypto.wss.provider.SecurityToken var7 = (weblogic.xml.crypto.wss.provider.SecurityToken)this.ctxHandler.getValue("weblogic.wsee.wsc.derived_from_token");
         }

         Reference var9 = this.newSigningTokenReference(var6, this.ctxHandler, var2.getDigestMethod().getAlgorithm());
         if (null == var9) {
            throw new WSSecurityException("Unable to add token " + var6.getTokenTypeUri() + " DK token type " + var6.getDerivedFromTokenType());
         }

         ArrayList var8 = new ArrayList();
         var8.add(var9);
         var2.addReferences(var8);
      }

   }

   private void processIdentity(IdentityPolicy var1) throws WSSecurityException, MarshalException {
      if (!var1.isAuthenticationRequired()) {
         if (verbose) {
            Verbose.log((Object)"Identity is not required.");
         }

      } else {
         this.doProcessIdentity(var1);
      }
   }

   private void doProcessIdentity(IdentityPolicy var1) throws WSSecurityException, MarshalException {
      String var2 = null;
      List var3 = var1.getValidIdentityTokens();
      if (verbose) {
         Verbose.log((Object)("There are " + var3.size() + " valid identity tokens"));
      }

      boolean var4 = false;
      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         SecurityToken var6 = (SecurityToken)var5.next();
         if (var6.isOptional() && !this.secBuilder.isCredentialAvailable(var6.getTokenTypeUri())) {
            Verbose.log((Object)("Skip the optional token due to its availability, token = " + var6 + " type =" + var6.getTokenTypeUri()));
            var4 = true;
         } else {
            this.setTokenIssuer(var6);
            this.addClaimsToContextHandler(var6.getClaims());
            var2 = var6.getTokenTypeUri();
            if ("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk".equals(var2) || "http://schemas.xmlsoap.org/ws/2005/02/sc/dk".equals(var2)) {
               var2 = var6.getDerivedFromTokenType();
            }

            if (TokenTypeHelper.isSamlValueType(var2) && this.isSamlAttributeOnly()) {
               if (verbose) {
                  Verbose.log((Object)"Requesting a SAML Token with SAML Attributes only");
               }

               this.ctxHandler.addContextElement("oracle.contextelement.saml2.AttributeOnly", "True");
            }

            if (this.secBuilder.addSecurityToken(var2, var6.getIssuerName(), Purpose.IDENTITY, this.ctxHandler) == null) {
               if (verbose) {
                  Verbose.log((Object)("Failed to add token: " + var6 + " TokenUri =" + var2));
               }

               if (!var2.endsWith("/sc/sct")) {
                  continue;
               }

               var2 = var2 + " SCT for authentication is not supported";
               break;
            }

            if (verbose) {
               Verbose.log((Object)("Added " + var6 + " for identity"));
            }

            var4 = true;
            this.blueprint.setPolicyIdToken(var6);
            break;
         }
      }

      if (!var4) {
         throw new WSSecurityException("Unable to add security token for identity, token uri =" + var2);
      }
   }

   private void processIntegrity(SignaturePolicy var1, boolean var2) throws WSSecurityException, MarshalException {
      if (!var1.isSignatureRequired()) {
         if (verbose) {
            Verbose.log((Object)"Signature is not required.");
         }

      } else {
         this.doProcessIntegrity(var1, var2);
      }
   }

   private void doProcessIntegrity(SignaturePolicy var1, boolean var2) throws WSSecurityException, MarshalException {
      SignedInfo var3 = var1.getSignedInfo();
      List var4 = var1.getValidSignatureTokens();
      if (verbose) {
         Verbose.log((Object)("There are " + var4.size() + " signing tokens"));
      }

      if (verbose) {
         Verbose.log((Object)("SignedInfo is: " + var3));
      }

      Node var5 = null;
      boolean var6 = var1.signedSecurityTokens();
      Iterator var7 = var4.iterator();

      while(var7.hasNext()) {
         SecurityToken var8 = (SecurityToken)var7.next();
         if (verbose) {
            Verbose.log((Object)("SecurityToken: " + var8.toString()));
         }

         this.setTokenIssuer(var8);
         this.addClaimsToContextHandler(var8.getClaims());
         this.addDerivedFromToken(var8, var2);
         if (var6) {
            Reference var9 = this.newSigningTokenReference(var8, this.ctxHandler, var1.getDigestMethod().getAlgorithm());
            if (var9 == null) {
               continue;
            }

            if (verbose) {
               Verbose.log((Object)"Attempting signature on token");
            }

            var5 = this.secBuilder.addSignature(var1.newSignedInfo(var9), var9, var8.getStrTypes(), this.ctxHandler);
         } else {
            if (verbose) {
               Verbose.log((Object)"Attempting signature on message");
            }

            var5 = this.secBuilder.addSignature(var3, (String)var8.getTokenTypeUri(), (List)var8.getStrTypes(), var8.getIssuerName(), var8.isIncludeInMessage(), this.ctxHandler);
         }

         if (var5 != null) {
            if (verbose) {
               Verbose.log((Object)("Added Signature using " + var8));
            }
            break;
         }
      }

      if (var5 == null) {
         throw new WSSecurityException("Failed to add Signature.");
      }
   }

   private Reference newSigningTokenReference(SecurityToken var1, ContextHandler var2, String var3) throws WSSecurityException {
      Reference var4 = null;
      XMLSignatureFactory var5 = this.secBuilder.getXMLSignatureFactory();

      try {
         this.setTokenIssuer(var1);
         var4 = this.secBuilder.createReference(var1.getTokenTypeUri(), var1.getStrTypes(), var1.getIssuerName(), var5.newDigestMethod(var3, (DigestMethodParameterSpec)null), new ArrayList(), var1.isIncludeInMessage(), var2);
         return var4;
      } catch (NoSuchAlgorithmException var7) {
         throw new WSSecurityException(var7);
      } catch (InvalidAlgorithmParameterException var8) {
         throw new WSSecurityException(var8);
      }
   }

   private void processIntegrityAndConfidentiality(SignaturePolicy var1, EncryptionPolicy var2, boolean var3) throws WSSecurityException, MarshalException, XMLEncryptionException {
      if (!var2.isEncryptionRequired() && !var1.isSignatureRequired() && verbose) {
         Verbose.log((Object)"Neither Encryption nor Signature is required.");
      }

      this.doProcessIntegrityAndConfidentiality(var1, var2, var3);
   }

   private void doProcessIntegrityAndConfidentiality(SignaturePolicy var1, EncryptionPolicy var2, boolean var3) throws WSSecurityException, MarshalException, XMLEncryptionException {
      List var4 = null;
      SignedInfo var5 = null;
      ArrayList var6 = null;
      EncryptionMethod var7 = var2.getKeyWrapMethod();
      EncryptionMethod var8 = var2.getEncryptionMethod();
      List var9 = var2.getEncryptionTargets();
      SecurityToken var12;
      if (!var2.isEncryptionRequired()) {
         var5 = var1.getSignedInfo();
         var4 = var1.getValidSignatureTokens();
         if (var3) {
            var12 = (SecurityToken)var4.get(0);
            this.setTokenIssuer(var12);
            this.secBuilder.addSignature(var5, var7, var12.getTokenTypeUri(), var12.getStrTypes(), var12.getIssuerName(), var12.isIncludeInMessage(), this.ctxHandler);
         } else {
            this.secBuilder.addSignature(var5, this.ctxHandler);
         }

      } else {
         var9 = var2.getEncryptionTargets();
         var4 = var2.getValidEncryptionTokens();
         if (null == var9) {
            if (verbose) {
               Verbose.log((Object)"There is no encryption target.");
            }
         } else {
            var6 = new ArrayList();
            Iterator var10 = var9.iterator();

            while(var10.hasNext()) {
               EncryptionTarget var11 = (EncryptionTarget)var10.next();
               var6.addAll(var11.getTBEs());
            }
         }

         if (var1.isSignatureRequired()) {
            var5 = var1.getSignedInfo();
            if (null == var4) {
               var4 = var1.getValidSignatureTokens();
            }

            if (var3) {
               var12 = (SecurityToken)var4.get(0);
               this.setTokenIssuer(var12);
               this.secBuilder.addSignatureAndEncryption(var5, var6, var7, var8, var12.getTokenTypeUri(), var12.getStrTypes(), var12.getIssuerName(), var12.isIncludeInMessage(), this.ctxHandler);
            } else {
               this.secBuilder.addSignatureAndEncryption(var5, var6, var8, this.ctxHandler);
            }

            if (verbose) {
               Verbose.log((Object)"Signature and Encryption is done.");
            }

         } else {
            if (var3) {
               var12 = (SecurityToken)var4.get(0);
               this.setTokenIssuer(var12);
               boolean var13 = this.secBuilder.addEncryption(var6, var7, var8, var12.getTokenTypeUri(), var12.getStrTypes(), var12.getIssuerName(), var12.isIncludeInMessage(), this.ctxHandler);
               if (!var13) {
                  throw new WSSecurityException("Failed to add Encryption.");
               }

               if (verbose) {
                  Verbose.log((Object)"Encryption is done.");
               }
            } else {
               this.secBuilder.addEncryption(var6, var8, this.ctxHandler);
            }

         }
      }
   }

   private void processConfidentiality(EncryptionPolicy var1, boolean var2) throws WSSecurityException, MarshalException, XMLEncryptionException {
      if (!var1.isEncryptionRequired()) {
         if (verbose) {
            Verbose.log((Object)"Encryption is not required.");
         }

      } else {
         this.doProcessConfidentiality(var1, var2);
      }
   }

   private void doProcessConfidentiality(EncryptionPolicy var1, boolean var2) throws WSSecurityException, MarshalException, XMLEncryptionException {
      List var3 = var1.getValidEncryptionTokens();
      List var4 = var1.getEncryptionTargets();
      EncryptionMethod var5 = var1.getKeyWrapMethod();
      Iterator var6 = var4.iterator();

      boolean var9;
      do {
         if (!var6.hasNext()) {
            return;
         }

         EncryptionTarget var7 = (EncryptionTarget)var6.next();
         EncryptionMethod var8;
         if (null != var7.getEncryptionMethod()) {
            var8 = var7.getEncryptionMethod();
         } else {
            var8 = var1.getEncryptionMethod();
         }

         var9 = false;
         Iterator var10 = var3.iterator();

         while(var10.hasNext()) {
            SecurityToken var11 = (SecurityToken)var10.next();
            this.setTokenIssuer(var11);
            this.addClaimsToContextHandler(var11.getClaims());
            this.addDerivedFromToken(var11, var2);
            var9 = this.secBuilder.addEncryption(var7.getTBEs(), var5, var8, var11.getTokenTypeUri(), var11.getStrTypes(), var11.getIssuerName(), var11.isIncludeInMessage(), this.ctxHandler);
            if (var9) {
               if (verbose) {
                  Verbose.log((Object)"Encryption is done.");
               }
               break;
            }
         }
      } while(var9);

      throw new WSSecurityException("Failed to add Encryption.");
   }

   private SignatureConfirmation[] processSignatureConfirmation(GeneralPolicy var1) throws WSSecurityException, MarshalException {
      return this.doProcessSignatureConfirmation(var1);
   }

   private SignatureConfirmation[] doProcessSignatureConfirmation(GeneralPolicy var1) throws WSSecurityException, MarshalException {
      return this.secBuilder.addSignatureConfirmation(var1.getSignatureValues(), this.ctxHandler);
   }

   private void addClaimsToContextHandler(Node var1) {
      if (verbose) {
         Verbose.log((Object)("Adding claims map to context: " + (var1 == null ? null : var1.getLocalName())));
      }

      this.ctxHandler.addContextElement("weblogic.xml.crypto.wss.policy.Claims", var1);
   }

   private void addDerivedFromToken(SecurityToken var1, boolean var2) throws MarshalException, WSSecurityException {
      this.doAddDerivedFromToken(var1, var2);
   }

   private void doAddDerivedFromToken(SecurityToken var1, boolean var2) throws MarshalException, WSSecurityException {
      String var3 = var1.getDerivedFromTokenType();
      if (verbose) {
         Verbose.log((Object)("TokenTypeUri is: " + var1.getTokenTypeUri()));
      }

      if (verbose) {
         Verbose.log((Object)("DerivedFrom token type is: " + var3));
      }

      this.setTokenIssuer(var1);
      this.setBootstrapPolicy(var1);
      if (var3 != null) {
         Object var4 = this.ctxHandler.getValue("weblogic.wsee.wsc.derived_from_token");
         EncryptionMethod var5;
         EncryptionMethod var6;
         weblogic.xml.crypto.wss.provider.SecurityToken var7;
         if (var4 != null) {
            if (!SAMLUtils.isSamlTokenType(var3) && var4 instanceof SAMLToken) {
               if (verbose) {
                  Verbose.log((Object)("Wrong DerivedFromToken for derivedFromTokenType =" + var3 + " reset the token"));
               }

               var4 = null;
            } else if (SAMLUtils.isSamlTokenType(var3) && var4 instanceof EncryptedKeyToken && var2) {
               if (verbose) {
                  Verbose.log((Object)("Building DerivedFromToken for derivedFromTokenType =" + var3));
               }

               var5 = var1.getEncryptionMethod();
               if (verbose) {
                  Verbose.log((Object)("EncryptionMethod is: " + var5));
               }

               if (var5 != null) {
                  this.ctxHandler.addContextElement("weblogic.wsee.ek.encrypt_method", var5);
               }

               var6 = var1.getKeyWrapMethod();
               if (var6 != null) {
                  this.ctxHandler.addContextElement("weblogic.wsee.ek.keywrap_method", var6);
               }

               var7 = this.getDkBaseToken(var1, var3);
               if (null == var7) {
                  throw new WSSecurityException("Unable to create DK base token of type =" + var3 + " For Sign");
               }

               this.ctxHandler.addContextElement("weblogic.wsee.wsc.derived_from_token", var7);
            }
         }

         if (var4 == null) {
            if (verbose) {
               Verbose.log((Object)("DerivedFromToken is: " + var4));
            }

            var5 = var1.getEncryptionMethod();
            if (verbose) {
               Verbose.log((Object)("EncryptionMethod is: " + var5));
            }

            if (var5 != null) {
               if (!var2) {
                  this.ctxHandler.addContextElement("weblogic.wsee.wsc.derived_from_token", SecurityTokenHelper.findSecurityTokenInContext((ContextHandler)this.ctxHandler, var1.getDerivedFromTokenType()));
                  this.ctxHandler.addContextElement("weblogic.wsee.dk.referece_type", WSSConstants.KEY_IDENTIFIER_QNAME);
                  return;
               }

               this.ctxHandler.addContextElement("weblogic.wsee.ek.encrypt_method", var5);
            }

            var6 = var1.getKeyWrapMethod();
            if (var6 != null) {
               this.ctxHandler.addContextElement("weblogic.wsee.ek.keywrap_method", var6);
            }

            var7 = this.getDkBaseToken(var1, var3);
            if (null == var7) {
               throw new WSSecurityException("Unable to create DK base token of type =" + var3 + " For Sign");
            }

            this.ctxHandler.addContextElement("weblogic.wsee.wsc.derived_from_token", var7);
         }
      }

   }

   private weblogic.xml.crypto.wss.provider.SecurityToken getDkBaseToken(SecurityToken var1, String var2) throws MarshalException, WSSecurityException {
      weblogic.xml.crypto.wss.provider.SecurityToken var3 = null;
      if (var1.isIncludeDerivedFromInMessage()) {
         if (verbose) {
            Verbose.log((Object)"------ per Policy including Derived From SC Token in message  ----");
         }

         this.ctxHandler.addContextElement("weblogic.wsee.security.move_node_to_top", "true");
         if (var1.getStrTypesForDKBaseToken() != null && var1.getStrTypesForDKBaseToken().size() > 0) {
            this.ctxHandler.addContextElement("weblogic.wsee.dk.base_token_referece_type", var1.getStrTypesForDKBaseToken().get(0));
         }

         var3 = this.secBuilder.addSecurityToken(var2, (String)null, Purpose.SIGN, this.ctxHandler);
         this.ctxHandler.addContextElement("weblogic.wsee.dk.base_token_referece_type", (Object)null);
         this.ctxHandler.addContextElement("weblogic.wsee.security.move_node_to_top", "false");
      } else {
         if (verbose) {
            Verbose.log((Object)"----- per Policy NOT including Derived From SC Token in message -----");
         }

         var3 = this.secBuilder.createSecurityToken(var2, (String)null, Purpose.SIGN, this.ctxHandler);
      }

      return var3;
   }

   private void setBootstrapPolicy(SecurityToken var1) {
      NormalizedExpression var2 = var1.getBootstrapPolicy();
      if (var2 != null) {
         this.ctxHandler.addContextElement("weblogic.wsee.security.wst_bootstrap_policy", var2);
      }

      HashSet var3 = new HashSet();
      var3.add(this.blueprint.getPolicyAlternative());
      NormalizedExpression var4 = NormalizedExpression.createFromPolicyAlternatives(var3);
      this.ctxHandler.addContextElement("weblogic.wsee.security.wst_outer_policy", var4);
   }

   private void setTokenIssuer(SecurityToken var1) {
      if (verbose) {
         Verbose.log((Object)("Setting token issuer for token " + var1.toString() + " to " + var1.getTokenIssuer()));
      }

      if (var1.getTokenIssuer() == null) {
         this.ctxHandler.addContextElement("weblogic.wsee.security.issuer_endpoint_ref", (Object)null);
      } else {
         this.ctxHandler.addContextElement("weblogic.wsee.security.issuer_endpoint_ref", var1.getTokenIssuer());
      }

   }

   private boolean isWssc13() {
      return this.blueprint.getGeneralPolicy().isWssc13();
   }

   private boolean isEndosingEncryptSignature() {
      Boolean var1 = (Boolean)this.ctxHandler.getValue("weblogic.wsee.security.endorse_signature_encrypt_signature");
      return var1 != null ? var1 : false;
   }

   private void processIntegrityAndConfidentialityAndEndorsing(SignaturePolicy var1, EncryptionPolicy var2, SignaturePolicy var3, int var4, boolean var5) throws WSSecurityException, MarshalException, XMLEncryptionException {
      if (!var2.isEncryptionRequired() && !var1.isSignatureRequired() && verbose) {
         Verbose.log((Object)"Neither Encryption nor Signature is required.");
      }

      this.doProcessIntegrityAndConfidentialityAndEndorsing(var1, var2, var3, var4, var5);
   }

   private void doProcessIntegrityAndConfidentialityAndEndorsing(SignaturePolicy var1, EncryptionPolicy var2, SignaturePolicy var3, int var4, boolean var5) throws WSSecurityException, MarshalException, XMLEncryptionException {
      boolean var6 = (var4 & 1024) == 1024;
      boolean var7 = (var4 & 512) == 512;
      boolean var8 = (var4 & 8192) == 8192;
      boolean var9 = (var4 & 4096) == 4096;
      if (!var5 || !var6 || var3.getValidSignatureTokens() != null && var3.getValidSignatureTokens().size() != 0) {
         List var10 = null;
         SignedInfo var11 = null;
         ArrayList var12 = null;
         EncryptionMethod var13 = var2.getKeyWrapMethod();
         EncryptionMethod var14 = var2.getEncryptionMethod();
         List var15 = var2.getEncryptionTargets();
         SecurityToken var18;
         if (!var2.isEncryptionRequired()) {
            var11 = var1.getSignedInfo();
            var10 = var1.getValidSignatureTokens();
            if (var5) {
               var18 = (SecurityToken)var10.get(0);
               this.setTokenIssuer(var18);
               this.secBuilder.addSignature(var11, var13, var18.getTokenTypeUri(), var18.getStrTypes(), var18.getIssuerName(), var18.isIncludeInMessage(), this.ctxHandler);
            } else {
               this.secBuilder.addSignature(var11, this.ctxHandler);
            }

         } else {
            var15 = var2.getEncryptionTargets();
            var10 = var2.getValidEncryptionTokens();
            if (null == var15) {
               if (verbose) {
                  Verbose.log((Object)"There is no encryption target.");
               }
            } else {
               var12 = new ArrayList();
               Iterator var16 = var15.iterator();

               while(var16.hasNext()) {
                  EncryptionTarget var17 = (EncryptionTarget)var16.next();
                  var12.addAll(var17.getTBEs());
               }
            }

            if (var1.isSignatureRequired()) {
               var11 = var1.getSignedInfo();
               if (null == var10) {
                  var10 = var1.getValidSignatureTokens();
               }

               if (var5) {
                  var18 = (SecurityToken)var10.get(0);
                  if (var7) {
                     this.addClaimsToContextHandler(var18.getClaims());
                     this.addDerivedFromToken(var18, var5);
                  }

                  this.setTokenIssuer(var18);
                  if (var6) {
                     SecurityToken var20 = (SecurityToken)var3.getValidSignatureTokens().get(0);
                     this.secBuilder.addSignatureAndEncryptionAndEndorsing(var11, var12, var13, var14, var18.getTokenTypeUri(), var18.getStrTypes(), var18.getIssuerName(), var18.isIncludeInMessage(), var4, var3.getSignedInfo(), var20.getTokenTypeUri(), var20.getStrTypes(), var20.getIssuerName(), var20.isIncludeInMessage(), this.ctxHandler);
                  } else {
                     this.secBuilder.addSignatureAndEncryptionAndEndorsing(var11, var12, var13, var14, var18.getTokenTypeUri(), var18.getStrTypes(), var18.getIssuerName(), var18.isIncludeInMessage(), var4, (SignedInfo)null, (String)null, (List)null, (String)null, false, this.ctxHandler);
                  }
               } else if (var6 && var3.getValidSignatureTokens() != null && var3.getValidSignatureTokens().size() > 0) {
                  var18 = (SecurityToken)var3.getValidSignatureTokens().get(0);
                  this.secBuilder.addSignatureAndEncryptionAndEndorsing(var11, var12, var14, var4, var3.getSignedInfo(), var18.getTokenTypeUri(), var18.getStrTypes(), var18.getIssuerName(), var18.isIncludeInMessage(), this.ctxHandler);
               } else {
                  this.secBuilder.addSignatureAndEncryptionAndEndorsing(var11, var12, var14, var4, (SignedInfo)null, (String)null, (List)null, (String)null, false, this.ctxHandler);
               }

               if (verbose) {
                  Verbose.log((Object)"Signature and Encryption is done.");
               }

            } else {
               if (var5) {
                  var18 = (SecurityToken)var10.get(0);
                  this.setTokenIssuer(var18);
                  boolean var19 = this.secBuilder.addEncryption(var12, var13, var14, var18.getTokenTypeUri(), var18.getStrTypes(), var18.getIssuerName(), var18.isIncludeInMessage(), this.ctxHandler);
                  if (!var19) {
                     throw new WSSecurityException("Failed to add Encryption.");
                  }

                  if (verbose) {
                     Verbose.log((Object)"Encryption is done.");
                  }
               } else {
                  this.secBuilder.addEncryption(var12, var14, this.ctxHandler);
               }

            }
         }
      } else {
         throw new IllegalArgumentException("No endorsing token");
      }
   }

   protected boolean isSamlAttributeOnly() {
      Object var1 = this.soapMessageCtx.getProperty("oracle.contextelement.saml2.AttributeOnly");
      if (null == var1) {
         return false;
      } else {
         if (verbose) {
            Verbose.log((Object)("Property oracle.contextelement.saml2.AttributeOnly = " + var1.toString()));
         }

         if (var1 instanceof Boolean) {
            return (Boolean)var1;
         } else {
            return var1 instanceof String ? Boolean.parseBoolean((String)var1) : false;
         }
      }
   }
}
