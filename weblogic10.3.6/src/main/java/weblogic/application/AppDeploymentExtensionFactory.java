package weblogic.application;

public interface AppDeploymentExtensionFactory {
   AppDeploymentExtension createPreProcessorExtension();

   AppDeploymentExtension createPostProcessorExtension();
}
