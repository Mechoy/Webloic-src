package weblogic.connector.external;

import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import weblogic.connector.external.impl.RAInfoImpl;
import weblogic.j2ee.descriptor.ConnectorBean;
import weblogic.j2ee.descriptor.wl.WeblogicConnectorBean;
import weblogic.j2ee.descriptor.wl.WorkManagerBean;

public interface RAInfo {
   RAInfo factory = RAInfoImpl.factoryHelper;

   RAInfo createRAInfo(ConnectorBean var1, WeblogicConnectorBean var2, URL var3, String var4);

   List getOutboundInfos();

   OutboundInfo getOutboundInfo(String var1);

   List getInboundInfos() throws ElementNotFoundException;

   String getRADescription();

   String getDisplayName();

   String getSmallIcon();

   String getLargeIcon();

   String getVendorName();

   String getEisType();

   String getRAVersion();

   String getLicenseDescription();

   String[] getLicenseDescriptions();

   boolean getLicenseRequired();

   String getRAClass();

   List getAdminObjs();

   String getNativeLibDir();

   Hashtable getRAConfigProps();

   String getLinkref();

   void setBaseRA(RAInfo var1);

   String getConnectionFactoryName();

   String getSpecVersion();

   String getJndiName();

   List getSecurityPermissions();

   URL getURL();

   boolean isEnableAccessOutsideApp();

   boolean isEnableGlobalAccessToClasses();

   SecurityIdentityInfo getSecurityIdentityInfo();

   WorkManagerBean getWorkManager();

   String getModuleName();

   ConnectorBean getConnectorBean();

   WeblogicConnectorBean getWeblogicConnectorBean();

   String[] getRADescriptions();

   AdminObjInfo getAdminObject(String var1);

   Hashtable getAdminObjectGroupProperties(String var1);

   Hashtable getConnectionGroupConfigProperties(String var1);

   String getConnectionGroupTransactionSupport(String var1);

   AuthMechInfo[] getConnectionGroupAuthenticationMechanisms(String var1);

   String getConnectionGroupResAuth(String var1);

   boolean isConnectionGroupReauthenticationSupport(String var1);

   LoggingInfo getConnectionGroupLoggingProperties(String var1);

   PoolInfo getConnectionGroupPoolProperties(String var1);
}
