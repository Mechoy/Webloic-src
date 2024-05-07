package weblogic.management.scripting.jsr88;

import javax.enterprise.deploy.spi.status.ProgressObject;
import weblogic.management.scripting.WLScriptContext;

public class WLSTProgressImpl implements WLSTProgress {
   ProgressObject progressObject = null;
   WLScriptContext ctx = null;

   public WLSTProgressImpl(ProgressObject po, WLScriptContext ctx) {
      this.progressObject = po;
      this.ctx = ctx;
   }

   public String getState() {
      return this.progressObject.getDeploymentStatus().getState().toString();
   }

   public boolean isRunning() {
      return this.progressObject.getDeploymentStatus().isRunning();
   }

   public boolean isFailed() {
      return this.progressObject.getDeploymentStatus().isFailed();
   }

   public boolean isCompleted() {
      return this.progressObject.getDeploymentStatus().isCompleted();
   }

   public String getMessage() {
      return this.progressObject.getDeploymentStatus().getMessage();
   }

   public String getCommandType() {
      return this.progressObject.getDeploymentStatus().getCommand().toString();
   }

   public void printStatus() {
      this.ctx.println("Current Status of your Deployment:");
      this.ctx.println("Deployment command type: " + this.getCommandType());
      this.ctx.println("Deployment State       : " + this.getState());
      if (this.getMessage() == null) {
         this.ctx.println("Deployment Message     : no message");
      } else {
         this.ctx.println("Deployment Message     : " + this.getMessage());
      }

   }

   public ProgressObject getProgressObject() {
      return this.progressObject;
   }
}
