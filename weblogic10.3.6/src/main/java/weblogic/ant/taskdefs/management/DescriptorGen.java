package weblogic.ant.taskdefs.management;

import com.bea.staxb.buildtime.Java2SchemaTask;
import com.bea.staxb.buildtime.internal.mbean.MBeanJava2SchemaTask;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import weblogic.descriptor.beangen.beangen;
import weblogic.utils.FileUtils;

public final class DescriptorGen extends Task {
   private static final String BEA_JAVA2SCHEMA_NAME = "weblogic.ant.taskdefs.management.DescriptorGen.java2schema";
   private static final String BEAN_SUFFIX = "Impl";
   private static final String BEAN_BASE_CLASS = "weblogic.descriptor.internal.AbstractDescriptorBean";
   private static final String BEAN_PATTERN = "**/*Bean.java";
   private static final String IMPL_PATTERN = "**/*BeanImpl.java";
   private File srcdir;
   private File destdir;
   private File bindingJarFile;
   private String targetNamespace;
   private Path classpath;

   public DescriptorGen() {
      this.classpath = Path.systemClasspath;
   }

   public void setClasspath(Path var1) {
      this.classpath.append(var1);
   }

   public Path getClasspath() {
      return this.classpath;
   }

   public Path createClasspath() {
      return this.classpath.createPath();
   }

   public void setSrcdir(File var1) {
      this.srcdir = var1;
   }

   public File getSrcdir() {
      return this.srcdir;
   }

   public void setDestdir(File var1) {
      this.destdir = var1;
   }

   public File getDestdir() {
      return this.destdir;
   }

   public void setBindingJarFile(File var1) {
      this.bindingJarFile = var1;
   }

   public File getBindingJarFile() {
      return this.bindingJarFile;
   }

   public void setTargetNamespace(String var1) {
      this.targetNamespace = var1;
   }

   public String getTargetNamespace() {
      return this.targetNamespace;
   }

   private void validateOpts() throws BuildException {
      if (this.srcdir == null) {
         throw new BuildException("srcdir was not set");
      } else if (!this.srcdir.exists()) {
         throw new BuildException("srcdir " + this.srcdir.getPath() + " does not exist or could not be read.");
      } else if (!this.srcdir.isDirectory()) {
         throw new BuildException("srcdir " + this.srcdir.getPath() + " is not a directory.");
      } else if (this.destdir == null) {
         throw new BuildException("destdir not set");
      } else {
         if (this.destdir.exists()) {
            if (!this.destdir.isDirectory()) {
               throw new BuildException("destdir " + this.destdir.getPath() + " is not a directory.");
            }

            this.destdir.mkdirs();
         }

         if (this.bindingJarFile == null) {
            throw new BuildException("bindingJarFile not set");
         }
      }
   }

   public void execute() throws BuildException {
      this.validateOpts();
      this.classpath = new Path(this.project);
      this.classpath.add(Path.systemClasspath);
      this.classpath.add(new Path(this.project, this.destdir.getAbsolutePath()));
      this.beangen();
      this.compileGeneratedCode();

      try {
         this.java2schema();
      } catch (IOException var2) {
         throw new BuildException(var2);
      }
   }

   private String find(File var1, String var2) {
      FileSet var3 = new FileSet();
      var3.setDir(var1);
      var3.createInclude().setName(var2);
      DirectoryScanner var4 = var3.getDirectoryScanner(this.project);
      String[] var5 = var4.getIncludedFiles();
      StringBuffer var6 = new StringBuffer();

      for(int var7 = 0; var7 < var5.length; ++var7) {
         var6.append(" " + var5[var7]);
      }

      return var6.toString();
   }

   private Commandline.Argument buildArgList(Java var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append("-d ").append(this.destdir.getAbsolutePath());
      var2.append(" -sourcedir ").append(this.srcdir.getAbsolutePath());
      var2.append(" -nolocalvalidation");
      var2.append(" -suffix ").append("Impl");
      var2.append(" -baseclass ").append("weblogic.descriptor.internal.AbstractDescriptorBean");
      if (this.getTargetNamespace() != null) {
         var2.append(" -targetNamespace ").append(this.getTargetNamespace());
      }

      var2.append(this.find(this.srcdir, "**/*Bean.java"));
      String var3 = var2.toString();
      System.out.println("\n Using beangen command-line of " + var3);
      Commandline.Argument var4 = var1.createArg();
      var4.setLine(var3);
      return var4;
   }

   private void beangen() throws BuildException {
      Java var1 = (Java)this.project.createTask("java");
      var1.setClassname(beangen.class.getName());
      var1.setFork(true);
      var1.setFailonerror(true);
      this.buildArgList(var1);
      var1.setClasspath(this.classpath);
      var1.execute();
   }

   private void compileGeneratedCode() throws BuildException {
      Javac var1 = (Javac)this.project.createTask("javac");
      var1.setSrcdir(new Path(this.project, this.destdir.getAbsolutePath()));
      var1.setDestdir(this.destdir);
      var1.setClasspath(this.classpath);
      var1.execute();
   }

   private Java2SchemaTask findOrCreateSchemaTask() {
      Hashtable var1 = this.project.getTaskDefinitions();
      if (!var1.contains("weblogic.ant.taskdefs.management.DescriptorGen.java2schema")) {
         this.project.addTaskDefinition("weblogic.ant.taskdefs.management.DescriptorGen.java2schema", MBeanJava2SchemaTask.class);
      }

      Java2SchemaTask var2 = (Java2SchemaTask)this.project.createTask("weblogic.ant.taskdefs.management.DescriptorGen.java2schema");
      var2.setTaskName("java2Schema");
      return var2;
   }

   private void java2schema() throws BuildException, IOException {
      File var1 = FileUtils.createTempDir("dgen");
      var1.mkdirs();
      System.out.println("java2schema using tempdir " + var1);
      Java2SchemaTask var2 = this.findOrCreateSchemaTask();
      var2.setSrcdir(new Path(this.project, this.destdir.getAbsolutePath()));
      var2.setIncludes("**/*BeanImpl.java");
      var2.setDestDir(var1);
      var2.setClasspath(this.classpath);
      var2.execute();
      Jar var3 = (Jar)this.project.createTask("jar");
      var3.setBasedir(var1);
      var3.setDestFile(this.bindingJarFile);
      var3.execute();
      FileUtils.remove(var1);
   }
}
