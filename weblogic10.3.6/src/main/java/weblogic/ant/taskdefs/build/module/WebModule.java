package weblogic.ant.taskdefs.build.module;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;
import weblogic.ant.taskdefs.build.BuildCtx;
import weblogic.ant.taskdefs.build.WLCompileTask;
import weblogic.application.library.LibraryManager;
import weblogic.application.library.LibraryReference;
import weblogic.application.library.LibraryReferenceFactory;
import weblogic.servlet.internal.War;
import weblogic.servlet.utils.WebAppLibraryUtils;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class WebModule extends Module {
   private static final String WEBINF_CLASSES;
   private static final String WEBINF_SRC;
   private static final File tmpDir;
   private War war = null;

   public WebModule(BuildCtx var1, File var2, File var3) {
      super(var1, var2, var3);
   }

   public void addToClasspath(Path var1) {
      try {
         VirtualJarFile var2 = VirtualJarFactory.createVirtualJar(new File[]{this.srcDir, this.destDir});
         this.war = new War(this.srcDir.getName(), tmpDir, var2);
         this.addLibraries(this.war);
         this.addToClasspath(var1, this.war.getClassFinder().getClassPath());
      } catch (IOException var3) {
         throw new BuildException(var3);
      }
   }

   private void addLibraries(War var1) throws IOException {
      LibraryManager var2 = WebAppLibraryUtils.getEmptyWebAppLibraryManager();
      var2.lookup((LibraryReference[])LibraryReferenceFactory.getWebAppLibReference());
      WebAppLibraryUtils.extractWebAppLibraries(var2, var1, WLCompileTask.libraryTmpDir);
   }

   private void addToClasspath(Path var1, String var2) {
      Path.PathElement var3 = var1.createPathElement();
      var3.setPath(var2);
   }

   public void build(Path var1) {
      try {
         this.log("Compiling module: " + this.getClass().getName() + ": " + this.srcDir);
         this.destDir.mkdir();
         File var2 = new File(this.srcDir, WEBINF_SRC);
         if (var2.exists() && var2.isDirectory()) {
            this.javac(var1, var2, new File(this.destDir, WEBINF_CLASSES));
         }
      } finally {
         if (this.war != null) {
            this.war.getClassFinder().close();
            this.war.remove();
         }

      }

   }

   static {
      WEBINF_CLASSES = File.separatorChar + "WEB-INF" + File.separatorChar + "classes";
      WEBINF_SRC = File.separatorChar + "WEB-INF" + File.separatorChar + "src";
      tmpDir = new File(System.getProperty("java.io.tmpdir"), "wlcmp");
   }
}
