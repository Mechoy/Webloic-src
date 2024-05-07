package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

/** @deprecated */
public interface JMSDistributedDestinationMemberMBean extends ConfigurationMBean, JMSConstants {
   int getWeight();

   void setWeight(int var1) throws InvalidAttributeValueException;
}
