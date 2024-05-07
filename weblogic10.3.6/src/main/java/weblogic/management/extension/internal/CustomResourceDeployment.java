package weblogic.management.extension.internal;

import java.io.File;
import weblogic.application.Module;
import weblogic.application.internal.SingleModuleDeployment;
import weblogic.deploy.container.DeploymentContext;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.CustomResourceMBean;
import weblogic.management.configuration.SystemResourceMBean;

public class CustomResourceDeployment extends SingleModuleDeployment {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationRuntime");
   String resourceName;

   public CustomResourceDeployment(CustomResourceMBean var1, File var2) throws DeploymentException {
      super((SystemResourceMBean)var1, createModule(var1), var2);
      this.resourceName = var1.getName();
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Created custom resource for file " + var2);
      }

   }

   private static Module createModule(CustomResourceMBean var0) {
      return new CustomResourceModule(var0.getName(), var0.getResource(), var0.getResourceClass());
   }

   public void prepareUpdate(DeploymentContext var1) throws DeploymentException {
      super.prepareUpdate(var1);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Prepare update for resource " + this.resourceName);
      }

   }

   public void activateUpdate(DeploymentContext var1) throws DeploymentException {
      super.activateUpdate(var1);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Activate update for resource " + this.resourceName);
      }

   }

   public void rollbackUpdate(DeploymentContext var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Rollback update for resource " + this.resourceName);
      }

   }
}
