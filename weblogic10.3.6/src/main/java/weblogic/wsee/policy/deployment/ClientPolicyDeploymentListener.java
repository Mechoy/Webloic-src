package weblogic.wsee.policy.deployment;

import java.util.Iterator;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.provider.ClientConfigurationHandler;
import weblogic.wsee.policy.provider.PolicyProvider;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public class ClientPolicyDeploymentListener implements WsDeploymentListener {
   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      ProviderRegistry var2 = null;

      try {
         var2 = ProviderRegistry.getTheRegistry();
      } catch (PolicyException var7) {
         throw new WsDeploymentException("Could not obtain the policy provider registry", var7);
      }

      Iterator var3 = var2.iterateProviders();

      while(var3.hasNext()) {
         ClientConfigurationHandler var4 = ((PolicyProvider)var3.next()).getClientConfigHandler();

         try {
            if (var4 != null) {
               var4.process(var1);
            }
         } catch (PolicyException var6) {
            throw new WsDeploymentException("Error in policy", var6);
         }
      }

   }
}
