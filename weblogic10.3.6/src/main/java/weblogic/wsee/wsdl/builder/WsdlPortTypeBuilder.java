package weblogic.wsee.wsdl.builder;

import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlWriter;

public interface WsdlPortTypeBuilder extends WsdlPortType {
   Map<QName, WsdlOperationBuilder> getOperations();

   WsdlOperationBuilder addOperation(QName var1);

   void parse(Element var1, String var2) throws WsdlException;

   void write(Element var1, WsdlWriter var2);
}
