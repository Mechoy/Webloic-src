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
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class MessagingBridgeMBeanImpl extends DeploymentMBeanImpl implements MessagingBridgeMBean, Serializable {
   private boolean _AsyncDisabled;
   private boolean _AsyncEnabled;
   private long _BatchInterval;
   private int _BatchSize;
   private boolean _DurabilityDisabled;
   private boolean _DurabilityEnabled;
   private String _ForwardingPolicy;
   private int _IdleTimeMaximum;
   private long _MaximumIdleTimeMilliseconds;
   private boolean _PreserveMsgProperty;
   private boolean _QOSDegradationAllowed;
   private String _QualityOfService;
   private int _ReconnectDelayIncrease;
   private long _ReconnectDelayIncrement;
   private long _ReconnectDelayInitialMilliseconds;
   private int _ReconnectDelayMaximum;
   private long _ReconnectDelayMaximumMilliseconds;
   private int _ReconnectDelayMinimum;
   private long _ScanUnitMilliseconds;
   private String _ScheduleTime;
   private String _Selector;
   private BridgeDestinationCommonMBean _SourceDestination;
   private boolean _Started;
   private BridgeDestinationCommonMBean _TargetDestination;
   private int _TransactionTimeout;
   private int _TransactionTimeoutSeconds;
   private static SchemaHelper2 _schemaHelper;

   public MessagingBridgeMBeanImpl() {
      this._initializeProperty(-1);
   }

   public MessagingBridgeMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public BridgeDestinationCommonMBean getSourceDestination() {
      return this._SourceDestination;
   }

   public String getSourceDestinationAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getSourceDestination();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isSourceDestinationSet() {
      return this._isSet(9);
   }

   public void setSourceDestinationAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, BridgeDestinationCommonMBean.class, new ReferenceManager.Resolver(this, 9) {
            public void resolveReference(Object var1) {
               try {
                  MessagingBridgeMBeanImpl.this.setSourceDestination((BridgeDestinationCommonMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         BridgeDestinationCommonMBean var2 = this._SourceDestination;
         this._initializeProperty(9);
         this._postSet(9, var2, this._SourceDestination);
      }

   }

   public void setSourceDestination(BridgeDestinationCommonMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 9, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return MessagingBridgeMBeanImpl.this.getSourceDestination();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      BridgeDestinationCommonMBean var3 = this._SourceDestination;
      this._SourceDestination = var1;
      this._postSet(9, var3, var1);
   }

   public BridgeDestinationCommonMBean getTargetDestination() {
      return this._TargetDestination;
   }

   public String getTargetDestinationAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getTargetDestination();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isTargetDestinationSet() {
      return this._isSet(10);
   }

   public void setTargetDestinationAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, BridgeDestinationCommonMBean.class, new ReferenceManager.Resolver(this, 10) {
            public void resolveReference(Object var1) {
               try {
                  MessagingBridgeMBeanImpl.this.setTargetDestination((BridgeDestinationCommonMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         BridgeDestinationCommonMBean var2 = this._TargetDestination;
         this._initializeProperty(10);
         this._postSet(10, var2, this._TargetDestination);
      }

   }

   public void setTargetDestination(BridgeDestinationCommonMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 10, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return MessagingBridgeMBeanImpl.this.getTargetDestination();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      BridgeDestinationCommonMBean var3 = this._TargetDestination;
      this._TargetDestination = var1;
      this._postSet(10, var3, var1);
   }

   public String getSelector() {
      return this._Selector;
   }

   public boolean isSelectorSet() {
      return this._isSet(11);
   }

   public void setSelector(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Selector;
      this._Selector = var1;
      this._postSet(11, var2, var1);
   }

   public String getForwardingPolicy() {
      return this._ForwardingPolicy;
   }

   public boolean isForwardingPolicySet() {
      return this._isSet(12);
   }

   public void setForwardingPolicy(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Automatic", "Manual", "Scheduled"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("ForwardingPolicy", var1, var2);
      this._ForwardingPolicy = var1;
   }

   public String getScheduleTime() {
      return this._ScheduleTime;
   }

   public boolean isScheduleTimeSet() {
      return this._isSet(13);
   }

   public void setScheduleTime(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._ScheduleTime = var1;
   }

   public String getQualityOfService() {
      return this._QualityOfService;
   }

   public boolean isQualityOfServiceSet() {
      return this._isSet(14);
   }

   public void setQualityOfService(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Exactly-once", "Atmost-once", "Duplicate-okay"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("QualityOfService", var1, var2);
      String var3 = this._QualityOfService;
      this._QualityOfService = var1;
      this._postSet(14, var3, var1);
   }

   public boolean isQOSDegradationAllowed() {
      return this._QOSDegradationAllowed;
   }

   public boolean isQOSDegradationAllowedSet() {
      return this._isSet(15);
   }

   public void setQOSDegradationAllowed(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._QOSDegradationAllowed;
      this._QOSDegradationAllowed = var1;
      this._postSet(15, var2, var1);
   }

   public boolean isDurabilityDisabled() {
      return this._DurabilityDisabled;
   }

   public boolean isDurabilityDisabledSet() {
      return this._isSet(16);
   }

   public void setDurabilityDisabled(boolean var1) throws InvalidAttributeValueException {
      this._DurabilityDisabled = var1;
   }

   public boolean isDurabilityEnabled() {
      return this._DurabilityEnabled;
   }

   public boolean isDurabilityEnabledSet() {
      return this._isSet(17);
   }

   public void setDurabilityEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._DurabilityEnabled;
      this._DurabilityEnabled = var1;
      this._postSet(17, var2, var1);
   }

   public long getReconnectDelayInitialMilliseconds() {
      return this._ReconnectDelayInitialMilliseconds;
   }

   public boolean isReconnectDelayInitialMillisecondsSet() {
      return this._isSet(18);
   }

   public void setReconnectDelayInitialMilliseconds(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ReconnectDelayInitialMilliseconds", var1, 0L, Long.MAX_VALUE);
      this._ReconnectDelayInitialMilliseconds = var1;
   }

   public int getReconnectDelayMinimum() {
      return this._ReconnectDelayMinimum;
   }

   public boolean isReconnectDelayMinimumSet() {
      return this._isSet(19);
   }

   public void setReconnectDelayMinimum(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ReconnectDelayMinimum", (long)var1, 0L, 2147483647L);
      int var2 = this._ReconnectDelayMinimum;
      this._ReconnectDelayMinimum = var1;
      this._postSet(19, var2, var1);
   }

   public long getReconnectDelayIncrement() {
      return this._ReconnectDelayIncrement;
   }

   public boolean isReconnectDelayIncrementSet() {
      return this._isSet(20);
   }

   public void setReconnectDelayIncrement(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ReconnectDelayIncrement", var1, 0L, Long.MAX_VALUE);
      this._ReconnectDelayIncrement = var1;
   }

   public int getReconnectDelayIncrease() {
      return this._ReconnectDelayIncrease;
   }

   public boolean isReconnectDelayIncreaseSet() {
      return this._isSet(21);
   }

   public void setReconnectDelayIncrease(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ReconnectDelayIncrease", (long)var1, 0L, 2147483647L);
      int var2 = this._ReconnectDelayIncrease;
      this._ReconnectDelayIncrease = var1;
      this._postSet(21, var2, var1);
   }

   public long getReconnectDelayMaximumMilliseconds() {
      return this._ReconnectDelayMaximumMilliseconds;
   }

   public boolean isReconnectDelayMaximumMillisecondsSet() {
      return this._isSet(22);
   }

   public void setReconnectDelayMaximumMilliseconds(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ReconnectDelayMaximumMilliseconds", var1, 0L, Long.MAX_VALUE);
      this._ReconnectDelayMaximumMilliseconds = var1;
   }

   public int getReconnectDelayMaximum() {
      return this._ReconnectDelayMaximum;
   }

   public boolean isReconnectDelayMaximumSet() {
      return this._isSet(23);
   }

   public void setReconnectDelayMaximum(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ReconnectDelayMaximum", (long)var1, 0L, 2147483647L);
      int var2 = this._ReconnectDelayMaximum;
      this._ReconnectDelayMaximum = var1;
      this._postSet(23, var2, var1);
   }

   public long getMaximumIdleTimeMilliseconds() {
      return this._MaximumIdleTimeMilliseconds;
   }

   public boolean isMaximumIdleTimeMillisecondsSet() {
      return this._isSet(24);
   }

   public void setMaximumIdleTimeMilliseconds(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaximumIdleTimeMilliseconds", var1, 0L, Long.MAX_VALUE);
      this._MaximumIdleTimeMilliseconds = var1;
   }

   public int getIdleTimeMaximum() {
      return this._IdleTimeMaximum;
   }

   public boolean isIdleTimeMaximumSet() {
      return this._isSet(25);
   }

   public void setIdleTimeMaximum(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("IdleTimeMaximum", (long)var1, 0L, 2147483647L);
      int var2 = this._IdleTimeMaximum;
      this._IdleTimeMaximum = var1;
      this._postSet(25, var2, var1);
   }

   public long getScanUnitMilliseconds() {
      return this._ScanUnitMilliseconds;
   }

   public boolean isScanUnitMillisecondsSet() {
      return this._isSet(26);
   }

   public void setScanUnitMilliseconds(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ScanUnitMilliseconds", var1, 0L, Long.MAX_VALUE);
      this._ScanUnitMilliseconds = var1;
   }

   public int getTransactionTimeoutSeconds() {
      return this._TransactionTimeoutSeconds;
   }

   public boolean isTransactionTimeoutSecondsSet() {
      return this._isSet(27);
   }

   public void setTransactionTimeoutSeconds(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("TransactionTimeoutSeconds", (long)var1, 0L, 2147483647L);
      this._TransactionTimeoutSeconds = var1;
   }

   public int getTransactionTimeout() {
      return this._TransactionTimeout;
   }

   public boolean isTransactionTimeoutSet() {
      return this._isSet(28);
   }

   public void setTransactionTimeout(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("TransactionTimeout", (long)var1, 0L, 2147483647L);
      int var2 = this._TransactionTimeout;
      this._TransactionTimeout = var1;
      this._postSet(28, var2, var1);
   }

   public boolean isAsyncDisabled() {
      return this._AsyncDisabled;
   }

   public boolean isAsyncDisabledSet() {
      return this._isSet(29);
   }

   public void setAsyncDisabled(boolean var1) throws InvalidAttributeValueException {
      this._AsyncDisabled = var1;
   }

   public boolean isAsyncEnabled() {
      return this._AsyncEnabled;
   }

   public boolean isAsyncEnabledSet() {
      return this._isSet(30);
   }

   public void setAsyncEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._AsyncEnabled;
      this._AsyncEnabled = var1;
      this._postSet(30, var2, var1);
   }

   public boolean isStarted() {
      return this._Started;
   }

   public boolean isStartedSet() {
      return this._isSet(31);
   }

   public void setStarted(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._Started;
      this._Started = var1;
      this._postSet(31, var2, var1);
   }

   public int getBatchSize() {
      return this._BatchSize;
   }

   public boolean isBatchSizeSet() {
      return this._isSet(32);
   }

   public void setBatchSize(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("BatchSize", var1, 0);
      int var2 = this._BatchSize;
      this._BatchSize = var1;
      this._postSet(32, var2, var1);
   }

   public long getBatchInterval() {
      return this._BatchInterval;
   }

   public boolean isBatchIntervalSet() {
      return this._isSet(33);
   }

   public void setBatchInterval(long var1) throws InvalidAttributeValueException {
      long var3 = this._BatchInterval;
      this._BatchInterval = var1;
      this._postSet(33, var3, var1);
   }

   public boolean getPreserveMsgProperty() {
      return this._PreserveMsgProperty;
   }

   public boolean isPreserveMsgPropertySet() {
      return this._isSet(34);
   }

   public void setPreserveMsgProperty(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._PreserveMsgProperty;
      this._PreserveMsgProperty = var1;
      this._postSet(34, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      BridgeLegalHelper.validateBridgeDestinations(this);
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
         var1 = 33;
      }

      try {
         switch (var1) {
            case 33:
               this._BatchInterval = -1L;
               if (var2) {
                  break;
               }
            case 32:
               this._BatchSize = 10;
               if (var2) {
                  break;
               }
            case 12:
               this._ForwardingPolicy = "Automatic";
               if (var2) {
                  break;
               }
            case 25:
               this._IdleTimeMaximum = 60;
               if (var2) {
                  break;
               }
            case 24:
               this._MaximumIdleTimeMilliseconds = 1800000L;
               if (var2) {
                  break;
               }
            case 34:
               this._PreserveMsgProperty = false;
               if (var2) {
                  break;
               }
            case 14:
               this._QualityOfService = "Exactly-once";
               if (var2) {
                  break;
               }
            case 21:
               this._ReconnectDelayIncrease = 5;
               if (var2) {
                  break;
               }
            case 20:
               this._ReconnectDelayIncrement = 5000L;
               if (var2) {
                  break;
               }
            case 18:
               this._ReconnectDelayInitialMilliseconds = 15000L;
               if (var2) {
                  break;
               }
            case 23:
               this._ReconnectDelayMaximum = 60;
               if (var2) {
                  break;
               }
            case 22:
               this._ReconnectDelayMaximumMilliseconds = 50000L;
               if (var2) {
                  break;
               }
            case 19:
               this._ReconnectDelayMinimum = 15;
               if (var2) {
                  break;
               }
            case 26:
               this._ScanUnitMilliseconds = 5000L;
               if (var2) {
                  break;
               }
            case 13:
               this._ScheduleTime = null;
               if (var2) {
                  break;
               }
            case 11:
               this._Selector = null;
               if (var2) {
                  break;
               }
            case 9:
               this._SourceDestination = null;
               if (var2) {
                  break;
               }
            case 10:
               this._TargetDestination = null;
               if (var2) {
                  break;
               }
            case 28:
               this._TransactionTimeout = 30;
               if (var2) {
                  break;
               }
            case 27:
               this._TransactionTimeoutSeconds = 30;
               if (var2) {
                  break;
               }
            case 29:
               this._AsyncDisabled = false;
               if (var2) {
                  break;
               }
            case 30:
               this._AsyncEnabled = true;
               if (var2) {
                  break;
               }
            case 16:
               this._DurabilityDisabled = false;
               if (var2) {
                  break;
               }
            case 17:
               this._DurabilityEnabled = true;
               if (var2) {
                  break;
               }
            case 15:
               this._QOSDegradationAllowed = false;
               if (var2) {
                  break;
               }
            case 31:
               this._Started = true;
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
      return "MessagingBridge";
   }

   public void putValue(String var1, Object var2) {
      boolean var6;
      if (var1.equals("AsyncDisabled")) {
         var6 = this._AsyncDisabled;
         this._AsyncDisabled = (Boolean)var2;
         this._postSet(29, var6, this._AsyncDisabled);
      } else if (var1.equals("AsyncEnabled")) {
         var6 = this._AsyncEnabled;
         this._AsyncEnabled = (Boolean)var2;
         this._postSet(30, var6, this._AsyncEnabled);
      } else {
         long var8;
         if (var1.equals("BatchInterval")) {
            var8 = this._BatchInterval;
            this._BatchInterval = (Long)var2;
            this._postSet(33, var8, this._BatchInterval);
         } else {
            int var3;
            if (var1.equals("BatchSize")) {
               var3 = this._BatchSize;
               this._BatchSize = (Integer)var2;
               this._postSet(32, var3, this._BatchSize);
            } else if (var1.equals("DurabilityDisabled")) {
               var6 = this._DurabilityDisabled;
               this._DurabilityDisabled = (Boolean)var2;
               this._postSet(16, var6, this._DurabilityDisabled);
            } else if (var1.equals("DurabilityEnabled")) {
               var6 = this._DurabilityEnabled;
               this._DurabilityEnabled = (Boolean)var2;
               this._postSet(17, var6, this._DurabilityEnabled);
            } else {
               String var7;
               if (var1.equals("ForwardingPolicy")) {
                  var7 = this._ForwardingPolicy;
                  this._ForwardingPolicy = (String)var2;
                  this._postSet(12, var7, this._ForwardingPolicy);
               } else if (var1.equals("IdleTimeMaximum")) {
                  var3 = this._IdleTimeMaximum;
                  this._IdleTimeMaximum = (Integer)var2;
                  this._postSet(25, var3, this._IdleTimeMaximum);
               } else if (var1.equals("MaximumIdleTimeMilliseconds")) {
                  var8 = this._MaximumIdleTimeMilliseconds;
                  this._MaximumIdleTimeMilliseconds = (Long)var2;
                  this._postSet(24, var8, this._MaximumIdleTimeMilliseconds);
               } else if (var1.equals("PreserveMsgProperty")) {
                  var6 = this._PreserveMsgProperty;
                  this._PreserveMsgProperty = (Boolean)var2;
                  this._postSet(34, var6, this._PreserveMsgProperty);
               } else if (var1.equals("QOSDegradationAllowed")) {
                  var6 = this._QOSDegradationAllowed;
                  this._QOSDegradationAllowed = (Boolean)var2;
                  this._postSet(15, var6, this._QOSDegradationAllowed);
               } else if (var1.equals("QualityOfService")) {
                  var7 = this._QualityOfService;
                  this._QualityOfService = (String)var2;
                  this._postSet(14, var7, this._QualityOfService);
               } else if (var1.equals("ReconnectDelayIncrease")) {
                  var3 = this._ReconnectDelayIncrease;
                  this._ReconnectDelayIncrease = (Integer)var2;
                  this._postSet(21, var3, this._ReconnectDelayIncrease);
               } else if (var1.equals("ReconnectDelayIncrement")) {
                  var8 = this._ReconnectDelayIncrement;
                  this._ReconnectDelayIncrement = (Long)var2;
                  this._postSet(20, var8, this._ReconnectDelayIncrement);
               } else if (var1.equals("ReconnectDelayInitialMilliseconds")) {
                  var8 = this._ReconnectDelayInitialMilliseconds;
                  this._ReconnectDelayInitialMilliseconds = (Long)var2;
                  this._postSet(18, var8, this._ReconnectDelayInitialMilliseconds);
               } else if (var1.equals("ReconnectDelayMaximum")) {
                  var3 = this._ReconnectDelayMaximum;
                  this._ReconnectDelayMaximum = (Integer)var2;
                  this._postSet(23, var3, this._ReconnectDelayMaximum);
               } else if (var1.equals("ReconnectDelayMaximumMilliseconds")) {
                  var8 = this._ReconnectDelayMaximumMilliseconds;
                  this._ReconnectDelayMaximumMilliseconds = (Long)var2;
                  this._postSet(22, var8, this._ReconnectDelayMaximumMilliseconds);
               } else if (var1.equals("ReconnectDelayMinimum")) {
                  var3 = this._ReconnectDelayMinimum;
                  this._ReconnectDelayMinimum = (Integer)var2;
                  this._postSet(19, var3, this._ReconnectDelayMinimum);
               } else if (var1.equals("ScanUnitMilliseconds")) {
                  var8 = this._ScanUnitMilliseconds;
                  this._ScanUnitMilliseconds = (Long)var2;
                  this._postSet(26, var8, this._ScanUnitMilliseconds);
               } else if (var1.equals("ScheduleTime")) {
                  var7 = this._ScheduleTime;
                  this._ScheduleTime = (String)var2;
                  this._postSet(13, var7, this._ScheduleTime);
               } else if (var1.equals("Selector")) {
                  var7 = this._Selector;
                  this._Selector = (String)var2;
                  this._postSet(11, var7, this._Selector);
               } else {
                  BridgeDestinationCommonMBean var5;
                  if (var1.equals("SourceDestination")) {
                     var5 = this._SourceDestination;
                     this._SourceDestination = (BridgeDestinationCommonMBean)var2;
                     this._postSet(9, var5, this._SourceDestination);
                  } else if (var1.equals("Started")) {
                     var6 = this._Started;
                     this._Started = (Boolean)var2;
                     this._postSet(31, var6, this._Started);
                  } else if (var1.equals("TargetDestination")) {
                     var5 = this._TargetDestination;
                     this._TargetDestination = (BridgeDestinationCommonMBean)var2;
                     this._postSet(10, var5, this._TargetDestination);
                  } else if (var1.equals("TransactionTimeout")) {
                     var3 = this._TransactionTimeout;
                     this._TransactionTimeout = (Integer)var2;
                     this._postSet(28, var3, this._TransactionTimeout);
                  } else if (var1.equals("TransactionTimeoutSeconds")) {
                     var3 = this._TransactionTimeoutSeconds;
                     this._TransactionTimeoutSeconds = (Integer)var2;
                     this._postSet(27, var3, this._TransactionTimeoutSeconds);
                  } else {
                     super.putValue(var1, var2);
                  }
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AsyncDisabled")) {
         return new Boolean(this._AsyncDisabled);
      } else if (var1.equals("AsyncEnabled")) {
         return new Boolean(this._AsyncEnabled);
      } else if (var1.equals("BatchInterval")) {
         return new Long(this._BatchInterval);
      } else if (var1.equals("BatchSize")) {
         return new Integer(this._BatchSize);
      } else if (var1.equals("DurabilityDisabled")) {
         return new Boolean(this._DurabilityDisabled);
      } else if (var1.equals("DurabilityEnabled")) {
         return new Boolean(this._DurabilityEnabled);
      } else if (var1.equals("ForwardingPolicy")) {
         return this._ForwardingPolicy;
      } else if (var1.equals("IdleTimeMaximum")) {
         return new Integer(this._IdleTimeMaximum);
      } else if (var1.equals("MaximumIdleTimeMilliseconds")) {
         return new Long(this._MaximumIdleTimeMilliseconds);
      } else if (var1.equals("PreserveMsgProperty")) {
         return new Boolean(this._PreserveMsgProperty);
      } else if (var1.equals("QOSDegradationAllowed")) {
         return new Boolean(this._QOSDegradationAllowed);
      } else if (var1.equals("QualityOfService")) {
         return this._QualityOfService;
      } else if (var1.equals("ReconnectDelayIncrease")) {
         return new Integer(this._ReconnectDelayIncrease);
      } else if (var1.equals("ReconnectDelayIncrement")) {
         return new Long(this._ReconnectDelayIncrement);
      } else if (var1.equals("ReconnectDelayInitialMilliseconds")) {
         return new Long(this._ReconnectDelayInitialMilliseconds);
      } else if (var1.equals("ReconnectDelayMaximum")) {
         return new Integer(this._ReconnectDelayMaximum);
      } else if (var1.equals("ReconnectDelayMaximumMilliseconds")) {
         return new Long(this._ReconnectDelayMaximumMilliseconds);
      } else if (var1.equals("ReconnectDelayMinimum")) {
         return new Integer(this._ReconnectDelayMinimum);
      } else if (var1.equals("ScanUnitMilliseconds")) {
         return new Long(this._ScanUnitMilliseconds);
      } else if (var1.equals("ScheduleTime")) {
         return this._ScheduleTime;
      } else if (var1.equals("Selector")) {
         return this._Selector;
      } else if (var1.equals("SourceDestination")) {
         return this._SourceDestination;
      } else if (var1.equals("Started")) {
         return new Boolean(this._Started);
      } else if (var1.equals("TargetDestination")) {
         return this._TargetDestination;
      } else if (var1.equals("TransactionTimeout")) {
         return new Integer(this._TransactionTimeout);
      } else {
         return var1.equals("TransactionTimeoutSeconds") ? new Integer(this._TransactionTimeoutSeconds) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 7:
               if (var1.equals("started")) {
                  return 31;
               }
               break;
            case 8:
               if (var1.equals("selector")) {
                  return 11;
               }
            case 9:
            case 11:
            case 12:
            case 15:
            case 16:
            case 20:
            case 26:
            case 28:
            case 29:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            default:
               break;
            case 10:
               if (var1.equals("batch-size")) {
                  return 32;
               }
               break;
            case 13:
               if (var1.equals("schedule-time")) {
                  return 13;
               }

               if (var1.equals("async-enabled")) {
                  return 30;
               }
               break;
            case 14:
               if (var1.equals("batch-interval")) {
                  return 33;
               }

               if (var1.equals("async-disabled")) {
                  return 29;
               }
               break;
            case 17:
               if (var1.equals("forwarding-policy")) {
                  return 12;
               }

               if (var1.equals("idle-time-maximum")) {
                  return 25;
               }
               break;
            case 18:
               if (var1.equals("quality-of-service")) {
                  return 14;
               }

               if (var1.equals("source-destination")) {
                  return 9;
               }

               if (var1.equals("target-destination")) {
                  return 10;
               }

               if (var1.equals("durability-enabled")) {
                  return 17;
               }
               break;
            case 19:
               if (var1.equals("transaction-timeout")) {
                  return 28;
               }

               if (var1.equals("durability-disabled")) {
                  return 16;
               }
               break;
            case 21:
               if (var1.equals("preserve-msg-property")) {
                  return 34;
               }
               break;
            case 22:
               if (var1.equals("scan-unit-milliseconds")) {
                  return 26;
               }
               break;
            case 23:
               if (var1.equals("reconnect-delay-maximum")) {
                  return 23;
               }

               if (var1.equals("reconnect-delay-minimum")) {
                  return 19;
               }

               if (var1.equals("qos-degradation-allowed")) {
                  return 15;
               }
               break;
            case 24:
               if (var1.equals("reconnect-delay-increase")) {
                  return 21;
               }
               break;
            case 25:
               if (var1.equals("reconnect-delay-increment")) {
                  return 20;
               }
               break;
            case 27:
               if (var1.equals("transaction-timeout-seconds")) {
                  return 27;
               }
               break;
            case 30:
               if (var1.equals("maximum-idle-time-milliseconds")) {
                  return 24;
               }
               break;
            case 36:
               if (var1.equals("reconnect-delay-initial-milliseconds")) {
                  return 18;
               }

               if (var1.equals("reconnect-delay-maximum-milliseconds")) {
                  return 22;
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
            case 9:
               return "source-destination";
            case 10:
               return "target-destination";
            case 11:
               return "selector";
            case 12:
               return "forwarding-policy";
            case 13:
               return "schedule-time";
            case 14:
               return "quality-of-service";
            case 15:
               return "qos-degradation-allowed";
            case 16:
               return "durability-disabled";
            case 17:
               return "durability-enabled";
            case 18:
               return "reconnect-delay-initial-milliseconds";
            case 19:
               return "reconnect-delay-minimum";
            case 20:
               return "reconnect-delay-increment";
            case 21:
               return "reconnect-delay-increase";
            case 22:
               return "reconnect-delay-maximum-milliseconds";
            case 23:
               return "reconnect-delay-maximum";
            case 24:
               return "maximum-idle-time-milliseconds";
            case 25:
               return "idle-time-maximum";
            case 26:
               return "scan-unit-milliseconds";
            case 27:
               return "transaction-timeout-seconds";
            case 28:
               return "transaction-timeout";
            case 29:
               return "async-disabled";
            case 30:
               return "async-enabled";
            case 31:
               return "started";
            case 32:
               return "batch-size";
            case 33:
               return "batch-interval";
            case 34:
               return "preserve-msg-property";
            default:
               return super.getElementName(var1);
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

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends DeploymentMBeanImpl.Helper {
      private MessagingBridgeMBeanImpl bean;

      protected Helper(MessagingBridgeMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 9:
               return "SourceDestination";
            case 10:
               return "TargetDestination";
            case 11:
               return "Selector";
            case 12:
               return "ForwardingPolicy";
            case 13:
               return "ScheduleTime";
            case 14:
               return "QualityOfService";
            case 15:
               return "QOSDegradationAllowed";
            case 16:
               return "DurabilityDisabled";
            case 17:
               return "DurabilityEnabled";
            case 18:
               return "ReconnectDelayInitialMilliseconds";
            case 19:
               return "ReconnectDelayMinimum";
            case 20:
               return "ReconnectDelayIncrement";
            case 21:
               return "ReconnectDelayIncrease";
            case 22:
               return "ReconnectDelayMaximumMilliseconds";
            case 23:
               return "ReconnectDelayMaximum";
            case 24:
               return "MaximumIdleTimeMilliseconds";
            case 25:
               return "IdleTimeMaximum";
            case 26:
               return "ScanUnitMilliseconds";
            case 27:
               return "TransactionTimeoutSeconds";
            case 28:
               return "TransactionTimeout";
            case 29:
               return "AsyncDisabled";
            case 30:
               return "AsyncEnabled";
            case 31:
               return "Started";
            case 32:
               return "BatchSize";
            case 33:
               return "BatchInterval";
            case 34:
               return "PreserveMsgProperty";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("BatchInterval")) {
            return 33;
         } else if (var1.equals("BatchSize")) {
            return 32;
         } else if (var1.equals("ForwardingPolicy")) {
            return 12;
         } else if (var1.equals("IdleTimeMaximum")) {
            return 25;
         } else if (var1.equals("MaximumIdleTimeMilliseconds")) {
            return 24;
         } else if (var1.equals("PreserveMsgProperty")) {
            return 34;
         } else if (var1.equals("QualityOfService")) {
            return 14;
         } else if (var1.equals("ReconnectDelayIncrease")) {
            return 21;
         } else if (var1.equals("ReconnectDelayIncrement")) {
            return 20;
         } else if (var1.equals("ReconnectDelayInitialMilliseconds")) {
            return 18;
         } else if (var1.equals("ReconnectDelayMaximum")) {
            return 23;
         } else if (var1.equals("ReconnectDelayMaximumMilliseconds")) {
            return 22;
         } else if (var1.equals("ReconnectDelayMinimum")) {
            return 19;
         } else if (var1.equals("ScanUnitMilliseconds")) {
            return 26;
         } else if (var1.equals("ScheduleTime")) {
            return 13;
         } else if (var1.equals("Selector")) {
            return 11;
         } else if (var1.equals("SourceDestination")) {
            return 9;
         } else if (var1.equals("TargetDestination")) {
            return 10;
         } else if (var1.equals("TransactionTimeout")) {
            return 28;
         } else if (var1.equals("TransactionTimeoutSeconds")) {
            return 27;
         } else if (var1.equals("AsyncDisabled")) {
            return 29;
         } else if (var1.equals("AsyncEnabled")) {
            return 30;
         } else if (var1.equals("DurabilityDisabled")) {
            return 16;
         } else if (var1.equals("DurabilityEnabled")) {
            return 17;
         } else if (var1.equals("QOSDegradationAllowed")) {
            return 15;
         } else {
            return var1.equals("Started") ? 31 : super.getPropertyIndex(var1);
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
            if (this.bean.isBatchIntervalSet()) {
               var2.append("BatchInterval");
               var2.append(String.valueOf(this.bean.getBatchInterval()));
            }

            if (this.bean.isBatchSizeSet()) {
               var2.append("BatchSize");
               var2.append(String.valueOf(this.bean.getBatchSize()));
            }

            if (this.bean.isForwardingPolicySet()) {
               var2.append("ForwardingPolicy");
               var2.append(String.valueOf(this.bean.getForwardingPolicy()));
            }

            if (this.bean.isIdleTimeMaximumSet()) {
               var2.append("IdleTimeMaximum");
               var2.append(String.valueOf(this.bean.getIdleTimeMaximum()));
            }

            if (this.bean.isMaximumIdleTimeMillisecondsSet()) {
               var2.append("MaximumIdleTimeMilliseconds");
               var2.append(String.valueOf(this.bean.getMaximumIdleTimeMilliseconds()));
            }

            if (this.bean.isPreserveMsgPropertySet()) {
               var2.append("PreserveMsgProperty");
               var2.append(String.valueOf(this.bean.getPreserveMsgProperty()));
            }

            if (this.bean.isQualityOfServiceSet()) {
               var2.append("QualityOfService");
               var2.append(String.valueOf(this.bean.getQualityOfService()));
            }

            if (this.bean.isReconnectDelayIncreaseSet()) {
               var2.append("ReconnectDelayIncrease");
               var2.append(String.valueOf(this.bean.getReconnectDelayIncrease()));
            }

            if (this.bean.isReconnectDelayIncrementSet()) {
               var2.append("ReconnectDelayIncrement");
               var2.append(String.valueOf(this.bean.getReconnectDelayIncrement()));
            }

            if (this.bean.isReconnectDelayInitialMillisecondsSet()) {
               var2.append("ReconnectDelayInitialMilliseconds");
               var2.append(String.valueOf(this.bean.getReconnectDelayInitialMilliseconds()));
            }

            if (this.bean.isReconnectDelayMaximumSet()) {
               var2.append("ReconnectDelayMaximum");
               var2.append(String.valueOf(this.bean.getReconnectDelayMaximum()));
            }

            if (this.bean.isReconnectDelayMaximumMillisecondsSet()) {
               var2.append("ReconnectDelayMaximumMilliseconds");
               var2.append(String.valueOf(this.bean.getReconnectDelayMaximumMilliseconds()));
            }

            if (this.bean.isReconnectDelayMinimumSet()) {
               var2.append("ReconnectDelayMinimum");
               var2.append(String.valueOf(this.bean.getReconnectDelayMinimum()));
            }

            if (this.bean.isScanUnitMillisecondsSet()) {
               var2.append("ScanUnitMilliseconds");
               var2.append(String.valueOf(this.bean.getScanUnitMilliseconds()));
            }

            if (this.bean.isScheduleTimeSet()) {
               var2.append("ScheduleTime");
               var2.append(String.valueOf(this.bean.getScheduleTime()));
            }

            if (this.bean.isSelectorSet()) {
               var2.append("Selector");
               var2.append(String.valueOf(this.bean.getSelector()));
            }

            if (this.bean.isSourceDestinationSet()) {
               var2.append("SourceDestination");
               var2.append(String.valueOf(this.bean.getSourceDestination()));
            }

            if (this.bean.isTargetDestinationSet()) {
               var2.append("TargetDestination");
               var2.append(String.valueOf(this.bean.getTargetDestination()));
            }

            if (this.bean.isTransactionTimeoutSet()) {
               var2.append("TransactionTimeout");
               var2.append(String.valueOf(this.bean.getTransactionTimeout()));
            }

            if (this.bean.isTransactionTimeoutSecondsSet()) {
               var2.append("TransactionTimeoutSeconds");
               var2.append(String.valueOf(this.bean.getTransactionTimeoutSeconds()));
            }

            if (this.bean.isAsyncDisabledSet()) {
               var2.append("AsyncDisabled");
               var2.append(String.valueOf(this.bean.isAsyncDisabled()));
            }

            if (this.bean.isAsyncEnabledSet()) {
               var2.append("AsyncEnabled");
               var2.append(String.valueOf(this.bean.isAsyncEnabled()));
            }

            if (this.bean.isDurabilityDisabledSet()) {
               var2.append("DurabilityDisabled");
               var2.append(String.valueOf(this.bean.isDurabilityDisabled()));
            }

            if (this.bean.isDurabilityEnabledSet()) {
               var2.append("DurabilityEnabled");
               var2.append(String.valueOf(this.bean.isDurabilityEnabled()));
            }

            if (this.bean.isQOSDegradationAllowedSet()) {
               var2.append("QOSDegradationAllowed");
               var2.append(String.valueOf(this.bean.isQOSDegradationAllowed()));
            }

            if (this.bean.isStartedSet()) {
               var2.append("Started");
               var2.append(String.valueOf(this.bean.isStarted()));
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
            MessagingBridgeMBeanImpl var2 = (MessagingBridgeMBeanImpl)var1;
            this.computeDiff("BatchInterval", this.bean.getBatchInterval(), var2.getBatchInterval(), true);
            this.computeDiff("BatchSize", this.bean.getBatchSize(), var2.getBatchSize(), true);
            this.computeDiff("IdleTimeMaximum", this.bean.getIdleTimeMaximum(), var2.getIdleTimeMaximum(), true);
            this.computeDiff("PreserveMsgProperty", this.bean.getPreserveMsgProperty(), var2.getPreserveMsgProperty(), true);
            this.computeDiff("QualityOfService", this.bean.getQualityOfService(), var2.getQualityOfService(), false);
            this.computeDiff("ReconnectDelayIncrease", this.bean.getReconnectDelayIncrease(), var2.getReconnectDelayIncrease(), true);
            this.computeDiff("ReconnectDelayMaximum", this.bean.getReconnectDelayMaximum(), var2.getReconnectDelayMaximum(), true);
            this.computeDiff("ReconnectDelayMinimum", this.bean.getReconnectDelayMinimum(), var2.getReconnectDelayMinimum(), true);
            this.computeDiff("Selector", this.bean.getSelector(), var2.getSelector(), false);
            this.computeDiff("SourceDestination", this.bean.getSourceDestination(), var2.getSourceDestination(), false);
            this.computeDiff("TargetDestination", this.bean.getTargetDestination(), var2.getTargetDestination(), false);
            this.computeDiff("TransactionTimeout", this.bean.getTransactionTimeout(), var2.getTransactionTimeout(), true);
            this.computeDiff("AsyncEnabled", this.bean.isAsyncEnabled(), var2.isAsyncEnabled(), false);
            this.computeDiff("DurabilityEnabled", this.bean.isDurabilityEnabled(), var2.isDurabilityEnabled(), false);
            this.computeDiff("QOSDegradationAllowed", this.bean.isQOSDegradationAllowed(), var2.isQOSDegradationAllowed(), false);
            this.computeDiff("Started", this.bean.isStarted(), var2.isStarted(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            MessagingBridgeMBeanImpl var3 = (MessagingBridgeMBeanImpl)var1.getSourceBean();
            MessagingBridgeMBeanImpl var4 = (MessagingBridgeMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("BatchInterval")) {
                  var3.setBatchInterval(var4.getBatchInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 33);
               } else if (var5.equals("BatchSize")) {
                  var3.setBatchSize(var4.getBatchSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 32);
               } else if (!var5.equals("ForwardingPolicy")) {
                  if (var5.equals("IdleTimeMaximum")) {
                     var3.setIdleTimeMaximum(var4.getIdleTimeMaximum());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 25);
                  } else if (!var5.equals("MaximumIdleTimeMilliseconds")) {
                     if (var5.equals("PreserveMsgProperty")) {
                        var3.setPreserveMsgProperty(var4.getPreserveMsgProperty());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 34);
                     } else if (var5.equals("QualityOfService")) {
                        var3.setQualityOfService(var4.getQualityOfService());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                     } else if (var5.equals("ReconnectDelayIncrease")) {
                        var3.setReconnectDelayIncrease(var4.getReconnectDelayIncrease());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 21);
                     } else if (!var5.equals("ReconnectDelayIncrement") && !var5.equals("ReconnectDelayInitialMilliseconds")) {
                        if (var5.equals("ReconnectDelayMaximum")) {
                           var3.setReconnectDelayMaximum(var4.getReconnectDelayMaximum());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 23);
                        } else if (!var5.equals("ReconnectDelayMaximumMilliseconds")) {
                           if (var5.equals("ReconnectDelayMinimum")) {
                              var3.setReconnectDelayMinimum(var4.getReconnectDelayMinimum());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 19);
                           } else if (!var5.equals("ScanUnitMilliseconds") && !var5.equals("ScheduleTime")) {
                              if (var5.equals("Selector")) {
                                 var3.setSelector(var4.getSelector());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                              } else if (var5.equals("SourceDestination")) {
                                 var3.setSourceDestinationAsString(var4.getSourceDestinationAsString());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                              } else if (var5.equals("TargetDestination")) {
                                 var3.setTargetDestinationAsString(var4.getTargetDestinationAsString());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                              } else if (var5.equals("TransactionTimeout")) {
                                 var3.setTransactionTimeout(var4.getTransactionTimeout());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 28);
                              } else if (!var5.equals("TransactionTimeoutSeconds") && !var5.equals("AsyncDisabled")) {
                                 if (var5.equals("AsyncEnabled")) {
                                    var3.setAsyncEnabled(var4.isAsyncEnabled());
                                    var3._conditionalUnset(var2.isUnsetUpdate(), 30);
                                 } else if (!var5.equals("DurabilityDisabled")) {
                                    if (var5.equals("DurabilityEnabled")) {
                                       var3.setDurabilityEnabled(var4.isDurabilityEnabled());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                                    } else if (var5.equals("QOSDegradationAllowed")) {
                                       var3.setQOSDegradationAllowed(var4.isQOSDegradationAllowed());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                                    } else if (var5.equals("Started")) {
                                       var3.setStarted(var4.isStarted());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 31);
                                    } else {
                                       super.applyPropertyUpdate(var1, var2);
                                    }
                                 }
                              }
                           }
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
            MessagingBridgeMBeanImpl var5 = (MessagingBridgeMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("BatchInterval")) && this.bean.isBatchIntervalSet()) {
               var5.setBatchInterval(this.bean.getBatchInterval());
            }

            if ((var3 == null || !var3.contains("BatchSize")) && this.bean.isBatchSizeSet()) {
               var5.setBatchSize(this.bean.getBatchSize());
            }

            if ((var3 == null || !var3.contains("IdleTimeMaximum")) && this.bean.isIdleTimeMaximumSet()) {
               var5.setIdleTimeMaximum(this.bean.getIdleTimeMaximum());
            }

            if ((var3 == null || !var3.contains("PreserveMsgProperty")) && this.bean.isPreserveMsgPropertySet()) {
               var5.setPreserveMsgProperty(this.bean.getPreserveMsgProperty());
            }

            if ((var3 == null || !var3.contains("QualityOfService")) && this.bean.isQualityOfServiceSet()) {
               var5.setQualityOfService(this.bean.getQualityOfService());
            }

            if ((var3 == null || !var3.contains("ReconnectDelayIncrease")) && this.bean.isReconnectDelayIncreaseSet()) {
               var5.setReconnectDelayIncrease(this.bean.getReconnectDelayIncrease());
            }

            if ((var3 == null || !var3.contains("ReconnectDelayMaximum")) && this.bean.isReconnectDelayMaximumSet()) {
               var5.setReconnectDelayMaximum(this.bean.getReconnectDelayMaximum());
            }

            if ((var3 == null || !var3.contains("ReconnectDelayMinimum")) && this.bean.isReconnectDelayMinimumSet()) {
               var5.setReconnectDelayMinimum(this.bean.getReconnectDelayMinimum());
            }

            if ((var3 == null || !var3.contains("Selector")) && this.bean.isSelectorSet()) {
               var5.setSelector(this.bean.getSelector());
            }

            if ((var3 == null || !var3.contains("SourceDestination")) && this.bean.isSourceDestinationSet()) {
               var5._unSet(var5, 9);
               var5.setSourceDestinationAsString(this.bean.getSourceDestinationAsString());
            }

            if ((var3 == null || !var3.contains("TargetDestination")) && this.bean.isTargetDestinationSet()) {
               var5._unSet(var5, 10);
               var5.setTargetDestinationAsString(this.bean.getTargetDestinationAsString());
            }

            if ((var3 == null || !var3.contains("TransactionTimeout")) && this.bean.isTransactionTimeoutSet()) {
               var5.setTransactionTimeout(this.bean.getTransactionTimeout());
            }

            if ((var3 == null || !var3.contains("AsyncEnabled")) && this.bean.isAsyncEnabledSet()) {
               var5.setAsyncEnabled(this.bean.isAsyncEnabled());
            }

            if ((var3 == null || !var3.contains("DurabilityEnabled")) && this.bean.isDurabilityEnabledSet()) {
               var5.setDurabilityEnabled(this.bean.isDurabilityEnabled());
            }

            if ((var3 == null || !var3.contains("QOSDegradationAllowed")) && this.bean.isQOSDegradationAllowedSet()) {
               var5.setQOSDegradationAllowed(this.bean.isQOSDegradationAllowed());
            }

            if ((var3 == null || !var3.contains("Started")) && this.bean.isStartedSet()) {
               var5.setStarted(this.bean.isStarted());
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
         this.inferSubTree(this.bean.getSourceDestination(), var1, var2);
         this.inferSubTree(this.bean.getTargetDestination(), var1, var2);
      }
   }
}
