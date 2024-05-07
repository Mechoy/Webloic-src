package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

public interface WLECConnectionPoolMBean extends DeploymentMBean {
   String[] getPrimaryAddresses();

   void setPrimaryAddresses(String[] var1) throws InvalidAttributeValueException;

   boolean addPrimaryAddress(String var1) throws InvalidAttributeValueException;

   boolean removePrimaryAddress(String var1);

   String[] getFailoverAddresses();

   void setFailoverAddresses(String[] var1) throws InvalidAttributeValueException;

   boolean addFailoverAddress(String var1) throws InvalidAttributeValueException;

   boolean removeFailoverAddress(String var1);

   int getMinimumPoolSize();

   void setMinimumPoolSize(int var1) throws InvalidAttributeValueException;

   int getMaximumPoolSize();

   void setMaximumPoolSize(int var1) throws InvalidAttributeValueException;

   String getUserName();

   void setUserName(String var1) throws InvalidAttributeValueException;

   String getUserPassword();

   void setUserPassword(String var1) throws InvalidAttributeValueException;

   byte[] getUserPasswordEncrypted();

   void setUserPasswordEncrypted(byte[] var1) throws InvalidAttributeValueException;

   String getApplicationPassword();

   void setApplicationPassword(String var1) throws InvalidAttributeValueException;

   byte[] getApplicationPasswordEncrypted();

   void setApplicationPasswordEncrypted(byte[] var1) throws InvalidAttributeValueException;

   String getUserRole();

   void setUserRole(String var1) throws InvalidAttributeValueException;

   String getWLEDomain();

   void setWLEDomain(String var1) throws InvalidAttributeValueException;

   int getMinimumEncryptionLevel();

   void setMinimumEncryptionLevel(int var1) throws InvalidAttributeValueException;

   int getMaximumEncryptionLevel();

   void setMaximumEncryptionLevel(int var1) throws InvalidAttributeValueException;

   boolean isCertificateAuthenticationEnabled();

   void setCertificateAuthenticationEnabled(boolean var1);

   boolean isSecurityContextEnabled();

   void setSecurityContextEnabled(boolean var1);
}
