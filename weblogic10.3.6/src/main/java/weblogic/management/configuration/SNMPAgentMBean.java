package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

public interface SNMPAgentMBean extends ConfigurationMBean {
   int DEBUG_NONE = 0;
   int DEBUG_FATAL = 1;
   int DEBUG_CRITICAL = 2;
   int DEBUG_NONCRITICAL = 3;
   int SNMPV1 = 1;
   int SNMPV2 = 2;
   int SNMPV3 = 3;
   String NO_AUTH = "noAuth";
   String MD5 = "MD5";
   String SHA = "SHA";
   String NO_PRIV = "noPriv";
   String DES = "DES";
   String AES_128 = "AES_128";

   boolean isEnabled();

   void setEnabled(boolean var1);

   boolean isSendAutomaticTrapsEnabled();

   void setSendAutomaticTrapsEnabled(boolean var1);

   int getSNMPPort();

   void setSNMPPort(int var1) throws InvalidAttributeValueException, ConfigurationException;

   int getSNMPTrapVersion();

   void setSNMPTrapVersion(int var1);

   /** @deprecated */
   int getMibDataRefreshInterval();

   void setMibDataRefreshInterval(int var1) throws InvalidAttributeValueException, ConfigurationException;

   /** @deprecated */
   int getServerStatusCheckIntervalFactor();

   void setServerStatusCheckIntervalFactor(int var1) throws InvalidAttributeValueException, ConfigurationException;

   String getCommunityPrefix();

   void setCommunityPrefix(String var1) throws InvalidAttributeValueException, ConfigurationException;

   String getUserDefinedMib();

   void setUserDefinedMib(String var1);

   /** @deprecated */
   int getDebugLevel();

   /** @deprecated */
   void setDebugLevel(int var1) throws InvalidAttributeValueException, ConfigurationException;

   /** @deprecated */
   SNMPTrapDestinationMBean[] getTargetedTrapDestinations();

   /** @deprecated */
   void setTargetedTrapDestinations(SNMPTrapDestinationMBean[] var1) throws InvalidAttributeValueException, ConfigurationException;

   /** @deprecated */
   boolean addTargetedTrapDestination(SNMPTrapDestinationMBean var1) throws InvalidAttributeValueException, ConfigurationException;

   /** @deprecated */
   boolean removeTargetedTrapDestination(SNMPTrapDestinationMBean var1) throws InvalidAttributeValueException, ConfigurationException;

   SNMPTrapDestinationMBean createSNMPTrapDestination(String var1);

   SNMPTrapDestinationMBean createSNMPTrapDestination(String var1, SNMPTrapDestinationMBean var2);

   void destroySNMPTrapDestination(SNMPTrapDestinationMBean var1);

   SNMPTrapDestinationMBean lookupSNMPTrapDestination(String var1);

   SNMPTrapDestinationMBean[] getSNMPTrapDestinations();

   /** @deprecated */
   boolean addSNMPTrapDestination(SNMPTrapDestinationMBean var1) throws InvalidAttributeValueException, ConfigurationException;

   SNMPProxyMBean[] getSNMPProxies();

   /** @deprecated */
   void setSNMPProxies(SNMPProxyMBean[] var1) throws InvalidAttributeValueException, ConfigurationException;

   /** @deprecated */
   boolean addSNMPProxy(SNMPProxyMBean var1) throws InvalidAttributeValueException, ConfigurationException;

   /** @deprecated */
   boolean removeSNMPProxy(SNMPProxyMBean var1) throws InvalidAttributeValueException, ConfigurationException;

   SNMPProxyMBean createSNMPProxy(String var1);

   SNMPProxyMBean createSNMPProxy(String var1, SNMPProxyMBean var2);

   void destroySNMPProxy(SNMPProxyMBean var1);

   SNMPProxyMBean lookupSNMPProxy(String var1);

   SNMPGaugeMonitorMBean[] getSNMPGaugeMonitors();

   /** @deprecated */
   void setSNMPGaugeMonitors(SNMPGaugeMonitorMBean[] var1) throws InvalidAttributeValueException, ConfigurationException;

   /** @deprecated */
   boolean addSNMPGaugeMonitor(SNMPGaugeMonitorMBean var1) throws InvalidAttributeValueException, ConfigurationException;

   /** @deprecated */
   boolean removeSNMPGaugeMonitor(SNMPGaugeMonitorMBean var1) throws InvalidAttributeValueException, ConfigurationException;

   SNMPGaugeMonitorMBean createSNMPGaugeMonitor(String var1);

   SNMPGaugeMonitorMBean createSNMPGaugeMonitor(String var1, SNMPGaugeMonitorMBean var2);

   void destroySNMPGaugeMonitor(SNMPGaugeMonitorMBean var1);

   SNMPGaugeMonitorMBean lookupSNMPGaugeMonitor(String var1);

   SNMPStringMonitorMBean[] getSNMPStringMonitors();

   /** @deprecated */
   void setSNMPStringMonitors(SNMPStringMonitorMBean[] var1) throws InvalidAttributeValueException, ConfigurationException;

   /** @deprecated */
   boolean addSNMPStringMonitor(SNMPStringMonitorMBean var1) throws InvalidAttributeValueException, ConfigurationException;

   /** @deprecated */
   boolean removeSNMPStringMonitor(SNMPStringMonitorMBean var1) throws InvalidAttributeValueException, ConfigurationException;

   SNMPStringMonitorMBean createSNMPStringMonitor(String var1);

   SNMPStringMonitorMBean createSNMPStringMonitor(String var1, SNMPStringMonitorMBean var2);

   void destroySNMPStringMonitor(SNMPStringMonitorMBean var1);

   SNMPStringMonitorMBean lookupSNMPStringMonitor(String var1);

   SNMPCounterMonitorMBean[] getSNMPCounterMonitors();

   /** @deprecated */
   void setSNMPCounterMonitors(SNMPCounterMonitorMBean[] var1) throws InvalidAttributeValueException, ConfigurationException;

   /** @deprecated */
   boolean addSNMPCounterMonitor(SNMPCounterMonitorMBean var1) throws InvalidAttributeValueException, ConfigurationException;

   /** @deprecated */
   boolean removeSNMPCounterMonitor(SNMPCounterMonitorMBean var1) throws InvalidAttributeValueException, ConfigurationException;

   SNMPCounterMonitorMBean createSNMPCounterMonitor(String var1);

   SNMPCounterMonitorMBean createSNMPCounterMonitor(String var1, SNMPCounterMonitorMBean var2);

   void destroySNMPCounterMonitor(SNMPCounterMonitorMBean var1);

   SNMPCounterMonitorMBean lookupSNMPCounterMonitor(String var1);

   SNMPLogFilterMBean[] getSNMPLogFilters();

   /** @deprecated */
   void setSNMPLogFilters(SNMPLogFilterMBean[] var1) throws InvalidAttributeValueException, ConfigurationException;

   /** @deprecated */
   boolean addSNMPLogFilter(SNMPLogFilterMBean var1) throws InvalidAttributeValueException, ConfigurationException;

   /** @deprecated */
   boolean removeSNMPLogFilter(SNMPLogFilterMBean var1) throws InvalidAttributeValueException, ConfigurationException;

   SNMPLogFilterMBean createSNMPLogFilter(String var1);

   SNMPLogFilterMBean createSNMPLogFilter(String var1, SNMPLogFilterMBean var2);

   void destroySNMPLogFilter(SNMPLogFilterMBean var1);

   SNMPLogFilterMBean lookupSNMPLogFilter(String var1);

   SNMPAttributeChangeMBean[] getSNMPAttributeChanges();

   /** @deprecated */
   void setSNMPAttributeChanges(SNMPAttributeChangeMBean[] var1) throws InvalidAttributeValueException, ConfigurationException;

   /** @deprecated */
   boolean addSNMPAttributeChange(SNMPAttributeChangeMBean var1) throws InvalidAttributeValueException, ConfigurationException;

   /** @deprecated */
   boolean removeSNMPAttributeChange(SNMPAttributeChangeMBean var1) throws InvalidAttributeValueException, ConfigurationException;

   SNMPAttributeChangeMBean createSNMPAttributeChange(String var1);

   SNMPAttributeChangeMBean createSNMPAttributeChange(String var1, SNMPAttributeChangeMBean var2);

   void destroySNMPAttributeChange(SNMPAttributeChangeMBean var1);

   SNMPAttributeChangeMBean lookupSNMPAttributeChange(String var1);

   boolean isCommunityBasedAccessEnabled();

   void setCommunityBasedAccessEnabled(boolean var1);

   String getSNMPEngineId();

   void setSNMPEngineId(String var1);

   String getAuthenticationProtocol();

   void setAuthenticationProtocol(String var1);

   String getPrivacyProtocol();

   void setPrivacyProtocol(String var1);

   int getInformRetryInterval();

   void setInformRetryInterval(int var1);

   int getMaxInformRetryCount();

   void setMaxInformRetryCount(int var1);

   long getLocalizedKeyCacheInvalidationInterval();

   void setLocalizedKeyCacheInvalidationInterval(long var1);

   boolean isSNMPAccessForUserMBeansEnabled();

   void setSNMPAccessForUserMBeansEnabled(boolean var1);

   boolean isInformEnabled();

   void setInformEnabled(boolean var1);

   int getMasterAgentXPort();

   void setMasterAgentXPort(int var1);
}
