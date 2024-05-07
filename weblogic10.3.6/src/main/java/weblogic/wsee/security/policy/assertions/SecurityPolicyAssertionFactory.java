package weblogic.wsee.security.policy.assertions;

import com.bea.xml.XmlException;
import java.util.Iterator;
import java.util.Map;
import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.ExternalizationUtils;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyAssertionFactory;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.security.policy.assertions.xbeans.ConfidentialityDocument;
import weblogic.wsee.security.policy.assertions.xbeans.IdentityDocument;
import weblogic.wsee.security.policy.assertions.xbeans.IntegrityDocument;
import weblogic.wsee.security.policy.assertions.xbeans.MessageAgeDocument;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsPort;

public class SecurityPolicyAssertionFactory extends PolicyAssertionFactory {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicyAssertionFactory.class);

   public PolicyAssertion createAssertion(Node var1) throws PolicyException {
      Map var2 = DOMUtils.getNamespaceMapping(var1);
      if (verbose) {
         Verbose.log((Object)("SecurityPolicyAssertionFactory.createAssertion(" + DOMUtils.getQNameOf(var1) + ")"));
      }

      try {
         return new IdentityAssertion(IdentityDocument.Factory.parse(var1));
      } catch (XmlException var7) {
         try {
            return new IntegrityAssertion(IntegrityDocument.Factory.parse(var1), var2);
         } catch (XmlException var6) {
            try {
               return new ConfidentialityAssertion(ConfidentialityDocument.Factory.parse(var1), var2);
            } catch (XmlException var5) {
               try {
                  return new MessageAgeAssertion(MessageAgeDocument.Factory.parse(var1));
               } catch (XmlException var4) {
                  return null;
               }
            }
         }
      }
   }

   /** @deprecated */
   public static boolean hasSecurityPolicy(WsPort var0, PolicyServer var1) throws PolicyException {
      boolean var2 = false;
      Iterator var3 = var0.getEndpoint().getMethods();

      while(var3.hasNext()) {
         WsMethod var4 = (WsMethod)var3.next();
         if (hasSecurityPolicy(PolicyContext.getRequestEffectivePolicy(var0, var4, var1, var1.getCachedPolicies()))) {
            var2 = true;
         }

         if (hasSecurityPolicy(PolicyContext.getResponseEffectivePolicy(var0, var4, var1, var1.getCachedPolicies()))) {
            var2 = true;
         }
      }

      return var2;
   }

   /** @deprecated */
   public static boolean hasSecurityPolicy(NormalizedExpression var0) {
      if (null == var0) {
         throw new IllegalArgumentException("Null policy found");
      } else {
         return var0.containsPolicyAssertion(MessageAgeAssertion.class) || var0.containsPolicyAssertion(IdentityAssertion.class) || var0.containsPolicyAssertion(IntegrityAssertion.class) || var0.containsPolicyAssertion(ConfidentialityAssertion.class);
      }
   }

   /** @deprecated */
   public static boolean hasSecurityPolicy(PolicyAlternative var0) {
      if (null == var0) {
         return false;
      } else {
         return !var0.getAssertions(MessageAgeAssertion.class).isEmpty() || !var0.getAssertions(IdentityAssertion.class).isEmpty() || !var0.getAssertions(IntegrityAssertion.class).isEmpty() || !var0.getAssertions(ConfidentialityAssertion.class).isEmpty();
      }
   }

   /** @deprecated */
   public static boolean isWSTEnabled(WsPort var0, PolicyServer var1) throws PolicyException {
      boolean var2 = false;
      Iterator var3 = var0.getEndpoint().getMethods();

      while(var3.hasNext()) {
         WsMethod var4 = (WsMethod)var3.next();
         if (isWSTEnabled(PolicyContext.getRequestEffectivePolicy(var0, var4, var1, var1.getCachedPolicies()))) {
            var2 = true;
         }

         if (isWSTEnabled(PolicyContext.getResponseEffectivePolicy(var0, var4, var1, var1.getCachedPolicies()))) {
            var2 = true;
         }
      }

      return var2;
   }

   /** @deprecated */
   public static boolean isWSTEnabled(NormalizedExpression var0) {
      IntegrityAssertion var1 = (IntegrityAssertion)var0.getPolicyAssertion(IntegrityAssertion.class);
      if (var1 != null) {
         IntegrityDocument.Integrity var2 = var1.getXbean().getIntegrity();
         if (var2.getSupportTrust10()) {
            return true;
         }
      }

      ConfidentialityAssertion var4 = (ConfidentialityAssertion)var0.getPolicyAssertion(ConfidentialityAssertion.class);
      if (var4 != null) {
         ConfidentialityDocument.Confidentiality var3 = var4.getXbean().getConfidentiality();
         if (var3.getSupportTrust10()) {
            return true;
         }
      }

      return false;
   }

   static {
      ExternalizationUtils.registerExternalizable(ConfidentialityAssertion.NAME, ConfidentialityAssertion.class.getName());
      ExternalizationUtils.registerExternalizable(IdentityAssertion.NAME, IdentityAssertion.class.getName());
      ExternalizationUtils.registerExternalizable(IntegrityAssertion.NAME, IntegrityAssertion.class.getName());
      ExternalizationUtils.registerExternalizable(MessageAgeAssertion.NAME, MessageAgeAssertion.class.getName());
   }
}
