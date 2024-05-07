package weblogic.wsee.mc.api;

import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.mc.internal.McPolicyInfoImpl;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;

public final class McPolicyInfoFactory {
   public static McPolicyInfo getInstance(MessageContext var0) throws PolicyException {
      return getInstance(PolicyContext.getRequestEffectivePolicy(var0));
   }

   public static McPolicyInfo getInstance(NormalizedExpression var0) throws PolicyException {
      return var0 != null ? new McPolicyInfoImpl(var0) : new McPolicyInfoImpl();
   }
}
