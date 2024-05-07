package weblogic.management.configuration;

import java.util.HashSet;
import java.util.Hashtable;
import javax.management.InvalidAttributeValueException;

/** @deprecated */
public interface ConnectorComponentMBean extends ComponentMBean {
   /** @deprecated */
   String getDescription();

   /** @deprecated */
   void setDescription(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getDisplayName();

   /** @deprecated */
   void setDisplayName(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getEisType();

   /** @deprecated */
   void setEisType(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getSmallIcon();

   /** @deprecated */
   void setSmallIcon(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getLargeIcon();

   /** @deprecated */
   void setLargeIcon(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getLicenseDescription();

   /** @deprecated */
   void setLicenseDescription(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean getLicenseRequired();

   /** @deprecated */
   void setLicenseRequired(boolean var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getSpecVersion();

   /** @deprecated */
   void setSpecVersion(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getVendorName();

   /** @deprecated */
   void setVendorName(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getVersion();

   /** @deprecated */
   void setVersion(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getConnectionFactoryImpl();

   /** @deprecated */
   void setConnectionFactoryImpl(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getConnectionFactoryInterface();

   /** @deprecated */
   void setConnectionFactoryInterface(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getConnectionImpl();

   /** @deprecated */
   void setConnectionImpl(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getConnectionInterface();

   /** @deprecated */
   void setConnectionInterface(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean getConnectionProfilingEnabled();

   /** @deprecated */
   void setConnectionProfilingEnabled(boolean var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getManagedConnectionFactoryClass();

   /** @deprecated */
   void setManagedConnectionFactoryClass(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean getreauthenticationSupport();

   /** @deprecated */
   void setreauthenticationSupport(boolean var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getTransactionSupport();

   /** @deprecated */
   void setTransactionSupport(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   Hashtable getConfigProperties();

   /** @deprecated */
   void setConfigProperties(Hashtable var1);

   /** @deprecated */
   HashSet getAuthenticationMechanisms();

   /** @deprecated */
   void setAuthenticationMechanisms(HashSet var1);

   /** @deprecated */
   HashSet getSecurityPermissions();

   /** @deprecated */
   void setSecurityPermissions(HashSet var1);

   /** @deprecated */
   ClassLoader getClassLoader();

   /** @deprecated */
   void setClassLoader(ClassLoader var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getConnectionFactoryName();

   /** @deprecated */
   void setConnectionFactoryName(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getConnectionFactoryDescription();

   /** @deprecated */
   void setConnectionFactoryDescription(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   int getMaxCapacity();

   /** @deprecated */
   void setMaxCapacity(int var1) throws InvalidAttributeValueException;
}
