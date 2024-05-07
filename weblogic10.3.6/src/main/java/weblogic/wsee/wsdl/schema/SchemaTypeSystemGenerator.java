package weblogic.wsee.wsdl.schema;

import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xbean.xb.xsdschema.SchemaDocument.Factory;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.SchemaTypeSystem;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlError;
import com.bea.xml.XmlException;
import com.bea.xml.XmlObject;
import com.bea.xml.XmlOptions;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SchemaTypeSystemGenerator {
   private static final String[] BASE_SCHEMA_NAMES = new String[]{"/weblogic/wsee/wsdl/schema/soap-encoding-11.xsd"};
   private static final List<SchemaDocument> BASE_SCHEMAS;
   private ArrayList<SchemaDocument> schemaList = new ArrayList();

   private SchemaTypeSystem compile(XmlObject[] var1) throws XmlException {
      return this.compile(var1, XmlBeans.getBuiltinTypeSystem());
   }

   private SchemaTypeSystem compile(XmlObject[] var1, SchemaTypeLoader var2) throws XmlException {
      XmlOptions var3 = new XmlOptions();
      ArrayList var4 = new ArrayList();
      var3.setErrorListener(var4);
      SchemaTypeSystem var5 = XmlBeans.compileXsd(var1, var2, var3);
      Iterator var6 = var4.iterator();

      XmlError var7;
      do {
         if (!var6.hasNext()) {
            return var5;
         }

         var7 = (XmlError)var6.next();
      } while(var7.getSeverity() != 0);

      throw new XmlException(var7);
   }

   private SchemaTypeSystem getWsdlSchemaTypeSystem(ArrayList<SchemaDocument> var1) throws XmlException {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.iterator();

      SchemaDocument var4;
      while(var3.hasNext()) {
         var4 = (SchemaDocument)var3.next();
         var2.add(var4.getSchema().getTargetNamespace());
      }

      var3 = BASE_SCHEMAS.iterator();

      while(var3.hasNext()) {
         var4 = (SchemaDocument)var3.next();
         if (!var2.contains(var4.getSchema().getTargetNamespace())) {
            var1.add(var4);
         }
      }

      SchemaDocument[] var5 = (SchemaDocument[])var1.toArray(new SchemaDocument[var1.size()]);
      return this.compile(var5);
   }

   public void addSchemaDocuments(SchemaDocument[] var1) {
      if (var1 != null) {
         List var2 = Arrays.asList(var1);
         this.schemaList.addAll(var2);
      }

   }

   public void reset() {
      this.schemaList.clear();
   }

   public SchemaTypeSystem generate() throws XmlException {
      return this.getWsdlSchemaTypeSystem(this.schemaList);
   }

   static {
      ArrayList var0 = new ArrayList(BASE_SCHEMA_NAMES.length);
      String[] var1 = BASE_SCHEMA_NAMES;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];

         try {
            InputStream var5 = SchemaTypeSystemGenerator.class.getResourceAsStream(var4);
            SchemaDocument var6 = Factory.parse(var5);
            var0.add(var6);
         } catch (Exception var7) {
         }
      }

      BASE_SCHEMAS = var0;
   }
}
