package weblogic.jms.dotnet.proxy.internal;

import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.security.auth.login.LoginException;
import weblogic.jms.client.ConnectionInternal;
import weblogic.jms.common.JMSConstants;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.dotnet.proxy.protocol.ProxyConnectionCommandRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyConnectionMetaDataImpl;
import weblogic.jms.dotnet.proxy.protocol.ProxyConnectionSetClientIdRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyPushExceptionRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxySessionCreateRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxySessionCreateResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyVoidResponse;
import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.ReceivedTwoWay;
import weblogic.jms.dotnet.transport.SendHandlerOneWay;
import weblogic.jms.dotnet.transport.ServiceTwoWay;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.jms.dotnet.transport.TransportError;
import weblogic.jms.extensions.WLConnection;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.subject.AbstractSubject;

public class ConnectionProxy extends BaseProxy implements ServiceTwoWay, ExceptionListener {
   private Connection connection;
   private AbstractSubject subject;
   private final long listenerServiceId;
   private ProxyConnectionMetaDataImpl metadata;
   private Map<Long, SessionProxy> sessions = new HashMap();

   ConnectionProxy(long var1, InitialContextProxy var3, final Connection var4, String var5, String var6, long var7) throws JMSException {
      super(var1, var3);
      this.connection = var4;
      if (var4 instanceof WLConnection) {
         ((WLConnection)var4).setReconnectPolicy(JMSConstants.RECONNECT_POLICY_NONE);
      }

      if (var4 instanceof ConnectionInternal && ((ConnectionInternal)var4).getFEPeerInfo().getMajor() < 8) {
         throw new JMSException(".NET client cannot talk to a pre-8.1 server");
      } else {
         this.subject = null;
         if (var5 != null && var6 != null) {
            try {
               this.subject = JMSSecurityHelper.authenticatedSubject(var5, var6);
            } catch (LoginException var10) {
               throw new JMSException("User '" + var5 + "' does not have the permission!");
            }
         } else {
            this.subject = var3.getSubject();
         }

         if (JMSSecurityHelper.isServerIdentity((AuthenticatedSubject)this.subject)) {
            this.subject = JMSSecurityHelper.getAnonymousSubject();
         }

         this.listenerServiceId = var7;
         JMSSecurityHelper.doAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws JMSException {
               var4.setExceptionListener(ConnectionProxy.this);
               return null;
            }
         });
         this.metadata = new ProxyConnectionMetaDataImpl(var4.getMetaData());
      }
   }

   private final synchronized void setClientId(final String var1) throws JMSException {
      JMSSecurityHelper.doAs(this.subject, new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            if (ConnectionProxy.this.connection != null) {
               ConnectionProxy.this.connection.setClientID(var1);
            }

            return null;
         }
      });
   }

   private final synchronized void start() throws JMSException {
      JMSSecurityHelper.doAs(this.subject, new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            if (ConnectionProxy.this.connection != null) {
               ConnectionProxy.this.connection.start();
            }

            return null;
         }
      });
   }

   private final synchronized void stop() throws JMSException {
      JMSSecurityHelper.doAs(this.subject, new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            if (ConnectionProxy.this.connection != null) {
               ConnectionProxy.this.connection.stop();
            }

            return null;
         }
      });
   }

   final void close() throws JMSException {
      synchronized(this) {
         if ((this.state & 1) != 0) {
            return;
         }

         this.state = 1;
      }

      this.unregister();
      this.parent.remove(this.serviceId);
      JMSSecurityHelper.doAs(this.subject, new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            ConnectionProxy.this.connection.close();
            return null;
         }
      });
   }

   private void unregister() {
      this.getTransport().unregisterService(this.serviceId);
      synchronized(this) {
         Iterator var2 = this.sessions.values().iterator();

         while(var2.hasNext()) {
            SessionProxy var3 = (SessionProxy)var2.next();
            var3.unregister();
         }

         this.sessions.clear();
      }
   }

   public final String toString() {
      return "Proxy for " + this.connection.toString();
   }

   private final MarshalWritable createSession(ProxySessionCreateRequest var1) throws JMSException {
      final boolean var2 = var1.getTransacted();
      boolean var3 = var1.getXASession();
      int var4 = var1.getAcknowledgeMode();
      long var6 = var1.getSessionMsgListenerServiceId();
      final int var5;
      if (var4 != 1 && var4 != 3) {
         var5 = var4;
      } else {
         var5 = 2;
      }

      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("CreateSession: transacted = " + var2 + " ackMode = " + this.getAckModeString(var4) + " adjusted ackMode = " + this.getAckModeString(var5));
      }

      Session var8 = (Session)JMSSecurityHelper.doAs(this.subject, new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            return ConnectionProxy.this.connection.createSession(var2, var5);
         }
      });
      long var9 = this.getTransport().allocateServiceID();
      SessionProxy var11 = new SessionProxy(var9, this, var8, var4, var6);
      this.getTransport().registerService(var9, var11);
      this.addSession(var9, var11);
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("Created JMS session: id = " + this.serviceId);
      }

      return new ProxySessionCreateResponse(var9, var8);
   }

   private synchronized void addSession(long var1, SessionProxy var3) throws JMSException {
      this.checkShutdownOrClosed("The connection to the proxy has been closed");
      this.sessions.put(var1, var3);
   }

   synchronized void remove(long var1) {
      this.sessions.remove(var1);
   }

   public final void invoke(ReceivedTwoWay var1) {
      if (this.isShutdown()) {
         var1.send(new TransportError(new JMSException("The JMS service is shutting down")));
      } else if (this.isClosed()) {
         var1.send(new TransportError(new JMSException("The connection has been closed")));
      } else {
         MarshalReadable var2 = var1.getRequest();
         if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
            JMSDebug.JMSDotNetProxy.debug("Invoking: code = " + var2.getMarshalTypeCode() + " request = " + var2);
         }

         Object var3 = ProxyVoidResponse.THE_ONE;

         try {
            switch (var2.getMarshalTypeCode()) {
               case 8:
                  int var4 = ((ProxyConnectionCommandRequest)var2).getCommandCode();
                  if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
                     JMSDebug.JMSDotNetProxy.debug("Invoke(): CommandCode = " + ((ProxyConnectionCommandRequest)var2).getCommandCodeString());
                  }

                  try {
                     if (var4 == 1) {
                        this.start();
                     } else if (var4 == 3) {
                        this.close();
                     } else if (var4 == 2) {
                        this.stop();
                     }
                  } catch (JMSException var6) {
                     var3 = new TransportError(var6);
                  }
                  break;
               case 11:
                  this.setClientId(((ProxyConnectionSetClientIdRequest)var2).getClientId());
                  break;
               case 30:
                  var3 = this.createSession((ProxySessionCreateRequest)var2);
                  break;
               default:
                  var3 = new TransportError("Invalid MarshalReadableType : " + var2.getMarshalTypeCode(), false);
            }
         } catch (JMSException var7) {
            var3 = new TransportError(var7);
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

   Transport getTransport() {
      return this.parent.getTransport();
   }

   InitialContextProxy getContext() {
      return this.parent.getContext();
   }

   AbstractSubject getSubject() {
      return this.subject;
   }

   public ProxyConnectionMetaDataImpl getMetadata() {
      return this.metadata;
   }

   private String getAckModeString(int var1) {
      switch (var1) {
         case 1:
            return "AUTO_ACKNOWLEDGE";
         case 2:
            return "CLIENT_ACKNOWLEDGE";
         case 3:
            return "DUPS_OK_ACKNOWLEDGE";
         default:
            return this.connection instanceof WLConnection && var1 == 4 ? "NO_ACKNOWLEDGE" : "Invalid Ack Mode: " + var1;
      }
   }

   public void onException(JMSException var1) {
      try {
         this.close();
      } catch (JMSException var10) {
      }

      synchronized(this) {
         Transport var3 = this.getTransport();
         final SendHandlerOneWay var4 = var3.createOneWay(this.listenerServiceId);
         final ProxyPushExceptionRequest var5 = new ProxyPushExceptionRequest(var1);

         try {
            JMSSecurityHelper.doAs(JMSSecurityHelper.getAnonymousSubject(), new PrivilegedExceptionAction() {
               public Object run() {
                  var4.send(var5);
                  return null;
               }
            });
         } catch (JMSException var8) {
         }

      }
   }
}
