package weblogic.wsee.wsdl.internal;

import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xbean.xb.xsdschema.SchemaDocument.Factory;
import com.bea.xml.XmlException;
import com.bea.xml.XmlOptions;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.apache.xml.serialize.OutputFormat;
import weblogic.apache.xml.serialize.XMLSerializer;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.deploy.WsdlAddressInfo;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlSchema;
import weblogic.wsee.wsdl.WsdlSchemaImport;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;
import weblogic.wsee.wsdl.builder.WsdlSchemaBuilder;
import weblogic.wsee.wsdl.builder.WsdlSchemaImportBuilder;
import weblogic.xml.jaxp.WebLogicDocumentBuilderFactory;

public class WsdlSchemaImpl extends WsdlBase implements WsdlSchemaBuilder {
   private SchemaDocument schema;
   private String locationUrl;
   private WsdlDefinitionsBuilder definitions;
   private List<WsdlSchemaImportBuilder> importList = new ArrayList();
   private TransportInfo transportInfo;

   WsdlSchemaImpl(TransportInfo var1) {
      this.transportInfo = var1;
   }

   WsdlSchemaImpl(WsdlDefinitionsBuilder var1, TransportInfo var2) {
      this.definitions = var1;
      this.transportInfo = var2;
   }

   WsdlSchemaImpl(SchemaDocument var1) {
      this.schema = var1;
   }

   public SchemaDocument getSchema() {
      return this.schema;
   }

   public WsdlDefinitionsBuilder getWsdlDefinitions() {
      return this.definitions;
   }

   public String getLocationUrl() {
      return this.locationUrl;
   }

   public List<WsdlSchemaImportBuilder> getImports() {
      return this.importList;
   }

   public void parse(Element var1, String var2) throws WsdlException {
      this.parse((Element)var1, (String)var2, (Set)(new HashSet()));
   }

   public void parse(Element var1, String var2, Set<String> var3) throws WsdlException {
      try {
         XmlOptions var4 = new XmlOptions();
         this.locationUrl = var2;
         String var5 = var2;
         if (var2 != null && var2.startsWith("file:") && var2.charAt(5) != '/') {
            var5 = "file:/" + var2.substring(5);
         }

         var4.setDocumentSourceName(var5);
         this.schema = Factory.parse(var1, var4);
      } catch (XmlException var8) {
         throw new WsdlException("Failed to parse schema", var8);
      }

      List var9 = this.findImportAndIncludeNodes(var1);
      Iterator var10 = var9.iterator();

      while(var10.hasNext()) {
         Element var6 = (Element)var10.next();
         WsdlSchemaImportImpl var7 = new WsdlSchemaImportImpl(this, this.transportInfo);
         var7.parse(var6, var3);
         if (var7.getSchema() != null) {
            this.importList.add(var7);
         }
      }

   }

   public static WsdlSchema parse(String var0) throws WsdlException {
      return parse((String)var0, (Set)(new HashSet()));
   }

   public static WsdlSchema parse(String var0, Set<String> var1) throws WsdlException {
      return parse((String)var0, (Set)var1, (WsdlDefinitionsBuilder)null);
   }

   public static WsdlSchemaBuilder parse(String var0, Set<String> var1, WsdlDefinitionsBuilder var2) throws WsdlException {
      return parse((TransportInfo)null, var0, var1, var2);
   }

   public static WsdlSchemaBuilder parse(TransportInfo var0, String var1, Set<String> var2, WsdlDefinitionsBuilder var3) throws WsdlException {
      WsdlSchemaImpl var4 = new WsdlSchemaImpl(var3, var0);
      Document var5 = WsdlReader.getDocument(var0, var1);
      var4.parse(var5.getDocumentElement(), var1, var2);
      return var4;
   }

   public void write(Element var1, WsdlWriter var2) throws WsdlException {
      Element var3 = this.getSchemaElement(var2);
      Node var4 = var1.getOwnerDocument().importNode(var3, true);
      var1.appendChild(var4);
   }

   public void write(Document var1, WsdlWriter var2) throws WsdlException {
      Element var3 = this.getSchemaElement(var2);
      Node var4 = var1.importNode(var3, true);
      var1.appendChild(var4);
   }

   public void writeToFile(File var1, String var2) throws IOException, WsdlException {
      if (var1.getParentFile() != null) {
         var1.getParentFile().mkdirs();
      }

      FileOutputStream var3 = null;

      try {
         var3 = new FileOutputStream(var1);
         Document var4 = this.toDocument((WsdlAddressInfo)null);
         this.writeDocument(var3, var4, var2);
         this.writeImportToFile(var1, var2);
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var10) {
            }
         }

      }

   }

   static String validFilename(String var0) {
      return var0.replace('?', '_');
   }

   void writeImportToFile(File var1, String var2) throws IOException, WsdlException {
      File var3 = var1.getCanonicalFile();
      Iterator var4 = this.importList.iterator();

      while(var4.hasNext()) {
         WsdlSchemaImportBuilder var5 = (WsdlSchemaImportBuilder)var4.next();
         var5.writeToFile(var3, var2);
      }

   }

   public void write(OutputStream var1, WsdlAddressInfo var2, String var3) throws IOException, WsdlException {
      Document var4 = this.toDocument(var2);
      this.writeDocument(var1, var4, var3);
   }

   private void writeDocument(OutputStream var1, Document var2, String var3) throws IOException {
      OutputFormat var4 = new OutputFormat("XML", var3, true);
      XMLSerializer var5 = new XMLSerializer(var1, var4);
      var5.serialize(var2);
   }

   /** @deprecated */
   public void write(OutputStream var1, WsdlAddressInfo var2) throws IOException, WsdlException {
      this.write(var1, var2, (String)null);
   }

   WsdlSchema findImport(String var1) {
      Iterator var2 = this.importList.iterator();

      WsdlSchema var4;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         WsdlSchemaImport var3 = (WsdlSchemaImport)var2.next();
         if (var3.isRelative() && var1.equals(var3.getSchemaLocation())) {
            return var3.getSchema();
         }

         var4 = ((WsdlSchemaImpl)var3.getSchema()).findImport(var1);
      } while(var4 == null);

      return var4;
   }

   private Document toDocument(WsdlAddressInfo var1) throws WsdlException {
      DocumentBuilderFactory var2 = WebLogicDocumentBuilderFactory.newInstance();
      var2.setNamespaceAware(true);

      Document var3;
      try {
         var3 = var2.newDocumentBuilder().newDocument();
      } catch (ParserConfigurationException var5) {
         throw new WsdlException("Failed to create XML document", var5);
      }

      WsdlWriter var4 = new WsdlWriter();
      var4.setWsdlAddressInfo(var1);
      this.write(var3, var4);
      return var3;
   }

   private Element getSchemaElement(WsdlWriter var1) {
      Element var2 = ((Document)this.schema.newDomNode()).getDocumentElement();
      if (var1 != null) {
         this.modifyRelativeImports(var2, var1);
      }

      return var2;
   }

   private void modifyRelativeImports(Element var1, WsdlWriter var2) {
      List var3 = this.findImportAndIncludeNodes(var1);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Element var5 = (Element)var4.next();
         String var6 = var5.getAttribute("schemaLocation");
         if (var6 != null && !"".equals(var6) && this.isRelative(var6)) {
            var6 = validFilename(var6);
            String var7 = var2.getImportPrefix();
            if (var7 != null) {
               var6 = var7 + "/" + var6;
            }

            var5.setAttribute("schemaLocation", var6);
         }
      }

   }

   private List<Node> findImportAndIncludeNodes(Element var1) {
      ArrayList var2 = new ArrayList();
      NodeList var3 = var1.getChildNodes();

      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         if (var3.item(var4) instanceof Element) {
            Element var5 = (Element)var3.item(var4);
            if ("http://www.w3.org/2001/XMLSchema".equals(var5.getNamespaceURI()) && ("import".equals(var5.getLocalName()) || "include".equals(var5.getLocalName()) || "redefine".equals(var5.getLocalName()))) {
               var2.add(var5);
            }
         }
      }

      return var2;
   }

   private boolean isRelative(String var1) {
      try {
         new URL(var1);
         return false;
      } catch (MalformedURLException var3) {
         return true;
      }
   }
}
