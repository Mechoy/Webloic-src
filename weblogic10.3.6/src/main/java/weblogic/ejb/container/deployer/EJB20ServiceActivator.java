package weblogic.ejb.container.deployer;

import weblogic.ejb.container.EJBServiceActivator;

public class EJB20ServiceActivator extends EJBServiceActivator {
   public static final EJB20ServiceActivator INSTANCE = new EJB20ServiceActivator();

   private EJB20ServiceActivator() {
      super("weblogic.ejb.container.deployer.EJB20Service");
   }
}
