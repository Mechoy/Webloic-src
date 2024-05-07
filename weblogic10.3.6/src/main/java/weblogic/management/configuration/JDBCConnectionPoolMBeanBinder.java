package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class JDBCConnectionPoolMBeanBinder extends DeploymentMBeanBinder implements AttributeBinder {
   private JDBCConnectionPoolMBeanImpl bean;

   protected JDBCConnectionPoolMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (JDBCConnectionPoolMBeanImpl)var1;
   }

   public JDBCConnectionPoolMBeanBinder() {
      super(new JDBCConnectionPoolMBeanImpl());
      this.bean = (JDBCConnectionPoolMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("ACLName")) {
                  try {
                     this.bean.setACLName((String)var2);
                  } catch (BeanAlreadyExistsException var62) {
                     System.out.println("Warning: multiple definitions with same name: " + var62.getMessage());
                  }
               } else if (var1.equals("CapacityIncrement")) {
                  this.handleDeprecatedProperty("CapacityIncrement", "10.3.6.0");

                  try {
                     this.bean.setCapacityIncrement(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var61) {
                     System.out.println("Warning: multiple definitions with same name: " + var61.getMessage());
                  }
               } else if (var1.equals("ConnectionCreationRetryFrequencySeconds")) {
                  try {
                     this.bean.setConnectionCreationRetryFrequencySeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var60) {
                     System.out.println("Warning: multiple definitions with same name: " + var60.getMessage());
                  }
               } else if (var1.equals("ConnectionReserveTimeoutSeconds")) {
                  try {
                     this.bean.setConnectionReserveTimeoutSeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var59) {
                     System.out.println("Warning: multiple definitions with same name: " + var59.getMessage());
                  }
               } else if (var1.equals("CountOfRefreshFailuresTillDisable")) {
                  try {
                     this.bean.setCountOfRefreshFailuresTillDisable(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var58) {
                     System.out.println("Warning: multiple definitions with same name: " + var58.getMessage());
                  }
               } else if (var1.equals("CountOfTestFailuresTillFlush")) {
                  try {
                     this.bean.setCountOfTestFailuresTillFlush(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var57) {
                     System.out.println("Warning: multiple definitions with same name: " + var57.getMessage());
                  }
               } else if (var1.equals("DriverName")) {
                  try {
                     this.bean.setDriverName((String)var2);
                  } catch (BeanAlreadyExistsException var56) {
                     System.out.println("Warning: multiple definitions with same name: " + var56.getMessage());
                  }
               } else if (var1.equals("EnableResourceHealthMonitoring")) {
                  try {
                     this.bean.setEnableResourceHealthMonitoring(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var55) {
                     System.out.println("Warning: multiple definitions with same name: " + var55.getMessage());
                  }
               } else if (var1.equals("HighestNumUnavailable")) {
                  try {
                     this.bean.setHighestNumUnavailable(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var54) {
                     System.out.println("Warning: multiple definitions with same name: " + var54.getMessage());
                  }
               } else if (var1.equals("HighestNumWaiters")) {
                  try {
                     this.bean.setHighestNumWaiters(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var53) {
                     System.out.println("Warning: multiple definitions with same name: " + var53.getMessage());
                  }
               } else if (var1.equals("InactiveConnectionTimeoutSeconds")) {
                  try {
                     this.bean.setInactiveConnectionTimeoutSeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var52) {
                     System.out.println("Warning: multiple definitions with same name: " + var52.getMessage());
                  }
               } else if (var1.equals("InitSQL")) {
                  try {
                     this.bean.setInitSQL((String)var2);
                  } catch (BeanAlreadyExistsException var51) {
                     System.out.println("Warning: multiple definitions with same name: " + var51.getMessage());
                  }
               } else if (var1.equals("InitialCapacity")) {
                  try {
                     this.bean.setInitialCapacity(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var50) {
                     System.out.println("Warning: multiple definitions with same name: " + var50.getMessage());
                  }
               } else if (var1.equals("JDBCSystemResource")) {
                  this.bean.setJDBCSystemResourceAsString((String)var2);
               } else if (var1.equals("JDBCXADebugLevel")) {
                  try {
                     this.bean.setJDBCXADebugLevel(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var49) {
                     System.out.println("Warning: multiple definitions with same name: " + var49.getMessage());
                  }
               } else if (var1.equals("KeepLogicalConnOpenOnRelease")) {
                  try {
                     this.bean.setKeepLogicalConnOpenOnRelease(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var48) {
                     System.out.println("Warning: multiple definitions with same name: " + var48.getMessage());
                  }
               } else if (var1.equals("KeepXAConnTillTxComplete")) {
                  try {
                     this.bean.setKeepXAConnTillTxComplete(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var47) {
                     System.out.println("Warning: multiple definitions with same name: " + var47.getMessage());
                  }
               } else if (var1.equals("LoginDelaySeconds")) {
                  try {
                     this.bean.setLoginDelaySeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var46) {
                     System.out.println("Warning: multiple definitions with same name: " + var46.getMessage());
                  }
               } else if (var1.equals("MaxCapacity")) {
                  try {
                     this.bean.setMaxCapacity(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var45) {
                     System.out.println("Warning: multiple definitions with same name: " + var45.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var44) {
                     System.out.println("Warning: multiple definitions with same name: " + var44.getMessage());
                  }
               } else if (var1.equals("NeedTxCtxOnClose")) {
                  try {
                     this.bean.setNeedTxCtxOnClose(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var43) {
                     System.out.println("Warning: multiple definitions with same name: " + var43.getMessage());
                  }
               } else if (var1.equals("NewXAConnForCommit")) {
                  try {
                     this.bean.setNewXAConnForCommit(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var42) {
                     System.out.println("Warning: multiple definitions with same name: " + var42.getMessage());
                  }
               } else if (var1.equals("Password")) {
                  try {
                     if (this.bean.isPasswordEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to Password [ JDBCConnectionPoolMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setPassword((String)var2);
                  } catch (BeanAlreadyExistsException var41) {
                     System.out.println("Warning: multiple definitions with same name: " + var41.getMessage());
                  }
               } else if (var1.equals("PasswordEncrypted")) {
                  if (this.bean.isPasswordEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to PasswordEncrypted [ JDBCConnectionPoolMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setPasswordEncryptedAsString((String)var2);
               } else if (var1.equals("PrepStmtCacheProfilingThreshold")) {
                  try {
                     this.bean.setPrepStmtCacheProfilingThreshold(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var40) {
                     System.out.println("Warning: multiple definitions with same name: " + var40.getMessage());
                  }
               } else if (var1.equals("PreparedStatementCacheSize")) {
                  try {
                     this.bean.setPreparedStatementCacheSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var39) {
                     System.out.println("Warning: multiple definitions with same name: " + var39.getMessage());
                  }
               } else if (var1.equals("Properties")) {
                  this.bean.setPropertiesAsString((String)var2);
               } else if (var1.equals("RecoverOnlyOnce")) {
                  try {
                     this.bean.setRecoverOnlyOnce(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var38) {
                     System.out.println("Warning: multiple definitions with same name: " + var38.getMessage());
                  }
               } else if (var1.equals("RefreshMinutes")) {
                  try {
                     this.bean.setRefreshMinutes(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var37) {
                     System.out.println("Warning: multiple definitions with same name: " + var37.getMessage());
                  }
               } else if (var1.equals("RollbackLocalTxUponConnClose")) {
                  try {
                     this.bean.setRollbackLocalTxUponConnClose(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var36) {
                     System.out.println("Warning: multiple definitions with same name: " + var36.getMessage());
                  }
               } else if (var1.equals("SecondsToTrustAnIdlePoolConnection")) {
                  try {
                     this.bean.setSecondsToTrustAnIdlePoolConnection(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var35) {
                     System.out.println("Warning: multiple definitions with same name: " + var35.getMessage());
                  }
               } else if (var1.equals("ShrinkFrequencySeconds")) {
                  try {
                     this.bean.setShrinkFrequencySeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var34) {
                     System.out.println("Warning: multiple definitions with same name: " + var34.getMessage());
                  }
               } else if (var1.equals("ShrinkPeriodMinutes")) {
                  try {
                     this.bean.setShrinkPeriodMinutes(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var33) {
                     System.out.println("Warning: multiple definitions with same name: " + var33.getMessage());
                  }
               } else if (var1.equals("SqlStmtMaxParamLength")) {
                  try {
                     this.bean.setSqlStmtMaxParamLength(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var32) {
                     System.out.println("Warning: multiple definitions with same name: " + var32.getMessage());
                  }
               } else if (var1.equals("StatementCacheSize")) {
                  try {
                     this.bean.setStatementCacheSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var31) {
                     System.out.println("Warning: multiple definitions with same name: " + var31.getMessage());
                  }
               } else if (var1.equals("StatementCacheType")) {
                  try {
                     this.bean.setStatementCacheType((String)var2);
                  } catch (BeanAlreadyExistsException var30) {
                     System.out.println("Warning: multiple definitions with same name: " + var30.getMessage());
                  }
               } else if (var1.equals("StatementTimeout")) {
                  try {
                     this.bean.setStatementTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var29) {
                     System.out.println("Warning: multiple definitions with same name: " + var29.getMessage());
                  }
               } else if (var1.equals("SupportsLocalTransaction")) {
                  try {
                     this.bean.setSupportsLocalTransaction(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var28) {
                     System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                  }
               } else if (var1.equals("Targets")) {
                  this.bean.setTargetsAsString((String)var2);
               } else if (var1.equals("TestConnectionsOnCreate")) {
                  try {
                     this.bean.setTestConnectionsOnCreate(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                  }
               } else if (var1.equals("TestConnectionsOnRelease")) {
                  try {
                     this.bean.setTestConnectionsOnRelease(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("TestConnectionsOnReserve")) {
                  try {
                     this.bean.setTestConnectionsOnReserve(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("TestFrequencySeconds")) {
                  try {
                     this.bean.setTestFrequencySeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("TestStatementTimeout")) {
                  try {
                     this.bean.setTestStatementTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("TestTableName")) {
                  try {
                     this.bean.setTestTableName((String)var2);
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("URL")) {
                  try {
                     this.bean.setURL((String)var2);
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("XAEndOnlyOnce")) {
                  try {
                     this.bean.setXAEndOnlyOnce(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("XAPassword")) {
                  try {
                     if (this.bean.isXAPasswordEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to XAPassword [ JDBCConnectionPoolMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setXAPassword((String)var2);
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("XAPasswordEncrypted")) {
                  if (this.bean.isXAPasswordEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to XAPasswordEncrypted [ JDBCConnectionPoolMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setXAPasswordEncryptedAsString((String)var2);
               } else if (var1.equals("XAPreparedStatementCacheSize")) {
                  try {
                     this.bean.setXAPreparedStatementCacheSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("XARetryDurationSeconds")) {
                  try {
                     this.bean.setXARetryDurationSeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("XARetryIntervalSeconds")) {
                  try {
                     this.bean.setXARetryIntervalSeconds(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("XASetTransactionTimeout")) {
                  try {
                     this.bean.setXASetTransactionTimeout(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("XATransactionTimeout")) {
                  try {
                     this.bean.setXATransactionTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("ConnLeakProfilingEnabled")) {
                  try {
                     this.bean.setConnLeakProfilingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("ConnProfilingEnabled")) {
                  try {
                     this.bean.setConnProfilingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("CredentialMappingEnabled")) {
                  try {
                     this.bean.setCredentialMappingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("IgnoreInUseConnectionsEnabled")) {
                  try {
                     this.bean.setIgnoreInUseConnectionsEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("PrepStmtCacheProfilingEnabled")) {
                  try {
                     this.bean.setPrepStmtCacheProfilingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("RemoveInfectedConnectionsEnabled")) {
                  try {
                     this.bean.setRemoveInfectedConnectionsEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("ShrinkingEnabled")) {
                  try {
                     this.bean.setShrinkingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("SqlStmtParamLoggingEnabled")) {
                  try {
                     this.bean.setSqlStmtParamLoggingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("SqlStmtProfilingEnabled")) {
                  try {
                     this.bean.setSqlStmtProfilingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var63) {
         System.out.println(var63 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var63;
      } catch (RuntimeException var64) {
         throw var64;
      } catch (Exception var65) {
         if (var65 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var65);
         } else if (var65 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var65.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var65);
         }
      }
   }
}
