package weblogic.jndi.internal;

import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jndi.Environment;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class ForeignJNDIManagerService extends AbstractServerService {
   private Context context;
   private Environment env;

   public void start() throws ServiceFailureException {
      try {
         this.env = new Environment();
         this.env.setReplicateBindings(false);
         this.context = this.env.getInitialContext();
      } catch (NamingException var2) {
         throw new ServiceFailureException("Failed to start ForeignJNDIManager service: ", var2);
      }

      ForeignJNDIManager.initialize();
   }
}
