package weblogic.wsee.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
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
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeType;

public class CompositeDataProxyFactory {
   private PrintWriter _logWriter;
   private boolean _isLogEnabled = false;

   public CompositeDataProxyFactory(PrintWriter var1) throws Exception {
      this._logWriter = var1;
      this._isLogEnabled = this.isLogEnabled();
   }

   public <T> T createProxy(ClassLoader var1, Class<T> var2, CompositeData var3) throws IntrospectionException {
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

   private void log(String var1) {
      if (this._logWriter != null) {
         this._logWriter.println(var1);
      }

   }

   private boolean isLogEnabled() {
      return this._logWriter != null;
   }

   class MyInvocationHandler<T> implements InvocationHandler {
      private Class _intfClass;
      private CompositeData _data;
      private Map<Method, MyInvocationHandler<T>.Info> _methodToInfoMap;
      private T _proxy;

      public MyInvocationHandler(Class var2, CompositeData var3) throws IntrospectionException {
         this._intfClass = var2;
         this._data = var3;
         if (CompositeDataProxyFactory.this._isLogEnabled) {
            CompositeDataProxyFactory.this.log("Looking for " + this._intfClass.getName() + " to wrap: " + this._data);
         }

         CompositeType var4 = this._data.getCompositeType();
         Maps var5 = this.getMapsFromCompositeDataClass(this._intfClass);
         Map var6 = var5.propDescMap;
         this._methodToInfoMap = new HashMap();
         Iterator var7 = var4.keySet().iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();
            String var9 = Introspector.decapitalize(var8);
            PropertyDescriptor var10 = (PropertyDescriptor)var6.get(var9);
            if (var10 == null) {
               var10 = (PropertyDescriptor)var6.get(var8);
            }

            if (var10 == null) {
               throw new IllegalArgumentException("Didn't find a JavaBean property (" + var8 + " or " + var9 + ") for CompositeData item " + var8 + " on interface: " + this._intfClass.getName());
            }

            Info var11;
            if (var10.getWriteMethod() != null) {
               var11 = new Info();
               var11.javaType = var10.getPropertyType();
               var11.direction = CompositeDataProxyFactory.Direction.SET;
               var11.itemName = var8;
               this._methodToInfoMap.put(var10.getWriteMethod(), var11);
            }

            var11 = new Info();
            var11.javaType = var10.getPropertyType();
            var11.direction = CompositeDataProxyFactory.Direction.GET;
            var11.itemName = var8;
            this._methodToInfoMap.put(var10.getReadMethod(), var11);
         }

      }

      public void setProxy(T var1) {
         this._proxy = var1;
      }

      private MyInvocationHandler<T>.Maps getMapsFromCompositeDataClass(Class var1) throws IntrospectionException {
         Stack var2 = this.findInterfaces(var1);
         Maps var3 = new Maps();

         while(!var2.isEmpty()) {
            Class var4 = (Class)var2.pop();
            this.updateMapsFromCompositeDataClass(var4, var3);
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

      private void updateMapsFromCompositeDataClass(Class var1, MyInvocationHandler<T>.Maps var2) throws IntrospectionException {
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
            } else if (var4.direction == CompositeDataProxyFactory.Direction.SET) {
               throw new IllegalArgumentException("CompositeData is read-only");
            } else {
               Object var5 = this._data.get(var4.itemName);
               if (var4.javaType.isArray()) {
                  Class var6 = var4.javaType.getComponentType();
                  if (this.isCompositeDataType(var6) && var5 != null) {
                     CompositeData[] var7 = (CompositeData[])((CompositeData[])var5);
                     var5 = Array.newInstance(var6, var7.length);
                     int var8 = 0;
                     CompositeData[] var9 = var7;
                     int var10 = var7.length;

                     for(int var11 = 0; var11 < var10; ++var11) {
                        CompositeData var12 = var9[var11];
                        Object var13 = CompositeDataProxyFactory.this.createProxy(this.getClass().getClassLoader(), var6, var12);
                        Array.set(var5, var8, var13);
                        ++var8;
                     }
                  }
               } else if (this.isCompositeDataType(var4.javaType)) {
                  var5 = CompositeDataProxyFactory.this.createProxy(this.getClass().getClassLoader(), var4.javaType, (CompositeData)var5);
               }

               return var5;
            }
         }
      }

      private boolean isCompositeDataType(Class var1) {
         boolean var2 = !var1.isArray() && (var1.getName().startsWith("java.lang") || var1.isPrimitive());
         return !var2;
      }

      public String toString() {
         int var1 = 0;
         StringBuffer var2 = new StringBuffer();
         this.indent(var1, var2).append("Type Name: ").append(this._data.getCompositeType().getTypeName()).append("\n");
         this.indent(var1, var2).append("Description: ").append(this._data.getCompositeType().getDescription()).append("\n");
         this.indent(var1, var2).append("Items: ").append(this._data.getCompositeType().getDescription()).append("\n");
         var1 = 1;
         Iterator var3 = this._methodToInfoMap.keySet().iterator();

         while(true) {
            Method var4;
            Info var5;
            do {
               if (!var3.hasNext()) {
                  return var2.toString();
               }

               var4 = (Method)var3.next();
               var5 = (Info)this._methodToInfoMap.get(var4);
            } while(var5.direction == CompositeDataProxyFactory.Direction.SET);

            try {
               Object var6 = var4.invoke(this._proxy);
               if (var6 != null && var6.getClass().isArray()) {
                  this.indent(var1, var2).append(var5.itemName).append(": ").append(var6.getClass()).append("\n");
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
                  this.indent(var1, var2).append(var5.itemName).append(": ").append(var6).append("\n");
               }
            } catch (Exception var14) {
               throw new RuntimeException(var14.toString(), var14);
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
         Class javaType;
         Direction direction;
         String itemName;
      }
   }

   static enum Direction {
      SET,
      GET;
   }
}
