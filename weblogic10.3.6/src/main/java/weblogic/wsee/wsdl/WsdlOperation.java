package weblogic.wsee.wsdl;

import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.policy.deployment.PolicyURIs;

public interface WsdlOperation extends WsdlExtensible {
   int REQUEST_RESPONSE = 0;
   int ONE_WAY = 1;
   int SOLICIT_RESPONSE = 2;
   int NOTIFICATION = 3;

   QName getName();

   int getType();

   WsdlMessage getInput();

   String getInputAction();

   String getInputName();

   PolicyURIs getInputPolicyUris();

   void setInputPolicyUris(PolicyURIs var1);

   WsdlMessage getOutput();

   String getOutputAction();

   String getOutputName();

   PolicyURIs getOutputPolicyUris();

   void setOutputPolicyUris(PolicyURIs var1);

   Map<String, ? extends WsdlMessage> getFaults();

   WsdlMethod getWsdlMethod();

   WsdlMethod getWsdlMethod(boolean var1);

   PolicyURIs getFaultPolicyUris(String var1);

   void setFaultPolicyUris(String var1, PolicyURIs var2);

   PolicyURIs getPolicyUris();

   void setPolicyUris(PolicyURIs var1);

   boolean isWrapped();
}
