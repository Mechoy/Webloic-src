package weblogic.management.security.internal.compatibility;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weblogic.apache.xml.serialize.DOMSerializer;
import weblogic.apache.xml.serialize.OutputFormat;
import weblogic.apache.xml.serialize.SerializerFactory;
import weblogic.management.configuration.ConfigurationException;

public class ConfigXMLFile {
   Document doc = null;
   int level = 0;

   public ConfigXMLFile(InputStream var1) throws IOException, SAXException, ParserConfigurationException {
      this.load(var1);
   }

   protected Document getDocument() {
      return this.doc;
   }

   public int getLevel() {
      return this.level;
   }

   private void load(InputStream var1) throws IOException, SAXException, ParserConfigurationException {
      BufferedReader var2 = new BufferedReader(new InputStreamReader(var1, "UTF8"));
      DocumentBuilderFactory var3 = DocumentBuilderFactory.newInstance();
      DocumentBuilder var4 = var3.newDocumentBuilder();
      this.doc = var4.parse(new InputSource(var2));
   }

   public InputStream getInputStream() throws IOException, SAXException, ParserConfigurationException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      OutputStreamWriter var2 = new OutputStreamWriter(var1, "UTF8");
      OutputFormat var3 = new OutputFormat(this.doc);
      var3.setIndenting(true);
      DOMSerializer var4 = SerializerFactory.getSerializerFactory("xml").makeSerializer(var2, var3).asDOMSerializer();
      var4.serialize(this.doc);
      var2.flush();
      ByteArrayInputStream var5 = new ByteArrayInputStream(var1.toByteArray());
      var2.close();
      return var5;
   }

   protected synchronized void parse(ConfigFileHandler var1) throws ConfigurationException {
      Element var2 = this.doc.getDocumentElement();
      var1.setDocument(this.doc);
      var1.startDocument();
      this.processElements(var1, var2);
      var1.endDocument();
   }

   private void processElements(ConfigFileHandler var1, Node var2) {
      String var3 = var2.getNodeName();
      XMLAttributeList var4 = new XMLAttributeList(var2);
      var1.startElement(var3, var4, var2);
      NodeList var5 = var2.getChildNodes();

      for(int var6 = 0; var6 < var5.getLength(); ++var6) {
         Node var7 = var5.item(var6);
         if (var7.getNodeType() == 1) {
            ++this.level;
            this.processElements(var1, var7);
            --this.level;
         }
      }

      var1.endElement(var2);
   }

   class DumpHandler extends ConfigFileHandler {
      protected void startElement(String var1, XMLAttributeList var2, Node var3) {
         String var4 = "";

         int var5;
         for(var5 = 0; var5 < ConfigXMLFile.this.getLevel(); ++var5) {
            var4 = var4 + "  ";
         }

         System.out.println(var4 + "Element: " + var1);

         for(var5 = 0; var5 < var2.getLength(); ++var5) {
            String var6 = var2.getName(var5);
            String var7 = var2.getValue(var5);
            System.out.println(var4 + "   " + var6 + "=" + var7);
         }

      }
   }
}
