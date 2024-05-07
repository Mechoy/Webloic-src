package weblogic.xml.jaxp;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import weblogic.xml.XMLLogger;
import weblogic.xml.registry.XMLRegistry;
import weblogic.xml.registry.XMLRegistryException;
import weblogic.xml.util.CachedInputStream;
import weblogic.xml.util.Debug;

public class RegistryDocumentBuilderFactory extends DocumentBuilderFactory {
   static Debug.DebugFacility dbg;
   private DOMFactoryProperties factoryProperties = new DOMFactoryProperties();
   private DocumentBuilderFactory factory = null;
   private boolean docSpecificParser = false;
   private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
   private CachedInputStream jaxpSchemaStream = null;

   public Object getAttribute(String var1) throws IllegalArgumentException {
      try {
         XMLRegistry[] var2 = XMLRegistry.getXMLRegistryPath();
         if (this.factory == null) {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               this.factory = var2[var3].getDocumentBuilderFactory();
               this.docSpecificParser = var2[var3].hasDocumentSpecificParserEntries();
               if (this.factory != null) {
                  break;
               }
            }
         }

         if (this.factory == null) {
            this.factory = new WebLogicDocumentBuilderFactory();
            this.docSpecificParser = false;
            this.factory.getAttribute(var1);
         } else if (!this.docSpecificParser) {
            this.factory.getAttribute(var1);
         }
      } catch (XMLRegistryException var4) {
      }

      return this.factoryProperties.getAttribute(var1);
   }

   public boolean isCoalescing() {
      return this.factoryProperties.get(DOMFactoryProperties.COALESCING);
   }

   public boolean isExpandEntityReferences() {
      return this.factoryProperties.get(DOMFactoryProperties.EXPANDENTITYREFERENCES);
   }

   public boolean isIgnoringComments() {
      return this.factoryProperties.get(DOMFactoryProperties.IGNORINGCOMMENTS);
   }

   public boolean isIgnoringElementContentWhitespace() {
      return this.factoryProperties.get(DOMFactoryProperties.IGNORINGELEMENTCONTENTWHITESPACE);
   }

   public boolean isNamespaceAware() {
      return this.factoryProperties.get(DOMFactoryProperties.NAMESPACEAWARE);
   }

   public boolean isValidating() {
      return this.factoryProperties.get(DOMFactoryProperties.VALIDATING);
   }

   public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
      RegistryDocumentBuilder var1 = new RegistryDocumentBuilder((DOMFactoryProperties)this.factoryProperties.clone());
      var1.setJaxpSchemaSource(this.jaxpSchemaStream);
      var1.setJaxpSchemaSource(this.jaxpSchemaStream);
      return var1;
   }

   public void setAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         var2 = this.filterJaxpStream(var1, var2);
         XMLRegistry[] var3 = XMLRegistry.getXMLRegistryPath();
         if (this.factory == null) {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               this.factory = var3[var4].getDocumentBuilderFactory();
               this.docSpecificParser = var3[var4].hasDocumentSpecificParserEntries();
               if (this.factory != null) {
                  break;
               }
            }
         }

         if (this.factory == null) {
            this.factory = new WebLogicDocumentBuilderFactory();
            this.docSpecificParser = false;
            this.factory.setAttribute(var1, var2);
         } else if (!this.docSpecificParser) {
            this.factory.setAttribute(var1, var2);
         }
      } catch (XMLRegistryException var5) {
      }

      this.factoryProperties.setAttribute(var1, var2);
   }

   public void setCoalescing(boolean var1) {
      this.factoryProperties.put(DOMFactoryProperties.COALESCING, var1);
   }

   public void setExpandEntityReferences(boolean var1) {
      this.factoryProperties.put(DOMFactoryProperties.EXPANDENTITYREFERENCES, var1);
   }

   public void setIgnoringComments(boolean var1) {
      this.factoryProperties.put(DOMFactoryProperties.IGNORINGCOMMENTS, var1);
   }

   public void setIgnoringElementContentWhitespace(boolean var1) {
      this.factoryProperties.put(DOMFactoryProperties.IGNORINGELEMENTCONTENTWHITESPACE, var1);
   }

   public void setNamespaceAware(boolean var1) {
      this.factoryProperties.put(DOMFactoryProperties.NAMESPACEAWARE, var1);
   }

   public void setValidating(boolean var1) {
      this.factoryProperties.put(DOMFactoryProperties.VALIDATING, var1);
   }

   public boolean getFeature(String var1) {
      return this.factoryProperties.get(var1);
   }

   public void setFeature(String var1, boolean var2) {
      this.factoryProperties.put(var1, var2);
   }

   public boolean isXIncludeAware() throws UnsupportedOperationException {
      return this.factoryProperties.get(DOMFactoryProperties.XINCL);
   }

   public void setXIncludeAware(boolean var1) throws UnsupportedOperationException {
      this.factoryProperties.put(DOMFactoryProperties.XINCL, var1);
   }

   public Schema getSchema() throws UnsupportedOperationException {
      return (Schema)this.factoryProperties.getAttribute(DOMFactoryProperties.SCHEMA);
   }

   public void setSchema(Schema var1) throws UnsupportedOperationException {
      this.factoryProperties.setAttribute(DOMFactoryProperties.SCHEMA, var1);
   }

   private Object filterJaxpStream(String var1, Object var2) {
      if (var1 != null && var1.equals("http://java.sun.com/xml/jaxp/properties/schemaSource")) {
         if (var2 instanceof InputStream) {
            try {
               if (this.jaxpSchemaStream != null) {
                  this.closeJaxpSchemaStream();
               }

               this.jaxpSchemaStream = new CachedInputStream((InputStream)var2);
               return this.jaxpSchemaStream;
            } catch (IOException var4) {
               XMLLogger.logIOException(var4.getMessage());
               throw new IllegalArgumentException("Error caching stream for http://java.sun.com/xml/jaxp/properties/schemaSource " + var4.toString());
            }
         }

         if (this.jaxpSchemaStream != null) {
            try {
               this.closeJaxpSchemaStream();
            } catch (IOException var5) {
               XMLLogger.logIOException(var5.getMessage());
               throw new IllegalArgumentException("Error Processing Attribute http://java.sun.com/xml/jaxp/properties/schemaSource " + var5.toString());
            }
         }
      }

      return var2;
   }

   private void closeJaxpSchemaStream() throws IOException {
      weblogic.utils.Debug.assertion(this.jaxpSchemaStream != null, "JaxpSchemaStream cannot be null");

      try {
         this.jaxpSchemaStream.closeAll();
      } catch (IOException var2) {
         XMLLogger.logIOException(var2.getMessage());
         throw var2;
      }
   }

   static {
      dbg = XMLContext.dbg;
   }
}
