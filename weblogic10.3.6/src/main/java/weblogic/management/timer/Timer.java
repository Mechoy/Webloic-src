package weblogic.management.timer;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.management.InstanceNotFoundException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.timer.TimerMBean;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;

public final class Timer extends javax.management.timer.Timer implements TimerMBean, NotificationBroadcaster, Serializable {
   private Object listenerLock = new Object();
   private Map allNotifications = new HashMap(11);
   private TimerListener[] allListeners = new TimerListener[0];
   private int notificationCount = 0;
   private int listenerCount = 0;
   private boolean sendPastNotifications = false;
   TimerManager timerManager = TimerManagerFactory.getTimerManagerFactory().getTimerManager("TimerMBean");
   private int Id = 0;
   private volatile boolean isActive = true;
   private Set pastNotifications = new HashSet(7);

   public int getNbNotifications() {
      return this.notificationCount;
   }

   public Vector getAllNotificationIDs() {
      Vector var1 = new Vector();
      synchronized(this.allNotifications) {
         Iterator var3 = this.allNotifications.keySet().iterator();

         while(var3.hasNext()) {
            var1.addElement(var3.next());
         }

         return var1;
      }
   }

   public Vector getNotificationIDs(String var1) {
      Vector var2 = new Vector();
      synchronized(this.allNotifications) {
         Iterator var4 = this.allNotifications.keySet().iterator();

         while(var4.hasNext()) {
            Integer var5 = (Integer)var4.next();
            TimerNotification var6 = (TimerNotification)this.allNotifications.get(var5);
            if (var6 != null && var1.equals(var6.getType())) {
               var2.addElement(new Integer(var6.getTriggerID()));
            }
         }

         return var2;
      }
   }

   public String getNotificationType(Integer var1) {
      TimerNotification var2 = this.getNotification(var1);
      return var2 != null ? var2.getType() : null;
   }

   public String getNotificationMessage(Integer var1) {
      TimerNotification var2 = this.getNotification(var1);
      return var2 != null ? var2.getMessage() : null;
   }

   public Object getNotificationUserData(Integer var1) {
      TimerNotification var2 = this.getNotification(var1);
      return var2 != null ? var2.getUserData() : null;
   }

   public Date getDate(Integer var1) {
      TimerNotification var2 = this.getNotification(var1);
      return var2 != null ? var2.getDate() : null;
   }

   public Long getPeriod(Integer var1) {
      TimerNotification var2 = this.getNotification(var1);
      return var2 != null ? new Long(var2.getTimeStamp()) : null;
   }

   public Long getNbOccurences(Integer var1) {
      TimerNotification var2 = this.getNotification(var1);
      return var2 != null ? new Long(var2.getNbOccurences()) : null;
   }

   public Boolean getFixedRate(Integer var1) {
      TimerNotification var2 = this.getNotification(var1);
      return var2 != null ? new Boolean(var2.isFixedRate()) : null;
   }

   public boolean getSendPastNotifications() {
      return this.sendPastNotifications;
   }

   public boolean isActive() {
      return this.isActive;
   }

   public synchronized boolean isEmpty() {
      return this.notificationCount == 0;
   }

   public MBeanNotificationInfo[] getNotificationInfo() {
      return new MBeanNotificationInfo[0];
   }

   public void setSendPastNotifications(boolean var1) {
      this.sendPastNotifications = var1;
   }

   public void start() {
      this.isActive = true;
      this.firePastNotifications();
   }

   public void stop() {
      this.isActive = false;
   }

   public Integer addNotification(String var1, String var2, Object var3, Date var4, long var5, long var7) throws IllegalArgumentException {
      return this.addNotification(var1, var2, var3, var4, var5, var7, false);
   }

   public Integer addNotification(String var1, String var2, Object var3, Date var4, long var5, long var7, boolean var9) throws IllegalArgumentException {
      if (var4 == null) {
         throw new IllegalArgumentException("Null notification date.");
      } else if (var5 < 0L) {
         throw new IllegalArgumentException("Negative period value: " + var5);
      } else if (var7 < 0L) {
         throw new IllegalArgumentException("Negative occurances: " + var7);
      } else {
         TimerNotification var10 = new TimerNotification(var1, var2, var3, var4, var5, var7, var9, this);
         weblogic.timers.Timer var11 = null;
         if (!var9) {
            var11 = this.timerManager.schedule(var10, var4, var5);
         } else {
            var11 = this.timerManager.scheduleAtFixedRate(var10, var4, var5);
         }

         var10.setTracker(var11);
         Integer var12 = this.getNextId();
         synchronized(this.allNotifications) {
            this.allNotifications.put(var12, var10);
            ++this.notificationCount;
            return var12;
         }
      }
   }

   public Integer addNotification(String var1, String var2, Object var3, Date var4, long var5) throws IllegalArgumentException {
      return this.addNotification(var1, var2, var3, var4, var5, 0L);
   }

   public Integer addNotification(String var1, String var2, Object var3, Date var4) throws IllegalArgumentException {
      return this.addNotification(var1, var2, var3, var4, 0L, 0L);
   }

   public void removeNotification(Integer var1) throws InstanceNotFoundException {
      synchronized(this.allNotifications) {
         TimerNotification var3 = (TimerNotification)this.allNotifications.remove(var1);
         if (var3 != null) {
            var3.getTracker().cancel();
            --this.notificationCount;
         } else {
            throw new InstanceNotFoundException("Notification with id=" + var1 + " could not be found");
         }
      }
   }

   public void removeNotifications(String var1) throws InstanceNotFoundException {
      Vector var2 = new Vector();
      synchronized(this.allNotifications) {
         Iterator var4 = this.allNotifications.keySet().iterator();

         while(var4.hasNext()) {
            Integer var5 = (Integer)var4.next();
            TimerNotification var6 = (TimerNotification)this.allNotifications.get(var5);
            if (var1.equals(var6.getType())) {
               var2.add(var5);
               var6.getTracker().cancel();
            }
         }

         if (var2.size() == 0) {
            throw new InstanceNotFoundException("No notification of type=" + var1 + " found");
         } else {
            for(int var9 = 0; var9 < var2.size(); ++var9) {
               this.allNotifications.remove(var2.get(var9));
               --this.notificationCount;
            }

         }
      }
   }

   public void removeAllNotifications() {
      synchronized(this.allNotifications) {
         Iterator var2 = this.allNotifications.keySet().iterator();

         while(var2.hasNext()) {
            TimerNotification var3 = (TimerNotification)this.allNotifications.get((Integer)var2.next());
            var3.getTracker().cancel();
         }

         this.allNotifications.clear();
         this.allNotifications = new HashMap(11);
         this.notificationCount = 0;
      }
   }

   public void addNotificationListener(NotificationListener var1, NotificationFilter var2, Object var3) throws IllegalArgumentException {
      synchronized(this.listenerLock) {
         if (this.listenerCount == this.allListeners.length) {
            int var5 = this.listenerCount == 0 ? 1 : this.listenerCount * 2;
            TimerListener[] var6 = new TimerListener[var5];
            System.arraycopy(this.allListeners, 0, var6, 0, this.listenerCount);
            this.allListeners = var6;
         }

         TimerListener var9 = new TimerListener(var1, var2, var3);
         this.allListeners[this.listenerCount] = var9;
         ++this.listenerCount;
      }
   }

   public void removeNotificationListener(NotificationListener var1) throws ListenerNotFoundException {
      synchronized(this.listenerLock) {
         for(int var3 = 0; var3 < this.listenerCount; ++var3) {
            if (this.allListeners[var3].getListener().equals(var1)) {
               --this.listenerCount;
               System.arraycopy(this.allListeners, var3 + 1, this.allListeners, var3, this.listenerCount - var3);
               this.allListeners[this.listenerCount] = null;
               break;
            }
         }

      }
   }

   public void deliverNotifications(Notification var1) {
      for(int var2 = 0; var2 < this.listenerCount; ++var2) {
         this.allListeners[var2].deliverNotification(var1);
      }

   }

   private synchronized Integer getNextId() {
      return new Integer(this.Id++);
   }

   private TimerNotification getNotification(Integer var1) {
      synchronized(this.allNotifications) {
         return (TimerNotification)this.allNotifications.get(var1);
      }
   }

   void addPastNotification(TimerNotification var1) {
      this.pastNotifications.add(var1);
   }

   private void firePastNotifications() {
      Iterator var1 = this.pastNotifications.iterator();

      while(var1.hasNext()) {
         TimerNotification var2 = (TimerNotification)var1.next();

         for(int var3 = var2.getPastNotificationCount(); var3 > 0; --var3) {
            this.deliverNotifications(var2.getNotificationObject());
         }

         var2.unsetPastNotificationCount();
      }

   }
}
