package weblogic.wsee.security.wssp.deploy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.handler.HandlerList;
import weblogic.wsee.mtom.internal.MtomXopClientHandler;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.security.wssp.handlers.PostWssClientPolicyHandler;
import weblogic.wsee.security.wssp.handlers.PreWssClientPolicyHandler;
import weblogic.wsee.security.wssp.handlers.WssClientHandler;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.init.WsDeploymentContext;

public class WssClientDeploymentListener extends WssDeploymentListener {
   ArrayList getPrecedingWssHandlers() {
      ArrayList var1 = new ArrayList();
      var1.add("CONNECTION_HANDLER");
      return var1;
   }

   ArrayList getFollowingWssHandlers() {
      ArrayList var1 = new ArrayList();
      var1.add("ADDRESSING_HANDLER");
      var1.add("POLICY_CLIENT_RT_HANDLER");
      var1.add("RELIABILITY_HANDLER");
      var1.add("ASYNC_HANDLER");
      return var1;
   }

   ArrayList getPrecedingPreWssPolicyHandlers() {
      ArrayList var1 = new ArrayList();
      var1.add("CODEC_HANDLER");
      return var1;
   }

   ArrayList getFollowingPreWssPolicyHandlers() {
      return new ArrayList();
   }

   List getPrecedingPostWssPolicyHandlers() {
      ArrayList var1 = new ArrayList();
      var1.add("CONNECTION_HANDLER");
      return var1;
   }

   List getFollowingPostWssPolicyHandlers(HandlerList var1) {
      ArrayList var2 = new ArrayList();
      var2.add("WS_SECURITY_1.1");
      if (var1.contains("SECURITY_HANDLER")) {
         var2.add("SECURITY_HANDLER");
      }

      return var2;
   }

   void insertWstHandlers(HandlerList var1, WsPort var2, PolicyServer var3, WsDeploymentContext var4) throws HandlerException {
   }

   void insertForwardingHandler(HandlerList var1) throws HandlerException {
   }

   HandlerInfo getWssHandlerInfo() {
      HashMap var1 = new HashMap();
      return new HandlerInfo(WssClientHandler.class, var1, (QName[])null);
   }

   HandlerInfo getPreWssPolicyHandlerInfo() {
      HashMap var1 = new HashMap();
      return new HandlerInfo(PreWssClientPolicyHandler.class, var1, (QName[])null);
   }

   HandlerInfo getPostWssPolicyHandlerInfo() {
      HashMap var1 = new HashMap();
      return new HandlerInfo(PostWssClientPolicyHandler.class, var1, (QName[])null);
   }

   List getFollowingXopHandlers() {
      ArrayList var1 = new ArrayList();
      var1.add("POST_WS_SECURITY_POLICY_1.2");
      return var1;
   }

   List getPrecedingXopHandlers() {
      return this.getPrecedingPostWssPolicyHandlers();
   }

   HandlerInfo getXopHandlerInfo() {
      return new HandlerInfo(MtomXopClientHandler.class, new HashMap(), (QName[])null);
   }
}
