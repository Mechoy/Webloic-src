package weblogic.wsee.deploy;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import javax.xml.stream.XMLStreamException;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class JavaWsdlMappingDescriptor {
   private final MyJavaWsdlMappingDescriptor javaWsdlMappingDescriptor;

   public JavaWsdlMappingDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
      this.javaWsdlMappingDescriptor = new MyJavaWsdlMappingDescriptor(var1, var2, var3, var4, var5);
   }

   private JavaWsdlMappingDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
      this.javaWsdlMappingDescriptor = new MyJavaWsdlMappingDescriptor(var1, var2, var3, var4, var5);
   }

   public DeploymentPlanBean getDeploymentPlan() {
      return this.javaWsdlMappingDescriptor.getDeploymentPlan();
   }

   public JavaWsdlMappingBean getJavaWsdlMappingBean() throws IOException, XMLStreamException {
      JavaWsdlMappingBean var1 = (JavaWsdlMappingBean)this.javaWsdlMappingDescriptor.loadDescriptorBean();
      if (var1 == null) {
         throw new IOException("Could not find " + this.javaWsdlMappingDescriptor.getDocumentURI());
      } else {
         return var1;
      }
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 1) {
         usage();
      }

      String var1 = var0[0];
      File var2 = new File(var1);
      if (var2.getName().endsWith(".jar")) {
         JarFile var3 = new JarFile(var1);
         VirtualJarFile var4 = VirtualJarFactory.createVirtualJar(var3);
         System.out.println("\n\n... getting WebservicesBean:");
         String var5 = var0[1];
         JavaWsdlMappingDescriptor var6 = new JavaWsdlMappingDescriptor(var4, (File)null, (DeploymentPlanBean)null, (String)null, var5);
         JavaWsdlMappingBean var7 = var6.getJavaWsdlMappingBean();
         ((DescriptorBean)var7).getDescriptor().toXML(System.out);
      } else if (var2.getPath().endsWith("weblogic-webservices.xml")) {
         System.out.println("\n\n... getting WeblogicWebservicesBean from: " + var2);
         JavaWsdlMappingBean var8 = (new JavaWsdlMappingDescriptor(var2, (File)null, (DeploymentPlanBean)null, (String)null, var1)).getJavaWsdlMappingBean();
         ((DescriptorBean)var8).getDescriptor().toXML(System.out);
      } else {
         System.out.println("\n\n... neither webservices.xml nor weblogic-webservices.xml specified");
      }

   }

   private static void usage() {
      System.err.println("usage: java weblogic.wsee.deploy.JavaWsdlMappingDescriptor <descriptor file name>");
      System.err.println("\n\n example:\n java weblogic.wsee.deploy.JavaWsdlMappingDescriptor jar or altDD file name ");
      System.exit(0);
   }

   private class MyJavaWsdlMappingDescriptor extends AbstractDescriptorLoader2 {
      MyJavaWsdlMappingDescriptor(VirtualJarFile var2, File var3, DeploymentPlanBean var4, String var5, String var6) {
         super(var2, var3, var4, var5, var6);
      }

      MyJavaWsdlMappingDescriptor(File var2, File var3, DeploymentPlanBean var4, String var5, String var6) {
         super(var2, var3, var4, var5, var6);
      }
   }
}
