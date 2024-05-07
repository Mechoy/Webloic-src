package weblogic.management.mbeanservers.domainruntime.internal;

import java.io.IOException;
import java.security.AccessController;
import java.util.HashSet;
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
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.security.auth.Subject;
import weblogic.descriptor.DescriptorClassLoader;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.context.JMXContext;
import weblogic.management.context.JMXContextHelper;
import weblogic.management.jmx.mbeanserver.WLSMBeanServerInterceptorBase;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public class FederatedMBeanServerInterceptor extends WLSMBeanServerInterceptorBase {
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJMXDomain");
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   MBeanServerConnectionManager connectionManager;
   String domainName;

   public FederatedMBeanServerInterceptor(MBeanServerConnectionManager var1, String var2) {
      this.connectionManager = var1;
      this.domainName = var2;
   }

   private boolean isDelegate(ObjectName var1) {
      if (var1 == null) {
         return false;
      } else {
         String var2 = var1.getKeyProperty("Location");
         return var2 != null && var2.length() != 0 && !var2.equals(this.domainName);
      }
   }

   MBeanServerConnection lookupMBeanServer(ObjectName var1) {
      if (var1 == null) {
         return null;
      } else {
         String var2 = var1.getKeyProperty("Location");
         return var2 != null && var2.length() != 0 && !var2.equals(this.domainName) ? this.connectionManager.lookupMBeanServerConnection(var2) : this.mbeanServerConnection;
      }
   }

   private void initializeJMXContext(ObjectName var1) {
      if (this.isDelegate(var1)) {
         AuthenticatedSubject var2 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
         if (!AuthenticatedSubject.ANON.equals(var2) && !KERNEL_ID.equals(var2)) {
            JMXContext var3 = JMXContextHelper.getJMXContext(true);
            var3.setSubject(var2.getSubject());
            JMXContextHelper.putJMXContext(var3);
         }
      }
   }

   private void cleanupJMXContext(ObjectName var1) {
      if (this.isDelegate(var1)) {
         JMXContext var2 = JMXContextHelper.getJMXContext(false);
         if (var2 != null) {
            var2.setSubject((Subject)null);
            JMXContextHelper.putJMXContext(var2);
         }

      }
   }

   MBeanServerConnection lookupMBeanServerForCreate(ObjectName var1) throws IOException {
      MBeanServerConnection var2 = this.lookupMBeanServer(var1);
      if (var2 == null) {
         throw new IOException("Unable to contact MBeanServer for " + var1);
      } else {
         return var2;
      }
   }

   MBeanServerConnection lookupMBeanServerRequired(ObjectName var1) throws InstanceNotFoundException {
      MBeanServerConnection var2 = this.lookupMBeanServer(var1);
      if (var2 == null) {
         throw new InstanceNotFoundException("Unable to contact MBeanServer for " + var1);
      } else {
         return var2;
      }
   }

   public ClassLoader getClassLoaderFor(ObjectName var1) throws InstanceNotFoundException {
      String var2 = var1.getKeyProperty("Location");
      return var2 != null && var2.length() != 0 ? DescriptorClassLoader.getClassLoader() : super.getClassLoaderFor(var1);
   }

   public ObjectInstance createMBean(String var1, ObjectName var2) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
      this.initializeJMXContext(var2);

      ObjectInstance var3;
      try {
         var3 = this.lookupMBeanServerForCreate(var2).createMBean(var1, var2);
      } finally {
         this.cleanupJMXContext(var2);
      }

      return var3;
   }

   public ObjectInstance createMBean(String var1, ObjectName var2, ObjectName var3) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
      this.initializeJMXContext(var2);

      ObjectInstance var4;
      try {
         var4 = this.lookupMBeanServerForCreate(var2).createMBean(var1, var2, var3);
      } finally {
         this.cleanupJMXContext(var2);
      }

      return var4;
   }

   public ObjectInstance createMBean(String var1, ObjectName var2, Object[] var3, String[] var4) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
      this.initializeJMXContext(var2);

      ObjectInstance var5;
      try {
         var5 = this.lookupMBeanServerForCreate(var2).createMBean(var1, var2, var3, var4);
      } finally {
         this.cleanupJMXContext(var2);
      }

      return var5;
   }

   public ObjectInstance createMBean(String var1, ObjectName var2, ObjectName var3, Object[] var4, String[] var5) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
      this.initializeJMXContext(var2);

      ObjectInstance var6;
      try {
         var6 = this.lookupMBeanServerForCreate(var2).createMBean(var1, var2, var3, var4, var5);
      } finally {
         this.cleanupJMXContext(var2);
      }

      return var6;
   }

   public void unregisterMBean(ObjectName var1) throws InstanceNotFoundException, MBeanRegistrationException, IOException {
      this.initializeJMXContext(var1);

      try {
         this.lookupMBeanServerRequired(var1).unregisterMBean(var1);
      } finally {
         this.cleanupJMXContext(var1);
      }

   }

   public ObjectInstance getObjectInstance(ObjectName var1) throws InstanceNotFoundException, IOException {
      return this.lookupMBeanServerRequired(var1).getObjectInstance(var1);
   }

   public Set queryMBeans(final ObjectName var1, final QueryExp var2) throws IOException {
      String var3 = this.getLocationFromObjectName(var1);
      if (var3 == null) {
         final Set var5 = this.mbeanServerConnection.queryMBeans(var1, var2);
         this.connectionManager.iterateConnections(new MBeanServerConnectionManager.ConnectionCallback() {
            public void connection(MBeanServerConnection var1x) throws IOException {
               Set var2x = var1x.queryMBeans(var1, var2);
               var5.addAll(var2x);
            }
         }, false);
         return var5;
      } else {
         MBeanServerConnection var4 = this.connectionManager.lookupMBeanServerConnection(var3);
         return (Set)(var4 == null ? new HashSet(0) : var4.queryMBeans(var1, var2));
      }
   }

   public String getLocationFromObjectName(ObjectName var1) {
      return var1 != null ? var1.getKeyProperty("Location") : null;
   }

   public Set queryNames(final ObjectName var1, final QueryExp var2) throws IOException {
      String var3 = this.getLocationFromObjectName(var1);
      if (var3 == null) {
         final Set var5 = this.mbeanServerConnection.queryNames(var1, var2);
         this.connectionManager.iterateConnections(new MBeanServerConnectionManager.ConnectionCallback() {
            public void connection(MBeanServerConnection var1x) throws IOException {
               Set var2x = var1x.queryNames(var1, var2);
               var5.addAll(var2x);
            }
         }, false);
         return var5;
      } else {
         MBeanServerConnection var4 = this.connectionManager.lookupMBeanServerConnection(var3);
         return (Set)(var4 == null ? new HashSet(0) : var4.queryNames(var1, var2));
      }
   }

   public boolean isRegistered(ObjectName var1) throws IOException {
      return this.lookupMBeanServerForCreate(var1).isRegistered(var1);
   }

   public Integer getMBeanCount() throws IOException {
      final int[] var1 = new int[]{this.mbeanServerConnection.getMBeanCount()};
      this.connectionManager.iterateConnections(new MBeanServerConnectionManager.ConnectionCallback() {
         public void connection(MBeanServerConnection var1x) throws IOException {
            int[] var10000 = var1;
            var10000[0] += var1x.getMBeanCount();
         }
      }, false);
      return new Integer(var1[0]);
   }

   public Object getAttribute(ObjectName var1, String var2) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException {
      this.initializeJMXContext(var1);

      Object var3;
      try {
         var3 = this.lookupMBeanServerRequired(var1).getAttribute(var1, var2);
      } finally {
         this.cleanupJMXContext(var1);
      }

      return var3;
   }

   public AttributeList getAttributes(ObjectName var1, String[] var2) throws InstanceNotFoundException, ReflectionException, IOException {
      this.initializeJMXContext(var1);

      AttributeList var3;
      try {
         var3 = this.lookupMBeanServerRequired(var1).getAttributes(var1, var2);
      } finally {
         this.cleanupJMXContext(var1);
      }

      return var3;
   }

   public void setAttribute(ObjectName var1, Attribute var2) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException {
      this.initializeJMXContext(var1);

      try {
         this.lookupMBeanServerRequired(var1).setAttribute(var1, var2);
      } finally {
         this.cleanupJMXContext(var1);
      }

   }

   public AttributeList setAttributes(ObjectName var1, AttributeList var2) throws InstanceNotFoundException, ReflectionException, IOException {
      this.initializeJMXContext(var1);

      AttributeList var3;
      try {
         var3 = this.lookupMBeanServerRequired(var1).setAttributes(var1, var2);
      } finally {
         this.cleanupJMXContext(var1);
      }

      return var3;
   }

   public Object invoke(ObjectName var1, String var2, Object[] var3, String[] var4) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
      this.initializeJMXContext(var1);

      Object var5;
      try {
         var5 = this.lookupMBeanServerRequired(var1).invoke(var1, var2, var3, var4);
      } finally {
         this.cleanupJMXContext(var1);
      }

      return var5;
   }

   public String getDefaultDomain() throws IOException {
      return this.mbeanServerConnection.getDefaultDomain();
   }

   private void addToSet(Set var1, Object[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         Object var4 = var2[var3];
         var1.add(var4);
      }

   }

   public String[] getDomains() throws IOException {
      final HashSet var1 = new HashSet();
      this.addToSet(var1, this.mbeanServerConnection.getDomains());
      this.connectionManager.iterateConnections(new MBeanServerConnectionManager.ConnectionCallback() {
         public void connection(MBeanServerConnection var1x) throws IOException {
            FederatedMBeanServerInterceptor.this.addToSet(var1, var1x.getDomains());
         }
      }, false);
      return (String[])((String[])var1.toArray(new String[var1.size()]));
   }

   public void addNotificationListener(ObjectName var1, NotificationListener var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException, IOException {
      if (debug.isDebugEnabled()) {
         debug.debug("Add notification listener for " + var1);
      }

      this.lookupMBeanServerRequired(var1).addNotificationListener(var1, var2, var3, var4);
   }

   public void addNotificationListener(ObjectName var1, ObjectName var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException, IOException {
      this.lookupMBeanServerRequired(var1).addNotificationListener(var1, var2, var3, var4);
   }

   public void removeNotificationListener(ObjectName var1, ObjectName var2) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
      this.initializeJMXContext(var1);

      try {
         this.lookupMBeanServerRequired(var1).removeNotificationListener(var1, var2);
      } finally {
         this.cleanupJMXContext(var1);
      }

   }

   public void removeNotificationListener(ObjectName var1, ObjectName var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
      this.initializeJMXContext(var1);

      try {
         this.lookupMBeanServerRequired(var1).removeNotificationListener(var1, var2, var3, var4);
      } finally {
         this.cleanupJMXContext(var1);
      }

   }

   public void removeNotificationListener(ObjectName var1, NotificationListener var2) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
      this.initializeJMXContext(var1);

      try {
         this.lookupMBeanServerRequired(var1).removeNotificationListener(var1, var2);
      } finally {
         this.cleanupJMXContext(var1);
      }

   }

   public void removeNotificationListener(ObjectName var1, NotificationListener var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
      this.initializeJMXContext(var1);

      try {
         this.lookupMBeanServerRequired(var1).removeNotificationListener(var1, var2, var3, var4);
      } finally {
         this.cleanupJMXContext(var1);
      }

   }

   public MBeanInfo getMBeanInfo(ObjectName var1) throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException {
      this.initializeJMXContext(var1);

      MBeanInfo var2;
      try {
         var2 = this.lookupMBeanServerRequired(var1).getMBeanInfo(var1);
      } finally {
         this.cleanupJMXContext(var1);
      }

      return var2;
   }

   public boolean isInstanceOf(ObjectName var1, String var2) throws InstanceNotFoundException, IOException {
      return this.lookupMBeanServerRequired(var1).isInstanceOf(var1, var2);
   }
}
