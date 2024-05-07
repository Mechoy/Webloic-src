package weblogic.wsee.wsdl.builder;

import java.util.Map;
import org.w3c.dom.Element;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlWriter;

public interface WsdlBindingOperationBuilder extends WsdlBindingOperation {
   WsdlBindingBuilder getBinding();

   WsdlBindingMessageBuilder getInput();

   WsdlBindingMessageBuilder createInput();

   WsdlBindingMessageBuilder getOutput();

   WsdlBindingMessageBuilder createOutput();

   void flipCallbackInputAndOutput();

   Map<String, WsdlBindingMessageBuilder> getFaults();

   WsdlBindingMessageBuilder createFault(String var1);

   void write(Element var1, WsdlWriter var2);
}
