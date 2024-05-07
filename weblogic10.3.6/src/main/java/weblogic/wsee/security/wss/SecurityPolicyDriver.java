package weblogic.wsee.security.wss;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.security.policy.EncryptionPolicy;
import weblogic.wsee.security.policy.EncryptionTarget;
import weblogic.wsee.security.policy.IdentityPolicy;
import weblogic.wsee.security.policy.SecurityToken;
import weblogic.wsee.security.policy.SigningPolicy;
import weblogic.wsee.security.policy.SigningReferencesFactory;
import weblogic.wsee.security.policy.TimestampPolicy;
import weblogic.wsee.security.policy.assertions.ConfidentialityAssertion;
import weblogic.wsee.security.policy.assertions.IdentityAssertion;
import weblogic.wsee.security.policy.assertions.IntegrityAssertion;
import weblogic.wsee.security.policy.assertions.MessageAgeAssertion;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionFactory;
import weblogic.xml.crypto.wss.BSTUtils;
import weblogic.xml.crypto.wss.SecurityBuilder;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.Purpose;

public class SecurityPolicyDriver {
   private SecurityBuilder sbuilder;
   private SecurityTokenContextHandler ctxHandler;
   private SecurityToken policyIdToken;
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicyDriver.class);

   public SecurityPolicyDriver(SecurityBuilder var1, WSSecurityContext var2) {
      this.sbuilder = var1;
      this.ctxHandler = new SecurityTokenContextHandler(var2);
   }

   public void processOutbound(PolicyAlternative var1, SOAPMessageContext var2) throws PolicyException, WSSecurityException, SecurityPolicyException, MarshalException, XMLEncryptionException {
      this.processOutbound(var1, (PolicyAlternative)null, var2);
   }

   public void processOutbound(PolicyAlternative var1, PolicyAlternative var2, SOAPMessageContext var3) throws PolicyException, WSSecurityException, SecurityPolicyException, MarshalException, XMLEncryptionException {
      if (var2 != null) {
         this.processConfidentialityTokens(var2, var3);
      }

      if (var1 != null) {
         this.processMessageAge(var1);
         this.processIdentity(var1);
         this.processIntegrity(var1, var3);
         this.processConfidentiality(var1, var3);
      }

   }

   private void processConfidentialityTokens(PolicyAlternative var1, SOAPMessageContext var2) throws PolicyException, WSSecurityException, MarshalException {
      Set var3 = var1.getAssertions(ConfidentialityAssertion.class);
      if (var3 != null && var3.size() != 0) {
         this.doConfidentialityTokens(var3, var2);
      }
   }

   private void doConfidentialityTokens(Set<ConfidentialityAssertion> var1, SOAPMessageContext var2) throws PolicyException, WSSecurityException, MarshalException {
      XMLEncryptionFactory var3 = this.sbuilder.getXMLEncryptionFactory();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         ConfidentialityAssertion var5 = (ConfidentialityAssertion)var4.next();
         EncryptionPolicy var6 = new EncryptionPolicy(var3, var2, var5, false);
         List var7 = var6.getValidEncryptionTokens();
         boolean var8 = false;
         Iterator var9 = var7.iterator();

         while(var9.hasNext()) {
            SecurityToken var10 = (SecurityToken)var9.next();
            this.ctxHandler.addContextElement("weblogic.xml.crypto.wss.policy.Claims", var10.getClaims());
            var8 = this.sbuilder.addSecurityToken(var10.getTokenTypeUri(), var10.getTokenIssuer(), Purpose.ENCRYPT_RESPONSE, this.ctxHandler) != null;
            if (var8) {
               break;
            }
         }
      }

   }

   private void processMessageAge(PolicyAlternative var1) throws WSSecurityException, MarshalException, PolicyException {
      Set var2 = var1.getAssertions(MessageAgeAssertion.class);
      if (var2.size() > 0) {
         this.doMessageAge(var2);
      }

   }

   private void doMessageAge(Set<MessageAgeAssertion> var1) throws WSSecurityException, MarshalException, PolicyException {
      if (var1.size() > 1) {
         throw new PolicyException("Only one MessageAge specification is supported in a policy alternative");
      } else {
         TimestampPolicy var2 = new TimestampPolicy((MessageAgeAssertion)var1.iterator().next());
         short var3 = var2.getMessageAgeSeconds();
         if (!this.sbuilder.addTimestamp(var3, this.ctxHandler)) {
            throw new WSSecurityException("Unable to add a Timestamp to the message");
         } else {
            if (verbose) {
               Verbose.log((Object)("Added timestamp(maxAgesSecs=" + var3 + ")..."));
            }

         }
      }
   }

   private void processIdentity(PolicyAlternative var1) throws WSSecurityException, MarshalException {
      this.doIdentity(var1);
   }

   private void doIdentity(PolicyAlternative var1) throws WSSecurityException, MarshalException {
      Set var2 = var1.getAssertions(IdentityAssertion.class);
      Iterator var3 = var2.iterator();

      boolean var7;
      do {
         if (!var3.hasNext()) {
            return;
         }

         IdentityAssertion var4 = (IdentityAssertion)var3.next();
         IdentityPolicy var5 = new IdentityPolicy(var4);
         List var6 = var5.getValidIdentityTokens();
         var7 = false;
         Iterator var8 = var6.iterator();

         while(var8.hasNext()) {
            SecurityToken var9 = (SecurityToken)var8.next();
            this.addClaimsToContextHandler(var9.getClaims());
            if (this.sbuilder.addSecurityToken(var9.getTokenTypeUri(), var9.getTokenIssuer(), Purpose.IDENTITY, this.ctxHandler) != null) {
               if (verbose) {
                  Verbose.log((Object)("Added " + var9));
               }

               var7 = true;
               this.policyIdToken = var9;
               break;
            }

            if (verbose) {
               Verbose.log((Object)("Failed to add token: " + var9));
            }
         }
      } while(var7);

      throw new WSSecurityException("Unable to add security token for identity");
   }

   private void processIntegrity(PolicyAlternative var1, SOAPMessageContext var2) throws SecurityPolicyException, WSSecurityException, MarshalException, PolicyException {
      Set var3 = var1.getAssertions(IntegrityAssertion.class);
      if (var3 != null && var3.size() != 0) {
         this.doIntegrity(var3, var2);
      }
   }

   private void doIntegrity(Set<IntegrityAssertion> var1, SOAPMessageContext var2) throws SecurityPolicyException, WSSecurityException, MarshalException, PolicyException {
      XMLSignatureFactory var3 = this.sbuilder.getXMLSignatureFactory();
      SigningReferencesFactory var4 = new SigningReferencesFactory(this.sbuilder);
      SigningPolicy var5 = new SigningPolicy(var3, var4, var2, var1);
      if (!var5.isX509AuthConditional() || this.policyIdToken != null && BSTUtils.isX509Type(this.policyIdToken.getTokenTypeUri())) {
         SignedInfo var6 = var5.getSignedInfo();
         List var7 = var5.getValidSignatureTokens();
         Node var8 = null;
         boolean var9 = var5.signedSecurityTokens();
         Iterator var10 = var7.iterator();

         while(var10.hasNext()) {
            SecurityToken var11 = (SecurityToken)var10.next();
            this.addClaimsToContextHandler(var11.getClaims());
            this.addDerivedFromToken(var11);
            if (var9) {
               Reference var12 = var4.newSigningTokenReference(var11, this.ctxHandler, var5.getDigestAlgorithm());
               if (var12 == null) {
                  continue;
               }

               var8 = this.sbuilder.addSignature(var5.newSignedInfo(var3, var12), var12, this.ctxHandler);
            } else {
               var8 = this.sbuilder.addSignature(var6, var11.getTokenTypeUri(), var11.getTokenIssuer(), var11.isIncludeInMessage(), this.ctxHandler);
            }

            if (var8 != null) {
               if (verbose) {
                  Verbose.log((Object)("Added Signature using " + var11));
               }
               break;
            }
         }

         if (var8 == null) {
            throw new WSSecurityException("Failed to add Signature.");
         }
      }
   }

   private void processConfidentiality(PolicyAlternative var1, SOAPMessageContext var2) throws WSSecurityException, MarshalException, XMLEncryptionException, PolicyException {
      Set var3 = var1.getAssertions(ConfidentialityAssertion.class);
      if (var3 != null && var3.size() != 0) {
         this.doConfidentiality(var3, var2);
      }
   }

   private void doConfidentiality(Set<ConfidentialityAssertion> var1, SOAPMessageContext var2) throws WSSecurityException, MarshalException, XMLEncryptionException, PolicyException {
      XMLEncryptionFactory var3 = this.sbuilder.getXMLEncryptionFactory();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         ConfidentialityAssertion var5 = (ConfidentialityAssertion)var4.next();
         EncryptionPolicy var6 = new EncryptionPolicy(var3, var2, var5);
         List var7 = var6.getValidEncryptionTokens();
         List var8 = var6.getEncryptionTargets();
         EncryptionMethod var9 = var6.getKeyWrapMethod();
         Iterator var10 = var8.iterator();

         while(var10.hasNext()) {
            EncryptionTarget var11 = (EncryptionTarget)var10.next();
            boolean var12 = false;
            Iterator var13 = var7.iterator();

            while(var13.hasNext()) {
               SecurityToken var14 = (SecurityToken)var13.next();
               this.addClaimsToContextHandler(var14.getClaims());
               this.addDerivedFromToken(var14);
               var12 = this.sbuilder.addEncryption(var11.getTBEs(), var9, var11.getEncryptionMethod(), var14.getTokenTypeUri(), var14.getTokenIssuer(), var14.isIncludeInMessage(), this.ctxHandler);
               if (var12) {
                  break;
               }
            }

            if (!var12) {
               throw new WSSecurityException("Failed to add Encryption.");
            }
         }
      }

   }

   private void addClaimsToContextHandler(Node var1) {
      this.ctxHandler.addContextElement("weblogic.xml.crypto.wss.policy.Claims", var1);
   }

   private void addDerivedFromToken(SecurityToken var1) throws MarshalException, WSSecurityException {
      String var2 = var1.getDerivedFromTokenType();
      if (var2 != null) {
         Object var3 = this.ctxHandler.getValue("weblogic.wsee.wsc.derived_from_token");
         if (var3 == null) {
            this.ctxHandler.addContextElement("weblogic.wsee.security.move_node_to_top", "true");
            this.ctxHandler.addContextElement("weblogic.wsee.wsc.derived_from_token", this.sbuilder.addSecurityToken(var2, (String)null, Purpose.SIGN, this.ctxHandler));
            this.ctxHandler.addContextElement("weblogic.wsee.security.move_node_to_top", "false");
         }
      }

   }
}
