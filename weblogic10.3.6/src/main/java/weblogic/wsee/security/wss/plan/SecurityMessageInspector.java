package weblogic.wsee.security.wss.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.security.policy.EncryptionTarget;
import weblogic.wsee.security.policy.SecurityToken;
import weblogic.wsee.security.saml.SAMLUtils;
import weblogic.wsee.security.wss.plan.helper.SOAPSecurityHeaderHelper;
import weblogic.wsee.security.wss.policy.EncryptionPolicy;
import weblogic.wsee.security.wss.policy.IdentityPolicy;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wss.policy.SecurityPolicyInspectionException;
import weblogic.wsee.security.wss.policy.SignaturePolicy;
import weblogic.wsee.security.wss.policy.TimestampPolicy;
import weblogic.wsee.security.wss.sps.SmartSecurityPolicyBlueprint;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.dsig.XMLSignatureImpl;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.dsig.api.spec.DigestMethodParameterSpec;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss11.internal.STRType;
import weblogic.xml.crypto.wss11.internal.SecurityBuilderImpl;
import weblogic.xml.crypto.wss11.internal.SecurityValidator;
import weblogic.xml.crypto.wss11.internal.SecurityValidatorFactory;
import weblogic.xml.crypto.wss11.internal.SignatureConfirmation;
import weblogic.xml.crypto.wss11.internal.WSS11Context;

public class SecurityMessageInspector {
   private static final boolean verbose = Verbose.isVerbose(SecurityMessageInspector.class);
   private static final boolean debug = false;
   private static final boolean WS_POLICY_INTEROP = true;
   private static final boolean DEBUG_DUMP_SOAP = false;
   private SecurityTokenContextHandler ctxHandler;
   private SecurityPolicyBlueprint blueprint;
   private SOAPMessageContext soapMessageCtx;
   private WSS11Context securityCtx;
   private SecurityValidator svalidator;
   private Map<String, SecurityToken> validatedTokenMap = new HashMap();
   private static final String COMPAT_FLAG_3006 = "Bypass.3006.error";

   public SecurityMessageInspector(SOAPMessageContext var1, WSS11Context var2) {
      this.soapMessageCtx = var1;
      this.securityCtx = var2;
      this.ctxHandler = new SecurityTokenContextHandler(var2);
      new SecurityValidatorFactory();
      this.svalidator = SecurityValidatorFactory.getSecurityValidator(var2);
   }

   public void inspectWssMessage(SmartSecurityPolicyBlueprint var1, boolean var2) throws SecurityPolicyInspectionException, SecurityPolicyArchitectureException, WSSecurityException {
      this.blueprint = var1.getSecurityPolicyBlueprint();
      this.init();
      this.checkMessage(var2);
      if (verbose) {
         Verbose.log((Object)"SOAP Security Message has been verified");
      }

   }

   private static boolean isTransportSecure(SOAPMessageContext var0) {
      WlMessageContext var1 = WlMessageContext.narrow(var0);
      boolean var2 = true;
      if (null != var1) {
         Object var3 = var1.getProperty("weblogic.wsee.transport.servlet.request.secure");
         if (var3 != null) {
            var2 = (Boolean)var3;
            if (verbose) {
               Verbose.say("The massage protected by HTTPS is " + var2);
            }
         }
      } else {
         Verbose.say("Unable to determine it is from HTTPS or not");
      }

      return var2;
   }

   private static boolean hasClientCert(SOAPMessageContext var0) {
      WlMessageContext var1 = WlMessageContext.narrow(var0);
      boolean var2 = true;
      if (null != var1) {
         Object var3 = var1.getProperty("weblogic.wsee.transport.client.cert.required");
         if (var3 != null) {
            var2 = (Boolean)var3;
            if (verbose) {
               Verbose.say("The client cert present " + var2);
            }
         }
      } else {
         Verbose.say("Unable to determine it has client cert or not");
      }

      return var2;
   }

   private void checkMessage(boolean var1) throws WSSecurityException, SecurityPolicyArchitectureException, SecurityPolicyInspectionException {
      boolean var2 = this.blueprint.isRequest();
      if (this.blueprint.hasTransportSecuirity() && var2) {
         if (!isTransportSecure(this.soapMessageCtx)) {
            Verbose.banner("*** Security Warning ***");
            Verbose.say("The policy requires transport security, but the message does not come from HTTPs");
            System.err.println("*** Security Warning ***\n The message is NOT protected by HTTPS");
            throw new SecurityPolicyInspectionException(2681);
         }

         if (this.blueprint.getGenrealPolicy().isClientCertificateRequired() && !hasClientCert(this.soapMessageCtx)) {
            Verbose.banner("*** Security Warning ***");
            Verbose.say("The policy requires client cert on two-way SSL, but the message does not come from two-way SSL");
            System.err.println("*** Security Warning ***\n The message is NOT protected by TWO-WAY SSL");
            throw new SecurityPolicyInspectionException(2641);
         }
      }

      SignatureConfirmation[] var3 = null;
      if (this.doAction(2)) {
         if (verbose) {
            Verbose.log((Object)"Inspecting message age ...");
         }

         if (var1) {
            this.inspectMessageAge(this.blueprint.getTimestampPolicy());
         }
      }

      if (this.doAction(1)) {
         if (verbose) {
            Verbose.log((Object)"Inspecting message authentication identity ...");
         }

         this.inspectIdentity(this.blueprint.getIdentityPolicy());
      }

      if (this.doAction(128)) {
         if (verbose) {
            Verbose.log((Object)"Inspecting signature confirmation ...");
         }

         var3 = this.inspectSignatureConfirmation(this.blueprint.getGeneralPolicy().isRequireSignatureConfirmation(), this.blueprint.getGeneralPolicy().isOptionalSignatureConfirmation());
      } else {
         var3 = this.inspectSignatureConfirmation(false, this.blueprint.getGeneralPolicy().isOptionalSignatureConfirmation());
      }

      if (this.doAction(4)) {
         if (verbose) {
            Verbose.log((Object)"Adding toekns to the message ...");
         }

         this.inspectEndorseToken(this.blueprint.getEndorsingPolicy(), this.blueprint.getSigningPolicy());
      }

      if (this.doAction(8192)) {
         Map var4 = this.blueprint.getEncryptionPolicy().getNodeMap();
         if (var4.size() == 1 && var4.containsKey("EncryptSignature")) {
            var4.remove("EncryptSignature");
         }
      }

      if (this.doAction(256)) {
         boolean var7 = false;
         if (this.doAction(288)) {
            var7 = true;
         }

         if (verbose) {
            Verbose.log((Object)("Inspecting signature and encryption ..., request =" + var2));
         }

         this.resolveSignatureList(false);
         this.resolveEncryptionList(var3);
         this.inspectIntegrityAndConfidentiality(this.blueprint.getSigningPolicy(), this.blueprint.getEncryptionPolicy(), var7, this.blueprint.getGeneralPolicy().isEncryptBeforeSigning());
      } else if (this.blueprint.getGeneralPolicy().isEncryptBeforeSigning()) {
         SecurityBuilderImpl.setEncryptBeforeSign(this.ctxHandler, true);
         if (this.doAction(8)) {
            if (verbose) {
               Verbose.log((Object)"Inspecting encryption  ...");
            }

            this.resolveEncryptionList(var3);
            this.inspectConfidentiality(this.blueprint.getEncryptionPolicy(), var2, true);
         }

         if (this.doAction(16)) {
            if (verbose) {
               Verbose.log((Object)"Inspecting signature ...");
            }

            this.resolveSignatureList(true);
            if (this.blueprint.isX509AuthConditional()) {
            }

            this.inspectIntegrity(this.blueprint.getSigningPolicy(), var2);
         }

         SecurityBuilderImpl.setEncryptBeforeSign(this.ctxHandler, false);
      } else {
         if (this.doAction(16)) {
            if (verbose) {
               Verbose.log((Object)"Inspecting signature ...");
            }

            this.resolveSignatureList(false);
            if (this.blueprint.isX509AuthConditional()) {
            }

            this.inspectIntegrity(this.blueprint.getSigningPolicy(), var2);
         }

         if (this.doAction(8)) {
            if (verbose) {
               Verbose.log((Object)"Inspecting encryption  ...");
            }

            if (this.blueprint.getEncryptionPolicy().getNodeMap().containsKey("EncryptSignature") && !this.blueprint.getSigningPolicy().isSignatureRequired()) {
               this.blueprint.getEncryptionPolicy().getNodeMap().remove("EncryptSignature");
            }

            this.resolveEncryptionList(var3);
            this.inspectConfidentiality(this.blueprint.getEncryptionPolicy(), var2, false);
         }
      }

      if (this.doAction(1024)) {
         if (verbose) {
            Verbose.log((Object)"Endorsing support token  ...");
         }

         this.resolveSignatureElementSignatureList();
         if ((this.blueprint.getGeneralPolicy().hasTrustOptions() || this.blueprint.getGeneralPolicy().isCompatMSFT()) && this.blueprint.getEndorsingPolicy().getValidSignatureTokens() != null) {
            Iterator var8 = this.blueprint.getEndorsingPolicy().getValidSignatureTokens().iterator();
            if (var8 != null && var8.hasNext()) {
               SecurityToken var5 = (SecurityToken)var8.next();
               List var6 = var5.getStrTypes();
               var6.add(new STRType(WSSConstants.REFERENCE_QNAME));
            }
         }

         this.inspectIntegrity(this.blueprint.getEndorsingPolicy(), var2);
      }

   }

   private boolean doAction(int var1) {
      int var2 = this.blueprint.getBuildingPlan();
      return (var2 & var1) == var1;
   }

   private void init() throws SecurityPolicyArchitectureException {
      this.blueprint.verifyPolicy(this.soapMessageCtx);
   }

   private void resolveSignatureList(boolean var1) throws SecurityPolicyInspectionException, WSSecurityException {
      SignaturePolicy var2 = this.blueprint.getSigningPolicy();
      Map var3 = var2.getSigningNodeMap();
      SOAPMessage var4 = this.soapMessageCtx.getMessage();

      try {
         SOAPSecurityHeaderHelper var5 = new SOAPSecurityHeaderHelper(this.soapMessageCtx);
         if (var3.containsKey("Body")) {
            var2.addSignatureNode("Body", var4.getSOAPBody());
         }

         if (var3.containsKey("UserNameToken")) {
            this.addReferenceByTokenValueType(var2, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken", var1);
         }

         List var6;
         if (var3.containsKey("SamlToken")) {
            var6 = this.blueprint.getIdentityPolicy().getValidIdentityTokens();
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               SecurityToken var8 = (SecurityToken)var7.next();
               String var9 = var8.getTokenTypeUri();
               boolean var10 = this.isSamlTokenType(var9);
               if (var10) {
                  this.addReferenceByTokenValueType(var2, var9, var1);
                  break;
               }
            }
         }

         if (var3.containsKey("SamlToken")) {
            var6 = null;
         }

         if (var3.containsKey("Header") && null == var3.get("Header")) {
         }

         if (var2.isSignatureRequired()) {
            Element var13 = var5.getTimestampElement();
            if (null != var13) {
               var2.addSignatureNode("TimeStamp", var13);
            }
         }

         var2.addSignatureNodeListToReference(this.soapMessageCtx);
      } catch (SecurityPolicyArchitectureException var11) {
         Verbose.logException(var11);
         throw new WSSecurityException(var11.getMessage(), var11);
      } catch (SOAPException var12) {
         Verbose.logException(var12);
         throw new WSSecurityException(var12.getMessage(), var12);
      }
   }

   private void addReferenceByTokenValueType(SignaturePolicy var1, String var2, boolean var3) throws WSSecurityException {
      SecurityToken var4 = (SecurityToken)this.validatedTokenMap.get(var2);
      if (null == var4) {
         throw new SecurityPolicyInspectionException(1601);
      } else {
         this.addReferenceByTokenValueType(var1, var2, var4, var3);
      }
   }

   private boolean isSamlTokenType(String var1) {
      if (null == var1) {
         return false;
      } else {
         return var1.startsWith("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile") || var1.startsWith("http://docs.oasis-open.org/wss/2004/01/oasis-2004-01-saml-token-profile");
      }
   }

   private void addReferenceByTokenValueType(SignaturePolicy var1, String var2, SecurityToken var3, boolean var4) throws WSSecurityException {
      weblogic.xml.crypto.wss.provider.SecurityToken var5 = null;
      List var6 = this.securityCtx.getSecurityTokens(var2);
      String var8;
      if (null == var6 || var6.size() == 0) {
         boolean var7 = this.isSamlTokenType(var2);
         if (var7) {
            var8 = var2;
            if (!var2.equals("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0")) {
               var8 = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1";
            }

            var6 = this.securityCtx.getSecurityTokens(var8);
         }
      }

      if (null != var6 && var6.size() != 0) {
         Iterator var12 = var6.iterator();

         while(var12.hasNext()) {
            var5 = (weblogic.xml.crypto.wss.provider.SecurityToken)var12.next();
            Node var14 = this.securityCtx.getNode(var5);
            if (null != var14) {
               break;
            }
         }

         if (null == var5) {
            throw new SecurityPolicyInspectionException(3601);
         } else {
            try {
               var12 = null;
               var8 = null;
               if (var4) {
                  Map var9 = (Map)this.ctxHandler.getValue("weblogic.wsee.security.encrypted_element.map");
                  if (null != var9) {
                     QName var10 = null;
                     if ("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken".equals(var2)) {
                        var10 = WSSConstants.UNT_QNAME;
                     }

                     var8 = (String)var9.get(var10);
                  }
               }

               Reference var13;
               if (var8 != null) {
                  var13 = this.svalidator.getReference(var8, this.blueprint.getXmlSignatureFactory().newDigestMethod(var1.getDigestMethod().getAlgorithm(), (DigestMethodParameterSpec)null), new ArrayList(), var3.isIncludeInMessage());
               } else {
                  var13 = this.svalidator.getReference(var5, this.blueprint.getXmlSignatureFactory().newDigestMethod(var1.getDigestMethod().getAlgorithm(), (DigestMethodParameterSpec)null), new ArrayList(), var3.isIncludeInMessage());
               }

               ArrayList var15 = new ArrayList(1);
               var15.add(var13);
               var1.addReferences(var15);
            } catch (Exception var11) {
               throw new SecurityPolicyInspectionException(3603);
            }
         }
      } else {
         throw new SecurityPolicyInspectionException(3601);
      }
   }

   private void resolveEncryptionList(SignatureConfirmation[] var1) throws SecurityPolicyInspectionException, SecurityPolicyArchitectureException, WSSecurityException {
      EncryptionPolicy var2 = this.blueprint.getEncryptionPolicy();
      Map var3 = var2.getNodeMap();
      SOAPMessage var4 = this.soapMessageCtx.getMessage();

      try {
         new SOAPSecurityHeaderHelper(this.soapMessageCtx);
         if (var3.containsKey("Body")) {
            if (var4.getSOAPBody().hasChildNodes()) {
               var2.addNode("Body", var4.getSOAPBody());
            } else if (verbose) {
               Verbose.log((Object)"No body encryption due to body is empty");
            }
         }

         if (var3.containsKey("EncryptSignature")) {
            List var6 = this.securityCtx.getSignatures();
            if (null == var6 || var6.size() == 0) {
               throw new SecurityPolicyInspectionException(3001);
            }

            XMLSignatureImpl var7 = (XMLSignatureImpl)var6.get(0);
            Element var8 = var7.getSignatureNode();
            var2.addNode("EncryptSignature", var8);
            if (var1 != null && var1.length > 0) {
               for(int var9 = 0; var9 < var1.length; ++var9) {
                  Element var10 = (Element)var1[var9].getSignatureConfirmationNode();
                  var2.addNode("Signature" + var9, var10);
               }
            }
         }

         if (var3.containsKey("UserNameToken")) {
            Node var12 = this.getNodeByTokenValueType("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken");
            if (null == var12) {
               throw new SecurityPolicyInspectionException(4621);
            }

            var2.addNode("UserNameToken", var12);
         }

         if (var3.containsKey("SamlToken")) {
            String var13 = this.getValidatedSamlTokenTypeUri();
            if (null == var13) {
               throw new SecurityPolicyInspectionException(4661);
            }

            Node var14 = this.getNodeByTokenValueType(var13);
            if (null == var14) {
               String var15 = var13;
               if (!var13.equals("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0")) {
                  var15 = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1";
               }

               var14 = this.getNodeByTokenValueType(var15);
            }

            if (null == var14) {
               throw new SecurityPolicyInspectionException(4661);
            }

            var2.addNode("SamlToken", var14);
         }

         if (var3.containsKey("Header")) {
         }

         var2.addEncryptionNodeList(this.soapMessageCtx);
      } catch (SOAPException var11) {
         Verbose.logException(var11);
         throw new WSSecurityException(var11.getMessage(), var11);
      }
   }

   private String getValidatedSamlTokenTypeUri() {
      if (this.validatedTokenMap.isEmpty()) {
         Verbose.log((Object)"No validated SAML Token");
         return null;
      } else {
         List var1 = this.blueprint.getIdentityPolicy().getValidIdentityTokens();
         Iterator var2 = var1.iterator();

         String var4;
         boolean var5;
         do {
            if (!var2.hasNext()) {
               Verbose.log((Object)"Unable to find any SAML Token Type URI from IdentityPolicy");
               return null;
            }

            SecurityToken var3 = (SecurityToken)var2.next();
            var4 = var3.getTokenTypeUri();
            var5 = this.isSamlTokenType(var4);
         } while(!var5);

         if (verbose) {
            Verbose.log((Object)("Found the SAML Token Type URI from IdentityPolicy =[" + var4 + "]"));
         }

         SecurityToken var6 = (SecurityToken)this.validatedTokenMap.get(var4);
         if (null == var6) {
            Verbose.log((Object)("No validated SAML Token for token type =[" + var4 + "]"));
            return null;
         } else {
            return var4;
         }
      }
   }

   private Node getNodeByTokenValueType(String var1) {
      List var2 = this.securityCtx.getSecurityTokens(var1);
      if (null != var2 && var2.size() != 0) {
         Iterator var3 = var2.iterator();

         Node var5;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            weblogic.xml.crypto.wss.provider.SecurityToken var4 = (weblogic.xml.crypto.wss.provider.SecurityToken)var3.next();
            var5 = this.securityCtx.getNode(var4);
         } while(null == var5);

         return var5;
      } else {
         return null;
      }
   }

   private void resolveSignatureElementSignatureList() throws SecurityPolicyInspectionException, WSSecurityException {
      SignaturePolicy var1 = this.blueprint.getEndorsingPolicy();
      Map var2 = var1.getSigningNodeMap();

      try {
         if (var2.containsKey("EndoseSignature")) {
            List var3 = this.securityCtx.getSignatures();
            XMLSignatureImpl var4 = null;
            if (this.blueprint.hasTransportSecuirity()) {
               if (verbose) {
                  Verbose.log((Object)"Endorsing supporting token + transport security caes....");
               }

               if (null == var3 || var3.size() == 0) {
                  throw new SecurityPolicyInspectionException(7501);
               }

               if (var3.size() == 1) {
                  return;
               }
            } else if (null == var3 || var3.size() != 2) {
               throw new SecurityPolicyInspectionException(7501);
            }

            var4 = (XMLSignatureImpl)var3.get(1);
            Element var5 = var4.getSignatureNode();
            ArrayList var6 = new ArrayList();
            if (!SecurityPolicyOutlineSketcher.isSignatureElement(var5)) {
               XMLSignatureImpl var7 = (XMLSignatureImpl)var3.get(0);
               var5 = var7.getSignatureNode();
            }

            var6.add(var5);
            var1.setNewSignatureNodeListToReference(var6);
         }

      } catch (Exception var8) {
         Verbose.logException(var8);
         if (var8 instanceof WSSecurityException) {
            throw (WSSecurityException)var8;
         } else {
            throw new WSSecurityException(var8.getMessage(), var8);
         }
      }
   }

   private void inspectMessageAge(TimestampPolicy var1) throws WSSecurityException {
      if (var1.isIncludeTimestamp()) {
         this.doMessageAge(var1);
      } else if (verbose) {
         Verbose.log((Object)"Timestamp is not required.");
      }

   }

   private void doMessageAge(TimestampPolicy var1) throws WSSecurityException {
      short var2 = var1.getMessageAgeSeconds();
      if (var2 == 0) {
         var2 = -1;
      }

      if (!this.svalidator.validateTimestamp(var2)) {
         throw new WSSecurityException("Timestamp validation failed.", WSSConstants.FAILURE_INVALID);
      } else {
         if (verbose) {
            Verbose.log((Object)(" timestamp(maxAgesSecs=" + var2 + ") verified"));
         }

      }
   }

   private void inspectEndorseToken(SignaturePolicy var1, SignaturePolicy var2) throws SecurityPolicyInspectionException, WSSecurityException {
      if (!var1.isSignatureRequired()) {
         if (verbose) {
            Verbose.log((Object)"No need to verify support token endorsing.");
         }

      } else {
         this.doEndorseToken(var1, var2);
      }
   }

   private void doEndorseToken(SignaturePolicy var1, SignaturePolicy var2) throws SecurityPolicyInspectionException, WSSecurityException {
      List var3 = var1.getValidSignatureTokens();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         SecurityToken var5 = (SecurityToken)var4.next();
         this.addReferenceByTokenValueType(var2, var5.getTokenTypeUri(), var5, false);
      }

   }

   private void inspectIdentity(IdentityPolicy var1) throws WSSecurityException {
      if (!var1.isAuthenticationRequired()) {
         if (verbose) {
            Verbose.log((Object)"Identity is not required.");
         }

      } else {
         this.doIdentity(var1);
      }
   }

   private void doIdentity(IdentityPolicy var1) throws WSSecurityException {
      List var2 = var1.getValidIdentityTokens();
      boolean var3 = false;
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         SecurityToken var5 = (SecurityToken)var4.next();
         this.setTokenIssuer(var5);
         this.securityCtx.setProperty("weblogic.xml.crypto.wss.SecurityValidator.securityTokenIncludedInMessage", var5.isIncludeInMessage());
         String var6 = var5.getTokenTypeUri();
         if (var6.endsWith("/dk") && SAMLUtils.isSamlTokenType(var5.getDerivedFromTokenType())) {
            var6 = var5.getDerivedFromTokenType();
         }

         if (verbose) {
            Verbose.log((Object)("Trying to validate identity assertion token type =" + var5.getTokenTypeUri() + " DerivedFromTokenType =" + var5.getDerivedFromTokenType() + " use Token Type for validation =" + var6));
         }

         if (this.svalidator.validateSecurityToken(var6, var5.getIssuerName(), var5.getClaims())) {
            var3 = true;
            if (verbose) {
               Verbose.log((Object)("Validated identity assertion token " + var5.getTokenTypeUri()));
            }

            this.validatedTokenMap.put(var5.getTokenTypeUri(), var5);
         }
      }

      this.securityCtx.setProperty("weblogic.xml.crypto.wss.SecurityValidator.securityTokenIncludedInMessage", (Object)null);
      if (!var3) {
         throw new SecurityPolicyInspectionException(1000);
      }
   }

   private void inspectIntegrity(SignaturePolicy var1, boolean var2) throws SecurityPolicyInspectionException, WSSecurityException {
      if (!var1.isSignatureRequired()) {
         if (verbose) {
            Verbose.log((Object)"Signature is not required.");
         }

      } else {
         this.doIntegrity(var1, var2);
      }
   }

   private void doIntegrity(SignaturePolicy var1, boolean var2) throws SecurityPolicyInspectionException, WSSecurityException {
      SignedInfo var3 = var1.getSignedInfo();
      List var4 = var1.getValidSignatureTokens();
      boolean var5 = false;
      boolean var6 = var1.signedSecurityTokens();
      Iterator var7 = var4.iterator();

      while(var7.hasNext()) {
         SecurityToken var8 = (SecurityToken)var7.next();
         this.setTokenIssuer(var8);
         this.securityCtx.setProperty("weblogic.xml.crypto.wss.SecurityValidator.securityTokenIncludedInMessage", var8.isIncludeInMessage());
         if (var6) {
            SecurityTokenContextHandler var9 = new SecurityTokenContextHandler();
            var9.addContextElement("weblogic.xml.crypto.wss.policy.Claims", var8.getClaims());
            Reference var10 = this.blueprint.getSigningReferencesFactory().newSigningTokenReference(var8, var9, var1.getDigestMethod().getAlgorithm());
            if (null == var10) {
               throw new SecurityPolicyInspectionException(3701);
            }

            var5 = this.svalidator.validateSignature(var1.newSignedInfo(this.blueprint.getXmlSignatureFactory(), var10), var8.getTokenTypeUri(), var8.getStrTypes(), var8.getIssuerName(), var8.getClaims());
         } else {
            var5 = this.svalidator.validateSignature(var3, var8.getTokenTypeUri(), var8.getStrTypes(), var8.getIssuerName(), var8.getClaims());
         }

         if (var5) {
            if (verbose) {
               Verbose.log((Object)"Signature has been validated successfully");
            }
            break;
         }
      }

      this.securityCtx.setProperty("weblogic.xml.crypto.wss.SecurityValidator.securityTokenIncludedInMessage", (Object)null);
      if (!var5) {
         if (var2) {
            throw new SecurityPolicyInspectionException(3000);
         } else {
            try {
               String var12 = (String)this.soapMessageCtx.getProperty("weblogic.wsee.policy.compat.preference");
               if (null != var12 && "Bypass.3006.error".equals(var12)) {
                  Verbose.banner("Policy Enforcement Problem Dectected");
                  Verbose.log((Object)"By pass Policy Error 3006 due to the property weblogic.wsee.policy.compat.preference is set to Bypass.3006.error");
                  return;
               }
            } catch (Exception var11) {
               Verbose.log((Object)"ERROR ON on getting compact property during processing of policy enforcement Error code 3006 for the response message");
               Verbose.logException(var11);
            }

            throw new SecurityPolicyInspectionException(3006);
         }
      }
   }

   private void inspectIntegrityAndConfidentiality(SignaturePolicy var1, EncryptionPolicy var2, boolean var3, boolean var4) throws SecurityPolicyInspectionException, WSSecurityException {
      if (!var2.isEncryptionRequired() && !var1.isSignatureRequired() && verbose) {
         Verbose.log((Object)"Neither Encryption nor Signature is required.");
      }

      this.doIntegrityAndConfidentiality(var1, var2, var3, var4);
   }

   private void doIntegrityAndConfidentiality(SignaturePolicy var1, EncryptionPolicy var2, boolean var3, boolean var4) throws SecurityPolicyInspectionException, WSSecurityException {
      List var5 = null;
      SignedInfo var6 = null;
      ArrayList var7 = null;
      EncryptionMethod var8 = var2.getKeyWrapMethod();
      EncryptionMethod var9 = var2.getEncryptionMethod();
      List var10 = var2.getEncryptionTargets();
      if (var1.isSignatureRequired()) {
         var6 = var1.getSignedInfo();
         var5 = var1.getValidSignatureTokens();
      }

      if (var2.isEncryptionRequired()) {
         var10 = var2.getEncryptionTargets();
         if (null == var5) {
            var5 = var2.getValidEncryptionTokens();
         }
      }

      if (null == var10) {
         if (verbose) {
            Verbose.log((Object)"There is no encryption target.");
         }
      } else {
         var7 = new ArrayList();
         Iterator var11 = var10.iterator();

         while(var11.hasNext()) {
            EncryptionTarget var12 = (EncryptionTarget)var11.next();
            var7.addAll(var12.getTBEs());
         }
      }

      boolean var15 = false;

      try {
         if (var3) {
            SecurityToken var16 = (SecurityToken)var5.get(0);
            this.setTokenIssuer(var16);
            this.securityCtx.setProperty("weblogic.xml.crypto.wss.SecurityValidator.securityTokenIncludedInMessage", var16.isIncludeInMessage());
            if (var4) {
               var15 = this.svalidator.validateEncryptionAndSignatureRequest(var6, var7, var8, var9, var16.getTokenTypeUri(), var16.getStrTypes(), var16.getIssuerName(), var16.isIncludeInMessage(), this.ctxHandler);
            } else {
               var15 = this.svalidator.validateSignatureAndEncryptionRequest(var6, var7, var8, var9, var16.getTokenTypeUri(), var16.getStrTypes(), var16.getIssuerName(), var16.isIncludeInMessage(), this.ctxHandler);
            }
         } else {
            try {
               var15 = this.svalidator.validateSignatureAndEncryptionResponse(var6, var7, var9, this.ctxHandler);
            } catch (Exception var13) {
               throw new SecurityPolicyInspectionException(5006, var13);
            }
         }
      } catch (XMLEncryptionException var14) {
         this.securityCtx.setProperty("weblogic.xml.crypto.wss.SecurityValidator.securityTokenIncludedInMessage", (Object)null);
         throw new SecurityPolicyInspectionException(4003, var14);
      }

      this.securityCtx.setProperty("weblogic.xml.crypto.wss.SecurityValidator.securityTokenIncludedInMessage", (Object)null);
      if (var15) {
         if (verbose) {
            Verbose.log((Object)"Signature and Encryption has been validated successfully.");
         }

      } else if (var3) {
         throw new SecurityPolicyInspectionException(5000);
      } else {
         throw new SecurityPolicyInspectionException(5006);
      }
   }

   private void inspectConfidentiality(EncryptionPolicy var1, boolean var2, boolean var3) throws SecurityPolicyInspectionException, WSSecurityException {
      if (!var1.isEncryptionRequired()) {
         if (verbose) {
            Verbose.log((Object)"Encryption is not required.");
         }

      } else {
         this.doConfidentiality(var1, var2, var3);
      }
   }

   private void doConfidentiality(EncryptionPolicy var1, boolean var2, boolean var3) throws SecurityPolicyInspectionException, WSSecurityException {
      List var4 = var1.getValidEncryptionTokens();
      List var5 = var1.getEncryptionTargets();
      EncryptionMethod var6 = var1.getKeyWrapMethod();
      Iterator var7 = var5.iterator();

      boolean var10;
      do {
         if (!var7.hasNext()) {
            return;
         }

         EncryptionTarget var8 = (EncryptionTarget)var7.next();
         EncryptionMethod var9;
         if (null != var8.getEncryptionMethod()) {
            var9 = var8.getEncryptionMethod();
         } else {
            var9 = var1.getEncryptionMethod();
         }

         var10 = false;
         Iterator var11 = var4.iterator();

         while(var11.hasNext()) {
            try {
               SecurityToken var12 = (SecurityToken)var11.next();
               this.setTokenIssuer(var12);
               this.securityCtx.setProperty("weblogic.xml.crypto.wss.SecurityValidator.securityTokenIncludedInMessage", var12.isIncludeInMessage());
               if (var3) {
                  HashMap var13 = new HashMap();
                  var10 = this.svalidator.validateEncryptionforEncryptFirst(var8.getTBEs(), var6, var9, var12.getTokenTypeUri(), var12.getStrTypes(), var12.getIssuerName(), var12.getClaims(), var13);
                  if (!var13.isEmpty()) {
                     this.ctxHandler.addContextElement("weblogic.wsee.security.encrypted_element.map", var13);
                  }
               } else {
                  var10 = this.svalidator.validateEncryption(var8.getTBEs(), var6, var9, var12.getTokenTypeUri(), var12.getStrTypes(), var12.getIssuerName(), var12.getClaims());
               }
            } catch (XMLEncryptionException var14) {
               this.securityCtx.setProperty("weblogic.xml.crypto.wss.SecurityValidator.securityTokenIncludedInMessage", (Object)null);
               throw new SecurityPolicyInspectionException(4003, var14);
            }

            if (var10) {
               if (verbose) {
                  Verbose.log((Object)"Encryption has been validated successfull.");
               }
               break;
            }
         }

         this.securityCtx.setProperty("weblogic.xml.crypto.wss.SecurityValidator.securityTokenIncludedInMessage", (Object)null);
      } while(var10);

      if (var2) {
         throw new SecurityPolicyInspectionException(4000);
      } else {
         throw new SecurityPolicyInspectionException(4006);
      }
   }

   private SignatureConfirmation[] inspectSignatureConfirmation(boolean var1, boolean var2) throws SecurityPolicyInspectionException {
      return this.doSignatureConfirmation(var1, var2);
   }

   private SignatureConfirmation[] doSignatureConfirmation(boolean var1, boolean var2) throws SecurityPolicyInspectionException {
      SignatureConfirmation[] var3 = null;
      List var4 = this.securityCtx.getSignatureConfirmations();
      if (var1) {
         if (null == var4 || var4.size() == 0) {
            throw new SecurityPolicyInspectionException(6301);
         }

         boolean var5 = this.svalidator.validateSignatureConfirmation();
         if (!var5) {
            throw new SecurityPolicyInspectionException(6300);
         }

         var3 = new SignatureConfirmation[var4.size()];

         for(int var6 = 0; var6 < var3.length; ++var6) {
            var3[var6] = (SignatureConfirmation)var4.get(var6);
         }
      } else if (!var2 && var4 != null && var4.size() > 0) {
         throw new SecurityPolicyInspectionException(6308);
      }

      return var3;
   }

   private void setTokenIssuer(SecurityToken var1) {
      if (var1.getTokenIssuer() == null) {
         this.ctxHandler.addContextElement("weblogic.wsee.security.issuer_endpoint_ref", (Object)null);
      } else {
         this.ctxHandler.addContextElement("weblogic.wsee.security.issuer_endpoint_ref", var1.getTokenIssuer());
      }

   }
}
