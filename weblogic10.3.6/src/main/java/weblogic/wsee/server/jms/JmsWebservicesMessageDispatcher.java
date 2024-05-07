package weblogic.wsee.server.jms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.LoginException;
import javax.xml.rpc.JAXRPCException;
import weblogic.jws.security.RunAs;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityService;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.utils.encoders.BASE64Decoder;
import weblogic.wsee.component.pojo.JavaClassComponent;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.connection.ConnectionException;
import weblogic.wsee.connection.ConnectionFactory;
import weblogic.wsee.connection.transport.TransportUtil;
import weblogic.wsee.connection.transport.jms.JmsServerQueueTransport;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsException;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsRegistry;
import weblogic.wsee.ws.WsSkel;

public class JmsWebservicesMessageDispatcher {
   private static final boolean verbose = Verbose.isVerbose(JmsWebservicesMessageDispatcher.class);
   private String wsUrl;
   private QueueConnectionFactory factory = null;
   private String userName = null;
   private String passwd = null;
   private PrincipalAuthenticator _pa;
   private static final AuthenticatedSubject _kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public JmsWebservicesMessageDispatcher(String var1, String var2) throws JMSException, NamingException {
      this.factory = this.getConnectionFactory(var2);
      this.wsUrl = var1;
   }

   protected JmsWebservicesMessageDispatcher(String var1, QueueConnectionFactory var2) throws JMSException {
      this.factory = var2;
      this.wsUrl = var1;
   }

   protected JmsWebservicesMessageDispatcher(String var1, QueueConnectionFactory var2, String var3, String var4) throws JMSException {
      this.factory = var2;
      this.wsUrl = var1;
      this.userName = var3;
      this.passwd = var4;
   }

   public void dispatchMessage(Message var1) {
      String var2 = null;

      String var3;
      try {
         var2 = var1.getStringProperty("URI");
         var3 = var1.getStringProperty("WSEE_JMS_SUBJECT");
      } catch (JMSException var16) {
         throw new JAXRPCException("Failed to get URI property from JMS text message:" + var16, var16);
      }

      if (verbose) {
         Verbose.log((Object)("URI = " + var2));
      }

      WsRegistry var4 = WsRegistry.instance();
      final WsPort var5 = var4.lookup(this.wsUrl);
      if (var5 == null) {
         throw new JAXRPCException("Failed to lookup Web Service Endpoint at " + this.wsUrl + "." + "\nThe Web Service does not exist anymore or its server is shut(or shutting)," + "\nand the request will failover to another server if the Web Service is running on cluster.");
      } else {
         final WsSkel var6 = (WsSkel)var5.getEndpoint();
         JmsServerQueueTransport var7 = new JmsServerQueueTransport(this.wsUrl, var1, this.factory, this.userName, this.passwd);
         String var8 = var5.getWsdlPort().getBinding().getBindingType();

         final Connection var9;
         try {
            var9 = ConnectionFactory.instance().createServerConnection(var7, var8);
         } catch (ConnectionException var15) {
            throw new JAXRPCException("Failed to create connection", var15);
         }

         AuthenticatedSubject var10 = null;
         if (var6.getComponent() instanceof JavaClassComponent) {
            RunAs var11 = (RunAs)var6.getJwsClass().getAnnotation(RunAs.class);
            if (var11 != null) {
               String var12 = var11.mapToPrincipal();
               var10 = this.authenticateAs(var12);
               if (verbose) {
                  Verbose.log((Object)("Get @RunAs subject on skel: " + var10));
               }
            } else if (verbose) {
               Verbose.log((Object)("No @RunAs defined on skel: " + var6));
            }
         }

         try {
            if (var10 == null && var3 == null) {
               var6.invoke(var9, var5);
            } else {
               if (var10 == null) {
                  byte[] var21 = (new BASE64Decoder()).decodeBuffer(var3);
                  ByteArrayInputStream var22 = new ByteArrayInputStream(var21);
                  ObjectInputStream var13 = new ObjectInputStream(var22);
                  AuthenticatedSubject var14 = (AuthenticatedSubject)var13.readObject();
                  if (verbose) {
                     Verbose.log((Object)("Subject = " + var14));
                  }

                  var10 = var14;
               }

               SecurityServiceManager.runAs((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction()), var10, new PrivilegedExceptionAction() {
                  public Object run() throws WsException {
                     var6.invoke(var9, var5);
                     return null;
                  }
               });
            }

         } catch (PrivilegedActionException var17) {
            throw new JAXRPCException("Failed to invoke skel", var17.getException());
         } catch (IOException var18) {
            throw new JAXRPCException("Failed to invoke skel", var18);
         } catch (ClassNotFoundException var19) {
            throw new JAXRPCException("Failed to invoke skel", var19);
         } catch (WsException var20) {
            throw new JAXRPCException("Failed to invoke skel", var20);
         }
      }
   }

   private QueueConnectionFactory getConnectionFactory(String var1) throws NamingException {
      InitialContext var2 = new InitialContext();
      boolean var3 = false;
      StringBuffer var4 = new StringBuffer();
      StringBuffer var5 = new StringBuffer();
      var3 = TransportUtil.getForeignCredentials(var1, var2, var4, var5);
      if (var3) {
         this.userName = var4.toString();
         this.passwd = var5.toString();
      }

      return (QueueConnectionFactory)var2.lookup(var1);
   }

   public void shutdown() {
   }

   private AuthenticatedSubject authenticateAs(String var1) {
      try {
         if (this._pa == null) {
            this._pa = (PrincipalAuthenticator)this.getService(ServiceType.AUTHENTICATION);
         }

         return this._pa.impersonateIdentity(var1);
      } catch (LoginException var3) {
         throw new SecurityException("User " + var1 + " is an invalid user");
      }
   }

   private SecurityService getService(SecurityService.ServiceType var1) {
      try {
         SecurityService var2 = SecurityServiceManager.getSecurityService(_kernelId, SecurityServiceManager.getDefaultRealmName(), var1);
         return var2;
      } catch (Exception var4) {
         throw new SecurityException("Unexpected exception: " + var4.toString(), var4);
      }
   }
}
