package weblogic.connector.monitoring;

import java.security.AccessController;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import javax.management.j2ee.statistics.Stats;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.configuration.Configuration;
import weblogic.connector.configuration.ConfigurationFactory;
import weblogic.connector.external.OutboundInfo;
import weblogic.connector.external.RAInfo;
import weblogic.connector.monitoring.outbound.JCAStatsImpl;
import weblogic.connector.security.outbound.SecurityContext;
import weblogic.j2ee.ComponentRuntimeMBeanImpl;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ConnectorComponentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ConnectorComponentRuntimeMBean;
import weblogic.management.runtime.ConnectorConnectionPoolRuntimeMBean;
import weblogic.management.runtime.ConnectorInboundRuntimeMBean;
import weblogic.management.runtime.ConnectorServiceRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.EISResource;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.ErrorCollectionException;

public final class ConnectorComponentRuntimeMBeanImpl extends ComponentRuntimeMBeanImpl implements ConnectorComponentRuntimeMBean {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private Set connPoolRuntimes;
   private Set connInboundRuntimes;
   private ConnectorServiceRuntimeMBean connServiceRuntime;
   private RAInstanceManager raInstanceManager;

   public ConnectorComponentRuntimeMBeanImpl(String var1, String var2, RAInstanceManager var3, RuntimeMBean var4, ConnectorServiceRuntimeMBean var5) throws ManagementException {
      super(var1, var2, var4, false);
      this.connServiceRuntime = var5;
      this.raInstanceManager = var3;
      this.connPoolRuntimes = new HashSet(10);
      this.connInboundRuntimes = new HashSet(10);
      this.register();
   }

   public void suspendAll() throws ErrorCollectionException {
      if (this.raInstanceManager != null) {
         this.raInstanceManager.suspend(7, (Properties)null);
      }

   }

   public void resumeAll() throws ErrorCollectionException {
      if (this.raInstanceManager != null) {
         this.raInstanceManager.resume(7, (Properties)null);
      }

   }

   public void suspend(int var1) throws ErrorCollectionException {
      if (this.raInstanceManager != null) {
         this.raInstanceManager.suspend(var1, (Properties)null);
      }

   }

   public void suspend(int var1, Properties var2) throws ErrorCollectionException {
      if (this.raInstanceManager != null) {
         this.raInstanceManager.suspend(var1, var2);
      }

   }

   public void resume(int var1) throws ErrorCollectionException {
      if (this.raInstanceManager != null) {
         this.raInstanceManager.resume(var1, (Properties)null);
      }

   }

   public void resume(int var1, Properties var2) throws ErrorCollectionException {
      if (this.raInstanceManager != null) {
         this.raInstanceManager.resume(var1, var2);
      }

   }

   public void setRAInstanceManager(RAInstanceManager var1) {
      Debug.println(this, ".setRAInstanceManager() called.");
      this.raInstanceManager = var1;
   }

   public int getConnectionPoolCount() {
      return this.connPoolRuntimes.size();
   }

   public ConnectorConnectionPoolRuntimeMBean[] getConnectionPools() {
      return (ConnectorConnectionPoolRuntimeMBean[])((ConnectorConnectionPoolRuntimeMBean[])this.connPoolRuntimes.toArray(new ConnectorConnectionPoolRuntimeMBean[this.connPoolRuntimes.size()]));
   }

   public ConnectorConnectionPoolRuntimeMBean getConnectionPool(String var1) {
      boolean var2 = false;
      ConnectorConnectionPoolRuntimeMBean var3 = null;
      ConnectorConnectionPoolRuntimeMBean var4 = null;
      if (var1 != null && var1.length() > 0) {
         Iterator var5 = this.connPoolRuntimes.iterator();

         while(var5.hasNext() && !var2) {
            var3 = (ConnectorConnectionPoolRuntimeMBean)var5.next();
            if (var1.equals(var3.getKey())) {
               var4 = var3;
               var2 = true;
            }
         }
      }

      return var4;
   }

   public int getInboundConnectionsCount() {
      return this.connInboundRuntimes.size();
   }

   public ConnectorInboundRuntimeMBean[] getInboundConnections() {
      Iterator var1 = this.connInboundRuntimes.iterator();

      while(var1.hasNext()) {
         ConnectorInboundRuntimeMBean var2 = (ConnectorInboundRuntimeMBean)var1.next();
         if (var2 != null) {
            System.out.println("[ConnectorComponentRuntimeMBeanImpl.getInboundConnections()] inboundMBean = " + var2);
         } else {
            System.out.println("[ConnectorComponentRuntimeMBeanImpl.getInboundConnections()] inboundMBean = <NULL>");
         }
      }

      return (ConnectorInboundRuntimeMBean[])((ConnectorInboundRuntimeMBean[])this.connInboundRuntimes.toArray(new ConnectorInboundRuntimeMBean[this.connInboundRuntimes.size()]));
   }

   public ConnectorInboundRuntimeMBean getInboundConnection(String var1) {
      boolean var2 = false;
      ConnectorInboundRuntimeMBean var3 = null;
      ConnectorInboundRuntimeMBean var4 = null;
      if (var1 != null && var1.length() > 0) {
         Iterator var5 = this.connInboundRuntimes.iterator();

         while(var5.hasNext() && !var2) {
            var3 = (ConnectorInboundRuntimeMBean)var5.next();
            if (var1.equals(var3.getMsgListenerType())) {
               var4 = var3;
               var2 = true;
            }
         }
      }

      return var4;
   }

   public String getEISResourceId() {
      EISResource var1 = SecurityContext.getGlobalEISResource(this.raInstanceManager.getApplicationName(), this.raInstanceManager.getComponentName(), this.raInstanceManager.getRAInfo());
      return var1.toString();
   }

   public boolean addConnPoolRuntime(ConnectorConnectionPoolRuntimeMBean var1) {
      return this.connPoolRuntimes.add(var1);
   }

   public boolean removeConnPoolRuntime(ConnectorConnectionPoolRuntimeMBean var1) {
      return this.connPoolRuntimes.remove(var1);
   }

   public boolean addConnInboundRuntime(ConnectorInboundRuntimeMBean var1) {
      return this.connInboundRuntimes.add(var1);
   }

   public boolean removeConnInboundRuntime(ConnectorInboundRuntimeMBean var1) {
      return this.connInboundRuntimes.remove(var1);
   }

   public ConnectorServiceRuntimeMBean getConnectorServiceRuntime() {
      return this.connServiceRuntime;
   }

   public String getVersionId() {
      return this.raInstanceManager.getVersionId();
   }

   public String getActiveVersionId() {
      return this.raInstanceManager.getActiveVersion();
   }

   public boolean isVersioned() {
      return this.raInstanceManager.isVersioned();
   }

   public boolean isActiveVersion() {
      return this.raInstanceManager.isActiveVersion();
   }

   public String getJndiName() {
      return this.raInstanceManager.getJndiName();
   }

   public String getState() {
      return this.raInstanceManager.getState();
   }

   public int getSuspendedState() {
      return this.raInstanceManager.getSuspendedState();
   }

   public AppDeploymentMBean getAppDeploymentMBean() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      String var2 = this.getParent().getName();
      return var1.lookupAppDeployment(var2);
   }

   public ConnectorComponentMBean getConnectorComponentMBean() {
      return this.raInstanceManager.getConnectorComponentMBean();
   }

   public Properties getConfiguredProperties() {
      Properties var1 = new Properties();
      RAInfo var2 = this.raInstanceManager.getRAInfo();
      if (var2.getRADescription() != null) {
         var1.setProperty("Description", var2.getRADescription());
      }

      if (var2.getEisType() != null) {
         var1.setProperty("EisType", var2.getEisType());
      }

      if (var2.getSpecVersion() != null) {
         var1.setProperty("SpecVersion", var2.getSpecVersion());
      }

      if (var2.getVendorName() != null) {
         var1.setProperty("VendorName", var2.getVendorName());
      }

      if (var2.getRAVersion() != null) {
         var1.setProperty("Version", var2.getRAVersion());
      }

      if (var2.getLinkref() != null) {
         var1.setProperty("RaLinkRef", var2.getLinkref());
      }

      String var3 = "";
      String var4 = "";
      String var5 = "";
      Iterator var6 = var2.getOutboundInfos().iterator();

      for(int var7 = 0; var6.hasNext(); ++var7) {
         OutboundInfo var8 = (OutboundInfo)var6.next();
         if (var7 != 0) {
            var3 = var3 + ",";
            var4 = var4 + ",";
            var5 = var5 + ",";
         }

         var3 = var3 + var8.getMCFClass();
         var4 = var4 + var8.getJndiName();
         var5 = var5 + var8.getTransactionSupport();
      }

      if (var3 != null && var3.length() > 0) {
         var1.setProperty("MCFClassNames", var3);
      }

      if (var4 != null && var4.length() > 0) {
         var1.setProperty("OutboundJndiNames", var4);
      }

      if (var5 != null && var5.length() > 0) {
         var1.setProperty("TransactionSupports", var5);
      }

      return var1;
   }

   public String getSchema() {
      return ConfigurationFactory.getConfiguration(this.raInstanceManager.getRAInfo()).getSchema();
   }

   public String getSchema(String var1) {
      Configuration var2 = ConfigurationFactory.getConfiguration(var1, this.raInstanceManager.getRAInfo());
      return var2 != null ? var2.getSchema() : null;
   }

   public String getConfigurationVersion() {
      return "1.0";
   }

   public String getConfiguration() {
      return ConfigurationFactory.getConfiguration(this.raInstanceManager.getRAInfo()).getConfiguration();
   }

   public String getConfiguration(String var1) {
      Configuration var2 = ConfigurationFactory.getConfiguration(var1, this.raInstanceManager.getRAInfo());
      return var2 != null ? var2.getConfiguration() : null;
   }

   public String getDescription() {
      return this.raInstanceManager.getRAInfo().getRADescription();
   }

   public String[] getDescriptions() {
      return this.raInstanceManager.getRAInfo().getRADescriptions();
   }

   public String getEISType() {
      return this.raInstanceManager.getRAInfo().getEisType();
   }

   public String getSpecVersion() {
      return this.raInstanceManager.getRAInfo().getSpecVersion();
   }

   public String getVendorName() {
      return this.raInstanceManager.getRAInfo().getVendorName();
   }

   public String getVersion() {
      return this.raInstanceManager.getRAInfo().getRAVersion();
   }

   public String getLinkref() {
      return this.raInstanceManager.getRAInfo().getLinkref();
   }

   public String getComponentName() {
      return this.raInstanceManager.getComponentName();
   }

   public Stats getStats() {
      return new JCAStatsImpl(this);
   }
}
