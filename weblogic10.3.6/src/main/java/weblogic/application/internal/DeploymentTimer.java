package weblogic.application.internal;

import weblogic.application.ApplicationContext;
import weblogic.application.Deployment;
import weblogic.deploy.container.DeploymentContext;
import weblogic.j2ee.J2EELogger;
import weblogic.management.DeploymentException;
import weblogic.utils.Debug;
import weblogic.utils.StringUtils;

public final class DeploymentTimer extends DeploymentStateChecker {
   DeploymentTimer(Deployment var1) {
      super(var1);
   }

   private void print(String var1, long var2) {
      String var4 = var1 + " on app " + this.getApplicationContext().getApplicationId() + " took " + var2 + "ms.";
      Debug.say(var4);
      J2EELogger.logDebug(var4);
   }

   public void prepare(DeploymentContext var1) throws DeploymentException {
      long var2 = System.currentTimeMillis();
      boolean var10 = false;

      try {
         var10 = true;
         super.prepare(var1);
         var10 = false;
      } finally {
         if (var10) {
            long var7 = System.currentTimeMillis();
            this.print("PREPARE", var7 - var2);
         }
      }

      long var4 = System.currentTimeMillis();
      this.print("PREPARE", var4 - var2);
   }

   public void activate(DeploymentContext var1) throws DeploymentException {
      long var2 = System.currentTimeMillis();
      boolean var10 = false;

      try {
         var10 = true;
         super.activate(var1);
         var10 = false;
      } finally {
         if (var10) {
            long var7 = System.currentTimeMillis();
            this.print("ACTIVATE", var7 - var2);
         }
      }

      long var4 = System.currentTimeMillis();
      this.print("ACTIVATE", var4 - var2);
   }

   public void deactivate(DeploymentContext var1) throws DeploymentException {
      long var2 = System.currentTimeMillis();
      boolean var10 = false;

      try {
         var10 = true;
         super.deactivate(var1);
         var10 = false;
      } finally {
         if (var10) {
            long var7 = System.currentTimeMillis();
            this.print("DEACTIVATE", var7 - var2);
         }
      }

      long var4 = System.currentTimeMillis();
      this.print("DEACTIVATE", var4 - var2);
   }

   public void unprepare(DeploymentContext var1) throws DeploymentException {
      long var2 = System.currentTimeMillis();
      boolean var10 = false;

      try {
         var10 = true;
         super.unprepare(var1);
         var10 = false;
      } finally {
         if (var10) {
            long var7 = System.currentTimeMillis();
            this.print("UNPREPARE", var7 - var2);
         }
      }

      long var4 = System.currentTimeMillis();
      this.print("UNPREPARE", var4 - var2);
   }

   public void remove(DeploymentContext var1) throws DeploymentException {
      long var2 = System.currentTimeMillis();
      boolean var10 = false;

      try {
         var10 = true;
         super.remove(var1);
         var10 = false;
      } finally {
         if (var10) {
            long var7 = System.currentTimeMillis();
            this.print("REMOVE", var7 - var2);
         }
      }

      long var4 = System.currentTimeMillis();
      this.print("REMOVE", var4 - var2);
   }

   public void prepareUpdate(DeploymentContext var1) throws DeploymentException {
      long var2 = System.currentTimeMillis();
      boolean var12 = false;

      try {
         var12 = true;
         super.prepareUpdate(var1);
         var12 = false;
      } finally {
         if (var12) {
            long var8 = System.currentTimeMillis();
            String[] var10 = null;
            if (var1 != null) {
               var10 = var1.getUpdatedResourceURIs();
            }

            this.print("PREPARE_UPDATE uris: " + StringUtils.join(var10, ","), var8 - var2);
         }
      }

      long var4 = System.currentTimeMillis();
      String[] var6 = null;
      if (var1 != null) {
         var6 = var1.getUpdatedResourceURIs();
      }

      this.print("PREPARE_UPDATE uris: " + StringUtils.join(var6, ","), var4 - var2);
   }

   public void activateUpdate(DeploymentContext var1) throws DeploymentException {
      long var2 = System.currentTimeMillis();
      boolean var12 = false;

      try {
         var12 = true;
         super.activateUpdate(var1);
         var12 = false;
      } finally {
         if (var12) {
            long var8 = System.currentTimeMillis();
            String[] var10 = null;
            if (var1 != null) {
               var10 = var1.getUpdatedResourceURIs();
            }

            this.print("ACTIVATE_UPDATE uris: " + StringUtils.join(var10, ","), var8 - var2);
         }
      }

      long var4 = System.currentTimeMillis();
      String[] var6 = null;
      if (var1 != null) {
         var6 = var1.getUpdatedResourceURIs();
      }

      this.print("ACTIVATE_UPDATE uris: " + StringUtils.join(var6, ","), var4 - var2);
   }

   public void rollbackUpdate(DeploymentContext var1) {
      long var2 = System.currentTimeMillis();
      boolean var12 = false;

      try {
         var12 = true;
         super.rollbackUpdate(var1);
         var12 = false;
      } finally {
         if (var12) {
            long var8 = System.currentTimeMillis();
            String[] var10 = null;
            if (var1 != null) {
               var10 = var1.getUpdatedResourceURIs();
            }

            this.print("ROLLBACK_UPDATE uris: " + StringUtils.join(var10, ","), var8 - var2);
         }
      }

      long var4 = System.currentTimeMillis();
      String[] var6 = null;
      if (var1 != null) {
         var6 = var1.getUpdatedResourceURIs();
      }

      this.print("ROLLBACK_UPDATE uris: " + StringUtils.join(var6, ","), var4 - var2);
   }

   public void adminToProduction(DeploymentContext var1) throws DeploymentException {
      long var2 = System.currentTimeMillis();
      boolean var10 = false;

      try {
         var10 = true;
         super.adminToProduction(var1);
         var10 = false;
      } finally {
         if (var10) {
            long var7 = System.currentTimeMillis();
            this.print("ADMIN_2_PRODUCTION", var7 - var2);
         }
      }

      long var4 = System.currentTimeMillis();
      this.print("ADMIN_2_PRODUCTION", var4 - var2);
   }

   public void gracefulProductionToAdmin(DeploymentContext var1) throws DeploymentException {
      long var2 = System.currentTimeMillis();
      boolean var10 = false;

      try {
         var10 = true;
         super.gracefulProductionToAdmin(var1);
         var10 = false;
      } finally {
         if (var10) {
            long var7 = System.currentTimeMillis();
            this.print("GRACEFUL_PRODUCTION_2_ADMIN", var7 - var2);
         }
      }

      long var4 = System.currentTimeMillis();
      this.print("GRACEFUL_PRODUCTION_2_ADMIN", var4 - var2);
   }

   public void forceProductionToAdmin(DeploymentContext var1) throws DeploymentException {
      long var2 = System.currentTimeMillis();
      boolean var10 = false;

      try {
         var10 = true;
         super.forceProductionToAdmin(var1);
         var10 = false;
      } finally {
         if (var10) {
            long var7 = System.currentTimeMillis();
            this.print("FORCE_PRODUCTION_2_ADMIN", var7 - var2);
         }
      }

      long var4 = System.currentTimeMillis();
      this.print("FORCE_PRODUCTION_2_ADMIN", var4 - var2);
   }

   public void stop(DeploymentContext var1) throws DeploymentException {
      long var2 = System.currentTimeMillis();
      boolean var12 = false;

      try {
         var12 = true;
         super.stop(var1);
         var12 = false;
      } finally {
         if (var12) {
            long var8 = System.currentTimeMillis();
            String[] var10 = null;
            if (var1 != null) {
               var10 = var1.getUpdatedResourceURIs();
            }

            this.print("STOP uris: " + StringUtils.join(var10, ","), var8 - var2);
         }
      }

      long var4 = System.currentTimeMillis();
      String[] var6 = null;
      if (var1 != null) {
         var6 = var1.getUpdatedResourceURIs();
      }

      this.print("STOP uris: " + StringUtils.join(var6, ","), var4 - var2);
   }

   public ApplicationContext getApplicationContext() {
      return super.getApplicationContext();
   }
}
