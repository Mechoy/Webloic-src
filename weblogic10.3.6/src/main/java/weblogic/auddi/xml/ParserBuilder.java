package weblogic.auddi.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.ErrorHandler;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Logger;

class ParserBuilder {
   public static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
   public static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
   public static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
   private DocumentBuilderFactory m_vFactory;
   private DocumentBuilderFactory m_nvFactory;

   public ParserBuilder(String var1) {
      Logger.trace("+ParserBuilder.CTOR(String) - JAXPFactory: " + var1);
      if (var1 != null && var1.length() != 0) {
         try {
            this.m_vFactory = (DocumentBuilderFactory)Class.forName(var1).newInstance();
            this.m_nvFactory = (DocumentBuilderFactory)Class.forName(var1).newInstance();
         } catch (ClassNotFoundException var3) {
            throw new RuntimeException(UDDIMessages.get("error.class.notfound", new String[]{var1}));
         } catch (InstantiationException var4) {
            throw new RuntimeException(UDDIMessages.get("error.class.instantiation", new String[]{var1}));
         } catch (IllegalAccessException var5) {
            throw new RuntimeException(UDDIMessages.get("error.class.access", new String[]{var1}));
         }
      } else {
         this.m_vFactory = DocumentBuilderFactory.newInstance();
         this.m_nvFactory = DocumentBuilderFactory.newInstance();
      }

      this.m_nvFactory.setIgnoringElementContentWhitespace(true);
      this.m_nvFactory.setValidating(false);
      this.m_nvFactory.setNamespaceAware(false);
      this.m_nvFactory.setIgnoringComments(true);
      this.m_vFactory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
      this.m_vFactory.setIgnoringElementContentWhitespace(true);
      this.m_vFactory.setValidating(true);
      this.m_vFactory.setNamespaceAware(true);
      this.m_vFactory.setIgnoringComments(true);
      Logger.trace("-ParserBuilder.CTOR(String)");
   }

   public DocumentBuilder createParser(String var1) throws ParserConfigurationException {
      Logger.trace("+ParserBuilder.createParser(String)");
      if (var1 != null) {
         Logger.trace("-ParserBuilder.createParser(String)");
         return this.createParser(var1, true);
      } else {
         Logger.trace("-ParserBuilder.createParser(String)");
         return this.createParser(var1, false);
      }
   }

   public DocumentBuilder createParser(String var1, boolean var2) throws ParserConfigurationException {
      Logger.trace("+ParserBuilder.createParser(String, boolean)");
      DocumentBuilderFactory var3;
      if (var1 != null) {
         this.m_vFactory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", var1);
         var3 = this.m_vFactory;
      } else {
         var3 = this.m_nvFactory;
         var3.setNamespaceAware(var2);
      }

      DocumentBuilder var4 = var3.newDocumentBuilder();
      var4.setErrorHandler((ErrorHandler)null);
      Logger.trace("-ParserBuilder.createParser(String, boolean)");
      return var4;
   }
}
