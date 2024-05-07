package weblogic.logging;

import com.bea.logging.LogFileConfigBean;
import weblogic.management.configuration.CommonLogMBean;
import weblogic.management.configuration.LogFileMBean;

public class LogFileConfigUtil {
   private static final String DEFAULT_ROTATION_TIME = "00:00";

   public static LogFileConfigBean getLogFileConfig(LogFileMBean var0) {
      LogFileConfigBean var1 = new LogFileConfigBean();
      var1.setBaseLogFileName(var0.computeLogFilePath());
      var1.setRotateLogOnStartupEnabled(var0.getRotateLogOnStartup());
      String var2;
      if (var0 instanceof CommonLogMBean) {
         var2 = ((CommonLogMBean)var0).getLogFileSeverity();
         if (var2 != null) {
            var1.setLogFileSeverity(var2);
         }
      }

      var1.setRotatedFileCount(var0.getFileCount());
      var1.setRotationSize(var0.getFileMinSize());
      var2 = var0.getLogFileRotationDir();
      if (var2 != null && var2.length() > 0) {
         var1.setLogFileRotationDir(var2);
      }

      var1.setNumberOfFilesLimited(var0.isNumberOfFilesLimited());
      String var3 = var0.getRotationTime();
      if (var3 == null || var3.equals("")) {
         var3 = "00:00";
      }

      var1.setRotationTime(var3);
      String var4 = var0.getRotationType();
      if (var4 == null || var4.equals("")) {
         var4 = "bySize";
      }

      var1.setRotationType(var4);
      var1.setRotationTimeSpan(var0.getFileTimeSpan());
      var1.setRotationTimeSpanFactor(var0.getFileTimeSpanFactor());
      var1.setBufferSizeKB(var0.getBufferSizeKB());
      return var1;
   }
}
