package weblogic.wsee.bind.buildtime.internal;

import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.SchemaTypeSystem;
import com.bea.xml.XmlException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.BindingConfig;
import org.apache.xmlbeans.Filer;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlDocumentProperties;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.NameUtil;
import org.apache.xmlbeans.impl.common.XmlErrorWatcher;
import org.apache.xmlbeans.impl.config.BindingConfigImpl;
import org.apache.xmlbeans.impl.schema.SchemaTypeSystemCompiler;
import org.apache.xmlbeans.impl.schema.SchemaTypeSystemImpl;
import org.apache.xmlbeans.impl.tool.CodeGenUtil;
import org.apache.xmlbeans.impl.util.FilerImpl;
import org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelElement;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelSimpleType;
import org.w3c.dom.Node;
import repackage.Repackager;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;
import weblogic.wsee.bind.buildtime.S2JBindingsBuilder;
import weblogic.wsee.util.HolderUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.jspgen.GenFactory;
import weblogic.wsee.wsdl.WsdlDefinitions;

public class XmlBeansApacheBindingsBuilderImpl extends XmlBeansBaseBindingsBuilderImpl implements S2JBindingsBuilder {
   private static final boolean VERBOSE = Verbose.isVerbose(XmlBeansApacheBindingsBuilderImpl.class);
   private static final boolean useExistingXBeans = Boolean.getBoolean("weblogic.wsee.bind.useExistingXBeans");
   protected Set<QName> holderTypeNames = new HashSet();
   protected Set<QName> holderElementNames = new HashSet();
   public static final String SRC_OUTPUT_PATH;

   public XmlBeansApacheBindingsBuilderImpl() {
      if (VERBOSE) {
         Verbose.log((Object)"Constructed a XmlBeansApacheBindingsBuilderImpl");
      }

   }

   protected XmlBeansBaseBuildtimeBindings createBuildtimeBindings(SchemaTypeSystem var1, SchemaDocument[] var2, WsdlDefinitions var3, File var4) throws IOException, XmlException {
      ArrayList var5 = new ArrayList();
      var5.add(var3);
      List var6 = var3.getImportedWsdlDefinitions();
      if (var6 != null) {
         var5.addAll(var6);
      }

      HashSet var7 = new HashSet();
      org.apache.xmlbeans.SchemaTypeSystem var8 = null;
      SchemaTypeLoader var9 = XmlBeans.typeLoaderForClassLoader(org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.class.getClassLoader());
      if (useExistingXBeans) {
         SchemaTypeLoader var10 = XmlBeans.typeLoaderForClassLoader(Thread.currentThread().getContextClassLoader());
         var9 = XmlBeans.typeLoaderUnion(new SchemaTypeLoader[]{var10, var9});
      }

      Iterator var22 = var5.iterator();

      while(var22.hasNext()) {
         WsdlDefinitions var11 = (WsdlDefinitions)var22.next();
         SchemaDocument[] var12 = new SchemaDocument[this.schemaDocs.size()];
         var12 = (SchemaDocument[])this.schemaDocs.toArray(var12);
         String var13 = convertDirtyURIStringToFile(var11.getWsdlLocation());
         if (var13 == null) {
            throw new XmlException("Invalid WSDL Location: " + var11.getWsdlLocation());
         }

         org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema[] var14 = new org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema[var12.length];

         int var15;
         String var19;
         try {
            for(var15 = 0; var15 < var12.length; ++var15) {
               SchemaDocument var16 = var12[var15];
               Node var17 = var16.getDomNode();
               org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument var18 = org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Factory.parse(var17);
               var14[var15] = var18.getSchema();
               if (var14[var15].documentProperties().getSourceName() == null) {
                  var14[var15].documentProperties().setSourceName(var13);
               } else {
                  var19 = convertDirtyURIStringToFile(var14[var15].documentProperties().getSourceName());
                  if (var19 != null) {
                     var14[var15].documentProperties().setSourceName(var19);
                  }
               }
            }
         } catch (Exception var21) {
            throw new XmlException("Exception attempting to create apache XmlObject for DOM node " + var21.getMessage());
         }

         if (useExistingXBeans) {
            var7.clear();

            for(var15 = 0; var15 < var14.length; ++var15) {
               org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema var25 = var14[var15];
               String var27 = null;
               if (var25.isSetTargetNamespace()) {
                  var27 = var25.getTargetNamespace();
               }

               if (var27 != null && var27.length() > 0) {
                  if (var25.sizeOfComplexTypeArray() > 0) {
                     TopLevelComplexType var31 = var25.getComplexTypeArray(0);
                     var19 = var31.getName();
                     if (var19 != null && var19.length() > 0) {
                        if (VERBOSE) {
                           Verbose.log((Object)(" ApacheXBeanCompile:  check complexType '{" + var27 + "}" + var19 + "'"));
                        }

                        if (var9.findType(new QName(var27, var19)) == null) {
                           if (VERBOSE) {
                              Verbose.log((Object)(" ApacheXBeanCompile:  adding SCHEMA for complexType '{" + var27 + "}" + var19 + "'"));
                           }

                           var7.add(var25);
                        }
                     } else {
                        var7.add(var25);
                     }
                  } else if (var25.sizeOfElementArray() > 0) {
                     TopLevelElement var30 = var25.getElementArray(0);
                     var19 = var30.getName();
                     if (var19 != null && var19.length() > 0) {
                        if (VERBOSE) {
                           Verbose.log((Object)(" ApacheXBeanCompile:  check element '{" + var27 + "}" + var19 + "'"));
                        }

                        if (var9.findElement(new QName(var27, var19)) == null) {
                           if (VERBOSE) {
                              Verbose.log((Object)(" ApacheXBeanCompile:  adding SCHEMA for element '{" + var27 + "}" + var19 + "'"));
                           }

                           var7.add(var25);
                        }
                     } else {
                        var7.add(var25);
                     }
                  } else if (var25.sizeOfSimpleTypeArray() > 0) {
                     TopLevelSimpleType var29 = var25.getSimpleTypeArray(0);
                     var19 = var29.getName();
                     if (var19 != null && var19.length() > 0) {
                        if (VERBOSE) {
                           Verbose.log((Object)(" ApacheXBeanCompile:  check simpleType '{" + var27 + "}" + var19 + "'"));
                        }

                        if (var9.findType(new QName(var27, var19)) == null) {
                           if (VERBOSE) {
                              Verbose.log((Object)(" ApacheXBeanCompile:  adding SCHEMA for simpleType '{" + var27 + "}" + var19 + "'"));
                           }

                           var7.add(var25);
                        }
                     } else {
                        var7.add(var25);
                     }
                  } else {
                     var7.add(var25);
                  }
               } else {
                  var7.add(var25);
               }
            }

            var14 = (org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema[])((org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema[])var7.toArray(new org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema[0]));
         }

         XmlOptions var24 = new XmlOptions();
         var24.setCompileNoAnnotations();
         var24.setCompileDownloadUrls();
         var24.setCompileNoValidation();
         var24.setGenerateJavaVersion("1.5");
         var24.setCompileNoPvrRule();
         FilerImpl var26 = new FilerImpl(var4, var4, (Repackager)null, false, false);

         try {
            org.apache.xmlbeans.SchemaTypeSystem var28 = this.compileXmlBeans(var4, var8, var14, this.getApacheBindingConfig(this.xsdConfigFiles), var9, var26, var24, (new File(var13)).getParentFile());
            if (var8 == null && var28 == null) {
               throw new XmlException("XmlBeans compile failed for an unknown reason for wsdl " + var13);
            }

            if (var28 != null) {
               var8 = var28;
            }
         } catch (org.apache.xmlbeans.XmlException var20) {
            throw new XmlException("Exception while compiling org apache xmlbeans: " + var20.getMessage(), var20);
         }
      }

      XmlBeansApacheBuildtimeBindings var23 = new XmlBeansApacheBuildtimeBindings(var1, var2, XmlBeans.typeLoaderUnion(new SchemaTypeLoader[]{var9, var8}));
      this.generateHolderTypes(var4, var23);
      return var23;
   }

   private org.apache.xmlbeans.SchemaTypeSystem compileXmlBeans(File var1, org.apache.xmlbeans.SchemaTypeSystem var2, org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema[] var3, ConfigDocument.Config[] var4, SchemaTypeLoader var5, Filer var6, XmlOptions var7, File var8) throws org.apache.xmlbeans.XmlException {
      if (VERBOSE) {
         for(int var9 = 0; var9 < var3.length; ++var9) {
            debug("================= SCHEMA PROPS======================");
            XmlDocumentProperties var10 = var3[var9].documentProperties();
            debug("Id: " + var3[var9].getId());
            debug("Target Namesace: " + var3[var9].getTargetNamespace());
            debug("DocTypeName: " + var10.getDoctypeName());
            debug("Public Id: " + var10.getDoctypePublicId());
            debug("System Id: " + var10.getDoctypeSystemId());
            debug("ENCODING: " + var10.getEncoding());
            debug("SOURCE NAME: " + var10.getSourceName());
            debug("BASEURI: " + var8.toURI());
            debug("BASEURI FILE: " + var8.getAbsolutePath());
            debug("VERSION: " + var10.getVersion());
            debug("==================================================");
         }
      }

      File var18 = new File(var1, SRC_OUTPUT_PATH);
      var18.getParentFile().mkdir();
      var18.mkdir();
      if (!var18.exists()) {
         throw new org.apache.xmlbeans.XmlException("Could not create schemas folder " + var18.getAbsolutePath());
      } else {
         Object var19 = (Collection)var7.get("ERROR_LISTENER");
         if (var19 == null) {
            var19 = new ArrayList();
         }

         XmlErrorWatcher var11 = new XmlErrorWatcher((Collection)var19);
         BindingConfig var12 = null;
         File[] var13 = new File[0];
         File[] var14 = CodeGenUtil.systemClasspath();
         if (var4 != null && var4.length > 0) {
            var12 = BindingConfigImpl.forConfigDocuments(var4, var13, var14);
         } else {
            var12 = BindingConfigImpl.forConfigDocuments(new ConfigDocument.Config[0], var13, var14);
         }

         HashMap var15 = new HashMap();
         SchemaTypeSystemCompiler.Parameters var16 = new SchemaTypeSystemCompiler.Parameters();
         var16.setName((String)null);
         var16.setConfig(var12);
         var16.setErrorListener(var11);
         var16.setExistingTypeSystem(var2);
         var16.setSchemas(var3);
         var16.setLinkTo(var5);
         var16.setOptions(var7);
         var16.setJavaize(true);
         var16.setBaseURI(var8.toURI());
         var16.setSourcesToCopyMap(var15);
         var16.setSchemasDir(var18);
         SchemaTypeSystemImpl var17 = (SchemaTypeSystemImpl)SchemaTypeSystemCompiler.compile(var16);
         if (var11.hasError() && var17 == null && var2 == null) {
            throw new org.apache.xmlbeans.XmlException(var11.firstError());
         } else {
            if (var17 != null && !var17.isIncomplete() && var6 != null) {
               var17.save(var6);
               SchemaTypeSystemCompiler.generateTypes(var17, var6, var7);
            }

            return var17;
         }
      }
   }

   public void addHolderType(QName var1) {
      this.holderTypeNames.add(var1);
   }

   public void addHolderElement(QName var1) {
      this.holderElementNames.add(var1);
   }

   protected boolean shouldCompile() {
      return false;
   }

   private void generateHolderTypes(File var1, BuildtimeBindings var2) throws IOException {
      HolderUtil.NameCollisionsFilter.getInstance().reset();
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      Iterator var6 = this.holderTypeNames.iterator();

      QName var7;
      String var8;
      while(var6.hasNext()) {
         var7 = (QName)var6.next();
         var8 = var2.getClassFromSchemaType(var7);
         if (var8 != null && HolderUtil.getStandardHolder(var8) == null) {
            this.createHolderName(var8, var7, var5, var4, var3);
         }
      }

      var6 = this.holderElementNames.iterator();

      while(var6.hasNext()) {
         var7 = (QName)var6.next();
         var8 = var2.getClassFromSchemaElement(var7);
         if (HolderUtil.getStandardHolder(var8) == null) {
            this.createHolderName(var8, var7, var5, var4, var3);
         }
      }

      this.createHolderClasses(var5, var4, var3, var1);
   }

   private void createHolderName(String var1, QName var2, List<String> var3, List<String> var4, List<String> var5) throws IOException {
      if (VERBOSE) {
         Verbose.log((Object)("Creating holder for type " + var1));
      }

      String var6;
      String var7;
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

   static {
      SRC_OUTPUT_PATH = "schema" + SchemaTypeSystemImpl.METADATA_PACKAGE_GEN + "/src";
   }
}
