package weblogic.management.runtime;

import java.util.Properties;
import javax.management.j2ee.statistics.Stats;
import weblogic.connector.ConnectorLogger;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ConnectorComponentMBean;
import weblogic.utils.ErrorCollectionException;

public interface ConnectorComponentRuntimeMBean extends ComponentRuntimeMBean {
   String NEW = ConnectorLogger.getStringNewLoggable().getMessageText();
   String INITIALIZED = ConnectorLogger.getStringInitializedLoggable().getMessageText();
   String PREPARED = ConnectorLogger.getStringPreparedLoggable().getMessageText();
   String ACTIVATED = ConnectorLogger.getStringActivatedLoggable().getMessageText();
   String SUSPENDED = ConnectorLogger.getStringSuspendedLoggable().getMessageText();

   int getConnectionPoolCount();

   ConnectorConnectionPoolRuntimeMBean[] getConnectionPools();

   ConnectorConnectionPoolRuntimeMBean getConnectionPool(String var1);

   int getInboundConnectionsCount();

   ConnectorInboundRuntimeMBean[] getInboundConnections();

   ConnectorInboundRuntimeMBean getInboundConnection(String var1);

   String getEISResourceId();

   void suspendAll() throws ErrorCollectionException;

   void suspend(int var1) throws ErrorCollectionException;

   void suspend(int var1, Properties var2) throws ErrorCollectionException;

   void resumeAll() throws ErrorCollectionException;

   void resume(int var1) throws ErrorCollectionException;

   void resume(int var1, Properties var2) throws ErrorCollectionException;

   Properties getConfiguredProperties();

   /** @deprecated */
   AppDeploymentMBean getAppDeploymentMBean();

   /** @deprecated */
   ConnectorComponentMBean getConnectorComponentMBean();

   ConnectorServiceRuntimeMBean getConnectorServiceRuntime();

   String getVersionId();

   String getActiveVersionId();

   boolean isVersioned();

   boolean isActiveVersion();

   String getJndiName();

   String getState();

   int getSuspendedState();

   String getSchema();

   String getSchema(String var1);

   String getConfigurationVersion();

   String getConfiguration();

   String getConfiguration(String var1);

   String getDescription();

   String[] getDescriptions();

   String getEISType();

   String getSpecVersion();

   String getVendorName();

   String getVersion();

   String getLinkref();

   String getComponentName();

   Stats getStats();
}
