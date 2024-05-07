package weblogic.cluster.migration.rmiservice;

import java.util.HashMap;
import java.util.Map;
import weblogic.cluster.migration.MigratableRemote;
import weblogic.cluster.migration.MigrationManager;
import weblogic.management.DeploymentException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.MigratableRMIServiceMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.internal.DeploymentHandler;
import weblogic.management.internal.DeploymentHandlerContext;
import weblogic.management.internal.DeploymentHandlerHome;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.utils.Debug;

public final class MigratableRMIService extends AbstractServerService implements DeploymentHandler {
   private boolean debug = true;
   private static MigratableRMIService singleton = null;
   private Map deployments = null;

   public MigratableRMIService() {
      Debug.assertion(singleton == null, "MigratableRMIService singleton already set");
      singleton = this;
      this.deployments = new HashMap();
   }

   public void start() throws ServiceFailureException {
      DeploymentHandlerHome.addDeploymentHandler(this);
   }

   public void stop() throws ServiceFailureException {
      this.shutdown();
   }

   public void halt() throws ServiceFailureException {
      this.shutdown();
   }

   public void shutdown() throws ServiceFailureException {
      DeploymentHandlerHome.removeDeploymentHandler(this);
   }

   private void deploy(MigratableRMIServiceMBean var1) {
      try {
         if (!this.deployments.containsKey(var1.getName())) {
            Class var2 = Class.forName(var1.getClassname());
            Object var3 = var2.newInstance();
            MigratableRemote var4 = (MigratableRemote)var3;
            ((Initialization)var4).initialize(var1.getArgument());
            Debug.assertion(var1.getTargets().length == 1, "[MigratableRMIService] OAM did not localize targets properly!");
            MigratableTargetMBean var5 = (MigratableTargetMBean)((MigratableTargetMBean)var1.getTargets()[0]);
            String var6 = var1.getArgument();
            MigrationManager.singleton().register(var4, var6, var5);
            this.deployments.put(var1.getName(), var4);
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   private void undeploy(MigratableRMIServiceMBean var1) {
      try {
         MigratableRemote var2 = (MigratableRemote)this.deployments.remove(var1.getName());
         if (var2 != null) {
            Debug.assertion(var2 != null);
            ((Initialization)var2).destroy(var1.getArgument());
            Debug.assertion(var1.getTargets().length == 1, "[MigratableRMIService] OAM did not localize targets properly!");
            MigratableTargetMBean var3 = (MigratableTargetMBean)((MigratableTargetMBean)var1.getTargets()[0]);
            String var4 = var1.getArgument();
            MigrationManager.singleton().unregister(var2, var3);
         } else {
            Debug.say("--> undeploy called but m is null!");
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void prepareDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws DeploymentException {
   }

   public void activateDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws DeploymentException {
      if (var1 instanceof MigratableRMIServiceMBean) {
         try {
            this.deploy((MigratableRMIServiceMBean)var1);
         } catch (Exception var4) {
            throw new DeploymentException("failed to deploy MigratableRMIServiceMBean " + var1.getName(), var4);
         }
      }

   }

   public void deactivateDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws UndeploymentException {
      if (var1 instanceof MigratableRMIServiceMBean) {
         try {
            this.undeploy((MigratableRMIServiceMBean)var1);
         } catch (Exception var4) {
            throw new UndeploymentException("failed to undeploy MigratableRMIServiceMBean " + var1.getName(), var4);
         }
      }

   }

   public void unprepareDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws UndeploymentException {
   }
}
