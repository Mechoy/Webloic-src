package weblogic.management.deploy;

public final class DeploymentCompatibilityEvent {
   private String eventType = null;
   private String serverName = null;
   private String applicationName = null;
   private String applicationPhase = null;
   private String transition = null;
   private String moduleName = null;
   private String currentState = null;
   private String targetState = null;
   private String taskID = null;

   public DeploymentCompatibilityEvent(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9) {
      this.eventType = var1;
      this.serverName = var2;
      this.applicationName = var3;
      this.applicationPhase = var4;
      this.transition = var5;
      this.moduleName = var6;
      this.currentState = var7;
      this.targetState = var8;
      this.taskID = var9;
   }

   public String getEventType() {
      return this.eventType;
   }

   public String getServerName() {
      return this.serverName;
   }

   public String getApplicationName() {
      return this.applicationName;
   }

   public String getApplicationPhase() {
      return this.applicationPhase;
   }

   public String getTransition() {
      return this.transition;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public String getCurrentState() {
      return this.currentState;
   }

   public String getTargetState() {
      return this.targetState;
   }

   public String getTaskID() {
      return this.taskID;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer("DeployEvent[");
      if (this.eventType.equals("weblogic.deployment.application.module")) {
         var1.append("MODULE.").append(this.applicationName).append(".").append(this.moduleName).append(".").append(this.currentState).append(">").append(this.targetState).append(".").append(this.transition).append("@").append(this.serverName);
      } else {
         var1.append("APP.").append(this.applicationName).append(".").append(this.applicationPhase).append("@").append(this.serverName);
      }

      var1.append("]").toString();
      return var1.toString();
   }
}
