package weblogic.management.mbeanservers.internal;

import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.security.PrivilegedActionException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerDelegate;
import javax.management.MBeanServerNotification;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.context.JMXContext;
import weblogic.management.context.JMXContextHelper;
import weblogic.management.jmx.mbeanserver.WLSMBeanServerInterceptorBase;
import weblogic.management.utils.MBeanInfoLocalizationHelper;

public class JMXContextInterceptor extends WLSMBeanServerInterceptorBase {
   private static final DebugLogger DEBUG_LOGGER = DebugLogger.getDebugLogger("DebugJMXContext");
   private boolean isDomainRuntime;
   private String domainName;
   private final Map<ObjectName, InternalWeakReference> mbeansContextLoaderCache_ = new ConcurrentHashMap();
   private ReferenceQueue<ClassLoader> referenceQueue_ = new ReferenceQueue();

   public JMXContextInterceptor() {
      this.isDomainRuntime = false;
   }

   public JMXContextInterceptor(String var1) {
      this.isDomainRuntime = true;
      this.domainName = var1;
   }

   public void setNextMBeanServerConnection(MBeanServerConnection var1) {
      super.setNextMBeanServerConnection(var1);

      try {
         var1.addNotificationListener(MBeanServerDelegate.DELEGATE_NAME, new RegistrationUnRegistrationMBeanServerNotificationListener(), new RegistrationUnRegistrationMBeanServerNotificationFilter(), new LocalNotificationHandback());
      } catch (Exception var3) {
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("JMXContextInterceptor.setNextMBeanServerConnection(MBeanServerConnection next). Error registering listener with MBeanServerDelegate: " + var3.getMessage());
         }

         throw new RuntimeException(var3);
      }
   }

   public Object getAttribute(ObjectName var1, String var2) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException {
      ClassLoader var3 = null;
      if (this.isLocalizable(var1)) {
         ClassLoader var4 = this.getMBeanContextLoader(var1);
         if (var4 != null) {
            var3 = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(var4);
         }
      }

      Object var5;
      try {
         var5 = super.getAttribute(var1, var2);
      } finally {
         if (var3 != null) {
            Thread.currentThread().setContextClassLoader(var3);
         }

      }

      return var5;
   }

   public AttributeList getAttributes(ObjectName var1, String[] var2) throws InstanceNotFoundException, ReflectionException, IOException {
      ClassLoader var3 = null;
      if (this.isLocalizable(var1)) {
         ClassLoader var4 = this.getMBeanContextLoader(var1);
         if (var4 != null) {
            var3 = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(var4);
         }
      }

      AttributeList var5;
      try {
         var5 = super.getAttributes(var1, var2);
      } finally {
         if (var3 != null) {
            Thread.currentThread().setContextClassLoader(var3);
         }

      }

      return var5;
   }

   public void setAttribute(ObjectName var1, Attribute var2) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException {
      ClassLoader var3 = null;
      if (this.isLocalizable(var1)) {
         ClassLoader var4 = this.getMBeanContextLoader(var1);
         if (var4 != null) {
            var3 = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(var4);
         }
      }

      try {
         super.setAttribute(var1, var2);
      } finally {
         if (var3 != null) {
            Thread.currentThread().setContextClassLoader(var3);
         }

      }

   }

   public Object invoke(ObjectName var1, String var2, Object[] var3, String[] var4) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
      ClassLoader var5 = null;
      if (this.isLocalizable(var1)) {
         ClassLoader var6 = this.getMBeanContextLoader(var1);
         if (var6 != null) {
            var5 = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(var6);
         }
      }

      Object var7;
      try {
         var7 = super.invoke(var1, var2, var3, var4);
      } finally {
         if (var5 != null) {
            Thread.currentThread().setContextClassLoader(var5);
         }

      }

      return var7;
   }

   public AttributeList setAttributes(ObjectName var1, AttributeList var2) throws InstanceNotFoundException, ReflectionException, IOException {
      ClassLoader var3 = null;
      if (this.isLocalizable(var1)) {
         ClassLoader var4 = this.getMBeanContextLoader(var1);
         if (var4 != null) {
            var3 = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(var4);
         }
      }

      AttributeList var5;
      try {
         var5 = super.setAttributes(var1, var2);
      } finally {
         if (var3 != null) {
            Thread.currentThread().setContextClassLoader(var3);
         }

      }

      return var5;
   }

   public MBeanInfo getMBeanInfo(ObjectName var1) throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException {
      if (!this.isLocalizable(var1)) {
         return super.getMBeanInfo(var1);
      } else {
         ClassLoader var2 = null;
         ClassLoader var3 = this.getMBeanContextLoader(var1);
         if (var3 != null) {
            var2 = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(var3);
         }

         MBeanInfo var6;
         try {
            MBeanInfo var4 = super.getMBeanInfo(var1);
            Locale var5 = this.getLocale();
            if (var5 == null) {
               var6 = var4;
               return var6;
            }

            var6 = MBeanInfoLocalizationHelper.localizeMBeanInfo(var4, var5);
         } finally {
            if (var2 != null) {
               Thread.currentThread().setContextClassLoader(var2);
            }

         }

         return var6;
      }
   }

   private boolean isLocalizable(ObjectName var1) {
      return var1 == null || !"com.bea".equals(var1.getDomain()) && !"Security".equals(var1.getDomain()) && !this.isDelegate(var1);
   }

   private boolean isDelegate(ObjectName var1) {
      if (this.isDomainRuntime && var1 != null) {
         String var2 = var1.getKeyProperty("Location");
         if (var2 != null && var2.length() > 0 && !var2.equals(this.domainName)) {
            return true;
         }
      }

      return false;
   }

   private Locale getLocale() {
      Locale var1 = null;

      try {
         var1 = getThreadLocale();
      } catch (PrivilegedActionException var3) {
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("JMXContextInterceptor.getLocale(): Privileged action violation: " + var3.getMessage());
         }
      }

      return var1;
   }

   public static Locale getThreadLocale() throws PrivilegedActionException {
      JMXContext var0 = JMXContextHelper.getJMXContext(false);
      return var0 != null ? var0.getLocale() : null;
   }

   public ObjectInstance registerMBean(Object var1, ObjectName var2) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
      ObjectInstance var3 = super.registerMBean(var1, var2);
      this.cleanupCache();
      return var3;
   }

   public void unregisterMBean(ObjectName var1) throws InstanceNotFoundException, MBeanRegistrationException, IOException {
      super.unregisterMBean(var1);
   }

   private ClassLoader getMBeanContextLoader(ObjectName var1) {
      ClassLoader var2 = null;
      InternalWeakReference var3 = (InternalWeakReference)this.mbeansContextLoaderCache_.get(var1);
      if (var3 != null) {
         var2 = (ClassLoader)var3.get();
         if (var2 == null) {
            this.mbeansContextLoaderCache_.remove(var1);
            if (DEBUG_LOGGER.isDebugEnabled()) {
               DEBUG_LOGGER.debug("JMXContextInterceptor.getMBeanContextLoader(objectName mbeanName): MBean associated ClassLoader was recycled, prior to MBean named: \"" + var1 + "\" was unregistered. This mostly indicates improper unregistration life-cycle.");
            }
         }
      } else if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("JMXContextInterceptor.getMBeanContextLoader(objectName mbeanName): Could not find ClassLoader associated with MBean named: \"" + var1 + "\"");
      }

      return var2;
   }

   private synchronized void cleanupCache() {
      for(InternalWeakReference var1 = (InternalWeakReference)InternalWeakReference.class.cast(this.referenceQueue_.poll()); var1 != null; var1 = (InternalWeakReference)InternalWeakReference.class.cast(this.referenceQueue_.poll())) {
         ObjectName var2 = var1.getObjectName();
         this.mbeansContextLoaderCache_.remove(var2);
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("JMXContextInterceptor.cleanupCache: MBean associated ClassLoader was recycled, prior to MBean named: \"" + var2 + "\" was unregistered. This mostly indicates improper MBean unregistration life-cycle.");
         }
      }

   }

   private class RegistrationUnRegistrationMBeanServerNotificationListener implements NotificationListener {
      private RegistrationUnRegistrationMBeanServerNotificationListener() {
      }

      public void handleNotification(Notification var1, Object var2) {
         MBeanServerNotification var3 = (MBeanServerNotification)MBeanServerNotification.class.cast(var1);
         if ("JMX.mbean.registered".equals(var3.getType())) {
            InternalWeakReference var4 = JMXContextInterceptor.this.new InternalWeakReference(Thread.currentThread().getContextClassLoader(), var3.getMBeanName());
            JMXContextInterceptor.this.mbeansContextLoaderCache_.put(var3.getMBeanName(), var4);
         }

         if ("JMX.mbean.unregistered".equals(var3.getType())) {
            JMXContextInterceptor.this.mbeansContextLoaderCache_.remove(var3.getMBeanName());
         }

      }

      // $FF: synthetic method
      RegistrationUnRegistrationMBeanServerNotificationListener(Object var2) {
         this();
      }
   }

   private class RegistrationUnRegistrationMBeanServerNotificationFilter implements NotificationFilter {
      private RegistrationUnRegistrationMBeanServerNotificationFilter() {
      }

      public boolean isNotificationEnabled(Notification var1) {
         if (var1 instanceof MBeanServerNotification && ("JMX.mbean.registered".equals(var1.getType()) || "JMX.mbean.unregistered".equals(var1.getType()))) {
            ObjectName var2 = ((MBeanServerNotification)MBeanServerNotification.class.cast(var1)).getMBeanName();
            if (JMXContextInterceptor.this.isLocalizable(var2)) {
               return true;
            }
         }

         return false;
      }

      // $FF: synthetic method
      RegistrationUnRegistrationMBeanServerNotificationFilter(Object var2) {
         this();
      }
   }

   private class InternalWeakReference extends WeakReference<ClassLoader> {
      private ObjectName associatedMBean_;

      InternalWeakReference(ClassLoader var2, ObjectName var3) {
         super(var2, JMXContextInterceptor.this.referenceQueue_);
         this.associatedMBean_ = var3;
      }

      public ObjectName getObjectName() {
         return this.associatedMBean_;
      }
   }
}
