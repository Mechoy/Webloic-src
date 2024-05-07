package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.jms.module.validators.JMSModuleValidator;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.JMSTemplate;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class JMSTemplateMBeanImpl extends JMSDestCommonMBeanImpl implements JMSTemplateMBean, Serializable {
   private long _BytesMaximum;
   private boolean _BytesPagingEnabled;
   private long _BytesThresholdHigh;
   private long _BytesThresholdLow;
   private String _DeliveryModeOverride;
   private JMSDestinationMBean[] _Destinations;
   private JMSDestinationMBean _ErrorDestination;
   private String _ExpirationLoggingPolicy;
   private String _ExpirationPolicy;
   private int _MaximumMessageSize;
   private long _MessagesMaximum;
   private boolean _MessagesPagingEnabled;
   private long _MessagesThresholdHigh;
   private long _MessagesThresholdLow;
   private String _Name;
   private String _Notes;
   private int _PriorityOverride;
   private long _RedeliveryDelayOverride;
   private int _RedeliveryLimit;
   private String _TimeToDeliverOverride;
   private long _TimeToLiveOverride;
   private JMSTemplate _customizer;
   private static SchemaHelper2 _schemaHelper;

   public JMSTemplateMBeanImpl() {
      try {
         this._customizer = new JMSTemplate(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public JMSTemplateMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new JMSTemplate(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public JMSDestinationMBean[] getDestinations() {
      return this._Destinations;
   }

   public String getDestinationsAsString() {
      return this._getHelper()._serializeKeyList(this.getDestinations());
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

   public boolean isDestinationsSet() {
      return this._isSet(25);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setDestinationsAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._Destinations);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, JMSDestinationMBean.class, new ReferenceManager.Resolver(this, 25) {
                  public void resolveReference(Object var1) {
                     try {
                        JMSTemplateMBeanImpl.this.addDestination((JMSDestinationMBean)var1);
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
               JMSDestinationMBean[] var6 = this._Destinations;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  JMSDestinationMBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removeDestination(var9);
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
         JMSDestinationMBean[] var2 = this._Destinations;
         this._initializeProperty(25);
         this._postSet(25, var2, this._Destinations);
      }
   }

   public void setDestinations(JMSDestinationMBean[] var1) throws InvalidAttributeValueException {
      Object var3 = var1 == null ? new JMSDestinationMBeanImpl[0] : var1;
      JMSDestinationMBean[] var2 = this._Destinations;
      this._Destinations = (JMSDestinationMBean[])var3;
      this._postSet(25, var2, var3);
   }

   public boolean addDestination(JMSDestinationMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 25)) {
         JMSDestinationMBean[] var2;
         if (this._isSet(25)) {
            var2 = (JMSDestinationMBean[])((JMSDestinationMBean[])this._getHelper()._extendArray(this.getDestinations(), JMSDestinationMBean.class, var1));
         } else {
            var2 = new JMSDestinationMBean[]{var1};
         }

         try {
            this.setDestinations(var2);
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

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Name", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("Name", var1);
      ConfigurationValidator.validateName(var1);
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(2, var2, var1);
   }

   public String getNotes() {
      return this._customizer.getNotes();
   }

   public boolean isNotesSet() {
      return this._isSet(3);
   }

   public boolean removeDestination(JMSDestinationMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      JMSDestinationMBean[] var2 = this.getDestinations();
      JMSDestinationMBean[] var3 = (JMSDestinationMBean[])((JMSDestinationMBean[])this._getHelper()._removeElement(var2, JMSDestinationMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setDestinations(var3);
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

   public boolean isMessagesPagingEnabled() {
      return this._MessagesPagingEnabled;
   }

   public boolean isMessagesPagingEnabledSet() {
      return this._isSet(26);
   }

   public void setNotes(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getNotes();
      this._customizer.setNotes(var1);
      this._postSet(3, var2, var1);
   }

   public long getBytesMaximum() {
      return this._customizer.getBytesMaximum();
   }

   public boolean isBytesMaximumSet() {
      return this._isSet(8);
   }

   public void setMessagesPagingEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._MessagesPagingEnabled;
      this._MessagesPagingEnabled = var1;
      this._postSet(26, var2, var1);
   }

   public boolean isBytesPagingEnabled() {
      return this._BytesPagingEnabled;
   }

   public boolean isBytesPagingEnabledSet() {
      return this._isSet(27);
   }

   public void setBytesMaximum(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("BytesMaximum", var1, -1L, Long.MAX_VALUE);
      long var3 = this.getBytesMaximum();
      this._customizer.setBytesMaximum(var1);
      this._postSet(8, var3, var1);
   }

   public long getBytesThresholdHigh() {
      return this._customizer.getBytesThresholdHigh();
   }

   public boolean isBytesThresholdHighSet() {
      return this._isSet(9);
   }

   public void setBytesPagingEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._BytesPagingEnabled;
      this._BytesPagingEnabled = var1;
      this._postSet(27, var2, var1);
   }

   public void setBytesThresholdHigh(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("BytesThresholdHigh", var1, -1L, Long.MAX_VALUE);
      long var3 = this.getBytesThresholdHigh();
      this._customizer.setBytesThresholdHigh(var1);
      this._postSet(9, var3, var1);
   }

   public void useDelegates(DomainMBean var1, JMSBean var2, TemplateBean var3) {
      this._customizer.useDelegates(var1, var2, var3);
   }

   public long getBytesThresholdLow() {
      return this._customizer.getBytesThresholdLow();
   }

   public boolean isBytesThresholdLowSet() {
      return this._isSet(10);
   }

   public void setBytesThresholdLow(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("BytesThresholdLow", var1, -1L, Long.MAX_VALUE);
      long var3 = this.getBytesThresholdLow();
      this._customizer.setBytesThresholdLow(var1);
      this._postSet(10, var3, var1);
   }

   public long getMessagesMaximum() {
      return this._customizer.getMessagesMaximum();
   }

   public boolean isMessagesMaximumSet() {
      return this._isSet(11);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setMessagesMaximum(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MessagesMaximum", var1, -1L, Long.MAX_VALUE);
      long var3 = this.getMessagesMaximum();
      this._customizer.setMessagesMaximum(var1);
      this._postSet(11, var3, var1);
   }

   public long getMessagesThresholdHigh() {
      return this._customizer.getMessagesThresholdHigh();
   }

   public boolean isMessagesThresholdHighSet() {
      return this._isSet(12);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setMessagesThresholdHigh(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MessagesThresholdHigh", var1, -1L, Long.MAX_VALUE);
      long var3 = this.getMessagesThresholdHigh();
      this._customizer.setMessagesThresholdHigh(var1);
      this._postSet(12, var3, var1);
   }

   public long getMessagesThresholdLow() {
      return this._customizer.getMessagesThresholdLow();
   }

   public boolean isMessagesThresholdLowSet() {
      return this._isSet(13);
   }

   public void setMessagesThresholdLow(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MessagesThresholdLow", var1, -1L, Long.MAX_VALUE);
      long var3 = this.getMessagesThresholdLow();
      this._customizer.setMessagesThresholdLow(var1);
      this._postSet(13, var3, var1);
   }

   public int getPriorityOverride() {
      return this._customizer.getPriorityOverride();
   }

   public boolean isPriorityOverrideSet() {
      return this._isSet(14);
   }

   public void setPriorityOverride(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("PriorityOverride", (long)var1, -1L, 9L);
      int var2 = this.getPriorityOverride();
      this._customizer.setPriorityOverride(var1);
      this._postSet(14, var2, var1);
   }

   public String getTimeToDeliverOverride() {
      return this._customizer.getTimeToDeliverOverride();
   }

   public boolean isTimeToDeliverOverrideSet() {
      return this._isSet(15);
   }

   public void setTimeToDeliverOverride(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      JMSModuleValidator.validateTimeToDeliverOverride(var1);
      String var2 = this.getTimeToDeliverOverride();
      this._customizer.setTimeToDeliverOverride(var1);
      this._postSet(15, var2, var1);
   }

   public long getRedeliveryDelayOverride() {
      return this._customizer.getRedeliveryDelayOverride();
   }

   public boolean isRedeliveryDelayOverrideSet() {
      return this._isSet(16);
   }

   public void setRedeliveryDelayOverride(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RedeliveryDelayOverride", var1, -1L, Long.MAX_VALUE);
      long var3 = this.getRedeliveryDelayOverride();
      this._customizer.setRedeliveryDelayOverride(var1);
      this._postSet(16, var3, var1);
   }

   public void setErrorDestination(JMSDestinationMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      JMSDestinationMBean var2 = this.getErrorDestination();
      this._customizer.setErrorDestination(var1);
      this._postSet(17, var2, var1);
   }

   public JMSDestinationMBean getErrorDestination() {
      return this._customizer.getErrorDestination();
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
                  JMSTemplateMBeanImpl.this.setErrorDestination((JMSDestinationMBean)var1);
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
      return this._customizer.getRedeliveryLimit();
   }

   public boolean isRedeliveryLimitSet() {
      return this._isSet(18);
   }

   public void setRedeliveryLimit(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RedeliveryLimit", (long)var1, -1L, 2147483647L);
      int var2 = this.getRedeliveryLimit();
      this._customizer.setRedeliveryLimit(var1);
      this._postSet(18, var2, var1);
   }

   public long getTimeToLiveOverride() {
      return this._customizer.getTimeToLiveOverride();
   }

   public boolean isTimeToLiveOverrideSet() {
      return this._isSet(19);
   }

   public void setTimeToLiveOverride(long var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("TimeToLiveOverride", var1, -1L, Long.MAX_VALUE);
      long var3 = this.getTimeToLiveOverride();
      this._customizer.setTimeToLiveOverride(var1);
      this._postSet(19, var3, var1);
   }

   public String getDeliveryModeOverride() {
      return this._customizer.getDeliveryModeOverride();
   }

   public boolean isDeliveryModeOverrideSet() {
      return this._isSet(20);
   }

   public void setDeliveryModeOverride(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Persistent", "Non-Persistent", "No-Delivery"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("DeliveryModeOverride", var1, var2);
      String var3 = this.getDeliveryModeOverride();
      this._customizer.setDeliveryModeOverride(var1);
      this._postSet(20, var3, var1);
   }

   public void setExpirationPolicy(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Discard", "Log", "Redirect"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("ExpirationPolicy", var1, var2);
      String var3 = this.getExpirationPolicy();
      this._customizer.setExpirationPolicy(var1);
      this._postSet(21, var3, var1);
   }

   public String getExpirationPolicy() {
      return this._customizer.getExpirationPolicy();
   }

   public boolean isExpirationPolicySet() {
      return this._isSet(21);
   }

   public void setExpirationLoggingPolicy(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getExpirationLoggingPolicy();
      this._customizer.setExpirationLoggingPolicy(var1);
      this._postSet(22, var2, var1);
   }

   public String getExpirationLoggingPolicy() {
      return this._customizer.getExpirationLoggingPolicy();
   }

   public boolean isExpirationLoggingPolicySet() {
      return this._isSet(22);
   }

   public int getMaximumMessageSize() {
      return this._customizer.getMaximumMessageSize();
   }

   public boolean isMaximumMessageSizeSet() {
      return this._isSet(23);
   }

   public void setMaximumMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaximumMessageSize", (long)var1, 0L, 2147483647L);
      int var2 = this.getMaximumMessageSize();
      this._customizer.setMaximumMessageSize(var1);
      this._postSet(23, var2, var1);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
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
               this._customizer.setBytesMaximum(-1L);
               if (var2) {
                  break;
               }
            case 9:
               this._customizer.setBytesThresholdHigh(-1L);
               if (var2) {
                  break;
               }
            case 10:
               this._customizer.setBytesThresholdLow(-1L);
               if (var2) {
                  break;
               }
            case 20:
               this._customizer.setDeliveryModeOverride("No-Delivery");
               if (var2) {
                  break;
               }
            case 25:
               this._Destinations = new JMSDestinationMBean[0];
               if (var2) {
                  break;
               }
            case 17:
               this._customizer.setErrorDestination((JMSDestinationMBean)null);
               if (var2) {
                  break;
               }
            case 22:
               this._customizer.setExpirationLoggingPolicy((String)null);
               if (var2) {
                  break;
               }
            case 21:
               this._customizer.setExpirationPolicy("Discard");
               if (var2) {
                  break;
               }
            case 23:
               this._customizer.setMaximumMessageSize(Integer.MAX_VALUE);
               if (var2) {
                  break;
               }
            case 11:
               this._customizer.setMessagesMaximum(-1L);
               if (var2) {
                  break;
               }
            case 12:
               this._customizer.setMessagesThresholdHigh(-1L);
               if (var2) {
                  break;
               }
            case 13:
               this._customizer.setMessagesThresholdLow(-1L);
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 3:
               this._customizer.setNotes((String)null);
               if (var2) {
                  break;
               }
            case 14:
               this._customizer.setPriorityOverride(-1);
               if (var2) {
                  break;
               }
            case 16:
               this._customizer.setRedeliveryDelayOverride(-1L);
               if (var2) {
                  break;
               }
            case 18:
               this._customizer.setRedeliveryLimit(-1);
               if (var2) {
                  break;
               }
            case 15:
               this._customizer.setTimeToDeliverOverride("-1");
               if (var2) {
                  break;
               }
            case 19:
               this._customizer.setTimeToLiveOverride(-1L);
               if (var2) {
                  break;
               }
            case 27:
               this._BytesPagingEnabled = false;
               if (var2) {
                  break;
               }
            case 26:
               this._MessagesPagingEnabled = false;
               if (var2) {
                  break;
               }
            case 4:
            case 5:
            case 6:
            case 7:
            case 24:
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
      return "JMSTemplate";
   }

   public void putValue(String var1, Object var2) {
      long var5;
      if (var1.equals("BytesMaximum")) {
         var5 = this._BytesMaximum;
         this._BytesMaximum = (Long)var2;
         this._postSet(8, var5, this._BytesMaximum);
      } else {
         boolean var8;
         if (var1.equals("BytesPagingEnabled")) {
            var8 = this._BytesPagingEnabled;
            this._BytesPagingEnabled = (Boolean)var2;
            this._postSet(27, var8, this._BytesPagingEnabled);
         } else if (var1.equals("BytesThresholdHigh")) {
            var5 = this._BytesThresholdHigh;
            this._BytesThresholdHigh = (Long)var2;
            this._postSet(9, var5, this._BytesThresholdHigh);
         } else if (var1.equals("BytesThresholdLow")) {
            var5 = this._BytesThresholdLow;
            this._BytesThresholdLow = (Long)var2;
            this._postSet(10, var5, this._BytesThresholdLow);
         } else {
            String var6;
            if (var1.equals("DeliveryModeOverride")) {
               var6 = this._DeliveryModeOverride;
               this._DeliveryModeOverride = (String)var2;
               this._postSet(20, var6, this._DeliveryModeOverride);
            } else if (var1.equals("Destinations")) {
               JMSDestinationMBean[] var10 = this._Destinations;
               this._Destinations = (JMSDestinationMBean[])((JMSDestinationMBean[])var2);
               this._postSet(25, var10, this._Destinations);
            } else if (var1.equals("ErrorDestination")) {
               JMSDestinationMBean var9 = this._ErrorDestination;
               this._ErrorDestination = (JMSDestinationMBean)var2;
               this._postSet(17, var9, this._ErrorDestination);
            } else if (var1.equals("ExpirationLoggingPolicy")) {
               var6 = this._ExpirationLoggingPolicy;
               this._ExpirationLoggingPolicy = (String)var2;
               this._postSet(22, var6, this._ExpirationLoggingPolicy);
            } else if (var1.equals("ExpirationPolicy")) {
               var6 = this._ExpirationPolicy;
               this._ExpirationPolicy = (String)var2;
               this._postSet(21, var6, this._ExpirationPolicy);
            } else {
               int var7;
               if (var1.equals("MaximumMessageSize")) {
                  var7 = this._MaximumMessageSize;
                  this._MaximumMessageSize = (Integer)var2;
                  this._postSet(23, var7, this._MaximumMessageSize);
               } else if (var1.equals("MessagesMaximum")) {
                  var5 = this._MessagesMaximum;
                  this._MessagesMaximum = (Long)var2;
                  this._postSet(11, var5, this._MessagesMaximum);
               } else if (var1.equals("MessagesPagingEnabled")) {
                  var8 = this._MessagesPagingEnabled;
                  this._MessagesPagingEnabled = (Boolean)var2;
                  this._postSet(26, var8, this._MessagesPagingEnabled);
               } else if (var1.equals("MessagesThresholdHigh")) {
                  var5 = this._MessagesThresholdHigh;
                  this._MessagesThresholdHigh = (Long)var2;
                  this._postSet(12, var5, this._MessagesThresholdHigh);
               } else if (var1.equals("MessagesThresholdLow")) {
                  var5 = this._MessagesThresholdLow;
                  this._MessagesThresholdLow = (Long)var2;
                  this._postSet(13, var5, this._MessagesThresholdLow);
               } else if (var1.equals("Name")) {
                  var6 = this._Name;
                  this._Name = (String)var2;
                  this._postSet(2, var6, this._Name);
               } else if (var1.equals("Notes")) {
                  var6 = this._Notes;
                  this._Notes = (String)var2;
                  this._postSet(3, var6, this._Notes);
               } else if (var1.equals("PriorityOverride")) {
                  var7 = this._PriorityOverride;
                  this._PriorityOverride = (Integer)var2;
                  this._postSet(14, var7, this._PriorityOverride);
               } else if (var1.equals("RedeliveryDelayOverride")) {
                  var5 = this._RedeliveryDelayOverride;
                  this._RedeliveryDelayOverride = (Long)var2;
                  this._postSet(16, var5, this._RedeliveryDelayOverride);
               } else if (var1.equals("RedeliveryLimit")) {
                  var7 = this._RedeliveryLimit;
                  this._RedeliveryLimit = (Integer)var2;
                  this._postSet(18, var7, this._RedeliveryLimit);
               } else if (var1.equals("TimeToDeliverOverride")) {
                  var6 = this._TimeToDeliverOverride;
                  this._TimeToDeliverOverride = (String)var2;
                  this._postSet(15, var6, this._TimeToDeliverOverride);
               } else if (var1.equals("TimeToLiveOverride")) {
                  var5 = this._TimeToLiveOverride;
                  this._TimeToLiveOverride = (Long)var2;
                  this._postSet(19, var5, this._TimeToLiveOverride);
               } else if (var1.equals("customizer")) {
                  JMSTemplate var3 = this._customizer;
                  this._customizer = (JMSTemplate)var2;
               } else {
                  super.putValue(var1, var2);
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("BytesMaximum")) {
         return new Long(this._BytesMaximum);
      } else if (var1.equals("BytesPagingEnabled")) {
         return new Boolean(this._BytesPagingEnabled);
      } else if (var1.equals("BytesThresholdHigh")) {
         return new Long(this._BytesThresholdHigh);
      } else if (var1.equals("BytesThresholdLow")) {
         return new Long(this._BytesThresholdLow);
      } else if (var1.equals("DeliveryModeOverride")) {
         return this._DeliveryModeOverride;
      } else if (var1.equals("Destinations")) {
         return this._Destinations;
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
      } else if (var1.equals("MessagesPagingEnabled")) {
         return new Boolean(this._MessagesPagingEnabled);
      } else if (var1.equals("MessagesThresholdHigh")) {
         return new Long(this._MessagesThresholdHigh);
      } else if (var1.equals("MessagesThresholdLow")) {
         return new Long(this._MessagesThresholdLow);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("Notes")) {
         return this._Notes;
      } else if (var1.equals("PriorityOverride")) {
         return new Integer(this._PriorityOverride);
      } else if (var1.equals("RedeliveryDelayOverride")) {
         return new Long(this._RedeliveryDelayOverride);
      } else if (var1.equals("RedeliveryLimit")) {
         return new Integer(this._RedeliveryLimit);
      } else if (var1.equals("TimeToDeliverOverride")) {
         return this._TimeToDeliverOverride;
      } else if (var1.equals("TimeToLiveOverride")) {
         return new Long(this._TimeToLiveOverride);
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends JMSDestCommonMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
               break;
            case 5:
               if (var1.equals("notes")) {
                  return 3;
               }
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
            case 18:
            default:
               break;
            case 11:
               if (var1.equals("destination")) {
                  return 25;
               }
               break;
            case 13:
               if (var1.equals("bytes-maximum")) {
                  return 8;
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

               if (var1.equals("bytes-paging-enabled")) {
                  return 27;
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

               if (var1.equals("messages-paging-enabled")) {
                  return 26;
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
            case 2:
               return "name";
            case 3:
               return "notes";
            case 4:
            case 5:
            case 6:
            case 7:
            case 24:
            default:
               return super.getElementName(var1);
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
            case 25:
               return "destination";
            case 26:
               return "messages-paging-enabled";
            case 27:
               return "bytes-paging-enabled";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 25:
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

   protected static class Helper extends JMSDestCommonMBeanImpl.Helper {
      private JMSTemplateMBeanImpl bean;

      protected Helper(JMSTemplateMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
               return "Notes";
            case 4:
            case 5:
            case 6:
            case 7:
            case 24:
            default:
               return super.getPropertyName(var1);
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
            case 25:
               return "Destinations";
            case 26:
               return "MessagesPagingEnabled";
            case 27:
               return "BytesPagingEnabled";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("BytesMaximum")) {
            return 8;
         } else if (var1.equals("BytesThresholdHigh")) {
            return 9;
         } else if (var1.equals("BytesThresholdLow")) {
            return 10;
         } else if (var1.equals("DeliveryModeOverride")) {
            return 20;
         } else if (var1.equals("Destinations")) {
            return 25;
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
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("Notes")) {
            return 3;
         } else if (var1.equals("PriorityOverride")) {
            return 14;
         } else if (var1.equals("RedeliveryDelayOverride")) {
            return 16;
         } else if (var1.equals("RedeliveryLimit")) {
            return 18;
         } else if (var1.equals("TimeToDeliverOverride")) {
            return 15;
         } else if (var1.equals("TimeToLiveOverride")) {
            return 19;
         } else if (var1.equals("BytesPagingEnabled")) {
            return 27;
         } else {
            return var1.equals("MessagesPagingEnabled") ? 26 : super.getPropertyIndex(var1);
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

            if (this.bean.isDeliveryModeOverrideSet()) {
               var2.append("DeliveryModeOverride");
               var2.append(String.valueOf(this.bean.getDeliveryModeOverride()));
            }

            if (this.bean.isDestinationsSet()) {
               var2.append("Destinations");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getDestinations())));
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

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isNotesSet()) {
               var2.append("Notes");
               var2.append(String.valueOf(this.bean.getNotes()));
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

            if (this.bean.isBytesPagingEnabledSet()) {
               var2.append("BytesPagingEnabled");
               var2.append(String.valueOf(this.bean.isBytesPagingEnabled()));
            }

            if (this.bean.isMessagesPagingEnabledSet()) {
               var2.append("MessagesPagingEnabled");
               var2.append(String.valueOf(this.bean.isMessagesPagingEnabled()));
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
            JMSTemplateMBeanImpl var2 = (JMSTemplateMBeanImpl)var1;
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
               this.computeDiff("DeliveryModeOverride", this.bean.getDeliveryModeOverride(), var2.getDeliveryModeOverride(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("Destinations", this.bean.getDestinations(), var2.getDestinations(), false);
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

            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("Notes", this.bean.getNotes(), var2.getNotes(), true);
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

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("BytesPagingEnabled", this.bean.isBytesPagingEnabled(), var2.isBytesPagingEnabled(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("MessagesPagingEnabled", this.bean.isMessagesPagingEnabled(), var2.isMessagesPagingEnabled(), false);
            }

         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JMSTemplateMBeanImpl var3 = (JMSTemplateMBeanImpl)var1.getSourceBean();
            JMSTemplateMBeanImpl var4 = (JMSTemplateMBeanImpl)var1.getProposedBean();
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
               } else if (var5.equals("DeliveryModeOverride")) {
                  var3.setDeliveryModeOverride(var4.getDeliveryModeOverride());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("Destinations")) {
                  var3.setDestinationsAsString(var4.getDestinationsAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 25);
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
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("Notes")) {
                  var3.setNotes(var4.getNotes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 3);
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
               } else if (var5.equals("BytesPagingEnabled")) {
                  var3.setBytesPagingEnabled(var4.isBytesPagingEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 27);
               } else if (var5.equals("MessagesPagingEnabled")) {
                  var3.setMessagesPagingEnabled(var4.isMessagesPagingEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 26);
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
            JMSTemplateMBeanImpl var5 = (JMSTemplateMBeanImpl)var1;
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

            if (var2 && (var3 == null || !var3.contains("DeliveryModeOverride")) && this.bean.isDeliveryModeOverrideSet()) {
               var5.setDeliveryModeOverride(this.bean.getDeliveryModeOverride());
            }

            if (var2 && (var3 == null || !var3.contains("Destinations")) && this.bean.isDestinationsSet()) {
               var5._unSet(var5, 25);
               var5.setDestinationsAsString(this.bean.getDestinationsAsString());
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

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("Notes")) && this.bean.isNotesSet()) {
               var5.setNotes(this.bean.getNotes());
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

            if (var2 && (var3 == null || !var3.contains("BytesPagingEnabled")) && this.bean.isBytesPagingEnabledSet()) {
               var5.setBytesPagingEnabled(this.bean.isBytesPagingEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("MessagesPagingEnabled")) && this.bean.isMessagesPagingEnabledSet()) {
               var5.setMessagesPagingEnabled(this.bean.isMessagesPagingEnabled());
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
         this.inferSubTree(this.bean.getDestinations(), var1, var2);
         this.inferSubTree(this.bean.getErrorDestination(), var1, var2);
      }
   }
}
