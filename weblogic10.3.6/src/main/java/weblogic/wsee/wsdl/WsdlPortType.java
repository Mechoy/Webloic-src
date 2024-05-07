package weblogic.wsee.wsdl;

import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.policy.deployment.PolicyURIs;

public interface WsdlPortType extends WsdlElement {
   QName getName();

   Map<QName, ? extends WsdlOperation> getOperations();

   PolicyURIs getPolicyUris();

   void setPolicyUris(PolicyURIs var1);
}
