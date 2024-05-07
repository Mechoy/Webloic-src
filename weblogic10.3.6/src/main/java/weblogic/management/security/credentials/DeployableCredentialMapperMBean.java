package weblogic.management.security.credentials;

import javax.management.InvalidAttributeValueException;

public interface DeployableCredentialMapperMBean extends CredentialMapperMBean {
   boolean isCredentialMappingDeploymentEnabled();

   void setCredentialMappingDeploymentEnabled(boolean var1) throws InvalidAttributeValueException;
}
