package weblogic.corba.cos.transactions;

import java.util.HashMap;
import weblogic.rmi.extensions.activation.Activatable;
import weblogic.rmi.extensions.activation.Activator;

public class RecoveryFactory implements Activator {
   private static final HashMap map = new HashMap();
   private static final Activator activator = new RecoveryFactory();

   public static final Activator getActivator() {
      return activator;
   }

   private RecoveryFactory() {
   }

   public synchronized Activatable activate(Object var1) {
      if (map.get(var1) == null) {
         map.put(var1, new RecoveryCoordinatorImpl(var1));
      }

      return (Activatable)map.get(var1);
   }

   public synchronized void deactivate(Activatable var1) {
      map.remove(var1.getActivationID());
   }
}
