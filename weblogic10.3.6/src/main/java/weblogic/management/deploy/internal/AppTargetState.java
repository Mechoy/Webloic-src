package weblogic.management.deploy.internal;

import java.io.Serializable;
import weblogic.deploy.common.Debug;

public class AppTargetState implements Serializable {
   private static final long serialVersionUID = 1L;
   private String appName;
   private String target;
   private String state;
   private int stagingState;

   public AppTargetState() {
      this.stagingState = -1;
   }

   public AppTargetState(String var1) {
      this();
      this.setAppName(var1);
   }

   public AppTargetState(String var1, String var2) {
      this(var1);
      this.setTarget(var2);
   }

   public void setAppName(String var1) {
      this.appName = var1;
   }

   public void setTarget(String var1) {
      this.target = var1;
   }

   public String getState() {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug(" +++ Returning app's state : " + this.state);
      }

      return this.state;
   }

   public void setState(String var1) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug(" +++ Setting app's state : " + var1);
      }

      this.state = var1;
   }

   public final int getStagingState() {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug(" +++ Getting app's stage state : " + this.stagingState);
      }

      return this.stagingState;
   }

   public final void setStagingState(int var1) {
      if (var1 >= 0 && var1 <= 1) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug(" +++ Setting app's stage state : " + var1);
         }

         this.stagingState = var1;
      } else {
         throw new AssertionError("Given stagingState '" + var1 + "' is " + "invalid. It should be in the range'" + 0 + "' or '" + 1 + "'");
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toString()).append("(");
      var1.append("appName=").append(this.appName).append(", ");
      var1.append("target=").append(this.target).append(", ");
      var1.append("state=").append(this.state).append(", ");
      var1.append("stagingState=").append(this.stagingState).append(")");
      return var1.toString();
   }
}
