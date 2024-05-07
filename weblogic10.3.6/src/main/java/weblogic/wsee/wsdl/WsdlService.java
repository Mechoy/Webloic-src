package weblogic.wsee.wsdl;

import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.policy.deployment.PolicyURIs;

public interface WsdlService extends WsdlExtensible {
   QName getName();

   WsdlDefinitions getDefinitions();

   List<? extends WsdlPortType> getPortTypes();

   Map<QName, ? extends WsdlPort> getPorts();

   PolicyURIs getPolicyUris();

   void setPolicyUris(PolicyURIs var1);

   WsdlFilter getWsdlFilter();
}
