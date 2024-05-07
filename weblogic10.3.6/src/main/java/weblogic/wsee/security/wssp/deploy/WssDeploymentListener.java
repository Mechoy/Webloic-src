package weblogic.wsee.security.wssp.deploy;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.handler.HandlerList;
import weblogic.wsee.policy.deployment.PolicyURIs;
import weblogic.wsee.policy.deployment.WsdlPolicySubject;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.security.configuration.WssConfigurationException;
import weblogic.wsee.security.policy.SecurityPolicyAssertionHelper;
import weblogic.wsee.security.policy.assertions.SecurityPolicyAssertionFactory;
import weblogic.wsee.security.policy12.assertions.TransportBinding;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfoFactory;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPort;

public abstract class WssDeploymentListener implements WsDeploymentListener {
   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      WsService var2 = var1.getWsService();
      PolicyServer var3 = var2.getPolicyServer();
      Iterator var4 = var2.getPorts();

      while(var4.hasNext()) {
         WsPort var5 = (WsPort)var4.next();
         HandlerList var6 = var5.getInternalHandlerList();

         try {
            if (var3 != null && var1.getType() == WebServiceType.JAXWS && SecurityPolicyAssertionFactory.hasSecurityPolicy(var5, var3)) {
               throw new WsDeploymentException("The WebLogic Server 9.x-style policy is not supported in JAX-WS web services.");
            }

            if (isWsspEnabled(var5, var3)) {
               checkLogicalError(var5, var3);
               this.insertHandlers(var6);
               var2.initWssConfiguration();
               if (isWSTEnabled(var5, var3)) {
                  this.insertWstHandlers(var6, var5, var3, var1);
                  this.insertForwardingHandler(var6);
               }
            } else {
               this.removeHandlers(var6);
            }
         } catch (WssConfigurationException var8) {
            throw new WsDeploymentException(var8);
         } catch (HandlerException var9) {
            throw new WsDeploymentException(var9);
         } catch (PolicyException var10) {
            throw new WsDeploymentException(var10);
         }
      }

   }

   private static boolean isWsspEnabled(WsPort var0, PolicyServer var1) throws PolicyException {
      if (var1 != null) {
         Iterator var2 = var0.getEndpoint().getMethods();

         while(var2.hasNext()) {
            WsMethod var3 = (WsMethod)var2.next();
            if (SecurityPolicyAssertionInfoFactory.hasSecurityPolicy(PolicyContext.getRequestEffectivePolicy(var0, var3, var1, var1.getCachedPolicies()))) {
               return true;
            }

            if (SecurityPolicyAssertionInfoFactory.hasSecurityPolicy(PolicyContext.getResponseEffectivePolicy(var0, var3, var1, var1.getCachedPolicies()))) {
               return true;
            }
         }
      }

      return false;
   }

   private static void checkLogicalError(WsPort var0, PolicyServer var1) throws PolicyException {
      if (var1 != null) {
         boolean var2 = false;
         boolean var3 = isHttpsEndpoint(var0, var1);
         Iterator var4 = var0.getEndpoint().getMethods();

         while(var4.hasNext()) {
            WsMethod var5 = (WsMethod)var4.next();
            if (!var3) {
               validateTransportBindingPolicy(var0, var5.getOperationName(), var1);
            }

            NormalizedExpression var6 = PolicyContext.getRequestEffectivePolicy(var0, var5, var1, var1.getCachedPolicies());
            if (SecurityPolicyAssertionFactory.hasSecurityPolicy(var6)) {
               var2 = true;
               break;
            }

            if (SecurityPolicyAssertionInfoFactory.hasSecurityPolicy(var6)) {
               SecurityPolicyAssertionHelper.checkLogicalError(var6);
            } else {
               SecurityPolicyAssertionHelper.checkLogicalError(PolicyContext.getResponseEffectivePolicy(var0, var5, var1, var1.getCachedPolicies()));
            }
         }

         if (var2) {
            throw new PolicyException("The WebLogic Server 9.x-style policy and Wssp policy is not allowed to mix and match.");
         }
      }

   }

   private static boolean isWSTEnabled(WsPort var0, PolicyServer var1) throws PolicyException {
      Iterator var2 = var0.getEndpoint().getMethods();

      WsMethod var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (WsMethod)var2.next();
         if (SecurityPolicyAssertionInfoFactory.hasWsTrustPolicy(PolicyContext.getRequestEffectivePolicy(var0, var3, var1, var1.getCachedPolicies()))) {
            return true;
         }
      } while(!SecurityPolicyAssertionInfoFactory.hasWsTrustPolicy(PolicyContext.getResponseEffectivePolicy(var0, var3, var1, var1.getCachedPolicies())));

      return true;
   }

   private static boolean isHttpsEndpoint(WsPort var0, PolicyServer var1) throws PolicyException {
      Map var2 = var1.getCachedPolicies();
      NormalizedExpression var3 = WsdlPolicySubject.getEndpointPolicySubject(var0.getWsdlPort(), var2);
      return var3.containsPolicyAssertion(TransportBinding.class);
   }

   private static void validateTransportBindingPolicy(WsPort var0, QName var1, PolicyServer var2) throws PolicyException {
      WsdlPort var3 = var0.getWsdlPort();
      WsdlOperation var4 = (WsdlOperation)var3.getPortType().getOperations().get(var1);
      WsdlBindingOperation var5 = (WsdlBindingOperation)var3.getBinding().getOperations().get(var1);
      Map var6 = var2.getCachedPolicies();
      String var7 = "Bad usage of Https Policy (TransportBinding assertion): It need be applied at Endpoint (Class) level  or at least another Https policy defined at the Endpoint level!\n NOTE: for JAX-RPC, any Https policy defined at the Endpoint level can NOT have the \"direction\" attribute definition!";
      NormalizedExpression var8 = WsdlPolicySubject.getOperationPolicySubject(var2, var4, var5, var6);
      if (var8.containsPolicyAssertion(TransportBinding.class)) {
         throw new PolicyException(var7);
      } else {
         var8 = WsdlPolicySubject.getMessagePolicySubject(var2, var4.getInput(), var5.getInput(), (PolicyURIs)null, var6);
         if (var8.containsPolicyAssertion(TransportBinding.class)) {
            throw new PolicyException(var7);
         } else {
            var8 = WsdlPolicySubject.getMessagePolicySubject(var2, var4.getOutput(), var5.getOutput(), (PolicyURIs)null, var6);
            if (var8.containsPolicyAssertion(TransportBinding.class)) {
               throw new PolicyException(var7);
            }
         }
      }
   }

   private void insertHandlers(HandlerList var1) throws HandlerException {
      if (!var1.contains("WS_SECURITY_1.1")) {
         var1.lenientInsert("WS_SECURITY_1.1", this.getWssHandlerInfo(), this.getFollowingWssHandlers(), this.getPrecedingWssHandlers());
         var1.lenientInsert("PRE_WS_SECURITY_POLICY_1.2", this.getPreWssPolicyHandlerInfo(), this.getFollowingPreWssPolicyHandlers(), this.getPrecedingPreWssPolicyHandlers());
         var1.insert("POST_WS_SECURITY_POLICY_1.2", this.getPostWssPolicyHandlerInfo(), this.getFollowingPostWssPolicyHandlers(var1), this.getPrecedingPostWssPolicyHandlers());
         var1.lenientInsert("NORMAL_XOP_HANDLER", this.getXopHandlerInfo(), this.getFollowingXopHandlers(), this.getPrecedingXopHandlers());
      }
   }

   private void removeHandlers(HandlerList var1) {
      var1.remove("WS_SECURITY_1.1");
      var1.remove("PRE_WS_SECURITY_POLICY_1.2");
      var1.remove("POST_WS_SECURITY_POLICY_1.2");
      var1.remove("NORMAL_XOP_HANDLER");
   }

   abstract HandlerInfo getWssHandlerInfo();

   abstract HandlerInfo getPreWssPolicyHandlerInfo();

   abstract HandlerInfo getPostWssPolicyHandlerInfo();

   abstract List getPrecedingWssHandlers();

   abstract List getFollowingWssHandlers();

   abstract List getPrecedingPreWssPolicyHandlers();

   abstract List getFollowingPreWssPolicyHandlers();

   abstract List getPrecedingPostWssPolicyHandlers();

   abstract List getFollowingPostWssPolicyHandlers(HandlerList var1);

   abstract void insertWstHandlers(HandlerList var1, WsPort var2, PolicyServer var3, WsDeploymentContext var4) throws HandlerException, PolicyException;

   abstract void insertForwardingHandler(HandlerList var1) throws HandlerException;

   abstract HandlerInfo getXopHandlerInfo();

   abstract List getPrecedingXopHandlers();

   abstract List getFollowingXopHandlers();
}
