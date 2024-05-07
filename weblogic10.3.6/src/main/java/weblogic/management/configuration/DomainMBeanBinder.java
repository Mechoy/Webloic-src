package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class DomainMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private DomainMBeanImpl bean;

   protected DomainMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (DomainMBeanImpl)var1;
   }

   public DomainMBeanBinder() {
      super(new DomainMBeanImpl());
      this.bean = (DomainMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("AdminConsole")) {
                  try {
                     this.bean.setAdminConsole((AdminConsoleMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var124) {
                     System.out.println("Warning: multiple definitions with same name: " + var124.getMessage());
                  }
               } else if (var1.equals("AdminServerMBean")) {
                  try {
                     this.bean.setAdminServerMBean((AdminServerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var123) {
                     System.out.println("Warning: multiple definitions with same name: " + var123.getMessage());
                  }
               } else if (var1.equals("AdminServerName")) {
                  try {
                     this.bean.setAdminServerName((String)var2);
                  } catch (BeanAlreadyExistsException var122) {
                     System.out.println("Warning: multiple definitions with same name: " + var122.getMessage());
                  }
               } else if (var1.equals("AdministrationPort")) {
                  try {
                     this.bean.setAdministrationPort(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var121) {
                     System.out.println("Warning: multiple definitions with same name: " + var121.getMessage());
                  }
               } else if (var1.equals("AdministrationProtocol")) {
                  try {
                     this.bean.setAdministrationProtocol((String)var2);
                  } catch (BeanAlreadyExistsException var120) {
                     System.out.println("Warning: multiple definitions with same name: " + var120.getMessage());
                  }
               } else if (var1.equals("AppDeployment")) {
                  try {
                     this.bean.addAppDeployment((AppDeploymentMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var119) {
                     System.out.println("Warning: multiple definitions with same name: " + var119.getMessage());
                     this.bean.removeAppDeployment((AppDeploymentMBean)var119.getExistingBean());
                     this.bean.addAppDeployment((AppDeploymentMBean)((AbstractDescriptorBean)((AppDeploymentMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("Application")) {
                  try {
                     this.bean.addApplication((ApplicationMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var118) {
                     System.out.println("Warning: multiple definitions with same name: " + var118.getMessage());
                     this.bean.removeApplication((ApplicationMBean)var118.getExistingBean());
                     this.bean.addApplication((ApplicationMBean)((AbstractDescriptorBean)((ApplicationMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("ArchiveConfigurationCount")) {
                  try {
                     this.bean.setArchiveConfigurationCount(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var117) {
                     System.out.println("Warning: multiple definitions with same name: " + var117.getMessage());
                  }
               } else if (var1.equals("BridgeDestination")) {
                  try {
                     this.bean.addBridgeDestination((BridgeDestinationMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var116) {
                     System.out.println("Warning: multiple definitions with same name: " + var116.getMessage());
                     this.bean.removeBridgeDestination((BridgeDestinationMBean)var116.getExistingBean());
                     this.bean.addBridgeDestination((BridgeDestinationMBean)((AbstractDescriptorBean)((BridgeDestinationMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("CachingRealm")) {
                  try {
                     this.bean.addCachingRealm((CachingRealmMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var115) {
                     System.out.println("Warning: multiple definitions with same name: " + var115.getMessage());
                     this.bean.removeCachingRealm((CachingRealmMBean)var115.getExistingBean());
                     this.bean.addCachingRealm((CachingRealmMBean)((AbstractDescriptorBean)((CachingRealmMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("Cluster")) {
                  try {
                     this.bean.addCluster((ClusterMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var114) {
                     System.out.println("Warning: multiple definitions with same name: " + var114.getMessage());
                     this.bean.removeCluster((ClusterMBean)var114.getExistingBean());
                     this.bean.addCluster((ClusterMBean)((AbstractDescriptorBean)((ClusterMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("CoherenceClusterSystemResource")) {
                  try {
                     this.bean.addCoherenceClusterSystemResource((CoherenceClusterSystemResourceMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var113) {
                     System.out.println("Warning: multiple definitions with same name: " + var113.getMessage());
                     this.bean.removeCoherenceClusterSystemResource((CoherenceClusterSystemResourceMBean)var113.getExistingBean());
                     this.bean.addCoherenceClusterSystemResource((CoherenceClusterSystemResourceMBean)((AbstractDescriptorBean)((CoherenceClusterSystemResourceMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("CoherenceServer")) {
                  try {
                     this.bean.addCoherenceServer((CoherenceServerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var112) {
                     System.out.println("Warning: multiple definitions with same name: " + var112.getMessage());
                     this.bean.removeCoherenceServer((CoherenceServerMBean)var112.getExistingBean());
                     this.bean.addCoherenceServer((CoherenceServerMBean)((AbstractDescriptorBean)((CoherenceServerMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("ConfigurationAuditType")) {
                  try {
                     this.bean.setConfigurationAuditType((String)var2);
                  } catch (BeanAlreadyExistsException var111) {
                     System.out.println("Warning: multiple definitions with same name: " + var111.getMessage());
                  }
               } else if (var1.equals("ConfigurationVersion")) {
                  try {
                     this.bean.setConfigurationVersion((String)var2);
                  } catch (BeanAlreadyExistsException var110) {
                     System.out.println("Warning: multiple definitions with same name: " + var110.getMessage());
                  }
               } else if (var1.equals("ConsoleContextPath")) {
                  try {
                     this.bean.setConsoleContextPath((String)var2);
                  } catch (BeanAlreadyExistsException var109) {
                     System.out.println("Warning: multiple definitions with same name: " + var109.getMessage());
                  }
               } else if (var1.equals("ConsoleExtensionDirectory")) {
                  try {
                     this.bean.setConsoleExtensionDirectory((String)var2);
                  } catch (BeanAlreadyExistsException var108) {
                     System.out.println("Warning: multiple definitions with same name: " + var108.getMessage());
                  }
               } else if (var1.equals("CustomRealm")) {
                  try {
                     this.bean.addCustomRealm((CustomRealmMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var107) {
                     System.out.println("Warning: multiple definitions with same name: " + var107.getMessage());
                     this.bean.removeCustomRealm((CustomRealmMBean)var107.getExistingBean());
                     this.bean.addCustomRealm((CustomRealmMBean)((AbstractDescriptorBean)((CustomRealmMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("CustomResource")) {
                  try {
                     this.bean.addCustomResource((CustomResourceMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var106) {
                     System.out.println("Warning: multiple definitions with same name: " + var106.getMessage());
                     this.bean.removeCustomResource((CustomResourceMBean)var106.getExistingBean());
                     this.bean.addCustomResource((CustomResourceMBean)((AbstractDescriptorBean)((CustomResourceMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("DeploymentConfiguration")) {
                  try {
                     this.bean.setDeploymentConfiguration((DeploymentConfigurationMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var105) {
                     System.out.println("Warning: multiple definitions with same name: " + var105.getMessage());
                  }
               } else if (var1.equals("DomainLibrary")) {
                  try {
                     this.bean.addDomainLibrary((DomainLibraryMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var104) {
                     System.out.println("Warning: multiple definitions with same name: " + var104.getMessage());
                     this.bean.removeDomainLibrary((DomainLibraryMBean)var104.getExistingBean());
                     this.bean.addDomainLibrary((DomainLibraryMBean)((AbstractDescriptorBean)((DomainLibraryMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("DomainLogFilter")) {
                  try {
                     this.bean.addDomainLogFilter((DomainLogFilterMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var103) {
                     System.out.println("Warning: multiple definitions with same name: " + var103.getMessage());
                     this.bean.removeDomainLogFilter((DomainLogFilterMBean)var103.getExistingBean());
                     this.bean.addDomainLogFilter((DomainLogFilterMBean)((AbstractDescriptorBean)((DomainLogFilterMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("DomainVersion")) {
                  try {
                     this.bean.setDomainVersion((String)var2);
                  } catch (BeanAlreadyExistsException var102) {
                     System.out.println("Warning: multiple definitions with same name: " + var102.getMessage());
                  }
               } else if (var1.equals("EJBContainer")) {
                  try {
                     this.bean.setEJBContainer((EJBContainerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var101) {
                     System.out.println("Warning: multiple definitions with same name: " + var101.getMessage());
                  }
               } else if (var1.equals("EmbeddedLDAP")) {
                  try {
                     this.bean.setEmbeddedLDAP((EmbeddedLDAPMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var100) {
                     System.out.println("Warning: multiple definitions with same name: " + var100.getMessage());
                  }
               } else if (var1.equals("ErrorHandling")) {
                  try {
                     this.bean.addErrorHandling((ErrorHandlingMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var99) {
                     System.out.println("Warning: multiple definitions with same name: " + var99.getMessage());
                     this.bean.removeErrorHandling((ErrorHandlingMBean)var99.getExistingBean());
                     this.bean.addErrorHandling((ErrorHandlingMBean)((AbstractDescriptorBean)((ErrorHandlingMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("FileRealm")) {
                  try {
                     this.bean.addFileRealm((FileRealmMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var98) {
                     System.out.println("Warning: multiple definitions with same name: " + var98.getMessage());
                     this.bean.removeFileRealm((FileRealmMBean)var98.getExistingBean());
                     this.bean.addFileRealm((FileRealmMBean)((AbstractDescriptorBean)((FileRealmMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("FileStore")) {
                  try {
                     this.bean.addFileStore((FileStoreMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var97) {
                     System.out.println("Warning: multiple definitions with same name: " + var97.getMessage());
                     this.bean.removeFileStore((FileStoreMBean)var97.getExistingBean());
                     this.bean.addFileStore((FileStoreMBean)((AbstractDescriptorBean)((FileStoreMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("FileT3")) {
                  try {
                     this.bean.addFileT3((FileT3MBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var96) {
                     System.out.println("Warning: multiple definitions with same name: " + var96.getMessage());
                     this.bean.removeFileT3((FileT3MBean)var96.getExistingBean());
                     this.bean.addFileT3((FileT3MBean)((AbstractDescriptorBean)((FileT3MBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("ForeignJMSConnectionFactory")) {
                  try {
                     this.bean.addForeignJMSConnectionFactory((ForeignJMSConnectionFactoryMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var95) {
                     System.out.println("Warning: multiple definitions with same name: " + var95.getMessage());
                     this.bean.removeForeignJMSConnectionFactory((ForeignJMSConnectionFactoryMBean)var95.getExistingBean());
                     this.bean.addForeignJMSConnectionFactory((ForeignJMSConnectionFactoryMBean)((AbstractDescriptorBean)((ForeignJMSConnectionFactoryMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("ForeignJMSDestination")) {
                  try {
                     this.bean.addForeignJMSDestination((ForeignJMSDestinationMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var94) {
                     System.out.println("Warning: multiple definitions with same name: " + var94.getMessage());
                     this.bean.removeForeignJMSDestination((ForeignJMSDestinationMBean)var94.getExistingBean());
                     this.bean.addForeignJMSDestination((ForeignJMSDestinationMBean)((AbstractDescriptorBean)((ForeignJMSDestinationMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("ForeignJMSServer")) {
                  try {
                     this.bean.addForeignJMSServer((ForeignJMSServerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var93) {
                     System.out.println("Warning: multiple definitions with same name: " + var93.getMessage());
                     this.bean.removeForeignJMSServer((ForeignJMSServerMBean)var93.getExistingBean());
                     this.bean.addForeignJMSServer((ForeignJMSServerMBean)((AbstractDescriptorBean)((ForeignJMSServerMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("ForeignJNDIProvider")) {
                  try {
                     this.bean.addForeignJNDIProvider((ForeignJNDIProviderMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var92) {
                     System.out.println("Warning: multiple definitions with same name: " + var92.getMessage());
                     this.bean.removeForeignJNDIProvider((ForeignJNDIProviderMBean)var92.getExistingBean());
                     this.bean.addForeignJNDIProvider((ForeignJNDIProviderMBean)((AbstractDescriptorBean)((ForeignJNDIProviderMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JDBCConnectionPool")) {
                  try {
                     this.bean.addJDBCConnectionPool((JDBCConnectionPoolMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var91) {
                     System.out.println("Warning: multiple definitions with same name: " + var91.getMessage());
                     this.bean.removeJDBCConnectionPool((JDBCConnectionPoolMBean)var91.getExistingBean());
                     this.bean.addJDBCConnectionPool((JDBCConnectionPoolMBean)((AbstractDescriptorBean)((JDBCConnectionPoolMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JDBCDataSourceFactory")) {
                  try {
                     this.bean.addJDBCDataSourceFactory((JDBCDataSourceFactoryMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var90) {
                     System.out.println("Warning: multiple definitions with same name: " + var90.getMessage());
                     this.bean.removeJDBCDataSourceFactory((JDBCDataSourceFactoryMBean)var90.getExistingBean());
                     this.bean.addJDBCDataSourceFactory((JDBCDataSourceFactoryMBean)((AbstractDescriptorBean)((JDBCDataSourceFactoryMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JDBCDataSource")) {
                  try {
                     this.bean.addJDBCDataSource((JDBCDataSourceMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var89) {
                     System.out.println("Warning: multiple definitions with same name: " + var89.getMessage());
                     this.bean.removeJDBCDataSource((JDBCDataSourceMBean)var89.getExistingBean());
                     this.bean.addJDBCDataSource((JDBCDataSourceMBean)((AbstractDescriptorBean)((JDBCDataSourceMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JDBCMultiPool")) {
                  try {
                     this.bean.addJDBCMultiPool((JDBCMultiPoolMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var88) {
                     System.out.println("Warning: multiple definitions with same name: " + var88.getMessage());
                     this.bean.removeJDBCMultiPool((JDBCMultiPoolMBean)var88.getExistingBean());
                     this.bean.addJDBCMultiPool((JDBCMultiPoolMBean)((AbstractDescriptorBean)((JDBCMultiPoolMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JDBCStore")) {
                  try {
                     this.bean.addJDBCStore((JDBCStoreMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var87) {
                     System.out.println("Warning: multiple definitions with same name: " + var87.getMessage());
                     this.bean.removeJDBCStore((JDBCStoreMBean)var87.getExistingBean());
                     this.bean.addJDBCStore((JDBCStoreMBean)((AbstractDescriptorBean)((JDBCStoreMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JDBCSystemResource")) {
                  try {
                     this.bean.addJDBCSystemResource((JDBCSystemResourceMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var86) {
                     System.out.println("Warning: multiple definitions with same name: " + var86.getMessage());
                     this.bean.removeJDBCSystemResource((JDBCSystemResourceMBean)var86.getExistingBean());
                     this.bean.addJDBCSystemResource((JDBCSystemResourceMBean)((AbstractDescriptorBean)((JDBCSystemResourceMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JDBCTxDataSource")) {
                  try {
                     this.bean.addJDBCTxDataSource((JDBCTxDataSourceMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var85) {
                     System.out.println("Warning: multiple definitions with same name: " + var85.getMessage());
                     this.bean.removeJDBCTxDataSource((JDBCTxDataSourceMBean)var85.getExistingBean());
                     this.bean.addJDBCTxDataSource((JDBCTxDataSourceMBean)((AbstractDescriptorBean)((JDBCTxDataSourceMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSBridgeDestination")) {
                  try {
                     this.bean.addJMSBridgeDestination((JMSBridgeDestinationMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var84) {
                     System.out.println("Warning: multiple definitions with same name: " + var84.getMessage());
                     this.bean.removeJMSBridgeDestination((JMSBridgeDestinationMBean)var84.getExistingBean());
                     this.bean.addJMSBridgeDestination((JMSBridgeDestinationMBean)((AbstractDescriptorBean)((JMSBridgeDestinationMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSConnectionConsumer")) {
                  try {
                     this.bean.addJMSConnectionConsumer((JMSConnectionConsumerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var83) {
                     System.out.println("Warning: multiple definitions with same name: " + var83.getMessage());
                     this.bean.removeJMSConnectionConsumer((JMSConnectionConsumerMBean)var83.getExistingBean());
                     this.bean.addJMSConnectionConsumer((JMSConnectionConsumerMBean)((AbstractDescriptorBean)((JMSConnectionConsumerMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSConnectionFactory")) {
                  try {
                     this.bean.addJMSConnectionFactory((JMSConnectionFactoryMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var82) {
                     System.out.println("Warning: multiple definitions with same name: " + var82.getMessage());
                     this.bean.removeJMSConnectionFactory((JMSConnectionFactoryMBean)var82.getExistingBean());
                     this.bean.addJMSConnectionFactory((JMSConnectionFactoryMBean)((AbstractDescriptorBean)((JMSConnectionFactoryMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSDestinationKey")) {
                  try {
                     this.bean.addJMSDestinationKey((JMSDestinationKeyMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var81) {
                     System.out.println("Warning: multiple definitions with same name: " + var81.getMessage());
                     this.bean.removeJMSDestinationKey((JMSDestinationKeyMBean)var81.getExistingBean());
                     this.bean.addJMSDestinationKey((JMSDestinationKeyMBean)((AbstractDescriptorBean)((JMSDestinationKeyMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSDestinations")) {
                  this.handleDeprecatedProperty("JMSDestinations", "9.0.0.0");
                  this.bean.setJMSDestinationsAsString((String)var2);
               } else if (var1.equals("JMSDistributedQueueMember")) {
                  try {
                     this.bean.addJMSDistributedQueueMember((JMSDistributedQueueMemberMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var80) {
                     System.out.println("Warning: multiple definitions with same name: " + var80.getMessage());
                     this.bean.removeJMSDistributedQueueMember((JMSDistributedQueueMemberMBean)var80.getExistingBean());
                     this.bean.addJMSDistributedQueueMember((JMSDistributedQueueMemberMBean)((AbstractDescriptorBean)((JMSDistributedQueueMemberMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSDistributedQueue")) {
                  try {
                     this.bean.addJMSDistributedQueue((JMSDistributedQueueMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var79) {
                     System.out.println("Warning: multiple definitions with same name: " + var79.getMessage());
                     this.bean.removeJMSDistributedQueue((JMSDistributedQueueMBean)var79.getExistingBean());
                     this.bean.addJMSDistributedQueue((JMSDistributedQueueMBean)((AbstractDescriptorBean)((JMSDistributedQueueMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSDistributedTopicMember")) {
                  try {
                     this.bean.addJMSDistributedTopicMember((JMSDistributedTopicMemberMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var78) {
                     System.out.println("Warning: multiple definitions with same name: " + var78.getMessage());
                     this.bean.removeJMSDistributedTopicMember((JMSDistributedTopicMemberMBean)var78.getExistingBean());
                     this.bean.addJMSDistributedTopicMember((JMSDistributedTopicMemberMBean)((AbstractDescriptorBean)((JMSDistributedTopicMemberMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSDistributedTopic")) {
                  try {
                     this.bean.addJMSDistributedTopic((JMSDistributedTopicMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var77) {
                     System.out.println("Warning: multiple definitions with same name: " + var77.getMessage());
                     this.bean.removeJMSDistributedTopic((JMSDistributedTopicMBean)var77.getExistingBean());
                     this.bean.addJMSDistributedTopic((JMSDistributedTopicMBean)((AbstractDescriptorBean)((JMSDistributedTopicMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSFileStore")) {
                  try {
                     this.bean.addJMSFileStore((JMSFileStoreMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var76) {
                     System.out.println("Warning: multiple definitions with same name: " + var76.getMessage());
                     this.bean.removeJMSFileStore((JMSFileStoreMBean)var76.getExistingBean());
                     this.bean.addJMSFileStore((JMSFileStoreMBean)((AbstractDescriptorBean)((JMSFileStoreMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSInteropModule")) {
                  try {
                     this.bean.addJMSInteropModule((JMSInteropModuleMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var75) {
                     System.out.println("Warning: multiple definitions with same name: " + var75.getMessage());
                     this.bean.removeJMSInteropModule((JMSInteropModuleMBean)var75.getExistingBean());
                     this.bean.addJMSInteropModule((JMSInteropModuleMBean)((AbstractDescriptorBean)((JMSInteropModuleMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSJDBCStore")) {
                  try {
                     this.bean.addJMSJDBCStore((JMSJDBCStoreMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var74) {
                     System.out.println("Warning: multiple definitions with same name: " + var74.getMessage());
                     this.bean.removeJMSJDBCStore((JMSJDBCStoreMBean)var74.getExistingBean());
                     this.bean.addJMSJDBCStore((JMSJDBCStoreMBean)((AbstractDescriptorBean)((JMSJDBCStoreMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSQueue")) {
                  try {
                     this.bean.addJMSQueue((JMSQueueMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var73) {
                     System.out.println("Warning: multiple definitions with same name: " + var73.getMessage());
                     this.bean.removeJMSQueue((JMSQueueMBean)var73.getExistingBean());
                     this.bean.addJMSQueue((JMSQueueMBean)((AbstractDescriptorBean)((JMSQueueMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSServer")) {
                  try {
                     this.bean.addJMSServer((JMSServerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var72) {
                     System.out.println("Warning: multiple definitions with same name: " + var72.getMessage());
                     this.bean.removeJMSServer((JMSServerMBean)var72.getExistingBean());
                     this.bean.addJMSServer((JMSServerMBean)((AbstractDescriptorBean)((JMSServerMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSSessionPool")) {
                  try {
                     this.bean.addJMSSessionPool((JMSSessionPoolMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var71) {
                     System.out.println("Warning: multiple definitions with same name: " + var71.getMessage());
                     this.bean.removeJMSSessionPool((JMSSessionPoolMBean)var71.getExistingBean());
                     this.bean.addJMSSessionPool((JMSSessionPoolMBean)((AbstractDescriptorBean)((JMSSessionPoolMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSSystemResource")) {
                  try {
                     this.bean.addJMSSystemResource((JMSSystemResourceMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var70) {
                     System.out.println("Warning: multiple definitions with same name: " + var70.getMessage());
                     this.bean.removeJMSSystemResource((JMSSystemResourceMBean)var70.getExistingBean());
                     this.bean.addJMSSystemResource((JMSSystemResourceMBean)((AbstractDescriptorBean)((JMSSystemResourceMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSTemplate")) {
                  try {
                     this.bean.addJMSTemplate((JMSTemplateMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var69) {
                     System.out.println("Warning: multiple definitions with same name: " + var69.getMessage());
                     this.bean.removeJMSTemplate((JMSTemplateMBean)var69.getExistingBean());
                     this.bean.addJMSTemplate((JMSTemplateMBean)((AbstractDescriptorBean)((JMSTemplateMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMSTopic")) {
                  try {
                     this.bean.addJMSTopic((JMSTopicMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var68) {
                     System.out.println("Warning: multiple definitions with same name: " + var68.getMessage());
                     this.bean.removeJMSTopic((JMSTopicMBean)var68.getExistingBean());
                     this.bean.addJMSTopic((JMSTopicMBean)((AbstractDescriptorBean)((JMSTopicMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JMX")) {
                  try {
                     this.bean.setJMX((JMXMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var67) {
                     System.out.println("Warning: multiple definitions with same name: " + var67.getMessage());
                  }
               } else if (var1.equals("JPA")) {
                  try {
                     this.bean.setJPA((JPAMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var66) {
                     System.out.println("Warning: multiple definitions with same name: " + var66.getMessage());
                  }
               } else if (var1.equals("JTA")) {
                  try {
                     this.bean.setJTA((JTAMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var65) {
                     System.out.println("Warning: multiple definitions with same name: " + var65.getMessage());
                  }
               } else if (var1.equals("JoltConnectionPool")) {
                  try {
                     this.bean.addJoltConnectionPool((JoltConnectionPoolMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var64) {
                     System.out.println("Warning: multiple definitions with same name: " + var64.getMessage());
                     this.bean.removeJoltConnectionPool((JoltConnectionPoolMBean)var64.getExistingBean());
                     this.bean.addJoltConnectionPool((JoltConnectionPoolMBean)((AbstractDescriptorBean)((JoltConnectionPoolMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("LDAPRealm")) {
                  try {
                     this.bean.addLDAPRealm((LDAPRealmMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var63) {
                     System.out.println("Warning: multiple definitions with same name: " + var63.getMessage());
                     this.bean.removeLDAPRealm((LDAPRealmMBean)var63.getExistingBean());
                     this.bean.addLDAPRealm((LDAPRealmMBean)((AbstractDescriptorBean)((LDAPRealmMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else {
                  if (var1.equals("LastModificationTime")) {
                     throw new AssertionError("can't set read-only property LastModificationTime");
                  }

                  if (var1.equals("Library")) {
                     try {
                        this.bean.addLibrary((LibraryMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var62) {
                        System.out.println("Warning: multiple definitions with same name: " + var62.getMessage());
                        this.bean.removeLibrary((LibraryMBean)var62.getExistingBean());
                        this.bean.addLibrary((LibraryMBean)((AbstractDescriptorBean)((LibraryMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                     }
                  } else if (var1.equals("Log")) {
                     try {
                        this.bean.setLog((LogMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var61) {
                        System.out.println("Warning: multiple definitions with same name: " + var61.getMessage());
                     }
                  } else if (var1.equals("LogFilter")) {
                     try {
                        this.bean.addLogFilter((LogFilterMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var60) {
                        System.out.println("Warning: multiple definitions with same name: " + var60.getMessage());
                        this.bean.removeLogFilter((LogFilterMBean)var60.getExistingBean());
                        this.bean.addLogFilter((LogFilterMBean)((AbstractDescriptorBean)((LogFilterMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                     }
                  } else if (var1.equals("Machine")) {
                     try {
                        this.bean.addMachine((MachineMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var59) {
                        System.out.println("Warning: multiple definitions with same name: " + var59.getMessage());
                        this.bean.removeMachine((MachineMBean)var59.getExistingBean());
                        this.bean.addMachine((MachineMBean)((AbstractDescriptorBean)((MachineMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                     }
                  } else if (var1.equals("MailSession")) {
                     try {
                        this.bean.addMailSession((MailSessionMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var58) {
                        System.out.println("Warning: multiple definitions with same name: " + var58.getMessage());
                        this.bean.removeMailSession((MailSessionMBean)var58.getExistingBean());
                        this.bean.addMailSession((MailSessionMBean)((AbstractDescriptorBean)((MailSessionMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                     }
                  } else if (var1.equals("MessagingBridge")) {
                     try {
                        this.bean.addMessagingBridge((MessagingBridgeMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var57) {
                        System.out.println("Warning: multiple definitions with same name: " + var57.getMessage());
                        this.bean.removeMessagingBridge((MessagingBridgeMBean)var57.getExistingBean());
                        this.bean.addMessagingBridge((MessagingBridgeMBean)((AbstractDescriptorBean)((MessagingBridgeMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                     }
                  } else if (var1.equals("MigratableRMIService")) {
                     try {
                        this.bean.addMigratableRMIService((MigratableRMIServiceMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var56) {
                        System.out.println("Warning: multiple definitions with same name: " + var56.getMessage());
                        this.bean.removeMigratableRMIService((MigratableRMIServiceMBean)var56.getExistingBean());
                        this.bean.addMigratableRMIService((MigratableRMIServiceMBean)((AbstractDescriptorBean)((MigratableRMIServiceMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                     }
                  } else if (var1.equals("MigratableTarget")) {
                     try {
                        this.bean.addMigratableTarget((MigratableTargetMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var55) {
                        System.out.println("Warning: multiple definitions with same name: " + var55.getMessage());
                        this.bean.removeMigratableTarget((MigratableTargetMBean)var55.getExistingBean());
                        this.bean.addMigratableTarget((MigratableTargetMBean)((AbstractDescriptorBean)((MigratableTargetMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                     }
                  } else if (var1.equals("NTRealm")) {
                     try {
                        this.bean.addNTRealm((NTRealmMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var54) {
                        System.out.println("Warning: multiple definitions with same name: " + var54.getMessage());
                        this.bean.removeNTRealm((NTRealmMBean)var54.getExistingBean());
                        this.bean.addNTRealm((NTRealmMBean)((AbstractDescriptorBean)((NTRealmMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                     }
                  } else if (var1.equals("Name")) {
                     try {
                        this.bean.setName((String)var2);
                     } catch (BeanAlreadyExistsException var53) {
                        System.out.println("Warning: multiple definitions with same name: " + var53.getMessage());
                     }
                  } else if (var1.equals("NetworkChannel")) {
                     try {
                        this.bean.addNetworkChannel((NetworkChannelMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var52) {
                        System.out.println("Warning: multiple definitions with same name: " + var52.getMessage());
                        this.bean.removeNetworkChannel((NetworkChannelMBean)var52.getExistingBean());
                        this.bean.addNetworkChannel((NetworkChannelMBean)((AbstractDescriptorBean)((NetworkChannelMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                     }
                  } else if (var1.equals("PasswordPolicy")) {
                     try {
                        this.bean.addPasswordPolicy((PasswordPolicyMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var51) {
                        System.out.println("Warning: multiple definitions with same name: " + var51.getMessage());
                        this.bean.removePasswordPolicy((PasswordPolicyMBean)var51.getExistingBean());
                        this.bean.addPasswordPolicy((PasswordPolicyMBean)((AbstractDescriptorBean)((PasswordPolicyMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                     }
                  } else if (var1.equals("PathService")) {
                     try {
                        this.bean.addPathService((PathServiceMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var50) {
                        System.out.println("Warning: multiple definitions with same name: " + var50.getMessage());
                        this.bean.removePathService((PathServiceMBean)var50.getExistingBean());
                        this.bean.addPathService((PathServiceMBean)((AbstractDescriptorBean)((PathServiceMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                     }
                  } else if (var1.equals("RDBMSRealm")) {
                     try {
                        this.bean.addRDBMSRealm((RDBMSRealmMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var49) {
                        System.out.println("Warning: multiple definitions with same name: " + var49.getMessage());
                        this.bean.removeRDBMSRealm((RDBMSRealmMBean)var49.getExistingBean());
                        this.bean.addRDBMSRealm((RDBMSRealmMBean)((AbstractDescriptorBean)((RDBMSRealmMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                     }
                  } else if (var1.equals("Realm")) {
                     try {
                        this.bean.addRealm((RealmMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var48) {
                        System.out.println("Warning: multiple definitions with same name: " + var48.getMessage());
                        this.bean.removeRealm((RealmMBean)var48.getExistingBean());
                        this.bean.addRealm((RealmMBean)((AbstractDescriptorBean)((RealmMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                     }
                  } else if (var1.equals("RemoteSAFContext")) {
                     try {
                        this.bean.addRemoteSAFContext((RemoteSAFContextMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var47) {
                        System.out.println("Warning: multiple definitions with same name: " + var47.getMessage());
                        this.bean.removeRemoteSAFContext((RemoteSAFContextMBean)var47.getExistingBean());
                        this.bean.addRemoteSAFContext((RemoteSAFContextMBean)((AbstractDescriptorBean)((RemoteSAFContextMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                     }
                  } else if (var1.equals("RestfulManagementService")) {
                     try {
                        this.bean.setRestfulManagementServices((RestfulManagementServicesMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var46) {
                        System.out.println("Warning: multiple definitions with same name: " + var46.getMessage());
                     }
                  } else {
                     if (var1.equals("RootDirectory")) {
                        throw new AssertionError("can't set read-only property RootDirectory");
                     }

                     if (var1.equals("SAFAgent")) {
                        try {
                           this.bean.addSAFAgent((SAFAgentMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var45) {
                           System.out.println("Warning: multiple definitions with same name: " + var45.getMessage());
                           this.bean.removeSAFAgent((SAFAgentMBean)var45.getExistingBean());
                           this.bean.addSAFAgent((SAFAgentMBean)((AbstractDescriptorBean)((SAFAgentMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("SNMPAgent")) {
                        try {
                           this.bean.setSNMPAgent((SNMPAgentMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var44) {
                           System.out.println("Warning: multiple definitions with same name: " + var44.getMessage());
                        }
                     } else if (var1.equals("SNMPAgentDeployment")) {
                        try {
                           this.bean.addSNMPAgentDeployment((SNMPAgentDeploymentMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var43) {
                           System.out.println("Warning: multiple definitions with same name: " + var43.getMessage());
                           this.bean.removeSNMPAgentDeployment((SNMPAgentDeploymentMBean)var43.getExistingBean());
                           this.bean.addSNMPAgentDeployment((SNMPAgentDeploymentMBean)((AbstractDescriptorBean)((SNMPAgentDeploymentMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("SNMPAttributeChange")) {
                        try {
                           this.bean.addSNMPAttributeChange((SNMPAttributeChangeMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var42) {
                           System.out.println("Warning: multiple definitions with same name: " + var42.getMessage());
                           this.bean.removeSNMPAttributeChange((SNMPAttributeChangeMBean)var42.getExistingBean());
                           this.bean.addSNMPAttributeChange((SNMPAttributeChangeMBean)((AbstractDescriptorBean)((SNMPAttributeChangeMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("SNMPCounterMonitor")) {
                        try {
                           this.bean.addSNMPCounterMonitor((SNMPCounterMonitorMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var41) {
                           System.out.println("Warning: multiple definitions with same name: " + var41.getMessage());
                           this.bean.removeSNMPCounterMonitor((SNMPCounterMonitorMBean)var41.getExistingBean());
                           this.bean.addSNMPCounterMonitor((SNMPCounterMonitorMBean)((AbstractDescriptorBean)((SNMPCounterMonitorMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("SNMPGaugeMonitor")) {
                        try {
                           this.bean.addSNMPGaugeMonitor((SNMPGaugeMonitorMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var40) {
                           System.out.println("Warning: multiple definitions with same name: " + var40.getMessage());
                           this.bean.removeSNMPGaugeMonitor((SNMPGaugeMonitorMBean)var40.getExistingBean());
                           this.bean.addSNMPGaugeMonitor((SNMPGaugeMonitorMBean)((AbstractDescriptorBean)((SNMPGaugeMonitorMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("SNMPLogFilter")) {
                        try {
                           this.bean.addSNMPLogFilter((SNMPLogFilterMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var39) {
                           System.out.println("Warning: multiple definitions with same name: " + var39.getMessage());
                           this.bean.removeSNMPLogFilter((SNMPLogFilterMBean)var39.getExistingBean());
                           this.bean.addSNMPLogFilter((SNMPLogFilterMBean)((AbstractDescriptorBean)((SNMPLogFilterMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("SNMPProxy")) {
                        try {
                           this.bean.addSNMPProxy((SNMPProxyMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var38) {
                           System.out.println("Warning: multiple definitions with same name: " + var38.getMessage());
                           this.bean.removeSNMPProxy((SNMPProxyMBean)var38.getExistingBean());
                           this.bean.addSNMPProxy((SNMPProxyMBean)((AbstractDescriptorBean)((SNMPProxyMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("SNMPStringMonitor")) {
                        try {
                           this.bean.addSNMPStringMonitor((SNMPStringMonitorMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var37) {
                           System.out.println("Warning: multiple definitions with same name: " + var37.getMessage());
                           this.bean.removeSNMPStringMonitor((SNMPStringMonitorMBean)var37.getExistingBean());
                           this.bean.addSNMPStringMonitor((SNMPStringMonitorMBean)((AbstractDescriptorBean)((SNMPStringMonitorMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("SNMPTrapDestination")) {
                        try {
                           this.bean.addSNMPTrapDestination((SNMPTrapDestinationMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var36) {
                           System.out.println("Warning: multiple definitions with same name: " + var36.getMessage());
                           this.bean.removeSNMPTrapDestination((SNMPTrapDestinationMBean)var36.getExistingBean());
                           this.bean.addSNMPTrapDestination((SNMPTrapDestinationMBean)((AbstractDescriptorBean)((SNMPTrapDestinationMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("Security")) {
                        try {
                           this.bean.setSecurity((SecurityMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var35) {
                           System.out.println("Warning: multiple definitions with same name: " + var35.getMessage());
                        }
                     } else if (var1.equals("SecurityConfiguration")) {
                        try {
                           this.bean.setSecurityConfiguration((SecurityConfigurationMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var34) {
                           System.out.println("Warning: multiple definitions with same name: " + var34.getMessage());
                        }
                     } else if (var1.equals("SelfTuning")) {
                        try {
                           this.bean.setSelfTuning((SelfTuningMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var33) {
                           System.out.println("Warning: multiple definitions with same name: " + var33.getMessage());
                        }
                     } else if (var1.equals("Server")) {
                        try {
                           this.bean.addServer((ServerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var32) {
                           System.out.println("Warning: multiple definitions with same name: " + var32.getMessage());
                           this.bean.removeServer((ServerMBean)var32.getExistingBean());
                           this.bean.addServer((ServerMBean)((AbstractDescriptorBean)((ServerMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("ShutdownClass")) {
                        try {
                           this.bean.addShutdownClass((ShutdownClassMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var31) {
                           System.out.println("Warning: multiple definitions with same name: " + var31.getMessage());
                           this.bean.removeShutdownClass((ShutdownClassMBean)var31.getExistingBean());
                           this.bean.addShutdownClass((ShutdownClassMBean)((AbstractDescriptorBean)((ShutdownClassMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("SingletonService")) {
                        try {
                           this.bean.addSingletonService((SingletonServiceMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var30) {
                           System.out.println("Warning: multiple definitions with same name: " + var30.getMessage());
                           this.bean.removeSingletonService((SingletonServiceMBean)var30.getExistingBean());
                           this.bean.addSingletonService((SingletonServiceMBean)((AbstractDescriptorBean)((SingletonServiceMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("StartupClass")) {
                        try {
                           this.bean.addStartupClass((StartupClassMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var29) {
                           System.out.println("Warning: multiple definitions with same name: " + var29.getMessage());
                           this.bean.removeStartupClass((StartupClassMBean)var29.getExistingBean());
                           this.bean.addStartupClass((StartupClassMBean)((AbstractDescriptorBean)((StartupClassMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("UnixRealm")) {
                        try {
                           this.bean.addUnixRealm((UnixRealmMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var28) {
                           System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                           this.bean.removeUnixRealm((UnixRealmMBean)var28.getExistingBean());
                           this.bean.addUnixRealm((UnixRealmMBean)((AbstractDescriptorBean)((UnixRealmMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("VirtualHost")) {
                        try {
                           this.bean.addVirtualHost((VirtualHostMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var27) {
                           System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                           this.bean.removeVirtualHost((VirtualHostMBean)var27.getExistingBean());
                           this.bean.addVirtualHost((VirtualHostMBean)((AbstractDescriptorBean)((VirtualHostMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("WLDFSystemResource")) {
                        try {
                           this.bean.addWLDFSystemResource((WLDFSystemResourceMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var26) {
                           System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                           this.bean.removeWLDFSystemResource((WLDFSystemResourceMBean)var26.getExistingBean());
                           this.bean.addWLDFSystemResource((WLDFSystemResourceMBean)((AbstractDescriptorBean)((WLDFSystemResourceMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("WLECConnectionPool")) {
                        try {
                           this.bean.addWLECConnectionPool((WLECConnectionPoolMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var25) {
                           System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                           this.bean.removeWLECConnectionPool((WLECConnectionPoolMBean)var25.getExistingBean());
                           this.bean.addWLECConnectionPool((WLECConnectionPoolMBean)((AbstractDescriptorBean)((WLECConnectionPoolMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("WSReliableDeliveryPolicy")) {
                        try {
                           this.bean.addWSReliableDeliveryPolicy((WSReliableDeliveryPolicyMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var24) {
                           System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                           this.bean.removeWSReliableDeliveryPolicy((WSReliableDeliveryPolicyMBean)var24.getExistingBean());
                           this.bean.addWSReliableDeliveryPolicy((WSReliableDeliveryPolicyMBean)((AbstractDescriptorBean)((WSReliableDeliveryPolicyMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("WTCServer")) {
                        try {
                           this.bean.addWTCServer((WTCServerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var23) {
                           System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                           this.bean.removeWTCServer((WTCServerMBean)var23.getExistingBean());
                           this.bean.addWTCServer((WTCServerMBean)((AbstractDescriptorBean)((WTCServerMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("WebAppContainer")) {
                        try {
                           this.bean.setWebAppContainer((WebAppContainerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var22) {
                           System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                        }
                     } else if (var1.equals("WebserviceSecurity")) {
                        try {
                           this.bean.addWebserviceSecurity((WebserviceSecurityMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var21) {
                           System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                           this.bean.removeWebserviceSecurity((WebserviceSecurityMBean)var21.getExistingBean());
                           this.bean.addWebserviceSecurity((WebserviceSecurityMBean)((AbstractDescriptorBean)((WebserviceSecurityMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("XMLEntityCache")) {
                        try {
                           this.bean.addXMLEntityCache((XMLEntityCacheMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var20) {
                           System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                           this.bean.removeXMLEntityCache((XMLEntityCacheMBean)var20.getExistingBean());
                           this.bean.addXMLEntityCache((XMLEntityCacheMBean)((AbstractDescriptorBean)((XMLEntityCacheMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else if (var1.equals("XMLRegistry")) {
                        try {
                           this.bean.addXMLRegistry((XMLRegistryMBean)((ReadOnlyMBeanBinder)var2).getBean());
                        } catch (BeanAlreadyExistsException var19) {
                           System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                           this.bean.removeXMLRegistry((XMLRegistryMBean)var19.getExistingBean());
                           this.bean.addXMLRegistry((XMLRegistryMBean)((AbstractDescriptorBean)((XMLRegistryMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                        }
                     } else {
                        if (var1.equals("Active")) {
                           this.handleDeprecatedProperty("Active", "<unknown>");
                           throw new AssertionError("can't set read-only property Active");
                        }

                        if (var1.equals("AdministrationMBeanAuditingEnabled")) {
                           this.handleDeprecatedProperty("AdministrationMBeanAuditingEnabled", "Please use <code>DomainMBean.getConfigurationAuditType()</code>");

                           try {
                              this.bean.setAdministrationMBeanAuditingEnabled(Boolean.valueOf((String)var2));
                           } catch (BeanAlreadyExistsException var18) {
                              System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                           }
                        } else if (var1.equals("AdministrationPortEnabled")) {
                           try {
                              this.bean.setAdministrationPortEnabled(Boolean.valueOf((String)var2));
                           } catch (BeanAlreadyExistsException var17) {
                              System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                           }
                        } else if (var1.equals("AutoConfigurationSaveEnabled")) {
                           this.handleDeprecatedProperty("AutoConfigurationSaveEnabled", "9.0.0.0 The configuration is explicit written on a save call.");

                           try {
                              this.bean.setAutoConfigurationSaveEnabled(Boolean.valueOf((String)var2));
                           } catch (BeanAlreadyExistsException var16) {
                              System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                           }
                        } else if (var1.equals("AutoDeployForSubmodulesEnabled")) {
                           try {
                              this.bean.setAutoDeployForSubmodulesEnabled(Boolean.valueOf((String)var2));
                           } catch (BeanAlreadyExistsException var15) {
                              System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                           }
                        } else if (var1.equals("ClusterConstraintsEnabled")) {
                           try {
                              this.bean.setClusterConstraintsEnabled(Boolean.valueOf((String)var2));
                           } catch (BeanAlreadyExistsException var14) {
                              System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                           }
                        } else if (var1.equals("ConfigBackupEnabled")) {
                           try {
                              this.bean.setConfigBackupEnabled(Boolean.valueOf((String)var2));
                           } catch (BeanAlreadyExistsException var13) {
                              System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                           }
                        } else if (var1.equals("ConsoleEnabled")) {
                           try {
                              this.bean.setConsoleEnabled(Boolean.valueOf((String)var2));
                           } catch (BeanAlreadyExistsException var12) {
                              System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                           }
                        } else if (var1.equals("ExalogicOptimizationsEnabled")) {
                           try {
                              this.bean.setExalogicOptimizationsEnabled(Boolean.valueOf((String)var2));
                           } catch (BeanAlreadyExistsException var11) {
                              System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                           }
                        } else if (var1.equals("GuardianEnabled")) {
                           try {
                              this.bean.setGuardianEnabled(Boolean.valueOf((String)var2));
                           } catch (BeanAlreadyExistsException var10) {
                              System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                           }
                        } else if (var1.equals("InternalAppsDeployOnDemandEnabled")) {
                           try {
                              this.bean.setInternalAppsDeployOnDemandEnabled(Boolean.valueOf((String)var2));
                           } catch (BeanAlreadyExistsException var9) {
                              System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                           }
                        } else if (var1.equals("MsgIdPrefixCompatibilityEnabled")) {
                           try {
                              this.bean.setMsgIdPrefixCompatibilityEnabled(Boolean.valueOf((String)var2));
                           } catch (BeanAlreadyExistsException var8) {
                              System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                           }
                        } else if (var1.equals("OCMEnabled")) {
                           try {
                              this.bean.setOCMEnabled(Boolean.valueOf((String)var2));
                           } catch (BeanAlreadyExistsException var7) {
                              System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                           }
                        } else if (var1.equals("ProductionModeEnabled")) {
                           try {
                              this.bean.setProductionModeEnabled(Boolean.valueOf((String)var2));
                           } catch (BeanAlreadyExistsException var6) {
                              System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                           }
                        } else if (this.isInstance(MachineMBean.class, var1)) {
                           try {
                              this.bean.addMachine((MachineMBean)((ReadOnlyMBeanBinder)var2).getBean());
                           } catch (BeanAlreadyExistsException var5) {
                              System.out.println("Warning: multiple definitions with same name : " + var5.getMessage());
                              this.bean.removeMachine((MachineMBean)var5.getExistingBean());
                              this.bean.addMachine((MachineMBean)((ReadOnlyMBeanBinder)var2).getBean());
                           }
                        } else {
                           var3 = super.bindAttribute(var1, var2);
                        }
                     }
                  }
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var125) {
         System.out.println(var125 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var125;
      } catch (RuntimeException var126) {
         throw var126;
      } catch (Exception var127) {
         if (var127 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var127);
         } else if (var127 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var127.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var127);
         }
      }
   }
}
