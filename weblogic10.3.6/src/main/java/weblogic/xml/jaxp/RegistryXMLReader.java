package weblogic.xml.jaxp;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.ParserAdapter;
import weblogic.logging.NonCatalogLogger;
import weblogic.xml.XMLLogger;
import weblogic.xml.registry.ReParsingEntityNotChangedException;
import weblogic.xml.registry.RegistryEntityResolver;
import weblogic.xml.registry.XMLRegistryException;
import weblogic.xml.sax.XMLInputSource;
import weblogic.xml.util.Debug;
import weblogic.xml.util.InputSourceUtil;
import weblogicx.xml.parser.BaseParser;

public class RegistryXMLReader implements XMLReader {
   static Debug.DebugFacility dbg;
   static NonCatalogLogger logger;
   private RegistryEntityResolver registry;
   private final ChainingEntityResolver chainingEntityResolver = new ChainingEntityResolver();
   private ReParsingContentHandler contentHandlerProxy = new ReParsingContentHandler();
   private ReParsingDTDHandler dtdHandlerProxy = new ReParsingDTDHandler();
   private ReParsingErrorHandler errorHandlerProxy = new ReParsingErrorHandler();
   private XMLReader xmlReader;
   private SAXFactoryProperties saxFactoryProperties;
   private XMLInputSource xmlInputSource = null;
   private SAXParser saxParser = null;
   private ReParsingLexicalHandler lexicalHandlerProxy = new ReParsingLexicalHandler();
   private ReParsingDeclHandler declHandlerProxy = new ReParsingDeclHandler();
   private Map<String, SAXParser> cache = new HashMap();

   public RegistryXMLReader() {
      try {
         this.registry = new RegistryEntityResolver();
         this.chainingEntityResolver.pushEntityResolver(this.registry);
         this.saxFactoryProperties = new SAXFactoryProperties();
         this.saxFactoryProperties.put(SAXFactoryProperties.NAMESPACEAWARE, true);
         this.xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
         this.xmlReader.setEntityResolver(this.chainingEntityResolver);
      } catch (XMLRegistryException var2) {
         throw new FactoryConfigurationError(var2, "Could not access XML Registry. " + var2.getMessage());
      } catch (ParserConfigurationException var3) {
         throw new FactoryConfigurationError(var3, "Could not create parser. " + var3.getMessage());
      } catch (SAXException var4) {
         throw new FactoryConfigurationError(var4, "Could not create parser. " + var4.getMessage());
      }
   }

   protected RegistryXMLReader(SAXParser var1, SAXFactoryProperties var2) {
      try {
         this.registry = new RegistryEntityResolver();
         this.chainingEntityResolver.pushEntityResolver(this.registry);
         this.saxFactoryProperties = var2;
         if (var1 != null) {
            this.xmlReader = var1.getXMLReader();
            this.xmlReader.setEntityResolver(this.chainingEntityResolver);
         }

      } catch (XMLRegistryException var4) {
         throw new FactoryConfigurationError(var4, "Could not access XML Registry. " + var4.getMessage());
      } catch (SAXException var5) {
         throw new FactoryConfigurationError(var5, "Could not create parser. " + var5.getMessage());
      }
   }

   public RegistryEntityResolver getRegistry() {
      return this.registry;
   }

   public void parse(InputSource var1) throws SAXException, IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("InputSource is missing.");
      } else {
         try {
            try {
               this.xmlReader = this.getXMLReaderInternal(var1);
            } catch (ParserConfigurationException var52) {
               throw new SAXException(var52);
            }

            boolean var2 = this.isHandleEntityInvalidation(var1);
            if (!var2) {
               if (this.errorHandlerProxy.getErrorHandler() != null) {
                  this.xmlReader.setErrorHandler(this.errorHandlerProxy.getErrorHandler());
               }

               if (this.contentHandlerProxy.getContentHandler() != null) {
                  this.xmlReader.setContentHandler(this.contentHandlerProxy.getContentHandler());
               }

               if (this.dtdHandlerProxy.getDTDHandler() != null) {
                  this.xmlReader.setDTDHandler(this.dtdHandlerProxy.getDTDHandler());
               }

               this.xmlReader.parse(var1);
            } else {
               boolean var3 = false;
               ReParsingEventQueue var4 = new ReParsingEventQueue();

               try {
                  ReParsingStatus var5 = new ReParsingStatus();
                  this.registry.hookStatus(var5);
                  this.errorHandlerProxy.hookStatus(var5);
                  this.errorHandlerProxy.registerQueue(var4);
                  this.contentHandlerProxy.registerQueue(var4);
                  this.dtdHandlerProxy.registerQueue(var4);
                  this.delegateExtendedHandlers(var4);
                  SAXParseException var6 = null;
                  Object var7 = null;

                  try {
                     InputSourceUtil.transformInputSource(var1);
                  } catch (Exception var51) {
                     var5.inLastReParsing = true;
                  }

                  int var8 = 0;

                  while(true) {
                     if (var8 >= 2) {
                        var5.inLastReParsing = true;
                     }

                     try {
                        this.xmlReader.parse(var1);
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
                        this.saxParser.reset();
                        this.xmlReader = this.saxParser.getXMLReader();
                        this.setupXMLReader();
                        this.delegateExtendedHandlers(var4);
                        InputSourceUtil.resetInputSource(var1);
                     } catch (Exception var53) {
                        break;
                     }

                     ++var8;
                  }

                  if (var6 != null) {
                     throw var6;
                  }
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
                     this.contentHandlerProxy.registerQueue((ReParsingEventQueue)null);
                     this.dtdHandlerProxy.registerQueue((ReParsingEventQueue)null);
                     this.cleanUpExtendedHandlers();
                  }
               }
            }
         } finally {
            if (this.xmlInputSource != null) {
               this.xmlInputSource = null;
            }

         }

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

   public void parse(String var1) throws SAXException, IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("System ID is missing.");
      } else {
         this.parse(new InputSource(var1));
      }
   }

   public void setErrorHandler(ErrorHandler var1) {
      this.errorHandlerProxy.setErrorHandler(var1);
      if (this.xmlReader != null) {
         this.xmlReader.setErrorHandler(var1);
      }

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

   public void setDTDHandler(DTDHandler var1) {
      this.dtdHandlerProxy.setDTDHandler(var1);
      if (this.xmlReader != null) {
         this.xmlReader.setDTDHandler(var1);
      }

   }

   public void setContentHandler(ContentHandler var1) {
      this.contentHandlerProxy.setContentHandler(var1);
      if (this.xmlReader != null) {
         this.xmlReader.setContentHandler(var1);
      }

   }

   public ContentHandler getContentHandler() {
      return this.contentHandlerProxy.getContentHandler();
   }

   public DTDHandler getDTDHandler() {
      return this.dtdHandlerProxy.getDTDHandler();
   }

   public EntityResolver getEntityResolver() {
      return this.chainingEntityResolver.peekEntityResolver();
   }

   public ErrorHandler getErrorHandler() {
      return this.errorHandlerProxy.getErrorHandler();
   }

   public boolean getFeature(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      Boolean var2 = this.saxFactoryProperties.getFeature(var1);
      if (var2 == null && this.xmlReader != null) {
         return this.xmlReader.getFeature(var1);
      } else if (var2 == null) {
         throw new SAXNotRecognizedException("You have specified a document-specific parser in the console. Features for such parsers are set at the parse time.");
      } else {
         return var2;
      }
   }

   public Object getProperty(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      Object var2 = this.saxFactoryProperties.getProperty(var1);
      if (var2 == null && this.xmlReader != null) {
         return this.xmlReader.getProperty(var1);
      } else if (var2 == null) {
         throw new SAXNotRecognizedException("You have specified a custom parser in the console. Properties for such parsers are set at the parse time.");
      } else {
         return var2;
      }
   }

   public void setFeature(String var1, boolean var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      this.saxFactoryProperties.setFeature(var1, var2);
      if (this.xmlReader != null) {
         this.xmlReader.setFeature(var1, var2);
      }

   }

   public void setProperty(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      this.saxFactoryProperties.setProperty(var1, var2);
      if (this.xmlReader != null) {
         this.xmlReader.setProperty(var1, var2);
      }

   }

   private XMLReader getXMLReaderInternal(InputSource var1) throws ParserConfigurationException, SAXException, IOException {
      if (this.xmlInputSource != null) {
         this.xmlInputSource = null;
      }

      if (this.registry.hasDocumentSpecificParserEntries()) {
         this.xmlInputSource = new XMLInputSource(var1);
         this.xmlReader = this.getXMLReader(this.xmlInputSource);
      } else {
         this.xmlReader = this.getXMLReader((XMLInputSource)null);
      }

      this.setupXMLReader();
      return this.xmlReader;
   }

   private XMLReader getXMLReader(XMLInputSource var1) throws ParserConfigurationException, SAXException {
      Object var2 = null;
      SAXParserFactory var3 = null;
      ParserCreationHelper var4 = new ParserCreationHelper(this.registry);
      if (var1 != null) {
         Parser var5 = var4.getCustomParser(var1);
         if (var5 != null) {
            if (var5 instanceof BaseParser && (this.saxFactoryProperties.get(SAXFactoryProperties.VALIDATING) || this.saxFactoryProperties.get(SAXFactoryProperties.NAMESPACEAWARE))) {
               throw new ParserConfigurationException("Custom-generated parser " + var5.getClass().getName() + " cannot be used. This parser is neither validating nor namespaceaware.");
            }

            var2 = new ParserAdapter(var5);
         }
      }

      if (var2 == null) {
         if (var1 != null) {
            var3 = var4.getCustomSAXParserFactory(var1);
         }

         if (var3 == null) {
            var3 = var4.getDefaultSAXParserFactory();
         }

         String var7 = var3.getClass().getName();
         if (this.cache.containsKey(var7)) {
            this.saxParser = (SAXParser)this.cache.get(var7);
            var2 = this.saxParser.getXMLReader();
         } else {
            var3.setValidating(this.saxFactoryProperties.get(SAXFactoryProperties.VALIDATING));
            if (this.saxFactoryProperties.isSetExplicitly(SAXFactoryProperties.NAMESPACEAWARE)) {
               var3.setNamespaceAware(this.saxFactoryProperties.get(SAXFactoryProperties.NAMESPACEAWARE));
            }

            Schema var6 = (Schema)this.saxFactoryProperties.getProperty(SAXFactoryProperties.SCHEMA);
            if (var6 != null) {
               var3.setSchema(var6);
            }

            if (this.saxFactoryProperties.get(SAXFactoryProperties.XINCL)) {
               var3.setXIncludeAware(true);
            }

            this.saxParser = var3.newSAXParser();
            this.cache.put(var7, this.saxParser);
            var2 = this.saxParser.getXMLReader();
         }
      }

      return (XMLReader)var2;
   }

   private void setupXMLReader() throws SAXException {
      this.xmlReader.setContentHandler(this.contentHandlerProxy);
      this.xmlReader.setDTDHandler(this.dtdHandlerProxy);
      this.xmlReader.setErrorHandler(this.errorHandlerProxy);
      this.xmlReader.setEntityResolver(this.chainingEntityResolver);
      Enumeration var1 = this.saxFactoryProperties.features();

      String var2;
      while(var1.hasMoreElements()) {
         var2 = (String)var1.nextElement();
         Boolean var3 = this.saxFactoryProperties.getFeature(var2);

         try {
            this.xmlReader.setFeature(var2, var3);
         } catch (SAXNotRecognizedException var6) {
            XMLLogger.logPropertyNotAccepted(var2, var3.toString(), var6.getMessage());
         } catch (SAXNotSupportedException var7) {
            XMLLogger.logPropertyNotAccepted(var2, var3.toString(), var7.getMessage());
         }
      }

      var1 = this.saxFactoryProperties.properties();

      while(var1.hasMoreElements()) {
         var2 = (String)var1.nextElement();
         Object var4 = this.saxFactoryProperties.getProperty(var2);

         try {
            this.xmlReader.setProperty(var2, var4);
         } catch (SAXNotRecognizedException var8) {
            XMLLogger.logPropertyNotAccepted(var2, var4 == null ? "null" : var4.toString(), var8.getMessage());
         } catch (SAXNotSupportedException var9) {
            XMLLogger.logPropertyNotAccepted(var2, var4 == null ? "null" : var4.toString(), var9.getMessage());
         }
      }

   }

   private void delegateExtendedHandlers(ReParsingEventQueue var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      String var2 = "http://xml.org/sax/properties/lexical-handler";
      Object var3 = null;

      try {
         var3 = this.xmlReader.getProperty(var2);
      } catch (SAXNotRecognizedException var7) {
      } catch (SAXNotSupportedException var8) {
      }

      if (var3 != null && var3 instanceof LexicalHandler && !(var3 instanceof ReParsingLexicalHandler)) {
         this.lexicalHandlerProxy.setLexicalHandler((LexicalHandler)var3);
         this.lexicalHandlerProxy.registerQueue(var1);
         this.xmlReader.setProperty(var2, this.lexicalHandlerProxy);
      }

      var2 = "http://xml.org/sax/properties/declaration-handler";
      var3 = null;

      try {
         var3 = this.xmlReader.getProperty(var2);
      } catch (SAXNotRecognizedException var5) {
      } catch (SAXNotSupportedException var6) {
      }

      if (var3 != null && var3 instanceof DeclHandler && !(var3 instanceof ReParsingDeclHandler)) {
         this.declHandlerProxy.setDeclHandler((DeclHandler)var3);
         this.declHandlerProxy.registerQueue(var1);
         this.xmlReader.setProperty(var2, this.declHandlerProxy);
      }

   }

   private void cleanUpExtendedHandlers() {
      this.lexicalHandlerProxy.setLexicalHandler((LexicalHandler)null);
      this.lexicalHandlerProxy.registerQueue((ReParsingEventQueue)null);
      this.declHandlerProxy.setDeclHandler((DeclHandler)null);
      this.declHandlerProxy.registerQueue((ReParsingEventQueue)null);
   }

   void reset() {
      this.contentHandlerProxy.setContentHandler((ContentHandler)null);
      this.dtdHandlerProxy.setDTDHandler((DTDHandler)null);
      this.errorHandlerProxy.setErrorHandler((ErrorHandler)null);
      this.resetCachedSAXParsers();
   }

   private void resetCachedSAXParsers() {
      Iterator var1 = this.cache.values().iterator();

      while(var1.hasNext()) {
         SAXParser var2 = (SAXParser)var1.next();
         var2.reset();
      }

   }

   static {
      dbg = XMLContext.dbg;
      logger = new NonCatalogLogger(RegistryXMLReader.class.getName());
   }
}
