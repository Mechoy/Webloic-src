package weblogic.logging;

import java.io.OutputStream;
import java.util.logging.ErrorManager;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import weblogic.kernel.Kernel;
import weblogic.management.configuration.CommonLogMBean;
import weblogic.management.configuration.KernelMBean;
import weblogic.management.configuration.LogMBean;

public final class ConsoleHandler extends StreamHandler {
   /** @deprecated */
   public ConsoleHandler(KernelMBean var1) {
      this(System.out, var1.getLog());
   }

   /** @deprecated */
   public ConsoleHandler(LogMBean var1) {
      this(System.out, var1);
   }

   public ConsoleHandler(CommonLogMBean var1) {
      this(System.out, var1);
   }

   ConsoleHandler(OutputStream var1, CommonLogMBean var2) {
      super(var1, new SimpleFormatter());
      ErrorManager var3 = this.getErrorManager();
      if (Kernel.isApplet()) {
         this.setErrorManager(var3);
      } else {
         this.setErrorManager(new WLErrorManager(this));
      }

      this.setLevel(WLLevel.getLevel(Severities.severityStringToNum(var2.getStdoutSeverity())));
   }

   public synchronized void publish(LogRecord var1) {
      super.publish(var1);
      super.flush();
   }

   public void close() {
      super.flush();
   }
}
