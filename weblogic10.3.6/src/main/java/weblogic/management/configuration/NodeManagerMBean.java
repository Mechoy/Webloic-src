package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

public interface NodeManagerMBean extends ConfigurationMBean {
   String ADAPTER_SEPARATOR = "_";

   void setNMType(String var1) throws InvalidAttributeValueException;

   String getNMType();

   String getListenAddress();

   void setListenAddress(String var1) throws InvalidAttributeValueException;

   int getListenPort();

   void setListenPort(int var1);

   boolean isDebugEnabled();

   void setDebugEnabled(boolean var1);

   void setShellCommand(String var1);

   String getShellCommand();

   void setNodeManagerHome(String var1);

   String getNodeManagerHome();

   void setAdapter(String var1);

   String getAdapter();

   /** @deprecated */
   void setAdapterName(String var1);

   String getAdapterName();

   /** @deprecated */
   void setAdapterVersion(String var1);

   String getAdapterVersion();

   void setUserName(String var1);

   String getUserName();

   void setPassword(String var1);

   String getPassword();

   byte[] getPasswordEncrypted();

   void setPasswordEncrypted(byte[] var1);

   String[] getInstalledVMMAdapters();
}
