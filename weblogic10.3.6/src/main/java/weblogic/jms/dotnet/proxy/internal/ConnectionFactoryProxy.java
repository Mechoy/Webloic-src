package weblogic.jms.dotnet.proxy.internal;

import java.security.PrivilegedExceptionAction;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.dotnet.proxy.protocol.ProxyConnectionCreateRequest;
import weblogic.jms.dotnet.proxy.protocol.ProxyConnectionCreateResponse;
import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.ReceivedTwoWay;
import weblogic.jms.dotnet.transport.ServiceTwoWay;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.jms.dotnet.transport.TransportError;
import weblogic.jms.extensions.WLConnection;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.subject.AbstractSubject;

public class ConnectionFactoryProxy extends BaseProxy implements ServiceTwoWay {
   private String jndiName;
   private ConnectionFactory factory;

   public ConnectionFactoryProxy(long var1, String var3, InitialContextProxy var4, ConnectionFactory var5) {
      super(var1, var4);
      this.jndiName = var3;
      this.factory = var5;
   }

   private final Connection createConnection() throws JMSException {
      return this.factory.createConnection();
   }

   private final Connection createConnection(String var1, String var2) throws JMSException {
      return this.factory.createConnection(var1, var2);
   }

   private final MarshalWritable createConnection(ProxyConnectionCreateRequest var1) {
      String var2;
      String var3;
      try {
         var2 = var1.getUserName();
         var2 = EncrypUtil.decryptString(this.getTransport(), var2);
         var3 = var1.getPassword();
         var3 = EncrypUtil.decryptString(this.getTransport(), var3);
      } catch (Exception var18) {
         return new TransportError(var18);
      }

      final String var4 = var2;
      final String var5 = var3;
      boolean var6 = var1.isCreateXAConnection();
      Connection var7 = null;
      AuthenticatedSubject var8 = JMSSecurityHelper.getCurrentSubject();

      TransportError var10;
      try {
         var7 = (Connection)JMSSecurityHelper.doAs(this.getSubject(), new PrivilegedExceptionAction() {
            public Object run() throws JMSException {
               return var4 == null ? ConnectionFactoryProxy.this.createConnection() : ConnectionFactoryProxy.this.createConnection(var4, var5);
            }
         });
         long var9 = this.getTransport().allocateServiceID();
         int var11 = ((WLConnection)var7).getAcknowledgePolicy();
         if (var11 != 2 && var11 != 1) {
            throw new JMSException("Unknown acknowledge policy " + var11);
         }

         ((WLConnection)var7).setAcknowledgePolicy(2);
         ConnectionProxy var12 = new ConnectionProxy(var9, (InitialContextProxy)this.parent, var7, var4, var5, var1.getListenerServiceId());
         this.getTransport().registerService(var9, var12);
         ((InitialContextProxy)this.parent).addConnection(var9, var12);
         if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
            JMSDebug.JMSDotNetProxy.debug("Created JMS connection: id = " + var9 + " client id = " + var7.getClientID());
         }

         ProxyConnectionCreateResponse var13 = new ProxyConnectionCreateResponse(var9, var7.getClientID(), var11, var12.getMetadata());
         return var13;
      } catch (JMSException var19) {
         var10 = new TransportError(var19);
      } finally {
         JMSSecurityHelper.pushSubject(var8);
      }

      return var10;
   }

   synchronized void remove(long var1) {
   }

   public final void invoke(ReceivedTwoWay var1) {
      if (this.isShutdown()) {
         var1.send(new TransportError(new JMSException("The JMS service is shutting down")));
      } else {
         MarshalReadable var2 = var1.getRequest();
         if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
            JMSDebug.JMSDotNetProxy.debug("Invoking: code = " + var2.getMarshalTypeCode() + " request = " + var2);
         }

         Object var3 = null;
         switch (var2.getMarshalTypeCode()) {
            case 9:
               var3 = this.createConnection((ProxyConnectionCreateRequest)var2);
               break;
            default:
               var3 = new TransportError("Invalid MarshalReadableType : " + var2.getMarshalTypeCode(), false);
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
}
