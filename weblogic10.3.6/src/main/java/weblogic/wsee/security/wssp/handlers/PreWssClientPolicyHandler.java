package weblogic.wsee.security.wssp.handlers;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.security.wss.SecurityPolicyException;
import weblogic.wsee.security.wssp.IntegrityAssertion;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfo;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfoFactory;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.wss.WSSecurityException;

public class PreWssClientPolicyHandler extends WssHandler {
   protected boolean processRequest(SOAPMessageContext var1) throws PolicyException, SOAPException, WSSecurityException {
      NormalizedExpression var2 = PolicyContext.getRequestEffectivePolicy(var1);
      if (var2 != null && null != var2.getPolicyAlternatives()) {
         Set var3 = var2.getPolicyAlternatives();
         if (var3 == null) {
            return true;
         } else {
            Iterator var4 = var3.iterator();

            while(true) {
               SecurityPolicyAssertionInfo var6;
               do {
                  if (!var4.hasNext()) {
                     return true;
                  }

                  PolicyAlternative var5 = (PolicyAlternative)var4.next();
                  var6 = SecurityPolicyAssertionInfoFactory.getSecurityPolicyAssertionInfo(var5);
               } while(var6 == null);

               List var7 = var6.getIntegrityAssertions();
               if (var7 != null) {
                  Iterator var9 = var7.iterator();

                  while(var9.hasNext()) {
                     IntegrityAssertion var8 = (IntegrityAssertion)var9.next();
                     if (var8 != null && var8.isSignedWsaHeadersRequired()) {
                        var1.setProperty("weblogic.wsee.complex", "true");
                     }

                     if (var8 != null && !"encrypt".equals(var1.getProperty("weblogic.wsee.xop.normal"))) {
                        var1.setProperty("weblogic.wsee.xop.normal", "sign");
                     }
                  }
               }

               List var10 = var6.getConfidentialityAssertions();
               if (var10 != null && var10.size() > 0) {
                  var1.setProperty("weblogic.wsee.xop.normal", "encrypt");
               }
            }
         }
      } else {
         return true;
      }
   }

   protected boolean processResponse(SOAPMessageContext var1) throws PolicyException, SOAPException, WSSecurityException {
      try {
         NormalizedExpression var2 = PolicyContext.getResponseEffectivePolicy(var1);
         if (var2 != null && var2.getPolicyAlternatives() != null) {
            this.postValidate(var2, var1, false);
         }

         return true;
      } catch (MarshalException var3) {
         throw new WSSecurityException(var3);
      } catch (SecurityPolicyException var4) {
         throw new WSSecurityException(var4);
      } catch (XMLEncryptionException var5) {
         throw new WSSecurityException(var5);
      }
   }
}
