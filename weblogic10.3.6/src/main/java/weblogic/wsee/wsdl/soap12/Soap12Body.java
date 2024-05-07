package weblogic.wsee.wsdl.soap12;

import java.util.Iterator;
import java.util.List;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.mime.MimeMultipartRelated;
import weblogic.wsee.wsdl.mime.MimePart;
import weblogic.wsee.wsdl.soap11.SoapBody;

public class Soap12Body extends SoapBody implements WsdlExtension {
   public static final String KEY = "SOAP12-body";

   protected String getSOAPNS() {
      return "http://schemas.xmlsoap.org/wsdl/soap12/";
   }

   public String getKey() {
      return "SOAP12-body";
   }

   public static Soap12Body narrow(WsdlBindingMessage var0) {
      Soap12Body var1 = (Soap12Body)var0.getExtension("SOAP12-body");
      if (var1 == null) {
         MimeMultipartRelated var2 = MimeMultipartRelated.narrow(var0);
         if (var2 != null) {
            List var3 = var2.getParts();
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               MimePart var5 = (MimePart)var4.next();
               var1 = narrow(var5);
               if (var1 != null) {
                  if (var1.getNamespace() == null) {
                     var1.setNamespace(var0.getBindingOperation().getName().getNamespaceURI());
                  }
                  break;
               }
            }
         }
      }

      return var1;
   }

   public static Soap12Body narrow(MimePart var0) {
      return (Soap12Body)var0.getExtension("SOAP12-body");
   }

   public static Soap12Body attach(WsdlBindingMessage var0) {
      Soap12Body var1 = new Soap12Body();
      var0.putExtension(var1);
      return var1;
   }
}
