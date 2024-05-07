package weblogic.management.configuration;

import java.util.Properties;
import javax.management.InvalidAttributeValueException;

/** @deprecated */
public interface CustomRealmMBean extends BasicRealmMBean {
   String getRealmClassName();

   void setRealmClassName(String var1) throws InvalidAttributeValueException;

   Properties getConfigurationData();

   void setConfigurationData(Properties var1) throws InvalidAttributeValueException;

   String getPassword();

   void setPassword(String var1) throws InvalidAttributeValueException;

   byte[] getPasswordEncrypted();

   void setPasswordEncrypted(byte[] var1) throws InvalidAttributeValueException;
}
