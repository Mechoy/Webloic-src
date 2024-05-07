package weblogic.wsee.jaxws.owsm;

import java.util.List;

public interface SecurityPolicyList {
   List<String> getSecurityPolicies() throws SecurityPolicyException;
}
