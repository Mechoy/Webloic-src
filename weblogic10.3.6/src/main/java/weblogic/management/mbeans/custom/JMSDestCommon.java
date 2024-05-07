package weblogic.management.mbeans.custom;

import javax.management.InvalidAttributeValueException;
import weblogic.j2ee.descriptor.wl.DestinationBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.QuotaBean;
import weblogic.jms.JMSLogger;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.module.MBeanConverter;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSDestinationKeyMBean;
import weblogic.management.configuration.JMSDestinationMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public abstract class JMSDestCommon extends ConfigurationMBeanCustomizer {
   protected transient DomainMBean domain;
   protected transient JMSBean interopModule;
   private transient DestinationBean delegate;
   private static final String BYTES_MAXIMUM = "BytesMaximum";
   private static final String MESSAGES_MAXIMUM = "MessagesMaximum";

   public JMSDestCommon(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void useDelegates(DomainMBean var1, JMSBean var2, DestinationBean var3) {
      this.domain = var1;
      this.interopModule = var2;
      this.delegate = var3;
   }

   public long getBytesMaximum() {
      if (this.delegate != null) {
         QuotaBean var2 = this.delegate.getQuota();
         if (var2 != null && this.delegate.isSet("Quota")) {
            return var2.isSet("BytesMaximum") ? var2.getBytesMaximum() : -1L;
         } else {
            return -1L;
         }
      } else {
         Object var1 = this.getValue("BytesMaximum");
         return var1 != null && var1 instanceof Long ? (Long)var1 : -1L;
      }
   }

   public void setBytesMaximum(long var1) throws InvalidAttributeValueException {
      this.putValue("BytesMaximum", new Long(var1));
      if (this.delegate != null) {
         String var3 = this.delegate.getName();
         String var4 = MBeanConverter.constructQuotaNameFromDestinationName(var3);
         QuotaBean var5 = null;

         try {
            var5 = this.interopModule.lookupQuota(var4);
            if (var5 == null) {
               var5 = this.interopModule.createQuota(var4);
            }
         } finally {
            if (var5 == null) {
               JMSLogger.logBytesMaximumNoEffect(var3, var1);
            } else {
               if (var1 >= 0L) {
                  var5.setBytesMaximum(var1);
               } else {
                  var5.unSet("BytesMaximum");
               }

               if (!var5.isSet("BytesMaximum") && !var5.isSet("MessagesMaximum")) {
                  this.delegate.unSet("Quota");
               } else {
                  this.delegate.setQuota(var5);
               }
            }

         }

      }
   }

   public long getMessagesMaximum() {
      if (this.delegate != null) {
         QuotaBean var2 = this.delegate.getQuota();
         if (var2 != null && this.delegate.isSet("Quota")) {
            return var2.isSet("MessagesMaximum") ? var2.getMessagesMaximum() : -1L;
         } else {
            return -1L;
         }
      } else {
         Object var1 = this.getValue("MessagesMaximum");
         return var1 != null && var1 instanceof Long ? (Long)var1 : -1L;
      }
   }

   public void setMessagesMaximum(long var1) throws InvalidAttributeValueException {
      this.putValue("MessagesMaximum", new Long(var1));
      if (this.delegate != null) {
         String var3 = this.delegate.getName();
         String var4 = MBeanConverter.constructQuotaNameFromDestinationName(var3);
         QuotaBean var5 = null;

         try {
            var5 = this.interopModule.lookupQuota(var4);
            if (var5 == null) {
               var5 = this.interopModule.createQuota(var4);
            }
         } finally {
            if (var5 == null) {
               JMSLogger.logMessagesMaximumNoEffect(var3, var1);
            } else {
               if (var1 >= 0L) {
                  var5.setMessagesMaximum(var1);
               } else {
                  var5.unSet("MessagesMaximum");
               }

               if (!var5.isSet("BytesMaximum") && !var5.isSet("MessagesMaximum")) {
                  this.delegate.unSet("Quota");
               } else {
                  this.delegate.setQuota(var5);
               }
            }

         }

      }
   }

   public JMSDestinationMBean getErrorDestination() {
      if (this.delegate == null) {
         Object var2 = this.getValue("ErrorDestination");
         return var2 != null && var2 instanceof JMSDestinationMBean ? (JMSDestinationMBean)var2 : null;
      } else {
         DestinationBean var1 = this.delegate.getDeliveryFailureParams().getErrorDestination();
         return var1 == null ? null : MBeanConverter.findErrorQueue(this.domain, var1.getName());
      }
   }

   public void setErrorDestination(JMSDestinationMBean var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("ErrorDestination", var1);
      } else {
         String var2 = var1 == null ? null : var1.getName();
         DestinationBean var3 = JMSBeanHelper.findDestinationBean(var2, this.interopModule);
         this.delegate.getDeliveryFailureParams().setErrorDestination(var3);
      }

   }

   public JMSDestinationKeyMBean[] getDestinationKeys() {
      if (this.delegate == null) {
         Object var4 = this.getValue("DestinationKeys");
         return var4 != null && var4 instanceof JMSDestinationKeyMBean[] ? (JMSDestinationKeyMBean[])((JMSDestinationKeyMBean[])var4) : null;
      } else {
         String[] var1 = this.delegate.getDestinationKeys();
         if (var1 == null) {
            return null;
         } else {
            JMSDestinationKeyMBean[] var2 = new JMSDestinationKeyMBean[var1.length];

            for(int var3 = 0; var3 < var1.length; ++var3) {
               var2[var3] = this.domain.lookupJMSDestinationKey(var1[var3]);
            }

            return var2;
         }
      }
   }

   public void setDestinationKeys(JMSDestinationKeyMBean[] var1) {
      if (this.delegate == null) {
         this.putValue("DestinationKeys", var1);
      } else if (var1 != null) {
         String[] var2 = new String[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = var1[var3].getName();
         }

         this.delegate.setDestinationKeys(var2);
      }

   }

   public long getBytesThresholdHigh() {
      if (this.delegate != null && this.delegate.getThresholds().isSet("BytesHigh")) {
         long var5 = this.delegate.getThresholds().getBytesHigh();
         long var3 = (Long)this.getValue("BytesThresholdHigh");
         return var5 == Long.MAX_VALUE && var3 == -1L ? var3 : var5;
      } else {
         Object var1 = this.getValue("BytesThresholdHigh");
         return var1 != null && var1 instanceof Long ? (Long)var1 : -1L;
      }
   }

   public void setBytesThresholdHigh(long var1) throws InvalidAttributeValueException {
      this.putValue("BytesThresholdHigh", new Long(var1));
      if (this.delegate != null) {
         if (var1 == -1L) {
            var1 = Long.MAX_VALUE;
         }

         this.delegate.getThresholds().setBytesHigh(var1);
      }

   }

   public long getBytesThresholdLow() {
      if (this.delegate != null && this.delegate.getThresholds().isSet("BytesLow")) {
         long var5 = this.delegate.getThresholds().getBytesLow();
         long var3 = (Long)this.getValue("BytesThresholdLow");
         return var5 == Long.MAX_VALUE && var3 == -1L ? var3 : var5;
      } else {
         Object var1 = this.getValue("BytesThresholdLow");
         return var1 != null && var1 instanceof Long ? (Long)var1 : -1L;
      }
   }

   public void setBytesThresholdLow(long var1) throws InvalidAttributeValueException {
      this.putValue("BytesThresholdLow", new Long(var1));
      if (this.delegate != null) {
         if (var1 == -1L) {
            var1 = Long.MAX_VALUE;
         }

         this.delegate.getThresholds().setBytesLow(var1);
      }

   }

   public long getMessagesThresholdHigh() {
      if (this.delegate != null && this.delegate.getThresholds().isSet("MessagesHigh")) {
         long var5 = this.delegate.getThresholds().getMessagesHigh();
         long var3 = (Long)this.getValue("MessagesThresholdHigh");
         return var5 == Long.MAX_VALUE && var3 == -1L ? var3 : var5;
      } else {
         Object var1 = this.getValue("MessagesThresholdHigh");
         return var1 != null && var1 instanceof Long ? (Long)var1 : -1L;
      }
   }

   public void setMessagesThresholdHigh(long var1) throws InvalidAttributeValueException {
      this.putValue("MessagesThresholdHigh", new Long(var1));
      if (this.delegate != null) {
         if (var1 == -1L) {
            var1 = Long.MAX_VALUE;
         }

         this.delegate.getThresholds().setMessagesHigh(var1);
      }

   }

   public long getMessagesThresholdLow() {
      if (this.delegate != null && this.delegate.getThresholds().isSet("MessagesLow")) {
         long var5 = this.delegate.getThresholds().getMessagesLow();
         long var3 = (Long)this.getValue("MessagesThresholdLow");
         return var5 == Long.MAX_VALUE && var3 == -1L ? var3 : var5;
      } else {
         Object var1 = this.getValue("MessagesThresholdLow");
         return var1 != null && var1 instanceof Long ? (Long)var1 : -1L;
      }
   }

   public void setMessagesThresholdLow(long var1) throws InvalidAttributeValueException {
      this.putValue("MessagesThresholdLow", new Long(var1));
      if (this.delegate != null) {
         if (var1 == -1L) {
            var1 = Long.MAX_VALUE;
         }

         this.delegate.getThresholds().setMessagesLow(var1);
      }

   }

   public int getPriorityOverride() {
      if (this.delegate != null && this.delegate.getDeliveryParamsOverrides().isSet("Priority")) {
         return this.delegate.getDeliveryParamsOverrides().getPriority();
      } else {
         Object var1 = this.getValue("PriorityOverride");
         return var1 != null && var1 instanceof Integer ? (Integer)var1 : -1;
      }
   }

   public void setPriorityOverride(int var1) throws InvalidAttributeValueException {
      this.putValue("PriorityOverride", new Integer(var1));
      if (this.delegate != null) {
         this.delegate.getDeliveryParamsOverrides().setPriority(var1);
      }

   }

   public String getTimeToDeliverOverride() {
      if (this.delegate != null && this.delegate.getDeliveryParamsOverrides().isSet("TimeToDeliver")) {
         return this.delegate.getDeliveryParamsOverrides().getTimeToDeliver();
      } else {
         Object var1 = this.getValue("TimeToDeliverOverride");
         return var1 != null && var1 instanceof String ? (String)var1 : "-1";
      }
   }

   public void setTimeToDeliverOverride(String var1) throws InvalidAttributeValueException {
      this.putValue("TimeToDeliverOverride", var1);
      if (this.delegate != null) {
         this.delegate.getDeliveryParamsOverrides().setTimeToDeliver(var1);
      }

   }

   public long getRedeliveryDelayOverride() {
      if (this.delegate != null && this.delegate.getDeliveryParamsOverrides().isSet("RedeliveryDelay")) {
         return this.delegate.getDeliveryParamsOverrides().getRedeliveryDelay();
      } else {
         Object var1 = this.getValue("RedeliveryDelayOverride");
         return var1 != null && var1 instanceof Long ? (Long)var1 : -1L;
      }
   }

   public void setRedeliveryDelayOverride(long var1) throws InvalidAttributeValueException {
      this.putValue("RedeliveryDelayOverride", new Long(var1));
      if (this.delegate != null) {
         this.delegate.getDeliveryParamsOverrides().setRedeliveryDelay(var1);
      }

   }

   public int getRedeliveryLimit() {
      if (this.delegate != null && this.delegate.getDeliveryFailureParams().isSet("RedeliveryLimit")) {
         return this.delegate.getDeliveryFailureParams().getRedeliveryLimit();
      } else {
         Object var1 = this.getValue("RedeliveryLimit");
         return var1 != null && var1 instanceof Integer ? (Integer)var1 : -1;
      }
   }

   public void setRedeliveryLimit(int var1) throws InvalidAttributeValueException {
      this.putValue("RedeliveryLimit", new Integer(var1));
      if (this.delegate != null) {
         this.delegate.getDeliveryFailureParams().setRedeliveryLimit(var1);
      }

   }

   public long getTimeToLiveOverride() {
      if (this.delegate != null && this.delegate.getDeliveryParamsOverrides().isSet("TimeToLive")) {
         return this.delegate.getDeliveryParamsOverrides().getTimeToLive();
      } else {
         Object var1 = this.getValue("TimeToLiveOverride");
         return var1 != null && var1 instanceof Long ? (Long)var1 : -1L;
      }
   }

   public void setTimeToLiveOverride(long var1) throws InvalidAttributeValueException {
      this.putValue("TimeToLiveOverride", new Long(var1));
      if (this.delegate != null) {
         this.delegate.getDeliveryParamsOverrides().setTimeToLive(var1);
      }

   }

   public String getDeliveryModeOverride() {
      if (this.delegate != null && this.delegate.getDeliveryParamsOverrides().isSet("DeliveryMode")) {
         return this.delegate.getDeliveryParamsOverrides().getDeliveryMode();
      } else {
         Object var1 = this.getValue("DeliveryModeOverride");
         return var1 != null && var1 instanceof String ? (String)var1 : "No-Delivery";
      }
   }

   public void setDeliveryModeOverride(String var1) throws InvalidAttributeValueException {
      this.putValue("DeliveryModeOverride", var1);
      if (this.delegate != null) {
         this.delegate.getDeliveryParamsOverrides().setDeliveryMode(var1);
      }

   }

   public String getExpirationPolicy() {
      if (this.delegate != null && this.delegate.getDeliveryFailureParams().isSet("ExpirationPolicy")) {
         return this.delegate.getDeliveryFailureParams().getExpirationPolicy();
      } else {
         Object var1 = this.getValue("ExpirationPolicy");
         return var1 != null && var1 instanceof String ? (String)var1 : "Discard";
      }
   }

   public void setExpirationPolicy(String var1) throws InvalidAttributeValueException {
      this.putValue("ExpirationPolicy", var1);
      if (this.delegate != null) {
         this.delegate.getDeliveryFailureParams().setExpirationPolicy(var1);
      }

   }

   public String getExpirationLoggingPolicy() {
      if (this.delegate != null && this.delegate.getDeliveryFailureParams().isSet("ExpirationLoggingPolicy")) {
         return this.delegate.getDeliveryFailureParams().getExpirationLoggingPolicy();
      } else {
         Object var1 = this.getValue("ExpirationLoggingPolicy");
         return var1 != null && var1 instanceof String ? (String)var1 : null;
      }
   }

   public void setExpirationLoggingPolicy(String var1) throws InvalidAttributeValueException {
      this.putValue("ExpirationLoggingPolicy", var1);
      if (this.delegate != null) {
         this.delegate.getDeliveryFailureParams().setExpirationLoggingPolicy(var1);
      }

   }

   public int getMaximumMessageSize() {
      if (this.delegate != null && this.delegate.isSet("MaximumMessageSize")) {
         return this.delegate.getMaximumMessageSize();
      } else {
         Object var1 = this.getValue("MaximumMessageSize");
         return var1 != null && var1 instanceof Integer ? (Integer)var1 : Integer.MAX_VALUE;
      }
   }

   public void setMaximumMessageSize(int var1) throws InvalidAttributeValueException {
      this.putValue("MaximumMessageSize", new Integer(var1));
      if (this.delegate != null) {
         this.delegate.setMaximumMessageSize(var1);
      }

   }

   public String getNotes() {
      if (this.delegate != null && this.delegate.isSet("Notes")) {
         return this.delegate.getNotes();
      } else {
         Object var1 = this.getValue("Notes");
         return var1 != null && var1 instanceof String ? (String)var1 : null;
      }
   }

   public void setNotes(String var1) throws InvalidAttributeValueException {
      this.putValue("Notes", var1);
      if (this.delegate != null) {
         this.delegate.setNotes(var1);
      }

   }

   public void _preDestroy() {
      WebLogicMBean var1 = this.getMbean().getParent();
      if (var1 instanceof JMSServerMBean) {
         JMSServerMBean var2 = (JMSServerMBean)var1;
         DomainMBean var3 = (DomainMBean)var2.getParent();
         JMSServerMBean[] var4 = var3.getJMSServers();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            JMSDestinationMBean[] var6 = var4[var5].getDestinations();

            for(int var7 = 0; var7 < var6.length; ++var7) {
               JMSDestinationMBean var8 = var6[var7].getErrorDestination();
               if (var8 != null) {
                  String var9 = var8.getName();
                  String var10 = var8.getType();
                  if (var9.equals(this.getMbean().getName()) && var10.equals(this.getMbean().getType())) {
                     String var11 = "Destination " + this.getMbean().getName() + " cannot be removed because it is the" + " ErrorDestinatin of " + var9;
                     throw new RuntimeException(var11);
                  }
               }
            }
         }
      }

   }
}
