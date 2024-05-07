package weblogic.wsee.policy.runtime;

import com.sun.xml.ws.api.pipe.Fiber;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.policy.deployment.WsdlPolicySubject;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyMath;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.dispatch.Dispatcher;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPort;

public class PolicyContext {
   public static final String EFFECTIVE_REQ_POLICY = "weblogic.wsee.policy.effectiveRequestPolicy";
   public static final String EFFECTIVE_RES_POLICY = "weblogic.wsee.policy.effectiveResponsePolicy";
   private static final String IGNORE_POLICY = "weblogic.wsee.policy.ignorePolicy";
   private static ThreadLocal<Boolean> ignoringStatus = null;
   private static NormalizedExpression empty = new NormalizedExpression();

   private PolicyContext() {
   }

   public static NormalizedExpression getRequestEffectivePolicy(WsPort var0, WsMethod var1, PolicyServer var2, Map var3) throws PolicyException {
      if (isPolicyIgnored()) {
         return empty;
      } else if (var1 == null) {
         return WsdlPolicySubject.getEndpointPolicySubject(var2, var0.getWsdlPort(), var3);
      } else {
         NormalizedExpression var4 = var1.getCachedEffectiveInboundPolicy();
         QName var5 = var1.getOperationName();
         if (var4 == null) {
            WsdlPort var6 = var0.getWsdlPort();
            WsdlOperation var7 = (WsdlOperation)var6.getPortType().getOperations().get(var5);
            WsdlBindingOperation var8 = (WsdlBindingOperation)var6.getBinding().getOperations().get(var5);
            var4 = getRequestEffectivePolicy(var6, var7, var8, var2, var3);
            var1.setCachedEffectiveInboundPolicy(var4);
         }

         return var4;
      }
   }

   public static NormalizedExpression getRequestEffectivePolicy(WsdlPort var0, WsdlOperation var1, WsdlBindingOperation var2, PolicyServer var3, Map var4) throws PolicyException {
      if (isPolicyIgnored()) {
         return empty;
      } else {
         NormalizedExpression var5 = null;
         var5 = WsdlPolicySubject.getMessagePolicySubject(var3, var1.getInput(), var2.getInput(), var1.getInputPolicyUris(), var4);
         var5 = PolicyMath.merge(var5, WsdlPolicySubject.getOperationPolicySubject(var3, var1, var2, var4));
         var5 = PolicyMath.merge(var5, WsdlPolicySubject.getEndpointPolicySubject(var3, var0, var4));
         return var5;
      }
   }

   public static NormalizedExpression getRequestEffectivePolicy(WsdlOperation var0, WsdlBindingOperation var1, PolicyServer var2, Map var3) throws PolicyException {
      if (isPolicyIgnored()) {
         return empty;
      } else {
         NormalizedExpression var4 = null;
         var4 = WsdlPolicySubject.getMessagePolicySubject(var2, var0.getInput(), var1.getInput(), var0.getInputPolicyUris(), var3);
         var4 = PolicyMath.merge(var4, WsdlPolicySubject.getOperationPolicySubject(var2, var0, var1, var3));
         WsdlBinding var5 = var1.getBinding();
         var4 = PolicyMath.merge(var4, WsdlPolicySubject.getEndpointPolicySubject(var2, var5, var3));
         return var4;
      }
   }

   public static NormalizedExpression getResponseEffectivePolicy(WsPort var0, WsMethod var1, PolicyServer var2, Map var3) throws PolicyException {
      if (isPolicyIgnored()) {
         return empty;
      } else if (var1 == null) {
         return WsdlPolicySubject.getEndpointPolicySubject(var2, var0.getWsdlPort(), var3);
      } else {
         NormalizedExpression var4 = var1.getCachedEffectiveOutboundPolicy();
         QName var5 = var1.getOperationName();
         if (var4 == null) {
            WsdlPort var6 = var0.getWsdlPort();
            WsdlOperation var7 = (WsdlOperation)var6.getPortType().getOperations().get(var5);
            WsdlBindingOperation var8 = (WsdlBindingOperation)var6.getBinding().getOperations().get(var5);
            var4 = getResponseEffectivePolicy(var6, var7, var8, var2, var3);
            var1.setCachedEffectiveOutboundPolicy(var4);
         }

         return var4;
      }
   }

   public static NormalizedExpression getResponseEffectivePolicy(WsdlPort var0, WsdlOperation var1, WsdlBindingOperation var2, PolicyServer var3, Map var4) throws PolicyException {
      if (isPolicyIgnored()) {
         return empty;
      } else {
         NormalizedExpression var5 = null;
         var5 = WsdlPolicySubject.getMessagePolicySubject(var3, var1.getOutput(), var2.getOutput(), var1.getOutputPolicyUris(), var4);
         var5 = PolicyMath.merge(var5, WsdlPolicySubject.getOperationPolicySubject(var3, var1, var2, var4));
         var5 = PolicyMath.merge(var5, WsdlPolicySubject.getEndpointPolicySubject(var3, var0, var4));
         return var5;
      }
   }

   public static NormalizedExpression getResponseEffectivePolicy(WsdlOperation var0, WsdlBindingOperation var1, PolicyServer var2, Map var3) throws PolicyException {
      if (isPolicyIgnored()) {
         return empty;
      } else {
         NormalizedExpression var4 = null;
         var4 = WsdlPolicySubject.getMessagePolicySubject(var2, var0.getOutput(), var1.getOutput(), var0.getOutputPolicyUris(), var3);
         var4 = PolicyMath.merge(var4, WsdlPolicySubject.getOperationPolicySubject(var2, var0, var1, var3));
         WsdlBinding var5 = var1.getBinding();
         var4 = PolicyMath.merge(var4, WsdlPolicySubject.getEndpointPolicySubject(var2, var5, var3));
         return var4;
      }
   }

   public static NormalizedExpression getRequestEffectivePolicy(MessageContext var0) throws PolicyException {
      if (isPolicyIgnored()) {
         return empty;
      } else {
         Object var1 = var0.getProperty("weblogic.wsee.policy.effectiveRequestPolicy");
         if (var1 != null) {
            assert var1 instanceof NormalizedExpression;

            return (NormalizedExpression)var1;
         } else {
            Dispatcher var2 = getDispatcher(var0);
            return getRequestEffectivePolicy((WsPort)var2.getWsPort(), (WsMethod)var2.getWsMethod(), (PolicyServer)null, var2.getWsPort().getEndpoint().getService().getPolicyServer().getCachedPolicies());
         }
      }
   }

   public static NormalizedExpression getResponseEffectivePolicy(MessageContext var0) throws PolicyException {
      if (isPolicyIgnored()) {
         return empty;
      } else {
         Object var1 = var0.getProperty("weblogic.wsee.policy.effectiveResponsePolicy");
         if (var1 != null) {
            assert var1 instanceof NormalizedExpression;

            return (NormalizedExpression)var1;
         } else {
            Dispatcher var2 = getDispatcher(var0);
            return getResponseEffectivePolicy((WsPort)var2.getWsPort(), (WsMethod)var2.getWsMethod(), (PolicyServer)null, var2.getWsPort().getEndpoint().getService().getPolicyServer().getCachedPolicies());
         }
      }
   }

   public static NormalizedExpression getEndpointPolicy(MessageContext var0) throws PolicyException {
      if (isPolicyIgnored()) {
         return empty;
      } else {
         Dispatcher var1 = getDispatcher(var0);
         if (var1 == null) {
            return empty;
         } else {
            WsPort var2 = var1.getWsPort();
            if (var2 == null) {
               throw new PolicyException("WsPort can not be null");
            } else {
               PolicyServer var3 = var2.getEndpoint().getService().getPolicyServer();
               return WsdlPolicySubject.getEndpointPolicySubject(var3, var2.getWsdlPort(), var3.getCachedPolicies());
            }
         }
      }
   }

   private static Dispatcher getDispatcher(MessageContext var0) {
      WlMessageContext var1 = null;

      try {
         var1 = (WlMessageContext)var0;
      } catch (ClassCastException var3) {
         throw new AssertionError(var3);
      }

      return var1.getDispatcher();
   }

   public static boolean isPolicyIgnored() {
      Fiber var0 = Fiber.getCurrentIfSet();
      if (var0 == null) {
         return ignoringStatus == null ? false : (Boolean)ignoringStatus.get();
      } else {
         Boolean var1 = (Boolean)var0.getPacket().invocationProperties.get("weblogic.wsee.policy.ignorePolicy");
         return var1 != null ? var1 : false;
      }
   }

   public static void setPolicyIgnored(boolean var0) {
      Fiber var1 = Fiber.getCurrentIfSet();
      if (var1 == null) {
         if (ignoringStatus == null) {
            synchronized(empty) {
               if (ignoringStatus == null) {
                  ignoringStatus = new ThreadLocal<Boolean>() {
                     protected Boolean initialValue() {
                        return Boolean.FALSE;
                     }
                  };
               }
            }
         }

         ignoringStatus.set(var0);
      } else {
         var1.getPacket().invocationProperties.put("weblogic.wsee.policy.ignorePolicy", var0);
      }

   }
}
