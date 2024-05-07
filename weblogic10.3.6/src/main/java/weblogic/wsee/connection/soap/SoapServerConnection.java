package weblogic.wsee.connection.soap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.connection.transport.ServerTransport;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.WLMessageFactory;
import weblogic.xml.saaj.MessageFactoryImpl;

public class SoapServerConnection extends SoapConnection {
   private static final boolean verbose = Verbose.isVerbose(SoapServerConnection.class);

   public void send(MessageContext var1) throws IOException {
      SOAPMessage var2 = ((SOAPMessageContext)var1).getMessage();
      WlMessageContext var3 = (WlMessageContext)var1;
      MimeHeaders var4 = var2.getMimeHeaders();
      this.addCustomHeaders(var3, var4);
      String var5;
      if ((var5 = (String)var3.getProperty("weblogic.wsee.transport.jms.url")) != null) {
         if (verbose) {
            Verbose.say("Adding JMS JNDI URL to header " + var5);
         }

         var4.addHeader("weblogic.wsee.transport.jms.url", var5);
      }

      String var6;
      if ((var6 = (String)var3.getProperty("javax.xml.rpc.security.auth.username")) != null) {
         if (verbose) {
            Verbose.say("Adding JMS username/passwrod to header " + var6);
         }

         var4.addHeader("javax.xml.rpc.security.auth.username", var6);
      }

      String var7;
      if ((var7 = (String)var3.getProperty("javax.xml.rpc.security.auth.password")) != null) {
         var4.addHeader("javax.xml.rpc.security.auth.password", var7);
      }

      String var8;
      if ((var8 = (String)var3.getProperty("weblogic.wsee.transport.jms.messagetype")) != null) {
         if (verbose) {
            Verbose.say("Adding JMS JNDI URL to header " + var8);
         }

         var4.addHeader("weblogic.wsee.transport.jms.messagetype", var8);
      }

      dumpSoapMsg((SoapMessageContext)var3, true);
      ServerTransport var10 = (ServerTransport)this.getTransport();
      OutputStream var9;
      if (var3.hasFault()) {
         if ("true".equals(var3.getProperty("weblogic.wsee.AuthRequired"))) {
            var9 = var10.sendAuthorizationRequiredFault(var4);
         } else if (this.isAuthError(var2)) {
            var9 = var10.sendAuthorizationFault(var4);
         } else {
            var9 = var10.sendGeneralFault(var4);
         }
      } else {
         var9 = var10.send(var4);
      }

      IOException var11 = null;

      try {
         var2.writeTo(var9);
      } catch (SOAPException var18) {
         if (verbose) {
            Verbose.logException(var18);
         }

         IOException var13 = new IOException("Could not write SOAPMessage" + var18.toString());
         var13.initCause(var18);
         var11 = var13;
         throw var13;
      } catch (IOException var19) {
         var11 = var19;
         throw var19;
      } finally {
         this.safeClose(var9, var11);
         this.cleanUpCachedInputStream();
      }

      if (verbose) {
         Verbose.log((Object)"Message send ok");
      }

   }

   private boolean isAuthError(SOAPMessage var1) {
      try {
         SOAPFault var2 = var1.getSOAPBody().getFault();
         if (var2 != null) {
            Name var3 = var2.getFaultCodeAsName();
            if ("http://schemas.xmlsoap.org/soap/envelope/".equals(var3.getURI()) && "Client.Authentication".equals(var3.getLocalName())) {
               return true;
            }
         }

         return false;
      } catch (SOAPException var4) {
         return false;
      }
   }

   SOAPMessage createSOAPMessage(SoapMessageContext var1, MimeHeaders var2, InputStream var3, boolean var4) throws IOException, SOAPException {
      Boolean var5 = (Boolean)var1.getProperty("weblogic.wsee.stream_attachments");
      if (var5 != null && var5) {
         return WLMessageFactory.createMessageWithStreamingAttachments(var2, var3, var1.isSoap12());
      } else {
         return var4 ? ((MessageFactoryImpl)var1.getMessageFactory()).createMessage(var2, var3, false, true) : var1.getMessageFactory().createMessage(var2, var3);
      }
   }
}
