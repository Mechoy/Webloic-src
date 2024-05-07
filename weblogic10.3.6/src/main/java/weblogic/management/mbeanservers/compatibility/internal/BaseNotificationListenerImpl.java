package weblogic.management.mbeanservers.compatibility.internal;

import javax.management.InstanceNotFoundException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import weblogic.management.ManagementLogger;
import weblogic.management.configuration.ConfigurationError;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.utils.Debug;

public class BaseNotificationListenerImpl implements BaseNotificationListener {
   private MBeanServer server = null;
   private MBeanProxy proxy = null;
   private NotificationListener listener = null;

   BaseNotificationListenerImpl(MBeanProxy var1, NotificationListener var2) {
      this.proxy = var1;
      this.listener = var2;

      try {
         this.server = this.proxy.getMBeanHome().getMBeanServer();
         Debug.assertion(this.proxy.getObjectName() != null);
      } catch (RemoteRuntimeException var4) {
         throw new ConfigurationError(var4);
      }
   }

   public ObjectName getObjectName() {
      return this.proxy.getObjectName();
   }

   public NotificationListener getListener() {
      return this.listener;
   }

   public void addFilterAndHandback(NotificationFilter var1, Object var2) {
      try {
         this.server.addNotificationListener(this.proxy.getObjectName(), this, var1, var2);
      } catch (InstanceNotFoundException var4) {
         ManagementLogger.logAddFilterFailed(var4);
      }

   }

   public void remove() {
      try {
         this.server.removeNotificationListener(this.proxy.getObjectName(), this);
      } catch (InstanceNotFoundException var2) {
         ManagementLogger.logRemoveNotificationFailed(var2);
      } catch (ListenerNotFoundException var3) {
         ManagementLogger.logRemoveNotificationFailed(var3);
      }

      this.listener = null;
   }

   public void handleNotification(Notification var1, Object var2) {
      NotificationListener var3 = this.listener;
      if (var3 != null) {
         this.proxy.sendNotification(var3, var1, var2);
      }

   }

   public void unregister() {
      try {
         this.server.removeNotificationListener(this.proxy.getObjectName(), this);
      } catch (ListenerNotFoundException var2) {
         ManagementLogger.logUnregisterNotificationFailed(var2);
      } catch (InstanceNotFoundException var3) {
         ManagementLogger.logUnregisterNotificationFailed(var3);
      }

   }

   public String toString() {
      return "RNL [" + this.proxy + "]";
   }
}
