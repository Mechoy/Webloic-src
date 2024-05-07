package weblogic.wsee.tools.anttasks;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.TempDirManager;
import weblogic.wsee.tools.xcatalog.CatalogInfo;
import weblogic.wsee.tools.xcatalog.ClientGenXMLs;
import weblogic.wsee.util.JAXWSClassLoaderFactory;

public class ClientGenTask extends ClientGenFacadeTask {
   private boolean failOnError = true;
   private File destFile;
   private File srcDir;
   private List<FileSet> fileSets = new ArrayList();
   private boolean overwrite = false;
   private final DelegatingJavacTask javac = new DelegatingJavacTask();

   public void setSrcdir(File var1) {
      this.srcDir = var1;
   }

   public void addFileSet(FileSet var1) {
      assert var1 != null;

      this.fileSets.add(var1);
   }

   public void setDestfile(File var1) {
      this.destFile = var1;
   }

   public void setDestDir(File var1) {
      super.setDestDir(var1);
      this.javac.setDestdir(var1);
   }

   private void compileAndMakeJar() throws Exception {
      if (this.destFile == null) {
         AntUtil.copyFiles(this.javac.getProject(), this.fileSets, this.javac.getDestdir());
      } else {
         this.compileFiles();
         this.makeJar();
      }

   }

   private void compileFiles() throws IOException {
      FileSet var1 = new FileSet();
      var1.setProject(this.javac.getProject());
      var1.setDir(this.javac.getDestdir());
      var1.setIncludes("**/*.java");
      DirectoryScanner var2 = var1.getDirectoryScanner(this.javac.getProject());
      String[] var3 = var2.getIncludedFiles();
      ArrayList var4 = new ArrayList();
      String[] var5 = var3;
      int var6 = var3.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String var8 = var5[var7];
         var4.add((new File(this.javac.getDestdir(), var8)).getCanonicalFile());
      }

      this.javac.compile((File[])var4.toArray(new File[var4.size()]));
   }

   private void makeJar() {
      Jar var1 = new Jar();
      var1.setProject(this.javac.getProject());
      var1.setDestFile(this.destFile);
      var1.setBasedir(this.javac.getDestdir());
      FileSet var2 = new FileSet();
      var2.setDir(this.javac.getDestdir());
      var1.addFileset(var2);
      Iterator var3 = this.fileSets.iterator();

      while(var3.hasNext()) {
         FileSet var4 = (FileSet)var3.next();
         var1.addFileset(var4);
      }

      var1.setExcludes("**/*");
      if (this.overwrite) {
         var1.setUpdate(true);
      }

      var1.execute();
      this.deleteTempFiles();
   }

   private void deleteTempFiles() {
      AntUtil.deleteDir(this.javac.getProject(), this.javac.getDestdir());
   }

   private void intializeCompilerClasspath() {
      if (this.javac.getClasspath() == null) {
         this.javac.createClasspath();
      }

   }

   private void validate() throws BuildException {
      if (this.javac.getDestdir() != null && this.destFile != null) {
         throw new BuildException("Both DestDir and DestFile options are specified. Please specify only one");
      } else if (this.destFile != null && this.destFile.isDirectory()) {
         throw new BuildException("Destination File cannot be a directory");
      }
   }

   private AntClassLoader createClassLoader() {
      ClassLoader var1 = ClientGenTask.class.getClassLoader();
      return new AntClassLoader(var1, this.javac.getProject(), this.javac.getClasspath(), true);
   }

   public void setDebugLevel(String var1) {
      this.javac.setDebugLevel(var1);
   }

   public void setProject(Project var1) {
      super.setProject(var1);
      this.javac.setProject(var1);
   }

   public void setSource(String var1) {
      this.javac.setSource(var1);
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
      this.failOnError = var1;
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

   public Path getSourcepath() {
      if (this.javac.getSourcepath() == null) {
         Path var1 = this.createSourcepath();
         var1.createPathElement().setLocation(this.srcDir);
      }

      return this.javac.getSourcepath();
   }

   public void setOverwrite(boolean var1) {
      this.overwrite = var1;
   }

   public void execute() {
      this.validate();
      this.intializeCompilerClasspath();
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();
      AntClassLoader var2 = this.createClassLoader();
      if (WebServiceType.JAXWS.equals(this.type)) {
         JAXWSClassLoaderFactory.getInstance().setContextLoader(var2);
      } else {
         Thread.currentThread().setContextClassLoader(var2);
      }

      TempDirManager var3 = null;

      try {
         var3 = this.initialize();
         this.initJAXWSOptions();
         this.jaxwsOptions.setDebug(this.javac.getDebug());
         this.jaxwsOptions.setDebugLevel(this.javac.getDebugLevel());
         this.jaxwsOptions.setVerbose(this.javac.getVerbose());
         this.jaxwsOptions.setOptimize(this.javac.getOptimize());
         this.jaxwsOptions.setIncludeantruntime(this.javac.getIncludeantruntime());
         this.jaxwsOptions.setIncludejavaruntime(this.javac.getIncludejavaruntime());
         this.jaxwsOptions.setFork(this.javac.getFork());
         CatalogInfo var4 = ClientGenXMLs.processClient(this, this.type, this.jaxwsOptions, this.wsdl, (CatalogInfo)null);
         super.execute();
         File var11 = this.destDir;
         if (this.destFile != null) {
            var11 = this.javac.getDestdir();
         }

         ClientGenXMLs.doAllCatalogFiles(this, new File(var11, "META-INF"), var4);
         this.compileAndMakeJar();
      } catch (Exception var9) {
         if (this.javac.getVerbose()) {
            StringWriter var5 = new StringWriter();
            var9.printStackTrace(new PrintWriter(var5));
            this.log(var5.toString(), 0);
         }

         if (this.failOnError) {
            throw new BuildException(var9);
         }

         this.log("ClientGen Build Failed : " + var9.getMessage(), 0);
      } finally {
         if (var2 != null) {
            var2.cleanup();
         }

         Thread.currentThread().setContextClassLoader(var1);
         if (var3 != null) {
            var3.cleanup();
         }

      }

   }

   private TempDirManager initialize() throws IOException {
      TempDirManager var1 = new TempDirManager(this.getProject());
      if (this.destFile != null) {
         File var2 = var1.createTempDir("clientgen", this.javac.getTempdir());
         this.setDestDir(var2);
      }

      return var1;
   }
}
