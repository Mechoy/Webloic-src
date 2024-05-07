package weblogic.wsee.policy.provider;

import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyStatement;

public interface PolicyValidationHandler {
   boolean validate(String var1, PolicyStatement var2) throws PolicyException;
}
