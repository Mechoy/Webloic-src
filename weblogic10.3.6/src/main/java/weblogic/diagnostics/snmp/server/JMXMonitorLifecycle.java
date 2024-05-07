package weblogic.diagnostics.snmp.server;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import weblogic.common.internal.VersionInfo;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.snmp.agent.SNMPAgent;
import weblogic.management.configuration.SNMPAgentMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.beaninfo.BeanInfoAccess;

public abstract class JMXMonitorLifecycle {
   static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugSNMPAgent");
   private static final String LOCATION_KEY = "Location";
   static final String SNMP_DOMAIN = "com.bea.wls.snmp";
   static final String OBJECT_NAME_PREFIX = "com.bea.wls.snmp:";
   private static BeanInfoAccess beanInfoAccess;
   boolean adminServer;
   SNMPAgent snmpAgent;
   String serverName;
   MBeanServerConnection mbeanServerConnection;
   List monitorListenerList = new LinkedList();
   Map monitorListenerRegistry = new HashMap();
   protected boolean deregisterMonitorListener = false;

   public JMXMonitorLifecycle(boolean var1, String var2, SNMPAgent var3, MBeanServerConnection var4) {
      this.adminServer = var1;
      this.snmpAgent = var3;
      this.serverName = var2;
      this.mbeanServerConnection = var4;
   }

   protected ObjectName getMonitorObjectName(ObjectName var1, JMXMonitorListener var2, String var3) throws MalformedObjectNameException {
      Hashtable var4 = var1.getKeyPropertyList();
      String var5 = var1.getKeyProperty("Location");
      String var6 = var2.getName() + "-" + var2.getAttributeName();
      if (var5 != null && var5.length() > 0) {
         var4.remove("Location");
         var6 = var6 + "-" + var5;
      }

      var4.put(var3, var6);
      ObjectName var7 = new ObjectName("com.bea.wls.snmp", var4);
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Creating MBean " + var7);
      }

      return var7;
   }

   void registerMonitorListener(ObjectName var1, JMXMonitorListener var2, Object var3) throws Exception {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Registering listener: this=" + this + " objectName=" + var1 + " listener=" + var2 + " handback=" + var3);
      }

      this.mbeanServerConnection.addNotificationListener(var1, var2, var2, var3);
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Registered listener succesfully: this=" + this + " objectName=" + var1 + " listener=" + var2 + " handback=" + var3);
      }

      Object var4 = null;
      synchronized(this.monitorListenerRegistry) {
         var4 = (List)this.monitorListenerRegistry.get(var1);
         if (var4 == null) {
            var4 = new LinkedList();
            this.monitorListenerRegistry.put(var1, var4);
         }
      }

      synchronized(var4) {
         ((List)var4).add(var2);
      }
   }

   void serverStarted(String var1) {
   }

   void serverStopped(String var1) {
   }

   Iterator getJMXMonitorListeners() {
      LinkedList var1 = new LinkedList();
      synchronized(this.monitorListenerList) {
         var1.addAll(this.monitorListenerList);
      }

      return var1.iterator();
   }

   abstract void initializeMonitorListenerList(SNMPAgentMBean var1) throws Exception;

   void registerMonitorListeners(ObjectName var1) {
      synchronized(this.monitorListenerList) {
         Iterator var3 = this.monitorListenerList.iterator();

         while(var3.hasNext()) {
            JMXMonitorListener var4 = (JMXMonitorListener)var3.next();
            ObjectName var5 = var4.getQueryExpression();
            if (var5 != null && var5.apply(var1)) {
               if (DEBUG.isDebugEnabled()) {
                  DEBUG.debug("Registering JMXMonitor for " + var1 + " as it matches " + var4.getQueryExpression());
               }

               this.registerMonitor(var1, var4);
            }
         }

      }
   }

   abstract void registerMonitor(ObjectName var1, JMXMonitorListener var2);

   void deregisterMonitorListeners() {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("De-registering monitor listeners on " + this);
      }

      synchronized(this.monitorListenerRegistry) {
         Iterator var2 = this.monitorListenerRegistry.keySet().iterator();

         while(true) {
            if (!var2.hasNext()) {
               break;
            }

            ObjectName var3 = (ObjectName)var2.next();
            this.deregisterMonitorListeners(var3, false);
            var2.remove();
         }
      }

      this.monitorListenerList.clear();
   }

   void deregisterMonitorListeners(ObjectName var1) {
      this.deregisterMonitorListeners(var1, true);
   }

   protected void deregisterMonitorListener(ObjectName var1, JMXMonitorListener var2) {
      if (this.deregisterMonitorListener) {
         try {
            if (this.mbeanServerConnection.isRegistered(var1)) {
               if (DEBUG.isDebugEnabled()) {
                  DEBUG.debug("Unregistering NotificationListener " + var2 + " on " + var1);
               }

               this.mbeanServerConnection.removeNotificationListener(var1, var2);
            }
         } catch (Throwable var4) {
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Exception deregistering listener", var4);
            }
         }

      }
   }

   private List getMonitorsForObservedInstance(ObjectName var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.monitorListenerRegistry.keySet().iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         List var5 = (List)this.monitorListenerRegistry.get(var4);
         boolean var6 = false;
         Iterator var7 = var5.iterator();

         while(!var6 && var7.hasNext()) {
            JMXMonitorListener var8 = (JMXMonitorListener)var7.next();
            ObjectName var9 = var8.getQueryExpression();
            if (var8.getMonitor() != null && var9 != null && var9.apply(var1)) {
               var6 = true;
            }
         }

         if (var6) {
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Monitor " + var4 + " observes an attribute on " + var1);
            }

            var2.add(var4);
         }
      }

      return var2;
   }

   private void deregisterMonitorListeners(ObjectName var1, boolean var2) {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("deregisterMonitorListeners " + var1);
      }

      List var3 = null;
      List var4 = null;
      synchronized(this.monitorListenerRegistry) {
         var3 = (List)this.monitorListenerRegistry.get(var1);
         if (var3 == null) {
            var4 = this.getMonitorsForObservedInstance(var1);
         }
      }

      if (var3 == null) {
         if (var4 != null) {
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
               ObjectName var17 = (ObjectName)var5.next();
               this.deregisterMonitorListeners(var17, var2);
            }
         }

      } else {
         synchronized(var3) {
            Iterator var6 = var3.iterator();

            label85:
            while(true) {
               JMXMonitorListener var7;
               ObjectName var8;
               do {
                  if (!var6.hasNext()) {
                     break label85;
                  }

                  var7 = (JMXMonitorListener)var6.next();
                  this.deregisterMonitorListener(var1, var7);
                  var8 = var7.getMonitor();
               } while(var8 == null);

               try {
                  if (this.mbeanServerConnection.isRegistered(var8)) {
                     this.mbeanServerConnection.invoke(var8, "stop", new Object[0], new String[0]);
                  }
               } catch (Throwable var13) {
                  if (DEBUG.isDebugEnabled()) {
                     DEBUG.debug("Exception stopping monitor", var13);
                  }
               }

               try {
                  if (this.mbeanServerConnection.isRegistered(var8)) {
                     if (DEBUG.isDebugEnabled()) {
                        DEBUG.debug("Unregistering NotificationListener " + var7 + " for " + var8);
                     }

                     this.mbeanServerConnection.removeNotificationListener(var8, var7);
                     this.mbeanServerConnection.unregisterMBean(var8);
                  }
               } catch (Throwable var14) {
                  if (DEBUG.isDebugEnabled()) {
                     DEBUG.debug("Exception deregistering listener", var14);
                  }
               }
            }
         }

         if (var2) {
            synchronized(this.monitorListenerRegistry) {
               this.monitorListenerRegistry.remove(var1);
            }
         }

      }
   }

   private void removeObservedInstances(ObjectName var1) {
      try {
         ObjectName[] var2 = (ObjectName[])((ObjectName[])this.mbeanServerConnection.invoke(var1, "getObservedObjects", new Object[]{var1}, new String[]{"javax.management.ObjectName"}));
         int var3 = var2 != null ? var2.length : 0;

         for(int var4 = 0; var4 < var3; ++var4) {
            this.mbeanServerConnection.invoke(var1, "removeObservedObject", new Object[]{var2[var4]}, new String[]{"javax.management.ObjectName"});
         }

         this.mbeanServerConnection.setAttribute(var1, new Attribute("ObservedAttribute", (Object)null));
      } catch (Exception var5) {
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Exception while removing observed instance", var5);
         }
      }

   }

   protected Number getNumber(String var1, String var2, int var3) throws IllegalArgumentException {
      try {
         PropertyDescriptor var4 = getPropertyDescriptor(var1, var2);
         Class var5 = var4.getPropertyType();
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Class = " + var5 + " for " + var1 + "." + var2);
         }

         if (var5 == Double.TYPE) {
            var5 = Double.class;
         } else if (var5 == Float.TYPE) {
            var5 = Float.class;
         } else if (var5 == Long.TYPE) {
            var5 = Long.class;
         } else {
            var5 = Integer.class;
         }

         Method var6 = var5.getMethod("valueOf", String.class);
         return (Number)var6.invoke((Object)null, "" + var3);
      } catch (Throwable var7) {
         throw new IllegalArgumentException(var7);
      }
   }

   private static PropertyDescriptor getPropertyDescriptor(String var0, String var1) {
      BeanInfo var2 = getBeanInfo(var0);
      if (var2 == null) {
         throw new IllegalArgumentException();
      } else {
         PropertyDescriptor[] var3 = var2.getPropertyDescriptors();
         if (var3 != null) {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               PropertyDescriptor var5 = var3[var4];
               String var6 = var5.getName();
               if (var6 != null && var6.equals(var1)) {
                  return var5;
               }
            }
         }

         throw new IllegalArgumentException();
      }
   }

   private static BeanInfo getBeanInfo(String var0) {
      if (beanInfoAccess == null) {
         beanInfoAccess = ManagementService.getBeanInfoAccess();
      }

      String var1 = VersionInfo.theOne().getReleaseVersion();
      BeanInfo var2 = beanInfoAccess.getBeanInfoForInterface(var0, true, var1);
      String var3;
      if (var2 == null && var0.indexOf(".") == -1) {
         var3 = "weblogic.management.configuration." + var0 + "MBean";
         var2 = beanInfoAccess.getBeanInfoForInterface(var3, true, var1);
      }

      if (var2 == null && var0.indexOf(".") == -1) {
         var3 = "weblogic.management.runtime." + var0 + "MBean";
         var2 = beanInfoAccess.getBeanInfoForInterface(var3, true, var1);
      }

      return var2;
   }
}
