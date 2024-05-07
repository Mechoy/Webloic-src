package weblogic.ejb.container.compliance;

import weblogic.ejb.container.interfaces.DeploymentInfo;

final class ClientJarChecker extends BaseComplianceChecker {
   private static final boolean debug = true;
   private static final boolean verbose = true;
   private DeploymentInfo deploymentInfo;

   public ClientJarChecker(DeploymentInfo var1) {
      this.deploymentInfo = var1;
   }

   public void checkForEmptyClientJar() {
      if ("".equals(this.deploymentInfo.getClientJarFileName())) {
         log.logWarning(this.fmt.EMPTY_CLIENT_JAR_TAG());
      }

   }
}
