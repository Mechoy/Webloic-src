package weblogic.ejb.container.ejbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;

public final class ClientJarMaker {
   private static final DebugLogger debugLogger;
   private final ClassLoader jarLoader;
   private Set<String> clientJarFiles = new HashSet();
   private boolean containsRemoteEJBs = false;

   public ClientJarMaker(ClassLoader var1) {
      assert var1 != null;

      this.jarLoader = var1;
   }

   public String[] createClientJar(Collection<BeanInfo> var1, Collection<Class<?>> var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Class var4 = (Class)var3.next();
         this.addClass(var4);
      }

      this.createClientJar(var1);
      if (this.clientJarFiles.isEmpty()) {
         if (this.containsRemoteEJBs) {
            EJBLogger.logUnableToCreateClientJarDueToClasspathIssues();
         } else {
            EJBLogger.logSkippingClientJarCreationSinceNoRemoteEJBsFound();
         }

         return new String[0];
      } else {
         String[] var5 = new String[this.clientJarFiles.size()];
         var5 = (String[])((String[])this.clientJarFiles.toArray(var5));
         return var5;
      }
   }

   public String[] createClientJar(Collection<BeanInfo> var1) {
      Iterator var2 = var1.iterator();

      while(true) {
         BeanInfo var3;
         ClientDrivenBeanInfo var4;
         do {
            do {
               if (!var2.hasNext()) {
                  return (String[])this.clientJarFiles.toArray(new String[this.clientJarFiles.size()]);
               }

               var3 = (BeanInfo)var2.next();
            } while(!(var3 instanceof ClientDrivenBeanInfo));

            var4 = (ClientDrivenBeanInfo)var3;
         } while(!var4.hasRemoteClientView());

         this.containsRemoteEJBs = true;
         if (var4.hasDeclaredRemoteHome()) {
            if (!this.addClass(var4.getHomeInterfaceClass())) {
               EJBLogger.logUnableToAddToClientJarDueToClasspath("home", var4.getHomeInterfaceClass().getName());
            }

            if (!this.addClass(var4.getRemoteInterfaceClass())) {
               EJBLogger.logUnableToAddToClientJarDueToClasspath("remote", var4.getRemoteInterfaceClass().getName());
            }
         }

         if (var3 instanceof Ejb3SessionBeanInfo) {
            Ejb3SessionBeanInfo var5 = (Ejb3SessionBeanInfo)var3;
            Iterator var6 = var5.getBusinessRemotes().iterator();

            while(var6.hasNext()) {
               Class var7 = (Class)var6.next();
               if (!this.addClass(var7)) {
                  EJBLogger.logUnableToAddToClientJarDueToClasspath("business-remote", var7.getName());
               }
            }
         }

         if (var3 instanceof EntityBeanInfo) {
            EntityBeanInfo var8 = (EntityBeanInfo)var3;
            if (!var8.isUnknownPrimaryKey()) {
               this.addClass(var8.getPrimaryKeyClass());
            }
         }
      }
   }

   private void addClass(Class<?>[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.addClass(var1[var2]);
      }

   }

   private void addClass(Field[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.addClass(var1[var2].getType());
      }

   }

   private void addClass(Constructor<?>[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.addClass(var1[var2].getParameterTypes());
         this.addClass(var1[var2].getExceptionTypes());
      }

   }

   private void addClass(Method[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.addClass(var1[var2].getReturnType());
         this.addClass(var1[var2].getExceptionTypes());
         this.addClass(var1[var2].getParameterTypes());
      }

   }

   private boolean addClass(Class<?> var1) {
      if (var1 == null) {
         return false;
      } else if (var1.isArray()) {
         return this.addClass(var1.getComponentType());
      } else if (var1.getClassLoader() != this.jarLoader) {
         if (debugLogger.isDebugEnabled()) {
            debug("** Rejecting class not from jar: " + var1.getName());
         }

         return false;
      } else if (this.clientJarFiles.contains(var1.getName())) {
         if (debugLogger.isDebugEnabled()) {
            debug("** We already have: " + var1.getName());
         }

         return true;
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug("** Adding: " + var1.getName());
         }

         this.clientJarFiles.add(var1.getName());
         this.addClass(var1.getSuperclass());
         this.addClass(var1.getInterfaces());
         this.addClass(var1.getDeclaredFields());
         this.addClass(var1.getConstructors());
         this.addClass(var1.getDeclaredMethods());
         return true;
      }
   }

   private static void debug(String var0) {
      debugLogger.debug("[ClientJarMaker] " + var0);
   }

   static {
      debugLogger = EJBDebugService.compilationLogger;
   }
}
