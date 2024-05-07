package weblogic.management.mbeanservers.internal.utils.typing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.management.InstanceNotFoundException;
import javax.management.JMException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerNotification;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;

public class MBeanTypeUtil {
   private MBeanServerConnection mbs;
   private MBeanCategorizer categorizer;
   private Map notificationListeners = Collections.synchronizedMap(new HashMap());
   private static final String MBEAN_HARVESTABLE_TYPE_NAME = "DiagnosticTypeName";
   private UpdateTracker updateTracker = new UpdateTracker();

   public MBeanTypeUtil(MBeanServerConnection var1, MBeanCategorizer var2) throws JMException {
      this.mbs = var1;
      this.categorizer = var2;
   }

   public void addRegistrationHandler(final RegHandler var1) throws JMException, IOException {
      final ArrayList var2 = new ArrayList();
      final ArrayList var3 = new ArrayList();
      NotificationListener var4 = new NotificationListener() {
         public void handleNotification(Notification var1x, Object var2x) {
            MBeanServerNotification var3x = (MBeanServerNotification)var1x;
            ObjectName var4 = var3x.getMBeanName();
            String var5 = var3x.getType();
            if (MBeanTypeUtil.this.updateTracker.initDone) {
               MBeanTypeUtil.this.callbackNow(var5, var4, var1);
            } else {
               synchronized(MBeanTypeUtil.this.updateTracker) {
                  if (MBeanTypeUtil.this.updateTracker.initDone) {
                     MBeanTypeUtil.this.callbackNow(var5, var4, var1);
                  } else if (var5.equals("JMX.mbean.registered")) {
                     var3.add(var4);
                  } else if (var5.equals("JMX.mbean.unregistered")) {
                     var2.add(var4);
                  }
               }
            }

         }
      };
      this.mbs.addNotificationListener(new ObjectName("JMImplementation:type=MBeanServerDelegate"), var4, new MyNotificationFilter(), (Object)null);
      this.notificationListeners.put(var1, var4);
      Set var5 = this.mbs.queryNames(new ObjectName("*:*"), this.categorizer != null ? new QueryFilter(this.categorizer) : null);
      synchronized(this.updateTracker) {
         Iterator var7 = var3.iterator();

         ObjectName var8;
         while(var7.hasNext()) {
            var8 = (ObjectName)var7.next();
            var5.add(var8);
         }

         var7 = var2.iterator();

         while(var7.hasNext()) {
            var8 = (ObjectName)var7.next();
            if (!this.mbs.isRegistered(var8)) {
               var5.remove(var8);
            }
         }

         var7 = var5.iterator();

         while(var7.hasNext()) {
            var8 = (ObjectName)var7.next();
            this.newInstance(var8, var1);
         }

         this.updateTracker.initDone = true;
      }
   }

   public void removeRegistrationHandler(RegHandler var1) throws InstanceNotFoundException, ListenerNotFoundException, MalformedObjectNameException, IOException {
      NotificationListener var2 = (NotificationListener)this.notificationListeners.get(var1);
      if (var1 != null) {
         this.mbs.removeNotificationListener(new ObjectName("JMImplementation:type=MBeanServerDelegate"), var2);
      }

   }

   private void callbackNow(String var1, ObjectName var2, RegHandler var3) {
      if (var1.equals("JMX.mbean.registered")) {
         try {
            this.newInstance(var2, var3);
         } catch (JMException var7) {
            throw new RuntimeException(var7);
         } catch (IOException var8) {
            throw new RuntimeException(var8);
         }
      } else if (var1.equals("JMX.mbean.unregistered")) {
         try {
            this.deletedInstance(var2, var3);
         } catch (JMException var5) {
            throw new RuntimeException(var5);
         } catch (IOException var6) {
            throw new RuntimeException(var6);
         }
      }

   }

   private void newInstance(ObjectName var1, RegHandler var2) throws JMException, IOException {
      MBeanCategorizer.TypeInfo var3 = this.categorizer.getTypeInfo(this.mbs, var1);
      if (var3 != null) {
         String var4 = var3.getTypeName();
         if (var4 != null) {
            try {
               var2.newInstance(var4, var1.getCanonicalName(), var3.getCategoryName());
            } catch (RuntimeException var6) {
               if (var6.getCause() == null || !(var6.getCause() instanceof InstanceNotFoundException)) {
                  throw var6;
               }
            } catch (Exception var7) {
               throw new RuntimeException(var7);
            }
         }

      }
   }

   private void deletedInstance(ObjectName var1, RegHandler var2) throws JMException, IOException {
      try {
         var2.instanceDeleted(var1.getCanonicalName());
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new RuntimeException(var5);
      }
   }

   public String[] getAttributes(String var1) throws JMException {
      return null;
   }

   public interface RegHandler {
      void newInstance(String var1, String var2, String var3) throws Exception;

      void instanceDeleted(String var1) throws Exception;
   }

   private class MBSListener implements NotificationListener {
      public void handleNotification(Notification var1, Object var2) {
      }
   }

   private class UpdateTracker {
      boolean initDone;

      private UpdateTracker() {
         this.initDone = false;
      }

      // $FF: synthetic method
      UpdateTracker(Object var2) {
         this();
      }
   }
}
