package weblogic.logging.j2ee;

import java.io.IOException;
import java.util.logging.Logger;
import weblogic.j2ee.descriptor.wl.LoggingBean;
import weblogic.kernel.KernelLogManager;
import weblogic.logging.FileStreamHandler;
import weblogic.logging.LogFileFormatter;
import weblogic.logging.MessageLogger;
import weblogic.logging.WLErrorManager;
import weblogic.logging.WLLevel;
import weblogic.logging.WLLogRecord;
import weblogic.logging.WLLogger;

public final class ServletContextLogger {
   private static final String LOCALIZER_CLASS = "weblogic.i18n.LogMgmtLogLocalizer";
   private static final String MSG_ID = "170028";
   private String contextName;
   private LoggingBeanAdapter logAdapter = null;
   private Logger logger = KernelLogManager.getLogger();

   public ServletContextLogger(String var1, LoggingBean var2) {
      this.contextName = var1;
      if (var2 != null && var2.getLogFilename() != null && var2.getLogFilename().length() > 0) {
         WLLogger var3 = new WLLogger(this.contextName);
         this.logAdapter = new LoggingBeanAdapter(var2);

         try {
            FileStreamHandler var4 = new FileStreamHandler(this.logAdapter);
            var4.setFormatter(new LogFileFormatter(this.logAdapter));
            var4.setErrorManager(new WLErrorManager(var4));
            var3.addHandler(var4);
            this.logger = var3;
         } catch (IOException var6) {
            Object[] var5 = new Object[]{this.contextName};
            MessageLogger.log("170028", var5, "weblogic.i18n.LogMgmtLogLocalizer");
         }
      }

   }

   public LoggingBeanAdapter getLogAdapter() {
      return this.logAdapter;
   }

   public void log(String var1) {
      WLLogRecord var2 = new WLLogRecord(WLLevel.INFO, var1);
      var2.setLoggerName(this.contextName);
      this.logger.log(var2);
   }

   public void logError(String var1) {
      WLLogRecord var2 = new WLLogRecord(WLLevel.ERROR, var1);
      var2.setLoggerName(this.contextName);
      this.logger.log(var2);
   }

   public void log(String var1, Throwable var2) {
      WLLogRecord var3 = new WLLogRecord(WLLevel.ERROR, var1, var2);
      var3.setLoggerName(this.contextName);
      this.logger.log(var3);
   }
}
