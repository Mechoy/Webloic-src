package weblogic.servlet.logging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.configuration.WebServerMBean;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.utils.StringUtils;

public final class ELFLogger implements Logger {
   private static final String LINE_SEP = System.getProperty("line.separator");
   private static final String ELF_HEADERS_0;
   private static final String ELF_HEADERS_2;
   private static final String ELF_HEADERS_4 = "\t";
   private static final String ELF_HEADERS_6;
   private static final String[] ELF_HEADERS;
   private final String fieldsDirective;
   private final LogFormat logFormat;
   private final boolean logTimeInGMT;
   private final LogManagerHttp logManager;
   private static final DebugLogger DEBUG_LOGGING;
   private volatile boolean needToWriteELFHeaders;
   private boolean logMillis;

   public ELFLogger(LogManagerHttp var1, WebServerMBean var2) {
      this.logManager = var1;
      this.logTimeInGMT = var2.getWebServerLog().isLogTimeInGMT();
      this.logMillis = var2.getWebServerLog().isLogMilliSeconds();
      this.fieldsDirective = this.findFieldsDirective(var2);
      this.logFormat = new LogFormat(this.fieldsDirective);
      if (var2.getWebServerLog().getRotateLogOnStartup()) {
         this.needToWriteELFHeaders = true;
      }

   }

   private String findFieldsDirective(WebServerMBean var1) {
      try {
         String[] var2 = readELFFields(var1.getWebServerLog().computeLogFilePath());
         if (var2 == null) {
            if (DEBUG_LOGGING.isDebugEnabled()) {
               DEBUG_LOGGING.debug("No previous access.log for webserver: " + var1.getName());
            }

            this.needToWriteELFHeaders = true;
            return var1.getWebServerLog().getELFFields();
         }

         if (var2[0] != null && !var2[0].startsWith("1.0")) {
            HTTPLogger.logELFLogNotFormattedProperly();
         } else if (var2[1] != null && var2[1].length() > 0) {
            if (var2[1].equals(var1.getWebServerLog().getELFFields())) {
               if (DEBUG_LOGGING.isDebugEnabled()) {
                  DEBUG_LOGGING.debug("Fields in access.log match with webserver: " + var1.getName());
               }

               return var2[1];
            }

            if (!var1.getWebServerLog().isSet("ELFFields")) {
               if (DEBUG_LOGGING.isDebugEnabled()) {
                  DEBUG_LOGGING.debug("Found modified fields: " + var2[1] + " in access.log for webserver: " + var1.getName());
               }

               return var2[1];
            }

            if (DEBUG_LOGGING.isDebugEnabled()) {
               DEBUG_LOGGING.debug("WebServerMBean's ELF-Fields have been modified for : " + var1.getName() + " access.log will be rotated");
            }

            HTTPLogger.logELFFieldsChanged(var1.getName(), var2[1], var1.getWebServerLog().getELFFields());
            this.logManager.rotateLog();
            this.needToWriteELFHeaders = true;
            return var1.getWebServerLog().getELFFields();
         }
      } catch (IOException var4) {
         HTTPLogger.logELFReadHeadersException(var4);
      }

      if (DEBUG_LOGGING.isDebugEnabled()) {
         DEBUG_LOGGING.debug("Rotating access.log for : " + var1.getName() + " since the old format doesn't seem to ELF");
      }

      try {
         this.logManager.rotateLog();
      } catch (IOException var3) {
         HTTPLogger.logFailedToRollLogFile(var1.getName(), var3);
      }

      this.needToWriteELFHeaders = true;
      return var1.getWebServerLog().getELFFields();
   }

   static String[] readELFFields(String var0) throws IOException {
      BufferedReader var1 = getLogReader(var0);
      if (var1 == null) {
         return null;
      } else {
         String[] var2 = new String[2];

         String var3;
         try {
            while((var3 = var1.readLine()) != null && var3.startsWith("#")) {
               String[] var4 = StringUtils.split(var3.substring(1), ':');
               var4[0] = var4[0].trim();
               if ("Version".equals(var4[0])) {
                  var2[0] = var4[1].trim();
               } else if ("Fields".equals(var4[0])) {
                  var2[1] = var4[1].trim();
               }
            }
         } finally {
            var1.close();
         }

         return var2;
      }
   }

   public int log(ServletRequestImpl var1, ServletResponseImpl var2) {
      if (this.needToWriteELFHeaders) {
         OutputStream var3 = this.logManager.getLogStream();
         synchronized(var3) {
            this.writeELFHeaders(var3);
         }
      }

      FormatStringBuffer var10 = new FormatStringBuffer(128);
      var10.setUseGMT(this.logTimeInGMT);
      var10.setLogMillis(this.logMillis);
      int var4 = this.logFormat.countFields();
      HttpAccountingInfoImpl var5 = var1.getHttpAccountingInfo();

      for(int var6 = 0; var6 < var4; ++var6) {
         this.logFormat.getFieldAt(var6).logField(var5, var10);
         if (var6 != var4 - 1) {
            var10.append('\t');
         }
      }

      var10.append(LINE_SEP);
      OutputStream var11 = this.logManager.getLogStream();

      try {
         var11.write(var10.getBytes(), 0, var10.size());
      } catch (IOException var8) {
      }

      return var10.size();
   }

   private static BufferedReader getLogReader(String var0) {
      File var2 = null;

      try {
         if (var0 == null) {
            return null;
         } else {
            var2 = new File(var0);
            String var3 = var2.getParent();
            if (var3 != null) {
               File var4 = new File(var3);
               if (!var4.exists()) {
                  var4.mkdirs();
               }
            }

            BufferedReader var1 = new BufferedReader(new FileReader(var2));
            return var1;
         }
      } catch (IOException var5) {
         return null;
      }
   }

   private String getELFHeaders() {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < ELF_HEADERS.length; ++var2) {
         if (ELF_HEADERS[var2] == null) {
            FormatStringBuffer var3;
            switch (var2) {
               case 1:
                  var1.append(this.fieldsDirective);
               case 2:
               case 4:
               default:
                  break;
               case 3:
                  var3 = (new FormatStringBuffer()).appendDate();
                  var1.append(new String(var3.getBytes(), 0, var3.size()));
                  break;
               case 5:
                  var3 = (new FormatStringBuffer()).appendTime();
                  var1.append(new String(var3.getBytes(), 0, var3.size()));
            }
         } else {
            var1.append(ELF_HEADERS[var2]);
         }
      }

      return var1.toString();
   }

   public void markRotated() {
      OutputStream var1 = this.logManager.getLogStream();
      synchronized(var1) {
         this.needToWriteELFHeaders = true;
         this.writeELFHeaders(var1);
      }
   }

   private void writeELFHeaders(OutputStream var1) {
      if (this.needToWriteELFHeaders) {
         byte[] var2 = this.getELFHeaders().getBytes();

         try {
            var1.write(var2, 0, var2.length);
         } catch (IOException var4) {
         }

         this.needToWriteELFHeaders = false;
      }
   }

   static {
      ELF_HEADERS_0 = "#Version:\t1.0" + LINE_SEP + "#Fields:\t";
      ELF_HEADERS_2 = LINE_SEP + "#Software:\tWebLogic" + LINE_SEP + "#Start-Date:\t";
      ELF_HEADERS_6 = LINE_SEP;
      ELF_HEADERS = new String[]{ELF_HEADERS_0, null, ELF_HEADERS_2, null, "\t", null, ELF_HEADERS_6};
      DEBUG_LOGGING = DebugLogger.getDebugLogger("DebugHttpLogging");
   }
}
