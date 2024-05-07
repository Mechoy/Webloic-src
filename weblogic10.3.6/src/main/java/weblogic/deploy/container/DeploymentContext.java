package weblogic.deploy.container;

import java.io.InputStream;
import weblogic.application.Deployment;
import weblogic.management.configuration.DomainMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;

public interface DeploymentContext {
   DomainMBean getProposedDomain();

   String[] getUpdatedResourceURIs();

   String[] getStoppedModules();

   AuthenticatedSubject getInitiator();

   Deployment.AdminModeCallback getAdminModeCallback();

   boolean isAdminModeTransition();

   boolean isIgnoreSessionsEnabled();

   int getRMIGracePeriodSecs();

   String[] getUserSuppliedTargets();

   boolean requiresRestart();

   InputStream getApplicationDescriptor();

   InputStream getWLApplicationDescriptor();

   InputStream getAltDD();

   InputStream getAltWLDD();

   int getDeploymentOperation();

   boolean isStaticDeploymentOperation();

   boolean isAppStaged();
}
