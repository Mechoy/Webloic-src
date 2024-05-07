package weblogic.wsee.tools.anttasks;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.XMLCatalog;
import weblogic.wsee.tools.xcatalog.CatalogOptions;
import weblogic.wsee.tools.xcatalog.DownloadXMLs;
import weblogic.wsee.util.Verbose;

public class WsdlGetTask extends Task {
   private static final boolean verbose = Verbose.isVerbose(WsdlGetTask.class);
   private String wsdl;
   private File catalog;
   private XMLCatalog xmlCatalog;
   private File destDir;

   public void execute() throws BuildException {
      this.validate();
      DownloadXMLs var1 = new DownloadXMLs();
      CatalogOptions var2 = new CatalogOptions();
      var2.setCatalog(this.catalog);
      var2.setXmlCatalog(this.xmlCatalog);

      try {
         var1.parseXMLs(var2, this.destDir, this.wsdl, true);
      } catch (BuildException var4) {
         if (verbose) {
            Verbose.say("Build Error StackTrace:");
            var4.printStackTrace();
         }

         this.log("WsdlGet failed : " + var4.getMessage(), 0);
         throw var4;
      }
   }

   private void validate() throws BuildException {
      if (this.wsdl == null) {
         throw new BuildException("Option wsdl must be defined to a wsdl file");
      } else if (this.destDir == null) {
         throw new BuildException("option destDir [Destination directory] must be defined");
      }
   }

   public void setDestDir(File var1) {
      this.destDir = var1;
   }

   public void setWsdl(String var1) {
      this.wsdl = var1;
   }

   public void setCatalog(File var1) {
      this.catalog = var1;
   }

   public void addConfiguredXmlCatalog(XMLCatalog var1) {
      var1.setProject(this.getProject());
      this.xmlCatalog = var1;
   }
}
