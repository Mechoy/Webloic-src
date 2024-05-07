package weblogic.diagnostics.harvester;

import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.type.UnexpectedExceptionHandler;

public final class LogSupport implements I18NConstants {
   private static final DebugLogger DBG = DebugLogger.getDebugLogger("DebugDiagnosticsHarvester");
   private static final String GENERIC_PROBLEM_TEXT = I18NSupport.formatter().getGenericHarvesterProblemMessage();

   public static void logUnexpectedProblem(String var0) {
      DiagnosticsLogger.logGenericHarvesterProblem(getGenericHarvesterProblemText(var0));
   }

   public static void logExpectedProblem(String var0) {
      DiagnosticsLogger.logGenericHarvesterProblem(var0);
   }

   public static String getGenericHarvesterProblemText(String var0) {
      if (DBG.isDebugEnabled()) {
         DBG.debug("*** ERROR *** " + var0);
      }

      return GENERIC_PROBLEM_TEXT;
   }

   public static void logUnexpectedException(String var0, Throwable var1) {
      UnexpectedExceptionHandler.handle(getGenericHarvesterProblemText(var0), var1);
   }

   public static void assertCondition(boolean var0, String var1) {
      if (!var0) {
         throw new AssertionError(getGenericHarvesterProblemText(var1));
      }
   }
}
