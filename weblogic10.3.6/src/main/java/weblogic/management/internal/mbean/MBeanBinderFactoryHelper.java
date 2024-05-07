package weblogic.management.internal.mbean;

import weblogic.utils.codegen.AttributeBinder;
import weblogic.utils.codegen.AttributeBinderFactory;
import weblogic.utils.codegen.AttributeBinderFactoryHelper;

public class MBeanBinderFactoryHelper implements AttributeBinderFactoryHelper {
   private static String PACKAGE_NAME = "weblogic.management.configuration.";
   private static String BEAN_SUFFIX = "MBean";
   private static String IMPL_SUFFIX = "Binder";

   private String elementToBinderName(String var1) {
      return var1.indexOf(".") != -1 ? var1 + BEAN_SUFFIX + IMPL_SUFFIX : PACKAGE_NAME + var1 + BEAN_SUFFIX + IMPL_SUFFIX;
   }

   public AttributeBinderFactory getAttributeBinderFactory(String var1) throws ClassNotFoundException {
      String var2 = this.elementToBinderName(var1);
      if (var2 == "DummyMBeanBinderFactory") {
         return new DummyMBeanBinderFactory(var1);
      } else {
         Class var3 = null;
         var3 = this.getClassLoader().loadClass(var2);

         try {
            return (AttributeBinderFactory)var3.newInstance();
         } catch (InstantiationException var5) {
            throw new AssertionError(var5);
         } catch (IllegalAccessException var6) {
            throw new AssertionError(var6);
         }
      }
   }

   private ClassLoader getClassLoader() {
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();
      if (var1 != null) {
         return var1;
      } else {
         ClassLoader var2 = this.getClass().getClassLoader();
         return var2 == null ? ClassLoader.getSystemClassLoader() : var2;
      }
   }

   private static class DummyMBeanBinderFactory implements AttributeBinderFactory, AttributeBinder {
      private String beanClass;

      private DummyMBeanBinderFactory(String var1) {
         this.beanClass = var1;
      }

      public AttributeBinder getAttributeBinder() {
         return this;
      }

      public AttributeBinder bindAttribute(String var1, Object var2) {
         return this;
      }

      // $FF: synthetic method
      DummyMBeanBinderFactory(String var1, Object var2) {
         this(var1);
      }
   }
}
