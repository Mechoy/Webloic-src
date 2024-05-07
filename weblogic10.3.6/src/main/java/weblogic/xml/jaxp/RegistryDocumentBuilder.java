package weblogic.xml.jaxp;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import weblogic.logging.NonCatalogLogger;
import weblogic.xml.XMLLogger;
import weblogic.xml.registry.ReParsingEntityNotChangedException;
import weblogic.xml.registry.RegistryEntityResolver;
import weblogic.xml.registry.XMLRegistryException;
import weblogic.xml.sax.XMLInputSource;
import weblogic.xml.util.InputSourceUtil;

public class RegistryDocumentBuilder extends DocumentBuilder {
   static NonCatalogLogger logger = new NonCatalogLogger(RegistryDocumentBuilder.class.getName());
   private DocumentBuilder builder;
   private DOMFactoryProperties factoryProperties;
   private final ChainingEntityResolver chainingEntityResolver = new ChainingEntityResolver();
   private ReParsingErrorHandler errorHandlerProxy = new ReParsingErrorHandler();
   private RegistryEntityResolver registry;
   private InputStream jaxpSchemaSource = null;
   private XMLInputSource xmlInputSource = null;
   private Map<String, DocumentBuilder> cache = new HashMap();

   protected RegistryDocumentBuilder(DOMFactoryProperties var1) {
      try {
         this.factoryProperties = var1;
         this.registry = new RegistryEntityResolver();
         this.chainingEntityResolver.pushEntityResolver(this.registry);
      } catch (XMLRegistryException var3) {
         throw new FactoryConfigurationError(var3, "Could not access XML Registry. " + var3.getMessage());
      }
   }

   public DOMImplementation getDOMImplementation() {
      if (this.builder == null) {
         try {
            this.builder = this.getDocumentBuilder((InputSource)null);
         } catch (ParserConfigurationException var2) {
            XMLLogger.logParserConfigurationException(var2.getMessage());
            return null;
         } catch (IOException var3) {
            XMLLogger.logParserConfigurationException(var3.getMessage());
            return null;
         }
      }

      return this.builder.getDOMImplementation();
   }

   public boolean isNamespaceAware() {
      return this.factoryProperties.get(DOMFactoryProperties.NAMESPACEAWARE);
   }

   public boolean isValidating() {
      return this.factoryProperties.get(DOMFactoryProperties.VALIDATING);
   }

   public Document newDocument() {
      if (this.builder == null) {
         try {
            this.builder = this.getDocumentBuilder((InputSource)null);
         } catch (ParserConfigurationException var2) {
            XMLLogger.logParserConfigurationException(var2.getMessage());
            return null;
         } catch (IOException var3) {
            XMLLogger.logParserConfigurationException(var3.getMessage());
            return null;
         }
      }

      return this.builder.newDocument();
   }

   public Document parse(InputSource var1) throws SAXException, IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("InputSource is missing for DocumentBuilder.parse(InputSource)");
      } else {
         Document var8;
         try {
            try {
               this.builder = this.getDocumentBuilder(var1);
            } catch (ParserConfigurationException var52) {
               throw new SAXException(var52);
            }

            boolean var2 = this.isHandleEntityInvalidation(var1);
            if (!var2) {
               if (this.errorHandlerProxy.getErrorHandler() != null) {
                  this.builder.setErrorHandler(this.errorHandlerProxy.getErrorHandler());
               }

               Document var58 = this.builder.parse(var1);
               return var58;
            }

            boolean var3 = false;
            ReParsingEventQueue var4 = new ReParsingEventQueue();

            try {
               ReParsingStatus var5 = new ReParsingStatus();
               this.registry.hookStatus(var5);
               this.errorHandlerProxy.hookStatus(var5);
               this.errorHandlerProxy.registerQueue(var4);
               SAXParseException var6 = null;
               Document var7 = null;

               try {
                  InputSourceUtil.transformInputSource(var1);
               } catch (Exception var51) {
                  var5.inLastReParsing = true;
               }

               var8 = false;

               while(true) {
                  if (var8 >= 2) {
                     var5.inLastReParsing = true;
                  }

                  try {
                     var7 = this.builder.parse(var1);
                     var6 = null;
                  } catch (SAXParseException var54) {
                     var6 = var54;
                     var5.error = var54;
                  } catch (ReParsingEntityNotChangedException var55) {
                     var3 = true;
                     break;
                  }

                  if (!var5.needReParse()) {
                     break;
                  }

                  var5.prepareReParsing();
                  var4.shiftLastEvents();

                  try {
                     if (this.allowCached(this.builder)) {
                        this.builder.reset();
                        this.setupDocumentBuilder(this.builder);
                     } else {
                        this.builder = this.getDocumentBuilder(var1);
                     }

                     InputSourceUtil.resetInputSource(var1);
                  } catch (Exception var53) {
                     break;
                  }

                  ++var8;
               }

               if (var6 != null) {
                  throw var6;
               }

               var8 = var7;
            } finally {
               this.registry.hookStatus((ReParsingStatus)null);
               this.errorHandlerProxy.hookStatus((ReParsingStatus)null);
               if (!var3) {
                  var4.shiftLastEvents();
               }

               try {
                  var4.reSendLastEvents();
               } finally {
                  var4.clearAllEvents();
                  this.errorHandlerProxy.registerQueue((ReParsingEventQueue)null);
               }
            }
         } finally {
            if (this.jaxpSchemaSource != null) {
               this.jaxpSchemaSource.reset();
            }

            if (this.xmlInputSource != null) {
               this.xmlInputSource = null;
            }

         }

         return var8;
      }
   }

   private boolean isHandleEntityInvalidation(InputSource var1) {
      String var2 = null;

      try {
         if (this.registry.hasDocumentSpecificEntityEntries() && this.registry.hasHandleEntityInvalidationSetSupport()) {
            if (this.xmlInputSource == null) {
               this.xmlInputSource = new XMLInputSource(var1);
            }

            String var3 = this.xmlInputSource.getPublicIdInternal();
            String var4 = this.xmlInputSource.getDoctypeSystemIdInternal();
            var2 = this.registry.getHandleEntityInvalidation(var3, var4);
         }

         if (var2 == null) {
            var2 = this.registry.getHandleEntityInvalidation();
         }
      } catch (Exception var5) {
      }

      return "true".equals(var2);
   }

   public void setEntityResolver(EntityResolver var1) {
      if (this.chainingEntityResolver.getResolverCount() == 2) {
         this.chainingEntityResolver.popEntityResolver();
      }

      this.chainingEntityResolver.pushEntityResolver(var1);
   }

   public void setErrorHandler(ErrorHandler var1) {
      this.errorHandlerProxy.setErrorHandler(var1);
      if (this.builder != null) {
         this.builder.setErrorHandler(this.errorHandlerProxy);
      }

   }

   private DocumentBuilder getDocumentBuilder(InputSource var1) throws ParserConfigurationException, IOException {
      DocumentBuilder var2 = null;
      DocumentBuilderFactory var3 = null;
      if (this.xmlInputSource != null) {
         this.xmlInputSource = null;
      }

      if (var1 != null && this.registry.hasDocumentSpecificParserEntries()) {
         this.xmlInputSource = new XMLInputSource(var1);
         var3 = this.getCustomDocumentBuilderFactory(this.xmlInputSource);
      }

      if (var3 == null) {
         var3 = this.getDefaultDocumentBuilderFactory();
      }

      String var4 = var3.getClass().getName();
      if (this.cache.containsKey(var4)) {
         var2 = (DocumentBuilder)this.cache.get(var4);
      } else {
         var2 = var3.newDocumentBuilder();
         if (this.allowCached(var2)) {
            this.cache.put(var4, var2);
         }
      }

      this.setupDocumentBuilder(var2);
      return var2;
   }

   private boolean allowCached(DocumentBuilder var1) {
      return var1.getClass().getName().indexOf("org.apache.xerces") == -1 || !var1.isValidating();
   }

   private DocumentBuilderFactory getCustomDocumentBuilderFactory(XMLInputSource var1) throws IllegalArgumentException {
      DocumentBuilderFactory var2 = null;
      String var3 = var1.getPublicIdInternal();
      String var4 = var1.getDoctypeSystemIdInternal();
      String var5 = var1.getRootTagInternal();

      try {
         var2 = this.registry.getDocumentBuilderFactory(var3, var4, var5);
         if (var2 != null) {
            this.setupDocumentBuilderFactory(var2);
         }
      } catch (XMLRegistryException var7) {
         if (this.registry.hasDocumentSpecificParserEntries()) {
            XMLLogger.logXMLRegistryException(var7.getMessage());
         }
      }

      return var2;
   }

   private DocumentBuilderFactory getDefaultDocumentBuilderFactory() throws IllegalArgumentException {
      Object var1 = null;

      try {
         var1 = this.registry.getDocumentBuilderFactory();
      } catch (XMLRegistryException var3) {
         XMLLogger.logXMLRegistryException(var3.getMessage());
      }

      if (var1 == null) {
         var1 = new WebLogicDocumentBuilderFactory();
      }

      this.setupDocumentBuilderFactory((DocumentBuilderFactory)var1);
      return (DocumentBuilderFactory)var1;
   }

   private void setupDocumentBuilder(DocumentBuilder var1) {
      var1.setErrorHandler(this.errorHandlerProxy);
      var1.setEntityResolver(this.chainingEntityResolver);
   }

   private void setupDocumentBuilderFactory(DocumentBuilderFactory var1) {
      var1.setCoalescing(this.factoryProperties.get(DOMFactoryProperties.COALESCING));
      var1.setExpandEntityReferences(this.factoryProperties.get(DOMFactoryProperties.EXPANDENTITYREFERENCES));
      var1.setIgnoringComments(this.factoryProperties.get(DOMFactoryProperties.IGNORINGCOMMENTS));
      var1.setIgnoringElementContentWhitespace(this.factoryProperties.get(DOMFactoryProperties.IGNORINGELEMENTCONTENTWHITESPACE));
      if (this.factoryProperties.isSetExplicitly(DOMFactoryProperties.NAMESPACEAWARE)) {
         var1.setNamespaceAware(this.factoryProperties.get(DOMFactoryProperties.NAMESPACEAWARE));
      }

      var1.setValidating(this.factoryProperties.get(DOMFactoryProperties.VALIDATING));
      if (this.factoryProperties.get(DOMFactoryProperties.XINCL)) {
         var1.setXIncludeAware(true);
      }

      Schema var2 = (Schema)this.factoryProperties.getAttribute(DOMFactoryProperties.SCHEMA);
      if (var2 != null) {
         var1.setSchema(var2);
      }

      Iterator var5 = this.factoryProperties.attributes();

      while(var5.hasNext()) {
         String var3 = (String)var5.next();
         Object var4 = this.factoryProperties.getAttribute(var3);
         if (!DOMFactoryProperties.SCHEMA.equals(var3)) {
            try {
               var1.setAttribute(var3, var4);
            } catch (IllegalArgumentException var7) {
               XMLLogger.logPropertyNotAccepted(var3, var4.toString(), var7.getMessage());
            }
         }
      }

   }

   public Schema getSchema() {
      return (Schema)this.factoryProperties.getAttribute(DOMFactoryProperties.SCHEMA);
   }

   public boolean isXIncludeAware() {
      return this.factoryProperties.get(DOMFactoryProperties.XINCL);
   }

   void setJaxpSchemaSource(InputStream var1) {
      this.jaxpSchemaSource = var1;
   }

   public void reset() {
      this.errorHandlerProxy.setErrorHandler((ErrorHandler)null);
      Iterator var1 = this.cache.values().iterator();

      while(var1.hasNext()) {
         DocumentBuilder var2 = (DocumentBuilder)var1.next();
         var2.reset();
      }

   }
}
