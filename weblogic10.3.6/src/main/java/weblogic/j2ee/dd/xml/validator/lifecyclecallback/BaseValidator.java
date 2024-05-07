package weblogic.j2ee.dd.xml.validator.lifecyclecallback;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.dd.xml.validator.AbstractAnnotationValidator;
import weblogic.j2ee.dd.xml.validator.AnnotationValidatorHelper;
import weblogic.j2ee.descriptor.EjbCallbackBean;
import weblogic.j2ee.descriptor.J2eeClientEnvironmentBean;
import weblogic.j2ee.descriptor.LifecycleCallbackBean;
import weblogic.utils.ErrorCollectionException;

abstract class BaseValidator extends AbstractAnnotationValidator {
   protected Class getClass(DescriptorBean var1, ClassLoader var2) throws ClassNotFoundException {
      String var3 = ((LifecycleCallbackBean)var1).getLifecycleCallbackClass();
      return AnnotationValidatorHelper.getClass(var3, var2);
   }

   protected final Field getField(DescriptorBean var1, Class var2) {
      return null;
   }

   protected final void checkField(Field var1, ErrorCollectionException var2) {
   }

   protected final void checkAnnotation(Method var1, Field var2, ErrorCollectionException var3) {
   }

   protected final Method getMethod(DescriptorBean var1, Class var2) {
      String var3 = ((LifecycleCallbackBean)var1).getLifecycleCallbackMethod();
      List var4 = AnnotationValidatorHelper.getMethods(var2, var3);
      if (var4.size() == 0) {
         return null;
      } else {
         return var4.size() == 1 ? (Method)var4.get(0) : this.guessMethod(var4);
      }
   }

   private Method guessMethod(List<Method> var1) {
      Method var2 = this.guessMethodFromAnnotation(var1);
      if (var2 != null) {
         return var2;
      } else {
         var2 = this.guessMethodFromSignature(var1);
         return var2 != null ? var2 : (Method)var1.get(0);
      }
   }

   protected Method guessMethodFromAnnotation(List<Method> var1) {
      Iterator var2 = var1.iterator();

      Method var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Method)var2.next();
      } while(!var3.isAnnotationPresent(PreDestroy.class) && !var3.isAnnotationPresent(PostConstruct.class));

      return var3;
   }

   protected Method guessMethodFromSignature(List<Method> var1) {
      Iterator var2 = var1.iterator();

      Method var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Method)var2.next();
      } while(!var3.isAnnotationPresent(PreDestroy.class) && !var3.isAnnotationPresent(PostConstruct.class));

      return var3;
   }

   protected final void checkBean(DescriptorBean var1, ErrorCollectionException var2) {
      DescriptorBean var3 = var1.getParentBean();
      HashSet var6 = new HashSet();
      LifecycleCallbackBean[] var4;
      String var5;
      LifecycleCallbackBean[] var8;
      int var9;
      int var10;
      LifecycleCallbackBean var11;
      if (var3 instanceof J2eeClientEnvironmentBean) {
         J2eeClientEnvironmentBean var7 = (J2eeClientEnvironmentBean)var3;
         var4 = var7.getPostConstructs();
         var8 = var4;
         var9 = var4.length;

         for(var10 = 0; var10 < var9; ++var10) {
            var11 = var8[var10];
            var5 = var11.getLifecycleCallbackClass();
            if (var11.getBeanSource() == 1 && !var6.add(var5)) {
               var2.add(this.error("Cannot define multiple post-construct lifecycle callback methods on class \"" + var5 + "\""));
               break;
            }
         }

         var6.clear();
         var4 = var7.getPreDestroys();
         var8 = var4;
         var9 = var4.length;

         for(var10 = 0; var10 < var9; ++var10) {
            var11 = var8[var10];
            var5 = var11.getLifecycleCallbackClass();
            if (var11.getBeanSource() == 1 && !var6.add(var5)) {
               var2.add(this.error("Cannot define multiple pre-destroy lifecycle callback methods on class \"" + var5 + "\""));
               break;
            }
         }
      }

      var6.clear();
      if (var3 instanceof EjbCallbackBean) {
         EjbCallbackBean var12 = (EjbCallbackBean)var3;
         var4 = var12.getPrePassivates();
         var8 = var4;
         var9 = var4.length;

         for(var10 = 0; var10 < var9; ++var10) {
            var11 = var8[var10];
            var5 = var11.getLifecycleCallbackClass();
            if (var11.getBeanSource() == 1 && !var6.add(var5)) {
               var2.add(this.error("Cannot define multiple pre-passivate lifecycle callback methods on class \"" + var5 + "\""));
               break;
            }
         }

         var6.clear();
         var4 = var12.getPostActivates();
         var8 = var4;
         var9 = var4.length;

         for(var10 = 0; var10 < var9; ++var10) {
            var11 = var8[var10];
            var5 = var11.getLifecycleCallbackClass();
            if (var11.getBeanSource() == 1 && !var6.add(var5)) {
               var2.add(this.error("Cannot define multiple post-activate lifecycle callback methods on class \"" + var5 + "\""));
               break;
            }
         }
      }

   }

   protected void checkReturnType(Method var1, ErrorCollectionException var2) {
      if (var1.getReturnType() != Void.TYPE) {
         var2.add(this.error(var1, "its return type must be void"));
      }

   }

   protected void checkException(Method var1, ErrorCollectionException var2) {
      Class[] var3 = var1.getExceptionTypes();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Class var6 = var3[var5];
         if (var6.isAssignableFrom(Exception.class)) {
            return;
         }

         if (!RuntimeException.class.isAssignableFrom(var6) && !Error.class.isAssignableFrom(var6)) {
            var2.add(this.error(var1, "it cannot be declared to throw checked exception"));
         }
      }

   }

   protected void checkUndefinedMethodField(DescriptorBean var1, Field var2, Method var3, ErrorCollectionException var4) {
      LifecycleCallbackBean var5 = (LifecycleCallbackBean)var1;
      String var6 = var5.getLifecycleCallbackClass();
      String var7 = var5.getLifecycleCallbackMethod();
      if (var3 == null) {
         var4.add(this.error("Method \"" + var7 + "\" is defined in deployment descriptor as lifecycle callback method, " + "but it is not defined in class \"" + var6 + "\"."));
      }

   }

   protected void checkParameters(Method var1, ErrorCollectionException var2) {
      Class[] var3 = var1.getParameterTypes();
      if (var3 != null && var3.length > 0) {
         var2.add(this.error(var1, "it must not have any parameter"));
      }

   }

   protected final Exception error(String var1) {
      return new IllegalArgumentException(var1);
   }

   protected final Exception error(Method var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      if (var1 == null) {
         return this.error(var2);
      } else {
         var3.append("Method \"").append(var1.getName()).append("\" in class \"").append(var1.getDeclaringClass().getName()).append("\" is defined as lifecycle callback method ");
         String var4 = this.getLifecycleAnnotations(var1);
         if (var4 != null && var4.length() != 0) {
            var3.append("with annotation ").append(var4);
         } else {
            var3.append("in deployment descriptor file, ");
         }

         var3.append("but ").append(var2).append(".");
         return this.error(var3.toString());
      }
   }

   protected String getLifecycleAnnotations(Method var1) {
      String var2 = "";
      Annotation[] var3 = var1.getAnnotations();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Annotation var6 = var3[var5];
         if (var6.annotationType() == PostConstruct.class) {
            var2 = var2 + "@PostConstruct, ";
         }

         if (var6.annotationType() == PreDestroy.class) {
            var2 = var2 + "@PreDestroy, ";
         }
      }

      return var2;
   }
}
