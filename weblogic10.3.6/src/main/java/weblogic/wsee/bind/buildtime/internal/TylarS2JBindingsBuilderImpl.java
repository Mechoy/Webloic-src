package weblogic.wsee.bind.buildtime.internal;

import com.bea.staxb.buildtime.Schema2Java;
import com.bea.staxb.buildtime.internal.bts.BindingLoader;
import com.bea.staxb.buildtime.internal.bts.BindingTypeName;
import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.buildtime.internal.tylar.CompositeTylar;
import com.bea.staxb.buildtime.internal.tylar.Tylar;
import com.bea.staxb.buildtime.internal.tylar.TylarWriter;
import com.bea.xbean.common.NameUtil;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.SchemaTypeSystem;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlException;
import com.bea.xml.XmlOptions;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.rpc.holders.ByteArrayHolder;
import weblogic.wsee.bind.BaseTypeLoaderFactory;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;
import weblogic.wsee.bind.buildtime.S2JBindingsBuilder;
import weblogic.wsee.bind.buildtime.TylarS2JBindingsBuilder;
import weblogic.wsee.bind.tylar.CompilingTylar;
import weblogic.wsee.util.HolderUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.jspgen.GenFactory;
import weblogic.wsee.wsdl.WsdlDefinitions;

public class TylarS2JBindingsBuilderImpl extends TylarBindingsBuilderBase implements TylarS2JBindingsBuilder {
   private static final boolean VERBOSE = Verbose.isVerbose(TylarS2JBindingsBuilderImpl.class);
   private boolean jaxRPCWrappedArrayStyle = false;
   private boolean writeJavaTypes = true;
   private boolean useJaxRpcRules = true;
   protected Set<QName> holderTypeNames = new HashSet();
   protected Set<QName> holderElementNames = new HashSet();
   private File[] xsdConfigFiles = null;

   public void setJaxRPCWrappedArrayStyle(boolean var1) {
      this.jaxRPCWrappedArrayStyle = var1;
   }

   public void setWriteJavaTypes(boolean var1) {
      this.writeJavaTypes = var1;
   }

   public void setUseJaxRpcRules(boolean var1) {
      this.useJaxRpcRules = var1;
   }

   public TylarS2JBindingsBuilderImpl() {
      if (VERBOSE) {
         Verbose.log((Object)"Constructed a TylarS2JBindingsBuilderImpl");
      }

   }

   public void addHolderType(QName var1) {
      this.holderTypeNames.add(var1);
   }

   public void addHolderElement(QName var1) {
      this.holderElementNames.add(var1);
   }

   public BuildtimeBindings createBuildtimeBindings(File var1, WsdlDefinitions var2) throws IOException, XmlException {
      if (var1 == null) {
         throw new IllegalArgumentException("null dir");
      } else {
         if (VERBOSE) {
            Verbose.banner("TylarS2JBindingsBuilderImpl.createBuildtimeBindings");
            Verbose.log((Object)("generating BuildtimeBindings in " + var1 + " using " + this.schemaDocs.size() + " schemas"));
         }

         var1.mkdirs();
         Tylar var3 = this.getBaseTypeLibraries();
         SchemaTypeLoader var4 = BaseTypeLoaderFactory.newInstance(var3);
         ArrayList var5 = new ArrayList();
         var5.addAll(this.schemaDocs);
         SchemaDocument[] var6 = var2.getTypes().getSchemaArray();
         int var7 = var6.length;

         SchemaDocument var9;
         for(int var8 = 0; var8 < var7; ++var8) {
            var9 = var6[var8];
            if (!var5.contains(var9) && var9.getSchema().getTargetNamespace() != null && !var4.isNamespaceDefined(var9.getSchema().getTargetNamespace())) {
               var5.add(var9);
            }
         }

         SchemaDocument.Schema[] var13 = new SchemaDocument.Schema[var5.size()];
         var7 = 0;

         for(Iterator var14 = var5.iterator(); var14.hasNext(); var13[var7++] = var9.getSchema()) {
            var9 = (SchemaDocument)var14.next();
         }

         XmlOptions var15 = new XmlOptions();
         var15.setCompileDownloadUrls();
         var15.setCompileNoAnnotations();
         var15.setCompileNoPvrRule();
         SchemaTypeSystem var16 = XmlBeans.compileXsd(var13, var4, var15);
         Schema2Java var10 = new Schema2Java(var16);
         var10.setParamTypes(this.paramTypes);
         var10.setParamElements(this.paramElements);
         var10.setFaultTypes(this.faultTypes);
         var10.setFaultElements(this.faultElements);
         var10.setWrapperOperations(this.wrapperInfos);
         var10.setIncludeGlobalTypes(this.includeGlobalTypes);
         var10.setSortSchemaTypes(this.sortSchemaTypes);
         var10.setJaxRpcRules(this.useJaxRpcRules);
         var10.setJaxRPCWrappedArrayStyle(this.jaxRPCWrappedArrayStyle);
         var10.setWriteJavaFiles(this.writeJavaTypes);
         var10.setBindingConfig(this.getBindingConfig(this.xsdConfigFiles));
         if (var3 != null) {
            var10.setBaseLibrary(var3);
         }

         if (this.codegenDir != null) {
            var10.setCodegenDir(this.codegenDir);
         }

         var10.setVerbose(VERBOSE);
         var10.setCompileJava(false);
         Object var11 = CompilingTylar.create(var1);
         var10.bind((TylarWriter)var11);
         ((TylarWriter)var11).close();
         if (var11 == null) {
            throw new IOException("binding failed, check log for details");
         } else {
            if (var3 != null) {
               var11 = new CompositeTylar(new Tylar[]{(Tylar)var11, var3});
            }

            TylarBuildtimeBindings var12 = new TylarBuildtimeBindings((Tylar)var11, var1, S2JBindingsBuilder.class, this.getXmlObjectClassLoader());
            var12.setWrapperElements(var10.getNameListFromWrapperElement());
            this.generateHolderTypes(var1, (Tylar)var11, var12);
            return var12;
         }
      }
   }

   public void includeGlobalTypes(boolean var1) {
      this.includeGlobalTypes = var1;
   }

   public void sortSchemaTypes(boolean var1) {
      this.sortSchemaTypes = var1;
   }

   private void generateHolderTypes(File var1, Tylar var2, BuildtimeBindings var3) throws IOException {
      BindingLoader var4 = var2.getBindingLoader();
      HolderUtil.NameCollisionsFilter.getInstance().reset();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();
      ArrayList var7 = new ArrayList();
      Iterator var8 = this.holderTypeNames.iterator();

      QName var9;
      while(var8.hasNext()) {
         var9 = (QName)var8.next();
         XmlTypeName var10 = XmlTypeName.forTypeNamed(var9);
         BindingTypeName var11 = var4.lookupPojoFor(var10);
         if (var11 != null) {
            String var12 = var11.getJavaName().toString();
            if (HolderUtil.getStandardHolder(var12) == null && this.getStandardSchemaHolder(var9) == null) {
               this.createHolderName(var12, var9, var7, var6, var5);
            }
         }
      }

      var8 = this.holderElementNames.iterator();

      while(var8.hasNext()) {
         var9 = (QName)var8.next();
         String var13 = var3.getClassFromSchemaElement(var9);
         if (HolderUtil.getStandardHolder(var13) == null && this.getStandardSchemaHolder(var9) == null) {
            this.createHolderName(var13, var9, var7, var6, var5);
         }
      }

      this.createHolderClasses(var7, var6, var5, var1);
   }

   private String getStandardSchemaHolder(QName var1) {
      return !"http://www.w3.org/2001/XMLSchema".equals(var1.getNamespaceURI()) && !"http://schemas.xmlsoap.org/soap/encoding/".equals(var1.getNamespaceURI()) && !"http://www.w3.org/2003/05/soap-encoding".equals(var1.getNamespaceURI()) || !"base64Binary".equals(var1.getLocalPart()) && !"hexBinary".equals(var1.getLocalPart()) && !"base64".equals(var1.getLocalPart()) ? null : ByteArrayHolder.class.getName();
   }

   private void createHolderName(String var1, QName var2, List<String> var3, List<String> var4, List<String> var5) {
      if (VERBOSE) {
         Verbose.log((Object)("Creating holder for type " + var1));
      }

      String var6 = null;
      String var7 = null;
      if (!var1.startsWith("java.") && !var1.startsWith("javax.") && !HolderUtil.isPrimitiveType(var1) && !var1.startsWith("[") && !var1.endsWith("[]")) {
         var6 = HolderUtil.getHolderPackage(var1);
         var7 = HolderUtil.getShortHolderName(var1);
      } else {
         var6 = NameUtil.getPackageFromNamespace(var2.getNamespaceURI(), true);
         if (StringUtil.isEmpty(var6)) {
            var6 = "holders";
         } else {
            var6 = var6 + ".holders";
         }

         var7 = weblogic.xml.schema.binding.internal.NameUtil.getJAXRPCClassName(var2.getLocalPart());
      }

      var7 = var7 + "Holder";
      String var8 = null;
      if (var6 != null && !"".equals(var6)) {
         var8 = var6 + "." + var7;
      } else {
         var8 = var7;
      }

      HolderUtil.NameCollisionsFilter.getInstance().use(var8);
      var3.add(var6);
      var4.add(var7);
      var5.add(var1);
   }

   private void createHolderClasses(List<String> var1, List<String> var2, List<String> var3, File var4) throws IOException {
      File var5 = this.codegenDir;
      if (var5 == null) {
         var5 = new File(var4, "META-INF/src");
         var5.mkdirs();
      }

      for(int var6 = 0; var6 < var1.size(); ++var6) {
         String var7 = (String)var1.get(var6);
         String var8 = (String)var2.get(var6);
         String var9 = (String)var3.get(var6);
         var8 = HolderUtil.NameCollisionsFilter.getInstance().filterClassName(var7, var8);
         PrintStream var10 = createJavaSourceStream(var5, var7, var8);
         HolderTypeBase var11 = (HolderTypeBase)GenFactory.create("weblogic.wsee.bind.buildtime.internal.HolderType");
         var11.setClassName(var8.endsWith("Holder") ? var8.substring(0, var8.length() - "Holder".length()) : var8);
         var11.setPackageName(var7);
         var11.setValueType(var9);
         var11.setOutput(var10);
         var11.generate();
         var10.close();
      }

   }

   private static PrintStream createJavaSourceStream(File var0, String var1, String var2) throws IOException {
      File var3 = new File(var0, var1.replace('.', File.separatorChar));
      var3.mkdirs();
      File var4 = new File(var3, var2 + ".java");
      return new PrintStream(new FileOutputStream(var4), true);
   }

   public void setXsdConfig(File[] var1) {
      this.xsdConfigFiles = var1;
   }
}
