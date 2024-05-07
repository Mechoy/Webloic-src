package weblogic.management.scripting.plugin;

import org.python.util.InteractiveInterpreter;

public abstract class WLSTPlugin {
   public static InteractiveInterpreter interpreter = null;

   public static void setInterpreter(InteractiveInterpreter var0) {
      interpreter = var0;
   }

   public static void initialize() {
   }
}
