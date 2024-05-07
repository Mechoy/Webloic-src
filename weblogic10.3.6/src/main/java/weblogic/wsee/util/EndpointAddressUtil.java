package weblogic.wsee.util;

import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.ws.dispatch.client.ClientDispatcher;
import weblogic.wsee.wsdl.WsdlUtils;
import weblogic.wsee.wsdl.soap11.SoapAddress;

public class EndpointAddressUtil {
   public static String getEndpointAddress(MessageContext var0) {
      String var1 = (String)var0.getProperty("javax.xml.rpc.service.endpoint.address");
      if (var1 != null) {
         return var1;
      } else {
         ClientDispatcher var2 = (ClientDispatcher)WlMessageContext.narrow(var0).getDispatcher();
         if (var2 == null) {
            return null;
         } else {
            SoapAddress var3 = WsdlUtils.getSoapAddress(var2.getWsdlPort());
            return var3.getLocation();
         }
      }
   }

   public static String getProtocolFromEndpointAddress(MessageContext var0) {
      return getProtocolFromEndpointAddress(getEndpointAddress(var0));
   }

   public static String getProtocolFromEndpointAddress(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.indexOf(58);
         return var1 <= 0 ? null : var0.substring(0, var1);
      }
   }
}
