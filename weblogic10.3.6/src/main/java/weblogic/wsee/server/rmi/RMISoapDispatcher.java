package weblogic.wsee.server.rmi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Iterator;
import javax.xml.rpc.JAXRPCException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.wsee.async.SOAPInvokeState;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.connection.ConnectionFactory;
import weblogic.wsee.connection.transport.rmi.RMIServerTransport;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.WLMessageFactory;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsSkel;

public class RMISoapDispatcher {
   private static final boolean verbose = Verbose.isVerbose(RMISoapDispatcher.class);
   private final WsPort port;

   public RMISoapDispatcher(WsPort var1) {
      this.port = var1;
   }

   public SOAPMessage dispatch(SOAPInvokeState var1, String var2) {
      try {
         AuthorizedInvoke var3 = new AuthorizedInvoke(var1, var2);
         AuthenticatedSubject var4 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         AuthenticatedSubject var5 = var1.getSubject(var4);
         if (var5 != null) {
            try {
               return (SOAPMessage)SecurityServiceManager.runAs(var4, var5, var3);
            } catch (PrivilegedActionException var7) {
               if (var7.getException() instanceof IOException) {
                  throw (IOException)var7.getException();
               } else {
                  throw new UndeclaredThrowableException(var7.getException());
               }
            }
         } else {
            return (SOAPMessage)var3.run();
         }
      } catch (JAXRPCException var8) {
         throw var8;
      } catch (Exception var9) {
         throw new JAXRPCException(var9);
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
         String var1 = RMISoapDispatcher.this.port.getWsdlPort().getBinding().getBindingType();
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         RMIServerTransport var3 = new RMIServerTransport(this.targetURI, var2);
         WsSkel var4 = (WsSkel)RMISoapDispatcher.this.port.getEndpoint();
         Connection var5 = ConnectionFactory.instance().createServerConnection(var3, var1);
         var4.invoke(var5, RMISoapDispatcher.this.port, this.ctx);
         byte[] var6 = var2.toByteArray();
         if (var6.length <= 0) {
            return null;
         } else {
            if (RMISoapDispatcher.verbose) {
               Verbose.log((Object)("Length is " + var6.length));
            }

            ByteArrayInputStream var7 = new ByteArrayInputStream(var6);
            MessageFactory var8 = WLMessageFactory.getInstance().getMessageFactory("SOAP12".equals(var1));
            return var8.createMessage(new MimeHeaders(), var7);
         }
      }
   }
}
