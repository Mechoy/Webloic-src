package weblogic.jms.dotnet.proxy.internal;

import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jms.client.JMSConnectionFactory;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.dotnet.proxy.protocol.ProxyContextCloseRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyContextLookupConnectionFactoryRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyContextLookupConnectionFactoryResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyContextLookupDestinationRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyContextLookupDestinationResponse;
import weblogic.jms.dotnet.proxy.protocol.ProxyVoidResponse;
import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.ReceivedTwoWay;
import weblogic.jms.dotnet.transport.ServiceTwoWay;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.jms.dotnet.transport.TransportError;
import weblogic.jms.extensions.WLDestination;
import weblogic.jndi.internal.WLInternalContext;
import weblogic.protocol.ChannelHelperBase;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.subject.AbstractSubject;

public class InitialContextProxy extends BaseProxy implements ServiceTwoWay {
   private HashMap<Long, ConnectionProxy> connections = new HashMap();
   private HashSet<Long> cfServiceIds = new HashSet();
   private Context initialContext;
   private Transport transport;
   private AbstractSubject subject;
   private boolean closedAll;

   public InitialContextProxy(Transport var1, long var2, Context var4) {
      super(var2, (BaseProxy)null);
      this.initialContext = var4;
      this.transport = var1;
      this.subject = JMSSecurityHelper.getCurrentSubject();
      if (SubjectUtils.doesUserHaveAnyAdminRoles((AuthenticatedSubject)this.subject) && ChannelHelperBase.isLocalAdminChannelEnabled()) {
         throw new SecurityException("Server has admin channel configured, therefore admin traffic from .NET client is not allowed to go through");
      } else {
         if (JMSSecurityHelper.isServerIdentity((AuthenticatedSubject)this.subject)) {
            this.subject = JMSSecurityHelper.getAnonymousSubject();
         }

         if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
            JMSDebug.JMSDotNetProxy.debug("Creating initial context: subject = " + this.subject);
         }

      }
   }

   private final MarshalWritable lookupFactory(final String var1) {
      long var2 = 0L;
      ConnectionFactoryProxy var4 = null;
      synchronized(this) {
         label54: {
            TransportError var10000;
            try {
               ConnectionFactory var6 = (ConnectionFactory)JMSSecurityHelper.doAsJNDIOperation(this.subject, new PrivilegedExceptionAction() {
                  public Object run() throws NamingException {
                     return InitialContextProxy.this.initialContext.lookup(var1);
                  }
               });
               if (!(var6 instanceof JMSConnectionFactory)) {
                  var10000 = new TransportError(new JMSException("The connection factory is not a WebLogic JMS connection factory.  The WebLogic JMS .NET client supports WebLogic JMS destinations and connection factories."));
               } else {
                  var2 = this.transport.allocateServiceID();
                  var4 = new ConnectionFactoryProxy(var2, var1, this, var6);
                  this.transport.registerService(var2, var4);
                  this.addServiceId(var2);
                  break label54;
               }
            } catch (ClassCastException var8) {
               return new TransportError(var8);
            } catch (JMSException var9) {
               return new TransportError(var9);
            } catch (NamingException var10) {
               return new TransportError(var10);
            }

            return var10000;
         }
      }

      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("Looked up connection factory: id = " + var2 + " factory = " + var4);
      }

      return new ProxyContextLookupConnectionFactoryResponse(var2);
   }

   private final MarshalWritable lookupDestination(final String var1) {
      Destination var2 = null;

      try {
         var2 = (Destination)JMSSecurityHelper.doAsJNDIOperation(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws NamingException {
               return InitialContextProxy.this.initialContext.lookup(var1);
            }
         });
         if (!(var2 instanceof WLDestination)) {
            return new TransportError(new JMSException("The destination is not a WebLogic JMS destination.  The WebLogic JMS .NET client supports WebLogic JMS destinations and connection factories."));
         } else {
            if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
               JMSDebug.JMSDotNetProxy.debug("Looked up JMS destination: " + var2);
            }

            return new ProxyContextLookupDestinationResponse(var2);
         }
      } catch (ClassCastException var4) {
         return new TransportError(var4);
      } catch (JMSException var5) {
         return new TransportError(var5);
      } catch (NamingException var6) {
         return new TransportError(var6);
      }
   }

   private void close() {
      this.closeInternal();
   }

   private void closeInternal() {
      synchronized(this) {
         if ((this.state & 1) != 0) {
            return;
         }

         this.state = 1;
      }

      try {
         JMSSecurityHelper.doAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws NamingException {
               if (InitialContextProxy.this.initialContext instanceof WLInternalContext) {
                  ((WLInternalContext)InitialContextProxy.this.initialContext).disableThreadWarningOnClose();
               }

               InitialContextProxy.this.initialContext.close();
               return null;
            }
         });
      } catch (JMSException var3) {
      }

      ProxyManagerImpl.getProxyManager().removeContext(this.getServiceId());
      if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
         JMSDebug.JMSDotNetProxy.debug("Closed initial context:  id = " + this.getServiceId());
      }

   }

   private void closeAll() {
      synchronized(this) {
         this.closedAll = true;
      }

      this.cleanup(false);
   }

   synchronized void addConnection(long var1, ConnectionProxy var3) throws JMSException {
      this.checkShutdownOrClosed("The context has been closed");
      this.connections.put(var1, var3);
   }

   synchronized void remove(long var1) {
      this.connections.remove(var1);
   }

   InitialContextProxy getContext() {
      return this;
   }

   private synchronized boolean isClosedAll() {
      return this.closedAll;
   }

   public final void invoke(ReceivedTwoWay var1) {
      if (this.isShutdown()) {
         var1.send(new TransportError(new JMSException("The JMS service is shutting down")));
      } else if (this.isClosedAll()) {
         var1.send(new TransportError(new JMSException("The context has been closed")));
      } else {
         MarshalReadable var2 = var1.getRequest();
         Object var3 = null;
         if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
            JMSDebug.JMSDotNetProxy.debug("Invoking: code = " + var2.getMarshalTypeCode() + " request = " + var2);
         }

         switch (var2.getMarshalTypeCode()) {
            case 3:
               var3 = this.lookupFactory(((ProxyContextLookupConnectionFactoryRequest)var2).getJNDIName());
               break;
            case 4:
            case 6:
            default:
               var3 = new TransportError("Invalid MarshalReadableType : " + var2.getMarshalTypeCode(), false);
               break;
            case 5:
               var3 = this.lookupDestination(((ProxyContextLookupDestinationRequest)var2).getJNDIName());
               break;
            case 7:
               if (((ProxyContextCloseRequest)var2).isCloseAll()) {
                  this.closeAll();
               } else {
                  this.close();
               }

               var3 = ProxyVoidResponse.THE_ONE;
         }

         var1.send((MarshalWritable)var3);
      }
   }

   private synchronized void addServiceId(long var1) throws JMSException {
      this.checkShutdownOrClosed("The session has been closed");
      this.cfServiceIds.add(var1);
   }

   public void cleanup(boolean var1) {
      if (!var1) {
         this.closeInternal();
         Iterator var2 = null;
         synchronized(this) {
            var2 = ((HashMap)this.connections.clone()).values().iterator();
            this.connections.clear();
         }

         while(var2.hasNext()) {
            ConnectionProxy var3 = (ConnectionProxy)var2.next();

            try {
               var3.close();
            } catch (JMSException var5) {
            }
         }

         this.unregister();
      }
   }

   private void unregister() {
      this.transport.unregisterService(this.serviceId);
      synchronized(this) {
         Iterator var2 = this.cfServiceIds.iterator();

         while(var2.hasNext()) {
            long var3 = (Long)var2.next();
            this.getTransport().unregisterService(var3);
         }

         this.cfServiceIds.clear();
      }
   }

   public void onPeerGone(TransportError var1) {
      this.cleanup(false);
   }

   public void onShutdown() {
   }

   public void onUnregister() {
   }

   public Transport getTransport() {
      return this.transport;
   }

   public AbstractSubject getSubject() {
      return this.subject;
   }
}
