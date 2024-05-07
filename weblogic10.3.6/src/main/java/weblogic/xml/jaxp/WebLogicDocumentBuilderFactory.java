package weblogic.xml.jaxp;

import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import weblogic.xml.registry.RegistryEntityResolver;
import weblogic.xml.registry.XMLRegistryException;
import weblogic.xml.util.XMLConstants;

public class WebLogicDocumentBuilderFactory extends DocumentBuilderFactory implements XMLConstants {
   static Boolean allow_external_dtd = new Boolean(!"false".equalsIgnoreCase(System.getProperty("weblogic.xml.jaxp.allow.externalDTD")));
   static boolean IBM;
   private static String[] parserFactories;
   private static String[] ibmParserFactories;
   private boolean disabledEntityResolutionRegistry = false;
   private DocumentBuilderFactory delegate;

   public WebLogicDocumentBuilderFactory() {
      if (IBM) {
         this.delegate = (DocumentBuilderFactory)Utils.getDelegate(ibmParserFactories);
      } else {
         this.delegate = (DocumentBuilderFactory)Utils.getDelegate(parserFactories);
      }

      if (!allow_external_dtd) {
         try {
            this.delegate.setAttribute("http://xml.org/sax/features/external-general-entities", allow_external_dtd);
            this.delegate.setAttribute("http://xml.org/sax/features/external-parameter-entities", allow_external_dtd);
         } catch (Exception var2) {
         }

      }
   }

   public Object getAttribute(String var1) throws IllegalArgumentException {
      return this.delegate.getAttribute(var1);
   }

   public boolean isCoalescing() {
      return this.delegate.isCoalescing();
   }

   public boolean isDisabledEntityResolutionRegistry() {
      return this.disabledEntityResolutionRegistry;
   }

   public boolean isExpandEntityReferences() {
      return this.delegate.isExpandEntityReferences();
   }

   public boolean isIgnoringComments() {
      return this.delegate.isIgnoringComments();
   }

   public boolean isIgnoringElementContentWhitespace() {
      return this.delegate.isIgnoringElementContentWhitespace();
   }

   public boolean isNamespaceAware() {
      return this.delegate.isNamespaceAware();
   }

   public boolean isValidating() {
      return this.delegate.isValidating();
   }

   public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
      DocumentBuilder var1 = null;
      boolean var2 = false;
      Thread var3 = Thread.currentThread();
      ClassLoader var4 = var3.getContextClassLoader();

      try {
         if (var4 != this.getClass().getClassLoader()) {
            var2 = true;
            var3.setContextClassLoader(this.getClass().getClassLoader());
         }

         var1 = this.delegate.newDocumentBuilder();
      } finally {
         if (var2) {
            var3.setContextClassLoader(var4);
         }

      }

      if (!this.disabledEntityResolutionRegistry) {
         try {
            RegistryEntityResolver var5 = new RegistryEntityResolver();
            var1.setEntityResolver(var5);
         } catch (XMLRegistryException var9) {
            throw new FactoryConfigurationError(var9, "Could not access XML Registry");
         }
      }

      return var1;
   }

   public void setAttribute(String var1, Object var2) throws IllegalArgumentException {
      this.delegate.setAttribute(var1, var2);
   }

   public void setCoalescing(boolean var1) {
      this.delegate.setCoalescing(var1);
   }

   public void setDisabledEntityResolutionRegistry(boolean var1) {
      this.disabledEntityResolutionRegistry = var1;
   }

   public void setExpandEntityReferences(boolean var1) {
      this.delegate.setExpandEntityReferences(var1);
   }

   public void setIgnoringComments(boolean var1) {
      this.delegate.setIgnoringComments(var1);
   }

   public void setIgnoringElementContentWhitespace(boolean var1) {
      this.delegate.setIgnoringElementContentWhitespace(var1);
   }

   public void setNamespaceAware(boolean var1) {
      this.delegate.setNamespaceAware(var1);
   }

   public void setValidating(boolean var1) {
      this.delegate.setValidating(var1);
   }

   public boolean getFeature(String var1) throws ParserConfigurationException {
      return this.delegate.getFeature(var1);
   }

   public void setFeature(String var1, boolean var2) throws ParserConfigurationException {
      this.delegate.setFeature(var1, var2);
   }

   public boolean isXIncludeAware() throws UnsupportedOperationException {
      return this.delegate.isXIncludeAware();
   }

   public void setXIncludeAware(boolean var1) throws UnsupportedOperationException {
      this.delegate.setXIncludeAware(var1);
   }

   public Schema getSchema() throws UnsupportedOperationException {
      return this.delegate.getSchema();
   }

   public void setSchema(Schema var1) throws UnsupportedOperationException {
      this.delegate.setSchema(var1);
   }

   static {
      IBM = System.getProperty("java.vendor", "unknown").toLowerCase(Locale.ENGLISH).indexOf("ibm") >= 0;
      parserFactories = new String[]{"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl", "weblogic.apache.xerces.jaxp.DocumentBuilderFactoryImpl"};
      ibmParserFactories = new String[]{"org.apache.xerces.jaxp.DocumentBuilderFactoryImpl", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl", "weblogic.apache.xerces.jaxp.DocumentBuilderFactoryImpl"};
   }
}
