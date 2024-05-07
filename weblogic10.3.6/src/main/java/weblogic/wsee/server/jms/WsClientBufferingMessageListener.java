package weblogic.wsee.server.jms;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Iterator;
import java.util.List;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Element;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.wsee.async.SOAPInvokeState;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.connection.ConnectionFactory;
import weblogic.wsee.handler.InvocationException;
import weblogic.wsee.message.UnknownMsgHeader;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.util.ControlAPIUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsRegistry;
import weblogic.wsee.ws.WsSkel;

public class WsClientBufferingMessageListener implements MessageListener {
   private static final boolean verbose = Verbose.isVerbose(WsClientBufferingMessageListener.class);

   public void onMessage(Message var1) {
      try {
         if (var1 instanceof ObjectMessage) {
            String var2 = var1.getStringProperty("SERV_URI");
            if (var2 == null) {
               throw new JAXRPCException("No service URI found");
            } else {
               WsPort var3 = this.getPort(var2);
               if (var3 == null) {
                  throw new JAXRPCException("No port found for " + var2);
               } else {
                  ClassLoader var4 = ((WsSkel)var3.getEndpoint()).getClassLoader();
                  Thread var5 = Thread.currentThread();
                  ClassLoader var6 = var5.getContextClassLoader();

                  try {
                     var5.setContextClassLoader(var4);
                     Serializable var7 = ((ObjectMessage)var1).getObject();
                     if (!(var7 instanceof SOAPInvokeState)) {
                        throw new JAXRPCException("Wrong object message received, expected type: weblogic.wsee.async.SOAPInvokeState");
                     }

                     SOAPInvokeState var8 = (SOAPInvokeState)var7;
                     AuthenticatedSubject var9 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
                     AuthenticatedSubject var10 = var8.getSubject(var9);
                     AuthorizedInvoke var11 = new AuthorizedInvoke(var8);
                     if (var10 != null) {
                        try {
                           SecurityServiceManager.runAs(var9, var10, var11);
                        } catch (PrivilegedActionException var18) {
                           if (var18.getException() instanceof IOException) {
                              throw (IOException)var18.getException();
                           }

                           throw new UndeclaredThrowableException(var18.getException());
                        }
                     } else {
                        var11.run();
                     }
                  } finally {
                     var5.setContextClassLoader(var6);
                  }

               }
            }
         } else {
            throw new JAXRPCException("Wrong message type, only allow object message");
         }
      } catch (JAXRPCException var20) {
         throw var20;
      } catch (Exception var21) {
         var21.printStackTrace();
         throw new JAXRPCException(var21);
      }
   }

   private WsPort getPort(String var1) {
      String var2 = WsRegistry.getURL(var1);
      String var3 = WsRegistry.getVersion(var1);
      return WsRegistry.instance().lookup(var2, var3);
   }

   private class AuthorizedInvoke implements PrivilegedExceptionAction {
      SoapMessageContext ctx;

      AuthorizedInvoke(SOAPInvokeState var2) {
         this.ctx = new SoapMessageContext(var2.isSoap12());
         Iterator var3 = var2.getMessageContextProperties().keySet().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            this.ctx.setProperty(var4, var2.getMessageContextProperties().get(var4));
         }

         this.ctx.setMessage(var2.getSOAPMessage());
      }

      public Object run() throws Exception {
         String var1 = (String)this.ctx.getProperty("weblogic.wsee.binding.type");
         String var2 = (String)this.ctx.getProperty("javax.xml.rpc.service.endpoint.address");
         String var3 = var2.substring(0, var2.indexOf(58));
         Connection var4 = ConnectionFactory.instance().createClientConnection(var3, var1);
         this.writeOutputHeaders(this.ctx);

         try {
            var4.send(this.ctx);
            var4.getTransport().confirmOneway();
            return null;
         } catch (IOException var6) {
            throw new InvocationException("Failed to send message using connection:" + var4, var6);
         }
      }

      private void writeOutputHeaders(WlMessageContext var1) {
         List var2 = (List)var1.getProperty("weblogic.wsee.OutputHeaders");
         if (var2 != null) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               Element var4 = (Element)var3.next();
               var1.getHeaders().addHeader(new UnknownMsgHeader(var4));
            }

            ControlAPIUtil.unsetOutputHeaders((MessageContext)var1);
         }

      }
   }
}
