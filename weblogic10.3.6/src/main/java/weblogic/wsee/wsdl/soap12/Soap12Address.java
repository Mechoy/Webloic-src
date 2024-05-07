package weblogic.wsee.wsdl.soap12;

import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.builder.WsdlPortBuilder;
import weblogic.wsee.wsdl.soap11.SoapAddress;

public final class Soap12Address extends SoapAddress implements WsdlExtension {
   public static final String KEY = "SOAP12-address";

   public Soap12Address(String var1) {
      super(var1);
   }

   Soap12Address() {
   }

   protected String getSOAPNS() {
      return "http://schemas.xmlsoap.org/wsdl/soap12/";
   }

   public String getKey() {
      return "SOAP12-address";
   }

   public static Soap12Address attach(WsdlPort var0) {
      Soap12Address var1 = new Soap12Address();
      var0.putExtension(var1);
      if (var0 instanceof WsdlPortBuilder) {
         ((WsdlPortBuilder)var0).setTransport("http");
      }

      return var1;
   }
}
