package weblogic.xml.util.cache.entitycache;

import java.util.Enumeration;
import java.util.Vector;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;

class StatisticsMonitor implements TimerListener {
   EntityCache cache = null;
   Vector subjects = new Vector();
   long diskWriteInterval = 0L;
   Timer timer;

   StatisticsMonitor(EntityCache var1, long var2) {
      this.cache = var1;
      this.diskWriteInterval = var2;
   }

   void addSubject(Statistics var1, EntityCacheStats var2) {
      StatisticsMonitorSubject var3 = new StatisticsMonitorSubject(var1, var2);
      this.subjects.addElement(var3);
   }

   void addMBean(Statistics var1, EntityCacheStats var2) {
      Enumeration var3 = this.subjects.elements();

      while(var3.hasMoreElements()) {
         StatisticsMonitorSubject var4 = (StatisticsMonitorSubject)var3.nextElement();
         if (var4.stats == var1) {
            var4.mBean = var2;
            break;
         }
      }

   }

   void setDiskWriteInterval(long var1) {
      this.diskWriteInterval = var1;
   }

   public void timerExpired(Timer var1) {
      try {
         Enumeration var2 = this.subjects.elements();

         while(var2.hasMoreElements()) {
            StatisticsMonitorSubject var3 = (StatisticsMonitorSubject)var2.nextElement();
            var3.stats.save();
            if (var3.mBean != null) {
               var3.mBean.doNotifications();
            }
         }

         this.cache.statsCumulativeModification = false;
         this.cache.statsCurrentModification = false;
      } catch (Exception var4) {
      }

   }

   public void start() {
      this.timer = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(this, this.diskWriteInterval, this.diskWriteInterval);
   }

   public void finish() {
      if (this.timer != null) {
         this.timer.cancel();
      }

   }
}
