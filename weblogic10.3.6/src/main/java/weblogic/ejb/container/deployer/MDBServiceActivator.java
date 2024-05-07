package weblogic.ejb.container.deployer;

import weblogic.ejb.container.EJBServiceActivator;

public class MDBServiceActivator extends EJBServiceActivator {
   public static final MDBServiceActivator INSTANCE = new MDBServiceActivator();

   private MDBServiceActivator() {
      super("weblogic.ejb.container.deployer.MDBServiceImpl");
      this.setServiceAvailableBeforeStart(true);
   }

   public String getName() {
      return "MDBService";
   }

   public MDBService getMDBService() {
      return (MDBService)this.getServiceObj();
   }
}
