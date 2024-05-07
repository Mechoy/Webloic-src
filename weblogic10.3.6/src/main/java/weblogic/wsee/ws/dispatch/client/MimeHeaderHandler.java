package weblogic.wsee.ws.dispatch.client;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.connection.transport.http.HttpTransportInfo;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.util.MimeHeadersUtil;
import weblogic.wsee.util.Verbose;

public class MimeHeaderHandler extends GenericHandler {
   private static final boolean verbose = Verbose.isVerbose(MimeHeaderHandler.class);

   public boolean handleRequest(MessageContext var1) {
      if (!(var1 instanceof SoapMessageContext)) {
         return true;
      } else {
         SoapMessageContext var2 = (SoapMessageContext)var1;
         SOAPMessage var3 = var2.getMessage();
         MimeHeaders var4 = var3.getMimeHeaders();
         TransportInfo var5 = (TransportInfo)var2.getProperty("weblogic.wsee.connection.transportinfo");
         HttpTransportInfo var6 = null;
         if (var5 != null && var5 instanceof HttpTransportInfo) {
            var6 = (HttpTransportInfo)var5;
         }

         Object var7 = var2.getProperty("javax.xml.rpc.security.auth.username");
         Object var8 = var2.getProperty("javax.xml.rpc.security.auth.password");
         if (var7 == null && var6 != null) {
            var7 = var6.getUsername();
         }

         if (var8 == null && var6 != null) {
            var8 = var6.getPassword();
         }

         String var9 = MimeHeadersUtil.getBasicAuthHeaderValue(var7, var8);
         if (var9 != null) {
            var4.setHeader("Authorization", var9);
         }

         Object var10 = var2.getProperty("weblogic.webservice.client.proxyusername");
         Object var11 = var2.getProperty("weblogic.webservice.client.proxypassword");
         if (var10 == null && var6 != null) {
            var10 = var6.getProxyUsername();
         }

         if (var11 == null && var6 != null) {
            var11 = var6.getProxyPassword();
         }

         String var12 = MimeHeadersUtil.getBasicProxyAuthHeaderValue(var10, var11);
         if (var12 != null) {
            var4.addHeader("Proxy-Authorization", var12);
         }

         return true;
      }
   }

   public QName[] getHeaders() {
      return new QName[0];
   }
}
