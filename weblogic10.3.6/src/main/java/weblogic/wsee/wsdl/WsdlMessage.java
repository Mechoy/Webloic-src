package weblogic.wsee.wsdl;

import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.policy.deployment.PolicyURIs;

public interface WsdlMessage extends WsdlExtensible {
   QName getName();

   Map<String, ? extends WsdlPart> getParts();

   PolicyURIs getPolicyUris();

   void setPolicyUris(PolicyURIs var1);
}
