package weblogic.logging;

import com.bea.logging.BaseLogEntry;
import com.bea.logging.LogBufferHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.diagnostics.image.ImageSource;
import weblogic.diagnostics.image.ImageSourceCreationException;
import weblogic.diagnostics.image.descriptor.LogEntryBean;
import weblogic.diagnostics.image.descriptor.LoggingImageSourceBean;

public class LoggingImageSource implements ImageSource {
   private static LoggingImageSource singleton = new LoggingImageSource();
   private boolean timedOut = false;

   public static LoggingImageSource getInstance() {
      return singleton;
   }

   private LoggingImageSource() {
   }

   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      this.timedOut = false;

      try {
         DescriptorManager var2 = new DescriptorManager();
         Descriptor var3 = var2.createDescriptorRoot(LoggingImageSourceBean.class);
         LoggingImageSourceBean var4 = (LoggingImageSourceBean)var3.getRootBean();
         Iterator var5 = LogBufferHandler.getInstance().getLogBufferIterator();

         while(var5.hasNext()) {
            BaseLogEntry var6 = (BaseLogEntry)var5.next();
            LogEntryBean var7 = var4.createLogEntry();
            var7.setDiagnosticContextId(var6.getDiagnosticContextId());
            var7.setMachineName(var6.getMachineName());
            var7.setServerName(var6.getServerName());
            var7.setTransactionId(var6.getTransactionId());
            var7.setUserId(var6.getUserId());
            var7.setLogMessage(var6.getLogMessage());
            var7.setFormattedDate(var6.getFormattedDate());
            var7.setMessageId(var6.getId());
            var7.setSeverity(var6.getSeverity());
            var7.setSubsystem(var6.getSubsystem());
            var7.setThreadName(var6.getThreadName());
            var7.setTimestamp(var6.getTimestamp());
            if (var6.getThrowableWrapper() != null) {
               var7.setStackTrace(var6.getThrowableWrapper().toString());
            }
         }

         var2.writeDescriptorBeanAsXML((DescriptorBean)var4, var1);
      } catch (IOException var8) {
         throw new ImageSourceCreationException(var8);
      }
   }

   public void timeoutImageCreation() {
      this.timedOut = true;
   }
}
