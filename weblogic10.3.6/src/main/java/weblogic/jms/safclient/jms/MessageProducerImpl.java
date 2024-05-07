package weblogic.jms.safclient.jms;

import java.util.Random;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueSender;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import weblogic.jms.client.JMSProducer;
import weblogic.jms.common.IllegalStateException;
import weblogic.jms.common.JMSMessageId;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.common.ProducerSendResponse;
import weblogic.jms.common.ResourceAllocationException;
import weblogic.jms.extensions.WLMessage;
import weblogic.jms.extensions.WLMessageProducer;
import weblogic.jms.safclient.ClientSAFDelegate;
import weblogic.jms.safclient.agent.DestinationImpl;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.KernelRequest;
import weblogic.messaging.kernel.QuotaException;
import weblogic.messaging.kernel.SendOptions;
import weblogic.transaction.TransactionHelper;

public final class MessageProducerImpl implements TopicPublisher, QueueSender, WLMessageProducer {
   private static final String NON_PERSISTENT = "Non-Persistent";
   private SessionImpl session;
   private int id;
   private DestinationImpl destination;
   private long timeToDeliver;
   private int redeliveryLimit = -1;
   private long sendTimeout;
   private String unitOfOrder;
   private int compressionThreshold;
   private boolean disableMessageID = false;
   private boolean disableMessageTimestamp = false;
   private int deliveryMode;
   private int priority;
   private long timeToLive;
   private boolean closed = false;

   MessageProducerImpl(SessionImpl var1, int var2, DestinationImpl var3) {
      this.session = var1;
      this.id = var2;
      this.destination = var3;
      this.timeToDeliver = Long.parseLong(this.session.getDefaultTimeToDeliver());
      this.sendTimeout = this.session.getSendTimeout();
      this.unitOfOrder = this.session.getDefaultUnitOfOrder();
      this.compressionThreshold = this.session.getDefaultCompressionThreshold();
      this.deliveryMode = deliveryModeToInt(this.session.getDefaultDeliveryMode());
      this.priority = this.session.getDefaultPriority();
      this.timeToLive = this.session.getDefaultTimeToLive();
   }

   private static int deliveryModeToInt(String var0) {
      return "Non-Persistent".equals(var0) ? 1 : 2;
   }

   public Topic getTopic() throws JMSException {
      this.checkClosed();
      if (this.destination == null) {
         return null;
      } else if (!this.destination.isTopic()) {
         throw new weblogic.jms.common.JMSException("The destination for this message producer is not a topic");
      } else {
         return this.destination;
      }
   }

   public void publish(Message var1) throws JMSException {
      this.send(var1, this.deliveryMode, this.priority, this.timeToLive);
   }

   public void publish(Message var1, int var2, int var3, long var4) throws JMSException {
      if (this.destination == null) {
         throw new weblogic.jms.common.JMSException("This is not a pinned message producer, cannot use this API");
      } else {
         this.send((Queue)this.destination, var1, var2, var3, var4);
      }
   }

   public void publish(Topic var1, Message var2) throws JMSException {
      this.publish(var1, var2, this.deliveryMode, this.priority, this.timeToLive);
   }

   public void publish(Topic var1, Message var2, int var3, int var4, long var5) throws JMSException {
      if (this.destination != null) {
         throw new weblogic.jms.common.JMSException("This is a pinned message producer, cannot use this API");
      } else {
         this.send((Destination)var1, var2, var3, var4, var5);
      }
   }

   public Queue getQueue() throws JMSException {
      this.checkClosed();
      if (this.destination == null) {
         return null;
      } else if (!this.destination.isQueue()) {
         throw new weblogic.jms.common.JMSException("The destination for this message producer is not a queue");
      } else {
         return this.destination;
      }
   }

   public void send(Message var1) throws JMSException {
      this.send(var1, this.deliveryMode, this.priority, this.timeToLive);
   }

   public void send(Message var1, int var2, int var3, long var4) throws JMSException {
      if (this.destination == null) {
         throw new weblogic.jms.common.JMSException("This is not a pinned message producer, cannot use this API");
      } else {
         this.send((Queue)this.destination, var1, var2, var3, var4);
      }
   }

   public void send(Queue var1, Message var2) throws JMSException {
      this.send(var1, var2, this.deliveryMode, this.priority, this.timeToLive);
   }

   public void send(Queue var1, Message var2, int var3, int var4, long var5) throws JMSException {
      this.send((Destination)var1, var2, var3, var4, var5);
   }

   public long getTimeToDeliver() throws JMSException {
      this.checkClosed();
      return this.timeToDeliver;
   }

   public void setTimeToDeliver(long var1) throws JMSException {
      this.checkClosed();
      this.timeToDeliver = var1;
   }

   public int getRedeliveryLimit() throws JMSException {
      this.checkClosed();
      return this.redeliveryLimit;
   }

   public void setRedeliveryLimit(int var1) throws JMSException {
      this.checkClosed();
      this.redeliveryLimit = var1;
   }

   public long getSendTimeout() throws JMSException {
      this.checkClosed();
      return this.sendTimeout;
   }

   public void setSendTimeout(long var1) throws JMSException {
      this.checkClosed();
      this.sendTimeout = var1;
   }

   public String getUnitOfOrder() throws JMSException {
      this.checkClosed();
      return this.unitOfOrder;
   }

   public void setUnitOfOrder(String var1) throws JMSException {
      this.checkClosed();
      this.unitOfOrder = var1;
   }

   private static char intToHexChar(int var0) {
      return var0 < 10 ? (char)(48 + var0) : (char)(97 + (var0 - 10));
   }

   private static String bitsToString(byte[] var0) {
      if (var0 != null && var0.length > 0) {
         StringBuffer var1 = new StringBuffer(var0.length * 2);

         for(int var2 = 0; var2 < var0.length; ++var2) {
            byte var3 = var0[var2];
            int var4 = var3 & 15;
            var1.append(intToHexChar(var4));
            int var5 = var3 >> 4 & 15;
            var1.append(intToHexChar(var5));
         }

         return var1.toString();
      } else {
         return new String();
      }
   }

   public void setUnitOfOrder() throws JMSException {
      this.checkClosed();
      Random var1 = new Random();
      byte[] var2 = new byte[16];
      var1.nextBytes(var2);
      this.setUnitOfOrder(bitsToString(var2));
   }

   public void forward(Message var1, int var2, int var3, long var4) throws JMSException {
      throw new weblogic.jms.common.JMSException("Not yet implemented");
   }

   public void forward(Message var1) throws JMSException {
      throw new weblogic.jms.common.JMSException("Not yet implemented");
   }

   public void forward(Destination var1, Message var2, int var3, int var4, long var5) throws JMSException {
      throw new weblogic.jms.common.JMSException("Not yet implemented");
   }

   public void forward(Destination var1, Message var2) throws JMSException {
      throw new weblogic.jms.common.JMSException("Not yet implemented");
   }

   public void setCompressionThreshold(int var1) throws JMSException {
      this.checkClosed();
      this.compressionThreshold = var1;
   }

   public int getCompressionThreshold() throws JMSException {
      this.checkClosed();
      return this.compressionThreshold;
   }

   public void setDisableMessageID(boolean var1) throws JMSException {
      this.checkClosed();
      this.disableMessageID = var1;
   }

   public boolean getDisableMessageID() throws JMSException {
      this.checkClosed();
      return this.disableMessageID;
   }

   public void setDisableMessageTimestamp(boolean var1) throws JMSException {
      this.checkClosed();
      this.disableMessageTimestamp = var1;
   }

   public boolean getDisableMessageTimestamp() throws JMSException {
      this.checkClosed();
      return this.disableMessageTimestamp;
   }

   public void setDeliveryMode(int var1) throws JMSException {
      this.checkClosed();
      if (var1 != 2 && var1 != 1) {
         throw new weblogic.jms.common.JMSException("Invalid delivery mode: " + var1);
      } else {
         this.deliveryMode = var1;
      }
   }

   public int getDeliveryMode() throws JMSException {
      this.checkClosed();
      return this.deliveryMode;
   }

   public void setPriority(int var1) throws JMSException {
      this.checkClosed();
      this.priority = var1;
   }

   public int getPriority() throws JMSException {
      this.checkClosed();
      return this.priority;
   }

   public void setTimeToLive(long var1) throws JMSException {
      this.checkClosed();
      this.timeToLive = var1;
   }

   public long getTimeToLive() throws JMSException {
      this.checkClosed();
      return this.timeToLive;
   }

   public Destination getDestination() throws JMSException {
      this.checkClosed();
      return this.destination;
   }

   public synchronized void close() {
      if (!this.closed) {
         this.closed = true;
         this.session.closeProducer(this.id);
      }
   }

   public void send(Destination var1, Message var2) throws JMSException {
      this.send(var1, var2, this.deliveryMode, this.priority, this.timeToLive);
   }

   private static long safeLongAdd(long var0, long var2) {
      long var4;
      if (var0 > 0L && var2 > 0L) {
         var4 = var0 + var2;
         return var4 < 0L ? Long.MAX_VALUE : var4;
      } else if (var0 < 0L && var2 < 0L) {
         var4 = var0 + var2;
         return var4 >= 0L ? Long.MIN_VALUE : var4;
      } else {
         return var0 + var2;
      }
   }

   private void handleJMSMessagePreSend(MessageImpl var1, boolean var2, long var3, long var5, int var7, boolean var8, int var9, boolean var10, String var11) throws JMSException {
      var1.resetUserPropertySize();
      if (!var2) {
         var1.setForward(false);
         var1.resetForwardsCount();
         var1.setOldMessage(false);
         var1.setJMSXUserID((String)null);
         var1.setId(JMSMessageId.create());
      }

      var1.setJMSExpiration(var3);
      var1.setDeliveryTime(var5);
      var1.setJMSRedeliveryLimit(var7);
      var1.setJMSDestinationImpl((weblogic.jms.common.DestinationImpl)null);
      var1.setJMSDeliveryMode(var8 ? 2 : 1);
      var1.setJMSPriority(var9);
      var1.setDDForwarded(false);
      var1.setDeliveryCount(0);
      var1.requestJMSXUserID(var10);
      var1.setUnitOfOrderName(var11);
   }

   private void internalSend(ClientSAFDelegate var1, SessionImpl var2, DestinationImpl var3, MessageImpl var4, boolean var5, boolean var6, int var7, int var8, long var9, long var11, long var13, boolean var15, String var16) throws JMSException {
      SendOptions var17 = new SendOptions();
      var17.setPersistent(var6);
      if (var7 >= 0) {
         var17.setRedeliveryLimit(var7);
      }

      long var18 = -1L;
      long var20 = 0L;
      if (var9 > 0L) {
         var18 = System.currentTimeMillis();
         var20 = safeLongAdd(var18, var9);
         var17.setExpirationTime(var20);
      }

      long var22 = 0L;
      if (var11 > 0L) {
         if (var18 == -1L) {
            var18 = System.currentTimeMillis();
         }

         var22 = safeLongAdd(var18, var11);
         var17.setDeliveryTime(var22);
      }

      if (var13 > 0L) {
         var17.setTimeout(var13);
      }

      weblogic.messaging.kernel.Queue var24 = var3.getKernelQueue();
      if (var24 == null) {
         throw new weblogic.jms.common.JMSException("Failed to send messages -- client SAF is not properly started. Check client SAF configuration.");
      } else {
         MessageImpl var25 = null;
         if (var4 != null) {
            var25 = var4.copy();
            this.handleJMSMessagePreSend(var25, var5, var20, var22, var7, var6, var8, var15, var16);
         }

         TransactionHelper var26 = var1.getTransactionHelper();
         TransactionHelper.pushTransactionHelper(var26);
         if (var2.getTransacted()) {
            var2.beginOrResume(var26);
         }

         try {
            KernelRequest var27;
            try {
               var17.setSequence(var24.findOrCreateSequence(this.getSequenceName(var25, var3), this.getSequenceMode(var25, var3)));
               var27 = var24.send(var25, var17);
            } catch (QuotaException var37) {
               throw new ResourceAllocationException(var37.toString(), var37);
            } catch (KernelException var38) {
               throw new weblogic.jms.common.JMSException(var38);
            }

            ProducerSendResponseImpl var28;
            if (var27 == null) {
               if (var25 != null) {
                  var28 = new ProducerSendResponseImpl(var25.getId(), var6, var8, var9, var11, var7);
                  JMSProducer.sendReturn(var28, var4, var25, var5, var11, var9, var6 ? 2 : 1, var8, var3);
               }

               return;
            }

            try {
               var27.getResult();
            } catch (QuotaException var35) {
               throw new ResourceAllocationException(var35.toString(), var35);
            } catch (KernelException var36) {
               throw new weblogic.jms.common.JMSException(var36);
            }

            if (var25 != null) {
               var28 = new ProducerSendResponseImpl(var25.getId(), var6, var8, var9, var11, var7);
               JMSProducer.sendReturn(var28, var4, var25, var5, var11, var9, var6 ? 2 : 1, var8, var3);
            }
         } finally {
            if (var2.getTransacted()) {
               var2.suspend(var26);
            }

            TransactionHelper.popTransactionHelper();
         }

      }
   }

   public void send(Destination var1, Message var2, int var3, int var4, long var5) throws JMSException {
      this.checkClosed();
      if (var1 == null) {
         throw new weblogic.jms.common.JMSException("The destination is null");
      } else if (!(var1 instanceof DestinationImpl)) {
         throw new weblogic.jms.common.JMSException("The destination passed to the client SAF implementation must befrom the proper initial context.  However, this destination is of type: " + var1.getClass().getName());
      } else {
         MessageImpl var7 = null;
         if (var2 != null) {
            if (!(var2 instanceof MessageImpl)) {
               throw new weblogic.jms.common.JMSException("A message of an unknown type was found.  It is of type " + var2.getClass().getName());
            }

            var7 = (MessageImpl)var2;
         }

         this.internalSend(this.session.getRoot(), this.session, (DestinationImpl)var1, var7, false, var3 == 2, this.redeliveryLimit, var4, var5, this.timeToDeliver, this.sendTimeout, this.session.getAttachJMSXUserId(), this.unitOfOrder);
      }
   }

   private synchronized void checkClosed() throws JMSException {
      if (this.closed) {
         throw new IllegalStateException("The message producer has been closed");
      }
   }

   private String getSequenceName(WLMessage var1, DestinationImpl var2) {
      String var3 = var1.getSAFSequenceName();
      return var3 != null ? var3 : var2.getSequenceName();
   }

   protected int getSequenceMode(WLMessage var1, DestinationImpl var2) {
      int var3 = ((MessageImpl)var1).getJMSDeliveryMode();
      if (var3 == 1 && var2.getNonPersistentQOS() != "Exactly-Once") {
         return 0;
      } else {
         boolean var4 = ((MessageImpl)var1).isForwarded();
         boolean var5 = var1.getSAFSeqNumber() != 0L;
         return var5 && var4 ? 2 : 1;
      }
   }

   private static class ProducerSendResponseImpl implements ProducerSendResponse {
      private JMSMessageId messageID;
      private boolean persistent;
      private int priority;
      private long timeToLive;
      private long timeToDeliver;
      private int redeliveryLimit;

      private ProducerSendResponseImpl(JMSMessageId var1, boolean var2, int var3, long var4, long var6, int var8) {
         this.messageID = var1;
         this.persistent = var2;
         this.priority = var3;
         this.timeToLive = var4;
         this.timeToDeliver = var6;
         this.redeliveryLimit = var8;
      }

      public JMSMessageId getMessageId() {
         return this.messageID;
      }

      public boolean get90StyleMessageId() {
         return true;
      }

      public int getDeliveryMode() {
         return this.persistent ? 2 : 1;
      }

      public int getPriority() {
         return this.priority;
      }

      public long getTimeToLive() {
         return this.timeToLive > 0L ? this.timeToLive : -1L;
      }

      public long getTimeToDeliver() {
         return this.timeToDeliver > 0L ? this.timeToDeliver : -1L;
      }

      public int getRedeliveryLimit() {
         return this.redeliveryLimit;
      }

      // $FF: synthetic method
      ProducerSendResponseImpl(JMSMessageId var1, boolean var2, int var3, long var4, long var6, int var8, Object var9) {
         this(var1, var2, var3, var4, var6, var8);
      }
   }
}
