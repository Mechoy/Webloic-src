package weblogic.servlet.jsp;

import java.net.URI;
import weblogic.version;
import weblogic.jsp.compiler.jsp.IWebAppProjectFeature;
import weblogic.jsp.wlw.filesystem.IFile;
import weblogic.jsp.wlw.util.filesystem.FS;
import weblogic.utils.classloaders.GenericClassLoader;

public class JavelinxTagFileStaleChecker implements StaleChecker {
   private static final boolean debug = false;
   private final IWebAppProjectFeature webapp;

   public JavelinxTagFileStaleChecker(IWebAppProjectFeature var1) {
      this.webapp = var1;
   }

   public boolean isResourceStale(String var1, long var2, String var4, String var5) {
      if (!version.getReleaseBuildVersion().equals(var4)) {
         return true;
      } else {
         URI var6 = this.webapp.findResource(var1);
         IFile var7 = FS.getIFile(var6);
         return var2 < var7.lastModified();
      }
   }

   public static boolean isClassStale(String var0, ClassLoader var1, IWebAppProjectFeature var2) {
      if (!(var1 instanceof GenericClassLoader)) {
         return true;
      } else {
         boolean var3 = false;
         JspClassLoader var4 = null;

         try {
            var4 = new JspClassLoader(((GenericClassLoader)var1).getClassFinder(), var1, var0);
            Class var5 = var4.loadClass(var0);
            var3 = JspStub.isJSPClassStale(var5, new JavelinxTagFileStaleChecker(var2));
         } catch (Throwable var9) {
            var3 = true;
         } finally {
            var4 = null;
         }

         if (var3 && var1 instanceof TagFileClassLoader) {
            TagFileClassLoader var11 = (TagFileClassLoader)var1;
            var11.forceToBounce();
         }

         return var3;
      }
   }
}
