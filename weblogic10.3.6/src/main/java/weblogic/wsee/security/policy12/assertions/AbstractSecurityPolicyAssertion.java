package weblogic.wsee.security.policy12.assertions;

import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.PolicyException;

public interface AbstractSecurityPolicyAssertion {
   void initialize(Element var1) throws PolicyException;
}
