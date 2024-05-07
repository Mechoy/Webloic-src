package weblogic.servlet.logging;

import com.bea.logging.LogFileConfigBean;
import com.bea.logging.RotatingFileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.logging.LogFileConfigUtil;
import weblogic.management.configuration.WebServerMBean;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;

public final class LogManagerHttp {
   private static final DebugLogger DEBUG_LOGGING = DebugLogger.getDebugLogger("DebugHttpLogging");
   private final WebServerMBean mbean;
   private HttpRotatingFileOutputStream outputStream;
   private Logger logger;
   private boolean isExtendedFormat = false;
   private boolean rotateLog = false;
   private boolean logInternalAppAccess = Boolean.getBoolean("weblogic.servlet.logging.LogInternalAppAccess");

   public LogManagerHttp(WebServerMBean var1) {
      this.mbean = var1;
      if (!this.mbean.getWebServerLog().isLoggingEnabled()) {
         HTTPLogger.logHttpLoggingDisabled(this.mbean.getName());
      }
   }

   private HttpRotatingFileOutputStream getLogOutputStream() {
      try {
         return new HttpRotatingFileOutputStream(LogFileConfigUtil.getLogFileConfig(this.mbean.getWebServerLog()));
      } catch (IOException var2) {
         if (DEBUG_LOGGING.isDebugEnabled()) {
            DEBUG_LOGGING.debug("Failed to create HttpRotatingFileOutputStream for the webserver: " + this.mbean.getName(), var2);
         }

         var2.printStackTrace();
         return null;
      }
   }

   public void start() {
      this.initLoggers();
   }

   private void initLoggers() {
      if (!"extended".equals(this.mbean.getWebServerLog().getLogFileFormat())) {
         if (DEBUG_LOGGING.isDebugEnabled()) {
            DEBUG_LOGGING.debug("Creating CLFLogger for the webserver: " + this.mbean.getName());
         }

         this.logger = new CLFLogger(this, this.mbean);
      } else {
         if (DEBUG_LOGGING.isDebugEnabled()) {
            DEBUG_LOGGING.debug("Creating ELFLogger for the webserver: " + this.mbean.getName());
         }

         this.isExtendedFormat = true;
         this.logger = new ELFLogger(this, this.mbean);
      }

      this.outputStream = this.getLogOutputStream();
      this.outputStream.setLogger(this.logger);
      this.mbean.getWebServerLog().setOutputStream(this.outputStream);

      try {
         if (this.rotateLog) {
            this.outputStream.forceRotation();
            this.rotateLog = false;
         }
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   protected void rotateLog() throws IOException {
      this.rotateLog = true;
   }

   public void close() {
      try {
         if (this.outputStream != null) {
            this.outputStream.close();
         }
      } catch (IOException var2) {
         if (DEBUG_LOGGING.isDebugEnabled()) {
            DEBUG_LOGGING.debug("Failed to close HttpRotatingFileOutputStream for the webserver: " + this.mbean.getName(), var2);
         }

         var2.printStackTrace();
      }

   }

   public void log(ServletRequestImpl var1, ServletResponseImpl var2) {
      if (this.mbean.getWebServerLog().isLoggingEnabled() && this.logger != null && this.outputStream != null && (var1.getContext() == null || !var1.getContext().getConfigManager().isAccessLoggingDisabled() || this.logInternalAppAccess)) {
         this.logger.log(var1, var2);
      }
   }

   OutputStream getLogStream() {
      return this.outputStream;
   }

   public boolean isExtendedFormat() {
      return this.isExtendedFormat;
   }

   class HttpRotatingFileOutputStream extends RotatingFileOutputStream {
      private Logger logger = null;

      HttpRotatingFileOutputStream(LogFileConfigBean var2) throws IOException {
         super(var2);
      }

      void setLogger(Logger var1) {
         this.logger = var1;
      }

      public synchronized void forceRotation() throws IOException {
         super.forceRotation();
         this.logger.markRotated();
      }
   }
}
