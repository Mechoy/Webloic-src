package weblogic.management.mbeans.custom;

import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public final class UnixRealm extends ConfigurationMBeanCustomizer {
   private static final long serialVersionUID = 6775637305805210195L;

   public UnixRealm(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public String getRealmClassName() {
      return "weblogic.security.unixrealm.UnixRealm";
   }
}
