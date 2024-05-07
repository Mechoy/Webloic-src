package weblogic.rmi.internal.wls;

import weblogic.application.AppClassLoaderManager;
import weblogic.rmi.utils.ClassLoaderEnvironment;
import weblogic.utils.classloaders.Annotation;

public class WLSClassLoaderEnvironment extends ClassLoaderEnvironment {
   public ClassLoader findLoader(String var1) {
      return AppClassLoaderManager.getAppClassLoaderManager().findLoader(new Annotation(var1));
   }

   public ClassLoader findInterAppLoader(String var1, ClassLoader var2) {
      return AppClassLoaderManager.getAppClassLoaderManager().findOrCreateInterAppLoader(new Annotation(var1), var2);
   }
}
