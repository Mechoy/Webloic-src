package weblogic.management.mbeans.custom;

import weblogic.j2ee.descriptor.wl.DestinationBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.QuotaBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.jms.JMSLogger;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.module.MBeanConverter;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSDestinationMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public final class JMSTemplate extends ConfigurationMBeanCustomizer {
   private DomainMBean domain;
   private TemplateBean delegate;
   private JMSBean interopBean;
   private static final String BYTES_MAXIMUM = "BytesMaximum";
   private static final String MESSAGES_MAXIMUM = "MessagesMaximum";

   public JMSTemplate(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void useDelegates(DomainMBean var1, JMSBean var2, TemplateBean var3) {
      this.domain = var1;
      this.interopBean = var2;
      this.delegate = var3;
   }

   public long getBytesMaximum() {
      if (this.delegate == null) {
         Object var2 = this.getValue("BytesMaximum");
         return var2 != null && var2 instanceof Long ? (Long)var2 : -1L;
      } else {
         QuotaBean var1 = this.delegate.getQuota();
         if (var1 == null) {
            return -1L;
         } else {
            return var1.isSet("BytesMaximum") ? var1.getBytesMaximum() : -1L;
         }
      }
   }

   public void setBytesMaximum(long var1) {
      if (this.delegate == null) {
         this.putValue("BytesMaximum", new Long(var1));
      } else {
         if (var1 < 0L) {
            var1 = Long.MAX_VALUE;
         }

         String var5 = this.delegate.getName();
         String var6 = MBeanConverter.constructQuotaNameFromDestinationName(var5);
         QuotaBean var7 = null;

         try {
            var7 = this.interopBean.lookupQuota(var6);
            if (var7 == null) {
               var7 = this.interopBean.createQuota(var6);
            }
         } finally {
            if (var7 == null) {
               JMSLogger.logTemplateBytesMaximumNoEffect(this.delegate.getName(), var1);
            } else {
               if (var1 >= 0L) {
                  var7.setBytesMaximum(var1);
               } else {
                  var7.unSet("BytesMaximum");
               }

               if (!var7.isSet("BytesMaximum") && !var7.isSet("MessagesMaximum")) {
                  this.delegate.unSet("Quota");
               } else {
                  this.delegate.setQuota(var7);
               }
            }

         }

      }
   }

   public long getMessagesMaximum() {
      if (this.delegate == null) {
         Object var2 = this.getValue("MessagesMaximum");
         return var2 != null && var2 instanceof Long ? (Long)var2 : -1L;
      } else {
         QuotaBean var1 = this.delegate.getQuota();
         if (var1 == null) {
            return -1L;
         } else {
            return var1.isSet("MessagesMaximum") ? var1.getMessagesMaximum() : -1L;
         }
      }
   }

   public void setMessagesMaximum(long var1) {
      if (this.delegate == null) {
         this.putValue("MessagesMaximum", new Long(var1));
      } else {
         if (var1 < 0L) {
            var1 = Long.MAX_VALUE;
         }

         String var5 = this.delegate.getName();
         String var6 = MBeanConverter.constructQuotaNameFromDestinationName(var5);
         QuotaBean var7 = null;

         try {
            var7 = this.interopBean.lookupQuota(var6);
            if (var7 == null) {
               var7 = this.interopBean.createQuota(var6);
            }
         } finally {
            if (var7 == null) {
               JMSLogger.logTemplateMessagesMaximumNoEffect(this.delegate.getName(), var1);
            } else {
               if (var1 >= 0L) {
                  var7.setMessagesMaximum(var1);
               } else {
                  var7.unSet("MessagesMaximum");
               }

               if (!var7.isSet("BytesMaximum") && !var7.isSet("MessagesMaximum")) {
                  this.delegate.unSet("Quota");
               } else {
                  this.delegate.setQuota(var7);
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

   public void setErrorDestination(JMSDestinationMBean var1) {
      if (this.delegate == null) {
         this.putValue("ErrorDestination", var1);
      } else {
         String var2 = var1 == null ? null : var1.getName();
         DestinationBean var3 = JMSBeanHelper.findDestinationBean(var2, this.interopBean);
         this.delegate.getDeliveryFailureParams().setErrorDestination(var3);
      }

   }

   public long getBytesThresholdHigh() {
      if (this.delegate == null) {
         Object var1 = this.getValue("BytesThresholdHigh");
         return var1 != null && var1 instanceof Long ? (Long)var1 : -1L;
      } else {
         return this.delegate.getThresholds().getBytesHigh();
      }
   }

   public void setBytesThresholdHigh(long var1) {
      if (this.delegate == null) {
         this.putValue("BytesThresholdHigh", new Long(var1));
      } else {
         this.delegate.getThresholds().setBytesHigh(var1);
      }

   }

   public long getBytesThresholdLow() {
      if (this.delegate == null) {
         Object var1 = this.getValue("BytesThresholdLow");
         return var1 != null && var1 instanceof Long ? (Long)var1 : -1L;
      } else {
         return this.delegate.getThresholds().getBytesLow();
      }
   }

   public void setBytesThresholdLow(long var1) {
      if (this.delegate == null) {
         this.putValue("BytesThresholdLow", new Long(var1));
      } else {
         this.delegate.getThresholds().setBytesLow(var1);
      }

   }

   public long getMessagesThresholdHigh() {
      if (this.delegate == null) {
         Object var1 = this.getValue("MessagesThresholdHigh");
         return var1 != null && var1 instanceof Long ? (Long)var1 : -1L;
      } else {
         return this.delegate.getThresholds().getMessagesHigh();
      }
   }

   public void setMessagesThresholdHigh(long var1) {
      if (this.delegate == null) {
         this.putValue("MessagesThresholdHigh", new Long(var1));
      } else {
         this.delegate.getThresholds().setMessagesHigh(var1);
      }

   }

   public long getMessagesThresholdLow() {
      if (this.delegate == null) {
         Object var1 = this.getValue("MessagesThresholdLow");
         return var1 != null && var1 instanceof Long ? (Long)var1 : -1L;
      } else {
         return this.delegate.getThresholds().getMessagesLow();
      }
   }

   public void setMessagesThresholdLow(long var1) {
      if (this.delegate == null) {
         this.putValue("MessagesThresholdLow", new Long(var1));
      } else {
         this.delegate.getThresholds().setMessagesLow(var1);
      }

   }

   public int getPriorityOverride() {
      if (this.delegate == null) {
         Object var1 = this.getValue("PriorityOverride");
         return var1 != null && var1 instanceof Integer ? (Integer)var1 : -1;
      } else {
         return this.delegate.getDeliveryParamsOverrides().getPriority();
      }
   }

   public void setPriorityOverride(int var1) {
      if (this.delegate == null) {
         this.putValue("PriorityOverride", new Integer(var1));
      } else {
         this.delegate.getDeliveryParamsOverrides().setPriority(var1);
      }

   }

   public String getTimeToDeliverOverride() {
      if (this.delegate == null) {
         Object var1 = this.getValue("TimeToDeliverOverride");
         return var1 != null && var1 instanceof String ? (String)var1 : "-1";
      } else {
         return this.delegate.getDeliveryParamsOverrides().getTimeToDeliver();
      }
   }

   public void setTimeToDeliverOverride(String var1) {
      if (this.delegate == null) {
         this.putValue("TimeToDeliverOverride", var1);
      } else {
         this.delegate.getDeliveryParamsOverrides().setTimeToDeliver(var1);
      }

   }

   public long getRedeliveryDelayOverride() {
      if (this.delegate == null) {
         Object var1 = this.getValue("RedeliveryDelayOverride");
         return var1 != null && var1 instanceof Long ? (Long)var1 : -1L;
      } else {
         return this.delegate.getDeliveryParamsOverrides().getRedeliveryDelay();
      }
   }

   public void setRedeliveryDelayOverride(long var1) {
      if (this.delegate == null) {
         this.putValue("RedeliveryDelayOverride", new Long(var1));
      } else {
         this.delegate.getDeliveryParamsOverrides().setRedeliveryDelay(var1);
      }

   }

   public int getRedeliveryLimit() {
      if (this.delegate == null) {
         Object var1 = this.getValue("RedeliveryLimit");
         return var1 != null && var1 instanceof Integer ? (Integer)var1 : -1;
      } else {
         return this.delegate.getDeliveryFailureParams().getRedeliveryLimit();
      }
   }

   public void setRedeliveryLimit(int var1) {
      if (this.delegate == null) {
         this.putValue("RedeliveryLimit", new Integer(var1));
      } else {
         this.delegate.getDeliveryFailureParams().setRedeliveryLimit(var1);
      }

   }

   public long getTimeToLiveOverride() {
      if (this.delegate == null) {
         Object var1 = this.getValue("TimeToLiveOverride");
         return var1 != null && var1 instanceof Long ? (Long)var1 : -1L;
      } else {
         return this.delegate.getDeliveryParamsOverrides().getTimeToLive();
      }
   }

   public void setTimeToLiveOverride(long var1) {
      if (this.delegate == null) {
         this.putValue("TimeToLiveOverride", new Long(var1));
      } else {
         this.delegate.getDeliveryParamsOverrides().setTimeToLive(var1);
      }

   }

   public String getDeliveryModeOverride() {
      if (this.delegate == null) {
         Object var1 = this.getValue("DeliveryModeOverride");
         return var1 != null && var1 instanceof String ? (String)var1 : "No-Delivery";
      } else {
         return this.delegate.getDeliveryParamsOverrides().getDeliveryMode();
      }
   }

   public void setDeliveryModeOverride(String var1) {
      if (this.delegate == null) {
         this.putValue("DeliveryModeOverride", var1);
      } else {
         this.delegate.getDeliveryParamsOverrides().setDeliveryMode(var1);
      }

   }

   public String getExpirationPolicy() {
      if (this.delegate == null) {
         Object var1 = this.getValue("ExpirationPolicy");
         return var1 != null && var1 instanceof String ? (String)var1 : "Discard";
      } else {
         return this.delegate.getDeliveryFailureParams().getExpirationPolicy();
      }
   }

   public void setExpirationPolicy(String var1) {
      if (this.delegate == null) {
         this.putValue("ExpirationPolicy", var1);
      } else {
         this.delegate.getDeliveryFailureParams().setExpirationPolicy(var1);
      }

   }

   public String getExpirationLoggingPolicy() {
      if (this.delegate == null) {
         Object var1 = this.getValue("ExpirationLoggingPolicy");
         return var1 != null && var1 instanceof String ? (String)var1 : null;
      } else {
         return this.delegate.getDeliveryFailureParams().getExpirationLoggingPolicy();
      }
   }

   public void setExpirationLoggingPolicy(String var1) {
      if (this.delegate == null) {
         this.putValue("ExpirationLoggingPolicy", var1);
      } else {
         this.delegate.getDeliveryFailureParams().setExpirationLoggingPolicy(var1);
      }

   }

   public int getMaximumMessageSize() {
      if (this.delegate == null) {
         Object var1 = this.getValue("MaximumMessageSize");
         return var1 != null && var1 instanceof Integer ? (Integer)var1 : Integer.MAX_VALUE;
      } else {
         return this.delegate.getMaximumMessageSize();
      }
   }

   public void setMaximumMessageSize(int var1) {
      if (this.delegate == null) {
         this.putValue("MaximumMessageSize", new Integer(var1));
      } else {
         this.delegate.setMaximumMessageSize(var1);
      }

   }

   public String getNotes() {
      if (this.delegate == null) {
         Object var1 = this.getValue("Notes");
         return var1 != null && var1 instanceof String ? (String)var1 : null;
      } else {
         return this.delegate.getNotes();
      }
   }

   public void setNotes(String var1) {
      if (this.delegate == null) {
         this.putValue("Notes", var1);
      } else {
         this.delegate.setNotes(var1);
      }

   }
}
