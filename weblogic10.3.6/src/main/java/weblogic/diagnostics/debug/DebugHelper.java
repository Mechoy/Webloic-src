package weblogic.diagnostics.debug;

public class DebugHelper {
   public static DebugScopeUtil getDebugScopeUtil() {
      return ServerDebugService.getInstance();
   }
}
