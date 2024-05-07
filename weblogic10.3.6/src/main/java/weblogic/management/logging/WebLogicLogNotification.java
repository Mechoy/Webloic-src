package weblogic.management.logging;

import javax.management.Notification;
import weblogic.logging.LogEntry;
import weblogic.logging.ThrowableInfo;

public final class WebLogicLogNotification extends Notification {
   private static final long serialVersionUID = 8373884439485373606L;
   private static final boolean debug = false;
   private static final String DELIM = ".";
   private static final int SS_INDEX = 3;
   private static final int ID_INDEX = 4;
   private int severity;
   private String serverName;
   private String machineName;
   private String threadId;
   private String tranId;
   private String userId;
   private String message;
   private Throwable th;
   private int intId = -1;
   private String msgId = null;
   private String subsystem = null;
   private String idAsString = null;
   private String diagCtxId = null;

   public WebLogicLogNotification(String var1, long var2, Object var4, LogEntry var5) {
      super(var1, var4, var2, var5.getTimestamp(), var5.getLogMessage());
      this.machineName = var5.getMachineName();
      this.serverName = var5.getServerName();
      this.threadId = var5.getThreadName();
      this.userId = var5.getUserId();
      this.tranId = var5.getTransactionId();
      this.severity = var5.getSeverity();
      this.diagCtxId = var5.getDiagnosticContextId();
      ThrowableInfo var6 = var5.getThrowableInfo();
      if (var6 != null) {
         this.th = new Throwable(var6.getMessage());
         this.th.setStackTrace(var6.getStackTrace());
      }

      this.subsystem = var5.getSubsystem();
      this.msgId = var5.getId();
   }

   public final int getVersion() {
      return 1;
   }

   public String getType() {
      return super.getType();
   }

   public String getMachineName() {
      return this.machineName;
   }

   public String getServername() {
      return this.serverName;
   }

   public String getThreadId() {
      return this.threadId;
   }

   public String getUserId() {
      return this.userId;
   }

   public String getTransactionId() {
      return this.tranId;
   }

   public int getSeverity() {
      return this.severity;
   }

   public Throwable getThrowable() {
      return this.th;
   }

   public int getMessageId() {
      if (this.intId == -1) {
         String var1 = this.getMessageIdString();
         if (var1 != null) {
            try {
               this.intId = Integer.parseInt(var1);
            } catch (NumberFormatException var3) {
            }
         }
      }

      return this.intId;
   }

   public String getId() {
      return this.msgId;
   }

   public String getMessageIdString() {
      if (this.idAsString == null) {
         int var1 = this.msgId.indexOf("-");
         if (var1 != -1) {
            this.idAsString = this.msgId.substring(var1 + 1);
         } else {
            this.idAsString = this.msgId;
         }
      }

      return this.idAsString;
   }

   public String getSubsystem() {
      return this.subsystem;
   }

   public String getDiagnosticContextId() {
      return this.diagCtxId;
   }
}
