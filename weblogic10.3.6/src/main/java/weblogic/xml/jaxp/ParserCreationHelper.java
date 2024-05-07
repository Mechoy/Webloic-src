package weblogic.xml.jaxp;

import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Parser;
import weblogic.xml.XMLLogger;
import weblogic.xml.registry.RegistryEntityResolver;
import weblogic.xml.registry.XMLRegistryException;
import weblogic.xml.sax.XMLInputSource;
import weblogic.xml.util.Debug;

class ParserCreationHelper {
   static Debug.DebugFacility dbg;
   private XMLInputSource inputSource;
   private RegistryEntityResolver registry;

   public ParserCreationHelper(RegistryEntityResolver var1) {
      this.registry = var1;
   }

   public SAXParserFactory getCustomSAXParserFactory(XMLInputSource var1) {
      String var2 = var1.getPublicIdInternal();
      String var3 = var1.getDoctypeSystemIdInternal();
      String var4 = var1.getRootTagInternal();
      this.inputSource = var1;
      SAXParserFactory var5 = null;

      try {
         var5 = this.registry.getSAXParserFactory(var2, var3, var4);
      } catch (XMLRegistryException var7) {
         XMLLogger.logXMLRegistryException(var7.getMessage());
      }

      return var5;
   }

   public SAXParserFactory getDefaultSAXParserFactory() {
      Object var1 = null;

      try {
         var1 = this.registry.getSAXParserFactory();
      } catch (XMLRegistryException var3) {
         XMLLogger.logXMLRegistryException(var3.getMessage());
      }

      if (var1 == null) {
         var1 = new WebLogicSAXParserFactory();
         dbg.println(2, "Could not get SAXParserFactory from the registry. Instantiating WebLogicSAXParserFactory");
      }

      return (SAXParserFactory)var1;
   }

   public Parser getCustomParser(XMLInputSource var1) {
      String var2 = var1.getPublicIdInternal();
      String var3 = var1.getDoctypeSystemIdInternal();
      String var4 = var1.getRootTagInternal();
      this.inputSource = var1;
      Parser var5 = null;

      try {
         var5 = this.registry.getParser(var2, var3, var4);
      } catch (XMLRegistryException var7) {
         if (dbg.areDebuggingAt(2)) {
            var7.printStackTrace();
         }

         XMLLogger.logXMLRegistryException(var7.getMessage());
      }

      return var5;
   }

   static {
      dbg = XMLContext.dbg;
   }
}
