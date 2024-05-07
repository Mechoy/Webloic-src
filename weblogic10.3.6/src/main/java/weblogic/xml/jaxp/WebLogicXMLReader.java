package weblogic.xml.jaxp;

import java.io.IOException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.SAXParser;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import weblogic.xml.registry.RegistryEntityResolver;
import weblogic.xml.registry.XMLRegistryException;
import weblogic.xml.util.Debug;

public class WebLogicXMLReader implements XMLReader {
   static Debug.DebugFacility dbg;
   private final ChainingEntityResolver chainingEntityResolver = new ChainingEntityResolver();
   private XMLReader xmlReader;

   public WebLogicXMLReader() {
   }

   public WebLogicXMLReader(SAXParser var1) {
      try {
         this.xmlReader = var1.getXMLReader();
         this.chainingEntityResolver.pushEntityResolver(new RegistryEntityResolver());
         this.xmlReader.setEntityResolver(this.chainingEntityResolver);
      } catch (SAXException var3) {
         throw new FactoryConfigurationError(var3, "Error getting a parser from a factory " + var3.getMessage());
      } catch (XMLRegistryException var4) {
         throw new FactoryConfigurationError(var4, "Could not access XML Registry");
      }
   }

   public ContentHandler getContentHandler() {
      return this.xmlReader.getContentHandler();
   }

   public DTDHandler getDTDHandler() {
      return this.xmlReader.getDTDHandler();
   }

   public EntityResolver getEntityResolver() {
      return this.chainingEntityResolver.peekEntityResolver();
   }

   public ErrorHandler getErrorHandler() {
      return this.xmlReader.getErrorHandler();
   }

   public boolean getFeature(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      return this.xmlReader.getFeature(var1);
   }

   public Object getProperty(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      return this.xmlReader.getProperty(var1);
   }

   public void parse(InputSource var1) throws SAXException, IOException {
      dbg.assertion(this.xmlReader != null);
      this.xmlReader.parse(var1);
   }

   public void parse(String var1) throws SAXException, IOException {
      dbg.assertion(this.xmlReader != null);
      this.xmlReader.parse(var1);
   }

   public void setContentHandler(ContentHandler var1) {
      this.xmlReader.setContentHandler(var1);
   }

   public void setDTDHandler(DTDHandler var1) {
      this.xmlReader.setDTDHandler(var1);
   }

   public void setEntityResolver(EntityResolver var1) {
      if (dbg.areDebuggingAt(2)) {
         int var2 = this.chainingEntityResolver.getResolverCount();
         dbg.assertion(var2 == 1 || var2 == 2);
      }

      if (this.chainingEntityResolver.getResolverCount() == 2) {
         this.chainingEntityResolver.popEntityResolver();
      }

      this.chainingEntityResolver.pushEntityResolver(var1);
   }

   public void setErrorHandler(ErrorHandler var1) {
      this.xmlReader.setErrorHandler(var1);
   }

   public void setFeature(String var1, boolean var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      this.xmlReader.setFeature(var1, var2);
   }

   public void setProperty(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      this.xmlReader.setProperty(var1, var2);
   }

   static {
      dbg = XMLContext.dbg;
   }
}
