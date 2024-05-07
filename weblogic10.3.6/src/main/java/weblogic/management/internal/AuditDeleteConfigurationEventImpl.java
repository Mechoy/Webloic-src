package weblogic.management.internal;

import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.spi.AuditDeleteConfigurationEvent;
import weblogic.security.spi.AuditSeverity;

public class AuditDeleteConfigurationEventImpl extends AuditConfigurationBaseEventImpl implements AuditDeleteConfigurationEvent {
   public AuditDeleteConfigurationEventImpl(AuditSeverity var1, AuthenticatedSubject var2, String var3) {
      super(var1, "Delete Configuration Audit Event", var2, var3);
   }
}
