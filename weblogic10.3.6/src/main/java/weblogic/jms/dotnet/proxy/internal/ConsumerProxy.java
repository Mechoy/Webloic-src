package weblogic.jms.dotnet.proxy.internal;

import java.security.PrivilegedExceptionAction;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import weblogic.jms.client.WLConsumerImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.common.ObjectMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyBytesMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyConsumerCloseRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyConsumerReceiveRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyConsumerReceiveResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyConsumerSetListenerRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyConsumerSetListenerResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyHdrMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyMapMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyObjectMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyStreamMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyTextMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyVoidResponse;
import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.ReceivedTwoWay;
import weblogic.jms.dotnet.transport.ServiceTwoWay;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.jms.dotnet.transport.TransportError;
import weblogic.jms.extensions.WLAsyncSession;
import weblogic.security.subject.AbstractSubject;

public final class ConsumerProxy extends BaseProxy implements ServiceTwoWay {
   private MessageConsumer consumer;
   private WLAsyncSession wlAsyncSession;
   private WLConsumerImpl wlConsumerImpl;
   private ProxyMessageListener listener;
   private static final boolean USE_ASYNC;

   ConsumerProxy(long var1, SessionProxy var3, MessageConsumer var4) {
      super(var1, var3);
      this.consumer = var4;
      if (USE_ASYNC && var3.getSession() instanceof WLAsyncSession) {
         this.wlAsyncSession = (WLAsyncSession)var3.getSession();
      }

      if (var4 instanceof WLConsumerImpl) {
         this.wlConsumerImpl = (WLConsumerImpl)var4;
      }

   }

   public final SessionProxy getSession() {
      return (SessionProxy)this.parent;
   }

   public final String toString() {
      return this.consumer.toString();
   }

   final void close() throws JMSException {
      this.close((ProxyConsumerCloseRequest)null);
   }

   final void close(final ProxyConsumerCloseRequest var1) throws JMSException {
      this.unregister();
      this.parent.remove(this.serviceId);
      JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            if (ConsumerProxy.this.wlConsumerImpl != null && var1 != null) {
               ConsumerProxy.this.wlConsumerImpl.close(var1.getSequenceNumber());
            }

            if (ConsumerProxy.this.consumer != null) {
               ConsumerProxy.this.consumer.close();
            }

            ConsumerProxy.this.wlConsumerImpl = null;
            ConsumerProxy.this.consumer = null;
            return null;
         }
      });
   }

   void unregister() {
      this.getTransport().unregisterService(this.serviceId);
   }

   void remove(long var1) {
   }

   private final void receiveAsync(ProxyConsumerReceiveRequest var1, ReceivedTwoWay var2) {
      final ProxyConsumerReceiveRequest var3 = var1;
      var1.setReceivedTwoWay(var2);
      var1.setConsumerProxy(this);
      final long var4 = var1.getTimeout();
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("ReceiveAsync(): timeout = " + var4);
      }

      try {
         JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
            public Object run() {
               if (var4 == 0L) {
                  ConsumerProxy.this.wlAsyncSession.receiveNoWaitAsync(ConsumerProxy.this.consumer, var3);
               } else if (var4 == -1L) {
                  ConsumerProxy.this.wlAsyncSession.receiveAsync(ConsumerProxy.this.consumer, var3);
               } else {
                  ConsumerProxy.this.wlAsyncSession.receiveAsync(ConsumerProxy.this.consumer, var4, var3);
               }

               return var3;
            }
         });
      } catch (Throwable var7) {
         var1.onException(var7);
      }

   }

   public void receiveException(Throwable var1, ReceivedTwoWay var2) {
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("ReceiveAsync() failed:  throwable = " + var1);
      }

      this.sendResult(var2, new TransportError(var1));
   }

   private void sendResult(final ReceivedTwoWay var1, final MarshalWritable var2) {
      try {
         JMSSecurityHelper.doAs(anonymous, new PrivilegedExceptionAction() {
            public Object run() {
               var1.send(var2);
               return var1;
            }
         });
      } catch (Throwable var4) {
      }

   }

   public void receiveCompletion(Object var1, ReceivedTwoWay var2) {
      this.sendResult(var2, this.asyncCompletion(var1));
   }

   private MarshalWritable asyncCompletion(Object var1) {
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("ReceiveAsync() returned:  response = " + var1);
      }

      if (var1 instanceof Throwable) {
         return new TransportError((Throwable)var1);
      } else if (var1 != null && !(var1 instanceof Message)) {
         return new TransportError(new Error("Wrong response " + var1 + " is " + var1.getClass().getName()));
      } else {
         try {
            return this.complete((Message)var1);
         } catch (Throwable var3) {
            return new TransportError(var3);
         }
      }
   }

   private MarshalWritable complete(Message var1) throws JMSException {
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("Received: JMS message = " + var1);
         if (var1 instanceof ObjectMessage) {
            JMSDebug.JMSDotNetProxy.debug(" Received: JMS message = " + ((ObjectMessageImpl)var1).getPayload());
         }
      }

      ProxyMessageImpl var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var2 = createProxyMessageImpl(var1);
      }

      return new ProxyConsumerReceiveResponse(var2);
   }

   private final MarshalWritable receiveSync(ProxyConsumerReceiveRequest var1) {
      final long var2 = var1.getTimeout();
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("ReceiveSync(): timeout = " + var2);
      }

      try {
         Message var4 = (Message)JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
            public Object run() throws JMSException {
               if (var2 == 0L) {
                  return ConsumerProxy.this.consumer.receiveNoWait();
               } else {
                  return var2 == -1L ? ConsumerProxy.this.consumer.receive() : ConsumerProxy.this.consumer.receive(var2);
               }
            }
         });
         return this.complete(var4);
      } catch (JMSException var6) {
         return new TransportError(var6);
      } catch (Throwable var7) {
         return new TransportError(var7);
      }
   }

   private final MarshalWritable setListener(final ProxyConsumerSetListenerRequest var1) throws JMSException {
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("SetListener: hasListener = " + var1.getHasListener());
      }

      if (var1.getHasListener()) {
         this.wlConsumerImpl.setProxyID(var1.getListenerServiceId());
         this.listener = new ProxyMessageListener(this, var1.getListenerServiceId());
      } else {
         this.listener = null;
      }

      JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            if (ConsumerProxy.this.wlConsumerImpl != null) {
               ConsumerProxy.this.wlConsumerImpl.setMessageListener(ConsumerProxy.this.listener, var1.getSequenceNumber());
               return null;
            } else {
               throw new JMSException("not implemented");
            }
         }
      });
      return new ProxyConsumerSetListenerResponse();
   }

   public final void invoke(ReceivedTwoWay var1) {
      MarshalReadable var2 = var1.getRequest();
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("invoke():  code = " + var2.getMarshalTypeCode() + " request = " + var2);
      }

      Object var3 = null;

      try {
         switch (var2.getMarshalTypeCode()) {
            case 12:
               this.close((ProxyConsumerCloseRequest)var2);
               var3 = ProxyVoidResponse.THE_ONE;
               break;
            case 16:
               if (this.wlAsyncSession != null) {
                  this.receiveAsync((ProxyConsumerReceiveRequest)var2, var1);
                  return;
               }

               var3 = this.receiveSync((ProxyConsumerReceiveRequest)var2);
               break;
            case 18:
               var3 = this.setListener((ProxyConsumerSetListenerRequest)var2);
               break;
            default:
               var3 = new TransportError("Invalid MarshalReadableType : " + var2.getMarshalTypeCode(), false);
         }
      } catch (JMSException var5) {
         var3 = new TransportError(var5);
      }

      var1.send((MarshalWritable)var3);
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

   public AbstractSubject getSubject() {
      return this.parent.getSubject();
   }

   static ProxyMessageImpl createProxyMessageImpl(Message var0) throws JMSException {
      if (var0 instanceof TextMessage) {
         return new ProxyTextMessageImpl((TextMessage)var0);
      } else if (var0 instanceof BytesMessage) {
         return new ProxyBytesMessageImpl((BytesMessage)var0);
      } else if (var0 instanceof MapMessage) {
         return new ProxyMapMessageImpl((MapMessage)var0);
      } else if (var0 instanceof StreamMessage) {
         return new ProxyStreamMessageImpl((StreamMessage)var0);
      } else if (var0 instanceof ObjectMessage) {
         return new ProxyObjectMessageImpl((ObjectMessage)var0);
      } else if (var0 instanceof Message) {
         return new ProxyHdrMessageImpl(var0);
      } else {
         throw new AssertionError("Unexpected message type " + var0.getClass().getName());
      }
   }

   static {
      String var0 = null;
      boolean var3 = true;

      try {
         var0 = System.getProperty("weblogic.jms.dotnet.RecvAsync");
         if (var0 != null) {
            var3 = Boolean.valueOf(var0);
         }

         if (!var3) {
            System.err.println("\n\nweblogic.jms.dotnet.proxy.internal.ConsumerProxy -Dweblogic.jms.dotnet.RecvAsync=" + var3);
         }
      } catch (Throwable var5) {
         if (var0 != null) {
            System.err.println("\n\nproblem processing -Dweblogic.jms.dotnet.RecvAsync=" + var0);
         }

         var5.printStackTrace();
      }

      USE_ASYNC = var3;
   }

   public final class ProxyMessageListener implements MessageListener {
      private final ConsumerProxy consumer;
      private final long listenerServiceId;

      ProxyMessageListener(ConsumerProxy var2, long var3) {
         this.consumer = var2;
         this.listenerServiceId = var3;
      }

      SessionProxy getSession() {
         return (SessionProxy)this.consumer.getParent();
      }

      public final String toString() {
         return this.consumer.toString();
      }

      public void onMessage(Message var1) {
      }
   }
}
