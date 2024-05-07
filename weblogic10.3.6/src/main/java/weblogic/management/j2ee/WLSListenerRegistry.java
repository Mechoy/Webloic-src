package weblogic.management.j2ee;

import java.io.Serializable;
import java.rmi.RemoteException;
import javax.management.InstanceNotFoundException;
import javax.management.ListenerNotFoundException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.j2ee.ListenerRegistration;

public class WLSListenerRegistry implements ListenerRegistration, Serializable {
   private ListenerRegistry service = null;

   public WLSListenerRegistry(ListenerRegistry var1) {
      this.service = var1;
   }

   public WLSListenerRegistry() {
   }

   public void addNotificationListener(ObjectName var1, NotificationListener var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException, RemoteException {
      this.service.addListener(var1, var2, var3, var4);
   }

   public void removeNotificationListener(ObjectName var1, NotificationListener var2) throws InstanceNotFoundException, ListenerNotFoundException, RemoteException {
      this.service.removeListener(var1, var2);
   }
}
