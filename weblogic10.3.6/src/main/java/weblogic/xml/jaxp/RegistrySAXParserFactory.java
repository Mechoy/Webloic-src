package weblogic.xml.jaxp;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import weblogic.xml.XMLLogger;
import weblogic.xml.registry.RegistryEntityResolver;
import weblogic.xml.registry.XMLRegistryException;
import weblogic.xml.util.Debug;

public class RegistrySAXParserFactory extends SAXParserFactory {
   static Debug.DebugFacility dbg;
   private SAXParserFactory factory = null;
   private SAXFactoryProperties factoryProperties;

   public RegistrySAXParserFactory() {
      try {
         RegistryEntityResolver var1 = new RegistryEntityResolver();
         boolean var2 = var1.hasDocumentSpecificParserEntries();
         this.factoryProperties = new SAXFactoryProperties();
         if (!var2) {
            try {
               this.factory = var1.getSAXParserFactory();
            } catch (XMLRegistryException var4) {
               XMLLogger.logXMLRegistryException(var4.getMessage());
            }

            if (this.factory == null) {
               this.factory = new WebLogicSAXParserFactory();
            }
         }

      } catch (XMLRegistryException var5) {
         XMLLogger.logXMLRegistryException(var5.getMessage());
         throw new FactoryConfigurationError("Failed to find SAXParserFactory. " + var5.getMessage());
      }
   }

   public SAXParser newSAXParser() {
      RegistrySAXParser var1 = new RegistrySAXParser(this.factory, (SAXFactoryProperties)this.factoryProperties.clone());
      return var1;
   }

   public boolean getFeature(String var1) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
      Boolean var2 = this.factoryProperties.getFeature(var1);
      if (var2 == null && this.factory != null) {
         return this.factory.getFeature(var1);
      } else if (var2 == null) {
         throw new SAXNotRecognizedException("You have specified a document-specific parser in the console. Features for such parsers are set at the parse time.");
      } else {
         return var2;
      }
   }

   public void setFeature(String var1, boolean var2) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
      this.factoryProperties.setFeature(var1, var2);
      if (this.factory != null) {
         this.factory.setFeature(var1, var2);
      }

   }

   public boolean isNamespaceAware() {
      return this.factory == null ? this.factoryProperties.get(SAXFactoryProperties.NAMESPACEAWARE) : this.factory.isNamespaceAware();
   }

   public boolean isValidating() {
      return this.factory == null ? this.factoryProperties.get(SAXFactoryProperties.VALIDATING) : this.factory.isValidating();
   }

   public void setNamespaceAware(boolean var1) {
      this.factoryProperties.put(SAXFactoryProperties.NAMESPACEAWARE, var1);
      if (this.factory != null) {
         this.factory.setNamespaceAware(var1);
      }

   }

   public void setValidating(boolean var1) {
      this.factoryProperties.put(SAXFactoryProperties.VALIDATING, var1);
      if (this.factory != null) {
         this.factory.setValidating(var1);
      }

   }

   public boolean isXIncludeAware() {
      return this.factory == null ? this.factoryProperties.get(SAXFactoryProperties.XINCL) : this.factory.isXIncludeAware();
   }

   public void setXIncludeAware(boolean var1) {
      this.factoryProperties.put(SAXFactoryProperties.XINCL, var1);
      if (this.factory != null) {
         this.factory.setXIncludeAware(var1);
      }

   }

   public Schema getSchema() {
      return this.factory == null ? (Schema)this.factoryProperties.getProperty(SAXFactoryProperties.SCHEMA) : this.factory.getSchema();
   }

   public void setSchema(Schema var1) {
      this.factoryProperties.setProperty(SAXFactoryProperties.SCHEMA, var1);
      if (this.factory != null) {
         this.factory.setSchema(var1);
      }

   }

   static {
      dbg = XMLContext.dbg;
   }
}
