package weblogic.wsee.wsdl;

import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.policy.deployment.PolicyURIs;

public interface WsdlBinding extends WsdlExtensible {
   String getBindingType();

   String getTransportProtocol();

   String getTransportURI();

   QName getName();

   WsdlPortType getPortType();

   PolicyURIs getPolicyUris();

   void setPolicyUris(PolicyURIs var1);

   Map<QName, ? extends WsdlBindingOperation> getOperations();
}
