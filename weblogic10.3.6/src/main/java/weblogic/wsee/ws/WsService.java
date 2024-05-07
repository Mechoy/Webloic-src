package weblogic.wsee.ws;

import java.util.Iterator;
import javax.xml.namespace.QName;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.security.configuration.WssConfiguration;
import weblogic.wsee.security.configuration.WssConfigurationException;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlService;

public interface WsService {
   WsdlService getWsdlService();

   WsEndpoint getEndpoint(QName var1);

   Iterator getEndpoints();

   WsPort getPort(String var1);

   Iterator<WsPort> getPorts();

   boolean isUsingPolicy();

   void setUsingPolicy(boolean var1);

   WssPolicyContext getWssPolicyContext();

   void setWssPolicyContext(WssPolicyContext var1);

   void addEndpoint(QName var1, WsEndpoint var2);

   WsPort addPort(String var1, WsdlPort var2, WsEndpoint var3);

   void initWssConfiguration() throws WssConfigurationException;

   WssConfiguration getWssConfiguration();

   PolicyServer getPolicyServer();
}
