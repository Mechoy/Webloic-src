package weblogic.wsee.wsdl.builder;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlPart;
import weblogic.wsee.wsdl.WsdlWriter;

public interface WsdlPartBuilder extends WsdlPart {
   void setType(QName var1);

   void setElement(QName var1);

   void parse(Element var1, String var2) throws WsdlException;

   void write(Element var1, WsdlWriter var2);
}
