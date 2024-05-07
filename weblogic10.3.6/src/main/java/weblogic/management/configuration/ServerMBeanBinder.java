package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class ServerMBeanBinder extends KernelMBeanBinder implements AttributeBinder {
   private ServerMBeanImpl bean;

   protected ServerMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (ServerMBeanImpl)var1;
   }

   public ServerMBeanBinder() {
      super(new ServerMBeanImpl());
      this.bean = (ServerMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("AcceptBacklog")) {
                  try {
                     this.bean.setAcceptBacklog(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var124) {
                     System.out.println("Warning: multiple definitions with same name: " + var124.getMessage());
                  }
               } else if (var1.equals("ActiveDirectoryName")) {
                  try {
                     this.bean.setActiveDirectoryName((String)var2);
                  } catch (BeanAlreadyExistsException var123) {
                     System.out.println("Warning: multiple definitions with same name: " + var123.getMessage());
                  }
               } else if (var1.equals("AdminReconnectIntervalSeconds")) {
                  try {
                     this.bean.setAdminReconnectIntervalSeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var122) {
                     System.out.println("Warning: multiple definitions with same name: " + var122.getMessage());
                  }
               } else if (var1.equals("AdministrationPort")) {
                  try {
                     this.bean.setAdministrationPort(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var121) {
                     System.out.println("Warning: multiple definitions with same name: " + var121.getMessage());
                  }
               } else if (var1.equals("AutoJDBCConnectionClose")) {
                  try {
                     this.bean.setAutoJDBCConnectionClose((String)var2);
                  } catch (BeanAlreadyExistsException var120) {
                     System.out.println("Warning: multiple definitions with same name: " + var120.getMessage());
                  }
               } else if (var1.equals("AutoKillIfFailed")) {
                  try {
                     this.bean.setAutoKillIfFailed(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var119) {
                     System.out.println("Warning: multiple definitions with same name: " + var119.getMessage());
                  }
               } else if (var1.equals("AutoRestart")) {
                  try {
                     this.bean.setAutoRestart(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var118) {
                     System.out.println("Warning: multiple definitions with same name: " + var118.getMessage());
                  }
               } else if (var1.equals("COM")) {
                  try {
                     this.bean.setCOM((COMMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var117) {
                     System.out.println("Warning: multiple definitions with same name: " + var117.getMessage());
                  }
               } else if (var1.equals("CandidateMachines")) {
                  this.bean.setCandidateMachinesAsString((String)var2);
               } else if (var1.equals("Cluster")) {
                  this.bean.setClusterAsString((String)var2);
               } else if (var1.equals("ClusterWeight")) {
                  try {
                     this.bean.setClusterWeight(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var116) {
                     System.out.println("Warning: multiple definitions with same name: " + var116.getMessage());
                  }
               } else if (var1.equals("CoherenceClusterSystemResource")) {
                  this.bean.setCoherenceClusterSystemResourceAsString((String)var2);
               } else if (var1.equals("ConsensusProcessIdentifier")) {
                  try {
                     this.bean.setConsensusProcessIdentifier(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var115) {
                     System.out.println("Warning: multiple definitions with same name: " + var115.getMessage());
                  }
               } else if (var1.equals("CustomIdentityKeyStoreFileName")) {
                  try {
                     this.bean.setCustomIdentityKeyStoreFileName((String)var2);
                  } catch (BeanAlreadyExistsException var114) {
                     System.out.println("Warning: multiple definitions with same name: " + var114.getMessage());
                  }
               } else if (var1.equals("CustomIdentityKeyStorePassPhrase")) {
                  try {
                     if (this.bean.isCustomIdentityKeyStorePassPhraseEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to CustomIdentityKeyStorePassPhrase [ ServerMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setCustomIdentityKeyStorePassPhrase((String)var2);
                  } catch (BeanAlreadyExistsException var113) {
                     System.out.println("Warning: multiple definitions with same name: " + var113.getMessage());
                  }
               } else if (var1.equals("CustomIdentityKeyStorePassPhraseEncrypted")) {
                  if (this.bean.isCustomIdentityKeyStorePassPhraseEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to CustomIdentityKeyStorePassPhraseEncrypted [ ServerMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setCustomIdentityKeyStorePassPhraseEncryptedAsString((String)var2);
               } else if (var1.equals("CustomIdentityKeyStoreType")) {
                  try {
                     this.bean.setCustomIdentityKeyStoreType((String)var2);
                  } catch (BeanAlreadyExistsException var112) {
                     System.out.println("Warning: multiple definitions with same name: " + var112.getMessage());
                  }
               } else if (var1.equals("CustomTrustKeyStoreFileName")) {
                  try {
                     this.bean.setCustomTrustKeyStoreFileName((String)var2);
                  } catch (BeanAlreadyExistsException var111) {
                     System.out.println("Warning: multiple definitions with same name: " + var111.getMessage());
                  }
               } else if (var1.equals("CustomTrustKeyStorePassPhrase")) {
                  try {
                     if (this.bean.isCustomTrustKeyStorePassPhraseEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to CustomTrustKeyStorePassPhrase [ ServerMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setCustomTrustKeyStorePassPhrase((String)var2);
                  } catch (BeanAlreadyExistsException var110) {
                     System.out.println("Warning: multiple definitions with same name: " + var110.getMessage());
                  }
               } else if (var1.equals("CustomTrustKeyStorePassPhraseEncrypted")) {
                  if (this.bean.isCustomTrustKeyStorePassPhraseEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to CustomTrustKeyStorePassPhraseEncrypted [ ServerMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setCustomTrustKeyStorePassPhraseEncryptedAsString((String)var2);
               } else if (var1.equals("CustomTrustKeyStoreType")) {
                  try {
                     this.bean.setCustomTrustKeyStoreType((String)var2);
                  } catch (BeanAlreadyExistsException var109) {
                     System.out.println("Warning: multiple definitions with same name: " + var109.getMessage());
                  }
               } else if (var1.equals("DataSource")) {
                  try {
                     this.bean.setDataSource((DataSourceMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var108) {
                     System.out.println("Warning: multiple definitions with same name: " + var108.getMessage());
                  }
               } else if (var1.equals("DefaultFileStore")) {
                  try {
                     this.bean.setDefaultFileStore((DefaultFileStoreMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var107) {
                     System.out.println("Warning: multiple definitions with same name: " + var107.getMessage());
                  }
               } else if (var1.equals("DefaultIIOPPassword")) {
                  try {
                     if (this.bean.isDefaultIIOPPasswordEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to DefaultIIOPPassword [ ServerMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setDefaultIIOPPassword((String)var2);
                  } catch (BeanAlreadyExistsException var106) {
                     System.out.println("Warning: multiple definitions with same name: " + var106.getMessage());
                  }
               } else if (var1.equals("DefaultIIOPPasswordEncrypted")) {
                  if (this.bean.isDefaultIIOPPasswordEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to DefaultIIOPPasswordEncrypted [ ServerMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setDefaultIIOPPasswordEncryptedAsString((String)var2);
               } else if (var1.equals("DefaultIIOPUser")) {
                  try {
                     this.bean.setDefaultIIOPUser((String)var2);
                  } catch (BeanAlreadyExistsException var105) {
                     System.out.println("Warning: multiple definitions with same name: " + var105.getMessage());
                  }
               } else if (var1.equals("DefaultTGIOPPassword")) {
                  try {
                     if (this.bean.isDefaultTGIOPPasswordEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to DefaultTGIOPPassword [ ServerMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setDefaultTGIOPPassword((String)var2);
                  } catch (BeanAlreadyExistsException var104) {
                     System.out.println("Warning: multiple definitions with same name: " + var104.getMessage());
                  }
               } else if (var1.equals("DefaultTGIOPPasswordEncrypted")) {
                  if (this.bean.isDefaultTGIOPPasswordEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to DefaultTGIOPPasswordEncrypted [ ServerMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setDefaultTGIOPPasswordEncryptedAsString((String)var2);
               } else if (var1.equals("DefaultTGIOPUser")) {
                  try {
                     this.bean.setDefaultTGIOPUser((String)var2);
                  } catch (BeanAlreadyExistsException var103) {
                     System.out.println("Warning: multiple definitions with same name: " + var103.getMessage());
                  }
               } else if (var1.equals("DomainLogFilter")) {
                  this.handleDeprecatedProperty("DomainLogFilter", "9.0.0.0 Replaced by LogMBean.LogBroadcastFilter The severity of messages going to the domain log is configured separately through LogMBean.LogBroadcastSeverity, For backward compatibility the changes to this attribute will be propagated to the LogMBean.");
                  this.bean.setDomainLogFilterAsString((String)var2);
               } else if (var1.equals("ExternalDNSName")) {
                  try {
                     this.bean.setExternalDNSName((String)var2);
                  } catch (BeanAlreadyExistsException var102) {
                     System.out.println("Warning: multiple definitions with same name: " + var102.getMessage());
                  }
               } else if (var1.equals("ExtraEjbcOptions")) {
                  try {
                     this.bean.setExtraEjbcOptions((String)var2);
                  } catch (BeanAlreadyExistsException var101) {
                     System.out.println("Warning: multiple definitions with same name: " + var101.getMessage());
                  }
               } else if (var1.equals("ExtraRmicOptions")) {
                  try {
                     this.bean.setExtraRmicOptions((String)var2);
                  } catch (BeanAlreadyExistsException var100) {
                     System.out.println("Warning: multiple definitions with same name: " + var100.getMessage());
                  }
               } else if (var1.equals("FederationService")) {
                  try {
                     this.bean.setFederationServices((FederationServicesMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var99) {
                     System.out.println("Warning: multiple definitions with same name: " + var99.getMessage());
                  }
               } else if (var1.equals("GracefulShutdownTimeout")) {
                  try {
                     this.bean.setGracefulShutdownTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var98) {
                     System.out.println("Warning: multiple definitions with same name: " + var98.getMessage());
                  }
               } else if (var1.equals("HealthCheckIntervalSeconds")) {
                  try {
                     this.bean.setHealthCheckIntervalSeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var97) {
                     System.out.println("Warning: multiple definitions with same name: " + var97.getMessage());
                  }
               } else if (var1.equals("HealthCheckStartDelaySeconds")) {
                  try {
                     this.bean.setHealthCheckStartDelaySeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var96) {
                     System.out.println("Warning: multiple definitions with same name: " + var96.getMessage());
                  }
               } else if (var1.equals("HealthCheckTimeoutSeconds")) {
                  this.handleDeprecatedProperty("HealthCheckTimeoutSeconds", "9.0.0.0 Replaced by Server self-health monitoring that");

                  try {
                     this.bean.setHealthCheckTimeoutSeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var95) {
                     System.out.println("Warning: multiple definitions with same name: " + var95.getMessage());
                  }
               } else if (var1.equals("HostsMigratableServices")) {
                  try {
                     this.bean.setHostsMigratableServices(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var94) {
                     System.out.println("Warning: multiple definitions with same name: " + var94.getMessage());
                  }
               } else if (var1.equals("IIOPConnectionPools")) {
                  this.bean.setIIOPConnectionPoolsAsString((String)var2);
               } else if (var1.equals("InterfaceAddress")) {
                  try {
                     this.bean.setInterfaceAddress((String)var2);
                  } catch (BeanAlreadyExistsException var93) {
                     System.out.println("Warning: multiple definitions with same name: " + var93.getMessage());
                  }
               } else if (var1.equals("JDBCLLRTableName")) {
                  try {
                     this.bean.setJDBCLLRTableName((String)var2);
                  } catch (BeanAlreadyExistsException var92) {
                     System.out.println("Warning: multiple definitions with same name: " + var92.getMessage());
                  }
               } else if (var1.equals("JDBCLLRTablePoolColumnSize")) {
                  try {
                     this.bean.setJDBCLLRTablePoolColumnSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var91) {
                     System.out.println("Warning: multiple definitions with same name: " + var91.getMessage());
                  }
               } else if (var1.equals("JDBCLLRTableRecordColumnSize")) {
                  try {
                     this.bean.setJDBCLLRTableRecordColumnSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var90) {
                     System.out.println("Warning: multiple definitions with same name: " + var90.getMessage());
                  }
               } else if (var1.equals("JDBCLLRTableXIDColumnSize")) {
                  try {
                     this.bean.setJDBCLLRTableXIDColumnSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var89) {
                     System.out.println("Warning: multiple definitions with same name: " + var89.getMessage());
                  }
               } else if (var1.equals("JDBCLogFileName")) {
                  this.handleDeprecatedProperty("JDBCLogFileName", "9.0.0.0");

                  try {
                     this.bean.setJDBCLogFileName((String)var2);
                  } catch (BeanAlreadyExistsException var88) {
                     System.out.println("Warning: multiple definitions with same name: " + var88.getMessage());
                  }
               } else if (var1.equals("JDBCLoginTimeoutSeconds")) {
                  try {
                     this.bean.setJDBCLoginTimeoutSeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var87) {
                     System.out.println("Warning: multiple definitions with same name: " + var87.getMessage());
                  }
               } else if (var1.equals("JNDITransportableObjectFactoryList")) {
                  try {
                     this.bean.setJNDITransportableObjectFactoryList(this.parseStringArrayInitializer((String)var2));
                  } catch (BeanAlreadyExistsException var86) {
                     System.out.println("Warning: multiple definitions with same name: " + var86.getMessage());
                  }
               } else if (var1.equals("JTAMigratableTarget")) {
                  try {
                     this.bean.setJTAMigratableTarget((JTAMigratableTargetMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var85) {
                     System.out.println("Warning: multiple definitions with same name: " + var85.getMessage());
                  }
               } else if (var1.equals("JavaCompiler")) {
                  try {
                     this.bean.setJavaCompiler((String)var2);
                  } catch (BeanAlreadyExistsException var84) {
                     System.out.println("Warning: multiple definitions with same name: " + var84.getMessage());
                  }
               } else if (var1.equals("JavaCompilerPostClassPath")) {
                  try {
                     this.bean.setJavaCompilerPostClassPath((String)var2);
                  } catch (BeanAlreadyExistsException var83) {
                     System.out.println("Warning: multiple definitions with same name: " + var83.getMessage());
                  }
               } else if (var1.equals("JavaCompilerPreClassPath")) {
                  try {
                     this.bean.setJavaCompilerPreClassPath((String)var2);
                  } catch (BeanAlreadyExistsException var82) {
                     System.out.println("Warning: multiple definitions with same name: " + var82.getMessage());
                  }
               } else if (var1.equals("JavaStandardTrustKeyStorePassPhrase")) {
                  try {
                     if (this.bean.isJavaStandardTrustKeyStorePassPhraseEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to JavaStandardTrustKeyStorePassPhrase [ ServerMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setJavaStandardTrustKeyStorePassPhrase((String)var2);
                  } catch (BeanAlreadyExistsException var81) {
                     System.out.println("Warning: multiple definitions with same name: " + var81.getMessage());
                  }
               } else if (var1.equals("JavaStandardTrustKeyStorePassPhraseEncrypted")) {
                  if (this.bean.isJavaStandardTrustKeyStorePassPhraseEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to JavaStandardTrustKeyStorePassPhraseEncrypted [ ServerMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setJavaStandardTrustKeyStorePassPhraseEncryptedAsString((String)var2);
               } else if (var1.equals("KeyStores")) {
                  try {
                     this.bean.setKeyStores((String)var2);
                  } catch (BeanAlreadyExistsException var80) {
                     System.out.println("Warning: multiple definitions with same name: " + var80.getMessage());
                  }
               } else if (var1.equals("ListenAddress")) {
                  try {
                     this.bean.setListenAddress((String)var2);
                  } catch (BeanAlreadyExistsException var79) {
                     System.out.println("Warning: multiple definitions with same name: " + var79.getMessage());
                  }
               } else if (var1.equals("ListenDelaySecs")) {
                  this.handleDeprecatedProperty("ListenDelaySecs", "<unknown>");

                  try {
                     this.bean.setListenDelaySecs(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var78) {
                     System.out.println("Warning: multiple definitions with same name: " + var78.getMessage());
                  }
               } else if (var1.equals("ListenPort")) {
                  try {
                     this.bean.setListenPort(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var77) {
                     System.out.println("Warning: multiple definitions with same name: " + var77.getMessage());
                  }
               } else if (var1.equals("ListenThreadStartDelaySecs")) {
                  try {
                     this.bean.setListenThreadStartDelaySecs(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var76) {
                     System.out.println("Warning: multiple definitions with same name: " + var76.getMessage());
                  }
               } else if (var1.equals("ListenersBindEarly")) {
                  try {
                     this.bean.setListenersBindEarly(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var75) {
                     System.out.println("Warning: multiple definitions with same name: " + var75.getMessage());
                  }
               } else if (var1.equals("LoginTimeout")) {
                  try {
                     this.bean.setLoginTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var74) {
                     System.out.println("Warning: multiple definitions with same name: " + var74.getMessage());
                  }
               } else if (var1.equals("LoginTimeoutMillis")) {
                  try {
                     this.bean.setLoginTimeoutMillis(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var73) {
                     System.out.println("Warning: multiple definitions with same name: " + var73.getMessage());
                  }
               } else if (var1.equals("LowMemoryGCThreshold")) {
                  try {
                     this.bean.setLowMemoryGCThreshold(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var72) {
                     System.out.println("Warning: multiple definitions with same name: " + var72.getMessage());
                  }
               } else if (var1.equals("LowMemoryGranularityLevel")) {
                  try {
                     this.bean.setLowMemoryGranularityLevel(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var71) {
                     System.out.println("Warning: multiple definitions with same name: " + var71.getMessage());
                  }
               } else if (var1.equals("LowMemorySampleSize")) {
                  try {
                     this.bean.setLowMemorySampleSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var70) {
                     System.out.println("Warning: multiple definitions with same name: " + var70.getMessage());
                  }
               } else if (var1.equals("LowMemoryTimeInterval")) {
                  try {
                     this.bean.setLowMemoryTimeInterval(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var69) {
                     System.out.println("Warning: multiple definitions with same name: " + var69.getMessage());
                  }
               } else if (var1.equals("Machine")) {
                  this.bean.setMachineAsString((String)var2);
               } else if (var1.equals("MaxBackoffBetweenFailures")) {
                  try {
                     this.bean.setMaxBackoffBetweenFailures(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var68) {
                     System.out.println("Warning: multiple definitions with same name: " + var68.getMessage());
                  }
               } else if (var1.equals("NMSocketCreateTimeoutInMillis")) {
                  try {
                     this.bean.setNMSocketCreateTimeoutInMillis(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var67) {
                     System.out.println("Warning: multiple definitions with same name: " + var67.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var66) {
                     System.out.println("Warning: multiple definitions with same name: " + var66.getMessage());
                  }
               } else if (var1.equals("NetworkAccessPoint")) {
                  try {
                     this.bean.addNetworkAccessPoint((NetworkAccessPointMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var65) {
                     System.out.println("Warning: multiple definitions with same name: " + var65.getMessage());
                     this.bean.removeNetworkAccessPoint((NetworkAccessPointMBean)var65.getExistingBean());
                     this.bean.addNetworkAccessPoint((NetworkAccessPointMBean)((AbstractDescriptorBean)((NetworkAccessPointMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("OverloadProtection")) {
                  try {
                     this.bean.setOverloadProtection((OverloadProtectionMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var64) {
                     System.out.println("Warning: multiple definitions with same name: " + var64.getMessage());
                  }
               } else if (var1.equals("PreferredSecondaryGroup")) {
                  try {
                     this.bean.setPreferredSecondaryGroup((String)var2);
                  } catch (BeanAlreadyExistsException var63) {
                     System.out.println("Warning: multiple definitions with same name: " + var63.getMessage());
                  }
               } else if (var1.equals("ReliableDeliveryPolicy")) {
                  this.bean.setReliableDeliveryPolicyAsString((String)var2);
               } else if (var1.equals("ReplicationGroup")) {
                  try {
                     this.bean.setReplicationGroup((String)var2);
                  } catch (BeanAlreadyExistsException var62) {
                     System.out.println("Warning: multiple definitions with same name: " + var62.getMessage());
                  }
               } else if (var1.equals("ReplicationPorts")) {
                  try {
                     this.bean.setReplicationPorts((String)var2);
                  } catch (BeanAlreadyExistsException var61) {
                     System.out.println("Warning: multiple definitions with same name: " + var61.getMessage());
                  }
               } else if (var1.equals("RestartDelaySeconds")) {
                  try {
                     this.bean.setRestartDelaySeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var60) {
                     System.out.println("Warning: multiple definitions with same name: " + var60.getMessage());
                  }
               } else if (var1.equals("RestartIntervalSeconds")) {
                  try {
                     this.bean.setRestartIntervalSeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var59) {
                     System.out.println("Warning: multiple definitions with same name: " + var59.getMessage());
                  }
               } else if (var1.equals("RestartMax")) {
                  try {
                     this.bean.setRestartMax(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var58) {
                     System.out.println("Warning: multiple definitions with same name: " + var58.getMessage());
                  }
               } else if (var1.equals("ServerDebug")) {
                  try {
                     this.bean.setServerDebug((ServerDebugMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var57) {
                     System.out.println("Warning: multiple definitions with same name: " + var57.getMessage());
                  }
               } else if (var1.equals("ServerDiagnosticConfig")) {
                  try {
                     this.bean.setServerDiagnosticConfig((WLDFServerDiagnosticMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var56) {
                     System.out.println("Warning: multiple definitions with same name: " + var56.getMessage());
                  }
               } else if (var1.equals("ServerLifeCycleTimeoutVal")) {
                  try {
                     this.bean.setServerLifeCycleTimeoutVal(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var55) {
                     System.out.println("Warning: multiple definitions with same name: " + var55.getMessage());
                  }
               } else if (var1.equals("ServerStart")) {
                  try {
                     this.bean.setServerStart((ServerStartMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var54) {
                     System.out.println("Warning: multiple definitions with same name: " + var54.getMessage());
                  }
               } else if (var1.equals("ServerVersion")) {
                  this.handleDeprecatedProperty("ServerVersion", "9.0.0.0");

                  try {
                     this.bean.setServerVersion((String)var2);
                  } catch (BeanAlreadyExistsException var53) {
                     System.out.println("Warning: multiple definitions with same name: " + var53.getMessage());
                  }
               } else if (var1.equals("SingleSignOnService")) {
                  try {
                     this.bean.setSingleSignOnServices((SingleSignOnServicesMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var52) {
                     System.out.println("Warning: multiple definitions with same name: " + var52.getMessage());
                  }
               } else if (var1.equals("StagingDirectoryName")) {
                  try {
                     this.bean.setStagingDirectoryName((String)var2);
                  } catch (BeanAlreadyExistsException var51) {
                     System.out.println("Warning: multiple definitions with same name: " + var51.getMessage());
                  }
               } else if (var1.equals("StagingMode")) {
                  try {
                     this.bean.setStagingMode((String)var2);
                  } catch (BeanAlreadyExistsException var50) {
                     System.out.println("Warning: multiple definitions with same name: " + var50.getMessage());
                  }
               } else if (var1.equals("StartupMode")) {
                  try {
                     this.bean.setStartupMode((String)var2);
                  } catch (BeanAlreadyExistsException var49) {
                     System.out.println("Warning: multiple definitions with same name: " + var49.getMessage());
                  }
               } else if (var1.equals("StartupTimeout")) {
                  try {
                     this.bean.setStartupTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var48) {
                     System.out.println("Warning: multiple definitions with same name: " + var48.getMessage());
                  }
               } else if (var1.equals("StdoutFormat")) {
                  this.handleDeprecatedProperty("StdoutFormat", "<unknown>");

                  try {
                     this.bean.setStdoutFormat((String)var2);
                  } catch (BeanAlreadyExistsException var47) {
                     System.out.println("Warning: multiple definitions with same name: " + var47.getMessage());
                  }
               } else if (var1.equals("StdoutSeverityLevel")) {
                  this.handleDeprecatedProperty("StdoutSeverityLevel", "9.0.0.0 Replaced by LogMBean.StdoutSeverity.  For backward compatibility the changes to this attribute will be  propagated to the LogMBean.");

                  try {
                     this.bean.setStdoutSeverityLevel(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var46) {
                     System.out.println("Warning: multiple definitions with same name: " + var46.getMessage());
                  }
               } else {
                  if (var1.equals("SupportedProtocols")) {
                     throw new AssertionError("can't set read-only property SupportedProtocols");
                  }

                  if (var1.equals("SystemPassword")) {
                     try {
                        if (this.bean.isSystemPasswordEncryptedSet()) {
                           throw new IllegalArgumentException("Encrypted attribute corresponding to SystemPassword [ ServerMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                        }

                        this.bean.setSystemPassword((String)var2);
                     } catch (BeanAlreadyExistsException var45) {
                        System.out.println("Warning: multiple definitions with same name: " + var45.getMessage());
                     }
                  } else if (var1.equals("SystemPasswordEncrypted")) {
                     if (this.bean.isSystemPasswordEncryptedSet()) {
                        throw new IllegalArgumentException("Unencrypted attribute corresponding to SystemPasswordEncrypted [ ServerMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setSystemPasswordEncryptedAsString((String)var2);
                  } else if (var1.equals("ThreadPoolSize")) {
                     this.handleDeprecatedProperty("ThreadPoolSize", "9.0.0.0 replaced with SelfTuningMBean");

                     try {
                        this.bean.setThreadPoolSize(Integer.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var44) {
                        System.out.println("Warning: multiple definitions with same name: " + var44.getMessage());
                     }
                  } else if (var1.equals("TransactionLogFilePrefix")) {
                     try {
                        this.bean.setTransactionLogFilePrefix((String)var2);
                     } catch (BeanAlreadyExistsException var43) {
                        System.out.println("Warning: multiple definitions with same name: " + var43.getMessage());
                     }
                  } else if (var1.equals("TransactionLogFileWritePolicy")) {
                     try {
                        this.bean.setTransactionLogFileWritePolicy((String)var2);
                     } catch (BeanAlreadyExistsException var42) {
                        System.out.println("Warning: multiple definitions with same name: " + var42.getMessage());
                     }
                  } else if (var1.equals("TransactionLogJDBCStore")) {
                     try {
                        this.bean.setTransactionLogJDBCStore((TransactionLogJDBCStoreMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var41) {
                        System.out.println("Warning: multiple definitions with same name: " + var41.getMessage());
                     }
                  } else if (var1.equals("TunnelingClientPingSecs")) {
                     try {
                        this.bean.setTunnelingClientPingSecs(Integer.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var40) {
                        System.out.println("Warning: multiple definitions with same name: " + var40.getMessage());
                     }
                  } else if (var1.equals("TunnelingClientTimeoutSecs")) {
                     try {
                        this.bean.setTunnelingClientTimeoutSecs(Integer.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var39) {
                        System.out.println("Warning: multiple definitions with same name: " + var39.getMessage());
                     }
                  } else if (var1.equals("UploadDirectoryName")) {
                     try {
                        this.bean.setUploadDirectoryName((String)var2);
                     } catch (BeanAlreadyExistsException var38) {
                        System.out.println("Warning: multiple definitions with same name: " + var38.getMessage());
                     }
                  } else if (var1.equals("VerboseEJBDeploymentEnabled")) {
                     this.handleDeprecatedProperty("VerboseEJBDeploymentEnabled", "Deprecated as of 10.3.3.0 in favor of");

                     try {
                        this.bean.setVerboseEJBDeploymentEnabled((String)var2);
                     } catch (BeanAlreadyExistsException var37) {
                        System.out.println("Warning: multiple definitions with same name: " + var37.getMessage());
                     }
                  } else if (var1.equals("VirtualMachineName")) {
                     try {
                        this.bean.setVirtualMachineName((String)var2);
                     } catch (BeanAlreadyExistsException var36) {
                        System.out.println("Warning: multiple definitions with same name: " + var36.getMessage());
                     }
                  } else if (var1.equals("WebServer")) {
                     try {
                        this.bean.setWebServer((WebServerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var35) {
                        System.out.println("Warning: multiple definitions with same name: " + var35.getMessage());
                     }
                  } else if (var1.equals("WebService")) {
                     try {
                        this.bean.setWebService((WebServiceMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var34) {
                        System.out.println("Warning: multiple definitions with same name: " + var34.getMessage());
                     }
                  } else if (var1.equals("XMLEntityCache")) {
                     this.bean.setXMLEntityCacheAsString((String)var2);
                  } else if (var1.equals("XMLRegistry")) {
                     this.bean.setXMLRegistryAsString((String)var2);
                  } else if (var1.equals("AdministrationPortEnabled")) {
                     try {
                        this.bean.setAdministrationPortEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var33) {
                        System.out.println("Warning: multiple definitions with same name: " + var33.getMessage());
                     }
                  } else if (var1.equals("AutoMigrationEnabled")) {
                     try {
                        this.bean.setAutoMigrationEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var32) {
                        System.out.println("Warning: multiple definitions with same name: " + var32.getMessage());
                     }
                  } else if (var1.equals("COMEnabled")) {
                     try {
                        this.bean.setCOMEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var31) {
                        System.out.println("Warning: multiple definitions with same name: " + var31.getMessage());
                     }
                  } else if (var1.equals("ClasspathServletDisabled")) {
                     try {
                        this.bean.setClasspathServletDisabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var30) {
                        System.out.println("Warning: multiple definitions with same name: " + var30.getMessage());
                     }
                  } else if (var1.equals("ClientCertProxyEnabled")) {
                     try {
                        this.bean.setClientCertProxyEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var29) {
                        System.out.println("Warning: multiple definitions with same name: " + var29.getMessage());
                     }
                  } else if (var1.equals("ConsoleInputEnabled")) {
                     try {
                        this.bean.setConsoleInputEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var28) {
                        System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                     }
                  } else if (var1.equals("DefaultInternalServletsDisabled")) {
                     try {
                        this.bean.setDefaultInternalServletsDisabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var27) {
                        System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                     }
                  } else if (var1.equals("EnabledForDomainLog")) {
                     this.handleDeprecatedProperty("EnabledForDomainLog", "9.0.0.0 replaced by LogMBean.LogBroadcastSeverity, For backward compatibility the changes to this attribute will be propagated to the LogMBean.");

                     try {
                        this.bean.setEnabledForDomainLog(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var26) {
                        System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                     }
                  } else if (var1.equals("HttpTraceSupportEnabled")) {
                     try {
                        this.bean.setHttpTraceSupportEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var25) {
                        System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                     }
                  } else if (var1.equals("HttpdEnabled")) {
                     try {
                        this.bean.setHttpdEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var24) {
                        System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                     }
                  } else if (var1.equals("IIOPEnabled")) {
                     try {
                        this.bean.setIIOPEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var23) {
                        System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                     }
                  } else if (var1.equals("IgnoreSessionsDuringShutdown")) {
                     try {
                        this.bean.setIgnoreSessionsDuringShutdown(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var22) {
                        System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                     }
                  } else if (var1.equals("J2EE12OnlyModeEnabled")) {
                     this.handleDeprecatedProperty("J2EE12OnlyModeEnabled", "<unknown>");

                     try {
                        this.bean.setJ2EE12OnlyModeEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var21) {
                        System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                     }
                  } else if (var1.equals("J2EE13WarningEnabled")) {
                     this.handleDeprecatedProperty("J2EE13WarningEnabled", "<unknown>");

                     try {
                        this.bean.setJ2EE13WarningEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var20) {
                        System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                     }
                  } else if (var1.equals("JDBCLoggingEnabled")) {
                     this.handleDeprecatedProperty("JDBCLoggingEnabled", "9.0.0.0 Use ServerDebugMBean.getJDBCDriverLogging");

                     try {
                        this.bean.setJDBCLoggingEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var19) {
                        System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                     }
                  } else if (var1.equals("JMSDefaultConnectionFactoriesEnabled")) {
                     try {
                        this.bean.setJMSDefaultConnectionFactoriesEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var18) {
                        System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                     }
                  } else if (var1.equals("JRMPEnabled")) {
                     try {
                        this.bean.setJRMPEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var17) {
                        System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                     }
                  } else if (var1.equals("ListenPortEnabled")) {
                     try {
                        this.bean.setListenPortEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var16) {
                        System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                     }
                  } else if (var1.equals("MSIFileReplicationEnabled")) {
                     this.handleDeprecatedProperty("MSIFileReplicationEnabled", "<unknown>");

                     try {
                        this.bean.setMSIFileReplicationEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var15) {
                        System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                     }
                  } else if (var1.equals("ManagedServerIndependenceEnabled")) {
                     try {
                        this.bean.setManagedServerIndependenceEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var14) {
                        System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                     }
                  } else if (var1.equals("MessageIdPrefixEnabled")) {
                     try {
                        this.bean.setMessageIdPrefixEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var13) {
                        System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                     }
                  } else if (var1.equals("NetworkClassLoadingEnabled")) {
                     try {
                        this.bean.setNetworkClassLoadingEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var12) {
                        System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                     }
                  } else if (var1.equals("StdoutDebugEnabled")) {
                     this.handleDeprecatedProperty("StdoutDebugEnabled", "9.0.0.0 replaced by LogMBean.StdoutSeverity For backward compatibility the changes to this attribute will be propagated to the LogMBean.");

                     try {
                        this.bean.setStdoutDebugEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var11) {
                        System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                     }
                  } else if (var1.equals("StdoutEnabled")) {
                     this.handleDeprecatedProperty("StdoutEnabled", "9.0.0.0 replaced by LogMBean.StdoutSeverity, for backward compatibility the changes to this attribute will be propagated to the LogMBean.");

                     try {
                        this.bean.setStdoutEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var10) {
                        System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                     }
                  } else if (var1.equals("StdoutLogStack")) {
                     this.handleDeprecatedProperty("StdoutLogStack", "9.0.0.0");

                     try {
                        this.bean.setStdoutLogStack(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var9) {
                        System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                     }
                  } else if (var1.equals("TGIOPEnabled")) {
                     try {
                        this.bean.setTGIOPEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var8) {
                        System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                     }
                  } else if (var1.equals("TunnelingEnabled")) {
                     try {
                        this.bean.setTunnelingEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var7) {
                        System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                     }
                  } else if (var1.equals("UseFusionForLLR")) {
                     try {
                        this.bean.setUseFusionForLLR(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var6) {
                        System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                     }
                  } else if (var1.equals("WeblogicPluginEnabled")) {
                     try {
                        this.bean.setWeblogicPluginEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var5) {
                        System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                     }
                  } else {
                     var3 = super.bindAttribute(var1, var2);
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
