package weblogic.wsee.connection.soap;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.connection.transport.ClientTransport;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.connection.transport.http.HttpTransportInfo;
import weblogic.wsee.connection.transport.https.HTTPSClientTransport;
import weblogic.wsee.connection.transport.https.SSLAdapter;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.util.EndpointAddressUtil;
import weblogic.wsee.util.Verbose;
import weblogic.xml.saaj.MessageFactoryImpl;

public class SoapClientConnection extends SoapConnection {
   public static final String TRANSPORT_INFO_PROPERTY = "weblogic.wsee.connection.transportinfo";
   private static final boolean verbose = Verbose.isVerbose(SoapClientConnection.class);

   public void send(MessageContext var1) throws IOException {
      TransportInfo var2 = (TransportInfo)var1.getProperty("weblogic.wsee.connection.transportinfo");
      String var3 = getDestinationAddress(var1);
      ClientTransport var4 = (ClientTransport)this.getTransport();
      SSLAdapter var5 = (SSLAdapter)var1.getProperty("weblogic.wsee.client.ssladapter");
      if (var5 != null && var4 instanceof HTTPSClientTransport) {
         ((HTTPSClientTransport)var4).setSSLAdapter(var5);
      }

      this.setTimeout(var1, var4);
      var4.connect(var3, var2);
      SOAPMessage var6 = ((SOAPMessageContext)var1).getMessage();
      WlMessageContext var7 = (WlMessageContext)var1;
      MimeHeaders var8 = var6.getMimeHeaders();
      String var9 = (String)var7.getProperty("weblogic.wsee.transport.jms.url");
      String var10 = (String)var7.getProperty("javax.xml.rpc.security.auth.username");
      String var11 = (String)var7.getProperty("javax.xml.rpc.security.auth.password");
      if (var10 == null && var2 instanceof HttpTransportInfo) {
         HttpTransportInfo var12 = (HttpTransportInfo)var2;
         if (var12.getUsername() != null) {
            assert var12.getPassword() != null;

            var10 = new String(var12.getUsername());
            var11 = new String(var12.getPassword());
         }
      }

      String var14 = (String)var7.getProperty("weblogic.wsee.transport.jms.messagetype");
      if (var9 != null) {
         if (verbose) {
            Verbose.say("Adding JMS JNDI URL to header " + var9);
         }

         var8.addHeader("weblogic.wsee.transport.jms.url", var9);
      }

      String var13 = EndpointAddressUtil.getProtocolFromEndpointAddress((MessageContext)var7);
      if (var13.equalsIgnoreCase("jms") && var10 != null) {
         var8.addHeader("javax.xml.rpc.security.auth.username", var10);
         var8.addHeader("javax.xml.rpc.security.auth.password", var11);
      }

      if (var14 != null) {
         if (verbose) {
            Verbose.say("Adding JMS Message Type to header " + var14);
         }

         var8.addHeader("weblogic.wsee.transport.jms.messagetype", var14);
      }

      super.send(var1);
   }

   private void setTimeout(MessageContext var1, ClientTransport var2) {
      Integer var3 = (Integer)var1.getProperty("weblogic.wsee.transport.read.timeout");
      if (verbose) {
         Verbose.log((Object)("Read timeout: " + var3));
      }

      Integer var4 = (Integer)var1.getProperty("weblogic.wsee.transport.connection.timeout");
      if (verbose) {
         Verbose.log((Object)("Connection timeout: " + var4));
      }

      if (var3 != null) {
         var2.setReadTimeout(var3);
      }

      if (var4 != null) {
         var2.setConnectionTimeout(var4);
      }

   }

   public static String getDestinationAddress(MessageContext var0) {
      return EndpointAddressUtil.getEndpointAddress(var0);
   }

   SOAPMessage createSOAPMessage(SoapMessageContext var1, MimeHeaders var2, InputStream var3, boolean var4) throws IOException, SOAPException {
      return ((MessageFactoryImpl)var1.getMessageFactory()).createMessage(var2, var3, false, var4);
   }
}
