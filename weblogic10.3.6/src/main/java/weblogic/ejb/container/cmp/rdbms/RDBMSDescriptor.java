package weblogic.ejb.container.cmp.rdbms;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.descriptor.AbstractDescriptorLoader;
import weblogic.application.descriptor.BasicMunger;
import weblogic.application.descriptor.NamespaceURIMunger;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class RDBMSDescriptor implements weblogic.ejb.spi.RDBMSDescriptor {
   private static EditableDescriptorManager edm = new EditableDescriptorManager();
   MyWlsRDBMSDescriptor myWlsDP = null;
   private boolean writable = false;

   public RDBMSDescriptor(VirtualJarFile var1, String var2, String var3, String var4, DeploymentPlanBean var5, File var6) {
      this.myWlsDP = new MyWlsRDBMSDescriptor(var1, var6, var5, var4, var2);
   }

   public RDBMSDescriptor(DescriptorManager var1, String var2, GenericClassLoader var3, File var4, DeploymentPlanBean var5, String var6) {
      this.myWlsDP = new MyWlsRDBMSDescriptor(var1, var2, var3, var4, var5, var6);
   }

   public RDBMSDescriptor(File var1) {
      this.myWlsDP = new MyWlsRDBMSDescriptor(var1);
   }

   public DeploymentPlanBean getDeploymentPlan() {
      return this.myWlsDP.getDeploymentPlan();
   }

   public DescriptorBean getDescriptorBean() throws IOException, XMLStreamException {
      try {
         return this.myWlsDP.getDeploymentPlan() != null ? this.myWlsDP.getPlanMergedDescriptorBean() : this.myWlsDP.getRootDescriptorBean();
      } catch (XMLStreamException var2) {
         throw new IOException(var2.toString());
      }
   }

   public DescriptorBean getEditableDescriptorBean() throws IOException, XMLStreamException {
      DescriptorBean var1;
      try {
         this.writable = true;
         var1 = this.getDescriptorBean();
      } catch (XMLStreamException var6) {
         throw new IOException(var6.toString());
      } finally {
         this.writable = false;
      }

      return var1;
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 1) {
         usage();
      }

      if (var0[0].lastIndexOf("create") > -1) {
         DescriptorManager var1 = new DescriptorManager();
         Descriptor var2 = var1.createDescriptorRoot(WeblogicRdbmsJarBean.class);
         var2.toXML(System.out);
         System.out.println("\n\n\n");
         var2 = var1.createDescriptorRoot(weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean.class);
         var2.toXML(System.out);
         System.exit(0);
      }

      String var6 = var0[0];
      File var7 = new File(var6);
      if (var7.getName().endsWith(".jar")) {
         JarFile var3 = new JarFile(var6);
         VirtualJarFile var4 = VirtualJarFactory.createVirtualJar(var3);
         System.out.println("\n\n... getting RDBMSDescriptor from: " + var3);
         RDBMSDescriptor var5 = new RDBMSDescriptor(var4, (String)null, (String)null, (String)null, (DeploymentPlanBean)null, (File)null);
         var5.getDescriptorBean().getDescriptor().toXML(System.out);
      } else if (var7.getPath().endsWith("weblogic-cmp-rdbms-jar.xml")) {
         System.out.println("\n\n... getting RDBMSDescriptor from: " + var7.getPath());
         RDBMSDescriptor var8 = new RDBMSDescriptor(var7);
         var8.getDescriptorBean().getDescriptor().toXML(System.out);
      } else {
         System.out.println("\n\n... no jar or file specified");
      }

   }

   private static void usage() {
      System.err.println("usage: java weblogic.ejb.container.cmp.rdbms.RDBMSDescriptor <descriptor file name>");
      System.err.println("\n\n example:\n java weblogic.ejb.container.cmp.rdbms.RDBMSDescriptor jar or altDD file name ");
      System.exit(0);
   }

   private class MyWlsRDBMSDescriptor extends AbstractDescriptorLoader {
      private String fileName;

      MyWlsRDBMSDescriptor(VirtualJarFile var2, File var3, DeploymentPlanBean var4, String var5, String var6) {
         super(var2, var3, var4, var5);
         this.fileName = var6;
      }

      MyWlsRDBMSDescriptor(DescriptorManager var2, String var3, GenericClassLoader var4, File var5, DeploymentPlanBean var6, String var7) {
         super(var2, var4, var5, var6, var7);
         this.fileName = var3;
      }

      MyWlsRDBMSDescriptor(File var2) {
         super(var2);
      }

      public Descriptor createDescriptor(InputStream var1) throws IOException, XMLStreamException {
         if (!RDBMSDescriptor.this.writable) {
            return super.createDescriptor(var1);
         } else {
            Descriptor var3;
            try {
               XMLStreamReader var2 = this.getXMLStreamReader(var1);
               var3 = RDBMSDescriptor.edm.createDescriptor(var2);
            } finally {
               try {
                  var1.close();
               } catch (Exception var10) {
               }

            }

            return var3;
         }
      }

      public String getDocumentURI() {
         return this.fileName;
      }

      protected BasicMunger createXMLStreamReader(InputStream var1) throws XMLStreamException {
         return new WlsCmpRdbmsReader(this.createXMLStreamReaderDelegate(var1), this, this.getDeploymentPlan(), this.getModuleName(), this.getDocumentURI());
      }

      public XMLStreamReader createXMLStreamReaderDelegate(InputStream var1) throws XMLStreamException {
         String[] var2 = new String[]{"http://www.bea.com/ns/weblogic/90", "http://www.bea.com/ns/weblogic/weblogic-rdbms-jar"};
         return new NamespaceURIMunger(var1, "http://xmlns.oracle.com/weblogic/weblogic-rdbms-jar", var2);
      }
   }
}
