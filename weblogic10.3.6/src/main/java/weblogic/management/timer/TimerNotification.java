package weblogic.management.timer;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;
import javax.management.Notification;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

class TimerNotification implements weblogic.timers.TimerListener {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final long period;
   private final long nbOccurences;
   private long executeCount;
   private final Timer timer;
   private boolean removed;
   private boolean fixedRate;
   private final Notification notificationEvent;
   private final AuthenticatedSubject subject;
   private ClassLoader initClassLoader;
   private long time;
   private volatile int pastNotificationCount;
   private weblogic.timers.Timer tracker;
   private int instance;

   public TimerNotification(String var1, String var2, Object var3, Date var4, long var5, long var7, Timer var9) {
      this(var1, var2, var3, var4, var5, var7, false, var9);
   }

   public TimerNotification(String var1, String var2, Object var3, Date var4, long var5, long var7, boolean var9, Timer var10) {
      this.executeCount = 0L;
      this.removed = false;
      this.fixedRate = false;
      this.pastNotificationCount = 0;
      this.tracker = null;
      this.instance = -1;
      this.time = var4.getTime();
      this.period = var5;
      this.nbOccurences = var7;
      this.fixedRate = var9;
      this.timer = var10;
      this.notificationEvent = new Notification(var1, var10, 0L, this.time, var2);
      this.notificationEvent.setUserData(var3);
      this.subject = SecurityServiceManager.getCurrentSubject(kernelId);
      this.initClassLoader = Thread.currentThread().getContextClassLoader();
   }

   public Date getDate() {
      return new Date(this.time);
   }

   public long getPeriod() {
      return this.period;
   }

   public long getNbOccurences() {
      return this.nbOccurences;
   }

   public int getTriggerID() {
      return this.instance;
   }

   public void setTriggerID(int var1) {
      this.instance = var1;
   }

   public boolean isRemoved() {
      return this.removed;
   }

   public boolean isFixedRate() {
      return this.fixedRate;
   }

   public void setRemoved(boolean var1) {
      this.removed = var1;
   }

   public String getType() {
      return this.notificationEvent.getType();
   }

   public String getMessage() {
      return this.notificationEvent.getMessage();
   }

   public Object getUserData() {
      return this.notificationEvent.getUserData();
   }

   public long getTimeStamp() {
      return this.time;
   }

   public void timerExpired(weblogic.timers.Timer var1) {
      ++this.executeCount;
      if (this.executeCount == this.nbOccurences) {
         var1.cancel();
      }

      if (!this.timer.isActive()) {
         if (this.timer.getSendPastNotifications()) {
            ++this.pastNotificationCount;
            this.timer.addPastNotification(this);
         }

      } else {
         if (this.initClassLoader != null) {
            Thread.currentThread().setContextClassLoader(this.initClassLoader);
         }

         SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedAction() {
            public Object run() {
               TimerNotification.this.timer.deliverNotifications(TimerNotification.this.notificationEvent);
               return null;
            }
         });
      }
   }

   void setTracker(weblogic.timers.Timer var1) {
      this.tracker = var1;
   }

   weblogic.timers.Timer getTracker() {
      return this.tracker;
   }

   Notification getNotificationObject() {
      return this.notificationEvent;
   }

   int getPastNotificationCount() {
      return this.pastNotificationCount;
   }

   void unsetPastNotificationCount() {
      this.pastNotificationCount = 0;
   }
}
