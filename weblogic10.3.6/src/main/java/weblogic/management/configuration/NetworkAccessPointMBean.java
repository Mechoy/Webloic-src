package weblogic.management.configuration;

import java.util.Properties;
import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;

public interface NetworkAccessPointMBean extends ConfigurationMBean {
   String getName();

   void setName(String var1) throws InvalidAttributeValueException;

   String getProtocol();

   void setProtocol(String var1) throws InvalidAttributeValueException;

   String getListenAddress();

   void setListenAddress(String var1) throws InvalidAttributeValueException;

   String getPublicAddress();

   void setPublicAddress(String var1) throws InvalidAttributeValueException;

   int getListenPort();

   void setListenPort(int var1) throws InvalidAttributeValueException;

   int getPublicPort();

   void setPublicPort(int var1) throws InvalidAttributeValueException;

   String getProxyAddress();

   void setProxyAddress(String var1) throws InvalidAttributeValueException;

   int getProxyPort();

   void setProxyPort(int var1) throws InvalidAttributeValueException;

   boolean isHttpEnabledForThisProtocol();

   void setHttpEnabledForThisProtocol(boolean var1) throws InvalidAttributeValueException;

   int getAcceptBacklog();

   void setAcceptBacklog(int var1) throws InvalidAttributeValueException;

   int getMaxBackoffBetweenFailures();

   void setMaxBackoffBetweenFailures(int var1) throws InvalidAttributeValueException;

   int getLoginTimeoutMillis();

   void setLoginTimeoutMillis(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getTunnelingClientPingSecs();

   void setTunnelingClientPingSecs(int var1) throws InvalidAttributeValueException;

   int getTunnelingClientTimeoutSecs();

   void setTunnelingClientTimeoutSecs(int var1) throws InvalidAttributeValueException;

   boolean isTunnelingEnabled();

   void setTunnelingEnabled(boolean var1) throws InvalidAttributeValueException;

   int getCompleteMessageTimeout();

   void setCompleteMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   boolean getTimeoutConnectionWithPendingResponses();

   void setTimeoutConnectionWithPendingResponses(boolean var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getIdleConnectionTimeout();

   void setIdleConnectionTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getConnectTimeout();

   void setConnectTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getMaxMessageSize();

   void setMaxMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   boolean isOutboundEnabled();

   void setOutboundEnabled(boolean var1) throws InvalidAttributeValueException;

   int getChannelWeight();

   void setChannelWeight(int var1) throws InvalidAttributeValueException;

   String getClusterAddress();

   void setClusterAddress(String var1) throws InvalidAttributeValueException;

   boolean isEnabled();

   void setEnabled(boolean var1) throws InvalidAttributeValueException;

   int getMaxConnectedClients();

   void setMaxConnectedClients(int var1) throws InvalidAttributeValueException;

   boolean isTwoWaySSLEnabled();

   void setTwoWaySSLEnabled(boolean var1);

   boolean isChannelIdentityCustomized();

   void setChannelIdentityCustomized(boolean var1);

   String getCustomPrivateKeyAlias();

   void setCustomPrivateKeyAlias(String var1);

   String getPrivateKeyAlias();

   String getCustomPrivateKeyPassPhrase();

   void setCustomPrivateKeyPassPhrase(String var1);

   String getPrivateKeyPassPhrase();

   byte[] getCustomPrivateKeyPassPhraseEncrypted();

   void setCustomPrivateKeyPassPhraseEncrypted(byte[] var1);

   boolean isClientCertificateEnforced();

   void setClientCertificateEnforced(boolean var1);

   boolean isOutboundPrivateKeyEnabled();

   void setOutboundPrivateKeyEnabled(boolean var1) throws InvalidAttributeValueException;

   boolean getUseFastSerialization();

   void setUseFastSerialization(boolean var1) throws InvalidAttributeValueException;

   /** @deprecated */
   int getIdleIIOPConnectionTimeout();

   void setIdleIIOPConnectionTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   int getSSLListenPort();

   void setSSLListenPort(int var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getExternalDNSName();

   void setExternalDNSName(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   int getLoginTimeoutMillisSSL();

   void setLoginTimeoutMillisSSL(int var1) throws InvalidAttributeValueException;

   /** @deprecated */
   int getCompleteT3MessageTimeout();

   void setCompleteT3MessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   int getCompleteHTTPMessageTimeout();

   void setCompleteHTTPMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   int getCompleteCOMMessageTimeout();

   void setCompleteCOMMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   int getCompleteIIOPMessageTimeout();

   void setCompleteIIOPMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   void setCustomProperties(Properties var1);

   Properties getCustomProperties();

   boolean isSDPEnabled();

   void setSDPEnabled(boolean var1);

   String getOutboundPrivateKeyAlias();

   String getOutboundPrivateKeyPassPhrase();
}
