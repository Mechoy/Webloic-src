package weblogic.jdbc.common.internal;

import com.bea.logging.LogFileConfigBean;
import com.bea.logging.RotatingFileStreamHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.management.configuration.CommonLogMBean;
import weblogic.management.configuration.DataSourceLogFileMBean;
import weblogic.management.configuration.LogFileMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class DataSourceServiceFullImpl extends DataSourceServiceImpl implements BeanUpdateListener {
   private static final AuthenticatedSubject KERNELID = getKernelID();
   static RotatingFileStreamHandler rotatingFileStreamHandler = null;
   private static final String DEFAULT_ROTATION_TIME = "00:00";

   private static AuthenticatedSubject getKernelID() {
      return (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   public OutputStream getLogFileOutputStream() throws Exception {
      if (rotatingFileStreamHandler == null) {
         ServerMBean var1 = ManagementService.getRuntimeAccess(KERNELID).getServer();
         DataSourceLogFileMBean var2 = var1.getDataSource().getDataSourceLogFile();
         LogFileConfigBean var3 = getLogFileConfig(var2);
         rotatingFileStreamHandler = new RotatingFileStreamHandler(var3);
         var2.addBeanUpdateListener(this);
      }

      return rotatingFileStreamHandler.getRotatingFileOutputStream();
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
   }

   public void activateUpdate(BeanUpdateEvent var1) throws BeanUpdateFailedException {
      Object var2 = var1.getSource();
      if (var2 instanceof DataSourceLogFileMBean) {
         DataSourceLogFileMBean var3 = (DataSourceLogFileMBean)var2;

         try {
            LogFileConfigBean var4 = getLogFileConfig(var3);
            rotatingFileStreamHandler.initialize(var4, true);
         } catch (IOException var5) {
            throw new BeanUpdateFailedException("Failed to update log configuration for " + var3.getName(), var5);
         }
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
   }

   private static LogFileConfigBean getLogFileConfig(LogFileMBean var0) {
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
