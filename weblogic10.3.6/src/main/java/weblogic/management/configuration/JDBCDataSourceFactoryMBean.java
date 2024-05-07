package weblogic.management.configuration;

import java.util.Map;

/** @deprecated */
public interface JDBCDataSourceFactoryMBean extends ConfigurationMBean {
   void setUserName(String var1);

   String getUserName();

   void setPassword(String var1);

   String getPassword();

   byte[] getPasswordEncrypted();

   void setPasswordEncrypted(byte[] var1);

   void setURL(String var1);

   String getURL();

   void setDriverClassName(String var1);

   String getDriverClassName();

   Map getProperties();

   void setProperties(Map var1);

   void setFactoryName(String var1);

   String getFactoryName();
}
