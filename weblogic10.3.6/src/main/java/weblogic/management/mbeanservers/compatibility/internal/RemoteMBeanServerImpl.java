package weblogic.management.mbeanservers.compatibility.internal;

import java.io.ObjectInputStream;
import java.rmi.Remote;
import java.security.AccessController;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.loading.ClassLoaderRepository;
import weblogic.management.MBeanHome;
import weblogic.management.RemoteMBeanServer;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.rmi.extensions.DisconnectEvent;
import weblogic.rmi.extensions.DisconnectListener;
import weblogic.rmi.extensions.DisconnectMonitor;
import weblogic.rmi.extensions.DisconnectMonitorListImpl;
import weblogic.rmi.extensions.DisconnectMonitorUnavailableException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class RemoteMBeanServerImpl implements RemoteMBeanServer {
   MBeanHome mbeanHome;
   MBeanServer mbeanServer;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private Map remoteListeners = Collections.synchronizedMap(new HashMap());

   public RemoteMBeanServerImpl(MBeanServer var1) {
      this.mbeanServer = var1;
   }

   public MBeanHome getMBeanHome() {
      return this.mbeanHome;
   }

   void setMBeanHome(MBeanHome var1) {
      this.mbeanHome = var1;
   }

   public String getServerName() {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      return var1 == null ? null : var1.getServerName();
   }

   public MBeanInfo getMBeanInfo(ObjectName var1) throws InstanceNotFoundException, IntrospectionException, ReflectionException {
      return this.mbeanServer.getMBeanInfo(var1);
   }

   public AttributeList getAttributes(ObjectName var1, String[] var2) throws InstanceNotFoundException, ReflectionException {
      return this.mbeanServer.getAttributes(var1, var2);
   }

   public ObjectInstance getObjectInstance(ObjectName var1) throws InstanceNotFoundException {
      return this.mbeanServer.getObjectInstance(var1);
   }

   public boolean isRegistered(ObjectName var1) {
      return this.mbeanServer.isRegistered(var1);
   }

   public Object getAttribute(ObjectName var1, String var2) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException {
      return this.mbeanServer.getAttribute(var1, var2);
   }

   public void setAttribute(ObjectName var1, Attribute var2) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
      this.mbeanServer.setAttribute(var1, var2);
   }

   public AttributeList setAttributes(ObjectName var1, AttributeList var2) throws InstanceNotFoundException, ReflectionException {
      return this.mbeanServer.setAttributes(var1, var2);
   }

   public ObjectInstance registerMBean(Object var1, ObjectName var2) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
      return this.mbeanServer.registerMBean(var1, var2);
   }

   public void unregisterMBean(ObjectName var1) throws InstanceNotFoundException, MBeanRegistrationException {
      this.mbeanServer.unregisterMBean(var1);
   }

   public void addNotificationListener(ObjectName var1, NotificationListener var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException {
      if (var2 instanceof Remote) {
         var2 = this.cacheRemoteStub(var2);
      }

      this.mbeanServer.addNotificationListener(var1, var2, var3, var4);
   }

   public void addNotificationListener(ObjectName var1, ObjectName var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException {
      this.mbeanServer.addNotificationListener(var1, var2, var3, var4);
   }

   public void removeNotificationListener(ObjectName var1, NotificationListener var2) throws InstanceNotFoundException, ListenerNotFoundException {
      if (var2 != null) {
         if (var2 instanceof Remote) {
            var2 = this.getRemoteCachedStub(var2);
         }

         this.mbeanServer.removeNotificationListener(var1, var2);
      }

   }

   public ObjectInstance createMBean(String var1, ObjectName var2) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException {
      return this.mbeanServer.createMBean(var1, var2);
   }

   public ObjectInstance createMBean(String var1, ObjectName var2, ObjectName var3) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException {
      return this.mbeanServer.createMBean(var1, var2, var3);
   }

   public ObjectInstance createMBean(String var1, ObjectName var2, Object[] var3, String[] var4) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException {
      return this.mbeanServer.createMBean(var1, var2, var3, var4);
   }

   public ObjectInstance createMBean(String var1, ObjectName var2, ObjectName var3, Object[] var4, String[] var5) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException {
      return this.mbeanServer.createMBean(var1, var2, var3, var4, var5);
   }

   public Set queryMBeans(ObjectName var1, QueryExp var2) {
      return this.mbeanServer.queryMBeans(var1, var2);
   }

   public Set queryNames(ObjectName var1, QueryExp var2) {
      return this.mbeanServer.queryNames(var1, var2);
   }

   public Object invoke(ObjectName var1, String var2, Object[] var3, String[] var4) throws InstanceNotFoundException, MBeanException, ReflectionException {
      return this.mbeanServer.invoke(var1, var2, var3, var4);
   }

   public Integer getMBeanCount() {
      return this.mbeanServer.getMBeanCount();
   }

   public String getDefaultDomain() {
      return this.mbeanServer.getDefaultDomain();
   }

   public String[] getDomains() {
      return this.mbeanServer.getDomains();
   }

   public void removeNotificationListener(ObjectName var1, ObjectName var2) throws InstanceNotFoundException, ListenerNotFoundException {
      this.mbeanServer.removeNotificationListener(var1, var2);
   }

   public void removeNotificationListener(ObjectName var1, ObjectName var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException, ListenerNotFoundException {
      this.mbeanServer.removeNotificationListener(var1, var2, var3, var4);
   }

   public void removeNotificationListener(ObjectName var1, NotificationListener var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException, ListenerNotFoundException {
      if (var2 instanceof Remote) {
         var2 = this.getRemoteCachedStub(var2);
         if (var2 == null) {
            return;
         }
      }

      this.mbeanServer.removeNotificationListener(var1, var2, var3, var4);
   }

   public boolean isInstanceOf(ObjectName var1, String var2) throws InstanceNotFoundException {
      return this.mbeanServer.isInstanceOf(var1, var2);
   }

   public Object instantiate(String var1) throws ReflectionException, MBeanException {
      return this.mbeanServer.instantiate(var1);
   }

   public Object instantiate(String var1, ObjectName var2) throws ReflectionException, MBeanException, InstanceNotFoundException {
      return this.mbeanServer.instantiate(var1, var2);
   }

   public Object instantiate(String var1, Object[] var2, String[] var3) throws ReflectionException, MBeanException {
      return this.mbeanServer.instantiate(var1, var2, var3);
   }

   public Object instantiate(String var1, ObjectName var2, Object[] var3, String[] var4) throws ReflectionException, MBeanException, InstanceNotFoundException {
      return this.mbeanServer.instantiate(var1, var2, var3, var4);
   }

   public ObjectInputStream deserialize(ObjectName var1, byte[] var2) throws InstanceNotFoundException, OperationsException {
      return this.mbeanServer.deserialize(var1, var2);
   }

   public ObjectInputStream deserialize(String var1, byte[] var2) throws OperationsException, ReflectionException {
      return this.mbeanServer.deserialize(var1, var2);
   }

   public ObjectInputStream deserialize(String var1, ObjectName var2, byte[] var3) throws InstanceNotFoundException, OperationsException, ReflectionException {
      return this.mbeanServer.deserialize(var1, var2, var3);
   }

   public ClassLoader getClassLoaderFor(ObjectName var1) throws InstanceNotFoundException {
      return this.mbeanServer.getClassLoaderFor(var1);
   }

   public ClassLoader getClassLoader(ObjectName var1) throws InstanceNotFoundException {
      return this.mbeanServer.getClassLoader(var1);
   }

   public ClassLoaderRepository getClassLoaderRepository() {
      return this.mbeanServer.getClassLoaderRepository();
   }

   private NotificationListener cacheRemoteStub(NotificationListener var1) {
      Iterator var2 = this.remoteListeners.keySet().iterator();

      NotificationListener var3;
      do {
         if (!var2.hasNext()) {
            StubCleaner var4 = new StubCleaner(var1);
            this.remoteListeners.put(var1, var4);
            return var1;
         }

         var3 = (NotificationListener)var2.next();
      } while(!var3.equals(var1));

      return var3;
   }

   private NotificationListener getRemoteCachedStub(NotificationListener var1) {
      if (this.remoteListeners.isEmpty()) {
         return var1;
      } else {
         Iterator var2 = this.remoteListeners.keySet().iterator();

         NotificationListener var3;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            var3 = (NotificationListener)var2.next();
         } while(!var3.equals(var1));

         return var3;
      }
   }

   private Map getRemoteCachedStubs() {
      return this.remoteListeners;
   }

   private class StubCleaner implements DisconnectListener {
      private final Remote remoteListener;
      private final DisconnectMonitor monitor = DisconnectMonitorListImpl.getDisconnectMonitor();

      public StubCleaner(NotificationListener var2) {
         if (var2 instanceof Remote) {
            this.remoteListener = (Remote)var2;

            try {
               this.monitor.addDisconnectListener(this.remoteListener, this);
            } catch (DisconnectMonitorUnavailableException var4) {
            }

         } else {
            throw new AssertionError("Impossible exception");
         }
      }

      public void onDisconnect(DisconnectEvent var1) {
         RemoteMBeanServerImpl.this.getRemoteCachedStubs().remove(this.remoteListener);
      }
   }
}
