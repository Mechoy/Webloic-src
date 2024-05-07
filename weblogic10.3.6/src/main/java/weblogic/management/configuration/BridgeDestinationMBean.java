package weblogic.management.configuration;

import java.util.Properties;
import javax.management.InvalidAttributeValueException;

/** @deprecated */
public interface BridgeDestinationMBean extends BridgeDestinationCommonMBean {
   Properties getProperties();

   void setProperties(Properties var1) throws InvalidAttributeValueException;
}
