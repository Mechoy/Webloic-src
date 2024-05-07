package weblogic.rmi.internal;

import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jndi.Environment;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class HeartbeatHelperService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      try {
         Environment var1 = new Environment();
         var1.setReplicateBindings(false);
         Context var2 = var1.getInitialContext();
         var2.addToEnvironment("weblogic.jndi.createIntermediateContexts", "true");
         var2.bind("weblogic/rmi/extensions/server/HeartbeatHelper", HeartbeatHelperImpl.getHeartbeatHelper());
      } catch (NamingException var3) {
         throw new ServiceFailureException(var3);
      }
   }
}
