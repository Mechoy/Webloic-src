package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

public interface MessagingBridgeMBean extends DeploymentMBean {
   String BRIDGE_POLICY_AUTO = "Automatic";
   String BRIDGE_POLICY_MANUAL = "Manual";
   String BRIDGE_POLICY_SCHEDULED = "Scheduled";
   String BRIDGE_QOS_EXACTLY_ONCE = "Exactly-once";
   String BRIDGE_QOS_ATMOST_ONCE = "Atmost-once";
   String BRIDGE_QOS_DUPLICATE_OKAY = "Duplicate-okay";

   BridgeDestinationCommonMBean getSourceDestination();

   void setSourceDestination(BridgeDestinationCommonMBean var1) throws InvalidAttributeValueException;

   BridgeDestinationCommonMBean getTargetDestination();

   void setTargetDestination(BridgeDestinationCommonMBean var1) throws InvalidAttributeValueException;

   String getSelector();

   void setSelector(String var1) throws InvalidAttributeValueException;

   String getForwardingPolicy();

   void setForwardingPolicy(String var1) throws InvalidAttributeValueException;

   String getScheduleTime();

   void setScheduleTime(String var1) throws InvalidAttributeValueException;

   String getQualityOfService();

   void setQualityOfService(String var1) throws InvalidAttributeValueException;

   boolean isQOSDegradationAllowed();

   void setQOSDegradationAllowed(boolean var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean isDurabilityDisabled();

   /** @deprecated */
   void setDurabilityDisabled(boolean var1) throws InvalidAttributeValueException;

   boolean isDurabilityEnabled();

   void setDurabilityEnabled(boolean var1) throws InvalidAttributeValueException;

   /** @deprecated */
   long getReconnectDelayInitialMilliseconds();

   /** @deprecated */
   void setReconnectDelayInitialMilliseconds(long var1) throws InvalidAttributeValueException;

   int getReconnectDelayMinimum();

   void setReconnectDelayMinimum(int var1) throws InvalidAttributeValueException;

   /** @deprecated */
   long getReconnectDelayIncrement();

   /** @deprecated */
   void setReconnectDelayIncrement(long var1) throws InvalidAttributeValueException;

   int getReconnectDelayIncrease();

   void setReconnectDelayIncrease(int var1) throws InvalidAttributeValueException;

   /** @deprecated */
   long getReconnectDelayMaximumMilliseconds();

   /** @deprecated */
   void setReconnectDelayMaximumMilliseconds(long var1) throws InvalidAttributeValueException;

   int getReconnectDelayMaximum();

   void setReconnectDelayMaximum(int var1) throws InvalidAttributeValueException;

   /** @deprecated */
   long getMaximumIdleTimeMilliseconds();

   /** @deprecated */
   void setMaximumIdleTimeMilliseconds(long var1) throws InvalidAttributeValueException;

   int getIdleTimeMaximum();

   void setIdleTimeMaximum(int var1) throws InvalidAttributeValueException;

   /** @deprecated */
   long getScanUnitMilliseconds();

   /** @deprecated */
   void setScanUnitMilliseconds(long var1) throws InvalidAttributeValueException;

   /** @deprecated */
   int getTransactionTimeoutSeconds();

   /** @deprecated */
   void setTransactionTimeoutSeconds(int var1) throws InvalidAttributeValueException;

   int getTransactionTimeout();

   void setTransactionTimeout(int var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean isAsyncDisabled();

   /** @deprecated */
   void setAsyncDisabled(boolean var1) throws InvalidAttributeValueException;

   boolean isAsyncEnabled();

   void setAsyncEnabled(boolean var1) throws InvalidAttributeValueException;

   boolean isStarted();

   void setStarted(boolean var1) throws InvalidAttributeValueException;

   int getBatchSize();

   void setBatchSize(int var1) throws InvalidAttributeValueException;

   long getBatchInterval();

   void setBatchInterval(long var1) throws InvalidAttributeValueException;

   boolean getPreserveMsgProperty();

   void setPreserveMsgProperty(boolean var1) throws InvalidAttributeValueException;
}
