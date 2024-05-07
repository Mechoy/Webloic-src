package weblogic.deploy.service;

import weblogic.management.ManagementException;

public interface DeploymentRequestFactory {
   DeploymentRequest createDeploymentRequest() throws ManagementException;
}
