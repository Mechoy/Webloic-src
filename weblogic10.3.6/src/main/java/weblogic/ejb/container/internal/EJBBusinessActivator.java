package weblogic.ejb.container.internal;

import java.rmi.Remote;
import weblogic.rmi.extensions.activation.Activatable;
import weblogic.rmi.extensions.activation.Activator;

public class EJBBusinessActivator implements Activator {
   private StatefulEJBHomeImpl home;
   private Class businessImplClass;
   private Class iFace;

   public EJBBusinessActivator(StatefulEJBHomeImpl var1, Class var2, Class var3) {
      this.home = var1;
      this.businessImplClass = var2;
      this.iFace = var3;
   }

   public Activatable activate(Object var1) {
      Activatable var2 = (Activatable)this.home.allocateBI(var1, this.businessImplClass, this.iFace, this);
      if (!Remote.class.isAssignableFrom(this.iFace)) {
         ((StatefulRemoteObject)var2).setIsImplementsRemote(false);
      }

      return var2;
   }

   public void deactivate(Activatable var1) {
   }
}
