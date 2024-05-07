package weblogic.wsee.wsdl.builder;

import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlWriter;

public interface WsdlBindingBuilder extends WsdlBinding {
   void setBindingType(String var1);

   void setTransportProtocol(String var1);

   void setTransportURI(String var1);

   WsdlPortTypeBuilder getPortType();

   WsdlBindingOperationBuilder addOperation(WsdlOperationBuilder var1);

   void write(Element var1, WsdlWriter var2);

   Map<QName, WsdlBindingOperationBuilder> getOperations();
}
