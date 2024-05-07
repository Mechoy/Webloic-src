package weblogic.nodemanager;

import java.io.IOException;

public class ScriptExecutionFailureException extends IOException {
   public static final int SCRIPT_TIMEOUT_EXIT_CODE = -101;
   public static final int SCRIPT_USAGE_ERROR_EXIT_CODE = -100;
   public final String scriptPath;
   public final int exitCode;

   public ScriptExecutionFailureException(String var1, int var2) {
      super(NodeManagerTextTextFormatter.getInstance().getScriptExecutionFailure(var1, var2));
      this.scriptPath = var1;
      this.exitCode = var2;
   }

   public String getScriptPath() {
      return this.scriptPath;
   }

   public int getExitCode() {
      return this.exitCode;
   }
}
