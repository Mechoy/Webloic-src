package weblogic.wsee.tools.anttasks;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.xml.namespace.QName;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.XMLCatalog;
import org.apache.tools.ant.types.selectors.FilenameSelector;
import org.apache.tools.ant.types.selectors.OrSelector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.bind.TypeFamily;
import weblogic.wsee.tools.TempDirManager;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.logging.AntLogger;
import weblogic.wsee.tools.wsdlc.Wsdl2JwsBuilder;
import weblogic.wsee.tools.wsdlc.Wsdl2JwsBuilderFactory;
import weblogic.wsee.tools.wsdlc.WsdlcUtils;
import weblogic.wsee.tools.wsdlc.jaxrpc.Options;
import weblogic.wsee.tools.xcatalog.CatalogOptions;
import weblogic.wsee.tools.xcatalog.DownloadXMLs;
import weblogic.wsee.tools.xcatalog.XCatalogUtil;
import weblogic.wsee.util.JAXWSClassLoaderFactory;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.cow.CowWriter;
import weblogic.wsee.util.jspgen.Main;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlFactory;

public class WsdlcTask extends DelegatingJavacTask {
   private static final boolean verbose = Verbose.isVerbose(WsdlcTask.class);
   private static final String NS_JAXWS_BINDINGS = "http://java.sun.com/xml/ns/jaxws";
   private static final String NS_JAXB_BINDINGS = "http://java.sun.com/xml/ns/jaxb";
   private static final String JAXWS_BINDINGS_LOCAL = "bindings";
   private static final String JAXWS_ATTR_WSDL_LOCATION = "wsdlLocation";
   private static final String JAXWS_ATTR_SCHEMA_LOCATION = "schemaLocation";
   private File catalogDir = null;
   private String srcWsdl;
   private QName portName;
   private String packageName;
   private File destJwsDir;
   private File destImplDir;
   private File destJavadocDir;
   private AntLogger logger;
   private WsdlDefinitions definitions;
   private List<FileSet> fileSets = new ArrayList();
   private boolean explode = false;
   private List<FileSet> bindingFileSets = new ArrayList();
   private WebServiceType type;
   private String implTemplate;
   private String implTemplateClassName;
   private Options jaxrpcOptions;
   private weblogic.wsee.tools.wsdlc.jaxws.Options jaxwsOptions;
   private CatalogOptions catalogOptions;

   public WsdlcTask() {
      this.type = WebServiceType.JAXRPC;
      this.implTemplate = null;
      this.implTemplateClassName = null;
      this.jaxrpcOptions = null;
      this.jaxwsOptions = null;
      this.catalogOptions = null;
   }

   public void setType(String var1) {
      this.type = WebServiceType.valueOf(var1);
   }

   private void initCatalogOptions() {
      if (this.catalogOptions == null) {
         this.catalogOptions = new CatalogOptions();
      }

   }

   private void initJAXRPCOptions() {
      if (this.jaxrpcOptions == null) {
         this.jaxrpcOptions = new Options();
      }

   }

   private void initJAXWSOptions() {
      if (this.jaxwsOptions == null) {
         this.jaxwsOptions = new weblogic.wsee.tools.wsdlc.jaxws.Options();
      }

   }

   public void setCatalog(File var1) {
      this.initCatalogOptions();
      this.catalogOptions.setCatalog(var1);
   }

   public void addConfiguredDepends(FileSet var1) {
      this.initJAXWSOptions();
      this.jaxwsOptions.addConfiguredDepends(var1);
   }

   public void addConfiguredProduces(FileSet var1) {
      this.initJAXWSOptions();
      this.jaxwsOptions.addConfiguredProduces(var1);
   }

   public void setWlw81CallbackGen(boolean var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setWlw81CallbackGen(var1);
   }

   public void setUpgraded81Jws(boolean var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setUpgraded81Jws(var1);
   }

   public void setExplode(boolean var1) {
      this.explode = var1;
   }

   public void addFileSet(FileSet var1) {
      assert var1 != null;

      var1.setProject(this.getProject());
      this.fileSets.add(var1);
   }

   public void setSrcWsdl(String var1) {
      File var2 = this.getProject().resolveFile(var1);
      if (var2.exists()) {
         try {
            var2 = var2.getCanonicalFile();
         } catch (IOException var4) {
         }

         this.srcWsdl = var2.toURI().toString();
      } else {
         this.srcWsdl = var1;
      }

   }

   /** @deprecated */
   @Deprecated
   public void setSrcBindingName(String var1) {
      this.log("DEPRECATED - Use of srcBindingName is deprecated.  Use srcPortName instead.");
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setBindingName(QName.valueOf(var1));
   }

   public void setSrcPortName(String var1) {
      this.portName = QName.valueOf(var1);
   }

   public void setSrcServiceName(String var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setServiceName(QName.valueOf(var1));
      this.initJAXWSOptions();
      this.jaxwsOptions.setServiceName(QName.valueOf(var1));
   }

   public void setPackageName(String var1) {
      this.packageName = var1;
   }

   public void setTypeFamily(String var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setTypeFamily(TypeFamily.getTypeFamilyForKey(var1));
   }

   public void setTypeFamily(TypeFamily var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setTypeFamily(var1);
   }

   public void setDestJwsDir(File var1) {
      this.destJwsDir = var1;
   }

   public void setDestImplDir(File var1) {
      this.destImplDir = var1;
   }

   public void setImplTemplate(String var1) {
      this.implTemplate = var1;
   }

   public void setDestJavadocDir(File var1) {
      this.destJavadocDir = var1;
   }

   public void setAutoDetectWrapped(boolean var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setAutoDetectWrapped(var1);
   }

   public void setJaxRPCWrappedArrayStyle(boolean var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setJaxRPCWrappedArrayStyle(var1);
   }

   public void setIncludeGlobalTypes(boolean var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setIncludeGlobalTypes(var1);
   }

   public void setSortSchemaTypes(boolean var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setSortSchemaTypes(var1);
   }

   public void setFillIncompleteParameterOrderList(boolean var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setFillIncompleteParameterOrderList(var1);
   }

   public void setCodeGenBaseData(Object var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setCodeGenBaseData(var1);
   }

   public void setImplTemplateClassName(String var1) {
      this.implTemplateClassName = var1;
   }

   private void preProcessXCatalog() throws BuildException {
      DownloadXMLs var1 = new DownloadXMLs();
      String var2 = null;
      TempDirManager var3 = new TempDirManager(this.getProject());

      try {
         this.catalogDir = var3.createTempDir("xcatalog", this.getTempdir());
      } catch (IOException var18) {
         throw new BuildException("Can not create temporary directory for xcatalog");
      }

      this.initCatalogOptions();
      var2 = var1.parseXMLs(this.catalogOptions, this.catalogDir, this.srcWsdl, true);
      if (this.bindingFileSets != null) {
         ListIterator var4 = this.bindingFileSets.listIterator();

         label39:
         while(true) {
            String[] var7;
            File var8;
            do {
               if (!var4.hasNext()) {
                  break label39;
               }

               FileSet var5 = (FileSet)var4.next();
               DirectoryScanner var6 = var5.getDirectoryScanner(this.getProject());
               var7 = var6.getIncludedFiles();
               var8 = var6.getBasedir();
            } while(var7 == null);

            FileSet var9 = new FileSet();
            var9.setProject(this.getProject());
            var9.setDir(this.catalogDir);
            OrSelector var10 = null;
            String[] var11 = var7;
            int var12 = var7.length;

            for(int var13 = 0; var13 < var12; ++var13) {
               String var14 = var11[var13];
               File var15 = new File(var8, var14);
               File var16 = new File(this.catalogDir, var14);
               this.copyFile(this.getProject(), var15, var16, this.catalogOptions.getXmlMaps());
               FilenameSelector var17 = new FilenameSelector();
               var17.setProject(this.getProject());
               var17.setName(var14);
               if (var10 == null) {
                  var10 = new OrSelector();
                  var9.add(var10);
               }

               var10.add(var17);
            }

            var4.set(var9);
         }
      }

      this.setSrcWsdl(var2);
      System.out.println("srcWsdl is redefined as [ " + var2 + " ]");
   }

   private void addNodes(List<Element> var1, NodeList var2) {
      for(int var4 = 0; var4 < var2.getLength(); ++var4) {
         Node var3 = var2.item(var4);
         if (var3 instanceof Element) {
            var1.add((Element)var3);
         }
      }

   }

   private boolean replaceAttr(Element var1, String var2, String var3, HashMap<URL, String> var4) throws MalformedURLException, IOException {
      String var5 = null;
      URL var6 = null;
      var5 = var1.getAttribute(var2);
      if (var5 == null) {
         return false;
      } else {
         if (!var5.startsWith("./") && !var5.startsWith("../")) {
            var6 = XCatalogUtil.toURL(var5);
         } else {
            var6 = (new File(var3, var5)).getCanonicalFile().toURI().toURL();
         }

         var5 = (String)var4.get(var6);
         if (var5 == null) {
            return false;
         } else {
            var1.setAttribute(var2, var5);
            return true;
         }
      }
   }

   private void copyFile(Project var1, File var2, File var3, HashMap<URL, String> var4) {
      if (var4 != null && var4.size() != 0) {
         try {
            boolean var5 = false;
            InputSource var6 = new InputSource(var2.getPath());
            String var7 = var2.getParent();
            String var8 = var6.getEncoding();
            Document var9 = XCatalogUtil.getDocument(var6);
            ArrayList var10 = new ArrayList();
            NodeList var11 = var9.getElementsByTagNameNS("http://java.sun.com/xml/ns/jaxws", "bindings");
            this.addNodes(var10, var11);
            var11 = var9.getElementsByTagNameNS("http://java.sun.com/xml/ns/jaxb", "bindings");
            this.addNodes(var10, var11);
            if (var10.size() > 0) {
               for(Iterator var12 = var10.iterator(); var12.hasNext(); var5 = true) {
                  Element var13 = (Element)var12.next();
                  if (!this.replaceAttr(var13, "wsdlLocation", var7, var4) && this.replaceAttr(var13, "schemaLocation", var7, var4)) {
                  }
               }
            }

            if (!var5) {
               throw new NoWsdlLocationFoundException();
            }

            XCatalogUtil.writeDoc2File(var9, var3, var8);
            if (verbose) {
               Verbose.log((Object)("Copy file [" + var2.getPath() + "] \ninto [" + var3.getPath() + "]"));
            }
         } catch (Exception var14) {
            if (!(var14 instanceof NoWsdlLocationFoundException)) {
               throw new BuildException("Process binding file failed", var14);
            }

            AntUtil.copyFile(var1, var2, var3);
         }
      } else {
         AntUtil.copyFile(var1, var2, var3);
      }

   }

   private void postProcessXCatalog() throws BuildException {
      if (this.catalogDir != null) {
         AntUtil.deleteDir(this.getProject(), this.catalogDir);
      }

   }

   public void execute() throws BuildException {
      this.validate();
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();
      ClassLoader var2 = WsdlcTask.class.getClassLoader();
      if (WebServiceType.JAXWS.equals(this.type)) {
         JAXWSClassLoaderFactory.getInstance().setContextLoader(var2);
      } else {
         Thread.currentThread().setContextClassLoader(var2);
      }

      try {
         this.preProcessXCatalog();
         this.initialize();
         CowWriter var3 = CowWriter.Factory.newInstance(WsdlcUtils.getWsdlFileName(this.definitions, this.srcWsdl), this.destJwsDir, this.explode);
         this.generateJws(var3);
         this.generateJavadoc(var3.getGeneratedDir());
         this.compileXmlBeansArtifacts(var3.getGeneratedDir());
         this.buildCow(var3);
      } catch (BuildException var8) {
         throw var8;
      } catch (Exception var9) {
         throw new BuildException(var9);
      } finally {
         Thread.currentThread().setContextClassLoader(var1);
         this.postProcessXCatalog();
      }

   }

   private void buildCow(CowWriter var1) throws IOException, WsdlException {
      var1.writeWsdl(this.definitions);
      var1.writeFiles(this.fileSets);
      var1.writeCow();
   }

   private void generateJws(CowWriter var1) throws WsBuildException {
      Wsdl2JwsBuilder var2 = Wsdl2JwsBuilderFactory.newInstance(this.type);
      var2.setLogger(this.logger);
      var2.setWsdl(this.srcWsdl);
      var2.setWsdlLocation(var1.getOutputWsdl());
      var2.setPortName(this.portName);
      var2.setDestDir(var1.getGeneratedDir());
      var2.setPackageName(this.packageName);
      var2.setDestImplDir(this.destImplDir);
      if (this.getClasspath() != null) {
         var2.setClasspath(this.getClasspath().list());
      }

      var2.setBindingFiles(AntUtil.getFiles((Collection)this.bindingFileSets, this.getProject()));
      if (this.type == WebServiceType.JAXRPC) {
         this.initJAXRPCOptions();
         if (this.implTemplateClassName != null) {
            this.jaxrpcOptions.setImplTemplate(this.implTemplateClassName);
         }

         this.jaxrpcOptions.setDefinitions(this.definitions);
         var2.setOptions(this.jaxrpcOptions);
      } else if (this.type == WebServiceType.JAXWS) {
         this.initJAXWSOptions();
         this.jaxwsOptions.setDebug(this.getDebug());
         this.jaxwsOptions.setDebugLevel(this.getDebugLevel());
         this.jaxwsOptions.setVerbose(this.getVerbose());
         this.jaxwsOptions.setOptimize(this.getOptimize());
         this.jaxwsOptions.setIncludeantruntime(this.getIncludeantruntime());
         this.jaxwsOptions.setIncludejavaruntime(this.getIncludejavaruntime());
         this.jaxwsOptions.setFork(this.getFork());
         var2.setOptions(this.jaxwsOptions);
      }

      var2.execute();
   }

   private void validate() throws BuildException {
      if (this.type == WebServiceType.JAXWS && this.jaxrpcOptions != null) {
         this.log("Ignoring JAX-RPC options - building a JAX-WS web service", 1);
      }

      if (this.type == WebServiceType.JAXRPC && this.jaxwsOptions != null) {
         this.log("Ignoring JAX-WS options - building a JAX-RPC web service", 1);
      }

   }

   private void initialize() {
      this.logger = new AntLogger(this);
      this.initWsdl();
      this.initJavac();
      this.initImplTemplate();
   }

   private void initImplTemplate() {
      if (this.implTemplateClassName == null) {
         this.implTemplateClassName = this.buildImplTemplate();
      }

   }

   private void initWsdl() {
      try {
         this.definitions = WsdlFactory.getInstance().parse(this.srcWsdl);
      } catch (WsdlException var2) {
         throw new BuildException(var2);
      }
   }

   private void initJavac() {
      this.setDestdir(this.destImplDir);
   }

   private String buildImplTemplate() {
      if (this.implTemplate == null) {
         return null;
      } else {
         try {
            WsdlcUtils.logVerbose(this.logger, "Parsing Jws impl template from ", this.implTemplate);
            Main var1 = new Main(this.implTemplate, this.destImplDir.toString(), true);
            File[] var2 = new File[]{new File(var1.getJavaFileName())};
            WsdlcUtils.logVerbose(this.logger, "Compiling Jws impl java source ", var1.getJavaFileName());
            if (this.destImplDir == null) {
               this.compile(var2);
            } else {
               this.compile(var2, new Path(this.getProject(), this.destImplDir.getAbsolutePath()));
            }

            return var1.getFullyQualifiedClassName();
         } catch (IOException var3) {
            throw new BuildException(var3);
         } catch (NullPointerException var4) {
            var4.printStackTrace();
            return null;
         }
      }
   }

   private void generateJavadoc(File var1) {
      if (this.destJavadocDir != null) {
         Project var2 = this.getProject();
         StringBuilder var3 = new StringBuilder();
         var3.append(buildJavadocSourceFiles(var2, var1));
         if (this.destImplDir != null) {
            var3.append(buildJavadocSourceFiles(var2, this.destImplDir));
         }

         AntUtil.javadocFiles(var2, var3.toString(), this.destJavadocDir);
      }

   }

   private static String buildJavadocSourceFiles(Project var0, File var1) {
      FileSet var2 = new FileSet();
      var2.setDir(var1);
      var2.setIncludes("**/*.java");
      DirectoryScanner var3 = var2.getDirectoryScanner(var0);
      String[] var4 = var3.getIncludedFiles();
      StringBuilder var5 = new StringBuilder();
      String[] var6 = var4;
      int var7 = var4.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         String var9 = var6[var8];
         var5.append((new File(var1, var9)).getAbsolutePath() + ",");
      }

      return var5.toString();
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

   public void addConfiguredXmlCatalog(XMLCatalog var1) {
      this.initCatalogOptions();
      var1.setProject(this.getProject());
      this.catalogOptions.setXmlCatalog(var1);
   }

   public void setAllowWrappedArrayForDocLiteral(boolean var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setAllowWrappedArrayForDocLiteral(var1);
   }

   private void compileXmlBeansArtifacts(File var1) {
      if (this.type == WebServiceType.JAXRPC && this.jaxrpcOptions != null && (this.jaxrpcOptions.getTypeFamily() == TypeFamily.XMLBEANS_APACHE || this.jaxrpcOptions.getTypeFamily() == TypeFamily.XMLBEANS)) {
         try {
            this.setDestdir(var1);
            FileSet var2 = new FileSet();
            var2.setProject(this.getProject());
            var2.setDir(var1);
            var2.setIncludes("**/*.java");
            String[] var3 = var2.getDirectoryScanner(this.getProject()).getIncludedFiles();
            ArrayList var4 = new ArrayList();
            String[] var5 = var3;
            int var6 = var3.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String var8 = var5[var7];
               var4.add((new File(var1, var8)).getCanonicalFile());
            }

            this.compile((File[])var4.toArray(new File[var4.size()]));
         } catch (IOException var12) {
            throw new BuildException(var12);
         } finally {
            this.setDestdir(this.destImplDir);
         }
      }

   }

   private class NoWsdlLocationFoundException extends RuntimeException {
      public NoWsdlLocationFoundException() {
      }
   }
}
