package weblogic.j2ee.dd.xml.validator.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.PersistenceUnit;
import javax.xml.ws.WebServiceRef;

public class AnnotationWrapperFactory {
   public static InjectionAnnotationWrapper getAnnotationWrapper(EJB var0) {
      return new EJBAnnotationWrapper(var0);
   }

   public static InjectionAnnotationWrapper getAnnotationWrapper(EJB var0, Field var1) {
      EJBAnnotationWrapper var2 = new EJBAnnotationWrapper(var0);
      if (var0.beanInterface().equals(Object.class)) {
         var2.setBeanInterface(var1.getType());
      }

      return var2;
   }

   public static InjectionAnnotationWrapper getAnnotationWrapper(EJB var0, Method var1) {
      EJBAnnotationWrapper var2 = new EJBAnnotationWrapper(var0);
      if (var0.beanInterface().equals(Object.class)) {
         var2.setBeanInterface(var1.getParameterTypes()[0]);
      }

      return var2;
   }

   public static InjectionAnnotationWrapper getAnnotationWrapper(Resource var0) {
      return new ResourceAnnotationWrapper(var0);
   }

   public static InjectionAnnotationWrapper getAnnotationWrapper(Resource var0, Field var1) {
      ResourceAnnotationWrapper var2 = new ResourceAnnotationWrapper(var0);
      if (var0.type().equals(Object.class)) {
         var2.setType(var1.getType());
      }

      return var2;
   }

   public static InjectionAnnotationWrapper getAnnotationWrapper(Resource var0, Method var1) {
      ResourceAnnotationWrapper var2 = new ResourceAnnotationWrapper(var0);
      if (var0.type().equals(Object.class)) {
         var2.setType(var1.getParameterTypes()[0]);
      }

      return var2;
   }

   public static InjectionAnnotationWrapper getAnnotationWrapper(PersistenceUnit var0) {
      return new PersistenceUnitAnnotationWrapper(var0);
   }

   public static InjectionAnnotationWrapper getAnnotationWrapper(WebServiceRef var0) {
      return new WebServiceRefAnnotationWrapper(var0);
   }

   public static InjectionAnnotationWrapper getAnnotationWrapper(WebServiceRef var0, Field var1) {
      WebServiceRefAnnotationWrapper var2 = new WebServiceRefAnnotationWrapper(var0);
      if (var0.type().equals(Object.class)) {
         var2.setType(var1.getType());
      }

      return var2;
   }

   public static InjectionAnnotationWrapper getAnnotationWrapper(WebServiceRef var0, Method var1) {
      WebServiceRefAnnotationWrapper var2 = new WebServiceRefAnnotationWrapper(var0);
      if (var0.type().equals(Object.class)) {
         var2.setType(var1.getParameterTypes()[0]);
      }

      return var2;
   }
}
