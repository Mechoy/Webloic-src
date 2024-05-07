package weblogic.wsee.wsdl.internal;

import com.bea.xbean.xb.xsdschema.SchemaDocument;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlConstants;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlSchema;
import weblogic.wsee.wsdl.WsdlTypes;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.builder.WsdlSchemaBuilder;
import weblogic.wsee.wsdl.builder.WsdlSchemaImportBuilder;
import weblogic.wsee.wsdl.builder.WsdlTypesBuilder;

public class WsdlTypesImpl extends WsdlBase implements WsdlTypesBuilder {
   private List<WsdlSchemaBuilder> schemas = new ArrayList();
   private static final boolean verbose = Verbose.isVerbose(WsdlTypes.class);
   HashMap parentNamespaceDefs = new HashMap();
   private String typesLocation;
   private TransportInfo transportInfo;

   WsdlTypesImpl(TransportInfo var1) {
      this.transportInfo = var1;
   }

   public HashMap getNameSpaceDefs() {
      return this.parentNamespaceDefs;
   }

   public SchemaDocument[] getSchemaArray() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.schemas.iterator();

      while(var2.hasNext()) {
         WsdlSchemaBuilder var3 = (WsdlSchemaBuilder)var2.next();
         this.addSchemasToList(var3, var1);
      }

      return schemaListToArray(var1);
   }

   public List getNodeList() {
      SchemaDocument[] var1 = this.getSchemaArray();
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         Node var4 = var1[var3].getDomNode();
         if (var4 != null) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public List getNodeListWithoutImport() {
      SchemaDocument[] var1 = this.getSchemaArrayWithoutImport();
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         Node var4 = var1[var3].getDomNode();
         if (var4 != null) {
            var2.add(var4);
         }
      }

      return var2;
   }

   private static SchemaDocument[] schemaListToArray(List<WsdlSchemaBuilder> var0) {
      SchemaDocument[] var1 = new SchemaDocument[var0.size()];
      int var2 = 0;

      WsdlSchemaBuilder var4;
      for(Iterator var3 = var0.iterator(); var3.hasNext(); var1[var2++] = var4.getSchema()) {
         var4 = (WsdlSchemaBuilder)var3.next();
      }

      return var1;
   }

   public SchemaDocument[] getSchemaArrayWithoutImport() {
      return schemaListToArray(this.schemas);
   }

   public List<WsdlSchemaBuilder> getSchemaListWithoutImport() {
      return this.schemas;
   }

   private void addSchemasToList(WsdlSchemaBuilder var1, List<WsdlSchemaBuilder> var2) {
      var2.add(var1);
      List var3 = var1.getImports();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         WsdlSchemaImportBuilder var5 = (WsdlSchemaImportBuilder)var4.next();
         WsdlSchemaBuilder var6 = var5.getSchema();
         if (!this.isLoadedSchema(var6, var2)) {
            this.addSchemasToList(var6, var2);
         }
      }

   }

   public List<WsdlSchemaBuilder> getImportedWsdlSchemas() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.schemas.iterator();

      while(var2.hasNext()) {
         WsdlSchemaBuilder var3 = (WsdlSchemaBuilder)var2.next();
         List var4 = var3.getImports();
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            WsdlSchemaImportBuilder var6 = (WsdlSchemaImportBuilder)var5.next();
            WsdlSchemaBuilder var7 = var6.getSchema();
            if (!this.isLoadedSchema(var7, var1)) {
               this.addSchemasToList(var7, var1);
            }
         }
      }

      return var1;
   }

   private boolean isLoadedSchema(WsdlSchemaBuilder var1, List<WsdlSchemaBuilder> var2) {
      Iterator var3 = var2.iterator();

      WsdlSchemaBuilder var4;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         var4 = (WsdlSchemaBuilder)var3.next();
      } while(!var4.getLocationUrl().equals(var1.getLocationUrl()));

      return true;
   }

   public void parse(Element var1, String var2) throws WsdlException {
      this.addDocumentation(var1);
      NodeList var3 = var1.getChildNodes();
      this.collectNamespaces(var1, this.parentNamespaceDefs);
      if (verbose) {
         Verbose.log((Object)("Collected namespaces ...." + this.parentNamespaceDefs));
      }

      this.typesLocation = var2;

      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         Node var5 = var3.item(var4);
         if (!WsdlReader.isDocumentation(var5) && var5 instanceof Element) {
            this.setNamespaces((Element)var5, this.parentNamespaceDefs);
            WsdlSchemaImpl var6 = new WsdlSchemaImpl(this.transportInfo);
            var6.parse((Element)var5, var2);
            this.addSchema(var6);
         }
      }

   }

   private void setNamespaces(Element var1, Map var2) {
      Iterator var3 = var2.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();
         if (this.isDefined((String)var4.getKey(), var1)) {
            if (verbose) {
               Verbose.log((Object)("Namespace redefined -- " + var4.getKey()));
            }
         } else if ("".equals(var4.getKey())) {
            var1.setAttribute("xmlns", (String)var4.getValue());
         } else {
            var1.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + var4.getKey(), (String)var4.getValue());
         }
      }

   }

   private boolean isDefined(String var1, Element var2) {
      NamedNodeMap var3 = var2.getAttributes();

      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         Node var5 = var3.item(var4);
         if ("xmlns".equals(var5.getPrefix()) && var5.getLocalName().equals(var1)) {
            if (verbose) {
               Verbose.log((Object)("Namespace found:" + var5.getLocalName()));
            }

            return true;
         }
      }

      return false;
   }

   private void collectNamespaces(Element var1, Map var2) {
      NamedNodeMap var3 = var1.getAttributes();

      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         Attr var5 = (Attr)var3.item(var4);
         if ("xmlns".equals(var5.getPrefix())) {
            String var6 = var5.getLocalName();
            if (!var2.containsKey(var6)) {
               var2.put(var6, var5.getValue());
            }
         }
      }

      Node var7 = var1.getParentNode();
      if (var7 != null && var7.getNodeType() == 1) {
         this.collectNamespaces((Element)var7, var2);
      }

   }

   public void write(Element var1, WsdlWriter var2) throws WsdlException {
      Element var3 = var2.addChild(var1, "types", WsdlConstants.wsdlNS);
      this.writeDocumentation(var3, var2);
      Iterator var4 = this.schemas.iterator();

      while(var4.hasNext()) {
         WsdlSchemaBuilder var5 = (WsdlSchemaBuilder)var4.next();
         var5.write(var3, var2);
      }

   }

   void writeImportToFile(File var1, String var2) throws IOException, WsdlException {
      Iterator var3 = this.schemas.iterator();

      while(var3.hasNext()) {
         WsdlSchemaImpl var4 = (WsdlSchemaImpl)var3.next();
         var4.writeImportToFile(var1, var2);
      }

   }

   public void addSchemas(SchemaDocument[] var1) throws WsdlException {
      SchemaDocument[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         SchemaDocument var5 = var2[var4];
         this.schemas.add(new WsdlSchemaImpl(var5));
      }

   }

   void addSchemaAsFirst(WsdlSchemaBuilder var1) {
      this.schemas.add(0, var1);
   }

   public void addSchema(WsdlSchemaBuilder var1) {
      this.schemas.add(var1);
   }

   void addTypes(WsdlTypesImpl var1) {
      if (var1 != null) {
         if (this.typesLocation != null && this.typesLocation.equals(var1.typesLocation)) {
            return;
         }

         this.schemas.addAll(var1.schemas);
      }

   }

   public WsdlSchema findImport(String var1) {
      Iterator var2 = this.schemas.iterator();

      WsdlSchema var4;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         WsdlSchemaImpl var3 = (WsdlSchemaImpl)var2.next();
         var4 = var3.findImport(var1);
      } while(var4 == null);

      return var4;
   }
}
