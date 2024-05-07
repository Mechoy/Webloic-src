package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorValidateException;
import weblogic.descriptor.beangen.StringHelper;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.JDBCConnectionPool;
import weblogic.management.runtime.JDBCConnectionPoolRuntimeMBean;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class JDBCConnectionPoolMBeanImpl extends DeploymentMBeanImpl implements JDBCConnectionPoolMBean, Serializable {
   private String _ACLName;
   private int _CapacityIncrement;
   private boolean _ConnLeakProfilingEnabled;
   private boolean _ConnProfilingEnabled;
   private int _ConnectionCreationRetryFrequencySeconds;
   private int _ConnectionReserveTimeoutSeconds;
   private int _CountOfRefreshFailuresTillDisable;
   private int _CountOfTestFailuresTillFlush;
   private boolean _CredentialMappingEnabled;
   private String _DriverName;
   private boolean _EnableResourceHealthMonitoring;
   private int _HighestNumUnavailable;
   private int _HighestNumWaiters;
   private boolean _IgnoreInUseConnectionsEnabled;
   private int _InactiveConnectionTimeoutSeconds;
   private String _InitSQL;
   private int _InitialCapacity;
   private JDBCConnectionPoolRuntimeMBean _JDBCConnectionPoolRuntime;
   private JDBCSystemResourceMBean _JDBCSystemResource;
   private int _JDBCXADebugLevel;
   private boolean _KeepLogicalConnOpenOnRelease;
   private boolean _KeepXAConnTillTxComplete;
   private int _LoginDelaySeconds;
   private int _MaxCapacity;
   private String _Name;
   private boolean _NeedTxCtxOnClose;
   private boolean _NewXAConnForCommit;
   private String _Password;
   private byte[] _PasswordEncrypted;
   private boolean _PrepStmtCacheProfilingEnabled;
   private int _PrepStmtCacheProfilingThreshold;
   private int _PreparedStatementCacheSize;
   private Properties _Properties;
   private boolean _RecoverOnlyOnce;
   private int _RefreshMinutes;
   private boolean _RemoveInfectedConnectionsEnabled;
   private boolean _RollbackLocalTxUponConnClose;
   private int _SecondsToTrustAnIdlePoolConnection;
   private int _ShrinkFrequencySeconds;
   private int _ShrinkPeriodMinutes;
   private boolean _ShrinkingEnabled;
   private int _SqlStmtMaxParamLength;
   private boolean _SqlStmtParamLoggingEnabled;
   private boolean _SqlStmtProfilingEnabled;
   private int _StatementCacheSize;
   private String _StatementCacheType;
   private int _StatementTimeout;
   private boolean _SupportsLocalTransaction;
   private TargetMBean[] _Targets;
   private boolean _TestConnectionsOnCreate;
   private boolean _TestConnectionsOnRelease;
   private boolean _TestConnectionsOnReserve;
   private int _TestFrequencySeconds;
   private int _TestStatementTimeout;
   private String _TestTableName;
   private String _URL;
   private boolean _XAEndOnlyOnce;
   private String _XAPassword;
   private byte[] _XAPasswordEncrypted;
   private int _XAPreparedStatementCacheSize;
   private int _XARetryDurationSeconds;
   private int _XARetryIntervalSeconds;
   private boolean _XASetTransactionTimeout;
   private int _XATransactionTimeout;
   private JDBCConnectionPool _customizer;
   private static SchemaHelper2 _schemaHelper;

   public JDBCConnectionPoolMBeanImpl() {
      try {
         this._customizer = new JDBCConnectionPool(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public JDBCConnectionPoolMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new JDBCConnectionPool(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getName() {
      if (!this._isSet(2)) {
         try {
            return ((ConfigurationMBean)this.getParent()).getName();
         } catch (NullPointerException var2) {
         }
      }

      return this._customizer.getName();
   }

   public TargetMBean[] getTargets() {
      return this._customizer.getTargets();
   }

   public String getTargetsAsString() {
      return this._getHelper()._serializeKeyList(this.getTargets());
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isTargetsSet() {
      return this._isSet(7);
   }

   public void setJDBCSystemResource(JDBCSystemResourceMBean var1) {
      JDBCSystemResourceMBean var2 = this.getJDBCSystemResource();
      this._customizer.setJDBCSystemResource(var1);
      this._postSet(9, var2, var1);
   }

   public void setTargetsAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._Targets);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, TargetMBean.class, new ReferenceManager.Resolver(this, 7) {
                  public void resolveReference(Object var1) {
                     try {
                        JDBCConnectionPoolMBeanImpl.this.addTarget((TargetMBean)var1);
                     } catch (RuntimeException var3) {
                        throw var3;
                     } catch (Exception var4) {
                        throw new AssertionError("Impossible exception: " + var4);
                     }
                  }
               });
            }
         }

         Iterator var14 = var3.iterator();

         while(true) {
            while(var14.hasNext()) {
               var5 = (String)var14.next();
               TargetMBean[] var6 = this._Targets;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  TargetMBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removeTarget(var9);
                        break;
                     } catch (RuntimeException var11) {
                        throw var11;
                     } catch (Exception var12) {
                        throw new AssertionError("Impossible exception: " + var12);
                     }
                  }
               }
            }

            return;
         }
      } else {
         TargetMBean[] var2 = this._Targets;
         this._initializeProperty(7);
         this._postSet(7, var2, this._Targets);
      }
   }

   public JDBCSystemResourceMBean getJDBCSystemResource() {
      return this._customizer.getJDBCSystemResource();
   }

   public String getJDBCSystemResourceAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getJDBCSystemResource();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isJDBCSystemResourceSet() {
      return this._isSet(9);
   }

   public void setJDBCSystemResourceAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JDBCSystemResourceMBean.class, new ReferenceManager.Resolver(this, 9) {
            public void resolveReference(Object var1) {
               try {
                  JDBCConnectionPoolMBeanImpl.this.setJDBCSystemResource((JDBCSystemResourceMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JDBCSystemResourceMBean var2 = this._JDBCSystemResource;
         this._initializeProperty(9);
         this._postSet(9, var2, this._JDBCSystemResource);
      }

   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Name", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("Name", var1);
      ConfigurationValidator.validateName(var1);
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(2, var2, var1);
   }

   public void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      Object var4 = var1 == null ? new TargetMBeanImpl[0] : var1;
      var1 = (TargetMBean[])((TargetMBean[])this._getHelper()._cleanAndValidateArray(var4, TargetMBean.class));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 7, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return JDBCConnectionPoolMBeanImpl.this.getTargets();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      TargetMBean[] var5 = this.getTargets();
      this._customizer.setTargets(var1);
      this._postSet(7, var5, var1);
   }

   public boolean addTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 7)) {
         TargetMBean[] var2 = (TargetMBean[])((TargetMBean[])this._getHelper()._extendArray(this.getTargets(), TargetMBean.class, var1));

         try {
            this.setTargets(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public boolean isPrepStmtCacheProfilingEnabled() {
      return this._customizer.isPrepStmtCacheProfilingEnabled();
   }

   public boolean isPrepStmtCacheProfilingEnabledSet() {
      return this._isSet(10);
   }

   public boolean removeTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      TargetMBean[] var2 = this.getTargets();
      TargetMBean[] var3 = (TargetMBean[])((TargetMBean[])this._getHelper()._removeElement(var2, TargetMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setTargets(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else if (var5 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var5;
            } else if (var5 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public void setPrepStmtCacheProfilingEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.isPrepStmtCacheProfilingEnabled();
      this._customizer.setPrepStmtCacheProfilingEnabled(var1);
      this._postSet(10, var2, var1);
   }

   public int getPrepStmtCacheProfilingThreshold() {
      return this._customizer.getPrepStmtCacheProfilingThreshold();
   }

   public boolean isPrepStmtCacheProfilingThresholdSet() {
      return this._isSet(11);
   }

   public void setPrepStmtCacheProfilingThreshold(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("PrepStmtCacheProfilingThreshold", var1, 10);
      int var2 = this.getPrepStmtCacheProfilingThreshold();
      this._customizer.setPrepStmtCacheProfilingThreshold(var1);
      this._postSet(11, var2, var1);
   }

   public void setConnLeakProfilingEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.isConnLeakProfilingEnabled();
      this._customizer.setConnLeakProfilingEnabled(var1);
      this._postSet(12, var2, var1);
   }

   public boolean isConnLeakProfilingEnabled() {
      return this._customizer.isConnLeakProfilingEnabled();
   }

   public boolean isConnLeakProfilingEnabledSet() {
      return this._isSet(12);
   }

   public void setConnProfilingEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.isConnProfilingEnabled();
      this._customizer.setConnProfilingEnabled(var1);
      this._postSet(13, var2, var1);
   }

   public boolean isConnProfilingEnabled() {
      return this._customizer.isConnProfilingEnabled();
   }

   public boolean isConnProfilingEnabledSet() {
      return this._isSet(13);
   }

   public boolean isSqlStmtProfilingEnabled() {
      return this._customizer.isSqlStmtProfilingEnabled();
   }

   public boolean isSqlStmtProfilingEnabledSet() {
      return this._isSet(14);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setSqlStmtProfilingEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.isSqlStmtProfilingEnabled();
      this._customizer.setSqlStmtProfilingEnabled(var1);
      this._postSet(14, var2, var1);
   }

   public boolean isSqlStmtParamLoggingEnabled() {
      return this._customizer.isSqlStmtParamLoggingEnabled();
   }

   public boolean isSqlStmtParamLoggingEnabledSet() {
      return this._isSet(15);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setSqlStmtParamLoggingEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.isSqlStmtParamLoggingEnabled();
      this._customizer.setSqlStmtParamLoggingEnabled(var1);
      this._postSet(15, var2, var1);
   }

   public int getSqlStmtMaxParamLength() {
      return this._customizer.getSqlStmtMaxParamLength();
   }

   public boolean isSqlStmtMaxParamLengthSet() {
      return this._isSet(16);
   }

   public void setSqlStmtMaxParamLength(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("SqlStmtMaxParamLength", var1, 1);
      int var2 = this.getSqlStmtMaxParamLength();
      this._customizer.setSqlStmtMaxParamLength(var1);
      this._postSet(16, var2, var1);
   }

   public String getACLName() {
      return this._ACLName;
   }

   public boolean isACLNameSet() {
      return this._isSet(17);
   }

   public void setACLName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ACLName;
      this._ACLName = var1;
      this._postSet(17, var2, var1);
   }

   public String getURL() {
      return this._customizer.getURL();
   }

   public boolean isURLSet() {
      return this._isSet(18);
   }

   public void setURL(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getURL();
      this._customizer.setURL(var1);
      this._postSet(18, var2, var1);
   }

   public String getDriverName() {
      return this._customizer.getDriverName();
   }

   public boolean isDriverNameSet() {
      return this._isSet(19);
   }

   public void setDriverName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getDriverName();
      this._customizer.setDriverName(var1);
      this._postSet(19, var2, var1);
   }

   public Properties getProperties() {
      return this._customizer.getProperties();
   }

   public String getPropertiesAsString() {
      return StringHelper.objectToString(this.getProperties());
   }

   public boolean isPropertiesSet() {
      return this._isSet(20);
   }

   public void setPropertiesAsString(String var1) {
      try {
         this.setProperties(StringHelper.stringToProperties(var1));
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void setProperties(Properties var1) throws InvalidAttributeValueException {
      Properties var2 = this.getProperties();
      this._customizer.setProperties(var1);
      this._postSet(20, var2, var1);
   }

   public int getLoginDelaySeconds() {
      return this._customizer.getLoginDelaySeconds();
   }

   public boolean isLoginDelaySecondsSet() {
      return this._isSet(21);
   }

   public void setLoginDelaySeconds(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LoginDelaySeconds", (long)var1, 0L, 2147483647L);
      int var2 = this.getLoginDelaySeconds();
      this._customizer.setLoginDelaySeconds(var1);
      this._postSet(21, var2, var1);
   }

   public int getSecondsToTrustAnIdlePoolConnection() {
      return this._customizer.getSecondsToTrustAnIdlePoolConnection();
   }

   public boolean isSecondsToTrustAnIdlePoolConnectionSet() {
      return this._isSet(22);
   }

   public void setSecondsToTrustAnIdlePoolConnection(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("SecondsToTrustAnIdlePoolConnection", (long)var1, 0L, 2147483647L);
      int var2 = this.getSecondsToTrustAnIdlePoolConnection();
      this._customizer.setSecondsToTrustAnIdlePoolConnection(var1);
      this._postSet(22, var2, var1);
   }

   public int getInitialCapacity() {
      return this._customizer.getInitialCapacity();
   }

   public boolean isInitialCapacitySet() {
      return this._isSet(23);
   }

   public void setInitialCapacity(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("InitialCapacity", (long)var1, 0L, 2147483647L);
      int var2 = this.getInitialCapacity();
      this._customizer.setInitialCapacity(var1);
      this._postSet(23, var2, var1);
   }

   public int getMaxCapacity() {
      return this._customizer.getMaxCapacity();
   }

   public boolean isMaxCapacitySet() {
      return this._isSet(24);
   }

   public void setMaxCapacity(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxCapacity", (long)var1, 1L, 2147483647L);
      int var2 = this.getMaxCapacity();
      this._customizer.setMaxCapacity(var1);
      this._postSet(24, var2, var1);
   }

   public int getCapacityIncrement() {
      return this._customizer.getCapacityIncrement();
   }

   public boolean isCapacityIncrementSet() {
      return this._isSet(25);
   }

   public void setCapacityIncrement(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CapacityIncrement", (long)var1, 1L, 2147483647L);
      int var2 = this.getCapacityIncrement();
      this._customizer.setCapacityIncrement(var1);
      this._postSet(25, var2, var1);
   }

   public boolean isShrinkingEnabled() {
      return this._customizer.isShrinkingEnabled();
   }

   public boolean isShrinkingEnabledSet() {
      return this._isSet(26);
   }

   public void setShrinkingEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.isShrinkingEnabled();
      this._customizer.setShrinkingEnabled(var1);
      this._postSet(26, var2, var1);
   }

   public int getShrinkPeriodMinutes() {
      return this._customizer.getShrinkPeriodMinutes();
   }

   public boolean isShrinkPeriodMinutesSet() {
      return this._isSet(27);
   }

   public void setShrinkPeriodMinutes(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ShrinkPeriodMinutes", (long)var1, 1L, 2147483647L);
      int var2 = this.getShrinkPeriodMinutes();
      this._customizer.setShrinkPeriodMinutes(var1);
      this._postSet(27, var2, var1);
   }

   public int getShrinkFrequencySeconds() {
      return this._customizer.getShrinkFrequencySeconds();
   }

   public boolean isShrinkFrequencySecondsSet() {
      return this._isSet(28);
   }

   public void setShrinkFrequencySeconds(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ShrinkFrequencySeconds", (long)var1, 0L, 2147483647L);
      int var2 = this.getShrinkFrequencySeconds();
      this._customizer.setShrinkFrequencySeconds(var1);
      this._postSet(28, var2, var1);
   }

   public int getRefreshMinutes() {
      return this._RefreshMinutes;
   }

   public boolean isRefreshMinutesSet() {
      return this._isSet(29);
   }

   public void setRefreshMinutes(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RefreshMinutes", (long)var1, 0L, 35791394L);
      int var2 = this._RefreshMinutes;
      this._RefreshMinutes = var1;
      this._postSet(29, var2, var1);
   }

   public int getTestFrequencySeconds() {
      return this._customizer.getTestFrequencySeconds();
   }

   public boolean isTestFrequencySecondsSet() {
      return this._isSet(30);
   }

   public void setTestFrequencySeconds(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("TestFrequencySeconds", (long)var1, 0L, 2147483647L);
      int var2 = this.getTestFrequencySeconds();
      this._customizer.setTestFrequencySeconds(var1);
      this._postSet(30, var2, var1);
   }

   public String getTestTableName() {
      return this._customizer.getTestTableName();
   }

   public boolean isTestTableNameSet() {
      return this._isSet(31);
   }

   public void setTestTableName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getTestTableName();
      this._customizer.setTestTableName(var1);
      this._postSet(31, var2, var1);
   }

   public void setTestConnectionsOnReserve(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.getTestConnectionsOnReserve();
      this._customizer.setTestConnectionsOnReserve(var1);
      this._postSet(32, var2, var1);
   }

   public boolean getTestConnectionsOnReserve() {
      return this._customizer.getTestConnectionsOnReserve();
   }

   public boolean isTestConnectionsOnReserveSet() {
      return this._isSet(32);
   }

   public void setTestConnectionsOnRelease(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.getTestConnectionsOnRelease();
      this._customizer.setTestConnectionsOnRelease(var1);
      this._postSet(33, var2, var1);
   }

   public boolean getTestConnectionsOnRelease() {
      return this._customizer.getTestConnectionsOnRelease();
   }

   public boolean isTestConnectionsOnReleaseSet() {
      return this._isSet(33);
   }

   public void setTestConnectionsOnCreate(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.getTestConnectionsOnCreate();
      this._customizer.setTestConnectionsOnCreate(var1);
      this._postSet(34, var2, var1);
   }

   public boolean getTestConnectionsOnCreate() {
      return this._customizer.getTestConnectionsOnCreate();
   }

   public boolean isTestConnectionsOnCreateSet() {
      return this._isSet(34);
   }

   public JDBCConnectionPoolRuntimeMBean getJDBCConnectionPoolRuntime() {
      return this._JDBCConnectionPoolRuntime;
   }

   public boolean isJDBCConnectionPoolRuntimeSet() {
      return this._isSet(35);
   }

   public void setJDBCConnectionPoolRuntime(JDBCConnectionPoolRuntimeMBean var1) throws InvalidAttributeValueException {
      this._JDBCConnectionPoolRuntime = var1;
   }

   public String getPassword() {
      byte[] var1 = this.getPasswordEncrypted();
      return var1 == null ? null : this._decrypt("Password", var1);
   }

   public boolean isPasswordSet() {
      return this.isPasswordEncryptedSet();
   }

   public void setPassword(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setPasswordEncrypted(var1 == null ? null : this._encrypt("Password", var1));
   }

   public byte[] getPasswordEncrypted() {
      return this._customizer.getPasswordEncrypted();
   }

   public String getPasswordEncryptedAsString() {
      byte[] var1 = this.getPasswordEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isPasswordEncryptedSet() {
      return this._isSet(37);
   }

   public void setPasswordEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setPasswordEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public String getXAPassword() {
      byte[] var1 = this.getXAPasswordEncrypted();
      return var1 == null ? null : this._decrypt("XAPassword", var1);
   }

   public boolean isXAPasswordSet() {
      return this.isXAPasswordEncryptedSet();
   }

   public void setXAPassword(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setXAPasswordEncrypted(var1 == null ? null : this._encrypt("XAPassword", var1));
   }

   public byte[] getXAPasswordEncrypted() {
      return this._getHelper()._cloneArray(this._XAPasswordEncrypted);
   }

   public String getXAPasswordEncryptedAsString() {
      byte[] var1 = this.getXAPasswordEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isXAPasswordEncryptedSet() {
      return this._isSet(39);
   }

   public void setXAPasswordEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setXAPasswordEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public int getJDBCXADebugLevel() {
      return this._customizer.getJDBCXADebugLevel();
   }

   public boolean isJDBCXADebugLevelSet() {
      return this._isSet(40);
   }

   public void setJDBCXADebugLevel(int var1) throws InvalidAttributeValueException {
      int var2 = this.getJDBCXADebugLevel();
      this._customizer.setJDBCXADebugLevel(var1);
      this._postSet(40, var2, var1);
   }

   public boolean getSupportsLocalTransaction() {
      return this._customizer.getSupportsLocalTransaction();
   }

   public boolean isSupportsLocalTransactionSet() {
      return this._isSet(41);
   }

   public void setSupportsLocalTransaction(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.getSupportsLocalTransaction();
      this._customizer.setSupportsLocalTransaction(var1);
      this._postSet(41, var2, var1);
   }

   public boolean getKeepXAConnTillTxComplete() {
      return this._customizer.getKeepXAConnTillTxComplete();
   }

   public boolean isKeepXAConnTillTxCompleteSet() {
      return this._isSet(42);
   }

   public void setKeepXAConnTillTxComplete(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.getKeepXAConnTillTxComplete();
      this._customizer.setKeepXAConnTillTxComplete(var1);
      this._postSet(42, var2, var1);
   }

   public boolean getNeedTxCtxOnClose() {
      return this._customizer.getNeedTxCtxOnClose();
   }

   public boolean isNeedTxCtxOnCloseSet() {
      return this._isSet(43);
   }

   public void setNeedTxCtxOnClose(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.getNeedTxCtxOnClose();
      this._customizer.setNeedTxCtxOnClose(var1);
      this._postSet(43, var2, var1);
   }

   public boolean getXAEndOnlyOnce() {
      return this._customizer.getXAEndOnlyOnce();
   }

   public boolean isXAEndOnlyOnceSet() {
      return this._isSet(44);
   }

   public void setXAEndOnlyOnce(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.getXAEndOnlyOnce();
      this._customizer.setXAEndOnlyOnce(var1);
      this._postSet(44, var2, var1);
   }

   public boolean getNewXAConnForCommit() {
      return this._customizer.getNewXAConnForCommit();
   }

   public boolean isNewXAConnForCommitSet() {
      return this._isSet(45);
   }

   public void setNewXAConnForCommit(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.getNewXAConnForCommit();
      this._customizer.setNewXAConnForCommit(var1);
      this._postSet(45, var2, var1);
   }

   public boolean getKeepLogicalConnOpenOnRelease() {
      return this._customizer.getKeepLogicalConnOpenOnRelease();
   }

   public boolean isKeepLogicalConnOpenOnReleaseSet() {
      return this._isSet(46);
   }

   public void setKeepLogicalConnOpenOnRelease(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.getKeepLogicalConnOpenOnRelease();
      this._customizer.setKeepLogicalConnOpenOnRelease(var1);
      this._postSet(46, var2, var1);
   }

   public int getXAPreparedStatementCacheSize() {
      return this._customizer.getXAPreparedStatementCacheSize();
   }

   public boolean isXAPreparedStatementCacheSizeSet() {
      return this._isSet(47);
   }

   public void setXAPreparedStatementCacheSize(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("XAPreparedStatementCacheSize", (long)var1, -1L, 1024L);
      int var2 = this.getXAPreparedStatementCacheSize();
      this._customizer.setXAPreparedStatementCacheSize(var1);
      this._postSet(47, var2, var1);
   }

   public boolean getEnableResourceHealthMonitoring() {
      return this._customizer.getEnableResourceHealthMonitoring();
   }

   public boolean isEnableResourceHealthMonitoringSet() {
      return this._isSet(48);
   }

   public void setEnableResourceHealthMonitoring(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.getEnableResourceHealthMonitoring();
      this._customizer.setEnableResourceHealthMonitoring(var1);
      this._postSet(48, var2, var1);
   }

   public boolean getRecoverOnlyOnce() {
      return this._customizer.getRecoverOnlyOnce();
   }

   public boolean isRecoverOnlyOnceSet() {
      return this._isSet(49);
   }

   public void setRecoverOnlyOnce(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.getRecoverOnlyOnce();
      this._customizer.setRecoverOnlyOnce(var1);
      this._postSet(49, var2, var1);
   }

   public boolean getXASetTransactionTimeout() {
      return this._customizer.getXASetTransactionTimeout();
   }

   public boolean isXASetTransactionTimeoutSet() {
      return this._isSet(50);
   }

   public void setXASetTransactionTimeout(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.getXASetTransactionTimeout();
      this._customizer.setXASetTransactionTimeout(var1);
      this._postSet(50, var2, var1);
   }

   public int getXATransactionTimeout() {
      return this._XATransactionTimeout;
   }

   public boolean isXATransactionTimeoutSet() {
      return this._isSet(51);
   }

   public void setXATransactionTimeout(int var1) throws InvalidAttributeValueException {
      int var2 = this._XATransactionTimeout;
      this._XATransactionTimeout = var1;
      this._postSet(51, var2, var1);
   }

   public int getXARetryDurationSeconds() {
      return this._XARetryDurationSeconds;
   }

   public boolean isXARetryDurationSecondsSet() {
      return this._isSet(52);
   }

   public void setXARetryDurationSeconds(int var1) throws InvalidAttributeValueException {
      int var2 = this._XARetryDurationSeconds;
      this._XARetryDurationSeconds = var1;
      this._postSet(52, var2, var1);
   }

   public int getXARetryIntervalSeconds() {
      return this._XARetryIntervalSeconds;
   }

   public boolean isXARetryIntervalSecondsSet() {
      return this._isSet(53);
   }

   public void setXARetryIntervalSeconds(int var1) throws InvalidAttributeValueException {
      int var2 = this._XARetryIntervalSeconds;
      this._XARetryIntervalSeconds = var1;
      this._postSet(53, var2, var1);
   }

   public void setPreparedStatementCacheSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("PreparedStatementCacheSize", (long)var1, -1L, 1024L);
      int var2 = this.getPreparedStatementCacheSize();

      try {
         this._customizer.setPreparedStatementCacheSize(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(54, var2, var1);
   }

   public int getPreparedStatementCacheSize() {
      return this._customizer.getPreparedStatementCacheSize();
   }

   public boolean isPreparedStatementCacheSizeSet() {
      return this._isSet(54);
   }

   public void setConnectionReserveTimeoutSeconds(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ConnectionReserveTimeoutSeconds", (long)var1, -1L, 2147483647L);
      int var2 = this.getConnectionReserveTimeoutSeconds();

      try {
         this._customizer.setConnectionReserveTimeoutSeconds(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(55, var2, var1);
   }

   public int getConnectionReserveTimeoutSeconds() {
      return this._customizer.getConnectionReserveTimeoutSeconds();
   }

   public boolean isConnectionReserveTimeoutSecondsSet() {
      return this._isSet(55);
   }

   public void setConnectionCreationRetryFrequencySeconds(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ConnectionCreationRetryFrequencySeconds", (long)var1, 0L, 2147483647L);
      int var2 = this.getConnectionCreationRetryFrequencySeconds();

      try {
         this._customizer.setConnectionCreationRetryFrequencySeconds(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(56, var2, var1);
   }

   public int getConnectionCreationRetryFrequencySeconds() {
      return this._customizer.getConnectionCreationRetryFrequencySeconds();
   }

   public boolean isConnectionCreationRetryFrequencySecondsSet() {
      return this._isSet(56);
   }

   public void setInactiveConnectionTimeoutSeconds(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("InactiveConnectionTimeoutSeconds", (long)var1, 0L, 2147483647L);
      int var2 = this.getInactiveConnectionTimeoutSeconds();

      try {
         this._customizer.setInactiveConnectionTimeoutSeconds(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(57, var2, var1);
   }

   public int getInactiveConnectionTimeoutSeconds() {
      return this._customizer.getInactiveConnectionTimeoutSeconds();
   }

   public boolean isInactiveConnectionTimeoutSecondsSet() {
      return this._isSet(57);
   }

   public void setHighestNumWaiters(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("HighestNumWaiters", (long)var1, 0L, 2147483647L);
      int var2 = this.getHighestNumWaiters();

      try {
         this._customizer.setHighestNumWaiters(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(58, var2, var1);
   }

   public int getHighestNumWaiters() {
      return this._customizer.getHighestNumWaiters();
   }

   public boolean isHighestNumWaitersSet() {
      return this._isSet(58);
   }

   public void setHighestNumUnavailable(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("HighestNumUnavailable", (long)var1, 0L, 2147483647L);
      int var2 = this.getHighestNumUnavailable();

      try {
         this._customizer.setHighestNumUnavailable(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(59, var2, var1);
   }

   public int getHighestNumUnavailable() {
      return this._customizer.getHighestNumUnavailable();
   }

   public boolean isHighestNumUnavailableSet() {
      return this._isSet(59);
   }

   public void setInitSQL(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getInitSQL();
      this._customizer.setInitSQL(var1);
      this._postSet(60, var2, var1);
   }

   public String getInitSQL() {
      return this._customizer.getInitSQL();
   }

   public boolean isInitSQLSet() {
      return this._isSet(60);
   }

   public void setStatementCacheSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("StatementCacheSize", (long)var1, 0L, 1024L);
      int var2 = this.getStatementCacheSize();
      this._customizer.setStatementCacheSize(var1);
      this._postSet(61, var2, var1);
   }

   public int getStatementCacheSize() {
      return this._customizer.getStatementCacheSize();
   }

   public boolean isStatementCacheSizeSet() {
      return this._isSet(61);
   }

   public void setStatementCacheType(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"LRU", "FIXED"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("StatementCacheType", var1, var2);
      String var3 = this.getStatementCacheType();
      this._customizer.setStatementCacheType(var1);
      this._postSet(62, var3, var1);
   }

   public String getStatementCacheType() {
      return this._customizer.getStatementCacheType();
   }

   public boolean isStatementCacheTypeSet() {
      return this._isSet(62);
   }

   public void setRemoveInfectedConnectionsEnabled(boolean var1) {
      boolean var2 = this.isRemoveInfectedConnectionsEnabled();
      this._customizer.setRemoveInfectedConnectionsEnabled(var1);
      this._postSet(63, var2, var1);
   }

   public boolean isRemoveInfectedConnectionsEnabled() {
      return this._customizer.isRemoveInfectedConnectionsEnabled();
   }

   public boolean isRemoveInfectedConnectionsEnabledSet() {
      return this._isSet(63);
   }

   public void setRollbackLocalTxUponConnClose(boolean var1) {
      boolean var2 = this.getRollbackLocalTxUponConnClose();
      this._customizer.setRollbackLocalTxUponConnClose(var1);
      this._postSet(64, var2, var1);
   }

   public boolean getRollbackLocalTxUponConnClose() {
      return this._customizer.getRollbackLocalTxUponConnClose();
   }

   public boolean isRollbackLocalTxUponConnCloseSet() {
      return this._isSet(64);
   }

   public void setTestStatementTimeout(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("TestStatementTimeout", (long)var1, -1L, 2147483647L);
      int var2 = this.getTestStatementTimeout();
      this._customizer.setTestStatementTimeout(var1);
      this._postSet(65, var2, var1);
   }

   public int getTestStatementTimeout() {
      return this._customizer.getTestStatementTimeout();
   }

   public boolean isTestStatementTimeoutSet() {
      return this._isSet(65);
   }

   public void setStatementTimeout(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("StatementTimeout", (long)var1, -1L, 2147483647L);
      int var2 = this.getStatementTimeout();
      this._customizer.setStatementTimeout(var1);
      this._postSet(66, var2, var1);
   }

   public int getStatementTimeout() {
      return this._customizer.getStatementTimeout();
   }

   public boolean isStatementTimeoutSet() {
      return this._isSet(66);
   }

   public void setIgnoreInUseConnectionsEnabled(boolean var1) {
      boolean var2 = this.isIgnoreInUseConnectionsEnabled();
      this._customizer.setIgnoreInUseConnectionsEnabled(var1);
      this._postSet(67, var2, var1);
   }

   public boolean isIgnoreInUseConnectionsEnabled() {
      return this._customizer.isIgnoreInUseConnectionsEnabled();
   }

   public boolean isIgnoreInUseConnectionsEnabledSet() {
      return this._isSet(67);
   }

   public void setCredentialMappingEnabled(boolean var1) {
      boolean var2 = this.isCredentialMappingEnabled();
      this._customizer.setCredentialMappingEnabled(var1);
      this._postSet(68, var2, var1);
   }

   public boolean isCredentialMappingEnabled() {
      return this._customizer.isCredentialMappingEnabled();
   }

   public boolean isCredentialMappingEnabledSet() {
      return this._isSet(68);
   }

   public void setCountOfTestFailuresTillFlush(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("CountOfTestFailuresTillFlush", var1, 0);
      int var2 = this.getCountOfTestFailuresTillFlush();

      try {
         this._customizer.setCountOfTestFailuresTillFlush(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(69, var2, var1);
   }

   public int getCountOfTestFailuresTillFlush() {
      return this._customizer.getCountOfTestFailuresTillFlush();
   }

   public boolean isCountOfTestFailuresTillFlushSet() {
      return this._isSet(69);
   }

   public void setCountOfRefreshFailuresTillDisable(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("CountOfRefreshFailuresTillDisable", var1, 0);
      int var2 = this.getCountOfRefreshFailuresTillDisable();

      try {
         this._customizer.setCountOfRefreshFailuresTillDisable(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(70, var2, var1);
   }

   public int getCountOfRefreshFailuresTillDisable() {
      return this._customizer.getCountOfRefreshFailuresTillDisable();
   }

   public boolean isCountOfRefreshFailuresTillDisableSet() {
      return this._isSet(70);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public void setPasswordEncrypted(byte[] var1) {
      byte[] var2 = this.getPasswordEncrypted();

      try {
         this._customizer.setPasswordEncrypted(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(37, var2, var1);
   }

   public void setXAPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._XAPasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: XAPasswordEncrypted of JDBCConnectionPoolMBean");
      } else {
         this._getHelper()._clearArray(this._XAPasswordEncrypted);
         this._XAPasswordEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(39, var2, var1);
      }
   }

   public boolean _hasKey() {
      return true;
   }

   public boolean _isPropertyAKey(Munger.ReaderEventInfo var1) {
      String var2 = var1.getElementName();
      switch (var2.length()) {
         case 4:
            if (var2.equals("name")) {
               return var1.compareXpaths(this._getPropertyXpath("name"));
            }

            return super._isPropertyAKey(var1);
         default:
            return super._isPropertyAKey(var1);
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
         if (var1 == 36) {
            this._markSet(37, false);
         }

         if (var1 == 38) {
            this._markSet(39, false);
         }
      }

   }

   protected AbstractDescriptorBeanHelper _createHelper() {
      return new Helper(this);
   }

   public boolean _isAnythingSet() {
      return super._isAnythingSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 17;
      }

      try {
         switch (var1) {
            case 17:
               this._ACLName = null;
               if (var2) {
                  break;
               }
            case 25:
               this._customizer.setCapacityIncrement(1);
               if (var2) {
                  break;
               }
            case 56:
               this._customizer.setConnectionCreationRetryFrequencySeconds(0);
               if (var2) {
                  break;
               }
            case 55:
               this._customizer.setConnectionReserveTimeoutSeconds(10);
               if (var2) {
                  break;
               }
            case 70:
               this._customizer.setCountOfRefreshFailuresTillDisable(2);
               if (var2) {
                  break;
               }
            case 69:
               this._customizer.setCountOfTestFailuresTillFlush(2);
               if (var2) {
                  break;
               }
            case 19:
               this._customizer.setDriverName((String)null);
               if (var2) {
                  break;
               }
            case 48:
               this._customizer.setEnableResourceHealthMonitoring(true);
               if (var2) {
                  break;
               }
            case 59:
               this._customizer.setHighestNumUnavailable(0);
               if (var2) {
                  break;
               }
            case 58:
               this._customizer.setHighestNumWaiters(Integer.MAX_VALUE);
               if (var2) {
                  break;
               }
            case 57:
               this._customizer.setInactiveConnectionTimeoutSeconds(0);
               if (var2) {
                  break;
               }
            case 60:
               this._customizer.setInitSQL((String)null);
               if (var2) {
                  break;
               }
            case 23:
               this._customizer.setInitialCapacity(1);
               if (var2) {
                  break;
               }
            case 35:
               this._JDBCConnectionPoolRuntime = null;
               if (var2) {
                  break;
               }
            case 9:
               this._customizer.setJDBCSystemResource((JDBCSystemResourceMBean)null);
               if (var2) {
                  break;
               }
            case 40:
               this._customizer.setJDBCXADebugLevel(10);
               if (var2) {
                  break;
               }
            case 46:
               this._customizer.setKeepLogicalConnOpenOnRelease(false);
               if (var2) {
                  break;
               }
            case 42:
               this._customizer.setKeepXAConnTillTxComplete(false);
               if (var2) {
                  break;
               }
            case 21:
               this._customizer.setLoginDelaySeconds(0);
               if (var2) {
                  break;
               }
            case 24:
               this._customizer.setMaxCapacity(15);
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 43:
               this._customizer.setNeedTxCtxOnClose(false);
               if (var2) {
                  break;
               }
            case 45:
               this._customizer.setNewXAConnForCommit(false);
               if (var2) {
                  break;
               }
            case 36:
               this._customizer.setPasswordEncrypted((byte[])null);
               if (var2) {
                  break;
               }
            case 37:
               this._customizer.setPasswordEncrypted((byte[])null);
               if (var2) {
                  break;
               }
            case 11:
               this._customizer.setPrepStmtCacheProfilingThreshold(10);
               if (var2) {
                  break;
               }
            case 54:
               this._customizer.setPreparedStatementCacheSize(-1);
               if (var2) {
                  break;
               }
            case 20:
               this._customizer.setProperties((Properties)null);
               if (var2) {
                  break;
               }
            case 49:
               this._customizer.setRecoverOnlyOnce(false);
               if (var2) {
                  break;
               }
            case 29:
               this._RefreshMinutes = 0;
               if (var2) {
                  break;
               }
            case 64:
               this._customizer.setRollbackLocalTxUponConnClose(false);
               if (var2) {
                  break;
               }
            case 22:
               this._customizer.setSecondsToTrustAnIdlePoolConnection(10);
               if (var2) {
                  break;
               }
            case 28:
               this._customizer.setShrinkFrequencySeconds(900);
               if (var2) {
                  break;
               }
            case 27:
               this._customizer.setShrinkPeriodMinutes(15);
               if (var2) {
                  break;
               }
            case 16:
               this._customizer.setSqlStmtMaxParamLength(10);
               if (var2) {
                  break;
               }
            case 61:
               this._customizer.setStatementCacheSize(10);
               if (var2) {
                  break;
               }
            case 62:
               this._customizer.setStatementCacheType("LRU");
               if (var2) {
                  break;
               }
            case 66:
               this._customizer.setStatementTimeout(-1);
               if (var2) {
                  break;
               }
            case 41:
               this._customizer.setSupportsLocalTransaction(true);
               if (var2) {
                  break;
               }
            case 7:
               this._customizer.setTargets(new TargetMBean[0]);
               if (var2) {
                  break;
               }
            case 34:
               this._customizer.setTestConnectionsOnCreate(true);
               if (var2) {
                  break;
               }
            case 33:
               this._customizer.setTestConnectionsOnRelease(false);
               if (var2) {
                  break;
               }
            case 32:
               this._customizer.setTestConnectionsOnReserve(false);
               if (var2) {
                  break;
               }
            case 30:
               this._customizer.setTestFrequencySeconds(120);
               if (var2) {
                  break;
               }
            case 65:
               this._customizer.setTestStatementTimeout(-1);
               if (var2) {
                  break;
               }
            case 31:
               this._customizer.setTestTableName((String)null);
               if (var2) {
                  break;
               }
            case 18:
               this._customizer.setURL((String)null);
               if (var2) {
                  break;
               }
            case 44:
               this._customizer.setXAEndOnlyOnce(false);
               if (var2) {
                  break;
               }
            case 38:
               this._XAPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 39:
               this._XAPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 47:
               this._customizer.setXAPreparedStatementCacheSize(-1);
               if (var2) {
                  break;
               }
            case 52:
               this._XARetryDurationSeconds = 0;
               if (var2) {
                  break;
               }
            case 53:
               this._XARetryIntervalSeconds = 60;
               if (var2) {
                  break;
               }
            case 50:
               this._customizer.setXASetTransactionTimeout(false);
               if (var2) {
                  break;
               }
            case 51:
               this._XATransactionTimeout = 0;
               if (var2) {
                  break;
               }
            case 12:
               this._customizer.setConnLeakProfilingEnabled(false);
               if (var2) {
                  break;
               }
            case 13:
               this._customizer.setConnProfilingEnabled(false);
               if (var2) {
                  break;
               }
            case 68:
               this._customizer.setCredentialMappingEnabled(false);
               if (var2) {
                  break;
               }
            case 67:
               this._customizer.setIgnoreInUseConnectionsEnabled(true);
               if (var2) {
                  break;
               }
            case 10:
               this._customizer.setPrepStmtCacheProfilingEnabled(false);
               if (var2) {
                  break;
               }
            case 63:
               this._customizer.setRemoveInfectedConnectionsEnabled(true);
               if (var2) {
                  break;
               }
            case 26:
               this._customizer.setShrinkingEnabled(true);
               if (var2) {
                  break;
               }
            case 15:
               this._customizer.setSqlStmtParamLoggingEnabled(false);
               if (var2) {
                  break;
               }
            case 14:
               this._customizer.setSqlStmtProfilingEnabled(false);
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
            default:
               if (var2) {
                  return false;
               }
         }

         return true;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw (Error)(new AssertionError("Impossible Exception")).initCause(var5);
      }
   }

   public Munger.SchemaHelper _getSchemaHelper() {
      return null;
   }

   public String _getElementName(int var1) {
      return this._getSchemaHelper2().getElementName(var1);
   }

   protected String getSchemaLocation() {
      return "http://xmlns.oracle.com/weblogic/1.0/domain.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/domain";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String getType() {
      return "JDBCConnectionPool";
   }

   public void putValue(String var1, Object var2) {
      String var7;
      if (var1.equals("ACLName")) {
         var7 = this._ACLName;
         this._ACLName = (String)var2;
         this._postSet(17, var7, this._ACLName);
      } else {
         int var4;
         if (var1.equals("CapacityIncrement")) {
            var4 = this._CapacityIncrement;
            this._CapacityIncrement = (Integer)var2;
            this._postSet(25, var4, this._CapacityIncrement);
         } else {
            boolean var5;
            if (var1.equals("ConnLeakProfilingEnabled")) {
               var5 = this._ConnLeakProfilingEnabled;
               this._ConnLeakProfilingEnabled = (Boolean)var2;
               this._postSet(12, var5, this._ConnLeakProfilingEnabled);
            } else if (var1.equals("ConnProfilingEnabled")) {
               var5 = this._ConnProfilingEnabled;
               this._ConnProfilingEnabled = (Boolean)var2;
               this._postSet(13, var5, this._ConnProfilingEnabled);
            } else if (var1.equals("ConnectionCreationRetryFrequencySeconds")) {
               var4 = this._ConnectionCreationRetryFrequencySeconds;
               this._ConnectionCreationRetryFrequencySeconds = (Integer)var2;
               this._postSet(56, var4, this._ConnectionCreationRetryFrequencySeconds);
            } else if (var1.equals("ConnectionReserveTimeoutSeconds")) {
               var4 = this._ConnectionReserveTimeoutSeconds;
               this._ConnectionReserveTimeoutSeconds = (Integer)var2;
               this._postSet(55, var4, this._ConnectionReserveTimeoutSeconds);
            } else if (var1.equals("CountOfRefreshFailuresTillDisable")) {
               var4 = this._CountOfRefreshFailuresTillDisable;
               this._CountOfRefreshFailuresTillDisable = (Integer)var2;
               this._postSet(70, var4, this._CountOfRefreshFailuresTillDisable);
            } else if (var1.equals("CountOfTestFailuresTillFlush")) {
               var4 = this._CountOfTestFailuresTillFlush;
               this._CountOfTestFailuresTillFlush = (Integer)var2;
               this._postSet(69, var4, this._CountOfTestFailuresTillFlush);
            } else if (var1.equals("CredentialMappingEnabled")) {
               var5 = this._CredentialMappingEnabled;
               this._CredentialMappingEnabled = (Boolean)var2;
               this._postSet(68, var5, this._CredentialMappingEnabled);
            } else if (var1.equals("DriverName")) {
               var7 = this._DriverName;
               this._DriverName = (String)var2;
               this._postSet(19, var7, this._DriverName);
            } else if (var1.equals("EnableResourceHealthMonitoring")) {
               var5 = this._EnableResourceHealthMonitoring;
               this._EnableResourceHealthMonitoring = (Boolean)var2;
               this._postSet(48, var5, this._EnableResourceHealthMonitoring);
            } else if (var1.equals("HighestNumUnavailable")) {
               var4 = this._HighestNumUnavailable;
               this._HighestNumUnavailable = (Integer)var2;
               this._postSet(59, var4, this._HighestNumUnavailable);
            } else if (var1.equals("HighestNumWaiters")) {
               var4 = this._HighestNumWaiters;
               this._HighestNumWaiters = (Integer)var2;
               this._postSet(58, var4, this._HighestNumWaiters);
            } else if (var1.equals("IgnoreInUseConnectionsEnabled")) {
               var5 = this._IgnoreInUseConnectionsEnabled;
               this._IgnoreInUseConnectionsEnabled = (Boolean)var2;
               this._postSet(67, var5, this._IgnoreInUseConnectionsEnabled);
            } else if (var1.equals("InactiveConnectionTimeoutSeconds")) {
               var4 = this._InactiveConnectionTimeoutSeconds;
               this._InactiveConnectionTimeoutSeconds = (Integer)var2;
               this._postSet(57, var4, this._InactiveConnectionTimeoutSeconds);
            } else if (var1.equals("InitSQL")) {
               var7 = this._InitSQL;
               this._InitSQL = (String)var2;
               this._postSet(60, var7, this._InitSQL);
            } else if (var1.equals("InitialCapacity")) {
               var4 = this._InitialCapacity;
               this._InitialCapacity = (Integer)var2;
               this._postSet(23, var4, this._InitialCapacity);
            } else if (var1.equals("JDBCConnectionPoolRuntime")) {
               JDBCConnectionPoolRuntimeMBean var11 = this._JDBCConnectionPoolRuntime;
               this._JDBCConnectionPoolRuntime = (JDBCConnectionPoolRuntimeMBean)var2;
               this._postSet(35, var11, this._JDBCConnectionPoolRuntime);
            } else if (var1.equals("JDBCSystemResource")) {
               JDBCSystemResourceMBean var10 = this._JDBCSystemResource;
               this._JDBCSystemResource = (JDBCSystemResourceMBean)var2;
               this._postSet(9, var10, this._JDBCSystemResource);
            } else if (var1.equals("JDBCXADebugLevel")) {
               var4 = this._JDBCXADebugLevel;
               this._JDBCXADebugLevel = (Integer)var2;
               this._postSet(40, var4, this._JDBCXADebugLevel);
            } else if (var1.equals("KeepLogicalConnOpenOnRelease")) {
               var5 = this._KeepLogicalConnOpenOnRelease;
               this._KeepLogicalConnOpenOnRelease = (Boolean)var2;
               this._postSet(46, var5, this._KeepLogicalConnOpenOnRelease);
            } else if (var1.equals("KeepXAConnTillTxComplete")) {
               var5 = this._KeepXAConnTillTxComplete;
               this._KeepXAConnTillTxComplete = (Boolean)var2;
               this._postSet(42, var5, this._KeepXAConnTillTxComplete);
            } else if (var1.equals("LoginDelaySeconds")) {
               var4 = this._LoginDelaySeconds;
               this._LoginDelaySeconds = (Integer)var2;
               this._postSet(21, var4, this._LoginDelaySeconds);
            } else if (var1.equals("MaxCapacity")) {
               var4 = this._MaxCapacity;
               this._MaxCapacity = (Integer)var2;
               this._postSet(24, var4, this._MaxCapacity);
            } else if (var1.equals("Name")) {
               var7 = this._Name;
               this._Name = (String)var2;
               this._postSet(2, var7, this._Name);
            } else if (var1.equals("NeedTxCtxOnClose")) {
               var5 = this._NeedTxCtxOnClose;
               this._NeedTxCtxOnClose = (Boolean)var2;
               this._postSet(43, var5, this._NeedTxCtxOnClose);
            } else if (var1.equals("NewXAConnForCommit")) {
               var5 = this._NewXAConnForCommit;
               this._NewXAConnForCommit = (Boolean)var2;
               this._postSet(45, var5, this._NewXAConnForCommit);
            } else if (var1.equals("Password")) {
               var7 = this._Password;
               this._Password = (String)var2;
               this._postSet(36, var7, this._Password);
            } else {
               byte[] var6;
               if (var1.equals("PasswordEncrypted")) {
                  var6 = this._PasswordEncrypted;
                  this._PasswordEncrypted = (byte[])((byte[])var2);
                  this._postSet(37, var6, this._PasswordEncrypted);
               } else if (var1.equals("PrepStmtCacheProfilingEnabled")) {
                  var5 = this._PrepStmtCacheProfilingEnabled;
                  this._PrepStmtCacheProfilingEnabled = (Boolean)var2;
                  this._postSet(10, var5, this._PrepStmtCacheProfilingEnabled);
               } else if (var1.equals("PrepStmtCacheProfilingThreshold")) {
                  var4 = this._PrepStmtCacheProfilingThreshold;
                  this._PrepStmtCacheProfilingThreshold = (Integer)var2;
                  this._postSet(11, var4, this._PrepStmtCacheProfilingThreshold);
               } else if (var1.equals("PreparedStatementCacheSize")) {
                  var4 = this._PreparedStatementCacheSize;
                  this._PreparedStatementCacheSize = (Integer)var2;
                  this._postSet(54, var4, this._PreparedStatementCacheSize);
               } else if (var1.equals("Properties")) {
                  Properties var9 = this._Properties;
                  this._Properties = (Properties)var2;
                  this._postSet(20, var9, this._Properties);
               } else if (var1.equals("RecoverOnlyOnce")) {
                  var5 = this._RecoverOnlyOnce;
                  this._RecoverOnlyOnce = (Boolean)var2;
                  this._postSet(49, var5, this._RecoverOnlyOnce);
               } else if (var1.equals("RefreshMinutes")) {
                  var4 = this._RefreshMinutes;
                  this._RefreshMinutes = (Integer)var2;
                  this._postSet(29, var4, this._RefreshMinutes);
               } else if (var1.equals("RemoveInfectedConnectionsEnabled")) {
                  var5 = this._RemoveInfectedConnectionsEnabled;
                  this._RemoveInfectedConnectionsEnabled = (Boolean)var2;
                  this._postSet(63, var5, this._RemoveInfectedConnectionsEnabled);
               } else if (var1.equals("RollbackLocalTxUponConnClose")) {
                  var5 = this._RollbackLocalTxUponConnClose;
                  this._RollbackLocalTxUponConnClose = (Boolean)var2;
                  this._postSet(64, var5, this._RollbackLocalTxUponConnClose);
               } else if (var1.equals("SecondsToTrustAnIdlePoolConnection")) {
                  var4 = this._SecondsToTrustAnIdlePoolConnection;
                  this._SecondsToTrustAnIdlePoolConnection = (Integer)var2;
                  this._postSet(22, var4, this._SecondsToTrustAnIdlePoolConnection);
               } else if (var1.equals("ShrinkFrequencySeconds")) {
                  var4 = this._ShrinkFrequencySeconds;
                  this._ShrinkFrequencySeconds = (Integer)var2;
                  this._postSet(28, var4, this._ShrinkFrequencySeconds);
               } else if (var1.equals("ShrinkPeriodMinutes")) {
                  var4 = this._ShrinkPeriodMinutes;
                  this._ShrinkPeriodMinutes = (Integer)var2;
                  this._postSet(27, var4, this._ShrinkPeriodMinutes);
               } else if (var1.equals("ShrinkingEnabled")) {
                  var5 = this._ShrinkingEnabled;
                  this._ShrinkingEnabled = (Boolean)var2;
                  this._postSet(26, var5, this._ShrinkingEnabled);
               } else if (var1.equals("SqlStmtMaxParamLength")) {
                  var4 = this._SqlStmtMaxParamLength;
                  this._SqlStmtMaxParamLength = (Integer)var2;
                  this._postSet(16, var4, this._SqlStmtMaxParamLength);
               } else if (var1.equals("SqlStmtParamLoggingEnabled")) {
                  var5 = this._SqlStmtParamLoggingEnabled;
                  this._SqlStmtParamLoggingEnabled = (Boolean)var2;
                  this._postSet(15, var5, this._SqlStmtParamLoggingEnabled);
               } else if (var1.equals("SqlStmtProfilingEnabled")) {
                  var5 = this._SqlStmtProfilingEnabled;
                  this._SqlStmtProfilingEnabled = (Boolean)var2;
                  this._postSet(14, var5, this._SqlStmtProfilingEnabled);
               } else if (var1.equals("StatementCacheSize")) {
                  var4 = this._StatementCacheSize;
                  this._StatementCacheSize = (Integer)var2;
                  this._postSet(61, var4, this._StatementCacheSize);
               } else if (var1.equals("StatementCacheType")) {
                  var7 = this._StatementCacheType;
                  this._StatementCacheType = (String)var2;
                  this._postSet(62, var7, this._StatementCacheType);
               } else if (var1.equals("StatementTimeout")) {
                  var4 = this._StatementTimeout;
                  this._StatementTimeout = (Integer)var2;
                  this._postSet(66, var4, this._StatementTimeout);
               } else if (var1.equals("SupportsLocalTransaction")) {
                  var5 = this._SupportsLocalTransaction;
                  this._SupportsLocalTransaction = (Boolean)var2;
                  this._postSet(41, var5, this._SupportsLocalTransaction);
               } else if (var1.equals("Targets")) {
                  TargetMBean[] var8 = this._Targets;
                  this._Targets = (TargetMBean[])((TargetMBean[])var2);
                  this._postSet(7, var8, this._Targets);
               } else if (var1.equals("TestConnectionsOnCreate")) {
                  var5 = this._TestConnectionsOnCreate;
                  this._TestConnectionsOnCreate = (Boolean)var2;
                  this._postSet(34, var5, this._TestConnectionsOnCreate);
               } else if (var1.equals("TestConnectionsOnRelease")) {
                  var5 = this._TestConnectionsOnRelease;
                  this._TestConnectionsOnRelease = (Boolean)var2;
                  this._postSet(33, var5, this._TestConnectionsOnRelease);
               } else if (var1.equals("TestConnectionsOnReserve")) {
                  var5 = this._TestConnectionsOnReserve;
                  this._TestConnectionsOnReserve = (Boolean)var2;
                  this._postSet(32, var5, this._TestConnectionsOnReserve);
               } else if (var1.equals("TestFrequencySeconds")) {
                  var4 = this._TestFrequencySeconds;
                  this._TestFrequencySeconds = (Integer)var2;
                  this._postSet(30, var4, this._TestFrequencySeconds);
               } else if (var1.equals("TestStatementTimeout")) {
                  var4 = this._TestStatementTimeout;
                  this._TestStatementTimeout = (Integer)var2;
                  this._postSet(65, var4, this._TestStatementTimeout);
               } else if (var1.equals("TestTableName")) {
                  var7 = this._TestTableName;
                  this._TestTableName = (String)var2;
                  this._postSet(31, var7, this._TestTableName);
               } else if (var1.equals("URL")) {
                  var7 = this._URL;
                  this._URL = (String)var2;
                  this._postSet(18, var7, this._URL);
               } else if (var1.equals("XAEndOnlyOnce")) {
                  var5 = this._XAEndOnlyOnce;
                  this._XAEndOnlyOnce = (Boolean)var2;
                  this._postSet(44, var5, this._XAEndOnlyOnce);
               } else if (var1.equals("XAPassword")) {
                  var7 = this._XAPassword;
                  this._XAPassword = (String)var2;
                  this._postSet(38, var7, this._XAPassword);
               } else if (var1.equals("XAPasswordEncrypted")) {
                  var6 = this._XAPasswordEncrypted;
                  this._XAPasswordEncrypted = (byte[])((byte[])var2);
                  this._postSet(39, var6, this._XAPasswordEncrypted);
               } else if (var1.equals("XAPreparedStatementCacheSize")) {
                  var4 = this._XAPreparedStatementCacheSize;
                  this._XAPreparedStatementCacheSize = (Integer)var2;
                  this._postSet(47, var4, this._XAPreparedStatementCacheSize);
               } else if (var1.equals("XARetryDurationSeconds")) {
                  var4 = this._XARetryDurationSeconds;
                  this._XARetryDurationSeconds = (Integer)var2;
                  this._postSet(52, var4, this._XARetryDurationSeconds);
               } else if (var1.equals("XARetryIntervalSeconds")) {
                  var4 = this._XARetryIntervalSeconds;
                  this._XARetryIntervalSeconds = (Integer)var2;
                  this._postSet(53, var4, this._XARetryIntervalSeconds);
               } else if (var1.equals("XASetTransactionTimeout")) {
                  var5 = this._XASetTransactionTimeout;
                  this._XASetTransactionTimeout = (Boolean)var2;
                  this._postSet(50, var5, this._XASetTransactionTimeout);
               } else if (var1.equals("XATransactionTimeout")) {
                  var4 = this._XATransactionTimeout;
                  this._XATransactionTimeout = (Integer)var2;
                  this._postSet(51, var4, this._XATransactionTimeout);
               } else if (var1.equals("customizer")) {
                  JDBCConnectionPool var3 = this._customizer;
                  this._customizer = (JDBCConnectionPool)var2;
               } else {
                  super.putValue(var1, var2);
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ACLName")) {
         return this._ACLName;
      } else if (var1.equals("CapacityIncrement")) {
         return new Integer(this._CapacityIncrement);
      } else if (var1.equals("ConnLeakProfilingEnabled")) {
         return new Boolean(this._ConnLeakProfilingEnabled);
      } else if (var1.equals("ConnProfilingEnabled")) {
         return new Boolean(this._ConnProfilingEnabled);
      } else if (var1.equals("ConnectionCreationRetryFrequencySeconds")) {
         return new Integer(this._ConnectionCreationRetryFrequencySeconds);
      } else if (var1.equals("ConnectionReserveTimeoutSeconds")) {
         return new Integer(this._ConnectionReserveTimeoutSeconds);
      } else if (var1.equals("CountOfRefreshFailuresTillDisable")) {
         return new Integer(this._CountOfRefreshFailuresTillDisable);
      } else if (var1.equals("CountOfTestFailuresTillFlush")) {
         return new Integer(this._CountOfTestFailuresTillFlush);
      } else if (var1.equals("CredentialMappingEnabled")) {
         return new Boolean(this._CredentialMappingEnabled);
      } else if (var1.equals("DriverName")) {
         return this._DriverName;
      } else if (var1.equals("EnableResourceHealthMonitoring")) {
         return new Boolean(this._EnableResourceHealthMonitoring);
      } else if (var1.equals("HighestNumUnavailable")) {
         return new Integer(this._HighestNumUnavailable);
      } else if (var1.equals("HighestNumWaiters")) {
         return new Integer(this._HighestNumWaiters);
      } else if (var1.equals("IgnoreInUseConnectionsEnabled")) {
         return new Boolean(this._IgnoreInUseConnectionsEnabled);
      } else if (var1.equals("InactiveConnectionTimeoutSeconds")) {
         return new Integer(this._InactiveConnectionTimeoutSeconds);
      } else if (var1.equals("InitSQL")) {
         return this._InitSQL;
      } else if (var1.equals("InitialCapacity")) {
         return new Integer(this._InitialCapacity);
      } else if (var1.equals("JDBCConnectionPoolRuntime")) {
         return this._JDBCConnectionPoolRuntime;
      } else if (var1.equals("JDBCSystemResource")) {
         return this._JDBCSystemResource;
      } else if (var1.equals("JDBCXADebugLevel")) {
         return new Integer(this._JDBCXADebugLevel);
      } else if (var1.equals("KeepLogicalConnOpenOnRelease")) {
         return new Boolean(this._KeepLogicalConnOpenOnRelease);
      } else if (var1.equals("KeepXAConnTillTxComplete")) {
         return new Boolean(this._KeepXAConnTillTxComplete);
      } else if (var1.equals("LoginDelaySeconds")) {
         return new Integer(this._LoginDelaySeconds);
      } else if (var1.equals("MaxCapacity")) {
         return new Integer(this._MaxCapacity);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("NeedTxCtxOnClose")) {
         return new Boolean(this._NeedTxCtxOnClose);
      } else if (var1.equals("NewXAConnForCommit")) {
         return new Boolean(this._NewXAConnForCommit);
      } else if (var1.equals("Password")) {
         return this._Password;
      } else if (var1.equals("PasswordEncrypted")) {
         return this._PasswordEncrypted;
      } else if (var1.equals("PrepStmtCacheProfilingEnabled")) {
         return new Boolean(this._PrepStmtCacheProfilingEnabled);
      } else if (var1.equals("PrepStmtCacheProfilingThreshold")) {
         return new Integer(this._PrepStmtCacheProfilingThreshold);
      } else if (var1.equals("PreparedStatementCacheSize")) {
         return new Integer(this._PreparedStatementCacheSize);
      } else if (var1.equals("Properties")) {
         return this._Properties;
      } else if (var1.equals("RecoverOnlyOnce")) {
         return new Boolean(this._RecoverOnlyOnce);
      } else if (var1.equals("RefreshMinutes")) {
         return new Integer(this._RefreshMinutes);
      } else if (var1.equals("RemoveInfectedConnectionsEnabled")) {
         return new Boolean(this._RemoveInfectedConnectionsEnabled);
      } else if (var1.equals("RollbackLocalTxUponConnClose")) {
         return new Boolean(this._RollbackLocalTxUponConnClose);
      } else if (var1.equals("SecondsToTrustAnIdlePoolConnection")) {
         return new Integer(this._SecondsToTrustAnIdlePoolConnection);
      } else if (var1.equals("ShrinkFrequencySeconds")) {
         return new Integer(this._ShrinkFrequencySeconds);
      } else if (var1.equals("ShrinkPeriodMinutes")) {
         return new Integer(this._ShrinkPeriodMinutes);
      } else if (var1.equals("ShrinkingEnabled")) {
         return new Boolean(this._ShrinkingEnabled);
      } else if (var1.equals("SqlStmtMaxParamLength")) {
         return new Integer(this._SqlStmtMaxParamLength);
      } else if (var1.equals("SqlStmtParamLoggingEnabled")) {
         return new Boolean(this._SqlStmtParamLoggingEnabled);
      } else if (var1.equals("SqlStmtProfilingEnabled")) {
         return new Boolean(this._SqlStmtProfilingEnabled);
      } else if (var1.equals("StatementCacheSize")) {
         return new Integer(this._StatementCacheSize);
      } else if (var1.equals("StatementCacheType")) {
         return this._StatementCacheType;
      } else if (var1.equals("StatementTimeout")) {
         return new Integer(this._StatementTimeout);
      } else if (var1.equals("SupportsLocalTransaction")) {
         return new Boolean(this._SupportsLocalTransaction);
      } else if (var1.equals("Targets")) {
         return this._Targets;
      } else if (var1.equals("TestConnectionsOnCreate")) {
         return new Boolean(this._TestConnectionsOnCreate);
      } else if (var1.equals("TestConnectionsOnRelease")) {
         return new Boolean(this._TestConnectionsOnRelease);
      } else if (var1.equals("TestConnectionsOnReserve")) {
         return new Boolean(this._TestConnectionsOnReserve);
      } else if (var1.equals("TestFrequencySeconds")) {
         return new Integer(this._TestFrequencySeconds);
      } else if (var1.equals("TestStatementTimeout")) {
         return new Integer(this._TestStatementTimeout);
      } else if (var1.equals("TestTableName")) {
         return this._TestTableName;
      } else if (var1.equals("URL")) {
         return this._URL;
      } else if (var1.equals("XAEndOnlyOnce")) {
         return new Boolean(this._XAEndOnlyOnce);
      } else if (var1.equals("XAPassword")) {
         return this._XAPassword;
      } else if (var1.equals("XAPasswordEncrypted")) {
         return this._XAPasswordEncrypted;
      } else if (var1.equals("XAPreparedStatementCacheSize")) {
         return new Integer(this._XAPreparedStatementCacheSize);
      } else if (var1.equals("XARetryDurationSeconds")) {
         return new Integer(this._XARetryDurationSeconds);
      } else if (var1.equals("XARetryIntervalSeconds")) {
         return new Integer(this._XARetryIntervalSeconds);
      } else if (var1.equals("XASetTransactionTimeout")) {
         return new Boolean(this._XASetTransactionTimeout);
      } else if (var1.equals("XATransactionTimeout")) {
         return new Integer(this._XATransactionTimeout);
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      try {
         String[] var0 = new String[]{"LRU", "FIXED"};
         weblogic.descriptor.beangen.LegalChecks.checkInEnum("StatementCacheType", "LRU", var0);
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("Default value for a property  should be one of the legal values. Refer annotation legalValues on property StatementCacheType in JDBCConnectionPoolMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 3:
               if (var1.equals("url")) {
                  return 18;
               }
               break;
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 7:
            case 9:
            case 13:
            case 14:
            case 31:
            case 36:
            case 37:
            case 39:
            case 41:
            case 42:
            default:
               break;
            case 6:
               if (var1.equals("target")) {
                  return 7;
               }
               break;
            case 8:
               if (var1.equals("acl-name")) {
                  return 17;
               }

               if (var1.equals("init-sql")) {
                  return 60;
               }

               if (var1.equals("password")) {
                  return 36;
               }
               break;
            case 10:
               if (var1.equals("properties")) {
                  return 20;
               }
               break;
            case 11:
               if (var1.equals("driver-name")) {
                  return 19;
               }

               if (var1.equals("xa-password")) {
                  return 38;
               }
               break;
            case 12:
               if (var1.equals("max-capacity")) {
                  return 24;
               }
               break;
            case 15:
               if (var1.equals("refresh-minutes")) {
                  return 29;
               }

               if (var1.equals("test-table-name")) {
                  return 31;
               }
               break;
            case 16:
               if (var1.equals("initial-capacity")) {
                  return 23;
               }

               if (var1.equals("xa-end-only-once")) {
                  return 44;
               }
               break;
            case 17:
               if (var1.equals("recover-only-once")) {
                  return 49;
               }

               if (var1.equals("statement-timeout")) {
                  return 66;
               }

               if (var1.equals("shrinking-enabled")) {
                  return 26;
               }
               break;
            case 18:
               if (var1.equals("capacity-increment")) {
                  return 25;
               }

               if (var1.equals("password-encrypted")) {
                  return 37;
               }
               break;
            case 19:
               if (var1.equals("highest-num-waiters")) {
                  return 58;
               }

               if (var1.equals("jdbc-xa-debug-level")) {
                  return 40;
               }

               if (var1.equals("login-delay-seconds")) {
                  return 21;
               }
               break;
            case 20:
               if (var1.equals("jdbc-system-resource")) {
                  return 9;
               }

               if (var1.equals("need-tx-ctx-on-close")) {
                  return 43;
               }

               if (var1.equals("statement-cache-size")) {
                  return 61;
               }

               if (var1.equals("statement-cache-type")) {
                  return 62;
               }
               break;
            case 21:
               if (var1.equals("shrink-period-minutes")) {
                  return 27;
               }

               if (var1.equals("xa-password-encrypted")) {
                  return 39;
               }
               break;
            case 22:
               if (var1.equals("new-xa-conn-for-commit")) {
                  return 45;
               }

               if (var1.equals("test-frequency-seconds")) {
                  return 30;
               }

               if (var1.equals("test-statement-timeout")) {
                  return 65;
               }

               if (var1.equals("xa-transaction-timeout")) {
                  return 51;
               }

               if (var1.equals("conn-profiling-enabled")) {
                  return 13;
               }
               break;
            case 23:
               if (var1.equals("highest-num-unavailable")) {
                  return 59;
               }
               break;
            case 24:
               if (var1.equals("shrink-frequency-seconds")) {
                  return 28;
               }
               break;
            case 25:
               if (var1.equals("sql-stmt-max-param-length")) {
                  return 16;
               }

               if (var1.equals("xa-retry-duration-seconds")) {
                  return 52;
               }

               if (var1.equals("xa-retry-interval-seconds")) {
                  return 53;
               }
               break;
            case 26:
               if (var1.equals("supports-local-transaction")) {
                  return 41;
               }

               if (var1.equals("test-connections-on-create")) {
                  return 34;
               }

               if (var1.equals("xa-set-transaction-timeout")) {
                  return 50;
               }

               if (var1.equals("credential-mapping-enabled")) {
                  return 68;
               }

               if (var1.equals("sql-stmt-profiling-enabled")) {
                  return 14;
               }
               break;
            case 27:
               if (var1.equals("test-connections-on-release")) {
                  return 33;
               }

               if (var1.equals("test-connections-on-reserve")) {
                  return 32;
               }

               if (var1.equals("conn-leak-profiling-enabled")) {
                  return 12;
               }
               break;
            case 28:
               if (var1.equals("jdbc-connection-pool-runtime")) {
                  return 35;
               }
               break;
            case 29:
               if (var1.equals("keep-xa-conn-till-tx-complete")) {
                  return 42;
               }

               if (var1.equals("prepared-statement-cache-size")) {
                  return 54;
               }
               break;
            case 30:
               if (var1.equals("sql-stmt-param-logging-enabled")) {
                  return 15;
               }
               break;
            case 32:
               if (var1.equals("xa-prepared-statement-cache-size")) {
                  return 47;
               }
               break;
            case 33:
               if (var1.equals("count-of-test-failures-till-flush")) {
                  return 69;
               }

               if (var1.equals("enable-resource-health-monitoring")) {
                  return 48;
               }

               if (var1.equals("keep-logical-conn-open-on-release")) {
                  return 46;
               }

               if (var1.equals("rollback-local-tx-upon-conn-close")) {
                  return 64;
               }

               if (var1.equals("ignore-in-use-connections-enabled")) {
                  return 67;
               }

               if (var1.equals("prep-stmt-cache-profiling-enabled")) {
                  return 10;
               }
               break;
            case 34:
               if (var1.equals("connection-reserve-timeout-seconds")) {
                  return 55;
               }
               break;
            case 35:
               if (var1.equals("inactive-connection-timeout-seconds")) {
                  return 57;
               }

               if (var1.equals("prep-stmt-cache-profiling-threshold")) {
                  return 11;
               }

               if (var1.equals("remove-infected-connections-enabled")) {
                  return 63;
               }
               break;
            case 38:
               if (var1.equals("count-of-refresh-failures-till-disable")) {
                  return 70;
               }
               break;
            case 40:
               if (var1.equals("seconds-to-trust-an-idle-pool-connection")) {
                  return 22;
               }
               break;
            case 43:
               if (var1.equals("connection-creation-retry-frequency-seconds")) {
                  return 56;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 2:
               return "name";
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
            default:
               return super.getElementName(var1);
            case 7:
               return "target";
            case 9:
               return "jdbc-system-resource";
            case 10:
               return "prep-stmt-cache-profiling-enabled";
            case 11:
               return "prep-stmt-cache-profiling-threshold";
            case 12:
               return "conn-leak-profiling-enabled";
            case 13:
               return "conn-profiling-enabled";
            case 14:
               return "sql-stmt-profiling-enabled";
            case 15:
               return "sql-stmt-param-logging-enabled";
            case 16:
               return "sql-stmt-max-param-length";
            case 17:
               return "acl-name";
            case 18:
               return "url";
            case 19:
               return "driver-name";
            case 20:
               return "properties";
            case 21:
               return "login-delay-seconds";
            case 22:
               return "seconds-to-trust-an-idle-pool-connection";
            case 23:
               return "initial-capacity";
            case 24:
               return "max-capacity";
            case 25:
               return "capacity-increment";
            case 26:
               return "shrinking-enabled";
            case 27:
               return "shrink-period-minutes";
            case 28:
               return "shrink-frequency-seconds";
            case 29:
               return "refresh-minutes";
            case 30:
               return "test-frequency-seconds";
            case 31:
               return "test-table-name";
            case 32:
               return "test-connections-on-reserve";
            case 33:
               return "test-connections-on-release";
            case 34:
               return "test-connections-on-create";
            case 35:
               return "jdbc-connection-pool-runtime";
            case 36:
               return "password";
            case 37:
               return "password-encrypted";
            case 38:
               return "xa-password";
            case 39:
               return "xa-password-encrypted";
            case 40:
               return "jdbc-xa-debug-level";
            case 41:
               return "supports-local-transaction";
            case 42:
               return "keep-xa-conn-till-tx-complete";
            case 43:
               return "need-tx-ctx-on-close";
            case 44:
               return "xa-end-only-once";
            case 45:
               return "new-xa-conn-for-commit";
            case 46:
               return "keep-logical-conn-open-on-release";
            case 47:
               return "xa-prepared-statement-cache-size";
            case 48:
               return "enable-resource-health-monitoring";
            case 49:
               return "recover-only-once";
            case 50:
               return "xa-set-transaction-timeout";
            case 51:
               return "xa-transaction-timeout";
            case 52:
               return "xa-retry-duration-seconds";
            case 53:
               return "xa-retry-interval-seconds";
            case 54:
               return "prepared-statement-cache-size";
            case 55:
               return "connection-reserve-timeout-seconds";
            case 56:
               return "connection-creation-retry-frequency-seconds";
            case 57:
               return "inactive-connection-timeout-seconds";
            case 58:
               return "highest-num-waiters";
            case 59:
               return "highest-num-unavailable";
            case 60:
               return "init-sql";
            case 61:
               return "statement-cache-size";
            case 62:
               return "statement-cache-type";
            case 63:
               return "remove-infected-connections-enabled";
            case 64:
               return "rollback-local-tx-upon-conn-close";
            case 65:
               return "test-statement-timeout";
            case 66:
               return "statement-timeout";
            case 67:
               return "ignore-in-use-connections-enabled";
            case 68:
               return "credential-mapping-enabled";
            case 69:
               return "count-of-test-failures-till-flush";
            case 70:
               return "count-of-refresh-failures-till-disable";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            default:
               return super.isBean(var1);
         }
      }

      public boolean isKey(int var1) {
         switch (var1) {
            case 2:
               return true;
            default:
               return super.isKey(var1);
         }
      }

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends DeploymentMBeanImpl.Helper {
      private JDBCConnectionPoolMBeanImpl bean;

      protected Helper(JDBCConnectionPoolMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "Targets";
            case 9:
               return "JDBCSystemResource";
            case 10:
               return "PrepStmtCacheProfilingEnabled";
            case 11:
               return "PrepStmtCacheProfilingThreshold";
            case 12:
               return "ConnLeakProfilingEnabled";
            case 13:
               return "ConnProfilingEnabled";
            case 14:
               return "SqlStmtProfilingEnabled";
            case 15:
               return "SqlStmtParamLoggingEnabled";
            case 16:
               return "SqlStmtMaxParamLength";
            case 17:
               return "ACLName";
            case 18:
               return "URL";
            case 19:
               return "DriverName";
            case 20:
               return "Properties";
            case 21:
               return "LoginDelaySeconds";
            case 22:
               return "SecondsToTrustAnIdlePoolConnection";
            case 23:
               return "InitialCapacity";
            case 24:
               return "MaxCapacity";
            case 25:
               return "CapacityIncrement";
            case 26:
               return "ShrinkingEnabled";
            case 27:
               return "ShrinkPeriodMinutes";
            case 28:
               return "ShrinkFrequencySeconds";
            case 29:
               return "RefreshMinutes";
            case 30:
               return "TestFrequencySeconds";
            case 31:
               return "TestTableName";
            case 32:
               return "TestConnectionsOnReserve";
            case 33:
               return "TestConnectionsOnRelease";
            case 34:
               return "TestConnectionsOnCreate";
            case 35:
               return "JDBCConnectionPoolRuntime";
            case 36:
               return "Password";
            case 37:
               return "PasswordEncrypted";
            case 38:
               return "XAPassword";
            case 39:
               return "XAPasswordEncrypted";
            case 40:
               return "JDBCXADebugLevel";
            case 41:
               return "SupportsLocalTransaction";
            case 42:
               return "KeepXAConnTillTxComplete";
            case 43:
               return "NeedTxCtxOnClose";
            case 44:
               return "XAEndOnlyOnce";
            case 45:
               return "NewXAConnForCommit";
            case 46:
               return "KeepLogicalConnOpenOnRelease";
            case 47:
               return "XAPreparedStatementCacheSize";
            case 48:
               return "EnableResourceHealthMonitoring";
            case 49:
               return "RecoverOnlyOnce";
            case 50:
               return "XASetTransactionTimeout";
            case 51:
               return "XATransactionTimeout";
            case 52:
               return "XARetryDurationSeconds";
            case 53:
               return "XARetryIntervalSeconds";
            case 54:
               return "PreparedStatementCacheSize";
            case 55:
               return "ConnectionReserveTimeoutSeconds";
            case 56:
               return "ConnectionCreationRetryFrequencySeconds";
            case 57:
               return "InactiveConnectionTimeoutSeconds";
            case 58:
               return "HighestNumWaiters";
            case 59:
               return "HighestNumUnavailable";
            case 60:
               return "InitSQL";
            case 61:
               return "StatementCacheSize";
            case 62:
               return "StatementCacheType";
            case 63:
               return "RemoveInfectedConnectionsEnabled";
            case 64:
               return "RollbackLocalTxUponConnClose";
            case 65:
               return "TestStatementTimeout";
            case 66:
               return "StatementTimeout";
            case 67:
               return "IgnoreInUseConnectionsEnabled";
            case 68:
               return "CredentialMappingEnabled";
            case 69:
               return "CountOfTestFailuresTillFlush";
            case 70:
               return "CountOfRefreshFailuresTillDisable";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ACLName")) {
            return 17;
         } else if (var1.equals("CapacityIncrement")) {
            return 25;
         } else if (var1.equals("ConnectionCreationRetryFrequencySeconds")) {
            return 56;
         } else if (var1.equals("ConnectionReserveTimeoutSeconds")) {
            return 55;
         } else if (var1.equals("CountOfRefreshFailuresTillDisable")) {
            return 70;
         } else if (var1.equals("CountOfTestFailuresTillFlush")) {
            return 69;
         } else if (var1.equals("DriverName")) {
            return 19;
         } else if (var1.equals("EnableResourceHealthMonitoring")) {
            return 48;
         } else if (var1.equals("HighestNumUnavailable")) {
            return 59;
         } else if (var1.equals("HighestNumWaiters")) {
            return 58;
         } else if (var1.equals("InactiveConnectionTimeoutSeconds")) {
            return 57;
         } else if (var1.equals("InitSQL")) {
            return 60;
         } else if (var1.equals("InitialCapacity")) {
            return 23;
         } else if (var1.equals("JDBCConnectionPoolRuntime")) {
            return 35;
         } else if (var1.equals("JDBCSystemResource")) {
            return 9;
         } else if (var1.equals("JDBCXADebugLevel")) {
            return 40;
         } else if (var1.equals("KeepLogicalConnOpenOnRelease")) {
            return 46;
         } else if (var1.equals("KeepXAConnTillTxComplete")) {
            return 42;
         } else if (var1.equals("LoginDelaySeconds")) {
            return 21;
         } else if (var1.equals("MaxCapacity")) {
            return 24;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("NeedTxCtxOnClose")) {
            return 43;
         } else if (var1.equals("NewXAConnForCommit")) {
            return 45;
         } else if (var1.equals("Password")) {
            return 36;
         } else if (var1.equals("PasswordEncrypted")) {
            return 37;
         } else if (var1.equals("PrepStmtCacheProfilingThreshold")) {
            return 11;
         } else if (var1.equals("PreparedStatementCacheSize")) {
            return 54;
         } else if (var1.equals("Properties")) {
            return 20;
         } else if (var1.equals("RecoverOnlyOnce")) {
            return 49;
         } else if (var1.equals("RefreshMinutes")) {
            return 29;
         } else if (var1.equals("RollbackLocalTxUponConnClose")) {
            return 64;
         } else if (var1.equals("SecondsToTrustAnIdlePoolConnection")) {
            return 22;
         } else if (var1.equals("ShrinkFrequencySeconds")) {
            return 28;
         } else if (var1.equals("ShrinkPeriodMinutes")) {
            return 27;
         } else if (var1.equals("SqlStmtMaxParamLength")) {
            return 16;
         } else if (var1.equals("StatementCacheSize")) {
            return 61;
         } else if (var1.equals("StatementCacheType")) {
            return 62;
         } else if (var1.equals("StatementTimeout")) {
            return 66;
         } else if (var1.equals("SupportsLocalTransaction")) {
            return 41;
         } else if (var1.equals("Targets")) {
            return 7;
         } else if (var1.equals("TestConnectionsOnCreate")) {
            return 34;
         } else if (var1.equals("TestConnectionsOnRelease")) {
            return 33;
         } else if (var1.equals("TestConnectionsOnReserve")) {
            return 32;
         } else if (var1.equals("TestFrequencySeconds")) {
            return 30;
         } else if (var1.equals("TestStatementTimeout")) {
            return 65;
         } else if (var1.equals("TestTableName")) {
            return 31;
         } else if (var1.equals("URL")) {
            return 18;
         } else if (var1.equals("XAEndOnlyOnce")) {
            return 44;
         } else if (var1.equals("XAPassword")) {
            return 38;
         } else if (var1.equals("XAPasswordEncrypted")) {
            return 39;
         } else if (var1.equals("XAPreparedStatementCacheSize")) {
            return 47;
         } else if (var1.equals("XARetryDurationSeconds")) {
            return 52;
         } else if (var1.equals("XARetryIntervalSeconds")) {
            return 53;
         } else if (var1.equals("XASetTransactionTimeout")) {
            return 50;
         } else if (var1.equals("XATransactionTimeout")) {
            return 51;
         } else if (var1.equals("ConnLeakProfilingEnabled")) {
            return 12;
         } else if (var1.equals("ConnProfilingEnabled")) {
            return 13;
         } else if (var1.equals("CredentialMappingEnabled")) {
            return 68;
         } else if (var1.equals("IgnoreInUseConnectionsEnabled")) {
            return 67;
         } else if (var1.equals("PrepStmtCacheProfilingEnabled")) {
            return 10;
         } else if (var1.equals("RemoveInfectedConnectionsEnabled")) {
            return 63;
         } else if (var1.equals("ShrinkingEnabled")) {
            return 26;
         } else if (var1.equals("SqlStmtParamLoggingEnabled")) {
            return 15;
         } else {
            return var1.equals("SqlStmtProfilingEnabled") ? 14 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         return new CombinedIterator(var1);
      }

      protected long computeHashValue(CRC32 var1) {
         try {
            StringBuffer var2 = new StringBuffer();
            long var3 = super.computeHashValue(var1);
            if (var3 != 0L) {
               var2.append(String.valueOf(var3));
            }

            long var5 = 0L;
            if (this.bean.isACLNameSet()) {
               var2.append("ACLName");
               var2.append(String.valueOf(this.bean.getACLName()));
            }

            if (this.bean.isCapacityIncrementSet()) {
               var2.append("CapacityIncrement");
               var2.append(String.valueOf(this.bean.getCapacityIncrement()));
            }

            if (this.bean.isConnectionCreationRetryFrequencySecondsSet()) {
               var2.append("ConnectionCreationRetryFrequencySeconds");
               var2.append(String.valueOf(this.bean.getConnectionCreationRetryFrequencySeconds()));
            }

            if (this.bean.isConnectionReserveTimeoutSecondsSet()) {
               var2.append("ConnectionReserveTimeoutSeconds");
               var2.append(String.valueOf(this.bean.getConnectionReserveTimeoutSeconds()));
            }

            if (this.bean.isCountOfRefreshFailuresTillDisableSet()) {
               var2.append("CountOfRefreshFailuresTillDisable");
               var2.append(String.valueOf(this.bean.getCountOfRefreshFailuresTillDisable()));
            }

            if (this.bean.isCountOfTestFailuresTillFlushSet()) {
               var2.append("CountOfTestFailuresTillFlush");
               var2.append(String.valueOf(this.bean.getCountOfTestFailuresTillFlush()));
            }

            if (this.bean.isDriverNameSet()) {
               var2.append("DriverName");
               var2.append(String.valueOf(this.bean.getDriverName()));
            }

            if (this.bean.isEnableResourceHealthMonitoringSet()) {
               var2.append("EnableResourceHealthMonitoring");
               var2.append(String.valueOf(this.bean.getEnableResourceHealthMonitoring()));
            }

            if (this.bean.isHighestNumUnavailableSet()) {
               var2.append("HighestNumUnavailable");
               var2.append(String.valueOf(this.bean.getHighestNumUnavailable()));
            }

            if (this.bean.isHighestNumWaitersSet()) {
               var2.append("HighestNumWaiters");
               var2.append(String.valueOf(this.bean.getHighestNumWaiters()));
            }

            if (this.bean.isInactiveConnectionTimeoutSecondsSet()) {
               var2.append("InactiveConnectionTimeoutSeconds");
               var2.append(String.valueOf(this.bean.getInactiveConnectionTimeoutSeconds()));
            }

            if (this.bean.isInitSQLSet()) {
               var2.append("InitSQL");
               var2.append(String.valueOf(this.bean.getInitSQL()));
            }

            if (this.bean.isInitialCapacitySet()) {
               var2.append("InitialCapacity");
               var2.append(String.valueOf(this.bean.getInitialCapacity()));
            }

            if (this.bean.isJDBCConnectionPoolRuntimeSet()) {
               var2.append("JDBCConnectionPoolRuntime");
               var2.append(String.valueOf(this.bean.getJDBCConnectionPoolRuntime()));
            }

            if (this.bean.isJDBCSystemResourceSet()) {
               var2.append("JDBCSystemResource");
               var2.append(String.valueOf(this.bean.getJDBCSystemResource()));
            }

            if (this.bean.isJDBCXADebugLevelSet()) {
               var2.append("JDBCXADebugLevel");
               var2.append(String.valueOf(this.bean.getJDBCXADebugLevel()));
            }

            if (this.bean.isKeepLogicalConnOpenOnReleaseSet()) {
               var2.append("KeepLogicalConnOpenOnRelease");
               var2.append(String.valueOf(this.bean.getKeepLogicalConnOpenOnRelease()));
            }

            if (this.bean.isKeepXAConnTillTxCompleteSet()) {
               var2.append("KeepXAConnTillTxComplete");
               var2.append(String.valueOf(this.bean.getKeepXAConnTillTxComplete()));
            }

            if (this.bean.isLoginDelaySecondsSet()) {
               var2.append("LoginDelaySeconds");
               var2.append(String.valueOf(this.bean.getLoginDelaySeconds()));
            }

            if (this.bean.isMaxCapacitySet()) {
               var2.append("MaxCapacity");
               var2.append(String.valueOf(this.bean.getMaxCapacity()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isNeedTxCtxOnCloseSet()) {
               var2.append("NeedTxCtxOnClose");
               var2.append(String.valueOf(this.bean.getNeedTxCtxOnClose()));
            }

            if (this.bean.isNewXAConnForCommitSet()) {
               var2.append("NewXAConnForCommit");
               var2.append(String.valueOf(this.bean.getNewXAConnForCommit()));
            }

            if (this.bean.isPasswordSet()) {
               var2.append("Password");
               var2.append(String.valueOf(this.bean.getPassword()));
            }

            if (this.bean.isPasswordEncryptedSet()) {
               var2.append("PasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getPasswordEncrypted())));
            }

            if (this.bean.isPrepStmtCacheProfilingThresholdSet()) {
               var2.append("PrepStmtCacheProfilingThreshold");
               var2.append(String.valueOf(this.bean.getPrepStmtCacheProfilingThreshold()));
            }

            if (this.bean.isPreparedStatementCacheSizeSet()) {
               var2.append("PreparedStatementCacheSize");
               var2.append(String.valueOf(this.bean.getPreparedStatementCacheSize()));
            }

            if (this.bean.isPropertiesSet()) {
               var2.append("Properties");
               var2.append(String.valueOf(this.bean.getProperties()));
            }

            if (this.bean.isRecoverOnlyOnceSet()) {
               var2.append("RecoverOnlyOnce");
               var2.append(String.valueOf(this.bean.getRecoverOnlyOnce()));
            }

            if (this.bean.isRefreshMinutesSet()) {
               var2.append("RefreshMinutes");
               var2.append(String.valueOf(this.bean.getRefreshMinutes()));
            }

            if (this.bean.isRollbackLocalTxUponConnCloseSet()) {
               var2.append("RollbackLocalTxUponConnClose");
               var2.append(String.valueOf(this.bean.getRollbackLocalTxUponConnClose()));
            }

            if (this.bean.isSecondsToTrustAnIdlePoolConnectionSet()) {
               var2.append("SecondsToTrustAnIdlePoolConnection");
               var2.append(String.valueOf(this.bean.getSecondsToTrustAnIdlePoolConnection()));
            }

            if (this.bean.isShrinkFrequencySecondsSet()) {
               var2.append("ShrinkFrequencySeconds");
               var2.append(String.valueOf(this.bean.getShrinkFrequencySeconds()));
            }

            if (this.bean.isShrinkPeriodMinutesSet()) {
               var2.append("ShrinkPeriodMinutes");
               var2.append(String.valueOf(this.bean.getShrinkPeriodMinutes()));
            }

            if (this.bean.isSqlStmtMaxParamLengthSet()) {
               var2.append("SqlStmtMaxParamLength");
               var2.append(String.valueOf(this.bean.getSqlStmtMaxParamLength()));
            }

            if (this.bean.isStatementCacheSizeSet()) {
               var2.append("StatementCacheSize");
               var2.append(String.valueOf(this.bean.getStatementCacheSize()));
            }

            if (this.bean.isStatementCacheTypeSet()) {
               var2.append("StatementCacheType");
               var2.append(String.valueOf(this.bean.getStatementCacheType()));
            }

            if (this.bean.isStatementTimeoutSet()) {
               var2.append("StatementTimeout");
               var2.append(String.valueOf(this.bean.getStatementTimeout()));
            }

            if (this.bean.isSupportsLocalTransactionSet()) {
               var2.append("SupportsLocalTransaction");
               var2.append(String.valueOf(this.bean.getSupportsLocalTransaction()));
            }

            if (this.bean.isTargetsSet()) {
               var2.append("Targets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTargets())));
            }

            if (this.bean.isTestConnectionsOnCreateSet()) {
               var2.append("TestConnectionsOnCreate");
               var2.append(String.valueOf(this.bean.getTestConnectionsOnCreate()));
            }

            if (this.bean.isTestConnectionsOnReleaseSet()) {
               var2.append("TestConnectionsOnRelease");
               var2.append(String.valueOf(this.bean.getTestConnectionsOnRelease()));
            }

            if (this.bean.isTestConnectionsOnReserveSet()) {
               var2.append("TestConnectionsOnReserve");
               var2.append(String.valueOf(this.bean.getTestConnectionsOnReserve()));
            }

            if (this.bean.isTestFrequencySecondsSet()) {
               var2.append("TestFrequencySeconds");
               var2.append(String.valueOf(this.bean.getTestFrequencySeconds()));
            }

            if (this.bean.isTestStatementTimeoutSet()) {
               var2.append("TestStatementTimeout");
               var2.append(String.valueOf(this.bean.getTestStatementTimeout()));
            }

            if (this.bean.isTestTableNameSet()) {
               var2.append("TestTableName");
               var2.append(String.valueOf(this.bean.getTestTableName()));
            }

            if (this.bean.isURLSet()) {
               var2.append("URL");
               var2.append(String.valueOf(this.bean.getURL()));
            }

            if (this.bean.isXAEndOnlyOnceSet()) {
               var2.append("XAEndOnlyOnce");
               var2.append(String.valueOf(this.bean.getXAEndOnlyOnce()));
            }

            if (this.bean.isXAPasswordSet()) {
               var2.append("XAPassword");
               var2.append(String.valueOf(this.bean.getXAPassword()));
            }

            if (this.bean.isXAPasswordEncryptedSet()) {
               var2.append("XAPasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getXAPasswordEncrypted())));
            }

            if (this.bean.isXAPreparedStatementCacheSizeSet()) {
               var2.append("XAPreparedStatementCacheSize");
               var2.append(String.valueOf(this.bean.getXAPreparedStatementCacheSize()));
            }

            if (this.bean.isXARetryDurationSecondsSet()) {
               var2.append("XARetryDurationSeconds");
               var2.append(String.valueOf(this.bean.getXARetryDurationSeconds()));
            }

            if (this.bean.isXARetryIntervalSecondsSet()) {
               var2.append("XARetryIntervalSeconds");
               var2.append(String.valueOf(this.bean.getXARetryIntervalSeconds()));
            }

            if (this.bean.isXASetTransactionTimeoutSet()) {
               var2.append("XASetTransactionTimeout");
               var2.append(String.valueOf(this.bean.getXASetTransactionTimeout()));
            }

            if (this.bean.isXATransactionTimeoutSet()) {
               var2.append("XATransactionTimeout");
               var2.append(String.valueOf(this.bean.getXATransactionTimeout()));
            }

            if (this.bean.isConnLeakProfilingEnabledSet()) {
               var2.append("ConnLeakProfilingEnabled");
               var2.append(String.valueOf(this.bean.isConnLeakProfilingEnabled()));
            }

            if (this.bean.isConnProfilingEnabledSet()) {
               var2.append("ConnProfilingEnabled");
               var2.append(String.valueOf(this.bean.isConnProfilingEnabled()));
            }

            if (this.bean.isCredentialMappingEnabledSet()) {
               var2.append("CredentialMappingEnabled");
               var2.append(String.valueOf(this.bean.isCredentialMappingEnabled()));
            }

            if (this.bean.isIgnoreInUseConnectionsEnabledSet()) {
               var2.append("IgnoreInUseConnectionsEnabled");
               var2.append(String.valueOf(this.bean.isIgnoreInUseConnectionsEnabled()));
            }

            if (this.bean.isPrepStmtCacheProfilingEnabledSet()) {
               var2.append("PrepStmtCacheProfilingEnabled");
               var2.append(String.valueOf(this.bean.isPrepStmtCacheProfilingEnabled()));
            }

            if (this.bean.isRemoveInfectedConnectionsEnabledSet()) {
               var2.append("RemoveInfectedConnectionsEnabled");
               var2.append(String.valueOf(this.bean.isRemoveInfectedConnectionsEnabled()));
            }

            if (this.bean.isShrinkingEnabledSet()) {
               var2.append("ShrinkingEnabled");
               var2.append(String.valueOf(this.bean.isShrinkingEnabled()));
            }

            if (this.bean.isSqlStmtParamLoggingEnabledSet()) {
               var2.append("SqlStmtParamLoggingEnabled");
               var2.append(String.valueOf(this.bean.isSqlStmtParamLoggingEnabled()));
            }

            if (this.bean.isSqlStmtProfilingEnabledSet()) {
               var2.append("SqlStmtProfilingEnabled");
               var2.append(String.valueOf(this.bean.isSqlStmtProfilingEnabled()));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            JDBCConnectionPoolMBeanImpl var2 = (JDBCConnectionPoolMBeanImpl)var1;
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ACLName", this.bean.getACLName(), var2.getACLName(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("CapacityIncrement", this.bean.getCapacityIncrement(), var2.getCapacityIncrement(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ConnectionCreationRetryFrequencySeconds", this.bean.getConnectionCreationRetryFrequencySeconds(), var2.getConnectionCreationRetryFrequencySeconds(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ConnectionReserveTimeoutSeconds", this.bean.getConnectionReserveTimeoutSeconds(), var2.getConnectionReserveTimeoutSeconds(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("CountOfRefreshFailuresTillDisable", this.bean.getCountOfRefreshFailuresTillDisable(), var2.getCountOfRefreshFailuresTillDisable(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("CountOfTestFailuresTillFlush", this.bean.getCountOfTestFailuresTillFlush(), var2.getCountOfTestFailuresTillFlush(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("DriverName", this.bean.getDriverName(), var2.getDriverName(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("EnableResourceHealthMonitoring", this.bean.getEnableResourceHealthMonitoring(), var2.getEnableResourceHealthMonitoring(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("HighestNumUnavailable", this.bean.getHighestNumUnavailable(), var2.getHighestNumUnavailable(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("HighestNumWaiters", this.bean.getHighestNumWaiters(), var2.getHighestNumWaiters(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("InactiveConnectionTimeoutSeconds", this.bean.getInactiveConnectionTimeoutSeconds(), var2.getInactiveConnectionTimeoutSeconds(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("InitSQL", this.bean.getInitSQL(), var2.getInitSQL(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("InitialCapacity", this.bean.getInitialCapacity(), var2.getInitialCapacity(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("JDBCSystemResource", this.bean.getJDBCSystemResource(), var2.getJDBCSystemResource(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("JDBCXADebugLevel", this.bean.getJDBCXADebugLevel(), var2.getJDBCXADebugLevel(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("KeepLogicalConnOpenOnRelease", this.bean.getKeepLogicalConnOpenOnRelease(), var2.getKeepLogicalConnOpenOnRelease(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("KeepXAConnTillTxComplete", this.bean.getKeepXAConnTillTxComplete(), var2.getKeepXAConnTillTxComplete(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LoginDelaySeconds", this.bean.getLoginDelaySeconds(), var2.getLoginDelaySeconds(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("MaxCapacity", this.bean.getMaxCapacity(), var2.getMaxCapacity(), true);
            }

            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("NeedTxCtxOnClose", this.bean.getNeedTxCtxOnClose(), var2.getNeedTxCtxOnClose(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("NewXAConnForCommit", this.bean.getNewXAConnForCommit(), var2.getNewXAConnForCommit(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("PasswordEncrypted", this.bean.getPasswordEncrypted(), var2.getPasswordEncrypted(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("PrepStmtCacheProfilingThreshold", this.bean.getPrepStmtCacheProfilingThreshold(), var2.getPrepStmtCacheProfilingThreshold(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("PreparedStatementCacheSize", this.bean.getPreparedStatementCacheSize(), var2.getPreparedStatementCacheSize(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("Properties", this.bean.getProperties(), var2.getProperties(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("RecoverOnlyOnce", this.bean.getRecoverOnlyOnce(), var2.getRecoverOnlyOnce(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("RefreshMinutes", this.bean.getRefreshMinutes(), var2.getRefreshMinutes(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("RollbackLocalTxUponConnClose", this.bean.getRollbackLocalTxUponConnClose(), var2.getRollbackLocalTxUponConnClose(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("SecondsToTrustAnIdlePoolConnection", this.bean.getSecondsToTrustAnIdlePoolConnection(), var2.getSecondsToTrustAnIdlePoolConnection(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ShrinkFrequencySeconds", this.bean.getShrinkFrequencySeconds(), var2.getShrinkFrequencySeconds(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ShrinkPeriodMinutes", this.bean.getShrinkPeriodMinutes(), var2.getShrinkPeriodMinutes(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("SqlStmtMaxParamLength", this.bean.getSqlStmtMaxParamLength(), var2.getSqlStmtMaxParamLength(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("StatementCacheSize", this.bean.getStatementCacheSize(), var2.getStatementCacheSize(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("StatementCacheType", this.bean.getStatementCacheType(), var2.getStatementCacheType(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("StatementTimeout", this.bean.getStatementTimeout(), var2.getStatementTimeout(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("SupportsLocalTransaction", this.bean.getSupportsLocalTransaction(), var2.getSupportsLocalTransaction(), false);
            }

            this.computeDiff("Targets", this.bean.getTargets(), var2.getTargets(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("TestConnectionsOnCreate", this.bean.getTestConnectionsOnCreate(), var2.getTestConnectionsOnCreate(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("TestConnectionsOnRelease", this.bean.getTestConnectionsOnRelease(), var2.getTestConnectionsOnRelease(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("TestConnectionsOnReserve", this.bean.getTestConnectionsOnReserve(), var2.getTestConnectionsOnReserve(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("TestFrequencySeconds", this.bean.getTestFrequencySeconds(), var2.getTestFrequencySeconds(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("TestStatementTimeout", this.bean.getTestStatementTimeout(), var2.getTestStatementTimeout(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("TestTableName", this.bean.getTestTableName(), var2.getTestTableName(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("URL", this.bean.getURL(), var2.getURL(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("XAEndOnlyOnce", this.bean.getXAEndOnlyOnce(), var2.getXAEndOnlyOnce(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("XAPasswordEncrypted", this.bean.getXAPasswordEncrypted(), var2.getXAPasswordEncrypted(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("XAPreparedStatementCacheSize", this.bean.getXAPreparedStatementCacheSize(), var2.getXAPreparedStatementCacheSize(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("XARetryDurationSeconds", this.bean.getXARetryDurationSeconds(), var2.getXARetryDurationSeconds(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("XARetryIntervalSeconds", this.bean.getXARetryIntervalSeconds(), var2.getXARetryIntervalSeconds(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("XASetTransactionTimeout", this.bean.getXASetTransactionTimeout(), var2.getXASetTransactionTimeout(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("XATransactionTimeout", this.bean.getXATransactionTimeout(), var2.getXATransactionTimeout(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ConnLeakProfilingEnabled", this.bean.isConnLeakProfilingEnabled(), var2.isConnLeakProfilingEnabled(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ConnProfilingEnabled", this.bean.isConnProfilingEnabled(), var2.isConnProfilingEnabled(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("CredentialMappingEnabled", this.bean.isCredentialMappingEnabled(), var2.isCredentialMappingEnabled(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("IgnoreInUseConnectionsEnabled", this.bean.isIgnoreInUseConnectionsEnabled(), var2.isIgnoreInUseConnectionsEnabled(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("PrepStmtCacheProfilingEnabled", this.bean.isPrepStmtCacheProfilingEnabled(), var2.isPrepStmtCacheProfilingEnabled(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("RemoveInfectedConnectionsEnabled", this.bean.isRemoveInfectedConnectionsEnabled(), var2.isRemoveInfectedConnectionsEnabled(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ShrinkingEnabled", this.bean.isShrinkingEnabled(), var2.isShrinkingEnabled(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("SqlStmtParamLoggingEnabled", this.bean.isSqlStmtParamLoggingEnabled(), var2.isSqlStmtParamLoggingEnabled(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("SqlStmtProfilingEnabled", this.bean.isSqlStmtProfilingEnabled(), var2.isSqlStmtProfilingEnabled(), true);
            }

         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JDBCConnectionPoolMBeanImpl var3 = (JDBCConnectionPoolMBeanImpl)var1.getSourceBean();
            JDBCConnectionPoolMBeanImpl var4 = (JDBCConnectionPoolMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ACLName")) {
                  var3.setACLName(var4.getACLName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("CapacityIncrement")) {
                  var3.setCapacityIncrement(var4.getCapacityIncrement());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 25);
               } else if (var5.equals("ConnectionCreationRetryFrequencySeconds")) {
                  var3.setConnectionCreationRetryFrequencySeconds(var4.getConnectionCreationRetryFrequencySeconds());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 56);
               } else if (var5.equals("ConnectionReserveTimeoutSeconds")) {
                  var3.setConnectionReserveTimeoutSeconds(var4.getConnectionReserveTimeoutSeconds());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 55);
               } else if (var5.equals("CountOfRefreshFailuresTillDisable")) {
                  var3.setCountOfRefreshFailuresTillDisable(var4.getCountOfRefreshFailuresTillDisable());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 70);
               } else if (var5.equals("CountOfTestFailuresTillFlush")) {
                  var3.setCountOfTestFailuresTillFlush(var4.getCountOfTestFailuresTillFlush());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 69);
               } else if (var5.equals("DriverName")) {
                  var3.setDriverName(var4.getDriverName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("EnableResourceHealthMonitoring")) {
                  var3.setEnableResourceHealthMonitoring(var4.getEnableResourceHealthMonitoring());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 48);
               } else if (var5.equals("HighestNumUnavailable")) {
                  var3.setHighestNumUnavailable(var4.getHighestNumUnavailable());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 59);
               } else if (var5.equals("HighestNumWaiters")) {
                  var3.setHighestNumWaiters(var4.getHighestNumWaiters());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 58);
               } else if (var5.equals("InactiveConnectionTimeoutSeconds")) {
                  var3.setInactiveConnectionTimeoutSeconds(var4.getInactiveConnectionTimeoutSeconds());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 57);
               } else if (var5.equals("InitSQL")) {
                  var3.setInitSQL(var4.getInitSQL());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 60);
               } else if (var5.equals("InitialCapacity")) {
                  var3.setInitialCapacity(var4.getInitialCapacity());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 23);
               } else if (!var5.equals("JDBCConnectionPoolRuntime")) {
                  if (var5.equals("JDBCSystemResource")) {
                     var3.setJDBCSystemResourceAsString(var4.getJDBCSystemResourceAsString());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                  } else if (var5.equals("JDBCXADebugLevel")) {
                     var3.setJDBCXADebugLevel(var4.getJDBCXADebugLevel());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 40);
                  } else if (var5.equals("KeepLogicalConnOpenOnRelease")) {
                     var3.setKeepLogicalConnOpenOnRelease(var4.getKeepLogicalConnOpenOnRelease());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 46);
                  } else if (var5.equals("KeepXAConnTillTxComplete")) {
                     var3.setKeepXAConnTillTxComplete(var4.getKeepXAConnTillTxComplete());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 42);
                  } else if (var5.equals("LoginDelaySeconds")) {
                     var3.setLoginDelaySeconds(var4.getLoginDelaySeconds());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 21);
                  } else if (var5.equals("MaxCapacity")) {
                     var3.setMaxCapacity(var4.getMaxCapacity());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 24);
                  } else if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("NeedTxCtxOnClose")) {
                     var3.setNeedTxCtxOnClose(var4.getNeedTxCtxOnClose());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 43);
                  } else if (var5.equals("NewXAConnForCommit")) {
                     var3.setNewXAConnForCommit(var4.getNewXAConnForCommit());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 45);
                  } else if (!var5.equals("Password")) {
                     if (var5.equals("PasswordEncrypted")) {
                        var3.setPasswordEncrypted(var4.getPasswordEncrypted());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 37);
                     } else if (var5.equals("PrepStmtCacheProfilingThreshold")) {
                        var3.setPrepStmtCacheProfilingThreshold(var4.getPrepStmtCacheProfilingThreshold());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                     } else if (var5.equals("PreparedStatementCacheSize")) {
                        var3.setPreparedStatementCacheSize(var4.getPreparedStatementCacheSize());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 54);
                     } else if (var5.equals("Properties")) {
                        var3.setProperties(var4.getProperties() == null ? null : (Properties)var4.getProperties().clone());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 20);
                     } else if (var5.equals("RecoverOnlyOnce")) {
                        var3.setRecoverOnlyOnce(var4.getRecoverOnlyOnce());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 49);
                     } else if (var5.equals("RefreshMinutes")) {
                        var3.setRefreshMinutes(var4.getRefreshMinutes());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 29);
                     } else if (var5.equals("RollbackLocalTxUponConnClose")) {
                        var3.setRollbackLocalTxUponConnClose(var4.getRollbackLocalTxUponConnClose());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 64);
                     } else if (var5.equals("SecondsToTrustAnIdlePoolConnection")) {
                        var3.setSecondsToTrustAnIdlePoolConnection(var4.getSecondsToTrustAnIdlePoolConnection());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 22);
                     } else if (var5.equals("ShrinkFrequencySeconds")) {
                        var3.setShrinkFrequencySeconds(var4.getShrinkFrequencySeconds());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 28);
                     } else if (var5.equals("ShrinkPeriodMinutes")) {
                        var3.setShrinkPeriodMinutes(var4.getShrinkPeriodMinutes());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 27);
                     } else if (var5.equals("SqlStmtMaxParamLength")) {
                        var3.setSqlStmtMaxParamLength(var4.getSqlStmtMaxParamLength());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                     } else if (var5.equals("StatementCacheSize")) {
                        var3.setStatementCacheSize(var4.getStatementCacheSize());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 61);
                     } else if (var5.equals("StatementCacheType")) {
                        var3.setStatementCacheType(var4.getStatementCacheType());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 62);
                     } else if (var5.equals("StatementTimeout")) {
                        var3.setStatementTimeout(var4.getStatementTimeout());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 66);
                     } else if (var5.equals("SupportsLocalTransaction")) {
                        var3.setSupportsLocalTransaction(var4.getSupportsLocalTransaction());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 41);
                     } else if (var5.equals("Targets")) {
                        var3.setTargetsAsString(var4.getTargetsAsString());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                     } else if (var5.equals("TestConnectionsOnCreate")) {
                        var3.setTestConnectionsOnCreate(var4.getTestConnectionsOnCreate());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 34);
                     } else if (var5.equals("TestConnectionsOnRelease")) {
                        var3.setTestConnectionsOnRelease(var4.getTestConnectionsOnRelease());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 33);
                     } else if (var5.equals("TestConnectionsOnReserve")) {
                        var3.setTestConnectionsOnReserve(var4.getTestConnectionsOnReserve());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 32);
                     } else if (var5.equals("TestFrequencySeconds")) {
                        var3.setTestFrequencySeconds(var4.getTestFrequencySeconds());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 30);
                     } else if (var5.equals("TestStatementTimeout")) {
                        var3.setTestStatementTimeout(var4.getTestStatementTimeout());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 65);
                     } else if (var5.equals("TestTableName")) {
                        var3.setTestTableName(var4.getTestTableName());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 31);
                     } else if (var5.equals("URL")) {
                        var3.setURL(var4.getURL());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 18);
                     } else if (var5.equals("XAEndOnlyOnce")) {
                        var3.setXAEndOnlyOnce(var4.getXAEndOnlyOnce());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 44);
                     } else if (!var5.equals("XAPassword")) {
                        if (var5.equals("XAPasswordEncrypted")) {
                           var3.setXAPasswordEncrypted(var4.getXAPasswordEncrypted());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 39);
                        } else if (var5.equals("XAPreparedStatementCacheSize")) {
                           var3.setXAPreparedStatementCacheSize(var4.getXAPreparedStatementCacheSize());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 47);
                        } else if (var5.equals("XARetryDurationSeconds")) {
                           var3.setXARetryDurationSeconds(var4.getXARetryDurationSeconds());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 52);
                        } else if (var5.equals("XARetryIntervalSeconds")) {
                           var3.setXARetryIntervalSeconds(var4.getXARetryIntervalSeconds());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 53);
                        } else if (var5.equals("XASetTransactionTimeout")) {
                           var3.setXASetTransactionTimeout(var4.getXASetTransactionTimeout());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 50);
                        } else if (var5.equals("XATransactionTimeout")) {
                           var3.setXATransactionTimeout(var4.getXATransactionTimeout());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 51);
                        } else if (var5.equals("ConnLeakProfilingEnabled")) {
                           var3.setConnLeakProfilingEnabled(var4.isConnLeakProfilingEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                        } else if (var5.equals("ConnProfilingEnabled")) {
                           var3.setConnProfilingEnabled(var4.isConnProfilingEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                        } else if (var5.equals("CredentialMappingEnabled")) {
                           var3.setCredentialMappingEnabled(var4.isCredentialMappingEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 68);
                        } else if (var5.equals("IgnoreInUseConnectionsEnabled")) {
                           var3.setIgnoreInUseConnectionsEnabled(var4.isIgnoreInUseConnectionsEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 67);
                        } else if (var5.equals("PrepStmtCacheProfilingEnabled")) {
                           var3.setPrepStmtCacheProfilingEnabled(var4.isPrepStmtCacheProfilingEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                        } else if (var5.equals("RemoveInfectedConnectionsEnabled")) {
                           var3.setRemoveInfectedConnectionsEnabled(var4.isRemoveInfectedConnectionsEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 63);
                        } else if (var5.equals("ShrinkingEnabled")) {
                           var3.setShrinkingEnabled(var4.isShrinkingEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 26);
                        } else if (var5.equals("SqlStmtParamLoggingEnabled")) {
                           var3.setSqlStmtParamLoggingEnabled(var4.isSqlStmtParamLoggingEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                        } else if (var5.equals("SqlStmtProfilingEnabled")) {
                           var3.setSqlStmtProfilingEnabled(var4.isSqlStmtProfilingEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                        } else {
                           super.applyPropertyUpdate(var1, var2);
                        }
                     }
                  }
               }

            }
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected AbstractDescriptorBean finishCopy(AbstractDescriptorBean var1, boolean var2, List var3) {
         try {
            JDBCConnectionPoolMBeanImpl var5 = (JDBCConnectionPoolMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if (var2 && (var3 == null || !var3.contains("ACLName")) && this.bean.isACLNameSet()) {
               var5.setACLName(this.bean.getACLName());
            }

            if (var2 && (var3 == null || !var3.contains("CapacityIncrement")) && this.bean.isCapacityIncrementSet()) {
               var5.setCapacityIncrement(this.bean.getCapacityIncrement());
            }

            if (var2 && (var3 == null || !var3.contains("ConnectionCreationRetryFrequencySeconds")) && this.bean.isConnectionCreationRetryFrequencySecondsSet()) {
               var5.setConnectionCreationRetryFrequencySeconds(this.bean.getConnectionCreationRetryFrequencySeconds());
            }

            if (var2 && (var3 == null || !var3.contains("ConnectionReserveTimeoutSeconds")) && this.bean.isConnectionReserveTimeoutSecondsSet()) {
               var5.setConnectionReserveTimeoutSeconds(this.bean.getConnectionReserveTimeoutSeconds());
            }

            if (var2 && (var3 == null || !var3.contains("CountOfRefreshFailuresTillDisable")) && this.bean.isCountOfRefreshFailuresTillDisableSet()) {
               var5.setCountOfRefreshFailuresTillDisable(this.bean.getCountOfRefreshFailuresTillDisable());
            }

            if (var2 && (var3 == null || !var3.contains("CountOfTestFailuresTillFlush")) && this.bean.isCountOfTestFailuresTillFlushSet()) {
               var5.setCountOfTestFailuresTillFlush(this.bean.getCountOfTestFailuresTillFlush());
            }

            if (var2 && (var3 == null || !var3.contains("DriverName")) && this.bean.isDriverNameSet()) {
               var5.setDriverName(this.bean.getDriverName());
            }

            if (var2 && (var3 == null || !var3.contains("EnableResourceHealthMonitoring")) && this.bean.isEnableResourceHealthMonitoringSet()) {
               var5.setEnableResourceHealthMonitoring(this.bean.getEnableResourceHealthMonitoring());
            }

            if (var2 && (var3 == null || !var3.contains("HighestNumUnavailable")) && this.bean.isHighestNumUnavailableSet()) {
               var5.setHighestNumUnavailable(this.bean.getHighestNumUnavailable());
            }

            if (var2 && (var3 == null || !var3.contains("HighestNumWaiters")) && this.bean.isHighestNumWaitersSet()) {
               var5.setHighestNumWaiters(this.bean.getHighestNumWaiters());
            }

            if (var2 && (var3 == null || !var3.contains("InactiveConnectionTimeoutSeconds")) && this.bean.isInactiveConnectionTimeoutSecondsSet()) {
               var5.setInactiveConnectionTimeoutSeconds(this.bean.getInactiveConnectionTimeoutSeconds());
            }

            if (var2 && (var3 == null || !var3.contains("InitSQL")) && this.bean.isInitSQLSet()) {
               var5.setInitSQL(this.bean.getInitSQL());
            }

            if (var2 && (var3 == null || !var3.contains("InitialCapacity")) && this.bean.isInitialCapacitySet()) {
               var5.setInitialCapacity(this.bean.getInitialCapacity());
            }

            if (var2 && (var3 == null || !var3.contains("JDBCSystemResource")) && this.bean.isJDBCSystemResourceSet()) {
               var5._unSet(var5, 9);
               var5.setJDBCSystemResourceAsString(this.bean.getJDBCSystemResourceAsString());
            }

            if (var2 && (var3 == null || !var3.contains("JDBCXADebugLevel")) && this.bean.isJDBCXADebugLevelSet()) {
               var5.setJDBCXADebugLevel(this.bean.getJDBCXADebugLevel());
            }

            if (var2 && (var3 == null || !var3.contains("KeepLogicalConnOpenOnRelease")) && this.bean.isKeepLogicalConnOpenOnReleaseSet()) {
               var5.setKeepLogicalConnOpenOnRelease(this.bean.getKeepLogicalConnOpenOnRelease());
            }

            if (var2 && (var3 == null || !var3.contains("KeepXAConnTillTxComplete")) && this.bean.isKeepXAConnTillTxCompleteSet()) {
               var5.setKeepXAConnTillTxComplete(this.bean.getKeepXAConnTillTxComplete());
            }

            if (var2 && (var3 == null || !var3.contains("LoginDelaySeconds")) && this.bean.isLoginDelaySecondsSet()) {
               var5.setLoginDelaySeconds(this.bean.getLoginDelaySeconds());
            }

            if (var2 && (var3 == null || !var3.contains("MaxCapacity")) && this.bean.isMaxCapacitySet()) {
               var5.setMaxCapacity(this.bean.getMaxCapacity());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if (var2 && (var3 == null || !var3.contains("NeedTxCtxOnClose")) && this.bean.isNeedTxCtxOnCloseSet()) {
               var5.setNeedTxCtxOnClose(this.bean.getNeedTxCtxOnClose());
            }

            if (var2 && (var3 == null || !var3.contains("NewXAConnForCommit")) && this.bean.isNewXAConnForCommitSet()) {
               var5.setNewXAConnForCommit(this.bean.getNewXAConnForCommit());
            }

            byte[] var4;
            if (var2 && (var3 == null || !var3.contains("PasswordEncrypted")) && this.bean.isPasswordEncryptedSet()) {
               var4 = this.bean.getPasswordEncrypted();
               var5.setPasswordEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if (var2 && (var3 == null || !var3.contains("PrepStmtCacheProfilingThreshold")) && this.bean.isPrepStmtCacheProfilingThresholdSet()) {
               var5.setPrepStmtCacheProfilingThreshold(this.bean.getPrepStmtCacheProfilingThreshold());
            }

            if (var2 && (var3 == null || !var3.contains("PreparedStatementCacheSize")) && this.bean.isPreparedStatementCacheSizeSet()) {
               var5.setPreparedStatementCacheSize(this.bean.getPreparedStatementCacheSize());
            }

            if (var2 && (var3 == null || !var3.contains("Properties")) && this.bean.isPropertiesSet()) {
               var5.setProperties(this.bean.getProperties());
            }

            if (var2 && (var3 == null || !var3.contains("RecoverOnlyOnce")) && this.bean.isRecoverOnlyOnceSet()) {
               var5.setRecoverOnlyOnce(this.bean.getRecoverOnlyOnce());
            }

            if (var2 && (var3 == null || !var3.contains("RefreshMinutes")) && this.bean.isRefreshMinutesSet()) {
               var5.setRefreshMinutes(this.bean.getRefreshMinutes());
            }

            if (var2 && (var3 == null || !var3.contains("RollbackLocalTxUponConnClose")) && this.bean.isRollbackLocalTxUponConnCloseSet()) {
               var5.setRollbackLocalTxUponConnClose(this.bean.getRollbackLocalTxUponConnClose());
            }

            if (var2 && (var3 == null || !var3.contains("SecondsToTrustAnIdlePoolConnection")) && this.bean.isSecondsToTrustAnIdlePoolConnectionSet()) {
               var5.setSecondsToTrustAnIdlePoolConnection(this.bean.getSecondsToTrustAnIdlePoolConnection());
            }

            if (var2 && (var3 == null || !var3.contains("ShrinkFrequencySeconds")) && this.bean.isShrinkFrequencySecondsSet()) {
               var5.setShrinkFrequencySeconds(this.bean.getShrinkFrequencySeconds());
            }

            if (var2 && (var3 == null || !var3.contains("ShrinkPeriodMinutes")) && this.bean.isShrinkPeriodMinutesSet()) {
               var5.setShrinkPeriodMinutes(this.bean.getShrinkPeriodMinutes());
            }

            if (var2 && (var3 == null || !var3.contains("SqlStmtMaxParamLength")) && this.bean.isSqlStmtMaxParamLengthSet()) {
               var5.setSqlStmtMaxParamLength(this.bean.getSqlStmtMaxParamLength());
            }

            if (var2 && (var3 == null || !var3.contains("StatementCacheSize")) && this.bean.isStatementCacheSizeSet()) {
               var5.setStatementCacheSize(this.bean.getStatementCacheSize());
            }

            if (var2 && (var3 == null || !var3.contains("StatementCacheType")) && this.bean.isStatementCacheTypeSet()) {
               var5.setStatementCacheType(this.bean.getStatementCacheType());
            }

            if (var2 && (var3 == null || !var3.contains("StatementTimeout")) && this.bean.isStatementTimeoutSet()) {
               var5.setStatementTimeout(this.bean.getStatementTimeout());
            }

            if (var2 && (var3 == null || !var3.contains("SupportsLocalTransaction")) && this.bean.isSupportsLocalTransactionSet()) {
               var5.setSupportsLocalTransaction(this.bean.getSupportsLocalTransaction());
            }

            if ((var3 == null || !var3.contains("Targets")) && this.bean.isTargetsSet()) {
               var5._unSet(var5, 7);
               var5.setTargetsAsString(this.bean.getTargetsAsString());
            }

            if (var2 && (var3 == null || !var3.contains("TestConnectionsOnCreate")) && this.bean.isTestConnectionsOnCreateSet()) {
               var5.setTestConnectionsOnCreate(this.bean.getTestConnectionsOnCreate());
            }

            if (var2 && (var3 == null || !var3.contains("TestConnectionsOnRelease")) && this.bean.isTestConnectionsOnReleaseSet()) {
               var5.setTestConnectionsOnRelease(this.bean.getTestConnectionsOnRelease());
            }

            if (var2 && (var3 == null || !var3.contains("TestConnectionsOnReserve")) && this.bean.isTestConnectionsOnReserveSet()) {
               var5.setTestConnectionsOnReserve(this.bean.getTestConnectionsOnReserve());
            }

            if (var2 && (var3 == null || !var3.contains("TestFrequencySeconds")) && this.bean.isTestFrequencySecondsSet()) {
               var5.setTestFrequencySeconds(this.bean.getTestFrequencySeconds());
            }

            if (var2 && (var3 == null || !var3.contains("TestStatementTimeout")) && this.bean.isTestStatementTimeoutSet()) {
               var5.setTestStatementTimeout(this.bean.getTestStatementTimeout());
            }

            if (var2 && (var3 == null || !var3.contains("TestTableName")) && this.bean.isTestTableNameSet()) {
               var5.setTestTableName(this.bean.getTestTableName());
            }

            if (var2 && (var3 == null || !var3.contains("URL")) && this.bean.isURLSet()) {
               var5.setURL(this.bean.getURL());
            }

            if (var2 && (var3 == null || !var3.contains("XAEndOnlyOnce")) && this.bean.isXAEndOnlyOnceSet()) {
               var5.setXAEndOnlyOnce(this.bean.getXAEndOnlyOnce());
            }

            if (var2 && (var3 == null || !var3.contains("XAPasswordEncrypted")) && this.bean.isXAPasswordEncryptedSet()) {
               var4 = this.bean.getXAPasswordEncrypted();
               var5.setXAPasswordEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if (var2 && (var3 == null || !var3.contains("XAPreparedStatementCacheSize")) && this.bean.isXAPreparedStatementCacheSizeSet()) {
               var5.setXAPreparedStatementCacheSize(this.bean.getXAPreparedStatementCacheSize());
            }

            if (var2 && (var3 == null || !var3.contains("XARetryDurationSeconds")) && this.bean.isXARetryDurationSecondsSet()) {
               var5.setXARetryDurationSeconds(this.bean.getXARetryDurationSeconds());
            }

            if (var2 && (var3 == null || !var3.contains("XARetryIntervalSeconds")) && this.bean.isXARetryIntervalSecondsSet()) {
               var5.setXARetryIntervalSeconds(this.bean.getXARetryIntervalSeconds());
            }

            if (var2 && (var3 == null || !var3.contains("XASetTransactionTimeout")) && this.bean.isXASetTransactionTimeoutSet()) {
               var5.setXASetTransactionTimeout(this.bean.getXASetTransactionTimeout());
            }

            if (var2 && (var3 == null || !var3.contains("XATransactionTimeout")) && this.bean.isXATransactionTimeoutSet()) {
               var5.setXATransactionTimeout(this.bean.getXATransactionTimeout());
            }

            if (var2 && (var3 == null || !var3.contains("ConnLeakProfilingEnabled")) && this.bean.isConnLeakProfilingEnabledSet()) {
               var5.setConnLeakProfilingEnabled(this.bean.isConnLeakProfilingEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("ConnProfilingEnabled")) && this.bean.isConnProfilingEnabledSet()) {
               var5.setConnProfilingEnabled(this.bean.isConnProfilingEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("CredentialMappingEnabled")) && this.bean.isCredentialMappingEnabledSet()) {
               var5.setCredentialMappingEnabled(this.bean.isCredentialMappingEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("IgnoreInUseConnectionsEnabled")) && this.bean.isIgnoreInUseConnectionsEnabledSet()) {
               var5.setIgnoreInUseConnectionsEnabled(this.bean.isIgnoreInUseConnectionsEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("PrepStmtCacheProfilingEnabled")) && this.bean.isPrepStmtCacheProfilingEnabledSet()) {
               var5.setPrepStmtCacheProfilingEnabled(this.bean.isPrepStmtCacheProfilingEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("RemoveInfectedConnectionsEnabled")) && this.bean.isRemoveInfectedConnectionsEnabledSet()) {
               var5.setRemoveInfectedConnectionsEnabled(this.bean.isRemoveInfectedConnectionsEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("ShrinkingEnabled")) && this.bean.isShrinkingEnabledSet()) {
               var5.setShrinkingEnabled(this.bean.isShrinkingEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("SqlStmtParamLoggingEnabled")) && this.bean.isSqlStmtParamLoggingEnabledSet()) {
               var5.setSqlStmtParamLoggingEnabled(this.bean.isSqlStmtParamLoggingEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("SqlStmtProfilingEnabled")) && this.bean.isSqlStmtProfilingEnabledSet()) {
               var5.setSqlStmtProfilingEnabled(this.bean.isSqlStmtProfilingEnabled());
            }

            return var5;
         } catch (RuntimeException var6) {
            throw var6;
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
         this.inferSubTree(this.bean.getJDBCSystemResource(), var1, var2);
         this.inferSubTree(this.bean.getTargets(), var1, var2);
      }
   }
}
