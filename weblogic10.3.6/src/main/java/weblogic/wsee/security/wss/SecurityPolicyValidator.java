package weblogic.wsee.security.wss;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.security.configuration.TimestampConfiguration;
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
import weblogic.wsee.security.policy.assertions.SecurityPolicyAssertionFactory;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionFactory;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.SecurityImpl;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.SecurityValidator;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityException;

public class SecurityPolicyValidator {
   private static boolean VERBOSE;
   private SecurityValidator svalidator;
   private TimestampConfiguration timestampConfig;

   public SecurityPolicyValidator(SecurityValidator var1) {
      this.svalidator = var1;
   }

   public SecurityPolicyValidator(SecurityValidator var1, TimestampConfiguration var2) {
      this.svalidator = var1;
      this.timestampConfig = var2;
   }

   public void processInbound(PolicyAlternative var1, SOAPMessageContext var2) throws PolicyException, WSSecurityException, SecurityPolicyException, MarshalException, XMLEncryptionException {
      if (!this.svalidator.hasSecurity() && SecurityPolicyAssertionFactory.hasSecurityPolicy(var1)) {
         throw new WSSecurityException("No Security header in message but required by policy.");
      } else {
         this.processConfidentiality(var1, var2);
         this.processIntegrity(var1, var2);
         this.processIdentity(var1);
         this.processMessageAge(var1);
      }
   }

   private void processMessageAge(PolicyAlternative var1) throws WSSecurityException, PolicyException {
      Set var2 = var1.getAssertions(MessageAgeAssertion.class);
      if (var2.size() > 0) {
         this.doMessageAge(var2);
      }

   }

   private void doMessageAge(Set<MessageAgeAssertion> var1) throws WSSecurityException, PolicyException {
      if (var1.size() > 1) {
         throw new PolicyException("Only one MessageAge specification is supported in a policy alternative");
      } else {
         TimestampPolicy var2 = new TimestampPolicy((MessageAgeAssertion)var1.iterator().next());
         short var3 = var2.getMessageAgeSeconds();
         if (!this.svalidator.validateTimestamp(var3)) {
            throw new WSSecurityException("Timestamp validation failed.", WSSConstants.FAILURE_INVALID);
         }
      }
   }

   private void processIdentity(PolicyAlternative var1) throws WSSecurityException {
      this.doIdentity(var1);
   }

   private void doIdentity(PolicyAlternative var1) throws WSSecurityException {
      Set var2 = var1.getAssertions(IdentityAssertion.class);
      Iterator var3 = var2.iterator();

      boolean var4;
      do {
         if (!var3.hasNext()) {
            return;
         }

         var4 = false;
         IdentityAssertion var5 = (IdentityAssertion)var3.next();
         IdentityPolicy var6 = new IdentityPolicy(var5);
         List var7 = var6.getValidIdentityTokens();
         Iterator var8 = var7.iterator();

         while(var8.hasNext()) {
            SecurityToken var9 = (SecurityToken)var8.next();
            LogUtils.logWss("Trying to validate identity assertion token " + var9.getTokenTypeUri());
            if (this.svalidator.validateSecurityToken(var9.getTokenTypeUri(), var9.getTokenIssuer(), var9.getClaims())) {
               var4 = true;
               LogUtils.logWss("Validated identity assertion token " + var9.getTokenTypeUri());
            }
         }
      } while(var4);

      throw new WSSecurityException("Unable to validate identity assertions.", WSSConstants.FAILURE_INVALID);
   }

   private void processIntegrity(PolicyAlternative var1, SOAPMessageContext var2) throws SecurityPolicyException, WSSecurityException, PolicyException {
      Set var3 = var1.getAssertions(IntegrityAssertion.class);
      if (var3 != null && var3.size() != 0) {
         this.doIntegrity(var3, var2);
      }
   }

   private void doIntegrity(Set<IntegrityAssertion> var1, SOAPMessageContext var2) throws SecurityPolicyException, WSSecurityException, PolicyException {
      XMLSignatureFactory var3 = this.svalidator.getXMLSignatureFactory();
      SigningReferencesFactory var4 = new SigningReferencesFactory(this.svalidator);
      SigningPolicy var5 = new SigningPolicy(var3, var4, var2, var1);
      SignedInfo var6 = var5.getSignedInfo();
      List var7 = var5.getValidSignatureTokens();
      boolean var8 = false;
      boolean var9 = var5.signedSecurityTokens();
      Iterator var10 = var7.iterator();

      while(var10.hasNext()) {
         SecurityToken var11 = (SecurityToken)var10.next();
         if (var9) {
            SecurityTokenContextHandler var12 = new SecurityTokenContextHandler();
            var12.addContextElement("weblogic.xml.crypto.wss.policy.Claims", var11.getClaims());
            Reference var13 = var4.newSigningTokenReference(var11, var12, var5.getDigestAlgorithm());
            var8 = this.svalidator.validateSignature(var5.newSignedInfo(var3, var13), var11.getTokenTypeUri(), var11.getTokenIssuer(), var11.getClaims());
         } else {
            var8 = this.svalidator.validateSignature(var6, var11.getTokenTypeUri(), var11.getTokenIssuer(), var11.getClaims());
         }

         if (var8) {
            break;
         }
      }

      if (!var8) {
         throw new WSSecurityException("Could not validate signature using any of the supported token types", WSSConstants.FAILURE_INVALID);
      }
   }

   private void processConfidentiality(PolicyAlternative var1, SOAPMessageContext var2) throws WSSecurityException, PolicyException, XMLEncryptionException {
      Set var3 = var1.getAssertions(ConfidentialityAssertion.class);
      if (var3 != null && var3.size() != 0) {
         this.doConfidentiality(var3, var2);
      }
   }

   private void doConfidentiality(Set<ConfidentialityAssertion> var1, SOAPMessageContext var2) throws WSSecurityException, PolicyException, XMLEncryptionException {
      XMLEncryptionFactory var3 = this.svalidator.getXMLEncryptionFactory();
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
               var12 = this.svalidator.validateEncryption(var11.getTBEs(), var9, var11.getEncryptionMethod(), var14.getTokenTypeUri(), var14.getTokenIssuer(), var14.getClaims());
               if (var12) {
                  break;
               }
            }

            if (!var12) {
               throw new WSSecurityException("Could not validate encryption against any of the supported token types", WSSConstants.FAILURE_INVALID);
            }
         }
      }

   }

   static {
      VERBOSE = SecurityImpl.VERBOSE;
   }
}
