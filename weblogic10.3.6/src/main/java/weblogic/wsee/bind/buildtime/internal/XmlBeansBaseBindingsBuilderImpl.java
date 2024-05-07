package weblogic.wsee.bind.buildtime.internal;

import com.bea.staxb.buildtime.Schema2Java;
import com.bea.staxb.buildtime.internal.tylar.Tylar;
import com.bea.staxb.buildtime.internal.tylar.TylarWriter;
import com.bea.util.annogen.generate.internal.joust.CodeGenUtil;
import com.bea.xbean.common.XmlErrorWatcher;
import com.bea.xbean.config.BindingConfigImpl;
import com.bea.xbean.schema.BuiltinSchemaTypeSystem;
import com.bea.xbean.schema.SchemaTypeSystemCompiler;
import com.bea.xbean.schema.SchemaTypeSystemImpl;
import com.bea.xbean.util.FilerImpl;
import com.bea.xbean.xb.xmlconfig.ConfigDocument;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.BindingConfig;
import com.bea.xml.Filer;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.SchemaTypeSystem;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlDocumentProperties;
import com.bea.xml.XmlException;
import com.bea.xml.XmlOptions;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.xml.namespace.QName;
import repackage.Repackager;
import weblogic.wsee.bind.BaseTypeLoaderFactory;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;
import weblogic.wsee.bind.buildtime.S2JBindingsBuilder;
import weblogic.wsee.jws.wlw.WLW81XBeanTylar;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlDefinitions;

public abstract class XmlBeansBaseBindingsBuilderImpl extends BindingsBuilderBase implements S2JBindingsBuilder {
   protected static final boolean VERBOSE = Verbose.isVerbose(XmlBeansBaseBindingsBuilderImpl.class);
   protected File[] xsdConfigFiles;
   public static final String SRC_OUTPUT_PATH;

   public void addHolderType(QName var1) {
   }

   public void addHolderElement(QName var1) {
   }

   public BuildtimeBindings createBuildtimeBindings(File var1, WsdlDefinitions var2) throws IOException, XmlException {
      if (var1 == null) {
         throw new IllegalArgumentException("XmlBeansBuildtimeBindings.createBuildtimeBindings() received null output dir");
      } else {
         if (VERBOSE) {
            Verbose.banner("XmlBeansBindingsBuilderImpl.createBuildtimeBindings");
            Verbose.log((Object)("generating BuildtimeBindings in " + var1 + " using " + this.schemaDocs.size() + " schemaDocs"));
         }

         var1.mkdirs();
         SchemaDocument[] var3 = new SchemaDocument[this.schemaDocs.size()];
         var3 = (SchemaDocument[])this.schemaDocs.toArray(var3);
         String var4 = convertDirtyURIStringToFile(var2.getWsdlLocation());
         if (var4 == null) {
            throw new XmlException("Invalid WSDL Location: " + var2.getWsdlLocation());
         } else {
            SchemaDocument.Schema[] var5 = new SchemaDocument.Schema[this.schemaDocs.size()];

            for(int var6 = 0; var6 < var5.length; ++var6) {
               var5[var6] = var3[var6].getSchema();
               if (var5[var6].documentProperties().getSourceName() == null) {
                  var5[var6].documentProperties().setSourceName(var4);
               } else {
                  String var7 = convertDirtyURIStringToFile(var5[var6].documentProperties().getSourceName());
                  if (var7 != null) {
                     var5[var6].documentProperties().setSourceName(var7);
                  }
               }
            }

            SchemaTypeSystem var17 = BuiltinSchemaTypeSystem.get();
            XmlOptions var18 = new XmlOptions();
            var18.setCompileNoAnnotations();
            var18.setCompileDownloadUrls();
            var18.setCompileNoValidation();
            var18.setGenerateJavaVersion("1.5");
            var18.setCompileNoPvrRule();
            FilerImpl var8 = new FilerImpl(var1, var1, (Repackager)null, false, false);
            ConfigDocument.Config[] var9 = this.getBindingConfig(this.xsdConfigFiles);
            SchemaTypeSystem var10 = this.compileXmlBeans(var1, (String)null, (SchemaTypeSystem)null, var5, var9, var17, var8, var18, (new File(var4)).getParentFile());
            XmlBeansBaseBuildtimeBindings var11 = this.createBuildtimeBindings(var10, var3, var2, this.codegenDir);
            SchemaTypeLoader var12 = BaseTypeLoaderFactory.newInstance((Tylar)null);
            XmlOptions var13 = new XmlOptions();
            var13.setCompileDownloadUrls();
            var13.setCompileNoAnnotations();
            var13.setGenerateJavaVersion("1.5");
            var13.setCompileNoPvrRule();
            SchemaTypeSystem var14 = XmlBeans.compileXsd(var5, var12, var13);
            Schema2Java var15 = new Schema2Java(var14);
            var15.setParamTypes(this.paramTypes);
            var15.setParamElements(this.paramElements);
            var15.setFaultTypes(this.faultTypes);
            var15.setFaultElements(this.faultElements);
            var15.setWrapperOperations(this.wrapperInfos);
            var15.setJaxRpcRules(false);
            var15.setJaxRPCWrappedArrayStyle(false);
            var15.setBindingConfig(var9);
            var15.setWriteJavaFiles(false);
            if (this.codegenDir != null) {
               var15.setCodegenDir(this.codegenDir);
            }

            var15.setVerbose(VERBOSE);
            var15.setCompileJava(false);
            WLW81XBeanTylar var16 = WLW81XBeanTylar.create(var1);
            var15.bind((TylarWriter)var16);
            ((TylarWriter)var16).close();
            var11.setWrapperElements(var15.getNameListFromWrapperElement());
            var11.setTylar(var16);
            return var11;
         }
      }
   }

   protected abstract XmlBeansBaseBuildtimeBindings createBuildtimeBindings(SchemaTypeSystem var1, SchemaDocument[] var2, WsdlDefinitions var3, File var4) throws IOException, XmlException;

   protected abstract boolean shouldCompile();

   private SchemaTypeSystem compileXmlBeans(File var1, String var2, SchemaTypeSystem var3, SchemaDocument.Schema[] var4, ConfigDocument.Config[] var5, SchemaTypeLoader var6, Filer var7, XmlOptions var8, File var9) throws XmlException {
      if (!this.shouldCompile()) {
         return null;
      } else {
         if (VERBOSE) {
            for(int var10 = 0; var10 < var4.length; ++var10) {
               debug("================= SCHEMA PROPS======================");
               XmlDocumentProperties var11 = var4[var10].documentProperties();
               debug("Id: " + var4[var10].getId());
               debug("Target Namesace: " + var4[var10].getTargetNamespace());
               debug("DocTypeName: " + var11.getDoctypeName());
               debug("Public Id: " + var11.getDoctypePublicId());
               debug("System Id: " + var11.getDoctypeSystemId());
               debug("ENCODING: " + var11.getEncoding());
               debug("SOURCE NAME: " + var11.getSourceName());
               debug("BASEURI: " + var9.toURI());
               debug("BASEURI FILE: " + var9.getAbsolutePath());
               debug("VERSION: " + var11.getVersion());
               debug("==================================================");
            }
         }

         File var19 = new File(var1, SRC_OUTPUT_PATH);
         var19.getParentFile().mkdir();
         var19.mkdir();
         if (!var19.exists()) {
            throw new XmlException("Could not create schemas folder " + var19.getAbsolutePath());
         } else {
            Object var20 = (Collection)var8.get("ERROR_LISTENER");
            if (var20 == null) {
               var20 = new ArrayList();
            }

            XmlErrorWatcher var12 = new XmlErrorWatcher((Collection)var20);
            BindingConfig var13 = null;
            File[] var14 = new File[0];
            File[] var15 = CodeGenUtil.systemClasspath();
            if (var5 != null && var5.length > 0) {
               var13 = BindingConfigImpl.forConfigDocuments(var5, var14, var15);
            } else {
               var13 = BindingConfigImpl.forConfigDocuments(new ConfigDocument.Config[0], var14, var15);
            }

            HashMap var16 = new HashMap();
            SchemaTypeSystemCompiler.Parameters var17 = new SchemaTypeSystemCompiler.Parameters();
            var17.setName((String)null);
            var17.setConfig(var13);
            var17.setErrorListener(var12);
            var17.setExistingTypeSystem(var3);
            var17.setSchemas(var4);
            var17.setLinkTo(var6);
            var17.setOptions(var8);
            var17.setJavaize(true);
            var17.setBaseURI(var9.toURI());
            var17.setSourcesToCopyMap(var16);
            SchemaTypeSystemImpl var18 = (SchemaTypeSystemImpl)SchemaTypeSystemCompiler.compile(var17);
            if (var12.hasError() && var18 == null) {
               throw new XmlException(var12.firstError());
            } else {
               if (var18 != null && !var18.isIncomplete() && var7 != null) {
                  var18.save(var7);
                  SchemaTypeSystemCompiler.generateTypes(var18, var7, var8);
               }

               return var18;
            }
         }
      }
   }

   public void setXsdConfig(File[] var1) {
      this.xsdConfigFiles = var1;
   }

   public static final String convertDirtyURIStringToFile(String var0) {
      if (var0 == null) {
         return null;
      } else {
         String var1 = var0.replace('\\', '/').replaceAll("%20", " ");
         boolean var2 = var1.startsWith("jar:");
         var1 = var1.replaceAll("jar:", "");
         if (var1.startsWith("file:")) {
            if (!var1.startsWith("file:/")) {
               var1 = var1.substring("file:".length());
            } else {
               var1 = var1.substring("file:/".length());
            }

            var1 = "/" + var1;
            var1 = (new File(var1)).toURI().toString();
         }

         if (var2) {
            var1 = "jar:".concat(var1);
         }

         return var1;
      }
   }

   public static final void debug(String var0) {
      if (VERBOSE) {
         Verbose.log((Object)var0);
      }

   }

   static {
      SRC_OUTPUT_PATH = "schema" + SchemaTypeSystemImpl.METADATA_PACKAGE_GEN + "/src";
   }
}
