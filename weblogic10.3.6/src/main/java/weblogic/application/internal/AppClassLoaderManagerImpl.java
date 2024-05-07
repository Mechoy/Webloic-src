package weblogic.application.internal;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import weblogic.application.AppClassLoaderManager;
import weblogic.application.CannotRedeployException;
import weblogic.deploy.container.NonFatalDeploymentException;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.Source;

public final class AppClassLoaderManagerImpl extends AppClassLoaderManager {
   private static final boolean DEBUG = false;
   private static final String NULL_KEY = new String();
   private final Map<GenericClassLoader, Map<ClassLoader, GenericClassLoader>> interAppCLMap = new ConcurrentHashMap();
   private final CLInfo rootInfo = new CLInfo((GenericClassLoader)null);

   public void addModuleLoader(GenericClassLoader var1, String var2) {
      this.rootInfo.findOrCreateInfo(var1, var2);
   }

   public void removeApplicationLoader(String var1) {
      CLInfo var2 = this.rootInfo.findInfo(var1);
      if (var2 != null) {
         GenericClassLoader var3 = var2.getClassLoader();
         this.removeClassLoaderFromCLMap(var3);
         Iterator var4 = var2.children.values().iterator();

         while(var4.hasNext()) {
            var3 = ((CLInfo)var4.next()).getClassLoader();
            this.removeClassLoaderFromCLMap(var3);
         }
      }

      this.rootInfo.removeAppInfo(var1);
   }

   private void removeClassLoaderFromCLMap(GenericClassLoader var1) {
      if (var1 != null) {
         this.interAppCLMap.remove(var1);
         Iterator var2 = this.interAppCLMap.keySet().iterator();

         while(var2.hasNext()) {
            GenericClassLoader var3 = (GenericClassLoader)var2.next();
            if (this.sameAnnotation(var3.getAnnotation(), var1.getAnnotation())) {
               this.interAppCLMap.remove(var3);
            } else if (var3.getParent() != null && var3.getParent() instanceof GenericClassLoader && this.sameAnnotation(((GenericClassLoader)var3.getParent()).getAnnotation(), var1.getAnnotation())) {
               this.interAppCLMap.remove(var3);
            }
         }

         var2 = this.interAppCLMap.values().iterator();

         while(var2.hasNext()) {
            Map var6 = (Map)var2.next();
            var6.remove(var1);
            Iterator var4 = var6.keySet().iterator();

            while(var4.hasNext()) {
               ClassLoader var5 = (ClassLoader)var4.next();
               if (var5 instanceof GenericClassLoader) {
                  if (this.sameAnnotation(((GenericClassLoader)var5).getAnnotation(), var1.getAnnotation())) {
                     var6.remove(var5);
                  } else if (var5.getParent() != null && var5.getParent() instanceof GenericClassLoader && this.sameAnnotation(((GenericClassLoader)var5.getParent()).getAnnotation(), var1.getAnnotation())) {
                     var6.remove(var5);
                  }
               }
            }
         }
      }

   }

   private boolean sameAnnotation(Annotation var1, Annotation var2) {
      return var1.getAnnotationString().length() != 0 && var2.getAnnotationString().length() != 0 && var1.equals(var2);
   }

   public GenericClassLoader findLoader(Annotation var1) {
      CLInfo var2 = this.rootInfo.findInfo(var1);
      return var2 == null ? null : var2.getClassLoader();
   }

   private CLInfo findModuleInfo(String var1, String var2) {
      CLInfo var3 = this.rootInfo.findInfo(var1, var2);
      if (var3 == null) {
         var3 = this.rootInfo.findInfo(var1);
      }

      return var3;
   }

   public GenericClassLoader findModuleLoader(String var1, String var2) {
      CLInfo var3 = this.findModuleInfo(var1, var2);
      return var3 == null ? null : var3.getClassLoader();
   }

   public Class loadApplicationClass(Annotation var1, String var2) throws ClassNotFoundException {
      GenericClassLoader var3 = this.findLoader(var1);
      if (var3 == null) {
         throw new ClassNotFoundException(var2);
      } else {
         return Class.forName(var2, false, var3);
      }
   }

   public Source findApplicationSource(Annotation var1, String var2) {
      GenericClassLoader var3 = this.findLoader(var1);
      return var3 == null ? null : var3.getClassFinder().getSource(var2);
   }

   public GenericClassLoader findOrCreateIntraAppLoader(Annotation var1, ClassLoader var2) {
      return this.findLoader(var1);
   }

   public GenericClassLoader findOrCreateInterAppLoader(Annotation var1, ClassLoader var2) {
      GenericClassLoader var3 = this.findLoader(var1);
      if (var3 == null) {
         return null;
      } else {
         Object var4 = (Map)this.interAppCLMap.get(var3);
         if (var4 == null) {
            synchronized(var3) {
               var4 = (Map)this.interAppCLMap.get(var3);
               if (var4 == null) {
                  var4 = new ConcurrentHashMap();
                  this.interAppCLMap.put(var3, var4);
               }
            }
         }

         GenericClassLoader var5 = (GenericClassLoader)((Map)var4).get(var2);
         if (var5 == null) {
            synchronized(var2) {
               var5 = (GenericClassLoader)((Map)var4).get(var2);
               if (var5 == null) {
                  var5 = new GenericClassLoader(var3.getClassFinder(), var2);
                  if (!(var2 instanceof GenericClassLoader)) {
                     var5.setAnnotation(var3.getAnnotation());
                  }

                  ((Map)var4).put(var2, var5);
               }
            }
         }

         return var5;
      }
   }

   public void checkPartialRedeploy(FlowContext var1, String[] var2) throws NonFatalDeploymentException {
      String var3 = var1.getApplicationId();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         CLInfo var5 = this.findModuleInfo(var3, var2[var4]);
         CLInfo var6 = this.rootInfo.findInfo(var3);
         GenericClassLoader var7 = var6 != null ? var6.cl : null;
         if (var5 != null) {
            if (var7 != null && var5.cl == var7) {
               if (var1.isStopOperation()) {
                  throw new NonFatalDeploymentException("Module '" + var2[var4] + "' has the same ClassLoader as the Application '" + var3 + "'. Consider stopping the entire application.");
               }

               throw new CannotRedeployException("Module '" + var2[var4] + "' has the same ClassLoader as the Application '" + var3 + "'. Consider redeploying the entire application.");
            }

            Set var8 = var5.findMissingURIs(new HashSet(Arrays.asList(var2)));
            if (!var8.isEmpty()) {
               if (var1.isStopOperation()) {
                  throw new NonFatalDeploymentException("Module " + var2[var4] + " in application '" + var3 + "' cannot be stopped because the following modules" + " which depend on " + var2[var4] + " were not included in the list: " + prettyPrint(var8));
               }

               throw new CannotRedeployException("Module " + var2[var4] + " in application '" + var3 + "' cannot be redeployed because the following modules" + " which depend on " + var2[var4] + " were not included in the redeploy list: " + prettyPrint(var8));
            }
         }
      }

   }

   public Set<String> updatePartialDeploySet(FlowContext var1, String[] var2) throws NonFatalDeploymentException {
      String var3 = var1.getApplicationId();
      HashSet var4 = new HashSet(Arrays.asList(var2));
      CLInfo var5 = this.rootInfo.findInfo(var3);
      GenericClassLoader var6 = var5 != null ? var5.cl : null;

      for(int var7 = 0; var7 < var2.length; ++var7) {
         CLInfo var8 = this.findModuleInfo(var3, var2[var7]);
         if (var8 != null) {
            if (var6 != null && var8.cl == var6) {
               if (var1.isStopOperation()) {
                  throw new NonFatalDeploymentException("Module '" + var2[var7] + "' has the same ClassLoader as the Application '" + var3 + "'. Consider stopping the entire application.");
               }

               throw new CannotRedeployException("Module '" + var2[var7] + "' has the same ClassLoader as the Application '" + var3 + "'. Consider redeploying the entire application.");
            }

            Set var9 = var8.findMissingURIs(var4);
            if (!var9.isEmpty()) {
               var4.addAll(var9);
            }
         }
      }

      return var4;
   }

   private static String prettyPrint(Set<String> var0) {
      String var1 = "";
      StringBuffer var2 = new StringBuffer();

      for(Iterator var3 = var0.iterator(); var3.hasNext(); var1 = ", ") {
         var2.append(var1);
         var2.append((String)var3.next());
      }

      return var2.toString();
   }

   private static class CLInfo {
      private GenericClassLoader cl;
      private Map<String, CLInfo> children = Collections.emptyMap();
      private final Set<String> uriSet = Collections.synchronizedSet(new HashSet());

      CLInfo(GenericClassLoader var1) {
         this.cl = var1;
      }

      void setClassLoader(GenericClassLoader var1) {
         this.cl = var1;
      }

      GenericClassLoader getClassLoader() {
         return this.cl;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1 = this.toStringBuffer(var1, 0);
         return var1.toString();
      }

      StringBuffer toStringBuffer(StringBuffer var1, int var2) {
         for(int var3 = 0; var3 < var2; ++var3) {
            var1.append(" ");
         }

         var1.append("[Annotation]: " + (this.cl == null ? "Null CL" : this.cl.getAnnotation().toString()));
         var1.append(" [uris:] " + AppClassLoaderManagerImpl.prettyPrint(this.uriSet));
         var1.append("\n");
         Iterator var5 = this.children.values().iterator();

         while(var5.hasNext()) {
            CLInfo var4 = (CLInfo)var5.next();
            var4.toStringBuffer(var1, var2 + 2);
         }

         return var1;
      }

      private void addChild(String var1, CLInfo var2) {
         if (this.children.size() == 0) {
            this.children = new ConcurrentHashMap();
         }

         this.children.put(var1 == null ? AppClassLoaderManagerImpl.NULL_KEY : var1, var2);
      }

      CLInfo findInfo(final String var1) {
         return this.findInfo(new Finder() {
            CLInfo findInfo(CLInfo var1x) {
               return (CLInfo)var1x.children.get(var1 == null ? AppClassLoaderManagerImpl.NULL_KEY : var1);
            }
         });
      }

      CLInfo findInfo(String var1, String var2) {
         return var2 == null ? this.findInfo(var1) : this.findInfo((Finder)(new ModuleFinder(var1, var2)));
      }

      CLInfo findInfo(Annotation var1) {
         return this.findInfo((Finder)(new AnnotationFinder(var1)));
      }

      private CLInfo findInfo(Finder var1) {
         return var1.findInfo(this);
      }

      CLInfo findOrCreateInfo(GenericClassLoader var1, String var2) {
         LinkedList var3 = new LinkedList();

         for(Object var4 = var1; var4 instanceof GenericClassLoader; var4 = ((ClassLoader)var4).getParent()) {
            var3.add(0, var4);
            Annotation var5 = ((GenericClassLoader)var4).getAnnotation();
            if (var5.getModuleName() == null) {
               break;
            }
         }

         return this.findOrCreateInfo(var3.iterator(), var2);
      }

      private CLInfo findOrCreateInfo(Iterator<ClassLoader> var1, String var2) {
         if (!var1.hasNext()) {
            this.uriSet.add(var2);
            return this;
         } else {
            GenericClassLoader var3 = (GenericClassLoader)var1.next();
            Annotation var4 = var3.getAnnotation();
            String var5 = var4.getModuleName() == null ? var4.getApplicationName() : var4.getModuleName();
            CLInfo var6 = null;
            var6 = (CLInfo)this.children.get(var5 == null ? AppClassLoaderManagerImpl.NULL_KEY : var5);
            if (var6 == null) {
               var6 = new CLInfo(var3);
               this.addChild(var5, var6);
            } else if (var6.getClassLoader() != var3) {
               var6.children.clear();
               var6.setClassLoader(var3);
            }

            return var6.findOrCreateInfo(var1, var2);
         }
      }

      void removeAppInfo(String var1) {
         this.children.remove(var1 == null ? AppClassLoaderManagerImpl.NULL_KEY : var1);
      }

      Set<String> findMissingURIs(Set<String> var1) {
         HashSet var2 = new HashSet();
         this.findMissingURIs(var2, var1);
         return var2;
      }

      private void findMissingURIs(Set<String> var1, Set<String> var2) {
         Iterator var3 = this.uriSet.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            if (!var2.contains(var4)) {
               var1.add(var4);
            }
         }

         Iterator var6 = this.children.values().iterator();

         while(var6.hasNext()) {
            CLInfo var5 = (CLInfo)var6.next();
            var5.findMissingURIs(var1, var2);
         }

      }

      private static class ModuleFinder extends Finder {
         private final String appName;
         private final String moduleName;

         ModuleFinder(String var1, String var2) {
            super(null);
            this.appName = var1;
            this.moduleName = var2;
         }

         CLInfo findInfo(CLInfo var1) {
            CLInfo var2 = (CLInfo)var1.children.get(this.appName == null ? AppClassLoaderManagerImpl.NULL_KEY : this.appName);
            return var2 == null ? null : this.findModuleInfo(var2);
         }

         private CLInfo findModuleInfo(CLInfo var1) {
            if (var1.uriSet.contains(this.moduleName)) {
               return var1;
            } else {
               Iterator var2 = var1.children.values().iterator();

               CLInfo var4;
               do {
                  if (!var2.hasNext()) {
                     return null;
                  }

                  CLInfo var3 = (CLInfo)var2.next();
                  var4 = this.findModuleInfo(var3);
               } while(var4 == null);

               return var4;
            }
         }
      }

      private static class AnnotationFinder extends Finder {
         private final Annotation annotation;

         AnnotationFinder(Annotation var1) {
            super(null);
            this.annotation = var1;
         }

         CLInfo findInfo(CLInfo var1) {
            if (var1.cl != null && this.annotation.equals(var1.cl.getAnnotation())) {
               return var1;
            } else {
               Iterator var2 = var1.children.values().iterator();

               CLInfo var4;
               do {
                  if (!var2.hasNext()) {
                     return null;
                  }

                  CLInfo var3 = (CLInfo)var2.next();
                  var4 = this.findInfo(var3);
               } while(var4 == null);

               return var4;
            }
         }
      }

      private abstract static class Finder {
         private Finder() {
         }

         abstract CLInfo findInfo(CLInfo var1);

         // $FF: synthetic method
         Finder(Object var1) {
            this();
         }
      }
   }
}
