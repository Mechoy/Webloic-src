package weblogic.diagnostics.instrumentation;

import java.security.AccessController;
import java.security.Principal;
import java.util.Iterator;
import javax.security.auth.Subject;
import weblogic.diagnostics.accessor.DataRecord;
import weblogic.diagnostics.archive.ArchiveConstants;
import weblogic.diagnostics.context.DiagnosticContext;
import weblogic.diagnostics.context.DiagnosticContextFactory;
import weblogic.kernel.Kernel;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.Security;
import weblogic.security.SubjectUtils;
import weblogic.security.WLSPrincipals;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.spi.WLSUser;
import weblogic.transaction.TxHelper;

public final class InstrumentationEvent {
   private String retValString;
   private String argsString;
   private DiagnosticMonitorControl monitor;
   private DiagnosticAction action;
   private String eventType;
   private JoinPoint jp;
   private long timestamp;
   private String contextId;
   private String threadName;
   private String contextPayload;
   private String txId;
   private String userId;
   private Object payload;
   private long dyeVector;
   private static String serverName;
   private static String domainName;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public InstrumentationEvent(DiagnosticAction var1, JoinPoint var2, boolean var3) {
      this();
      this.action = var1;
      this.jp = var2;
      if (var3) {
         this.captureArgReturnValues();
      }

   }

   public InstrumentationEvent(DiagnosticMonitorControl var1, JoinPoint var2) {
      this();
      this.monitor = var1;
      this.jp = var2;
   }

   public InstrumentationEvent(String var1) {
      this();
      this.eventType = var1;
   }

   public InstrumentationEvent(String var1, Object var2) {
      this();
      this.eventType = var1;
      this.payload = var2;
   }

   private InstrumentationEvent() {
      this.retValString = "";
      this.argsString = "";
      this.contextId = "";
      this.threadName = "";
      this.txId = "";
      this.userId = "";
      this.doInit();
   }

   private void doInit() {
      this.timestamp = System.currentTimeMillis();
      this.threadName = Thread.currentThread().getName();
      DiagnosticContext var1 = DiagnosticContextFactory.findOrCreateDiagnosticContext();
      if (var1 != null) {
         this.contextId = var1.getContextId();
         this.contextPayload = var1.getPayload();
         this.dyeVector = var1.getDyeVector();
      }

      if (Kernel.isInitialized()) {
         this.txId = TxHelper.getTransactionId();
         Subject var2 = Security.getCurrentSubject();
         this.userId = this.extractCurrentWLSSubject(var2);
         if (this.userId == null) {
            this.userId = SubjectUtils.getUsername(var2);
         }

         if (this.txId == null) {
            this.txId = "";
         }

         if (this.userId == null) {
            this.userId = "";
         }

         if (serverName == null) {
            RuntimeAccess var3 = ManagementService.getRuntimeAccess(kernelId);
            if (var3 != null) {
               serverName = var3.getServerName();
               domainName = var3.getDomainName();
            }
         }
      }

   }

   private String extractCurrentWLSSubject(Subject var1) {
      String var2 = null;
      int var3 = var1.getPrincipals().size();
      if (var3 > 0) {
         Iterator var4 = var1.getPrincipals().iterator();

         while(var4.hasNext()) {
            Principal var5 = (Principal)var4.next();
            if (var5 instanceof WLSUser) {
               var2 = var5.getName();
               break;
            }
         }
      } else {
         var2 = WLSPrincipals.getAnonymousUsername();
      }

      return var2;
   }

   public String getEventType() {
      return this.eventType;
   }

   public void setEventType(String var1) {
      this.eventType = var1;
   }

   public void setContextId(String var1) {
      this.contextId = var1;
   }

   public String getContextId() {
      return this.contextId;
   }

   public void setPayload(Object var1) {
      this.payload = var1;
   }

   public Object getPayload() {
      return this.payload;
   }

   public DataRecord getDataRecord() {
      String var1 = "";
      String var2 = "";
      String var3 = "";
      if (this.action != null) {
         DiagnosticMonitor var4 = this.action.getDiagnosticMonitor();
         if (var4 instanceof DiagnosticMonitorControl) {
            this.monitor = (DiagnosticMonitorControl)var4;
         }

         var1 = this.action.getType();
      }

      if (this.monitor != null) {
         var2 = this.monitor.getType();
         InstrumentationScope var15 = this.monitor.getInstrumentationScope();
         if (var15 != null) {
            var3 = var15.getName();
         }
      }

      int var16 = 0;
      String var5 = "";
      String var6 = "";
      String var7 = "";
      String var8 = "";
      String var9 = "";
      if (this.jp != null) {
         var5 = this.jp.getModuleName();
         var16 = this.jp.getLineNumber();
         var6 = this.jp.getSourceFile();
         var7 = this.jp.getClassName();
         var8 = this.jp.getMethodName();
         var9 = this.jp.getMethodDescriptor();
      }

      String var10 = serverName != null ? serverName : "";
      String var11 = domainName != null ? domainName : "";
      String var12 = this.eventType != null ? this.eventType : var1;
      this.contextPayload = this.contextPayload != null ? this.contextPayload : "";
      Object[] var13 = new Object[ArchiveConstants.EVENTS_ARCHIVE_COLUMNS_COUNT];
      int var14 = 0;
      var13[var14++] = null;
      var13[var14++] = new Long(this.timestamp);
      var13[var14++] = this.contextId;
      var13[var14++] = this.txId;
      var13[var14++] = this.userId;
      var13[var14++] = var12;
      var13[var14++] = var11;
      var13[var14++] = var10;
      var13[var14++] = var3;
      var13[var14++] = var5;
      var13[var14++] = var2;
      var13[var14++] = var6;
      var13[var14++] = new Integer(var16);
      var13[var14++] = var7;
      var13[var14++] = var8;
      var13[var14++] = var9;
      var13[var14++] = this.argsString;
      var13[var14++] = this.retValString;
      var13[var14++] = this.payload;
      var13[var14++] = this.contextPayload;
      var13[var14++] = new Long(this.dyeVector);
      var13[var14++] = this.threadName;
      return new DataRecord(var13);
   }

   private void captureArgReturnValues() {
      if (this.jp instanceof DynamicJoinPoint) {
         DynamicJoinPoint var1 = (DynamicJoinPoint)this.jp;
         Object var2 = var1.getReturnValue();
         if (var2 != null) {
            this.retValString = this.getArgValue(var2);
         }

         Object[] var3 = var1.getArguments();
         if (var3 != null) {
            int var4 = var3 != null ? var3.length : 0;
            StringBuffer var5 = new StringBuffer();

            for(int var6 = 0; var6 < var4; ++var6) {
               if (var6 > 0) {
                  var5.append(", ");
               }

               var5.append(this.getArgValue(var3[var6]));
            }

            this.argsString = var5.toString();
         }
      }

   }

   private String getArgValue(Object var1) {
      if (var1 == null) {
         return "null";
      } else {
         String var2 = null;

         try {
            var2 = var1.toString();
         } catch (Throwable var4) {
            var2 = "??" + var1.getClass().getName() + "??";
         }

         return var2;
      }
   }
}
