package weblogic.wsee.wsdl;

import weblogic.wsee.policy.deployment.PolicyURIs;

public interface WsdlBindingMessage extends WsdlExtensible {
   int INPUT = 0;
   int OUTPUT = 1;
   int FAULT = 2;
   int UNKNOWN = -1;

   String getName();

   WsdlBindingOperation getBindingOperation();

   PolicyURIs getPolicyUris();

   void setPolicyUris(PolicyURIs var1);

   WsdlMessage getMessage() throws WsdlException;

   int getType();
}
