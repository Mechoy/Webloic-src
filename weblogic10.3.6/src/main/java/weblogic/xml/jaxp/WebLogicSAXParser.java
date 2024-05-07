package weblogic.xml.jaxp;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public final class WebLogicSAXParser extends SAXParser {
   private SAXParser saxParser = null;
   private boolean disabledEntityResolutionRegistry = false;

   protected WebLogicSAXParser(SAXParserFactory var1, boolean var2) {
      this.disabledEntityResolutionRegistry = var2;

      try {
         this.saxParser = var1.newSAXParser();
      } catch (ParserConfigurationException var4) {
         throw new FactoryConfigurationError("WebLogicSAXParser cannot be created." + var4.getMessage());
      } catch (SAXException var5) {
         throw new FactoryConfigurationError("WebLogicSAXParser cannot be created." + var5.getMessage());
      }
   }

   public Parser getParser() throws SAXException {
      WebLogicParser var1 = new WebLogicParser(this.saxParser, this.disabledEntityResolutionRegistry);
      return var1;
   }

   public Object getProperty(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      return this.saxParser.getProperty(var1);
   }

   public XMLReader getXMLReader() throws SAXException {
      WebLogicXMLReader var1 = new WebLogicXMLReader(this.saxParser);
      return var1;
   }

   public boolean isNamespaceAware() {
      return this.saxParser.isNamespaceAware();
   }

   public boolean isValidating() {
      return this.saxParser.isValidating();
   }

   public void setProperty(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      this.saxParser.setProperty(var1, var2);
   }
}
