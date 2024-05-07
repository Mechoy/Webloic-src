package weblogic.ant.taskdefs.connector;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.zip.ZipOutputStream;

public class Rar extends Jar {
   private File deploymentDescriptor;
   private File weblogicDeploymentDescriptor;
   private boolean descriptorAdded;
   private static final FileUtils fu = FileUtils.newFileUtils();

   public Rar() {
      this.archiveType = "rar";
      this.emptyBehavior = "create";
   }

   /** @deprecated */
   public void setWarfile(File var1) {
      this.setDestFile(var1);
   }

   public void setRaxml(File var1) {
      this.deploymentDescriptor = var1;
      if (!this.deploymentDescriptor.exists()) {
         throw new BuildException("Deployment descriptor: " + this.deploymentDescriptor + " does not exist.");
      } else {
         ZipFileSet var2 = new ZipFileSet();
         var2.setFile(this.deploymentDescriptor);
         var2.setFullpath("META-INF/ra.xml");
         super.addFileset(var2);
      }
   }

   public void setWeblogicRaxml(File var1) {
      this.weblogicDeploymentDescriptor = var1;
      if (!this.weblogicDeploymentDescriptor.exists()) {
         throw new BuildException("Weblogic Deployment descriptor: " + this.weblogicDeploymentDescriptor + " does not exist.");
      } else {
         ZipFileSet var2 = new ZipFileSet();
         var2.setFile(this.weblogicDeploymentDescriptor);
         var2.setFullpath("META-INF/weblogic-ra.xml");
         super.addFileset(var2);
      }
   }

   public void addLib(ZipFileSet var1) {
      super.addFileset(var1);
   }

   public void addClasses(ZipFileSet var1) {
      super.addFileset(var1);
   }

   protected void initZipOutputStream(ZipOutputStream var1) throws IOException, BuildException {
      if (this.deploymentDescriptor == null && !this.isInUpdateMode()) {
         throw new BuildException("raxml attribute is required", this.getLocation());
      } else {
         super.initZipOutputStream(var1);
      }
   }

   protected void zipFile(File var1, ZipOutputStream var2, String var3, int var4) throws IOException {
      if (var3.equalsIgnoreCase("META-INF/ra.xml")) {
         if (this.deploymentDescriptor != null && fu.fileNameEquals(this.deploymentDescriptor, var1) && !this.descriptorAdded) {
            super.zipFile(var1, var2, var3, var4);
            this.descriptorAdded = true;
         } else {
            this.log("Warning: selected " + this.archiveType + " files include a META-INF/ra.xml which will be ignored " + "(please use raxmll attribute to " + this.archiveType + " task)", 1);
         }
      } else {
         super.zipFile(var1, var2, var3, var4);
      }

   }

   protected void cleanUp() {
      this.descriptorAdded = false;
      super.cleanUp();
   }
}
