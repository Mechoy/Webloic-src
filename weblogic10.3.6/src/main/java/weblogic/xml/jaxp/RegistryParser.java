package weblogic.xml.jaxp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import weblogic.xml.registry.ReParsingEntityNotChangedException;
import weblogic.xml.registry.RegistryEntityResolver;
import weblogic.xml.registry.XMLRegistryException;
import weblogic.xml.sax.XMLInputSource;
import weblogic.xml.util.Debug;
import weblogic.xml.util.InputSourceUtil;
import weblogicx.xml.parser.BaseParser;

public class RegistryParser implements Parser {
   static Debug.DebugFacility dbg;
   private RegistryEntityResolver registry;
   private ChainingEntityResolver chainingEntityResolver;
   private ReParsingDocumentHandler documentHandlerProxy = new ReParsingDocumentHandler();
   private ReParsingDTDHandler dtdHandlerProxy = new ReParsingDTDHandler();
   private ReParsingErrorHandler errorHandlerProxy = new ReParsingErrorHandler();
   private Locale locale;
   private Parser parser;
   private SAXFactoryProperties saxFactoryProperties;
   private XMLInputSource xmlInputSource = null;
   private SAXParser saxParser = null;
   private Map<String, SAXParser> cache = new HashMap();

   public RegistryParser() {
      try {
         this.registry = new RegistryEntityResolver();
         this.chainingEntityResolver = new ChainingEntityResolver();
         this.chainingEntityResolver.pushEntityResolver(this.registry);
         this.saxFactoryProperties = new SAXFactoryProperties();
      } catch (XMLRegistryException var3) {
         AssertionError var2 = new AssertionError("Could not create RegistryParser.");
         var2.initCause(var3);
         throw var2;
      }
   }

   protected RegistryParser(SAXParser var1, SAXFactoryProperties var2) {
      try {
         this.registry = new RegistryEntityResolver();
         this.chainingEntityResolver = new ChainingEntityResolver();
         this.chainingEntityResolver.pushEntityResolver(this.registry);
         this.saxFactoryProperties = var2;
         if (var1 != null) {
            this.parser = var1.getParser();
            this.parser.setEntityResolver(this.chainingEntityResolver);
         }

      } catch (XMLRegistryException var4) {
         throw new FactoryConfigurationError(var4, "Could not access XML Registry. " + var4.getMessage());
      } catch (SAXException var5) {
         throw new FactoryConfigurationError(var5, "Could not create parser. " + var5.getMessage());
      } catch (NullPointerException var6) {
         throw new FactoryConfigurationError(var6, "Could not create parser. " + var6.getMessage());
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
               this.parser = this.getParserInternal(var1);
            } catch (ParserConfigurationException var52) {
               throw new SAXException(var52);
            }

            boolean var2 = this.isHandleEntityInvalidation(var1);
            if (!var2) {
               if (this.errorHandlerProxy.getErrorHandler() != null) {
                  this.parser.setErrorHandler(this.errorHandlerProxy.getErrorHandler());
               }

               if (this.documentHandlerProxy.getDocumentHandler() != null) {
                  this.parser.setDocumentHandler(this.documentHandlerProxy.getDocumentHandler());
               }

               if (this.dtdHandlerProxy.getDTDHandler() != null) {
                  this.parser.setDTDHandler(this.dtdHandlerProxy.getDTDHandler());
               }

               this.parser.parse(var1);
            } else {
               boolean var3 = false;
               ReParsingEventQueue var4 = new ReParsingEventQueue();

               try {
                  ReParsingStatus var5 = new ReParsingStatus();
                  this.registry.hookStatus(var5);
                  this.errorHandlerProxy.hookStatus(var5);
                  this.errorHandlerProxy.registerQueue(var4);
                  this.documentHandlerProxy.registerQueue(var4);
                  this.dtdHandlerProxy.registerQueue(var4);
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
                        this.parser.parse(var1);
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
                        this.parser = this.saxParser.getParser();
                        this.setupParser();
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
                     this.documentHandlerProxy.registerQueue((ReParsingEventQueue)null);
                     this.dtdHandlerProxy.registerQueue((ReParsingEventQueue)null);
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

   public void setDocumentHandler(DocumentHandler var1) {
      this.documentHandlerProxy.setDocumentHandler(var1);
      if (this.parser != null) {
         this.parser.setDocumentHandler(var1);
      }

   }

   public void setErrorHandler(ErrorHandler var1) {
      this.errorHandlerProxy.setErrorHandler(var1);
      if (this.parser != null) {
         this.parser.setErrorHandler(var1);
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
      if (this.parser != null) {
         this.parser.setDTDHandler(var1);
      }

   }

   public void setLocale(Locale var1) throws SAXException {
      this.locale = var1;
      if (this.parser != null) {
         this.parser.setLocale(var1);
      }

   }

   private Parser getParserInternal(InputSource var1) throws ParserConfigurationException, SAXException, IOException {
      if (this.xmlInputSource != null) {
         this.xmlInputSource = null;
      }

      if (this.registry.hasDocumentSpecificParserEntries()) {
         this.xmlInputSource = new XMLInputSource(var1);
         this.parser = this.getParser(this.xmlInputSource);
      } else {
         this.parser = this.getParser((XMLInputSource)null);
      }

      this.setupParser();
      return this.parser;
   }

   private Parser getParser(XMLInputSource var1) throws ParserConfigurationException, SAXException {
      Parser var2 = null;
      SAXParserFactory var3 = null;
      ParserCreationHelper var4 = new ParserCreationHelper(this.registry);
      if (var1 != null) {
         var2 = var4.getCustomParser(var1);
      }

      if (var2 != null) {
         if (this.parser instanceof BaseParser && (this.saxFactoryProperties.get(SAXFactoryProperties.VALIDATING) || this.saxFactoryProperties.get(SAXFactoryProperties.NAMESPACEAWARE))) {
            throw new ParserConfigurationException("Custom-generated parser " + var2.getClass().getName() + " cannot be used. This parser is neither validating nor namespaceaware.");
         }
      } else {
         if (var1 != null) {
            var3 = var4.getCustomSAXParserFactory(var1);
         }

         if (var3 == null) {
            var3 = var4.getDefaultSAXParserFactory();
         }

         String var5 = var3.getClass().getName();
         if (this.cache.containsKey(var5)) {
            this.saxParser = (SAXParser)this.cache.get(var5);
            var2 = this.saxParser.getParser();
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
            this.cache.put(var5, this.saxParser);
            var2 = this.saxParser.getParser();
         }
      }

      return var2;
   }

   private void setupParser() throws SAXException {
      this.parser.setDocumentHandler(this.documentHandlerProxy);
      this.parser.setErrorHandler(this.errorHandlerProxy);
      this.parser.setDTDHandler(this.dtdHandlerProxy);
      if (this.locale != null) {
         this.parser.setLocale(this.locale);
      }

      this.parser.setEntityResolver(this.chainingEntityResolver);
   }

   void reset() {
      this.documentHandlerProxy.setDocumentHandler((DocumentHandler)null);
      this.errorHandlerProxy.setErrorHandler((ErrorHandler)null);
      this.dtdHandlerProxy.setDTDHandler((DTDHandler)null);
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
   }
}
