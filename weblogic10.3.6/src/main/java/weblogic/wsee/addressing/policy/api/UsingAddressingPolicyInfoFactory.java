package weblogic.wsee.addressing.policy.api;

import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.addressing.policy.internal.UsingAddressingPolicyInfoImpl;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;

public final class UsingAddressingPolicyInfoFactory {
   private static UsingAddressingPolicyInfoFactory factory = new UsingAddressingPolicyInfoFactory();

   private UsingAddressingPolicyInfoFactory() {
   }

   public static UsingAddressingPolicyInfoFactory getInstance() {
      return factory;
   }

   public UsingAddressingPolicyInfo getAddressigPolicyInfo(MessageContext var1) throws PolicyException {
      WlMessageContext var2 = null;

      try {
         var2 = (WlMessageContext)var1;
      } catch (ClassCastException var4) {
         throw new AssertionError(var4);
      }

      if (var2.getDispatcher() != null && var2.getDispatcher().getWsPort() != null && var2.getDispatcher().getWsPort().getEndpoint() != null && var2.getDispatcher().getWsPort().getEndpoint().getService() != null && var2.getDispatcher().getWsPort().getEndpoint().getService().getPolicyServer() != null) {
         NormalizedExpression var3 = PolicyContext.getRequestEffectivePolicy(var1);
         return var3 != null ? new UsingAddressingPolicyInfoImpl(var3) : new UsingAddressingPolicyInfoImpl();
      } else {
         return null;
      }
   }
}
