package weblogic.xml.jaxp;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import weblogic.xml.util.Debug;
import weblogic.xml.util.XMLConstants;

public class RegistrySAXParser extends SAXParser implements XMLConstants {
   private static final boolean debug = Boolean.getBoolean("weblogic.xml.debug");
   static Debug.DebugFacility dbg;
   private SAXParser saxParser;
   private SAXFactoryProperties saxFactoryProperties;
   private boolean isXIncludeAware;
   private Schema schema;
   private RegistryParser parser;
   private RegistryXMLReader reader;
   private SAXFactoryProperties initSaxFactoryProperties;

   protected RegistrySAXParser(SAXParserFactory var1, SAXFactoryProperties var2) {
      this(var1, var2, false, (Schema)null);
   }

   protected RegistrySAXParser(SAXParserFactory var1, SAXFactoryProperties var2, boolean var3, Schema var4) {
      this.isXIncludeAware = false;
      this.schema = null;
      this.saxFactoryProperties = var2;
      this.initSaxFactoryProperties = (SAXFactoryProperties)var2.clone();

      try {
         if (var1 != null) {
            this.saxParser = var1.newSAXParser();
         }
      } catch (ParserConfigurationException var6) {
         throw new FactoryConfigurationError("WebLogicSAXParser cannot be created which satisfies the requested configuration." + var6.getMessage());
      } catch (SAXException var7) {
         throw new FactoryConfigurationError("WebLogicSAXParser cannot be created." + var7.getMessage());
      }

      this.parser = new RegistryParser(this.saxParser, this.saxFactoryProperties);
      this.reader = new RegistryXMLReader(this.saxParser, this.saxFactoryProperties);
   }

   public Parser getParser() throws SAXException {
      return this.parser;
   }

   public Object getProperty(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      Object var2 = this.saxFactoryProperties.getProperty(var1);
      if (var2 == null && this.saxParser != null) {
         return this.saxParser.getProperty(var1);
      } else if (var2 == null) {
         throw new SAXNotRecognizedException("You have specified a custom parser in the console. Properties for such parsers are set at the parse time.");
      } else {
         return var2;
      }
   }

   public void setProperty(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      this.saxFactoryProperties.setProperty(var1, var2);
      if (this.saxParser != null) {
         this.saxParser.setProperty(var1, var2);
      }

   }

   public XMLReader getXMLReader() throws SAXException {
      return this.reader;
   }

   public boolean isNamespaceAware() {
      return this.saxParser == null ? this.saxFactoryProperties.get(SAXFactoryProperties.NAMESPACEAWARE) : this.saxParser.isNamespaceAware();
   }

   public boolean isValidating() {
      return this.saxParser == null ? this.saxFactoryProperties.get(SAXFactoryProperties.VALIDATING) : this.saxParser.isValidating();
   }

   public boolean isXIncludeAware() {
      return this.saxParser == null ? this.saxFactoryProperties.get(SAXFactoryProperties.XINCL) : this.saxParser.isXIncludeAware();
   }

   public Schema getSchema() {
      return this.saxParser == null ? (Schema)this.saxFactoryProperties.getProperty(SAXFactoryProperties.SCHEMA) : this.saxParser.getSchema();
   }

   public void reset() {
      this.saxFactoryProperties.copyFrom(this.initSaxFactoryProperties);
      this.parser.reset();
      this.reader.reset();
   }

   static {
      dbg = XMLContext.dbg;
   }
}
