package weblogic.wsee.wsdl.soap11;

import org.w3c.dom.Element;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlExtensionParser;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.builder.WsdlBindingBuilder;

public class SoapExtensionParser implements WsdlExtensionParser {
   public WsdlExtension parseMessage(WsdlMessage var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseOperation(WsdlOperation var1, Element var2) {
      return null;
   }

   public WsdlExtension parseBinding(WsdlBinding var1, Element var2) throws WsdlException {
      if (WsdlReader.tagEquals(var2, "binding", "http://schemas.xmlsoap.org/wsdl/soap/")) {
         SoapBinding var3 = new SoapBinding();
         var3.parse(var2);
         if (var1 instanceof WsdlBindingBuilder) {
            WsdlBindingBuilder var4 = (WsdlBindingBuilder)var1;
            var4.setBindingType("SOAP11");
            var4.setTransportProtocol(var3.getTransportProtocol());
            var4.setTransportURI(var3.getTransport());
         }

         return var3;
      } else {
         return null;
      }
   }

   public WsdlExtension parseBindingOperation(WsdlBindingOperation var1, Element var2) {
      if (WsdlReader.tagEquals(var2, "operation", "http://schemas.xmlsoap.org/wsdl/soap/")) {
         SoapBindingOperation var3 = new SoapBindingOperation();
         var3.parse(var2);
         return var3;
      } else {
         return null;
      }
   }

   public WsdlExtension parseBindingMessage(WsdlBindingMessage var1, Element var2) throws WsdlException {
      if (WsdlReader.tagEquals(var2, "body", "http://schemas.xmlsoap.org/wsdl/soap/")) {
         SoapBody var5 = new SoapBody();
         var5.parse(var2);
         return var5;
      } else if (WsdlReader.tagEquals(var2, "header", "http://schemas.xmlsoap.org/wsdl/soap/")) {
         SoapHeader var4 = new SoapHeader();
         var4.parse(var2);
         return var4;
      } else if (WsdlReader.tagEquals(var2, "fault", "http://schemas.xmlsoap.org/wsdl/soap/")) {
         SoapFault var3 = new SoapFault();
         var3.parse(var2);
         return var3;
      } else {
         return null;
      }
   }

   public WsdlExtension parseService(WsdlService var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parsePort(WsdlPort var1, Element var2) throws WsdlException {
      if (WsdlReader.tagEquals(var2, "address", "http://schemas.xmlsoap.org/wsdl/soap/")) {
         SoapAddress var3 = new SoapAddress();
         var3.parse(var2, var1);
         return var3;
      } else {
         return null;
      }
   }

   public WsdlExtension parseDefinitions(WsdlDefinitions var1, Element var2) throws WsdlException {
      return null;
   }

   public void cleanUp() {
   }
}
