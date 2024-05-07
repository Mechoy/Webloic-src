package weblogic.wsee.server.jms;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Map;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.server.ServletEndpointContext;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.wsee.async.SOAPInvokeState;
import weblogic.wsee.buffer.NoRetryException;
import weblogic.wsee.jws.container.Request;
import weblogic.wsee.util.DirectInvokeUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsRegistry;
import weblogic.wsee.ws.WsSkel;

public class WsClientBufferingErrorMessageListener implements MessageListener {
   private static final boolean verbose = Verbose.isVerbose(WsClientBufferingErrorMessageListener.class);

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
                        } catch (PrivilegedActionException var17) {
                           if (var17.getException() instanceof IOException) {
                              throw (IOException)var17.getException();
                           }

                           throw new UndeclaredThrowableException(var17.getException());
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
      } catch (Throwable var19) {
         throw new NoRetryException(var19);
      }
   }

   private WsPort getPort(String var1) {
      String var2 = WsRegistry.getURL(var1);
      String var3 = WsRegistry.getVersion(var1);
      return WsRegistry.instance().lookup(var2, var3);
   }

   private class AuthorizedInvoke implements PrivilegedExceptionAction {
      SOAPInvokeState sis;

      AuthorizedInvoke(SOAPInvokeState var2) {
         this.sis = var2;
      }

      public Object run() throws Exception {
         Map var1 = this.sis.getMessageContextProperties();

         try {
            String var2 = (String)var1.get("weblogic.wsee.enclosing.classname");
            if (var2 == null) {
               if (WsClientBufferingErrorMessageListener.verbose) {
                  Verbose.log((Object)"No target class found");
               }

               return null;
            }

            String var3 = "onAsyncFailure";
            Class[] var4 = new Class[]{String.class, Object[].class};
            Object[] var5 = new Object[]{(String)var1.get("weblogic.wsee.callback.client.methodname"), (Object[])((Object[])var1.get("weblogic.wsee.callback.client.args"))};
            String var6 = (String)var1.get("weblogic.wsee.enclosing.jws.serviceuri");
            String var7 = (String)var1.get("weblogic.wsee.conversation.ConversationId");
            Request var8 = new Request(var2, var3, var4, var5);
            DirectInvokeUtil.invoke(var6, var8, var7, (ServletEndpointContext)null);
         } catch (Throwable var9) {
            if (WsClientBufferingErrorMessageListener.verbose) {
               Verbose.logException(var9);
            }
         }

         return null;
      }
   }
}
