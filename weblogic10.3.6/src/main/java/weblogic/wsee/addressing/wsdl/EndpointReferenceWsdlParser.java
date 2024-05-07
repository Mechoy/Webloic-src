package weblogic.wsee.addressing.wsdl;

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

public class EndpointReferenceWsdlParser implements WsdlExtensionParser {
   public void cleanUp() {
   }

   public WsdlExtension parseBinding(WsdlBinding var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseBindingMessage(WsdlBindingMessage var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseBindingOperation(WsdlBindingOperation var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseDefinitions(WsdlDefinitions var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseMessage(WsdlMessage var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseOperation(WsdlOperation var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parsePort(WsdlPort var1, Element var2) throws WsdlException {
      return !WsdlReader.tagEquals(var2, "EndpointReference", "http://www.w3.org/2005/08/addressing") && !WsdlReader.tagEquals(var2, "EndpointReference", "http://schemas.xmlsoap.org/ws/2004/08/addressing") ? null : new EndpointReferenceWsdlExtension(var2.cloneNode(true));
   }

   public WsdlExtension parseService(WsdlService var1, Element var2) throws WsdlException {
      return null;
   }
}
