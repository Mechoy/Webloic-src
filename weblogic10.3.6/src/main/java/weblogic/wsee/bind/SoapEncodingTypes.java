package weblogic.wsee.bind;

import com.bea.xbean.schema.SoapEncSchemaTypeSystem;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xbean.xb.xsdschema.SchemaDocument.Factory;
import com.bea.xml.SchemaTypeSystem;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlException;
import com.bea.xml.XmlOptions;
import java.io.IOException;
import java.io.InputStream;

class SoapEncodingTypes {
   private static String SOAP_ENCODING_FILE = "SoapEncoding11.xsd";
   private static SoapEncodingTypes INSTANCE = createInstance();
   private SchemaTypeSystem mTypeSystem;

   static SoapEncodingTypes getInstance() {
      return INSTANCE;
   }

   private static SoapEncodingTypes createInstance() {
      Thread var0 = Thread.currentThread();
      ClassLoader var1 = var0.getContextClassLoader();

      SoapEncodingTypes var2;
      try {
         var0.setContextClassLoader(ClassLoader.getSystemClassLoader());
         var2 = new SoapEncodingTypes();
      } catch (XmlException var7) {
         throw new RuntimeException(var7);
      } catch (IOException var8) {
         throw new RuntimeException(var8);
      } finally {
         var0.setContextClassLoader(var1);
      }

      return var2;
   }

   SchemaTypeSystem getSchemaTypeSystem() {
      return this.mTypeSystem;
   }

   private SoapEncodingTypes() throws XmlException, IOException {
      InputStream var1 = this.getClass().getResourceAsStream(SOAP_ENCODING_FILE);
      if (var1 == null) {
         throw new AssertionError(SOAP_ENCODING_FILE + " is not in the classpath");
      } else {
         SchemaDocument var2;
         XmlOptions var3;
         try {
            var3 = new XmlOptions();
            var3.setDocumentSourceName("http://schemas.xmlsoap.org/soap/encoding");
            var3.setCompileDownloadUrls();
            var3.setLoadUseDefaultResolver();
            var2 = Factory.parse(var1, var3);
         } finally {
            if (var1 != null) {
               var1.close();
            }

         }

         var3 = new XmlOptions();
         var3.setCompileDownloadUrls();
         var3.setLoadUseDefaultResolver();
         var3.setCompileNoAnnotations();
         this.mTypeSystem = XmlBeans.compileXsd(new SchemaDocument[]{var2}, SoapEncSchemaTypeSystem.get(), var3);
      }
   }
}
