package weblogic.management.mbeans.custom;

import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public final class NTRealm extends ConfigurationMBeanCustomizer {
   private static final long serialVersionUID = -7354563683049235307L;

   public NTRealm(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public String getRealmClassName() {
      return "weblogic.security.ntrealm.NTRealm";
   }
}
