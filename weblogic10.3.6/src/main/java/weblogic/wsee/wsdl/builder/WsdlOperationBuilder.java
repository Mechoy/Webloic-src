package weblogic.wsee.wsdl.builder;

import java.util.Map;
import org.w3c.dom.Element;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlWriter;

public interface WsdlOperationBuilder extends WsdlOperation {
   String getParameterOrder();

   void setParameterOrder(String var1);

   void setType(int var1);

   WsdlMessageBuilder getInput();

   WsdlMessageBuilder getOutput();

   Map<String, WsdlMessageBuilder> getFaults();

   WsdlMethodBuilder getWsdlMethod();

   WsdlMethodBuilder getWsdlMethod(boolean var1);

   boolean isWrappedNormal();

   boolean isWrappedWLW81Callback();

   boolean isWLW81CallbackOperation();

   void flipCallbackInputAndOutputParts();

   void parse(Element var1, String var2) throws WsdlException;

   void write(Element var1, WsdlWriter var2);

   void setInput(WsdlMessageBuilder var1);

   void setOutput(WsdlMessageBuilder var1);

   void addFault(WsdlMessageBuilder var1, String var2);
}
