package weblogic.management.mbeanservers.compatibility.internal;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.management.Attribute;
import javax.management.AttributeChangeNotification;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.ReflectionException;
import javax.management.RuntimeErrorException;
import javax.management.RuntimeOperationsException;
import weblogic.common.internal.InteropWriteReplaceable;
import weblogic.common.internal.PeerInfo;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.AttributeAddNotification;
import weblogic.management.AttributeRemoveNotification;
import weblogic.management.MBeanHome;
import weblogic.management.ManagementException;
import weblogic.management.ManagementLogger;
import weblogic.management.WebLogicMBean;
import weblogic.management.WebLogicObjectName;
import weblogic.management.commo.StandardInterface;
import weblogic.management.configuration.ConfigurationError;
import weblogic.management.configuration.JMSDestinationMBean;
import weblogic.management.configuration.JMSSessionPoolMBean;
import weblogic.management.jmx.ExceptionMapper;
import weblogic.management.jmx.PrimitiveMapper;
import weblogic.rmi.extensions.RemoteRuntimeException;

public class MBeanProxy implements InvocationHandler, Serializable, InteropWriteReplaceable {
   private static final long serialVersionUID = -3989625551485752570L;
   private final MBeanInfo info;
   protected MBeanHome mbeanHome;
   protected ObjectName objectName;
   protected transient Map listeners;
   private boolean unregistered = false;
   private MBeanServerConnection mbeanServer;

   public MBeanHome getMBeanHome() {
      return this.mbeanHome;
   }

   protected MBeanProxy(ObjectName var1, MBeanHome var2) {
      this.mbeanHome = var2;
      this.objectName = var1;
      this.mbeanServer = var2.getMBeanServer();

      try {
         this.info = this.mbeanServer.getMBeanInfo(this.objectName);
      } catch (Exception var4) {
         throw new ConfigurationError(var4);
      }
   }

   public final ObjectName getObjectName() {
      return this.objectName;
   }

   public final MBeanInfo getMBeanInfo() {
      return this.info;
   }

   private final boolean isUnregistered() {
      return this.unregistered;
   }

   public final Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      String var4 = var2.getName();

      try {
         if (var2.getDeclaringClass() == Object.class) {
            return var2.invoke(this, var3);
         } else if (var4.equals("isRegistered") && (null == var3 || var3.length == 0) && var2.getReturnType().equals(Boolean.TYPE)) {
            return this.isUnregistered() ? Boolean.FALSE : Boolean.TRUE;
         } else if (var4.equals("toString") && (null == var3 || var3.length == 0) && var2.getReturnType().equals(String.class)) {
            return "(MBeanProxy:" + this.objectName.getCanonicalName() + ")";
         } else if (var4.equals("getMBeanInfo")) {
            return this.info;
         } else if (var4.equals("getObjectName")) {
            WebLogicObjectName var5 = null;

            try {
               var5 = new WebLogicObjectName(this.objectName);
            } catch (MalformedObjectNameException var7) {
               ManagementLogger.logExceptionCreatingObjectName(var7);
            }

            return var5;
         } else if (var2.getName().equals("wls_getObjectName")) {
            return this.getObjectName();
         } else if (!var4.startsWith("get") && !var4.startsWith("is") || var3 != null && var3.length != 0) {
            if (var4.equals("setAttributes") && var2.getDeclaringClass() == DynamicMBean.class) {
               return this.setAttributes(var3[0]);
            } else if (var4.equals("setAttribute") && var2.getDeclaringClass() == DynamicMBean.class) {
               return this.setOneAttribute(var3[0]);
            } else if (var4.equals("getAttributes") && var2.getDeclaringClass() == DynamicMBean.class) {
               return this.getAttributes(var3[0]);
            } else if (var4.equals("getAttribute") && var2.getDeclaringClass() == DynamicMBean.class) {
               return this.getOneAttribute(var3[0]);
            } else if (var4.startsWith("set") && var3 != null && var3.length == 1) {
               this.setAttribute(var4, var3[0]);
               return null;
            } else if (var4.equals("addNotificationListener")) {
               this.addNotificationListener((NotificationListener)var3[0], (NotificationFilter)var3[1], var3[2]);
               return null;
            } else if (var4.equals("removeNotificationListener")) {
               this.removeNotificationListener((NotificationListener)var3[0]);
               return null;
            } else {
               return var4.equals("invoke") && var2.getDeclaringClass() == DynamicMBean.class ? this.dynamicInvoke(var2.getReturnType(), var3) : this.invoke(var2, var3);
            }
         } else {
            return this.getAttribute(var2);
         }
      } catch (Throwable var8) {
         throw ExceptionMapper.matchJMXException(var2, var8);
      }
   }

   private final void addNotificationListener(NotificationListener var1, NotificationFilter var2, Object var3) throws IllegalArgumentException {
      if (this.listeners == null) {
         this.listeners = new HashMap();
      }

      Object var4 = (BaseNotificationListener)this.listeners.get(var1);
      if (var4 == null) {
         if (this.objectName.getKeyProperty("Type").equals("LogBroadcasterRuntime")) {
            var4 = new OnewayNotificationListenerImpl(this, var1);
         } else {
            var4 = new RelayNotificationListenerImpl(this, var1);
         }

         if (null != var4) {
            this.listeners.put(var1, var4);
         }
      }

      ((BaseNotificationListener)var4).addFilterAndHandback(var2, var3);
   }

   private final void removeNotificationListener(NotificationListener var1) throws ListenerNotFoundException {
      if (this.listeners == null) {
         throw new ListenerNotFoundException("listener: " + var1);
      } else {
         BaseNotificationListener var2 = (BaseNotificationListener)this.listeners.get(var1);
         if (var2 == null) {
            throw new ListenerNotFoundException("listener: " + var1);
         } else {
            var2.remove();
            this.listeners.remove(var1);
         }
      }
   }

   public final void sendNotification(NotificationListener var1, Notification var2, Object var3) {
      if (!this.isUnregistered()) {
         Notification var4 = null;

         try {
            var4 = this.wrapNotification(var2);
         } catch (ManagementException var6) {
            ManagementLogger.logSendNotificationFailed(var6);
            return;
         }

         var1.handleNotification(var4, var3);
      }
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (var1 == this) {
         return true;
      } else {
         if (var1 instanceof Proxy) {
            Proxy var2 = (Proxy)var1;
            InvocationHandler var3 = Proxy.getInvocationHandler(var2);
            if (var3 instanceof MBeanProxy) {
               MBeanProxy var4 = (MBeanProxy)var3;
               return this.equalsMBeanProxy(var4);
            }
         } else if (var1 instanceof MBeanProxy) {
            MBeanProxy var5 = (MBeanProxy)var1;
            return this.equalsMBeanProxy(var5);
         }

         return false;
      }
   }

   public String toString() {
      return "Proxy for " + this.objectName;
   }

   void unregister() {
      if (!this.isUnregistered()) {
         this.unregistered = true;
         if (this.listeners != null) {
            Iterator var1 = (new HashSet(this.listeners.keySet())).iterator();

            while(var1.hasNext()) {
               try {
                  this.removeNotificationListener((NotificationListener)var1.next());
               } catch (ListenerNotFoundException var3) {
               }
            }
         }

         this.mbeanHome = null;
      }
   }

   public Object interopWriteReplace(PeerInfo var1) throws IOException {
      Object var2 = this.objectName;
      if (var1.compareTo(PeerInfo.VERSION_DIABLO) < 0) {
         if (!(this.objectName instanceof WebLogicObjectName)) {
            try {
               var2 = new WebLogicObjectName(this.objectName);
            } catch (MalformedObjectNameException var4) {
               throw new Error(var4);
            }
         }

         weblogic.management.internal.MBeanProxy var3 = new weblogic.management.internal.MBeanProxy((ObjectName)var2, this.mbeanHome);
         return var3;
      } else {
         return this;
      }
   }

   private MBeanAttributeInfo getAttributeInfo(String var1) throws AttributeNotFoundException {
      if (this.info != null) {
         MBeanAttributeInfo[] var2 = this.info.getAttributes();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3].getName().equals(var1)) {
               return var2[var3];
            }
         }
      }

      throw new AttributeNotFoundException(var1);
   }

   private Object getAttribute(Method var1) throws AttributeNotFoundException, InstanceNotFoundException, ReflectionException, MBeanException {
      if (this.mbeanHome == null) {
         throw new InstanceNotFoundException(this.objectName.toString());
      } else {
         String var2 = var1.getName();
         String var3 = null;
         if (var2.charAt(0) == 'g') {
            var3 = var2.substring(3);
         } else {
            var3 = var2.substring(2);
         }

         MBeanServerConnection var4 = this.mbeanServer;
         Object var5 = null;

         try {
            var5 = var4.getAttribute(this.objectName, var3);
         } catch (IOException var7) {
            throw new MBeanException(var7);
         } catch (RemoteRuntimeException var8) {
            throw new RuntimeOperationsException(var8);
         }

         return this.wrap(var1.getReturnType(), var5);
      }
   }

   private Object getOneAttribute(Object var1) throws AttributeNotFoundException, InstanceNotFoundException, ReflectionException, MBeanException, IOException {
      return this.mbeanServer.getAttribute(this.objectName, (String)var1);
   }

   private Object getAttributes(Object var1) throws InstanceNotFoundException, ReflectionException, IOException {
      return this.mbeanServer.getAttributes(this.objectName, (String[])((String[])var1));
   }

   private final Object setAttribute(String var1, Object var2) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException {
      String var3 = var1.substring(3);
      Attribute var4 = new Attribute(var3, this.unwrap(var2));

      try {
         this.mbeanServer.setAttribute(this.objectName, var4);
         this.updateObjectNameForSetParent(var4);
         return null;
      } catch (RuntimeErrorException var6) {
         ManagementLogger.logMBeanProxySetAttributeError(this.objectName, var1, var2, (Object)null, var6.getTargetError());
         throw var6;
      } catch (RuntimeOperationsException var7) {
         throw new MBeanException(var7);
      }
   }

   private final Object setOneAttribute(Object var1) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException {
      Attribute var2 = (Attribute)var1;
      Attribute var3 = this.unwrap(var2);
      this.mbeanServer.setAttribute(this.objectName, var3);
      this.updateObjectNameForSetParent(var3);
      return null;
   }

   private static WebLogicObjectName asWebLogicObjectName(ObjectName var0) {
      if (var0 instanceof WebLogicObjectName) {
         return (WebLogicObjectName)var0;
      } else {
         try {
            return new WebLogicObjectName(var0);
         } catch (MalformedObjectNameException var2) {
            throw new AssertionError(var2);
         }
      }
   }

   private final void updateObjectNameForSetParent(Attribute var1) {
      if (var1.getName().equals("Parent")) {
         try {
            this.objectName = new WebLogicObjectName(asWebLogicObjectName(this.objectName), asWebLogicObjectName((ObjectName)var1.getValue()));
         } catch (MalformedObjectNameException var3) {
            throw new AssertionError(var3);
         }
      }

   }

   private MBeanProxy getProxy(WebLogicMBean var1) {
      return (MBeanProxy)Proxy.getInvocationHandler(var1);
   }

   private final void updateObjectNameForAddRemove(String var1, Object[] var2) {
      if (var2 != null && var2.length == 1) {
         Object var3 = var2[0];
         if (var3 instanceof JMSSessionPoolMBean || var3 instanceof JMSDestinationMBean) {
            WebLogicMBean var4 = (WebLogicMBean)var2[0];
            MBeanProxy var5;
            WebLogicObjectName var6;
            if (!var1.equals("addSessionPool") && !var1.equals("addDestination")) {
               if (var1.equals("removeSessionPool") || var1.equals("removeDestination")) {
                  try {
                     var5 = this.getProxy(var4);
                     var6 = new WebLogicObjectName(asWebLogicObjectName(var5.getObjectName()), asWebLogicObjectName(this.objectName).getParent());
                     var5.setObjectName(var6);
                  } catch (MalformedObjectNameException var7) {
                     throw new AssertionError(var7);
                  }
               }

            } else {
               try {
                  var5 = this.getProxy(var4);
                  var6 = new WebLogicObjectName(asWebLogicObjectName(var5.getObjectName()), asWebLogicObjectName(this.objectName));
                  var5.setObjectName(var6);
               } catch (MalformedObjectNameException var8) {
                  throw new AssertionError(var8);
               }
            }
         }
      }
   }

   void setObjectName(ObjectName var1) {
      this.objectName = var1;
   }

   private Object invoke(Method var1, Object[] var2) throws InstanceNotFoundException, MBeanException, ReflectionException, OperationsException, IOException {
      String var3 = var1.getName();
      Object[] var4 = this.unwrap(var2);
      if (var4 == null) {
         var4 = new Object[0];
      }

      Class[] var5 = var1.getParameterTypes();
      String[] var6 = new String[var4.length];

      for(int var7 = 0; var7 < var6.length; ++var7) {
         var6[var7] = this.getMappedType(var5[var7]);
      }

      Object var9 = this.mbeanServer.invoke(this.objectName, var3, var4, var6);
      Object var8 = this.wrap(var1.getReturnType(), var9);
      this.updateObjectNameForAddRemove(var3, var2);
      return var8;
   }

   private String getMappedType(Class var1) {
      if (!WebLogicMBean.class.isAssignableFrom(var1) && !StandardInterface.class.isAssignableFrom(var1)) {
         String var2 = PrimitiveMapper.lookupWrapperClassName(var1);
         return var2 != null ? var2 : var1.getName();
      } else {
         return ObjectName.class.getName();
      }
   }

   private Object dynamicInvoke(Class var1, Object[] var2) throws InstanceNotFoundException, MBeanException, ReflectionException, OperationsException, IOException {
      try {
         Object[] var3 = this.unwrap((Object[])((Object[])var2[1]));
         Object var4 = this.mbeanServer.invoke(this.objectName, (String)var2[0], var3, (String[])((String[])var2[2]));
         return this.wrap(var1, var4);
      } catch (RuntimeErrorException var5) {
         throw var5.getTargetError();
      }
   }

   private Object wrap(Class var1, Object var2) {
      if (var2 == null) {
         return null;
      } else if (var1 == Void.class) {
         return null;
      } else if (var1.isAssignableFrom(var2.getClass())) {
         return var2;
      } else {
         try {
            if (var2 instanceof ObjectName) {
               Object var11 = this.mbeanHome.getProxy((ObjectName)var2);
               return var11;
            } else if (var2 instanceof ObjectName[]) {
               Class var3 = var1.getComponentType();
               Object[] var4 = (Object[])((Object[])var2);
               int var5 = var4.length;
               Object[] var6 = (Object[])((Object[])Array.newInstance(var3, var5));
               int var7 = 0;

               for(int var8 = 0; var8 < var4.length; ++var8) {
                  Object var9 = this.wrap(var3, var4[var8]);
                  if (var9 != null) {
                     var6[var7++] = var9;
                  }
               }

               if (var7 < var5) {
                  Object[] var12 = (Object[])((Object[])Array.newInstance(var3, var7));
                  System.arraycopy(var6, 0, var12, 0, var7);
                  var6 = var12;
               }

               return var6;
            } else {
               return var2;
            }
         } catch (InstanceNotFoundException var10) {
            var2 = null;
            return var2;
         }
      }
   }

   private Object wrap(String var1, Object var2) throws ManagementException {
      if (var2 == null) {
         return null;
      } else {
         try {
            if (var1.equals("ObjectName")) {
               return var2;
            } else {
               MBeanAttributeInfo var3 = this.getAttributeInfo(var1);
               Class var4 = TypesHelper.findClass(var3.getType());
               return this.wrap(var4, var2);
            }
         } catch (ClassNotFoundException var5) {
            throw new ManagementException("error wrapping " + var1 + ", value=" + var2, var5);
         } catch (AttributeNotFoundException var6) {
            throw new ManagementException("error wrapping " + var1 + ", value=" + var2, var6);
         }
      }
   }

   private final Object setAttributes(Object var1) throws InstanceNotFoundException, ReflectionException, IOException {
      AttributeList var2 = this.unwrap((AttributeList)var1);
      return this.mbeanServer.setAttributes(this.objectName, var2);
   }

   private final boolean equalsMBeanProxy(MBeanProxy var1) {
      if (var1.mbeanHome == null && this.mbeanHome == null) {
         return var1.objectName.equals(this.objectName);
      } else {
         return var1.mbeanHome.equals(this.mbeanHome) ? var1.objectName.equals(this.objectName) : false;
      }
   }

   private AttributeList unwrap(AttributeList var1) {
      if (var1 == null) {
         return null;
      } else {
         AttributeList var2 = new AttributeList();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Attribute var4 = this.unwrap((Attribute)var3.next());
            if (null != var4) {
               var2.add(var4);
            }
         }

         return var2;
      }
   }

   private Attribute unwrap(Attribute var1) {
      if (null == var1) {
         return null;
      } else {
         String var2 = var1.getName();
         Object var3 = this.unwrap(var1.getValue());
         return null == var2 ? null : new Attribute(var2, var3);
      }
   }

   private Object[] unwrap(Object[] var1) {
      if (var1 == null) {
         return null;
      } else if (var1.length == 0) {
         return var1;
      } else {
         Object var2 = this.unwrap(var1[0]);
         if (!(var2 instanceof ObjectName)) {
            return var1;
         } else {
            ObjectName[] var3 = new ObjectName[var1.length];
            var3[0] = (ObjectName)var2;

            for(int var4 = 1; var4 < var1.length; ++var4) {
               var3[var4] = (ObjectName)this.unwrap(var1[var4]);
            }

            return var3;
         }
      }
   }

   private Object unwrap(Object var1) {
      if (var1 == null) {
         return null;
      } else {
         Class var2 = var1.getClass();
         if (var2.isArray()) {
            return var2.getComponentType().isPrimitive() ? var1 : this.unwrap((Object[])((Object[])var1));
         } else {
            if (Proxy.isProxyClass(var2)) {
               InvocationHandler var3 = Proxy.getInvocationHandler(var1);
               if (var3 instanceof MBeanProxy) {
                  MBeanProxy var4 = (MBeanProxy)var3;
                  return var4.getObjectName();
               }
            }

            return var1;
         }
      }
   }

   private Notification wrapNotification(Notification var1) throws ManagementException {
      String var2 = null;
      String var4 = null;
      Object var3;
      Object var6;
      if (var1 instanceof AttributeAddNotification) {
         AttributeAddNotification var5 = (AttributeAddNotification)var1;
         var4 = var5.getAttributeName();
         var2 = var5.getAttributeType();
         var6 = var5.getAddedValue();
         var3 = new AttributeAddNotification(this.objectName, var4, var2, this.wrap(var4, var6));
      } else if (var1 instanceof AttributeRemoveNotification) {
         AttributeRemoveNotification var8 = (AttributeRemoveNotification)var1;
         var4 = var8.getAttributeName();
         var2 = var8.getAttributeType();
         var6 = var8.getRemovedValue();
         var3 = new AttributeRemoveNotification(this.objectName, var4, var2, this.wrap(var4, var6));
      } else if (var1 instanceof AttributeChangeNotification) {
         AttributeChangeNotification var9 = (AttributeChangeNotification)var1;
         var4 = var9.getAttributeName();
         var2 = var9.getAttributeType();
         if (var4.equals("ObjectName")) {
            return var1;
         }

         var6 = var9.getOldValue();
         Object var7 = var9.getNewValue();
         var3 = new AttributeChangeNotification(this.objectName, 0L, 0L, (String)null, var4, var2, this.wrap(var4, var6), this.wrap(var4, var7));
      } else {
         var3 = var1;
      }

      return (Notification)var3;
   }

   public Descriptor getDescriptor() {
      return null;
   }

   public DescriptorBean getParentBean() {
      return null;
   }

   public boolean isSet(String var1) {
      return false;
   }

   public void unSet(String var1) {
   }

   public void addBeanUpdateListener(BeanUpdateListener var1) {
   }

   public void removeBeanUpdateListener(BeanUpdateListener var1) {
   }

   public boolean isEditable() {
      return false;
   }

   public Object clone() {
      return null;
   }

   static class DebugNotificationFilter implements NotificationFilter, Serializable {
      private static final long serialVersionUID = -277365551078287855L;
      ObjectName name = null;

      DebugNotificationFilter(ObjectName var1) {
         this.name = var1;
      }

      public boolean isNotificationEnabled(Notification var1) {
         return true;
      }
   }
}
