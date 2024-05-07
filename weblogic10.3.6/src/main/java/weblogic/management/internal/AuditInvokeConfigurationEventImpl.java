package weblogic.management.internal;

import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.spi.AuditInvokeConfigurationEvent;
import weblogic.security.spi.AuditSeverity;

public class AuditInvokeConfigurationEventImpl extends AuditConfigurationBaseEventImpl implements AuditInvokeConfigurationEvent {
   private static final String OPERATION_ATTR = "Operation";
   private static final String PARAMETERS_ATTR = "Parameters";
   private String methodName;
   private String params;

   public AuditInvokeConfigurationEventImpl(AuditSeverity var1, AuthenticatedSubject var2, String var3, String var4, String var5) {
      super(var1, "Invoke Configuration Audit Event", var2, var3);
      this.methodName = var4;
      this.params = var5;
   }

   public String getMethodName() {
      return this.methodName;
   }

   public String getParameters() {
      return this.params;
   }

   public void writeAttributes(StringBuffer var1) {
      super.writeAttributes(var1);
      var1.append("<");
      var1.append("Operation");
      var1.append(" = ");
      var1.append(this.methodName);
      var1.append("><");
      var1.append("Parameters");
      var1.append(" = ");
      var1.append(this.params);
      var1.append(">");
   }
}
