package weblogic.wsee.wsdl;

import org.w3c.dom.Element;

public interface WsdlExtension {
   String getKey();

   void write(Element var1, WsdlWriter var2);
}
