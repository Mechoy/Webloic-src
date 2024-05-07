package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;

public interface JTAMBean extends ConfigurationMBean {
   String SSLNOTREQUIRED = "SSLNotRequired";
   String SSLREQUIRED = "SSLRequired";
   String CLIENTCERTREQUIRED = "ClientCertRequired";

   int getTimeoutSeconds();

   void setTimeoutSeconds(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getAbandonTimeoutSeconds();

   void setAbandonTimeoutSeconds(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getCompletionTimeoutSeconds();

   void setCompletionTimeoutSeconds(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   boolean getForgetHeuristics();

   void setForgetHeuristics(boolean var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getBeforeCompletionIterationLimit();

   void setBeforeCompletionIterationLimit(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getMaxTransactions();

   void setMaxTransactions(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getMaxUniqueNameStatistics();

   void setMaxUniqueNameStatistics(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getMaxResourceRequestsOnServer();

   void setMaxResourceRequestsOnServer(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   long getMaxXACallMillis();

   void setMaxXACallMillis(long var1) throws InvalidAttributeValueException, DistributedManagementException;

   long getMaxResourceUnavailableMillis();

   void setMaxResourceUnavailableMillis(long var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   long getRecoveryThresholdMillis();

   /** @deprecated */
   void setRecoveryThresholdMillis(long var1);

   int getMigrationCheckpointIntervalSeconds();

   void setMigrationCheckpointIntervalSeconds(int var1);

   long getMaxTransactionsHealthIntervalMillis();

   void setMaxTransactionsHealthIntervalMillis(long var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getPurgeResourceFromCheckpointIntervalSeconds();

   void setPurgeResourceFromCheckpointIntervalSeconds(int var1);

   int getCheckpointIntervalSeconds();

   void setCheckpointIntervalSeconds(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   long getSerializeEnlistmentsGCIntervalMillis();

   void setSerializeEnlistmentsGCIntervalMillis(long var1) throws InvalidAttributeValueException, DistributedManagementException;

   boolean getParallelXAEnabled();

   void setParallelXAEnabled(boolean var1) throws InvalidAttributeValueException, DistributedManagementException;

   String getParallelXADispatchPolicy();

   void setParallelXADispatchPolicy(String var1);

   int getUnregisterResourceGracePeriod();

   void setUnregisterResourceGracePeriod(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   String getSecurityInteropMode();

   void setSecurityInteropMode(String var1) throws InvalidAttributeValueException, DistributedManagementException;

   String getWSATTransportSecurityMode();

   void setWSATTransportSecurityMode(String var1) throws InvalidAttributeValueException, DistributedManagementException;

   boolean isWSATIssuedTokenEnabled();

   void setWSATIssuedTokenEnabled(boolean var1) throws InvalidAttributeValueException, DistributedManagementException;

   boolean isTwoPhaseEnabled();

   void setTwoPhaseEnabled(boolean var1) throws InvalidAttributeValueException, DistributedManagementException;
}
