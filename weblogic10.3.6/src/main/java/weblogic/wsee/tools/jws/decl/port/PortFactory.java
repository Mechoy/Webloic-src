package weblogic.wsee.tools.jws.decl.port;

import java.net.URL;
import weblogic.wsee.wsdl.WsdlPort;

public class PortFactory {
   public static Port newPort(String var0) {
      Object var1 = null;
      if (var0.equals("http")) {
         var1 = new HttpPort();
      } else if (var0.equals("https")) {
         var1 = new HttpsPort();
      } else if (var0.equals("local")) {
         var1 = new LocalPort();
      } else if (var0.equals("jms")) {
         var1 = new JmsPort();
      }

      return (Port)var1;
   }

   public static Port newPort(URL var0, WsdlPort var1) {
      Port var2 = newPort(var0.getProtocol());
      if (var2 != null) {
         var2.setContextPath(var0.getFile());
         var2.setServiceUri("");
         var2.setPortName(var1.getName().getLocalPart());
      }

      return var2;
   }
}
