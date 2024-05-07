package weblogic.metadata.management;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import weblogic.j2ee.descriptor.wl.AnnotationInstanceBean;
import weblogic.j2ee.descriptor.wl.ArrayMemberBean;
import weblogic.j2ee.descriptor.wl.MemberBean;
import weblogic.j2ee.descriptor.wl.NestedAnnotationBean;

public class AnnotationProxy implements InvocationHandler {
   private AnnotationInstanceBean _annotationOverrides;
   private String _annotationClassName;
   private HashSet<String> _annotationMembers = new HashSet();
   private Annotation _originalAnnotation;

   public static Annotation newInstance(Class var0, AnnotationInstanceBean var1) {
      ClassLoader var2 = var0.getClassLoader();
      Class[] var3 = var0.getInterfaces();
      Object var4 = Proxy.newProxyInstance(var2, var3, new AnnotationProxy(var0, var1));
      return (Annotation)var4;
   }

   private AnnotationProxy(Class var1, AnnotationInstanceBean var2) {
      this._annotationClassName = var1.getName();
      this._annotationOverrides = var2;
      Method[] var3 = var1.getDeclaredMethods();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         this._annotationMembers.add(var3[var4].getName());
      }

   }

   public void setDelegate(Annotation var1) {
      this._originalAnnotation = var1;
   }

   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      assert var3 == null;

      Object var4 = null;
      String var5 = var2.getName();
      if (!this._annotationMembers.contains(var5)) {
         if (this._originalAnnotation == null) {
            var4 = var2.invoke(this, var3);
         } else {
            var4 = var2.invoke(this._originalAnnotation, var3);
         }

         return var4;
      } else {
         Class var6 = var2.getReturnType();
         return getValueForMember(var5, var6, this._annotationOverrides);
      }
   }

   public static Object getValueForMember(MemberBean var0, Class var1, AnnotationInstanceBean var2) throws NoSuchMethodException {
      String var3 = var0.getMemberName();
      Method var4 = var1.getDeclaredMethod(var3);
      Class var5 = var4.getReturnType();
      return getValueForMember(var3, var5, var2);
   }

   public static Object getValueForMember(String var0, Class var1, AnnotationInstanceBean var2) {
      Object var3 = null;

      try {
         if (var1.isArray()) {
            if (Annotation.class.isAssignableFrom(var1.getComponentType())) {
               throw new RuntimeException("Arrays of annotations not yet implemented");
            }

            ArrayMemberBean[] var4 = var2.getArrayMembers();
            ArrayMemberBean var5 = findMemberArray(var4, var0);
            if (var5 != null) {
               String[] var6 = var5.getOverrideValues();
               if (var6 != null && var6.length > 0) {
                  var3 = buildReturnValues(var1, var6, var2, var0);
               }
            }
         } else {
            MemberBean var8 = findMember(var2.getMembers(), var0);
            if (var8 != null) {
               String var9 = var8.getOverrideValue();
               if (var9 != null && !var9.trim().equals("")) {
                  var3 = buildReturnValue(var1, var9, var2, var0);
               }
            }
         }

         return var3;
      } catch (Exception var7) {
         throw new RuntimeException("Error Getting Override Value. method:" + var0, var7);
      }
   }

   private static NestedAnnotationBean findNestedAnnotation(NestedAnnotationBean[] var0, String var1) {
      NestedAnnotationBean var2 = null;

      for(int var3 = 0; var3 < var0.length; ++var3) {
         if (var0[var3].getMemberName().equals(var1)) {
            var2 = var0[var3];
         }
      }

      return var2;
   }

   private static ArrayMemberBean findMemberArray(ArrayMemberBean[] var0, String var1) {
      ArrayMemberBean var2 = null;

      for(int var3 = 0; var3 < var0.length; ++var3) {
         if (var0[var3].getMemberName().equals(var1)) {
            var2 = var0[var3];
         }
      }

      assert var2 != null;

      return var2;
   }

   private static MemberBean findMember(MemberBean[] var0, String var1) {
      MemberBean var2 = null;

      for(int var3 = 0; var3 < var0.length; ++var3) {
         if (var0[var3].getMemberName().equals(var1)) {
            var2 = var0[var3];
         }
      }

      assert var2 != null;

      return var2;
   }

   private static Object buildReturnValues(Class var0, String[] var1, AnnotationInstanceBean var2, String var3) {
      assert var0.isArray();

      assert var1 != null;

      Object var4 = null;
      Class var5 = var0.getComponentType();
      int var6 = var1.length;
      if (var5 == String.class) {
         var4 = var1;
      } else {
         int var8;
         if (var5.isPrimitive()) {
            if (var5 == Byte.TYPE) {
               byte[] var7 = new byte[var6];

               for(var8 = 0; var8 < var6; ++var8) {
                  var7[var8] = Byte.valueOf(var1[var8]);
               }

               var4 = var7;
            } else if (var5 == Short.TYPE) {
               short[] var9 = new short[var6];

               for(var8 = 0; var8 < var6; ++var8) {
                  var9[var8] = Short.valueOf(var1[var8]);
               }

               var4 = var9;
            } else if (var5 == Integer.TYPE) {
               int[] var10 = new int[var6];

               for(var8 = 0; var8 < var6; ++var8) {
                  var10[var8] = Integer.valueOf(var1[var8]);
               }

               var4 = var10;
            } else if (var5 == Long.TYPE) {
               long[] var11 = new long[var6];

               for(var8 = 0; var8 < var6; ++var8) {
                  var11[var8] = Long.valueOf(var1[var8]);
               }

               var4 = var11;
            } else if (var5 == Double.TYPE) {
               double[] var12 = new double[var6];

               for(var8 = 0; var8 < var6; ++var8) {
                  var12[var8] = Double.valueOf(var1[var8]);
               }

               var4 = var12;
            } else if (var5 == Float.TYPE) {
               float[] var13 = new float[var6];

               for(var8 = 0; var8 < var6; ++var8) {
                  var13[var8] = Float.valueOf(var1[var8]);
               }

               var4 = var13;
            } else if (var5 == Character.TYPE) {
               char[] var14 = new char[var6];

               for(var8 = 0; var8 < var6; ++var8) {
                  var14[var8] = var1[var8].charAt(0);
               }

               var4 = var14;
            } else {
               if (var5 != Boolean.TYPE) {
                  throw new RuntimeException("Unknown primitive type:" + var5.getName());
               }

               boolean[] var15 = new boolean[var6];

               for(var8 = 0; var8 < var6; ++var8) {
                  var15[var8] = Boolean.valueOf(var1[var8]);
               }

               var4 = var15;
            }
         } else {
            Object[] var16 = new Object[var6];

            for(var8 = 0; var8 < var6; ++var8) {
               var16[var8] = buildReturnValue(var5, var1[var8], var2, var3);
            }

            var4 = var16;
         }
      }

      return var4;
   }

   private static Object buildReturnValue(Class var0, String var1, AnnotationInstanceBean var2, String var3) {
      Object var4 = null;
      if (var0.isPrimitive()) {
         if (var0 == Byte.TYPE) {
            var4 = Byte.valueOf(var1);
         } else if (var0 == Short.TYPE) {
            var4 = Short.valueOf(var1);
         } else if (var0 == Integer.TYPE) {
            var4 = Integer.valueOf(var1);
         } else if (var0 == Long.TYPE) {
            var4 = Long.valueOf(var1);
         } else if (var0 == Double.TYPE) {
            var4 = Double.valueOf(var1);
         } else if (var0 == Float.TYPE) {
            var4 = Float.valueOf(var1);
         } else if (var0 == Character.TYPE) {
            var4 = var1.charAt(0);
         } else {
            if (var0 != Boolean.TYPE) {
               throw new RuntimeException("Unknown primitive type:" + var0.getName());
            }

            var4 = Boolean.valueOf(var1);
         }
      } else if (var0.equals(String.class)) {
         var4 = var1;
      } else if (Enum.class.isAssignableFrom(var0)) {
         var4 = Enum.valueOf(var0, var1);
      } else if (Class.class.isAssignableFrom(var0)) {
         ClassLoader var5 = Thread.currentThread().getContextClassLoader();

         try {
            var4 = var5.loadClass(var1);
         } catch (ClassNotFoundException var10) {
            System.err.println(AnnotationProxy.class.getName() + ": unable to load class: " + var1);
         }
      } else {
         if (!Annotation.class.isAssignableFrom(var0)) {
            throw new RuntimeException("Unknown Member Type:" + var0);
         }

         NestedAnnotationBean var11 = findNestedAnnotation(var2.getNestedAnnotations(), var3);
         if (var11 == null) {
            throw new RuntimeException("unable to find NestedAnnotationBean - memberName = " + var3);
         }

         AnnotationInstanceBean var6 = var11.getAnnotation();
         ClassLoader var7 = Thread.currentThread().getContextClassLoader();

         try {
            Class var8 = var7.loadClass(var6.getAnnotationClassName());
            var4 = newInstance(var8, var6);
         } catch (ClassNotFoundException var9) {
            System.err.println(AnnotationProxy.class.getName() + ": unable to load class: " + var1);
         }
      }

      return var4;
   }
}
