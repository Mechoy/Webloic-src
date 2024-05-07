package weblogic.management.logging;

import weblogic.management.jmx.modelmbean.NotificationGenerator;
import weblogic.management.jmx.modelmbean.WLSModelMBeanContext;

public class NotificationTranslator {
   public NotificationTranslator(WLSModelMBeanContext var1, Object var2, NotificationGenerator var3) {
      LogBroadcaster var4 = (LogBroadcaster)var2;
      var4.addNotificationGenerator(var3);
   }
}
