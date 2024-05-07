package weblogic.management.mbeans.custom;

import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public final class LDAPRealm extends ConfigurationMBeanCustomizer {
   private static final long serialVersionUID = 1104465984402207751L;

   public LDAPRealm(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public String getRealmClassName() {
      return "weblogic.security.ldaprealmv1.LDAPRealm";
   }
}
