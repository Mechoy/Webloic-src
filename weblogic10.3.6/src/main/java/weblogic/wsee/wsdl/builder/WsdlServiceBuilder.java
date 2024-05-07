package weblogic.wsee.wsdl.builder;

import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.policy.deployment.PolicyURIs;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlFilter;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.WsdlWriter;

public interface WsdlServiceBuilder extends WsdlService {
   WsdlDefinitionsBuilder getDefinitions();

   void setName(QName var1);

   List<WsdlPortTypeBuilder> getPortTypes();

   Map<QName, WsdlPortBuilder> getPorts();

   void setPolicyUris(PolicyURIs var1);

   WsdlPortBuilder addPort(QName var1, WsdlBinding var2);

   void write(Element var1, WsdlWriter var2);

   void setWsdlFilter(WsdlFilter var1);
}
