package weblogic.wsee.tools.jws.jaxrpc;

import com.bea.xbean.xb.xsdschema.SchemaDocument;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.jws.WildcardParticle;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;
import weblogic.wsee.bind.buildtime.J2SBindingsBuilder;
import weblogic.wsee.bind.buildtime.TylarJ2SBindingsBuilder;
import weblogic.wsee.bind.buildtime.internal.WLW81TylarJ2SBindingsBuilderImpl;
import weblogic.wsee.bind.buildtime.internal.WLW81TylarSchemaAndJavaBindingsBuilderImpl;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.clientgen.ClientGenUtil;
import weblogic.wsee.tools.jws.ModuleInfo;
import weblogic.wsee.tools.jws.WebServiceInfo;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlFactory;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.WsdlTypes;

public class JAXRPCWebServiceInfo extends WebServiceInfo<WebServiceSEIDecl> {
   private File outputDir;
   private byte[] endpointInterface;
   private JavaWsdlMappingBean javaWsdlMappingBean;
   private J2SBindingsBuilder j2sBuilder;
   private BuildtimeBindings buildtimeBindings;

   public JAXRPCWebServiceInfo(ModuleInfo var1, WebServiceSEIDecl var2, File[] var3) throws WsBuildException {
      super(var2);
      this.outputDir = var1.getOutputDir();
      J2SBindingsBuilder var4 = null;
      if (!var2.isWlw81UpgradedService()) {
         var4 = this.loadBindingsBuilder(var3, var1.isJaxRPCWrappedArrayStyle(), var1.isUpperCasePropName(), var1.isLocalElementDefaultRequired(), var1.isLocalElementDefaultNillable());
      } else {
         var4 = this.loadWLW81BindingsBuilder(var1.isLocalElementDefaultRequired(), var1.isLocalElementDefaultNillable());
      }

      var4.setXmlObjectClassLoader(var1.getJwsBuildContext().getClassLoader());
      this.setBindingsBuilder(var4);
   }

   private J2SBindingsBuilder loadBindingsBuilder(File[] var1, boolean var2, boolean var3, boolean var4, boolean var5) throws WsBuildException {
      J2SBindingsBuilder var6 = J2SBindingsBuilder.Factory.newInstance();

      assert var6 instanceof TylarJ2SBindingsBuilder;

      ((TylarJ2SBindingsBuilder)var6).setXsdConfig(var1);
      ((TylarJ2SBindingsBuilder)var6).setJaxRpcByteArrayStyle(var2);
      ((TylarJ2SBindingsBuilder)var6).setUpperCasePropName(var3);
      ((TylarJ2SBindingsBuilder)var6).setLocalElementDefaultRequired(var4);
      ((TylarJ2SBindingsBuilder)var6).setLocalElementDefaultNillable(var5);
      Iterator var7 = ((WebServiceSEIDecl)this.webService).getWildcardBindings().getBindings().entrySet().iterator();

      while(var7.hasNext()) {
         Map.Entry var8 = (Map.Entry)var7.next();
         ((TylarJ2SBindingsBuilder)var6).addWildcardBinding((String)var8.getKey(), (WildcardParticle)var8.getValue());
      }

      ClassLoader var10 = null;
      if (((WebServiceSEIDecl)this.webService).getCowReader() != null) {
         var10 = ((WebServiceSEIDecl)this.webService).getCowReader().getClassLoader();

         try {
            this.setDefinitions(((WebServiceSEIDecl)this.webService).getCowReader().getWsdl(((WebServiceSEIDecl)this.webService).getWsdlLocation()));
         } catch (WsdlException var9) {
            throw new WsBuildException(var9);
         }
      }

      if (var10 != null) {
         var6.addBaseTypeLibrary(var10);
      }

      return var6;
   }

   private J2SBindingsBuilder loadWLW81BindingsBuilder(boolean var1, boolean var2) throws WsBuildException {
      Object var3 = null;
      String var4 = ((WebServiceSEIDecl)this.webService).getWsdlLocation();
      boolean var5 = ((WebServiceSEIDecl)this.webService).isClientSideCallbackService();
      Map.Entry var8;
      if (var4 != null && !var5) {
         WLW81TylarSchemaAndJavaBindingsBuilderImpl var14 = new WLW81TylarSchemaAndJavaBindingsBuilderImpl();
         File var15 = new File(((WebServiceSEIDecl)this.webService).getSourceFile().getParent(), var4);
         if (var15.exists()) {
            var4 = var15.getAbsolutePath();
         }

         var8 = null;

         WsdlDefinitions var16;
         try {
            var16 = WsdlFactory.getInstance().parse(var4);
            this.setDefinitions(var16);
         } catch (WsdlException var12) {
            throw new WsBuildException(var12);
         }

         WsdlTypes var9 = var16.getTypes();
         if (var9 != null) {
            SchemaDocument[] var10 = var9.getSchemaArray();

            for(int var11 = 0; var11 < var10.length; ++var11) {
               var14.addSchemaDocument(var10[var11]);
            }
         }

         QName var17 = ClientGenUtil.findServiceName(var16, ((WebServiceSEIDecl)this.webService).getServiceName(), var4);
         var14.setServiceJClass(((WebServiceSEIDecl)this.webService).getJClass());
         var14.setWsdlService((WsdlService)var16.getServices().get(var17));
         var3 = var14;
      } else {
         WLW81TylarJ2SBindingsBuilderImpl var6 = new WLW81TylarJ2SBindingsBuilderImpl(((WebServiceSEIDecl)this.webService).getJClass());
         if (var5 && ((WebServiceSEIDecl)this.webService).getCowReader() != null) {
            var6.addBaseTypeLibrary(((WebServiceSEIDecl)this.webService).getCowReader().getClassLoader());

            try {
               this.setDefinitions(((WebServiceSEIDecl)this.webService).getCowReader().getWsdl(((WebServiceSEIDecl)this.webService).getWsdlLocation()));
            } catch (WsdlException var13) {
               throw new WsBuildException(var13);
            }
         }

         var6.setLocalElementDefaultRequired(var1);
         var6.setLocalElementDefaultNillable(var2);
         Iterator var7 = ((WebServiceSEIDecl)this.webService).getWildcardBindings().getBindings().entrySet().iterator();

         while(var7.hasNext()) {
            var8 = (Map.Entry)var7.next();
            var6.addWildcardBinding((String)var8.getKey(), (WildcardParticle)var8.getValue());
         }

         var3 = var6;
      }

      return (J2SBindingsBuilder)var3;
   }

   public WebServiceSEIDecl getWebService() {
      return (WebServiceSEIDecl)this.webService;
   }

   public WsdlDefinitions getDefinitions() {
      return this.definitions;
   }

   public void setDefinitions(WsdlDefinitions var1) {
      this.definitions = var1;
   }

   public byte[] getEndpointInterface() {
      return this.endpointInterface;
   }

   public void setEndpointInterface(byte[] var1) {
      this.endpointInterface = var1;
   }

   public JavaWsdlMappingBean getJavaWsdlMappingBean() {
      return this.javaWsdlMappingBean;
   }

   public void setJavaWsdlMappingBean(JavaWsdlMappingBean var1) {
      this.javaWsdlMappingBean = var1;
   }

   public J2SBindingsBuilder getBindingsBuilder() {
      return this.j2sBuilder;
   }

   public void setBindingsBuilder(J2SBindingsBuilder var1) {
      this.j2sBuilder = var1;
   }

   public BuildtimeBindings getBuildtimeBindings() {
      return this.buildtimeBindings;
   }

   public void setBuildtimeBindings(BuildtimeBindings var1) {
      this.buildtimeBindings = var1;
   }

   public BuildtimeBindings createBindings() throws WsBuildException {
      if (this.buildtimeBindings == null) {
         try {
            this.buildtimeBindings = this.getBindingsBuilder().createBuildtimeBindings(this.outputDir);
         } catch (Exception var2) {
            throw new WsBuildException(var2);
         }
      }

      return this.buildtimeBindings;
   }
}
