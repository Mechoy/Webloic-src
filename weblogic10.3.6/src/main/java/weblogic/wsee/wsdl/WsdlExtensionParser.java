package weblogic.wsee.wsdl;

import org.w3c.dom.Element;

public interface WsdlExtensionParser {
   WsdlExtension parseMessage(WsdlMessage var1, Element var2) throws WsdlException;

   WsdlExtension parseOperation(WsdlOperation var1, Element var2) throws WsdlException;

   WsdlExtension parseBinding(WsdlBinding var1, Element var2) throws WsdlException;

   WsdlExtension parseBindingOperation(WsdlBindingOperation var1, Element var2) throws WsdlException;

   WsdlExtension parseBindingMessage(WsdlBindingMessage var1, Element var2) throws WsdlException;

   WsdlExtension parseService(WsdlService var1, Element var2) throws WsdlException;

   WsdlExtension parsePort(WsdlPort var1, Element var2) throws WsdlException;

   WsdlExtension parseDefinitions(WsdlDefinitions var1, Element var2) throws WsdlException;

   void cleanUp();
}
