package weblogic.t3.srvr.servicegroups;

import weblogic.server.ServiceActivator;
import weblogic.server.servicegraph.Service;
import weblogic.server.servicegraph.ServiceGroup;

public class WseeServiceGroup extends ServiceGroup {
   public static final String WSEE = "WSEE";
   private static final Service wseeService;
   private static final DeploymentService deploymentService;
   static final Service wseePolicySubjectManagerService;
   static final Service wseeBufferingService;

   public static Service getWseeService() {
      return wseeService;
   }

   public static DeploymentService getDeploymentService() {
      return deploymentService;
   }

   public WseeServiceGroup(boolean var1) {
      super(var1);
      this.addService(wseeService);
      wseeService.addDependency(JMSServiceGroup.getJMSService());
      wseeService.addDependency(CoreServiceGroup.getStandbyState());
      this.addService(wseePolicySubjectManagerService);
      wseePolicySubjectManagerService.addDependency(wseeService);
      this.addService(wseeBufferingService);
      wseeBufferingService.addDependency(wseeService);
      this.addService(deploymentService);
      deploymentService.addSuccessor(CoreServiceGroup.getDeploymentService());
   }

   public boolean isAvailable() {
      return this.isConfigured();
   }

   static {
      wseeService = new Service(WseeServiceGroup.WseeServiceActivator.INSTANCE);
      deploymentService = WseeServiceGroup.DeploymentService.INSTANCE;
      wseePolicySubjectManagerService = new Service("weblogic.wsee.policy.deployment.WseePolicySubjectManagerService");
      wseeBufferingService = new Service("weblogic.wsee.buffer2.internal.wls.BufferingService");
   }

   private static class WseeServiceActivator extends ServiceActivator {
      public static final WseeServiceActivator INSTANCE = new WseeServiceActivator();

      private WseeServiceActivator() {
         super("weblogic.wsee.runtime.WseeService");
      }

      public String getVersion() {
         return "1.0";
      }

      public String getName() {
         return "Web Services Execution Engine";
      }
   }

   public static class DeploymentService extends Service {
      static final DeploymentService INSTANCE = new DeploymentService("weblogic.wsee.runtime.DeploymentService");

      private DeploymentService(String var1) {
         super(var1);
      }
   }
}
