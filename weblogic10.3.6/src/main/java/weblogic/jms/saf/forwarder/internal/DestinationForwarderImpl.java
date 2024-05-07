package weblogic.jms.saf.forwarder.internal;

import java.util.HashMap;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jms.cache.CacheContextInfo;
import weblogic.jms.client.JMSProducer;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.deployer.DeployerConstants;
import weblogic.jms.forwarder.DestinationName;
import weblogic.jms.forwarder.SessionRuntimeContext;
import weblogic.jms.forwarder.dd.DDLoadBalancerDelegate;
import weblogic.jms.forwarder.dd.internal.DDInfoImpl;
import weblogic.jms.forwarder.dd.internal.DDLoadBalancerDelegateImpl;
import weblogic.jms.forwarder.internal.DestinationNameImpl;
import weblogic.jms.forwarder.internal.SessionRuntimeContextImpl;
import weblogic.jms.saf.forwarder.DestinationForwarder;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.Timer;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManager;

public class DestinationForwarderImpl implements DestinationForwarder, NakedTimerListener {
   private DestinationName destinationName;
   private CacheContextInfo remoteContextInfo;
   private String jmsConnectionFactoryName;
   private SessionRuntimeContext jmsSessionRuntimeContext;
   private JMSProducer messageProducer;
   private long forarderCreateTimerInterval;
   private long forwarderCreateInitialDelay;
   private TimerManager timerManager;
   private Timer producerCreatePoller;
   private int destinationType;
   private Destination destination;
   private boolean isDistributedDestination;
   private HashMap nullDDForwarderMap;
   private DDExactlyOnceForwarder ddExactlyOnceForwarder;
   private boolean isInLocalCluster;

   public DestinationForwarderImpl(CacheContextInfo var1, String var2, String var3, String var4, int var5, String var6, String var7) {
      this(var1, var2, var3, var4, var5, 10000L, 100000L, var6, var7);
   }

   public DestinationForwarderImpl(CacheContextInfo var1, String var2, String var3, String var4, int var5, long var6, long var8, String var10, String var11) {
      this.forarderCreateTimerInterval = 10000L;
      this.forwarderCreateInitialDelay = 100000L;
      this.timerManager = TimerManagerFactory.getTimerManagerFactory().getTimerManager("weblogic.jms.JMSProducerTimer", (WorkManager)null);
      this.nullDDForwarderMap = new HashMap();
      this.remoteContextInfo = var1;
      this.destinationName = new DestinationNameImpl(var4, var3);
      this.destinationType = var5;
      if (var2 == null) {
         this.jmsConnectionFactoryName = DeployerConstants.DEFAULT_FACTORY_NAMES[0][0];
      }

      this.jmsConnectionFactoryName = var2;
      this.forwarderCreateInitialDelay = var6;
      this.forarderCreateTimerInterval = var8;
      this.createJMSProducer();
   }

   public DestinationForwarderImpl(CacheContextInfo var1, String var2) {
      this(var1, var2, 10000L, 100000L);
   }

   public DestinationForwarderImpl(CacheContextInfo var1, String var2, long var3, long var5) {
      this.forarderCreateTimerInterval = 10000L;
      this.forwarderCreateInitialDelay = 100000L;
      this.timerManager = TimerManagerFactory.getTimerManagerFactory().getTimerManager("weblogic.jms.JMSProducerTimer", (WorkManager)null);
      this.nullDDForwarderMap = new HashMap();
      this.remoteContextInfo = var1;
      this.jmsConnectionFactoryName = var2;
      this.forwarderCreateInitialDelay = var3;
      this.forarderCreateTimerInterval = var5;
   }

   public int getDestinationType() {
      return this.destinationType;
   }

   public long getForwarderCreateTimerInterval() {
      return this.forarderCreateTimerInterval;
   }

   public void setForwarderCreateTimerInterval(long var1) {
      this.forarderCreateTimerInterval = var1;
      if (this.producerCreatePoller != null) {
         this.timerManager.stop();
         this.startProducerCreatePoller();
      }

   }

   public long getForwarderCreateInitialDelay() {
      return this.forwarderCreateInitialDelay;
   }

   public void setForwarderCreateInitialDelay(long var1) {
      this.forwarderCreateInitialDelay = var1;
      if (this.producerCreatePoller != null) {
         this.timerManager.stop();
         this.startProducerCreatePoller();
      }

   }

   private void createJMSProducer() {
      try {
         this.jmsSessionRuntimeContext = new SessionRuntimeContextImpl(this.remoteContextInfo.getProviderUrl(), this.remoteContextInfo.getUserName(), this.remoteContextInfo.getPassword(), this.jmsConnectionFactoryName);
         Session var1 = this.jmsSessionRuntimeContext.getJMSSession();
         if (this.destinationName != null) {
            this.initDestination(this.jmsSessionRuntimeContext);
         }

         Destination var2 = null;
         if (this.destination != null && !(this.destination instanceof DistributedDestinationImpl)) {
            var2 = this.destination;
         }

         if (this.producerCreatePoller != null) {
            this.producerCreatePoller.cancel();
         }

         this.messageProducer = (JMSProducer)var1.createProducer(var2);
      } catch (NamingException var3) {
         this.startProducerCreatePoller();
      } catch (JMSException var4) {
         this.startProducerCreatePoller();
      }

   }

   private void startProducerCreatePoller() {
      this.producerCreatePoller = this.timerManager.schedule(this, this.forwarderCreateInitialDelay, this.forarderCreateTimerInterval);
   }

   private void initDestination(SessionRuntimeContext var1) throws NamingException, JMSException {
      String var2 = this.destinationName.getJNDIName();
      Object var3;
      if (var2 != null) {
         Context var4 = var1.getProviderContext();
         var3 = (Destination)var4.lookup(var2);
      } else {
         String var6 = this.destinationName.getConfigName();
         Session var5 = var1.getJMSSession();
         if (this.destinationType == 1) {
            var3 = var5.createQueue(var6);
         } else {
            var3 = var5.createTopic(var6);
         }
      }

      this.destination = (Destination)var3;
      this.isDistributedDestination = var3 instanceof DistributedDestinationImpl;
      if (this.isDistributedDestination) {
         this.findOrCreateDDExactlyOnceForwardHandler((Destination)var3);
      }

   }

   private DDExactlyOnceForwarder findOrCreateDDExactlyOnceForwardHandler(Destination var1) throws JMSException {
      DDExactlyOnceForwarder var2 = (DDExactlyOnceForwarder)this.nullDDForwarderMap.get(var1);
      if (var2 == null) {
         var2 = new DDExactlyOnceForwarder((DistributedDestinationImpl)var1);
         this.nullDDForwarderMap.put(var1, var2);
      }

      return var2;
   }

   public void timerExpired(Timer var1) {
      this.producerCreatePoller.cancel();
      this.createJMSProducer();
   }

   public long getTimeToDeliver() throws JMSException {
      return this.messageProducer.getTimeToDeliver();
   }

   public void setTimeToDeliver(long var1) throws JMSException {
      this.messageProducer.setTimeToDeliver(var1);
   }

   public int getRedeliveryLimit() throws JMSException {
      return this.messageProducer.getRedeliveryLimit();
   }

   public void setRedeliveryLimit(int var1) throws JMSException {
      this.messageProducer.setRedeliveryLimit(var1);
   }

   public long getSendTimeout() throws JMSException {
      return this.messageProducer.getSendTimeout();
   }

   public void setSendTimeout(long var1) throws JMSException {
      this.messageProducer.setSendTimeout(var1);
   }

   public String getUnitOfOrder() throws JMSException {
      return this.messageProducer.getUnitOfOrder();
   }

   public void setUnitOfOrder(String var1) throws JMSException {
      this.messageProducer.setUnitOfOrder(var1);
   }

   public void setUnitOfOrder() throws JMSException {
      this.messageProducer.setUnitOfOrder();
   }

   public void forward(Message var1, int var2, int var3, long var4) throws JMSException {
      if (this.isDistributedDestination) {
         this.ddExactlyOnceForwarder.forward(var1, var2, var3, var4);
      } else {
         this.messageProducer.forward(var1, var2, var3, var4);
      }

   }

   public void forward(Message var1) throws JMSException {
      if (this.isDistributedDestination) {
         this.ddExactlyOnceForwarder.forward(var1);
      } else {
         this.messageProducer.forward(var1);
      }

   }

   public void forward(Destination var1, Message var2, int var3, int var4, long var5) throws JMSException {
      if (this.isDistributedDestination) {
         throw new JMSException(" Cannot switch destinations for non null WLForwarder");
      } else {
         if (var1 instanceof DistributedDestinationImpl) {
            DDExactlyOnceForwarder var7 = this.findOrCreateDDExactlyOnceForwardHandler(var1);
            var7.forward(var2, var3, var4, var5);
         } else {
            this.messageProducer.forward(var1, var2, var3, var4, var5);
         }

      }
   }

   public void forward(Destination var1, Message var2) throws JMSException {
      if (this.isDistributedDestination) {
         throw new JMSException(" Cannot switch destinations for non null WLMessageProducer");
      } else {
         if (var1 instanceof DistributedDestinationImpl) {
            DDExactlyOnceForwarder var3 = this.findOrCreateDDExactlyOnceForwardHandler(var1);
            var3.forward(var2);
         } else {
            this.messageProducer.forward(var1, var2);
         }

      }
   }

   public void setCompressionThreshold(int var1) throws JMSException {
      this.messageProducer.setCompressionThreshold(var1);
   }

   public int getCompressionThreshold() throws JMSException {
      return this.messageProducer.getCompressionThreshold();
   }

   public void setDeliveryMode(int var1) throws JMSException {
      this.messageProducer.setDeliveryMode(var1);
   }

   public int getDeliveryMode() throws JMSException {
      return this.messageProducer.getDeliveryMode();
   }

   public void setPriority(int var1) throws JMSException {
      this.messageProducer.setPriority(var1);
   }

   public int getPriority() throws JMSException {
      return this.messageProducer.getPriority();
   }

   public void setTimeToLive(long var1) throws JMSException {
      this.messageProducer.setTimeToLive(var1);
   }

   public long getTimeToLive() throws JMSException {
      return this.messageProducer.getTimeToLive();
   }

   public Destination getDestination() throws JMSException {
      return this.messageProducer.getDestination();
   }

   public void close() throws JMSException {
      this.messageProducer.close();
   }

   public void setDisableMessageID(boolean var1) throws JMSException {
      this.messageProducer.setDisableMessageID(var1);
   }

   public boolean getDisableMessageID() throws JMSException {
      return this.messageProducer.getDisableMessageID();
   }

   public void setDisableMessageTimestamp(boolean var1) throws JMSException {
      this.messageProducer.setDisableMessageTimestamp(var1);
   }

   public boolean getDisableMessageTimestamp() throws JMSException {
      return this.messageProducer.getDisableMessageTimestamp();
   }

   public void send(Message var1) throws JMSException {
      if (this.isDistributedDestination) {
         this.ddExactlyOnceForwarder.forward(var1);
      } else {
         this.messageProducer.send(var1);
      }

   }

   public void send(Message var1, int var2, int var3, long var4) throws JMSException {
      if (this.isDistributedDestination) {
         this.ddExactlyOnceForwarder.send(var1, var2, var3, var4);
      } else {
         this.messageProducer.send(var1, var2, var3, var4);
      }

   }

   public void send(Destination var1, Message var2, int var3, int var4, long var5) throws JMSException {
      if (this.isDistributedDestination) {
         throw new JMSException(" Cannot switch destinations for non null WLForwarder");
      } else {
         if (var1 instanceof DistributedDestinationImpl) {
            DDExactlyOnceForwarder var7 = this.findOrCreateDDExactlyOnceForwardHandler(var1);
            var7.send(var2, var3, var4, var5);
         } else {
            this.messageProducer.send(var1, var2, var3, var4, var5);
         }

      }
   }

   public void send(Destination var1, Message var2) throws JMSException {
      if (this.isDistributedDestination) {
         throw new JMSException(" Cannot switch destinations for non null WLMessageProducer");
      } else {
         if (var1 instanceof DistributedDestinationImpl) {
            DDExactlyOnceForwarder var3 = this.findOrCreateDDExactlyOnceForwardHandler(var1);
            var3.send(var2);
         } else {
            this.messageProducer.send(var1, var2);
         }

      }
   }

   private class DDExactlyOnceForwarder {
      private DistributedDestinationImpl destination;
      private DDLoadBalancerDelegate ddLoadBalancerDelegate;

      DDExactlyOnceForwarder(DistributedDestinationImpl var2) throws JMSException {
         this.destination = var2;
         DDInfoImpl var3 = new DDInfoImpl(var2.getDDJNDIName(), var2.getName(), var2.getDestinationInstanceType(), var2.getApplicationName(), var2.getModuleName(), var2.getLoadBalancingPolicy(), var2.getMessageForwardingPolicy());
         this.ddLoadBalancerDelegate = new DDLoadBalancerDelegateImpl(DestinationForwarderImpl.this.jmsSessionRuntimeContext, var3, (PersistentStoreXA)null);
      }

      void forward(Message var1, int var2, int var3, long var4) throws JMSException {
         synchronized(this.ddLoadBalancerDelegate) {
            try {
               Destination var7 = this.ddLoadBalancerDelegate.loadBalance();
               DestinationForwarderImpl.this.messageProducer.forward(var7, var1, var2, var3, var4);
            } catch (Throwable var9) {
            }

         }
      }

      void forward(Message var1) throws JMSException {
         synchronized(this.ddLoadBalancerDelegate) {
            try {
               Destination var3 = this.ddLoadBalancerDelegate.loadBalance();
               DestinationForwarderImpl.this.messageProducer.forward(var3, var1);
            } catch (Throwable var5) {
            }

         }
      }

      void send(Message var1, int var2, int var3, long var4) throws JMSException {
         synchronized(this.ddLoadBalancerDelegate) {
            try {
               Destination var7 = this.ddLoadBalancerDelegate.loadBalance();
               DestinationForwarderImpl.this.messageProducer.send(var7, var1, var2, var3, var4);
            } catch (Throwable var9) {
            }

         }
      }

      void send(Message var1) throws JMSException {
         synchronized(this.ddLoadBalancerDelegate) {
            try {
               Destination var3 = this.ddLoadBalancerDelegate.loadBalance();
               DestinationForwarderImpl.this.messageProducer.send(var3, var1);
            } catch (Throwable var5) {
            }

         }
      }
   }
}
