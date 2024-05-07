package weblogic.deploy.api.internal.utils;

import java.io.IOException;
import java.util.ArrayList;
import weblogic.deploy.api.model.WebLogicDeployableObject;
import weblogic.utils.classloaders.ClassFinder;

public class ClassLoaderControl {
   private ClassLoader oldCL;
   private WebLogicDeployableObject wldObject;
   private ArrayList resources;
   private ClassLoader cl;

   protected void finalize() throws Throwable {
      if (this.cl != null) {
         this.close();
      }

   }

   public ClassLoaderControl(WebLogicDeployableObject var1, ClassLoader var2) {
      this.oldCL = null;
      this.resources = new ArrayList();
      this.cl = null;
      this.wldObject = var1;
      this.oldCL = var2;
   }

   public ClassLoaderControl(WebLogicDeployableObject var1) {
      this(var1, Thread.currentThread().getContextClassLoader());
   }

   public ClassLoader getClassLoader() throws IOException {
      if (this.cl == null) {
         this.cl = this.createClassLoader();
      }

      return this.cl;
   }

   private ClassLoader createClassLoader() throws IOException {
      return this.wldObject.getOrCreateGCL();
   }

   public void restoreClassloader() {
      this.close();
      Thread.currentThread().setContextClassLoader(this.oldCL);
   }

   public void close() {
      for(int var1 = 0; var1 < this.resources.size(); ++var1) {
         ClassFinder var2 = (ClassFinder)this.resources.get(var1);
         var2.close();
      }

      this.resources.clear();
      this.cl = null;
   }
}
