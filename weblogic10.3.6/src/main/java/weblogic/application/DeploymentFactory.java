package weblogic.application;

import java.io.File;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;

public interface DeploymentFactory {
   Deployment createDeployment(AppDeploymentMBean var1, File var2) throws DeploymentException;

   Deployment createDeployment(SystemResourceMBean var1, File var2) throws DeploymentException;
}
