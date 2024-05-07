package weblogic.management.utils;

import weblogic.management.DeploymentException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.DeploymentMBean;

public interface GenericAdminHandler {
   void prepare(DeploymentMBean var1) throws DeploymentException;

   void activate(DeploymentMBean var1) throws DeploymentException;

   void deactivate(DeploymentMBean var1) throws UndeploymentException;

   void unprepare(DeploymentMBean var1) throws UndeploymentException;
}
