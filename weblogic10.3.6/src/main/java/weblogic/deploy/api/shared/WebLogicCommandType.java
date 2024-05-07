package weblogic.deploy.api.shared;

import java.util.HashSet;
import java.util.Set;
import javax.enterprise.deploy.shared.CommandType;

public class WebLogicCommandType extends CommandType {
   private int value;
   private static int base = findNextSlot();
   private static int numCmds;
   private static final int WLS_OFFSET = 323;
   public static final WebLogicCommandType DEPLOY;
   public static final WebLogicCommandType UPDATE;
   /** @deprecated */
   public static final WebLogicCommandType DEACTIVATE;
   /** @deprecated */
   public static final WebLogicCommandType REMOVE;
   /** @deprecated */
   public static final WebLogicCommandType ACTIVATE;
   private static final Set moduleCommandsSet;
   private static final String[] stringTable = new String[]{"deploy", "update", "deactivate", "remove", "activate"};
   private static String[] strTable = null;
   private static final WebLogicCommandType[] enumTable;
   private static CommandType[] enumValueTable = null;

   protected WebLogicCommandType(int var1) {
      super(var1);
      this.value = var1;
   }

   public int getValue() {
      return this.value;
   }

   public String[] getStringTable() {
      if (strTable == null) {
         strTable = new String[numCmds];
         String[] var1 = super.getStringTable();

         int var2;
         for(var2 = 0; var2 < base; ++var2) {
            strTable[var2] = var1[var2];
         }

         for(var2 = 323; var2 < numCmds; ++var2) {
            strTable[var2] = stringTable[var2 - 323];
         }
      }

      return strTable;
   }

   public CommandType[] getEnumValueTable() {
      if (enumValueTable == null) {
         enumValueTable = new CommandType[numCmds];
         CommandType[] var1 = super.getEnumValueTable();

         int var2;
         for(var2 = 0; var2 < base; ++var2) {
            enumValueTable[var2] = var1[var2];
         }

         for(var2 = 323; var2 < numCmds; ++var2) {
            enumValueTable[var2] = enumTable[var2 - 323];
         }
      }

      return enumValueTable;
   }

   public static CommandType getCommandType(int var0) {
      return (CommandType)(var0 < base ? CommandType.getCommandType(var0) : enumTable[var0 - 323]);
   }

   public static boolean supportsModuleTargeting(CommandType var0) {
      return moduleCommandsSet.contains(var0);
   }

   public String toString() {
      return this.getStringTable()[this.value];
   }

   protected int getOffset() {
      return 323;
   }

   private static int findNextSlot() {
      int var1 = 0;

      while(true) {
         try {
            CommandType var0 = CommandType.getCommandType(var1);
         } catch (ArrayIndexOutOfBoundsException var3) {
            return var1;
         }

         ++var1;
      }
   }

   static {
      int var0 = 323;
      DEPLOY = new WebLogicCommandType(var0++);
      UPDATE = new WebLogicCommandType(var0++);
      DEACTIVATE = new WebLogicCommandType(var0++);
      REMOVE = new WebLogicCommandType(var0++);
      ACTIVATE = new WebLogicCommandType(var0++);
      numCmds = var0;
      enumTable = new WebLogicCommandType[]{DEPLOY, UPDATE};
      moduleCommandsSet = new HashSet();
      moduleCommandsSet.add(START);
      moduleCommandsSet.add(STOP);
      moduleCommandsSet.add(UNDEPLOY);
      moduleCommandsSet.add(REDEPLOY);
      moduleCommandsSet.add(DEPLOY);
      moduleCommandsSet.add(UPDATE);
      moduleCommandsSet.add(DEACTIVATE);
      moduleCommandsSet.add(REMOVE);
      moduleCommandsSet.add(ACTIVATE);
   }
}
