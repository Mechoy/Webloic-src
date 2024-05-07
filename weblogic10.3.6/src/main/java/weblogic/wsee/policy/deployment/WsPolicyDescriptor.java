package weblogic.wsee.policy.deployment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.jar.JarFile;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.descriptor.VersionMunger;
import weblogic.deploy.internal.DeploymentPlanDescriptorLoader;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WebservicePolicyRefBean;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class WsPolicyDescriptor {
   public static final String WLS_WS_POLICY_WEB_URI = "WEB-INF/weblogic-webservices-policy.xml";
   public static final String WLS_WS_POLICY_EJB_URI = "META-INF/weblogic-webservices-policy.xml";
   private MyWsPolicyDescriptor wsPolicyDescriptor;

   public WsPolicyDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4) {
      this.wsPolicyDescriptor = new MyWsPolicyDescriptor(var1, var2, var3, var4);
   }

   public WsPolicyDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, boolean var5) {
      this.wsPolicyDescriptor = new MyWsPolicyDescriptor(var1, var2, var3, var4, var5);
   }

   public WsPolicyDescriptor(WebAppServletContext var1, File var2, DeploymentPlanBean var3, String var4) throws IOException, XMLStreamException {
      this.wsPolicyDescriptor = new MyWsPolicyDescriptor(var1, var2, var3, var4);
      this.mergePolicyDescriptors(var1);
   }

   public WsPolicyDescriptor(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5, boolean var6) {
      this.wsPolicyDescriptor = new MyWsPolicyDescriptor(var1, var2, var3, var4, var5, var6);
   }

   private void mergePolicyDescriptors(WebAppServletContext var1) throws IOException, XMLStreamException {
      String var2 = "/WEB-INF/weblogic-webservices-policy.xml";
      Enumeration var3 = var1.getResourceFinder("/").getSources(var2);
      Object[] var4 = Collections.list(var3).toArray();
      if (var4.length > 1) {
         this.wsPolicyDescriptor.mergeDescriptors(var4);
      }

   }

   public DeploymentPlanBean getDeploymentPlan() {
      return this.wsPolicyDescriptor.getDeploymentPlan();
   }

   public WebservicePolicyRefBean getWebservicesPolicyBean() throws IOException, XMLStreamException {
      WebservicePolicyRefBean var1 = (WebservicePolicyRefBean)this.wsPolicyDescriptor.loadDescriptorBean();
      if (var1 == null) {
         EditableDescriptorManager var2 = new EditableDescriptorManager();
         var1 = (WebservicePolicyRefBean)var2.createDescriptorRoot(WebservicePolicyRefBean.class).getRootBean();
      }

      return var1;
   }

   public static void main(String[] var0) throws Exception {
      String var1 = var0[0];
      File var2 = new File(var1);
      if (var2.getName().endsWith(".war") || var2.getName().endsWith(".jar")) {
         JarFile var3 = new JarFile(var1);
         VirtualJarFile var4 = VirtualJarFactory.createVirtualJar(var3);
         WsPolicyDescriptor var5 = new WsPolicyDescriptor(var4, (File)null, (DeploymentPlanBean)null, (String)null);
         WebservicePolicyRefBean var6 = var5.getWebservicesPolicyBean();
         if (var6 != null) {
            Descriptor var7 = ((DescriptorBean)var6).getDescriptor();
            if (var7 != null) {
               var7.toXML(System.out);
            }
         }

         System.out.println("\n\n... getting WebservicePolicyBean:");
         if (var0.length > 1) {
            File var11 = new File(var0[1]);
            if (var11.getPath().endsWith("plan.xml")) {
               System.out.println("\n\n... plan:");
               DeploymentPlanDescriptorLoader var8 = new DeploymentPlanDescriptorLoader(var11);
               DeploymentPlanBean var9 = var8.getDeploymentPlanBean();
               ((DescriptorBean)var9).getDescriptor().toXML(System.out);
               System.out.println("\n\nConfig root = " + var9.getConfigRoot());
               System.out.println("\nApplication name = " + var9.getApplicationName());
               WsPolicyDescriptor var10 = new WsPolicyDescriptor(var4, new File(var9.getConfigRoot()), var9, var1);
               System.out.println("\n\n... plan merged WeblogicWebservicesBean with :");
               ((DescriptorBean)var10.getWebservicesPolicyBean()).getDescriptor().toXML(System.out);
            }
         }
      }

   }

   public static class MyWsPolicyDescriptor extends MyAbstractDescriptorLoader {
      public MyWsPolicyDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4) {
         super(var1, var2, var3, var4);
      }

      public MyWsPolicyDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, boolean var5) {
         super(var1, var2, var3, var4);
         this.useWarPath = var5;
      }

      public MyWsPolicyDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super(var1, var2, var3, var4, var5);
      }

      public MyWsPolicyDescriptor(WebAppServletContext var1, File var2, DeploymentPlanBean var3, String var4) {
         super(var1, var2, var3, var4);
      }

      public MyWsPolicyDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4) {
         super(var1, var2, var3, var4);
      }

      public MyWsPolicyDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super(var1, var2, var3, var4, var5);
      }

      public MyWsPolicyDescriptor(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5, boolean var6) {
         super(var1, var2, var3, var4, var5);
         this.useWarPath = var6;
      }

      public String getDocumentURI() {
         return this.useWarPath ? "WEB-INF/weblogic-webservices-policy.xml" : "META-INF/weblogic-webservices-policy.xml";
      }

      protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
         String var2 = "weblogic.j2ee.descriptor.wl.WebservicePolicyRefBeanImpl$SchemaHelper2";
         return new VersionMunger(var1, this, var2, "http://xmlns.oracle.com/weblogic/webservice-policy-ref");
      }
   }

   public abstract static class MyAbstractDescriptorLoader extends AbstractDescriptorLoader2 {
      protected boolean useWarPath = false;
      protected WebAppServletContext svltCtx;

      public MyAbstractDescriptorLoader(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4) {
         super((VirtualJarFile)var1, var2, var3, var4, (String)null);
         if (var1.getName().endsWith(".war")) {
            this.useWarPath = true;
         }

      }

      public MyAbstractDescriptorLoader(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super(var1, var2, var3, var4, var5);
         if (var1.getName().endsWith(".war")) {
            this.useWarPath = true;
         }

      }

      public MyAbstractDescriptorLoader(WebAppServletContext var1, File var2, DeploymentPlanBean var3, String var4) {
         super((VirtualJarFile)((VirtualJarFile)null), var2, var3, var4, (String)null);
         this.useWarPath = true;
         this.svltCtx = var1;
      }

      public MyAbstractDescriptorLoader(File var1, File var2, DeploymentPlanBean var3, String var4) {
         super((File)var1, var2, var3, var4, (String)null);
      }

      public MyAbstractDescriptorLoader(File var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super(var1, var2, var3, var4, var5);
      }

      MyAbstractDescriptorLoader(DescriptorManager var1, GenericClassLoader var2) {
         super(var1, var2, (String)null);
      }

      MyAbstractDescriptorLoader(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5) {
         super(var1, var2, var3, var4, var5, (String)null);
      }

      public InputStream getInputStream() throws IOException {
         return this.svltCtx != null ? this.svltCtx.getResourceAsStream(this.getDocumentURI()) : super.getInputStream();
      }
   }
}
