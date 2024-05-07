package weblogic.diagnostics.watch;

import com.bea.adaptive.harvester.WatchedValues;
import com.bea.diagnostics.notifications.InvalidNotificationException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.logging.Severities;

public final class WatchConfiguration {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticWatch");
   private Map<String, WatchNotificationListener> enabledNotifications = new Hashtable();
   private Map<String, WatchNotificationListener> disabledNotifications = new Hashtable();
   private Map<String, Watch> allWatches = new Hashtable();
   private ArrayList<Watch> enabledHarvesterWatches = new ArrayList();
   private ArrayList<Watch> enabledLogWatches = new ArrayList();
   private ArrayList<Watch> enabledEventDataWatches = new ArrayList();
   private int logEventHandlerSeverity = 16;
   private WatchedValues watchedValues;
   private boolean watchNotificationEnabled = true;
   JMXNotificationProducer notificationProducer = null;

   public WatchConfiguration(WatchedValues var1) {
      this.watchedValues = var1;
   }

   WatchConfiguration() {
   }

   boolean isWatchNotificationEnabled() {
      return this.watchNotificationEnabled;
   }

   void setJMXNotificationProducer(JMXNotificationProducer var1) {
      this.notificationProducer = var1;
   }

   void setWatchNotificationEnabled(boolean var1) {
      this.watchNotificationEnabled = var1;
   }

   public void addWatch(Watch var1) {
      if (var1 == null) {
         throw new InvalidWatchException("Watch can not be null");
      } else {
         String var2 = var1.getWatchName();
         if (var2 == null) {
            throw new InvalidWatchException("Watch name can not be null");
         } else {
            Watch var3 = (Watch)this.allWatches.get(var2);
            if (var3 != null) {
               DiagnosticsLogger.logDuplicateWatch(var2);
            } else {
               this.allWatches.put(var2, var1);
               if (var1.isEnabled()) {
                  this.addEnabledWatch(var1);
               }

               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Added watch " + var2);
               }

            }
         }
      }
   }

   public Watch getWatch(String var1) throws WatchNotFoundException {
      if (var1 == null) {
         throw new WatchNotFoundException("Watch name can not be null");
      } else {
         Watch var2 = (Watch)this.allWatches.get(var1);
         if (var2 != null) {
            return var2;
         } else {
            throw new WatchNotFoundException("Watch " + var1 + " is not a registered watch");
         }
      }
   }

   public boolean isWatchEnabled(String var1) throws WatchNotFoundException {
      Watch var2 = this.getWatch(var1);
      return var2.isEnabled();
   }

   public boolean isWatchDisabled(String var1) throws WatchNotFoundException {
      Watch var2 = this.getWatch(var1);
      return var2.isDisabled();
   }

   public boolean isWatchAlarmActive(String var1) throws WatchNotFoundException {
      Watch var2 = this.getWatch(var1);
      return var2.isAlarm();
   }

   public Watch[] getWatches() {
      Watch[] var1 = new Watch[this.allWatches.size()];
      this.allWatches.values().toArray(var1);
      return var1;
   }

   public ArrayList getEnabledHarvesterWatches() {
      return this.enabledHarvesterWatches;
   }

   public ArrayList getEnabledLogWatches() {
      return this.enabledLogWatches;
   }

   public ArrayList getEnabledEventDataWatches() {
      return this.enabledEventDataWatches;
   }

   private synchronized void addEnabledWatch(Watch var1) {
      var1.setEnabled();
      switch (var1.getRuleType()) {
         case 1:
            this.enabledLogWatches.add(var1);
            break;
         case 2:
            this.enabledHarvesterWatches.add(var1);
            break;
         case 3:
            this.enabledEventDataWatches.add(var1);
            break;
         default:
            throw new AssertionError("Unknown rule type" + var1.getRuleType());
      }

   }

   public void addNotificationListener(WatchNotificationListener var1) {
      if (var1 == null) {
         throw new InvalidNotificationException("Notification can not be null");
      } else {
         String var2 = var1.getNotificationName();
         if (var2 == null) {
            throw new InvalidNotificationException("Notification name can not be null");
         } else {
            WatchNotificationListener var3 = (WatchNotificationListener)this.enabledNotifications.get(var2);
            if (var3 == null) {
               var3 = (WatchNotificationListener)this.disabledNotifications.get(var2);
            }

            if (var3 != null) {
               DiagnosticsLogger.logDuplicateNotification(var1.getNotificationName());
            } else {
               if (var1.isEnabled()) {
                  this.enabledNotifications.put(var2, var1);
               } else {
                  this.disabledNotifications.put(var2, var1);
               }

               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Added notification " + var1);
               }

            }
         }
      }
   }

   public synchronized void removeNotification(String var1) throws NotificationNotFoundException, NotificationInUseException {
      WatchNotificationListener var2 = this.getNotification(var1);
      if (var2.isEnabled()) {
         this.enabledNotifications.remove(var1);
      } else {
         this.disabledNotifications.remove(var1);
      }

      Watch[] var3 = this.getWatches();

      for(int var4 = 0; var3 != null && var4 < var3.length; ++var4) {
         String[] var5 = var3[var4].getNotifications();

         for(int var6 = 0; var5 != null && var6 < var5.length; ++var6) {
            if (var1.equals(var5[var6])) {
               throw new NotificationInUseException("Notification " + var1 + " can not be deleted as it is used by watch " + var3[var4].getWatchName() + ". Remove the notification from the watch.");
            }
         }
      }

      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Removed notification " + var1);
      }

   }

   public synchronized WatchNotificationListener getNotification(String var1) throws NotificationNotFoundException {
      if (var1 == null) {
         throw new NotificationNotFoundException("Notification name can not be null");
      } else {
         WatchNotificationListener var2 = (WatchNotificationListener)this.enabledNotifications.get(var1);
         if (var2 == null) {
            var2 = (WatchNotificationListener)this.disabledNotifications.get(var1);
         }

         if (var2 == null) {
            throw new NotificationNotFoundException("Notification " + var1 + " has is not a registered notification");
         } else {
            return var2;
         }
      }
   }

   void enableNotification(String var1) throws NotificationNotFoundException, NotificationAlreadyEnabledException {
      WatchNotificationListener var2 = this.getNotification(var1);
      if (var2.isEnabled()) {
         throw new NotificationAlreadyEnabledException("Notification " + var1 + " is already enabled");
      } else {
         this.disabledNotifications.remove(var1);
         var2.setEnabled();
         this.enabledNotifications.put(var1, var2);
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Enabled notification " + var1);
         }

      }
   }

   void disableNotification(String var1) throws NotificationNotFoundException, NotificationAlreadyDisabledException {
      WatchNotificationListener var2 = this.getNotification(var1);
      if (var2.isDisabled()) {
         throw new NotificationAlreadyDisabledException("Notification " + var1 + " is already disabled");
      } else {
         this.enabledNotifications.remove(var1);
         var2.setDisabled();
         this.disabledNotifications.put(var1, var2);
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Disabled notification " + var1);
         }

      }
   }

   public boolean isNotificationEnabled(String var1) throws NotificationNotFoundException {
      WatchNotificationListener var2 = this.getNotification(var1);
      return var2.isEnabled();
   }

   public boolean isNotificationDisabled(String var1) throws NotificationNotFoundException {
      WatchNotificationListener var2 = this.getNotification(var1);
      return var2.isDisabled();
   }

   public WatchNotificationListener[] getNotifications() {
      int var1 = this.enabledNotifications.size() + this.disabledNotifications.size();
      WatchNotificationListener[] var2 = new WatchNotificationListener[var1];
      WatchNotificationListener[] var3 = new WatchNotificationListener[this.disabledNotifications.size()];
      this.enabledNotifications.values().toArray(var2);
      System.arraycopy(this.disabledNotifications.values().toArray(var3), 0, var2, this.enabledNotifications.size(), this.disabledNotifications.size());
      return var2;
   }

   public WatchNotificationListener[] getEnabledNotifications() {
      WatchNotificationListener[] var1 = new WatchNotificationListener[this.enabledNotifications.size()];
      this.enabledNotifications.values().toArray(var1);
      return var1;
   }

   public WatchNotificationListener[] getDisabledNotifications() {
      WatchNotificationListener[] var1 = new WatchNotificationListener[this.disabledNotifications.size()];
      this.disabledNotifications.values().toArray(var1);
      return var1;
   }

   public int getEventHandlerSeverity() {
      return this.logEventHandlerSeverity;
   }

   public void initializeLogEventHandlerSeverity(String var1) {
      this.logEventHandlerSeverity = Severities.severityStringToNum(var1);
   }

   WatchedValues getWatchedValues() {
      return this.watchedValues;
   }
}
