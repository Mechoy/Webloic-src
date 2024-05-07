package weblogic.t3.srvr.servicegroups;

import weblogic.ejb.container.deployer.EJB20ServiceActivator;
import weblogic.ejb.container.deployer.MDBServiceActivator;
import weblogic.ejb.container.timer.EJBTimerStarterActivator;
import weblogic.server.servicegraph.Service;
import weblogic.server.servicegraph.ServiceGroup;

public class EJBServiceGroup extends ServiceGroup {
   static final Service ejbService;
   static final Service timerStarter;
   static final Service mdbService;

   public static Service getEJBService() {
      return ejbService;
   }

   public static Service getTimerStarter() {
      return timerStarter;
   }

   public static Service getMDBService() {
      return mdbService;
   }

   public boolean isEJBServiceAvailable() {
      return ejbService.isNeeded();
   }

   public EJBServiceGroup(boolean var1) {
      super(var1);
      this.addService(ejbService);
      ejbService.addSuccessor(mdbService);
      ejbService.addSuccessor(CoreServiceGroup.getDeploymentService());
      this.addService(mdbService);
      mdbService.addSuccessor(timerStarter);
      mdbService.addDependency(CoreServiceGroup.getClusterInboundService());
      this.addService(timerStarter);
      timerStarter.addDependency(CoreServiceGroup.getJobSchedulerService());
      timerStarter.addPredecessor(CoreServiceGroup.getEnableListenersService());
   }

   public boolean isAvailable() {
      return this.isConfigured();
   }

   static {
      ejbService = new Service(EJB20ServiceActivator.INSTANCE);
      timerStarter = new Service(EJBTimerStarterActivator.INSTANCE);
      mdbService = new Service(MDBServiceActivator.INSTANCE);
   }
}
