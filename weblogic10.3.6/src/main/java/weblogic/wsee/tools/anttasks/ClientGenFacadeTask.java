package weblogic.wsee.tools.anttasks;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.XMLCatalog;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.bind.TypeFamily;
import weblogic.wsee.tools.ToolsUtil;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.clientgen.ClientGen;
import weblogic.wsee.tools.clientgen.ClientGenFactory;
import weblogic.wsee.tools.clientgen.jaxrpc.ClientGenProcessor;
import weblogic.wsee.tools.clientgen.jaxrpc.Options;
import weblogic.wsee.tools.logging.AntLogger;

public class ClientGenFacadeTask extends Task {
   protected String wsdl;
   private String packageName;
   protected File destDir;
   private List<FileSet> bindingFileSets = new ArrayList();
   protected WebServiceType type;
   private Options jaxrpcOptions;
   protected weblogic.wsee.tools.clientgen.jaxws.Options jaxwsOptions;

   public ClientGenFacadeTask() {
      this.type = WebServiceType.JAXRPC;
   }

   private void initJAXRPCOptions() {
      if (this.jaxrpcOptions == null) {
         this.jaxrpcOptions = new Options();
      }

   }

   public void initJAXWSOptions() {
      if (this.jaxwsOptions == null) {
         this.jaxwsOptions = new weblogic.wsee.tools.clientgen.jaxws.Options();
      }

   }

   public void setCatalog(File var1) {
      this.initJAXWSOptions();
      this.jaxwsOptions.setCatalog(var1);
   }

   public void addConfiguredSysProperty(Environment.Variable var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.addSysemProperty(var1.getKey(), var1.getValue());
      this.initJAXWSOptions();
      this.jaxwsOptions.addSysemProperty(var1.getKey(), var1.getValue());
   }

   public void addConfiguredDepends(FileSet var1) {
      this.initJAXWSOptions();
      this.jaxwsOptions.addConfiguredDepends(var1);
   }

   public void addConfiguredProduces(FileSet var1) {
      this.initJAXWSOptions();
      this.jaxwsOptions.addConfiguredProduces(var1);
   }

   /** @deprecated */
   @Deprecated
   public void addXsdConfig(FileSet var1) {
      this.log("xsdConfig is deprecated.  Use binding instead.", 1);
      this.addBinding(var1);
   }

   public void addBinding(FileSet var1) {
      this.bindingFileSets.add(var1);
   }

   public void setAutoDetectWrapped(boolean var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setAutoDetectWrapped(var1);
   }

   public void setIncludeGlobalTypes(boolean var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setIncludeGlobalTypes(var1);
   }

   public void setSortSchemaTypes(boolean var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setSortSchemaTypes(var1);
   }

   public void setFillIncompleteParameterOrderList(boolean var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setFillIncompleteParameterOrderList(var1);
   }

   public void setWsdl(String var1) {
      try {
         ToolsUtil.validateRequiredAttr(var1, "wsdl");
      } catch (WsBuildException var3) {
         throw new BuildException(var3.getMessage());
      }

      File var2 = this.getProject().resolveFile(var1);
      if (var2 != null && var2.isDirectory()) {
         throw new BuildException("wsdl attribute must not be a directory");
      } else {
         if (var2 != null && var2.exists()) {
            this.wsdl = var2.toURI().toString();
         } else {
            this.wsdl = var1;
         }

      }
   }

   public void setServiceName(String var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setServiceName(var1);
   }

   public void setPackageName(String var1) {
      this.packageName = var1;
   }

   void setDestDir(File var1) {
      this.destDir = var1;
   }

   public File getDestDir() {
      return this.destDir;
   }

   public void setHandlerChainFile(File var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setHandlerChain(var1);
   }

   public void setGeneratePolicyMethods(boolean var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setGeneratePolicyMethods(var1);
   }

   public void setJaxRPCWrappedArrayStyle(boolean var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setJaxRPCWrappedArrayStyle(var1);
   }

   public void setGenerateAsyncMethods(boolean var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setGenerateAsyncMethods(var1);
   }

   public void setTypeFamily(String var1) {
      this.initJAXRPCOptions();
      TypeFamily var2 = TypeFamily.getTypeFamilyForKey(var1);
      this.jaxrpcOptions.setTypeFamily(var2);
   }

   public WebServiceType getType() {
      return this.type;
   }

   public void setType(String var1) {
      this.type = WebServiceType.valueOf(var1);
   }

   void addProcessor(ClientGenProcessor var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.addProcessor(var1);
   }

   private void validate() throws BuildException {
      if (this.type == WebServiceType.JAXWS && this.jaxrpcOptions != null) {
         this.log("Ignoring JAX-RPC options - building a JAX-WS client", 1);
      }

      if (this.type == WebServiceType.JAXRPC && this.jaxwsOptions != null) {
         if (this.jaxwsOptions.getCatalog() != null || this.jaxwsOptions.getXmlCatalog() != null) {
            throw new BuildException("clientgen does not support catalog feature on JAXRPC type!");
         }

         this.log("Ignoring JAX-WS options - building a JAX-RPC client", 1);
      }

   }

   public void execute() throws BuildException {
      this.validate();

      try {
         ClientGen var1 = ClientGenFactory.newInstance(this.type);
         var1.setDestDir(this.destDir);
         var1.setPackageName(this.packageName);
         var1.setWsdl(this.wsdl);
         File[] var2 = AntUtil.getFiles((Collection)this.bindingFileSets, this.getProject());
         if (!this.bindingFileSets.isEmpty() && (var2 == null || var2.length == 0)) {
            this.log("Warning: Not found binding files defined in binding sub-element, some binding configuration might not take effect!");
         }

         var1.setBindingFiles(var2);
         var1.setLogger(new AntLogger(this));
         if (this.type == WebServiceType.JAXRPC) {
            var1.setOptions(this.jaxrpcOptions);
         } else if (this.type == WebServiceType.JAXWS) {
            var1.setOptions(this.jaxwsOptions);
         }

         this.log(var1.toString(), 2);
         var1.execute();
      } catch (WsBuildException var3) {
         throw new BuildException(var3);
      }
   }

   public void init() {
      this.jaxrpcOptions = null;
   }

   public void addConfiguredXmlCatalog(XMLCatalog var1) {
      this.initJAXWSOptions();
      var1.setProject(this.getProject());
      this.jaxwsOptions.setXmlCatalog(var1);
   }

   public void setGenerateRuntimeCatalog(boolean var1) {
      this.initJAXWSOptions();
      this.jaxwsOptions.setGenRuntimeCatalog(var1);
   }

   public void setWsdlLocation(String var1) {
      this.initJAXWSOptions();
      this.jaxwsOptions.setWsdllocationOverride(var1);
   }

   public void setCopyWsdl(boolean var1) {
      this.initJAXWSOptions();
      this.jaxwsOptions.setCopyWsdl(var1);
   }

   public void setAllowWrappedArrayForDocLiteral(boolean var1) {
      this.initJAXRPCOptions();
      this.jaxrpcOptions.setAllowWrappedArrayForDocLiteral(var1);
   }

   public void setXauthfile(File var1) {
      this.initJAXWSOptions();
      this.jaxwsOptions.setXauthfile(var1);
   }
}
