package weblogic.ejb.container;

import weblogic.server.ServiceActivator;

public class EJBServiceActivator extends ServiceActivator {
   protected EJBServiceActivator(String var1) {
      super(var1);
   }

   public String getName() {
      return "EJB Container";
   }

   public String getVersion() {
      return "EJB 3.0";
   }
}
