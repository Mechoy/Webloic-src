package weblogic.wsee.wsdl.builder;

import org.w3c.dom.Element;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlWriter;

public interface WsdlBindingMessageBuilder extends WsdlBindingMessage {
   void setName(String var1);

   WsdlBindingOperationBuilder getBindingOperation();

   WsdlMessageBuilder getMessage() throws WsdlException;

   void write(Element var1, WsdlWriter var2);
}
