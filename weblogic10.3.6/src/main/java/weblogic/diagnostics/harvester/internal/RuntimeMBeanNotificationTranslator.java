package weblogic.diagnostics.harvester.internal;

import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.jmx.modelmbean.NotificationGenerator;
import weblogic.management.jmx.modelmbean.WLSModelMBeanContext;

public class RuntimeMBeanNotificationTranslator {
   private final DebugLogger dbg = DebugSupport.getDebugLogger();

   public RuntimeMBeanNotificationTranslator(WLSModelMBeanContext var1, Object var2, NotificationGenerator var3) {
      this.dbg.debug("NotificationGenerator=" + var2 + " " + var3);
      if (var2 instanceof HarvesterRuntimeMBeanImpl) {
         ((HarvesterRuntimeMBeanImpl)var2).setNotificationGenerator(var3);
      }

   }
}
