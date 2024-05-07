package weblogic.wsee.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import weblogic.management.WebLogicMBean;
import weblogic.management.WebLogicObjectName;
import weblogic.management.jmx.modelmbean.WLSModelMBean;
import weblogic.management.provider.Service;

public class MBeanProxyFactory {
   private PrintWriter _logWriter;
   private JMXConnector _jmxConnector;
   private MBeanServerConnection _conn;
   private LoginInfo _loginInfo;
   private boolean _isLogEnabled = false;

   public MBeanProxyFactory(MBeanServerConnection var1, PrintWriter var2) throws Exception {
      this._logWriter = var2;
      this._conn = var1;
      this._isLogEnabled = this.isLogEnabled();
   }

   public MBeanProxyFactory(LoginInfo var1, PrintWriter var2) throws Exception {
      this._loginInfo = var1;
      this._logWriter = var2;
      this._conn = this.getMBeanServerConnection();
      this._isLogEnabled = this.isLogEnabled();
   }

   public <T> T createProxy(ClassLoader var1, Class<T> var2, ObjectName var3) throws ReflectionException, InstanceNotFoundException, IOException, IntrospectionException, javax.management.IntrospectionException {
      MyInvocationHandler var4 = new MyInvocationHandler(var2, var3);
      if (var1 == null) {
         var1 = Thread.currentThread().getContextClassLoader();
         if (var1 == null) {
            var1 = this.getClass().getClassLoader();
         }
      }

      Object var5 = Proxy.newProxyInstance(var1, new Class[]{var2}, var4);
      var4.setProxy(var5);
      return var5;
   }

   public MBeanServerConnection getMBeanServerConnection() throws Exception {
      try {
         this.dumpEffectiveConnectionProps();
         JMXConnector var1 = this.getJMXConnector();
         this.dumpEffectiveConnectionProps();
         return var1.getMBeanServerConnection();
      } catch (Exception var2) {
         throw new Exception(var2.toString(), var2);
      }
   }

   private void dumpEffectiveConnectionProps() {
      if (this._isLogEnabled) {
         this.log("--- Effective prop values: ---");
         this.log(this._loginInfo.toString());
         this.log("--- End --");
      }

   }

   private JMXConnector getJMXConnector() throws Exception {
      if (this._jmxConnector != null) {
         return this._jmxConnector;
      } else {
         String var1 = "/jndi/" + this._loginInfo.jmxJndiName;
         JMXServiceURL var2 = new JMXServiceURL("t3", this._loginInfo.adminHost, this._loginInfo.adminPort, var1);
         HashMap var3 = new HashMap();
         if (this._loginInfo.userName != null && this._loginInfo.password != null) {
            var3.put("java.naming.security.principal", this._loginInfo.userName);
            var3.put("java.naming.security.credentials", this._loginInfo.password);
            String[] var4 = new String[]{this._loginInfo.userName, this._loginInfo.password};
            var3.put("jmx.remote.credentials", var4);
         }

         var3.put("jmx.remote.protocol.provider.pkgs", "weblogic.management.remote");
         this._jmxConnector = JMXConnectorFactory.connect(var2, var3);
         return this._jmxConnector;
      }
   }

   public void dispose() throws IOException {
      if (this._jmxConnector != null) {
         this._jmxConnector.close();
      }

   }

   private Object invokeOperation(MBeanServerConnection var1, ObjectName var2, MyInvocationHandler.Info var3, Object[] var4) throws ReflectionException, IOException, InstanceNotFoundException, MBeanException {
      MBeanOperationInfo var5 = var3.opInfo;
      if (this._isLogEnabled) {
         this.log("Invoking " + var5.getName());
      }

      if (var4 == null) {
         var4 = new Class[0];
      }

      if (var3.argTypes.length != ((Object[])var4).length) {
         throw new IllegalArgumentException("Wrong number of args. Expected " + var3.argTypes.length + " but got " + ((Object[])var4).length);
      } else {
         for(int var6 = 0; var6 < ((Object[])var4).length; ++var6) {
            if (isMBeanType(var3.argTypes[var6])) {
               ((Object[])var4)[var6] = getMBeanObjectName(((Object[])var4)[var6]);
            }
         }

         long var12 = System.currentTimeMillis();
         String[] var8 = new String[var5.getSignature().length];

         for(int var9 = 0; var9 < var5.getSignature().length; ++var9) {
            var8[var9] = var5.getSignature()[var9].getType();
         }

         Object var13 = var1.invoke(var2, var5.getName(), (Object[])var4, var8);
         long var10 = System.currentTimeMillis() - var12;
         if (this._isLogEnabled) {
            this.log("Done. Operation took " + (float)var10 / 1000.0F + " seconds");
         }

         return var13;
      }
   }

   private void setAttribute(MBeanServerConnection var1, ObjectName var2, MBeanAttributeInfo var3, Object[] var4) throws ReflectionException, IOException, InstanceNotFoundException, MBeanException, AttributeNotFoundException, InvalidAttributeValueException {
      if (this._isLogEnabled) {
         this.log("Setting attr " + var3.getName());
      }

      long var5 = System.currentTimeMillis();
      Attribute var7 = new Attribute(var3.getName(), var4[0]);
      var1.setAttribute(var2, var7);
      long var8 = System.currentTimeMillis() - var5;
      if (this._isLogEnabled) {
         this.log("Done. Set attr took " + (float)var8 / 1000.0F + " seconds");
      }

   }

   private Object getAttribute(MBeanServerConnection var1, ObjectName var2, MBeanAttributeInfo var3) throws ReflectionException, IOException, InstanceNotFoundException, MBeanException, AttributeNotFoundException {
      if (this._isLogEnabled) {
         this.log("Getting attr " + var3.getName());
      }

      long var4 = System.currentTimeMillis();
      Object var6 = var1.getAttribute(var2, var3.getName());
      long var7 = System.currentTimeMillis() - var4;
      if (this._isLogEnabled) {
         this.log("Done. Get attr took " + (float)var7 / 1000.0F + " seconds");
      }

      return var6;
   }

   private void log(String var1) {
      if (this._logWriter != null) {
         this._logWriter.println(var1);
      }

   }

   private boolean isLogEnabled() {
      return this._logWriter != null;
   }

   private static boolean isMBeanType(Class var0) {
      return WebLogicMBean.class.isAssignableFrom(var0) || WLSModelMBean.class.isAssignableFrom(var0) || Service.class.isAssignableFrom(var0);
   }

   private static ObjectName getMBeanObjectName(Object var0) {
      if (Proxy.isProxyClass(var0.getClass())) {
         InvocationHandler var1 = Proxy.getInvocationHandler(var0);
         if (MyInvocationHandler.class.isAssignableFrom(var1.getClass())) {
            MyInvocationHandler var2 = (MyInvocationHandler)var1;
            return var2._objectName;
         }
      }

      try {
         if (WebLogicMBean.class.isAssignableFrom(var0.getClass())) {
            WebLogicObjectName var5 = ((WebLogicMBean)var0).getObjectName();
            return var5;
         } else if (WLSModelMBean.class.isAssignableFrom(var0.getClass())) {
            ObjectName var4 = ((WLSModelMBean)var0).getObjectName();
            return var4;
         } else {
            throw new IllegalArgumentException("Attempt to extract an ObjectName from non-MBean object: " + var0);
         }
      } catch (Exception var3) {
         throw new RuntimeException(var3.toString(), var3);
      }
   }

   public static class LoginInfo implements Cloneable {
      public String adminHost;
      public int adminPort;
      public String userName;
      public String password;
      public String jmxJndiName;

      public LoginInfo() {
         this(MBeanProxyFactory.LoginInfo.Type.DOMAIN_RUNTIME);
      }

      public LoginInfo(Type var1) {
         switch (var1) {
            case CONFIG:
               this.jmxJndiName = "weblogic.management.mbeanservers.edit";
               break;
            case RUNTIME:
               this.jmxJndiName = "weblogic.management.mbeanservers.runtime";
               break;
            default:
               this.jmxJndiName = "weblogic.management.mbeanservers.domainruntime";
         }

      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("Admin Server Host:     ").append(this.adminHost).append("\n");
         var1.append("Admin Server Port:     ").append(this.adminPort).append("\n");
         var1.append("Admin User Name:       ").append(this.userName).append("\n");
         var1.append("JMX JNDI Name:         ").append(this.jmxJndiName).append("\n");
         return var1.toString();
      }

      public Object clone() throws CloneNotSupportedException {
         return super.clone();
      }

      public static enum Type {
         DOMAIN_RUNTIME,
         RUNTIME,
         CONFIG;
      }
   }

   class MyInvocationHandler<T> implements InvocationHandler {
      private Class _intfClass;
      private ObjectName _objectName;
      private Map<Method, MyInvocationHandler<T>.Info> _methodToInfoMap;
      private T _proxy;

      public MyInvocationHandler(Class var2, ObjectName var3) throws ReflectionException, IOException, InstanceNotFoundException, javax.management.IntrospectionException, IntrospectionException {
         this._intfClass = var2;
         this._objectName = var3;
         if (MBeanProxyFactory.this._isLogEnabled) {
            MBeanProxyFactory.this.log("Looking for " + this._intfClass.getName() + " named: " + this._objectName);
         }

         MBeanInfo var4 = MBeanProxyFactory.this._conn.getMBeanInfo(this._objectName);
         if (MBeanProxyFactory.this._isLogEnabled) {
            MBeanProxyFactory.this.log("MBeanInfo for: " + this._objectName);
         }

         MBeanAttributeInfo[] var5 = var4.getAttributes();
         HashMap var6 = new HashMap();
         if (MBeanProxyFactory.this._isLogEnabled) {
            MBeanProxyFactory.this.log("Attributes:");
         }

         MBeanAttributeInfo[] var7 = var5;
         int var8 = var5.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            MBeanAttributeInfo var10 = var7[var9];
            if (MBeanProxyFactory.this._isLogEnabled) {
               MBeanProxyFactory.this.log("    Attribute: " + var10.getName() + " type: " + var10.getType());
            }

            var6.put(var10.getName(), var10);
         }

         MBeanOperationInfo[] var18 = var4.getOperations();
         HashMap var19 = new HashMap();
         if (MBeanProxyFactory.this._isLogEnabled) {
            MBeanProxyFactory.this.log("Operations:");
         }

         MBeanOperationInfo[] var20 = var18;
         int var22 = var18.length;

         for(int var11 = 0; var11 < var22; ++var11) {
            MBeanOperationInfo var12 = var20[var11];
            if (MBeanProxyFactory.this._isLogEnabled) {
               MBeanProxyFactory.this.log("    Operation: " + var12.getName() + " return type: " + var12.getReturnType());
            }

            var19.put(var12.getName(), var12);
         }

         Maps var21 = this.getMapsFromBeanClass(this._intfClass);
         Map var23 = var21.propDescMap;
         Map var24 = var21.methodDescMap;
         this._methodToInfoMap = new HashMap();
         Iterator var25 = var6.values().iterator();

         String var14;
         while(var25.hasNext()) {
            MBeanAttributeInfo var13 = (MBeanAttributeInfo)var25.next();
            var14 = var13.getName();
            String var15 = Introspector.decapitalize(var13.getName());
            PropertyDescriptor var16 = (PropertyDescriptor)var23.get(var15);
            if (var16 == null) {
               var16 = (PropertyDescriptor)var23.get(var14);
            }

            if (var16 == null) {
               throw new IllegalArgumentException("Didn't find a JavaBean property (" + var14 + " or " + var15 + ") for MBean attribute " + var13.getName() + " on interface: " + this._intfClass.getName());
            }

            Info var17;
            if (var16.getWriteMethod() != null) {
               var17 = new Info();
               var17.type = MBeanProxyFactory.Type.ATTR;
               var17.javaType = var16.getPropertyType();
               var17.argTypes = new Class[0];
               var17.direction = MBeanProxyFactory.Direction.SET;
               var17.attrInfo = var13;
               var17.name = var13.getName();
               this._methodToInfoMap.put(var16.getWriteMethod(), var17);
            }

            var17 = new Info();
            var17.type = MBeanProxyFactory.Type.ATTR;
            var17.javaType = var16.getPropertyType();
            var17.argTypes = new Class[0];
            var17.direction = MBeanProxyFactory.Direction.GET;
            var17.attrInfo = var13;
            var17.name = var13.getName();
            if (var16.getReadMethod() != null) {
               this._methodToInfoMap.put(var16.getReadMethod(), var17);
            }
         }

         var25 = var19.values().iterator();

         while(var25.hasNext()) {
            MBeanOperationInfo var26 = (MBeanOperationInfo)var25.next();
            var14 = var26.getName();
            MethodDescriptor var27 = (MethodDescriptor)var24.get(var14);
            if (var27 == null) {
               throw new IllegalArgumentException("Didn't find an JavaBean method " + var14 + " for MBean operation " + var26.getName() + " in interface: " + this._intfClass);
            }

            Info var28 = new Info();
            var28.type = MBeanProxyFactory.Type.OP;
            var28.javaType = var27.getMethod().getReturnType();
            Class[] var29 = var27.getMethod().getParameterTypes();
            if (var29.length > 0) {
               var28.argTypes = var29;
            } else {
               var28.argTypes = new Class[0];
            }

            var28.opInfo = var26;
            var28.name = var26.getName();
            this._methodToInfoMap.put(var27.getMethod(), var28);
         }

      }

      public void setProxy(T var1) {
         this._proxy = var1;
      }

      private MyInvocationHandler<T>.Maps getMapsFromBeanClass(Class var1) throws IntrospectionException {
         Stack var2 = this.findInterfaces(var1);
         Maps var3 = new Maps();

         while(!var2.isEmpty()) {
            Class var4 = (Class)var2.pop();
            this.updateMapsFromBeanClass(var4, var3);
         }

         return var3;
      }

      private Stack<Class> findInterfaces(Class var1) {
         HashSet var2 = new HashSet();
         Stack var3 = new Stack();
         this.findInterfaces(var1, var2, var3);
         return var3;
      }

      private void findInterfaces(Class var1, Set<Class> var2, Stack<Class> var3) {
         if (!var2.contains(var1)) {
            var2.add(var1);
            var3.push(var1);
            Class[] var4 = var1.getInterfaces();
            Class[] var5 = var4;
            int var6 = var4.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               Class var8 = var5[var7];
               this.findInterfaces(var8, var2, var3);
            }

         }
      }

      private void updateMapsFromBeanClass(Class var1, MyInvocationHandler<T>.Maps var2) throws IntrospectionException {
         BeanInfo var3 = Introspector.getBeanInfo(var1);
         PropertyDescriptor[] var4 = var3.getPropertyDescriptors();
         PropertyDescriptor[] var5 = var4;
         int var6 = var4.length;

         int var7;
         for(var7 = 0; var7 < var6; ++var7) {
            PropertyDescriptor var8 = var5[var7];
            var2.propDescMap.put(var8.getName(), var8);
         }

         MethodDescriptor[] var10 = var3.getMethodDescriptors();
         MethodDescriptor[] var11 = var10;
         var7 = var10.length;

         for(int var12 = 0; var12 < var7; ++var12) {
            MethodDescriptor var9 = var11[var12];
            var2.methodDescMap.put(var9.getName(), var9);
         }

      }

      public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
         if (var2.getDeclaringClass() == Object.class) {
            return var2.invoke(this, var3);
         } else {
            Info var4 = (Info)this._methodToInfoMap.get(var2);
            if (var4 == null) {
               throw new IllegalArgumentException("Don't know how to handle method " + var2.getName() + " on interface: " + this._intfClass.getName());
            } else {
               Object var5 = null;
               if (var4.type == MBeanProxyFactory.Type.ATTR) {
                  if (var4.direction == MBeanProxyFactory.Direction.SET) {
                     MBeanProxyFactory.this.setAttribute(MBeanProxyFactory.this._conn, this._objectName, var4.attrInfo, var3);
                  } else {
                     var5 = MBeanProxyFactory.this.getAttribute(MBeanProxyFactory.this._conn, this._objectName, var4.attrInfo);
                  }
               } else {
                  var5 = MBeanProxyFactory.this.invokeOperation(MBeanProxyFactory.this._conn, this._objectName, var4, var3);
               }

               if (var5 == null) {
                  return var5;
               } else {
                  Class var6;
                  if (MBeanProxyFactory.isMBeanType(var4.javaType)) {
                     var6 = this.getJavaTypeFromObjectName((ObjectName)var5, var4.javaType);
                     var5 = MBeanProxyFactory.this.createProxy(this.getClass().getClassLoader(), var6, (ObjectName)var5);
                  } else if (var4.javaType.isArray()) {
                     var6 = var4.javaType.getComponentType();
                     if (MBeanProxyFactory.isMBeanType(var6)) {
                        ObjectName[] var7 = (ObjectName[])((ObjectName[])var5);
                        var5 = Array.newInstance(var6, var7.length);
                        int var8 = 0;
                        ObjectName[] var9 = var7;
                        int var10 = var7.length;

                        for(int var11 = 0; var11 < var10; ++var11) {
                           ObjectName var12 = var9[var11];
                           Class var13 = this.getJavaTypeFromObjectName(var12, var6);
                           Object var14 = MBeanProxyFactory.this.createProxy(this.getClass().getClassLoader(), var13, var12);
                           Array.set(var5, var8, var14);
                           ++var8;
                        }
                     }
                  }

                  return var5;
               }
            }
         }
      }

      private Class getJavaTypeFromObjectName(ObjectName var1, Class var2) {
         String var3 = var1.getKeyProperty("Type");
         if (var3 != null) {
            if (var3.startsWith("[")) {
               return var2;
            }

            String var4 = "";
            if (!var3.startsWith("weblogic.")) {
               var4 = var3.endsWith("Runtime") ? "weblogic.management.runtime." : "weblogic.management.configuration.";
            }

            String var5 = var4 + var3 + "MBean";
            if (MBeanProxyFactory.this.isLogEnabled()) {
               MBeanProxyFactory.this.log("Attempting to load specific MBean subclass of " + var2.getName() + " with class: " + var5);
            }

            try {
               Class var6 = this.getClass().getClassLoader().loadClass(var5);
               if (var2.isAssignableFrom(var6)) {
                  if (MBeanProxyFactory.this.isLogEnabled()) {
                     MBeanProxyFactory.this.log("Using specific MBean subclass of " + var2.getName() + " with class: " + var5);
                  }

                  var2 = var6;
               }
            } catch (Exception var7) {
            }
         }

         return var2;
      }

      public int hashCode() {
         return this._objectName.hashCode();
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof MyInvocationHandler)) {
            return false;
         } else {
            MyInvocationHandler var2 = (MyInvocationHandler)var1;
            return var2._objectName.equals(this._objectName);
         }
      }

      public String toString() {
         int var1 = 0;
         StringBuffer var2 = new StringBuffer();
         this.indent(var1, var2).append("Object Name: ").append(this._objectName).append("\n");
         var1 = 1;
         Iterator var3 = this._methodToInfoMap.keySet().iterator();

         while(true) {
            Method var4;
            Info var5;
            do {
               do {
                  if (!var3.hasNext()) {
                     return var2.toString();
                  }

                  var4 = (Method)var3.next();
                  var5 = (Info)this._methodToInfoMap.get(var4);
               } while(var5.direction == MBeanProxyFactory.Direction.SET);
            } while(var5.name.equalsIgnoreCase("parent") && var4.getDeclaringClass() == WebLogicMBean.class);

            try {
               Object var6 = null;

               try {
                  var6 = var4.invoke(this._proxy);
               } catch (Exception var14) {
               }

               if (var6 != null && var6.getClass().isArray()) {
                  this.indent(var1, var2).append(var5.name).append(": ").append(var6.getClass()).append("\n");
                  ++var1;
                  Object[] var7 = (Object[])((Object[])var6);
                  int var8 = 0;
                  Object[] var9 = var7;
                  int var10 = var7.length;

                  for(int var11 = 0; var11 < var10; ++var11) {
                     Object var12 = var9[var11];
                     this.indent(var1, var2).append("[").append(var8).append("]:").append("\n");
                     ++var1;
                     String var13 = var12.toString();
                     var13 = this.indentString(var1, var13);
                     var2.append(var13).append("\n");
                     --var1;
                     ++var8;
                  }

                  --var1;
               } else {
                  this.indent(var1, var2).append(var5.name).append(": ").append(var6).append("\n");
               }
            } catch (Exception var15) {
               throw new RuntimeException(var15.toString(), var15);
            }
         }
      }

      private String indentString(int var1, String var2) {
         StringBuffer var3 = new StringBuffer();
         StringTokenizer var4 = new StringTokenizer(var2, "\n");

         while(var4.hasMoreTokens()) {
            String var5 = var4.nextToken();
            this.indent(var1, var3).append(var5).append("\n");
         }

         return var3.toString();
      }

      private StringBuffer indent(int var1, StringBuffer var2) {
         for(int var3 = 0; var3 < var1; ++var3) {
            var2.append("    ");
         }

         return var2;
      }

      class Maps {
         public Map<String, PropertyDescriptor> propDescMap = new HashMap();
         public Map<String, MethodDescriptor> methodDescMap = new HashMap();
      }

      class Info {
         Type type;
         Class javaType;
         Class[] argTypes;
         Direction direction;
         String name;
         MBeanAttributeInfo attrInfo;
         MBeanOperationInfo opInfo;
      }
   }

   static enum Direction {
      SET,
      GET;
   }

   static enum Type {
      ATTR,
      OP;
   }
}
