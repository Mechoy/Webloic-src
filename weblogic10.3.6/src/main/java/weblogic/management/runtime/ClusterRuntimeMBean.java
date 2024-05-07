package weblogic.management.runtime;

import java.util.HashMap;
import weblogic.health.HealthFeedback;
import weblogic.health.HealthState;
import weblogic.management.configuration.MachineMBean;

public interface ClusterRuntimeMBean extends ReplicationRuntimeMBean, HealthFeedback {
   int getAliveServerCount();

   long getResendRequestsCount();

   long getFragmentsSentCount();

   long getFragmentsReceivedCount();

   /** @deprecated */
   String[] getSecondaryDistributionNames();

   long getMulticastMessagesLostCount();

   String[] getServerNames();

   long getForeignFragmentsDroppedCount();

   /** @deprecated */
   String getCurrentSecondaryServer();

   HashMap getUnreliableServers();

   HealthState getHealthState();

   MachineMBean getCurrentMachine();

   ServerMigrationRuntimeMBean getServerMigrationRuntime();

   JobSchedulerRuntimeMBean getJobSchedulerRuntime();

   UnicastMessagingRuntimeMBean getUnicastMessaging();

   String[] getActiveSingletonServices();
}
