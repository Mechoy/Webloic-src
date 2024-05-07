package weblogic.wsee.tools.anttasks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import weblogic.wsee.tools.jws.context.JwsBuildContextImpl;
import weblogic.wsee.tools.logging.AntLogger;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.util.Verbose;

public class JwscTask extends MatchingJavacTask {
   private static final boolean verbose = Verbose.isVerbose(JwscTask.class);
   private List<JwsModule> modules = new ArrayList();
   private JwsBuildContextImpl ctx = new JwsBuildContextImpl();
   private File includeResourcesDir = null;
   private boolean dotNetStyle = true;
   private File srcDir = null;
   private boolean keepTempFiles = false;
   private File applicationXml = null;
   private boolean keepGenerated = false;
   private List<FileSet> bindingFileSets = new ArrayList();
   private File[] bindingFiles;
   private boolean jaxRpcByteArrayStyle;
   private boolean upperCasePropName = true;
   private boolean localElementDefaultRequired = true;
   private boolean localElementDefaultNillable = true;

   public void setKeepTempFiles(boolean var1) {
      this.keepTempFiles = var1;
   }

   boolean isKeepTempFiles() {
      return this.keepTempFiles;
   }

   public void setSrcdir(File var1) {
      this.srcDir = var1;
   }

   File getSrcdir() {
      return this.srcDir;
   }

   /** @deprecated */
   @Deprecated
   public void setEnableAsyncService(boolean var1) {
      this.log("DEPRECATED - Use of enableAsyncService is deprecated.  The AsyncResponseService is now available by default in every domain.");
   }

   public void setKeepGenerated(boolean var1) {
      this.keepGenerated = var1;
   }

   public boolean isKeepGenerated() {
      return this.keepGenerated;
   }

   public void setApplicationXml(File var1) {
      this.applicationXml = var1;
   }

   public void setDotNetStyle(boolean var1) {
      this.dotNetStyle = var1;
   }

   /** @deprecated */
   @Deprecated
   public void setIncludeResourcesDir(File var1) {
      this.log("DEPRECATED - Use of includeResourcesDir is deprecated.  Use nested fileset element under the modules instead.");
      this.includeResourcesDir = var1;
   }

   public MultipleJwsModule createModule() {
      MultipleJwsModule var1 = new MultipleJwsModule(this, this.ctx);
      this.modules.add(var1);
      return var1;
   }

   public SingleJwsModule createJws() {
      SingleJwsModule var1 = new SingleJwsModule(this, this.ctx);
      this.modules.add(var1);
      return var1;
   }

   public void setSrcEncoding(String var1) {
      this.ctx.setSrcEncoding(var1);
      super.setEncoding(var1);
   }

   public void setDestEncoding(String var1) {
      this.ctx.setDestEncoding(var1);
   }

   public Path getSourcepath() {
      if (super.getSourcepath() == null) {
         Path var1 = this.createSourcepath();
         if (this.srcDir != null) {
            var1.createPathElement().setLocation(this.srcDir);
         }
      }

      return super.getSourcepath();
   }

   public void execute() throws BuildException {
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();
      Thread.currentThread().setContextClassLoader(JwscTask.class.getClassLoader());

      try {
         this.checkParameters();
         this.initialize();
         EarFile var2 = new EarFile(this.getDestdir(), this.ctx.getDestEncoding());
         var2.setApplicationXml(this.applicationXml);
         Iterator var3 = this.modules.iterator();

         while(var3.hasNext()) {
            JwsModule var4 = (JwsModule)var3.next();
            var4.build(var2);
         }

         var2.write();
      } catch (Exception var9) {
         if (this.getFailonerror()) {
            throw new BuildException(var9);
         }

         this.log("JWS Compile failed: " + var9.getMessage());
      } finally {
         Thread.currentThread().setContextClassLoader(var1);
         this.cleanup();
      }

   }

   protected void checkParameters() {
      File var1 = this.getDestdir();
      if (var1 == null) {
         throw new BuildException("attribute destDir must be set!");
      } else {
         if (!var1.exists()) {
            var1.mkdirs();
         }

         super.checkParameters();
      }
   }

   private void initialize() {
      this.ctx.getProperties().put("jwsc.dotNetStyle", this.dotNetStyle);
      AntLogger var1 = new AntLogger(this);
      this.ctx.addLogger(var1);
      this.ctx.getLogger().log(EventLevel.VERBOSE, "Verbose is on.");
      if (this.getClasspath() != null) {
         this.ctx.setClasspath(this.getClasspath().list());
      }

      this.ctx.setSourcepath(this.getSourcepath().list());
      if (this.includeResourcesDir != null) {
         this.createClasspath().setPath(this.includeResourcesDir.getAbsolutePath());
      }

      this.getSourcepath();
   }

   private void cleanup() {
      this.modules.clear();
      this.modules = null;
      this.ctx = null;
      this.includeResourcesDir = null;
      this.dotNetStyle = true;
      this.keepGenerated = false;
      this.bindingFileSets = null;
      this.bindingFiles = null;
   }

   Path getCompleteClasspath() {
      Path var1 = new Path(this.getProject());
      Path var2 = this.getClasspath();
      if (var2 == null) {
         var2 = new Path(this.getProject());
      }

      if (this.getIncludeantruntime()) {
         var1.addExisting(var2.concatSystemClasspath("last"));
      } else {
         var1.addExisting(var2.concatSystemClasspath("ignore"));
      }

      if (this.getIncludejavaruntime()) {
         var1.addJavaRuntime();
      }

      return var1;
   }

   void javac(File var1, File[] var2, Path var3) throws IOException {
      File var4 = this.getDestdir();
      this.setDestdir(var1);
      this.compile(var2, var3);
      this.setDestdir(var4);
   }

   FileSet getResourceFileSet() {
      if (this.includeResourcesDir != null) {
         ExposingFileSet var1 = new ExposingFileSet(this.getImplicitFileSet());
         var1.setDir(this.includeResourcesDir);
         return var1;
      } else {
         return null;
      }
   }

   /** @deprecated */
   @Deprecated
   public void addXsdConfig(FileSet var1) {
      this.log("xsdConfig is deprecated.  Use binding instead.", 1);
      this.addBinding(var1);
   }

   public void addBinding(FileSet var1) {
      this.bindingFileSets.add(var1);
   }

   File[] getBindingFiles() {
      if (this.bindingFileSets.isEmpty()) {
         return null;
      } else {
         if (this.bindingFiles == null) {
            this.bindingFiles = AntUtil.getFiles((Collection)this.bindingFileSets, this.getProject());
         }

         if (this.bindingFiles == null || this.bindingFiles.length == 0) {
            this.log("Warning: Not found binding files defined in binding sub-element, some binding configuration might not take effect!");
         }

         return this.bindingFiles;
      }
   }

   List<FileSet> getBindingFileSets() {
      return this.bindingFileSets;
   }

   public void setJaxRpcByteArrayStyle(boolean var1) {
      this.jaxRpcByteArrayStyle = var1;
   }

   public boolean isJaxRpcByteArrayStyle() {
      return this.jaxRpcByteArrayStyle;
   }

   public void setUpperCasePropName(boolean var1) {
      this.upperCasePropName = var1;
   }

   public boolean isUpperCasePropName() {
      return this.upperCasePropName;
   }

   public void setLocalElementDefaultRequired(boolean var1) {
      this.localElementDefaultRequired = var1;
   }

   public boolean isLocalElementDefaultRequired() {
      return this.localElementDefaultRequired;
   }

   public void setLocalElementDefaultNillable(boolean var1) {
      this.localElementDefaultNillable = var1;
   }

   public boolean isLocalElementDefaultNillable() {
      return this.localElementDefaultNillable;
   }

   private static class ExposingFileSet extends FileSet {
      ExposingFileSet(FileSet var1) {
         super(var1);
      }
   }
}
