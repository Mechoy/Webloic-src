package weblogic.wsee.wsdl.builder;

import java.util.Map;
import org.w3c.dom.Element;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlWriter;

public interface WsdlMessageBuilder extends WsdlMessage {
   Map<String, WsdlPartBuilder> getParts();

   WsdlPartBuilder addPart(String var1);

   WsdlExtension parseChild(Element var1, String var2) throws WsdlException;

   void write(Element var1, WsdlWriter var2);
}
