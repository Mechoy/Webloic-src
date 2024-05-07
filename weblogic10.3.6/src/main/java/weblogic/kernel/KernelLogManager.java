package weblogic.kernel;

import com.bea.logging.LogBufferHandler;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.diagnostics.debug.KernelDebugService;
import weblogic.logging.ConsoleFormatter;
import weblogic.logging.ConsoleHandler;
import weblogic.logging.FileStreamHandler;
import weblogic.management.configuration.KernelMBean;

public class KernelLogManager {
   public static Logger getLogger() {
      return KernelLogManager.LoggerMaker.LOGGER;
   }

   public static void setLogger(Logger var0) {
      if (var0 != null) {
         KernelLogManager.LoggerMaker.LOGGER = var0;
      }

   }

   public static void initialize(KernelMBean var0) {
      KernelLogManager.LoggerMaker.LOGGER = createServerLogger(var0);
   }

   private static Logger createClientLogger(KernelMBean var0) {
      Logger var1 = Logger.getAnonymousLogger();
      var1.setUseParentHandlers(false);
      var1.setLevel(Level.ALL);
      ConsoleHandler var2 = new ConsoleHandler(var0);
      var2.setFormatter(new ConsoleFormatter(var0));
      var1.addHandler(var2);
      if (var0.getLog().getFileName() != null) {
         try {
            FileStreamHandler var3 = new FileStreamHandler(var0.getLog());
            var3.setFormatter(new ConsoleFormatter(var0));
            var1.addHandler(var3);
         } catch (IOException var4) {
            System.err.println("Error opening log file " + var0.getLog().getFileName());
         }
      }

      var1.addHandler(LogBufferHandler.getInstance());
      KernelDebugService.getKernelDebugService().initializeDebugLogging(var1);
      return var1;
   }

   private static Logger createServerLogger(KernelMBean var0) {
      Logger var1 = Logger.getAnonymousLogger();
      var1.setUseParentHandlers(false);
      var1.setLevel(Level.ALL);
      Handler[] var2 = var1.getHandlers();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1.removeHandler(var2[var3]);
         }
      }

      ConsoleHandler var4 = new ConsoleHandler(var0);
      var4.setFormatter(new ConsoleFormatter(var0));
      var1.addHandler(var4);
      var1.addHandler(LogBufferHandler.getInstance());
      return var1;
   }

   private static final class LoggerMaker {
      private static Logger LOGGER = KernelLogManager.createClientLogger(new KernelMBeanStub());
   }
}
