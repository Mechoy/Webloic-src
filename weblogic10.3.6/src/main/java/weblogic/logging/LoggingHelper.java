package weblogic.logging;

import java.util.logging.Logger;
import weblogic.i18n.logging.LoggingTextFormatter;
import weblogic.kernel.Kernel;
import weblogic.kernel.KernelLogManager;
import weblogic.management.logging.DomainLogHandlerException;
import weblogic.management.logging.DomainLogHandlerImpl;

public final class LoggingHelper {
   public static Logger getServerLogger() {
      return Kernel.isServer() ? KernelLogManager.getLogger() : null;
   }

   public static Logger getClientLogger() {
      return !Kernel.isServer() ? KernelLogManager.getLogger() : null;
   }

   public static Logger getDomainLogger() throws LoggerNotAvailableException {
      try {
         return ((DomainLogHandlerImpl)DomainLogHandlerImpl.getInstance()).getDomainLogger();
      } catch (DomainLogHandlerException var2) {
         LoggingTextFormatter var1 = new LoggingTextFormatter();
         throw new LoggerNotAvailableException(var1.getDomainLoggerDoesNotExistMsg());
      }
   }
}
