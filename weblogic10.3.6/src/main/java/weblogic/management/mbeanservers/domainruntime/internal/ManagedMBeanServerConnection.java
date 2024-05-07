package weblogic.management.mbeanservers.domainruntime.internal;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerNotification;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import weblogic.diagnostics.debug.DebugLogger;

public class ManagedMBeanServerConnection implements MBeanServerConnection {
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJMXDomain");
   ManagedMBeanServerObjectNameManager objectNameManager = null;
   MBeanServerConnection mbeanServerConnection;
   JMXExecutor executor;
   String location;
   Map listenerToRelayListenerMap = Collections.synchronizedMap(new HashMap());

   public ManagedMBeanServerConnection(MBeanServerConnection var1, String var2, JMXExecutor var3) {
      this.mbeanServerConnection = var1;
      this.objectNameManager = new ManagedMBeanServerObjectNameManager(var2);
      this.executor = var3;
      this.location = var2;
   }

   public void disconnected() {
      if (this.executor != null) {
         this.executor.cancel();
      }

   }

   public MBeanServerConnection getWrappedConnection() {
      return this.mbeanServerConnection;
   }

   private ObjectName addLocationToObjectName(ObjectName var1) {
      if (var1 != null) {
         String var2 = var1.getKeyProperty("Location");
         if (var2 != null && !var2.equalsIgnoreCase(this.location)) {
            return var1;
         }
      }

      return this.objectNameManager.lookupScopedObjectName(var1);
   }

   private ObjectInstance addLocationToObjectInstance(ObjectInstance var1) {
      return new ObjectInstance(this.addLocationToObjectName(var1.getObjectName()), var1.getClassName());
   }

   private Object addLocationToResult(Object var1) {
      if (var1 instanceof ObjectName) {
         return this.addLocationToObjectName((ObjectName)var1);
      } else if (!(var1 instanceof ObjectName[])) {
         return var1;
      } else {
         ObjectName[] var2 = (ObjectName[])((ObjectName[])var1);

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3] = this.addLocationToObjectName(var2[var3]);
         }

         return var2;
      }
   }

   private AttributeList addLocationToAttributeList(AttributeList var1) {
      ListIterator var2 = var1.listIterator();
      AttributeList var3 = new AttributeList();

      while(var2.hasNext()) {
         Attribute var4 = (Attribute)var2.next();
         Object var5 = this.addLocationToResult(var4.getValue());
         Attribute var6 = new Attribute(var4.getName(), var5);
         var3.add(var6);
      }

      return var3;
   }

   private Set addLocationToObjectNameSet(Set var1) {
      HashSet var2 = new HashSet(var1.size());
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         ObjectName var4 = (ObjectName)var3.next();
         var2.add(this.addLocationToObjectName(var4));
      }

      return var2;
   }

   private Set addLocationToObjectInstances(Set var1) {
      if (var1 != null && var1.size() != 0) {
         HashSet var2 = new HashSet(var1.size());
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            ObjectInstance var4 = (ObjectInstance)var3.next();
            var2.add(this.addLocationToObjectInstance(var4));
         }

         return var2;
      } else {
         return var1;
      }
   }

   private ObjectName removeLocationFromObjectName(ObjectName var1) {
      if (var1 == null) {
         return var1;
      } else {
         String var2 = var1.getKeyProperty("Location");
         if (var2 != null && var2.equalsIgnoreCase(this.location)) {
            ObjectName var3 = this.objectNameManager.lookupObjectName(var1);
            return var3;
         } else {
            return var1;
         }
      }
   }

   private Attribute removeLocationFromAttribute(Attribute var1) {
      return new Attribute(var1.getName(), this.removeLocationFromParam(var1.getValue()));
   }

   private AttributeList removeLocationFromAttributeList(AttributeList var1) {
      ListIterator var2 = var1.listIterator();
      AttributeList var3 = new AttributeList();

      while(var2.hasNext()) {
         Attribute var4 = (Attribute)var2.next();
         var3.add(this.removeLocationFromAttribute(var4));
      }

      return var3;
   }

   private Object removeLocationFromParam(Object var1) {
      if (var1 instanceof ObjectName) {
         return this.removeLocationFromObjectName((ObjectName)var1);
      } else if (!(var1 instanceof ObjectName[])) {
         return var1;
      } else {
         ObjectName[] var2 = (ObjectName[])((ObjectName[])var1);

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3] = this.removeLocationFromObjectName(var2[var3]);
         }

         return var2;
      }
   }

   private Object[] removeLocationFromParams(Object[] var1) {
      if (var1 == null) {
         return null;
      } else {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            Object var3 = var1[var2];
            var1[var2] = this.removeLocationFromParam(var3);
         }

         return var1;
      }
   }

   private QueryExp processQueryExp(QueryExp var1) {
      return var1;
   }

   public ObjectInstance createMBean(String var1, ObjectName var2) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
      ObjectInstance var3 = this.mbeanServerConnection.createMBean(var1, this.removeLocationFromObjectName(var2));
      return new ObjectInstance(this.addLocationToObjectName(var3.getObjectName()), var3.getClassName());
   }

   public ObjectInstance createMBean(String var1, ObjectName var2, ObjectName var3) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
      ObjectInstance var4 = this.mbeanServerConnection.createMBean(var1, this.removeLocationFromObjectName(var2), var3);
      return new ObjectInstance(this.addLocationToObjectName(var4.getObjectName()), var4.getClassName());
   }

   public ObjectInstance createMBean(String var1, ObjectName var2, Object[] var3, String[] var4) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
      ObjectInstance var5 = this.mbeanServerConnection.createMBean(var1, this.removeLocationFromObjectName(var2), var3, var4);
      return new ObjectInstance(this.addLocationToObjectName(var5.getObjectName()), var5.getClassName());
   }

   public ObjectInstance createMBean(String var1, ObjectName var2, ObjectName var3, Object[] var4, String[] var5) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
      ObjectInstance var6 = this.mbeanServerConnection.createMBean(var1, this.removeLocationFromObjectName(var2), var3, var4, var5);
      return this.addLocationToObjectInstance(var6);
   }

   public void unregisterMBean(ObjectName var1) throws InstanceNotFoundException, MBeanRegistrationException, IOException {
      this.mbeanServerConnection.unregisterMBean(this.removeLocationFromObjectName(var1));
      this.objectNameManager.unregisterObjectInstance(var1);
   }

   public ObjectInstance getObjectInstance(ObjectName var1) throws InstanceNotFoundException, IOException {
      return this.addLocationToObjectInstance(this.mbeanServerConnection.getObjectInstance(this.removeLocationFromObjectName(var1)));
   }

   public Set queryMBeans(ObjectName var1, QueryExp var2) throws IOException {
      Set var3 = this.mbeanServerConnection.queryMBeans(this.removeLocationFromObjectName(var1), this.processQueryExp(var2));
      return this.addLocationToObjectInstances(var3);
   }

   public Set queryNames(ObjectName var1, QueryExp var2) throws IOException {
      Set var3 = this.mbeanServerConnection.queryNames(this.removeLocationFromObjectName(var1), this.processQueryExp(var2));
      return this.addLocationToObjectNameSet(var3);
   }

   public boolean isRegistered(ObjectName var1) throws IOException {
      return this.mbeanServerConnection.isRegistered(this.removeLocationFromObjectName(var1));
   }

   public Integer getMBeanCount() throws IOException {
      return this.mbeanServerConnection.getMBeanCount();
   }

   public Object getAttribute(ObjectName var1, String var2) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException {
      return this.addLocationToResult(this.mbeanServerConnection.getAttribute(this.removeLocationFromObjectName(var1), var2));
   }

   public AttributeList getAttributes(ObjectName var1, String[] var2) throws InstanceNotFoundException, ReflectionException, IOException {
      return this.addLocationToAttributeList(this.mbeanServerConnection.getAttributes(this.removeLocationFromObjectName(var1), var2));
   }

   public void setAttribute(ObjectName var1, Attribute var2) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException {
      this.mbeanServerConnection.setAttribute(this.removeLocationFromObjectName(var1), this.removeLocationFromAttribute(var2));
   }

   public AttributeList setAttributes(ObjectName var1, AttributeList var2) throws InstanceNotFoundException, ReflectionException, IOException {
      AttributeList var3 = this.mbeanServerConnection.setAttributes(this.removeLocationFromObjectName(var1), this.removeLocationFromAttributeList(var2));
      return this.addLocationToAttributeList(var3);
   }

   public Object invoke(ObjectName var1, String var2, Object[] var3, String[] var4) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
      Object var5 = this.mbeanServerConnection.invoke(this.removeLocationFromObjectName(var1), var2, this.removeLocationFromParams(var3), var4);
      return this.addLocationToResult(var5);
   }

   public String getDefaultDomain() throws IOException {
      return this.mbeanServerConnection.getDefaultDomain();
   }

   public String[] getDomains() throws IOException {
      return this.mbeanServerConnection.getDomains();
   }

   private NotificationListener createRelayNotificationListener(NotificationListener var1) {
      RelayNotificationListener var2 = new RelayNotificationListener(var1);
      this.listenerToRelayListenerMap.put(var1, var2);
      return var2;
   }

   private NotificationListener removeRelayNotificationListener(NotificationListener var1) {
      return (NotificationListener)this.listenerToRelayListenerMap.remove(var1);
   }

   public void addNotificationListener(ObjectName var1, NotificationListener var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException, IOException {
      this.mbeanServerConnection.addNotificationListener(this.removeLocationFromObjectName(var1), this.createRelayNotificationListener(var2), var3, var4);
   }

   public void addNotificationListener(ObjectName var1, ObjectName var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException, IOException {
      this.mbeanServerConnection.addNotificationListener(this.removeLocationFromObjectName(var1), this.removeLocationFromObjectName(var2), var3, var4);
   }

   public void removeNotificationListener(ObjectName var1, ObjectName var2) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
      this.mbeanServerConnection.removeNotificationListener(this.removeLocationFromObjectName(var1), this.removeLocationFromObjectName(var2));
   }

   public void removeNotificationListener(ObjectName var1, ObjectName var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
      this.mbeanServerConnection.removeNotificationListener(this.removeLocationFromObjectName(var1), this.removeLocationFromObjectName(var2), var3, var4);
   }

   public void removeNotificationListener(ObjectName var1, NotificationListener var2) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
      NotificationListener var3 = this.removeRelayNotificationListener(var2);
      this.mbeanServerConnection.removeNotificationListener(this.removeLocationFromObjectName(var1), var3);
   }

   public void removeNotificationListener(ObjectName var1, NotificationListener var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
      NotificationListener var5 = this.removeRelayNotificationListener(var2);
      this.mbeanServerConnection.removeNotificationListener(this.removeLocationFromObjectName(var1), var5, var3, var4);
   }

   public MBeanInfo getMBeanInfo(ObjectName var1) throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException {
      return this.mbeanServerConnection.getMBeanInfo(this.removeLocationFromObjectName(var1));
   }

   public boolean isInstanceOf(ObjectName var1, String var2) throws InstanceNotFoundException, IOException {
      return this.mbeanServerConnection.isInstanceOf(this.removeLocationFromObjectName(var1), var2);
   }

   private class RelayNotificationListener implements NotificationListener {
      private NotificationListener listener;

      RelayNotificationListener(NotificationListener var2) {
         this.listener = var2;
      }

      public void handleNotification(Notification var1, Object var2) {
         if (var1 instanceof MBeanServerNotification) {
            MBeanServerNotification var3 = (MBeanServerNotification)var1;
            ObjectName var4 = ManagedMBeanServerConnection.this.addLocationToObjectName(var3.getMBeanName());
            var1 = new MBeanServerNotification(((Notification)var1).getType(), ((Notification)var1).getSource(), ((Notification)var1).getSequenceNumber(), var4);
         } else {
            ((Notification)var1).setSource(ManagedMBeanServerConnection.this.addLocationToResult(((Notification)var1).getSource()));
         }

         if (ManagedMBeanServerConnection.debug.isDebugEnabled()) {
            ManagedMBeanServerConnection.debug.debug("RelayNotificationListener handles Notification: \nType: " + ((Notification)var1).getType() + "\nSource: " + ((Notification)var1).getSource() + "\nMessage: " + ((Notification)var1).getMessage());
         }

         this.listener.handleNotification((Notification)var1, var2);
      }
   }
}
