package weblogic.wsee.wsdl;

import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.policy.deployment.PolicyURIs;

public interface WsdlBindingOperation extends WsdlExtensible {
   WsdlBinding getBinding();

   QName getName();

   WsdlBindingMessage getInput();

   WsdlBindingMessage getOutput();

   Map<String, ? extends WsdlBindingMessage> getFaults();

   PolicyURIs getPolicyUris();

   void setPolicyUris(PolicyURIs var1);
}
