package weblogic.wsee.tools.jws.build;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.ServletMappingBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.WebservicesBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebservicesBean;
import weblogic.j2ee.descriptor.wl.WebservicePolicyRefBean;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.anttasks.JwscTask;
import weblogic.wsee.tools.jws.JWSProcessor;
import weblogic.wsee.tools.jws.JwsArchiveWriter;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.jws.ModuleInfo;
import weblogic.wsee.tools.jws.WebServiceInfo;
import weblogic.wsee.tools.jws.WebServiceInfoFactory;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.decl.port.PortDecl;
import weblogic.wsee.tools.jws.ejb.EjbGenInvoker;
import weblogic.wsee.tools.jws.process.JWSProcessorFactory;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.LogEvent;
import weblogic.wsee.util.DescriptorBeanUtil;
import weblogic.wsee.util.JAXWSClassLoaderFactory;
import weblogic.wsee.util.UniqueNameSet;

public class JwsCompiler implements ModuleInfo {
   private JwsBuildContext ctx;
   private boolean wsdlOnly;
   private boolean generateWsdl;
   private boolean generateDescriptors;
   private File outputDir;
   private WebAppBean webAppBean;
   private WeblogicWebAppBean weblogicWebAppBean;
   private WebservicePolicyRefBean webservicePolicyRefBean;
   private WebservicesBean webServicesBean;
   private WeblogicWebservicesBean weblogicWebServicesBean;
   private List<File> descriptors;
   private File[] bindingFiles;
   private boolean jaxRPCWrappedArrayStyle;
   private boolean upperCasePropName;
   private boolean localElementDefaultRequired;
   private boolean localElementDefaultNillable;
   private JwscTask owningTask;
   private List<WebServiceDecl> webServices;

   public JwsCompiler(JwsBuildContext var1) {
      this(var1, (JwscTask)null);
   }

   public JwsCompiler(JwsBuildContext var1, JwscTask var2) {
      this.ctx = null;
      this.wsdlOnly = false;
      this.generateWsdl = false;
      this.generateDescriptors = false;
      this.outputDir = null;
      this.webAppBean = null;
      this.weblogicWebAppBean = null;
      this.webservicePolicyRefBean = null;
      this.webServicesBean = null;
      this.weblogicWebServicesBean = null;
      this.descriptors = new ArrayList();
      this.upperCasePropName = true;
      this.localElementDefaultRequired = true;
      this.localElementDefaultNillable = true;
      this.webServices = new ArrayList();
      if (var1 == null) {
         throw new NullPointerException("ctx must not be null");
      } else {
         this.owningTask = var2;
         this.ctx = var1;
      }
   }

   public JwsBuildContext getJwsBuildContext() {
      return this.ctx;
   }

   public JwscTask getOwningTask() {
      return this.owningTask;
   }

   public boolean isWsdlOnly() {
      return this.wsdlOnly;
   }

   public void setWsdlOnly(boolean var1) {
      this.wsdlOnly = var1;
   }

   public boolean isGenerateWsdl() {
      return this.generateWsdl;
   }

   public void setGenerateWsdl(boolean var1) {
      this.generateWsdl = var1;
   }

   public boolean isGenerateDescriptors() {
      return this.generateDescriptors;
   }

   public void setGenerateDescriptors(boolean var1) {
      this.generateDescriptors = var1;
   }

   public File getOutputDir() {
      return this.outputDir;
   }

   public void setOutputDir(File var1) {
      if (var1 == null) {
         throw new NullPointerException("outputDir");
      } else {
         this.outputDir = var1;
      }
   }

   public List<WebServiceDecl> getWebServices() {
      return Collections.unmodifiableList(this.webServices);
   }

   public void addWebService(WebServiceDecl var1) {
      if (var1 == null) {
         throw new NullPointerException("webService");
      } else {
         this.webServices.add(var1);
      }
   }

   public WebservicesBean getWebServicesBean() {
      return this.webServicesBean;
   }

   public void setWebServicesBean(WebservicesBean var1) {
      this.webServicesBean = var1;
   }

   public WeblogicWebservicesBean getWeblogicWebservicesBean() {
      return this.weblogicWebServicesBean;
   }

   public void setWeblogicWebservicesBean(WeblogicWebservicesBean var1) {
      this.weblogicWebServicesBean = var1;
   }

   public WebAppBean getWebAppBean() {
      return this.webAppBean;
   }

   public void setWebAppBean(WebAppBean var1) {
      this.webAppBean = var1;
   }

   public WeblogicWebAppBean getWeblogicWebAppBean() {
      return this.weblogicWebAppBean;
   }

   public void setWeblogicWebAppBean(WeblogicWebAppBean var1) {
      this.weblogicWebAppBean = var1;
   }

   public WebservicePolicyRefBean getWebservicePolicyRefBean() {
      return this.webservicePolicyRefBean;
   }

   public void setWebservicePolicyRefBean(WebservicePolicyRefBean var1) {
      this.webservicePolicyRefBean = var1;
   }

   private void addDescriptor(DescriptorBean var1) {
      if (var1 == null) {
         throw new NullPointerException("bean");
      } else {
         if (var1 instanceof WeblogicWebAppBean) {
            this.setWeblogicWebAppBean((WeblogicWebAppBean)var1);
         } else {
            if (!(var1 instanceof WebAppBean)) {
               throw new IllegalArgumentException(var1.getClass().getName() + " not supported");
            }

            this.setWebAppBean((WebAppBean)var1);
         }

      }
   }

   public void addDescriptor(File var1) throws IOException {
      if (var1 == null) {
         throw new NullPointerException("file");
      } else if (!var1.exists()) {
         throw new IllegalArgumentException(var1.getAbsolutePath() + " does not exist");
      } else if (var1.isDirectory()) {
         throw new IllegalArgumentException(var1.getAbsolutePath() + " is a directory");
      } else {
         this.addDescriptor(DescriptorBeanUtil.loadWebDescriptor(var1));
         this.descriptors.add(var1);
      }
   }

   public boolean isEjb() {
      if (this.webServices.isEmpty()) {
         return false;
      } else {
         WebServiceDecl var1 = (WebServiceDecl)this.webServices.get(0);
         return !(var1 instanceof WebServiceSEIDecl) ? false : ((WebServiceSEIDecl)var1).isEjb();
      }
   }

   public void validate() throws WsBuildException {
      this.validateOutputDir();
      this.validateDescriptors();
      if (!this.webServices.isEmpty()) {
         this.validateBacking();
         this.validateServicesQName();
         this.validatePortTypesQName();
         this.validatePortsQName();
         this.validatePortsName();
         this.validateContextPath();
         this.validateDocWrapped();
      }

      this.validateServiceUris();
      this.validateContextPathAndServiceUris();
   }

   private void validateServicesQName() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.webServices.iterator();

      while(true) {
         WebServiceDecl var3;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = (WebServiceDecl)var2.next();
         } while(var3.isWlw81UpgradedService());

         QName var4 = var3.getServiceQName();
         if (var1.containsKey(var4)) {
            String var5 = (String)var1.get(var4);
            if (var5 == null || !var5.equals(var3.getWsdlLocation())) {
               this.ctx.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var3.getJClass(), "unique.qname.service", new Object[]{var4})));
            }
         }

         var1.put(var4, var3.getWsdlLocation());
      }
   }

   private void validatePortTypesQName() {
      HashMap var1 = new HashMap();

      WebServiceDecl var3;
      QName var4;
      for(Iterator var2 = this.webServices.iterator(); var2.hasNext(); var1.put(var4, var3.getWsdlLocation())) {
         var3 = (WebServiceDecl)var2.next();
         var4 = var3.getPortTypeQName();
         if (var1.containsKey(var4)) {
            String var5 = (String)var1.get(var4);
            if (var5 == null || !var5.equals(var3.getWsdlLocation())) {
               this.ctx.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var3.getJClass(), "unique.qname.porttype", new Object[]{var4})));
            }
         }
      }

   }

   private void validatePortsQName() {
      HashSet var1 = new HashSet();
      Iterator var2 = this.webServices.iterator();

      while(var2.hasNext()) {
         WebServiceDecl var3 = (WebServiceDecl)var2.next();
         String var4 = var3.getTargetNamespace();

         QName var7;
         for(Iterator var5 = var3.getPorts(); var5.hasNext(); var1.add(var7)) {
            PortDecl var6 = (PortDecl)var5.next();
            var7 = new QName(var4, var6.getPortName());
            if (var1.contains(var7)) {
               this.ctx.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var3.getJClass(), "unique.qname.port", new Object[]{var7})));
            }
         }
      }

   }

   private void validatePortsName() {
      HashSet var1 = new HashSet();
      Iterator var2 = this.webServices.iterator();

      while(var2.hasNext()) {
         WebServiceDecl var3 = (WebServiceDecl)var2.next();

         String var6;
         for(Iterator var4 = var3.getPorts(); var4.hasNext(); var1.add(var6)) {
            PortDecl var5 = (PortDecl)var4.next();
            var6 = var5.getPortName();
            if (var1.contains(var6)) {
               this.ctx.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var3.getJClass(), "unique.name.port", new Object[]{var6})));
            }
         }
      }

   }

   private void validateDescriptors() throws WsBuildException {
      Iterator var1 = this.descriptors.iterator();

      while(var1.hasNext()) {
         File var2 = (File)var1.next();

         try {
            Iterator var3 = DescriptorBeanUtil.validateBean(var2).iterator();

            while(var3.hasNext()) {
               Object var4 = var3.next();
               this.ctx.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var2.toURI(), "descriptor.invalid", new Object[]{var4})));
            }
         } catch (IOException var5) {
            throw new WsBuildException(var5);
         }
      }

   }

   private void validateOutputDir() throws WsBuildException {
      if (this.outputDir == null) {
         throw new WsBuildException("'outputDir' not specified");
      } else {
         if (this.outputDir.exists()) {
            this.ctx.getLogger().log(EventLevel.VERBOSE, "JWS output directory " + this.outputDir + " already exists.");
            if (!this.outputDir.isDirectory()) {
               throw new WsBuildException("'outputDir' must be a directory");
            }
         }

      }
   }

   private void validateBacking() {
      boolean var1 = ((WebServiceDecl)this.webServices.get(0)).isEjb();
      Iterator var2 = this.webServices.iterator();

      while(var2.hasNext()) {
         WebServiceDecl var3 = (WebServiceDecl)var2.next();
         if (var1 != var3.isEjb()) {
            this.ctx.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var3.getJClass(), "type.backing.mixed", new Object[0])));
         }
      }

   }

   private void validateContextPath() {
      if (!((WebServiceDecl)this.webServices.get(0)).isEjb()) {
         String var1 = null;
         Iterator var2 = this.webServices.iterator();

         while(var2.hasNext()) {
            WebServiceDecl var3 = (WebServiceDecl)var2.next();
            Iterator var4 = var3.getPorts();

            while(var4.hasNext()) {
               PortDecl var5 = (PortDecl)var4.next();

               assert var5.getContextPath() != null : "Port " + var5 + " does not have a context path";

               if (var1 == null) {
                  var1 = var5.getContextPath();
               } else if (!var1.equals(var5.getContextPath())) {
                  this.ctx.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var3.getJClass(), "port.invalidContextPath", new Object[]{var3.getServiceQName().getLocalPart(), var5.getContextPath(), var1})));
               }
            }
         }
      }

   }

   private void validateDocWrapped() {
   }

   private void validateContextPathAndServiceUris() {
      HashSet var1 = new HashSet();
      Iterator var2 = this.webServices.iterator();

      while(var2.hasNext()) {
         WebServiceDecl var3 = (WebServiceDecl)var2.next();
         HashSet var4 = new HashSet();

         String var7;
         for(Iterator var5 = var3.getPorts(); var5.hasNext(); var4.add(var7)) {
            PortDecl var6 = (PortDecl)var5.next();
            var7 = var6.getNormalizedPath();
            if (var1.contains(var7)) {
               this.ctx.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var3.getJClass(), "port.uri.alreadyInUse", new Object[]{var6.getContextPath(), var6.getServiceUri(), var3.getServiceQName().getLocalPart()})));
            }
         }

         var1.addAll(var4);
      }

   }

   private void validateServiceUris() {
      if (!this.isEjb() && this.getWebAppBean() != null) {
         HashSet var1 = new HashSet();
         ServletMappingBean[] var2 = this.getWebAppBean().getServletMappings();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ServletMappingBean var5 = var2[var4];
            String[] var6 = var5.getUrlPatterns();
            if (var6 != null && var6.length > 0) {
               var1.addAll(Arrays.asList(var6));
            }
         }

         Iterator var7 = this.webServices.iterator();

         while(var7.hasNext()) {
            WebServiceDecl var8 = (WebServiceDecl)var7.next();
            Iterator var9 = var8.getPorts();

            while(var9.hasNext()) {
               PortDecl var10 = (PortDecl)var9.next();
               if (var1.contains(var10.getServiceUri())) {
                  this.ctx.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var8.getJClass(), "port.serviceUri.alreadyInUse", new Object[]{var10.getServiceUri(), var8.getServiceQName().getLocalPart()})));
               }
            }
         }
      }

   }

   public void compile() throws WsBuildException {
      if (!this.getOutputDir().exists() && !this.getOutputDir().mkdirs()) {
         throw new WsBuildException("Unable to create JWS output directory " + this.getOutputDir().getAbsolutePath());
      } else {
         this.validateDescriptors();
         this.buildWebServices();
         this.generateEJBs();
         JwsArchiveWriter var1 = new JwsArchiveWriter();
         var1.process(this);
      }
   }

   private void generateEJBs() throws WsBuildException {
      if (this.isEjb()) {
         if (this.webServices.isEmpty()) {
            return;
         }

         if (((WebServiceDecl)this.webServices.get(0)).getType() == WebServiceType.JAXRPC) {
            EjbGenInvoker var1 = new EjbGenInvoker();
            var1.process(this);
         }
      }

   }

   private void buildWebServices() throws CompileException, WsBuildException {
      UniqueNameSet var1 = new UniqueNameSet();
      JWSProcessor var2 = JWSProcessorFactory.createProcessor();
      var2.init(this);
      Iterator var3 = this.webServices.iterator();

      while(var3.hasNext()) {
         WebServiceDecl var4 = (WebServiceDecl)var3.next();
         ClassLoader var5 = Thread.currentThread().getContextClassLoader();
         if (WebServiceType.JAXWS.equals(var4.getType())) {
            JAXWSClassLoaderFactory.getInstance().setContextLoader(var5);
         }

         try {
            var4.setArtifactName(var1.add(var4.getServiceQName().getLocalPart()));

            try {
               WebServiceInfo var6 = WebServiceInfoFactory.newInstance(this, var4, this.bindingFiles);
               var2.process(var6);
            } catch (WsBuildException var10) {
               this.ctx.getLogger().log(EventLevel.ERROR, var10.getMessage());
               throw new CompileException(var4, var10);
            }
         } finally {
            Thread.currentThread().setContextClassLoader(var5);
         }
      }

      var2.finish();
   }

   public void setBindingFiles(File[] var1) {
      this.bindingFiles = var1;
   }

   public void setJaxRPCWrappedArrayStyle(boolean var1) {
      this.jaxRPCWrappedArrayStyle = var1;
   }

   public boolean isJaxRPCWrappedArrayStyle() {
      return this.jaxRPCWrappedArrayStyle;
   }

   public void cleanup() {
      if (this.webServices != null) {
         this.webServices.clear();
         this.webServices = null;
      }

      this.bindingFiles = null;
      this.descriptors = null;
      this.webAppBean = null;
      this.weblogicWebAppBean = null;
      this.webservicePolicyRefBean = null;
      this.webServicesBean = null;
      this.weblogicWebServicesBean = null;
   }

   public void setUpperCasePropName(boolean var1) {
      this.upperCasePropName = var1;
   }

   public boolean isUpperCasePropName() {
      return this.upperCasePropName;
   }

   public void setLocalElementDefaultRequired(boolean var1) {
      this.localElementDefaultRequired = var1;
   }

   public boolean isLocalElementDefaultRequired() {
      return this.localElementDefaultRequired;
   }

   public void setLocalElementDefaultNillable(boolean var1) {
      this.localElementDefaultNillable = var1;
   }

   public boolean isLocalElementDefaultNillable() {
      return this.localElementDefaultNillable;
   }
}
