package weblogic.wsee.wsdl.soap12;

import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.builder.WsdlBindingBuilder;
import weblogic.wsee.wsdl.soap11.SoapBinding;

public class Soap12Binding extends SoapBinding implements WsdlExtension {
   public static final String KEY = "SOAP12";

   protected String getSOAPNS() {
      return "http://schemas.xmlsoap.org/wsdl/soap12/";
   }

   public String getKey() {
      return "SOAP12";
   }

   public static Soap12Binding narrow(WsdlBinding var0) {
      return (Soap12Binding)var0.getExtension("SOAP12");
   }

   public static Soap12Binding attach(WsdlBinding var0) throws WsdlException {
      Soap12Binding var1 = new Soap12Binding();
      if (var0 instanceof WsdlBindingBuilder) {
         WsdlBindingBuilder var2 = (WsdlBindingBuilder)var0;
         var2.setBindingType("SOAP12");
         var2.setTransportProtocol(var1.getTransportProtocol());
      }

      var0.putExtension(var1);
      return var1;
   }

   static {
      knownProtocols.put("http://schemas.xmlsoap.org/soap12/http", "http");
      knownProtocols.put("http://schemas.xmlsoap.org/soap12/https", "https");
      knownProtocols.put("http://www.openuri.org/2002/04/soap12/jms/", "jms");
   }
}
