package weblogic.common;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jndi.Environment;

final class NameServicesImpl implements NameServicesDef {
   private T3Client t3;
   private Context initialContext;

   NameServicesImpl(T3Client var1) {
      this.t3 = var1;
   }

   NameServicesImpl() {
   }

   public void private_setT3Client(T3Client var1) {
      this.t3 = var1;
   }

   public Context getInitialContext() throws NamingException {
      if (this.initialContext == null) {
         this.initialContext = this.getInitialContext(new Hashtable());
      }

      return this.initialContext;
   }

   public Context getInitialContext(Hashtable var1) throws NamingException {
      Environment var2 = new Environment(var1);
      var2.setProviderUrl(this.t3.getT3Connection().getURL());
      var2.setSecurityCredentials(this.t3.getT3Connection().getUser());
      return var2.getInitialContext();
   }
}
