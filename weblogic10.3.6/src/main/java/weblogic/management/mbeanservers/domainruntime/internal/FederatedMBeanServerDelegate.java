package weblogic.management.mbeanservers.domainruntime.internal;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.management.InstanceNotFoundException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerDelegate;
import javax.management.MBeanServerNotification;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.QueryExp;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.mbeanservers.internal.LocalNotificationHandback;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class FederatedMBeanServerDelegate extends MBeanServerDelegate {
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJMXDomain");
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   MBeanServerConnectionManager connectionManager;
   MBeanServerDelegate wrappedDelegate;
   int registeredListeners;
   boolean managingListeners;
   private final Map<String, FederatedNotificationListener> listenersByServerMap = new ConcurrentHashMap();
   private WeakHashMap<NotificationListener, Object> localListeners = new WeakHashMap();
   static final ObjectName ALLMBEANS;
   static final ObjectName MBEANSERVERDELEGATE;

   public FederatedMBeanServerDelegate(MBeanServerDelegate var1, MBeanServerConnectionManager var2) {
      this.wrappedDelegate = var1;
      this.connectionManager = var2;
      var2.addCallback(this.createConnectionListener());
   }

   private MBeanServerConnectionManager.MBeanServerConnectionListener createConnectionListener() {
      return new MBeanServerConnectionManager.MBeanServerConnectionListener() {
         public void connect(final String var1, final MBeanServerConnection var2) {
            FederatedMBeanServerDelegate.kernelId.doAs(FederatedMBeanServerDelegate.kernelId, new PrivilegedAction() {
               public Object run() {
                  _connect(var1, var2);
                  return null;
               }
            });
         }

         private void _connect(String var1, MBeanServerConnection var2) {
            try {
               if (FederatedMBeanServerDelegate.debug.isDebugEnabled()) {
                  FederatedMBeanServerDelegate.debug.debug("MBeanServerDelegate: Querying Managed Server");
               }

               if (FederatedMBeanServerDelegate.this.isManagingListeners()) {
                  if (FederatedMBeanServerDelegate.debug.isDebugEnabled()) {
                     FederatedMBeanServerDelegate.debug.debug("MBeanServerDelegate: There are registered listeners");
                  }

                  Set var3 = var2.queryNames(FederatedMBeanServerDelegate.ALLMBEANS, (QueryExp)null);
                  Iterator var4 = var3.iterator();
                  FederatedNotificationListener var5 = FederatedMBeanServerDelegate.this.createRegistrationListener(var2);
                  FederatedMBeanServerDelegate.this.listenersByServerMap.put(var1, var5);

                  while(var4.hasNext()) {
                     ObjectName var6 = (ObjectName)var4.next();
                     MBeanServerNotification var7 = new MBeanServerNotification("JMX.mbean.registered", FederatedMBeanServerDelegate.MBEANSERVERDELEGATE, 0L, var6);
                     FederatedMBeanServerDelegate.this.sendNotification(var7);
                     var5.addRegisteredObject(var6);
                  }

                  try {
                     var2.addNotificationListener(FederatedMBeanServerDelegate.MBEANSERVERDELEGATE, var5, (NotificationFilter)null, (Object)null);
                  } catch (InstanceNotFoundException var8) {
                     throw new Error(var8);
                  }
               }
            } catch (IOException var9) {
               if (FederatedMBeanServerDelegate.debug.isDebugEnabled()) {
                  FederatedMBeanServerDelegate.debug.debug("Failed Connection to Managed MBean Server ", var9);
               }
            }

         }

         public void disconnect(String var1) {
            if (FederatedMBeanServerDelegate.this.hasRegisteredListeners()) {
               FederatedNotificationListener var2 = (FederatedNotificationListener)FederatedMBeanServerDelegate.this.listenersByServerMap.remove(var1);
               if (var2 != null) {
                  var2.unregisterAll();
               }
            }

         }
      };
   }

   private FederatedNotificationListener createRegistrationListener(final MBeanServerConnection var1) {
      return new FederatedNotificationListener() {
         Set registeredObjectNames = Collections.synchronizedSet(new HashSet(1024));
         Set unmatchedObjectNames = Collections.synchronizedSet(new HashSet(1024));
         int numNotifications = 0;
         final MBeanServerConnection connection = var1;
         static final int REFRESH_LEVEL = 10000;

         public void handleNotification(Notification var1x, Object var2) {
            if (FederatedMBeanServerDelegate.debug.isDebugEnabled()) {
               FederatedMBeanServerDelegate.debug.debug("MBeanServerDelegate: handleNotification" + var1x);
            }

            if (var1x instanceof MBeanServerNotification) {
               MBeanServerNotification var3 = (MBeanServerNotification)var1x;
               String var4 = var3.getType();
               if (var4.equals("JMX.mbean.registered")) {
                  if (this.unmatchedObjectNames.remove(var3.getMBeanName())) {
                     if (FederatedMBeanServerDelegate.debug.isDebugEnabled()) {
                        FederatedMBeanServerDelegate.debug.debug("Skipping registration for previous unregistered notifications " + var3.getMBeanName());
                     }
                  } else {
                     ++this.numNotifications;
                     this.registeredObjectNames.add(var3.getMBeanName());
                     if (FederatedMBeanServerDelegate.debug.isDebugEnabled()) {
                        FederatedMBeanServerDelegate.debug.debug("Registered " + var3.getMBeanName());
                     }
                  }
               } else if (var4.equals("JMX.mbean.unregistered")) {
                  if (this.registeredObjectNames.remove(var3.getMBeanName())) {
                     --this.numNotifications;
                  } else {
                     this.unmatchedObjectNames.add(var3.getMBeanName());
                  }

                  if (FederatedMBeanServerDelegate.debug.isDebugEnabled()) {
                     FederatedMBeanServerDelegate.debug.debug("Unregistered " + var3.getMBeanName());
                  }

                  try {
                     this.connection.removeNotificationListener(var3.getMBeanName(), this);
                  } catch (Exception var8) {
                  }
               }
            }

            var1x.setSequenceNumber(0L);
            FederatedMBeanServerDelegate.this.sendNotification(var1x);
            if (this.numNotifications > 10000) {
               synchronized(this.registeredObjectNames) {
                  if (this.numNotifications > 10000) {
                     this.numNotifications = 0;
                     this.registeredObjectNames.clear();
                     this.unmatchedObjectNames.clear();
                     if (FederatedMBeanServerDelegate.debug.isDebugEnabled()) {
                        FederatedMBeanServerDelegate.debug.debug("Clearing out registered object names and refreshing cache.");
                     }

                     try {
                        Set var11 = this.connection.queryNames(FederatedMBeanServerDelegate.ALLMBEANS, (QueryExp)null);
                        Iterator var5 = var11.iterator();

                        while(var5.hasNext()) {
                           ObjectName var6 = (ObjectName)var5.next();
                           this.registeredObjectNames.add(var6);
                        }
                     } catch (Exception var9) {
                        if (FederatedMBeanServerDelegate.debug.isDebugEnabled()) {
                           FederatedMBeanServerDelegate.debug.debug("Failed query to Managed MBean Server ", var9);
                        }
                     }
                  }
               }
            }

         }

         public void addRegisteredObject(ObjectName var1x) {
            this.registeredObjectNames.add(var1x);
         }

         public void unregisterAll() {
            synchronized(this.registeredObjectNames) {
               Iterator var2 = this.registeredObjectNames.iterator();

               while(var2.hasNext()) {
                  ObjectName var3 = (ObjectName)var2.next();
                  MBeanServerNotification var4 = new MBeanServerNotification("JMX.mbean.unregistered", FederatedMBeanServerDelegate.MBEANSERVERDELEGATE, 0L, var3);
                  FederatedMBeanServerDelegate.this.sendNotification(var4);
               }

            }
         }
      };
   }

   public String getMBeanServerId() {
      return this.wrappedDelegate.getMBeanServerId();
   }

   public String getSpecificationName() {
      return this.wrappedDelegate.getSpecificationName();
   }

   public String getSpecificationVersion() {
      return this.wrappedDelegate.getSpecificationVersion();
   }

   public String getSpecificationVendor() {
      return this.wrappedDelegate.getSpecificationVendor();
   }

   public String getImplementationName() {
      return this.wrappedDelegate.getImplementationName();
   }

   public String getImplementationVersion() {
      return this.wrappedDelegate.getImplementationVersion();
   }

   public String getImplementationVendor() {
      return this.wrappedDelegate.getImplementationVendor();
   }

   public synchronized void removeNotificationListener(NotificationListener var1, NotificationFilter var2, Object var3) throws ListenerNotFoundException {
      super.removeNotificationListener(var1, var2, var3);
      this.decrementRegisteredListeners();
   }

   private synchronized boolean isManagingListeners() {
      return this.managingListeners;
   }

   private synchronized boolean hasRegisteredListeners() {
      return this.registeredListeners != 0;
   }

   private synchronized void incrementRegisteredListeners() {
      ++this.registeredListeners;
      if (this.registeredListeners == 1 && !this.isManagingListeners()) {
         kernelId.doAs(kernelId, new PrivilegedAction() {
            public Object run() {
               FederatedMBeanServerDelegate.this.registerManagedListeners();
               return null;
            }
         });
      }

      this.managingListeners = true;
   }

   private synchronized void decrementRegisteredListeners() {
      --this.registeredListeners;
   }

   public synchronized void addNotificationListener(NotificationListener var1, NotificationFilter var2, Object var3) throws IllegalArgumentException {
      super.addNotificationListener(var1, var2, var3);
      if (var3 instanceof LocalNotificationHandback) {
         this.localListeners.put(var1, var3);
      } else {
         this.incrementRegisteredListeners();
      }
   }

   private void registerManagedListeners() {
      try {
         this.connectionManager.iterateConnections(new MBeanServerConnectionManager.ConnectionCallback() {
            public void connection(MBeanServerConnection var1) throws IOException {
               try {
                  FederatedNotificationListener var2 = FederatedMBeanServerDelegate.this.createRegistrationListener(var1);

                  try {
                     Set var3 = var1.queryNames(FederatedMBeanServerDelegate.ALLMBEANS, (QueryExp)null);
                     Iterator var4 = var3.iterator();

                     while(var4.hasNext()) {
                        ObjectName var5 = (ObjectName)var4.next();
                        var2.addRegisteredObject(var5);
                     }
                  } catch (Exception var6) {
                     if (FederatedMBeanServerDelegate.debug.isDebugEnabled()) {
                        FederatedMBeanServerDelegate.debug.debug("Failed query to Managed MBean Server ", var6);
                     }
                  }

                  var1.addNotificationListener(FederatedMBeanServerDelegate.MBEANSERVERDELEGATE, var2, (NotificationFilter)null, (Object)null);
               } catch (InstanceNotFoundException var7) {
                  throw new AssertionError(var7);
               }
            }
         }, false);
      } catch (IOException var2) {
         throw new AssertionError(var2);
      }
   }

   public synchronized void removeNotificationListener(NotificationListener var1) throws ListenerNotFoundException {
      super.removeNotificationListener(var1);
      if (this.localListeners.containsKey(var1)) {
         this.localListeners.remove(var1);
      } else {
         this.decrementRegisteredListeners();
      }
   }

   public MBeanNotificationInfo[] getNotificationInfo() {
      return this.wrappedDelegate.getNotificationInfo();
   }

   static {
      try {
         ALLMBEANS = new ObjectName("*:*");
         MBEANSERVERDELEGATE = new ObjectName("JMImplementation:type=MBeanServerDelegate");
      } catch (MalformedObjectNameException var1) {
         throw new Error(var1);
      }
   }

   interface FederatedNotificationListener extends NotificationListener {
      void addRegisteredObject(ObjectName var1);

      void unregisterAll();
   }
}
