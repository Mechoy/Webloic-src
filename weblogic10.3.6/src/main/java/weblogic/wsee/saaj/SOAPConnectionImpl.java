package weblogic.wsee.saaj;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.connection.ConnectionException;
import weblogic.wsee.connection.ConnectionFactory;
import weblogic.wsee.connection.transport.Transport;
import weblogic.wsee.connection.transport.http.HTTPClientTransport;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.xml.saaj.SOAPMessageImpl;

public class SOAPConnectionImpl extends SOAPConnection {
   private boolean closed = false;

   public SOAPMessage call(SOAPMessage var1, Object var2) throws SOAPException {
      if (this.closed) {
         throw new SOAPException("Connection already closed");
      } else if (var2 == null) {
         throw new IllegalArgumentException("Endpoint can not be null");
      } else {
         String var3;
         if (var2 instanceof String) {
            var3 = (String)var2;
         } else {
            if (!(var2 instanceof URL)) {
               throw new IllegalArgumentException("Endpoint should be String or URL");
            }

            var3 = var2.toString();
         }

         ConnectionFactory var4 = ConnectionFactory.instance();
         Connection var5 = null;

         try {
            if (var3.startsWith("https")) {
               var5 = var4.createClientConnection("https", getBinding(var1));
            } else {
               var5 = var4.createClientConnection("http", getBinding(var1));
            }
         } catch (ConnectionException var9) {
            throw new SOAPException("Unable to create connection.  Error: ", var9);
         }

         SoapMessageContext var6 = new SoapMessageContext(isSoap12(var1));
         var6.setProperty("javax.xml.rpc.service.endpoint.address", var3);
         var6.setMessage(var1);
         if (var1.getProperty("weblogic.wsee.client.ssladapter") != null) {
            var6.setProperty("weblogic.wsee.client.ssladapter", var1.getProperty("weblogic.wsee.client.ssladapter"));
         }

         try {
            var5.send(var6);
         } catch (IOException var8) {
            throw new SOAPException("Unable to send message.", var8);
         }

         try {
            Transport var7 = var5.getTransport();
            if (var7 instanceof HTTPClientTransport && this.confirmOneway(((HTTPClientTransport)var7).getResponseCode())) {
               return null;
            }

            var5.receive(var6);
         } catch (IOException var10) {
            throw new SOAPException("Unable to receive message.", var10);
         }

         return var6.getMessage();
      }
   }

   private static boolean isSoap12(SOAPMessage var0) throws SOAPException {
      return var0.getSOAPPart().getEnvelope().getNamespaceURI().equals("http://www.w3.org/2003/05/soap-envelope");
   }

   private static String getBinding(SOAPMessage var0) throws SOAPException {
      return isSoap12(var0) ? "SOAP12" : "SOAP11";
   }

   public SOAPMessage get(Object var1) throws SOAPException {
      GetSOAPMessageImpl var2 = new GetSOAPMessageImpl();
      return this.call(var2, var1);
   }

   public void close() throws SOAPException {
      if (this.closed) {
         throw new SOAPException("connection already closed");
      } else {
         this.closed = true;
      }
   }

   private boolean confirmOneway(int var1) {
      return var1 == 202;
   }

   static final class GetSOAPMessageImpl extends SOAPMessageImpl {
      GetSOAPMessageImpl() throws SOAPException {
         super("http://www.w3.org/2003/05/soap-envelope");
      }

      public void writeTo(OutputStream var1) throws SOAPException, IOException {
      }
   }
}
