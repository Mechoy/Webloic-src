package weblogic.xml.crypto.wss.provider;

import org.w3c.dom.Element;
import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.wss.WSSecurityConfigurationException;

/** @deprecated */
public interface SecurityTokenPolicyInfo {
   boolean supports(Purpose var1);

   Element getSecurityTokenAssertion(Element var1, Purpose var2, ContextHandler var3) throws WSSecurityConfigurationException;
}
