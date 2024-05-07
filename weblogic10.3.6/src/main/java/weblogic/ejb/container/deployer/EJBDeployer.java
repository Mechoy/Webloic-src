package weblogic.ejb.container.deployer;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ModuleException;
import weblogic.deployment.EnvironmentException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.ReadConfig;
import weblogic.ejb.container.cmp.rdbms.RDBMSPersistenceManager;
import weblogic.ejb.container.ejbc.VersionHelperImpl;
import weblogic.ejb.container.injection.EjbComponentCreatorImpl;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.CMPInfo;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.EjbComponentCreator;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.InvalidationBeanManager;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.container.interfaces.MessageDrivenManagerIntf;
import weblogic.ejb.container.interfaces.SecurityRoleMapping;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.interfaces.TimerManager;
import weblogic.ejb.container.internal.DataBeanMapper;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb.container.internal.RuntimeHelper;
import weblogic.ejb.container.manager.BaseEntityManager;
import weblogic.ejb.container.manager.StatelessManager;
import weblogic.ejb.container.persistence.spi.PersistenceManager;
import weblogic.ejb.container.timer.EJBTimerManagerFactory;
import weblogic.ejb.spi.EJBC;
import weblogic.ejb.spi.EJBCFactory;
import weblogic.ejb.spi.EJBDeployListener;
import weblogic.ejb.spi.EJBDeploymentException;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.InvalidationMessage;
import weblogic.ejb.spi.VersionHelper;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.j2ee.J2EEUtils;
import weblogic.j2ee.descriptor.InterceptorBean;
import weblogic.j2ee.descriptor.PersistenceContextRefBean;
import weblogic.j2ee.descriptor.PersistenceUnitRefBean;
import weblogic.j2ee.descriptor.wl.MessageDestinationDescriptorBean;
import weblogic.j2ee.descriptor.wl.SecurityPermissionBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.j2ee.injection.PitchforkContext;
import weblogic.jndi.Environment;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.configuration.EJBComponentMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.t3.srvr.T3Srvr;
import weblogic.utils.AssertionError;
import weblogic.utils.BadOptionException;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.FileUtils;
import weblogic.utils.Getopt2;
import weblogic.utils.StringUtils;
import weblogic.utils.classloaders.DirectoryClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.compiler.CompilerInvoker;
import weblogic.utils.jars.VirtualJarFile;

public final class EJBDeployer {
   private static final DebugLogger debugLogger;
   private DeployedManagers deployedManagers;
   private static final AuthenticatedSubject kernelId;
   private VersionHelper vHelper;
   private DeploymentInfo dinfo;
   private ApplicationContextInternal appCtx;
   private Map eBuilderMap = new HashMap();
   private String name;
   private EJBModule ejbModule;
   private EJBComponentRuntimeMBeanImpl compRTMBean;
   private Map cacheMap = null;
   private Map queryCacheMap = null;
   private RuntimeHelper helper = null;
   private String jarFilePath = null;
   private String outputDirPath = null;
   private String securitySpec = null;
   private String objectSchemaName = null;
   private Map dataBeansMap = null;
   private static String separator;
   private List deployListeners = new ArrayList();
   private boolean firstActivate = true;
   private final List<MessageDrivenManagerIntf> subscriptionDeleteList = new LinkedList();
   private PitchforkContext pitchforkContext;
   ClassLoader moduleCL = null;

   public EJBDeployer(ApplicationContextInternal var1, EJBModule var2, EJBComponentRuntimeMBeanImpl var3) throws EJBDeploymentException, ModuleException {
      this.ejbModule = var2;
      this.compRTMBean = var3;
      this.name = var3.getName();
      this.appCtx = var1;
      this.deployedManagers = new DeployedManagers();
      this.createDeployListeners();
      Iterator var4 = this.deployListeners.iterator();

      while(var4.hasNext()) {
         EJBDeployListener var5 = (EJBDeployListener)var4.next();
         var5.init(var1, var2.getEJBComponent());
      }

   }

   private void createDeployListeners() throws EJBDeploymentException {
      Object var1 = null;
      if (this.appCtx != null) {
         var1 = this.appCtx.getAppClassLoader();
      }

      if (var1 == null) {
         var1 = Thread.currentThread().getContextClassLoader();
      }

      Class var2 = null;

      try {
         var2 = ((ClassLoader)var1).loadClass("weblogic.wsee.deploy.WsEJBDeployListener");
         this.deployListeners.add(var2.newInstance());
      } catch (InstantiationException var4) {
         throw new EJBDeploymentException(this.ejbModule.getURI(), this.ejbModule.getURI(), var4);
      } catch (IllegalAccessException var5) {
         throw new EJBDeploymentException(this.ejbModule.getURI(), this.ejbModule.getURI(), var5);
      } catch (ClassNotFoundException var6) {
         throw new EJBDeploymentException(this.ejbModule.getURI(), this.ejbModule.getURI(), var6);
      }
   }

   public EJBComponentRuntimeMBean getRuntimeMBean() {
      return this.compRTMBean;
   }

   private Context setupEnvironmentContext(Context var1, BeanInfo var2, String var3, String var4) throws NamingException, EnvironmentException {
      var1.bind("ejb-info", new EjbInfoImpl(var2));
      LinkedList var5 = new LinkedList();
      LinkedList var6 = new LinkedList();
      LinkedList var7 = new LinkedList();
      LinkedList var8 = new LinkedList();
      String var9 = var2.getEJBName();
      List var10 = this.dinfo.getInterceptorBeans(var9);
      Iterator var11 = var10.iterator();

      while(var11.hasNext()) {
         InterceptorBean var12 = (InterceptorBean)var11.next();
         var5.addAll(Arrays.asList(var12.getEnvEntries()));
         var6.addAll(Arrays.asList(var12.getResourceEnvRefs()));
         var7.addAll(Arrays.asList(var12.getPersistenceContextRefs()));
         var8.addAll(Arrays.asList(var12.getPersistenceUnitRefs()));
      }

      EnvironmentBuilder var13 = new EnvironmentBuilder(var1, var3, var4, var2.getEJBName(), var2);
      var5.addAll(var2.getAllEnvironmentEntries());
      var13.addEnvironmentEntries(var5);
      var6.addAll(var2.getAllResourceEnvReferences());
      var13.addResourceEnvReferences(var6, var2.getAllResourceEnvReferenceJNDINames(this.appCtx == null ? null : this.appCtx.getApplicationId()));
      var7.addAll(Arrays.asList(var2.getPersistenceContextRefs()));
      var13.addPersistenceContextRefs((PersistenceContextRefBean[])var7.toArray(new PersistenceContextRefBean[0]), var2.getModuleClassLoader(), this.ejbModule);
      var8.addAll(Arrays.asList(var2.getPersistenceUnitRefs()));
      var13.addPersistenceUnitRefs((PersistenceUnitRefBean[])var8.toArray(new PersistenceUnitRefBean[0]), var2.getModuleClassLoader(), this.ejbModule);
      var13.addTimerServiceBinding();
      this.eBuilderMap.put(var2.getEJBName(), var13);
      return var13.getContext();
   }

   private void unsetupEnvironmentContext() {
      if (this.dinfo != null) {
         Iterator var1 = this.dinfo.getBeanInfos().iterator();

         while(true) {
            BeanInfo var2;
            EnvironmentBuilder var3;
            do {
               if (!var1.hasNext()) {
                  return;
               }

               var2 = (BeanInfo)var1.next();
               var3 = (EnvironmentBuilder)this.eBuilderMap.get(var2.getEJBName());
            } while(var3 == null);

            LinkedList var4 = new LinkedList();
            LinkedList var5 = new LinkedList();
            LinkedList var6 = new LinkedList();
            LinkedList var7 = new LinkedList();
            String var8 = var2.getEJBName();
            List var9 = this.dinfo.getInterceptorBeans(var8);
            Iterator var10 = var9.iterator();

            while(var10.hasNext()) {
               InterceptorBean var11 = (InterceptorBean)var10.next();
               var4.addAll(Arrays.asList(var11.getEnvEntries()));
               var5.addAll(Arrays.asList(var11.getResourceEnvRefs()));
               var6.addAll(Arrays.asList(var11.getPersistenceContextRefs()));
               var7.addAll(Arrays.asList(var11.getPersistenceUnitRefs()));
            }

            try {
               var3.getContext().unbind("ejb-info");
            } catch (NamingException var12) {
            }

            var4.addAll(var2.getAllEnvironmentEntries());
            var3.removeEnvironmentEntries(var4);
            var5.addAll(var2.getAllResourceEnvReferences());
            var3.removeResourceEnvReferences(var5);
            var6.addAll(Arrays.asList(var2.getPersistenceContextRefs()));
            var3.removePersistenceContextRefs((PersistenceContextRefBean[])var6.toArray(new PersistenceContextRefBean[0]));
            var7.addAll(Arrays.asList(var2.getPersistenceUnitRefs()));
            var3.removePersistenceUnitRefs((PersistenceUnitRefBean[])var7.toArray(new PersistenceUnitRefBean[0]));
            var3.removeTimerServiceBinding();
            var3.removeStandardEntries();
            this.eBuilderMap.remove(var2.getEJBName());
         }
      }
   }

   private Getopt2 makeGetopt2() {
      Getopt2 var1 = new Getopt2();
      var1.addFlag("nodeploy", "Do not unpack jar files into the target dir.");
      var1.setUsageArgs("<source jar file> <target directory or jar file>");
      var1.addFlag("idl", "Generate idl for remote interfaces");
      var1.addFlag("idlOverwrite", "Always overwrite existing IDL files");
      var1.addFlag("idlVerbose", "Display verbose information for IDL generation");
      var1.addFlag("idlNoValueTypes", "Do not generate valuetypes and methods/attributes that contain them.");
      var1.markPrivate("idlNoValueTypes");
      var1.addOption("idlDirectory", "dir", "Specify the directory where IDL files will be created (default : current directory)");
      var1.addFlag("iiop", "Generate CORBA stubs");
      var1.addOption("iiopDirectory", "dir", "Specify the directory where IIOP stub files will be written (default : current directory)");
      var1.addFlag("forceGeneration", "Force generation of wrapper classes.  Without this flag the classes may not be regenerated if it is determined to be unnecessary.");
      var1.addFlag("convertDDs", "Convert old deployment descriptors to new ones");
      var1.addFlag("disableHotCodeGen", "Generate ejb stub and skel as part of ejbc. Avoid HotCodeGen to have better performance.");
      new CompilerInvoker(var1);
      var1.markPrivate("nowrite");
      var1.markPrivate("commentary");
      var1.markPrivate("nodeploy");
      var1.markPrivate("compilerclass");
      var1.markPrivate("O");
      var1.markPrivate("d");
      var1.markPrivate("J");
      return var1;
   }

   private String makeClasspath(GenericClassLoader var1, EJBComponentMBean var2) {
      String var3 = ReadConfig.getJavaCompilerPreClassPath(var2);
      String var4 = ReadConfig.getJavaCompilerPostClassPath(var2);
      if (var3 == null && var4 == null) {
         return null;
      } else {
         StringBuffer var5 = new StringBuffer();
         if (null != var3) {
            var5.append(var3);
            var5.append(separator);
         }

         var5.append(var1.getClassPath());
         if (null != var4) {
            var5.append(separator);
            var5.append(var4);
         }

         return var5.toString();
      }
   }

   private void runEJBC(GenericClassLoader var1, Getopt2 var2, EJBComponentMBean var3, VirtualJarFile var4, Collection var5) throws ErrorCollectionException {
      EJBC var6 = EJBCFactory.createEJBC(var2);
      var6.compileEJB(var1, var3, var4, this.vHelper, var5);
   }

   private void unregisterMBeans() {
      if (this.compRTMBean != null) {
         try {
            this.compRTMBean.unregisterDependents();
         } catch (ManagementException var2) {
            if (debugLogger.isDebugEnabled()) {
               debug("Couldn't unregister MBeans");
            }

            EJBLogger.logStackTraceAndMessage("Couldn't unregister MBeans", var2);
         }
      }

   }

   private File getEJBCompilerCache() {
      File var1 = getEjbCompilerCacheDir();
      String var2 = this.ejbModule.getApplicationName();
      String var3 = this.ejbModule.getURI();
      return new File(var1, StringUtils.mangle(var2 + "_" + var3));
   }

   public static File getEjbCompilerCacheDir() {
      StringBuffer var0 = new StringBuffer();
      String var1 = ManagementService.getRuntimeAccess(kernelId).getServerName();
      String var2 = System.getProperty("weblogic.j2ee.application.tmpDir");
      if (var2 != null) {
         var2 = var2 + File.separator + "servers" + File.separator + var1;
      } else {
         var2 = DomainDir.getCacheDirForServer(var1);
      }

      var0.append(var2);
      var0.append(File.separator);
      var0.append("EJBCompilerCache");
      return new File(var0.toString());
   }

   private Properties getFileHash(File var1) {
      File var2 = new File(var1, "_WL_GENERATED");
      if (!var2.exists()) {
         return null;
      } else {
         FileInputStream var3 = null;

         Properties var5;
         try {
            Properties var4 = new Properties();
            var3 = new FileInputStream(var2);
            var4.load(var3);
            var5 = var4;
            return var5;
         } catch (IOException var10) {
            EJBLogger.logExceptionLoadingTimestamp(var10);
            var5 = null;
         } finally {
            closeStream(var3);
         }

         return var5;
      }
   }

   private Properties getFileHash(VirtualJarFile var1) {
      ZipEntry var2 = var1.getEntry("_WL_GENERATED");
      if (var2 == null) {
         return null;
      } else {
         InputStream var3 = null;

         Properties var5;
         try {
            Properties var4 = new Properties();
            var3 = var1.getInputStream(var2);
            var4.load(var3);
            var5 = var4;
            return var5;
         } catch (IOException var10) {
            EJBLogger.logExceptionLoadingTimestamp(var10);
            var5 = null;
         } finally {
            closeStream(var3);
         }

         return var5;
      }
   }

   private void dumpProps(Properties var1) {
      Enumeration var2 = var1.propertyNames();

      while(var2.hasMoreElements()) {
         String var3 = (String)var2.nextElement();
         debug("Prop: " + var3 + "value is: " + var1.getProperty(var3));
      }

   }

   private void compileIfNecessary(VirtualJarFile var1, GenericClassLoader var2) throws EJBDeploymentException, ModuleException {
      try {
         this.vHelper = new VersionHelperImpl(this.dinfo, var1, (Getopt2)null);
      } catch (ClassNotFoundException var11) {
         throw new EJBDeploymentException(this.ejbModule.getURI(), this.ejbModule.getURI(), var11);
      }

      Properties var3 = this.getFileHash(var1);
      if (var3 != null) {
         this.vHelper.removeCompilerOptions(var3);
      }

      boolean var4 = ReadConfig.getForceGeneration(this.ejbModule.getEJBComponent());
      Collection var5 = null;
      if (var4) {
         if (debugLogger.isDebugEnabled()) {
            debug("force-generation has been enabled.");
         }

         var5 = this.dinfo.getBeanInfos();
      } else {
         var5 = this.vHelper.needsRecompile(var3);
      }

      if (!var5.isEmpty()) {
         File var6 = this.getEJBCompilerCache();
         Properties var7;
         WLDeploymentException var8;
         Loggable var12;
         if (var6.exists()) {
            if (!var6.isDirectory()) {
               var12 = EJBLogger.logUnableToCreateTempDirLoggable(var6.getAbsolutePath());
               var8 = new WLDeploymentException(var12.getMessage());
               throw new EJBDeploymentException(this.ejbModule.getURI(), this.ejbModule.getURI(), var8);
            }

            if (!var4) {
               var7 = this.getFileHash(var6);
               if (var7 != null) {
                  this.vHelper.removeCompilerOptions(var7);
                  var5 = this.vHelper.needsRecompile(var7);
               }
            }
         } else if (!var6.mkdirs()) {
            var12 = EJBLogger.logUnableToCreateTempDirLoggable(var6.getAbsolutePath());
            var8 = new WLDeploymentException(var12.getMessage());
            throw new EJBDeploymentException(this.ejbModule.getURI(), this.ejbModule.getURI(), var8);
         }

         var7 = null;

         DirectoryClassFinder var13;
         try {
            var13 = new DirectoryClassFinder(var6);
         } catch (IOException var10) {
            throw new EJBDeploymentException(this.ejbModule.getURI(), this.ejbModule.getURI(), var10);
         }

         var2.addClassFinderFirst(var13);
         if (!var5.isEmpty()) {
            this.compileJar(var6, var5, var2, var1);
         }

         try {
            var13.indexFiles();
         } catch (IOException var9) {
            throw new EJBDeploymentException(this.ejbModule.getURI(), this.ejbModule.getURI(), var9);
         }
      }

   }

   private boolean perhapsUseDisableHotCodeGen(VirtualJarFile var1) {
      boolean var2 = false;
      Iterator var3 = var1.entries();

      while(var3.hasNext()) {
         ZipEntry var4 = (ZipEntry)var3.next();
         if (var4.getName().indexOf("_WLSkel") != -1) {
            var2 = true;
            break;
         }
      }

      return var2;
   }

   private void compileJar(File var1, Collection var2, GenericClassLoader var3, VirtualJarFile var4) throws EJBDeploymentException, ModuleException {
      FileFilter var5 = new FileFilter() {
         public boolean accept(File var1) {
            return var1.getName().endsWith(".java");
         }
      };
      FileUtils.remove(var1, var5);
      Getopt2 var6 = this.makeGetopt2();
      EJBComponentMBean var7 = this.ejbModule.getEJBComponent();
      String var8 = ReadConfig.getExtraEjbcOptions(var7);
      this.outputDirPath = var1.getAbsolutePath();
      if (var8 != null && var8.trim().length() > 0) {
         try {
            var6.grok(StringUtils.splitCompletely(var8, " "));
         } catch (IllegalArgumentException var12) {
            throw new EJBDeploymentException(this.ejbModule.getURI(), this.ejbModule.getURI(), var12);
         }
      }

      try {
         var6.setOption("compiler", ReadConfig.getJavaCompiler(var7));
         String var9 = var1.getPath();
         var6.setOption("d", var9);
         String var10 = this.makeClasspath(var3, var7);
         if (var10 != null) {
            var6.setOption("classpath", var10);
         }

         var6.setFlag("nowarn", true);
         var6.setFlag("noexit", true);
         if (var7 != null) {
            var6.setFlag("keepgenerated", var7.getKeepGenerated());
            var6.setFlag("forceGeneration", ReadConfig.getForceGeneration(var7));
         }

         if (this.perhapsUseDisableHotCodeGen(var4)) {
            if (debugLogger.isDebugEnabled()) {
               debug("Setting disableHotCodeGen to true, as the existing jar contained pre-generated skeletons");
            }

            var6.setFlag("disableHotCodeGen", true);
         }
      } catch (BadOptionException var13) {
         throw new AssertionError(var13);
      }

      try {
         if (var6.hasOption("keepgenerated")) {
            EJBLogger.logEJBBeingRecompiledOnServerKeepgenerated(this.ejbModule.getURI(), var1.getAbsolutePath());
         } else {
            EJBLogger.logEJBBeingRecompiledOnServer(this.ejbModule.getURI());
         }

         this.runEJBC(var3, var6, var7, var4, var2);
      } catch (ErrorCollectionException var11) {
         throw new EJBDeploymentException(this.ejbModule.getURI(), this.ejbModule.getURI(), var11);
      }
   }

   private static void closeStream(InputStream var0) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (IOException var2) {
      }

   }

   private void registerSupplementalPolicyObject(EjbDescriptorBean var1) {
      WeblogicEjbJarBean var2 = var1.getWeblogicEjbJarBean();
      SecurityPermissionBean var3 = var2.getSecurityPermission();
      if (var3 != null) {
         this.securitySpec = var3.getSecurityPermissionSpec();
      }

      String var4 = null;
      String[] var5 = this.getDeploymentPaths();
      if (this.jarFilePath != null) {
         StringBuilder var6 = new StringBuilder("permission java.io.FilePermission \"");
         var6.append(var5[0]);
         var6.append("/-\", \"read\"");
         var4 = this.insertPermission(var6.toString(), this.securitySpec);
      } else {
         var4 = this.securitySpec;
      }

      if (debugLogger.isDebugEnabled()) {
         debug("  setting J2EE Sandbox Security: component Name: '" + this.jarFilePath + "',  securitySpec: '" + var4 + "'");
      }

      RuntimeHelper.registerSupplementalPolicyObject(var5, var4);
   }

   private void removeSupplementalPolicyObject() {
      if (debugLogger.isDebugEnabled()) {
         debug("  removing J2EE Sandbox Security: component Name: '" + this.jarFilePath + "'");
      }

      RuntimeHelper.removeSupplementalPolicyObject(this.getDeploymentPaths());
   }

   private String insertPermission(String var1, String var2) {
      StringBuilder var3 = new StringBuilder();
      if (var2 != null && var2.trim().length() != 0) {
         StringBuilder var4 = new StringBuilder(var2);
         int var5 = var4.indexOf("}");
         if (var5 > 0) {
            var4.insert(var5, var1 + ";\n");
         }

         var3 = var4;
      } else {
         var3.append("grant {\n");
         var3.append(var1);
         var3.append(";\n};");
      }

      return var3.toString();
   }

   private void deployAllPolicies() throws Exception {
      this.helper.deployAllPolicies();
   }

   private void deployRoles() throws Exception {
      SecurityRoleMapping var1 = this.dinfo.getDeploymentRoles();
      Collection var2 = var1.getSecurityRoleNames();
      if (var2.size() > 0) {
         this.helper.deployRoles(this.dinfo, var1);
      }
   }

   private void unDeployRoles() {
      if (this.helper != null) {
         SecurityRoleMapping var1 = this.dinfo.getDeploymentRoles();
         Collection var2 = var1.getSecurityRoleNames();
         if (var2.size() > 0) {
            this.helper.unDeployRoles(this.dinfo, var1);
         }
      }
   }

   private String getEJBName(BeanInfo var1, EjbDescriptorBean var2) {
      return var1 == null ? var2.getJarFileName() : var1.getEJBName();
   }

   public Collection getBeanInfos() {
      return this.dinfo.getBeanInfos();
   }

   private void setupMessageDestinations(EjbDescriptorBean var1, Context var2) throws EJBDeploymentException {
      Context var3 = null;

      try {
         var3 = (Context)var2.lookup("messageDestination");
      } catch (NameNotFoundException var10) {
         try {
            var3 = var2.createSubcontext("messageDestination");
         } catch (NamingException var9) {
            throw new AssertionError(var9);
         }
      } catch (NamingException var11) {
         throw new AssertionError(var11);
      }

      Collection var4 = this.dinfo.getMessageDestinationDescriptors();
      String var5 = J2EEUtils.normalizeJarName(this.ejbModule.getURI());

      try {
         Iterator var6 = var4.iterator();

         while(var6.hasNext()) {
            MessageDestinationDescriptorBean var7 = (MessageDestinationDescriptorBean)var6.next();
            String var8 = J2EEUtils.normalizeJNDIName(var7.getMessageDestinationName());
            var3.bind(var5 + "#" + var8, var7);
         }

      } catch (NamingException var12) {
         throw new EJBDeploymentException(var1.getJarFileName(), this.ejbModule.getURI(), var12);
      }
   }

   final void setupBeanInfos(EjbDescriptorBean var1, Context var2, EjbComponentCreator var3) throws EJBDeploymentException {
      BeanInfo var4 = null;

      try {
         Iterator var5 = this.dinfo.getBeanInfos().iterator();
         ArrayList var6 = new ArrayList();
         HashMap var7 = new HashMap();
         HashMap var8 = new HashMap();

         BeanManager var10;
         while(var5.hasNext()) {
            var4 = (BeanInfo)var5.next();
            var4.setEjbComponentCreator(var3);
            var4.setRuntimeHelper(this.helper);
            if (!(var4 instanceof MessageDrivenBeanInfo)) {
               var4.setupBeanManager(this.compRTMBean);
            } else {
               ((MessageDrivenBeanInfo)var4).setEJBComponentRuntime(this.compRTMBean);
               this.deployedManagers.hasMDBs = true;
            }

            if (var4 instanceof EntityBeanInfo) {
               EntityBeanInfo var9 = (EntityBeanInfo)var4;
               var10 = var9.getBeanManager();
               if (!var9.getIsBeanManagedPersistence() && var9.getCMPInfo().uses20CMP()) {
                  var6.add(var10);
                  var7.put(var9.getEJBName(), var10);
               }
            }

            Context var19 = this.setupEnvironmentFor(var4, var2);
            var8.put(var4, var19);

            try {
               EJBRuntimeUtils.pushEnvironment(var19);
               var4.prepare(this.appCtx, this.dinfo);
            } finally {
               EJBRuntimeUtils.popEnvironment();
            }

            if (var4 instanceof ClientDrivenBeanInfo) {
               ClientDrivenBeanInfo var21 = (ClientDrivenBeanInfo)var4;
               var21.bindEJBRefs(var19);
            }
         }

         this.deployAllPolicies();
         this.deployRoles();
         Iterator var20 = var6.iterator();

         while(var20.hasNext()) {
            var10 = (BeanManager)var20.next();
            if (var10 instanceof BaseEntityManager) {
               ((BaseEntityManager)var10).setBMMap(var7);
            }
         }

      } catch (WLDeploymentException var17) {
         throw new EJBDeploymentException(this.getEJBName(var4, var1), this.ejbModule.getURI(), var17);
      } catch (Throwable var18) {
         throw new EJBDeploymentException(this.getEJBName(var4, var1), this.ejbModule.getURI(), var18);
      }
   }

   final Context setupEnvironmentFor(BeanInfo var1, Context var2) throws WLDeploymentException {
      if (debugLogger.isDebugEnabled()) {
         debug("==================SETTING ENV. FOR EJB====================");
         debug("EJB: " + var1.getDisplayName());
      }

      String var3 = J2EEUtils.normalizeJarName(this.ejbModule.getURI());
      Context var4 = null;

      Context var5;
      try {
         var5 = (Context)var2.lookup("ejb");
         String var6 = var1.getEJBName();
         var4 = var5.createSubcontext(var3 + "#" + var6);
         var4.bind("app", var2);
      } catch (NamingException var9) {
         throw new AssertionError(var9);
      }

      Loggable var10;
      try {
         var5 = this.setupEnvironmentContext(var4, var1, this.appCtx.getApplicationId(), this.ejbModule.getName());
         return var5;
      } catch (NamingException var7) {
         var10 = EJBLogger.logFailureWhileCreatingCompEnvLoggable(var7);
         throw new WLDeploymentException(var10.getMessage(), var7);
      } catch (EnvironmentException var8) {
         var10 = EJBLogger.logFailureWhileCreatingCompEnvLoggable(var8);
         throw new WLDeploymentException(var10.getMessage(), var8);
      }
   }

   final String needsRecompile(List var1, ClassLoader var2) throws ClassNotFoundException {
      Iterator var3 = var1.iterator();

      String var4;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var4 = (String)var3.next();
      } while(!this.vHelper.needsRecompile(var4, var2));

      return var4;
   }

   final void updateImplClassLoader(String var1) throws WLDeploymentException {
      BeanInfo var2 = this.dinfo.getBeanInfo(var1);
      var2.updateImplClassLoader();
   }

   final void prepare(VirtualJarFile var1, ClassLoader var2, EjbDescriptorBean var3, Context var4, Map var5, Map var6) throws EJBDeploymentException, ModuleException {
      assert var2 != null;

      if (var1 != null) {
         this.jarFilePath = var1.getName();
      }

      GenericClassLoader var7 = (GenericClassLoader)var2;
      this.cacheMap = var5;
      this.queryCacheMap = var6;
      EJBLogger.logDeploying(this.ejbModule.getURI());

      try {
         this.dinfo = new MBeanDeploymentInfoImpl(var3, var7, this.ejbModule.getName(), this.ejbModule.getURI(), var1, this.appCtx);
      } catch (Exception var16) {
         throw new EJBDeploymentException(var3.getJarFileName(), this.ejbModule.getURI(), var16);
      }

      boolean var8 = false;
      Iterator var9 = this.dinfo.getBeanInfos().iterator();

      while(var9.hasNext()) {
         BeanInfo var10 = (BeanInfo)var9.next();
         if (var10 instanceof EntityBeanInfo) {
            EntityBeanInfo var11 = (EntityBeanInfo)var10;
            if (!var11.getIsBeanManagedPersistence()) {
               try {
                  var11.getCMPInfo().setup(new File(this.dinfo.getJarFileName()), (Getopt2)null, this.dinfo.getVirtualJarFile());
               } catch (WLDeploymentException var15) {
                  throw new EJBDeploymentException(var11.getEJBName(), this.ejbModule.getURI(), var15);
               }
            }
         } else if (var10 instanceof Ejb3SessionBeanInfo) {
            var8 = true;
         }
      }

      this.pitchforkContext = this.dinfo.getPitchforkContext();
      if (var8) {
         this.pitchforkContext.getPitchforkUtils().acceptClassLoader(var2);
      }

      if (var1 != null) {
         this.compileIfNecessary(var1, var7);
      }

      try {
         this.helper = new RuntimeHelper(this.dinfo, this.appCtx);
         this.registerSupplementalPolicyObject(var3);
      } catch (Exception var14) {
         throw new EJBDeploymentException(var3.getJarFileName(), this.ejbModule.getURI(), var14);
      }

      EjbComponentCreatorImpl var17 = new EjbComponentCreatorImpl(this.pitchforkContext);

      try {
         var17.initialize(this.dinfo, var7);
      } catch (DeploymentException var13) {
         throw new EJBDeploymentException(var3.getJarFileName(), this.ejbModule.getURI(), var13);
      }

      this.setupBeanInfos(var3, var4, var17);
      if (var1 != null) {
         Iterator var18 = this.deployListeners.iterator();

         while(var18.hasNext()) {
            EJBDeployListener var19 = (EJBDeployListener)var18.next();
            var19.prepare(this.dinfo, var3, this.appCtx);
         }
      }

      this.setupMessageDestinations(var3, var4);
      this.firstActivate = true;
   }

   final void activate(EjbDescriptorBean var1, ClassLoader var2, Context var3) throws EJBDeploymentException {
      Context var4 = null;
      Context var5 = null;
      BeanInfo var6 = null;

      try {
         String var7 = J2EEUtils.normalizeJarName(this.ejbModule.getURI());
         Context var8 = (Context)var3.lookup("ejb");
         Iterator var9 = this.dinfo.getBeanInfos().iterator();

         while(var9.hasNext()) {
            var6 = (BeanInfo)var9.next();
            String var10 = var6.getEJBName();
            var5 = (Context)var8.lookup(var7 + "#" + var10);
            Environment var11 = new Environment();
            var11.setCreateIntermediateContexts(true);
            var4 = var11.getInitialContext();
            EnvironmentBuilder var12 = (EnvironmentBuilder)this.eBuilderMap.get(var10);
            Context var13 = var12.getContext();

            try {
               EJBRuntimeUtils.pushEnvironment(var13);
               var6.activate(var4, this.cacheMap, this.queryCacheMap, this.dinfo, var13);
            } finally {
               EJBRuntimeUtils.popEnvironment();
               this.deployedManagers.addManager(var6);
            }

            if (var6 instanceof Ejb3SessionBeanInfo) {
               ((Ejb3SessionBeanInfo)var6).setPersistenceUnitRegistry(this.ejbModule.getPersistenceUnitRegistry());
               Map var14 = ((Ejb3SessionBeanInfo)var6).getRemoteBusinessJNDINames();
               if (var14 != null && var14.size() > 0) {
                  EJBLogger.logDeployedWithEJBName(var6.getDisplayName());
                  Iterator var15 = var14.entrySet().iterator();

                  while(var15.hasNext()) {
                     Map.Entry var16 = (Map.Entry)var15.next();
                     Object var17 = var16.getKey();
                     Object var18 = var16.getValue();
                     if (var17 instanceof Class && var18 instanceof String) {
                        EJBLogger.logJNDINamesMap(((Class)var17).getCanonicalName(), (String)var18);
                     }
                  }
               }
            }

            if (var6.getJNDIName() != null) {
               if (!(var6 instanceof MessageDrivenBeanInfo)) {
                  EJBLogger.logDeployedWithJNDIName(var6.getJNDIName().toString());
               } else {
                  EJBLogger.logDeployedMDB(var10);
               }
            }

            if (var6 instanceof ClientDrivenBeanInfo) {
               ClientDrivenBeanInfo var29 = (ClientDrivenBeanInfo)var6;
               if (var29.getLocalJNDIName() != null) {
                  EJBLogger.logDeployedWithJNDIName(var29.getLocalJNDIName().toString());
               }
            }
         }

         DataBeanMapper.addBeans(this, this.dinfo.getBeanInfos());
         if (this.firstActivate) {
            var9 = this.dinfo.getBeanInfos().iterator();

            EntityBeanInfo var25;
            CMPInfo var27;
            while(var9.hasNext()) {
               var6 = (BeanInfo)var9.next();
               if (var6 instanceof EntityBeanInfo) {
                  var25 = (EntityBeanInfo)var6;
                  var27 = var25.getCMPInfo();
                  if (var27 != null) {
                     var27.setupParentBeanManagers();
                     var27.setupMNBeanManagers();
                  }
               }
            }

            var9 = this.dinfo.getBeanInfos().iterator();

            while(var9.hasNext()) {
               var6 = (BeanInfo)var9.next();
               if (var6 instanceof EntityBeanInfo) {
                  var25 = (EntityBeanInfo)var6;
                  var27 = var25.getCMPInfo();
                  if (var27 != null) {
                     var27.setCycleExists();
                  }
               }
            }

            this.firstActivate = false;
         }

         this.helper.activate();
         this.activateEnvironment();
         this.setIsDeployed(true);
         Iterator var26 = this.deployListeners.iterator();

         while(var26.hasNext()) {
            EJBDeployListener var28 = (EJBDeployListener)var26.next();
            var28.activate(var1, var2, var3);
         }

      } catch (WLDeploymentException var23) {
         throw new EJBDeploymentException(this.getEJBName(var6, var1), this.ejbModule.getURI(), var23);
      } catch (Throwable var24) {
         throw new EJBDeploymentException(this.getEJBName(var6, var1), this.ejbModule.getURI(), var24);
      }
   }

   protected void deployMessageDrivenBeansUsingModuleCL() throws Exception {
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();
      if (this.moduleCL != null) {
         Thread.currentThread().setContextClassLoader(this.moduleCL);
      }

      try {
         this.deployMessageDrivenBeans();
      } finally {
         if (var1 != null) {
            Thread.currentThread().setContextClassLoader(var1);
         }

         if (this.moduleCL != null) {
            this.moduleCL = null;
         }

      }

   }

   final void start(boolean var1) throws EJBDeploymentException {
      this.initializePools();
      boolean var2 = this.isServerStarted();
      if (this.deployedManagers.hasMDBs()) {
         MDBService var3 = MDBServiceActivator.INSTANCE.getMDBService();
         if (var1 && !var2 && var3 != null && var3.addMDBStarter(this)) {
            this.moduleCL = Thread.currentThread().getContextClassLoader();
            if (debugLogger.isDebugEnabled()) {
               debug("MDB is added to MDBService to start.  Server Status : " + T3Srvr.getT3Srvr().getState());
            }
         } else {
            try {
               this.deployMessageDrivenBeans();
            } catch (WLDeploymentException var10) {
               throw new EJBDeploymentException(this.ejbModule.getURI(), this.ejbModule.getURI(), var10);
            } catch (Throwable var11) {
               throw new EJBDeploymentException(this.ejbModule.getURI(), this.ejbModule.getURI(), var11);
            }
         }
      }

      HashSet var12 = new HashSet();
      var12.addAll(this.deployedManagers.getNoneMdbManagers());
      var12.addAll(this.deployedManagers.getMdbManagers());
      this.deployedManagers.removeMdbManagers();
      Iterator var4 = var12.iterator();

      while(var4.hasNext()) {
         BeanManager var5 = (BeanManager)var4.next();
         TimerManager var6 = var5.getTimerManager();
         if (var6 != null) {
            try {
               var6.perhapsStart();
            } catch (Exception var9) {
               throw new EJBDeploymentException(var5.getBeanInfo().getEJBName(), this.ejbModule.getURI(), var9);
            }
         }
      }

      if (!this.dinfo.isDynamicQueriesEnabled()) {
         var4 = this.dinfo.getBeanInfos().iterator();

         while(var4.hasNext()) {
            BeanInfo var13 = (BeanInfo)var4.next();
            if (var13 instanceof EntityBeanInfo) {
               EntityBeanInfo var14 = (EntityBeanInfo)var13;
               PersistenceManager var7 = var14.getPersistenceManager();
               if (var7 != null && var7 instanceof RDBMSPersistenceManager) {
                  RDBMSPersistenceManager var8 = (RDBMSPersistenceManager)var7;
                  var8.cleanup();
               }
            }
         }
      }

   }

   public void invalidate(InvalidationMessage var1) {
      String var2 = var1.getEjbName();
      BeanInfo var3 = this.dinfo.getBeanInfo(var2);

      assert var3 instanceof EntityBeanInfo;

      EntityBeanInfo var4 = (EntityBeanInfo)var3;
      InvalidationBeanManager var5 = (InvalidationBeanManager)var4.getBeanManager();
      if (var1.getPrimaryKey() != null) {
         var5.invalidateLocalServer((Object)null, (Object)var1.getPrimaryKey());
      } else if (var1.getPrimaryKeys() != null) {
         var5.invalidateLocalServer((Object)null, (Collection)var1.getPrimaryKeys());
      } else {
         var5.invalidateAllLocalServer((Object)null);
      }

   }

   private boolean isServerStarted() {
      return T3Srvr.getT3Srvr().getRunState() == 2;
   }

   final void rollback(Context var1) throws NamingException {
      this.unprepare();
      this.unregisterMBeans();
      if (this.dinfo != null) {
         Collection var2 = this.dinfo.getBeanInfos();
         if (var2 != null) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               BeanInfo var4 = (BeanInfo)var3.next();
               this.cleanupAppContext(var4, var1);
            }
         }
      }

      this.removeSupplementalPolicyObject();
      this.unsetupEnvironmentContext();
      Iterator var5 = this.deployListeners.iterator();

      while(var5.hasNext()) {
         EJBDeployListener var6 = (EJBDeployListener)var5.next();
         var6.unprepare();
      }

      if (this.pitchforkContext != null) {
         this.pitchforkContext.getPitchforkUtils().clearClassLoader(this.appCtx.getAppClassLoader());
      }

      this.eBuilderMap.clear();
      this.cacheMap = null;
   }

   private void initializePools() {
      BeanInfo var1 = null;
      Iterator var2 = this.dinfo.getBeanInfos().iterator();

      while(var2.hasNext()) {
         var1 = (BeanInfo)var2.next();
         if (var1 instanceof SessionBeanInfo) {
            SessionBeanInfo var3 = (SessionBeanInfo)var1;
            if (!var3.isStateful()) {
               StatelessManager var4 = (StatelessManager)var3.getBeanManager();

               try {
                  var4.initializePool();
               } catch (Exception var6) {
                  EJBLogger.logErrorCreatingFreepool(var3.getDisplayName(), var6);
               }
            }
         } else if (var1 instanceof EntityBeanInfo) {
            EntityBeanInfo var8 = (EntityBeanInfo)var1;
            BeanManager var9 = var8.getBeanManager();
            if (var9 instanceof BaseEntityManager) {
               try {
                  ((BaseEntityManager)var9).initializePool();
               } catch (Exception var7) {
                  EJBLogger.logErrorCreatingFreepool(var8.getDisplayName(), var7);
               }
            }
         }
      }

   }

   protected void deployMessageDrivenBeans() throws Exception {
      BeanInfo var1 = null;
      if (this.dinfo == null) {
         EJBLogger.logErrorOnStartMDBs(this.name);
      } else {
         Iterator var2 = this.dinfo.getBeanInfos().iterator();

         while(var2.hasNext()) {
            var1 = (BeanInfo)var2.next();
            if (var1 instanceof MessageDrivenBeanInfo) {
               MessageDrivenBeanInfo var3 = (MessageDrivenBeanInfo)var1;
               var3.deployMessageDrivenBeans();
            }
         }

      }
   }

   final void activateEnvironment() throws EnvironmentException, NamingException {
      Iterator var1 = this.dinfo.getBeanInfos().iterator();

      while(var1.hasNext()) {
         BeanInfo var2 = (BeanInfo)var1.next();
         EnvironmentBuilder var3 = (EnvironmentBuilder)this.eBuilderMap.get(var2.getEJBName());
         Context var4 = var3.getContext();

         try {
            EJBRuntimeUtils.pushEnvironment(var4);
            LinkedList var5 = new LinkedList();
            LinkedList var6 = new LinkedList();
            LinkedList var7 = new LinkedList();
            LinkedList var8 = new LinkedList();
            LinkedList var9 = new LinkedList();
            String var10 = var2.getEJBName();
            List var11 = this.dinfo.getInterceptorBeans(var10);
            Iterator var12 = var11.iterator();

            while(var12.hasNext()) {
               InterceptorBean var13 = (InterceptorBean)var12.next();
               var5.addAll(Arrays.asList(var13.getResourceRefs()));
               var6.addAll(Arrays.asList(var13.getEjbRefs()));
               var7.addAll(Arrays.asList(var13.getEjbLocalRefs()));
               var8.addAll(Arrays.asList(var13.getMessageDestinationRefs()));
               var9.addAll(Arrays.asList(var13.getServiceRefs()));
            }

            var5.addAll(var2.getAllResourceReferences());
            var3.addResourceReferences(var5, var2.getAllResourceReferenceJNDINames(this.appCtx == null ? null : this.appCtx.getApplicationId()), var2);
            var6.addAll(var2.getAllEJBReferences());
            var3.addEJBReferences(var6, var2.getAllEJBReferenceJNDINames(), this.ejbModule.getURI());
            var7.addAll(var2.getAllEJBLocalReferences());
            var3.addEJBLocalReferences(var7, var2.getAllEJBLocalReferenceJNDINames(), this.ejbModule.getURI());
            var8.addAll(var2.getAllMessageDestinationReferences());
            var3.addMessageDestinationReferences(var8, this.ejbModule.getURI());
            Thread var25 = Thread.currentThread();
            ClassLoader var14 = var25.getContextClassLoader();

            try {
               var25.setContextClassLoader(var2.getClassLoader());
               var9.addAll(var2.getAllServiceReferences());
               var3.addServiceReferences(var9, var2.getAllServiceReferenceDescriptions(), (ServletContext)null, this.ejbModule.getURI());
            } finally {
               var25.setContextClassLoader(var14);
            }
         } finally {
            EJBRuntimeUtils.popEnvironment();
         }
      }

   }

   final void deactivateEnvironment(BeanInfo var1, EnvironmentBuilder var2) throws NamingException {
      if (var2 != null) {
         LinkedList var3 = new LinkedList();
         LinkedList var4 = new LinkedList();
         LinkedList var5 = new LinkedList();
         LinkedList var6 = new LinkedList();
         String var7 = var1.getEJBName();
         List var8 = this.dinfo.getInterceptorBeans(var7);
         Iterator var9 = var8.iterator();

         while(var9.hasNext()) {
            InterceptorBean var10 = (InterceptorBean)var9.next();
            var3.addAll(Arrays.asList(var10.getResourceRefs()));
            var4.addAll(Arrays.asList(var10.getEjbRefs()));
            var4.addAll(Arrays.asList(var10.getEjbLocalRefs()));
            var5.addAll(Arrays.asList(var10.getMessageDestinationRefs()));
            var6.addAll(Arrays.asList(var10.getServiceRefs()));
         }

         var4.addAll(var1.getAllEJBReferences());
         var4.addAll(var1.getAllEJBLocalReferences());
         var2.removeEJBReferences(var4);
         var5.addAll(var1.getAllMessageDestinationReferences());
         var2.removeMessageDestinationReferences(var5);
         var6.addAll(var1.getAllServiceReferences());
         var2.removeServiceReferences(var6);
         var3.addAll(var1.getAllResourceReferences());
         var2.removeResourceReferences(var3);
      }

   }

   final void setIsDeployed(boolean var1) {
      if (this.deployedManagers != null) {
         Iterator var2 = this.deployedManagers.getNoneMdbManagers().iterator();

         BeanManager var3;
         while(var2.hasNext()) {
            var3 = (BeanManager)var2.next();
            var3.setIsDeployed(var1);
         }

         var2 = this.deployedManagers.getMdbManagers().iterator();

         while(var2.hasNext()) {
            var3 = (BeanManager)var2.next();
            var3.setIsDeployed(var1);
         }
      }

   }

   final void deactivate() {
      this.removeSupplementalPolicyObject();
      if (this.dinfo != null) {
         this.unDeployRoles();
      }

      if (this.helper != null) {
         this.helper.deactivate();
      }

      Iterator var1;
      BeanInfo var2;
      if (this.dinfo != null) {
         var1 = this.dinfo.getBeanInfos().iterator();

         while(var1.hasNext()) {
            var2 = (BeanInfo)var1.next();
            if (var2 instanceof MessageDrivenBeanInfo) {
               this.deployedManagers.addManager(var2);
            }
         }
      }

      var1 = this.deployedManagers.getMdbManagers().iterator();

      while(var1.hasNext()) {
         MessageDrivenManagerIntf var5 = (MessageDrivenManagerIntf)var1.next();
         var5.undeploy();
         if (var5.subscriptionDeletionRequired()) {
            this.subscriptionDeleteList.add(var5);
         }
      }

      this.setIsDeployed(false);
      if (this.dinfo != null) {
         var1 = this.dinfo.getBeanInfos().iterator();

         while(var1.hasNext()) {
            var2 = (BeanInfo)var1.next();

            try {
               EnvironmentBuilder var3 = (EnvironmentBuilder)this.eBuilderMap.get(var2.getEJBName());
               this.deactivateEnvironment(var2, var3);
            } catch (NamingException var4) {
            }
         }
      }

      if (this.deployedManagers != null) {
         var1 = this.deployedManagers.getNoneMdbManagers().iterator();

         while(var1.hasNext()) {
            BeanManager var6 = (BeanManager)var1.next();
            var6.undeploy();
         }

         this.deployedManagers.removeAll();
      }

      var1 = this.dinfo.getBeanInfos().iterator();

      while(var1.hasNext()) {
         var2 = (BeanInfo)var1.next();
         if (var2 instanceof MessageDrivenBeanInfo && ((MessageDrivenBeanInfo)var2).getIsWeblogicJMS()) {
            MessageDrivenBeanInfo var7 = (MessageDrivenBeanInfo)var2;
            var7.unRegister();
         }
      }

      this.unregisterMBeans();
      Iterator var8 = this.deployListeners.iterator();

      while(var8.hasNext()) {
         EJBDeployListener var9 = (EJBDeployListener)var8.next();
         var9.deactivate();
      }

   }

   public void unprepare() {
      if (this.dinfo != null) {
         Iterator var1 = this.dinfo.getBeanInfos().iterator();

         while(var1.hasNext()) {
            BeanInfo var2 = (BeanInfo)var1.next();
            var2.unprepare();
         }
      }

   }

   void remove() {
      Iterator var1 = this.subscriptionDeleteList.iterator();

      while(var1.hasNext()) {
         ((MessageDrivenManagerIntf)var1.next()).remove();
         var1.remove();
      }

   }

   public void removeEJBTimers() {
      if (this.dinfo != null) {
         Iterator var1 = this.dinfo.getBeanInfos().iterator();

         while(var1.hasNext()) {
            BeanInfo var2 = (BeanInfo)var1.next();
            if (var2.isTimerDriven()) {
               EJBTimerManagerFactory.removeAllTimers(var2);
            }
         }
      }

   }

   private void cleanupAppContext(BeanInfo var1, Context var2) throws NamingException {
      String var3 = J2EEUtils.normalizeJarName(this.ejbModule.getURI());
      Context var4 = null;

      try {
         var4 = (Context)var2.lookup("ejb");
      } catch (NamingException var9) {
         EJBLogger.logStackTrace(var9);
      }

      String var5 = var1.getEJBName();

      try {
         Context var6 = (Context)var4.lookup(var3 + "#" + var5);
         if (var1 instanceof ClientDrivenBeanInfo) {
            ClientDrivenBeanInfo var7 = (ClientDrivenBeanInfo)var1;
            var7.unbindEJBRefs(var6);
         }
      } catch (NameNotFoundException var8) {
      }

   }

   private String[] getDeploymentPaths() {
      String[] var1 = new String[]{this.getCanonicalPath(this.jarFilePath), null};
      if (this.outputDirPath != null) {
         var1[1] = this.getCanonicalPath(this.outputDirPath);
      } else {
         var1[1] = this.getEJBCompilerCache().getAbsolutePath();
      }

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null && File.separatorChar == '\\') {
            var1[var2] = var1[var2].replace(File.separatorChar, '/');
         }
      }

      return var1;
   }

   private String getCanonicalPath(String var1) {
      String var2 = null;
      if (var1 != null) {
         var2 = (new File(var1)).getAbsolutePath();
      }

      return var2;
   }

   public String getObjectSchemaName() {
      return this.objectSchemaName;
   }

   public void setObjectSchemaName(String var1) {
      this.objectSchemaName = var1;
   }

   public Map getDataBeansMap() {
      return this.dataBeansMap;
   }

   public void setDataBeansMap(Map var1) {
      this.dataBeansMap = var1;
   }

   private static void debug(String var0) {
      debugLogger.debug("[EJBDeployer] " + var0);
   }

   static {
      debugLogger = EJBDebugService.deploymentLogger;
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      separator = System.getProperty("path.separator");
   }

   private class DeployedManagers {
      private List<BeanManager> noneMdbManagers = new LinkedList();
      private List<MessageDrivenManagerIntf> mdbManagers = new LinkedList();
      private boolean hasMDBs = false;

      DeployedManagers() {
      }

      List<BeanManager> getNoneMdbManagers() {
         return this.noneMdbManagers;
      }

      List<MessageDrivenManagerIntf> getMdbManagers() {
         return this.mdbManagers;
      }

      void addManager(BeanInfo var1) {
         if (var1 instanceof MessageDrivenBeanInfo) {
            this.mdbManagers.addAll(((MessageDrivenBeanInfo)var1).getMDManagerList());
         } else {
            if (!(var1 instanceof ClientDrivenBeanInfo)) {
               throw new AssertionError("Unexpected BeanInfo type: " + var1.getClass().getName());
            }

            this.noneMdbManagers.add(var1.getBeanManager());
         }

      }

      void removeMdbManagers() {
         this.mdbManagers.clear();
      }

      void removeAll() {
         this.noneMdbManagers.clear();
         this.mdbManagers.clear();
      }

      boolean hasMDBs() {
         return this.hasMDBs;
      }
   }
}
