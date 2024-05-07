package weblogic.management.internal;

import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.spi.AuditSetAttributeConfigurationEvent;
import weblogic.security.spi.AuditSeverity;

public class AuditSetAttributeConfigurationEventImpl extends AuditConfigurationBaseEventImpl implements AuditSetAttributeConfigurationEvent {
   private static final String ATTRIBUTE_ATTR = "Attribute";
   private static final String FROM_ATTR = "From";
   private static final String TO_ATTR = "To";
   private String attrName;
   private Object oldValue;
   private Object newValue;

   public AuditSetAttributeConfigurationEventImpl(AuditSeverity var1, AuthenticatedSubject var2, String var3, String var4, Object var5, Object var6) {
      super(var1, "SetAttribute Configuration Audit Event", var2, var3);
      this.attrName = var4;
      this.oldValue = var5;
      this.newValue = var6;
   }

   public String getAttributeName() {
      return this.attrName;
   }

   public Object getOldValue() {
      return this.oldValue;
   }

   public Object getNewValue() {
      return this.newValue;
   }

   public void writeAttributes(StringBuffer var1) {
      super.writeAttributes(var1);
      var1.append("<");
      var1.append("Attribute");
      var1.append(" = ");
      var1.append(this.attrName);
      var1.append("><");
      var1.append("From");
      var1.append(" = ");
      var1.append(this.oldValue);
      var1.append("><");
      var1.append("To");
      var1.append(" = ");
      var1.append(this.newValue);
      var1.append(">");
   }
}
