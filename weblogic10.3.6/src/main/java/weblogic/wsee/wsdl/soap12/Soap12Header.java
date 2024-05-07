package weblogic.wsee.wsdl.soap12;

import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.mime.MimePart;
import weblogic.wsee.wsdl.soap11.SoapHeader;

public class Soap12Header extends SoapHeader implements WsdlExtension {
   private static final String KEY = "SOAP12-header";

   protected String getSOAPNS() {
      return "http://schemas.xmlsoap.org/wsdl/soap12/";
   }

   public String getKey() {
      return "SOAP12-header";
   }

   public static Soap12Header narrow(WsdlBindingMessage var0) {
      return (Soap12Header)var0.getExtension("SOAP12-header");
   }

   public static Soap12Header narrow(MimePart var0) {
      return (Soap12Header)var0.getExtension("SOAP12-header");
   }

   public static Soap12Header attach(WsdlBindingMessage var0) {
      Soap12Header var1 = new Soap12Header();
      var0.putExtension(var1);
      return var1;
   }
}
