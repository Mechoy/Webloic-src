package weblogic.ejb.container.compliance;

import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.utils.ErrorCollectionException;

public interface ComplianceChecker {
   String COMPLIANCE_DEBUG_PROP = "weblogic.ejb.container.compliance.debug";
   String COMPLIANCE_VERBOSE_PROP = "weblogic.ejb.container.compliance.verbose";

   void checkDeploymentInfo(DeploymentInfo var1) throws ErrorCollectionException, ClassNotFoundException;
}
