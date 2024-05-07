package weblogic.jms.dotnet.proxy.internal;

import java.security.PrivilegedExceptionAction;
import java.util.HashSet;
import java.util.Iterator;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.Topic;
import javax.jms.TopicSession;
import weblogic.jms.client.MMessageListener;
import weblogic.jms.client.MessageWrapper;
import weblogic.jms.client.WLSessionImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.dotnet.proxy.protocol.ProxyConsumerCreateRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyConsumerCreateResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyDestinationCreateRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyDestinationCreateResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyDestinationImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyProducerCreateRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyProducerCreateResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyPushMessageListRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyPushMessageRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyRemoveSubscriptionRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxySessionAcknowledgeRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxySessionCloseRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxySessionRecoverRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxySessionRecoverResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxySessionWindowTurnRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyVoidResponse;
import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.ReceivedOneWay;
import weblogic.jms.dotnet.transport.ReceivedTwoWay;
import weblogic.jms.dotnet.transport.SendHandlerOneWay;
import weblogic.jms.dotnet.transport.ServiceOneWay;
import weblogic.jms.dotnet.transport.ServiceTwoWay;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.jms.dotnet.transport.TransportError;
import weblogic.jms.extensions.WLAsyncSession;
import weblogic.security.subject.AbstractSubject;

public class SessionProxy extends BaseProxy implements ServiceTwoWay, ServiceOneWay, MMessageListener {
   private Session session;
   private int ackMode;
   private final long sessionMsgListenerServiceId;
   private WLAsyncSession wlAsyncSession;
   private WLSessionImpl wlSessionImpl;
   private HashSet<Long> serviceIds = new HashSet();
   private static final int MAX_PUSH_COUNT = Integer.MAX_VALUE;

   protected SessionProxy(long var1, ConnectionProxy var3, Session var4, int var5, long var6) {
      super(var1, var3);
      this.session = var4;
      this.ackMode = var5;
      this.sessionMsgListenerServiceId = var6;
      if (var4 instanceof WLAsyncSession) {
         this.wlAsyncSession = (WLAsyncSession)var4;
         this.wlSessionImpl = (WLSessionImpl)var4;
         this.wlSessionImpl.setMMessageListener(this);
      } else {
         this.wlAsyncSession = null;
         this.wlSessionImpl = null;
      }

   }

   public final String toString() {
      return this.session.toString();
   }

   boolean getTransacted() throws JMSException {
      return this.session.getTransacted();
   }

   final int getAcknowledgeMode() {
      return this.ackMode;
   }

   private final void unsubscribe(final String var1) throws JMSException {
      JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            synchronized(SessionProxy.this) {
               ((TopicSession)SessionProxy.this.session).unsubscribe(var1);
               return null;
            }
         }
      });
   }

   private final TemporaryQueue createTemporaryQueue(String var1) throws JMSException {
      throw new JMSException("createTemporaryQueue() is not supported");
   }

   private final TemporaryTopic createTemporaryTopic(String var1) throws JMSException {
      throw new JMSException("createTemporaryTopic() is not supported");
   }

   void unregister() {
      this.getTransport().unregisterService(this.serviceId);
      synchronized(this) {
         Iterator var2 = this.serviceIds.iterator();

         while(var2.hasNext()) {
            long var3 = (Long)var2.next();
            this.getTransport().unregisterService(var3);
         }

         this.serviceIds.clear();
      }
   }

   private final void close(long var1) throws JMSException {
      this.closeInner(true, var1);
   }

   final void close() throws JMSException {
      this.closeInner(false, 0L);
   }

   private final void closeInner(final boolean var1, final long var2) throws JMSException {
      synchronized(this) {
         if ((this.state & 1) != 0) {
            return;
         }

         this.state = 1;
      }

      this.unregister();
      this.parent.remove(this.serviceId);
      JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            synchronized(SessionProxy.this) {
               if (var1 && SessionProxy.this.wlSessionImpl != null) {
                  SessionProxy.this.wlSessionImpl.close(var2);
               } else {
                  SessionProxy.this.session.close();
               }

               return null;
            }
         }
      });
   }

   private final MarshalWritable createConsumer(final ProxyConsumerCreateRequest var1) throws JMSException {
      final ProxyDestinationImpl var2 = var1.getDestination();
      String var3 = var1.getClientId();
      final String var4 = var1.getName();
      MessageConsumer var5 = null;
      String var6 = var1.getSelector();
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("Creating consumer on " + var2.getJMSDestination() + " selector = " + var6 + " noLocal = " + var1.getNoLocal() + " name = " + var4);
      }

      if (var4 == null) {
         var5 = (MessageConsumer)JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
            public Object run() throws JMSException {
               synchronized(SessionProxy.this) {
                  return SessionProxy.this.session.createConsumer(var2.getJMSDestination(), var1.getSelector(), var1.getNoLocal());
               }
            }
         });
      } else {
         var5 = (MessageConsumer)JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
            public Object run() throws JMSException {
               synchronized(SessionProxy.this) {
                  return SessionProxy.this.session.createDurableSubscriber((Topic)var2.getJMSDestination(), var4, var1.getSelector(), var1.getNoLocal());
               }
            }
         });
      }

      long var7 = this.getTransport().allocateServiceID();
      ConsumerProxy var9 = new ConsumerProxy(var7, this, var5);
      this.getTransport().registerService(var7, var9);
      this.addServiceId(var7);
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("Created consumer: id = " + var7);
      }

      return new ProxyConsumerCreateResponse(var7);
   }

   private final MarshalWritable createProducer(ProxyProducerCreateRequest var1) throws JMSException {
      ProxyDestinationImpl var2 = var1.getDestination();
      final Destination var3 = var2 == null ? null : var2.getJMSDestination();
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("Creating producer on " + var3);
      }

      MessageProducer var4 = (MessageProducer)JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            synchronized(SessionProxy.this) {
               return SessionProxy.this.session.createProducer(var3);
            }
         }
      });
      long var5 = this.getTransport().allocateServiceID();
      ProducerProxy var7 = new ProducerProxy(var5, this, var4, var2);
      this.getTransport().registerService(var5, var7);
      this.addServiceId(var5);
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("Created producer: id = " + var5);
      }

      return new ProxyProducerCreateResponse(var5, var4);
   }

   private void acknowledgeAsync(ProxySessionAcknowledgeRequest var1, ReceivedTwoWay var2) {
      final ProxySessionAcknowledgeRequest var3 = var1;
      var1.setReceivedTwoWay(var2);
      var1.setSessionProxy(this);

      try {
         if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
            JMSDebug.JMSDotNetProxy.debug("AcknowledgeAsync(): transacted=" + this.session.getTransacted() + " doCommit=" + var3.isDoCommit() + " seq=" + var3.getSequenceNumber());
         }

         JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
            public Object run() throws JMSException {
               synchronized(SessionProxy.this) {
                  SessionProxy.this.wlAsyncSession.acknowledgeAsync(var3, var3);
                  return null;
               }
            }
         });
      } catch (JMSException var5) {
         var1.onException(var5);
      }

   }

   public void receiveCompletion(ProxySessionAcknowledgeRequest var1, Object var2, ReceivedTwoWay var3) {
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("AcknowledgeAsync() returned:  response = " + var2);
      }

      if (var2 instanceof Throwable) {
         this.receiveException((Throwable)var2, var3);
      } else {
         ProxyVoidResponse var4 = ProxyVoidResponse.THE_ONE;
         var3.send(var4);
      }
   }

   public void receiveException(Throwable var1, ReceivedTwoWay var2) {
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("AcknowledgeAsync() failed:  throwable = " + var1);
      }

      var2.send(new TransportError(var1));
   }

   private MarshalWritable acknowledgeSync(final ProxySessionAcknowledgeRequest var1) throws JMSException {
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("AcknowledgeSync(): transacted=" + this.session.getTransacted() + " doCommit=" + var1.isDoCommit() + " seq=" + var1.getSequenceNumber());
      }

      JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            synchronized(SessionProxy.this) {
               if (var1.isDoCommit()) {
                  if (SessionProxy.this.wlSessionImpl != null) {
                     SessionProxy.this.wlSessionImpl.commit(var1.getSequenceNumber());
                  } else {
                     SessionProxy.this.session.commit();
                  }

                  return null;
               } else {
                  throw new AssertionError("not implemented");
               }
            }
         }
      });
      return ProxyVoidResponse.THE_ONE;
   }

   private MarshalWritable recover(final ProxySessionRecoverRequest var1) throws JMSException {
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("recover(): transacted=" + this.session.getTransacted() + " doRollback=" + var1.isDoRollback() + " seq=" + var1.getSequenceNumber());
      }

      int var2 = (Integer)JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            synchronized(SessionProxy.this) {
               int var2 = 0;
               if (SessionProxy.this.wlSessionImpl != null) {
                  if (var1.isDoRollback()) {
                     var2 = SessionProxy.this.wlSessionImpl.rollback(var1.getSequenceNumber());
                  } else {
                     var2 = SessionProxy.this.wlSessionImpl.recover(var1.getSequenceNumber());
                  }

                  return new Integer(var2);
               } else {
                  if (var1.isDoRollback()) {
                     SessionProxy.this.session.rollback();
                  } else {
                     SessionProxy.this.session.recover();
                  }

                  return new Integer(var2);
               }
            }
         }
      });
      return new ProxySessionRecoverResponse(var2);
   }

   private synchronized void windowTurn(ProxySessionWindowTurnRequest var1) {
      if (this.wlSessionImpl != null) {
         try {
            this.wlSessionImpl.removePendingWTMessage(var1.getSequenceNumber());
         } catch (Exception var3) {
         }
      }

   }

   private final MarshalWritable close(ProxySessionCloseRequest var1) throws JMSException {
      this.close(var1.getSequenceNumber());
      return ProxyVoidResponse.THE_ONE;
   }

   private final MarshalWritable createDestination(ProxyDestinationCreateRequest var1) throws JMSException {
      final int var2 = var1.getDestinationType();
      final boolean var3 = var1.isTemporary();
      final String var4 = var1.getDestinationName();
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("CreateDestination(): type = " + (var2 == 1 ? "Queue" : "Topic") + " name =  " + var4 + " isTemp = " + var3);
      }

      Destination var5 = (Destination)JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            synchronized(SessionProxy.this) {
               if (var2 == 1) {
                  return var3 ? SessionProxy.this.createTemporaryQueue(var4) : SessionProxy.this.session.createQueue(var4);
               } else if (var2 == 2) {
                  return var3 ? SessionProxy.this.createTemporaryTopic(var4) : SessionProxy.this.session.createTopic(var4);
               } else {
                  return null;
               }
            }
         }
      });
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("CreateDestination(): found destination = " + var5);
      }

      return new ProxyDestinationCreateResponse(new ProxyDestinationImpl(var4, var5));
   }

   private final ProxyPushMessageRequest createPush(MessageWrapper var1, boolean var2) throws JMSException {
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("CreatePush:  Pushing  msg=" + var1.getMessageImpl() + " seq=" + var1.getSequence() + " gen=" + var1.getGeneration());
      }

      ProxyMessageImpl var3 = ConsumerProxy.createProxyMessageImpl(var1.getMessageImpl());
      var3.setSequenceNumber(var1.getSequence());
      var3.setPipelineGeneration(var1.getGeneration());
      var3.setDeliveryCount(var1.getDeliveryCount());
      if (var1.getDeliveryCount() > 1) {
         var3.setRedelivered(true);
      }

      return new ProxyPushMessageRequest(var1.getProxyId(), var3, var2);
   }

   private final void pushMany(final ProxyPushMessageListRequest var1) throws JMSException {
      final SendHandlerOneWay var2 = this.getTransport().createOneWay(this.sessionMsgListenerServiceId, this.sessionMsgListenerServiceId);
      JMSSecurityHelper.doAs(anonymous, new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            var2.send(var1);
            return null;
         }
      });
   }

   public final void onMessages(MessageWrapper var1, int var2) {
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("Starting onMessages");
      }

      Object var3 = null;

      try {
         ProxyPushMessageListRequest var4;
         try {
            for(MessageWrapper var5 = var1; var5 != null; this.pushMany(var4)) {
               int var6 = 0;

               for(var4 = new ProxyPushMessageListRequest(var2); var5 != null; var5 = var5.next()) {
                  boolean var7 = var5.next() == null;
                  var4.add(this.createPush(var5, var7));
                  ++var6;
                  if (var6 == Integer.MAX_VALUE) {
                     var5 = var5.next();
                     break;
                  }
               }
            }
         } catch (JMSException var13) {
            var3 = var13;
            throw new RuntimeException(var13);
         } catch (RuntimeException var14) {
            var3 = var14;
            throw var14;
         } catch (Error var15) {
            var3 = var15;
            throw var15;
         }
      } finally {
         if (var3 != null && JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
            JMSDebug.JMSDotNetProxy.debug("should never happen " + var3);
            ((Throwable)var3).printStackTrace();
         }

      }

   }

   public final void invoke(ReceivedOneWay var1) {
      if (!this.isShutdown()) {
         MarshalReadable var2 = var1.getRequest();
         if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
            JMSDebug.JMSDotNetProxy.debug("invoke(): one-way:  code = " + var2.getMarshalTypeCode() + " request = " + var2);
         }

         switch (var2.getMarshalTypeCode()) {
            case 49:
               this.windowTurn((ProxySessionWindowTurnRequest)var2);
               break;
            default:
               if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
                  JMSDebug.JMSDotNetProxy.debug("DEBUG: should never reach here, Session one-way: Unexpected type code" + var2.getMarshalTypeCode());
               }
         }

      }
   }

   private synchronized void addServiceId(long var1) throws JMSException {
      this.checkShutdownOrClosed("The session has been closed");
      this.serviceIds.add(var1);
   }

   synchronized void remove(long var1) {
      this.serviceIds.remove(var1);
   }

   public final void invoke(ReceivedTwoWay var1) {
      if (this.isShutdown()) {
         var1.send(new TransportError(new JMSException("The JMS service is shutting down")));
      } else {
         MarshalReadable var2 = var1.getRequest();
         ProxyVoidResponse var3 = ProxyVoidResponse.THE_ONE;
         if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
            JMSDebug.JMSDotNetProxy.debug("Invoking: code = " + var2.getMarshalTypeCode() + " request = " + var2);
         }

         Object var6;
         try {
            switch (var2.getMarshalTypeCode()) {
               case 13:
                  var6 = this.createConsumer((ProxyConsumerCreateRequest)var2);
                  break;
               case 14:
               case 15:
               case 16:
               case 17:
               case 18:
               case 19:
               case 21:
               case 22:
               case 24:
               case 25:
               case 26:
               case 30:
               case 31:
               default:
                  var6 = new TransportError("Invalid MarshalReadableType : " + var2.getMarshalTypeCode(), false);
                  break;
               case 20:
                  var6 = this.createDestination((ProxyDestinationCreateRequest)var2);
                  break;
               case 23:
                  var6 = this.createProducer((ProxyProducerCreateRequest)var2);
                  break;
               case 27:
                  this.unsubscribe(((ProxyRemoveSubscriptionRequest)var2).getName());
                  var6 = ProxyVoidResponse.THE_ONE;
                  break;
               case 28:
                  if (this.wlAsyncSession != null && !((ProxySessionAcknowledgeRequest)var2).isDoCommit()) {
                     this.acknowledgeAsync((ProxySessionAcknowledgeRequest)var2, var1);
                     return;
                  }

                  var6 = this.acknowledgeSync((ProxySessionAcknowledgeRequest)var2);
                  break;
               case 29:
                  this.close((ProxySessionCloseRequest)var2);
                  var6 = ProxyVoidResponse.THE_ONE;
                  break;
               case 32:
                  var6 = this.recover((ProxySessionRecoverRequest)var2);
            }
         } catch (JMSException var5) {
            var6 = new TransportError(var5);
         }

         var1.send((MarshalWritable)var6);
      }
   }

   public void onPeerGone(TransportError var1) {
   }

   public void onShutdown() {
   }

   public void onUnregister() {
   }

   public Transport getTransport() {
      return this.parent.getTransport();
   }

   InitialContextProxy getContext() {
      return this.parent.getContext();
   }

   Session getSession() {
      return this.session;
   }

   AbstractSubject getSubject() {
      return this.parent.getSubject();
   }
}
