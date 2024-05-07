package weblogic.application;

import weblogic.management.DeploymentException;

public interface AppDeploymentExtension {
   String getName();

   void init(ApplicationContextInternal var1) throws DeploymentException;

   void prepare(ApplicationContextInternal var1) throws DeploymentException;

   void activate(ApplicationContextInternal var1) throws DeploymentException;

   void deactivate(ApplicationContextInternal var1) throws DeploymentException;

   void unprepare(ApplicationContextInternal var1) throws DeploymentException;
}
