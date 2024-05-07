package weblogic.management.mbeans.custom;

import weblogic.management.configuration.FederationServicesMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;
import weblogic.security.providers.saml.SAMLUtil;

public final class FederationServices extends ConfigurationMBeanCustomizer {
   public FederationServices(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public String getSourceIdHex() {
      return SAMLUtil.calculateSourceIdHex(((FederationServicesMBean)this.getMbean()).getSourceSiteURL());
   }

   public String getSourceIdBase64() {
      return SAMLUtil.calculateSourceIdBase64(((FederationServicesMBean)this.getMbean()).getSourceSiteURL());
   }
}
