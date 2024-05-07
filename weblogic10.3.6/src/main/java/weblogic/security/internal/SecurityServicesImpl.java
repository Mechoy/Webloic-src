package weblogic.security.internal;

import weblogic.security.spi.AuditorService;
import weblogic.security.spi.SecurityServices;

public class SecurityServicesImpl implements SecurityServices {
   private AuditorService auditorService = null;

   public SecurityServicesImpl(AuditorService var1, String var2) {
      this.auditorService = var1;
      SecurityServicesManagerHelper.registerSecurityServices(this, var2);
   }

   public AuditorService getAuditorService() {
      return this.auditorService;
   }
}
