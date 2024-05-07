package weblogic.corba.cos.transactions;

import java.util.HashMap;
import weblogic.iiop.IIOPLogger;
import weblogic.rmi.extensions.activation.Activatable;
import weblogic.rmi.extensions.activation.Activator;

public class ResourceFactory implements Activator {
   private static final HashMap map = new HashMap();
   private static final Activator activator = new ResourceFactory();

   public static final Activator getActivator() {
      return activator;
   }

   private ResourceFactory() {
   }

   public synchronized Activatable activate(Object var1) {
      if (map.get(var1) == null) {
         ResourceImpl var2 = ResourceImpl.getResource(((ResourceImpl.ResourceActivationID)var1).getXid());
         if (OTSHelper.isDebugEnabled()) {
            IIOPLogger.logDebugOTS("activating new resource " + var2);
         }

         return var2;
      } else {
         return (Activatable)map.get(var1);
      }
   }

   synchronized void activateResource(ResourceImpl var1) {
      if (OTSHelper.isDebugEnabled()) {
         IIOPLogger.logDebugOTS("activating resource " + var1);
      }

      map.put(var1.getActivationID(), var1);
   }

   public synchronized void deactivate(Activatable var1) {
   }

   public synchronized void release(Activatable var1) {
      if (OTSHelper.isDebugEnabled()) {
         IIOPLogger.logDebugOTS("released resource " + var1);
      }

      map.remove(var1.getActivationID());
   }
}
