package weblogic.ejb.container.compliance;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import weblogic.utils.StackTraceUtils;

public final class Log {
   private static final boolean debug = false;
   private boolean verboseLog = false;
   private boolean hasErrors = false;
   private BufferedWriter logWriter;
   private BufferedWriter stderrWriter;
   private EJBComplianceTextFormatter fmt;

   public Log() {
      this.stderrWriter = new BufferedWriter(new OutputStreamWriter(System.err));
      this.fmt = new EJBComplianceTextFormatter();
   }

   public boolean getVerbose() {
      return this.verboseLog;
   }

   public void setVerbose(boolean var1) {
      this.verboseLog = var1;
   }

   public void logError(String var1) {
      this.log(this.fmt.error());
      this.logln(" " + var1);
      this.hasErrors = true;
   }

   public void logWarning(String var1) {
      this.log(this.fmt.warning());
      this.logln(" " + var1);
   }

   public void logInfo(String var1) {
      this.logln(var1);
   }

   public boolean hasErrors() {
      return this.hasErrors;
   }

   private void logln(String var1) {
      this.log(var1);
      this.log("\n");
   }

   private void log(String var1) {
      try {
         this.stderrWriter.write(var1);
         this.stderrWriter.flush();
      } catch (IOException var3) {
         System.err.println(this.fmt.logWriteError(var1, StackTraceUtils.throwable2StackTrace(var3)));
      }

   }
}
