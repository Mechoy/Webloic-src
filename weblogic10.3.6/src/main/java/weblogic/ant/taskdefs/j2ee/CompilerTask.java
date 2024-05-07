package weblogic.ant.taskdefs.j2ee;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

public abstract class CompilerTask extends MatchingTask {
   private static final Class[] MAIN_SIGNATURE = new Class[]{String[].class};
   private static final Set inProcessJavacNames = new HashSet();
   protected String compiler = null;
   protected String version = null;
   protected Path compileClasspath = null;
   protected String compilerClass = null;
   protected boolean keepgenerated = false;
   protected boolean commentary = false;
   protected String destdir = null;
   protected boolean debug = false;
   protected boolean optimize = false;
   protected boolean noexit = true;
   protected boolean nowarn = false;
   protected boolean nowrite = false;
   protected boolean normi = false;
   protected boolean deprecation = false;
   protected boolean verboseJavac = false;
   protected String dispatchPolicy = null;
   protected boolean supressCompiler = false;
   protected String runtimeFlags = null;

   public void setCompiler(String var1) {
      this.compiler = var1;
   }

   public void setVersion(String var1) {
      this.version = var1;
   }

   public void setClasspath(Path var1) {
      if (this.compileClasspath == null) {
         this.compileClasspath = var1;
      } else {
         this.compileClasspath.append(var1);
      }

   }

   public Path createClasspath() {
      if (this.compileClasspath == null) {
         this.compileClasspath = new Path(this.project);
      }

      return this.compileClasspath.createPath();
   }

   public void setClasspathRef(Reference var1) {
      this.createClasspath().setRefid(var1);
   }

   public void setKeepGenerated(boolean var1) {
      this.keepgenerated = var1;
   }

   public void setCommentary(boolean var1) {
      this.commentary = var1;
   }

   public void setDestDir(String var1) {
      this.destdir = var1;
   }

   public void setDispatchPolicy(String var1) {
      this.dispatchPolicy = var1;
   }

   public void setRuntimeFlags(String var1) {
      this.runtimeFlags = var1;
   }

   public void setDebug(boolean var1) {
      this.debug = var1;
   }

   public void setOptimize(boolean var1) {
      this.optimize = var1;
   }

   public void setNoWarn(boolean var1) {
      this.nowarn = var1;
   }

   public void setNoWrite(boolean var1) {
      this.nowrite = var1;
   }

   public void setNoRmi(boolean var1) {
      this.normi = var1;
   }

   public void setDeprecation(boolean var1) {
      this.deprecation = var1;
   }

   public void setVerboseJavac(boolean var1) {
      this.verboseJavac = var1;
   }

   public void setExit() {
      this.noexit = false;
   }

   private void setCompilerFlags(Vector var1) {
      if (this.version != null) {
         var1.addElement("-source");
         var1.addElement(this.version);
         var1.addElement("-target");
         var1.addElement(this.version);
      } else {
         var1.addElement("-source");
         var1.addElement(System.getProperty("java.specification.version"));
      }

      if (!this.supressCompiler) {
         if (this.compiler == null && this.compilerClass == null) {
            String var2 = this.getProject().getProperty("build.compiler");
            if (var2 != null) {
               if ("weblogicx.ant.sj".equals(var2)) {
                  this.compiler = "sj";
               } else if ("extJavac".equals(var2)) {
                  this.compiler = "javac";
               } else if (inProcessJavacNames.contains(var2)) {
                  this.compilerClass = "com.sun.tools.javac.Main";
               }
            }
         }

         if (this.compiler != null) {
            var1.addElement("-compiler");
            var1.addElement(this.compiler);
         } else if (this.compilerClass != null) {
            var1.addElement("-compilerClass");
            var1.addElement(this.compilerClass);
         }

      }
   }

   protected Vector getFlags() {
      Vector var1 = new Vector();
      this.setCompilerFlags(var1);
      if (this.destdir != null) {
         var1.addElement("-d");
         var1.addElement(this.destdir);
      }

      if (this.dispatchPolicy != null) {
         var1.addElement("-dispatchPolicy");
         var1.addElement(this.dispatchPolicy);
      }

      if (this.runtimeFlags != null) {
         var1.addElement(this.runtimeFlags);
      }

      if (this.keepgenerated) {
         var1.addElement("-keepgenerated");
      }

      if (this.commentary) {
         var1.addElement("-commentary");
      }

      if (this.debug) {
         var1.addElement("-g");
      }

      if (this.optimize) {
         var1.addElement("-O");
      }

      if (this.nowarn) {
         var1.addElement("-nowarn");
      }

      if (this.verboseJavac) {
         var1.addElement("-verboseJavac");
      }

      if (this.nowrite) {
         var1.addElement("-nowrite");
      }

      if (this.normi) {
         var1.addElement("-normi");
      }

      if (this.deprecation) {
         var1.addElement("-deprecation");
      }

      if (!this.supressCompiler) {
         Path var2 = this.getCompileClasspath();
         var1.addElement("-classpath");
         var1.addElement(var2.toString());
      }

      if (this.noexit) {
         var1.addElement("-noexit");
      }

      return var1;
   }

   protected String[] getArgs(Vector var1) {
      String[] var2 = new String[var1.size()];

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         var2[var3] = (String)var1.elementAt(var3);
      }

      return var2;
   }

   protected Path getCompileClasspath() {
      Path var1 = new Path(this.project);
      if (this.destdir != null) {
         File var2 = this.project.resolveFile(this.destdir);
         var1.addExisting(new Path(this.project, var2.getAbsolutePath()));
      }

      if (this.compileClasspath != null) {
         var1.addExisting(this.compileClasspath);
      }

      var1.addExisting(Path.systemClasspath);
      if (Project.getJavaVersion().startsWith("1.2")) {
         String var3 = System.getProperty("sun.boot.class.path");
         if (var3 != null) {
            var1.addExisting(new Path(this.project, var3));
         }
      }

      return var1;
   }

   protected void invokeMain(String var1, String[] var2) {
      try {
         File var3 = new File(this.getProject().getProperty("dest") + "/classes");
         URL[] var12 = new URL[]{var3.toURL()};
         URLClassLoader var5 = new URLClassLoader(var12, this.getClass().getClassLoader());
         Class var6 = var5.loadClass(var1);
         Method var7 = null;
         var7 = var6.getMethod("main", MAIN_SIGNATURE);
         Object[] var8 = new Object[]{var2};
         var7.invoke((Object)null, var8);
      } catch (BuildException var9) {
         throw var9;
      } catch (InvocationTargetException var10) {
         Object var4 = var10.getTargetException();
         if (var4 == null) {
            var4 = var10;
         }

         throw new BuildException((Throwable)var4);
      } catch (Exception var11) {
         throw new BuildException(var11);
      }
   }

   static {
      inProcessJavacNames.add("classic");
      inProcessJavacNames.add("javac1.1");
      inProcessJavacNames.add("javac1.2");
      inProcessJavacNames.add("modern");
      inProcessJavacNames.add("javac1.3");
      inProcessJavacNames.add("javac1.4");
   }
}
