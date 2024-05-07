package weblogic.jms.dotnet.proxy.internal;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.dotnet.proxy.ProxyManager;
import weblogic.jms.dotnet.proxy.protocol.PrimitiveMap;
import weblogic.jms.dotnet.proxy.protocol.ProxyBootstrapResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyBytesMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyConnectionCommandRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyConnectionCreateRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyConnectionCreateResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyConnectionSetClientIdRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyConsumerCloseRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyConsumerCreateRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyConsumerCreateResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyConsumerReceiveRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyConsumerReceiveResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyConsumerSetListenerRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyConsumerSetListenerResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyContextCloseRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyContextCreateRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyContextCreateResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyContextLookupConnectionFactoryRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyContextLookupConnectionFactoryResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyContextLookupDestinationRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyContextLookupDestinationResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyDestinationCreateRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyDestinationCreateResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyHdrMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyMapMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyObjectMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyProducerCloseRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyProducerCreateRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyProducerCreateResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyProducerSendRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyProducerSendResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyPushExceptionRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyPushMessageListRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyPushMessageRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyPushMessageResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyRemoveSubscriptionRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxySessionAcknowledgeRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxySessionCloseRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxySessionCreateRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxySessionCreateResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxySessionRecoverRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxySessionRecoverResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxySessionWindowTurnRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyTextMessageImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyVoidResponse;
import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalReadableFactory;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.ReceivedTwoWay;
import weblogic.jms.dotnet.transport.ServiceTwoWay;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.jms.dotnet.transport.TransportError;
import weblogic.jms.dotnet.transport.t3plugin.T3ServerPlugin;
import weblogic.jndi.Environment;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.server.ServiceFailureException;

public final class ProxyManagerImpl implements ProxyManager, ServiceTwoWay, MarshalReadableFactory {
   public static final int PROXY_CONTEXT_CREATE_REQUEST = 1;
   public static final int PROXY_CONTEXT_CREATE_RESPONSE = 2;
   public static final int PROXY_CONTEXT_LOOKUP_CONNECTION_FACTORY_REQUEST = 3;
   public static final int PROXY_CONTEXT_LOOKUP_CONNECTION_FACTORY_RESPONSE = 4;
   public static final int PROXY_CONTEXT_LOOKUP_DESTINATION_REQUEST = 5;
   public static final int PROXY_CONTEXT_LOOKUP_DESTINATION_RESPONSE = 6;
   public static final int PROXY_CONTEXT_CLOSE_REQUEST = 7;
   public static final int PROXY_CONNECTION_COMMAND_REQUEST = 8;
   public static final int PROXY_CONNECTION_CREATE_REQUEST = 9;
   public static final int PROXY_CONNECTION_CREATE_RESPONSE = 10;
   public static final int PROXY_CONNECTION_SET_CLIENT_ID_REQUEST = 11;
   public static final int PROXY_CONSUMER_CLOSE_REQUEST = 12;
   public static final int PROXY_CONSUMER_CREATE_REQUEST = 13;
   public static final int PROXY_CONSUMER_CREATE_RESPONSE = 14;
   public static final int PROXY_CONSUMER_INCREMENT_WINDOW_CURRENT_REQUEST = 15;
   public static final int PROXY_CONSUMER_RECEIVE_REQUEST = 16;
   public static final int PROXY_CONSUMER_RECEIVE_RESPONSE = 17;
   public static final int PROXY_CONSUMER_SET_LISTENER_REQUEST = 18;
   public static final int PROXY_CONSUMER_SET_LISTENER_RESPONSE = 19;
   public static final int PROXY_DESTINATION_CREATE_REQUEST = 20;
   public static final int PROXY_DESTINATION_CREATE_RESPONSE = 21;
   public static final int PROXY_PRODUCER_CLOSE_REQUEST = 22;
   public static final int PROXY_PRODUCER_CREATE_REQUEST = 23;
   public static final int PROXY_PRODUCER_CREATE_RESPONSE = 24;
   public static final int PROXY_PRODUCER_SEND_REQUEST = 25;
   public static final int PROXY_PRODUCER_SEND_RESPONSE = 26;
   public static final int PROXY_REMOVE_SUBSCRIPTION_REQUEST = 27;
   public static final int PROXY_SESSION_ACKNOWLEDGE_REQUEST = 28;
   public static final int PROXY_SESSION_CLOSE_REQUEST = 29;
   public static final int PROXY_SESSION_CREATE_REQUEST = 30;
   public static final int PROXY_SESSION_CREATE_RESPONSE = 31;
   public static final int PROXY_SESSION_RECOVER_REQUEST = 32;
   public static final int PROXY_SESSION_RECOVER_RESPONSE = 33;
   public static final int PROXY_SESSION_SET_REDELIVERY_DELAY_REQUEST = 34;
   public static final int PROXY_TEXT_MESSAGE_IMPL = 35;
   public static final int PROXY_BYTES_MESSAGE_IMPL = 36;
   public static final int PROXY_MAP_MESSAGE_IMPL = 37;
   public static final int PROXY_STREAM_MESSAGE_IMPL = 38;
   public static final int PROXY_OBJECT_MESSAGE_IMPL = 39;
   public static final int PROXY_PRIMITIVE_MAP = 40;
   public static final int PROXY_DESTINATION_IMPL = 41;
   public static final int PROXY_BOOTSTRAP_RESPONSE = 42;
   public static final int PROXY_PUSH_EXCEPTION_REQUEST = 43;
   public static final int PROXY_PUSH_MESSAGE_REQUEST = 44;
   public static final int PROXY_PUSH_MESSAGE_RESPONSE = 45;
   public static final int PROXY_VOID_RESPONSE = 46;
   public static final int PROXY_ACKNOWLEDGE_INFO = 47;
   public static final int PROXY_HDR_MESSAGE_IMPL = 48;
   public static final int PROXY_SESSION_WINDOW_TURN_REQUEST = 49;
   public static final int PROXY_CONNECTION_METADATA_IMPL = 50;
   public static final int PROXY_PUSH_MESSAGE_LIST_REQUEST = 51;
   static final int SERVICE_ID = 10004;
   static final String INITIAL_CONTEXT_FACTORY_KEY = "weblogic.naming.factory.initial";
   static final String INITIAL_CONTEXT_FACTORY_VALUE = "weblogic.jndi.WLInitialContextFactory";
   private static final ProxyManagerImpl singleton = new ProxyManagerImpl();
   private Map<Long, InitialContextProxy> contexts = new HashMap();
   static final boolean debug = false;
   private int state = 0;

   public static ProxyManagerImpl getProxyManager() {
      return singleton;
   }

   private MarshalWritable createInitialContext(Transport var1, ProxyContextCreateRequest var2) {
      Hashtable var3 = new Hashtable();
      PrimitiveMap var4 = var2.getEnvironment();
      Iterator var5 = var4.keySet().iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         if (!var6.equals("java.naming.provider.url")) {
            String var7 = (String)var4.get(var6);
            var3.put(var6, var7);
         }
      }

      var3.put("weblogic.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");

      Hashtable var25;
      try {
         var25 = EncrypUtil.decrypt(var1, var3);
      } catch (Exception var20) {
         return new TransportError(var20);
      }

      AuthenticatedSubject var26 = JMSSecurityHelper.getCurrentSubject();
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("Creating initial context: initial context env is " + var25);
      }

      TransportError var9;
      try {
         Environment var8 = var25.size() == 0 ? new Environment() : new Environment(var25);
         Context var27 = var8.getInitialContext();
         long var10 = var1.allocateServiceID();
         InitialContextProxy var12 = new InitialContextProxy(var1, var10, var27);
         var1.registerService(var10, var12);
         this.addContext(var10, var12);
         if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
            JMSDebug.JMSDotNetProxy.debug("Created initial context: initial context id = " + var10);
         }

         ProxyContextCreateResponse var13 = new ProxyContextCreateResponse(var10);
         return var13;
      } catch (JMSException var21) {
         var9 = new TransportError(var21);
         return var9;
      } catch (SecurityException var22) {
         var9 = new TransportError(var22);
      } catch (NamingException var23) {
         var9 = new TransportError(var23);
         return var9;
      } finally {
         JMSSecurityHelper.pushSubject(var26);
      }

      return var9;
   }

   private void addContext(long var1, InitialContextProxy var3) throws JMSException {
      synchronized(this) {
         if (this.isShutdown()) {
            throw new JMSException("The JMS service is shutting down");
         } else {
            this.contexts.put(new Long(var1), var3);
         }
      }
   }

   synchronized void removeContext(long var1) {
      this.contexts.remove(new Long(var1));
   }

   public final void invoke(ReceivedTwoWay var1) {
      if (singleton.isShutdown()) {
         var1.send(new TransportError(new JMSException("The JMS service is shutting down")));
      } else {
         Object var2 = null;
         MarshalReadable var3 = var1.getRequest();
         switch (var3.getMarshalTypeCode()) {
            case 1:
               var2 = this.createInitialContext(var1.getTransport(), (ProxyContextCreateRequest)var3);
               break;
            default:
               var2 = new TransportError("Invalid MarshalReadableType : " + var3.getMarshalTypeCode(), false);
         }

         var1.send((MarshalWritable)var2);
      }
   }

   public MarshalReadable createMarshalReadable(int var1) {
      switch (var1) {
         case 1:
            return new ProxyContextCreateRequest();
         case 2:
            return new ProxyContextCreateResponse();
         case 3:
            return new ProxyContextLookupConnectionFactoryRequest();
         case 4:
            return new ProxyContextLookupConnectionFactoryResponse();
         case 5:
            return new ProxyContextLookupDestinationRequest();
         case 6:
            return new ProxyContextLookupDestinationResponse();
         case 7:
            return new ProxyContextCloseRequest();
         case 8:
            return new ProxyConnectionCommandRequest();
         case 9:
            return new ProxyConnectionCreateRequest();
         case 10:
            return new ProxyConnectionCreateResponse();
         case 11:
            return new ProxyConnectionSetClientIdRequest();
         case 12:
            return new ProxyConsumerCloseRequest();
         case 13:
            return new ProxyConsumerCreateRequest();
         case 14:
            return new ProxyConsumerCreateResponse();
         case 15:
         case 16:
            return new ProxyConsumerReceiveRequest();
         case 17:
            return new ProxyConsumerReceiveResponse();
         case 18:
            return new ProxyConsumerSetListenerRequest();
         case 19:
            return new ProxyConsumerSetListenerResponse();
         case 20:
            return new ProxyDestinationCreateRequest();
         case 21:
            return new ProxyDestinationCreateResponse();
         case 22:
            return new ProxyProducerCloseRequest();
         case 23:
            return new ProxyProducerCreateRequest();
         case 24:
            return new ProxyProducerCreateResponse();
         case 25:
            return new ProxyProducerSendRequest();
         case 26:
            return new ProxyProducerSendResponse();
         case 27:
            return new ProxyRemoveSubscriptionRequest();
         case 28:
            return new ProxySessionAcknowledgeRequest();
         case 29:
            return new ProxySessionCloseRequest();
         case 30:
            return new ProxySessionCreateRequest();
         case 31:
            return new ProxySessionCreateResponse();
         case 32:
            return new ProxySessionRecoverRequest();
         case 33:
            return new ProxySessionRecoverResponse();
         case 34:
         case 36:
            return new ProxyBytesMessageImpl();
         case 35:
            return new ProxyTextMessageImpl();
         case 37:
            return new ProxyMapMessageImpl();
         case 38:
            return new ProxyTextMessageImpl();
         case 39:
            return new ProxyObjectMessageImpl();
         case 40:
         case 41:
         case 47:
         case 50:
         default:
            return null;
         case 42:
            return new ProxyBootstrapResponse();
         case 43:
            return new ProxyPushExceptionRequest();
         case 44:
            return new ProxyPushMessageRequest();
         case 45:
            return new ProxyPushMessageResponse();
         case 46:
            return ProxyVoidResponse.THE_ONE;
         case 48:
            return new ProxyHdrMessageImpl();
         case 49:
            return new ProxySessionWindowTurnRequest();
         case 51:
            return new ProxyPushMessageListRequest();
      }
   }

   public void onPeerGone(TransportError var1) {
   }

   public void onShutdown() {
   }

   public void onUnregister() {
   }

   public void resume() throws ServiceFailureException {
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("JMS .NET proxy is starting");
      }

      synchronized(this) {
         if (this.state == 4) {
            return;
         }

         this.state = 4;
      }

      T3ServerPlugin.register();
   }

   public void shutdown(boolean var1) throws ServiceFailureException {
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("JMS .NET proxy is shutting down");
      }

      synchronized(this) {
         if (this.state == 16) {
            return;
         }

         this.state = 16;
      }

      this.cleanup(var1);
   }

   private void cleanup(boolean var1) {
      Iterator var2 = null;
      synchronized(this) {
         var2 = ((HashMap)((HashMap)this.contexts).clone()).values().iterator();
         this.contexts.clear();
      }

      while(var2.hasNext()) {
         InitialContextProxy var3 = (InitialContextProxy)var2.next();
         var3.cleanup(var1);
         Transport var4 = var3.getTransport();
         if (var4 != null) {
            var4.shutdown(new TransportError("The WLS server is shutting down", false));
         }
      }

   }

   public final synchronized boolean isShutdown() {
      return (this.state & 4) == 0;
   }

   private void debug(String var1) {
      System.out.println("[ProxyManagerImpl]: " + var1);
   }
}
