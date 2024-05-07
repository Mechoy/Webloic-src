package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.jms.module.validators.JMSModuleValidator;
import weblogic.management.DistributedManagementException;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class JMSDestCommonMBeanImpl extends ConfigurationMBeanImpl implements JMSDestCommonMBean, Serializable {
   private long _BytesMaximum;
   private long _BytesThresholdHigh;
   private long _BytesThresholdLow;
   private long _CreationTime;
   private String _DeliveryModeOverride;
   private JMSDestinationKeyMBean[] _DestinationKeys;
   private JMSDestinationMBean _ErrorDestination;
   private String _ExpirationLoggingPolicy;
   private String _ExpirationPolicy;
   private int _MaximumMessageSize;
   private long _MessagesMaximum;
   private long _MessagesThresholdHigh;
   private long _MessagesThresholdLow;
   private int _PriorityOverride;
   private long _RedeliveryDelayOverride;
   private int _RedeliveryLimit;
   private String _TimeToDeliverOverride;
   private long _TimeToLiveOverride;
   private static SchemaHelper2 _schemaHelper;

   public JMSDestCommonMBeanImpl() {
      this._initializeProperty(-1);
   }

   public JMSDestCommonMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public JMSDestinationKeyMBean[] getDestinationKeys() {
      return this._DestinationKeys;
   }

   public String getDestinationKeysAsString() {
      return this._getHelper()._serializeKeyList(this.getDestinationKeys());
   }

   public boolean isDestinationKeysSet() {
      return this._isSet(7);
   }

   public void setDestinationKeysAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._DestinationKeys);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, JMSDestinationKeyMBean.class, new ReferenceManager.Resolver(this, 7) {
                  public void resolveReference(Object var1) {
                     try {
                        JMSDestCommonMBeanImpl.this.addDestinationKey((JMSDestinationKeyMBean)var1);
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
               JMSDestinationKeyMBean[] var6 = this._DestinationKeys;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  JMSDestinationKeyMBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removeDestinationKey(var9);
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
         JMSDestinationKeyMBean[] var2 = this._DestinationKeys;
         this._initializeProperty(7);
         this._postSet(7, var2, this._DestinationKeys);
      }
   }

   public boolean addDestinationKey(JMSDestinationKeyMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 7)) {
         JMSDestinationKeyMBean[] var2;
         if (this._isSet(7)) {
            var2 = (JMSDestinationKeyMBean[])((JMSDestinationKeyMBean[])this._getHelper()._extendArray(this.getDestinationKeys(), JMSDestinationKeyMBean.class, var1));
         } else {
            var2 = new JMSDestinationKeyMBean[]{var1};
         }

         try {
            this.setDestinationKeys(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public void setDestinationKeys(JMSDestinationKeyMBean[] var1) throws InvalidAttributeValueException {
      Object var3 = var1 == null ? new JMSDestinationKeyMBeanImpl[0] : var1;
      JMSDestinationKeyMBean[] var2 = this._DestinationKeys;
      this._DestinationKeys = (JMSDestinationKeyMBean[])var3;
      this._postSet(7, var2, var3);
   }

   public boolean removeDestinationKey(JMSDestinationKeyMBean var1) {
      JMSDestinationKeyMBean[] var2 = this.getDestinationKeys();
      JMSDestinationKeyMBean[] var3 = (JMSDestinationKeyMBean[])((JMSDestinationKeyMBean[])this._getHelper()._removeElement(var2, JMSDestinationKeyMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setDestinationKeys(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public long getBytesMaximum() {
      return this._BytesMaximum;
   }

   public boolean isBytesMaximumSet() {
      return this._isSet(8);
   }

   public void setBytesMaximum(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("BytesMaximum", var1, -1L, Long.MAX_VALUE);
      long var3 = this._BytesMaximum;
      this._BytesMaximum = var1;
      this._postSet(8, var3, var1);
   }

   public long getBytesThresholdHigh() {
      return this._BytesThresholdHigh;
   }

   public boolean isBytesThresholdHighSet() {
      return this._isSet(9);
   }

   public void setBytesThresholdHigh(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("BytesThresholdHigh", var1, -1L, Long.MAX_VALUE);
      long var3 = this._BytesThresholdHigh;
      this._BytesThresholdHigh = var1;
      this._postSet(9, var3, var1);
   }

   public long getBytesThresholdLow() {
      return this._BytesThresholdLow;
   }

   public boolean isBytesThresholdLowSet() {
      return this._isSet(10);
   }

   public void setBytesThresholdLow(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("BytesThresholdLow", var1, -1L, Long.MAX_VALUE);
      long var3 = this._BytesThresholdLow;
      this._BytesThresholdLow = var1;
      this._postSet(10, var3, var1);
   }

   public long getMessagesMaximum() {
      return this._MessagesMaximum;
   }

   public boolean isMessagesMaximumSet() {
      return this._isSet(11);
   }

   public void setMessagesMaximum(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MessagesMaximum", var1, -1L, Long.MAX_VALUE);
      long var3 = this._MessagesMaximum;
      this._MessagesMaximum = var1;
      this._postSet(11, var3, var1);
   }

   public long getMessagesThresholdHigh() {
      return this._MessagesThresholdHigh;
   }

   public boolean isMessagesThresholdHighSet() {
      return this._isSet(12);
   }

   public void setMessagesThresholdHigh(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MessagesThresholdHigh", var1, -1L, Long.MAX_VALUE);
      long var3 = this._MessagesThresholdHigh;
      this._MessagesThresholdHigh = var1;
      this._postSet(12, var3, var1);
   }

   public long getMessagesThresholdLow() {
      return this._MessagesThresholdLow;
   }

   public boolean isMessagesThresholdLowSet() {
      return this._isSet(13);
   }

   public void setMessagesThresholdLow(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MessagesThresholdLow", var1, -1L, Long.MAX_VALUE);
      long var3 = this._MessagesThresholdLow;
      this._MessagesThresholdLow = var1;
      this._postSet(13, var3, var1);
   }

   public int getPriorityOverride() {
      return this._PriorityOverride;
   }

   public boolean isPriorityOverrideSet() {
      return this._isSet(14);
   }

   public void setPriorityOverride(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("PriorityOverride", (long)var1, -1L, 9L);
      int var2 = this._PriorityOverride;
      this._PriorityOverride = var1;
      this._postSet(14, var2, var1);
   }

   public String getTimeToDeliverOverride() {
      return this._TimeToDeliverOverride;
   }

   public boolean isTimeToDeliverOverrideSet() {
      return this._isSet(15);
   }

   public void setTimeToDeliverOverride(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      JMSModuleValidator.validateTimeToDeliverOverride(var1);
      String var2 = this._TimeToDeliverOverride;
      this._TimeToDeliverOverride = var1;
      this._postSet(15, var2, var1);
   }

   public long getRedeliveryDelayOverride() {
      return this._RedeliveryDelayOverride;
   }

   public boolean isRedeliveryDelayOverrideSet() {
      return this._isSet(16);
   }

   public void setRedeliveryDelayOverride(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RedeliveryDelayOverride", var1, -1L, Long.MAX_VALUE);
      long var3 = this._RedeliveryDelayOverride;
      this._RedeliveryDelayOverride = var1;
      this._postSet(16, var3, var1);
   }

   public void setErrorDestination(JMSDestinationMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      JMSDestinationMBean var2 = this._ErrorDestination;
      this._ErrorDestination = var1;
      this._postSet(17, var2, var1);
   }

   public JMSDestinationMBean getErrorDestination() {
      return this._ErrorDestination;
   }

   public String getErrorDestinationAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getErrorDestination();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isErrorDestinationSet() {
      return this._isSet(17);
   }

   public void setErrorDestinationAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JMSDestinationMBean.class, new ReferenceManager.Resolver(this, 17) {
            public void resolveReference(Object var1) {
               try {
                  JMSDestCommonMBeanImpl.this.setErrorDestination((JMSDestinationMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JMSDestinationMBean var2 = this._ErrorDestination;
         this._initializeProperty(17);
         this._postSet(17, var2, this._ErrorDestination);
      }

   }

   public int getRedeliveryLimit() {
      return this._RedeliveryLimit;
   }

   public boolean isRedeliveryLimitSet() {
      return this._isSet(18);
   }

   public void setRedeliveryLimit(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RedeliveryLimit", (long)var1, -1L, 2147483647L);
      int var2 = this._RedeliveryLimit;
      this._RedeliveryLimit = var1;
      this._postSet(18, var2, var1);
   }

   public long getTimeToLiveOverride() {
      return this._TimeToLiveOverride;
   }

   public boolean isTimeToLiveOverrideSet() {
      return this._isSet(19);
   }

   public void setTimeToLiveOverride(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("TimeToLiveOverride", var1, -1L, Long.MAX_VALUE);
      long var3 = this._TimeToLiveOverride;
      this._TimeToLiveOverride = var1;
      this._postSet(19, var3, var1);
   }

   public String getDeliveryModeOverride() {
      return this._DeliveryModeOverride;
   }

   public boolean isDeliveryModeOverrideSet() {
      return this._isSet(20);
   }

   public void setDeliveryModeOverride(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Persistent", "Non-Persistent", "No-Delivery"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("DeliveryModeOverride", var1, var2);
      String var3 = this._DeliveryModeOverride;
      this._DeliveryModeOverride = var1;
      this._postSet(20, var3, var1);
   }

   public void setExpirationPolicy(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Discard", "Log", "Redirect"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("ExpirationPolicy", var1, var2);
      String var3 = this._ExpirationPolicy;
      this._ExpirationPolicy = var1;
      this._postSet(21, var3, var1);
   }

   public String getExpirationPolicy() {
      return this._ExpirationPolicy;
   }

   public boolean isExpirationPolicySet() {
      return this._isSet(21);
   }

   public void setExpirationLoggingPolicy(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ExpirationLoggingPolicy;
      this._ExpirationLoggingPolicy = var1;
      this._postSet(22, var2, var1);
   }

   public String getExpirationLoggingPolicy() {
      return this._ExpirationLoggingPolicy;
   }

   public boolean isExpirationLoggingPolicySet() {
      return this._isSet(22);
   }

   public int getMaximumMessageSize() {
      return this._MaximumMessageSize;
   }

   public boolean isMaximumMessageSizeSet() {
      return this._isSet(23);
   }

   public void setMaximumMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaximumMessageSize", (long)var1, 0L, 2147483647L);
      int var2 = this._MaximumMessageSize;
      this._MaximumMessageSize = var1;
      this._postSet(23, var2, var1);
   }

   public long getCreationTime() {
      return this._CreationTime;
   }

   public boolean isCreationTimeSet() {
      return this._isSet(24);
   }

   public void setCreationTime(long var1) {
      long var3 = this._CreationTime;
      this._CreationTime = var1;
      this._postSet(24, var3, var1);
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
               this._BytesMaximum = -1L;
               if (var2) {
                  break;
               }
            case 9:
               this._BytesThresholdHigh = -1L;
               if (var2) {
                  break;
               }
            case 10:
               this._BytesThresholdLow = -1L;
               if (var2) {
                  break;
               }
            case 24:
               this._CreationTime = 1L;
               if (var2) {
                  break;
               }
            case 20:
               this._DeliveryModeOverride = "No-Delivery";
               if (var2) {
                  break;
               }
            case 7:
               this._DestinationKeys = new JMSDestinationKeyMBean[0];
               if (var2) {
                  break;
               }
            case 17:
               this._ErrorDestination = null;
               if (var2) {
                  break;
               }
            case 22:
               this._ExpirationLoggingPolicy = null;
               if (var2) {
                  break;
               }
            case 21:
               this._ExpirationPolicy = "Discard";
               if (var2) {
                  break;
               }
            case 23:
               this._MaximumMessageSize = Integer.MAX_VALUE;
               if (var2) {
                  break;
               }
            case 11:
               this._MessagesMaximum = -1L;
               if (var2) {
                  break;
               }
            case 12:
               this._MessagesThresholdHigh = -1L;
               if (var2) {
                  break;
               }
            case 13:
               this._MessagesThresholdLow = -1L;
               if (var2) {
                  break;
               }
            case 14:
               this._PriorityOverride = -1;
               if (var2) {
                  break;
               }
            case 16:
               this._RedeliveryDelayOverride = -1L;
               if (var2) {
                  break;
               }
            case 18:
               this._RedeliveryLimit = -1;
               if (var2) {
                  break;
               }
            case 15:
               this._TimeToDeliverOverride = "-1";
               if (var2) {
                  break;
               }
            case 19:
               this._TimeToLiveOverride = -1L;
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
      return "JMSDestCommon";
   }

   public void putValue(String var1, Object var2) {
      long var3;
      if (var1.equals("BytesMaximum")) {
         var3 = this._BytesMaximum;
         this._BytesMaximum = (Long)var2;
         this._postSet(8, var3, this._BytesMaximum);
      } else if (var1.equals("BytesThresholdHigh")) {
         var3 = this._BytesThresholdHigh;
         this._BytesThresholdHigh = (Long)var2;
         this._postSet(9, var3, this._BytesThresholdHigh);
      } else if (var1.equals("BytesThresholdLow")) {
         var3 = this._BytesThresholdLow;
         this._BytesThresholdLow = (Long)var2;
         this._postSet(10, var3, this._BytesThresholdLow);
      } else if (var1.equals("CreationTime")) {
         var3 = this._CreationTime;
         this._CreationTime = (Long)var2;
         this._postSet(24, var3, this._CreationTime);
      } else {
         String var5;
         if (var1.equals("DeliveryModeOverride")) {
            var5 = this._DeliveryModeOverride;
            this._DeliveryModeOverride = (String)var2;
            this._postSet(20, var5, this._DeliveryModeOverride);
         } else if (var1.equals("DestinationKeys")) {
            JMSDestinationKeyMBean[] var8 = this._DestinationKeys;
            this._DestinationKeys = (JMSDestinationKeyMBean[])((JMSDestinationKeyMBean[])var2);
            this._postSet(7, var8, this._DestinationKeys);
         } else if (var1.equals("ErrorDestination")) {
            JMSDestinationMBean var7 = this._ErrorDestination;
            this._ErrorDestination = (JMSDestinationMBean)var2;
            this._postSet(17, var7, this._ErrorDestination);
         } else if (var1.equals("ExpirationLoggingPolicy")) {
            var5 = this._ExpirationLoggingPolicy;
            this._ExpirationLoggingPolicy = (String)var2;
            this._postSet(22, var5, this._ExpirationLoggingPolicy);
         } else if (var1.equals("ExpirationPolicy")) {
            var5 = this._ExpirationPolicy;
            this._ExpirationPolicy = (String)var2;
            this._postSet(21, var5, this._ExpirationPolicy);
         } else {
            int var6;
            if (var1.equals("MaximumMessageSize")) {
               var6 = this._MaximumMessageSize;
               this._MaximumMessageSize = (Integer)var2;
               this._postSet(23, var6, this._MaximumMessageSize);
            } else if (var1.equals("MessagesMaximum")) {
               var3 = this._MessagesMaximum;
               this._MessagesMaximum = (Long)var2;
               this._postSet(11, var3, this._MessagesMaximum);
            } else if (var1.equals("MessagesThresholdHigh")) {
               var3 = this._MessagesThresholdHigh;
               this._MessagesThresholdHigh = (Long)var2;
               this._postSet(12, var3, this._MessagesThresholdHigh);
            } else if (var1.equals("MessagesThresholdLow")) {
               var3 = this._MessagesThresholdLow;
               this._MessagesThresholdLow = (Long)var2;
               this._postSet(13, var3, this._MessagesThresholdLow);
            } else if (var1.equals("PriorityOverride")) {
               var6 = this._PriorityOverride;
               this._PriorityOverride = (Integer)var2;
               this._postSet(14, var6, this._PriorityOverride);
            } else if (var1.equals("RedeliveryDelayOverride")) {
               var3 = this._RedeliveryDelayOverride;
               this._RedeliveryDelayOverride = (Long)var2;
               this._postSet(16, var3, this._RedeliveryDelayOverride);
            } else if (var1.equals("RedeliveryLimit")) {
               var6 = this._RedeliveryLimit;
               this._RedeliveryLimit = (Integer)var2;
               this._postSet(18, var6, this._RedeliveryLimit);
            } else if (var1.equals("TimeToDeliverOverride")) {
               var5 = this._TimeToDeliverOverride;
               this._TimeToDeliverOverride = (String)var2;
               this._postSet(15, var5, this._TimeToDeliverOverride);
            } else if (var1.equals("TimeToLiveOverride")) {
               var3 = this._TimeToLiveOverride;
               this._TimeToLiveOverride = (Long)var2;
               this._postSet(19, var3, this._TimeToLiveOverride);
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("BytesMaximum")) {
         return new Long(this._BytesMaximum);
      } else if (var1.equals("BytesThresholdHigh")) {
         return new Long(this._BytesThresholdHigh);
      } else if (var1.equals("BytesThresholdLow")) {
         return new Long(this._BytesThresholdLow);
      } else if (var1.equals("CreationTime")) {
         return new Long(this._CreationTime);
      } else if (var1.equals("DeliveryModeOverride")) {
         return this._DeliveryModeOverride;
      } else if (var1.equals("DestinationKeys")) {
         return this._DestinationKeys;
      } else if (var1.equals("ErrorDestination")) {
         return this._ErrorDestination;
      } else if (var1.equals("ExpirationLoggingPolicy")) {
         return this._ExpirationLoggingPolicy;
      } else if (var1.equals("ExpirationPolicy")) {
         return this._ExpirationPolicy;
      } else if (var1.equals("MaximumMessageSize")) {
         return new Integer(this._MaximumMessageSize);
      } else if (var1.equals("MessagesMaximum")) {
         return new Long(this._MessagesMaximum);
      } else if (var1.equals("MessagesThresholdHigh")) {
         return new Long(this._MessagesThresholdHigh);
      } else if (var1.equals("MessagesThresholdLow")) {
         return new Long(this._MessagesThresholdLow);
      } else if (var1.equals("PriorityOverride")) {
         return new Integer(this._PriorityOverride);
      } else if (var1.equals("RedeliveryDelayOverride")) {
         return new Long(this._RedeliveryDelayOverride);
      } else if (var1.equals("RedeliveryLimit")) {
         return new Integer(this._RedeliveryLimit);
      } else if (var1.equals("TimeToDeliverOverride")) {
         return this._TimeToDeliverOverride;
      } else {
         return var1.equals("TimeToLiveOverride") ? new Long(this._TimeToLiveOverride) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 13:
               if (var1.equals("bytes-maximum")) {
                  return 8;
               }

               if (var1.equals("creation-time")) {
                  return 24;
               }
            case 14:
            case 18:
            default:
               break;
            case 15:
               if (var1.equals("destination-key")) {
                  return 7;
               }
               break;
            case 16:
               if (var1.equals("messages-maximum")) {
                  return 11;
               }

               if (var1.equals("redelivery-limit")) {
                  return 18;
               }
               break;
            case 17:
               if (var1.equals("error-destination")) {
                  return 17;
               }

               if (var1.equals("expiration-policy")) {
                  return 21;
               }

               if (var1.equals("priority-override")) {
                  return 14;
               }
               break;
            case 19:
               if (var1.equals("bytes-threshold-low")) {
                  return 10;
               }
               break;
            case 20:
               if (var1.equals("bytes-threshold-high")) {
                  return 9;
               }

               if (var1.equals("maximum-message-size")) {
                  return 23;
               }
               break;
            case 21:
               if (var1.equals("time-to-live-override")) {
                  return 19;
               }
               break;
            case 22:
               if (var1.equals("delivery-mode-override")) {
                  return 20;
               }

               if (var1.equals("messages-threshold-low")) {
                  return 13;
               }
               break;
            case 23:
               if (var1.equals("messages-threshold-high")) {
                  return 12;
               }
               break;
            case 24:
               if (var1.equals("time-to-deliver-override")) {
                  return 15;
               }
               break;
            case 25:
               if (var1.equals("expiration-logging-policy")) {
                  return 22;
               }

               if (var1.equals("redelivery-delay-override")) {
                  return 16;
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
               return "destination-key";
            case 8:
               return "bytes-maximum";
            case 9:
               return "bytes-threshold-high";
            case 10:
               return "bytes-threshold-low";
            case 11:
               return "messages-maximum";
            case 12:
               return "messages-threshold-high";
            case 13:
               return "messages-threshold-low";
            case 14:
               return "priority-override";
            case 15:
               return "time-to-deliver-override";
            case 16:
               return "redelivery-delay-override";
            case 17:
               return "error-destination";
            case 18:
               return "redelivery-limit";
            case 19:
               return "time-to-live-override";
            case 20:
               return "delivery-mode-override";
            case 21:
               return "expiration-policy";
            case 22:
               return "expiration-logging-policy";
            case 23:
               return "maximum-message-size";
            case 24:
               return "creation-time";
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

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private JMSDestCommonMBeanImpl bean;

      protected Helper(JMSDestCommonMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "DestinationKeys";
            case 8:
               return "BytesMaximum";
            case 9:
               return "BytesThresholdHigh";
            case 10:
               return "BytesThresholdLow";
            case 11:
               return "MessagesMaximum";
            case 12:
               return "MessagesThresholdHigh";
            case 13:
               return "MessagesThresholdLow";
            case 14:
               return "PriorityOverride";
            case 15:
               return "TimeToDeliverOverride";
            case 16:
               return "RedeliveryDelayOverride";
            case 17:
               return "ErrorDestination";
            case 18:
               return "RedeliveryLimit";
            case 19:
               return "TimeToLiveOverride";
            case 20:
               return "DeliveryModeOverride";
            case 21:
               return "ExpirationPolicy";
            case 22:
               return "ExpirationLoggingPolicy";
            case 23:
               return "MaximumMessageSize";
            case 24:
               return "CreationTime";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("BytesMaximum")) {
            return 8;
         } else if (var1.equals("BytesThresholdHigh")) {
            return 9;
         } else if (var1.equals("BytesThresholdLow")) {
            return 10;
         } else if (var1.equals("CreationTime")) {
            return 24;
         } else if (var1.equals("DeliveryModeOverride")) {
            return 20;
         } else if (var1.equals("DestinationKeys")) {
            return 7;
         } else if (var1.equals("ErrorDestination")) {
            return 17;
         } else if (var1.equals("ExpirationLoggingPolicy")) {
            return 22;
         } else if (var1.equals("ExpirationPolicy")) {
            return 21;
         } else if (var1.equals("MaximumMessageSize")) {
            return 23;
         } else if (var1.equals("MessagesMaximum")) {
            return 11;
         } else if (var1.equals("MessagesThresholdHigh")) {
            return 12;
         } else if (var1.equals("MessagesThresholdLow")) {
            return 13;
         } else if (var1.equals("PriorityOverride")) {
            return 14;
         } else if (var1.equals("RedeliveryDelayOverride")) {
            return 16;
         } else if (var1.equals("RedeliveryLimit")) {
            return 18;
         } else if (var1.equals("TimeToDeliverOverride")) {
            return 15;
         } else {
            return var1.equals("TimeToLiveOverride") ? 19 : super.getPropertyIndex(var1);
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
            if (this.bean.isBytesMaximumSet()) {
               var2.append("BytesMaximum");
               var2.append(String.valueOf(this.bean.getBytesMaximum()));
            }

            if (this.bean.isBytesThresholdHighSet()) {
               var2.append("BytesThresholdHigh");
               var2.append(String.valueOf(this.bean.getBytesThresholdHigh()));
            }

            if (this.bean.isBytesThresholdLowSet()) {
               var2.append("BytesThresholdLow");
               var2.append(String.valueOf(this.bean.getBytesThresholdLow()));
            }

            if (this.bean.isCreationTimeSet()) {
               var2.append("CreationTime");
               var2.append(String.valueOf(this.bean.getCreationTime()));
            }

            if (this.bean.isDeliveryModeOverrideSet()) {
               var2.append("DeliveryModeOverride");
               var2.append(String.valueOf(this.bean.getDeliveryModeOverride()));
            }

            if (this.bean.isDestinationKeysSet()) {
               var2.append("DestinationKeys");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getDestinationKeys())));
            }

            if (this.bean.isErrorDestinationSet()) {
               var2.append("ErrorDestination");
               var2.append(String.valueOf(this.bean.getErrorDestination()));
            }

            if (this.bean.isExpirationLoggingPolicySet()) {
               var2.append("ExpirationLoggingPolicy");
               var2.append(String.valueOf(this.bean.getExpirationLoggingPolicy()));
            }

            if (this.bean.isExpirationPolicySet()) {
               var2.append("ExpirationPolicy");
               var2.append(String.valueOf(this.bean.getExpirationPolicy()));
            }

            if (this.bean.isMaximumMessageSizeSet()) {
               var2.append("MaximumMessageSize");
               var2.append(String.valueOf(this.bean.getMaximumMessageSize()));
            }

            if (this.bean.isMessagesMaximumSet()) {
               var2.append("MessagesMaximum");
               var2.append(String.valueOf(this.bean.getMessagesMaximum()));
            }

            if (this.bean.isMessagesThresholdHighSet()) {
               var2.append("MessagesThresholdHigh");
               var2.append(String.valueOf(this.bean.getMessagesThresholdHigh()));
            }

            if (this.bean.isMessagesThresholdLowSet()) {
               var2.append("MessagesThresholdLow");
               var2.append(String.valueOf(this.bean.getMessagesThresholdLow()));
            }

            if (this.bean.isPriorityOverrideSet()) {
               var2.append("PriorityOverride");
               var2.append(String.valueOf(this.bean.getPriorityOverride()));
            }

            if (this.bean.isRedeliveryDelayOverrideSet()) {
               var2.append("RedeliveryDelayOverride");
               var2.append(String.valueOf(this.bean.getRedeliveryDelayOverride()));
            }

            if (this.bean.isRedeliveryLimitSet()) {
               var2.append("RedeliveryLimit");
               var2.append(String.valueOf(this.bean.getRedeliveryLimit()));
            }

            if (this.bean.isTimeToDeliverOverrideSet()) {
               var2.append("TimeToDeliverOverride");
               var2.append(String.valueOf(this.bean.getTimeToDeliverOverride()));
            }

            if (this.bean.isTimeToLiveOverrideSet()) {
               var2.append("TimeToLiveOverride");
               var2.append(String.valueOf(this.bean.getTimeToLiveOverride()));
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
            JMSDestCommonMBeanImpl var2 = (JMSDestCommonMBeanImpl)var1;
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("BytesMaximum", this.bean.getBytesMaximum(), var2.getBytesMaximum(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("BytesThresholdHigh", this.bean.getBytesThresholdHigh(), var2.getBytesThresholdHigh(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("BytesThresholdLow", this.bean.getBytesThresholdLow(), var2.getBytesThresholdLow(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("CreationTime", this.bean.getCreationTime(), var2.getCreationTime(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("DeliveryModeOverride", this.bean.getDeliveryModeOverride(), var2.getDeliveryModeOverride(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("DestinationKeys", this.bean.getDestinationKeys(), var2.getDestinationKeys(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ErrorDestination", this.bean.getErrorDestination(), var2.getErrorDestination(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ExpirationLoggingPolicy", this.bean.getExpirationLoggingPolicy(), var2.getExpirationLoggingPolicy(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ExpirationPolicy", this.bean.getExpirationPolicy(), var2.getExpirationPolicy(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("MaximumMessageSize", this.bean.getMaximumMessageSize(), var2.getMaximumMessageSize(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("MessagesMaximum", this.bean.getMessagesMaximum(), var2.getMessagesMaximum(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("MessagesThresholdHigh", this.bean.getMessagesThresholdHigh(), var2.getMessagesThresholdHigh(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("MessagesThresholdLow", this.bean.getMessagesThresholdLow(), var2.getMessagesThresholdLow(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("PriorityOverride", this.bean.getPriorityOverride(), var2.getPriorityOverride(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("RedeliveryDelayOverride", this.bean.getRedeliveryDelayOverride(), var2.getRedeliveryDelayOverride(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("RedeliveryLimit", this.bean.getRedeliveryLimit(), var2.getRedeliveryLimit(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("TimeToDeliverOverride", this.bean.getTimeToDeliverOverride(), var2.getTimeToDeliverOverride(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("TimeToLiveOverride", this.bean.getTimeToLiveOverride(), var2.getTimeToLiveOverride(), true);
            }

         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JMSDestCommonMBeanImpl var3 = (JMSDestCommonMBeanImpl)var1.getSourceBean();
            JMSDestCommonMBeanImpl var4 = (JMSDestCommonMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("BytesMaximum")) {
                  var3.setBytesMaximum(var4.getBytesMaximum());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("BytesThresholdHigh")) {
                  var3.setBytesThresholdHigh(var4.getBytesThresholdHigh());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("BytesThresholdLow")) {
                  var3.setBytesThresholdLow(var4.getBytesThresholdLow());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("CreationTime")) {
                  var3.setCreationTime(var4.getCreationTime());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (var5.equals("DeliveryModeOverride")) {
                  var3.setDeliveryModeOverride(var4.getDeliveryModeOverride());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("DestinationKeys")) {
                  var3.setDestinationKeysAsString(var4.getDestinationKeysAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("ErrorDestination")) {
                  var3.setErrorDestinationAsString(var4.getErrorDestinationAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("ExpirationLoggingPolicy")) {
                  var3.setExpirationLoggingPolicy(var4.getExpirationLoggingPolicy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (var5.equals("ExpirationPolicy")) {
                  var3.setExpirationPolicy(var4.getExpirationPolicy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (var5.equals("MaximumMessageSize")) {
                  var3.setMaximumMessageSize(var4.getMaximumMessageSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 23);
               } else if (var5.equals("MessagesMaximum")) {
                  var3.setMessagesMaximum(var4.getMessagesMaximum());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("MessagesThresholdHigh")) {
                  var3.setMessagesThresholdHigh(var4.getMessagesThresholdHigh());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("MessagesThresholdLow")) {
                  var3.setMessagesThresholdLow(var4.getMessagesThresholdLow());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("PriorityOverride")) {
                  var3.setPriorityOverride(var4.getPriorityOverride());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("RedeliveryDelayOverride")) {
                  var3.setRedeliveryDelayOverride(var4.getRedeliveryDelayOverride());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("RedeliveryLimit")) {
                  var3.setRedeliveryLimit(var4.getRedeliveryLimit());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 18);
               } else if (var5.equals("TimeToDeliverOverride")) {
                  var3.setTimeToDeliverOverride(var4.getTimeToDeliverOverride());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("TimeToLiveOverride")) {
                  var3.setTimeToLiveOverride(var4.getTimeToLiveOverride());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
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
            JMSDestCommonMBeanImpl var5 = (JMSDestCommonMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if (var2 && (var3 == null || !var3.contains("BytesMaximum")) && this.bean.isBytesMaximumSet()) {
               var5.setBytesMaximum(this.bean.getBytesMaximum());
            }

            if (var2 && (var3 == null || !var3.contains("BytesThresholdHigh")) && this.bean.isBytesThresholdHighSet()) {
               var5.setBytesThresholdHigh(this.bean.getBytesThresholdHigh());
            }

            if (var2 && (var3 == null || !var3.contains("BytesThresholdLow")) && this.bean.isBytesThresholdLowSet()) {
               var5.setBytesThresholdLow(this.bean.getBytesThresholdLow());
            }

            if (var2 && (var3 == null || !var3.contains("CreationTime")) && this.bean.isCreationTimeSet()) {
               var5.setCreationTime(this.bean.getCreationTime());
            }

            if (var2 && (var3 == null || !var3.contains("DeliveryModeOverride")) && this.bean.isDeliveryModeOverrideSet()) {
               var5.setDeliveryModeOverride(this.bean.getDeliveryModeOverride());
            }

            if (var2 && (var3 == null || !var3.contains("DestinationKeys")) && this.bean.isDestinationKeysSet()) {
               var5._unSet(var5, 7);
               var5.setDestinationKeysAsString(this.bean.getDestinationKeysAsString());
            }

            if (var2 && (var3 == null || !var3.contains("ErrorDestination")) && this.bean.isErrorDestinationSet()) {
               var5._unSet(var5, 17);
               var5.setErrorDestinationAsString(this.bean.getErrorDestinationAsString());
            }

            if (var2 && (var3 == null || !var3.contains("ExpirationLoggingPolicy")) && this.bean.isExpirationLoggingPolicySet()) {
               var5.setExpirationLoggingPolicy(this.bean.getExpirationLoggingPolicy());
            }

            if (var2 && (var3 == null || !var3.contains("ExpirationPolicy")) && this.bean.isExpirationPolicySet()) {
               var5.setExpirationPolicy(this.bean.getExpirationPolicy());
            }

            if (var2 && (var3 == null || !var3.contains("MaximumMessageSize")) && this.bean.isMaximumMessageSizeSet()) {
               var5.setMaximumMessageSize(this.bean.getMaximumMessageSize());
            }

            if (var2 && (var3 == null || !var3.contains("MessagesMaximum")) && this.bean.isMessagesMaximumSet()) {
               var5.setMessagesMaximum(this.bean.getMessagesMaximum());
            }

            if (var2 && (var3 == null || !var3.contains("MessagesThresholdHigh")) && this.bean.isMessagesThresholdHighSet()) {
               var5.setMessagesThresholdHigh(this.bean.getMessagesThresholdHigh());
            }

            if (var2 && (var3 == null || !var3.contains("MessagesThresholdLow")) && this.bean.isMessagesThresholdLowSet()) {
               var5.setMessagesThresholdLow(this.bean.getMessagesThresholdLow());
            }

            if (var2 && (var3 == null || !var3.contains("PriorityOverride")) && this.bean.isPriorityOverrideSet()) {
               var5.setPriorityOverride(this.bean.getPriorityOverride());
            }

            if (var2 && (var3 == null || !var3.contains("RedeliveryDelayOverride")) && this.bean.isRedeliveryDelayOverrideSet()) {
               var5.setRedeliveryDelayOverride(this.bean.getRedeliveryDelayOverride());
            }

            if (var2 && (var3 == null || !var3.contains("RedeliveryLimit")) && this.bean.isRedeliveryLimitSet()) {
               var5.setRedeliveryLimit(this.bean.getRedeliveryLimit());
            }

            if (var2 && (var3 == null || !var3.contains("TimeToDeliverOverride")) && this.bean.isTimeToDeliverOverrideSet()) {
               var5.setTimeToDeliverOverride(this.bean.getTimeToDeliverOverride());
            }

            if (var2 && (var3 == null || !var3.contains("TimeToLiveOverride")) && this.bean.isTimeToLiveOverrideSet()) {
               var5.setTimeToLiveOverride(this.bean.getTimeToLiveOverride());
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
         this.inferSubTree(this.bean.getDestinationKeys(), var1, var2);
         this.inferSubTree(this.bean.getErrorDestination(), var1, var2);
      }
   }
}
