package weblogic.wsee.wsdl;

import javax.xml.namespace.QName;
import weblogic.wsee.deploy.WsdlAddressInfo;
import weblogic.wsee.policy.deployment.PolicyURIs;

public interface WsdlPort extends WsdlExtensible {
   QName getName();

   WsdlService getService();

   WsdlBinding getBinding();

   WsdlPortType getPortType();

   String getTransport();

   PolicyURIs getPolicyUris();

   void setPolicyUris(PolicyURIs var1);

   WsdlDefinitions getDefinitions();

   WsdlAddressInfo.PortAddress getPortAddress();

   void setPortAddress(WsdlAddressInfo.PortAddress var1);
}
