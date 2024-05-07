package weblogic.diagnostics.debug;

public class DebugValidators {
   public static void validateDebugScope(String var0) throws InvalidDebugScopeException {
      ServerDebugService var1 = ServerDebugService.getInstance();
      var1.testDebugScopeName(var0);
   }
}
