package weblogic.deploy.utils;

import javax.management.Notification;
import javax.management.NotificationListener;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.utils.Debug;

public class TaskCompletionNotificationListener implements NotificationListener {
   private static final long WAIT_PERIOD = 60000L;
   private final DeploymentTaskRuntimeMBean task;
   private boolean isCompleted = false;

   public TaskCompletionNotificationListener(DeploymentTaskRuntimeMBean var1) {
      this.task = var1;
   }

   public void handleNotification(Notification var1, Object var2) {
      synchronized(this) {
         this.isCompleted = true;
         this.notify();
      }
   }

   public void waitForTaskCompletion(long var1) {
      long var3 = var1 == 0L ? 60000L : var1;
      long var5 = System.currentTimeMillis() + var3;
      long var7 = var3;

      while(!this.isCompleted && !this.isTaskCompleted()) {
         if (var1 != 0L) {
            var7 = var5 - System.currentTimeMillis();
         }

         if (var7 <= 0L) {
            return;
         }

         long var9 = var7 <= 60000L ? var7 : 60000L;
         this.waitForTaskCompletionInternal(var9);
      }

   }

   private void waitForTaskCompletionInternal(long var1) {
      synchronized(this) {
         if (!this.isCompleted) {
            try {
               this.wait(var1);
            } catch (InterruptedException var6) {
            }
         }

      }
   }

   private boolean isTaskCompleted() {
      Debug.assertion(this.task != null);
      return !this.task.isRunning();
   }
}
