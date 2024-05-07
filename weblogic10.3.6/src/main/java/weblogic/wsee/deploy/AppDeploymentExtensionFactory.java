package weblogic.wsee.deploy;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import weblogic.application.AppDeploymentExtension;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.internal.ApplicationContextImpl;
import weblogic.management.DeploymentException;
import weblogic.wsee.util.Pair;

public class AppDeploymentExtensionFactory implements weblogic.application.AppDeploymentExtensionFactory {
   public static final AppDeploymentExtensionFactory INSTANCE = new AppDeploymentExtensionFactory();
   private Map<ApplicationContext, Pair<Collection<WSEEModule>, State>> modules = new ConcurrentHashMap();

   private AppDeploymentExtensionFactory() {
   }

   void addModule(ApplicationContext var1, WSEEModule var2) throws DeploymentException {
      Pair var3 = (Pair)this.modules.get(var1);
      if (var3 == null) {
         State var4 = AppDeploymentExtensionFactory.State.NOT_PREPARED;
         if (((ApplicationContextImpl)var1).getPartialRedeployURIs() != null) {
            var4 = AppDeploymentExtensionFactory.State.PREPARED;
         }

         var3 = new Pair(new ConcurrentLinkedQueue(), var4);
         this.modules.put(var1, var3);
      }

      synchronized(var3) {
         switch ((State)var3.getRight()) {
            case PREPARED:
               this.prepare(var2);
               break;
            case ACTIVE:
               this.prepare_and_activate(var2);
         }

         ((Collection)var3.getLeft()).add(var2);
      }
   }

   void removeModule(ApplicationContext var1, WSEEModule var2) throws DeploymentException {
      Pair var3 = (Pair)this.modules.get(var1);
      if (var3 != null) {
         synchronized(var3) {
            if (((Collection)var3.getLeft()).remove(var2)) {
               switch ((State)var3.getRight()) {
                  case PREPARED:
                     this.unprepare(var2);
                     break;
                  case ACTIVE:
                     this.deactivate_and_unprepare(var2);
               }
            }

            if (((Collection)var3.getLeft()).isEmpty()) {
               this.modules.remove(var1);
            }
         }
      }

   }

   private void prepare(WSEEModule var1) throws DeploymentException {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      ClassLoader var3 = var1.getClassLoader();
      boolean var4 = false;
      if (var2 != var3) {
         Thread.currentThread().setContextClassLoader(var3);
         var4 = true;
      }

      try {
         var1.prepare();
      } finally {
         if (var4) {
            Thread.currentThread().setContextClassLoader(var2);
         }

      }

   }

   private void prepare_and_activate(WSEEModule var1) throws DeploymentException {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      ClassLoader var3 = var1.getClassLoader();
      boolean var4 = false;
      if (var2 != var3) {
         Thread.currentThread().setContextClassLoader(var3);
         var4 = true;
      }

      try {
         var1.prepare();
         var1.activate();
      } finally {
         if (var4) {
            Thread.currentThread().setContextClassLoader(var2);
         }

      }

   }

   private void activate(WSEEModule var1) throws DeploymentException {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      ClassLoader var3 = var1.getClassLoader();
      boolean var4 = false;
      if (var2 != var3) {
         Thread.currentThread().setContextClassLoader(var3);
         var4 = true;
      }

      try {
         var1.activate();
      } finally {
         if (var4) {
            Thread.currentThread().setContextClassLoader(var2);
         }

      }

   }

   private void deactivate(WSEEModule var1) throws DeploymentException {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      ClassLoader var3 = var1.getClassLoader();
      boolean var4 = false;
      if (var2 != var3) {
         Thread.currentThread().setContextClassLoader(var3);
         var4 = true;
      }

      try {
         var1.deactivate();
      } finally {
         if (var4) {
            Thread.currentThread().setContextClassLoader(var2);
         }

      }

   }

   private void deactivate_and_unprepare(WSEEModule var1) throws DeploymentException {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      ClassLoader var3 = var1.getClassLoader();
      boolean var4 = false;
      if (var2 != var3) {
         Thread.currentThread().setContextClassLoader(var3);
         var4 = true;
      }

      try {
         var1.deactivate();
         var1.destroy();
      } finally {
         if (var4) {
            Thread.currentThread().setContextClassLoader(var2);
         }

      }

   }

   private void unprepare(WSEEModule var1) throws DeploymentException {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      ClassLoader var3 = var1.getClassLoader();
      boolean var4 = false;
      if (var2 != var3) {
         Thread.currentThread().setContextClassLoader(var3);
         var4 = true;
      }

      try {
         var1.destroy();
      } finally {
         if (var4) {
            Thread.currentThread().setContextClassLoader(var2);
         }

      }

   }

   public AppDeploymentExtension createPostProcessorExtension() {
      return new AppDeploymentExtension() {
         public void activate(ApplicationContextInternal var1) throws DeploymentException {
            Pair var2 = (Pair)AppDeploymentExtensionFactory.this.modules.get(var1);
            if (var2 != null) {
               synchronized(var2) {
                  Iterator var4;
                  WSEEModule var5;
                  if (var2.getRight() == AppDeploymentExtensionFactory.State.NOT_PREPARED) {
                     var2.setRight(AppDeploymentExtensionFactory.State.PREPARED);
                     var4 = ((Collection)var2.getLeft()).iterator();

                     while(var4.hasNext()) {
                        var5 = (WSEEModule)var4.next();
                        AppDeploymentExtensionFactory.this.prepare(var5);
                     }
                  }

                  var2.setRight(AppDeploymentExtensionFactory.State.ACTIVE);
                  var4 = ((Collection)var2.getLeft()).iterator();

                  while(var4.hasNext()) {
                     var5 = (WSEEModule)var4.next();
                     AppDeploymentExtensionFactory.this.activate(var5);
                  }
               }
            }

         }

         public void deactivate(ApplicationContextInternal var1) throws DeploymentException {
            Pair var2 = (Pair)AppDeploymentExtensionFactory.this.modules.get(var1);
            if (var2 != null) {
               synchronized(var2) {
                  var2.setRight(AppDeploymentExtensionFactory.State.PREPARED);
                  Iterator var4 = ((Collection)var2.getLeft()).iterator();

                  while(var4.hasNext()) {
                     WSEEModule var5 = (WSEEModule)var4.next();
                     AppDeploymentExtensionFactory.this.deactivate(var5);
                  }
               }
            }

         }

         public String getName() {
            return AppDeploymentExtension.class.getName();
         }

         public void prepare(ApplicationContextInternal var1) throws DeploymentException {
            Pair var2 = (Pair)AppDeploymentExtensionFactory.this.modules.get(var1);
            if (var2 != null) {
               synchronized(var2) {
                  var2.setRight(AppDeploymentExtensionFactory.State.PREPARED);
                  Iterator var4 = ((Collection)var2.getLeft()).iterator();

                  while(var4.hasNext()) {
                     WSEEModule var5 = (WSEEModule)var4.next();
                     AppDeploymentExtensionFactory.this.prepare(var5);
                  }
               }
            }

         }

         public void unprepare(ApplicationContextInternal var1) throws DeploymentException {
            Pair var2 = (Pair)AppDeploymentExtensionFactory.this.modules.remove(var1);
            if (var2 != null) {
               synchronized(var2) {
                  var2.setRight(AppDeploymentExtensionFactory.State.NOT_PREPARED);
                  Iterator var4 = ((Collection)var2.getLeft()).iterator();

                  while(var4.hasNext()) {
                     WSEEModule var5 = (WSEEModule)var4.next();
                     AppDeploymentExtensionFactory.this.unprepare(var5);
                  }
               }
            }

         }

         public void init(ApplicationContextInternal var1) {
         }
      };
   }

   public AppDeploymentExtension createPreProcessorExtension() {
      return null;
   }

   private static enum State {
      NOT_PREPARED,
      PREPARED,
      ACTIVE;
   }
}
