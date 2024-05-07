package weblogic.management.deploy.internal;

import java.util.Set;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.internal.targetserver.BasicDeployment;
import weblogic.deploy.internal.targetserver.SystemResourceDeployment;
import weblogic.management.configuration.CustomResourceMBean;
import weblogic.management.deploy.classdeployment.ClassDeploymentManager;
import weblogic.management.internal.DeploymentHandlerHome;

abstract class DeploymentAdapter {
   private static final boolean deploySystemResourceOnePhase = true;
   private static final boolean deployDeploymentHandlerOnePhase = true;
   static final DeploymentAdapter BASIC_DEP_ADAPTER = new DeploymentAdapter() {
      public String getName(Object var1) {
         BasicDeployment var2 = (BasicDeployment)var1;
         return ApplicationVersionUtils.getDisplayName(var2.getDeploymentMBean());
      }

      protected void doPrepare(Object var1) throws Exception {
         ((BasicDeployment)var1).prepare();
         if (DeploymentAdapter.isDeployedInPhaseOne(var1)) {
            ((BasicDeployment)var1).activateFromServerLifecycle();
         }

      }

      protected void doActivate(Object var1) throws Exception {
         if (!DeploymentAdapter.isDeployedInPhaseOne(var1)) {
            ((BasicDeployment)var1).activateFromServerLifecycle();
         }
      }

      public void adminToProduction(Object var1) throws Exception {
         ((BasicDeployment)var1).adminToProductionFromServerLifecycle();
      }

      public void gracefulProductionToAdmin(Object var1) throws Exception {
         ((BasicDeployment)var1).productionToAdminFromServerLifecycle(true);
      }

      public void forceProductionToAdmin(Object var1) throws Exception {
         ((BasicDeployment)var1).productionToAdminFromServerLifecycle(false);
      }

      protected void doDeactivate(Object var1) throws Exception {
         if (!DeploymentAdapter.isDeployedInPhaseOne(var1)) {
            ((BasicDeployment)var1).deactivateFromServerLifecycle();
         }
      }

      protected void doUnprepare(Object var1) throws Exception {
         if (DeploymentAdapter.isDeployedInPhaseOne(var1)) {
            ((BasicDeployment)var1).deactivateFromServerLifecycle();
         }

         ((BasicDeployment)var1).unprepare();
      }

      public void remove(Object var1) throws Exception {
         ((BasicDeployment)var1).remove();
      }

      public void remove(Object var1, boolean var2) throws Exception {
         ((BasicDeployment)var1).remove(var2);
      }
   };
   static final DeploymentAdapter DEPLOYMENT_HANDLERS_ADAPTER = new DeploymentAdapter() {
      Set deployments;

      protected void doPrepare(Object var1) throws Exception {
         this.deployments = DeploymentHandlerHome.getInstance().prepareInitialDeployments();
         DeploymentHandlerHome.getInstance().activateInitialDeployments(this.deployments);
      }

      protected void doActivate(Object var1) throws Exception {
      }

      protected void doDeactivate(Object var1) throws Exception {
      }

      protected void doUnprepare(Object var1) throws Exception {
         this.deployments = DeploymentHandlerHome.getInstance().deactivateCurrentDeployments();
         DeploymentHandlerHome.getInstance().unprepareCurrentDeployments(this.deployments);
      }
   };
   static final DeploymentAdapter RESOURCE_DEPENDENT_DEPLOYMENT_HANDLERS_ADAPTER = new DeploymentAdapter() {
      Set deployments;

      protected void doPrepare(Object var1) throws Exception {
         this.deployments = DeploymentHandlerHome.getInstance().prepareResourceDependentInitialDeployments();
         DeploymentHandlerHome.getInstance().activateResourceDependentInitialDeployments(this.deployments);
      }

      protected void doActivate(Object var1) throws Exception {
      }

      protected void doDeactivate(Object var1) throws Exception {
      }

      protected void doUnprepare(Object var1) throws Exception {
         this.deployments = DeploymentHandlerHome.getInstance().deactivateResourceDependentCurrentDeployments();
         DeploymentHandlerHome.getInstance().unprepareResourceDependentCurrentDeployments(this.deployments);
      }
   };
   static final DeploymentAdapter STARTUP_CLASSES_ADAPTER = new DeploymentAdapter() {
      void activate(Object var1) throws Exception {
         ClassDeploymentManager.getInstance().runStartupsBeforeAppActivation();
      }
   };

   private DeploymentAdapter() {
   }

   private static boolean isDeployedInPhaseOne(Object var0) {
      return var0 instanceof SystemResourceDeployment && !(((BasicDeployment)var0).getDeploymentMBean() instanceof CustomResourceMBean);
   }

   String getName(Object var1) {
      return var1.toString();
   }

   void prepare(Object var1) throws Exception {
      try {
         this.preInvoke();
         this.doPrepare(var1);
      } finally {
         this.postInvoke();
      }

   }

   void activate(Object var1) throws Exception {
      try {
         this.preInvoke();
         this.doActivate(var1);
      } finally {
         this.postInvoke();
      }

   }

   void adminToProduction(Object var1) throws Exception {
   }

   void gracefulProductionToAdmin(Object var1) throws Exception {
   }

   void forceProductionToAdmin(Object var1) throws Exception {
   }

   void deactivate(Object var1) throws Exception {
      try {
         this.preInvoke();
         this.doDeactivate(var1);
      } finally {
         this.postInvoke();
      }

   }

   void unprepare(Object var1) throws Exception {
      try {
         this.preInvoke();
         this.doUnprepare(var1);
      } finally {
         this.postInvoke();
      }

   }

   void remove(Object var1) throws Exception {
   }

   void remove(Object var1, boolean var2) throws Exception {
   }

   protected void preInvoke() {
      ApplicationVersionUtils.setCurrentAdminMode(true);
   }

   protected void postInvoke() {
      ApplicationVersionUtils.unsetCurrentAdminMode();
   }

   protected void doPrepare(Object var1) throws Exception {
   }

   protected void doActivate(Object var1) throws Exception {
   }

   protected void doDeactivate(Object var1) throws Exception {
   }

   protected void doUnprepare(Object var1) throws Exception {
   }

   // $FF: synthetic method
   DeploymentAdapter(Object var1) {
      this();
   }
}
