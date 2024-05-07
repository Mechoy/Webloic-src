package weblogic.management.logging;

import com.bea.logging.RotatingFileOutputStream;
import java.io.IOException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.LogFileMBean;
import weblogic.management.runtime.LogRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public final class LogRuntime extends RuntimeMBeanDelegate implements LogRuntimeMBean {
   private LogFileMBean logfileMBean;

   public LogRuntime(LogFileMBean var1, RuntimeMBean var2) throws ManagementException {
      super(var1.getName(), var2);
      this.logfileMBean = var1;
   }

   public void forceLogRotation() throws ManagementException {
      RotatingFileOutputStream var1 = (RotatingFileOutputStream)this.logfileMBean.getOutputStream();
      if (var1 != null) {
         try {
            var1.forceRotation();
         } catch (IOException var3) {
            throw new ManagementException(var3.toString());
         }
      }

   }

   public void ensureLogOpened() throws ManagementException {
      RotatingFileOutputStream var1 = (RotatingFileOutputStream)this.logfileMBean.getOutputStream();
      if (var1 != null && !var1.isStreamOpened()) {
         try {
            var1.open();
         } catch (IOException var3) {
            throw new ManagementException(var3.toString());
         }
      }

   }

   public void close() throws ManagementException {
      RotatingFileOutputStream var1 = (RotatingFileOutputStream)this.logfileMBean.getOutputStream();
      if (var1 != null) {
         try {
            var1.close();
         } catch (IOException var3) {
            throw new ManagementException(var3.toString());
         }
      }

   }
}
