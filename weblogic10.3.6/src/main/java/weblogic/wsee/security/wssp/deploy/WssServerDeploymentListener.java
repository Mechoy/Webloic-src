package weblogic.wsee.security.wssp.deploy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.cluster.ForwardingHandler;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.handler.HandlerList;
import weblogic.wsee.jaxws.tubeline.DelegateTubelineDeploymentListener;
import weblogic.wsee.mtom.internal.MtomXopServerHandler;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.security.wssc.utils.WSSCCompatibilityUtil;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfo;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfoFactory;
import weblogic.wsee.security.wssp.handlers.PostWssServerPolicyHandler;
import weblogic.wsee.security.wssp.handlers.PreWssServerPolicyHandler;
import weblogic.wsee.security.wssp.handlers.STSMessageHandler;
import weblogic.wsee.security.wssp.handlers.STSPolicyHandler;
import weblogic.wsee.security.wssp.handlers.WSTHeuristicHandler;
import weblogic.wsee.security.wssp.handlers.WssServerHandler;
import weblogic.wsee.security.wssp.tube.WSTHeuristicServerTubelineDeploymentListener;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.init.WsDeploymentContext;

public class WssServerDeploymentListener extends WssDeploymentListener {
   void insertWstHandlers(HandlerList var1, WsPort var2, PolicyServer var3, WsDeploymentContext var4) throws HandlerException, PolicyException {
      if (!var1.contains("WS_TRUST_POLICY_HANDLER")) {
         int var5 = var1.lenientInsert("WS_TRUST_POLICY_HANDLER", this.getWstPolicyHandlerInfo(var2, var3), this.getPrecedingWstPolicyHandlers(), this.getFollowingWstPolicyHandlers());
         var1.insert("WS_TRUST_STS_HANDLER", this.getWstMessageHandlerInfo(), this.getPrecedingWstMessageHandlers(), this.getFollowingWstMessageHandlers());
         if (var4.getType() == WebServiceType.JAXWS) {
            if (WSSCCompatibilityUtil.isHeuristicCompatibility()) {
               DelegateTubelineDeploymentListener.registerServerDeploymentListener(new WSTHeuristicServerTubelineDeploymentListener());
            }
         } else if (var4.getType() == WebServiceType.JAXRPC) {
            HandlerInfo var6 = new HandlerInfo(WSTHeuristicHandler.class, (Map)null, (QName[])null);
            var1.insert("WS_TRUST_HEURISTIC_HANDLER", var5, var6);
         }

      }
   }

   void insertForwardingHandler(HandlerList var1) throws HandlerException {
      if (!var1.contains("FORWARDING_HANDLER")) {
         var1.lenientInsert("FORWARDING_HANDLER", this.getForwardingHandlerInfo(), this.getFollowingForwardingHandlers(), this.getPrecedingForwardingHandlers());
      }
   }

   private HandlerInfo getForwardingHandlerInfo() {
      return new HandlerInfo(ForwardingHandler.class, new HashMap(), (QName[])null);
   }

   HandlerInfo getWssHandlerInfo() {
      HashMap var1 = new HashMap();
      return new HandlerInfo(WssServerHandler.class, var1, (QName[])null);
   }

   HandlerInfo getPreWssPolicyHandlerInfo() {
      HashMap var1 = new HashMap();
      return new HandlerInfo(PreWssServerPolicyHandler.class, var1, (QName[])null);
   }

   HandlerInfo getPostWssPolicyHandlerInfo() {
      HashMap var1 = new HashMap();
      return new HandlerInfo(PostWssServerPolicyHandler.class, var1, (QName[])null);
   }

   List getPrecedingWssHandlers() {
      ArrayList var1 = new ArrayList();
      return var1;
   }

   List getFollowingWssHandlers() {
      ArrayList var1 = new ArrayList();
      var1.add("CONNECTION_HANDLER");
      var1.add("FORWARDING_HANDLER");
      return var1;
   }

   List getPrecedingPostWssPolicyHandlers() {
      ArrayList var1 = new ArrayList();
      var1.add("PRE_INVOKE_HANDLER");
      return var1;
   }

   List getFollowingPostWssPolicyHandlers(HandlerList var1) {
      ArrayList var2 = new ArrayList();
      var2.add("OPERATION_LOOKUP_HANDLER");
      return var2;
   }

   ArrayList getPrecedingPreWssPolicyHandlers() {
      ArrayList var1 = new ArrayList();
      var1.add("WS_SECURITY_1.1");
      var1.add("SECURITY_HANDLER");
      return var1;
   }

   ArrayList getFollowingPreWssPolicyHandlers() {
      ArrayList var1 = new ArrayList();
      var1.add("CONNECTION_HANDLER");
      var1.add("FORWARDING_HANDLER");
      return var1;
   }

   List getFollowingForwardingHandlers() {
      ArrayList var1 = new ArrayList();
      var1.add("CONNECTION_HANDLER");
      return var1;
   }

   ArrayList getPrecedingForwardingHandlers() {
      ArrayList var1 = new ArrayList();
      var1.add("WS_SECURITY_1.1");
      var1.add("SECURITY_HANDLER");
      return var1;
   }

   private HandlerInfo getWstMessageHandlerInfo() {
      return new HandlerInfo(STSMessageHandler.class, new HashMap(), (QName[])null);
   }

   private List getPrecedingWstMessageHandlers() {
      return Arrays.asList((Object[])(new String[]{"ADDRESSING_HANDLER", "WS_SECURITY_1.1"}));
   }

   private List getFollowingWstMessageHandlers() {
      return Arrays.asList((Object[])(new String[]{"OPERATION_LOOKUP_HANDLER"}));
   }

   private HandlerInfo getWstPolicyHandlerInfo(WsPort var1, PolicyServer var2) throws PolicyException {
      NormalizedExpression var3 = null;
      NormalizedExpression var4 = null;
      NormalizedExpression var5 = PolicyContext.getRequestEffectivePolicy((WsPort)var1, (WsMethod)null, var2, var2.getCachedPolicies());
      Set var6 = var5.getPolicyAlternatives();
      if (var6 != null) {
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            PolicyAlternative var8 = (PolicyAlternative)var7.next();
            SecurityPolicyAssertionInfo var9 = SecurityPolicyAssertionInfoFactory.getSecurityPolicyAssertionInfo(var8);
            if (var9 != null && (var3 = var9.getWsTrustBootstrapPolicy()) != null) {
               var4 = var5;
               break;
            }
         }
      }

      if (var3 == null) {
         throw new PolicyException("Can not determine boot strap policy for WS-Trust. Either there is no boot strap policy or it is attached at method/operation level. The boot strap policy must be attached at service/port level, it can't be determined if it is attached at method/operation level.");
      } else {
         HashMap var10 = new HashMap();
         var10.put("BootstrapPolicy", var3);
         var10.put("ServicePolicy", var4);
         return new HandlerInfo(STSPolicyHandler.class, var10, (QName[])null);
      }
   }

   private List getPrecedingWstPolicyHandlers() {
      return Arrays.asList((Object[])(new String[]{"CONNECTION_HANDLER", "FORWARDING_HANDLER"}));
   }

   private List getFollowingWstPolicyHandlers() {
      return Arrays.asList((Object[])(new String[]{"WS_SECURITY_1.1"}));
   }

   List getFollowingXopHandlers() {
      return this.getFollowingPreWssPolicyHandlers();
   }

   List getPrecedingXopHandlers() {
      ArrayList var1 = new ArrayList();
      var1.add("PRE_WS_SECURITY_POLICY_1.2");
      return var1;
   }

   HandlerInfo getXopHandlerInfo() {
      return new HandlerInfo(MtomXopServerHandler.class, new HashMap(), (QName[])null);
   }
}
