package weblogic.ant.taskdefs.build;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import weblogic.ant.taskdefs.utils.AntLibraryUtils;
import weblogic.ant.taskdefs.utils.LibraryElement;
import weblogic.application.library.J2EELibraryReference;
import weblogic.application.library.LibraryInitializer;
import weblogic.application.library.LibraryManager;
import weblogic.application.library.LibraryProcessingException;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.application.utils.LibraryLoggingUtils;
import weblogic.application.utils.LibraryUtils;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.internal.War;
import weblogic.servlet.internal.WebAppDescriptor;
import weblogic.servlet.utils.WarUtils;
import weblogic.servlet.utils.WebAppLibraryUtils;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class LibClasspathTask extends Task {
   private File tmpdir = null;
   private boolean userSetTmpdir = false;
   private File basedir;
   private File basewar = null;
   private String property;
   private String classpathproperty;
   private String resourcepathproperty;
   private final Collection libdirs = new ArrayList();
   private final Collection libraries = new ArrayList();

   public void addConfiguredLibrary(LibraryElement var1) {
      if (var1.getFile() == null) {
         throw new BuildException("Location of Library must be set");
      } else {
         this.libraries.add(var1);
      }
   }

   public void addConfiguredLibrarydir(LibraryElement var1) {
      if (var1.getDir() == null) {
         throw new BuildException("Library dir must be set");
      } else {
         this.libdirs.add(var1.getDir());
      }
   }

   public void setProperty(String var1) {
      this.property = var1;
   }

   public void setClasspathProperty(String var1) {
      this.classpathproperty = var1;
   }

   public void setResourcepathProperty(String var1) {
      this.resourcepathproperty = var1;
   }

   public void setlibraryDir(File var1) {
      this.libdirs.add(var1);
   }

   public void settmpdir(File var1) {
      this.tmpdir = var1;
      this.userSetTmpdir = true;
   }

   public void setBasedir(File var1) {
      this.basedir = var1;
   }

   public void setBasewar(File var1) {
      this.basewar = var1;
   }

   private void checkProperty() {
      if (this.property != null && this.classpathproperty == null) {
         this.classpathproperty = this.property;
         this.log("The \"property\" attribute has been deprecated, please use the \"classpathproperty\" attribute instead.");
      }

      if (this.classpathproperty == null && this.resourcepathproperty == null) {
         throw new BuildException("The \"classpathproperty\" and/or \"resourcepathproperty\" attributes must be set.");
      }
   }

   private void checktmpdir() {
      if (!this.userSetTmpdir) {
         throw new BuildException("tmpdir must be set.");
      } else {
         if (this.tmpdir.exists()) {
            if (!this.tmpdir.isDirectory()) {
               throw new BuildException("tmpdir: " + this.tmpdir.getAbsolutePath() + " is not a directory.");
            }
         } else if (!this.tmpdir.mkdirs()) {
            throw new BuildException("tmpdir " + this.tmpdir.getAbsolutePath() + " does not exist, and we were unable to create it.");
         }

      }
   }

   private void checkbasedir() {
      if (this.basewar == null) {
         if (this.basedir == null) {
            this.basedir = new File(this.getProject().getProperty("basedir"));
            if (this.basedir == null) {
               this.basedir = new File(System.getProperty("user.dir"));
            }
         }

         if (!this.basedir.exists()) {
            throw new BuildException("basedir " + this.basedir.getAbsolutePath() + " does not exist.");
         }

         if (!this.basedir.isDirectory()) {
            throw new BuildException("basedir: " + this.basedir.getAbsolutePath() + " is not a directory.");
         }
      } else {
         ArrayList var1 = new ArrayList(1);
         LibraryElement var2 = new LibraryElement();
         var2.setFile(this.basewar);
         var1.add(var2);
         AntLibraryUtils.validateLibraries((Collection)this.libdirs, var1);
      }

   }

   private void checkParameters() {
      this.checkProperty();
      this.checktmpdir();
      this.checkbasedir();
      AntLibraryUtils.validateLibraries(this.libdirs, this.libraries);
   }

   public void execute() {
      this.checkParameters();

      try {
         this.initLibraries();
         LibraryResources var1 = this.getLibraryResouces();
         if (var1 != null) {
            if (var1.getClassPath() != null && this.classpathproperty != null) {
               this.getProject().setProperty(this.classpathproperty, var1.getClassPath());
            }

            if (var1.getResourcePath() != null && this.resourcepathproperty != null) {
               this.getProject().setProperty(this.resourcepathproperty, var1.getResourcePath());
            }

            return;
         }

         this.log("basedir does not point to an application or to a module that uses libraries");
      } finally {
         LibraryLoggingUtils.partialCleanupAndRemove();
         this.libdirs.clear();
         this.libraries.clear();
      }

   }

   private void initLibraries() {
      LibraryInitializer var1 = new LibraryInitializer(this.tmpdir, LibraryUtils.initDumbAppLibraryFactories());
      AntLibraryUtils.registerLibraries(var1, (File[])((File[])this.libdirs.toArray(new File[this.libdirs.size()])), (LibraryElement[])((LibraryElement[])this.libraries.toArray(new LibraryElement[this.libraries.size()])), false);
      this.log("Registered libraries: ", 3);
      AntLibraryUtils.logRegistryContent(this.getProject(), 3);

      try {
         var1.initRegisteredLibraries();
      } catch (LoggableLibraryProcessingException var3) {
         var3.printStackTrace();
         throw new BuildException(var3.getLoggable().getMessage(), var3);
      }
   }

   private LibraryResources getLibraryResouces() {
      if (this.basewar != null) {
         return this.getWebAppLibResources(this.basewar);
      } else if ((new File(this.basedir, "META-INF/weblogic-application.xml")).exists()) {
         return this.getAppLibResources(this.basedir);
      } else {
         return (new File(this.basedir, WarUtils.WEBLOGIC_XML)).exists() ? this.getWebAppLibResources(this.basedir) : null;
      }
   }

   private LibraryResources getAppLibResources(File var1) {
      BuildCtx var2 = new BuildCtx();

      LibraryResources var5;
      try {
         J2EELibraryReference[] var3 = LibraryUtils.initLibRefs(var1);
         LibraryManager var4 = new LibraryManager(LibraryUtils.initAppReferencer(), var3);
         this.initAppLibManager(var4);
         LibraryUtils.importAppLibraries(var4, var2, var2);
         var5 = new LibraryResources(AntLibraryUtils.getClassPath(var2.getLibraryFiles(), var2.getLibraryClassFinder()), (String)null);
      } catch (LoggableLibraryProcessingException var10) {
         throw new BuildException(var10.getLoggable().getMessage(), var10);
      } catch (LibraryProcessingException var11) {
         throw new BuildException(var11);
      } finally {
         var2.getLibraryClassFinder().close();
      }

      return var5;
   }

   private LibraryResources getWebAppLibResources(File var1) {
      VirtualJarFile var2 = null;
      War var3 = null;

      LibraryResources var7;
      try {
         var2 = VirtualJarFactory.createVirtualJar(var1);
         WebAppDescriptor var4 = new WebAppDescriptor(var2);
         var3 = new War("libclasspath", this.tmpdir, var2);
         WeblogicWebAppBean var5 = WarUtils.getWlWebAppBean(var4);
         LibraryManager var6 = WebAppLibraryUtils.getEmptyWebAppLibraryManager(var1.getName());
         WebAppLibraryUtils.initWebAppLibraryManager(var6, var5, var1.getName());
         WebAppLibraryUtils.extractWebAppLibraries(var6, var3, this.tmpdir);
         var7 = this.getWebAppLibResources(var3);
      } catch (IOException var17) {
         throw new BuildException(var17);
      } catch (ToolFailureException var18) {
         throw new BuildException(var18);
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var16) {
            }
         }

         if (var3 != null) {
            var3.getClassFinder().close();
         }

      }

      return var7;
   }

   private LibraryResources getWebAppLibResources(War var1) {
      String var2 = var1.getClassFinder().getClassPath();
      String var3 = null;
      List var4 = var1.getBeaExtensionRoots();
      if (var4 != null && var4.size() > 0) {
         StringBuffer var5 = new StringBuffer();
         Iterator var6 = var4.iterator();

         while(var6.hasNext()) {
            File var7 = (File)var6.next();
            var5.append(var7.getAbsolutePath());
            if (var6.hasNext()) {
               var5.append(File.pathSeparator);
            }
         }

         var3 = var5.toString();
      }

      return new LibraryResources(var2, var3);
   }

   private void initAppLibManager(LibraryManager var1) {
      try {
         LibraryLoggingUtils.verifyLibraryReferences(var1);
         var1.initializeReferencedLibraries();
      } catch (LoggableLibraryProcessingException var3) {
         throw new BuildException(var3.getLoggable().getMessage(), var3);
      }
   }

   private static final class LibraryResources {
      private final String classpath;
      private final String resourcepath;

      LibraryResources(String var1, String var2) {
         if (var1 != null && var1.trim().length() > 0) {
            this.classpath = var1;
         } else {
            this.classpath = null;
         }

         if (var2 != null && var2.trim().length() > 0) {
            this.resourcepath = var2;
         } else {
            this.resourcepath = null;
         }

      }

      String getClassPath() {
         return this.classpath;
      }

      String getResourcePath() {
         return this.resourcepath;
      }
   }
}
