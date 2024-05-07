package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class ClusterMBeanBinder extends TargetMBeanBinder implements AttributeBinder {
   private ClusterMBeanImpl bean;

   protected ClusterMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (ClusterMBeanImpl)var1;
   }

   public ClusterMBeanBinder() {
      super(new ClusterMBeanImpl());
      this.bean = (ClusterMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("AdditionalAutoMigrationAttempts")) {
                  try {
                     this.bean.setAdditionalAutoMigrationAttempts(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var57) {
                     System.out.println("Warning: multiple definitions with same name: " + var57.getMessage());
                  }
               } else if (var1.equals("AsyncSessionQueueTimeout")) {
                  try {
                     this.bean.setAsyncSessionQueueTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var56) {
                     System.out.println("Warning: multiple definitions with same name: " + var56.getMessage());
                  }
               } else if (var1.equals("AutoMigrationTableName")) {
                  try {
                     this.bean.setAutoMigrationTableName((String)var2);
                  } catch (BeanAlreadyExistsException var55) {
                     System.out.println("Warning: multiple definitions with same name: " + var55.getMessage());
                  }
               } else if (var1.equals("CandidateMachinesForMigratableServers")) {
                  this.bean.setCandidateMachinesForMigratableServersAsString((String)var2);
               } else if (var1.equals("ClusterAddress")) {
                  try {
                     this.bean.setClusterAddress((String)var2);
                  } catch (BeanAlreadyExistsException var54) {
                     System.out.println("Warning: multiple definitions with same name: " + var54.getMessage());
                  }
               } else if (var1.equals("ClusterBroadcastChannel")) {
                  try {
                     this.bean.setClusterBroadcastChannel((String)var2);
                  } catch (BeanAlreadyExistsException var53) {
                     System.out.println("Warning: multiple definitions with same name: " + var53.getMessage());
                  }
               } else if (var1.equals("ClusterMessagingMode")) {
                  try {
                     this.bean.setClusterMessagingMode((String)var2);
                  } catch (BeanAlreadyExistsException var52) {
                     System.out.println("Warning: multiple definitions with same name: " + var52.getMessage());
                  }
               } else if (var1.equals("ClusterType")) {
                  try {
                     this.bean.setClusterType((String)var2);
                  } catch (BeanAlreadyExistsException var51) {
                     System.out.println("Warning: multiple definitions with same name: " + var51.getMessage());
                  }
               } else if (var1.equals("ConsensusParticipants")) {
                  try {
                     this.bean.setConsensusParticipants(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var50) {
                     System.out.println("Warning: multiple definitions with same name: " + var50.getMessage());
                  }
               } else if (var1.equals("DataSourceForAutomaticMigration")) {
                  this.bean.setDataSourceForAutomaticMigrationAsString((String)var2);
               } else if (var1.equals("DataSourceForJobScheduler")) {
                  this.bean.setDataSourceForJobSchedulerAsString((String)var2);
               } else if (var1.equals("DataSourceForSessionPersistence")) {
                  this.bean.setDataSourceForSessionPersistenceAsString((String)var2);
               } else if (var1.equals("DatabaseLessLeasingBasi")) {
                  try {
                     this.bean.setDatabaseLessLeasingBasis((DatabaseLessLeasingBasisMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var49) {
                     System.out.println("Warning: multiple definitions with same name: " + var49.getMessage());
                  }
               } else if (var1.equals("DeathDetectorHeartbeatPeriod")) {
                  try {
                     this.bean.setDeathDetectorHeartbeatPeriod(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var48) {
                     System.out.println("Warning: multiple definitions with same name: " + var48.getMessage());
                  }
               } else if (var1.equals("DefaultLoadAlgorithm")) {
                  try {
                     this.bean.setDefaultLoadAlgorithm((String)var2);
                  } catch (BeanAlreadyExistsException var47) {
                     System.out.println("Warning: multiple definitions with same name: " + var47.getMessage());
                  }
               } else if (var1.equals("FencingGracePeriodMillis")) {
                  try {
                     this.bean.setFencingGracePeriodMillis(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var46) {
                     System.out.println("Warning: multiple definitions with same name: " + var46.getMessage());
                  }
               } else if (var1.equals("FrontendHTTPPort")) {
                  try {
                     this.bean.setFrontendHTTPPort(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var45) {
                     System.out.println("Warning: multiple definitions with same name: " + var45.getMessage());
                  }
               } else if (var1.equals("FrontendHTTPSPort")) {
                  try {
                     this.bean.setFrontendHTTPSPort(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var44) {
                     System.out.println("Warning: multiple definitions with same name: " + var44.getMessage());
                  }
               } else if (var1.equals("FrontendHost")) {
                  try {
                     this.bean.setFrontendHost((String)var2);
                  } catch (BeanAlreadyExistsException var43) {
                     System.out.println("Warning: multiple definitions with same name: " + var43.getMessage());
                  }
               } else if (var1.equals("GreedySessionFlushInterval")) {
                  try {
                     this.bean.setGreedySessionFlushInterval(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var42) {
                     System.out.println("Warning: multiple definitions with same name: " + var42.getMessage());
                  }
               } else if (var1.equals("HTTPPingRetryCount")) {
                  try {
                     this.bean.setHTTPPingRetryCount(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var41) {
                     System.out.println("Warning: multiple definitions with same name: " + var41.getMessage());
                  }
               } else if (var1.equals("HealthCheckIntervalMillis")) {
                  try {
                     this.bean.setHealthCheckIntervalMillis(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var40) {
                     System.out.println("Warning: multiple definitions with same name: " + var40.getMessage());
                  }
               } else if (var1.equals("HealthCheckPeriodsUntilFencing")) {
                  try {
                     this.bean.setHealthCheckPeriodsUntilFencing(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var39) {
                     System.out.println("Warning: multiple definitions with same name: " + var39.getMessage());
                  }
               } else if (var1.equals("IdlePeriodsUntilTimeout")) {
                  try {
                     this.bean.setIdlePeriodsUntilTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var38) {
                     System.out.println("Warning: multiple definitions with same name: " + var38.getMessage());
                  }
               } else if (var1.equals("InterClusterCommLinkHealthCheckInterval")) {
                  try {
                     this.bean.setInterClusterCommLinkHealthCheckInterval(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var37) {
                     System.out.println("Warning: multiple definitions with same name: " + var37.getMessage());
                  }
               } else if (var1.equals("JobSchedulerTableName")) {
                  try {
                     this.bean.setJobSchedulerTableName((String)var2);
                  } catch (BeanAlreadyExistsException var36) {
                     System.out.println("Warning: multiple definitions with same name: " + var36.getMessage());
                  }
               } else if (var1.equals("MaxServerCountForHttpPing")) {
                  try {
                     this.bean.setMaxServerCountForHttpPing(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var35) {
                     System.out.println("Warning: multiple definitions with same name: " + var35.getMessage());
                  }
               } else if (var1.equals("MemberWarmupTimeoutSeconds")) {
                  try {
                     this.bean.setMemberWarmupTimeoutSeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var34) {
                     System.out.println("Warning: multiple definitions with same name: " + var34.getMessage());
                  }
               } else if (var1.equals("MigrationBasis")) {
                  try {
                     this.bean.setMigrationBasis((String)var2);
                  } catch (BeanAlreadyExistsException var33) {
                     System.out.println("Warning: multiple definitions with same name: " + var33.getMessage());
                  }
               } else if (var1.equals("MillisToSleepBetweenAutoMigrationAttempts")) {
                  try {
                     this.bean.setMillisToSleepBetweenAutoMigrationAttempts(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var32) {
                     System.out.println("Warning: multiple definitions with same name: " + var32.getMessage());
                  }
               } else if (var1.equals("MulticastAddress")) {
                  try {
                     this.bean.setMulticastAddress((String)var2);
                  } catch (BeanAlreadyExistsException var31) {
                     System.out.println("Warning: multiple definitions with same name: " + var31.getMessage());
                  }
               } else if (var1.equals("MulticastBufferSize")) {
                  try {
                     this.bean.setMulticastBufferSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var30) {
                     System.out.println("Warning: multiple definitions with same name: " + var30.getMessage());
                  }
               } else if (var1.equals("MulticastDataEncryption")) {
                  try {
                     this.bean.setMulticastDataEncryption(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var29) {
                     System.out.println("Warning: multiple definitions with same name: " + var29.getMessage());
                  }
               } else if (var1.equals("MulticastPort")) {
                  try {
                     this.bean.setMulticastPort(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var28) {
                     System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                  }
               } else if (var1.equals("MulticastSendDelay")) {
                  try {
                     this.bean.setMulticastSendDelay(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                  }
               } else if (var1.equals("MulticastTTL")) {
                  try {
                     this.bean.setMulticastTTL(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("NumberOfServersInClusterAddress")) {
                  try {
                     this.bean.setNumberOfServersInClusterAddress(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("OverloadProtection")) {
                  try {
                     this.bean.setOverloadProtection((OverloadProtectionMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("PersistSessionsOnShutdown")) {
                  try {
                     this.bean.setPersistSessionsOnShutdown(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("RemoteClusterAddress")) {
                  try {
                     this.bean.setRemoteClusterAddress((String)var2);
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("ReplicationChannel")) {
                  try {
                     this.bean.setReplicationChannel((String)var2);
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("ServiceAgeThresholdSeconds")) {
                  try {
                     this.bean.setServiceAgeThresholdSeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("SessionFlushInterval")) {
                  try {
                     this.bean.setSessionFlushInterval(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("SessionFlushThreshold")) {
                  try {
                     this.bean.setSessionFlushThreshold(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("SingletonSQLQueryHelper")) {
                  try {
                     this.bean.setSingletonSQLQueryHelper((String)var2);
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("UnicastDiscoveryPeriodMillis")) {
                  try {
                     this.bean.setUnicastDiscoveryPeriodMillis(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("WANSessionPersistenceTableName")) {
                  try {
                     this.bean.setWANSessionPersistenceTableName((String)var2);
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("ClientCertProxyEnabled")) {
                  try {
                     this.bean.setClientCertProxyEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("HttpTraceSupportEnabled")) {
                  try {
                     this.bean.setHttpTraceSupportEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("MemberDeathDetectorEnabled")) {
                  try {
                     this.bean.setMemberDeathDetectorEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("MessageOrderingEnabled")) {
                  try {
                     this.bean.setMessageOrderingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("OneWayRmiForReplicationEnabled")) {
                  try {
                     this.bean.setOneWayRmiForReplicationEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("ReplicationTimeoutEnabled")) {
                  try {
                     this.bean.setReplicationTimeoutEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("SecureReplicationEnabled")) {
                  try {
                     this.bean.setSecureReplicationEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("SessionLazyDeserializationEnabled")) {
                  try {
                     this.bean.setSessionLazyDeserializationEnabled(Boolean.valueOf((String)var2));
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

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var58) {
         System.out.println(var58 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var58;
      } catch (RuntimeException var59) {
         throw var59;
      } catch (Exception var60) {
         if (var60 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var60);
         } else if (var60 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var60.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var60);
         }
      }
   }
}
