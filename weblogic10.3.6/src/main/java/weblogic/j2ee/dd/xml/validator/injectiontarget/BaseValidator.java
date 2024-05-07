package weblogic.j2ee.dd.xml.validator.injectiontarget;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.xml.ws.WebServiceRef;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.dd.xml.validator.AbstractAnnotationValidator;
import weblogic.j2ee.dd.xml.validator.AnnotationValidatorHelper;
import weblogic.j2ee.descriptor.InjectionTargetBean;
import weblogic.utils.ErrorCollectionException;

abstract class BaseValidator extends AbstractAnnotationValidator {
   private static Class[] ANNOTATION_CLASSES = new Class[]{Resource.class, EJB.class, PersistenceContext.class, PersistenceUnit.class, WebServiceRef.class};

   protected Class getClass(DescriptorBean var1, ClassLoader var2) throws ClassNotFoundException {
      String var3 = ((InjectionTargetBean)var1).getInjectionTargetClass();
      return AnnotationValidatorHelper.getClass(var3, var2);
   }

   protected final Method getMethod(DescriptorBean var1, Class var2) {
      String var3 = ((InjectionTargetBean)var1).getInjectionTargetName();
      String var4 = AnnotationValidatorHelper.getSetterName(var3);
      List var5 = AnnotationValidatorHelper.getMethods(var2, var4);
      Field var6 = AnnotationValidatorHelper.getField(var2, var3);
      if (var5.size() == 0) {
         return null;
      } else if (var5.size() == 1) {
         return (Method)var5.get(0);
      } else {
         if (var6 != null) {
            Iterator var7 = var5.iterator();

            while(var7.hasNext()) {
               Method var8 = (Method)var7.next();
               Class[] var9 = var8.getParameterTypes();
               if (var9.length == 1 && var9[0].equals(var6.getType())) {
                  return var8;
               }
            }
         }

         return (Method)var5.get(0);
      }
   }

   protected final Field getField(DescriptorBean var1, Class var2) {
      String var3 = ((InjectionTargetBean)var1).getInjectionTargetName();
      return AnnotationValidatorHelper.getField(var2, var3);
   }

   protected final void checkUndefinedMethodField(DescriptorBean var1, Field var2, Method var3, ErrorCollectionException var4) {
      if (var2 == null && var3 == null) {
         String var5 = ((InjectionTargetBean)var1).getInjectionTargetClass();
         String var6 = ((InjectionTargetBean)var1).getInjectionTargetName();
         String var7 = AnnotationValidatorHelper.getSetterName(var6);
         var4.add(this.error("\"" + var6 + "\" is defined as injection target" + " in descriptor file for class \"" + var5 + "\", but either field \"" + var6 + "\" or method \"" + var7 + "\" cannot be found within the class."));
      }

   }

   protected final void checkAnnotation(Method var1, Field var2, ErrorCollectionException var3) {
      Class[] var4 = ANNOTATION_CLASSES;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Class var7 = var4[var6];
         this.checkNameMethodOnAnnotation(var1, var2, var7, var3);
      }

   }

   private void checkNameMethodOnAnnotation(Method var1, Field var2, Class<? extends Annotation> var3, ErrorCollectionException var4) {
      if (var1.isAnnotationPresent(var3)) {
         if (var2.isAnnotationPresent(var3)) {
            String var5 = "name";
            Annotation var6 = var1.getAnnotation(var3);
            Annotation var7 = var2.getAnnotation(var3);

            try {
               Method var8 = var6.getClass().getDeclaredMethod(var5);
               Method var9 = var7.getClass().getDeclaredMethod(var5);
               Object var10 = var8.invoke(var6);
               Object var11 = var9.invoke(var7);
               if (var10 != null && var11 != null && !var10.equals(var11)) {
                  var4.add(this.error("Annotation @" + var3.getSimpleName() + " is defined on both method \"" + var1.getName() + "\" and field \"" + var2.getName() + "\", but they are inconsistent."));
               }
            } catch (NoSuchMethodException var12) {
            } catch (IllegalAccessException var13) {
            } catch (InvocationTargetException var14) {
            }

         }
      }
   }

   protected final Exception error(String var1) {
      return new IllegalArgumentException(var1);
   }

   protected final Exception error(Field var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      if (var1 == null) {
         return this.error(var2);
      } else {
         Class var4 = var1.getDeclaringClass();
         String var5 = var1.getName();
         var3.append("Field \"").append(var5).append(this.getMessageBody(var4, var5, var2));
         return this.error(var3.toString());
      }
   }

   protected final Exception error(Method var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      if (var1 == null) {
         return this.error(var2);
      } else {
         Class var4 = var1.getDeclaringClass();
         String var5 = var1.getName();
         String var6 = AnnotationValidatorHelper.getFieldName(var5);
         var3.append("Method \"").append(var5).append(this.getMessageBody(var4, var6, var2));
         return this.error(var3.toString());
      }
   }

   private String getMessageBody(Class var1, String var2, String var3) {
      StringBuffer var4 = new StringBuffer();
      var4.append("\" in class \"").append(var1.getName()).append("\" is defined as injection target ");
      String var5 = this.getInjectionTargetAnnotation(var1, var2);
      if (var5 != null && var5.length() != 0) {
         var4.append("with annotation ").append(var5);
      } else {
         var4.append("in deployment descriptor file, ");
      }

      var4.append("but ").append(var3).append(".");
      return var4.toString();
   }

   private String getInjectionTargetAnnotation(Class var1, String var2) {
      HashSet var3 = new HashSet();
      Field var4 = AnnotationValidatorHelper.getField(var1, var2);
      if (var4 != null) {
         Annotation[] var5 = var4.getDeclaredAnnotations();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Annotation var8 = var5[var7];
            var3.add(var8.annotationType());
         }
      }

      String var13 = AnnotationValidatorHelper.getSetterName(var2);
      List var14 = AnnotationValidatorHelper.getMethods(var1, var13);
      Iterator var15 = var14.iterator();

      while(var15.hasNext()) {
         Method var16 = (Method)var15.next();
         Annotation[] var9 = var16.getDeclaredAnnotations();
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            Annotation var12 = var9[var11];
            var3.add(var12.annotationType());
         }
      }

      return this.getInjectionTargetAnnotation(var3);
   }

   private String getInjectionTargetAnnotation(Set<Class> var1) {
      String var2 = "";
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Class var4 = (Class)var3.next();
         if (var1.contains(var4)) {
            var2 = var2 + "@" + var4.getSimpleName() + ", ";
         }
      }

      return var2;
   }
}
