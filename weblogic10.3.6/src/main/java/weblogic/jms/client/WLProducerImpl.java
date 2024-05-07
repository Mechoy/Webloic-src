package weblogic.jms.client;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Topic;
import weblogic.messaging.dispatcher.CompletionListener;

public class WLProducerImpl extends ReconnectController implements ProducerInternal {
   private WLSessionImpl parent;

   public WLProducerImpl(JMSProducer var1, WLSessionImpl var2) {
      super(var2, var1);
      this.parent = var2;
   }

   protected ReconnectController getParent() {
      return this.parent;
   }

   Object getConnectionStateLock() {
      return this.parent.getConnectionStateLock();
   }

   protected WLConnectionImpl getWLConnectionImpl() {
      return this.parent.getWLConnectionImpl();
   }

   protected JMSConnection getPhysicalJMSConnection() {
      return this.parent.getPhysicalJMSConnection();
   }

   private JMSProducer getPhysicalJMSProducer() {
      return (JMSProducer)this.getPhysical();
   }

   private JMSProducer checkClosedReconnectGetProducer(long var1, JMSProducer var3) throws JMSException {
      return (JMSProducer)this.checkClosedReconnect(var1, var3);
   }

   private JMSProducer checkClosedReconnectGetProducer() throws JMSException {
      return this.checkClosedReconnectGetProducer(System.currentTimeMillis(), (JMSProducer)this.getPhysical());
   }

   public int getCompressionThreshold() throws JMSException {
      return ((JMSProducer)this.getPhysicalWaitForState()).getCompressionThreshold();
   }

   public String getWLSServerName() {
      return ((JMSProducer)this.getPhysicalWaitForState()).getWLSServerName();
   }

   public String getRuntimeMBeanName() {
      return ((JMSProducer)this.getPhysicalWaitForState()).getRuntimeMBeanName();
   }

   public ClientRuntimeInfo getParentInfo() {
      return ((JMSProducer)this.getPhysicalWaitForState()).getParentInfo();
   }

   public Queue getQueue() throws JMSException {
      return (Queue)this.getDestination();
   }

   public void send(Queue var1, Message var2) throws JMSException {
      this.send((Destination)var1, var2);
   }

   public void send(Queue var1, Message var2, int var3, int var4, long var5) throws JMSException {
      this.send((Destination)var1, var2, var3, var4, var5);
   }

   public Topic getTopic() throws JMSException {
      return (Topic)this.getDestination();
   }

   public void publish(Message var1) throws JMSException {
      this.send(var1);
   }

   public void publish(Message var1, int var2, int var3, long var4) throws JMSException {
      this.send(var1, var2, var3, var4);
   }

   public void publish(Topic var1, Message var2) throws JMSException {
      this.send((Destination)var1, var2);
   }

   public void publish(Topic var1, Message var2, int var3, int var4, long var5) throws JMSException {
      this.send((Destination)var1, var2, var3, var4, var5);
   }

   public void setSequence(String var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSProducer var4 = this.checkClosedReconnectGetProducer();

      try {
         var4.setSequence(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         this.idempotentJMSProducer(var2, var4, var6).setSequence(var1);
      }

   }

   public String getSequence() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSProducer var3 = this.checkClosedReconnectGetProducer();

      try {
         return var3.getSequence();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.idempotentJMSProducer(var1, var3, var5).getSequence();
      }
   }

   public void reserveUnitOfOrderWithSequence() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSProducer var3 = this.checkClosedReconnectGetProducer();

      try {
         var3.reserveUnitOfOrderWithSequence();
      } catch (weblogic.jms.common.JMSException var5) {
         this.nonIdempotentJMSProducer(var1, var3, var5).reserveUnitOfOrderWithSequence();
      }

   }

   public void reserveSequence(Destination var1, Message var2, int var3, int var4, long var5) throws JMSException {
      long var7 = System.currentTimeMillis();
      JMSProducer var9 = this.getPhysicalJMSProducer();
      JMSProducer var10 = this.checkClosedReconnectGetProducer(var7, var9);
      long var11 = var5;
      if (var9 != var10 && var5 > 0L) {
         long var13 = System.currentTimeMillis() - var7;
         if (var13 < var5) {
            var11 = var5 - var13;
         }
      }

      try {
         var10.reserveSequence(var1, var2, var3, var4, var11);
      } catch (weblogic.jms.common.JMSException var16) {
         var10 = this.nonIdempotentJMSProducer(var7, var10, var16);
         if (var9 != var10 && var5 > 0L) {
            long var14 = System.currentTimeMillis() - var7;
            if (var14 < var5) {
               var5 -= var14;
            }
         }

         var10.reserveSequence(var1, var2, var3, var4, var5);
      }

   }

   public void releaseSequenceAndUnitOfOrder(boolean var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSProducer var4 = this.checkClosedReconnectGetProducer();

      try {
         var4.releaseSequenceAndUnitOfOrder(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         this.nonIdempotentJMSProducer(var2, var4, var6).releaseSequenceAndUnitOfOrder(var1);
      }

   }

   public void releaseSequenceAndUnitOfOrder(Destination var1, Message var2, int var3, int var4, long var5, boolean var7) throws JMSException {
      long var8 = System.currentTimeMillis();
      JMSProducer var10 = this.getPhysicalJMSProducer();
      JMSProducer var11 = this.checkClosedReconnectGetProducer(var8, var10);
      long var12 = var5;
      if (var10 != var11 && var5 > 0L) {
         long var14 = System.currentTimeMillis() - var8;
         if (var14 < var5) {
            var12 = var5 - var14;
         }
      }

      try {
         var11.releaseSequenceAndUnitOfOrder(var1, var2, var3, var4, var12, var7);
      } catch (weblogic.jms.common.JMSException var17) {
         var11 = this.nonIdempotentJMSProducer(var8, var11, var17);
         if (var10 != var11 && var5 > 0L) {
            long var15 = System.currentTimeMillis() - var8;
            if (var15 < var5) {
               var5 -= var15;
            }
         }

         var11.releaseSequenceAndUnitOfOrder(var1, var2, var3, var4, var5, var7);
      }

   }

   public long getTimeToDeliver() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSProducer var3 = this.checkClosedReconnectGetProducer();

      try {
         return var3.getTimeToDeliver();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.idempotentJMSProducer(var1, var3, var5).getTimeToDeliver();
      }
   }

   public void setTimeToDeliver(long var1) throws JMSException {
      long var3 = System.currentTimeMillis();
      JMSProducer var5 = this.checkClosedReconnectGetProducer();

      try {
         var5.setTimeToDeliver(var1);
      } catch (weblogic.jms.common.JMSException var7) {
         this.idempotentJMSProducer(var3, var5, var7).setTimeToDeliver(var1);
      }

   }

   public int getRedeliveryLimit() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSProducer var3 = this.checkClosedReconnectGetProducer();

      try {
         return var3.getRedeliveryLimit();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.idempotentJMSProducer(var1, var3, var5).getRedeliveryLimit();
      }
   }

   public void setRedeliveryLimit(int var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSProducer var4 = this.checkClosedReconnectGetProducer();

      try {
         var4.setRedeliveryLimit(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         this.idempotentJMSProducer(var2, var4, var6).setRedeliveryLimit(var1);
      }

   }

   public long getSendTimeout() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSProducer var3 = this.checkClosedReconnectGetProducer();

      try {
         return var3.getSendTimeout();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.idempotentJMSProducer(var1, var3, var5).getSendTimeout();
      }
   }

   public void setSendTimeout(long var1) throws JMSException {
      long var3 = System.currentTimeMillis();
      JMSProducer var5 = this.checkClosedReconnectGetProducer();

      try {
         var5.setSendTimeout(var1);
      } catch (weblogic.jms.common.JMSException var7) {
         this.idempotentJMSProducer(var3, var5, var7).setSendTimeout(var1);
      }

   }

   public String getUnitOfOrder() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSProducer var3 = this.checkClosedReconnectGetProducer();

      try {
         return var3.getUnitOfOrder();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.idempotentJMSProducer(var1, var3, var5).getUnitOfOrder();
      }
   }

   public void setUnitOfOrder(String var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSProducer var4 = this.checkClosedReconnectGetProducer();

      try {
         var4.setUnitOfOrder(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         this.idempotentJMSProducer(var2, var4, var6).setUnitOfOrder(var1);
      }

   }

   public void setUnitOfOrder() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSProducer var3 = this.checkClosedReconnectGetProducer();

      try {
         var3.setUnitOfOrder();
      } catch (weblogic.jms.common.JMSException var5) {
         this.idempotentJMSProducer(var1, var3, var5).setUnitOfOrder();
      }

   }

   public void forward(Message var1, int var2, int var3, long var4) throws JMSException {
      long var6 = System.currentTimeMillis();
      JMSProducer var8 = this.getPhysicalJMSProducer();
      JMSProducer var9 = this.checkClosedReconnectGetProducer(var6, var8);
      long var10 = var4;
      if (var8 != var9 && var4 > 0L) {
         long var12 = System.currentTimeMillis() - var6;
         if (var12 < var4) {
            var10 = var4 - var12;
         }
      }

      try {
         var9.forward(var1, var2, var3, var10);
      } catch (weblogic.jms.common.JMSException var15) {
         var9 = this.nonIdempotentJMSProducer(var6, var9, var15);
         if (var8 != var9 && var4 > 0L) {
            long var13 = System.currentTimeMillis() - var6;
            if (var13 < var4) {
               var4 -= var13;
            }
         }

         var9.forward(var1, var2, var3, var4);
      }

   }

   public void forward(Message var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSProducer var4 = this.checkClosedReconnectGetProducer();

      try {
         var4.forward(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         this.nonIdempotentJMSProducer(var2, var4, var6).forward(var1);
      }

   }

   public void forward(Destination var1, Message var2, int var3, int var4, long var5) throws JMSException {
      long var7 = System.currentTimeMillis();
      JMSProducer var9 = this.getPhysicalJMSProducer();
      JMSProducer var10 = this.checkClosedReconnectGetProducer(var7, var9);
      long var11 = var5;
      if (var9 != var10 && var5 > 0L) {
         long var13 = System.currentTimeMillis() - var7;
         if (var13 < var5) {
            var11 = var5 - var13;
         }
      }

      try {
         var10.forward(var1, var2, var3, var4, var11);
      } catch (weblogic.jms.common.JMSException var16) {
         var10 = this.nonIdempotentJMSProducer(var7, var10, var16);
         if (var9 != var10 && var5 > 0L) {
            long var14 = System.currentTimeMillis() - var7;
            if (var14 < var5) {
               var5 -= var14;
            }
         }

         var10.forward(var1, var2, var3, var4, var5);
      }

   }

   public void forward(Destination var1, Message var2) throws JMSException {
      long var3 = System.currentTimeMillis();
      JMSProducer var5 = this.checkClosedReconnectGetProducer();

      try {
         var5.forward(var1, var2);
      } catch (weblogic.jms.common.JMSException var7) {
         this.nonIdempotentJMSProducer(var3, var5, var7).forward(var1, var2);
      }

   }

   public void setCompressionThreshold(int var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSProducer var4 = this.checkClosedReconnectGetProducer();

      try {
         var4.setCompressionThreshold(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         this.idempotentJMSProducer(var2, var4, var6).setCompressionThreshold(var1);
      }

   }

   public void setDisableMessageID(boolean var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSProducer var4 = this.checkClosedReconnectGetProducer();

      try {
         var4.setDisableMessageID(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         this.idempotentJMSProducer(var2, var4, var6).setDisableMessageID(var1);
      }

   }

   public boolean getDisableMessageID() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSProducer var3 = this.checkClosedReconnectGetProducer();

      try {
         return var3.getDisableMessageID();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.idempotentJMSProducer(var1, var3, var5).getDisableMessageID();
      }
   }

   public void setDisableMessageTimestamp(boolean var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSProducer var4 = this.checkClosedReconnectGetProducer();

      try {
         var4.setDisableMessageTimestamp(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         this.idempotentJMSProducer(var2, var4, var6).setDisableMessageTimestamp(var1);
      }

   }

   public boolean getDisableMessageTimestamp() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSProducer var3 = this.checkClosedReconnectGetProducer();

      try {
         return var3.getDisableMessageTimestamp();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.idempotentJMSProducer(var1, var3, var5).getDisableMessageTimestamp();
      }
   }

   public void setDeliveryMode(int var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSProducer var4 = this.checkClosedReconnectGetProducer();

      try {
         var4.setDeliveryMode(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         this.idempotentJMSProducer(var2, var4, var6).setDeliveryMode(var1);
      }

   }

   public int getDeliveryMode() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSProducer var3 = this.checkClosedReconnectGetProducer();

      try {
         return var3.getDeliveryMode();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.idempotentJMSProducer(var1, var3, var5).getDeliveryMode();
      }
   }

   public void setPriority(int var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSProducer var4 = this.checkClosedReconnectGetProducer();

      try {
         var4.setPriority(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         this.idempotentJMSProducer(var2, var4, var6).setPriority(var1);
      }

   }

   public int getPriority() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSProducer var3 = this.checkClosedReconnectGetProducer();

      try {
         return var3.getPriority();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.idempotentJMSProducer(var1, var3, var5).getPriority();
      }
   }

   public void setTimeToLive(long var1) throws JMSException {
      long var3 = System.currentTimeMillis();
      JMSProducer var5 = this.checkClosedReconnectGetProducer();

      try {
         var5.setTimeToLive(var1);
      } catch (weblogic.jms.common.JMSException var7) {
         this.idempotentJMSProducer(var3, var5, var7).setTimeToLive(var1);
      }

   }

   public long getTimeToLive() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSProducer var3 = this.checkClosedReconnectGetProducer();

      try {
         return var3.getTimeToLive();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.idempotentJMSProducer(var1, var3, var5).getTimeToLive();
      }
   }

   public Destination getDestination() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSProducer var3 = this.checkClosedReconnectGetProducer();

      try {
         return var3.getDestination();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.idempotentJMSProducer(var1, var3, var5).getDestination();
      }
   }

   public void send(Message var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSProducer var4 = this.checkClosedReconnectGetProducer();

      try {
         var4.send(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         this.nonIdempotentJMSProducer(var2, var4, var6).send(var1);
      }

   }

   public void send(Message var1, int var2, int var3, long var4) throws JMSException {
      long var6 = System.currentTimeMillis();
      JMSProducer var8 = this.getPhysicalJMSProducer();
      JMSProducer var9 = this.checkClosedReconnectGetProducer(var6, var8);
      long var10 = var4;
      if (var8 != var9 && var4 > 0L) {
         long var12 = System.currentTimeMillis() - var6;
         if (var12 < var4) {
            var10 = var4 - var12;
         }
      }

      try {
         var9.send(var1, var2, var3, var10);
      } catch (weblogic.jms.common.JMSException var15) {
         var9 = this.nonIdempotentJMSProducer(var6, var9, var15);
         if (var8 != var9 && var4 > 0L) {
            long var13 = System.currentTimeMillis() - var6;
            if (var13 < var4) {
               var4 -= var13;
            }
         }

         var9.send(var1, var2, var3, var4);
      }

   }

   public void send(Destination var1, Message var2) throws JMSException {
      long var3 = System.currentTimeMillis();
      JMSProducer var5 = this.checkClosedReconnectGetProducer();

      try {
         var5.send(var1, var2);
      } catch (weblogic.jms.common.JMSException var7) {
         this.nonIdempotentJMSProducer(var3, var5, var7).send(var1, var2);
      }

   }

   public void send(Destination var1, Message var2, int var3, int var4, long var5) throws JMSException {
      long var7 = System.currentTimeMillis();
      JMSProducer var9 = this.getPhysicalJMSProducer();
      JMSProducer var10 = this.checkClosedReconnectGetProducer(var7, var9);
      long var11 = var5;
      if (var9 != var10 && var5 > 0L) {
         long var13 = System.currentTimeMillis() - var7;
         if (var13 < var5) {
            var11 = var5 - var13;
         }
      }

      try {
         var10.send(var1, var2, var3, var4, var11);
      } catch (weblogic.jms.common.JMSException var16) {
         var10 = this.nonIdempotentJMSProducer(var7, var10, var16);
         if (var9 != var10 && var5 > 0L) {
            long var14 = System.currentTimeMillis() - var7;
            if (var14 < var5) {
               var5 -= var14;
            }
         }

         var10.send(var1, var2, var3, var4, var5);
      }

   }

   public void sendAsync(Message var1, CompletionListener var2) {
      this.getPhysicalJMSProducer().sendAsync(var1, var2);
   }

   public void sendAsync(Message var1, int var2, int var3, long var4, CompletionListener var6) {
      this.getPhysicalJMSProducer().sendAsync(var1, var2, var3, var4, var6);
   }

   public void sendAsync(Destination var1, Message var2, CompletionListener var3) {
      this.getPhysicalJMSProducer().sendAsync(var1, var2, var3);
   }

   public void sendAsync(Destination var1, Message var2, int var3, int var4, long var5, CompletionListener var7) {
      this.getPhysicalJMSProducer().sendAsync(var1, var2, var3, var4, var5, var7);
   }
}
