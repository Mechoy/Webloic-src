package weblogic.jndi;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

public class WLInitialContextFactory implements InitialContextFactory {
   public final Context getInitialContext(Hashtable var1) throws NamingException {
      return (new Environment(var1)).getContext((String)null);
   }
}
