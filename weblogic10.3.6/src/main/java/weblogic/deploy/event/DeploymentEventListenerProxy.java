package weblogic.deploy.event;

import java.util.EventListener;
import java.util.EventListenerProxy;

class DeploymentEventListenerProxy extends EventListenerProxy {
   private String appName;

   DeploymentEventListenerProxy(String var1, EventListener var2) {
      super(var2);
      this.appName = var1;
   }

   public String getAppName() {
      return this.appName;
   }
}
