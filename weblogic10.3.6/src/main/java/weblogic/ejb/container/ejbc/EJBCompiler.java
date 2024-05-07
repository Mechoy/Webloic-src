package weblogic.ejb.container.ejbc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import javax.ejb.EJBHome;
import javax.xml.stream.XMLStreamException;
import weblogic.rmic;
import weblogic.application.ApplicationContextInternal;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.compliance.ComplianceChecker;
import weblogic.ejb.container.compliance.ComplianceCheckerFactory;
import weblogic.ejb.container.compliance.EJBComplianceTextFormatter;
import weblogic.ejb.container.deployer.MBeanDeploymentInfoImpl;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.ejbc.bytecodegen.GeneratorFactory;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.CMPCompiler;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.ejb.spi.EJBC;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.EjbDescriptorFactory;
import weblogic.ejb.spi.ICompiler;
import weblogic.ejb.spi.VersionHelper;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.EjbLocalRefBean;
import weblogic.j2ee.descriptor.EjbRefBean;
import weblogic.j2ee.descriptor.InterceptorBean;
import weblogic.j2ee.descriptor.InterceptorsBean;
import weblogic.j2ee.descriptor.ResourceEnvRefBean;
import weblogic.j2ee.descriptor.ResourceRefBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.j2ee.descriptor.wl.ResourceDescriptionBean;
import weblogic.j2ee.descriptor.wl.ResourceEnvDescriptionBean;
import weblogic.j2ee.validation.ModuleValidationInfo;
import weblogic.kernel.Kernel;
import weblogic.management.configuration.EJBComponentMBean;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.rmic.Remote2Java;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.Getopt2;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.compiler.CompilerInvoker;
import weblogic.utils.compiler.ICompilerFactory;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.compiler.jdt.JDTJavaCompilerFactory;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;

public final class EJBCompiler implements EJBC {
   private static final DebugLogger debugLogger;
   private static final AuthenticatedSubject kernelId;
   private static final String CHECKSUM_FILE = "_WL_GENERATED";
   private static final String JMS_RES_TYPE_ID = "javax.jms.";
   private static final String JDBC_RES_TYPE_ID = "javax.sql.";
   private static final String DISPATCH_POLICY = "dispatchPolicy";
   private static final String STICK_TO_FIRST_SERVER = "stickToFirstServer";
   private final Ejb2Rmi ejb2rmi;
   private final Getopt2 opts;
   private final File outputDir;
   private VersionHelper vHelper;
   private final boolean forceGeneration;
   private final boolean basicClientJar;
   private DeploymentInfo activeDeploymentInfo;
   private EJBComponentMBean mBean;
   private ClassLoader classLoader;
   private final boolean runComplianceChecker;
   protected ICompilerFactory compilerFactory;
   private static final int BUFFER_SIZE = 8192;

   public EJBCompiler(Getopt2 var1, ICompilerFactory var2) {
      this.ejb2rmi = new Ejb2Rmi(var1);
      Remote2Java.addRMIStubGeneratorOptions(var1);
      this.ejb2rmi.setCompilerFactory(var2);
      this.opts = var1;
      this.outputDir = new File(this.ejb2rmi.rootDirectory());
      this.runComplianceChecker = !var1.hasOption("nocompliance");
      this.forceGeneration = var1.hasOption("forceGeneration");
      this.basicClientJar = var1.hasOption("basicClientJar");
      this.compilerFactory = var2;
   }

   public EJBCompiler(Getopt2 var1) {
      this(var1, (ICompilerFactory)null);
   }

   private void setupEJB(GenericClassLoader var1, EJBComponentMBean var2, EjbDescriptorBean var3, VirtualJarFile var4, VersionHelper var5) throws ErrorCollectionException {
      this.classLoader = var1;
      this.mBean = var2;
      if (var5 != null) {
         this.activeDeploymentInfo = ((VersionHelperImpl)var5).getDeploymentInfo();
         this.vHelper = var5;
      } else {
         try {
            this.activeDeploymentInfo = getStandAloneDeploymentInfo(var1, var4, var3);
            Iterator var6 = this.activeDeploymentInfo.getBeanInfos().iterator();

            while(var6.hasNext()) {
               BeanInfo var7 = (BeanInfo)var6.next();
               if (var7 instanceof EntityBeanInfo) {
                  EntityBeanInfo var8 = (EntityBeanInfo)var7;
                  if (!var8.getIsBeanManagedPersistence()) {
                     var8.getCMPInfo().setup(new File(this.activeDeploymentInfo.getJarFileName()), this.opts, var4);
                  }
               }
            }

            this.vHelper = new VersionHelperImpl(this.activeDeploymentInfo, var4, this.opts);
         } catch (ErrorCollectionException var9) {
            throw var9;
         } catch (Exception var10) {
            throw new ErrorCollectionException(var10);
         }
      }

      this.addIIOPOptionsToOpts();
   }

   private List<String> getSourceFilePaths(Set<EjbCodeGenerator.Output> var1) {
      ArrayList var2 = new ArrayList(var1.size());
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         EjbCodeGenerator.Output var4 = (EjbCodeGenerator.Output)var3.next();
         var2.add(var4.getAbsoluteFilePath());
      }

      return var2;
   }

   private Map<String, String> getSourceContent(Set<EjbCodeGenerator.Output> var1) {
      HashMap var2 = new HashMap(var1.size());
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         EjbCodeGenerator.Output var4 = (EjbCodeGenerator.Output)var3.next();
         String var5 = var4.getPackage();
         String var6 = var4.getOutputFile().substring(0, var4.getOutputFile().length() - 5);
         String var7 = var5 != null && !"".equals(var5) ? var5 + "." + var6 : var6;
         var2.put(var7, var4.getOutputContent());
      }

      return var2;
   }

   private ICompiler makeCompilerInvoker() {
      if (this.ejb2rmi.isJDTBased()) {
         GenericClassLoader var1 = (GenericClassLoader)Thread.currentThread().getContextClassLoader();
         return new CompilerForJDT(this.ejb2rmi.rootDirectory(), var1.getClassPath());
      } else {
         return null != this.compilerFactory ? new CompilerForJavac(this.compilerFactory.makeCompilerInvoker(this.opts)) : new CompilerForJavac(new CompilerInvoker(this.opts));
      }
   }

   private void doCompile(Collection var1) throws ErrorCollectionException {
      if (this.runComplianceChecker) {
         this.checkCompliance(this.activeDeploymentInfo);
      }

      HashSet var2 = new HashSet();
      EjbDescriptorBean var3 = this.activeDeploymentInfo.getEjbDescriptorBean();
      InterceptorsBean var5;
      if (var3.isEjb30()) {
         EjbJarBean var4 = var3.getEjbJarBean();
         var5 = var4.getInterceptors();
         if (var5 != null) {
            InterceptorBean[] var6 = var5.getInterceptors();
            HashSet var7 = new HashSet();

            for(int var8 = 0; var8 < var6.length; ++var8) {
               try {
                  String var9 = var6[var8].getInterceptorClass();
                  Class var10 = this.classLoader.loadClass(var9);
                  if (!Serializable.class.isAssignableFrom(var10) && !var7.contains(var10)) {
                     this.generate(var6[var8]);
                     var7.add(var10);
                  }
               } catch (Exception var45) {
                  throw new ErrorCollectionException(var45);
               }
            }
         }
      }

      ArrayList var46 = new ArrayList();
      var5 = null;
      Iterator var49 = var1.iterator();

      while(var49.hasNext()) {
         BeanInfo var50 = (BeanInfo)var49.next();
         if (debugLogger.isDebugEnabled()) {
            debug("Generating Bean Sources");
         }

         try {
            var2.addAll(this.generate(var50));
         } catch (Exception var42) {
            throw new ErrorCollectionException(var42);
         }

         if (var50 instanceof EntityBeanInfo) {
            EntityBeanInfo var53 = (EntityBeanInfo)var50;
            if (!var53.getIsBeanManagedPersistence()) {
               try {
                  if (debugLogger.isDebugEnabled()) {
                     debug("Generating Persistence Sources");
                  }

                  EJBCMPCompiler var47 = new EJBCMPCompiler(this.outputDir, this.opts, this.compilerFactory);
                  var2.addAll(var47.generatePersistenceSources(var53));
                  var46.add(var47);
               } catch (Exception var41) {
                  throw new ErrorCollectionException(var41);
               }
            }
         }
      }

      if (!var2.isEmpty()) {
         if (debugLogger.isDebugEnabled()) {
            debug("Compiling EJB sources");
         }

         ICompiler var51 = this.makeCompilerInvoker();
         boolean var54 = Kernel.isServer();
         boolean var55 = false;

         try {
            if (this.ejb2rmi.isJDTBased()) {
               var51.compile(this.getSourceContent(var2));
            } else {
               String var57 = this.outputDir.getPath();
               var51.overrideTargetDirectory(var57);
               if (var54) {
                  var51.setWantCompilerErrors(true);
                  var51.setSourcepath("/dev/null");
               }

               var51.compile(this.getSourceFilePaths(var2));
            }
         } catch (IOException var40) {
            var55 = true;
            throw new ErrorCollectionException(var40);
         } finally {
            if (var54) {
               String var13 = var51.getCompilerErrors();
               if (var13 != null && var13.trim().length() > 0) {
                  if (var55) {
                     EJBLogger.logJavaCompilerErrorOutput(var13);
                  } else {
                     EJBLogger.logJavaCompilerOutput(var13);
                  }
               }
            }

         }
      }

      var49 = var46.iterator();

      while(var49.hasNext()) {
         CMPCompiler var48 = (CMPCompiler)var49.next();

         try {
            var48.postCompilation();
         } catch (Exception var43) {
            throw new ErrorCollectionException(var43);
         }
      }

      FileOutputStream var52 = null;

      try {
         File var56 = new File(this.outputDir, "_WL_GENERATED");
         if (var56.exists()) {
            var56.delete();
         }

         var52 = new FileOutputStream(var56);
         this.vHelper.getCurrentJarHash().store(var52, (String)null);
      } catch (Exception var38) {
         EJBLogger.logErrorSavingTimestamps(var38);
      } finally {
         try {
            if (var52 != null) {
               var52.close();
            }
         } catch (Exception var37) {
         }

      }

   }

   private void doRmic(Collection<BeanInfo> var1) throws ErrorCollectionException {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         BeanInfo var3 = (BeanInfo)var2.next();
         Map var4 = this.getIfaceToImplClassNameMap(var3);
         if (null != var4) {
            if (debugLogger.isDebugEnabled()) {
               debug("Got Remote Classes: " + var4.values());
            }

            if (!var4.isEmpty()) {
               this.runRmic(var4, var3);
            }
         }
      }

   }

   public void compileEJB(GenericClassLoader var1, VirtualJarFile var2, File var3, File var4, DeploymentPlanBean var5, ModuleValidationInfo var6) throws ErrorCollectionException {
      String var7 = var6 == null ? this.getUriFromPlan(var5) : var6.getURI();
      EjbDescriptorBean var8 = parseDescriptors(var2, var3, var4, var5, var7, var1);
      this.compileEJB(var1, (EJBComponentMBean)null, var8, var2, var6, (VersionHelper)null, (Collection)null);
   }

   private String getUriFromPlan(DeploymentPlanBean var1) {
      if (var1 == null) {
         return null;
      } else {
         ModuleOverrideBean[] var2 = var1.getModuleOverrides();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            ModuleOverrideBean var4 = var2[var3];
            if (var1.rootModule(var4.getModuleName())) {
               return var4.getModuleName();
            }
         }

         return null;
      }
   }

   public void compileEJB(GenericClassLoader var1, EJBComponentMBean var2, EjbDescriptorBean var3, VirtualJarFile var4) throws ErrorCollectionException {
      try {
         this.compileEJB(var1, var2, var3, var4, (ModuleValidationInfo)null, (VersionHelper)null, (Collection)null);
      } finally {
         JDTJavaCompilerFactory.getInstance().resetCache(var1);
      }

   }

   public void compileEJB(GenericClassLoader var1, EJBComponentMBean var2, VirtualJarFile var3, VersionHelper var4, Collection var5) throws ErrorCollectionException {
      this.compileEJB(var1, var2, (EjbDescriptorBean)null, var3, (ModuleValidationInfo)null, var4, var5);
   }

   private void compileEJB(GenericClassLoader var1, EJBComponentMBean var2, EjbDescriptorBean var3, VirtualJarFile var4, ModuleValidationInfo var5, VersionHelper var6, Collection var7) throws ErrorCollectionException {
      ClassLoader var8 = Thread.currentThread().getContextClassLoader();

      try {
         Thread.currentThread().setContextClassLoader(var1);
         this.setupEJB(var1, var2, var3, var4, var6);
         populateMVI(var5, this.activeDeploymentInfo);
         if (this.forceGeneration) {
            if (debugLogger.isDebugEnabled()) {
               debug("Recompiling because of forceGeneration flag");
            }

            var7 = this.activeDeploymentInfo.getBeanInfos();
         } else if (var7 == null) {
            var7 = this.vHelper.needsRecompile(var4);
         }

         Collection var9;
         if (this.allBeansNeedRmic()) {
            var9 = this.activeDeploymentInfo.getBeanInfos();
         } else {
            var9 = var7;
         }

         if (!var7.isEmpty()) {
            this.doCompile(var7);
            if (debugLogger.isDebugEnabled()) {
               debug("Recompilation completed");
            }
         } else if (debugLogger.isDebugEnabled()) {
            debug("Recompilation determined unnecessary");
         }

         if (!var9.isEmpty()) {
            this.doRmic(var9);
            if (debugLogger.isDebugEnabled()) {
               debug("Rmic completed");
            }
         } else if (debugLogger.isDebugEnabled()) {
            debug("Rmic determined unnecessary");
         }

         if (!Kernel.isServer()) {
            String var10 = this.activeDeploymentInfo.getClientJarFileName();

            try {
               if (var10 != null && !"".equals(var10)) {
                  this.createClientJar(var10, this.activeDeploymentInfo.getBeanInfos());
               }
            } catch (IOException var16) {
               throw new ErrorCollectionException(var16);
            }
         }
      } finally {
         if (var8 != null) {
            Thread.currentThread().setContextClassLoader(var8);
         }

      }

   }

   private boolean allBeansNeedRmic() {
      return this.isOptionPresent("idl") || this.isOptionPresent("idlOverwrite") || this.isOptionPresent("idlNoValueTypes") || this.isOptionPresent("idlFactories") || this.isOptionPresent("idlVisibroker") || this.isOptionPresent("idlDirectory") || this.isOptionPresent("idlMethodSignatures") || this.isOptionPresent("iiop") || this.isOptionPresent("iiopDirectory");
   }

   private boolean isOptionPresent(String var1) {
      return this.opts.hasOption(var1);
   }

   private Collection<Class<?>> getStubClasses(Collection<String> var1) {
      Iterator var2 = var1.iterator();
      ArrayList var3 = new ArrayList();

      while(var2.hasNext()) {
         String var4 = (String)var2.next();
         var4 = var4.substring(this.outputDir.getAbsolutePath().length() + 1, var4.length());
         var4 = ClassUtils.fileNameToClass(var4);

         try {
            Class var5 = this.classLoader.loadClass(var4);
            var3.add(var5);
         } catch (ClassNotFoundException var6) {
         }
      }

      return var3;
   }

   private void createClientJar(String var1, Collection<BeanInfo> var2) throws IOException {
      ClientJarMaker var3 = new ClientJarMaker(this.classLoader);
      Collection var4 = this.getStubClasses(this.getAllIIOPStubs(this.outputDir));
      if (this.opts.hasOption("disableHotCodeGen")) {
         Collection var5 = this.getStubClasses(this.getRMIStubClasses(this.outputDir));
         var4.addAll(var5);
      }

      String[] var8 = var3.createClientJar(var2, var4);
      if (debugLogger.isDebugEnabled()) {
         debug("** Client jar size is: " + var8.length);
         StringBuffer var6 = new StringBuffer();
         var6.append("** Client jar files: ");

         for(int var7 = 0; var7 < var8.length; ++var7) {
            if (var7 != 0) {
               var6.append(", ");
            }

            var6.append(var8[var7]);
         }
      }

      if (var8.length > 0) {
         this.makeJar(var1, var8);
         EJBLogger.logClientJarCreated(var1);
      }

   }

   private List<String> getRMIStubClasses(File var1) {
      assert var1 != null;

      assert var1.isDirectory();

      String[] var2 = var1.list();
      String var3 = ServerHelper.WLS_STUB_VERSION + ".class";
      ArrayList var4 = new ArrayList();

      for(int var5 = 0; var5 < var2.length; ++var5) {
         File var6 = new File(var1.getAbsolutePath() + File.separator + var2[var5]);
         if (var6.isDirectory()) {
            var4.addAll(this.getRMIStubClasses(var6));
         } else if (var6.getAbsolutePath().endsWith(var3)) {
            var4.add(var6.getAbsolutePath());
         }
      }

      return var4;
   }

   private List<String> getRuntimeDescriptors(File var1) {
      assert var1 != null;

      assert var1.isDirectory();

      String[] var2 = var1.list();
      String var3 = "ImplRTD.xml";
      ArrayList var4 = new ArrayList();

      for(int var5 = 0; var5 < var2.length; ++var5) {
         File var6 = new File(var1.getAbsolutePath() + File.separator + var2[var5]);
         if (var6.isDirectory()) {
            var4.addAll(this.getRuntimeDescriptors(var6));
         } else if (var6.getAbsolutePath().endsWith(var3)) {
            String var7 = var6.getAbsolutePath();
            String var8 = var7.substring(this.outputDir.getAbsolutePath().length() + 1, var7.length());
            var8 = var8.replace('\\', '/');
            var4.add(var8);
         }
      }

      return var4;
   }

   private List<String> getAllIIOPStubs(File var1) {
      assert var1 != null;

      assert var1.isDirectory();

      String[] var2 = var1.list();
      String var3 = "_Stub.class";
      ArrayList var4 = new ArrayList();

      for(int var5 = 0; var5 < var2.length; ++var5) {
         File var6 = new File(var1.getAbsolutePath() + File.separator + var2[var5]);
         if (var6.isDirectory()) {
            var4.addAll(this.getAllIIOPStubs(var6));
         } else if (var6.getAbsolutePath().endsWith(var3)) {
            var4.add(var6.getAbsolutePath());
         }
      }

      return var4;
   }

   private File makeClientJarOutputDirectoryIfNecessary() throws IOException {
      String var1 = this.opts.getOption("clientJarOutputDir");
      if (var1 != null && var1.length() != 0) {
         File var2 = new File(var1);
         if (var2.exists()) {
            if (!var2.isDirectory()) {
               throw new IOException("ERROR: the clientJarOutputDir [" + var2.getAbsolutePath() + "] must be directory.");
            }
         } else {
            var2.mkdir();
         }

         return var2;
      } else {
         return null;
      }
   }

   private void makeJar(String var1, String[] var2) throws IOException {
      assert var2.length > 0;

      File var3 = this.makeClientJarOutputDirectoryIfNecessary();
      FileOutputStream var4 = null;
      JarOutputStream var5 = null;

      try {
         String var6 = var1;
         if (var3 != null) {
            var6 = var3.getCanonicalPath() + File.separator + var1;
         }

         var4 = new FileOutputStream(var6);
         var5 = new JarOutputStream(var4);

         String var11;
         for(int var7 = 0; var7 < var2.length; ++var7) {
            InputStream var8 = null;
            ZipEntry var9 = null;

            try {
               String var10 = var2[var7].replace('.', '/');
               var11 = var10 + ".class";
               var8 = this.classLoader.getResourceAsStream(var11);
               var9 = new ZipEntry(var11);
               var5.putNextEntry(var9);
               this.copyBytes(var8, var5);
               var5.closeEntry();
            } finally {
               if (var8 != null) {
                  var8.close();
               }

            }
         }

         if (!this.basicClientJar) {
            InputStream var22 = this.classLoader.getResourceAsStream("META-INF/ejb-jar.xml");
            ZipEntry var23 = new ZipEntry("META-INF/ejb-jar.xml");
            var5.putNextEntry(var23);
            this.copyBytes(var22, var5);
            var5.closeEntry();
            var22 = this.classLoader.getResourceAsStream("META-INF/weblogic-ejb-jar.xml");
            if (var22 != null) {
               var23 = new ZipEntry("META-INF/weblogic-ejb-jar.xml");
               var5.putNextEntry(var23);
               this.copyBytes(var22, var5);
               var5.closeEntry();
            }

            List var24 = this.getRuntimeDescriptors(this.outputDir);
            Iterator var25 = var24.iterator();

            while(var25.hasNext()) {
               var11 = (String)var25.next();
               var22 = this.classLoader.getResourceAsStream(var11);
               if (var22 != null) {
                  var23 = new ZipEntry(var11);
                  var5.putNextEntry(var23);
                  this.copyBytes(var22, var5);
                  var5.closeEntry();
               }
            }
         }
      } finally {
         if (var5 != null) {
            var5.close();
         }

         if (var4 != null) {
            var4.close();
         }

      }

   }

   private void copyBytes(InputStream var1, OutputStream var2) throws IOException {
      byte[] var3 = new byte[8192];
      boolean var4 = false;

      int var5;
      while((var5 = var1.read(var3, 0, 8192)) != -1) {
         var2.write(var3, 0, var5);
      }

   }

   private void checkCompliance(DeploymentInfo var1) throws ErrorCollectionException {
      assert var1 != null;

      try {
         ComplianceChecker var2 = ComplianceCheckerFactory.getComplianceChecker();
         var2.checkDeploymentInfo(var1);
         if (debugLogger.isDebugEnabled()) {
            debug("Compliance Checker said bean was compliant");
         }

      } catch (ClassNotFoundException var3) {
         throw new ErrorCollectionException(new ToolFailureException("Unable to load a class required by your EJB: " + var3.getMessage()));
      } catch (ErrorCollectionException var4) {
         throw var4;
      }
   }

   private void generate(InterceptorBean var1) throws Exception {
      if (debugLogger.isDebugEnabled()) {
         debug("Generating code for interceptor " + var1.getInterceptorClass());
      }

      assert var1 != null;

      NamingConvention var2 = new NamingConvention(var1.getInterceptorClass());
      GeneratorFactory.generate(var1, var2, this.outputDir.getAbsolutePath());
   }

   private List<EjbCodeGenerator.Output> generate(BeanInfo var1) throws Exception {
      ArrayList var2 = new ArrayList();
      if (var1 instanceof MessageDrivenBeanInfo) {
         MessageDrivenBeanInfo var3 = (MessageDrivenBeanInfo)var1;
         if (null == var3.getName()) {
            EJBComplianceTextFormatter var7 = EJBComplianceTextFormatter.getInstance();
            throw new EJBCException(var7.destinationNotFound(var1.getEJBName()));
         }

         if (!var3.getIsWeblogicJMS() || var3.isIndirectlyImplMessageListener()) {
            if (debugLogger.isDebugEnabled()) {
               debug("Generating code for ejb " + var1.getEJBName());
            }

            NamingConvention var4 = new NamingConvention(var3.getBeanClassName(), var3.getEJBName());
            GeneratorFactory.generate(var3, var4, this.outputDir.getAbsolutePath());
         }
      } else {
         if (!(var1 instanceof ClientDrivenBeanInfo)) {
            throw new RuntimeException("Uknnown type of BeanInfo:" + var1);
         }

         if (debugLogger.isDebugEnabled()) {
            debug("Generating code for ejb " + var1.getEJBName());
         }

         assert var1 != null;

         if (((ClientDrivenBeanInfo)var1).isSessionBean()) {
            NamingConvention var5 = new NamingConvention(var1.getBeanClassName(), var1.getEJBName());
            GeneratorFactory.generate((SessionBeanInfo)var1, var5, this.outputDir.getAbsolutePath());
         } else {
            List var6 = this.ejb2rmi.generate(var1);
            if (debugLogger.isDebugEnabled()) {
               debug("Generated the following sources for this EJB: " + var6);
            }

            var2.addAll(this.ejb2rmi.getGeneratedOutputs());
         }
      }

      return var2;
   }

   private Map<Class<?>, String> getIfaceToImplClassNameMap(BeanInfo var1) {
      HashMap var2 = null;
      if (var1 instanceof ClientDrivenBeanInfo) {
         ClientDrivenBeanInfo var3 = (ClientDrivenBeanInfo)var1;
         if (var3.hasRemoteClientView()) {
            var2 = new HashMap();
            NamingConvention var4 = new NamingConvention(var1.getBeanClassName(), var1.getEJBName());
            if (var3.hasDeclaredRemoteHome()) {
               var2.put(var3.getHomeInterfaceClass(), var4.getHomeClassName());
               var2.put(var3.getRemoteInterfaceClass(), var4.getEJBObjectClassName());
            }

            if (var1 instanceof Ejb3SessionBeanInfo) {
               Ejb3SessionBeanInfo var5 = (Ejb3SessionBeanInfo)var1;
               Iterator var6 = var5.getBusinessRemotes().iterator();

               while(var6.hasNext()) {
                  Class var7 = (Class)var6.next();
                  var2.put(var7, var4.getRemoteBusinessImplClassName(var7));
               }
            }
         }
      }

      return var2;
   }

   private Set<String> runRmic(Map<Class<?>, String> var1, BeanInfo var2) throws ErrorCollectionException {
      try {
         HashSet var3 = new HashSet();
         ClassLoader var4 = var2.getClassLoader();
         Iterator var5 = var1.entrySet().iterator();

         while(var5.hasNext()) {
            Map.Entry var6 = (Map.Entry)var5.next();
            RMICOptions var7 = this.buildRmicOptions((Class)var6.getKey(), var2);
            String[] var8 = this.getRmicCommandOptions(var7, (String)var6.getValue());
            Collection var9 = var7.getRmicMethodDescriptors();
            String[] var10 = rmic.main_nocompile(var8, var4, var9);

            for(int var11 = 0; var11 < var10.length; ++var11) {
               var3.add(var10[var11]);
            }
         }

         return var3;
      } catch (Exception var12) {
         throw new ErrorCollectionException(var12);
      }
   }

   private boolean isHomeClass(Class<?> var1) {
      return EJBHome.class.isAssignableFrom(var1);
   }

   private RMICOptions buildRmicOptions(Class<?> var1, BeanInfo var2) {
      RMICOptions var3 = new RMICOptions(var2);
      var3.setIIOPSecurityOptions();
      if (this.isHomeClass(var1)) {
         var3.setHomeOptions();
      } else {
         var3.setEOOptions(var1);
      }

      var3.setOutputDirectory(this.outputDir.getAbsolutePath());
      return var3;
   }

   private String[] getRmicCommandOptions(RMICOptions var1, String var2) {
      List var3 = var1.asList();
      boolean var4 = var3.contains("-dispatchPolicy");
      boolean var5 = var3.contains("-stickToFirstServer");
      Getopt2 var6 = (Getopt2)this.opts.clone();
      if (var4 && var6.hasOption("dispatchPolicy")) {
         var6.removeOption("dispatchPolicy");
      }

      if (var5 && var6.hasOption("stickToFirstServer")) {
         var6.removeOption("stickToFirstServer");
      }

      String[] var7 = var6.asCommandArray();

      for(int var8 = 0; var8 < var7.length; ++var8) {
         if (var7[var8].equals("-output")) {
            ++var8;
         } else if (var7[var8].equals("-maxfiles")) {
            ++var8;
         } else if (var7[var8].equals("-plan")) {
            ++var8;
         } else if (var7[var8].equals("-clientJarOutputDir")) {
            ++var8;
         } else if (!var7[var8].equals("-nodeploy") && !var7[var8].equals("-nocompliance") && !var7[var8].equals("-lineNumbers") && !var7[var8].equals("-forceGeneration") && !var7[var8].equals("-basicClientJar") && !var7[var8].equals("-quiet") && !var7[var8].equals("-convertDDs") && !var7[var8].equals("-writeInferredDescriptors")) {
            var3.add(var7[var8]);
         }
      }

      var3.add(var2);
      return (String[])((String[])var3.toArray(new String[var3.size()]));
   }

   private void addIIOPOptionsToOpts() {
      this.opts.addOption("integrity", "integrity", "IIOP Transport integrity");
      this.opts.addOption("confidentiality", "confidentiality", "IIOP Transport confidentiality");
      this.opts.addOption("clientCertAuthentication", "clientCertAuthentication", "IIOP Transport clientCertAuthentication");
      this.opts.addOption("clientAuthentication", "clientAuthentication", "clientAuthentication");
      this.opts.addOption("identityAssertion", "identityAssertion", "identityAssertion");
   }

   private static void debug(String var0) {
      debugLogger.debug("[EJBCompiler] " + var0);
   }

   private void populateValidationInfo(GenericClassLoader var1, VirtualJarFile var2, File var3, File var4, DeploymentPlanBean var5, ModuleValidationInfo var6) throws ErrorCollectionException {
      String var7 = var6 == null ? this.getUriFromPlan(var5) : var6.getURI();
      EjbDescriptorBean var8 = parseDescriptors(var2, var3, var4, var5, var7, var1);
      DeploymentInfo var9 = null;

      try {
         var9 = getStandAloneDeploymentInfo(var1, var2, var8);
      } catch (Exception var11) {
         throw new ErrorCollectionException(var11);
      }

      populateMVI(var6, var9);
   }

   public void populateValidationInfo(GenericClassLoader var1, VirtualJarFile var2, File var3, ModuleValidationInfo var4) throws ErrorCollectionException {
      this.populateValidationInfo(var1, var2, var3, (File)null, (DeploymentPlanBean)null, var4);
   }

   private static void populateMVI(ModuleValidationInfo var0, DeploymentInfo var1) {
      if (var0 != null) {
         Iterator var2 = var1.getBeanInfos().iterator();

         label115:
         while(var2.hasNext()) {
            BeanInfo var3 = (BeanInfo)var2.next();
            var0.addEJBValidationInfo(var3.getEJBName(), var3);
            Iterator var4 = var3.getAllEJBReferences().iterator();

            while(var4.hasNext()) {
               EjbRefBean var5 = (EjbRefBean)var4.next();
               if (var5.getEjbLink() != null) {
                  var0.addEJBRef(var3.getEJBName(), var5.getEjbRefName(), false, var5.getRemote(), var5.getHome(), var5.getEjbRefType(), var5.getEjbLink(), false);
               }
            }

            Iterator var12 = var3.getAllEJBLocalReferences().iterator();

            while(var12.hasNext()) {
               EjbLocalRefBean var6 = (EjbLocalRefBean)var12.next();
               if (var6.getEjbLink() != null) {
                  var0.addEJBRef(var3.getEJBName(), var6.getEjbRefName(), true, var6.getLocal(), var6.getLocalHome(), var6.getEjbRefType(), var6.getEjbLink(), true);
               }
            }

            if (var3 instanceof EntityBeanInfo) {
               EntityBeanInfo var13 = (EntityBeanInfo)var3;
               String var7 = var13.getCacheName();
               if (var7 != null) {
                  var0.addAppScopedCacheReference(var3.getEJBName(), var7);
               }
            }

            Iterator var14 = var3.getAllWlResourceReferences().iterator();

            while(true) {
               ResourceDescriptionBean var15;
               do {
                  if (!var14.hasNext()) {
                     Iterator var16 = var3.getAllWlResourceEnvReferences().iterator();

                     while(true) {
                        ResourceEnvDescriptionBean var17;
                        do {
                           if (!var16.hasNext()) {
                              continue label115;
                           }

                           var17 = (ResourceEnvDescriptionBean)var16.next();
                        } while(var17.getResourceLink() == null);

                        String var18 = null;
                        Iterator var19 = var3.getAllResourceEnvReferences().iterator();

                        while(var19.hasNext()) {
                           ResourceEnvRefBean var11 = (ResourceEnvRefBean)var19.next();
                           if (var11.getResourceEnvRefName().equals(var17.getResourceEnvRefName())) {
                              var18 = var11.getResourceEnvRefType();
                              break;
                           }
                        }

                        if (var18 != null && var18.startsWith("javax.jms.")) {
                           var0.addJMSLinkRefs(var3.getEJBName(), "EJB", var17.getResourceEnvRefName(), var17.getResourceLink(), var18, false);
                        }

                        if (var18 != null && var18.startsWith("javax.sql.")) {
                           var0.addJDBCLinkRefs(var3.getEJBName(), "EJB", var17.getResourceEnvRefName(), var17.getResourceLink(), var18, false);
                        }
                     }
                  }

                  var15 = (ResourceDescriptionBean)var14.next();
               } while(var15.getResourceLink() == null);

               String var8 = null;
               Iterator var9 = var3.getAllResourceReferences().iterator();

               while(var9.hasNext()) {
                  ResourceRefBean var10 = (ResourceRefBean)var9.next();
                  if (var10.getResRefName().equals(var15.getResRefName())) {
                     var8 = var10.getResType();
                     break;
                  }
               }

               if (var8 != null && var8.startsWith("javax.jms.")) {
                  var0.addJMSLinkRefs(var3.getEJBName(), "EJB", var15.getResRefName(), var15.getResourceLink(), var8, false);
               }

               if (var8 != null && var8.startsWith("javax.sql.")) {
                  var0.addJDBCLinkRefs(var3.getEJBName(), "EJB", var15.getResRefName(), var15.getResourceLink(), var8, false);
               }
            }
         }
      }

   }

   private static DeploymentInfo getStandAloneDeploymentInfo(GenericClassLoader var0, VirtualJarFile var1, EjbDescriptorBean var2) throws Exception {
      return new MBeanDeploymentInfoImpl(var2, var0, "", "", var1, (ApplicationContextInternal)null);
   }

   private static EjbDescriptorBean parseDescriptors(VirtualJarFile var0, File var1, File var2, DeploymentPlanBean var3, String var4, GenericClassLoader var5) throws ErrorCollectionException {
      EjbDescriptorBean var6 = null;

      try {
         var6 = EjbDescriptorFactory.createReadOnlyDescriptorFromJarFile(var0, var1, var2, var3, (String)null, var4, var5, (VirtualJarFile[])null);
         return var6;
      } catch (XMLProcessingException var8) {
         throw new ErrorCollectionException(var8);
      } catch (XMLParsingException var9) {
         throw new ErrorCollectionException(var9);
      } catch (IOException var10) {
         throw new ErrorCollectionException(var10);
      } catch (XMLStreamException var11) {
         throw new ErrorCollectionException(var11);
      } catch (Throwable var12) {
         throw new ErrorCollectionException(var12);
      }
   }

   public void setCompilerFactory(ICompilerFactory var1) {
      this.compilerFactory = var1;
      this.ejb2rmi.setCompilerFactory(var1);
   }

   static {
      debugLogger = EJBDebugService.compilationLogger;
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }
}
