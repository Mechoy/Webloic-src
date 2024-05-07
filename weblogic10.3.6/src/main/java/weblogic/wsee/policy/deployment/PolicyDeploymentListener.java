package weblogic.wsee.policy.deployment;

import java.util.Iterator;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.provider.PolicyProvider;
import weblogic.wsee.policy.provider.ServiceConfigurationHandler;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public class PolicyDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(PolicyDeploymentListener.class);

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      if (verbose) {
         Verbose.log((Object)"PolicyDeploymentListener.process()");
      }

      invokeProviderConfig(var1);
   }

   private static void invokeProviderConfig(WsDeploymentContext var0) throws WsDeploymentException {
      ProviderRegistry var1 = null;

      try {
         var1 = ProviderRegistry.getTheRegistry();
      } catch (PolicyException var6) {
         throw new WsDeploymentException("Could not obtain the policy provider registry", var6);
      }

      if (var1 != null && !var1.isEmpty()) {
         Iterator var2 = var1.iterateProviders();

         while(var2.hasNext()) {
            ServiceConfigurationHandler var3 = ((PolicyProvider)var2.next()).getServiceConfigHandler();
            if (var3 != null) {
               try {
                  var3.process(var0);
               } catch (PolicyException var5) {
                  throw new WsDeploymentException("Could not process Policy", var5);
               }
            }
         }

      }
   }
}
