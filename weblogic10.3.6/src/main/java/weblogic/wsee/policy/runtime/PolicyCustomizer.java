package weblogic.wsee.policy.runtime;

import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyStatement;

public interface PolicyCustomizer {
   void process(String var1, PolicyStatement var2) throws PolicyException;
}
