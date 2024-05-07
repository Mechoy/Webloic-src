package weblogic.servlet.internal.dd.compliance;

import weblogic.tools.ui.progress.ProgressProducer;
import weblogic.utils.ErrorCollectionException;

public interface ComplianceChecker extends ProgressProducer {
   void check(DeploymentInfo var1) throws ErrorCollectionException;

   void update(String var1);

   void setVerbose(boolean var1);
}
