package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;

public interface JMSServerMBean extends DeploymentMBean, TargetMBean, JMSConstants {
   TargetMBean[] getTargets();

   void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException;

   boolean addTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException;

   boolean removeTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   JMSSessionPoolMBean[] getSessionPools();

   /** @deprecated */
   void setSessionPools(JMSSessionPoolMBean[] var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean addSessionPool(JMSSessionPoolMBean var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   boolean removeSessionPool(JMSSessionPoolMBean var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   JMSSessionPoolMBean[] getJMSSessionPools();

   /** @deprecated */
   JMSSessionPoolMBean createJMSSessionPool(String var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   JMSSessionPoolMBean createJMSSessionPool(String var1, JMSSessionPoolMBean var2) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   void destroyJMSSessionPool(JMSSessionPoolMBean var1);

   /** @deprecated */
   JMSSessionPoolMBean lookupJMSSessionPool(String var1);

   /** @deprecated */
   JMSDestinationMBean[] getDestinations();

   /** @deprecated */
   void setDestinations(JMSDestinationMBean[] var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean addDestination(JMSDestinationMBean var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   boolean removeDestination(JMSDestinationMBean var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   JMSStoreMBean getStore();

   /** @deprecated */
   void setStore(JMSStoreMBean var1) throws InvalidAttributeValueException;

   PersistentStoreMBean getPersistentStore();

   void setPersistentStore(PersistentStoreMBean var1) throws InvalidAttributeValueException;

   boolean getStoreEnabled();

   void setStoreEnabled(boolean var1);

   boolean isAllowsPersistentDowngrade();

   void setAllowsPersistentDowngrade(boolean var1);

   /** @deprecated */
   JMSTemplateMBean getTemporaryTemplate();

   /** @deprecated */
   void setTemporaryTemplate(JMSTemplateMBean var1) throws InvalidAttributeValueException;

   boolean isHostingTemporaryDestinations();

   void setHostingTemporaryDestinations(boolean var1);

   String getTemporaryTemplateResource();

   void setTemporaryTemplateResource(String var1) throws InvalidAttributeValueException;

   String getTemporaryTemplateName();

   void setTemporaryTemplateName(String var1) throws InvalidAttributeValueException;

   long getBytesMaximum();

   void setBytesMaximum(long var1) throws InvalidAttributeValueException, DistributedManagementException;

   long getBytesThresholdHigh();

   void setBytesThresholdHigh(long var1) throws InvalidAttributeValueException, DistributedManagementException;

   long getBytesThresholdLow();

   void setBytesThresholdLow(long var1) throws InvalidAttributeValueException, DistributedManagementException;

   long getMessagesMaximum();

   void setMessagesMaximum(long var1) throws InvalidAttributeValueException, DistributedManagementException;

   long getMessagesThresholdHigh();

   void setMessagesThresholdHigh(long var1) throws InvalidAttributeValueException;

   long getMessagesThresholdLow();

   void setMessagesThresholdLow(long var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   boolean isJDBCStoreUpgradeEnabled();

   /** @deprecated */
   void setJDBCStoreUpgradeEnabled(boolean var1) throws InvalidAttributeValueException;

   /** @deprecated */
   JMSStoreMBean getPagingStore();

   /** @deprecated */
   void setPagingStore(JMSStoreMBean var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean isMessagesPagingEnabled();

   /** @deprecated */
   void setMessagesPagingEnabled(boolean var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean isBytesPagingEnabled();

   /** @deprecated */
   void setBytesPagingEnabled(boolean var1) throws InvalidAttributeValueException;

   long getMessageBufferSize();

   void setMessageBufferSize(long var1) throws InvalidAttributeValueException;

   String getPagingDirectory();

   void setPagingDirectory(String var1) throws InvalidAttributeValueException;

   boolean isPagingFileLockingEnabled();

   void setPagingFileLockingEnabled(boolean var1);

   int getPagingMinWindowBufferSize();

   void setPagingMinWindowBufferSize(int var1);

   int getPagingMaxWindowBufferSize();

   void setPagingMaxWindowBufferSize(int var1);

   int getPagingIoBufferSize();

   void setPagingIoBufferSize(int var1);

   long getPagingMaxFileSize();

   void setPagingMaxFileSize(long var1);

   int getPagingBlockSize();

   void setPagingBlockSize(int var1);

   void setExpirationScanInterval(int var1) throws InvalidAttributeValueException;

   int getExpirationScanInterval();

   int getMaximumMessageSize();

   void setMaximumMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   String getBlockingSendPolicy();

   void setBlockingSendPolicy(String var1) throws InvalidAttributeValueException;

   void setProductionPausedAtStartup(String var1) throws InvalidAttributeValueException;

   String getProductionPausedAtStartup();

   void setInsertionPausedAtStartup(String var1) throws InvalidAttributeValueException;

   String getInsertionPausedAtStartup();

   void setConsumptionPausedAtStartup(String var1) throws InvalidAttributeValueException;

   String getConsumptionPausedAtStartup();

   /** @deprecated */
   JMSQueueMBean[] getJMSQueues();

   /** @deprecated */
   JMSQueueMBean createJMSQueue(String var1);

   /** @deprecated */
   JMSQueueMBean createJMSQueue(String var1, JMSQueueMBean var2);

   /** @deprecated */
   void destroyJMSQueue(JMSQueueMBean var1);

   /** @deprecated */
   JMSQueueMBean lookupJMSQueue(String var1);

   /** @deprecated */
   JMSTopicMBean[] getJMSTopics();

   /** @deprecated */
   JMSTopicMBean createJMSTopic(String var1);

   /** @deprecated */
   JMSTopicMBean createJMSTopic(String var1, JMSTopicMBean var2);

   /** @deprecated */
   void destroyJMSTopic(JMSTopicMBean var1);

   /** @deprecated */
   JMSTopicMBean lookupJMSTopic(String var1);

   /** @deprecated */
   JMSMessageLogFileMBean getJMSMessageLogFile();

   void useDelegates(DomainMBean var1);
}
