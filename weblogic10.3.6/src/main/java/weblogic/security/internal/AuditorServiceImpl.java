package weblogic.security.internal;

import weblogic.security.service.Auditor;
import weblogic.security.spi.AuditEvent;
import weblogic.security.spi.AuditorService;

public class AuditorServiceImpl implements AuditorService {
   private Auditor auditor;

   public AuditorServiceImpl(Auditor var1) {
      this.auditor = var1;
   }

   public void providerAuditWriteEvent(AuditEvent var1) {
      this.auditor.writeEvent(var1);
   }
}
