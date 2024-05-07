package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.utils.collections.CombinedIterator;

public class JTAMBeanImpl extends ConfigurationMBeanImpl implements JTAMBean, Serializable {
   private int _AbandonTimeoutSeconds;
   private int _BeforeCompletionIterationLimit;
   private int _CheckpointIntervalSeconds;
   private int _CompletionTimeoutSeconds;
   private boolean _ForgetHeuristics;
   private int _MaxResourceRequestsOnServer;
   private long _MaxResourceUnavailableMillis;
   private int _MaxTransactions;
   private long _MaxTransactionsHealthIntervalMillis;
   private int _MaxUniqueNameStatistics;
   private long _MaxXACallMillis;
   private int _MigrationCheckpointIntervalSeconds;
   private String _ParallelXADispatchPolicy;
   private boolean _ParallelXAEnabled;
   private int _PurgeResourceFromCheckpointIntervalSeconds;
   private long _RecoveryThresholdMillis;
   private String _SecurityInteropMode;
   private long _SerializeEnlistmentsGCIntervalMillis;
   private int _TimeoutSeconds;
   private boolean _TwoPhaseEnabled;
   private int _UnregisterResourceGracePeriod;
   private boolean _WSATIssuedTokenEnabled;
   private String _WSATTransportSecurityMode;
   private static SchemaHelper2 _schemaHelper;

   public JTAMBeanImpl() {
      this._initializeProperty(-1);
   }

   public JTAMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public int getTimeoutSeconds() {
      return this._TimeoutSeconds;
   }

   public boolean isTimeoutSecondsSet() {
      return this._isSet(7);
   }

   public void setTimeoutSeconds(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("TimeoutSeconds", (long)var1, 1L, 2147483647L);
      int var2 = this._TimeoutSeconds;
      this._TimeoutSeconds = var1;
      this._postSet(7, var2, var1);
   }

   public int getAbandonTimeoutSeconds() {
      return this._AbandonTimeoutSeconds;
   }

   public boolean isAbandonTimeoutSecondsSet() {
      return this._isSet(8);
   }

   public void setAbandonTimeoutSeconds(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("AbandonTimeoutSeconds", (long)var1, 1L, 2147483647L);
      int var2 = this._AbandonTimeoutSeconds;
      this._AbandonTimeoutSeconds = var1;
      this._postSet(8, var2, var1);
   }

   public int getCompletionTimeoutSeconds() {
      return this._CompletionTimeoutSeconds;
   }

   public boolean isCompletionTimeoutSecondsSet() {
      return this._isSet(9);
   }

   public void setCompletionTimeoutSeconds(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CompletionTimeoutSeconds", (long)var1, -1L, 2147483647L);
      int var2 = this._CompletionTimeoutSeconds;
      this._CompletionTimeoutSeconds = var1;
      this._postSet(9, var2, var1);
   }

   public boolean getForgetHeuristics() {
      return this._ForgetHeuristics;
   }

   public boolean isForgetHeuristicsSet() {
      return this._isSet(10);
   }

   public void setForgetHeuristics(boolean var1) throws InvalidAttributeValueException, DistributedManagementException {
      boolean var2 = this._ForgetHeuristics;
      this._ForgetHeuristics = var1;
      this._postSet(10, var2, var1);
   }

   public int getBeforeCompletionIterationLimit() {
      return this._BeforeCompletionIterationLimit;
   }

   public boolean isBeforeCompletionIterationLimitSet() {
      return this._isSet(11);
   }

   public void setBeforeCompletionIterationLimit(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("BeforeCompletionIterationLimit", (long)var1, 1L, 2147483647L);
      int var2 = this._BeforeCompletionIterationLimit;
      this._BeforeCompletionIterationLimit = var1;
      this._postSet(11, var2, var1);
   }

   public int getMaxTransactions() {
      return this._MaxTransactions;
   }

   public boolean isMaxTransactionsSet() {
      return this._isSet(12);
   }

   public void setMaxTransactions(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxTransactions", (long)var1, 1L, 2147483647L);
      int var2 = this._MaxTransactions;
      this._MaxTransactions = var1;
      this._postSet(12, var2, var1);
   }

   public int getMaxUniqueNameStatistics() {
      return this._MaxUniqueNameStatistics;
   }

   public boolean isMaxUniqueNameStatisticsSet() {
      return this._isSet(13);
   }

   public void setMaxUniqueNameStatistics(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxUniqueNameStatistics", (long)var1, 0L, 2147483647L);
      int var2 = this._MaxUniqueNameStatistics;
      this._MaxUniqueNameStatistics = var1;
      this._postSet(13, var2, var1);
   }

   public int getMaxResourceRequestsOnServer() {
      return this._MaxResourceRequestsOnServer;
   }

   public boolean isMaxResourceRequestsOnServerSet() {
      return this._isSet(14);
   }

   public void setMaxResourceRequestsOnServer(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxResourceRequestsOnServer", (long)var1, 10L, 2147483647L);
      int var2 = this._MaxResourceRequestsOnServer;
      this._MaxResourceRequestsOnServer = var1;
      this._postSet(14, var2, var1);
   }

   public long getMaxXACallMillis() {
      return this._MaxXACallMillis;
   }

   public boolean isMaxXACallMillisSet() {
      return this._isSet(15);
   }

   public void setMaxXACallMillis(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxXACallMillis", var1, 0L, Long.MAX_VALUE);
      long var3 = this._MaxXACallMillis;
      this._MaxXACallMillis = var1;
      this._postSet(15, var3, var1);
   }

   public long getMaxResourceUnavailableMillis() {
      return this._MaxResourceUnavailableMillis;
   }

   public boolean isMaxResourceUnavailableMillisSet() {
      return this._isSet(16);
   }

   public void setMaxResourceUnavailableMillis(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxResourceUnavailableMillis", var1, 0L, Long.MAX_VALUE);
      long var3 = this._MaxResourceUnavailableMillis;
      this._MaxResourceUnavailableMillis = var1;
      this._postSet(16, var3, var1);
   }

   public long getRecoveryThresholdMillis() {
      return this._RecoveryThresholdMillis;
   }

   public boolean isRecoveryThresholdMillisSet() {
      return this._isSet(17);
   }

   public void setRecoveryThresholdMillis(long var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RecoveryThresholdMillis", var1, 60000L, Long.MAX_VALUE);
      long var3 = this._RecoveryThresholdMillis;
      this._RecoveryThresholdMillis = var1;
      this._postSet(17, var3, var1);
   }

   public int getMigrationCheckpointIntervalSeconds() {
      return this._MigrationCheckpointIntervalSeconds;
   }

   public boolean isMigrationCheckpointIntervalSecondsSet() {
      return this._isSet(18);
   }

   public void setMigrationCheckpointIntervalSeconds(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MigrationCheckpointIntervalSeconds", (long)var1, 1L, 2147483647L);
      int var2 = this._MigrationCheckpointIntervalSeconds;
      this._MigrationCheckpointIntervalSeconds = var1;
      this._postSet(18, var2, var1);
   }

   public long getMaxTransactionsHealthIntervalMillis() {
      return this._MaxTransactionsHealthIntervalMillis;
   }

   public boolean isMaxTransactionsHealthIntervalMillisSet() {
      return this._isSet(19);
   }

   public void setMaxTransactionsHealthIntervalMillis(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxTransactionsHealthIntervalMillis", var1, 1000L, Long.MAX_VALUE);
      long var3 = this._MaxTransactionsHealthIntervalMillis;
      this._MaxTransactionsHealthIntervalMillis = var1;
      this._postSet(19, var3, var1);
   }

   public int getPurgeResourceFromCheckpointIntervalSeconds() {
      return this._PurgeResourceFromCheckpointIntervalSeconds;
   }

   public boolean isPurgeResourceFromCheckpointIntervalSecondsSet() {
      return this._isSet(20);
   }

   public void setPurgeResourceFromCheckpointIntervalSeconds(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("PurgeResourceFromCheckpointIntervalSeconds", (long)var1, 0L, 2147483647L);
      int var2 = this._PurgeResourceFromCheckpointIntervalSeconds;
      this._PurgeResourceFromCheckpointIntervalSeconds = var1;
      this._postSet(20, var2, var1);
   }

   public int getCheckpointIntervalSeconds() {
      return this._CheckpointIntervalSeconds;
   }

   public boolean isCheckpointIntervalSecondsSet() {
      return this._isSet(21);
   }

   public void setCheckpointIntervalSeconds(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CheckpointIntervalSeconds", (long)var1, 10L, 1800L);
      int var2 = this._CheckpointIntervalSeconds;
      this._CheckpointIntervalSeconds = var1;
      this._postSet(21, var2, var1);
   }

   public long getSerializeEnlistmentsGCIntervalMillis() {
      return this._SerializeEnlistmentsGCIntervalMillis;
   }

   public boolean isSerializeEnlistmentsGCIntervalMillisSet() {
      return this._isSet(22);
   }

   public void setSerializeEnlistmentsGCIntervalMillis(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("SerializeEnlistmentsGCIntervalMillis", var1, 0L);
      long var3 = this._SerializeEnlistmentsGCIntervalMillis;
      this._SerializeEnlistmentsGCIntervalMillis = var1;
      this._postSet(22, var3, var1);
   }

   public boolean getParallelXAEnabled() {
      return this._ParallelXAEnabled;
   }

   public boolean isParallelXAEnabledSet() {
      return this._isSet(23);
   }

   public void setParallelXAEnabled(boolean var1) throws InvalidAttributeValueException, DistributedManagementException {
      boolean var2 = this._ParallelXAEnabled;
      this._ParallelXAEnabled = var1;
      this._postSet(23, var2, var1);
   }

   public String getParallelXADispatchPolicy() {
      return this._ParallelXADispatchPolicy;
   }

   public boolean isParallelXADispatchPolicySet() {
      return this._isSet(24);
   }

   public void setParallelXADispatchPolicy(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ParallelXADispatchPolicy;
      this._ParallelXADispatchPolicy = var1;
      this._postSet(24, var2, var1);
   }

   public int getUnregisterResourceGracePeriod() {
      return this._UnregisterResourceGracePeriod;
   }

   public boolean isUnregisterResourceGracePeriodSet() {
      return this._isSet(25);
   }

   public void setUnregisterResourceGracePeriod(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("UnregisterResourceGracePeriod", (long)var1, 0L, 2147483647L);
      int var2 = this._UnregisterResourceGracePeriod;
      this._UnregisterResourceGracePeriod = var1;
      this._postSet(25, var2, var1);
   }

   public String getSecurityInteropMode() {
      return this._SecurityInteropMode;
   }

   public boolean isSecurityInteropModeSet() {
      return this._isSet(26);
   }

   public void setSecurityInteropMode(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"default", "performance", "compatibility"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("SecurityInteropMode", var1, var2);
      String var3 = this._SecurityInteropMode;
      this._SecurityInteropMode = var1;
      this._postSet(26, var3, var1);
   }

   public String getWSATTransportSecurityMode() {
      return this._WSATTransportSecurityMode;
   }

   public boolean isWSATTransportSecurityModeSet() {
      return this._isSet(27);
   }

   public void setWSATTransportSecurityMode(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"SSLNotRequired", "SSLRequired", "ClientCertRequired"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("WSATTransportSecurityMode", var1, var2);
      String var3 = this._WSATTransportSecurityMode;
      this._WSATTransportSecurityMode = var1;
      this._postSet(27, var3, var1);
   }

   public boolean isWSATIssuedTokenEnabled() {
      return this._WSATIssuedTokenEnabled;
   }

   public boolean isWSATIssuedTokenEnabledSet() {
      return this._isSet(28);
   }

   public void setWSATIssuedTokenEnabled(boolean var1) throws InvalidAttributeValueException, DistributedManagementException {
      boolean var2 = this._WSATIssuedTokenEnabled;
      this._WSATIssuedTokenEnabled = var1;
      this._postSet(28, var2, var1);
   }

   public boolean isTwoPhaseEnabled() {
      return this._TwoPhaseEnabled;
   }

   public boolean isTwoPhaseEnabledSet() {
      return this._isSet(29);
   }

   public void setTwoPhaseEnabled(boolean var1) throws InvalidAttributeValueException, DistributedManagementException {
      boolean var2 = this._TwoPhaseEnabled;
      this._TwoPhaseEnabled = var1;
      this._postSet(29, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
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
         var1 = 8;
      }

      try {
         switch (var1) {
            case 8:
               this._AbandonTimeoutSeconds = 86400;
               if (var2) {
                  break;
               }
            case 11:
               this._BeforeCompletionIterationLimit = 10;
               if (var2) {
                  break;
               }
            case 21:
               this._CheckpointIntervalSeconds = 300;
               if (var2) {
                  break;
               }
            case 9:
               this._CompletionTimeoutSeconds = 0;
               if (var2) {
                  break;
               }
            case 10:
               this._ForgetHeuristics = true;
               if (var2) {
                  break;
               }
            case 14:
               this._MaxResourceRequestsOnServer = 50;
               if (var2) {
                  break;
               }
            case 16:
               this._MaxResourceUnavailableMillis = 1800000L;
               if (var2) {
                  break;
               }
            case 12:
               this._MaxTransactions = 10000;
               if (var2) {
                  break;
               }
            case 19:
               this._MaxTransactionsHealthIntervalMillis = 60000L;
               if (var2) {
                  break;
               }
            case 13:
               this._MaxUniqueNameStatistics = 1000;
               if (var2) {
                  break;
               }
            case 15:
               this._MaxXACallMillis = 120000L;
               if (var2) {
                  break;
               }
            case 18:
               this._MigrationCheckpointIntervalSeconds = 60;
               if (var2) {
                  break;
               }
            case 24:
               this._ParallelXADispatchPolicy = null;
               if (var2) {
                  break;
               }
            case 23:
               this._ParallelXAEnabled = true;
               if (var2) {
                  break;
               }
            case 20:
               this._PurgeResourceFromCheckpointIntervalSeconds = 86400;
               if (var2) {
                  break;
               }
            case 17:
               this._RecoveryThresholdMillis = 300000L;
               if (var2) {
                  break;
               }
            case 26:
               this._SecurityInteropMode = "default";
               if (var2) {
                  break;
               }
            case 22:
               this._SerializeEnlistmentsGCIntervalMillis = 30000L;
               if (var2) {
                  break;
               }
            case 7:
               this._TimeoutSeconds = 30;
               if (var2) {
                  break;
               }
            case 25:
               this._UnregisterResourceGracePeriod = 30;
               if (var2) {
                  break;
               }
            case 27:
               this._WSATTransportSecurityMode = "SSLNotRequired";
               if (var2) {
                  break;
               }
            case 29:
               this._TwoPhaseEnabled = true;
               if (var2) {
                  break;
               }
            case 28:
               this._WSATIssuedTokenEnabled = false;
               if (var2) {
                  break;
               }
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
      return "JTA";
   }

   public void putValue(String var1, Object var2) {
      int var6;
      if (var1.equals("AbandonTimeoutSeconds")) {
         var6 = this._AbandonTimeoutSeconds;
         this._AbandonTimeoutSeconds = (Integer)var2;
         this._postSet(8, var6, this._AbandonTimeoutSeconds);
      } else if (var1.equals("BeforeCompletionIterationLimit")) {
         var6 = this._BeforeCompletionIterationLimit;
         this._BeforeCompletionIterationLimit = (Integer)var2;
         this._postSet(11, var6, this._BeforeCompletionIterationLimit);
      } else if (var1.equals("CheckpointIntervalSeconds")) {
         var6 = this._CheckpointIntervalSeconds;
         this._CheckpointIntervalSeconds = (Integer)var2;
         this._postSet(21, var6, this._CheckpointIntervalSeconds);
      } else if (var1.equals("CompletionTimeoutSeconds")) {
         var6 = this._CompletionTimeoutSeconds;
         this._CompletionTimeoutSeconds = (Integer)var2;
         this._postSet(9, var6, this._CompletionTimeoutSeconds);
      } else {
         boolean var5;
         if (var1.equals("ForgetHeuristics")) {
            var5 = this._ForgetHeuristics;
            this._ForgetHeuristics = (Boolean)var2;
            this._postSet(10, var5, this._ForgetHeuristics);
         } else if (var1.equals("MaxResourceRequestsOnServer")) {
            var6 = this._MaxResourceRequestsOnServer;
            this._MaxResourceRequestsOnServer = (Integer)var2;
            this._postSet(14, var6, this._MaxResourceRequestsOnServer);
         } else {
            long var7;
            if (var1.equals("MaxResourceUnavailableMillis")) {
               var7 = this._MaxResourceUnavailableMillis;
               this._MaxResourceUnavailableMillis = (Long)var2;
               this._postSet(16, var7, this._MaxResourceUnavailableMillis);
            } else if (var1.equals("MaxTransactions")) {
               var6 = this._MaxTransactions;
               this._MaxTransactions = (Integer)var2;
               this._postSet(12, var6, this._MaxTransactions);
            } else if (var1.equals("MaxTransactionsHealthIntervalMillis")) {
               var7 = this._MaxTransactionsHealthIntervalMillis;
               this._MaxTransactionsHealthIntervalMillis = (Long)var2;
               this._postSet(19, var7, this._MaxTransactionsHealthIntervalMillis);
            } else if (var1.equals("MaxUniqueNameStatistics")) {
               var6 = this._MaxUniqueNameStatistics;
               this._MaxUniqueNameStatistics = (Integer)var2;
               this._postSet(13, var6, this._MaxUniqueNameStatistics);
            } else if (var1.equals("MaxXACallMillis")) {
               var7 = this._MaxXACallMillis;
               this._MaxXACallMillis = (Long)var2;
               this._postSet(15, var7, this._MaxXACallMillis);
            } else if (var1.equals("MigrationCheckpointIntervalSeconds")) {
               var6 = this._MigrationCheckpointIntervalSeconds;
               this._MigrationCheckpointIntervalSeconds = (Integer)var2;
               this._postSet(18, var6, this._MigrationCheckpointIntervalSeconds);
            } else {
               String var3;
               if (var1.equals("ParallelXADispatchPolicy")) {
                  var3 = this._ParallelXADispatchPolicy;
                  this._ParallelXADispatchPolicy = (String)var2;
                  this._postSet(24, var3, this._ParallelXADispatchPolicy);
               } else if (var1.equals("ParallelXAEnabled")) {
                  var5 = this._ParallelXAEnabled;
                  this._ParallelXAEnabled = (Boolean)var2;
                  this._postSet(23, var5, this._ParallelXAEnabled);
               } else if (var1.equals("PurgeResourceFromCheckpointIntervalSeconds")) {
                  var6 = this._PurgeResourceFromCheckpointIntervalSeconds;
                  this._PurgeResourceFromCheckpointIntervalSeconds = (Integer)var2;
                  this._postSet(20, var6, this._PurgeResourceFromCheckpointIntervalSeconds);
               } else if (var1.equals("RecoveryThresholdMillis")) {
                  var7 = this._RecoveryThresholdMillis;
                  this._RecoveryThresholdMillis = (Long)var2;
                  this._postSet(17, var7, this._RecoveryThresholdMillis);
               } else if (var1.equals("SecurityInteropMode")) {
                  var3 = this._SecurityInteropMode;
                  this._SecurityInteropMode = (String)var2;
                  this._postSet(26, var3, this._SecurityInteropMode);
               } else if (var1.equals("SerializeEnlistmentsGCIntervalMillis")) {
                  var7 = this._SerializeEnlistmentsGCIntervalMillis;
                  this._SerializeEnlistmentsGCIntervalMillis = (Long)var2;
                  this._postSet(22, var7, this._SerializeEnlistmentsGCIntervalMillis);
               } else if (var1.equals("TimeoutSeconds")) {
                  var6 = this._TimeoutSeconds;
                  this._TimeoutSeconds = (Integer)var2;
                  this._postSet(7, var6, this._TimeoutSeconds);
               } else if (var1.equals("TwoPhaseEnabled")) {
                  var5 = this._TwoPhaseEnabled;
                  this._TwoPhaseEnabled = (Boolean)var2;
                  this._postSet(29, var5, this._TwoPhaseEnabled);
               } else if (var1.equals("UnregisterResourceGracePeriod")) {
                  var6 = this._UnregisterResourceGracePeriod;
                  this._UnregisterResourceGracePeriod = (Integer)var2;
                  this._postSet(25, var6, this._UnregisterResourceGracePeriod);
               } else if (var1.equals("WSATIssuedTokenEnabled")) {
                  var5 = this._WSATIssuedTokenEnabled;
                  this._WSATIssuedTokenEnabled = (Boolean)var2;
                  this._postSet(28, var5, this._WSATIssuedTokenEnabled);
               } else if (var1.equals("WSATTransportSecurityMode")) {
                  var3 = this._WSATTransportSecurityMode;
                  this._WSATTransportSecurityMode = (String)var2;
                  this._postSet(27, var3, this._WSATTransportSecurityMode);
               } else {
                  super.putValue(var1, var2);
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AbandonTimeoutSeconds")) {
         return new Integer(this._AbandonTimeoutSeconds);
      } else if (var1.equals("BeforeCompletionIterationLimit")) {
         return new Integer(this._BeforeCompletionIterationLimit);
      } else if (var1.equals("CheckpointIntervalSeconds")) {
         return new Integer(this._CheckpointIntervalSeconds);
      } else if (var1.equals("CompletionTimeoutSeconds")) {
         return new Integer(this._CompletionTimeoutSeconds);
      } else if (var1.equals("ForgetHeuristics")) {
         return new Boolean(this._ForgetHeuristics);
      } else if (var1.equals("MaxResourceRequestsOnServer")) {
         return new Integer(this._MaxResourceRequestsOnServer);
      } else if (var1.equals("MaxResourceUnavailableMillis")) {
         return new Long(this._MaxResourceUnavailableMillis);
      } else if (var1.equals("MaxTransactions")) {
         return new Integer(this._MaxTransactions);
      } else if (var1.equals("MaxTransactionsHealthIntervalMillis")) {
         return new Long(this._MaxTransactionsHealthIntervalMillis);
      } else if (var1.equals("MaxUniqueNameStatistics")) {
         return new Integer(this._MaxUniqueNameStatistics);
      } else if (var1.equals("MaxXACallMillis")) {
         return new Long(this._MaxXACallMillis);
      } else if (var1.equals("MigrationCheckpointIntervalSeconds")) {
         return new Integer(this._MigrationCheckpointIntervalSeconds);
      } else if (var1.equals("ParallelXADispatchPolicy")) {
         return this._ParallelXADispatchPolicy;
      } else if (var1.equals("ParallelXAEnabled")) {
         return new Boolean(this._ParallelXAEnabled);
      } else if (var1.equals("PurgeResourceFromCheckpointIntervalSeconds")) {
         return new Integer(this._PurgeResourceFromCheckpointIntervalSeconds);
      } else if (var1.equals("RecoveryThresholdMillis")) {
         return new Long(this._RecoveryThresholdMillis);
      } else if (var1.equals("SecurityInteropMode")) {
         return this._SecurityInteropMode;
      } else if (var1.equals("SerializeEnlistmentsGCIntervalMillis")) {
         return new Long(this._SerializeEnlistmentsGCIntervalMillis);
      } else if (var1.equals("TimeoutSeconds")) {
         return new Integer(this._TimeoutSeconds);
      } else if (var1.equals("TwoPhaseEnabled")) {
         return new Boolean(this._TwoPhaseEnabled);
      } else if (var1.equals("UnregisterResourceGracePeriod")) {
         return new Integer(this._UnregisterResourceGracePeriod);
      } else if (var1.equals("WSATIssuedTokenEnabled")) {
         return new Boolean(this._WSATIssuedTokenEnabled);
      } else {
         return var1.equals("WSATTransportSecurityMode") ? this._WSATTransportSecurityMode : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 15:
               if (var1.equals("timeout-seconds")) {
                  return 7;
               }
               break;
            case 16:
               if (var1.equals("max-transactions")) {
                  return 12;
               }
               break;
            case 17:
               if (var1.equals("forget-heuristics")) {
                  return 10;
               }

               if (var1.equals("two-phase-enabled")) {
                  return 29;
               }
               break;
            case 18:
               if (var1.equals("max-xa-call-millis")) {
                  return 15;
               }
               break;
            case 19:
               if (var1.equals("parallel-xa-enabled")) {
                  return 23;
               }
            case 20:
            case 22:
            case 24:
            case 29:
            case 30:
            case 34:
            case 35:
            case 36:
            case 38:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            default:
               break;
            case 21:
               if (var1.equals("security-interop-mode")) {
                  return 26;
               }
               break;
            case 23:
               if (var1.equals("abandon-timeout-seconds")) {
                  return 8;
               }
               break;
            case 25:
               if (var1.equals("recovery-threshold-millis")) {
                  return 17;
               }

               if (var1.equals("wsat-issued-token-enabled")) {
                  return 28;
               }
               break;
            case 26:
               if (var1.equals("completion-timeout-seconds")) {
                  return 9;
               }

               if (var1.equals("max-unique-name-statistics")) {
                  return 13;
               }
               break;
            case 27:
               if (var1.equals("checkpoint-interval-seconds")) {
                  return 21;
               }

               if (var1.equals("parallel-xa-dispatch-policy")) {
                  return 24;
               }
               break;
            case 28:
               if (var1.equals("wsat-transport-security-mode")) {
                  return 27;
               }
               break;
            case 31:
               if (var1.equals("max-resource-requests-on-server")) {
                  return 14;
               }

               if (var1.equals("max-resource-unavailable-millis")) {
                  return 16;
               }
               break;
            case 32:
               if (var1.equals("unregister-resource-grace-period")) {
                  return 25;
               }
               break;
            case 33:
               if (var1.equals("before-completion-iteration-limit")) {
                  return 11;
               }
               break;
            case 37:
               if (var1.equals("migration-checkpoint-interval-seconds")) {
                  return 18;
               }
               break;
            case 39:
               if (var1.equals("max-transactions-health-interval-millis")) {
                  return 19;
               }

               if (var1.equals("serialize-enlistmentsgc-interval-millis")) {
                  return 22;
               }
               break;
            case 47:
               if (var1.equals("purge-resource-from-checkpoint-interval-seconds")) {
                  return 20;
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
            case 7:
               return "timeout-seconds";
            case 8:
               return "abandon-timeout-seconds";
            case 9:
               return "completion-timeout-seconds";
            case 10:
               return "forget-heuristics";
            case 11:
               return "before-completion-iteration-limit";
            case 12:
               return "max-transactions";
            case 13:
               return "max-unique-name-statistics";
            case 14:
               return "max-resource-requests-on-server";
            case 15:
               return "max-xa-call-millis";
            case 16:
               return "max-resource-unavailable-millis";
            case 17:
               return "recovery-threshold-millis";
            case 18:
               return "migration-checkpoint-interval-seconds";
            case 19:
               return "max-transactions-health-interval-millis";
            case 20:
               return "purge-resource-from-checkpoint-interval-seconds";
            case 21:
               return "checkpoint-interval-seconds";
            case 22:
               return "serialize-enlistmentsgc-interval-millis";
            case 23:
               return "parallel-xa-enabled";
            case 24:
               return "parallel-xa-dispatch-policy";
            case 25:
               return "unregister-resource-grace-period";
            case 26:
               return "security-interop-mode";
            case 27:
               return "wsat-transport-security-mode";
            case 28:
               return "wsat-issued-token-enabled";
            case 29:
               return "two-phase-enabled";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 26:
               return true;
            case 27:
               return true;
            default:
               return super.isConfigurable(var1);
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

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private JTAMBeanImpl bean;

      protected Helper(JTAMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "TimeoutSeconds";
            case 8:
               return "AbandonTimeoutSeconds";
            case 9:
               return "CompletionTimeoutSeconds";
            case 10:
               return "ForgetHeuristics";
            case 11:
               return "BeforeCompletionIterationLimit";
            case 12:
               return "MaxTransactions";
            case 13:
               return "MaxUniqueNameStatistics";
            case 14:
               return "MaxResourceRequestsOnServer";
            case 15:
               return "MaxXACallMillis";
            case 16:
               return "MaxResourceUnavailableMillis";
            case 17:
               return "RecoveryThresholdMillis";
            case 18:
               return "MigrationCheckpointIntervalSeconds";
            case 19:
               return "MaxTransactionsHealthIntervalMillis";
            case 20:
               return "PurgeResourceFromCheckpointIntervalSeconds";
            case 21:
               return "CheckpointIntervalSeconds";
            case 22:
               return "SerializeEnlistmentsGCIntervalMillis";
            case 23:
               return "ParallelXAEnabled";
            case 24:
               return "ParallelXADispatchPolicy";
            case 25:
               return "UnregisterResourceGracePeriod";
            case 26:
               return "SecurityInteropMode";
            case 27:
               return "WSATTransportSecurityMode";
            case 28:
               return "WSATIssuedTokenEnabled";
            case 29:
               return "TwoPhaseEnabled";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AbandonTimeoutSeconds")) {
            return 8;
         } else if (var1.equals("BeforeCompletionIterationLimit")) {
            return 11;
         } else if (var1.equals("CheckpointIntervalSeconds")) {
            return 21;
         } else if (var1.equals("CompletionTimeoutSeconds")) {
            return 9;
         } else if (var1.equals("ForgetHeuristics")) {
            return 10;
         } else if (var1.equals("MaxResourceRequestsOnServer")) {
            return 14;
         } else if (var1.equals("MaxResourceUnavailableMillis")) {
            return 16;
         } else if (var1.equals("MaxTransactions")) {
            return 12;
         } else if (var1.equals("MaxTransactionsHealthIntervalMillis")) {
            return 19;
         } else if (var1.equals("MaxUniqueNameStatistics")) {
            return 13;
         } else if (var1.equals("MaxXACallMillis")) {
            return 15;
         } else if (var1.equals("MigrationCheckpointIntervalSeconds")) {
            return 18;
         } else if (var1.equals("ParallelXADispatchPolicy")) {
            return 24;
         } else if (var1.equals("ParallelXAEnabled")) {
            return 23;
         } else if (var1.equals("PurgeResourceFromCheckpointIntervalSeconds")) {
            return 20;
         } else if (var1.equals("RecoveryThresholdMillis")) {
            return 17;
         } else if (var1.equals("SecurityInteropMode")) {
            return 26;
         } else if (var1.equals("SerializeEnlistmentsGCIntervalMillis")) {
            return 22;
         } else if (var1.equals("TimeoutSeconds")) {
            return 7;
         } else if (var1.equals("UnregisterResourceGracePeriod")) {
            return 25;
         } else if (var1.equals("WSATTransportSecurityMode")) {
            return 27;
         } else if (var1.equals("TwoPhaseEnabled")) {
            return 29;
         } else {
            return var1.equals("WSATIssuedTokenEnabled") ? 28 : super.getPropertyIndex(var1);
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
            if (this.bean.isAbandonTimeoutSecondsSet()) {
               var2.append("AbandonTimeoutSeconds");
               var2.append(String.valueOf(this.bean.getAbandonTimeoutSeconds()));
            }

            if (this.bean.isBeforeCompletionIterationLimitSet()) {
               var2.append("BeforeCompletionIterationLimit");
               var2.append(String.valueOf(this.bean.getBeforeCompletionIterationLimit()));
            }

            if (this.bean.isCheckpointIntervalSecondsSet()) {
               var2.append("CheckpointIntervalSeconds");
               var2.append(String.valueOf(this.bean.getCheckpointIntervalSeconds()));
            }

            if (this.bean.isCompletionTimeoutSecondsSet()) {
               var2.append("CompletionTimeoutSeconds");
               var2.append(String.valueOf(this.bean.getCompletionTimeoutSeconds()));
            }

            if (this.bean.isForgetHeuristicsSet()) {
               var2.append("ForgetHeuristics");
               var2.append(String.valueOf(this.bean.getForgetHeuristics()));
            }

            if (this.bean.isMaxResourceRequestsOnServerSet()) {
               var2.append("MaxResourceRequestsOnServer");
               var2.append(String.valueOf(this.bean.getMaxResourceRequestsOnServer()));
            }

            if (this.bean.isMaxResourceUnavailableMillisSet()) {
               var2.append("MaxResourceUnavailableMillis");
               var2.append(String.valueOf(this.bean.getMaxResourceUnavailableMillis()));
            }

            if (this.bean.isMaxTransactionsSet()) {
               var2.append("MaxTransactions");
               var2.append(String.valueOf(this.bean.getMaxTransactions()));
            }

            if (this.bean.isMaxTransactionsHealthIntervalMillisSet()) {
               var2.append("MaxTransactionsHealthIntervalMillis");
               var2.append(String.valueOf(this.bean.getMaxTransactionsHealthIntervalMillis()));
            }

            if (this.bean.isMaxUniqueNameStatisticsSet()) {
               var2.append("MaxUniqueNameStatistics");
               var2.append(String.valueOf(this.bean.getMaxUniqueNameStatistics()));
            }

            if (this.bean.isMaxXACallMillisSet()) {
               var2.append("MaxXACallMillis");
               var2.append(String.valueOf(this.bean.getMaxXACallMillis()));
            }

            if (this.bean.isMigrationCheckpointIntervalSecondsSet()) {
               var2.append("MigrationCheckpointIntervalSeconds");
               var2.append(String.valueOf(this.bean.getMigrationCheckpointIntervalSeconds()));
            }

            if (this.bean.isParallelXADispatchPolicySet()) {
               var2.append("ParallelXADispatchPolicy");
               var2.append(String.valueOf(this.bean.getParallelXADispatchPolicy()));
            }

            if (this.bean.isParallelXAEnabledSet()) {
               var2.append("ParallelXAEnabled");
               var2.append(String.valueOf(this.bean.getParallelXAEnabled()));
            }

            if (this.bean.isPurgeResourceFromCheckpointIntervalSecondsSet()) {
               var2.append("PurgeResourceFromCheckpointIntervalSeconds");
               var2.append(String.valueOf(this.bean.getPurgeResourceFromCheckpointIntervalSeconds()));
            }

            if (this.bean.isRecoveryThresholdMillisSet()) {
               var2.append("RecoveryThresholdMillis");
               var2.append(String.valueOf(this.bean.getRecoveryThresholdMillis()));
            }

            if (this.bean.isSecurityInteropModeSet()) {
               var2.append("SecurityInteropMode");
               var2.append(String.valueOf(this.bean.getSecurityInteropMode()));
            }

            if (this.bean.isSerializeEnlistmentsGCIntervalMillisSet()) {
               var2.append("SerializeEnlistmentsGCIntervalMillis");
               var2.append(String.valueOf(this.bean.getSerializeEnlistmentsGCIntervalMillis()));
            }

            if (this.bean.isTimeoutSecondsSet()) {
               var2.append("TimeoutSeconds");
               var2.append(String.valueOf(this.bean.getTimeoutSeconds()));
            }

            if (this.bean.isUnregisterResourceGracePeriodSet()) {
               var2.append("UnregisterResourceGracePeriod");
               var2.append(String.valueOf(this.bean.getUnregisterResourceGracePeriod()));
            }

            if (this.bean.isWSATTransportSecurityModeSet()) {
               var2.append("WSATTransportSecurityMode");
               var2.append(String.valueOf(this.bean.getWSATTransportSecurityMode()));
            }

            if (this.bean.isTwoPhaseEnabledSet()) {
               var2.append("TwoPhaseEnabled");
               var2.append(String.valueOf(this.bean.isTwoPhaseEnabled()));
            }

            if (this.bean.isWSATIssuedTokenEnabledSet()) {
               var2.append("WSATIssuedTokenEnabled");
               var2.append(String.valueOf(this.bean.isWSATIssuedTokenEnabled()));
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
            JTAMBeanImpl var2 = (JTAMBeanImpl)var1;
            this.computeDiff("AbandonTimeoutSeconds", this.bean.getAbandonTimeoutSeconds(), var2.getAbandonTimeoutSeconds(), true);
            this.computeDiff("BeforeCompletionIterationLimit", this.bean.getBeforeCompletionIterationLimit(), var2.getBeforeCompletionIterationLimit(), true);
            this.computeDiff("CheckpointIntervalSeconds", this.bean.getCheckpointIntervalSeconds(), var2.getCheckpointIntervalSeconds(), true);
            this.computeDiff("CompletionTimeoutSeconds", this.bean.getCompletionTimeoutSeconds(), var2.getCompletionTimeoutSeconds(), true);
            this.computeDiff("ForgetHeuristics", this.bean.getForgetHeuristics(), var2.getForgetHeuristics(), true);
            this.computeDiff("MaxResourceRequestsOnServer", this.bean.getMaxResourceRequestsOnServer(), var2.getMaxResourceRequestsOnServer(), true);
            this.computeDiff("MaxResourceUnavailableMillis", this.bean.getMaxResourceUnavailableMillis(), var2.getMaxResourceUnavailableMillis(), true);
            this.computeDiff("MaxTransactions", this.bean.getMaxTransactions(), var2.getMaxTransactions(), true);
            this.computeDiff("MaxTransactionsHealthIntervalMillis", this.bean.getMaxTransactionsHealthIntervalMillis(), var2.getMaxTransactionsHealthIntervalMillis(), true);
            this.computeDiff("MaxUniqueNameStatistics", this.bean.getMaxUniqueNameStatistics(), var2.getMaxUniqueNameStatistics(), true);
            this.computeDiff("MaxXACallMillis", this.bean.getMaxXACallMillis(), var2.getMaxXACallMillis(), true);
            this.computeDiff("MigrationCheckpointIntervalSeconds", this.bean.getMigrationCheckpointIntervalSeconds(), var2.getMigrationCheckpointIntervalSeconds(), true);
            this.computeDiff("ParallelXADispatchPolicy", this.bean.getParallelXADispatchPolicy(), var2.getParallelXADispatchPolicy(), true);
            this.computeDiff("ParallelXAEnabled", this.bean.getParallelXAEnabled(), var2.getParallelXAEnabled(), true);
            this.computeDiff("PurgeResourceFromCheckpointIntervalSeconds", this.bean.getPurgeResourceFromCheckpointIntervalSeconds(), var2.getPurgeResourceFromCheckpointIntervalSeconds(), true);
            this.computeDiff("RecoveryThresholdMillis", this.bean.getRecoveryThresholdMillis(), var2.getRecoveryThresholdMillis(), true);
            this.computeDiff("SecurityInteropMode", this.bean.getSecurityInteropMode(), var2.getSecurityInteropMode(), false);
            this.computeDiff("SerializeEnlistmentsGCIntervalMillis", this.bean.getSerializeEnlistmentsGCIntervalMillis(), var2.getSerializeEnlistmentsGCIntervalMillis(), true);
            this.computeDiff("TimeoutSeconds", this.bean.getTimeoutSeconds(), var2.getTimeoutSeconds(), true);
            this.computeDiff("UnregisterResourceGracePeriod", this.bean.getUnregisterResourceGracePeriod(), var2.getUnregisterResourceGracePeriod(), true);
            this.computeDiff("WSATTransportSecurityMode", this.bean.getWSATTransportSecurityMode(), var2.getWSATTransportSecurityMode(), false);
            this.computeDiff("TwoPhaseEnabled", this.bean.isTwoPhaseEnabled(), var2.isTwoPhaseEnabled(), false);
            this.computeDiff("WSATIssuedTokenEnabled", this.bean.isWSATIssuedTokenEnabled(), var2.isWSATIssuedTokenEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JTAMBeanImpl var3 = (JTAMBeanImpl)var1.getSourceBean();
            JTAMBeanImpl var4 = (JTAMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AbandonTimeoutSeconds")) {
                  var3.setAbandonTimeoutSeconds(var4.getAbandonTimeoutSeconds());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("BeforeCompletionIterationLimit")) {
                  var3.setBeforeCompletionIterationLimit(var4.getBeforeCompletionIterationLimit());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("CheckpointIntervalSeconds")) {
                  var3.setCheckpointIntervalSeconds(var4.getCheckpointIntervalSeconds());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (var5.equals("CompletionTimeoutSeconds")) {
                  var3.setCompletionTimeoutSeconds(var4.getCompletionTimeoutSeconds());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("ForgetHeuristics")) {
                  var3.setForgetHeuristics(var4.getForgetHeuristics());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("MaxResourceRequestsOnServer")) {
                  var3.setMaxResourceRequestsOnServer(var4.getMaxResourceRequestsOnServer());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("MaxResourceUnavailableMillis")) {
                  var3.setMaxResourceUnavailableMillis(var4.getMaxResourceUnavailableMillis());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("MaxTransactions")) {
                  var3.setMaxTransactions(var4.getMaxTransactions());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("MaxTransactionsHealthIntervalMillis")) {
                  var3.setMaxTransactionsHealthIntervalMillis(var4.getMaxTransactionsHealthIntervalMillis());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("MaxUniqueNameStatistics")) {
                  var3.setMaxUniqueNameStatistics(var4.getMaxUniqueNameStatistics());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("MaxXACallMillis")) {
                  var3.setMaxXACallMillis(var4.getMaxXACallMillis());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("MigrationCheckpointIntervalSeconds")) {
                  var3.setMigrationCheckpointIntervalSeconds(var4.getMigrationCheckpointIntervalSeconds());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 18);
               } else if (var5.equals("ParallelXADispatchPolicy")) {
                  var3.setParallelXADispatchPolicy(var4.getParallelXADispatchPolicy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (var5.equals("ParallelXAEnabled")) {
                  var3.setParallelXAEnabled(var4.getParallelXAEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 23);
               } else if (var5.equals("PurgeResourceFromCheckpointIntervalSeconds")) {
                  var3.setPurgeResourceFromCheckpointIntervalSeconds(var4.getPurgeResourceFromCheckpointIntervalSeconds());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("RecoveryThresholdMillis")) {
                  var3.setRecoveryThresholdMillis(var4.getRecoveryThresholdMillis());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("SecurityInteropMode")) {
                  var3.setSecurityInteropMode(var4.getSecurityInteropMode());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 26);
               } else if (var5.equals("SerializeEnlistmentsGCIntervalMillis")) {
                  var3.setSerializeEnlistmentsGCIntervalMillis(var4.getSerializeEnlistmentsGCIntervalMillis());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (var5.equals("TimeoutSeconds")) {
                  var3.setTimeoutSeconds(var4.getTimeoutSeconds());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("UnregisterResourceGracePeriod")) {
                  var3.setUnregisterResourceGracePeriod(var4.getUnregisterResourceGracePeriod());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 25);
               } else if (var5.equals("WSATTransportSecurityMode")) {
                  var3.setWSATTransportSecurityMode(var4.getWSATTransportSecurityMode());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 27);
               } else if (var5.equals("TwoPhaseEnabled")) {
                  var3.setTwoPhaseEnabled(var4.isTwoPhaseEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 29);
               } else if (var5.equals("WSATIssuedTokenEnabled")) {
                  var3.setWSATIssuedTokenEnabled(var4.isWSATIssuedTokenEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 28);
               } else {
                  super.applyPropertyUpdate(var1, var2);
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
            JTAMBeanImpl var5 = (JTAMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AbandonTimeoutSeconds")) && this.bean.isAbandonTimeoutSecondsSet()) {
               var5.setAbandonTimeoutSeconds(this.bean.getAbandonTimeoutSeconds());
            }

            if ((var3 == null || !var3.contains("BeforeCompletionIterationLimit")) && this.bean.isBeforeCompletionIterationLimitSet()) {
               var5.setBeforeCompletionIterationLimit(this.bean.getBeforeCompletionIterationLimit());
            }

            if ((var3 == null || !var3.contains("CheckpointIntervalSeconds")) && this.bean.isCheckpointIntervalSecondsSet()) {
               var5.setCheckpointIntervalSeconds(this.bean.getCheckpointIntervalSeconds());
            }

            if ((var3 == null || !var3.contains("CompletionTimeoutSeconds")) && this.bean.isCompletionTimeoutSecondsSet()) {
               var5.setCompletionTimeoutSeconds(this.bean.getCompletionTimeoutSeconds());
            }

            if ((var3 == null || !var3.contains("ForgetHeuristics")) && this.bean.isForgetHeuristicsSet()) {
               var5.setForgetHeuristics(this.bean.getForgetHeuristics());
            }

            if ((var3 == null || !var3.contains("MaxResourceRequestsOnServer")) && this.bean.isMaxResourceRequestsOnServerSet()) {
               var5.setMaxResourceRequestsOnServer(this.bean.getMaxResourceRequestsOnServer());
            }

            if ((var3 == null || !var3.contains("MaxResourceUnavailableMillis")) && this.bean.isMaxResourceUnavailableMillisSet()) {
               var5.setMaxResourceUnavailableMillis(this.bean.getMaxResourceUnavailableMillis());
            }

            if ((var3 == null || !var3.contains("MaxTransactions")) && this.bean.isMaxTransactionsSet()) {
               var5.setMaxTransactions(this.bean.getMaxTransactions());
            }

            if ((var3 == null || !var3.contains("MaxTransactionsHealthIntervalMillis")) && this.bean.isMaxTransactionsHealthIntervalMillisSet()) {
               var5.setMaxTransactionsHealthIntervalMillis(this.bean.getMaxTransactionsHealthIntervalMillis());
            }

            if ((var3 == null || !var3.contains("MaxUniqueNameStatistics")) && this.bean.isMaxUniqueNameStatisticsSet()) {
               var5.setMaxUniqueNameStatistics(this.bean.getMaxUniqueNameStatistics());
            }

            if ((var3 == null || !var3.contains("MaxXACallMillis")) && this.bean.isMaxXACallMillisSet()) {
               var5.setMaxXACallMillis(this.bean.getMaxXACallMillis());
            }

            if ((var3 == null || !var3.contains("MigrationCheckpointIntervalSeconds")) && this.bean.isMigrationCheckpointIntervalSecondsSet()) {
               var5.setMigrationCheckpointIntervalSeconds(this.bean.getMigrationCheckpointIntervalSeconds());
            }

            if ((var3 == null || !var3.contains("ParallelXADispatchPolicy")) && this.bean.isParallelXADispatchPolicySet()) {
               var5.setParallelXADispatchPolicy(this.bean.getParallelXADispatchPolicy());
            }

            if ((var3 == null || !var3.contains("ParallelXAEnabled")) && this.bean.isParallelXAEnabledSet()) {
               var5.setParallelXAEnabled(this.bean.getParallelXAEnabled());
            }

            if ((var3 == null || !var3.contains("PurgeResourceFromCheckpointIntervalSeconds")) && this.bean.isPurgeResourceFromCheckpointIntervalSecondsSet()) {
               var5.setPurgeResourceFromCheckpointIntervalSeconds(this.bean.getPurgeResourceFromCheckpointIntervalSeconds());
            }

            if ((var3 == null || !var3.contains("RecoveryThresholdMillis")) && this.bean.isRecoveryThresholdMillisSet()) {
               var5.setRecoveryThresholdMillis(this.bean.getRecoveryThresholdMillis());
            }

            if ((var3 == null || !var3.contains("SecurityInteropMode")) && this.bean.isSecurityInteropModeSet()) {
               var5.setSecurityInteropMode(this.bean.getSecurityInteropMode());
            }

            if ((var3 == null || !var3.contains("SerializeEnlistmentsGCIntervalMillis")) && this.bean.isSerializeEnlistmentsGCIntervalMillisSet()) {
               var5.setSerializeEnlistmentsGCIntervalMillis(this.bean.getSerializeEnlistmentsGCIntervalMillis());
            }

            if ((var3 == null || !var3.contains("TimeoutSeconds")) && this.bean.isTimeoutSecondsSet()) {
               var5.setTimeoutSeconds(this.bean.getTimeoutSeconds());
            }

            if ((var3 == null || !var3.contains("UnregisterResourceGracePeriod")) && this.bean.isUnregisterResourceGracePeriodSet()) {
               var5.setUnregisterResourceGracePeriod(this.bean.getUnregisterResourceGracePeriod());
            }

            if ((var3 == null || !var3.contains("WSATTransportSecurityMode")) && this.bean.isWSATTransportSecurityModeSet()) {
               var5.setWSATTransportSecurityMode(this.bean.getWSATTransportSecurityMode());
            }

            if ((var3 == null || !var3.contains("TwoPhaseEnabled")) && this.bean.isTwoPhaseEnabledSet()) {
               var5.setTwoPhaseEnabled(this.bean.isTwoPhaseEnabled());
            }

            if ((var3 == null || !var3.contains("WSATIssuedTokenEnabled")) && this.bean.isWSATIssuedTokenEnabledSet()) {
               var5.setWSATIssuedTokenEnabled(this.bean.isWSATIssuedTokenEnabled());
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
      }
   }
}
