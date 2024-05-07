package weblogic.cacheprovider.coherence.application;

import weblogic.application.AppDeploymentExtension;
import weblogic.application.AppDeploymentExtensionFactory;

public class CoherenceApplicationDeploymentExtensionFactory implements AppDeploymentExtensionFactory {
   AppDeploymentExtension appExtn = new CoherenceAppDeploymentExtension();

   public AppDeploymentExtension createPreProcessorExtension() {
      return this.appExtn;
   }

   public AppDeploymentExtension createPostProcessorExtension() {
      return null;
   }
}
