package weblogic.wsee.tools.anttasks;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

public class DelegatingJavacTask extends Task {
   private ExposingJavac javac = new ExposingJavac();

   public void setDebugLevel(String var1) {
      this.javac.setDebugLevel(var1);
   }

   public void setSource(String var1) {
      this.javac.setSource(var1);
   }

   public void setDestdir(File var1) {
      this.javac.setDestdir(var1);
   }

   public void setSourcepath(Path var1) {
      this.javac.setSourcepath(var1);
   }

   public Path createSourcepath() {
      return this.javac.createSourcepath();
   }

   public void setSourcepathRef(Reference var1) {
      this.javac.setSourcepathRef(var1);
   }

   public void setClasspath(Path var1) {
      this.javac.setClasspath(var1);
   }

   public Path createClasspath() {
      return this.javac.createClasspath();
   }

   public void setClasspathRef(Reference var1) {
      this.javac.setClasspathRef(var1);
   }

   public void setBootclasspath(Path var1) {
      this.javac.setBootclasspath(var1);
   }

   public void setBootClasspathRef(Reference var1) {
      this.javac.setBootClasspathRef(var1);
   }

   public void setExtdirs(Path var1) {
      this.javac.setExtdirs(var1);
   }

   public Path createExtdirs() {
      return this.javac.createExtdirs();
   }

   public void setListfiles(boolean var1) {
      this.javac.setListfiles(var1);
   }

   public void setFailonerror(boolean var1) {
      this.javac.setFailonerror(var1);
   }

   public void setProceed(boolean var1) {
      this.javac.setProceed(var1);
   }

   public void setDeprecation(boolean var1) {
      this.javac.setDeprecation(var1);
   }

   public void setMemoryMaximumSize(String var1) {
      this.javac.setMemoryMaximumSize(var1);
   }

   public void setMemoryInitialSize(String var1) {
      this.javac.setMemoryInitialSize(var1);
   }

   public void setEncoding(String var1) {
      this.javac.setEncoding(var1);
   }

   public void setDebug(boolean var1) {
      this.javac.setDebug(var1);
   }

   public void setOptimize(boolean var1) {
      this.javac.setOptimize(var1);
   }

   public void setDepend(boolean var1) {
      this.javac.setDepend(var1);
   }

   public void setVerbose(boolean var1) {
      this.javac.setVerbose(var1);
   }

   public boolean isVerbose() {
      return this.javac.getVerbose();
   }

   public void setIncludeantruntime(boolean var1) {
      this.javac.setIncludeantruntime(var1);
   }

   public void setIncludejavaruntime(boolean var1) {
      this.javac.setIncludejavaruntime(var1);
   }

   public void setFork(boolean var1) {
      this.javac.setFork(var1);
   }

   public void setTempdir(File var1) {
      this.javac.setTempdir(var1);
   }

   public File getTempdir() {
      File var1 = this.javac.getTempdir();
      if (var1 == null) {
         var1 = new File(System.getProperty("java.io.tmpdir"));
      }

      return var1;
   }

   public void setProject(Project var1) {
      super.setProject(var1);
      this.javac.setProject(var1);
   }

   public void setTaskName(String var1) {
      super.setTaskName(var1);
      this.javac.setTaskName(var1);
   }

   public void setOwningTarget(Target var1) {
      super.setOwningTarget(var1);
      this.javac.setOwningTarget(var1);
   }

   public void setExecutable(String var1) {
      this.javac.setExecutable(var1);
   }

   public void setNowarn(boolean var1) {
      this.javac.setNowarn(var1);
   }

   public Javac.ImplementationSpecificArgument createCompilerArg() {
      return this.javac.createCompilerArg();
   }

   public void setCompiler(String var1) {
      this.javac.setCompiler(var1);
   }

   public boolean getFork() {
      return this.javac.isForkedJavac();
   }

   public boolean getVerbose() {
      return this.javac.getVerbose();
   }

   public boolean getOptimize() {
      return this.javac.getOptimize();
   }

   public boolean getDebug() {
      return this.javac.getDebug();
   }

   public String getDebugLevel() {
      return this.javac.getDebugLevel();
   }

   protected void checkParameters() throws BuildException {
      this.javac.checkParameters();
   }

   public Path getSourcepath() {
      return this.javac.getSourcepath();
   }

   public File getDestdir() {
      return this.javac.getDestdir();
   }

   public boolean getFailonerror() {
      return this.javac.getFailonerror();
   }

   public Path getClasspath() {
      return this.javac.getClasspath();
   }

   public boolean getIncludeantruntime() {
      return this.javac.getIncludeantruntime();
   }

   public boolean getIncludejavaruntime() {
      return this.javac.getIncludejavaruntime();
   }

   public String getMemoryMaximumSize() {
      return this.javac.getMemoryMaximumSize();
   }

   public String getMemoryInitialSize() {
      return this.javac.getMemoryInitialSize();
   }

   protected void compile(File[] var1) {
      this.compile(var1, (Path)null);
   }

   protected void compile(File[] var1, Path var2) {
      ExposingJavac var3 = this.javac;
      if (var2 != null) {
         var3 = this.cloneJavac();
         var3.setClasspath(var2);
      }

      var3.setCompileList(var1);
      var3.compile();
      var3.resetFileLists();
   }

   private ExposingJavac cloneJavac() {
      ExposingJavac var1 = new ExposingJavac();
      var1.setDebugLevel(this.javac.getDebugLevel());
      var1.setSource(this.javac.getSource());
      var1.setDestdir(this.javac.getDestdir());
      var1.setSourcepath(this.javac.getSourcepath());
      var1.setClasspath(this.javac.getClasspath());
      var1.setBootclasspath(this.javac.getBootclasspath());
      var1.setExtdirs(this.javac.getExtdirs());
      var1.setListfiles(this.javac.getListfiles());
      var1.setFailonerror(this.javac.getFailonerror());
      var1.setDeprecation(this.javac.getDeprecation());
      var1.setMemoryMaximumSize(this.javac.getMemoryMaximumSize());
      var1.setMemoryInitialSize(this.javac.getMemoryInitialSize());
      var1.setEncoding(this.javac.getEncoding());
      var1.setDebug(this.javac.getDebug());
      var1.setOptimize(this.javac.getOptimize());
      var1.setDepend(this.javac.getDepend());
      var1.setVerbose(this.javac.getVerbose());
      var1.setIncludeantruntime(this.javac.getIncludeantruntime());
      var1.setIncludejavaruntime(this.javac.getIncludejavaruntime());
      var1.setFork(this.javac.isForkedJavac());
      var1.setTempdir(this.javac.getTempdir());
      var1.setProject(this.javac.getProject());
      var1.setTaskName(this.javac.getTaskName());
      var1.setOwningTarget(this.javac.getOwningTarget());
      var1.setExecutable(this.javac.getExecutable());
      var1.setNowarn(this.javac.getNowarn());
      var1.setCompiler(this.javac.getCompiler());
      Javac.ImplementationSpecificArgument var2 = var1.createCompilerArg();
      var2.setLine(this.translate2Commandline(this.javac.getCurrentCompilerArgs()));
      return var1;
   }

   private String translate2Commandline(String[] var1) {
      StringBuffer var2 = new StringBuffer();
      String[] var4 = var1;
      int var5 = var1.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];
         if (var7.startsWith("-")) {
            int var3 = var7.indexOf("=");
            if (var3 > 0) {
               var2.append(var7.substring(0, var3));
               var2.append("=\"");
               var2.append(var7.substring(var3 + 1));
               var2.append("\" ");
            } else {
               var2.append(var7);
               var2.append(" ");
            }
         } else {
            var2.append("\"");
            var2.append(var7);
            var2.append("\" ");
         }
      }

      return var2.toString();
   }

   protected static class ExposingJavac extends Javac {
      private ExposingJavac() {
         this.setSrcdir(new Path(this.getProject(), "."));
      }

      protected void compile() {
         super.compile();
      }

      protected void checkParameters() throws BuildException {
         super.checkParameters();
      }

      protected void setCompileList(File[] var1) {
         this.compileList = var1;
      }

      protected void resetFileLists() {
         super.resetFileLists();
      }

      // $FF: synthetic method
      ExposingJavac(Object var1) {
         this();
      }
   }
}
