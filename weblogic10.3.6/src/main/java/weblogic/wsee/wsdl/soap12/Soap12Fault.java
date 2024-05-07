package weblogic.wsee.wsdl.soap12;

import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.soap11.SoapFault;

public class Soap12Fault extends SoapFault implements WsdlExtension {
   private static final String KEY = "SOAP12-fault";

   protected String getSOAPNS() {
      return "http://schemas.xmlsoap.org/wsdl/soap12/";
   }

   public String getKey() {
      return "SOAP12-fault";
   }

   public static Soap12Fault narrow(WsdlBindingMessage var0) {
      return (Soap12Fault)var0.getExtension("SOAP12-fault");
   }

   public static Soap12Fault attach(WsdlBindingMessage var0) {
      Soap12Fault var1 = new Soap12Fault();
      var0.putExtension(var1);
      return var1;
   }
}
