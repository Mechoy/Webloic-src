package weblogic.management.timer;

import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public class TimerListener {
   private NotificationListener listener;
   private NotificationFilter filter;
   private Object handback;
   private AuthenticatedSubject subject;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public TimerListener(NotificationListener var1, NotificationFilter var2, Object var3) {
      this.listener = var1;
      this.filter = var2;
      this.handback = var3;
      this.subject = SecurityServiceManager.getCurrentSubject(kernelId);
   }

   public NotificationListener getListener() {
      return this.listener;
   }

   public void deliverNotification(final Notification var1) {
      final NotificationFilter var3 = this.filter;
      final Object var4 = this.handback;
      final NotificationListener var5 = this.listener;
      SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedAction() {
         public Object run() {
            if (var3 == null || var3.isNotificationEnabled(var1)) {
               var5.handleNotification(var1, var4);
            }

            return null;
         }
      });
   }
}
