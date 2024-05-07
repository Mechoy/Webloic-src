package weblogic.ejb.container.dd.xml;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.InputSource;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.compliance.ComplianceException;
import weblogic.ejb.container.compliance.WeblogicJarChecker;
import weblogic.ejb.container.dd.DDConstants;
import weblogic.ejb.container.deployer.EJBDescriptorMBeanUtils;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.EjbDescriptorReader;
import weblogic.ejb.spi.EjbJarDescriptor;
import weblogic.j2ee.descriptor.ActivationConfigBean;
import weblogic.j2ee.descriptor.AroundInvokeBean;
import weblogic.j2ee.descriptor.AssemblyDescriptorBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.EnterpriseBeansBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.InterceptorBean;
import weblogic.j2ee.descriptor.InterceptorsBean;
import weblogic.j2ee.descriptor.LifecycleCallbackBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.SecurityRoleBean;
import weblogic.j2ee.descriptor.SecurityRoleRefBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.EntityDescriptorBean;
import weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.j2ee.descriptor.wl.StatefulSessionDescriptorBean;
import weblogic.j2ee.descriptor.wl.StatelessSessionDescriptorBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.DelegateFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.xml.process.ProcessorFactory;
import weblogic.xml.process.ProcessorFactoryException;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;

public final class EjbDescriptorReaderImpl implements EjbDescriptorReader {
   private static final boolean debug = false;
   private static final XMLInputFactory xiFactory;
   private static final int BUF_SIZE = 16384;
   private static final int MAX_DTD_ELEMENTS = 10;
   public static final String WEBLOGIC_EJB_JAR_NAME = "META-INF/weblogic-ejb-jar.xml";
   public static final String EJB_JAR_NAME = "META-INF/ejb-jar.xml";

   public EjbDescriptorBean createDescriptorFromJarFile(JarFile var1) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException {
      return this.createDescriptorFromJarFile((JarFile)var1, (File)null);
   }

   public EjbDescriptorBean createDescriptorFromJarFile(JarFile var1, File var2) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException {
      return this.createDescriptorFromJarFile(VirtualJarFactory.createVirtualJar(var1), var2);
   }

   public EjbDescriptorBean createDescriptorFromJarFile(VirtualJarFile var1) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException {
      return this.createDescriptorFromJarFile((VirtualJarFile)var1, (File)null);
   }

   public EjbDescriptorBean createDescriptorFromJarFile(VirtualJarFile var1, File var2) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException {
      return this.createDescriptorFromJarFile(var1, var2, (String)null, (String)null);
   }

   public EjbDescriptorBean createDescriptorFromJarFile(VirtualJarFile var1, File var2, String var3, String var4) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException {
      return this.createDescriptorFromJarFile(var1, var2, (File)null, (DeploymentPlanBean)null, var3, var4);
   }

   public EjbDescriptorBean createReadOnlyDescriptorFromJarFile(VirtualJarFile var1, GenericClassLoader var2) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException {
      return this.createReadOnlyDescriptorFromJarFile(var1, (File)null, (File)null, (DeploymentPlanBean)null, (String)null, (String)null, var2, (VirtualJarFile[])null);
   }

   public EjbDescriptorBean createDescriptorFromJarFile(VirtualJarFile var1, File var2, File var3, DeploymentPlanBean var4, String var5, String var6) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException {
      EjbDescriptorBean var7 = createEjbDescriptorBean(var1, var5, var6, var4, var3, false);
      processEjbJarXML(var7, var1, var2, var3, var4, var5, var6, (VirtualJarFile[])null);
      processWeblogicEjbJarXML(var7, var1, var3, var4, var5, var6);
      return var7;
   }

   public EjbDescriptorBean createReadOnlyDescriptorFromJarFile(VirtualJarFile var1, File var2, File var3, DeploymentPlanBean var4, String var5, String var6, GenericClassLoader var7, VirtualJarFile[] var8) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException {
      GenericClassLoader var9 = null;
      EjbDescriptorBean var10 = createEjbDescriptorBean(var1, var5, var6, var4, var3, true);
      processEjbJarXML(var10, var1, var2, var3, var4, var5, var6, var8);
      AnnotationDebugger.info("The ejb-jar.xml in " + var10.getJarFileName() + " before the processing of EJB annotation:");
      AnnotationDebugger.logEjbJar(var10);
      ClassLoader var11 = Thread.currentThread().getContextClassLoader();

      EjbDescriptorBean var12;
      try {
         var9 = createAnnotationProcessorClassLoader(var7);
         Thread.currentThread().setContextClassLoader(var9);
         processStandardAnnotations(var10, var1, var9);
         AnnotationDebugger.info("The ejb-jar.xml in " + var10.getJarFileName() + " after the processing of EJB annotation:");
         AnnotationDebugger.logEjbJar(var10);
         AnnotationDebugger.info("The weblogic-ejb-jar.xml in " + var10.getJarFileName() + " before the processing of Weblogic's EJB annotation:");
         processWeblogicEjbJarXML(var10, var1, var3, var4, var5, var6);
         AnnotationDebugger.logWlsEjbJar(var10);
         processWLSAnnotations(var10, var9);
         AnnotationDebugger.info("The weblogic-ejb-jar.xml in " + var10.getJarFileName() + " after the processing of Weblogic's EJB annotation:");
         AnnotationDebugger.logWlsEjbJar(var10);
         WeblogicJarChecker.validateEnterpriseBeansMinimalConfiguration(var10.getEjbJarBean(), var6);
         completeEjbJar(var10);
         completeWeblogicEjbJar(var10);
         var12 = var10;
      } catch (ComplianceException var16) {
         throw new IOException(var16.getMessage());
      } finally {
         Thread.currentThread().setContextClassLoader(var11);
         if (var9 != null) {
            var9.close();
         }

      }

      return var12;
   }

   private static EjbDescriptorBean createEjbDescriptorBean(VirtualJarFile var0, String var1, String var2, DeploymentPlanBean var3, File var4, boolean var5) {
      EjbDescriptorBean var6 = new EjbDescriptorBean(var5);
      var6.setAppName(var1);
      var6.setUri(var2);
      var6.setDeploymentPlan(var3);
      var6.setConfigDirectory(var4);
      var6.setJarFileName(var0.getName());
      return var6;
   }

   private static void processEjbJarXML(EjbDescriptorBean var0, VirtualJarFile var1, File var2, File var3, DeploymentPlanBean var4, String var5, String var6, VirtualJarFile[] var7) throws IOException, XMLStreamException {
      if (var2 != null || var1.getEntry("META-INF/ejb-jar.xml") != null || var7 != null) {
         processEjbJarXMLWithSchema(var1, var2, var3, var4, var0, var5, var6, var7);
      }

   }

   private static void completeEjbJar(EjbDescriptorBean var0) {
      EjbJarBean var1 = var0.getEjbJarBean();
      if (var0.isEjb30()) {
         SessionBeanBean[] var2 = var1.getEnterpriseBeans().getSessions();
         SessionBeanBean[] var3 = var2;
         int var4 = var2.length;

         int var5;
         for(var5 = 0; var5 < var4; ++var5) {
            SessionBeanBean var6 = var3[var5];
            String var7 = var6.getEjbClass();
            addAroundInvokeDefaults(var6.getAroundInvokes(), var7);
            addLifecycleCallbackDefaults(var6.getPostActivates(), var7);
            addLifecycleCallbackDefaults(var6.getPrePassivates(), var7);
            addLifecycleCallbackDefaults(var6.getPostConstructs(), var7);
            addLifecycleCallbackDefaults(var6.getPreDestroys(), var7);
         }

         MessageDrivenBeanBean[] var15 = var1.getEnterpriseBeans().getMessageDrivens();
         MessageDrivenBeanBean[] var16 = var15;
         var5 = var15.length;

         int var19;
         for(var19 = 0; var19 < var5; ++var19) {
            MessageDrivenBeanBean var21 = var16[var19];
            String var8 = var21.getEjbClass();
            addAroundInvokeDefaults(var21.getAroundInvokes(), var8);
            addLifecycleCallbackDefaults(var21.getPostConstructs(), var8);
            addLifecycleCallbackDefaults(var21.getPreDestroys(), var8);
         }

         InterceptorsBean var17 = var1.getInterceptors();
         if (var17 != null) {
            InterceptorBean[] var18 = var17.getInterceptors();
            var19 = var18.length;

            for(int var23 = 0; var23 < var19; ++var23) {
               InterceptorBean var25 = var18[var23];
               String var9 = var25.getInterceptorClass();
               addAroundInvokeDefaults(var25.getAroundInvokes(), var9);
               addLifecycleCallbackDefaults(var25.getPostActivates(), var9);
               addLifecycleCallbackDefaults(var25.getPrePassivates(), var9);
               addLifecycleCallbackDefaults(var25.getPostConstructs(), var9);
               addLifecycleCallbackDefaults(var25.getPreDestroys(), var9);
            }
         }

         HashSet var20 = new HashSet();
         AssemblyDescriptorBean var22 = var1.getAssemblyDescriptor();
         if (var22 != null) {
            SecurityRoleBean[] var24 = var22.getSecurityRoles();
            int var26 = var24.length;

            int var28;
            for(var28 = 0; var28 < var26; ++var28) {
               SecurityRoleBean var10 = var24[var28];
               var20.add(var10.getRoleName());
            }

            SessionBeanBean[] var27 = var2;
            var26 = var2.length;

            for(var28 = 0; var28 < var26; ++var28) {
               SessionBeanBean var29 = var27[var28];
               SecurityRoleRefBean[] var11 = var29.getSecurityRoleRefs();
               int var12 = var11.length;

               for(int var13 = 0; var13 < var12; ++var13) {
                  SecurityRoleRefBean var14 = var11[var13];
                  if (var14.getRoleLink() == null && var20.contains(var14.getRoleName())) {
                     var14.setRoleLink(var14.getRoleName());
                  }
               }
            }

         }
      }
   }

   private static void addLifecycleCallbackDefaults(LifecycleCallbackBean[] var0, String var1) {
      LifecycleCallbackBean[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         LifecycleCallbackBean var5 = var2[var4];
         if (var5.getLifecycleCallbackClass() == null) {
            var5.setLifecycleCallbackClass(var1);
         }
      }

   }

   private static void addAroundInvokeDefaults(AroundInvokeBean[] var0, String var1) {
      AroundInvokeBean[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         AroundInvokeBean var5 = var2[var4];
         if (var5.getClassName() == null) {
            var5.setClassName(var1);
         }
      }

   }

   private static void processStandardAnnotations(EjbDescriptorBean var0, VirtualJarFile var1, GenericClassLoader var2) throws IOException {
      EjbJarBean var3 = var0.getEjbJarBean();
      if (var3 == null || var0.isEjb30() && !var3.isMetadataComplete()) {
         try {
            var0.setEjb30(true);
            EjbAnnotationProcessor var4 = new EjbAnnotationProcessor(var2);
            var4.processAnnotations(var0, var1);
         } catch (Exception var6) {
            if (var6 instanceof ErrorCollectionException) {
               IOException var5 = new IOException("Error processing annotations: ");
               var5.initCause(var6);
               throw var5;
            }

            throw new IOException(": " + var6);
         }
      }

   }

   private static void processWLSAnnotations(EjbDescriptorBean var0, GenericClassLoader var1) throws IOException {
      WeblogicEjbJarBean var2 = var0.getWeblogicEjbJarBean();
      if (var2 == null || var0.isEjb30() && !var0.getEjbJarBean().isMetadataComplete()) {
         try {
            EjbAnnotationProcessor var3 = new EjbAnnotationProcessor(var1);
            var3.processWLSAnnotations(var0, var1);
         } catch (Exception var5) {
            IOException var4 = new IOException("Error processing annotations: " + var5);
            var4.initCause(var5);
            throw var4;
         }
      }

   }

   private static GenericClassLoader createAnnotationProcessorClassLoader(GenericClassLoader var0) {
      NonClosingClassFinder var1 = new NonClosingClassFinder(var0.getClassFinder());
      return new GenericClassLoader(var1, var0.getParent());
   }

   private static void processWeblogicEjbJarXML(EjbDescriptorBean var0, VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, String var5) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException {
      EjbJarDescriptorFactory var6;
      if (var3 != null) {
         var6 = EjbJarDescriptorFactory.newInstance(var1, var2, var3, var4, var5);
      } else {
         var6 = EjbJarDescriptorFactory.newInstance(var1, var4, var5);
      }

      if (var1.getEntry("META-INF/weblogic-ejb-jar.xml") != null) {
         BufferedInputStream var7 = null;

         try {
            var7 = getStream(var1, "META-INF/weblogic-ejb-jar.xml");
            parseWLDD(var6, var3, var5, var7, getSysId(var1, "META-INF/weblogic-ejb-jar.xml"), var0.isReadOnly(), var0);
         } finally {
            if (var7 != null) {
               var7.close();
            }

         }
      } else {
         processWLEjbJarXMLWithSchema(var6, var0, var0.isReadOnly());
         if (var0.getWeblogicEjbJarBean() == null) {
            var0.createWeblogicEjbJarBean();
            var0.markWeblogicEjbJarSynthetic();
         }
      }

   }

   private static boolean isDTDBased(InputStream var0) throws IOException, XMLStreamException {
      NoCloseInputStream var1 = new NoCloseInputStream(var0);
      var1.mark(16384);
      XMLStreamReader var2 = xiFactory.createXMLStreamReader(var1);

      try {
         for(int var3 = 0; var3 < 10 && var2.hasNext(); ++var3) {
            boolean var4;
            if (var2.isStartElement()) {
               var4 = false;
               return var4;
            }

            if (var2.next() == 11) {
               var4 = true;
               return var4;
            }
         }

         boolean var8 = false;
         return var8;
      } finally {
         var2.close();
         var1.reset();
      }
   }

   public WeblogicEjbJarBean parseWebLogicEjbJarXML(EjbJarDescriptor var1, DeploymentPlanBean var2, File var3, String var4, InputStream var5, EjbJarBean var6, String var7, boolean var8) throws IOException, XMLStreamException {
      EjbDescriptorBean var9 = new EjbDescriptorBean(var8);
      var9.setEjbJarBean(var6);
      var9.setDeploymentPlan(var2);
      var9.setConfigDirectory(var3);
      if (var5 == null) {
         if (var8) {
            var9.createWeblogicEjbJarBean();
            completeWeblogicEjbJar(var9);
         }

         return var9.getWeblogicEjbJarBean();
      } else {
         if (!((InputStream)var5).markSupported()) {
            var5 = new BufferedInputStream((InputStream)var5, 16384);
         }

         try {
            parseWLDD(EjbJarDescriptorFactory.newInstance(var1), var2, var4, (InputStream)var5, var7, var8, var9);
         } catch (XMLParsingException var11) {
            throw new XMLStreamException(var11.getMessage(), var11);
         } catch (XMLProcessingException var12) {
            throw new XMLStreamException(var12.getMessage(), var12);
         }

         return var9.getWeblogicEjbJarBean();
      }
   }

   private static void parseWLDD(EjbJarDescriptorFactory var0, DeploymentPlanBean var1, String var2, InputStream var3, String var4, boolean var5, EjbDescriptorBean var6) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException {
      if (isDTDBased(var3)) {
         if (var1 != null) {
            ModuleOverrideBean var7 = var1.findModuleOverride(var2);
            if (var7 != null) {
               ModuleDescriptorBean var8 = var1.findModuleDescriptor(var2, "META-INF/weblogic-ejb-jar.xml");
               if (var8 != null && var8.getVariableAssignments() != null && var8.getVariableAssignments().length > 0) {
                  Loggable var9 = EJBLogger.logNoPlanOverridesWithDTDDescriptorsLoggable(var2);
                  throw new XMLProcessingException(var9.getMessage());
               }
            }
         }

         processWLEjbJarXML(var3, var4, var6);
      } else {
         processWLEjbJarXMLWithSchema(var0, var6, var5);
      }

   }

   private static void completeWeblogicEjbJar(EjbDescriptorBean var0) {
      EjbJarBean var1 = var0.getEjbJarBean();
      EnterpriseBeansBean var2 = var1.getEnterpriseBeans();
      WeblogicEjbJarBean var3 = var0.getWeblogicEjbJarBean();
      WeblogicEnterpriseBeanBean[] var4 = var3.getWeblogicEnterpriseBeans();
      HashMap var5 = new HashMap();

      for(int var6 = 0; var6 < var4.length; ++var6) {
         var5.put(var4[var6].getEjbName(), var4[var6]);
      }

      SessionBeanBean[] var13 = var2.getSessions();

      for(int var7 = 0; var7 < var13.length; ++var7) {
         WeblogicEnterpriseBeanBean var8 = (WeblogicEnterpriseBeanBean)var5.get(var13[var7].getEjbName());
         if (var8 == null) {
            var8 = var3.createWeblogicEnterpriseBean();
            var8.setEjbName(var13[var7].getEjbName());
         }

         if (var8.getTransactionDescriptor() == null) {
            var8.createTransactionDescriptor();
         }

         if ("stateless".equalsIgnoreCase(var13[var7].getSessionType())) {
            StatelessSessionDescriptorBean var9 = var8.getStatelessSessionDescriptor();
            if (null == var9) {
               var9 = var8.createStatelessSessionDescriptor();
            }

            if (null == var9.getPool()) {
               var9.createPool();
            }

            if (null == var9.getStatelessClustering()) {
               var9.createStatelessClustering();
            }
         } else {
            StatefulSessionDescriptorBean var17 = var8.getStatefulSessionDescriptor();
            if (null == var17) {
               var17 = var8.createStatefulSessionDescriptor();
            }

            if (null == var17.getStatefulSessionCache()) {
               var17.createStatefulSessionCache();
            }

            if (null == var17.getStatefulSessionClustering()) {
               var17.createStatefulSessionClustering();
            }
         }
      }

      EntityBeanBean[] var14 = var2.getEntities();

      for(int var15 = 0; var15 < var14.length; ++var15) {
         WeblogicEnterpriseBeanBean var18 = (WeblogicEnterpriseBeanBean)var5.get(var14[var15].getEjbName());
         if (var18 == null) {
            var18 = var3.createWeblogicEnterpriseBean();
            var18.setEjbName(var14[var15].getEjbName());
         }

         if (var18.getTransactionDescriptor() == null) {
            var18.createTransactionDescriptor();
         }

         EntityDescriptorBean var10 = var18.getEntityDescriptor();
         if (null == var10) {
            var10 = var18.createEntityDescriptor();
         }

         if (null == var10.getEntityCache() && null == var10.getEntityCacheRef()) {
            var10.createEntityCache();
         }

         if (null == var10.getPersistence()) {
            var10.createPersistence();
         }

         if (null == var10.getEntityClustering()) {
            var10.createEntityClustering();
         }

         if (null == var10.getPool()) {
            var10.createPool();
         }
      }

      MessageDrivenBeanBean[] var16 = var2.getMessageDrivens();

      for(int var20 = 0; var20 < var16.length; ++var20) {
         WeblogicEnterpriseBeanBean var19 = (WeblogicEnterpriseBeanBean)var5.get(var16[var20].getEjbName());
         if (var19 == null) {
            var19 = var3.createWeblogicEnterpriseBean();
            var19.setEjbName(var16[var20].getEjbName());
         }

         if (var19.getTransactionDescriptor() == null) {
            var19.createTransactionDescriptor();
         }

         ActivationConfigBean var11 = var16[var20].getActivationConfig();
         if (var11 == null) {
            var11 = var16[var20].createActivationConfig();
         }

         MessageDrivenDescriptorBean var12 = var19.getMessageDrivenDescriptor();
         if (null == var12) {
            var12 = var19.createMessageDrivenDescriptor();
         }
      }

   }

   private static void processEjbJarXMLWithSchema(VirtualJarFile var0, File var1, File var2, DeploymentPlanBean var3, EjbDescriptorBean var4, String var5, String var6, VirtualJarFile[] var7) throws IOException, XMLStreamException {
      EjbJarDescriptor var8 = null;
      if (var1 != null) {
         if (var3 != null) {
            var8 = new EjbJarDescriptor(var1, var2, var3, var5, var6);
         } else {
            var8 = new EjbJarDescriptor(var1, var5, var6);
         }
      } else {
         var8 = new EjbJarDescriptor(var0, var2, var3, var5, var6);
      }

      if (var7 != null && var7.length > 0) {
         var8.mergeEJBJar(var7);
      }

      if (var4.isReadOnly()) {
         var4.setEjbJarBean(var8.getEjbJarBean());
      } else {
         var4.setEjbJarBean(var8.getEditableEjbJarBean());
      }

   }

   private static void processWLEjbJarXMLWithSchema(EjbJarDescriptorFactory var0, EjbDescriptorBean var1, boolean var2) throws IOException, XMLStreamException {
      EjbJarDescriptor var3 = var0.getEjbJarDescriptor();
      if (var2) {
         var1.setWeblogicEjbJarBean(var3.parseWeblogicEjbJarBean());
      } else {
         var1.setWeblogicEjbJarBean(var3.parseEditableWeblogicEjbJarBean());
      }

   }

   private static BufferedInputStream getStream(VirtualJarFile var0, String var1) throws IOException {
      ZipEntry var2 = var0.getEntry(var1);
      if (var2 == null) {
         Loggable var3 = EJBLogger.logmissingDescriptorLoggable(var1, var0.getName());
         throw new FileNotFoundException(var3.getMessage());
      } else {
         return new BufferedInputStream(var0.getInputStream(var2));
      }
   }

   private static BufferedInputStream getStream(File var0) throws IOException {
      return new BufferedInputStream(new FileInputStream(var0));
   }

   private static void processWLEjbJarXML(InputStream var0, String var1, EjbDescriptorBean var2) throws XMLProcessingException, XMLParsingException, IOException {
      processXML(var0, var1, var2, DDConstants.validWeblogicEjbJarPublicIds, "META-INF/weblogic-ejb-jar.xml", new ProcessorFactory());
   }

   private static void processXML(InputStream var0, String var1, EjbDescriptorBean var2, String[] var3, String var4, ProcessorFactory var5) throws XMLProcessingException, XMLParsingException, IOException {
      String var6 = DDUtils.getXMLEncoding((InputStream)var0, "META-INF/weblogic-ejb-jar.xml");
      if (!((InputStream)var0).markSupported()) {
         var0 = new BufferedInputStream((InputStream)var0);
      }

      ((InputStream)var0).mark(1048576);
      DDLoader var7 = null;

      try {
         var7 = (DDLoader)var5.getProcessor((InputStream)var0, var3);
      } catch (ProcessorFactoryException var11) {
         throw new XMLProcessingException(var11, var4);
      }

      ((InputStream)var0).reset();
      var7.setEJBDescriptor(var2);
      var7.setEncoding(var6);
      var7.setValidate(var5.isValidating());

      try {
         InputSource var8 = new InputSource((InputStream)var0);
         var8.setSystemId(var1);
         var7.process(var8);
      } catch (XMLParsingException var9) {
         var9.setFileName(var4);
         throw var9;
      } catch (XMLProcessingException var10) {
         var10.setFileName(var4);
         throw var10;
      }
   }

   public void loadWeblogicRDBMSJarMBeans(EjbDescriptorBean var1, VirtualJarFile var2, ProcessorFactory var3, boolean var4) throws Exception {
      EJBDescriptorMBeanUtils.loadWeblogicRDBMSJarMBeans(var1, var2, var3, var4);
   }

   private static String getSysId(VirtualJarFile var0, String var1) throws IOException {
      String var2 = var0.getName().replace('\\', '/');
      var2 = completePath(var2);
      return "zip://" + var2 + "!/" + var1;
   }

   private static String completePath(String var0) {
      if (var0.startsWith("./")) {
         return getPWD() + var0.substring(1);
      } else {
         return var0.startsWith("../") ? getPWD() + "/" + var0 : var0;
      }
   }

   private static String getPWD() {
      return System.getProperty("user.dir").replace('\\', '/');
   }

   static {
      ClassLoader var0 = Thread.currentThread().getContextClassLoader();

      try {
         Thread.currentThread().setContextClassLoader(EjbDescriptorReaderImpl.class.getClassLoader());
         xiFactory = XMLInputFactory.newInstance();
      } finally {
         Thread.currentThread().setContextClassLoader(var0);
      }

   }

   private static class NonClosingClassFinder extends DelegateFinder {
      public NonClosingClassFinder(ClassFinder var1) {
         super(var1);
      }

      public void close() {
      }
   }

   private static class NoCloseInputStream extends InputStream {
      private final InputStream is;

      NoCloseInputStream(InputStream var1) {
         this.is = var1;
      }

      public int read() throws IOException {
         return this.is.read();
      }

      public synchronized void mark(int var1) {
         this.is.mark(var1);
      }

      public boolean markSupported() {
         return this.is.markSupported();
      }

      public synchronized void reset() throws IOException {
         this.is.reset();
      }

      public void close() {
      }
   }
}
