package weblogic.diagnostics.watch;

import weblogic.management.jmx.modelmbean.NotificationGenerator;
import weblogic.management.jmx.modelmbean.WLSModelMBeanContext;

public class NotificationTranslator {
   public NotificationTranslator(WLSModelMBeanContext var1, Object var2, NotificationGenerator var3) {
      if (var2 instanceof JMXNotificationProducer) {
         ((JMXNotificationProducer)var2).setNotificationGenerator(var3);
      }

   }
}
