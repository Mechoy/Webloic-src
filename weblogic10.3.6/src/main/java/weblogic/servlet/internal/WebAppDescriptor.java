package weblogic.servlet.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.descriptor.CachingDescriptorLoader2;
import weblogic.deploy.internal.DeploymentPlanDescriptorLoader;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.utils.classloaders.DirectoryClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.Source;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class WebAppDescriptor implements WebAppInternalParser {
   private static final String STANDARD_DD = "WEB-INF/web.xml";
   private static final String WEBLOGIC_DD = "WEB-INF/weblogic.xml";
   private MyWebAppDescriptor webAppDescriptor;
   private MyWlsWebAppDescriptor wlsWebAppDescriptor;
   private boolean hasWebDescriptorFile;
   private boolean validateSchema;

   public WebAppDescriptor(File var1, File var2, File var3, DeploymentPlanBean var4, String var5) {
      this.hasWebDescriptorFile = true;
      this.validateSchema = true;
      this.webAppDescriptor = new MyWebAppDescriptor(var1, var3, var4, var5);
      this.wlsWebAppDescriptor = new MyWlsWebAppDescriptor(var2, var3, var4, var5);
   }

   public WebAppDescriptor(File var1, VirtualJarFile var2, File var3, DeploymentPlanBean var4, String var5) {
      this.hasWebDescriptorFile = true;
      this.validateSchema = true;
      this.webAppDescriptor = new MyWebAppDescriptor(var1, var3, var4, var5);
      this.wlsWebAppDescriptor = new MyWlsWebAppDescriptor(var2, var3, var4, var5);
   }

   public WebAppDescriptor(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5) {
      this.hasWebDescriptorFile = true;
      this.validateSchema = true;
      this.webAppDescriptor = new MyWebAppDescriptor(var1, var2, var3, var4, var5);
      this.wlsWebAppDescriptor = new MyWlsWebAppDescriptor(var1, var2, var3, var4, var5);
   }

   public WebAppDescriptor(DescriptorManager var1, GenericClassLoader var2) {
      this.hasWebDescriptorFile = true;
      this.validateSchema = true;
      this.webAppDescriptor = new MyWebAppDescriptor(var1, var2);
      this.wlsWebAppDescriptor = new MyWlsWebAppDescriptor(var1, var2);
   }

   public WebAppDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4) {
      this.hasWebDescriptorFile = true;
      this.validateSchema = true;
      this.webAppDescriptor = new MyWebAppDescriptor(var1, var2, var3, var4);
      this.wlsWebAppDescriptor = new MyWlsWebAppDescriptor(var1, var2, var3, var4);
   }

   public WebAppDescriptor(File var1) {
      this((File)var1, (File)((File)null), (File)null, (DeploymentPlanBean)null, (String)null);
   }

   public WebAppDescriptor(VirtualJarFile var1) {
      this(var1, (File)null, (DeploymentPlanBean)null, (String)null);
   }

   public void setValidateSchema(boolean var1) {
      this.validateSchema = var1;
   }

   public void writeDescriptors(File var1) throws IOException, XMLStreamException {
      DescriptorManager var2 = new DescriptorManager();
      File var3 = null;
      DescriptorBean var4 = (DescriptorBean)this.getWebAppBean();
      if (var4 != null) {
         var3 = new File(var1, this.webAppDescriptor.getDocumentURI());
         DescriptorUtils.writeDescriptor(var2, var4, var3);
      }

      var4 = (DescriptorBean)this.getWeblogicWebAppBean();
      if (var4 != null) {
         var3 = new File(var1, this.wlsWebAppDescriptor.getDocumentURI());
         DescriptorUtils.writeDescriptor(var2, var4, var3);
      }

   }

   public WebAppBean getWebAppBean() throws IOException, XMLStreamException {
      if (!this.validateSchema) {
         this.webAppDescriptor.setValidate(this.validateSchema);
      }

      WebAppBean var1 = (WebAppBean)this.webAppDescriptor.loadDescriptorBean();
      if (var1 == null) {
         this.hasWebDescriptorFile = false;
         var1 = this.createWebAppBean();
         this.webAppDescriptor.updateDescriptorWithBean((DescriptorBean)var1);
      }

      return var1;
   }

   public boolean hasWebDescriptorFile() {
      return this.hasWebDescriptorFile;
   }

   public WeblogicWebAppBean getWeblogicWebAppBean() throws IOException, XMLStreamException {
      if (this.wlsWebAppDescriptor == null) {
         return null;
      } else {
         if (!this.validateSchema) {
            this.wlsWebAppDescriptor.setValidate(this.validateSchema);
         }

         return (WeblogicWebAppBean)this.wlsWebAppDescriptor.loadDescriptorBean();
      }
   }

   public DescriptorBean mergeLibaryDescriptors(Source[] var1, String var2) throws IOException, XMLStreamException {
      if (var2 == null) {
         return null;
      } else if (var2.equals("WEB-INF/web.xml")) {
         return this.webAppDescriptor.mergeDescriptors(var1);
      } else {
         return var2.equals("WEB-INF/weblogic.xml") ? this.wlsWebAppDescriptor.mergeDescriptors(var1) : null;
      }
   }

   public AbstractDescriptorLoader2 getWebAppDescriptorLoader() {
      return this.webAppDescriptor;
   }

   public AbstractDescriptorLoader2 getWlsWebAppDescriptorLoader() {
      return this.wlsWebAppDescriptor;
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 1) {
         usage();
      }

      try {
         boolean var1 = false;
         String var2 = var0[0];
         File var3 = new File(var2);
         if (var3.getName().endsWith(".war")) {
            JarFile var4 = new JarFile(var2);
            VirtualJarFile var5 = VirtualJarFactory.createVirtualJar(var4);
            System.out.println("\n\n... getting WebAppBean:");
            WebAppDescriptor var6 = new WebAppDescriptor(var5, (File)null, (DeploymentPlanBean)null, (String)null);
            WebAppBean var7 = var6.getWebAppBean();
            ((DescriptorBean)var7).getDescriptor().toXML(System.out);
            if (var6.getWeblogicWebAppBean() != null) {
               System.out.println("\n\n... getting WeblogicWebAppBean:");
               ((DescriptorBean)var6.getWeblogicWebAppBean()).getDescriptor().toXML(System.out);
            }
         } else {
            if (var3.getPath().startsWith("edit")) {
               var1 = true;
            }

            WebAppDescriptor var10;
            if (var3.getPath().endsWith("web.xml")) {
               System.out.println("\n\n... getting WebAppBean: editable = " + var1);
               var10 = var1 ? new WebAppDescriptor(new EditableDescriptorManager(), new GenericClassLoader(new DirectoryClassFinder(new File(".")))) : new WebAppDescriptor(var3, (File)null, (File)null, (DeploymentPlanBean)null, (String)null);
               ((DescriptorBean)var10.getWebAppBean()).getDescriptor().toXML(System.out);
               System.out.println("\n\n\nDescriptor is editable = " + ((DescriptorBean)var10.getWebAppBean()).getDescriptor().isEditable());
            } else {
               if (var3.getPath().startsWith("edit")) {
                  var1 = true;
               }

               if (var3.getPath().endsWith("weblogic.xml")) {
                  System.out.println("\n\n... getting WeblogicWebAppBean:");
                  var10 = var1 ? new WebAppDescriptor(new EditableDescriptorManager(), new GenericClassLoader(new DirectoryClassFinder(new File(".")))) : new WebAppDescriptor((File)null, var3, (File)null, (DeploymentPlanBean)null, (String)null);
                  System.out.println("" + var10.getWeblogicWebAppBean());
                  ((DescriptorBean)var10.getWeblogicWebAppBean()).getDescriptor().toXML(System.out);
                  System.out.println("\n\n\nDescriptor is editable = " + ((DescriptorBean)var10.getWeblogicWebAppBean()).getDescriptor().isEditable());
                  if (var0.length > 1) {
                     File var11 = new File(var0[1]);
                     if (var11.getPath().endsWith("plan.xml")) {
                        System.out.println("\n\n... plan:");
                        DeploymentPlanDescriptorLoader var12 = new DeploymentPlanDescriptorLoader(var11);
                        DeploymentPlanBean var13 = var12.getDeploymentPlanBean();
                        ((DescriptorBean)var13).getDescriptor().toXML(System.out);
                        if (var0.length > 2) {
                           WebAppDescriptor var8 = new WebAppDescriptor((File)null, var3, new File(var13.getConfigRoot()), var13, var0[2]);
                           System.out.println("\n\n... plan merged with WeblogicApplicationBean:");
                           ((DescriptorBean)var8.getWeblogicWebAppBean()).getDescriptor().toXML(System.out);
                        } else {
                           System.out.println("\n\nNO MODULE NAME\n\nusage: java weblogic.servlet.internal.WebAppDescriptor weblogic.xml plan.xml module-name");
                        }
                     }
                  }
               } else {
                  System.out.println("\n\n... neither web nor weblogic xml specified");
               }
            }
         }
      } catch (Exception var9) {
         System.out.println(var9.toString());
         System.out.println(var9.getMessage());
         System.out.println(var9.getCause());
         var9.printStackTrace();
      }

   }

   private WebAppBean createWebAppBean() {
      WebAppBean var1 = (WebAppBean)(new DescriptorManager()).createDescriptorRoot(WebAppBean.class).getRootBean();
      var1.setVersion("2.5");
      return var1;
   }

   private static void usage() {
      System.err.println("usage: java weblogic.servlet.internal.WebAppDescriptor <descriptor file name>");
      System.err.println("\n\n example:\n java weblogic.servlet.internal.WebAppDescriptor war or altDD file name ");
      System.err.println("\n\n example:\n java weblogic.servlet.internal.WebAppDescriptor war or altDD file name ");
      System.exit(0);
   }

   public static class MyWlsWebAppDescriptor extends CachingDescriptorLoader2 {
      public MyWlsWebAppDescriptor(File var1) {
         this((File)var1, (File)null, (DeploymentPlanBean)null, (String)null);
      }

      public MyWlsWebAppDescriptor(VirtualJarFile var1) {
         this((VirtualJarFile)var1, (File)null, (DeploymentPlanBean)null, (String)null);
      }

      public MyWlsWebAppDescriptor(DescriptorManager var1, GenericClassLoader var2) {
         this((DescriptorManager)var1, (GenericClassLoader)var2, (File)null, (DeploymentPlanBean)null, (String)null);
      }

      public MyWlsWebAppDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4) {
         super(var1, var2, var3, var4, "WEB-INF/weblogic.xml");
      }

      public MyWlsWebAppDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super(var1, var2, var3, var4, "WEB-INF/weblogic.xml");
      }

      public MyWlsWebAppDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4) {
         super(var1, var2, var3, var4, "WEB-INF/weblogic.xml");
      }

      public MyWlsWebAppDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super(var1, var2, var3, var4, "WEB-INF/weblogic.xml");
      }

      public MyWlsWebAppDescriptor(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5) {
         super(var1, var2, var3, var4, var5, "WEB-INF/weblogic.xml");
      }

      protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
         return new WlsWebAppReader2(var1, this);
      }

      void setValidate(boolean var1) {
         super.setValidateSchema(var1);
      }
   }

   public static class MyWebAppDescriptor extends CachingDescriptorLoader2 {
      public MyWebAppDescriptor(File var1) {
         this((File)var1, (File)null, (DeploymentPlanBean)null, (String)null);
      }

      public MyWebAppDescriptor(VirtualJarFile var1) {
         this((VirtualJarFile)var1, (File)null, (DeploymentPlanBean)null, (String)null);
      }

      public MyWebAppDescriptor(DescriptorManager var1, GenericClassLoader var2) {
         this((DescriptorManager)var1, (GenericClassLoader)var2, (File)null, (DeploymentPlanBean)null, (String)null);
      }

      public MyWebAppDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4) {
         super(var1, var2, var3, var4, "WEB-INF/web.xml");
      }

      public MyWebAppDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super(var1, var2, var3, var4, "WEB-INF/web.xml");
      }

      public MyWebAppDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4) {
         super(var1, var2, var3, var4, "WEB-INF/web.xml");
      }

      public MyWebAppDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super(var1, var2, var3, var4, "WEB-INF/web.xml");
      }

      public MyWebAppDescriptor(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5) {
         super(var1, var2, var3, var4, var5, "WEB-INF/web.xml");
      }

      protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
         return new WebAppReader2(var1, this);
      }

      void setValidate(boolean var1) {
         super.setValidateSchema(var1);
      }
   }
}
