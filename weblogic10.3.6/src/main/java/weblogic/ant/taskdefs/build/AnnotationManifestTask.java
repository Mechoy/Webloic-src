package weblogic.ant.taskdefs.build;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.zip.ZipFile;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

public final class AnnotationManifestTask extends Task {
   private Path _classpath;
   private Path _searchPath;
   private String _moduleDir;
   private String _moduleName;
   private String _stagingDir = ".staging";
   private boolean _verbose = false;
   private String _version = "";
   private static final String MANIFEST_DIR = "META-INF";
   private static final String MANIFEST_FILENAME = "annotation-manifest.xml";
   private static final String MANIFESTMERGE_CLASS = "weblogic.controls.properties.ManifestMerger";
   private static final String GENERATEMANIFEST_CLASS = "weblogic.controls.properties.GenerateManifest";
   private static final String JAR_MANIFEST_TARGET_NAME = "weblogic.insert.manifest";

   public Path createClasspath() {
      if (this._classpath == null) {
         this._classpath = new Path(this.getProject());
      }

      return this._classpath.createPath();
   }

   public void setClasspath(Path var1) {
      if (this._classpath == null) {
         this._classpath = var1;
      } else {
         this._classpath.append(var1);
      }

   }

   public void setClasspathRef(Reference var1) {
      this.createClasspath().setRefid(var1);
   }

   public Path createSearchClasspath() {
      if (this._searchPath == null) {
         this._searchPath = new Path(this.getProject());
      }

      return this._searchPath.createPath();
   }

   public void setSearchClasspath(Path var1) {
      if (this._searchPath == null) {
         this._searchPath = var1;
      } else {
         this._searchPath.append(var1);
      }

   }

   public void setSearchClasspathRef(Reference var1) {
      this.createSearchClasspath().setRefid(var1);
   }

   public void setModuleDir(String var1) {
      this._moduleDir = var1;
      if (this._moduleDir != null && this._moduleDir.length() != 0) {
         this._moduleName = (new File(this._moduleDir)).getName();
      } else {
         throw new BuildException("Invalid moduleDir: " + this._moduleDir);
      }
   }

   public void setStagingDir(String var1) {
      if (var1 != null && !var1.equals("")) {
         this._stagingDir = var1;
      }

   }

   public void setVerbose(String var1) {
      if (var1 != null) {
         Boolean var2 = Boolean.valueOf(var1);
         if (var2 != null) {
            this._verbose = var2;
         }
      }

   }

   public void setVersion(String var1) {
      if (var1 != null) {
         this._version = var1;
      }

   }

   public void execute() throws BuildException {
      this.checkParameters();
      System.out.println("========================================================");
      System.out.println("| Start Building Annotation Manifest for " + this._moduleName);
      System.out.println("========================================================");
      long var1 = System.currentTimeMillis();
      GenManifestTarget var3 = new GenManifestTarget(this.getProject(), this._moduleDir, this._stagingDir, this._classpath, this._searchPath);
      var3.setVerbose(this._verbose);
      var3.setVersion(this._version);
      var3.execute();
      long var4 = System.currentTimeMillis();
      System.out.println("========================================================");
      System.out.println("| Finished Building Annotation Manifest in " + (var4 - var1) + " millis");
      System.out.println("========================================================");
   }

   private void checkParameters() throws BuildException {
      if (this._classpath != null && this._classpath.size() != 0) {
         if (this._searchPath != null && this._searchPath.size() != 0) {
            if (this._stagingDir != null && this._stagingDir.length() != 0) {
               if (this._moduleDir != null && this._moduleDir.length() != 0) {
                  File var1 = new File(this._moduleDir);
                  if (!var1.exists()) {
                     throw new BuildException("Module directory does not exist: " + var1.getAbsolutePath());
                  }
               } else {
                  throw new BuildException("Must define module directory");
               }
            } else {
               throw new BuildException("Must define staging directory");
            }
         } else {
            throw new BuildException("Must define searchClasspath");
         }
      } else {
         throw new BuildException("Must define classpath");
      }
   }

   private static void handleGeneralBuildError(Throwable var0) {
      Throwable var1 = var0;
      if (var0 instanceof InvocationTargetException) {
         var1 = var0.getCause();
      }

      throw new BuildException(var1);
   }

   private static class GenManifestTarget extends Target {
      private String _moduleDir;
      private String _stagingDir;
      private String[] _classpathEntries;
      private String[] _searchPathEntries;
      private boolean _verbose;
      private String _version;
      private static final String UNJAR_TASK_NAME = "unjar";
      private static final String JAR_TASK_NAME = "jar";
      private static final String TEMP_JAR_NAME = "temp.jar";

      protected GenManifestTarget(Project var1, String var2, String var3, Path var4, Path var5) {
         this.setName("weblogic.insert.manifest");
         this.setDescription("Generates Annotation Manifest for module");
         this.setProject(var1);
         this._moduleDir = var2;
         this._stagingDir = var3;

         assert var4 != null && var5 != null;

         this._classpathEntries = var4.list();
         this._searchPathEntries = var5.list();
         this._verbose = false;
      }

      public void execute() throws BuildException {
         this.deleteStagedFiles();
         ClassLoader var1 = this.getClassLoader(this._classpathEntries, this._searchPathEntries);

         for(int var2 = 0; var2 < this._searchPathEntries.length; ++var2) {
            File var3 = new File(this._searchPathEntries[var2]);
            if (!var3.exists()) {
               if (this._verbose) {
                  System.out.println(var3.getName() + " does not exist.");
               }
            } else if (var3.isDirectory()) {
               String var7 = var3.getAbsolutePath() + File.separator + "META-INF" + File.separator + "annotation-manifest.xml";
               if (this._verbose) {
                  System.out.println("Generating manifest for class directory " + var3.getAbsolutePath());
               }

               this.invokeGenerateManifest(var7, var3.getAbsolutePath(), var1);
            } else if (var3.isFile()) {
               ZipFile var4 = null;

               try {
                  var4 = new ZipFile(var3);
               } catch (Exception var6) {
                  System.out.println(var3.getName() + " is not a valid jar file");
                  if (this._verbose) {
                     var6.printStackTrace();
                  }
               }

               if (var4 != null) {
                  this.fixJar(var3, var1);
               }
            }
         }

         this.invokeMergeManifest(this._searchPathEntries, this._moduleDir, var1);
         this.deleteStagedFiles();
      }

      public void setVerbose(boolean var1) {
         this._verbose = var1;
      }

      public void setVersion(String var1) {
         this._version = var1;
      }

      private void deleteStagedFiles() {
         try {
            File var1 = new File(this._stagingDir);
            if (var1.exists()) {
               FileSet var2 = new FileSet();
               Delete var3 = new Delete();
               var3.setTaskName("delete");
               var3.setProject(this.getProject());
               var3.setIncludeEmptyDirs(true);
               var2.setDir(var1);
               var2.setIncludes("**/**");
               var3.addFileset(var2);
               var3.execute();
            }
         } catch (Exception var4) {
            System.out.println("Unable to delete files from " + this._stagingDir);
         }

      }

      private void fixJar(File var1, ClassLoader var2) {
         if (this._verbose) {
            System.out.println("Generating manifest for " + var1.getName());
         }

         String var3 = this._stagingDir + File.separator + var1.getName();
         File var4 = new File(var3);
         File var5 = new File(var3 + File.separator + "META-INF");
         if (!var5.exists()) {
            var5.mkdirs();
         }

         Expand var6 = new Expand();
         var6.setTaskName("unjar");
         var6.setProject(this.getProject());
         var6.setDest(var4);
         var6.setSrc(var1);
         var6.setOverwrite(true);
         var6.execute();
         String var7 = var3 + File.separator + "META-INF" + File.separator + "annotation-manifest.xml";
         this.invokeGenerateManifest(var7, var4.getAbsolutePath(), var2);
         File var8 = new File(var7);
         if (!var8.exists()) {
            if (this._verbose) {
               System.out.println(var1.getName() + " does not have configurable annotations.");
            }

         } else {
            Jar var9 = new Jar();
            var9.setTaskName("jar");
            var9.setProject(this.getProject());
            File var10 = new File(var3 + File.separator + "temp.jar");
            if (var10.exists()) {
               var10.delete();
            }

            var9.setDestFile(var10);
            var9.setBasedir(var4);
            var9.execute();
            Copy var11 = new Copy();
            var11.setTaskName(var1.getName());
            var11.setProject(this.getProject());
            var11.setFile(var10);
            var11.setTofile(var1);
            var11.setOverwrite(true);
            var11.execute();
            if (this._verbose) {
               System.out.println("Inserted manifest for " + var1.getName());
            }

         }
      }

      private void invokeGenerateManifest(String var1, String var2, ClassLoader var3) throws BuildException {
         try {
            Class var4 = var3.loadClass("weblogic.controls.properties.GenerateManifest");
            Class[] var5 = new Class[]{String[].class};
            Method var6 = var4.getDeclaredMethod("doGenerate", var5);
            Object[] var7 = new Object[]{new String[]{var2, var1, Boolean.toString(this._verbose), this._version}};
            var6.invoke((Object)null, var7);
         } catch (ClassNotFoundException var8) {
            throw new BuildException("weblogic.controls.properties.GenerateManifest is not on the classpath");
         } catch (Exception var9) {
            AnnotationManifestTask.handleGeneralBuildError(var9);
         }

      }

      private void invokeMergeManifest(String[] var1, String var2, ClassLoader var3) throws BuildException {
         try {
            Class var4 = var3.loadClass("weblogic.controls.properties.ManifestMerger");
            Class[] var5 = new Class[]{String.class, String[].class, Boolean.class};
            Method var6 = var4.getDeclaredMethod("mergeAll", var5);
            Object[] var7 = new Object[]{var2, var1, this._verbose};
            var6.invoke((Object)null, var7);
         } catch (ClassNotFoundException var8) {
            throw new BuildException("weblogic.controls.properties.ManifestMerger is not on the classpath");
         } catch (Exception var9) {
            AnnotationManifestTask.handleGeneralBuildError(var9);
         }

      }

      private ClassLoader getClassLoader(String[] var1, String[] var2) {
         assert var1 != null && var2 != null;

         URL[] var3 = new URL[var1.length + 1 + var2.length];
         int var4 = 0;

         int var5;
         URL var6;
         for(var5 = 0; var5 < var1.length; ++var5) {
            var6 = this.getURL(var1[var5]);
            if (var6 != null) {
               var3[var4++] = var6;
            }
         }

         for(var5 = 0; var5 < var2.length; ++var5) {
            var6 = this.getURL(var2[var5]);
            if (var6 != null) {
               var3[var4++] = var6;
            }
         }

         var3[var4++] = this.getURL(this._stagingDir);
         return new URLClassLoader(var3, System.class.getClassLoader());
      }

      private URL getURL(String var1) {
         URL var2 = null;

         try {
            var2 = (new File(var1)).toURL();
         } catch (Exception var4) {
            if (this._verbose) {
               var4.printStackTrace();
            }
         }

         return var2;
      }
   }
}
