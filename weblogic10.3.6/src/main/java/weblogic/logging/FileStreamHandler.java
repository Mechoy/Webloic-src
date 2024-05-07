package weblogic.logging;

import com.bea.logging.LogFileConfigBean;
import com.bea.logging.RotatingFileStreamHandler;
import java.io.File;
import java.io.IOException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.configuration.LogFileMBean;
import weblogic.management.configuration.LogMBean;

public final class FileStreamHandler extends RotatingFileStreamHandler implements BeanUpdateListener {
   private final LogFileMBean logConfig;
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugLoggingConfiguration");

   /** @deprecated */
   public FileStreamHandler(LogMBean var1) throws IOException {
      this((LogFileMBean)var1);
   }

   public FileStreamHandler(LogFileMBean var1) throws IOException {
      super(LogFileConfigUtil.getLogFileConfig(var1));
      this.setErrorManager(new WLErrorManager(this));
      this.logConfig = var1;
      var1.setOutputStream(this.getRotatingFileOutputStream());
      var1.addBeanUpdateListener(this);
   }

   public String toString() {
      String var1 = this.logConfig.computeLogFilePath();

      try {
         return (new File(var1)).getCanonicalPath();
      } catch (IOException var3) {
         return var1;
      }
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
   }

   public void activateUpdate(BeanUpdateEvent var1) throws BeanUpdateFailedException {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Activating update to log configuration");
      }

      Object var2 = var1.getSource();
      if (var2 instanceof LogFileMBean) {
         LogFileMBean var3 = (LogFileMBean)var2;

         try {
            LogFileConfigBean var4 = LogFileConfigUtil.getLogFileConfig(var3);
            this.initialize(var4, true);
         } catch (IOException var5) {
            throw new BeanUpdateFailedException("Failed to update log configuration for " + var3.getName(), var5);
         }
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
   }
}
