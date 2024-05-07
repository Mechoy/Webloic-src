package weblogic.xml.jaxp;

import java.util.Locale;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class WebLogicSAXParserFactory extends SAXParserFactory {
   static boolean allow_external_dtd = !"false".equalsIgnoreCase(System.getProperty("weblogic.xml.jaxp.allow.externalDTD"));
   static boolean IBM;
   String[] parserFactories = new String[]{"com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl", "weblogic.apache.xerces.jaxp.SAXParserFactoryImpl"};
   private static String[] ibmParserFactories;
   private SAXParserFactory factory = null;
   private boolean disabledEntityResolutionRegistry = false;

   public WebLogicSAXParserFactory() {
      if (IBM) {
         this.factory = (SAXParserFactory)Utils.getDelegate(ibmParserFactories);
      } else {
         this.factory = (SAXParserFactory)Utils.getDelegate(this.parserFactories);
      }

      try {
         this.factory.setFeature("http://xml.org/sax/features/external-general-entities", allow_external_dtd);
         this.factory.setFeature("http://xml.org/sax/features/external-parameter-entities", allow_external_dtd);
      } catch (Exception var2) {
      }

   }

   public SAXParser newSAXParser() throws ParserConfigurationException, SAXException {
      WebLogicSAXParser var1 = null;
      boolean var2 = false;
      Thread var3 = Thread.currentThread();
      ClassLoader var4 = var3.getContextClassLoader();

      try {
         if (var4 != this.getClass().getClassLoader()) {
            var2 = true;
            var3.setContextClassLoader(this.getClass().getClassLoader());
         }

         var1 = new WebLogicSAXParser(this.factory, this.disabledEntityResolutionRegistry);
      } finally {
         if (var2) {
            var3.setContextClassLoader(var4);
         }

      }

      return var1;
   }

   public void setFeature(String var1, boolean var2) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
      this.factory.setFeature(var1, var2);
   }

   public boolean getFeature(String var1) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
      return this.factory.getFeature(var1);
   }

   public boolean isDisabledEntityResolutionRegistry() {
      return this.disabledEntityResolutionRegistry;
   }

   public void setDisabledEntityResolutionRegistry(boolean var1) {
      this.disabledEntityResolutionRegistry = var1;
   }

   public void setNamespaceAware(boolean var1) {
      this.factory.setNamespaceAware(var1);
   }

   public boolean isNamespaceAware() {
      return this.factory.isNamespaceAware();
   }

   public void setValidating(boolean var1) {
      this.factory.setValidating(var1);
   }

   public boolean isValidating() {
      return this.factory.isValidating();
   }

   public boolean isXIncludeAware() throws UnsupportedOperationException {
      return this.factory.isXIncludeAware();
   }

   public void setXIncludeAware(boolean var1) throws UnsupportedOperationException {
      this.factory.setXIncludeAware(var1);
   }

   public Schema getSchema() throws UnsupportedOperationException {
      return this.factory.getSchema();
   }

   public void setSchema(Schema var1) throws UnsupportedOperationException {
      this.factory.setSchema(var1);
   }

   static {
      IBM = System.getProperty("java.vendor", "unknown").toLowerCase(Locale.ENGLISH).indexOf("ibm") >= 0;
      ibmParserFactories = new String[]{"org.apache.xerces.jaxp.SAXParserFactoryImpl", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl", "weblogic.apache.xerces.jaxp.SAXParserFactoryImpl"};
   }
}
