package weblogic.ejb.container.utils.ddconverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import weblogic.utils.StackTraceUtils;

public final class ConvertLog {
   private static final boolean debug = false;
   private static final boolean verbose = false;
   private static final String DEFAULT_LOG_NAME = "ddconverter.log";
   private boolean verboseLog;
   private boolean hasErrors;
   private String logFileName;
   private BufferedWriter logWriter;
   private BufferedWriter stderrWriter;
   private EJBddcTextFormatter fmt;

   public ConvertLog() throws IOException {
      this(new File("ddconverter.log"));
   }

   public ConvertLog(File var1) throws IOException {
      this.verboseLog = false;
      this.hasErrors = false;
      this.stderrWriter = new BufferedWriter(new OutputStreamWriter(System.err));
      this.fmt = new EJBddcTextFormatter();
      this.logFileName = var1.getPath();
      this.logWriter = new BufferedWriter(new FileWriter(var1));
   }

   public String getLogFileName() {
      return this.logFileName;
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
         this.logWriter.write(var1);
         this.logWriter.flush();
         this.stderrWriter.write(var1);
         this.stderrWriter.flush();
      } catch (IOException var3) {
         System.err.println(this.fmt.logWriteError(var1, StackTraceUtils.throwable2StackTrace(var3)));
      }

   }
}
