package weblogic.management.provider.internal;

import java.util.HashMap;
import java.util.Map;
import weblogic.deploy.service.ConfigurationContext;
import weblogic.deploy.service.DeploymentRequest;
import weblogic.diagnostics.debug.DebugLogger;

public class ConfigurationContextImpl implements ConfigurationContext {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationEdit");
   private DeploymentRequest deploymentRequest;
   private HashMap contextComponents = new HashMap();

   ConfigurationContextImpl(DeploymentRequest var1) {
      this.deploymentRequest = var1;
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Created configuration context impl " + this);
      }

   }

   public DeploymentRequest getDeploymentRequest() {
      return this.deploymentRequest;
   }

   public void addContextComponent(String var1, Object var2) {
      this.contextComponents.put(var1, var2);
   }

   public Object getContextComponent(String var1) {
      return this.contextComponents.get(var1);
   }

   public Map getContextComponents() {
      return this.contextComponents;
   }
}
