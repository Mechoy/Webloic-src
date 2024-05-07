package weblogic.wsee.tools.wsdlc.jaxws;

import com.sun.tools.ws.ant.WsImport2;
import com.sun.xml.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.ws.api.streaming.XMLStreamWriterFactory;
import java.io.IOException;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.anttasks.AntUtil;
import weblogic.wsee.tools.logging.BuildListenerLogger;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.wsdlc.BaseWsdl2JwsBuilder;

public class Wsdl2JwsBuilderImpl extends BaseWsdl2JwsBuilder<Options> {
   private Options options = new Options();

   public void setOptions(Options var1) {
      if (var1 == null) {
         this.options = new Options();
      } else {
         this.options = var1;
      }

   }

   protected void validate() throws WsBuildException {
   }

   protected void executeImpl() throws WsBuildException {
      this.validate();
      this.generateInterfaceAndImpl();
   }

   private void generateInterfaceAndImpl() throws WsBuildException {
      this.destDir.mkdirs();
      Project var1 = new Project();
      var1.setBaseDir(this.destDir.getAbsoluteFile());
      var1.addBuildListener(new BuildListenerLogger(this.logger));
      WsImport2 var2 = new WsImport2();
      var2.setProject(var1);
      var2.setDestdir(this.destDir);
      var2.setSourcedestdir(this.destDir);
      var2.setPackage(this.packageName);
      var2.setWsdl(this.wsdl);
      var2.setWsdllocation(this.wsdlLocation);
      var2.setDebug(this.options.isDebug());
      var2.setVerbose(this.options.isVerbose());
      var2.setExtension(true);
      var2.setFailonerror(this.options.isFailonerror());
      var2.setFork(this.options.isFork());
      var2.setIncludeantruntime(this.options.isIncludeantruntime());
      var2.setIncludejavaruntime(this.options.isIncludejavaruntime());
      var2.setOptimize(this.options.isOptimize());
      if (this.destImplDir != null) {
         try {
            if (this.destImplDir.getCanonicalPath().equals(this.destDir.getCanonicalPath())) {
               this.logger.log(EventLevel.WARNING, "destImplDir equals cowDir, your expected impl files might be generated into the Compiled WSDL jar.");
            }
         } catch (IOException var6) {
         }

         this.destImplDir.mkdirs();
         var2.setImplDestDir(this.destImplDir);
         if (this.options.getServiceName() != null) {
            var2.setImplServiceName(this.options.getServiceName().toString());
         }

         if (this.portName != null) {
            var2.setImplPortName(this.portName.toString());
         }
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

      try {
         var2.execute();
      } catch (BuildException var5) {
         throw new WsBuildException("Error running JAX-WS wsdlc", var5);
      }
   }

   static {
      System.setProperty(XMLStreamWriterFactory.class.getName() + ".woodstox", "true");
      System.setProperty(XMLStreamReaderFactory.class.getName() + ".woodstox", "true");
   }
}
