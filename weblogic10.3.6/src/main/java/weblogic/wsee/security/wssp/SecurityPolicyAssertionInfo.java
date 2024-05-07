package weblogic.wsee.security.wssp;

import java.util.List;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;

public interface SecurityPolicyAssertionInfo {
   void init(PolicyAlternative var1);

   boolean isMessageSecurityEnabled();

   TransportBindingInfo getTransportBindingInfo();

   SymmetricBindingInfo getSymmetricBindingInfo();

   AsymmetricBindingInfo getAsymmetricBindingInfo();

   SupportingTokensAssertion getSupportingTokensAssertion();

   WsTrustOptions getWsTrustOptions();

   NormalizedExpression getWsTrustBootstrapPolicy();

   Wss10Options getWss10Options();

   Wss11Options getWss11Options();

   List<IntegrityAssertion> getIntegrityAssertions();

   /** @deprecated */
   IntegrityAssertion getIntegrityAssertion();

   List<ConfidentialityAssertion> getConfidentialityAssertions();

   /** @deprecated */
   ConfidentialityAssertion getConfidentialityAssertion();

   List<ProtectionAssertion> getProtectionAssertions();

   AlgorithmSuiteInfo getAlgorithmSuiteInfo();

   boolean hasUnidentifiedAssertion();

   List getUnidentifiedAssertions();

   String getNamespaceUri();
}
