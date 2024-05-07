package weblogic.xml.jaxp;

import java.io.IOException;
import java.util.Locale;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.SAXParser;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import weblogic.xml.registry.RegistryEntityResolver;
import weblogic.xml.registry.XMLRegistryException;
import weblogic.xml.util.Debug;

public class WebLogicParser implements Parser {
   static Debug.DebugFacility dbg;
   private final ChainingEntityResolver chainingEntityResolver = new ChainingEntityResolver();
   private Parser parser;
   private boolean entityResolverDisabled;

   public WebLogicParser(SAXParser var1, boolean var2) {
      try {
         this.parser = var1.getParser();
         this.entityResolverDisabled = var2;
         if (!var2) {
            this.chainingEntityResolver.pushEntityResolver(new RegistryEntityResolver());
            this.parser.setEntityResolver(this.chainingEntityResolver);
         }

      } catch (SAXException var4) {
         throw new FactoryConfigurationError(var4, "Error getting a parser from a factory " + var4.getMessage());
      } catch (XMLRegistryException var5) {
         throw new FactoryConfigurationError(var5, "Could not access XML Registry");
      }
   }

   public void parse(InputSource var1) throws SAXException, IOException {
      this.parser.parse(var1);
   }

   public void parse(String var1) throws SAXException, IOException {
      this.parser.parse(var1);
   }

   public void setDocumentHandler(DocumentHandler var1) {
      this.parser.setDocumentHandler(var1);
   }

   public void setDTDHandler(DTDHandler var1) {
      this.parser.setDTDHandler(var1);
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
      if (this.entityResolverDisabled) {
         this.enableEntityResolver();
      }

   }

   private synchronized void enableEntityResolver() {
      if (this.entityResolverDisabled) {
         this.parser.setEntityResolver(this.chainingEntityResolver);
         this.entityResolverDisabled = false;
      }
   }

   public void setErrorHandler(ErrorHandler var1) {
      this.parser.setErrorHandler(var1);
   }

   public void setLocale(Locale var1) throws SAXException {
      this.parser.setLocale(var1);
   }

   static {
      dbg = XMLContext.dbg;
   }
}
