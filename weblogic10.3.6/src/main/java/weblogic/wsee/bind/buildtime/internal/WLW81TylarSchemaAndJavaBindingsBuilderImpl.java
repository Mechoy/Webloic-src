package weblogic.wsee.bind.buildtime.internal;

import com.bea.staxb.buildtime.internal.tylar.CompositeTylar;
import com.bea.staxb.buildtime.internal.tylar.Tylar;
import com.bea.staxb.buildtime.internal.tylar.TylarImpl;
import com.bea.staxb.buildtime.internal.tylar.TylarWriter;
import com.bea.util.jam.JClass;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.SchemaTypeSystem;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlException;
import com.bea.xml.XmlOptions;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.bind.BaseTypeLoaderFactory;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;
import weblogic.wsee.bind.buildtime.J2SBindingsBuilder;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlService;

public class WLW81TylarSchemaAndJavaBindingsBuilderImpl extends TylarBindingsBuilderBase implements J2SBindingsBuilder {
   private static final boolean VERBOSE = Verbose.isVerbose(WLW81TylarSchemaAndJavaBindingsBuilderImpl.class);
   private JClass serviceJClass;
   private WsdlService wsdlService;
   private boolean flipCallbackInputAndOutputParts;

   public WLW81TylarSchemaAndJavaBindingsBuilderImpl(boolean var1) {
      this.flipCallbackInputAndOutputParts = var1;
   }

   public WLW81TylarSchemaAndJavaBindingsBuilderImpl() {
      this(true);
      if (VERBOSE) {
         Verbose.log((Object)"Constructed a WLW81TylarSchemaAndJavaBindingsBuilderImpl");
      }

   }

   public void javaTypeToSchemaType(JClass var1, JClass var2, int var3) {
   }

   public void javaTypeToSchemaElement(JClass var1, JClass var2, QName var3, int var4) {
   }

   public void wrapJavaTypeToSchemaElement(JClass var1, JClass[] var2, String[] var3, QName var4) {
   }

   public void setServiceJClass(JClass var1) {
      this.serviceJClass = var1;
   }

   public JClass getServiceJClass() {
      return this.serviceJClass;
   }

   public void setWsdlService(WsdlService var1) {
      this.wsdlService = var1;
   }

   public WsdlService getWsdlService() {
      return this.wsdlService;
   }

   public BuildtimeBindings createBuildtimeBindings(File var1) throws IOException, XmlException {
      if (var1 == null) {
         throw new IllegalArgumentException("null dir");
      } else {
         if (VERBOSE) {
            Verbose.banner("WLW81TylarSchemaAndJavaBindingsBuilderImpl.createBuildtimeBindings");
            Verbose.log((Object)("generating BuildtimeBindings in " + var1 + " using " + this.schemaDocs.size() + " schemas"));
         }

         var1.mkdirs();
         SchemaDocument.Schema[] var2 = new SchemaDocument.Schema[this.schemaDocs.size()];
         int var3 = 0;

         SchemaDocument var5;
         for(Iterator var4 = this.schemaDocs.iterator(); var4.hasNext(); var2[var3++] = var5.getSchema()) {
            var5 = (SchemaDocument)var4.next();
         }

         Tylar var13 = this.getBaseTypeLibraries();
         SchemaTypeLoader var14 = BaseTypeLoaderFactory.newInstance(var13);
         XmlOptions var6 = new XmlOptions();
         var6.setCompileDownloadUrls();
         var6.setCompileNoAnnotations();
         SchemaTypeSystem var7 = XmlBeans.compileXsd(var2, var14, var6);
         WLW81SchemaAndJavaBinder var8 = new WLW81SchemaAndJavaBinder(var7);
         var8.setFlipCallbackInputAndOutputParts(this.flipCallbackInputAndOutputParts);
         var8.setServiceJClass(this.getServiceJClass());
         var8.setWsdlService(this.getWsdlService());
         var8.setJaxRpcRules(true);
         if (var13 != null) {
            var8.setBaseLibrary(var13);
         }

         var8.setVerbose(VERBOSE);
         Object var9 = new TylarImpl();
         var8.bind((TylarWriter)var9);
         Iterator var10 = this.schemaDocs.iterator();

         while(var10.hasNext()) {
            SchemaDocument var11 = (SchemaDocument)var10.next();
            ((TylarWriter)var9).writeSchema(var11, "fixme-schema-0.xsd", (Map)null);
         }

         ((TylarWriter)var9).close();
         if (var9 == null) {
            throw new IOException("binding failed, check log for details");
         } else {
            if (var13 != null) {
               var9 = new CompositeTylar(new Tylar[]{(Tylar)var9, var13});
            }

            TylarBuildtimeBindings var12 = new TylarBuildtimeBindings((Tylar)var9, var1, J2SBindingsBuilder.class, this.getXmlObjectClassLoader());
            var12.setWrapperElements(var8.getNameListFromWrapperElement());
            return var12;
         }
      }
   }
}
