package weblogic.management.deploy.internal;

import javax.management.MBeanException;
import javax.management.ObjectName;
import weblogic.management.DeploymentNotification;
import weblogic.management.deploy.DeploymentCompatibilityEvent;
import weblogic.management.deploy.DeploymentCompatibilityEventHandler;
import weblogic.management.deploy.DeploymentCompatibilityEventManager;
import weblogic.management.jmx.modelmbean.NotificationGenerator;
import weblogic.management.jmx.modelmbean.WLSModelMBeanContext;

public class NotificationTranslator implements DeploymentCompatibilityEventHandler {
   private ObjectName objName;
   private NotificationGenerator generator;

   public NotificationTranslator(WLSModelMBeanContext var1, Object var2, NotificationGenerator var3) {
      this.generator = var3;
      this.objName = var3.getObjectName();
      ((DeploymentCompatibilityEventManager)var2).addHandler(this);
   }

   public void handleEvent(DeploymentCompatibilityEvent var1) {
      if (this.generator.isSubscribed()) {
         DeploymentNotification var2 = null;
         if ("weblogic.deployment.application.module".equals(var1.getEventType())) {
            var2 = new DeploymentNotification(this.objName, this.generator.incrementSequenceNumber(), var1.getServerName(), var1.getApplicationName(), var1.getModuleName(), var1.getTransition(), var1.getCurrentState(), var1.getTargetState());
         } else if ("weblogic.deployment.application".equals(var1.getEventType())) {
            var2 = new DeploymentNotification(this.objName, this.generator.incrementSequenceNumber(), var1.getServerName(), var1.getApplicationName(), var1.getApplicationPhase());
            var2.setTask(var1.getTaskID());
         }

         if (var2 == null) {
            throw new AssertionError("Invalid Deployment Notfication Type " + var1);
         } else {
            try {
               this.generator.sendNotification(var2);
            } catch (MBeanException var4) {
               throw new AssertionError(var4);
            }
         }
      }
   }
}
