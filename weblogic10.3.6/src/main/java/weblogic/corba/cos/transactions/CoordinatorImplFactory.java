package weblogic.corba.cos.transactions;

import java.util.HashMap;
import javax.transaction.xa.Xid;
import weblogic.rmi.extensions.activation.Activatable;
import weblogic.rmi.extensions.activation.Activator;

public class CoordinatorImplFactory implements Activator {
   private static final HashMap map = new HashMap();
   private static final Activator activator = new CoordinatorImplFactory();

   public static final Activator getActivator() {
      return activator;
   }

   private CoordinatorImplFactory() {
   }

   public synchronized Activatable activate(Object var1) {
      if (map.get(var1) == null) {
         map.put(var1, new CoordinatorImpl((Xid)var1));
      }

      return (Activatable)map.get(var1);
   }

   public synchronized void deactivate(Activatable var1) {
      map.remove(var1.getActivationID());
   }
}
