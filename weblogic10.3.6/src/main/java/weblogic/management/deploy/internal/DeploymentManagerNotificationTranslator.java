package weblogic.management.deploy.internal;

import weblogic.management.jmx.modelmbean.NotificationGenerator;
import weblogic.management.jmx.modelmbean.WLSModelMBeanContext;

public class DeploymentManagerNotificationTranslator {
   public DeploymentManagerNotificationTranslator(WLSModelMBeanContext var1, Object var2, NotificationGenerator var3) {
      if (var2 instanceof DeploymentManagerImpl) {
         ((DeploymentManagerImpl)var2).setNotificationGenerator(var3);
      }

   }
}
