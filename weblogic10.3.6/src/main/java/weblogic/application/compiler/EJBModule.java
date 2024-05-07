package weblogic.application.compiler;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import javax.enterprise.deploy.shared.ModuleType;
import javax.xml.stream.XMLStreamException;
import weblogic.application.utils.PersistenceUtils;
import weblogic.deployment.PersistenceUnitViewer;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.ejb.container.cmp.rdbms.Deployer;
import weblogic.ejb.container.cmp.rdbms.RDBMSDescriptor;
import weblogic.ejb.container.cmp11.rdbms.RDBMSDeploymentInfo;
import weblogic.ejb.container.deployer.EJBDescriptorMBeanUtils;
import weblogic.ejb.spi.EJBC;
import weblogic.ejb.spi.EJBCFactory;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.EjbDescriptorFactory;
import weblogic.ejb20.cmp.rdbms.RDBMSException;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.EnterpriseBeansBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.PersistenceBean;
import weblogic.j2ee.descriptor.wl.PersistenceUseBean;
import weblogic.kernel.Kernel;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.compiler.jdt.JDTJavaCompilerFactory;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.wsee.deploy.WSEEDescriptor;
import weblogic.wsee.policy.deployment.WsPolicyDescriptor;
import weblogic.xml.process.ProcessorFactory;
import weblogic.xml.process.ProcessorFactoryException;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;

public class EJBModule extends EARModule {
   MultiClassFinder moduleClassFinder = new MultiClassFinder();
   EjbDescriptorBean desc;
   private WSEEModuleHelper wseeHelper;
   public static final String WSEE_EJB_URI_81 = "META-INF/web-services.xml";

   public EJBModule(String var1, String var2) {
      super(var1, var2);
   }

   public ClassFinder getClassFinder() {
      return this.moduleClassFinder;
   }

   public void initModuleClassLoader(CompilerCtx var1, GenericClassLoader var2) throws ToolFailureException {
      this.moduleClassFinder.addFinder(new ClasspathClassFinder2(this.getOutputDir().getPath()));
      if (this.isSplitDir(var1)) {
         File[] var3 = var1.getEar().getModuleRoots(this.getURI());

         for(int var4 = 0; var4 < var3.length; ++var4) {
            this.moduleClassFinder.addFinder(new ClasspathClassFinder2(var3[var4].getAbsolutePath()));
         }
      }

      if (var2 != null) {
         try {
            PersistenceUtils.addRootPersistenceJars(var1.getApplicationContext().getAppClassLoader(), var1.getApplicationContext().getApplicationId(), var1.getApplicationDD());
            PersistenceUnitViewer.ResourceViewer var6 = new PersistenceUnitViewer.ResourceViewer(var1.getApplicationContext().getAppClassLoader(), var1.getApplicationContext().getApplicationId(), var1.getConfigDir(), var1.getPlanBean());
            var6.loadDescriptors();
            var1.setPerViewer(var6);
         } catch (IOException var5) {
            throw new ToolFailureException("Unable to load persistence descriptors", var5);
         }

         var2.addClassFinder(this.moduleClassFinder);
         this.moduleClassLoader = new GenericClassLoader(var2);
      } else {
         this.moduleClassLoader = new GenericClassLoader(this.moduleClassFinder);
      }

      this.moduleClassLoader.setAnnotation(new Annotation(var1.getApplicationContext().getApplicationId(), this.getURI()));
   }

   public void compile(GenericClassLoader var1, CompilerCtx var2) throws ToolFailureException {
      if (var2.isVerbose() && !Kernel.isServer()) {
         System.setProperty("weblogic.debug.DebugEjbCompilation", "true");
      }

      try {
         AppcUtils.compileEJB(var1, this.getVirtualJarFile(), this.getAltDDFile(), var2.getConfigDir(), var2.getPlanBean(), this.getOutputDir(), this.getModuleValidationInfo(), var2.getOpts());
         if (var2.getModules().length == 1) {
            JDTJavaCompilerFactory.getInstance().resetCache(var1);
         }
      } catch (ErrorCollectionException var8) {
         throw new ToolFailureException(J2EELogger.logAppcErrorsEncounteredCompilingModuleLoggable(this.getURI(), var8.toString()).getMessage(), var8);
      }

      try {
         this.desc = EjbDescriptorFactory.createDescriptorFromJarFile(this.getVirtualJarFile(), this.getAltDDFile(), var2.getConfigDir(), var2.getPlanBean(), (String)null, this.getURI());
      } catch (XMLParsingException var4) {
         throw new ToolFailureException("Unable to parse EJB descriptor", var4);
      } catch (XMLProcessingException var5) {
         throw new ToolFailureException("Unable to parse EJB descriptor", var5);
      } catch (IOException var6) {
         throw new ToolFailureException("Unable to parse EJB descriptor", var6);
      } catch (XMLStreamException var7) {
         throw new ToolFailureException("Unable to parse EJB descriptor", var7);
      }

      if (var2.isWriteInferredDescriptors() && (this.desc.getEjbJarBean() == null || !this.desc.getEjbJarBean().isMetadataComplete())) {
         this.wseeHelper = new WSEEModuleHelper(var2, this.getVirtualJarFile(), this.getURI(), false);
         this.backupDescriptors();
         this.processAnnotations(var2);
         this.writeDescriptors(var2);
      }

   }

   public void cleanup() {
      super.cleanup();
      this.moduleClassFinder.close();
   }

   public void merge(CompilerCtx var1) throws ToolFailureException {
      try {
         this.desc = EjbDescriptorFactory.createDescriptorFromJarFile(this.getVirtualJarFile(), this.getAltDDFile(), var1.getConfigDir(), var1.getPlanBean(), (String)null, this.getURI());
         this.wseeHelper = new WSEEModuleHelper(var1, this.getVirtualJarFile(), this.getURI(), false);
         if (var1.isWriteInferredDescriptors() && (this.desc.getEjbJarBean() == null || !this.desc.getEjbJarBean().isMetadataComplete())) {
            this.backupDescriptors();
         }

         this.processAnnotations(var1);
         if (this.desc.getEjbJarBean() != null) {
            this.addRootBean("META-INF/ejb-jar.xml", (DescriptorBean)this.desc.getEjbJarBean());
         }

         if (this.desc.getWeblogicEjbJarBean() != null && !this.desc.isWeblogicEjbJarSynthetic()) {
            this.addRootBean("META-INF/weblogic-ejb-jar.xml", (DescriptorBean)this.desc.getWeblogicEjbJarBean());
         }

         PersistenceUnitViewer.EntryViewer var2 = new PersistenceUnitViewer.EntryViewer(this.getVirtualJarFile(), this.getURI(), var1.getConfigDir(), var1.getPlanBean());
         var2.loadDescriptors();
         Iterator var3 = var2.getDescriptorURIs();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            Descriptor var5 = var2.getDescriptor(var4);
            this.addRootBean(var4, var5.getRootBean());
         }

         if (this.wseeHelper.getWsBean() != null) {
            this.addRootBean("META-INF/webservices.xml", (DescriptorBean)this.wseeHelper.getWsBean());
         }

         if (this.wseeHelper.getWlWsBean() != null) {
            this.addRootBean("META-INF/weblogic-webservices.xml", (DescriptorBean)this.wseeHelper.getWlWsBean());
         }

         try {
            if (this.getVirtualJarFile().getEntry("META-INF/web-services.xml") != null) {
               WSEEDescriptor var23 = new WSEEDescriptor(new File(this.getOutputDir(), "META-INF/web-services.xml"), (File)null, (DeploymentPlanBean)null, (String)null);
               this.addRootBean("META-INF/web-services.xml", (DescriptorBean)var23.getWebservicesBean());
            }
         } catch (Exception var16) {
         }

         WsPolicyDescriptor var24 = new WsPolicyDescriptor(this.getVirtualJarFile(), var1.getConfigDir(), var1.getPlanBean(), this.getURI());

         try {
            if (var24.getWebservicesPolicyBean() != null) {
               this.addRootBean("META-INF/weblogic-webservices-policy.xml", (DescriptorBean)var24.getWebservicesPolicyBean());
            }
         } catch (Exception var15) {
         }

         HashMap var25 = new HashMap();
         HashMap var6 = new HashMap();
         Set var7 = EJBDescriptorMBeanUtils.getCMPEJBNames(this.desc);
         Iterator var8 = var7.iterator();

         String var9;
         String var10;
         while(var8.hasNext()) {
            var9 = (String)var8.next();
            var10 = null;
            PersistenceBean var11 = EJBDescriptorMBeanUtils.getPersistenceMBean(var9, this.desc);
            if (var11 != null) {
               PersistenceUseBean var12 = var11.getPersistenceUse();
               if (var12 != null) {
                  var10 = var12.getTypeStorage();
               }
            }

            if (var10 != null) {
               ZipEntry var28 = this.getVirtualJarFile().getEntry(var10);
               if (var28 != null) {
                  var25.put(var10, var28);
                  var6.put(var10, var9);
               } else {
                  File var13 = new File(new File(var1.getConfigDir(), this.getURI()), var10);
                  if (var13.exists()) {
                     DescriptorBean var14 = this.parseWLSCMPDescriptor(var13);
                     if (var14 != null) {
                        this.addRootBean(var10, var14);
                     }
                  }
               }
            }
         }

         var8 = var25.keySet().iterator();

         while(var8.hasNext()) {
            var9 = (String)var8.next();
            var10 = null;
            ZipEntry var26 = (ZipEntry)var25.get(var9);
            DescriptorBean var27;
            if (this.isSchemaBasedDD(var26)) {
               RDBMSDescriptor var29 = new RDBMSDescriptor(this.getVirtualJarFile(), var9, (String)null, this.getURI(), var1.getPlanBean(), var1.getConfigDir());
               var27 = var29.getDescriptorBean();
            } else {
               var27 = this.xpiParseWLSCMPDescriptor(var26, var9, (String)var6.get(var9));
            }

            if (var27 != null) {
               this.addRootBean(var9, var27);
            }
         }

         this.addDiagnosticDDRootBean(var1);
      } catch (IOException var17) {
         throw new ToolFailureException("Unable to parse EJB descriptor", var17);
      } catch (XMLParsingException var18) {
         throw new ToolFailureException("Unable to parse EJB descriptor", var18);
      } catch (XMLProcessingException var19) {
         throw new ToolFailureException("Unable to parse EJB descriptor", var19);
      } catch (XMLStreamException var20) {
         throw new ToolFailureException("Unable to parse EJB descriptor", var20);
      } catch (ProcessorFactoryException var21) {
         throw new ToolFailureException("Unable to parse EJB descriptor", var21);
      } catch (Exception var22) {
         throw new ToolFailureException("Unable to parse EJB descriptor", var22);
      }
   }

   private DescriptorBean parseWLSCMPDescriptor(File var1) throws Exception {
      BufferedInputStream var2 = null;

      DescriptorBean var4;
      try {
         var2 = new BufferedInputStream(new FileInputStream(var1));
         EditableDescriptorManager var3 = new EditableDescriptorManager();
         var4 = var3.createDescriptor(var2).getRootBean();
      } finally {
         this.closeAndIgnore(var2);
      }

      return var4;
   }

   private DescriptorBean xpiParseWLSCMPDescriptor(ZipEntry var1, String var2, String var3) throws Exception {
      BufferedInputStream var4 = null;

      DescriptorBean var8;
      try {
         var4 = new BufferedInputStream(this.getVirtualJarFile().getInputStream(var1));
         DescriptorBean var5 = (DescriptorBean)Deployer.parseXMLFile(var4, var2, this.desc, (Map)null);
         return var5;
      } catch (RDBMSException var13) {
         weblogic.ejb.container.cmp11.rdbms.Deployer var6 = new weblogic.ejb.container.cmp11.rdbms.Deployer();
         RDBMSDeploymentInfo var7 = var6.parseXMLFile(this.getVirtualJarFile(), var2, var3, new ProcessorFactory(), this.desc);
         var8 = (DescriptorBean)var7.getWeblogicRdbmsJarBean();
      } finally {
         this.closeAndIgnore(var4);
      }

      return var8;
   }

   private boolean isSchemaBasedDD(ZipEntry var1) throws IOException, ProcessorFactoryException {
      BufferedInputStream var2 = null;

      boolean var3;
      try {
         var2 = new BufferedInputStream(this.getVirtualJarFile().getInputStream(var1));
         var3 = Deployer.isSchemaBasedDD(var2);
      } finally {
         this.closeAndIgnore(var2);
      }

      return var3;
   }

   private void closeAndIgnore(Closeable var1) {
      try {
         if (var1 != null) {
            var1.close();
         }
      } catch (IOException var3) {
      }

   }

   private boolean wseeAnnotationsEnabled(EjbJarBean var1) {
      String var2 = ((DescriptorBean)var1).getDescriptor().getOriginalVersionInfo();
      if (var2 == null) {
         return false;
      } else {
         try {
            return var1.getVersion() != null && (double)Float.parseFloat(var2) >= 3.0;
         } catch (Exception var4) {
            return false;
         }
      }
   }

   protected void processAnnotations() throws ClassNotFoundException, ErrorCollectionException, NoSuchMethodException, NoClassDefFoundError {
      EjbJarBean var1 = this.desc.getEjbJarBean();
      if (var1 == null || this.desc.isEjb30() && !var1.isMetadataComplete()) {
         this.desc.setEjb30(true);

         Method var2;
         Method var3;
         Object var4;
         try {
            Class var5 = Class.forName("weblogic.ejb.container.dd.xml.EjbAnnotationProcessor");
            Constructor var6 = var5.getConstructor(GenericClassLoader.class);
            var4 = var6.newInstance(this.getModuleClassLoader());
            var2 = var5.getDeclaredMethod("processAnnotations", EjbDescriptorBean.class, VirtualJarFile.class);
            var3 = var5.getDeclaredMethod("processWLSAnnotations", EjbDescriptorBean.class, ClassLoader.class);
         } catch (ClassNotFoundException var9) {
            var9.printStackTrace();
            throw new AssertionError("Unable to instantiate Annotation processor class");
         } catch (InstantiationException var10) {
            var10.printStackTrace();
            throw new AssertionError("Unable to instantiate Annotation processor class");
         } catch (IllegalAccessException var11) {
            var11.printStackTrace();
            throw new AssertionError("Unable to instantiate Annotation processor class");
         } catch (NoSuchMethodException var12) {
            var12.printStackTrace();
            throw new AssertionError("Unable to instantiate Annotation processor class");
         } catch (InvocationTargetException var13) {
            var13.printStackTrace();
            throw new AssertionError("Unable to instantiate Annotation processor class");
         }

         try {
            var2.invoke(var4, this.desc, this.getVirtualJarFile());
            var3.invoke(var4, this.desc, this.getModuleClassLoader());
         } catch (IllegalAccessException var14) {
            var14.printStackTrace();
            throw new AssertionError("Unable to instantiate Annotation processor class");
         } catch (InvocationTargetException var15) {
            Throwable var17 = var15.getTargetException();
            if (var17 instanceof ClassNotFoundException) {
               throw (ClassNotFoundException)var17;
            }

            if (var17 instanceof ErrorCollectionException) {
               throw (ErrorCollectionException)var17;
            }

            if (var17 instanceof NoSuchMethodException) {
               throw (NoSuchMethodException)var17;
            }

            if (var17 instanceof NoClassDefFoundError) {
               throw (NoClassDefFoundError)var17;
            }

            var15.printStackTrace();
            throw new AssertionError("Unable to invoke Annotation processoror");
         }

         if (this.wseeAnnotationsEnabled(this.desc.getEjbJarBean())) {
            EnterpriseBeansBean var16 = this.desc.getEjbJarBean().getEnterpriseBeans();
            if (var16 == null) {
               return;
            }

            SessionBeanBean[] var18 = this.desc.getEjbJarBean().getEnterpriseBeans().getSessions();
            String[][] var7 = new String[var18.length][2];

            for(int var8 = 0; var8 < var18.length; ++var8) {
               var7[var8][0] = var18[var8].getEjbName();
               var7[var8][1] = var18[var8].getEjbClass();
            }

            this.wseeHelper.processAnnotations(this.getVirtualJarFile(), this.getModuleClassLoader(), var7);
         }
      }

   }

   public void populateMVI(GenericClassLoader var1, CompilerCtx var2) throws ToolFailureException {
      EJBC var3 = EJBCFactory.createEJBC(var2.getOpts());

      try {
         var3.populateValidationInfo(var1, this.getVirtualJarFile(), this.getAltDDFile(), this.getModuleValidationInfo());
      } catch (ErrorCollectionException var5) {
         throw new ToolFailureException(J2EELogger.logAppcUnableToContinueProcessingFileLoggable(var2.getSourceFile().getAbsolutePath(), var5.toString()).getMessage(), var5);
      }
   }

   public void writeDescriptors(CompilerCtx var1) throws ToolFailureException {
      if (var1.isWriteInferredDescriptors()) {
         this.desc.getEjbJarBean().setMetadataComplete(true);
      }

      AppcUtils.writeDescriptor(this.getOutputDir(), "META-INF/ejb-jar.xml", (DescriptorBean)this.desc.getEjbJarBean());
      AppcUtils.writeDescriptor(this.getOutputDir(), "META-INF/weblogic-ejb-jar.xml", (DescriptorBean)this.desc.getWeblogicEjbJarBean());
      AppcUtils.writeDescriptor(this.getOutputDir(), "META-INF/webservices.xml", (DescriptorBean)this.wseeHelper.getWsBean());
      AppcUtils.writeDescriptor(this.getOutputDir(), "META-INF/weblogic-webservices.xml", (DescriptorBean)this.wseeHelper.getWlWsBean());
   }

   public DescriptorBean getRootBean() {
      return this.getRootBean("META-INF/ejb-jar.xml");
   }

   public ModuleType getModuleType() {
      return ModuleType.EJB;
   }

   private void backupDescriptors() throws ToolFailureException {
      if (this.getVirtualJarFile().getEntry("META-INF/weblogic-ejb-jar.xml") != null) {
         AppcUtils.writeDescriptor(this.getOutputDir(), "META-INF/weblogic-ejb-jar.xml" + ORIGINAL_DESCRIPTOR_SUFFIX, (DescriptorBean)this.desc.getWeblogicEjbJarBean());
      }

      if (this.getVirtualJarFile().getEntry("META-INF/ejb-jar.xml") != null) {
         AppcUtils.writeDescriptor(this.getOutputDir(), "META-INF/ejb-jar.xml" + ORIGINAL_DESCRIPTOR_SUFFIX, (DescriptorBean)this.desc.getEjbJarBean());
      }

      if (this.getVirtualJarFile().getEntry("META-INF/webservices.xml") != null) {
         AppcUtils.writeDescriptor(this.getOutputDir(), "META-INF/webservices.xml" + ORIGINAL_DESCRIPTOR_SUFFIX, (DescriptorBean)this.wseeHelper.getWsBean());
      }

      if (this.getVirtualJarFile().getEntry("META-INF/weblogic-webservices.xml") != null) {
         AppcUtils.writeDescriptor(this.getOutputDir(), "META-INF/weblogic-webservices.xml" + ORIGINAL_DESCRIPTOR_SUFFIX, (DescriptorBean)this.wseeHelper.getWlWsBean());
      }

   }
}
