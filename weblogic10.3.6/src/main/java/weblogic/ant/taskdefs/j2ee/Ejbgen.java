package weblogic.ant.taskdefs.j2ee;

import java.io.File;
import java.util.StringTokenizer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.taskdefs.Javadoc;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import weblogic.tools.ejbgen.EJBGen;

public class Ejbgen extends Task {
   String outputJar;
   String tmpdir;
   String srcPath;
   String classpath;
   String ejbsource;
   String utils;
   String[] ejbcflags;
   boolean keepgenerated;
   String ejbPrefix;
   String ejbSuffix;
   String localHomePrefix;
   String localHomeSuffix;
   String remoteHomePrefix;
   String remoteHomeSuffix;
   String remotePrefix;
   String remoteSuffix;
   String valueObjectPrefix;
   String valueObjectSuffix;
   String propertyFile;
   String valueBaseClass;
   String jndiPrefix;
   String jndiSuffix;
   String localPrefix;
   String localSuffix;
   boolean valueClasses = true;
   boolean remoteInterfaces = true;
   boolean localInterfaces = true;

   public void addTask(Task var1) {
      p("addTask(" + var1.getClass().getName() + ")");
   }

   private static void p(String var0) {
      System.err.println("[ejbgen]: " + var0);
   }

   public void setEjbs(String var1) {
      this.ejbsource = var1;
   }

   public void setUtils(String var1) {
      this.utils = var1;
   }

   public void setSrcpath(String var1) {
      this.srcPath = var1;
   }

   public void setOutputJar(String var1) {
      this.outputJar = var1;
   }

   public void setTmpdir(String var1) {
      this.tmpdir = var1;
   }

   public void setClasspath(String var1) {
      this.classpath = var1;
   }

   public void setKeepgenerated(boolean var1) {
      this.keepgenerated = var1;
   }

   public void setEjbcFlags(String var1) {
      this.ejbcflags = split(var1);
   }

   public void setEjbprefix(String var1) {
      this.ejbPrefix = var1;
   }

   public void setEjbsuffix(String var1) {
      this.ejbSuffix = var1;
   }

   public void setRemotePrefix(String var1) {
      this.remotePrefix = var1;
   }

   public void setRemoteSuffix(String var1) {
      this.remoteSuffix = var1;
   }

   public void setLocalPrefix(String var1) {
      this.localPrefix = var1;
   }

   public void setLocalSuffix(String var1) {
      this.localSuffix = var1;
   }

   public void setRemoteHomePrefix(String var1) {
      this.remoteHomePrefix = var1;
   }

   public void setRemoteHomeSuffix(String var1) {
      this.remoteHomeSuffix = var1;
   }

   public void setLocalHomePrefix(String var1) {
      this.localHomePrefix = var1;
   }

   public void setLocalHomeSuffix(String var1) {
      this.localHomeSuffix = var1;
   }

   public void setJndiPrefix(String var1) {
      this.jndiPrefix = var1;
   }

   public void setJndiSuffix(String var1) {
      this.jndiSuffix = var1;
   }

   public void setValueObjectPrefix(String var1) {
      this.valueObjectPrefix = var1;
   }

   public void setValueObjectSuffix(String var1) {
      this.valueObjectSuffix = var1;
   }

   public void setPropertyFile(String var1) {
      this.propertyFile = var1;
   }

   public void setValuebaseclass(String var1) {
      this.valueBaseClass = var1;
   }

   public void setValueClasses(boolean var1) {
      this.valueClasses = var1;
   }

   public void setRemoteInterfaces(boolean var1) {
      this.remoteInterfaces = var1;
   }

   public void setLocalInterfaces(boolean var1) {
      this.localInterfaces = var1;
   }

   public void execute() throws BuildException {
      this.runJavaDoc();
      this.moveXML();
      this.runJavac();
      this.runEjbc();
      if (!this.keepgenerated) {
         this.deleteSrc();
      }

      this.runZip();
   }

   private void runEjbc() throws BuildException {
      Ejbc var1 = new Ejbc();
      var1.setTaskName("ejbc");
      var1.setProject(this.getProject());
      var1.setSource(this.tmpdir);
      var1.setTarget(this.tmpdir);
      var1.setKeepGenerated(this.keepgenerated);
      var1.execute();
   }

   private void moveXML() throws BuildException {
      Project var1 = this.getProject();
      File var2 = var1.resolveFile(this.tmpdir);
      File var3 = new File(var2, "META-INF");
      if (!var3.isDirectory() && !var3.mkdirs()) {
         throw new BuildException("Cannot make directory: " + var3.getAbsolutePath());
      } else {
         File[] var4 = var3.listFiles();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].getName().endsWith(".xml") && var4[var5].isFile() && !var4[var5].delete()) {
               throw new BuildException("Cannot delete file: " + var4[var5].getAbsolutePath());
            }
         }

         String[] var10 = var2.list();

         for(int var6 = 0; var6 < var10.length; ++var6) {
            String var7 = var10[var6];
            if (var7.endsWith(".xml")) {
               File var8 = new File(var2, var7);
               File var9 = new File(var3, var7);
               if (!var8.renameTo(var9)) {
                  throw new BuildException("Cannot copy to: " + var9.getAbsolutePath());
               }
            }
         }

      }
   }

   private void deleteSrc() throws BuildException {
      this.log("deleting generated source...");
      FileSet var1 = new FileSet();
      Project var2 = this.getProject();
      var1.setProject(var2);
      File var3 = var2.resolveFile(this.tmpdir);
      var1.setDir(var3);
      var1.setIncludes("**/*.java");
      DirectoryScanner var4 = var1.getDirectoryScanner(var2);
      String[] var5 = var4.getIncludedFiles();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         File var7 = new File(var3, var5[var6]);
         if (!var7.delete()) {
            throw new BuildException("Cannot delete: " + var7.getAbsolutePath());
         }
      }

   }

   private void runZip() throws BuildException {
      this.log("runzip");
      Zip var1 = new Zip();
      Project var2 = this.getProject();
      var1.setProject(var2);
      var1.setTaskName("zip");
      File var3 = var2.resolveFile(this.tmpdir);
      var1.setBasedir(var3);
      var1.setZipfile(var2.resolveFile(this.outputJar));
      var1.execute();
   }

   private void runJavaDoc() throws BuildException {
      FileSet var1 = new FileSet();
      var1.setProject(this.getProject());
      File var2 = null;
      var2 = this.getProject().getBaseDir();
      var1.setDir(var2);
      var1.setIncludes(this.ejbsource);
      DirectoryScanner var3 = var1.getDirectoryScanner(this.getProject());
      String[] var4 = var3.getIncludedFiles();
      Project var5 = this.getProject();
      Javadoc var6 = new Javadoc();
      var6.setProject(var5);
      var6.setTaskName("ejbgen");
      Javadoc.DocletInfo var7 = var6.createDoclet();
      var6.setDoclet(EJBGen.class.getName());
      this.addJavadocOptions(var7);
      Javadoc.DocletParam var8 = var7.createParam();
      var8.setName("-d");
      var8.setValue(var5.resolveFile(this.tmpdir).getAbsolutePath());
      this.log("outputting to " + this.tmpdir);

      for(int var9 = 0; var9 < var4.length; ++var9) {
         Javadoc.SourceFile var10 = new Javadoc.SourceFile();
         var10.setFile(new File(var2, var4[var9]));
         var6.addSource(var10);
      }

      Path var11 = var6.createSourcepath();
      var11.setPath(this.srcPath);
      var6.execute();
   }

   private void addJavadocOptions(Javadoc.DocletInfo var1) {
      if (this.ejbPrefix != null) {
         this.addString(var1, "-ejbPrefix", this.ejbPrefix);
      }

      if (this.ejbSuffix != null) {
         this.addString(var1, "-ejbSuffix", this.ejbSuffix);
      }

      if (this.localHomePrefix != null) {
         this.addString(var1, "-localHomePrefix", this.localHomePrefix);
      }

      if (this.localHomeSuffix != null) {
         this.addString(var1, "-localHomeSuffix", this.localHomeSuffix);
      }

      if (this.remoteHomePrefix != null) {
         this.addString(var1, "-remoteHomePrefix", this.remoteHomePrefix);
      }

      if (this.remoteHomeSuffix != null) {
         this.addString(var1, "-remoteHomeSuffix", this.remoteHomeSuffix);
      }

      if (this.remotePrefix != null) {
         this.addString(var1, "-remotePrefix", this.remotePrefix);
      }

      if (this.remoteSuffix != null) {
         this.addString(var1, "-remoteSuffix", this.remoteSuffix);
      }

      if (this.propertyFile != null) {
         this.addString(var1, "-propertyFile", this.propertyFile);
      }

      if (this.valueBaseClass != null) {
         this.addString(var1, "-valueBaseClass", this.valueBaseClass);
      }

      if (this.jndiPrefix != null) {
         this.addString(var1, "-jndiPrefix", this.jndiPrefix);
      }

      if (this.jndiSuffix != null) {
         this.addString(var1, "-jndiSuffix", this.jndiSuffix);
      }

      if (this.localPrefix != null) {
         this.addString(var1, "-localPrefix", this.localPrefix);
      }

      if (this.localSuffix != null) {
         this.addString(var1, "-localSuffix", this.localSuffix);
      }

      if (this.valueObjectPrefix != null) {
         this.addString(var1, "-valueObjectPrefix", this.valueObjectPrefix);
      }

      if (this.valueObjectSuffix != null) {
         this.addString(var1, "-valueObjectSuffix", this.valueObjectSuffix);
      }

      if (!this.valueClasses) {
         this.addFlag(var1, "-noValueClasses");
      }

      if (!this.remoteInterfaces) {
         this.addFlag(var1, "-noRemoteInterfaces");
      }

      if (!this.localInterfaces) {
         this.addFlag(var1, "-noLocalInterfaces");
      }

   }

   private void addString(Javadoc.DocletInfo var1, String var2, String var3) {
      Javadoc.DocletParam var4 = var1.createParam();
      var4.setName(var2);
      var4.setValue(var3);
   }

   private void addFlag(Javadoc.DocletInfo var1, String var2) {
      Javadoc.DocletParam var3 = var1.createParam();
      var3.setName(var2);
   }

   private void verifyRequired() throws BuildException {
      if (this.srcPath == null) {
         throw new BuildException("srcPath not specified");
      } else {
         File var1 = this.getProject().resolveFile(this.tmpdir);
         if (!var1.isDirectory() && !var1.mkdirs()) {
            throw new BuildException("cannot make tmpdir: " + var1.getAbsolutePath());
         } else if (this.ejbsource == null) {
            throw new BuildException("ejbs not specified");
         } else if (this.outputJar == null) {
            throw new BuildException("outputjar not specified");
         } else if (this.tmpdir == null) {
            throw new BuildException("tmpdir not specified");
         }
      }
   }

   private void runJavac() {
      Project var1 = this.getProject();
      Javac var2 = null;
      var2 = new Javac();
      var2.setTaskName("javac");
      var2.setProject(var1);
      File var3 = var1.resolveFile(this.tmpdir);
      var2.setDestdir(var3);
      Path var4 = new Path(this.getProject());
      var4.setLocation(var1.getBaseDir());
      var2.setSrcdir(var4);
      StringBuffer var5 = new StringBuffer(this.tmpdir + "/**/*.java");
      var5.append(",");
      var5.append(this.ejbsource);
      if (this.utils != null) {
         var5.append(",");
         var5.append(this.utils);
      }

      String var6 = var5.toString();
      var2.setIncludes(var6);
      p("tmpdir=" + this.tmpdir);
      var2.setFailonerror(true);
      var2.execute();
   }

   private static String[] split(String var0) {
      StringTokenizer var1 = new StringTokenizer(var0, ",", false);
      String[] var2 = new String[var1.countTokens()];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = var1.nextToken();
      }

      return var2;
   }
}
