package weblogic.logging;

import com.bea.logging.BaseLogRecord;
import com.bea.logging.LogLevel;
import com.bea.logging.ThrowableWrapper;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import weblogic.i18n.logging.LogMessage;

public class WLLogRecord extends BaseLogRecord implements LogEntry, Externalizable {
   private static final long serialVersionUID = -8930788966766077378L;
   private transient ThrowableInfo thInfo;

   public WLLogRecord() {
      this(WLLevel.INFO, "");
   }

   public WLLogRecord(Level var1, String var2) {
      super(LogLevel.getSeverity(var1), var2);
      this.thInfo = null;
      LogEntryInitializer.initializeLogEntry(this);
   }

   public WLLogRecord(Level var1, String var2, Throwable var3) {
      this(var1, var2);
      this.setThrown(var3);
   }

   public WLLogRecord(LogMessage var1) {
      super(var1);
      this.thInfo = null;
      LogEntryInitializer.initializeLogEntry(this);
   }

   /** @deprecated */
   public ThrowableInfo getThrowableInfo() {
      ThrowableWrapper var1 = this.getThrowableWrapper();
      if (var1 != null && var1.getThrowable() != null) {
         this.thInfo = new ThrowableInfo(var1.getThrowable());
      }

      return this.thInfo;
   }

   /** @deprecated */
   public void setThrowableInfo(ThrowableInfo var1) {
      this.thInfo = var1;
      if (var1 != null && var1.getThrowable() != null) {
         super.setThrown(var1.getThrowable());
      }

   }

   /** @deprecated */
   public static WLLogRecord normalizeLogRecord(LogRecord var0) {
      return WLLogger.normalizeLogRecord(var0);
   }

   public String toString() {
      return this.getMessage();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeUTF(this.getSubsystem());
      var1.writeUTF(this.getId());
      String var2 = this.getLogMessage();
      byte[] var3 = var2 != null ? var2.getBytes("UTF-8") : new byte[0];
      var1.writeInt(var3.length);
      var1.write(var3);
      var1.writeInt(this.getSeverity());
      var1.writeLong(this.getTimestamp());
      var1.writeObject(this.getThrowableWrapper());
      var1.writeUTF(this.getMachineName());
      var1.writeUTF(this.getServerName());
      var1.writeUTF(this.getThreadName());
      var1.writeUTF(this.getTransactionId());
      var1.writeUTF(this.getDiagnosticContextId());
      var1.writeUTF(this.getUserId());
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.setLoggerName(var1.readUTF());
      this.setId(var1.readUTF());
      int var2 = var1.readInt();
      byte[] var3 = new byte[var2];
      var1.readFully(var3);
      String var4 = new String(var3, "UTF-8");
      this.setMessage(var4);
      this.setLevel(WLLevel.getLevel(var1.readInt()));
      this.setMillis(var1.readLong());
      this.setThrowableWrapper((ThrowableWrapper)var1.readObject());
      this.setMachineName(var1.readUTF());
      this.setServerName(var1.readUTF());
      this.setThreadName(var1.readUTF());
      this.setTransactionId(var1.readUTF());
      this.setDiagnosticContextId(var1.readUTF());
      this.setUserId(var1.readUTF());
   }
}
