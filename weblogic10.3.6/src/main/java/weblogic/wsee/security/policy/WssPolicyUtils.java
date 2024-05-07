package weblogic.wsee.security.policy;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.policy.deployment.WsdlPolicySubject;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyMath;
import weblogic.wsee.security.configuration.WssConfigurationException;
import weblogic.wsee.security.policy.assertions.SecurityPolicyAssertionFactory;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.ws.dispatch.Dispatcher;

public final class WssPolicyUtils {
   private static final WssPolicyContext policyCtx = new WssPolicyContext();

   public static NormalizedExpression getPolicy(String var0) throws PolicyException {
      return getPolicy(policyCtx, var0);
   }

   public static NormalizedExpression getPolicy(WssPolicyContext var0, String var1) throws PolicyException {
      if (var0 == null) {
         var0 = policyCtx;
      }

      return var0.getPolicyServer().getPolicy(var1).normalize();
   }

   public static NormalizedExpression getPolicy(String[] var0) throws PolicyException {
      return getPolicy(policyCtx, var0);
   }

   public static NormalizedExpression getPolicy(WssPolicyContext var0, String[] var1) throws PolicyException {
      if (var0 == null) {
         var0 = policyCtx;
      }

      NormalizedExpression var2 = NormalizedExpression.createUnitializedExpression();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2 = PolicyMath.merge(var2, getPolicy(var0, var1[var3]));
      }

      return var2;
   }

   public static WssPolicyContext getContext() {
      return policyCtx;
   }

   public static List getCredentialProviders() throws WssConfigurationException {
      return policyCtx.getWssConfiguration().getCredentialProviders();
   }

   public static WsdlPolicySubject getWsdlPolicySubject(MessageContext var0) {
      WlMessageContext var1 = null;

      try {
         var1 = (WlMessageContext)var0;
      } catch (ClassCastException var4) {
         throw new AssertionError(var4);
      }

      Dispatcher var2 = var1.getDispatcher();
      if (var2 != null) {
         WsService var3 = var2.getWsPort().getEndpoint().getService();
         return new WsdlPolicySubject(var3.getWsdlService().getDefinitions(), var3.getPolicyServer());
      } else {
         return null;
      }
   }

   public static boolean hasSecurityPolicy(WsdlPolicySubject var0) throws PolicyException {
      if (var0 == null) {
         return false;
      } else {
         Map var1 = var0.getAllEffectivePolicies();
         Iterator var2 = var1.values().iterator();

         do {
            if (!var2.hasNext()) {
               return false;
            }
         } while(!SecurityPolicyAssertionFactory.hasSecurityPolicy((NormalizedExpression)var2.next()));

         return true;
      }
   }

   public static boolean hasSecurityPolicy(MessageContext var0) throws PolicyException {
      return hasSecurityPolicy(getWsdlPolicySubject(var0));
   }
}
