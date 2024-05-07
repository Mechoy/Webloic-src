package weblogic.wsee.util;

import java.io.IOException;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Element;
import weblogic.wsee.addressing.AddressingHelper;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.message.WlMessageContext;

public class AddressingUtil {
   public static boolean isAnonymous(EndpointReference var0) {
      return AddressingHelper.isAnonymousReferenceURI(var0.getAddress());
   }

   public static boolean isAnonymous(MessageContext var0, EndpointReference var1) {
      return AddressingHelper.isAnonymousEndpointReference(var0, var1);
   }

   public static void confirmOneway(WlMessageContext var0) {
      try {
         var0.getDispatcher().getConnection().getTransport().confirmOneway();
         var0.setProperty("weblogic.wsee.oneway.confirmed", "true");
      } catch (IOException var2) {
         throw new JAXRPCException(var2);
      }
   }

   public static String wrapSOAPAction(String var0) {
      StringBuffer var1 = new StringBuffer();
      if (!var0.startsWith("\"")) {
         var1.append("\"");
      }

      var1.append(var0);
      if (!var0.endsWith("\"")) {
         var1.append("\"");
      }

      return var1.toString();
   }

   public static Element retrieveAddressElement(Element var0) {
      if (null == var0) {
         return null;
      } else {
         Element var1 = (Element)var0.getElementsByTagNameNS("http://www.w3.org/2005/08/addressing", "Address").item(0);
         if (var1 == null) {
            var1 = (Element)var0.getElementsByTagNameNS("http://schemas.xmlsoap.org/ws/2004/08/addressing", "Address").item(0);
         }

         return var1;
      }
   }
}
