package weblogic.ejb.container.compliance;

import weblogic.ejb.container.interfaces.DeploymentInfo;

public final class DependentChecker extends BaseComplianceChecker {
   private static final boolean debug = System.getProperty("weblogic.ejb.container.persistence.debug") != null;
   private static final boolean verbose = System.getProperty("weblogic.ejb.container.persistence.verbose") != null;
   private DeploymentInfo di;

   public DependentChecker(DeploymentInfo var1) {
      this.di = var1;
   }
}
