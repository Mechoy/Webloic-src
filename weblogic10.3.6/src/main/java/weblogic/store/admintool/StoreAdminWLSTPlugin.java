package weblogic.store.admintool;

import java.io.InputStream;
import weblogic.kernel.Kernel;
import weblogic.management.scripting.plugin.WLSTPlugin;

public class StoreAdminWLSTPlugin extends WLSTPlugin {
   static final String PYTHON_COMMAND_FILE = "storeadminWLST.py";
   static final StoreAdmin sa = new StoreAdmin(new String[0], false);

   public static void initialize() {
      Kernel.ensureInitialized();
      InputStream var0 = StoreAdminIF.class.getResourceAsStream("storeadminWLST.py");

      assert var0 != null : "Could not load python command file for StoreAdmin";

      interpreter.execfile(var0);
   }

   public static String[] getStoreNames() {
      return CommandImpls.getStoreNames(sa);
   }

   public static String[] getConnectionNames(String var0) {
      return CommandImpls.getConnectionNames(sa, var0);
   }

   public static boolean runCommand(String var0) {
      return sa.execute(var0);
   }

   public static String getCommand(CommandDefs.CommandType var0) {
      return var0 == null ? "" : var0.toString();
   }

   public static String getParam(CommandDefs.CommandParam var0, String var1) {
      if (var0 == null) {
         return "";
      } else {
         if (var0.optCount() > 0) {
            if (var1 == null || var1.trim().equals("")) {
               return "";
            }

            var1 = var1.trim();
         }

         return var0.asString(var1);
      }
   }
}
