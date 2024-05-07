package weblogic.application.library;

import weblogic.logging.Loggable;

public class LoggableLibraryProcessingException extends LibraryProcessingException {
   private final Loggable loggable;

   public LoggableLibraryProcessingException(Loggable var1) {
      this(var1, (Throwable)null);
   }

   public LoggableLibraryProcessingException(Loggable var1, Throwable var2) {
      super(var1.getMessage(), var2);
      this.loggable = var1;
   }

   public Loggable getLoggable() {
      return this.loggable;
   }
}
