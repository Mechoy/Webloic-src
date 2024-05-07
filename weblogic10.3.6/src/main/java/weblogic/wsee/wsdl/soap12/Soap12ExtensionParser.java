package weblogic.wsee.wsdl.soap12;

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

public class Soap12ExtensionParser implements WsdlExtensionParser {
   public WsdlExtension parseMessage(WsdlMessage var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseOperation(WsdlOperation var1, Element var2) {
      return null;
   }

   public WsdlExtension parseBinding(WsdlBinding var1, Element var2) throws WsdlException {
      if (WsdlReader.tagEquals(var2, "binding", "http://schemas.xmlsoap.org/wsdl/soap12/")) {
         Soap12Binding var3 = new Soap12Binding();
         var3.parse(var2);
         if (var1 instanceof WsdlBindingBuilder) {
            WsdlBindingBuilder var4 = (WsdlBindingBuilder)var1;
            var4.setBindingType("SOAP12");
            var4.setTransportProtocol(var3.getTransportProtocol());
            var4.setTransportURI(var3.getTransport());
         }

         return var3;
      } else {
         return null;
      }
   }

   public WsdlExtension parseBindingOperation(WsdlBindingOperation var1, Element var2) {
      if (WsdlReader.tagEquals(var2, "operation", "http://schemas.xmlsoap.org/wsdl/soap12/")) {
         Soap12BindingOperation var3 = new Soap12BindingOperation();
         var3.parse(var2);
         return var3;
      } else {
         return null;
      }
   }

   public WsdlExtension parseBindingMessage(WsdlBindingMessage var1, Element var2) throws WsdlException {
      if (WsdlReader.tagEquals(var2, "body", "http://schemas.xmlsoap.org/wsdl/soap12/")) {
         Soap12Body var5 = new Soap12Body();
         var5.parse(var2);
         return var5;
      } else if (WsdlReader.tagEquals(var2, "header", "http://schemas.xmlsoap.org/wsdl/soap12/")) {
         Soap12Header var4 = new Soap12Header();
         var4.parse(var2);
         return var4;
      } else if (WsdlReader.tagEquals(var2, "fault", "http://schemas.xmlsoap.org/wsdl/soap12/")) {
         Soap12Fault var3 = new Soap12Fault();
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
      if (WsdlReader.tagEquals(var2, "address", "http://schemas.xmlsoap.org/wsdl/soap12/")) {
         Soap12Address var3 = new Soap12Address();
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
