package weblogic.messaging.interception.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSMessageId;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.extensions.WLMessageProducer;

public class JMSHelper {
   public static final void Send(WLMessageProducer var0, Message var1) throws JMSException {
      SendInternal(var0, (Destination)null, var1);
   }

   public static final void Send(WLMessageProducer var0, Destination var1, Message var2) throws JMSException {
      SendInternal(var0, var1, var2);
   }

   public static final void Send(WLMessageProducer var0, Message var1, int var2, int var3, long var4) throws JMSException {
      SendInternal(var0, (Destination)null, var1, var2, var3, var4);
   }

   public static final void Send(WLMessageProducer var0, Destination var1, Message var2, int var3, int var4, long var5) throws JMSException {
      SendInternal(var0, var1, var2, var3, var4, var5);
   }

   public static final void SendFromMessage(WLMessageProducer var0, Message var1) throws JMSException {
      SendInternal(var0, (Destination)null, var1, var1.getJMSDeliveryMode(), var1.getJMSPriority(), getRelativeTimeToLive(var1));
   }

   public static final void SendFromMessage(WLMessageProducer var0, Destination var1, Message var2) throws JMSException {
      SendInternal(var0, var1, var2, var2.getJMSDeliveryMode(), var2.getJMSPriority(), getRelativeTimeToLive(var2));
   }

   private static final void SendInternal(WLMessageProducer var0, Destination var1, Message var2) throws JMSException {
      CopiedMessage var3 = null;

      try {
         var3 = new CopiedMessage(var2);
         if (var1 == null) {
            var0.send(var2);
         } else {
            var0.send(var1, var2);
         }
      } finally {
         if (var3 != null) {
            var3.restoreMessage();
         }

      }

   }

   private static final void SendInternal(WLMessageProducer var0, Destination var1, Message var2, int var3, int var4, long var5) throws JMSException {
      CopiedMessage var7 = null;

      try {
         var7 = new CopiedMessage(var2);
         if (var1 == null) {
            var0.send(var2, var3, var4, var5);
         } else {
            var0.send(var1, var2, var3, var4, var5);
         }
      } finally {
         if (var7 != null) {
            var7.restoreMessage();
         }

      }

   }

   public static final void Forward(WLMessageProducer var0, Message var1) throws JMSException {
      ForwardInternal(var0, (Destination)null, var1, false);
   }

   public static final void Forward(WLMessageProducer var0, Destination var1, Message var2) throws JMSException {
      ForwardInternal(var0, var1, var2, false);
   }

   public static final void Forward(WLMessageProducer var0, Message var1, int var2, int var3, long var4) throws JMSException {
      ForwardInternal(var0, (Destination)null, var1, var2, var3, var4, false);
   }

   public static final void Forward(WLMessageProducer var0, Destination var1, Message var2, int var3, int var4, long var5) throws JMSException {
      ForwardInternal(var0, var1, var2, var3, var4, var5, false);
   }

   public static final void ForwardFromMessage(WLMessageProducer var0, Message var1) throws JMSException {
      ForwardInternal(var0, (Destination)null, var1, var1.getJMSDeliveryMode(), var1.getJMSPriority(), getRelativeTimeToLive(var1), true);
   }

   public static final void ForwardFromMessage(WLMessageProducer var0, Destination var1, Message var2) throws JMSException {
      ForwardInternal(var0, var1, var2, var2.getJMSDeliveryMode(), var2.getJMSPriority(), getRelativeTimeToLive(var2), true);
   }

   private static final void ForwardInternal(WLMessageProducer var0, Destination var1, Message var2, boolean var3) throws JMSException {
      CopiedMessage var4 = null;

      try {
         if (var3) {
            var4 = new CopiedMessage(var0, var2);
         } else {
            var4 = new CopiedMessage(var2);
         }

         if (var1 == null) {
            var0.forward(var2);
         } else {
            var0.forward(var1, var2);
         }
      } finally {
         if (var4 != null) {
            var4.restoreMessage();
         }

      }

   }

   private static final void ForwardInternal(WLMessageProducer var0, Destination var1, Message var2, int var3, int var4, long var5, boolean var7) throws JMSException {
      CopiedMessage var8 = null;

      try {
         if (var7) {
            var8 = new CopiedMessage(var0, var2);
         } else {
            var8 = new CopiedMessage(var2);
         }

         if (var1 == null) {
            var0.forward(var2, var3, var4, var5);
         } else {
            var0.forward(var1, var2, var3, var4, var5);
         }
      } finally {
         if (var8 != null) {
            var8.restoreMessage();
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
         }
      } else {
         var1 = 0L;
      }

      return var1;
   }

   public static final void ForwardPreserveMsgProperty(WLMessageProducer var0, Message var1) throws JMSException {
      var0.setUnitOfOrder(var1.getStringProperty("JMS_BEA_UnitOfOrder"));
      var0.setRedeliveryLimit(var1.getIntProperty("JMS_BEA_RedeliveryLimit"));
      var0.forward(var1, var1.getJMSDeliveryMode(), var1.getJMSPriority(), getRelativeTimeToLive(var1));
   }

   private static class CopiedMessage {
      private long absoluteExpirationTime;
      private long absoluteTimeToDeliver;
      private Destination destination;
      private int deliveryMode;
      private boolean redelivered;
      private int priority;
      private int redeliveryLimit;
      private JMSID connectionId;
      private JMSMessageId msgId;
      private boolean ddforwarded;
      private String unitOfOrderName;
      private boolean forwardFlag;
      private MessageImpl message;
      private WLMessageProducer producer;
      private String unitOfOrderName_p;
      private long timeToDelivery_p;
      private int redeliveryLimit_p;

      CopiedMessage(WLMessageProducer var1, Message var2) throws JMSException {
         this(var2);
         this.producer = var1;
         this.unitOfOrderName_p = var1.getUnitOfOrder();
         this.timeToDelivery_p = var1.getTimeToDeliver();
         this.redeliveryLimit_p = var1.getRedeliveryLimit();
         var1.setUnitOfOrder(this.unitOfOrderName);
         var1.setRedeliveryLimit(this.redeliveryLimit);
         if (this.absoluteTimeToDeliver > 0L && this.absoluteTimeToDeliver > System.currentTimeMillis()) {
            var1.setTimeToDeliver(this.absoluteTimeToDeliver - System.currentTimeMillis());
         } else {
            var1.setTimeToDeliver(0L);
         }

      }

      CopiedMessage(Message var1) throws JMSException {
         this.message = (MessageImpl)var1;
         this.absoluteExpirationTime = this.message.getJMSExpiration();
         this.absoluteTimeToDeliver = this.message.getJMSDeliveryTime();
         this.destination = this.message.getJMSDestination();
         this.deliveryMode = this.message.getJMSDeliveryMode();
         this.redelivered = this.message.getJMSRedelivered();
         this.priority = this.message.getJMSPriority();
         this.redeliveryLimit = this.message.getJMSRedeliveryLimit();
         this.connectionId = this.message.getConnectionId();
         this.msgId = this.message.getId();
         this.ddforwarded = this.message.getDDForwarded();
         this.unitOfOrderName = this.message.getUnitOfOrder();
         this.forwardFlag = this.message.isForwardable();
      }

      void restoreMessage() throws JMSException {
         this.message.setJMSExpiration(this.absoluteExpirationTime);
         this.message.setJMSDeliveryTime(this.absoluteTimeToDeliver);
         this.message.setJMSDestination(this.destination);
         this.message.setJMSDeliveryMode(this.deliveryMode);
         this.message.setJMSRedelivered(this.redelivered);
         this.message.setJMSPriority(this.priority);
         this.message.setJMSRedeliveryLimit(this.redeliveryLimit);
         this.message.setConnectionId(this.connectionId);
         this.message.setId(this.msgId);
         this.message.setDDForwarded(this.ddforwarded);
         this.message.setUnitOfOrderName(this.unitOfOrderName);
         this.message.setForward(this.forwardFlag);
         if (this.producer != null) {
            this.producer.setUnitOfOrder(this.unitOfOrderName_p);
            this.producer.setTimeToDeliver(this.timeToDelivery_p);
            this.producer.setRedeliveryLimit(this.redeliveryLimit_p);
         }

      }
   }
}
