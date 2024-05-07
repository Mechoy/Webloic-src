package weblogic.wsee.conversation.wsdl;

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
import weblogic.wsee.wsdl.WsdlService;

public class ConversationWsdlExtensionParser implements WsdlExtensionParser {
   public WsdlExtension parseMessage(WsdlMessage var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseOperation(WsdlOperation var1, Element var2) {
      return null;
   }

   public WsdlExtension parseBinding(WsdlBinding var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseBindingOperation(WsdlBindingOperation var1, Element var2) throws WsdlException {
      return var2.getNamespaceURI().equals("http://www.openuri.org/2002/04/wsdl/conversation/") && var2.getLocalName().equals("transition") ? new ConversationWsdlPhase(var2) : null;
   }

   public WsdlExtension parseBindingMessage(WsdlBindingMessage var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseService(WsdlService var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parsePort(WsdlPort var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseDefinitions(WsdlDefinitions var1, Element var2) throws WsdlException {
      return null;
   }

   public void cleanUp() {
   }
}
