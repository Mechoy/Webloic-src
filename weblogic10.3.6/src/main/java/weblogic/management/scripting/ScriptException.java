package weblogic.management.scripting;

public class ScriptException extends Exception {
   String message;
   String commandName;

   public ScriptException(String var1, String var2) {
      super(var1);
      this.message = var1;
      this.commandName = var2;
   }

   public ScriptException(String var1, Throwable var2, String var3) {
      super(var1, var2);
      this.message = var1;
      this.commandName = var3;
   }

   public String getMessage() {
      return this.message;
   }

   public String getCommandName() {
      return this.commandName;
   }
}
