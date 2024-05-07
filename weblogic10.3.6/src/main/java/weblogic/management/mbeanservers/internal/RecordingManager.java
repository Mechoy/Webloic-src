package weblogic.management.mbeanservers.internal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.NoAccessRuntimeException;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.management.mbeanservers.edit.RecordingException;
import weblogic.security.UserConfigFileManager;
import weblogic.security.UsernameAndPassword;
import weblogic.utils.StringUtils;

public class RecordingManager {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugJMXEdit");
   private String recordingFileName;
   private String configFileName;
   private String secretFileName;
   private boolean recording = false;
   private String lastCD = "";
   private String lastCommand = "";
   private boolean verbose = false;

   RecordingManager() {
   }

   public static RecordingManager getInstance() {
      return RecordingManager.Maker.SINGLETON;
   }

   public synchronized void startRecording(String var1, boolean var2) throws RecordingException {
      HashMap var3 = new HashMap();
      var3.put("append", var2 ? "true" : "false");
      this.startRecording(var1, var3);
   }

   public synchronized void startRecording(String var1, Map var2) throws RecordingException {
      if (this.isRecording()) {
         throw new RecordingException(ManagementTextTextFormatter.getInstance().getRecordingAlreadyStarted());
      } else if (StringUtils.isEmptyString(var1)) {
         throw new RecordingException(ManagementTextTextFormatter.getInstance().getMissingRecordingFileName());
      } else {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Starting WLST script recording session. The generated scripts will be written to " + var1);
         }

         boolean var3 = false;
         if ("true".equals((String)var2.get("append"))) {
            var3 = true;
         }

         this.verbose = false;
         if ("true".equals((String)var2.get("verbose"))) {
            this.verbose = true;
         }

         BufferedWriter var4 = null;
         File var5 = new File(var1);

         try {
            var4 = new BufferedWriter(new FileWriter(var5, var3));
            if (this.verbose) {
               boolean var6 = true;
               if (var5.length() > 0L) {
                  var6 = false;
               }

               var4.write("\n# WLST scripts recording begin time: " + (new Date()).toString() + "\n\n");
               if (var6) {
                  var4.write("connect()\nedit()\n");
               }

               var4.flush();
            }
         } catch (IOException var17) {
            throw new RecordingException(ManagementTextTextFormatter.getInstance().getRecordingIOException(), var17);
         } finally {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (Exception var16) {
               }
            }

         }

         this.recordingFileName = var1;
         File var19 = var5.getParentFile();
         if (var19 == null) {
            var19 = new File(".");
         }

         String var7 = var19.getAbsolutePath();
         var7 = StringUtils.replaceGlobal(var7, File.separator, "/");
         String var8 = var5.getName();
         int var9 = var8.lastIndexOf(".");
         if (var9 != -1) {
            var8 = var8.substring(0, var9);
         }

         this.configFileName = var7 + "/" + var8 + "Config";
         this.secretFileName = var7 + "/" + var8 + "Secret";
         if (!var3) {
            File var10 = new File(this.configFileName);
            if (var10.exists()) {
               var10.delete();
            }

            File var11 = new File(this.secretFileName);
            if (var11.exists()) {
               var11.delete();
            }
         }

         this.recording = true;
      }
   }

   public synchronized void stopRecording() throws RecordingException {
      if (!this.isRecording()) {
         throw new RecordingException(ManagementTextTextFormatter.getInstance().getRecordingAlreadyStopped());
      } else {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Stopping WLST script recording session.");
         }

         BufferedWriter var1 = null;

         try {
            var1 = new BufferedWriter(new FileWriter(new File(this.recordingFileName), true));
            if (this.verbose) {
               var1.write("\n# WLST scripts recording end time: " + (new Date()).toString() + "\n\n");
            }

            var1.flush();
         } catch (IOException var10) {
            throw new RecordingException(ManagementTextTextFormatter.getInstance().getRecordingIOException(), var10);
         } finally {
            if (var1 != null) {
               try {
                  var1.close();
               } catch (Exception var9) {
               }
            }

         }

         this.recording = false;
      }
   }

   public synchronized void record(String var1) throws RecordingException {
      if (!this.isRecording()) {
         throw new RecordingException(ManagementTextTextFormatter.getInstance().getRecordFailedDueToRecordingNotStarted(var1));
      } else {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Caller adds string '" + var1 + "' to recording file.");
         }

         try {
            this.write(var1, false, false);
         } catch (IOException var3) {
            throw new RecordingException(ManagementTextTextFormatter.getInstance().getRecordingIOException(), var3);
         }
      }
   }

   public synchronized void encrypt(String var1, String var2, String var3) throws IOException {
      System.setProperty("weblogic.management.confirmKeyfileCreation", "true");
      UserConfigFileManager.setUsernameAndPassword(new UsernameAndPassword("", var3.toCharArray()), this.configFileName, this.secretFileName, var2);
      String var4 = "'" + var1 + "', '" + var2 + "', '" + this.configFileName + "', '" + this.secretFileName + "'";
      this.write("setEncrypted(" + var4 + ")");
   }

   public synchronized boolean isRecording() {
      return this.recording;
   }

   public synchronized String getRecordingFileName() {
      return this.recordingFileName;
   }

   public synchronized boolean isVerbose() {
      return this.verbose;
   }

   public synchronized void write(String var1) throws IOException {
      this.write(var1, false, true);
   }

   public synchronized void write(String var1, boolean var2, boolean var3) throws IOException {
      if (!this.isRecording()) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Can not write to recording file since caller has not started a recording session.");
         }

         throw new NoAccessRuntimeException("Operation can not be performed as caller has not started a recording session");
      } else if (!StringUtils.isEmptyString(var1)) {
         if (var3) {
            if (var1.equals(this.lastCommand)) {
               return;
            }

            if (var1.equals(this.lastCD)) {
               return;
            }
         }

         BufferedWriter var4 = null;

         try {
            var4 = new BufferedWriter(new FileWriter(new File(this.recordingFileName), true));
            if (var2) {
               var4.write("\n");
            }

            var4.write(var1);
            this.lastCommand = var1;
            if (var1.startsWith("cd(")) {
               this.lastCD = var1;
            }

            var4.write("\n");
            var4.flush();
         } finally {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (Exception var11) {
               }
            }

         }

      }
   }

   private static class Maker {
      private static RecordingManager SINGLETON = new RecordingManager();
   }
}
