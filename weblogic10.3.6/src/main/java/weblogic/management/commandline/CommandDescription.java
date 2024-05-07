package weblogic.management.commandline;

public class CommandDescription {
   String command;
   int commandId;
   String syntax;
   String description;
   String example;
   String commandUse;
   String argumentDescription;
   String infoMessage;
   int genericType;
   boolean expose;

   public CommandDescription(String var1, int var2, String var3, String var4, boolean var5, String var6, String var7, String var8, int var9) {
      this.command = var1;
      this.commandId = var2;
      this.syntax = var3;
      this.description = var4;
      this.expose = var5;
      this.example = var6;
      this.commandUse = var7;
      this.argumentDescription = var8;
      this.genericType = var9;
      this.infoMessage = "";
   }

   public CommandDescription(String var1, int var2, String var3, String var4, boolean var5, String var6, String var7, String var8, int var9, String var10) {
      this.command = var1;
      this.commandId = var2;
      this.syntax = var3;
      this.description = var4;
      this.expose = var5;
      this.example = var6;
      this.commandUse = var7;
      this.argumentDescription = var8;
      this.genericType = var9;
      this.infoMessage = var10;
   }

   public CommandDescription(String var1, int var2, String var3, String var4, boolean var5, String var6, String var7, String var8) {
      this.command = var1;
      this.commandId = var2;
      this.syntax = var3;
      this.description = var4;
      this.expose = var5;
      this.example = var6;
      this.commandUse = var7;
      this.argumentDescription = var8;
      this.genericType = 0;
      this.infoMessage = "";
   }

   public String getCommand() {
      return this.command;
   }

   public int getCommandId() {
      return this.commandId;
   }

   public String getSyntax() {
      return this.syntax;
   }

   public String getDescription() {
      return this.description;
   }

   public boolean isExpose() {
      return this.expose;
   }

   public String getExample() {
      return this.example;
   }

   public String getCommandUse() {
      return this.commandUse;
   }

   public String getArgumentDescription() {
      return this.argumentDescription;
   }

   public int getGenericType() {
      return this.genericType;
   }

   public String getInfoMessage() {
      return this.infoMessage;
   }
}
