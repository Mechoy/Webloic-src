package weblogic.wsee.tools.clientgen.jaxws;

import com.sun.tools.ws.ant.WsImport2;
import com.sun.xml.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.ws.api.streaming.XMLStreamWriterFactory;
import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.anttasks.AntUtil;
import weblogic.wsee.tools.clientgen.ClientGen;
import weblogic.wsee.tools.logging.BuildListenerLogger;
import weblogic.wsee.tools.logging.DefaultLogger;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.Logger;
import weblogic.wsee.util.StringUtil;

public class ClientGenImpl implements ClientGen<Options> {
   private File destDir = null;
   private String packageName = null;
   private String wsdl = null;
   private File[] bindingFiles = null;
   private Options options = new Options();
   private Logger logger = new DefaultLogger();

   public void setDestDir(File var1) {
      this.destDir = var1;
   }

   public void setLogger(Logger var1) {
      if (var1 == null) {
         this.logger = new DefaultLogger();
      } else {
         this.logger = var1;
      }

   }

   public void setPackageName(String var1) {
      if (var1 != null) {
         var1 = var1.trim();
      }

      this.packageName = var1;
   }

   public void setWsdl(String var1) {
      this.wsdl = var1;
   }

   public void setBindingFiles(File[] var1) {
      this.bindingFiles = var1;
   }

   private void validate() throws WsBuildException {
      boolean var1 = true;
      if (StringUtil.isEmpty(this.wsdl)) {
         this.logger.log(EventLevel.ERROR, "No wsdl specified");
         var1 = false;
      }

      if (this.destDir == null) {
         this.logger.log(EventLevel.ERROR, "No destDir specified");
         var1 = false;
      }

      if (!var1) {
         throw new WsBuildException("ClientGen invalid - see log for details");
      }
   }

   private void setSystemProperties() {
      Properties var1 = this.options.getSystemProperties();
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         System.setProperty((String)var3.getKey(), (String)var3.getValue());
      }

   }

   private void resetSystemProperties() {
      Properties var1 = this.options.getSystemProperties();
      Enumeration var2 = var1.keys();

      while(var2.hasMoreElements()) {
         System.setProperty((String)var2.nextElement(), "");
      }

   }

   public void execute() throws WsBuildException {
      this.validate();
      if (!this.destDir.exists()) {
         this.destDir.mkdirs();
      }

      Project var1 = new Project();
      var1.setBaseDir(this.destDir.getAbsoluteFile());
      var1.addBuildListener(new BuildListenerLogger(this.logger));
      WsImport2 var2 = new WsImport2();
      var2.setProject(var1);
      var2.setDestdir(this.destDir);
      var2.setSourcedestdir(this.destDir);
      var2.setPackage(this.packageName);
      var2.setWsdl(this.wsdl);
      var2.setDebug(this.options.isDebug());
      var2.setVerbose(this.options.isVerbose());
      var2.setExtension(true);
      var2.setFailonerror(this.options.isFailonerror());
      var2.setFork(this.options.isFork());
      var2.setIncludeantruntime(this.options.isIncludeantruntime());
      var2.setIncludejavaruntime(this.options.isIncludejavaruntime());
      var2.setOptimize(this.options.isOptimize());
      var2.setXauthfile(this.options.getXauthfile());
      if (this.options.getCatalog() != null) {
         var2.setCatalog(this.options.getCatalog());
      }

      if (this.options.getXmlCatalog() != null) {
         if (this.options.getCatalog() == null) {
            var2.setCatalog(new File("fake_cfile_123.xml"));
         }

         var2.addConfiguredXMLCatalog(this.options.getXmlCatalog());
      }

      Iterator var3 = AntUtil.getFileSets(this.bindingFiles, var1).iterator();

      FileSet var4;
      while(var3.hasNext()) {
         var4 = (FileSet)var3.next();
         var2.addConfiguredBinding(var4);
      }

      var3 = this.options.getConfiguredDepends().iterator();

      while(var3.hasNext()) {
         var4 = (FileSet)var3.next();
         var2.addConfiguredDepends(var4);
      }

      var3 = this.options.getConfiguredProduces().iterator();

      while(var3.hasNext()) {
         var4 = (FileSet)var3.next();
         var2.addConfiguredProduces(var4);
      }

      if (this.options.getWsdllocationOverride() != null) {
         var2.setWsdllocation(this.options.getWsdllocationOverride());
      }

      this.setSystemProperties();

      try {
         var2.execute();
      } catch (BuildException var8) {
         Object var10;
         for(var10 = var8; var10 != null && ((Throwable)var10).getCause() != null && var10 instanceof BuildException; var10 = ((Throwable)var10).getCause()) {
         }

         throw new WsBuildException("Error running JAX-WS clientgen: " + ((Throwable)var10).getLocalizedMessage(), var8);
      } finally {
         this.resetSystemProperties();
      }

   }

   public void setOptions(Options var1) {
      if (var1 == null) {
         this.options = new Options();
      } else {
         this.options = var1;
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("\n*********** jax-ws clientgen attribute settings ***************\n\n");
      var1.append("wsdlURI: " + this.wsdl + "\n");
      var1.append("packageName : " + this.packageName + "\n");
      var1.append("destDir : " + this.destDir + "\n");
      var1.append("\n*********** jax-ws clientgen attribute settings end ***************\n");
      return var1.toString();
   }

   static {
      System.setProperty(XMLStreamWriterFactory.class.getName() + ".woodstox", "true");
      System.setProperty(XMLStreamReaderFactory.class.getName() + ".woodstox", "true");
   }
}
