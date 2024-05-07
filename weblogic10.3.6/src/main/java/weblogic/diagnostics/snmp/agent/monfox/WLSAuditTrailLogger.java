package weblogic.diagnostics.snmp.agent.monfox;

import java.security.AccessController;
import monfox.toolkit.snmp.Snmp;
import monfox.toolkit.snmp.SnmpOid;
import monfox.toolkit.snmp.SnmpVarBindList;
import monfox.toolkit.snmp.agent.ext.audit.SnmpAuditTrailLogger;
import monfox.toolkit.snmp.engine.SnmpPDU;
import monfox.toolkit.snmp.v3.usm.USMUser;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.Auditor;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.security.spi.AuditEvent;
import weblogic.security.spi.AuditSeverity;

public final class WLSAuditTrailLogger implements SnmpAuditTrailLogger {
   private static AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final DebugLogger DEBUG_LOGGER = DebugLogger.getDebugLogger("DebugSNMPAgent");
   private static final String SNMP_ERROR_REPORT = "SNMP_ERROR_REPORT";
   private static final String SNMP_ERROR_RESPONSE = "SNMP_ERROR_RESPONSE";
   private static final String SNMP_OBJECT_ACCESS = "SNMP_OBJECT_ACCESS";
   private static final String SNMP_AUTH_OPERATION = "SNMP_AUTH_OPERATION";
   private static final String SNMP_PRIV_OPERATION = "SNMP_PRIV_OPERATION";
   private static final String YES = "Yes";
   private static final String NO = "No";
   private static final String EVENT_TYPE_ATTR = "EVENT_TYPE";
   private static final String SUBJECT_ATTR = "Subject";
   private static final String SEVERITY_ATTR = "Severity";
   private static final String VARBIND_ATTR = "Variables";
   private static final String VERSION_ATTR = "SnmpV";
   private static final String CRYPT_ATTR = "Crypt-Op";
   private static final String ENCRYPT_OP = "Encrypt";
   private static final String DECRYPT_OP = "Decrypt";
   private static final String PROTOCOL_ATTR = "Protocol";
   private static final String INCOMING_ATTR = "Incoming";
   private static final String MSGTYPE_ATTR = "MsgType";
   private static final String OBJNAME_ATTR = "Object";
   private static final String INSTANCE_ATTR = "Instance";
   private static final String OBJVAL_ATTR = "Value";
   private static final String ERRORSTATUS_ATTR = "Error-Status";
   private static final String ERRORINDEX_ATTR = "Error-Index";
   private int authFailureCount;
   private int privFailureCount;
   private Auditor auditor = null;
   private static WLSAuditTrailLogger singleton;

   private WLSAuditTrailLogger() {
      String var1 = "weblogicDEFAULT";
      this.auditor = (Auditor)SecurityServiceManager.getSecurityService(KERNEL_ID, var1, ServiceType.AUDIT);
   }

   public static synchronized WLSAuditTrailLogger getInstance() {
      if (singleton == null) {
         singleton = new WLSAuditTrailLogger();
      }

      return singleton;
   }

   private void logEvent(SnmpBaseAuditEvent var1) {
      if (this.auditor != null) {
         this.auditor.writeEvent(var1);
      }

   }

   private String algToString(int var1) {
      switch (var1) {
         case 0:
            return "MD5";
         case 1:
            return "SHA";
         case 2:
            return "DES";
         case 3:
         default:
            return "Unknown:" + var1;
         case 4:
            return "AES128";
         case 5:
            return "AES192";
         case 6:
            return "AES256";
      }
   }

   public void logPrivOperation(String var1, int var2, int var3, int var4, int var5) {
      if (var5 != 1) {
         ++this.privFailureCount;
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("privFailureCount: " + this.privFailureCount + ", [" + var1 + ", " + Snmp.versionToString(var2) + ", " + var3 + ", " + USMUser.PrivProtocolToString(var4) + "]");
         }
      }

      this.logEvent(new SnmpPrivOperationEvent(var1, var2, var3, var4, var5));
   }

   public void logAuthOperation(String var1, int var2, int var3, int var4, int var5) {
      if (var5 != 1) {
         ++this.authFailureCount;
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("authFailureCount: " + this.authFailureCount + ", [" + var1 + ", " + Snmp.versionToString(var2) + ", " + var3 + ", " + USMUser.AuthProtocolToString(var4) + "]");
         }
      }

      this.logEvent(new SnmpAuthOperationEvent(var1, var2, var3, var4, var5));
   }

   public void logObjectAccess(String var1, int var2, int var3, String var4, SnmpOid var5, String var6) {
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("log object access, version: " + Snmp.versionToString(var2) + ", msg_type: " + SnmpPDU.typeToString(var3) + ", [" + var4 + ", " + var6 + "]");
      }

      this.logEvent(new SnmpObjectAccessEvent(var1, var2, var3, var4, var5, var6));
   }

   public void logErrorResponse(String var1, int var2, int var3, SnmpVarBindList var4, int var5, int var6) {
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("log error response, name: " + var1 + ", version: " + Snmp.versionToString(var2) + ", msg_type: " + SnmpPDU.typeToString(var3) + ", error status: " + Snmp.errorStatusToString(var5) + ", error index: " + var6);
      }

      this.logEvent(new SnmpErrorResponseEvent(var1, var2, var3, var4, var5, var6));
   }

   public void logErrorReport(String var1, SnmpVarBindList var2) {
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("log error report, name: " + var1 + ", var bindings: " + var2.toString());
      }

      this.logEvent(new SnmpErrorReportAuditEvent(var1, var2));
   }

   public int getFailedAuthenticationCount() {
      return this.authFailureCount;
   }

   public int getFailedEncryptionCount() {
      return this.privFailureCount;
   }

   class SnmpErrorReportAuditEvent extends SnmpBaseAuditEvent {
      private SnmpVarBindList report_varbinds;

      SnmpErrorReportAuditEvent(String var2, SnmpVarBindList var3) {
         super("SNMP_ERROR_REPORT", AuditSeverity.ERROR, var2);
         this.report_varbinds = var3;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         super.writeAttributes(var1);
         var1.append("<");
         var1.append("Variables");
         var1.append(" = ");
         var1.append(this.report_varbinds != null ? this.report_varbinds.toString() : "null");
         var1.append(">");
         return var1.toString();
      }
   }

   class SnmpErrorResponseEvent extends SnmpAuditEvent {
      private int msg_type;
      private SnmpVarBindList request_varbinds;
      private int error_status;
      private int error_index;

      SnmpErrorResponseEvent(String var2, int var3, int var4, SnmpVarBindList var5, int var6, int var7) {
         super("SNMP_ERROR_RESPONSE", AuditSeverity.ERROR, var2, var3);
         this.msg_type = var4;
         this.request_varbinds = var5;
         this.error_status = var6;
         this.error_index = var7;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         super.writeAttributes(var1);
         var1.append("<");
         var1.append("MsgType");
         var1.append(" = ");
         var1.append(this.msg_type);
         var1.append(">");
         var1.append("<");
         var1.append("Variables");
         var1.append(" = ");
         var1.append(this.request_varbinds.toString());
         var1.append(">");
         var1.append("<");
         var1.append("Error-Status");
         var1.append(" = ");
         var1.append(Snmp.errorStatusToString(this.error_status));
         var1.append(">");
         var1.append("<");
         var1.append("Error-Index");
         var1.append(" = ");
         var1.append(this.error_index);
         var1.append(">");
         return var1.toString();
      }
   }

   class SnmpObjectAccessEvent extends SnmpAuditEvent {
      private int msg_type;
      private String object_name;
      private SnmpOid instance_or_index;
      private String object_value;

      SnmpObjectAccessEvent(String var2, int var3, int var4, String var5, SnmpOid var6, String var7) {
         super("SNMP_OBJECT_ACCESS", AuditSeverity.INFORMATION, var2, var3);
         this.msg_type = var4;
         this.object_name = var5;
         this.instance_or_index = var6;
         this.object_value = var7;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         super.writeAttributes(var1);
         var1.append("<");
         var1.append("MsgType");
         var1.append(" = ");
         var1.append(this.msg_type);
         var1.append(">");
         var1.append("<");
         var1.append("Object");
         var1.append(" = ");
         var1.append(this.object_name);
         var1.append(">");
         var1.append("<");
         var1.append("Instance");
         var1.append(" = ");
         var1.append(this.instance_or_index);
         var1.append(">");
         var1.append("<");
         var1.append("Value");
         var1.append(" = ");
         var1.append(this.object_value);
         var1.append(">");
         return var1.toString();
      }
   }

   class SnmpAuthOperationEvent extends SnmpAuditEvent {
      private int auth_type;
      private int auth_protocol;
      private int result;

      SnmpAuthOperationEvent(String var2, int var3, int var4, int var5, int var6) {
         super("SNMP_AUTH_OPERATION", var6 == 1 ? AuditSeverity.SUCCESS : AuditSeverity.FAILURE, var2, var3);
         this.auth_type = var4;
         this.auth_protocol = var5;
         this.result = var6;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         super.writeAttributes(var1);
         var1.append("<");
         var1.append("Incoming");
         var1.append(" = ");
         var1.append(this.auth_type == 1 ? "Yes" : "No");
         var1.append(">");
         var1.append("<");
         var1.append("Protocol");
         var1.append(" = ");
         var1.append(WLSAuditTrailLogger.this.algToString(this.auth_protocol));
         var1.append(">");
         return var1.toString();
      }
   }

   class SnmpPrivOperationEvent extends SnmpAuditEvent {
      private int crypt_type;
      private int priv_protocol;
      private int result;

      SnmpPrivOperationEvent(String var2, int var3, int var4, int var5, int var6) {
         super("SNMP_PRIV_OPERATION", var6 == 1 ? AuditSeverity.SUCCESS : AuditSeverity.FAILURE, var2, var3);
         this.crypt_type = var4;
         this.priv_protocol = var5;
         this.result = var6;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         super.writeAttributes(var1);
         var1.append("<");
         var1.append("Crypt-Op");
         var1.append(" = ");
         var1.append(this.crypt_type == 2 ? "Encrypt" : "Decrypt");
         var1.append(">");
         var1.append("<");
         var1.append("Protocol");
         var1.append(" = ");
         var1.append(WLSAuditTrailLogger.this.algToString(this.priv_protocol));
         var1.append(">");
         return var1.toString();
      }
   }

   class SnmpAuditEvent extends SnmpBaseAuditEvent {
      private int snmp_version;

      public SnmpAuditEvent(String var2, AuditSeverity var3, String var4, int var5) {
         super(var2, var3, var4);
         this.snmp_version = var5;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         super.writeAttributes(var1);
         var1.append("<");
         var1.append("SnmpV");
         var1.append(" = ");
         var1.append(this.snmp_version);
         var1.append(">");
         return var1.toString();
      }
   }

   class SnmpBaseAuditEvent implements AuditEvent {
      private String eventType;
      private AuditSeverity severity;
      private String sec_name;

      public SnmpBaseAuditEvent(String var2, AuditSeverity var3, String var4) {
         this.eventType = var2;
         this.severity = var3;
         this.sec_name = var4;
      }

      public String getEventType() {
         return this.eventType;
      }

      public Exception getFailureException() {
         return null;
      }

      public AuditSeverity getSeverity() {
         return this.severity;
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
         var1.append("EVENT_TYPE");
         var1.append(" = ");
         var1.append(this.eventType);
         var1.append("><");
         var1.append("Subject");
         var1.append(" = ");
         var1.append(this.sec_name);
         var1.append("><");
         var1.append("Severity");
         var1.append(" = ");
         var1.append(this.severity.getSeverityString());
         var1.append(">");
      }
   }
}
