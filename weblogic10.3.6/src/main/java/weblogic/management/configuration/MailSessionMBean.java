package weblogic.management.configuration;

import java.util.Properties;
import javax.management.InvalidAttributeValueException;

public interface MailSessionMBean extends RMCFactoryMBean {
   Properties getProperties();

   void setProperties(Properties var1) throws InvalidAttributeValueException;
}
