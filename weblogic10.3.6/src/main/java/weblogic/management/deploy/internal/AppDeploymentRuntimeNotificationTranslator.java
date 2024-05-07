package weblogic.management.deploy.internal;

import weblogic.management.jmx.modelmbean.NotificationGenerator;
import weblogic.management.jmx.modelmbean.WLSModelMBeanContext;

public class AppDeploymentRuntimeNotificationTranslator {
   public AppDeploymentRuntimeNotificationTranslator(WLSModelMBeanContext var1, Object var2, NotificationGenerator var3) {
      if (var2 instanceof AppDeploymentRuntimeImpl) {
         ((AppDeploymentRuntimeImpl)var2).setNotificationGenerator(var3);
      }

   }
}
