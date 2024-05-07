package weblogic.wsee.security.policy;

import com.bea.xml.XmlException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.runtime.PolicyCustomizer;
import weblogic.wsee.security.configuration.WssConfiguration;
import weblogic.wsee.security.configuration.WssConfigurationException;
import weblogic.wsee.security.policy.assertions.ConfidentialityAssertion;
import weblogic.wsee.security.policy.assertions.IdentityAssertion;
import weblogic.wsee.security.policy.assertions.IntegrityAssertion;
import weblogic.wsee.security.policy.assertions.MessageAgeAssertion;
import weblogic.wsee.security.policy.assertions.SecurityPolicyConstants;
import weblogic.wsee.security.policy.assertions.xbeans.KeyInfoType;
import weblogic.wsee.security.policy.assertions.xbeans.MessageAgeDocument;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.WSSecurityConfigurationException;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityTokenPolicyInfo;

public class SecurityPolicyCustomizer implements PolicyCustomizer {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicyCustomizer.class);
   private SecurityTokenPolicyInfo[] supportedTokenTypes = null;
   private WssConfiguration wssConfig;

   public SecurityPolicyCustomizer(WssConfiguration var1) {
      this.wssConfig = var1;
   }

   private void init() throws PolicyException {
      if (this.supportedTokenTypes == null) {
         try {
            List var1 = this.wssConfig.getSupprotedTokens();
            ArrayList var2 = new ArrayList();
            Iterator var3 = var1.iterator();

            while(var3.hasNext()) {
               Object var4 = var3.next();
               if (var4 instanceof SecurityTokenPolicyInfo) {
                  if (verbose) {
                     Verbose.log((Object)("Adding " + var4.getClass().getName() + " to customized list since it implements SecurityTokenPolicyInfo"));
                  }

                  var2.add(var4);
               }
            }

            this.supportedTokenTypes = (SecurityTokenPolicyInfo[])((SecurityTokenPolicyInfo[])var2.toArray(new SecurityTokenPolicyInfo[0]));
         } catch (WssConfigurationException var5) {
            throw new PolicyException(var5);
         }
      }

   }

   public void process(String var1, PolicyStatement var2) throws PolicyException {
      this.init();
      PolicyAlternative var3 = getPolicyAlternative(var1, var2);
      this.handleIdentity(var3);
      this.handleIntegrity(var3);
      this.handlConfidentiality(var3);
      this.handleMessageAge(var3);
   }

   public static boolean isSecurityPolicyAbstract(String var0, PolicyStatement var1) throws PolicyException {
      PolicyAlternative var2 = getPolicyAlternative(var0, var1);
      Set var3 = var2.getAssertions(IdentityAssertion.class);
      Iterator var4 = var3.iterator();

      IdentityAssertion var5;
      do {
         if (!var4.hasNext()) {
            var3 = var2.getAssertions(IntegrityAssertion.class);
            var4 = var3.iterator();

            IntegrityAssertion var7;
            do {
               if (!var4.hasNext()) {
                  var3 = var2.getAssertions(ConfidentialityAssertion.class);
                  var4 = var3.iterator();

                  ConfidentialityAssertion var8;
                  do {
                     if (!var4.hasNext()) {
                        var3 = var2.getAssertions(MessageAgeAssertion.class);
                        var4 = var3.iterator();

                        MessageAgeDocument.MessageAge var6;
                        do {
                           if (!var4.hasNext()) {
                              return false;
                           }

                           MessageAgeAssertion var9 = (MessageAgeAssertion)var4.next();
                           var6 = var9.getXbean().getMessageAge();
                        } while(!isMessageAgeAbstract(var6));

                        return true;
                     }

                     var8 = (ConfidentialityAssertion)var4.next();
                  } while(!isConfidentialityAbstract(var8));

                  return true;
               }

               var7 = (IntegrityAssertion)var4.next();
            } while(!isIntegrityAbstract(var7));

            return true;
         }

         var5 = (IdentityAssertion)var4.next();
      } while(!isIdentityAbstract(var5));

      return true;
   }

   private static PolicyAlternative getPolicyAlternative(String var0, PolicyStatement var1) throws PolicyException {
      PolicyAlternative var2 = var1.normalize().getPolicyAlternative();
      if (var2 == null) {
         throw new PolicyException("\"" + var0 + "\" does not contain any assertions");
      } else {
         return var2;
      }
   }

   public static boolean isIdentityAbstract(IdentityAssertion var0) {
      return var0.getXbean().getIdentity().getSupportedTokens() == null;
   }

   public static boolean isIntegrityAbstract(IntegrityAssertion var0) {
      return var0.getXbean().getIntegrity().getSupportedTokens() == null;
   }

   public static boolean isConfidentialityAbstract(ConfidentialityAssertion var0) {
      KeyInfoType var1 = var0.getXbean().getConfidentiality().getKeyInfo();
      if (var1 == null) {
         return true;
      } else {
         SecurityTokenType[] var2 = var1.getSecurityTokenArray();
         return var2 == null || var2.length == 0;
      }
   }

   public static boolean isMessageAgeAbstract(MessageAgeDocument.MessageAge var0) {
      return !var0.isSetAge();
   }

   private void handleIdentity(PolicyAlternative var1) throws PolicyException {
      Set var2 = var1.getAssertions(IdentityAssertion.class);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         IdentityAssertion var4 = (IdentityAssertion)var3.next();
         if (isIdentityAbstract(var4)) {
            Element var5 = IdentityAssertion.getElement(var4.getXbean());
            String var6 = DOMUtils.getPrefix("http://www.bea.com/wls90/security/policy", var5);
            Element var7 = DOMUtils.createAndAddElement(var5, SecurityPolicyConstants.SUPPORTED_TOKENS_QNAME, var6);
            this.handleAbstractAssertions(var7, Purpose.IDENTITY);
            var5.appendChild(var7);

            try {
               var4.load(var5);
            } catch (XmlException var9) {
               throw new PolicyException(var9);
            }
         }
      }

   }

   private void handleIntegrity(PolicyAlternative var1) throws PolicyException {
      Set var2 = var1.getAssertions(IntegrityAssertion.class);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         IntegrityAssertion var4 = (IntegrityAssertion)var3.next();
         if (isIntegrityAbstract(var4)) {
            Element var5 = IntegrityAssertion.getElement(var4.getXbean());
            String var6 = DOMUtils.getPrefix("http://www.bea.com/wls90/security/policy", var5);
            Element var7 = DOMUtils.createAndAddElement(var5, SecurityPolicyConstants.SUPPORTED_TOKENS_QNAME, var6);
            this.handleAbstractAssertions(var7, Purpose.SIGN);
            var5.appendChild(var7);

            try {
               var4.load(var5);
            } catch (XmlException var9) {
               throw new PolicyException(var9);
            }
         }
      }

   }

   private void handlConfidentiality(PolicyAlternative var1) throws PolicyException {
      Set var2 = var1.getAssertions(ConfidentialityAssertion.class);
      Iterator var3 = var2.iterator();

      while(true) {
         ConfidentialityAssertion var4;
         do {
            if (!var3.hasNext()) {
               return;
            }

            var4 = (ConfidentialityAssertion)var3.next();
         } while(!isConfidentialityAbstract(var4) && !this.isTrustEnable(var4));

         Element var5 = ConfidentialityAssertion.getElement(var4.getXbean());
         Element var6 = (Element)DOMUtils.findNode(var5, "KeyInfo");
         this.handleAbstractAssertions(var6, Purpose.ENCRYPT);

         try {
            var4.load(var5);
         } catch (XmlException var8) {
            throw new PolicyException(var8);
         }
      }
   }

   private void handleMessageAge(PolicyAlternative var1) {
      Set var2 = var1.getAssertions(MessageAgeAssertion.class);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         MessageAgeAssertion var4 = (MessageAgeAssertion)var3.next();
         MessageAgeDocument.MessageAge var5 = var4.getXbean().getMessageAge();
         if (isMessageAgeAbstract(var5)) {
            var5.setAge(BigInteger.valueOf((long)this.wssConfig.getTimestampConfig().getMessageAge()));
         }
      }

   }

   private Element handleAbstractAssertions(Element var1, Purpose var2) throws PolicyException {
      ArrayList var3 = this.getSupportedTokenTypes(var2);
      if (var3.size() > 0) {
         for(int var4 = 0; var4 < var3.size(); ++var4) {
            SecurityTokenPolicyInfo var5 = (SecurityTokenPolicyInfo)var3.get(var4);

            try {
               var5.getSecurityTokenAssertion(var1, var2, this.wssConfig.getContextHandler());
            } catch (WSSecurityConfigurationException var7) {
               throw new PolicyException("Failed to fill abstract token assertion.", var7);
            }
         }
      }

      return var1;
   }

   private ArrayList getSupportedTokenTypes(Purpose var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < this.supportedTokenTypes.length; ++var3) {
         if (this.supportedTokenTypes[var3].supports(var1)) {
            var2.add(this.supportedTokenTypes[var3]);
         }
      }

      return var2;
   }

   private boolean isTrustEnable(ConfidentialityAssertion var1) {
      return var1.getXbean().getConfidentiality().getSupportTrust10();
   }
}
