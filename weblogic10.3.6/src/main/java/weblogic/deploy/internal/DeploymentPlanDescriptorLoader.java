package weblogic.deploy.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.descriptor.VersionMunger;
import weblogic.descriptor.DescriptorManager;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;

public class DeploymentPlanDescriptorLoader extends AbstractDescriptorLoader2 {
   static boolean debug = false;
   final File planFile;

   public DeploymentPlanDescriptorLoader(File var1) {
      super((File)var1, (String)null);
      this.planFile = var1;
      if (debug) {
         System.out.println("\n\n\n\nParsing plan at " + var1.toString());
      }

   }

   public String getDocumentURI() {
      return null;
   }

   public DeploymentPlanDescriptorLoader(InputStream var1, DescriptorManager var2) {
      super(var1, var2, (List)null, true);
      this.planFile = null;
   }

   public DeploymentPlanBean getDeploymentPlanBean() throws IOException, XMLStreamException {
      return (DeploymentPlanBean)this.loadDescriptorBean();
   }

   protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
      String var2 = "weblogic.j2ee.descriptor.wl.DeploymentPlanBeanImpl$SchemaHelper2";
      return new VersionMunger(var1, this, var2, "http://xmlns.oracle.com/weblogic/deployment-plan");
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 1) {
         usage();
      }

      debug = true;
      String var1 = var0[0];
      File var2 = new File(var1);
      System.out.println("\n\n... getting DeploymentPlanBean :");
      DeploymentPlanDescriptorLoader var3 = new DeploymentPlanDescriptorLoader(var2);
      var3.loadDescriptorBean().getDescriptor().toXML(System.out);
   }

   private static void usage() {
      System.err.println("usage: java weblogic.deploy.internal.DeploymentPlanDescriptorLoader <descriptor file name>");
      System.err.println("\n\n example:\n java weblogic.deploy.internal.DeploymentPlanDescriptorLoader sample.xml");
      System.exit(0);
   }
}
