package weblogic.wsee.reliability2.policy;

import com.sun.istack.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.policy.deployment.WsdlPolicySubject;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyMath;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.reliability.WsrmSecurityContext;
import weblogic.wsee.reliability.policy.RMAssertion;
import weblogic.wsee.reliability.policy11.RM11Assertion;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPort;

public class WsrmPolicyHelper {
   @Nullable
   private WsPort _port;

   public WsrmPolicyHelper(WsPort var1) {
      this._port = var1;
   }

   @Nullable
   private PolicyServer getPolicyServer() {
      return this._port == null ? null : this._port.getEndpoint().getService().getPolicyServer();
   }

   private Map getKnownPolicies() {
      PolicyServer var1 = this.getPolicyServer();
      return (Map)(var1 != null ? var1.getCachedPolicies() : new HashMap());
   }

   public boolean hasRMPolicy() throws PolicyException {
      if (this._port == null) {
         return false;
      } else {
         boolean var1 = false;
         Iterator var2 = this._port.getEndpoint().getMethods();

         while(var2.hasNext()) {
            WsMethod var3 = (WsMethod)var2.next();
            NormalizedExpression var4 = PolicyContext.getRequestEffectivePolicy(this._port, var3, this.getPolicyServer(), this.getKnownPolicies());
            if (hasRMPolicy(var4)) {
               var1 = true;
               break;
            }
         }

         return var1;
      }
   }

   public static boolean hasRMPolicy(NormalizedExpression var0) {
      return var0 != null && (var0.containsPolicyAssertion(RMAssertion.class) || var0.containsPolicyAssertion(RM11Assertion.class));
   }

   public WsrmSecurityContext createSecurityContext(PolicyAssertion var1) throws PolicyException {
      NormalizedExpression var2 = this.getEndpointPolicy();
      if (var2 == null) {
         throw new IllegalStateException("No RM policy, so can't create WsrmSecurityContext");
      } else {
         boolean var3 = false;
         if (var1 instanceof RM11Assertion) {
            var3 = ((RM11Assertion)var1).getSequenceTransportSecurity() != null;
         }

         return new WsrmSecurityContext(var2, var3);
      }
   }

   @Nullable
   public NormalizedExpression getEndpointPolicy() throws PolicyException {
      return this._port == null ? null : WsdlPolicySubject.getEndpointPolicySubject(this.getPolicyServer(), this._port.getWsdlPort(), this.getKnownPolicies());
   }

   @Nullable
   public NormalizedExpression getResponseEffectivePolicy(String var1) throws PolicyException {
      if (this._port == null) {
         return null;
      } else {
         WsMethod var2 = this._port.getEndpoint().getMethod(var1);
         if (var2 == null) {
            return WsdlPolicySubject.getEndpointPolicySubject(this.getPolicyServer(), this._port.getWsdlPort(), this.getKnownPolicies());
         } else {
            NormalizedExpression var3 = var2.getCachedEffectiveOutboundPolicy();
            QName var4 = var2.getOperationName();
            if (var3 == null) {
               WsdlPort var5 = this._port.getWsdlPort();
               WsdlOperation var6 = (WsdlOperation)var5.getPortType().getOperations().get(var4);
               WsdlBindingOperation var7 = (WsdlBindingOperation)var5.getBinding().getOperations().get(var4);
               var3 = getResponseEffectivePolicy(var5, var6, var7, this.getPolicyServer(), this.getKnownPolicies());
               var2.setCachedEffectiveOutboundPolicy(var3);
            }

            return var3;
         }
      }
   }

   private static NormalizedExpression getResponseEffectivePolicy(WsdlPort var0, WsdlOperation var1, WsdlBindingOperation var2, PolicyServer var3, Map var4) throws PolicyException {
      NormalizedExpression var5 = WsdlPolicySubject.getMessagePolicySubject(var3, var1.getOutput(), var2.getOutput(), var1.getOutputPolicyUris(), var4);
      var5 = PolicyMath.merge(var5, WsdlPolicySubject.getOperationPolicySubject(var3, var1, var2, var4));
      var5 = PolicyMath.merge(var5, WsdlPolicySubject.getEndpointPolicySubject(var3, var0, var4));
      return var5;
   }

   @Nullable
   public NormalizedExpression getRequestEffectivePolicy(String var1) throws PolicyException {
      if (this._port == null) {
         return null;
      } else {
         WsMethod var2 = this._port.getEndpoint().getMethod(var1);
         if (var2 == null) {
            return WsdlPolicySubject.getEndpointPolicySubject(this.getPolicyServer(), this._port.getWsdlPort(), this.getKnownPolicies());
         } else {
            NormalizedExpression var3 = var2.getCachedEffectiveInboundPolicy();
            QName var4 = var2.getOperationName();
            if (var3 == null) {
               WsdlPort var5 = this._port.getWsdlPort();
               WsdlOperation var6 = (WsdlOperation)var5.getPortType().getOperations().get(var4);
               WsdlBindingOperation var7 = (WsdlBindingOperation)var5.getBinding().getOperations().get(var4);
               var3 = getRequestEffectivePolicy(var5, var6, var7, this.getPolicyServer(), this.getKnownPolicies());
               var2.setCachedEffectiveInboundPolicy(var3);
            }

            return var3;
         }
      }
   }

   private static NormalizedExpression getRequestEffectivePolicy(WsdlPort var0, WsdlOperation var1, WsdlBindingOperation var2, PolicyServer var3, Map var4) throws PolicyException {
      NormalizedExpression var5 = WsdlPolicySubject.getMessagePolicySubject(var3, var1.getInput(), var2.getInput(), var1.getInputPolicyUris(), var4);
      var5 = PolicyMath.merge(var5, WsdlPolicySubject.getOperationPolicySubject(var3, var1, var2, var4));
      var5 = PolicyMath.merge(var5, WsdlPolicySubject.getEndpointPolicySubject(var3, var0, var4));
      return var5;
   }
}
