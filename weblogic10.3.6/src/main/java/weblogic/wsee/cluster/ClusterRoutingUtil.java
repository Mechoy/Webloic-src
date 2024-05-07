package weblogic.wsee.cluster;

import java.rmi.UnknownHostException;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jndi.Environment;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.URLManager;
import weblogic.wsee.util.Verbose;

public class ClusterRoutingUtil {
   private static final boolean verbose = Verbose.isVerbose(ClusterRoutingUtil.class);

   public static ClusterDispatcherRemote getClusterDispatcher(String var0, String var1) throws ClusterServiceException {
      try {
         String var2 = URLManager.findURL(var0, ProtocolManager.getDefaultProtocol());
         if (verbose) {
            Verbose.log((Object)("Dispatch message for " + var1 + " to " + var0 + " at " + var2));
         }

         Environment var3 = new Environment();
         var3.setProviderUrl(var2);
         Context var4 = var3.getInitialContext();
         ClusterDispatcherRemote var5 = (ClusterDispatcherRemote)var4.lookup("weblogic.wsee.cluster.ClusterDispatcher");
         return var5;
      } catch (NamingException var6) {
         throw new ClusterServiceException(var6);
      } catch (UnknownHostException var7) {
         throw new ClusterServiceException(var7);
      }
   }
}
