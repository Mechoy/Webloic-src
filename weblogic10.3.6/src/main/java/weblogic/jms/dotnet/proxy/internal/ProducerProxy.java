package weblogic.jms.dotnet.proxy.internal;

import java.security.PrivilegedExceptionAction;
import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSMessageId;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyBytesMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyDestinationImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyHdrMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyMapMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyObjectMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyProducerSendRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyProducerSendResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyStreamMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyTextMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyVoidResponse;
import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.ReceivedTwoWay;
import weblogic.jms.dotnet.transport.ServiceTwoWay;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.jms.dotnet.transport.TransportError;
import weblogic.jms.extensions.AsyncSendResponseInfo;
import weblogic.jms.extensions.WLAsyncSession;
import weblogic.jms.extensions.WLMessage;
import weblogic.jms.extensions.WLMessageProducer;
import weblogic.security.subject.AbstractSubject;

public final class ProducerProxy extends BaseProxy implements ServiceTwoWay {
   private MessageProducer producer;
   private final ProxyDestinationImpl destination;
   private final WLAsyncSession wlAsyncSession;
   private static final boolean USE_ASYNC;

   ProducerProxy(long var1, SessionProxy var3, MessageProducer var4, ProxyDestinationImpl var5) throws JMSException {
      super(var1, var3);
      this.producer = var4;
      this.destination = var5;
      if (USE_ASYNC && var3.getSession() instanceof WLAsyncSession) {
         this.wlAsyncSession = (WLAsyncSession)var3.getSession();
      } else {
         this.wlAsyncSession = null;
      }

      if (var4 instanceof WLMessageProducer && ((WLMessageProducer)var4).getRedeliveryLimit() == -1) {
         ((WLMessageProducer)var4).setRedeliveryLimit(1073741823);
      }

   }

   private void send(final Destination var1, final Message var2, final int var3, final int var4, final long var5) throws JMSException {
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("SendSync(): destination = " + var1 + " message = " + var2 + " deliveryMode = " + var3 + " priority = " + var4 + " timeToLive = " + var5);
      }

      JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            ProducerProxy.this.producer.send(var1, var2, var3, var4, var5);
            return null;
         }
      });
   }

   private void send(final Message var1, final int var2, final int var3, final long var4) throws JMSException {
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("SendSync(): message = " + var1 + " deliveryMode = " + var2 + " priority = " + var3 + " timeToLive = " + var4);
      }

      JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            ProducerProxy.this.producer.send(var1, var2, var3, var4);
            return null;
         }
      });
   }

   private void sendAsync(final ProxyProducerSendRequest var1, final Destination var2, final Message var3, final int var4, final int var5, final long var6) throws JMSException {
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("SendAsync(): destination = " + this.destination + " message = " + var3 + " deliveryMode = " + var4 + " priority = " + var5 + " timeToLive = " + var6);
      }

      JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            ProducerProxy.this.wlAsyncSession.sendAsync((WLMessageProducer)ProducerProxy.this.producer, var2, var3, var4, var5, var6, var1);
            return null;
         }
      });
   }

   private void sendAsync(final ProxyProducerSendRequest var1, final Message var2, final int var3, final int var4, final long var5) throws JMSException {
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("SendAsync(): message = " + var2 + " deliveryMode = " + var3 + " priority = " + var4 + " timeToLive = " + var5);
      }

      JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            ProducerProxy.this.wlAsyncSession.sendAsync((WLMessageProducer)ProducerProxy.this.producer, var2, var3, var4, var5, var1);
            return null;
         }
      });
   }

   synchronized void close() throws JMSException {
      this.unregister();
      this.parent.remove(this.serviceId);
      JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            if (ProducerProxy.this.producer != null) {
               ProducerProxy.this.producer.close();
            }

            ProducerProxy.this.producer = null;
            return null;
         }
      });
   }

   void unregister() {
      this.getTransport().unregisterService(this.serviceId);
   }

   void remove(long var1) {
   }

   public String toString() {
      return this.producer.toString();
   }

   private final void sendAsync(ProxyProducerSendRequest var1, ReceivedTwoWay var2) {
      var1.setReceivedTwoWay(var2);
      var1.setProducerProxy(this);
      ProxyMessageImpl var3 = var1.getMessage();
      ProxyDestinationImpl var4 = var1.getDestination();
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("SendAsync() starts, destination on the request = " + var4 + " destination on the message = " + var3.getDestination());
      }

      Destination var5 = null;
      String var6 = null;

      try {
         if (this.producer instanceof WLMessageProducer) {
            try {
               var6 = ((WLMessageProducer)this.producer).getUnitOfOrder();
            } catch (JMSException var17) {
            }

            if (var3.propertyExists("JMS_BEA_UnitOfOrder")) {
               ((WLMessageProducer)this.producer).setUnitOfOrder((String)var3.getProperty("JMS_BEA_UnitOfOrder"));
               var3.removeProperty("JMS_BEA_UnitOfOrder");
            }
         }

         Message var7 = this.createJMSMessage(var3);
         if (var4 != null) {
            var5 = var4.getJMSDestination();
            if (var5 != null) {
               this.sendAsync(var1, var5, var7, var3.getDeliveryMode(), var3.getPriority(), var3.getExpiration());
            } else {
               var1.onException(new JMSException("Destination not found"));
            }
         } else {
            this.sendAsync(var1, var7, var3.getDeliveryMode(), var3.getPriority(), var3.getExpiration());
         }
      } catch (Throwable var18) {
         var1.onException(var18);
      } finally {
         if (this.producer instanceof WLMessageProducer) {
            try {
               ((WLMessageProducer)this.producer).setUnitOfOrder(var6);
            } catch (JMSException var16) {
            }
         }

      }

   }

   public void receiveCompletion(ProxyProducerSendRequest var1, Object var2, ReceivedTwoWay var3) {
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("SendAsync() returned:  response = " + var2);
      }

      if (var2 instanceof Throwable) {
         this.receiveException((Throwable)var2, var3);
      } else {
         assert var2 instanceof AsyncSendResponseInfo;

         AsyncSendResponseInfo var4 = (AsyncSendResponseInfo)var2;
         Object var5 = null;

         try {
            var5 = this.setupResponse(var4, var4.getMessage(), var1.getMessage());
            if (var4.getAsyncFlowControlTime() != 0L) {
               ((ProxyProducerSendResponse)var5).setFlowControlTime(var4.getAsyncFlowControlTime());
            }
         } catch (JMSException var7) {
            var5 = new TransportError(var7);
         }

         this.sendResult(var3, (MarshalWritable)var5);
      }
   }

   private void sendResult(final ReceivedTwoWay var1, final MarshalWritable var2) {
      try {
         JMSSecurityHelper.getJMSSecurityHelper();
         JMSSecurityHelper.doAs(JMSSecurityHelper.getAnonymousSubject(), new PrivilegedExceptionAction() {
            public Object run() {
               var1.send(var2);
               return var1;
            }
         });
      } catch (Throwable var4) {
      }

   }

   public void receiveException(Throwable var1, ReceivedTwoWay var2) {
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("SendAsync() failed:  throwable = " + var1);
      }

      this.sendResult(var2, new TransportError(var1));
   }

   private Message createJMSMessage(ProxyMessageImpl var1) throws JMSException {
      Session var2 = ((SessionProxy)this.getParent()).getSession();
      byte var3 = var1.getType();
      Object var4 = null;
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         switch (var3) {
            case 1:
               JMSDebug.JMSDotNetProxy.debug("Sending bytes message");
               break;
            case 2:
               JMSDebug.JMSDotNetProxy.debug("Sending header message");
               break;
            case 3:
               JMSDebug.JMSDotNetProxy.debug("Sending map message");
               break;
            case 4:
               JMSDebug.JMSDotNetProxy.debug("Sending text message");
               break;
            case 5:
               JMSDebug.JMSDotNetProxy.debug("Sending object message");
               break;
            case 6:
               JMSDebug.JMSDotNetProxy.debug("Sending text message");
         }
      }

      if (!var1.propertyExists("JMS_BEA_UnitOfWork") && !var1.propertyExists("JMS_BEA_UnitOfWorkSequenceNumber") && !var1.propertyExists("JMS_BEA_IsUnitOfWorkEnd")) {
         switch (var3) {
            case 1:
               var4 = var2.createBytesMessage();
               ((ProxyBytesMessageImpl)var1).populateJMSMessage((BytesMessage)var4);
               break;
            case 2:
               var4 = var2.createMessage();
               ((ProxyHdrMessageImpl)var1).populateJMSMessage((Message)var4);
               break;
            case 3:
               var4 = var2.createMapMessage();
               ((ProxyMapMessageImpl)var1).populateJMSMessage((MapMessage)var4);
               break;
            case 4:
               var4 = var2.createObjectMessage();
               ((ProxyObjectMessageImpl)var1).populateJMSMessage((ObjectMessage)var4);
               break;
            case 5:
               var4 = var2.createStreamMessage();
               ((ProxyStreamMessageImpl)var1).populateJMSMessage((StreamMessage)var4);
               break;
            case 6:
               var4 = var2.createTextMessage();
               ((ProxyTextMessageImpl)var1).populateJMSMessage((TextMessage)var4);
         }

         if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
            JMSDebug.JMSDotNetProxy.debug("Message = " + var4);
         }

         return (Message)var4;
      } else {
         throw new JMSException("Unit-of-Work is not supported for .NET client");
      }
   }

   private final MarshalWritable sendSync(ProxyProducerSendRequest var1) throws JMSException {
      ProxyMessageImpl var2 = var1.getMessage();
      ProxyDestinationImpl var3 = var1.getDestination();
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("SendSync() starts, destination on the request = " + var3 + " destiantion on the message = " + var2.getDestination());
      }

      Destination var4 = null;

      TransportError var6;
      try {
         if (this.producer instanceof WLMessageProducer) {
            ((WLMessageProducer)this.producer).setUnitOfOrder((String)var2.getProperty("JMS_BEA_UnitOfOrder"));
            var2.removeProperty("JMS_BEA_UnitOfOrder");
         }

         Message var5 = this.createJMSMessage(var2);
         ProxyProducerSendResponse var20;
         if (var3 == null) {
            this.send(var5, var2.getDeliveryMode(), var2.getPriority(), var2.getExpiration());
            var20 = this.setupResponse((AsyncSendResponseInfo)null, var5, var2);
            return var20;
         }

         var4 = var3.getJMSDestination();
         if (var4 != null) {
            this.send(var4, var5, var2.getDeliveryMode(), var2.getPriority(), var2.getExpiration());
            var20 = this.setupResponse((AsyncSendResponseInfo)null, var5, var2);
            return var20;
         }

         var6 = new TransportError("Destination not found", false);
      } catch (JMSException var18) {
         var6 = new TransportError(var18);
         return var6;
      } finally {
         if (this.producer instanceof WLMessageProducer) {
            try {
               ((WLMessageProducer)this.producer).setUnitOfOrder((String)null);
            } catch (JMSException var17) {
            }
         }

      }

      return var6;
   }

   private ProxyProducerSendResponse setupResponse(AsyncSendResponseInfo var1, Message var2, ProxyMessageImpl var3) throws JMSException {
      ProxyProducerSendResponse var4;
      JMSMessageId var5;
      if (var1 != null) {
         var5 = ((MessageImpl)var1.getMessage()).getId();
         var4 = new ProxyProducerSendResponse(var5.getSeed(), var5.getCounter());
      } else if (var2 instanceof WLMessage && !((MessageImpl)var2).isOldMessage()) {
         var5 = ((MessageImpl)var1.getMessage()).getId();
         var4 = new ProxyProducerSendResponse(var5.getSeed(), var5.getCounter());
      } else {
         var4 = new ProxyProducerSendResponse(var2.getJMSMessageID());
      }

      var4.setTimestamp(var2.getJMSTimestamp());
      var4.setExpirationTime(var2.getJMSExpiration());
      int var6;
      int var7;
      boolean var8;
      int var9;
      if (var1 == null) {
         var9 = var2.getJMSPriority();
         var6 = var2.getJMSDeliveryMode();
         var7 = 0;
         var8 = false;
      } else {
         var9 = var1.getPriority();
         var6 = var1.getDeliveryMode();
         var7 = var1.getRedeliveryLimit();
         var8 = var1.isDispatchOneWay();
      }

      if (var8 || var6 != var3.getDeliveryMode()) {
         var4.setDeliveryMode(var6);
      }

      if (var8 || var9 != var3.getPriority()) {
         var4.setPriority(var9);
      }

      if (var2 instanceof WLMessage) {
         if (var2.propertyExists("JMS_BEA_DeliveryTime")) {
            var4.setDeliveryTime(var2.getLongProperty("JMS_BEA_DeliveryTime"));
         }

         if (var2.propertyExists("JMS_BEA_RedeliveryLimit") && var2.getIntProperty("JMS_BEA_RedeliveryLimit") != 0) {
            var4.setRedeliveryLimit(var2.getIntProperty("JMS_BEA_RedeliveryLimit"));
         } else if (var7 != 0) {
            var4.setRedeliveryLimit(var7);
         }
      }

      return var4;
   }

   public final void invoke(ReceivedTwoWay var1) {
      if (this.isShutdown()) {
         var1.send(new TransportError(new JMSException("The JMS service is shutting down")));
      } else {
         MarshalReadable var2 = var1.getRequest();
         if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
            JMSDebug.JMSDotNetProxy.debug("invoke():  code = " + var2.getMarshalTypeCode() + " request = " + var2);
         }

         Object var3 = null;

         try {
            switch (var2.getMarshalTypeCode()) {
               case 22:
                  this.close();
                  var3 = ProxyVoidResponse.THE_ONE;
                  break;
               case 25:
                  if (this.wlAsyncSession != null) {
                     this.sendAsync((ProxyProducerSendRequest)var2, var1);
                     return;
                  }

                  var3 = this.sendSync((ProxyProducerSendRequest)var2);
                  break;
               default:
                  var3 = new TransportError("Invalid MarshalReadableType : " + var2.getMarshalTypeCode(), false);
            }
         } catch (JMSException var5) {
            var3 = new TransportError(var5);
         }

         var1.send((MarshalWritable)var3);
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

   public AbstractSubject getSubject() {
      return this.parent.getSubject();
   }

   static {
      String var0 = null;
      boolean var3 = true;

      try {
         var0 = System.getProperty("weblogic.jms.dotnet.SendAsync");
         if (var0 != null) {
            var3 = Boolean.valueOf(var0);
         }

         if (!var3) {
            System.err.println("\n\nweblogic.jms.dotnet.proxy.internal.ProducerProxy -Dweblogic.jms.dotnet.SendAsync=" + var3);
         }
      } catch (Throwable var5) {
         if (var0 != null) {
            System.err.println("\n\nproblem processing -Dweblogic.jms.dotnet.SendAsync=" + var0);
         }

         var5.printStackTrace();
      }

      USE_ASYNC = var3;
   }
}
