package weblogic.wsee.server.jms;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Iterator;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.xml.rpc.JAXRPCException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.wsee.addressing.ActionHeader;
import weblogic.wsee.async.SOAPInvokeState;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.connection.ConnectionFactory;
import weblogic.wsee.connection.transport.jms.JMSServerTransport;
import weblogic.wsee.jws.container.ServerResponsePathDispatcher;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.server.WsLifeCycleEvent;
import weblogic.wsee.server.WsLifeCycleListenerRegistry;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsException;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsSkel;

public class WsDispatchMessageListener implements MessageListener {
   private static final boolean verbose = Verbose.isVerbose(WsDispatchMessageListener.class);
   private final WsPort port;
   private boolean isRM = true;

   public WsDispatchMessageListener(WsPort var1) {
      this.port = var1;
   }

   public void setRM(boolean var1) {
      this.isRM = var1;
   }

   public boolean isRM() {
      return this.isRM;
   }

   public void onMessage(Message var1) {
      long var2 = System.nanoTime();
      if (verbose) {
         Verbose.say(var2 + " Entering WsDispatchMessageListener.onMessage()");
      }

      try {
         if (!(var1 instanceof ObjectMessage)) {
            throw new JAXRPCException("Wrong message type, only allow object message");
         }

         Serializable var4 = ((ObjectMessage)var1).getObject();
         if (!(var4 instanceof SOAPInvokeState)) {
            throw new JAXRPCException("Wrong object message received, expected type: weblogic.wsee.async.SOAPInvokeState");
         }

         String var5 = var1.getStringProperty("ASYNC_URI");
         SOAPInvokeState var6 = (SOAPInvokeState)var4;

         try {
            int var7 = var1.getIntProperty("JMSXDeliveryCount");
            var6.getMessageContextProperties().put("weblogic.wsee.buffer.BufferedMessageJmsDeliveryCount", var7);
         } catch (Exception var12) {
            if (verbose) {
               Verbose.logException(var12);
            } else {
               var12.printStackTrace();
            }
         }

         AuthenticatedSubject var15 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         AuthenticatedSubject var8 = var6.getSubject(var15);
         AuthorizedInvoke var9 = new AuthorizedInvoke(var6, var5);
         if (var8 != null) {
            try {
               if (verbose) {
                  Verbose.say(var2 + " Inside WsDispatchMessageListener.onMessage() == 1");
               }

               SecurityServiceManager.runAs(var15, var8, var9);
               if (verbose) {
                  Verbose.say(var2 + " Inside WsDispatchMessageListener.onMessage() == 2");
               }
            } catch (PrivilegedActionException var11) {
               if (var11.getException() instanceof IOException) {
                  throw (IOException)var11.getException();
               }

               if (var11.getException() instanceof WsException) {
                  throw new JAXRPCException(var11.getException());
               }

               throw new UndeclaredThrowableException(var11.getException());
            }
         } else {
            var9.run();
         }
      } catch (JAXRPCException var13) {
         throw var13;
      } catch (Exception var14) {
         if (verbose) {
            Verbose.logException(var14);
         }

         throw new JAXRPCException(var14.toString(), var14);
      }

      if (verbose) {
         Verbose.say(var2 + " Exiting WsDispatchMessageListener.onMessage()");
      }

   }

   private class AuthorizedInvoke implements PrivilegedExceptionAction {
      SoapMessageContext ctx;
      String targetURI;

      AuthorizedInvoke(SOAPInvokeState var2, String var3) {
         this.ctx = new SoapMessageContext(var2.isSoap12());
         Iterator var4 = var2.getMessageContextProperties().keySet().iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            this.ctx.setProperty(var5, var2.getMessageContextProperties().get(var5));
         }

         this.ctx.setMessage(var2.getSOAPMessage());
         this.targetURI = var3;
      }

      public Object run() throws Exception {
         String var1 = (String)this.ctx.getProperty("weblogic.wsee.reliability.RequestMessageSeqID");
         String var2 = (String)this.ctx.getProperty("weblogic.wsee.reliability.RequestMessageSeqNumber");
         boolean var3 = false;
         String var4 = "Unknown";
         ActionHeader var5 = (ActionHeader)this.ctx.getHeaders().getHeader(ActionHeader.TYPE);
         if (var5 != null) {
            var4 = var5.getActionURI();
            if ("http://schemas.xmlsoap.org/ws/2005/02/rm/LastMessage".equals(var5.getActionURI())) {
               if (WsDispatchMessageListener.verbose) {
                  Verbose.say("WsDispatchMessageListener IGNORING WS-RM 'LastMessage' message.");
               }

               var3 = true;
            }
         }

         if (WsDispatchMessageListener.verbose && (var1 != null || var2 != null)) {
            Verbose.say("*** WsDispatchMessageListener received buffered message with requestSeqId " + var1 + " requestSeqNum " + var2 + " action(" + var4 + ")");
         }

         try {
            if (!var3) {
               if (WsDispatchMessageListener.verbose && (var1 != null || var2 != null)) {
                  Verbose.say("*** Processing buffered sequence message with requestSeqId " + var1 + " requestSeqNum " + var2 + " action(" + var4 + ")");
               }

               WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_RECV_AFTER_ENQUEUE);
               String var6 = WsDispatchMessageListener.this.port.getWsdlPort().getBinding().getBindingType();
               JMSServerTransport var7 = new JMSServerTransport(this.targetURI);
               WsSkel var8 = (WsSkel)WsDispatchMessageListener.this.port.getEndpoint();
               Connection var9 = ConnectionFactory.instance().createServerConnection(var7, var6);
               var8.invoke(var9, WsDispatchMessageListener.this.port, this.ctx);
               ServerResponsePathDispatcher.handleRMEndProcessing(this.ctx);
            } else if (WsDispatchMessageListener.verbose && (var1 != null || var2 != null)) {
               Verbose.say("*** Skipping/ignoring processing for buffered sequence message with requestSeqId " + var1 + " requestSeqNum " + var2 + " action(" + var4 + ")");
            }

            return null;
         } catch (Exception var10) {
            if (WsDispatchMessageListener.verbose) {
               Verbose.logException(var10);
            }

            throw var10;
         }
      }
   }
}
