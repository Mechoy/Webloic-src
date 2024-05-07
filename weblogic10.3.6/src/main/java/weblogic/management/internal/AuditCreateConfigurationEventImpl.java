package weblogic.management.internal;

import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.spi.AuditCreateConfigurationEvent;
import weblogic.security.spi.AuditSeverity;

public class AuditCreateConfigurationEventImpl extends AuditConfigurationBaseEventImpl implements AuditCreateConfigurationEvent {
   public AuditCreateConfigurationEventImpl(AuditSeverity var1, AuthenticatedSubject var2, String var3) {
      super(var1, "Create Configuration Audit Event", var2, var3);
   }
}
