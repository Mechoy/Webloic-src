package weblogic.diagnostics.watch;

import com.bea.diagnostics.notifications.Notification;
import com.bea.diagnostics.notifications.NotificationSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WatchNotificationInternal extends WatchNotification implements Notification {
   static final long serialVersionUID = -2415851253529341071L;
   private static transient ArrayList<Object> watchKeySet;
   private transient Map<Object, Object> dataMap;
   private transient WatchNotificationSourceImpl source;
   private transient long watchTimeMillis;

   WatchNotificationInternal(Watch var1, long var2, Map var4) throws NotificationCreateException {
      if (var1 == null) {
         throw new NotificationCreateException("Watch can not be null");
      } else {
         this.watchTimeMillis = var2;
         this.dataMap = new HashMap();
         this.source = new WatchNotificationSourceImpl(var1.getWatchName());
         super.setWatchData(var4);
         super.setWatchDataString(WatchUtils.getWatchDataString(var4));
         WatchUtils.setWatchTimeInDateFormat(this, var2);
         WatchUtils.populateFromWatch(this, var1);
      }
   }

   WatchNotification createWatchNotificationExternal() {
      WatchNotification var1 = new WatchNotification();
      var1.setMessage(this.getMessage());
      var1.setWatchAlarmResetPeriod(this.getWatchAlarmResetPeriod());
      var1.setWatchAlarmType(this.getWatchAlarmType());
      var1.setWatchDataString(this.getWatchDataToString());
      var1.setWatchDomainName(this.getWatchDomainName());
      var1.setWatchName(this.getWatchName());
      var1.setWatchRule(this.getWatchRule());
      var1.setWatchRuleType(this.getWatchRuleType());
      var1.setWatchServerName(this.getWatchServerName());
      var1.setWatchSeverityLevel(this.getWatchSeverityLevel());
      var1.setWatchTime(this.getWatchTime());
      var1.setWatchData(this.getWatchData());
      return var1;
   }

   public NotificationSource getSource() {
      return this.source;
   }

   public void setSource(NotificationSource var1) {
      throw new UnsupportedOperationException();
   }

   public Object getValue(Object var1) {
      return this.dataMap.get(var1);
   }

   public void setValue(Object var1, Object var2) {
      this.dataMap.put(var1, var2);
   }

   protected Map getData() {
      return this.dataMap;
   }

   public List keyList() {
      synchronized(this) {
         if (watchKeySet == null) {
            watchKeySet = new ArrayList(10);
            watchKeySet.add("WatchName");
            watchKeySet.add("WatchDomainName");
            watchKeySet.add("WatchServerName");
            watchKeySet.add("WatchRuleType");
            watchKeySet.add("WatchRule");
            watchKeySet.add("WatchTime");
            watchKeySet.add("WatchSeverityLevel");
            watchKeySet.add("WatchData");
            watchKeySet.add("WatchAlarmType");
            watchKeySet.add("WatchAlarmResetPeriod");
         }
      }

      return watchKeySet;
   }

   protected long getWatchTimeMillis() {
      return this.watchTimeMillis;
   }

   private static class WatchNotificationSourceImpl implements NotificationSource {
      static final long serialVersionUID = 8733358832295734061L;
      private String watchName;

      public WatchNotificationSourceImpl() {
      }

      public WatchNotificationSourceImpl(String var1) {
         this.watchName = var1;
      }

      public String getName() {
         return this.watchName;
      }

      public void setName(String var1) {
         throw new UnsupportedOperationException();
      }
   }
}
