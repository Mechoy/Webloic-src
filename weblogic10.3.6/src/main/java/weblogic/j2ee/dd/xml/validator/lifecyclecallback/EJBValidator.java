package weblogic.j2ee.dd.xml.validator.lifecyclecallback;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.dd.xml.validator.AnnotationValidatorHelper;
import weblogic.j2ee.descriptor.EnterpriseBeanBean;
import weblogic.j2ee.descriptor.LifecycleCallbackBean;
import weblogic.utils.ErrorCollectionException;

class EJBValidator extends J2EEValidator {
   protected Class getClass(DescriptorBean var1, ClassLoader var2) throws ClassNotFoundException {
      String var3 = ((LifecycleCallbackBean)var1).getLifecycleCallbackClass();
      if (var3 == null) {
         EnterpriseBeanBean var4 = (EnterpriseBeanBean)var1.getParentBean();
         var3 = var4.getEjbClass();
      }

      return AnnotationValidatorHelper.getClass(var3, var2);
   }

   protected Method guessMethodFromAnnotation(List<Method> var1) {
      Iterator var2 = var1.iterator();

      Method var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Method)var2.next();
      } while(!var3.isAnnotationPresent(PreDestroy.class) && !var3.isAnnotationPresent(PostConstruct.class) && !var3.isAnnotationPresent(PrePassivate.class) && !var3.isAnnotationPresent(PostActivate.class));

      return var3;
   }

   protected void checkModifier(Method var1, ErrorCollectionException var2) {
      StringBuffer var3 = null;
      if (Modifier.isStatic(var1.getModifiers())) {
         var3 = (new StringBuffer()).append("it cannot be declared as static");
      }

      if (Modifier.isFinal(var1.getModifiers())) {
         if (var3 == null) {
            var3 = (new StringBuffer()).append("it cannot be declared as final");
         } else {
            var3.append("and final");
         }
      }

      if (var3 != null) {
         var2.add(this.error(var1, var3.toString()));
      }

   }

   protected String getLifecycleAnnotations(Method var1) {
      String var2 = super.getLifecycleAnnotations(var1);
      Annotation[] var3 = var1.getAnnotations();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Annotation var6 = var3[var5];
         if (var6.annotationType() == PrePassivate.class) {
            var2 = var2 + "@PrePassivate, ";
         }

         if (var6.annotationType() == PostActivate.class) {
            var2 = var2 + "@PostActivate, ";
         }
      }

      return var2;
   }
}
