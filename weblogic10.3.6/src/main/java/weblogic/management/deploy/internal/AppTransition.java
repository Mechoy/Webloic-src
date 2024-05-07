package weblogic.management.deploy.internal;

abstract class AppTransition {
   private String name;
   private boolean startup;
   static final AppTransition PREPARE = new AppTransition("Prepare", true) {
      void transitionApp(DeploymentAdapter var1, Object var2) throws Exception {
         var1.prepare(var2);
      }
   };
   static final AppTransition ACTIVATE = new AppTransition("Activate", true) {
      void transitionApp(DeploymentAdapter var1, Object var2) throws Exception {
         var1.activate(var2);
      }
   };
   static final AppTransition ADMIN_TO_PRODUCTION = new AppTransition("Transition from admin to production", true) {
      void transitionApp(DeploymentAdapter var1, Object var2) throws Exception {
         var1.adminToProduction(var2);
      }
   };
   static final AppTransition GRACEFUL_PRODUCTION_TO_ADMIN = new AppTransition("Graceful transition from production to admin", false) {
      void transitionApp(DeploymentAdapter var1, Object var2) throws Exception {
         var1.gracefulProductionToAdmin(var2);
      }
   };
   static final AppTransition FORCE_PRODUCTION_TO_ADMIN = new AppTransition("Force transition from production to admin", false) {
      void transitionApp(DeploymentAdapter var1, Object var2) throws Exception {
         var1.forceProductionToAdmin(var2);
      }
   };
   static final AppTransition DEACTIVATE = new AppTransition("Deactivate", false) {
      void transitionApp(DeploymentAdapter var1, Object var2) throws Exception {
         var1.deactivate(var2);
      }
   };
   static final AppTransition UNPREPARE = new AppTransition("Unprepare", false) {
      void transitionApp(DeploymentAdapter var1, Object var2) throws Exception {
         var1.unprepare(var2);
      }
   };

   private AppTransition(String var1, boolean var2) {
      this.name = null;
      this.startup = true;
      this.name = var1;
      this.startup = var2;
   }

   public String toString() {
      return this.name;
   }

   boolean isStartup() {
      return this.startup;
   }

   abstract void transitionApp(DeploymentAdapter var1, Object var2) throws Exception;

   // $FF: synthetic method
   AppTransition(String var1, boolean var2, Object var3) {
      this(var1, var2);
   }
}
