package weblogic.wsee.tools.anttasks;

import com.bea.util.jam.JClass;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.ZipFileSet;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.TempDirManager;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.clientgen.callback.CallbackGen81Processor;
import weblogic.wsee.tools.clientgen.callback.CallbackGenProcessor;
import weblogic.wsee.tools.clientgen.jaxws.Options;
import weblogic.wsee.tools.jws.build.Jws;
import weblogic.wsee.tools.jws.build.JwsCompiler;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;
import weblogic.wsee.tools.jws.decl.WebServiceDeclFactory;
import weblogic.wsee.tools.jws.decl.port.PortDecl;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.xcatalog.CatalogInfo;
import weblogic.wsee.tools.xcatalog.ClientGenXMLs;
import weblogic.wsee.util.StringUtil;

public abstract class JwsModule {
   private final JwscTask owningTask;
   private JwsBuildContext ctx;
   private JwsCompiler compiler;
   private String name = null;
   private String defaultContextPath = null;
   private boolean explode = false;
   private List<FileSet> fileSets = new ArrayList();
   List<Jws> callbackJwsFiles;
   private List<ClientGenFacadeTask> clients = new ArrayList();
   private List<Descriptor> descriptors = new ArrayList();
   private final TempDirManager tempDirs;
   private ZipFileSet catalogFS = null;

   JwsModule(JwscTask var1, JwsBuildContext var2) {
      assert var1 != null;

      assert var2 != null;

      this.owningTask = var1;
      this.ctx = var2;
      this.compiler = new JwsCompiler(var2, var1);
      this.tempDirs = new TempDirManager(var1.getProject());
   }

   public void addFileSet(FileSet var1) {
      assert var1 != null;

      var1.setProject(this.owningTask.getProject());
      this.fileSets.add(var1);
   }

   public void addZipFileSet(ZipFileSet var1) {
      assert var1 != null;

      var1.setProject(this.owningTask.getProject());
      this.fileSets.add(var1);
   }

   List<FileSet> getFileSets() {
      return this.fileSets;
   }

   public void setName(String var1) {
      if (StringUtil.isEmpty(var1)) {
         throw new IllegalArgumentException("name must not be null or empty.");
      } else {
         this.name = var1;
      }
   }

   String getName() {
      return this.name;
   }

   String getFileName() {
      StringBuilder var1 = new StringBuilder(this.getName());
      if (!this.isExplode()) {
         if (this.isEjb()) {
            var1.append(".jar");
         } else {
            var1.append(".war");
         }
      }

      return var1.toString();
   }

   public void setContextPath(String var1) {
      this.defaultContextPath = var1;
      this.ctx.getProperties().put("jwsc.contextPathOverride", true);
   }

   public void setExplode(boolean var1) {
      this.explode = var1;
   }

   boolean isExplode() {
      return this.explode || this.isWsdlOnly();
   }

   public void setWsdlOnly(boolean var1) {
      this.compiler.setWsdlOnly(var1);
   }

   boolean isWsdlOnly() {
      return this.compiler.isWsdlOnly();
   }

   public void setGenerateWsdl(boolean var1) {
      this.compiler.setGenerateWsdl(var1);
   }

   boolean isGenerateWsdl() {
      return this.compiler.isGenerateWsdl();
   }

   public void setGenerateDescriptors(boolean var1) {
      this.compiler.setGenerateDescriptors(var1);
   }

   boolean isGenerateDescriptors() {
      return this.compiler.isGenerateDescriptors();
   }

   public ClientGenFacadeTask createClientGen() {
      ClientGenFacadeTask var1 = new ClientGenFacadeTask();
      var1.setProject(this.owningTask.getProject());
      this.clients.add(var1);
      return var1;
   }

   public Descriptor createDescriptor() {
      Descriptor var1 = new Descriptor();
      this.descriptors.add(var1);
      return var1;
   }

   abstract Collection<Jws> getJwsFiles();

   File getOutputDir() {
      return this.compiler.getOutputDir();
   }

   JwscTask getTask() {
      return this.owningTask;
   }

   boolean isEjb() {
      return this.compiler.isEjb();
   }

   List<WebServiceDecl> getWebServices() {
      return this.compiler.getWebServices();
   }

   JwsBuildContext getBuildContext() {
      return this.compiler.getJwsBuildContext();
   }

   Path getSourcepath() {
      Path var1 = new Path(this.owningTask.getProject());
      var1.add(this.owningTask.getSourcepath());
      Iterator var2 = this.tempDirs.getTempDirs().iterator();

      while(var2.hasNext()) {
         File var3 = (File)var2.next();
         var1.createPathElement().setLocation(var3);
      }

      var2 = this.fileSets.iterator();

      while(var2.hasNext()) {
         FileSet var4 = (FileSet)var2.next();
         var1.createPathElement().setLocation(var4.getDir(this.owningTask.getProject()));
      }

      return var1;
   }

   void build(EarFile var1) throws IOException, WsBuildException {
      this.log(EventLevel.INFO, "JWS: processing module " + this.getName());
      this.validate();
      this.initialize();

      try {
         boolean var2 = this.generate(var1);
         if (var2) {
            this.log(EventLevel.WARNING, "No web services specified.  Module will not be built");
         } else {
            this.compile();
            if (this.catalogFS != null) {
               if (this.isEjb()) {
                  this.catalogFS.setPrefix("META-INF/");
               } else {
                  this.catalogFS.setPrefix("WEB-INF/");
               }

               this.fileSets.add(this.catalogFS);
            }

            this.doPackage(var1);
         }
      } finally {
         this.cleanup();
      }

   }

   private void validate() {
      this.validateFileSets();
      this.validateImpl();
   }

   private void validateFileSets() {
      Iterator var1 = this.fileSets.iterator();

      while(var1.hasNext()) {
         FileSet var2 = (FileSet)var1.next();
         if (var2 instanceof ZipFileSet) {
            ZipFileSet var3 = (ZipFileSet)var2;
            if (!StringUtil.isEmpty(var3.getFullpath(this.getTask().getProject()))) {
               throw new BuildException("Fullpath not supported on zipFileSet in jwsc");
            }
         }
      }

   }

   abstract void validateImpl();

   private void initialize() throws IOException {
      this.compiler.setOutputDir(this.tempDirs.createTempDir(this.name, this.owningTask.getTempdir()));
      this.compiler.setBindingFiles(this.owningTask.getBindingFiles());
      this.compiler.setJaxRPCWrappedArrayStyle(this.owningTask.isJaxRpcByteArrayStyle());
      this.compiler.setUpperCasePropName(this.owningTask.isUpperCasePropName());
      this.compiler.setLocalElementDefaultRequired(this.owningTask.isLocalElementDefaultRequired());
      this.compiler.setLocalElementDefaultNillable(this.owningTask.isLocalElementDefaultNillable());
      Iterator var1 = this.descriptors.iterator();

      while(var1.hasNext()) {
         Descriptor var2 = (Descriptor)var1.next();
         this.compiler.addDescriptor(var2.file);
      }

   }

   private void cleanup() {
      Collection var1 = this.getJwsFiles();
      Iterator var2;
      Jws var3;
      if (var1 != null) {
         var2 = var1.iterator();

         while(var2.hasNext()) {
            var3 = (Jws)var2.next();
            var3.setJClass((JClass)null);
         }
      }

      var1 = null;
      this.reset();
      if (this.getTask().isKeepTempFiles()) {
         StringBuilder var5 = new StringBuilder("Temporary files for module " + this.getName() + " created in:\n");
         Iterator var6 = this.tempDirs.getTempDirs().iterator();

         while(var6.hasNext()) {
            File var4 = (File)var6.next();
            var5.append("\t" + var4 + "\n");
         }

         this.log(EventLevel.INFO, var5.toString());
      } else {
         if (this.callbackJwsFiles != null) {
            var2 = this.callbackJwsFiles.iterator();

            while(var2.hasNext()) {
               var3 = (Jws)var2.next();
               if (var3.getCowReader() != null) {
                  var3.getCowReader().cleanup();
               }
            }
         }

         this.tempDirs.cleanup();
      }

      this.compiler.cleanup();
      this.compiler = null;
      this.fileSets = null;
      this.clients = null;
      this.descriptors = null;
      this.catalogFS = null;
      this.ctx = null;
   }

   abstract void reset();

   private boolean generate(EarFile var1) throws IOException, WsBuildException {
      this.callbackJwsFiles = this.generateClients();
      this.loadWebServices(this.getJwsFiles(), this.defaultContextPath);
      if (!this.callbackJwsFiles.isEmpty()) {
         this.loadWebServices(this.callbackJwsFiles, this.getCallbackContextPath());
      }

      if (var1.isAdded(this.getFileName())) {
         throw new BuildException("Module " + this.getName() + " already exists in this jws compile set.");
      } else {
         this.compiler.compile();
         return this.compiler.getWebServices().isEmpty();
      }
   }

   private void compile() throws IOException {
      Path var1 = this.getSourcepath();
      Collection var2 = this.getJwsFiles();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Jws var4 = (Jws)var3.next();
         if (WebServiceType.JAXWS.equals(var4.getType()) && var4.getCowFile() != null) {
            var1.createPathElement().setLocation(var4.getCowFile());
         }
      }

      this.owningTask.javac(this.getOutputDir(), (new JwsCompileList(this)).getFiles(), var1);
   }

   private void doPackage(EarFile var1) throws WsBuildException, IOException {
      if (this.isWsdlOnly()) {
         this.copyWsdl(var1.getEarDir());
      } else {
         FileSet var2 = this.owningTask.getResourceFileSet();
         if (var2 != null) {
            this.addFileSet(var2);
         }

         File var3 = new File(var1.getEarDir(), this.getFileName());
         JarFile var4 = new JarFile(this.getTask(), var3, this.getOutputDir());
         var4.addZipFileSets((new ModuleFileSets(this, this.getTask().isKeepGenerated())).getZipFileSets());
         var4.write(this.isExplode());
         this.addToEar(var1, this.getFileName());
      }

   }

   private void copyWsdl(File var1) {
      Iterator var2 = this.getWebServices().iterator();

      while(var2.hasNext()) {
         WebServiceDecl var3 = (WebServiceDecl)var2.next();
         if (WebServiceType.JAXWS.equals(var3.getType())) {
            ZipFileSet var4 = new ZipFileSet();
            var4.setDir(this.getOutputDir());
            var4.setIncludes("**/policies/*.xml, **/*.wsdl, **/*.xsd");
            var4.setPrefix(this.getName());
            this.copy(var4, var1);
         } else {
            File var6 = new File(var1, this.getFileName());
            var6.mkdirs();
            File var5 = new File(this.getOutputDir(), var3.getWsdlFile());
            AntUtil.copyFile(this.getTask().getProject(), var5, new File(var6, var5.getName()));
            AntUtil.copyFiles(this.getTask().getProject(), this.getOutputDir(), var6, "*.xsd");
         }
      }

   }

   private void copy(ZipFileSet var1, File var2) {
      var2.mkdirs();
      Copy var3 = new Copy();
      var3.setTaskName(this.owningTask.getTaskName());
      var3.setProject(this.owningTask.getProject());
      var3.addFileset(var1);
      String var4 = var1.getPrefix(this.owningTask.getProject());
      String var5 = var4 == null ? "" : var4;
      var3.setTodir(new File(var2, var5));
      var3.execute();
   }

   private void addToEar(EarFile var1, String var2) throws IOException {
      if (var2.startsWith("/")) {
         var2 = var2.substring(1);
      }

      if (this.isEjb()) {
         var1.addEjbModule(var2);
      } else {
         Iterator var3 = ((WebServiceDecl)this.getWebServices().get(0)).getPorts();

         assert var3.hasNext();

         var1.addWebModule(var2, ((PortDecl)var3.next()).getContextPath());
      }

   }

   private List<Jws> generateClients() throws IOException {
      ArrayList var1 = new ArrayList();
      if (!this.clients.isEmpty()) {
         File var2 = this.tempDirs.createTempDir(this.name, this.owningTask.getTempdir());
         CatalogInfo var3 = new CatalogInfo(true);
         Iterator var4 = this.clients.iterator();

         while(var4.hasNext()) {
            ClientGenFacadeTask var5 = (ClientGenFacadeTask)var4.next();
            CallbackGenProcessor var6 = new CallbackGenProcessor(this.owningTask.getProject());
            CallbackGen81Processor var7 = new CallbackGen81Processor(this.owningTask.getProject());
            if (WebServiceType.JAXWS.equals(var5.getType())) {
               var5.initJAXWSOptions();
               Options var8 = var5.jaxwsOptions;
               var8.setDebug(this.owningTask.getDebug());
               if (this.owningTask.getDebugLevel() != null) {
                  var8.setDebugLevel(this.owningTask.getDebugLevel());
               }

               var8.setVerbose(this.owningTask.getVerbose());
               var8.setFailonerror(this.owningTask.getFailonerror());
               var8.setIncludeantruntime(this.owningTask.getIncludeantruntime());
               var8.setIncludejavaruntime(this.owningTask.getIncludejavaruntime());
               var8.setOptimize(this.owningTask.getOptimize());
            }

            Iterator var13 = this.owningTask.getBindingFileSets().iterator();

            while(var13.hasNext()) {
               FileSet var9 = (FileSet)var13.next();
               var5.addBinding(var9);
            }

            var5.setDestDir(var2);
            var5.addProcessor(var6);
            var5.addProcessor(var7);
            var3 = ClientGenXMLs.processClient(var5, var5.type, var5.jaxwsOptions, var5.wsdl, var3);
            var5.execute();
            List var14 = var6.getCallbackJws();
            if (var14.size() == 0) {
               var14 = var7.getCallbackJws();
            }

            Iterator var15 = var14.iterator();

            while(var15.hasNext()) {
               Jws var10 = (Jws)var15.next();
               var1.add(var10);
            }
         }

         File var11 = this.tempDirs.createTempDir(this.name, this.owningTask.getTempdir());
         ClientGenXMLs.doAllCatalogFiles(this.owningTask, var11, var3);
         FileSet var12 = new FileSet();
         var12.setProject(this.owningTask.getProject());
         var12.setDir(var2);
         this.fileSets.add(var12);
         if (var3 != null && var3.catalogSize() > 0) {
            this.catalogFS = new ZipFileSet();
            this.catalogFS.setProject(this.owningTask.getProject());
            this.catalogFS.setDir(var11);
         }

         this.ctx.getClientGenOutputDirs().add(var2);
      }

      return var1;
   }

   private String getCallbackContextPath() {
      assert !this.compiler.isEjb() : "Callbacks can only be used in webapps";

      assert !this.compiler.getWebServices().isEmpty() : "No web services found";

      WebServiceDecl var1 = (WebServiceDecl)this.compiler.getWebServices().get(0);
      Iterator var2 = var1.getPorts();

      assert var2.hasNext() : "No ports for web service " + var1.getSourceFile();

      PortDecl var3 = (PortDecl)var2.next();
      return var3.getContextPath();
   }

   private void loadWebServices(Collection<Jws> var1, String var2) throws IOException, WsBuildException {
      this.setDefaultSrcDirOnJwses(var1);
      boolean var3 = this.owningTask.getClasspath() != null;
      JwsLoader var4 = new JwsLoader(this.getBuildContext(), this.getOutputDir(), this.getSourcepath(), this.owningTask.getCompleteClasspath(), var3);
      var4.load(var1);
      this.log(EventLevel.INFO, var1.size() + " JWS files being processed for module " + this.getName());
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         Jws var6 = (Jws)var5.next();
         this.compiler.addWebService(this.buildAndValidate(var6, var2));
      }

      this.compiler.validate();
      if (this.getBuildContext().isInError()) {
         throw new WsBuildException("Compiler validation failed: " + this.getBuildContext().getErrorMsgs());
      }
   }

   private WebServiceDecl buildAndValidate(Jws var1, String var2) throws WsBuildException {
      WebServiceDecl var3 = (new WebServiceDeclFactory(this.getBuildContext(), var2)).newInstance(var1);
      var3.validate();
      if (this.getBuildContext().isInError()) {
         throw new WsBuildException("JWS Validation failed: " + this.getBuildContext().getErrorMsgs());
      } else {
         this.log(EventLevel.INFO, "JWS: " + var1.getAbsoluteFile() + " Validated.");
         return var3;
      }
   }

   private void setDefaultSrcDirOnJwses(Collection<Jws> var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Jws var3 = (Jws)var2.next();
         if (!var3.hasSrcDir()) {
            if (this.getTask().getSrcdir() == null) {
               throw new BuildException("srcdir attribute must be set!", this.owningTask.getLocation());
            }

            var3.srcdir(this.getTask().getSrcdir());
         }
      }

   }

   private void log(EventLevel var1, String var2) {
      this.getBuildContext().getLogger().log(var1, var2);
   }

   public static class Descriptor {
      private File file = null;

      public void setFile(File var1) {
         this.file = var1;
      }
   }
}
