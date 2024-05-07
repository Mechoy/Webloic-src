package weblogic.servlet.jsp;

import weblogic.servlet.internal.WebAppServletContext;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;

public class TagFileHelper {
   private static final boolean debug = false;
   private WebAppServletContext context;
   private TagFileClassLoader tagFileClassLoader;
   private long creationTime;
   private String jspWorkingDir;

   public TagFileHelper(WebAppServletContext var1) {
      this.context = var1;
   }

   public void initClassLoader(ClassFinder var1, ClassLoader var2) {
      this.initClassLoader(var1, var2, false);
   }

   private void initClassLoader(ClassFinder var1, ClassLoader var2, boolean var3) {
      this.tagFileClassLoader = new TagFileClassLoader(var1, var2);
      if (!var3) {
         this.addWorkingDirFinder(this.tagFileClassLoader);
      }

      this.creationTime = this.tagFileClassLoader.getCreationTime();
   }

   public TagFileClassLoader getTagFileClassLoader() {
      return this.tagFileClassLoader;
   }

   long getCLCreationTime() {
      return this.creationTime;
   }

   void reloadIfNecessary(long var1) {
      if (var1 >= 0L) {
         if (this.checkReloadTimeout(var1)) {
            synchronized(this) {
               if (this.checkReloadTimeout(var1) && this.needToReloadCL()) {
                  this.reloadTagFileClassLoader();
               }
            }
         }

      }
   }

   private boolean checkReloadTimeout(long var1) {
      if (var1 < 0L) {
         return false;
      } else {
         long var3 = var1 * 1000L;
         return System.currentTimeMillis() - var3 > this.tagFileClassLoader.getLastChecked();
      }
   }

   private boolean needToReloadCL() {
      return !this.tagFileClassLoader.upToDate();
   }

   private void reloadTagFileClassLoader() {
      ClassFinder var1 = this.context.getWarInstance().getClassFinder();
      ClassLoader var2 = this.context.getServletClassLoader();
      this.initClassLoader(var1, var2, false);
   }

   private void addWorkingDirFinder(TagFileClassLoader var1) {
      if (this.jspWorkingDir == null) {
         this.jspWorkingDir = this.context.getJSPManager().getJSPWorkingDir();
      }

      var1.addClassFinderFirst(new ClasspathClassFinder2(this.jspWorkingDir));
   }

   public void close() {
      this.tagFileClassLoader = null;
   }
}
