package weblogic.wsee.security.policy.assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.handler.HandlerList;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.provider.ServiceConfigurationHandler;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.security.configuration.WssConfigurationException;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.ws.init.WsDeploymentContext;

public class WssServiceConfigHandler implements ServiceConfigurationHandler {
   public void process(WsDeploymentContext var1) throws PolicyException {
      this.deployWssHandlers(var1);
   }

   private void deployWssHandlers(WsDeploymentContext var1) throws PolicyException {
      WsService var2 = var1.getWsService();
      boolean var3 = var2.isUsingPolicy();
      if (var3) {
         PolicyServer var4 = var2.getPolicyServer();
         Iterator var5 = var2.getPorts();

         while(var5.hasNext()) {
            WsPort var6 = (WsPort)var5.next();
            HandlerList var7 = var6.getInternalHandlerList();
            if (SecurityPolicyAssertionFactory.hasSecurityPolicy(var6, var4)) {
               this.insertWssHandlers(var7);

               try {
                  var2.initWssConfiguration();
               } catch (WssConfigurationException var9) {
                  throw new PolicyException(var9);
               }
            }

            if (SecurityPolicyAssertionFactory.isWSTEnabled(var6, var4)) {
               this.insertSTSPolicyHandler(var7);
               this.insertSTSMessageHandler(var7);
            }
         }

      }
   }

   private void insertSTSMessageHandler(HandlerList var1) throws PolicyException {
      List var2 = Arrays.asList((Object[])(new String[]{"ADDRESSING_HANDLER"}));
      List var3 = Arrays.asList((Object[])(new String[]{"OPERATION_LOOKUP_HANDLER"}));

      try {
         HandlerInfo var4 = getWstHandlerInfo();
         var1.insert("WS_TRUST_STS_HANDLER", var4, var2, var3);
      } catch (HandlerException var5) {
         throw new PolicyException("Failed to deploy WSTServerHandler: " + var5.getMessage());
      }
   }

   private void insertSTSPolicyHandler(HandlerList var1) throws PolicyException {
      List var2 = Arrays.asList((Object[])(new String[]{"CONNECTION_HANDLER"}));
      List var3 = Arrays.asList((Object[])(new String[]{"SECURITY_HANDLER"}));

      try {
         HandlerInfo var4 = getStsPolicyHandlerInfo();
         var1.insert("WS_TRUST_POLICY_HANDLER", var4, var2, var3);
      } catch (HandlerException var5) {
         throw new PolicyException("Failed to deploy WSTServerHandler: " + var5.getMessage());
      }
   }

   private void insertWssHandlers(HandlerList var1) throws PolicyException {
      ArrayList var2 = new ArrayList();
      List var3 = Arrays.asList((Object[])(new String[]{"CONNECTION_HANDLER", "FORWARDING_HANDLER"}));
      List var4 = Arrays.asList((Object[])(new String[]{"OPERATION_LOOKUP_HANDLER", "FORWARDING_HANDLER"}));
      HandlerInfo var5 = this.getWssHandlerInfo();
      HandlerInfo var6 = this.getPolicyHandlerInfo();

      try {
         var1.lenientInsert("SECURITY_HANDLER", var5, var3, var2);
         var1.lenientInsert("SECURITY_SERVER_POLICY_HANDLER", var6, var4, var2);
      } catch (HandlerException var8) {
         throw new PolicyException(var8);
      }
   }

   private HandlerInfo getWssHandlerInfo() throws PolicyException {
      HashMap var1 = new HashMap();

      try {
         return new HandlerInfo(Class.forName("weblogic.wsee.security.WssServerHandler"), var1, (QName[])null);
      } catch (ClassNotFoundException var3) {
         throw new PolicyException(var3);
      }
   }

   private HandlerInfo getPolicyHandlerInfo() throws PolicyException {
      HashMap var1 = new HashMap();

      try {
         return new HandlerInfo(Class.forName("weblogic.wsee.security.WssServerPolicyHandler"), var1, (QName[])null);
      } catch (ClassNotFoundException var3) {
         throw new PolicyException(var3);
      }
   }

   private static final HandlerInfo getWstHandlerInfo() throws PolicyException {
      HashMap var0 = new HashMap();

      try {
         return new HandlerInfo(Class.forName("weblogic.wsee.security.wst.internal.STSMessageHandler"), var0, (QName[])null);
      } catch (ClassNotFoundException var2) {
         throw new PolicyException(var2);
      }
   }

   private static final HandlerInfo getStsPolicyHandlerInfo() throws PolicyException {
      HashMap var0 = new HashMap();

      try {
         return new HandlerInfo(Class.forName("weblogic.wsee.security.wst.internal.STSPolicyHandler"), var0, (QName[])null);
      } catch (ClassNotFoundException var2) {
         throw new PolicyException(var2);
      }
   }
}
