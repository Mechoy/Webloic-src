package weblogic.diagnostics.watch;

import java.security.AccessController;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.WLDFWatchJMXNotificationRuntimeMBean;
import weblogic.management.runtime.WLDFWatchNotificationRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class WatchNotificationRuntimeMBeanImpl extends RuntimeMBeanDelegate implements WLDFWatchNotificationRuntimeMBean {
   private long totalHarvesterEvaluationCycles;
   private long totalHarvesterWatchEvaluations;
   private long totalHarvesterWatchesTriggered;
   private long totalHarvesterWatchEvaluationTime;
   private long minHarvesterWatchEvaluationTime;
   private long maxHarvesterWatchEvaluationTime;
   private long totalLogEvaluationCycles;
   private long totalLogWatchEvaluations;
   private long totalLogWatchesTriggered;
   private long totalLogWatchEvaluationTime;
   private long minLogWatchEvaluationTime;
   private long maxLogWatchEvaluationTime;
   private long totalEventDataEvaluationCycles;
   private long totalEventDataWatchEvaluations;
   private long totalEventDataWatchesTriggered;
   private long totalEventDataWatchEvaluationTime;
   private long minEventDataWatchEvaluationTime;
   private long maxEventDataWatchEvaluationTime;
   private int currentActiveAlarmsCount;
   private int maximumActiveAlarmsCount;
   private long totalActiveManualResetAlarms;
   private long totalActiveAutomaticResetAlarms;
   private long totalNotificationsPerformed;
   private long totalFailedNotifications;
   private long totalJMXNotificationsPerformed;
   private long totalFailedJMXNotifications;
   private long totalSMTPNotificationsPerformed;
   private long totalFailedSMTPNotifications;
   private long totalDIMGNotificationsPerformed;
   private long totalFailedDIMGNotifications;
   private long totalSNMPNotificationsPerformed;
   private long totalFailedSNMPNotifications;
   private long totalFailedJMSNotifications;
   private long totalJMSNotificationsPerformed;
   private WLDFWatchJMXNotificationRuntimeMBean notificationProducer;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   static WatchNotificationRuntimeMBeanImpl getInstance() throws ManagementException {
      return WatchNotificationRuntimeMBeanImpl.WatchNotificationRuntimeMBeanFactory.getSingleton();
   }

   private WatchNotificationRuntimeMBeanImpl() throws ManagementException {
      super("WatchNotification", ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getWLDFRuntime());
      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getWLDFRuntime().setWLDFWatchNotificationRuntime(this);
   }

   public long getTotalHarvesterEvaluationCycles() {
      return this.totalHarvesterEvaluationCycles;
   }

   public long getTotalHarvesterWatchEvaluations() {
      return this.totalHarvesterWatchEvaluations;
   }

   public long getTotalHarvesterWatchesTriggered() {
      return this.totalHarvesterWatchesTriggered;
   }

   public long getAverageHarvesterWatchEvaluationTime() {
      return this.totalHarvesterEvaluationCycles > 0L ? this.totalHarvesterWatchEvaluationTime / this.totalHarvesterEvaluationCycles : 0L;
   }

   public long getTotalLogEvaluationCycles() {
      return this.totalLogEvaluationCycles;
   }

   public long getTotalLogWatchEvaluations() {
      return this.totalLogWatchEvaluations;
   }

   public long getTotalLogWatchesTriggered() {
      return this.totalLogWatchesTriggered;
   }

   public long getAverageLogWatchEvaluationTime() {
      return this.totalLogEvaluationCycles > 0L ? this.totalLogWatchEvaluationTime / this.totalLogEvaluationCycles : 0L;
   }

   public long getTotalEventDataEvaluationCycles() {
      return this.totalEventDataEvaluationCycles;
   }

   public long getTotalEventDataWatchEvaluations() {
      return this.totalEventDataWatchEvaluations;
   }

   public long getTotalEventDataWatchesTriggered() {
      return this.totalEventDataWatchesTriggered;
   }

   public long getAverageEventDataWatchEvaluationTime() {
      return this.totalEventDataEvaluationCycles > 0L ? this.totalEventDataWatchEvaluationTime / this.totalEventDataEvaluationCycles : 0L;
   }

   public int getCurrentActiveAlarmsCount() {
      return this.currentActiveAlarmsCount;
   }

   public int getMaximumActiveAlarmsCount() {
      return this.maximumActiveAlarmsCount;
   }

   public long getTotalActiveManualResetAlarms() {
      return this.totalActiveManualResetAlarms;
   }

   public long getTotalActiveAutomaticResetAlarms() {
      return this.totalActiveAutomaticResetAlarms;
   }

   public long getTotalNotificationsPerformed() {
      return this.totalNotificationsPerformed;
   }

   public String[] getActiveAlarmWatches() throws ManagementException {
      Watch[] var1 = WatchManager.getInstance().getActiveAlarmWatches();
      if (var1 == null) {
         return new String[0];
      } else {
         String[] var2 = new String[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = var1[var3].getWatchName();
         }

         return var2;
      }
   }

   public void resetWatchAlarm(String var1) throws ManagementException {
      try {
         WatchManager.getInstance().resetWatchAlarm(var1);
      } catch (WatchException var3) {
         throw new ManagementException(var3);
      }
   }

   public WLDFWatchJMXNotificationRuntimeMBean getWLDFWatchJMXNotificationRuntime() {
      return this.notificationProducer;
   }

   void incrementTotalHarvesterEvaluationCycles() {
      ++this.totalHarvesterEvaluationCycles;
   }

   void incrementTotalHarvesterWatchEvaluations(int var1) {
      this.totalHarvesterWatchEvaluations += (long)var1;
   }

   void incrementTotalHarvesterWatchesTriggered() {
      ++this.totalHarvesterWatchesTriggered;
   }

   void incrementTotalHarvesterWatchEvaluationTime(long var1) {
      this.totalHarvesterWatchEvaluationTime += var1;
      this.minHarvesterWatchEvaluationTime = Math.min(this.minHarvesterWatchEvaluationTime, var1);
      this.maxHarvesterWatchEvaluationTime = Math.max(this.maxHarvesterWatchEvaluationTime, var1);
   }

   void incrementTotalLogEvaluationCycles() {
      ++this.totalLogEvaluationCycles;
   }

   void incrementTotalLogWatchEvaluations(int var1) {
      this.totalLogWatchEvaluations += (long)var1;
   }

   void incrementTotalLogWatchesTriggered() {
      ++this.totalLogWatchesTriggered;
   }

   void incrementTotalLogWatchEvaluationTime(long var1) {
      this.totalLogWatchEvaluationTime += var1;
      this.minLogWatchEvaluationTime = Math.min(this.minLogWatchEvaluationTime, var1);
      this.maxLogWatchEvaluationTime = Math.max(this.maxLogWatchEvaluationTime, var1);
   }

   void incrementTotalEventDataEvaluationCycles() {
      ++this.totalEventDataEvaluationCycles;
   }

   void incrementTotalEventDataWatchEvaluations(int var1) {
      this.totalEventDataWatchEvaluations += (long)var1;
   }

   void incrementTotalEventDataWatchesTriggered() {
      ++this.totalEventDataWatchesTriggered;
   }

   void incrementTotalEventDataWatchEvaluationTime(long var1) {
      this.totalEventDataWatchEvaluationTime += var1;
      this.minEventDataWatchEvaluationTime = Math.min(this.minEventDataWatchEvaluationTime, var1);
      this.maxEventDataWatchEvaluationTime = Math.max(this.maxEventDataWatchEvaluationTime, var1);
   }

   void setCurrentActiveAlarmsCount(int var1) {
      this.currentActiveAlarmsCount = var1;
      this.maximumActiveAlarmsCount = Math.max(this.maximumActiveAlarmsCount, var1);
   }

   void incrementTotalActiveManualResetAlarms() {
      ++this.totalActiveManualResetAlarms;
   }

   void incrementTotalActiveAutomaticResetAlarms() {
      ++this.totalActiveAutomaticResetAlarms;
   }

   synchronized void incrementTotalNotificationsPerformed() {
      ++this.totalNotificationsPerformed;
   }

   synchronized void incrementTotalFailedNotifications() {
      ++this.totalFailedNotifications;
   }

   public long getTotalFailedNotifications() {
      return this.totalFailedNotifications;
   }

   synchronized void setWatchJMXNotificationRuntime(WLDFWatchJMXNotificationRuntimeMBean var1) {
      this.notificationProducer = var1;
   }

   public long getMinimumHarvesterWatchEvaluationTime() {
      return this.minHarvesterWatchEvaluationTime;
   }

   public long getMaximumHarvesterWatchEvaluationTime() {
      return this.maxHarvesterWatchEvaluationTime;
   }

   public long getMinimumLogWatchEvaluationTime() {
      return this.minLogWatchEvaluationTime;
   }

   public long getMaximumLogWatchEvaluationTime() {
      return this.maxLogWatchEvaluationTime;
   }

   public long getMinimumEventDataWatchEvaluationTime() {
      return this.minEventDataWatchEvaluationTime;
   }

   public long getMaximumEventDataWatchEvaluationTime() {
      return this.maxEventDataWatchEvaluationTime;
   }

   void incrementTotalDIMGNotificationsPerformed() {
      ++this.totalDIMGNotificationsPerformed;
      this.incrementTotalNotificationsPerformed();
   }

   public long getTotalDIMGNotificationsPerformed() {
      return this.totalDIMGNotificationsPerformed;
   }

   void incrementTotalFailedDIMGNotifications() {
      ++this.totalFailedDIMGNotifications;
      this.incrementTotalFailedNotifications();
   }

   public long getTotalFailedDIMGNotifications() {
      return this.totalFailedDIMGNotifications;
   }

   void incrementTotalJMXNotificationsPerformed() {
      ++this.totalJMXNotificationsPerformed;
      this.incrementTotalNotificationsPerformed();
   }

   public long getTotalJMXNotificationsPerformed() {
      return this.totalJMXNotificationsPerformed;
   }

   void incrementTotalFailedJMXNotifications() {
      ++this.totalFailedJMXNotifications;
      this.incrementTotalFailedNotifications();
   }

   public long getTotalFailedJMXNotifications() {
      return this.totalFailedJMXNotifications;
   }

   void incrementTotalSMTPNotificationsPerformed() {
      ++this.totalSMTPNotificationsPerformed;
      this.incrementTotalNotificationsPerformed();
   }

   public long getTotalSMTPNotificationsPerformed() {
      return this.totalSMTPNotificationsPerformed;
   }

   void incrementTotalFailedSMTPNotifications() {
      ++this.totalFailedSMTPNotifications;
      this.incrementTotalFailedNotifications();
   }

   public long getTotalFailedSMTPNotifications() {
      return this.totalFailedSMTPNotifications;
   }

   void incrementTotalSNMPNotificationsPerformed() {
      ++this.totalSNMPNotificationsPerformed;
      this.incrementTotalNotificationsPerformed();
   }

   public long getTotalSNMPNotificationsPerformed() {
      return this.totalSNMPNotificationsPerformed;
   }

   void incrementTotalFailedSNMPNotifications() {
      ++this.totalFailedSNMPNotifications;
      this.incrementTotalFailedNotifications();
   }

   public long getTotalFailedSNMPNotifications() {
      return this.totalFailedSNMPNotifications;
   }

   void incrementTotalJMSNotificationsPerformed() {
      ++this.totalJMSNotificationsPerformed;
      this.incrementTotalNotificationsPerformed();
   }

   public long getTotalJMSNotificationsPerformed() {
      return this.totalJMSNotificationsPerformed;
   }

   void incrementTotalFailedJMSNotifications() {
      ++this.totalFailedJMSNotifications;
      this.incrementTotalFailedNotifications();
   }

   public long getTotalFailedJMSNotifications() {
      return this.totalFailedJMSNotifications;
   }

   // $FF: synthetic method
   WatchNotificationRuntimeMBeanImpl(Object var1) throws ManagementException {
      this();
   }

   static class WatchNotificationRuntimeMBeanFactory {
      private static WatchNotificationRuntimeMBeanImpl SINGLETON = null;

      private static WatchNotificationRuntimeMBeanImpl getSingleton() throws ManagementException {
         if (SINGLETON == null) {
            SINGLETON = new WatchNotificationRuntimeMBeanImpl();
            JMXNotificationProducer.getInstance();
         }

         return SINGLETON;
      }
   }
}
