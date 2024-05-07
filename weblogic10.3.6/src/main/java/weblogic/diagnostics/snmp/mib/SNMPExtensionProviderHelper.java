package weblogic.diagnostics.snmp.mib;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.utils.jars.ManifestManager;

public final class SNMPExtensionProviderHelper {
   public static final DebugLogger DEBUG_LOGGER = DebugLogger.getDebugLogger("DebugSNMPExtensionProvider");

   public static List<SNMPExtensionProvider> discoverSNMPAgentExtensionProviders() {
      ArrayList var0 = new ArrayList();
      HashSet var1 = new HashSet();
      ClassLoader var2 = ClassLoader.getSystemClassLoader();
      Iterator var3 = ManifestManager.getServices(SNMPExtensionProvider.class, var2).iterator();

      while(var3.hasNext()) {
         SNMPExtensionProvider var4 = (SNMPExtensionProvider)var3.next();
         if (var4 != null) {
            String var5 = var4.getClass().getName();
            if (var1.contains(var5)) {
               if (DEBUG_LOGGER.isDebugEnabled()) {
                  DEBUG_LOGGER.debug("SNMPExtensionProvider implementation class " + var5 + " already discovered");
               }
            } else {
               var1.add(var5);
               var0.add(var4);
               if (DEBUG_LOGGER.isDebugEnabled()) {
                  DEBUG_LOGGER.debug("SNMPExtensionProvider implementation class " + var5 + " discovered");
               }
            }
         }
      }

      return var0;
   }
}
