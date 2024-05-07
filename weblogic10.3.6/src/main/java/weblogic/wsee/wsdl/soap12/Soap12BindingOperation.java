package weblogic.wsee.wsdl.soap12;

import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.soap11.SoapBindingOperation;

public class Soap12BindingOperation extends SoapBindingOperation implements WsdlExtension {
   public static final String KEY = "SOAP12-binding-operation";

   protected String getSOAPNS() {
      return "http://schemas.xmlsoap.org/wsdl/soap12/";
   }

   public String getKey() {
      return "SOAP12-binding-operation";
   }

   public static Soap12BindingOperation narrow(WsdlBindingOperation var0) {
      return (Soap12BindingOperation)var0.getExtension("SOAP12-binding-operation");
   }

   public static Soap12BindingOperation attach(WsdlBindingOperation var0) {
      Soap12BindingOperation var1 = new Soap12BindingOperation();
      var0.putExtension(var1);
      return var1;
   }
}
