package weblogic.wsee.reliability.policy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.messaging.saf.internal.SAFManagerImpl;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.buffer.BufferManager;
import weblogic.wsee.cluster.ForwardingHandler;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.handler.HandlerList;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.provider.ServiceConfigurationHandler;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.reliability.WsrmEndpointManager;
import weblogic.wsee.reliability.WsrmSAFEndpoint;
import weblogic.wsee.reliability.WsrmServerConfigHandler;
import weblogic.wsee.reliability.WsrmServerHandler;
import weblogic.wsee.reliability.handshake.WsrmServerHandshakeHandler;
import weblogic.wsee.server.ServerUtil;
import weblogic.wsee.server.jms.WsDispatchMessageListener;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;

public class WsrmServiceConfigHandler implements ServiceConfigurationHandler {
   private static final boolean verbose = Verbose.isVerbose(WsrmServiceConfigHandler.class);
   private static final List BEFORE_CONFIG = Arrays.asList((Object[])(new String[]{"WSRM_HANDSHAKE_HANDLER"}));
   private static final List AFTER_CONFIG = Arrays.asList((Object[])(new String[]{"ADDRESSING_HANDLER", "SECURITY_HANDLER", "FORWARDING_HANDLER"}));
   private static final List BEFORE_HANDSHAKE = Arrays.asList((Object[])(new String[]{"OPERATION_LOOKUP_HANDLER"}));
   private static final List AFTER_HANDSHAKE = Arrays.asList((Object[])(new String[]{"ADDRESSING_HANDLER", "SECURITY_HANDLER", "FORWARDING_HANDLER"}));
   private static final List BEFORE_RELIABILITY = Arrays.asList((Object[])(new String[]{"ONE_WAY_HANDLER"}));
   private static final List AFTER_RELIABILITY = Arrays.asList((Object[])(new String[]{"OPERATION_LOOKUP_HANDLER", "SECURITY_SERVER_POLICY_HANDLER"}));
   private static final List BEFORE_FORWARDING = Arrays.asList((Object[])(new String[]{"SECURITY_HANDLER", "SECURITY_SERVER_POLICY_HANDLER", "RELIABILITY_HANDLER", "ADDRESSING_HANDLER"}));
   private static final List AFTER_FORWARDING = Arrays.asList((Object[])(new String[]{"CONNECTION_HANDLER"}));

   public void process(WsDeploymentContext var1) throws PolicyException {
      WsService var2 = var1.getWsService();
      WsrmEndpointManager var3 = (WsrmEndpointManager)SAFManagerImpl.getManager().getEndpointManager(2);

      assert var3 != null;

      PolicyServer var4 = var2.getPolicyServer();
      Iterator var5 = var2.getPorts();

      while(true) {
         WsPort var6;
         do {
            if (!var5.hasNext()) {
               return;
            }

            var6 = (WsPort)var5.next();
         } while(!ReliabilityPolicyAssertionsFactory.hasRMPolicy(var6, var4, var4.getCachedPolicies()));

         if (verbose) {
            Verbose.log((Object)("WSRM enabled for port " + var6.getWsdlPort().getName()));
         }

         this.insertHandlers(var6);
         Class var7 = var6.getEndpoint().getJwsClass();
         ServerUtil.QueueInfo var8 = ServerUtil.getBufferQueueInfo(var7);
         StringBuffer var9 = new StringBuffer();
         WsDispatchMessageListener var10 = new WsDispatchMessageListener(var6);
         boolean var11 = true;

         for(int var12 = 0; var12 < var1.getServiceURIs().length; ++var12) {
            String var13 = var1.getContextPath();
            String var14 = var1.getServiceURIs()[var12];
            String var15 = AsyncUtil.calculateServiceTargetURI(var13, var14);
            BufferManager var16 = BufferManager.instance();
            synchronized(var16) {
               if (var16.getMessageListener(var15) != null) {
                  var11 = false;
                  continue;
               }

               var16.addMessageListener(var15, var10);
            }

            var1.addBufferTargetURI(var15);
            var16.setTargetQueue(var15, var8);
            WsrmSAFEndpoint var17 = new WsrmSAFEndpoint(var15);
            if (verbose) {
               Verbose.log((Object)("Adding endpoint at " + var15));
            }

            var3.addEndpoint(var15, var17);
            if (var9.length() == 0) {
               var9.append(AsyncUtil.getAsyncSelector(var15));
            } else {
               var9.append(" OR (" + AsyncUtil.getAsyncSelector(var15) + ")");
            }
         }

         if (var11) {
            if (verbose) {
               Verbose.log((Object)("Set up dynamic MDB to queue: " + var8.getQueueName()));
            }

            try {
               AsyncUtil.setupDynamicMDB(var1, var9.toString(), var8.getQueueName(), var8.getMdbRunAsPrincipalName(), "weblogic.wsee.server.jms.MdbWS", 180);
            } catch (WsDeploymentException var19) {
               throw new PolicyException(var19);
            }
         }
      }
   }

   private void insertHandlers(WsPort var1) throws PolicyException {
      HandlerList var2 = var1.getInternalHandlerList();
      HandlerInfo var3 = new HandlerInfo(WsrmServerHandler.class, new HashMap(), (QName[])null);
      HandlerInfo var4 = new HandlerInfo(WsrmServerConfigHandler.class, new HashMap(), (QName[])null);
      HandlerInfo var5 = new HandlerInfo(WsrmServerHandshakeHandler.class, new HashMap(), (QName[])null);
      HandlerInfo var6 = new HandlerInfo(ForwardingHandler.class, new HashMap(), (QName[])null);

      try {
         var2.lenientInsert("WSRM_CONFIG_HANDLER", var4, AFTER_CONFIG, BEFORE_CONFIG);
         var2.lenientInsert("WSRM_HANDSHAKE_HANDLER", var5, AFTER_HANDSHAKE, BEFORE_HANDSHAKE);
         var2.lenientInsert("RELIABILITY_HANDLER", var3, AFTER_RELIABILITY, BEFORE_RELIABILITY);
         var2.lenientInsert("FORWARDING_HANDLER", var6, AFTER_FORWARDING, BEFORE_FORWARDING);
      } catch (HandlerException var8) {
         throw new PolicyException("Failed to register handler", var8);
      }
   }
}
