package weblogic.management.internal;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.ReflectionException;
import javax.management.RuntimeErrorException;
import javax.management.RuntimeMBeanException;
import javax.management.RuntimeOperationsException;
import javax.management.modelmbean.ModelMBeanInfo;
import weblogic.common.internal.PeerInfo;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.AttributeAddNotification;
import weblogic.management.AttributeRemoveNotification;
import weblogic.management.MBeanHome;
import weblogic.management.ManagementException;
import weblogic.management.ManagementLogger;
import weblogic.management.RemoteMBeanServer;
import weblogic.management.WebLogicMBean;
import weblogic.management.WebLogicObjectName;
import weblogic.management.commo.StandardInterface;
import weblogic.management.configuration.ConfigurationError;
import weblogic.management.configuration.ConfigurationException;
import weblogic.management.info.ExtendedInfo;
import weblogic.utils.AssertionError;

public class MBeanProxy implements InvocationHandler, Serializable, NotificationBroadcaster {
   private static final long serialVersionUID = -3989625551485752570L;
   protected MBeanHome mbeanHome;
   protected WebLogicObjectName objectName;
   protected transient MBeanInfo info;
   protected transient Map listeners;
   private final boolean cachingDisabled = true;
   private boolean unregistered = false;
   private DynamicMBean delegate;
   private transient RemoteMBeanServer mbeanServer;
   private static final String[] EMPTY_SIGNATURE = new String[0];
   private static final int INITIAL_CAPACITY_FOR_MBEAN_TYPES = 89;
   private static Map mbeanInfos = new HashMap(89);
   private static Map configMBeanInfos = new HashMap(89);
   private static Map adminMBeanInfos = new HashMap(89);
   private static final String ADMIN_CONFIG_PKG_NAME = "weblogic.management.configuration";

   public MBeanProxy(DynamicMBean var1) {
      this.delegate = var1;
   }

   public MBeanProxy(ObjectName var1, MBeanHome var2) {
      this.mbeanHome = var2;
      this.objectName = (WebLogicObjectName)var1;

      try {
         this.info = this.getMBeanServer().getMBeanInfo(this.objectName);
      } catch (Exception var4) {
         throw new ConfigurationError(var4);
      }
   }

   public final MBeanHome getMBeanHome() {
      return this.mbeanHome;
   }

   public final boolean isUnregistered() {
      return this.unregistered;
   }

   public WebLogicObjectName getObjectName() {
      return this.objectName;
   }

   public MBeanNotificationInfo[] getNotificationInfo() {
      return null;
   }

   public final Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      try {
         String var4 = var2.getName();
         if (var2.getDeclaringClass() != Object.class && var2.getDeclaringClass() != NotificationBroadcaster.class) {
            if (var4.equals("isRegistered") && (null == var3 || var3.length == 0) && var2.getReturnType() == Boolean.TYPE) {
               return this.isUnregistered() ? Boolean.FALSE : Boolean.TRUE;
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
                  return this.setAttribute(var4, var3[0]);
               } else {
                  return var4.equals("invoke") && var2.getDeclaringClass() == DynamicMBean.class ? this.dynamicInvoke(var3) : this.invoke(var4, var3);
               }
            } else {
               return this.getAttribute(var4);
            }
         } else {
            return var2.invoke(this, var3);
         }
      } catch (MBeanException var5) {
         throw var5.getTargetException();
      } catch (RuntimeMBeanException var6) {
         throw var6.getTargetException();
      } catch (ConfigurationError var7) {
         throw var7;
      } catch (Exception var8) {
         throw var8;
      }
   }

   public final void registerConfigMBean(String var1, MBeanServer var2) {
      if (!WebLogicObjectName.isAdmin(this.objectName)) {
         throw new AssertionError("registerConfigMBean should only be called on AdminMBean, and this is: " + this.objectName);
      } else {
         try {
            this.invokeForCachingStub("registerConfigMBean", new Object[]{var1, var2});
         } catch (Throwable var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            } else {
               throw new RuntimeException(var4);
            }
         }
      }
   }

   public final void unRegisterConfigMBean(String var1) {
      if (!WebLogicObjectName.isAdmin(this.objectName)) {
         throw new AssertionError("unRegisterConfigMBean should only be called on AdminMBean, and this is: " + this.objectName);
      } else {
         try {
            this.invokeForCachingStub("unRegisterConfigMBean", new Object[]{var1});
         } catch (Throwable var3) {
            if (var3 instanceof RuntimeException) {
               throw (RuntimeException)var3;
            } else {
               throw new RuntimeException(var3);
            }
         }
      }
   }

   public final void addNotificationListener(NotificationListener var1, NotificationFilter var2, Object var3) throws IllegalArgumentException {
      if (this.listeners == null) {
         this.listeners = new HashMap();
      }

      Object var4 = (BaseNotificationListener)this.listeners.get(var1);
      if (var4 == null) {
         if (this.objectName instanceof WebLogicObjectName && "LogBroadcasterRuntime".equals(this.objectName.getType())) {
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

   public final void removeNotificationListener(NotificationListener var1) throws ListenerNotFoundException {
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
            ManagementLogger.logExceptionInMBeanProxy(var6);
            return;
         }

         var1.handleNotification(var4, var3);
      }
   }

   public final Object interopWriteReplace(PeerInfo var1) throws IOException {
      return this;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 instanceof Proxy) {
         return var1.equals(this);
      } else {
         return var1 instanceof MBeanProxy && this.equalsMBeanProxy((MBeanProxy)var1);
      }
   }

   public int hashCode() {
      int var1 = this.objectName.hashCode();
      if (this.mbeanHome != null) {
         var1 ^= this.mbeanHome.hashCode();
      }

      return var1;
   }

   public String toString() {
      return "Proxy for " + this.objectName;
   }

   protected final Object invokeForCachingStub(String var1, Object[] var2) throws Throwable {
      try {
         if (var1.equals("isRegistered")) {
            return this.isUnregistered() ? Boolean.FALSE : Boolean.TRUE;
         } else if (var1.equals("getObjectName")) {
            return this.objectName;
         } else if (var1.equals("getName")) {
            return this.objectName.getName();
         } else if (var1.equals("getType")) {
            return this.objectName.getType();
         } else if (var1.equals("getMBeanInfo")) {
            return this.info;
         } else if (var1.equals("isCachingDisabled")) {
            return Boolean.TRUE;
         } else if (!var1.startsWith("get") && !var1.startsWith("is") || var2 != null && var2.length != 0) {
            if (var1.equals("addNotificationListener")) {
               System.out.println("addNotificationListener in invokeForCachingStub");
               this.addNotificationListener((NotificationListener)var2[0], (NotificationFilter)var2[1], var2[2]);
               return null;
            } else if (var1.equals("removeNotificationListener")) {
               this.removeNotificationListener((NotificationListener)var2[0]);
               return null;
            } else if (var1.equals("getNotificationInfo")) {
               return this.getNotificationInfo();
            } else if (var1.equals("setAttributes")) {
               return this.setAttributes(var2[0]);
            } else if (var1.equals("setAttribute")) {
               return this.setOneAttribute(var2[0]);
            } else if (var1.equals("getAttributes")) {
               return this.getAttributes(var2[0]);
            } else if (var1.equals("getAttribute")) {
               return this.getOneAttribute(var2[0]);
            } else if (var1.startsWith("set") && var2 != null && var2.length == 1) {
               return this.setAttribute(var1, var2[0]);
            } else {
               return var1.equals("invoke") ? this.dynamicInvoke(var2) : this.invoke(var1, var2);
            }
         } else {
            return this.getAttribute(var1);
         }
      } catch (MBeanException var4) {
         throw var4.getTargetException();
      } catch (RuntimeMBeanException var5) {
         throw var5.getTargetException();
      } catch (ConfigurationError var6) {
         throw var6;
      } catch (Exception var7) {
         throw var7;
      }
   }

   protected void readObjectForCachingStubs(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.objectName = (WebLogicObjectName)var1.readObject();
      this.mbeanHome = (MBeanHome)var1.readObject();
   }

   protected void writeObjectForCachingStubs(ObjectOutputStream var1) throws IOException {
      var1.writeObject(this.objectName);
      var1.writeObject(this.mbeanHome);
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

         this.info = null;
         this.mbeanHome = null;
         this.info = null;
      }
   }

   private RemoteMBeanServer getMBeanServer() {
      return this.mbeanServer != null ? this.mbeanServer : (this.mbeanServer = this.mbeanHome.getMBeanServer());
   }

   private Object getAttribute(String var1) throws AttributeNotFoundException, InstanceNotFoundException, ReflectionException, MBeanException {
      String var2 = null;
      if (var1.charAt(0) == 'g') {
         var2 = var1.substring(3);
      } else {
         var2 = var1.substring(2);
      }

      if (this.delegate != null) {
         try {
            return this.wrap(var2, this.delegate.getAttribute(var2));
         } catch (ManagementException var6) {
            throw new MBeanException(var6);
         }
      } else if (this.mbeanHome == null) {
         throw new InstanceNotFoundException(this.objectName.toString());
      } else {
         RemoteMBeanServer var3 = this.getMBeanServer();
         Object var4 = var3.getAttribute(this.objectName, var2);
         if ((this.unregistered || var4 == null) && this.mbeanHome.getMBean(this.objectName) == null) {
            throw new InstanceNotFoundException("MBean with " + this.objectName + " has been deleted or unregistered from the server");
         } else {
            try {
               var4 = this.wrap(var2, var4);
               return var4;
            } catch (ManagementException var7) {
               throw new MBeanException(var7);
            }
         }
      }
   }

   private Object getOneAttribute(Object var1) throws AttributeNotFoundException, InstanceNotFoundException, ReflectionException, MBeanException {
      if (this.delegate != null) {
         return this.delegate.getAttribute((String)var1);
      } else {
         RemoteMBeanServer var2 = this.getMBeanServer();
         Object var3 = var2.getAttribute(this.objectName, (String)var1);
         return var3;
      }
   }

   private Object getAttributes(Object var1) throws InstanceNotFoundException, ReflectionException {
      if (this.delegate != null) {
         return this.delegate.getAttributes((String[])((String[])var1));
      } else {
         RemoteMBeanServer var2 = this.getMBeanServer();
         AttributeList var3 = var2.getAttributes(this.objectName, (String[])((String[])var1));
         return var3;
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

   private MBeanOperationInfo getOperationInfo(String var1, Object[] var2) throws OperationsException, ClassNotFoundException {
      MBeanOperationInfo[] var3 = this.info.getOperations();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4].getName().equals(var1) && isAssignableFrom(var2, var3[var4].getSignature())) {
            return var3[var4];
         }
      }

      throw new OperationsException("no such operation: " + var1);
   }

   private final Object setAttribute(String var1, Object var2) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
      String var3 = var1.substring(3);
      if (this.delegate != null) {
         WebLogicAttribute var4 = new WebLogicAttribute(var3, var2);
         this.delegate.setAttribute(var4);
      } else {
         Object var11 = this.unwrap(var2);
         WebLogicAttribute var5 = new WebLogicAttribute(var3, var11);
         RemoteMBeanServer var6 = this.getMBeanServer();

         try {
            var6.setAttribute(this.objectName, var5);
            if (var5.getName().equals("Parent")) {
               this.setParent((WebLogicMBean)var2);
            }
         } catch (RuntimeErrorException var9) {
            ManagementLogger.logMBeanProxySetAttributeError(this.objectName, var1, var2, var11, var9.getTargetError());
            throw var9;
         } catch (RuntimeOperationsException var10) {
            RuntimeException var8 = var10.getTargetException();
            ManagementLogger.logExceptionInMBeanProxy(var8);
         }
      }

      return null;
   }

   private final Object setOneAttribute(Object var1) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
      if (this.delegate != null) {
         this.delegate.setAttribute((Attribute)var1);
      } else {
         Attribute var2 = (Attribute)var1;
         RemoteMBeanServer var3 = this.getMBeanServer();
         var3.setAttribute(this.objectName, this.unwrap(var2));
         if (var2.getName().equals("Parent")) {
            if (this.unwrap(var2) instanceof WebLogicMBean) {
               this.setParent((WebLogicMBean)this.unwrap(var2.getValue()));
            } else {
               try {
                  this.objectName = new WebLogicObjectName(this.objectName, (WebLogicObjectName)var2.getValue());
               } catch (MalformedObjectNameException var5) {
                  throw new ConfigurationError(var5);
               }
            }
         }
      }

      return null;
   }

   private final Object setAttributes(Object var1) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
      Object var2 = null;
      AttributeList var3 = null;
      if (this.delegate != null) {
         var3 = this.delegate.setAttributes((AttributeList)var1);
      } else {
         RemoteMBeanServer var4 = this.getMBeanServer();
         var3 = var4.setAttributes(this.objectName, this.unwrap((AttributeList)var1));
      }

      if (var2 != null) {
         this.setParent((WebLogicMBean)var2);
      }

      return var3;
   }

   private final void setParent(WebLogicMBean var1) {
      try {
         this.objectName = new WebLogicObjectName(this.objectName, var1.getObjectName());
      } catch (MalformedObjectNameException var3) {
         throw new ConfigurationError(var3);
      }
   }

   private final boolean equalsMBeanProxy(MBeanProxy var1) {
      boolean var10000;
      label25: {
         if (this.objectName.equals(var1.objectName)) {
            if (this.mbeanHome == null) {
               if (var1.mbeanHome == null) {
                  break label25;
               }
            } else if (this.mbeanHome.equals(var1.mbeanHome)) {
               break label25;
            }
         }

         var10000 = false;
         return var10000;
      }

      var10000 = true;
      return var10000;
   }

   private Object invoke(String var1, Object[] var2) throws InstanceNotFoundException, MBeanException, ReflectionException, ClassNotFoundException, OperationsException {
      RemoteMBeanServer var3 = this.getMBeanServer();
      MBeanOperationInfo var4 = this.getOperationInfo(var1, var2);
      Class var5 = findClass(var4.getReturnType());
      Object[] var6 = this.unwrap(var2);

      try {
         String[] var7;
         Object var8;
         if (this.delegate != null) {
            var7 = getSignature(var2);
            var8 = this.delegate.invoke(var1, var6, var7);
         } else {
            MBeanParameterInfo[] var9 = var4.getSignature();
            var7 = new String[var9.length];

            for(int var10 = 0; var10 < var7.length; ++var10) {
               var7[var10] = var9[var10].getType();
            }

            var8 = var3.invoke(this.objectName, var1, var6, var7);
         }

         return this.wrap(var5, var8);
      } catch (RuntimeErrorException var11) {
         throw var11.getTargetError();
      }
   }

   private Object dynamicInvoke(Object[] var1) throws InstanceNotFoundException, MBeanException, ReflectionException, ClassNotFoundException, OperationsException {
      RemoteMBeanServer var2 = this.getMBeanServer();
      MBeanOperationInfo var3 = this.getOperationInfo((String)var1[0], this.wrap((String[])((String[])var1[2]), (Object[])((Object[])var1[1])));
      Class var4 = findClass(var3.getReturnType());
      if (this.delegate != null) {
         return this.wrap(var4, this.delegate.invoke((String)var1[0], this.unwrap((Object[])((Object[])var1[1])), (String[])((String[])var1[2])));
      } else {
         try {
            return this.wrap(var4, var2.invoke(this.objectName, (String)var1[0], this.unwrap((Object[])((Object[])var1[1])), (String[])((String[])var1[2])));
         } catch (RuntimeErrorException var6) {
            throw var6.getTargetError();
         }
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
         return null == var2 ? null : new WebLogicAttribute(var2, var3);
      }
   }

   private Object[] unwrap(Object[] var1) {
      if (var1 == null) {
         return null;
      } else {
         Object[] var2 = new Object[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = this.unwrap(var1[var3]);
         }

         return var2;
      }
   }

   private Object unwrap(Object var1) {
      if (var1 instanceof WebLogicMBean) {
         return ((WebLogicMBean)var1).getObjectName();
      } else {
         int var4;
         if (var1 instanceof WebLogicMBean[]) {
            WebLogicMBean[] var5 = (WebLogicMBean[])((WebLogicMBean[])var1);
            WebLogicObjectName[] var6 = new WebLogicObjectName[var5.length];

            for(var4 = 0; var4 < var6.length; ++var4) {
               var6[var4] = var5[var4].getObjectName();
            }

            return var6;
         } else if (var1 instanceof StandardInterface) {
            return ((StandardInterface)var1).wls_getObjectName();
         } else if (!(var1 instanceof StandardInterface[])) {
            return var1;
         } else {
            StandardInterface[] var2 = (StandardInterface[])((StandardInterface[])var1);
            ObjectName[] var3 = new ObjectName[var2.length];

            for(var4 = 0; var4 < var3.length; ++var4) {
               var3[var4] = var2[var4].wls_getObjectName();
            }

            return var3;
         }
      }
   }

   private Object[] wrap(String[] var1, Object[] var2) {
      if (var2 != WebLogicAttribute.NULL_VALUE && var2 != null) {
         Object[] var3 = new Object[var2.length];

         for(int var4 = 0; var4 < var2.length; ++var4) {
            try {
               Class var5 = findClass(var1[var4]);
               var3[var4] = this.wrap(var5, var2[var4]);
            } catch (ClassNotFoundException var7) {
               var3[var4] = var2[var4];
            }
         }

         return var3;
      } else {
         return null;
      }
   }

   private Object wrap(Class var1, Object var2) {
      if (var2 != null && var2 != WebLogicAttribute.NULL_VALUE) {
         if (var1 == Void.class) {
            return null;
         } else if (ObjectName.class.isAssignableFrom(var1)) {
            return var2;
         } else {
            try {
               if (!(var2 instanceof ObjectName)) {
                  if (!(var2 instanceof ObjectName[])) {
                     return var2;
                  }

                  Class var11 = var1.getComponentType();
                  Object[] var12 = (Object[])((Object[])var2);
                  int var5 = var12.length;
                  Object[] var6 = (Object[])((Object[])Array.newInstance(var11, var5));

                  for(int var7 = 0; var7 < var12.length; ++var7) {
                     Object var8 = this.wrap(var11, var12[var7]);
                     var6[var7] = var8;
                  }

                  return var6;
               }

               ObjectName var3 = (ObjectName)var2;
               if (var3 instanceof WebLogicObjectName) {
                  WebLogicMBean var4 = this.mbeanHome.getMBean((ObjectName)var2);
                  return var4;
               }
            } catch (InstanceNotFoundException var9) {
               return null;
            } catch (Throwable var10) {
               ManagementLogger.logExceptionInMBeanProxy(var10);
            }

            return var2;
         }
      } else {
         return null;
      }
   }

   private Object wrap(String var1, Object var2) throws ManagementException {
      if (var2 == WebLogicAttribute.NULL_VALUE) {
         return null;
      } else {
         try {
            if (var1.equals("ObjectName")) {
               return var2;
            } else {
               MBeanAttributeInfo var3 = this.getAttributeInfo(var1);
               Class var4 = findClass(var3.getType());
               return this.wrap(var4, var2);
            }
         } catch (ClassNotFoundException var5) {
            throw new ManagementException("error wrapping " + var1 + ", value=" + var2, var5);
         } catch (AttributeNotFoundException var6) {
            throw new ManagementException("error wrapping " + var1 + ", value=" + var2, var6);
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
         var3 = new AttributeChangeNotification(this.objectName, var4, var2, this.wrap(var4, var6), this.wrap(var4, var7));
      } else {
         var3 = var1;
      }

      return (Notification)var3;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      try {
         var1.defaultWriteObject();
         var1.writeObject(((ModelMBeanInfo)this.info).getMBeanDescriptor().getFieldValue("interfaceclassname"));
         var1.writeBoolean(this.objectName.isAdmin() || this.objectName.isConfig());
      } catch (MBeanException var3) {
         throw new IOException(var3.toString());
      }
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.info = findExtendedInfo(var1);
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

   private static boolean isAssignableFrom(Object[] var0, MBeanParameterInfo[] var1) throws ClassNotFoundException {
      if (var0 == null) {
         return var1.length == 0;
      } else if (var0.length != var1.length) {
         return false;
      } else {
         Class[] var2 = new Class[var0.length];
         Class[] var3 = new Class[var1.length];

         int var4;
         for(var4 = 0; var4 < var0.length; ++var4) {
            var2[var4] = var0[var4] == null ? null : var0[var4].getClass();
         }

         for(var4 = 0; var4 < var0.length; ++var4) {
            String var5 = var1[var4].getType();
            var3[var4] = var5 == null ? Void.class : wrapClass(findClass(var5));
         }

         for(var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4] != null && !var3[var4].isAssignableFrom(var2[var4])) {
               return false;
            }
         }

         return true;
      }
   }

   private static String[] getSignature(Object[] var0) {
      String[] var1 = null;
      if (var0 != null) {
         var1 = new String[var0.length];

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var0[var2] != null) {
               if (WebLogicMBean.class.isAssignableFrom(var0[var2].getClass())) {
                  var1[var2] = "javax.management.ObjectName";
               } else {
                  var1[var2] = var0[var2].getClass().getName();
               }
            } else {
               var1[var2] = "void";
            }
         }
      } else {
         String[] var3 = EMPTY_SIGNATURE;
      }

      return var1;
   }

   private static ExtendedInfo findExtendedInfo(ObjectInput var0) throws IOException, ClassNotFoundException {
      String var1 = (String)var0.readObject();
      boolean var2 = var0.readBoolean();
      if (var1 == null) {
         return null;
      } else {
         try {
            return var2 && isAdminOrConfigMBeanClass(var1) ? getAdminOrConfigMBeanInfo(mbeanType(var1)) : getMBeanInfo(findClass(var1));
         } catch (ConfigurationException var5) {
            Throwable var4 = var5.getNestedException();
            if (var4 instanceof ClassNotFoundException) {
               throw (ClassNotFoundException)var4;
            } else if (var4 instanceof IOException) {
               throw (IOException)var4;
            } else {
               throw new IOException(var4.toString());
            }
         }
      }
   }

   private static Class findClass(String var0) throws ClassNotFoundException {
      if (var0.equals(Long.class.getName())) {
         return Long.TYPE;
      } else if (var0.equals(Double.class.getName())) {
         return Double.TYPE;
      } else if (var0.equals(Float.class.getName())) {
         return Float.TYPE;
      } else if (var0.equals(Integer.class.getName())) {
         return Integer.TYPE;
      } else if (var0.equals(Character.class.getName())) {
         return Character.TYPE;
      } else if (var0.equals(Short.class.getName())) {
         return Short.TYPE;
      } else if (var0.equals(Byte.class.getName())) {
         return Byte.TYPE;
      } else if (var0.equals(Boolean.class.getName())) {
         return Boolean.TYPE;
      } else if (var0.equals(Void.class.getName())) {
         return Void.TYPE;
      } else if (var0.equals("long")) {
         return Long.TYPE;
      } else if (var0.equals("double")) {
         return Double.TYPE;
      } else if (var0.equals("float")) {
         return Float.TYPE;
      } else if (var0.equals("int")) {
         return Integer.TYPE;
      } else if (var0.equals("char")) {
         return Character.TYPE;
      } else if (var0.equals("short")) {
         return Short.TYPE;
      } else if (var0.equals("byte")) {
         return Byte.TYPE;
      } else if (var0.equals("boolean")) {
         return Boolean.TYPE;
      } else if (var0.equals("void")) {
         return Void.TYPE;
      } else if (var0.endsWith("[]")) {
         Class var1 = findClass(var0.substring(0, var0.length() - 2));
         return Array.newInstance(var1, 0).getClass();
      } else {
         return Class.forName(var0);
      }
   }

   private static Class wrapClass(Class var0) {
      if (var0 == Long.TYPE) {
         return Long.class;
      } else if (var0 == Double.TYPE) {
         return Double.class;
      } else if (var0 == Float.TYPE) {
         return Float.class;
      } else if (var0 == Integer.TYPE) {
         return Integer.class;
      } else if (var0 == Character.TYPE) {
         return Character.class;
      } else if (var0 == Short.TYPE) {
         return Short.class;
      } else if (var0 == Byte.TYPE) {
         return Byte.class;
      } else {
         return var0 == Boolean.TYPE ? Boolean.class : var0;
      }
   }

   private static ExtendedInfo getAdminOrConfigMBeanInfo(String var0) {
      try {
         ExtendedInfo var1 = null;
         boolean var2 = var0.endsWith("Config");
         if (var2) {
            var0 = getAdminType(var0);
            var1 = (ExtendedInfo)configMBeanInfos.get(var0);
         } else {
            var1 = (ExtendedInfo)adminMBeanInfos.get(var0);
         }

         if (var1 != null) {
            return var1;
         } else {
            Class var3 = findClass("weblogic.management.configuration." + var0 + "MBean");
            var1 = getMBeanInfo(var3);
            if (var2) {
               configMBeanInfos.put(var0, var1);
            } else {
               adminMBeanInfos.put(var0, var1);
            }

            return var1;
         }
      } catch (ClassNotFoundException var4) {
         throw new ConfigurationError(var4);
      } catch (ConfigurationException var5) {
         throw new ConfigurationError(var5);
      }
   }

   private static ExtendedInfo getMBeanInfo(Class var0) throws ConfigurationException {
      ExtendedInfo var1 = (ExtendedInfo)mbeanInfos.get(var0);
      if (var1 == null) {
         StringBuffer var2 = new StringBuffer(var0.getName());

         for(int var3 = 0; var3 < var2.length(); ++var3) {
            if (var2.charAt(var3) == '.') {
               var2.setCharAt(var3, '/');
            }
         }

         var2.append(".mbi");
         ClassLoader var8 = var0.getClassLoader();
         InputStream var4 = null;
         if (var8 != null) {
            var4 = var8.getResourceAsStream(var2.toString());
         } else {
            var4 = ClassLoader.getSystemResourceAsStream(var2.toString());
         }

         if (var4 == null) {
            return null;
         }

         try {
            BufferedInputStream var9 = new BufferedInputStream(var4);
            ObjectInputStream var5 = new ObjectInputStream(var9);
            var1 = (ExtendedInfo)var5.readObject();
            var5.close();
         } catch (ClassNotFoundException var6) {
            throw new ConfigurationException(var6);
         } catch (IOException var7) {
            throw new ConfigurationException(var7);
         }

         if (var1 == null) {
            throw new ConfigurationException("no info found for " + var0);
         }

         mbeanInfos.put(var0, var1);
      }

      return var1;
   }

   private static String mbeanType(String var0) {
      String var1 = trimPackage(var0);
      if (var1.endsWith("MBean")) {
         var1 = var1.substring(0, var1.length() - 5);
      }

      return var1;
   }

   private static String trimPackage(String var0) {
      int var1 = var0.lastIndexOf(46);
      int var2 = var0.length();
      if (var1 != -1) {
         var0 = var0.substring(var1 + 1, var2);
      }

      return var0;
   }

   private static boolean isAdminOrConfigMBeanClass(String var0) {
      try {
         Class.forName(var0);
         int var1 = var0.lastIndexOf(46);
         if (var1 != -1) {
            var0 = var0.substring(0, var1);
            return var0.equals("weblogic.management.configuration");
         } else {
            return false;
         }
      } catch (ClassNotFoundException var2) {
         return false;
      }
   }

   private static String getAdminType(String var0) {
      if (var0.endsWith("Config")) {
         var0 = var0.substring(0, var0.length() - "Config".length());
      }

      return var0;
   }
}
