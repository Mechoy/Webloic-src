package weblogic.wsee.util;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.FilteringClassLoader;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.collections.WeakConcurrentHashMap;

public class JAXWSClassLoaderFactory {
   private static final JAXWSClassLoaderFactory theInstance = new JAXWSClassLoaderFactory();
   private static final boolean enabled;
   private static final List<String> theList = Arrays.asList("weblogic.xml.saaj.*", "weblogic.webservice.core.soap.*");
   private Map<ClassLoader, WeakReference<GenericClassLoader>> clMap = new WeakConcurrentHashMap();

   public static JAXWSClassLoaderFactory getInstance() {
      return theInstance;
   }

   private JAXWSClassLoaderFactory() {
   }

   public void setContextLoader(ClassLoader var1) {
      if (enabled) {
         GenericClassLoader var2 = null;
         WeakReference var3 = (WeakReference)this.clMap.get(var1);
         if (var3 != null) {
            var2 = (GenericClassLoader)var3.get();
         }

         if (var2 == null) {
            final ClassLoader var5 = var1 != null ? var1 : ClassLoader.getSystemClassLoader();
            FilteringClassLoader var4 = new FilteringClassLoader(var5) {
               protected Class findClass(String var1) throws ClassNotFoundException {
                  return "weblogic.webservice.core.soap.MessageFactoryImpl".equals(var1) ? var5.loadClass("weblogic.xml.saaj.MessageFactoryImpl") : super.findClass(var1);
               }
            };
            var4.setFilterList(theList);
            var2 = new GenericClassLoader(var4);
            if (var1 instanceof GenericClassLoader) {
               Annotation var6 = ((GenericClassLoader)var1).getAnnotation();
               var4.setAnnotation(var6);
               var2.setAnnotation(var6);
               this.clMap.put(var1, new WeakReference(var2));
            }
         }

         Thread.currentThread().setContextClassLoader(var2);
      }

   }

   public void remove(ClassLoader var1) {
      this.clMap.remove(var1);
   }

   static {
      String var0 = System.getProperty("javax.xml.soap.MetaFactory");
      String var1 = System.getProperty("javax.xml.soap.MessageFactory");
      String var2 = System.getProperty("javax.xml.soap.SOAPFactory");
      String var3 = "weblogic.xml.saaj.";
      enabled = (var0 == null || !var0.startsWith(var3)) && (var1 == null || !var1.startsWith(var3)) && (var2 == null || !var2.startsWith(var3));
   }
}
