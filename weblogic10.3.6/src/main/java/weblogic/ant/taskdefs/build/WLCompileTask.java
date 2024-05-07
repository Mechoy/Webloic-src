package weblogic.ant.taskdefs.build;

import com.bea.wls.ejbgen.ant.EJBGenAntTask;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.selectors.FileSelector;
import weblogic.ant.taskdefs.utils.AntLibraryUtils;
import weblogic.ant.taskdefs.utils.LibraryElement;
import weblogic.application.SplitDirectoryUtils;
import weblogic.application.library.LibraryInitializer;
import weblogic.application.library.LibraryManager;
import weblogic.application.library.LibraryReferenceFactory;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.application.utils.LibraryUtils;

public final class WLCompileTask extends MatchingTask {
   public static final File libraryTmpDir = new File(System.getProperty("java.io.tmpdir"), "wlcmp_libraries");
   private File srcdir;
   private File destdir;
   private File libdir;
   private Path compileClasspath;
   private Javac javacTask;
   private EJBGenAntTask ejbGenTask;
   private Collection libraries;
   private LibraryInitializer libraryInitializer;

   public WLCompileTask() {
      this.compileClasspath = (Path)Path.systemClasspath.clone();
      this.libraries = new ArrayList();
      this.libraryInitializer = null;
   }

   public void addConfiguredJavac(Javac var1) {
      this.javacTask = var1;
   }

   public void addConfiguredEjbgen(EJBGenAntTask var1) {
      this.ejbGenTask = var1;
   }

   public void setClasspath(Path var1) {
      this.compileClasspath.append(var1);
   }

   public Path createClasspath() {
      return this.compileClasspath.createPath();
   }

   public void setSrcdir(File var1) {
      this.srcdir = var1;
   }

   public void setDestdir(File var1) {
      this.destdir = var1;
   }

   public void setlibraryDir(File var1) {
      this.libdir = var1;
   }

   public void addConfiguredLibrary(LibraryElement var1) {
      this.libraries.add(var1);
   }

   private void checkSrcdir() throws BuildException {
      if (this.srcdir == null) {
         throw new BuildException("srcdir must be set");
      } else if (!this.srcdir.exists()) {
         throw new BuildException("srcdir: " + this.srcdir.getAbsolutePath() + " does not exist or cannot be read.");
      } else if (!this.srcdir.isDirectory()) {
         throw new BuildException("srcdir: " + this.srcdir.getAbsolutePath() + " is not a directory.");
      }
   }

   private void checkDestdir() throws BuildException {
      if (this.destdir == null) {
         throw new BuildException("destdir must be set");
      } else {
         if (this.destdir.exists()) {
            if (!this.destdir.isDirectory()) {
               throw new BuildException("destdir: " + this.destdir.getAbsolutePath() + " is not a directory.");
            }
         } else if (!this.destdir.mkdirs()) {
            throw new BuildException("destdir " + this.destdir.getAbsolutePath() + " does not exist, and we were unable to create it.");
         }

      }
   }

   private void checkParameters() throws BuildException {
      this.checkSrcdir();
      this.checkDestdir();
      AntLibraryUtils.validateLibraries(this.libdir, this.libraries);
   }

   public void execute() throws BuildException {
      Thread var1 = Thread.currentThread();
      ClassLoader var2 = var1.getContextClassLoader();
      ClassLoader var3 = this.getClass().getClassLoader();
      if (var2 != this.getClass().getClassLoader() && var3 instanceof AntClassLoader) {
         this.setClasspath(new Path(this.project, ((AntClassLoader)var3).getClasspath()));
      }

      try {
         var1.setContextClassLoader(var3);
         this.privateExecute();
      } finally {
         var1.setContextClassLoader(var2);
         if (this.libraryInitializer != null) {
            this.libraryInitializer.cleanup();
         }

      }

   }

   private void privateExecute() throws BuildException {
      this.log("Executing WLCompileTask", 3);
      this.fileset.setDir(this.srcdir);
      this.fileset.appendSelector(new BaseDirSelector());
      this.checkParameters();
      this.log("srcdir: " + this.srcdir.getAbsolutePath(), 3);
      this.log("destdir: " + this.destdir.getAbsolutePath(), 3);
      BuildCtx var1 = new BuildCtx();
      var1.setProject(this.project);
      var1.setSrcDir(this.srcdir);
      var1.setDestDir(this.destdir);
      var1.setJavacTask(this.javacTask);
      var1.setEJBGen(this.ejbGenTask);
      this.handleLibraries(var1);
      Application var2 = ApplicationFactory.newApplication(var1);
      var2.build(this.compileClasspath, this.buildDirSet(this.fileset));

      try {
         SplitDirectoryUtils.generatePropFile(this.srcdir, this.destdir);
      } catch (IOException var4) {
         var4.printStackTrace();
         throw new BuildException("Error generating build properties file", var4);
      }
   }

   private Set buildDirSet(FileSet var1) throws BuildException {
      DirectoryScanner var2 = var1.getDirectoryScanner(this.project);
      String[] var3 = var2.getIncludedDirectories();
      if (var3 != null && var3.length != 0) {
         HashSet var4 = new HashSet();

         for(int var5 = 0; var5 < var3.length; ++var5) {
            var4.add(new File(this.srcdir, var3[var5]));
         }

         return var4;
      } else {
         throw new BuildException("No modules were found to build.  Please ensure that your srcdir: " + this.srcdir.getAbsolutePath() + " is not empty, and your excludes or includes parameters do not " + "eliminate all directories.");
      }
   }

   private void handleLibraries(BuildCtx var1) {
      if (this.libdir != null || !this.libraries.isEmpty()) {
         try {
            this.libraryInitializer = new LibraryInitializer(libraryTmpDir, LibraryUtils.initDumbAppLibraryFactories());
            File[] var2 = null;
            if (this.libdir != null) {
               var2 = new File[]{this.libdir};
            }

            AntLibraryUtils.registerLibraries(this.libraryInitializer, var2, (LibraryElement[])((LibraryElement[])this.libraries.toArray(new LibraryElement[this.libraries.size()])), false);
            this.libraryInitializer.initRegisteredLibraries();
            LibraryManager var3 = new LibraryManager(LibraryUtils.initAppReferencer(), LibraryReferenceFactory.getAppLibReference());
            LibraryUtils.importAppLibraries(var3, var1, var1);
            this.addLibsToClasspath(var1);
         } catch (LoggableLibraryProcessingException var4) {
            throw new BuildException(var4.getLoggable().getMessage());
         }
      }
   }

   private void addLibsToClasspath(BuildCtx var1) {
      String var2 = AntLibraryUtils.getClassPath(var1.getLibraryFiles(), var1.getLibraryClassFinder());
      var1.getLibraryClassFinder().close();
      this.setClasspath(new Path(this.project, var2));
   }

   private static class BaseDirSelector implements FileSelector {
      private BaseDirSelector() {
      }

      public boolean isSelected(File var1, String var2, File var3) {
         return var3.isDirectory() && var3.getParentFile().equals(var1);
      }

      // $FF: synthetic method
      BaseDirSelector(Object var1) {
         this();
      }
   }
}
