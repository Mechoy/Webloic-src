package weblogic.security.providers.saml;

import weblogic.descriptor.DescriptorBean;
import weblogic.management.commo.StandardInterface;
import weblogic.management.security.authentication.AuthenticatorMBean;

public interface SAMLAuthenticatorMBean extends StandardInterface, DescriptorBean, AuthenticatorMBean {
   String getProviderClassName();

   String getDescription();

   String getVersion();

   String getName();
}
