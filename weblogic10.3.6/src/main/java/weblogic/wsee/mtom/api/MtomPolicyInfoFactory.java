package weblogic.wsee.mtom.api;

import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.mtom.internal.MtomPolicyInfoImpl;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;

public final class MtomPolicyInfoFactory {
   public static MtomPolicyInfo getInstance(MessageContext var0) throws PolicyException {
      NormalizedExpression var1 = PolicyContext.getRequestEffectivePolicy(var0);
      return var1 != null ? new MtomPolicyInfoImpl(var1) : new MtomPolicyInfoImpl();
   }
}
