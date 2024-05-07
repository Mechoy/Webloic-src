package weblogic.wsee.wsdl.builder;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlWriter;

public interface WsdlPortBuilder extends WsdlPort {
   WsdlServiceBuilder getService();

   WsdlBindingBuilder getBinding();

   WsdlPortTypeBuilder getPortType();

   void setTransport(String var1);

   WsdlDefinitionsBuilder getDefinitions();

   void setBinding(QName var1);

   void write(Element var1, WsdlWriter var2);
}
