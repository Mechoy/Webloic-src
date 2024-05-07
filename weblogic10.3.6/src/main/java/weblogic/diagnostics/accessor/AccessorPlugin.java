package weblogic.diagnostics.accessor;

import weblogic.management.scripting.plugin.WLSTPlugin;

public class AccessorPlugin extends WLSTPlugin {
   public static void initialize() {
      interpreter.execfile(XMLExporter.class.getResourceAsStream("export.py"));
   }
}
