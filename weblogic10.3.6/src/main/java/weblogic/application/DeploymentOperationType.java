package weblogic.application;

public enum DeploymentOperationType {
   ACTIVATE(1),
   DEACTIVATE(3),
   PREPARE(2),
   UNPREPARE(5),
   DISTRIBUTE(6),
   REMOVE(4),
   START(7),
   STOP(8),
   DEPLOY(11),
   UNDEPLOY(12),
   REDEPLOY(9),
   UPDATE(10),
   RETIRE(13);

   private final int deploymentTask;

   private DeploymentOperationType(int var3) {
      this.deploymentTask = var3;
   }

   public static DeploymentOperationType valueOf(int var0) {
      DeploymentOperationType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         DeploymentOperationType var4 = var1[var3];
         if (var4.deploymentTask == var0) {
            return var4;
         }
      }

      return null;
   }
}
