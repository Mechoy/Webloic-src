package weblogic.wsee.security;

import java.util.Iterator;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.handler.HandlerList;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.security.configuration.WssConfigurationException;
import weblogic.wsee.security.policy.assertions.SecurityPolicyAssertionFactory;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public abstract class WssDeploymentListener implements WsDeploymentListener {
   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      WsService var2 = var1.getWsService();
      PolicyServer var3 = var2.getPolicyServer();
      boolean var4 = var2.isUsingPolicy();
      if (var4) {
         Iterator var5 = var2.getPorts();

         while(var5.hasNext()) {
            WsPort var6 = (WsPort)var5.next();

            try {
               HandlerList var7 = var6.getInternalHandlerList();
               if (SecurityPolicyAssertionFactory.hasSecurityPolicy(var6, var3)) {
                  this.insertHandler(var7);
                  var2.initWssConfiguration();
               } else {
                  this.removeHandler(var7);
               }
            } catch (PolicyException var8) {
               throw new WsDeploymentException("Failed to register SECURITY_HANDLER", var8);
            } catch (HandlerException var9) {
               throw new WsDeploymentException("Failed to register SECURITY_HANDLER", var9);
            } catch (WssConfigurationException var10) {
               throw new WsDeploymentException("Failed to register SECURITY_HANDLER", var10);
            }
         }

      }
   }

   protected abstract void insertHandler(HandlerList var1) throws HandlerException;

   protected abstract void removeHandler(HandlerList var1);
}
