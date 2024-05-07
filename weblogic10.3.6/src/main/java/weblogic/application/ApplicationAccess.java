package weblogic.application;

import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.GenericClassLoader;

public final class ApplicationAccess {
   private static final char appVerSeparator = '#';

   private ApplicationAccess() {
   }

   public static ApplicationAccess getApplicationAccess() {
      return ApplicationAccess.ApplicationAccessSingleton.SINGLETON;
   }

   public String getCurrentApplicationName() {
      ClassLoader var1 = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return Thread.currentThread().getContextClassLoader();
         }
      });
      Annotation var2 = this.getAnnotation(var1);
      return var2 != null ? var2.getApplicationName() : null;
   }

   public String getApplicationName(ClassLoader var1) {
      Annotation var2 = this.getAnnotation(var1);
      String var3 = null;
      if (var2 != null) {
         var3 = var2.getApplicationName();
         if (var3 != null && var3.length() > 0) {
            int var4 = var3.indexOf(35);
            if (var4 > -1) {
               var3 = var3.substring(0, var4);
            }
         }
      }

      return var3;
   }

   public String getModuleName(ClassLoader var1) {
      Annotation var2 = this.getAnnotation(var1);
      return var2 != null ? var2.getModuleName() : null;
   }

   public String getApplicationVersion(ClassLoader var1) {
      Annotation var2 = this.getAnnotation(var1);
      if (var2 != null) {
         String var3 = var2.getApplicationName();
         if (var3 != null && var3.length() > 0) {
            int var4 = var3.indexOf(35);
            if (var4 > -1) {
               return var3.substring(var4 + 1);
            }
         }
      }

      return null;
   }

   private Annotation getAnnotation(final ClassLoader var1) {
      while(var1 != null) {
         if (var1 instanceof GenericClassLoader) {
            return ((GenericClassLoader)var1).getAnnotation();
         }

         var1 = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
               return var1.getParent();
            }
         });
      }

      return null;
   }

   public ApplicationContextInternal getCurrentApplicationContext() {
      return this.getApplicationContext(this.getCurrentApplicationName());
   }

   public ApplicationContextInternal getApplicationContext(String var1) {
      if (var1 == null) {
         return null;
      } else {
         Deployment var2 = DeploymentManager.getDeploymentManager().findDeployment(var1);
         return var2 == null ? null : (ApplicationContextInternal)var2.getApplicationContext();
      }
   }

   public String getCurrentModuleName() {
      try {
         InitialContext var1 = new InitialContext();
         return (String)var1.lookup("java:/bea/ModuleName");
      } catch (NamingException var2) {
         return null;
      }
   }

   public GenericClassLoader findModuleLoader(String var1, String var2) {
      AppClassLoaderManager var3 = AppClassLoaderManager.getAppClassLoaderManager();
      return var3.findModuleLoader(var1, var2);
   }

   // $FF: synthetic method
   ApplicationAccess(Object var1) {
      this();
   }

   private static final class ApplicationAccessSingleton {
      private static final ApplicationAccess SINGLETON = new ApplicationAccess();
   }
}
