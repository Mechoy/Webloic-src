package weblogic.wsee.wsdl;

import javax.xml.namespace.QName;

public interface WsdlPart extends WsdlElement {
   String getName();

   QName getType();

   QName getElement();
}
