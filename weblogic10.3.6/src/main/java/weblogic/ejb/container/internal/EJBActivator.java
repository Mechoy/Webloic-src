package weblogic.ejb.container.internal;

import weblogic.rmi.extensions.activation.Activatable;
import weblogic.rmi.extensions.activation.Activator;

public class EJBActivator implements Activator {
   private BaseEJBHome homeClass;

   public EJBActivator(BaseEJBHome var1) {
      this.homeClass = var1;
   }

   public Activatable activate(Object var1) {
      Activatable var2 = (Activatable)this.homeClass.allocateEO(var1);
      return var2;
   }

   public void deactivate(Activatable var1) {
   }
}
