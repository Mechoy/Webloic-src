package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

/** @deprecated */
public interface JMSVirtualDestinationMBean extends DeploymentMBean, JMSConstants {
   String getJNDIName();

   void setJNDIName(String var1) throws InvalidAttributeValueException;
}
