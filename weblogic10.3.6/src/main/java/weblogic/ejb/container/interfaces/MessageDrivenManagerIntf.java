package weblogic.ejb.container.interfaces;

import javax.ejb.MessageDrivenContext;
import javax.naming.Context;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.jms.extensions.DestinationDetail;
import weblogic.management.configuration.TargetMBean;

public interface MessageDrivenManagerIntf extends BeanManager {
   void setup(MessageDrivenBeanInfo var1, Context var2, String var3, String var4, String var5, String var6, TargetMBean var7, DestinationDetail var8) throws WLDeploymentException;

   void start() throws WLDeploymentException;

   void stop();

   String getDestinationName();

   String getJMSClientID();

   String getUniqueGlobalID();

   PoolIntf getPool();

   MessageDrivenContext getMessageDrivenContext();

   TargetMBean getTargetMBean();

   void resetMessageConsumer(boolean var1);

   void updateJMSPollingIntervalSeconds(int var1);

   boolean isAdvancedTopicSupported();

   boolean supportMultipleConncitons();

   int getDestinationType();

   String getCreateDestinationArgument();

   boolean isNoneDDMD();

   String getDDJNDIName();

   boolean needsSetForwardFilter();

   boolean isStarted();

   String getDDMemberName();

   String getMessageSelector();

   boolean isDurableSubscriptionDeletion();

   int getTopicMessagesDistributionMode();

   String getProviderURL();

   String getConnectionFactoryJNDIName();

   DestinationDetail getDestination();

   void enableDestination(DestinationDetail var1) throws WLDeploymentException;

   void setMDBStatus(String var1);

   String getResourceAdapterJndiName();

   boolean subscriptionDeletionRequired();
}
