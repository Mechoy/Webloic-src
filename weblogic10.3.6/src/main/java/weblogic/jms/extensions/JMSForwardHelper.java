package weblogic.jms.extensions;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import weblogic.jms.common.MessageImpl;

public class JMSForwardHelper {
   private static int Default = 0;
   private static long LDefault = 0L;

   public static final void Forward(WLMessageProducer var0, Message var1, boolean var2) throws JMSException {
      ForwardInternal(var0, (Destination)null, var1, Default, Default, LDefault, false, true, var2);
   }

   public static final void Forward(WLMessageProducer var0, Destination var1, Message var2, boolean var3) throws JMSException {
      ForwardInternal(var0, var1, var2, Default, Default, LDefault, false, true, var3);
   }

   public static final void Forward(WLMessageProducer var0, Message var1, int var2, int var3, long var4, boolean var6) throws JMSException {
      ForwardInternal(var0, (Destination)null, var1, var2, var3, var4, false, false, var6);
   }

   public static final void Forward(WLMessageProducer var0, Destination var1, Message var2, int var3, int var4, long var5, boolean var7) throws JMSException {
      ForwardInternal(var0, var1, var2, var3, var4, var5, false, false, var7);
   }

   public static final void ForwardFromMessage(WLMessageProducer var0, Message var1, boolean var2) throws JMSException {
      ForwardInternal(var0, (Destination)null, var1, var1.getJMSDeliveryMode(), var1.getJMSPriority(), getRelativeTimeToLive(var1), true, false, var2);
   }

   public static final void ForwardFromMessage(WLMessageProducer var0, Destination var1, Message var2, boolean var3) throws JMSException {
      ForwardInternal(var0, var1, var2, var2.getJMSDeliveryMode(), var2.getJMSPriority(), getRelativeTimeToLive(var2), true, false, var3);
   }

   private static final void ForwardInternal(WLMessageProducer var0, Destination var1, Message var2, int var3, int var4, long var5, boolean var7, boolean var8, boolean var9) throws JMSException {
      Preserve var10 = null;

      try {
         var10 = new Preserve(var0, var2, var7, var9);
         if (var1 == null) {
            if (var8) {
               var0.forward(var2);
            } else {
               var0.forward(var2, var3, var4, var5);
            }
         } else if (var8) {
            var0.forward(var1, var2);
         } else {
            var0.forward(var1, var2, var3, var4, var5);
         }
      } finally {
         if (var10 != null) {
            var10.restore();
         }

      }

   }

   public static long getRelativeTimeToLive(Message var0) throws JMSException {
      long var1 = var0.getJMSExpiration();
      if (var1 != 0L) {
         if (var1 < 0L) {
            if (Long.MAX_VALUE + var1 <= System.currentTimeMillis()) {
               var1 = Long.MIN_VALUE;
            } else {
               var1 -= System.currentTimeMillis();
            }
         } else {
            var1 -= System.currentTimeMillis();
            if (var1 == 0L) {
               var1 = -1L;
            }
         }
      } else {
         var1 = 0L;
      }

      return var1;
   }

   public static void copyHeaders(Message var0, Message var1) throws JMSException {
      if (var0.propertyExists("JMS_BEA_UnitOfWork")) {
         var1.setStringProperty("JMS_BEA_UnitOfWork", var0.getStringProperty("JMS_BEA_UnitOfWork"));
      }

      if (var0.propertyExists("JMS_BEA_UnitOfWorkSequenceNumber")) {
         var1.setIntProperty("JMS_BEA_UnitOfWorkSequenceNumber", var0.getIntProperty("JMS_BEA_UnitOfWorkSequenceNumber"));
      }

      if (var0.propertyExists("JMS_BEA_IsUnitOfWorkEnd")) {
         var1.setBooleanProperty("JMS_BEA_IsUnitOfWorkEnd", var0.getBooleanProperty("JMS_BEA_IsUnitOfWorkEnd"));
      }

   }

   private static class Preserve {
      private long absoluteExpirationTime;
      private long absoluteTimeToDeliver;
      private Destination destination;
      private int deliveryMode;
      private boolean redelivered;
      private int priority;
      private int redeliveryLimit;
      private String unitOfOrderName;
      private boolean forwardFlag;
      private WLMessage message;
      private WLMessageProducer producer;
      private String unitOfOrderName_p;
      private long timeToDelivery_p;
      private int redeliveryLimit_p;
      private boolean restoreAfterDone;
      private boolean fromMessage;
      private String jmsxuserid = null;

      Preserve(WLMessageProducer var1, Message var2, boolean var3, boolean var4) throws JMSException {
         this.producer = var1;
         this.message = (WLMessage)var2;
         this.fromMessage = var3;
         this.restoreAfterDone = var4;
         if (var4) {
            this.absoluteExpirationTime = this.message.getJMSExpiration();
            this.destination = this.message.getJMSDestination();
            this.deliveryMode = this.message.getJMSDeliveryMode();
            this.redelivered = this.message.getJMSRedelivered();
            this.priority = this.message.getJMSPriority();
            this.forwardFlag = ((MessageImpl)this.message).isForwardable();
            if (this.message.propertyExists("JMSXUserID")) {
               this.jmsxuserid = this.message.getStringProperty("JMSXUserID");
            }
         }

         if (var4 || var3) {
            this.unitOfOrderName = ((MessageImpl)this.message).getUnitOfOrder();
            this.redeliveryLimit = this.message.getJMSRedeliveryLimit();
            this.absoluteTimeToDeliver = this.message.getJMSDeliveryTime();
         }

         if (var3) {
            this.unitOfOrderName_p = var1.getUnitOfOrder();
            this.timeToDelivery_p = var1.getTimeToDeliver();
            this.redeliveryLimit_p = var1.getRedeliveryLimit();
            var1.setUnitOfOrder(this.unitOfOrderName);
            var1.setRedeliveryLimit(this.redeliveryLimit);
            if (this.absoluteTimeToDeliver > System.currentTimeMillis()) {
               var1.setTimeToDeliver(this.absoluteTimeToDeliver - System.currentTimeMillis());
            } else {
               var1.setTimeToDeliver(0L);
            }
         }

      }

      void restore() throws JMSException {
         if (this.restoreAfterDone) {
            this.message.setJMSExpiration(this.absoluteExpirationTime);
            this.message.setJMSDeliveryTime(this.absoluteTimeToDeliver);
            this.message.setJMSDestination(this.destination);
            this.message.setJMSDeliveryMode(this.deliveryMode);
            this.message.setJMSRedelivered(this.redelivered);
            this.message.setJMSPriority(this.priority);
            this.message.setJMSRedeliveryLimit(this.redeliveryLimit);
            ((MessageImpl)this.message).setUnitOfOrderName(this.unitOfOrderName);
            ((MessageImpl)this.message).setForward(this.forwardFlag);
            ((MessageImpl)this.message).setJMSXUserID(this.jmsxuserid);
         }

         if (this.fromMessage) {
            this.producer.setUnitOfOrder(this.unitOfOrderName_p);
            this.producer.setTimeToDeliver(this.timeToDelivery_p);
            this.producer.setRedeliveryLimit(this.redeliveryLimit_p);
         }

      }
   }
}
