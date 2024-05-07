package weblogic.application;

import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.Source;

public abstract class AppClassLoaderManager {
   private static final String IMPL_CLASS = "weblogic.application.internal.AppClassLoaderManagerImpl";
   private static final AppClassLoaderManager theOne;

   public static AppClassLoaderManager getAppClassLoaderManager() {
      return theOne;
   }

   public abstract Class loadApplicationClass(Annotation var1, String var2) throws ClassNotFoundException;

   public abstract Source findApplicationSource(Annotation var1, String var2);

   public abstract GenericClassLoader findOrCreateIntraAppLoader(Annotation var1, ClassLoader var2);

   public abstract GenericClassLoader findOrCreateInterAppLoader(Annotation var1, ClassLoader var2);

   public abstract void addModuleLoader(GenericClassLoader var1, String var2);

   public abstract GenericClassLoader findModuleLoader(String var1, String var2);

   public abstract GenericClassLoader findLoader(Annotation var1);

   static {
      try {
         theOne = (AppClassLoaderManager)Class.forName("weblogic.application.internal.AppClassLoaderManagerImpl").newInstance();
      } catch (Exception var1) {
         throw new AssertionError(var1);
      }
   }
}
