package weblogic.wsee.util;

import java.util.HashMap;
import javax.xml.rpc.server.ServletEndpointContext;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.connection.ConnectionFactory;
import weblogic.wsee.connection.local.LocalConnection;
import weblogic.wsee.handler.DirectInvokeData;
import weblogic.wsee.jws.container.Request;
import weblogic.wsee.message.soap.SoapMessageContext;

public class DirectInvokeUtil {
   private static final String LOCAL_TRANSPORT = "local";
   private static final String SOAP_VERSION = "SOAP11";

   public static Object invoke(String var0, Request var1, String var2, ServletEndpointContext var3) throws Throwable {
      SoapMessageContext var4 = new SoapMessageContext();
      MessageFactory var5 = WLMessageFactory.getInstance().getMessageFactory(var4.isSoap12());
      Object var6 = null;
      Throwable var7 = null;
      SOAPMessage var8 = var5.createMessage();
      String var9 = "local:/" + var0;
      var4.setProperty("javax.xml.rpc.service.endpoint.address", var9);
      var4.setMessage(var8);
      DirectInvokeData var10 = new DirectInvokeData();
      var10.setRequest(var1);
      var10.setConversationId(var2);
      var10.setContext(var3);
      HashMap var11 = new HashMap();
      var11.put("weblogic.wsee.direct.invoke.data.prop", var10);
      var4.setProperty("weblogic.wsee.local.invoke.request", var11);
      Connection var12 = ConnectionFactory.instance().createClientConnection("local", "SOAP11");
      if (var3 != null) {
         ((LocalConnection)var12).setIncomingContext(var3.getMessageContext());
      }

      var12.send(var4);
      var12.receive(var4);
      var6 = var4.getProperty("weblogic.wsee.direct.invoke.response.prop");
      var7 = (Throwable)var4.getProperty("weblogic.wsee.local.invoke.throwable");
      if (var7 != null) {
         throw var7;
      } else {
         return var6;
      }
   }
}
