package weblogic.application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.AccessController;
import java.util.jar.JarFile;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.descriptor.CachingDescriptorLoader2;
import weblogic.application.internal.ApplicationReader;
import weblogic.application.internal.WlsApplicationReader;
import weblogic.application.internal.WlsExtensionReader;
import weblogic.application.utils.ModuleDiscovery;
import weblogic.deploy.internal.DeploymentPlanDescriptorLoader;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.j2ee.descriptor.wl.WeblogicExtensionBean;
import weblogic.kernel.Kernel;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class ApplicationDescriptor {
   private final MyApplicationDescriptor appDescriptor;
   private final MyWlsApplicationDescriptor wlsAppDescriptor;
   private final MyWlsExtensionDescriptor wlsExtDescriptor;
   private VirtualJarFile vjf;
   private static final boolean isServer = Kernel.isServer();
   private static final AuthenticatedSubject kernelId;
   private boolean validateSchema;
   ApplicationBean discoveredModuleAppBean;
   boolean scannedForModules;

   public ApplicationDescriptor(String var1, boolean var2, VirtualJarFile var3, File var4, DeploymentPlanBean var5, String var6) {
      this(var3, var4, var5, var6);
      if (var2 && ManagementService.getRuntimeAccess(kernelId).getDomain().isInternalAppsDeployOnDemandEnabled()) {
         this.setValidateSchema(false);
      }

   }

   public ApplicationDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4) {
      this.vjf = null;
      this.validateSchema = true;
      this.discoveredModuleAppBean = null;
      this.scannedForModules = false;
      this.appDescriptor = new MyApplicationDescriptor(var1, var2, var3, var4);
      this.wlsAppDescriptor = new MyWlsApplicationDescriptor(var1, var2, var3, var4);
      this.wlsExtDescriptor = new MyWlsExtensionDescriptor(var1, var2, var3, var4);
      this.vjf = var1;
   }

   public ApplicationDescriptor(InputStream var1, InputStream var2) {
      this.vjf = null;
      this.validateSchema = true;
      this.discoveredModuleAppBean = null;
      this.scannedForModules = false;
      this.appDescriptor = new MyApplicationDescriptor(var1);
      this.wlsAppDescriptor = new MyWlsApplicationDescriptor(var2);
      this.wlsExtDescriptor = new MyWlsExtensionDescriptor((File)null);
   }

   public ApplicationDescriptor(File var1, File var2, File var3, File var4, DeploymentPlanBean var5, String var6) {
      this.vjf = null;
      this.validateSchema = true;
      this.discoveredModuleAppBean = null;
      this.scannedForModules = false;
      this.appDescriptor = new MyApplicationDescriptor(var1, var4, var5, var6);
      this.wlsAppDescriptor = new MyWlsApplicationDescriptor(var2, var4, var5, var6);
      this.wlsExtDescriptor = new MyWlsExtensionDescriptor(var3);
   }

   public ApplicationDescriptor(File var1, File var2, VirtualJarFile var3, File var4, DeploymentPlanBean var5, String var6) {
      this.vjf = null;
      this.validateSchema = true;
      this.discoveredModuleAppBean = null;
      this.scannedForModules = false;
      if (var1 != null) {
         this.appDescriptor = new MyApplicationDescriptor(var1, var4, var5, var6);
      } else {
         this.appDescriptor = new MyApplicationDescriptor(var3, var4, var5, var6);
      }

      if (var2 != null) {
         this.wlsAppDescriptor = new MyWlsApplicationDescriptor(var2, var4, var5, var6);
      } else {
         this.wlsAppDescriptor = new MyWlsApplicationDescriptor(var3, var4, var5, var6);
      }

      this.wlsExtDescriptor = new MyWlsExtensionDescriptor(var3, var4, var5, var6);
      this.vjf = var3;
   }

   public ApplicationDescriptor(VirtualJarFile var1) {
      this(var1, (File)null, (DeploymentPlanBean)null, (String)null);
   }

   public ApplicationDescriptor(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5) {
      this.vjf = null;
      this.validateSchema = true;
      this.discoveredModuleAppBean = null;
      this.scannedForModules = false;
      this.appDescriptor = new MyApplicationDescriptor(var1, var2, var3, var4, var5);
      this.wlsAppDescriptor = new MyWlsApplicationDescriptor(var1, var2, var3, var4, var5);
      this.wlsExtDescriptor = new MyWlsExtensionDescriptor(var1, var2);
   }

   public ApplicationDescriptor(DescriptorManager var1, GenericClassLoader var2) {
      this.vjf = null;
      this.validateSchema = true;
      this.discoveredModuleAppBean = null;
      this.scannedForModules = false;
      this.appDescriptor = new MyApplicationDescriptor(var1, var2);
      this.wlsAppDescriptor = new MyWlsApplicationDescriptor(var1, var2);
      this.wlsExtDescriptor = new MyWlsExtensionDescriptor(var1, var2);
   }

   public ApplicationDescriptor() {
      this.vjf = null;
      this.validateSchema = true;
      this.discoveredModuleAppBean = null;
      this.scannedForModules = false;
      byte[] var1 = (new String("<jav:application xmlns:jav=\"http://java.sun.com/xml/ns/javaee\" version=\"5\" />")).getBytes();
      this.appDescriptor = new MyApplicationDescriptor(new ByteArrayInputStream(var1));
      byte[] var2 = (new String("<ns:weblogic-application xmlns:ns=\"http://xmlns.oracle.com/weblogic/weblogic-application\" />")).getBytes();
      this.wlsAppDescriptor = new MyWlsApplicationDescriptor(new ByteArrayInputStream(var2));
      byte[] var3 = (new String("<ns:weblogic-extension xmlns:ns=\"http://xmlns.oracle.com/weblogic/weblogic-extension\" />")).getBytes();
      this.wlsExtDescriptor = new MyWlsExtensionDescriptor(new ByteArrayInputStream(var3));
   }

   public void setValidateSchema(boolean var1) {
      this.validateSchema = var1;
   }

   public void writeDescriptors(File var1) throws IOException, XMLStreamException {
      DescriptorManager var2 = new DescriptorManager();
      File var3 = null;
      DescriptorBean var4 = (DescriptorBean)this.getApplicationDescriptor();
      if (var4 != null && this.discoveredModuleAppBean == null) {
         var3 = new File(var1, this.appDescriptor.getDocumentURI());
         DescriptorUtils.writeDescriptor(var2, var4, var3);
      }

      var4 = (DescriptorBean)this.getWeblogicApplicationDescriptor();
      if (var4 != null) {
         var3 = new File(var1, this.wlsAppDescriptor.getDocumentURI());
         DescriptorUtils.writeDescriptor(var2, var4, var3);
      }

      var4 = (DescriptorBean)this.getWeblogicExtensionDescriptor();
      if (var4 != null) {
         var3 = new File(var1, this.wlsExtDescriptor.getDocumentURI());
         DescriptorUtils.writeDescriptor(var2, var4, var3);
      }

   }

   public void writeInferredApplicationDescriptor(File var1) throws IOException, XMLStreamException {
      if (this.discoveredModuleAppBean != null) {
         DescriptorManager var2 = new DescriptorManager();
         File var3 = null;
         var3 = new File(var1, this.appDescriptor.getDocumentURI());
         DescriptorUtils.writeDescriptor(var2, (DescriptorBean)this.getApplicationDescriptor(), var3);
      }

   }

   public void dumpMergedApplicationDescriptor(String var1) {
      try {
         System.out.println("Dumping the merged application.xml descriptor for application " + var1);
         DescriptorBean var2 = (DescriptorBean)this.getApplicationDescriptor();
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();
         (new DescriptorManager()).writeDescriptorAsXML(var2.getDescriptor(), var3);
         String var4 = var3.toString();
         System.out.println(" The merged application descriptor is ..." + var4);
         var3.close();
         System.out.println("********************************************");
         System.out.println("Dumping the weblogic-application.xml ");
         var2 = (DescriptorBean)this.getWeblogicApplicationDescriptor();
         var3 = new ByteArrayOutputStream();
         (new DescriptorManager()).writeDescriptorAsXML(var2.getDescriptor(), var3);
         var4 = var3.toString();
         System.out.println(" The merged wlsapplication descriptor is ..." + var4);
         System.out.println("********************************************");
         System.out.println("Dumping the weblogic-extension descriptor");
         var2 = (DescriptorBean)this.getWeblogicExtensionDescriptor();
         var3 = new ByteArrayOutputStream();
         (new DescriptorManager()).writeDescriptorAsXML(var2.getDescriptor(), var3);
         var4 = var3.toString();
         System.out.println(" The merged wlsextension descriptor is ..." + var4);
      } catch (Exception var5) {
         J2EELogger.logDebug(" unable to dump merged descriptor for " + var1);
      }

   }

   public ApplicationBean getApplicationDescriptor() throws IOException, XMLStreamException {
      if (!this.validateSchema) {
         this.appDescriptor.setValidate(false);
      }

      ApplicationBean var1 = (ApplicationBean)this.appDescriptor.loadDescriptorBean();
      if (var1 == null) {
         if (!this.scannedForModules) {
            this.scannedForModules = true;
            if (this.vjf != null) {
               this.discoveredModuleAppBean = ModuleDiscovery.discoverModules(this.vjf);
            }
         }

         var1 = this.discoveredModuleAppBean;
         if (var1 != null) {
            this.updateApplicationDescriptor(var1);
            var1 = (ApplicationBean)this.appDescriptor.loadDescriptorBean();
         }
      }

      return var1;
   }

   public WeblogicApplicationBean getWeblogicApplicationDescriptor() throws IOException, XMLStreamException {
      if (!this.validateSchema) {
         this.wlsAppDescriptor.setValidate(false);
      }

      return (WeblogicApplicationBean)this.wlsAppDescriptor.loadDescriptorBean();
   }

   public WeblogicExtensionBean getWeblogicExtensionDescriptor() throws IOException, XMLStreamException {
      if (!this.validateSchema) {
         this.wlsExtDescriptor.setValidate(false);
      }

      return (WeblogicExtensionBean)this.wlsExtDescriptor.loadDescriptorBean();
   }

   public void mergeDescriptors(VirtualJarFile var1) throws IOException, XMLStreamException {
      try {
         this.appDescriptor.mergeDescriptors(new VirtualJarFile[]{var1});
         if (var1.getEntry("META-INF/weblogic-application.xml") != null) {
            this.wlsAppDescriptor.mergeDescriptors(new VirtualJarFile[]{var1});
         }

         if (var1.getEntry("META-INF/weblogic-extension.xml") != null) {
            this.wlsExtDescriptor.mergeDescriptors(new VirtualJarFile[]{var1});
         }

      } catch (Exception var3) {
         var3.printStackTrace();
         throw new IOException(var3.getMessage());
      }
   }

   public void mergeDescriptors(ApplicationDescriptor var1) throws IOException, XMLStreamException {
      ApplicationBean var2 = var1.getApplicationDescriptor();
      if (var2 != null && var2.getModules() != null && var2.getModules().length > 0) {
         this.appDescriptor.mergeDescriptorBean(var1.getApplicationDescriptorLoader());
      }

      this.wlsAppDescriptor.mergeDescriptorBean(var1.getWlsApplicationDescriptorLoader());
      this.wlsExtDescriptor.mergeDescriptorBean(var1.getWlsExtensionDescriptorLoader());
   }

   private ApplicationBean mergeApplicationDescriptor(File var1) throws IOException, XMLStreamException {
      return (ApplicationBean)this.appDescriptor.mergeDescriptors(new File[]{var1});
   }

   private WeblogicApplicationBean mergeWlsApplicationDescriptor(File var1) throws IOException, XMLStreamException {
      return (WeblogicApplicationBean)this.wlsAppDescriptor.mergeDescriptors(new File[]{var1});
   }

   private WeblogicExtensionBean mergeWlsExtentionDescriptor(File var1) throws IOException, XMLStreamException {
      return (WeblogicExtensionBean)this.wlsExtDescriptor.mergeDescriptors(new File[]{var1});
   }

   public void updateApplicationDescriptor(ApplicationBean var1) throws IOException, XMLStreamException {
      this.appDescriptor.updateDescriptorWithBean((DescriptorBean)var1);
   }

   public void updateWeblogicApplicationDescriptor(WeblogicApplicationBean var1) throws IOException, XMLStreamException {
      this.wlsAppDescriptor.updateDescriptorWithBean((DescriptorBean)var1);
   }

   public AbstractDescriptorLoader2 getApplicationDescriptorLoader() {
      return this.appDescriptor;
   }

   public AbstractDescriptorLoader2 getWlsApplicationDescriptorLoader() {
      return this.wlsAppDescriptor;
   }

   public AbstractDescriptorLoader2 getWlsExtensionDescriptorLoader() {
      return this.wlsExtDescriptor;
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 1) {
         usage();
      }

      DescriptorManager var1 = new DescriptorManager();
      if (var0[0].lastIndexOf("create") > -1) {
         Descriptor var2 = var1.createDescriptorRoot(ApplicationBean.class);
         var1.writeDescriptorAsXML(var2, System.out);
         System.out.println("\n\n\n");
         var2 = var1.createDescriptorRoot(WeblogicApplicationBean.class);
         var1.writeDescriptorAsXML(var2, System.out);
         System.exit(0);
      }

      try {
         String var17 = var0[0];
         File var3 = new File(var17);
         if (!var3.getName().endsWith(".ear") && !var3.isDirectory()) {
            ApplicationDescriptor var18;
            if (var3.getPath().endsWith("weblogic-application.xml")) {
               System.out.println("\n\n... getting WeblogicApplicationBean:");
               var18 = new ApplicationDescriptor((File)null, var3, (File)null, (File)null, (DeploymentPlanBean)null, (String)null);
               var1.writeDescriptorAsXML(((DescriptorBean)var18.getWeblogicApplicationDescriptor()).getDescriptor(), System.out);
               if (var0.length > 1) {
                  File var19 = new File(var0[1]);
                  if (var19.getPath().endsWith("weblogic-application.xml")) {
                     System.out.println("\n\n... merged WeblogicApplicationBean:");
                     var1.writeDescriptorAsXML(((DescriptorBean)var18.mergeWlsApplicationDescriptor(var19)).getDescriptor(), System.out);
                  }

                  if (var19.getPath().endsWith("plan.xml")) {
                     System.out.println("\n\n... plan:");
                     DeploymentPlanDescriptorLoader var21 = new DeploymentPlanDescriptorLoader(var19);
                     DeploymentPlanBean var23 = var21.getDeploymentPlanBean();
                     var1.writeDescriptorAsXML(((DescriptorBean)var23).getDescriptor(), System.out);
                     ApplicationDescriptor var25 = new ApplicationDescriptor((File)null, var3, (File)null, new File(var23.getConfigRoot()), var23, var23.getApplicationName());
                     System.out.println("\n\n... plan merged with WeblogicApplicationBean:");
                     var1.writeDescriptorAsXML(((DescriptorBean)var25.getWeblogicApplicationDescriptor()).getDescriptor(), System.out);
                  }
               }
            } else {
               DescriptorBean var20;
               if (var3.getPath().endsWith("application.xml")) {
                  System.out.println("\n\n... getting WeblogicApplicationBean:");
                  var18 = new ApplicationDescriptor(var3, (File)null, (File)null, (File)null, (DeploymentPlanBean)null, (String)null);
                  var20 = (DescriptorBean)var18.getApplicationDescriptor();
                  var1.writeDescriptorAsXML(var20.getDescriptor(), System.out);
                  if (var0.length > 1) {
                     File var22 = new File(var0[1]);
                     ApplicationDescriptor var24 = new ApplicationDescriptor(var22, (File)null, (File)null, (File)null, (DeploymentPlanBean)null, (String)null);
                     System.out.println("\n\n... dump 2nd bean:");
                     DescriptorBean var26 = (DescriptorBean)var24.getApplicationDescriptor();
                     var1.writeDescriptorAsXML(var26.getDescriptor(), System.out);
                     System.out.println("\n\n... compared to:");
                     var1.writeDescriptorAsXML(((DescriptorBean)var18.mergeApplicationDescriptor(var22)).getDescriptor(), System.out);
                     if (var0.length > 2) {
                        File var27 = new File(var0[2]);
                        ApplicationDescriptor var10 = new ApplicationDescriptor(var27, (File)null, (File)null, (File)null, (DeploymentPlanBean)null, (String)null);
                        System.out.println("\n\n... dump 2nd bean:");
                        DescriptorBean var11 = (DescriptorBean)var10.getApplicationDescriptor();
                        var1.writeDescriptorAsXML(var11.getDescriptor(), System.out);
                        ApplicationDescriptor var12 = new ApplicationDescriptor();
                        DescriptorBean var13 = (DescriptorBean)var12.getApplicationDescriptor();
                        System.out.println("\n\n... getting null application bean :");
                        var1.writeDescriptorAsXML(var13.getDescriptor(), System.out);
                        var12.mergeApplicationDescriptor(var3);
                        var12.mergeApplicationDescriptor(var22);
                        var12.mergeApplicationDescriptor(var27);
                        var13 = (DescriptorBean)var12.getApplicationDescriptor();
                        System.out.println("\n\n... getting null application bean after merge:");
                        var1.writeDescriptorAsXML(var13.getDescriptor(), System.out);
                        ApplicationBean var14 = var18.getApplicationDescriptor();
                        ModuleBean var15 = var14.createModule();
                        var15.setConnector("my-made-up-connector");
                        System.out.println("\n\n... dump first bean-- should have my-made-up-connector: " + ((DescriptorBean)var14).getDescriptor());
                        var1.writeDescriptorAsXML(((DescriptorBean)var14).getDescriptor(), System.out);
                        System.out.println("\n\n... compared to:");
                        var1.writeDescriptorAsXML(((DescriptorBean)var18.mergeApplicationDescriptor(var27)).getDescriptor(), System.out);
                     }
                  }
               } else if (var3.getPath().endsWith("weblogic-extension.xml")) {
                  System.out.println("\n\n... getting WeblogicExtensionBean:");
                  var18 = new ApplicationDescriptor();
                  var18.mergeWlsExtentionDescriptor(var3);
                  var20 = (DescriptorBean)var18.getWeblogicExtensionDescriptor();
                  var1.writeDescriptorAsXML(var20.getDescriptor(), System.out);
               } else {
                  System.out.println("\n\n... neither application nor weblogic-application xml specified");
               }
            }
         } else {
            VirtualJarFile var4 = var3.getName().endsWith(".ear") ? VirtualJarFactory.createVirtualJar(new JarFile(var17)) : VirtualJarFactory.createVirtualJar(var3);
            ApplicationDescriptor var5 = new ApplicationDescriptor(var4, (File)null, (DeploymentPlanBean)null, (String)null);
            var1.writeDescriptorAsXML(((DescriptorBean)var5.getApplicationDescriptor()).getDescriptor(), System.out);
            var1.writeDescriptorAsXML(((DescriptorBean)var5.getWeblogicApplicationDescriptor()).getDescriptor(), System.out);
            if (var0.length > 1) {
               for(int var6 = 1; var6 < var0.length; ++var6) {
                  JarFile var7 = new JarFile(var0[var6]);
                  VirtualJarFile var8 = VirtualJarFactory.createVirtualJar(var7);
                  System.out.println("\n\n... output lib descriptors to merge:");
                  ApplicationDescriptor var9 = new ApplicationDescriptor(var8, (File)null, (DeploymentPlanBean)null, (String)null);
                  var1.writeDescriptorAsXML(((DescriptorBean)var9.getApplicationDescriptor()).getDescriptor(), System.out);
                  var5.getApplicationDescriptor().setDescriptions(new String[]{"from depths so profound come enduring change..."});
                  var5.mergeDescriptors(var8);
                  System.out.println("\n\n... getting merged ApplicationBean:");
                  var1.writeDescriptorAsXML(((DescriptorBean)var5.getApplicationDescriptor()).getDescriptor(), System.out);
               }
            }
         }
      } catch (Exception var16) {
         System.out.println(var16.toString());
         System.out.println(var16.getMessage());
         System.out.println(var16.getCause());
         var16.printStackTrace();
         System.exit(1);
      }

   }

   private static void usage() {
      System.err.println("usage: java weblogic.application.ApplicationDescriptor <descriptor file name>");
      System.err.println("\n\n example:\n java weblogic.application.ApplicationDescriptor ear or file name ");
      System.exit(0);
   }

   public void printAppDescriptors(PrintStream var1) {
      try {
         var1.println("----------------------------------");
      } catch (Exception var3) {
         Debug.say("Got this: " + var3);
      }

   }

   static {
      kernelId = isServer ? (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction()) : null;
   }

   public static class MyWlsExtensionDescriptor extends CachingDescriptorLoader2 {
      private InputStream is;

      public MyWlsExtensionDescriptor(VirtualJarFile var1) {
         super((VirtualJarFile)var1, (File)null, (DeploymentPlanBean)null, (String)null, "META-INF/weblogic-extension.xml");
      }

      public MyWlsExtensionDescriptor(File var1) {
         super((File)var1, (File)null, (DeploymentPlanBean)null, (String)null, "META-INF/weblogic-extension.xml");
      }

      public MyWlsExtensionDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4) {
         this((File)var1, var2, var3, var4, (String)null);
      }

      public MyWlsExtensionDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super(var1, var2, var3, var4, "META-INF/weblogic-extension.xml");
      }

      public MyWlsExtensionDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4) {
         this((VirtualJarFile)var1, var2, var3, var4, (String)null);
      }

      public MyWlsExtensionDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super(var1, var2, var3, var4, "META-INF/weblogic-extension.xml");
      }

      public MyWlsExtensionDescriptor(DescriptorManager var1, GenericClassLoader var2) {
         super(var1, var2, (File)null, (DeploymentPlanBean)null, (String)null, "META-INF/weblogic-extension.xml");
      }

      public MyWlsExtensionDescriptor(InputStream var1) {
         super((VirtualJarFile)((VirtualJarFile)null), (File)null, (DeploymentPlanBean)null, (String)null, "META-INF/weblogic-extension.xml");
         this.is = var1;
         this.setValidateSchema(false);
      }

      public InputStream getInputStream() throws IOException {
         return this.is == null ? super.getInputStream() : this.is;
      }

      public String getDocumentURI() {
         return "META-INF/weblogic-extension.xml";
      }

      protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
         return new WlsExtensionReader(var1, this);
      }

      void setValidate(boolean var1) {
         super.setValidateSchema(var1);
      }
   }

   public static class MyWlsApplicationDescriptor extends CachingDescriptorLoader2 {
      private InputStream is;

      public MyWlsApplicationDescriptor(File var1) {
         super((File)var1, (File)null, (DeploymentPlanBean)null, (String)null, "META-INF/weblogic-application.xml");
         this.is = null;
      }

      public MyWlsApplicationDescriptor(VirtualJarFile var1) {
         super((VirtualJarFile)var1, (File)null, (DeploymentPlanBean)null, (String)null, "META-INF/weblogic-application.xml");
         this.is = null;
      }

      public MyWlsApplicationDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4) {
         this((File)var1, (File)var2, (DeploymentPlanBean)var3, (String)var4, (String)null);
      }

      public MyWlsApplicationDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super(var1, var2, var3, var4, "META-INF/weblogic-application.xml");
         this.is = null;
      }

      public MyWlsApplicationDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4) {
         this((VirtualJarFile)var1, (File)var2, (DeploymentPlanBean)var3, (String)var4, (String)null);
      }

      public MyWlsApplicationDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super(var1, var2, var3, var4, "META-INF/weblogic-application.xml");
         this.is = null;
      }

      public MyWlsApplicationDescriptor(InputStream var1) {
         super((VirtualJarFile)((VirtualJarFile)null), (File)null, (DeploymentPlanBean)null, (String)null, "META-INF/weblogic-application.xml");
         this.is = null;
         this.is = var1;
         this.setValidateSchema(false);
      }

      public MyWlsApplicationDescriptor(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5) {
         super(var1, var2, var3, var4, var5, "META-INF/weblogic-application.xml");
         this.is = null;
      }

      public MyWlsApplicationDescriptor(DescriptorManager var1, GenericClassLoader var2) {
         super(var1, var2, "META-INF/weblogic-application.xml");
         this.is = null;
      }

      public InputStream getInputStream() throws IOException {
         return this.is == null ? super.getInputStream() : this.is;
      }

      public String getDocumentURI() {
         return "META-INF/weblogic-application.xml";
      }

      protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
         return new WlsApplicationReader(var1, this);
      }

      void setValidate(boolean var1) {
         super.setValidateSchema(var1);
      }
   }

   public static class MyApplicationDescriptor extends CachingDescriptorLoader2 {
      private InputStream is;

      public MyApplicationDescriptor(File var1) {
         super((File)var1, (File)null, (DeploymentPlanBean)null, (String)null, "META-INF/application.xml");
         this.is = null;
      }

      public MyApplicationDescriptor(VirtualJarFile var1) {
         super((VirtualJarFile)var1, (File)null, (DeploymentPlanBean)null, (String)null, "META-INF/application.xml");
         this.is = null;
      }

      public MyApplicationDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4) {
         this((File)var1, (File)var2, (DeploymentPlanBean)var3, (String)var4, (String)null);
      }

      public MyApplicationDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super(var1, var2, var3, var4, "META-INF/application.xml");
         this.is = null;
      }

      public MyApplicationDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4) {
         this((VirtualJarFile)var1, (File)var2, (DeploymentPlanBean)var3, (String)var4, (String)null);
      }

      public MyApplicationDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super(var1, var2, var3, var4, "META-INF/application.xml");
         this.is = null;
      }

      public MyApplicationDescriptor(InputStream var1) {
         super((VirtualJarFile)((VirtualJarFile)null), (File)null, (DeploymentPlanBean)null, (String)null, "META-INF/application.xml");
         this.is = null;
         this.is = var1;
         this.setValidateSchema(false);
      }

      public MyApplicationDescriptor(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5) {
         super(var1, var2, var3, var4, var5, "META-INF/application.xml");
         this.is = null;
      }

      public MyApplicationDescriptor(DescriptorManager var1, GenericClassLoader var2) {
         this((DescriptorManager)var1, (GenericClassLoader)var2, (File)null, (DeploymentPlanBean)null, (String)null);
      }

      public InputStream getInputStream() throws IOException {
         return this.is == null ? super.getInputStream() : this.is;
      }

      protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
         return new ApplicationReader(var1, this);
      }

      void setValidate(boolean var1) {
         super.setValidateSchema(var1);
      }
   }
}
