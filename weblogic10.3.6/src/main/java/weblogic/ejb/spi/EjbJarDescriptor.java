package weblogic.ejb.spi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.deploy.internal.DeploymentPlanDescriptorLoader;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.ejb.container.utils.AutoRefLibHelper;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class EjbJarDescriptor {
   public static final String STANDARD_DD = "META-INF/ejb-jar.xml";
   public static final String WEBLOGIC_DD = "META-INF/weblogic-ejb-jar.xml";
   private MyEjbJarDescriptor ejbJarDescriptor;
   private MyWlsEjbJarDescriptor wlsEjbJarDescriptor;
   private boolean isWritable = false;
   private EditableDescriptorManager edm = new EditableDescriptorManager();

   public EjbJarDescriptor(File var1, String var2, String var3) {
      ApplicationContextInternal var4 = ApplicationAccess.getApplicationAccess().getApplicationContext(var2);
      File var5 = null;
      DeploymentPlanBean var6 = null;
      if (var4 != null) {
         var6 = var4.findDeploymentPlan();
         AppDeploymentMBean var7 = var4.getAppDeploymentMBean();
         if (var7.getPlanDir() != null) {
            var5 = new File(var7.getLocalPlanDir());
         }
      }

      if (var1.getPath().endsWith("weblogic-ejb-jar.xml")) {
         this.wlsEjbJarDescriptor = new MyWlsEjbJarDescriptor(var1, var5, var6, var3, this.ejbJarDescriptor);
      } else {
         this.ejbJarDescriptor = new MyEjbJarDescriptor(var1, var5, var6, var2, var3);
      }

   }

   public EjbJarDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
      if (var1.getPath().endsWith("weblogic-ejb-jar.xml")) {
         this.wlsEjbJarDescriptor = new MyWlsEjbJarDescriptor(var1, var2, var3, var5, this.ejbJarDescriptor);
      } else {
         this.ejbJarDescriptor = new MyEjbJarDescriptor(var1, var2, var3, var4, var5);
      }

   }

   public EjbJarDescriptor(VirtualJarFile var1, String var2, String var3) {
      ApplicationContextInternal var4 = ApplicationAccess.getApplicationAccess().getApplicationContext(var2);
      File var5 = null;
      DeploymentPlanBean var6 = null;
      if (var4 != null) {
         var6 = var4.findDeploymentPlan();
         AppDeploymentMBean var7 = var4.getAppDeploymentMBean();
         if (var7.getPlanDir() != null) {
            var5 = new File(var7.getLocalPlanDir());
         }
      }

      this.ejbJarDescriptor = new MyEjbJarDescriptor(var1, var5, var6, var2, var3);
      this.wlsEjbJarDescriptor = new MyWlsEjbJarDescriptor(var1, var5, var6, var3, this.ejbJarDescriptor);
   }

   public EjbJarDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
      this.ejbJarDescriptor = new MyEjbJarDescriptor(var1, var2, var3, var4, var5);
      this.wlsEjbJarDescriptor = new MyWlsEjbJarDescriptor(var1, var2, var3, var5, this.ejbJarDescriptor);
   }

   public EjbJarDescriptor(DescriptorManager var1, GenericClassLoader var2) {
      this.ejbJarDescriptor = new MyEjbJarDescriptor(var1, var2);
      this.wlsEjbJarDescriptor = new MyWlsEjbJarDescriptor(var1, var2);
   }

   public EjbJarDescriptor(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5) {
      this.ejbJarDescriptor = new MyEjbJarDescriptor(var1, var2, var3, var4, var5, "META-INF/ejb-jar.xml");
      this.wlsEjbJarDescriptor = new MyWlsEjbJarDescriptor(var1, var2, var3, var4, var5);
   }

   public EjbJarDescriptor(File var1, File var2, File var3, DeploymentPlanBean var4, String var5) {
      this.ejbJarDescriptor = new MyEjbJarDescriptor(var1, var3, var4, var5, "META-INF/ejb-jar.xml");
      this.wlsEjbJarDescriptor = new MyWlsEjbJarDescriptor(var2, var3, var4, var5, (MyEjbJarDescriptor)null);
   }

   public void mergeEJBJar(VirtualJarFile[] var1) throws IOException, XMLStreamException {
      AutoRefLibHelper.mergeEJBJar(this, var1);
   }

   public EjbJarBean getEjbJarBean() throws IOException, XMLStreamException {
      return (EjbJarBean)this.ejbJarDescriptor.loadDescriptorBean();
   }

   public WeblogicEjbJarBean getWeblogicEjbJarBean() throws IOException, XMLStreamException {
      if (this.wlsEjbJarDescriptor == null) {
         return null;
      } else {
         EjbDescriptorReader var1 = EjbDescriptorFactory.getEjbDescriptorReader();
         return var1.parseWebLogicEjbJarXML(this, this.wlsEjbJarDescriptor.getDeploymentPlan(), this.wlsEjbJarDescriptor.getConfigDir(), this.wlsEjbJarDescriptor.getModuleName(), this.wlsEjbJarDescriptor.getInputStream(), this.ejbJarDescriptor == null ? null : this.getEjbJarBean(), (String)null, true);
      }
   }

   public WeblogicEjbJarBean parseWeblogicEjbJarBean() throws IOException, XMLStreamException {
      return this.wlsEjbJarDescriptor == null ? null : (WeblogicEjbJarBean)this.wlsEjbJarDescriptor.loadDescriptorBean();
   }

   public EjbJarBean getEditableEjbJarBean() throws IOException, XMLStreamException {
      return (EjbJarBean)this.ejbJarDescriptor.loadEditableDescriptorBean();
   }

   public WeblogicEjbJarBean getEditableWeblogicEjbJarBean() throws IOException, XMLStreamException {
      if (this.wlsEjbJarDescriptor == null) {
         return null;
      } else {
         EjbDescriptorReader var1 = EjbDescriptorFactory.getEjbDescriptorReader();
         return var1.parseWebLogicEjbJarXML(this, this.wlsEjbJarDescriptor.getDeploymentPlan(), this.wlsEjbJarDescriptor.getConfigDir(), this.wlsEjbJarDescriptor.getModuleName(), this.wlsEjbJarDescriptor.getInputStream(), this.getEjbJarBean(), (String)null, false);
      }
   }

   public WeblogicEjbJarBean parseEditableWeblogicEjbJarBean() throws IOException, XMLStreamException {
      this.isWritable = true;
      WeblogicEjbJarBean var1 = this.parseWeblogicEjbJarBean();
      this.isWritable = false;
      return var1;
   }

   public AbstractDescriptorLoader2 getEjbDescriptorLoader() {
      return this.ejbJarDescriptor;
   }

   public AbstractDescriptorLoader2 getWlsEjbDescriptorLoader() {
      return this.wlsEjbJarDescriptor;
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 1) {
         usage();
      }

      if (var0[0].lastIndexOf("create") > -1) {
         DescriptorManager var1 = new DescriptorManager();
         Descriptor var2 = var1.createDescriptorRoot(EjbJarBean.class);
         var2.toXML(System.out);
         System.out.println("\n\n\n");
         var2 = var1.createDescriptorRoot(WeblogicEjbJarBean.class);
         var2.toXML(System.out);
         System.exit(0);
      }

      try {
         String var9 = var0[0];
         File var10 = new File(var9);
         VirtualJarFile var4;
         if (var10.getName().endsWith(".jar")) {
            JarFile var3 = new JarFile(var9);
            var4 = VirtualJarFactory.createVirtualJar(var3);
            System.out.println("\n\n... getting EjbJarBean:");
            EjbJarBean var5 = (new EjbJarDescriptor(var4, (String)null, (String)null)).getEjbJarBean();
            ((DescriptorBean)var5).getDescriptor().toXML(System.out);
            System.out.println("\n\n... getting WeblogicEjbBean:");
            WeblogicEjbJarBean var6 = (new EjbJarDescriptor(var4, (String)null, (String)null)).getWeblogicEjbJarBean();
            ((DescriptorBean)var6).getDescriptor().toXML(System.out);
         } else if (var10.getPath().endsWith("weblogic-ejb-jar.xml")) {
            System.out.println("\n\n... getting WeblogicEjbBean:");
            WeblogicEjbJarBean var11 = (new EjbJarDescriptor(var10, (String)null, (String)null)).getWeblogicEjbJarBean();
            ((DescriptorBean)var11).getDescriptor().toXML(System.out);
            if (var0.length > 1) {
               File var13 = new File(var0[1]);
               if (var13.getPath().endsWith("plan.xml")) {
                  System.out.println("\n\n... plan:");
                  DeploymentPlanDescriptorLoader var16 = new DeploymentPlanDescriptorLoader(var13);
                  DeploymentPlanBean var19 = var16.getDeploymentPlanBean();
                  ((DescriptorBean)var19).getDescriptor().toXML(System.out);
                  if (var0.length > 2) {
                     EjbJarDescriptor var7 = new EjbJarDescriptor((File)null, var10, new File(var19.getConfigRoot()), var19, var0[2]);
                     System.out.println("\n\n... plan merged:");
                     ((DescriptorBean)var7.getWeblogicEjbJarBean()).getDescriptor().toXML(System.out);
                  } else {
                     System.out.println("\n\nNO MODULE NAME\n\nusage: java weblogic.ejb20.deployer.EjbJarDescriptor weblogic-ejb-jar.xml plan.xml module-name");
                  }
               }
            }
         } else {
            File var12;
            if (var10.getPath().endsWith("ejb-jar.xml")) {
               var12 = var0.length == 2 ? new File(var0[1]) : null;
               if (var12 != null && var12.getPath().endsWith("weblogic-ejb-jar.xml")) {
                  System.out.println("\n\n... getting EjbJarBean:");
                  var4 = null;
                  EjbJarDescriptor var17 = new EjbJarDescriptor(var10, var12, (File)null, var4, (String)null);
                  EjbJarBean var20 = var17.getEjbJarBean();
                  ((DescriptorBean)var20).getDescriptor().toXML(System.out);
                  System.out.println("\n\n... getting WlsEjbJarBean:");
                  WeblogicEjbJarBean var22 = var17.getWeblogicEjbJarBean();
                  ((DescriptorBean)var22).getDescriptor().toXML(System.out);
               } else {
                  System.out.println("\n\n... getting EjbJarBean:");
                  EjbJarBean var14 = (new EjbJarDescriptor(var10, (String)null, (String)null)).getEjbJarBean();
                  ((DescriptorBean)var14).getDescriptor().toXML(System.out);
               }
            }

            if (var0.length == 3) {
               var12 = new File(var0[1]);
               DeploymentPlanDescriptorLoader var15 = new DeploymentPlanDescriptorLoader(var12);
               DeploymentPlanBean var18 = var15.getDeploymentPlanBean();
               EjbJarDescriptor var21 = new EjbJarDescriptor(var10, (File)null, (File)null, var18, var0[2]);
               System.out.println("\n\n... plan merged:");
               ((DescriptorBean)var21.getEjbJarBean()).getDescriptor().toXML(System.out);
            } else {
               System.out.println("\n\n... neither ejb-jar nor weblogic-ejb-jar xml specified");
            }
         }
      } catch (Throwable var8) {
         System.out.println("\n\n printing problem");
         System.out.println(var8.toString());
         System.out.println(var8.getMessage());
         System.out.println(var8.getCause());
         var8.printStackTrace();
      }

   }

   private static void usage() {
      System.err.println("usage: java weblogic.ejb20.deployer.EjbJarDescriptor <descriptor file name>");
      System.err.println("\n\n example:\n java weblogic.ejb20.deployer.EjbJarDescriptor jar or altDD file name ");
      System.exit(0);
   }

   private class MyWlsEjbJarDescriptor extends AbstractDescriptorLoader2 {
      MyEjbJarDescriptor ejbJarDescriptor;

      MyWlsEjbJarDescriptor(VirtualJarFile var2, File var3, DeploymentPlanBean var4, String var5, MyEjbJarDescriptor var6) {
         super(var2, var3, var4, var5, "META-INF/weblogic-ejb-jar.xml");
         this.ejbJarDescriptor = var6;
      }

      MyWlsEjbJarDescriptor(File var2, File var3, DeploymentPlanBean var4, String var5, MyEjbJarDescriptor var6) {
         super(var2, var3, var4, var5, "META-INF/weblogic-ejb-jar.xml");
         this.ejbJarDescriptor = var6;
      }

      MyWlsEjbJarDescriptor(DescriptorManager var2, GenericClassLoader var3) {
         this((DescriptorManager)var2, (GenericClassLoader)var3, (File)null, (DeploymentPlanBean)null, (String)null);
      }

      MyWlsEjbJarDescriptor(DescriptorManager var2, GenericClassLoader var3, File var4, DeploymentPlanBean var5, String var6) {
         super(var2, var3, var4, var5, var6, "META-INF/weblogic-ejb-jar.xml");
      }

      public String getDocumentURI() {
         return "META-INF/weblogic-ejb-jar.xml";
      }

      protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
         return new WeblogicEjbJarReader(var1, this);
      }

      protected DescriptorManager getDescriptorManager() {
         return (DescriptorManager)(EjbJarDescriptor.this.isWritable ? EjbJarDescriptor.this.edm : super.getDescriptorManager());
      }
   }

   public static class MyEjbJarDescriptor extends AbstractDescriptorLoader2 {
      private boolean isWritable;
      private EditableDescriptorManager edm;

      MyEjbJarDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super(var1, var2, var3, var4, "META-INF/ejb-jar.xml");
         this.isWritable = false;
         this.edm = new EditableDescriptorManager();
      }

      public MyEjbJarDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super(var1, var2, var3, var5, "META-INF/ejb-jar.xml");
         this.isWritable = false;
         this.edm = new EditableDescriptorManager();
      }

      MyEjbJarDescriptor(DescriptorManager var1, GenericClassLoader var2) {
         this(var1, var2, (File)null, (DeploymentPlanBean)null, (String)null, "META-INF/ejb-jar.xml");
      }

      MyEjbJarDescriptor(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5, String var6) {
         super(var1, var2, var3, var4, var5, "META-INF/ejb-jar.xml");
         this.isWritable = false;
         this.edm = new EditableDescriptorManager();
      }

      public String getDocumentURI() {
         return "META-INF/ejb-jar.xml";
      }

      protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
         return new EjbJarReader(var1, this);
      }

      protected DescriptorManager getDescriptorManager() {
         return (DescriptorManager)(this.isWritable ? this.edm : super.getDescriptorManager());
      }

      protected DescriptorBean loadEditableDescriptorBean() throws IOException, XMLStreamException {
         this.isWritable = true;
         DescriptorBean var1 = this.loadDescriptorBean();
         this.isWritable = false;
         return var1;
      }
   }
}
