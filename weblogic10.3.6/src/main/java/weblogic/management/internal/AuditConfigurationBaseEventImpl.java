package weblogic.management.internal;

import javax.security.auth.Subject;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.security.spi.AuditConfigurationEvent;
import weblogic.security.spi.AuditSeverity;

public class AuditConfigurationBaseEventImpl implements AuditConfigurationEvent {
   private static final String EVENT_TYPE_ATTR = "Event Type";
   private static final String SUBJECT_ATTR = "Subject";
   private static final String OBJECT_ATTR = "Object";
   private AuditSeverity severity;
   private String eventType;
   private AuthenticatedSubject subject;
   private String objectName;
   private Exception exception;
   private ContextHandler context;

   public AuditConfigurationBaseEventImpl(AuditSeverity var1, String var2, AuthenticatedSubject var3, String var4) {
      this.severity = var1;
      this.eventType = var2;
      this.subject = var3;
      this.objectName = var4;
   }

   public void setFailureException(Exception var1) {
      this.exception = var1;
   }

   public Exception getFailureException() {
      return this.exception;
   }

   public AuditSeverity getSeverity() {
      return this.severity;
   }

   public String getEventType() {
      return this.eventType;
   }

   public String getObjectName() {
      return this.objectName;
   }

   public Subject getSubject() {
      return this.subject.getSubject();
   }

   public void setContext(ContextHandler var1) {
      this.context = var1;
   }

   public ContextHandler getContext() {
      return this.context;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<");
      this.writeAttributes(var1);
      var1.append(">");
      return var1.toString();
   }

   protected void writeAttributes(StringBuffer var1) {
      var1.append("<");
      var1.append("Event Type");
      var1.append(" = ");
      var1.append(this.eventType);
      var1.append("><");
      var1.append("Subject");
      var1.append(" = ");
      var1.append(SubjectUtils.displaySubject(this.subject));
      var1.append("><");
      var1.append("Object");
      var1.append(" = ");
      var1.append(this.objectName);
      var1.append(">");
   }
}
