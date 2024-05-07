package weblogic.management.configuration;

import java.util.Properties;
import javax.management.InvalidAttributeValueException;

/** @deprecated */
public interface RDBMSRealmMBean extends BasicRealmMBean {
   String getRealmClassName();

   void setRealmClassName(String var1) throws InvalidAttributeValueException;

   String getDatabaseDriver();

   void setDatabaseDriver(String var1) throws InvalidAttributeValueException;

   String getDatabaseURL();

   void setDatabaseURL(String var1) throws InvalidAttributeValueException;

   String getDatabaseUserName();

   void setDatabaseUserName(String var1) throws InvalidAttributeValueException;

   String getDatabasePassword();

   void setDatabasePassword(String var1) throws InvalidAttributeValueException;

   byte[] getDatabasePasswordEncrypted();

   void setDatabasePasswordEncrypted(byte[] var1) throws InvalidAttributeValueException;

   Properties getSchemaProperties();

   void setSchemaProperties(Properties var1) throws InvalidAttributeValueException;
}
