package weblogic.xml.jaxp;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.XMLFilter;
import weblogic.xml.XMLLogger;
import weblogic.xml.registry.RegistryEntityResolver;
import weblogic.xml.registry.XMLRegistryException;
import weblogic.xml.util.XMLConstants;

public class RegistryTransformerFactory extends SAXTransformerFactory implements XMLConstants {
   private static final boolean debug = Boolean.getBoolean("weblogic.xml.debug");
   private SAXTransformerFactory delegate;

   public RegistryTransformerFactory() {
      try {
         RegistryEntityResolver var1 = new RegistryEntityResolver();
         this.delegate = (SAXTransformerFactory)var1.getTransformerFactory();
      } catch (XMLRegistryException var2) {
         XMLLogger.logXMLRegistryException(var2.getMessage());
      }

      if (this.delegate == null) {
         this.delegate = new WebLogicTransformerFactory();
      }

   }

   public Source getAssociatedStylesheet(Source var1, String var2, String var3, String var4) throws TransformerConfigurationException {
      return this.delegate.getAssociatedStylesheet(var1, var2, var3, var4);
   }

   public Object getAttribute(String var1) throws IllegalArgumentException {
      return this.delegate.getAttribute(var1);
   }

   public ErrorListener getErrorListener() {
      return this.delegate.getErrorListener();
   }

   public boolean getFeature(String var1) {
      return this.delegate.getFeature(var1);
   }

   public URIResolver getURIResolver() {
      return this.delegate.getURIResolver();
   }

   public Templates newTemplates(Source var1) throws TransformerConfigurationException {
      return this.delegate.newTemplates(var1);
   }

   public Transformer newTransformer() throws TransformerConfigurationException {
      return this.delegate.newTransformer();
   }

   public Transformer newTransformer(Source var1) throws TransformerConfigurationException {
      return this.delegate.newTransformer(var1);
   }

   public void setAttribute(String var1, Object var2) throws IllegalArgumentException {
      this.delegate.setAttribute(var1, var2);
   }

   public void setErrorListener(ErrorListener var1) throws IllegalArgumentException {
      this.delegate.setErrorListener(var1);
   }

   public void setURIResolver(URIResolver var1) {
      this.delegate.setURIResolver(var1);
   }

   public TemplatesHandler newTemplatesHandler() throws TransformerConfigurationException {
      return this.delegate.newTemplatesHandler();
   }

   public TransformerHandler newTransformerHandler() throws TransformerConfigurationException {
      return this.delegate.newTransformerHandler();
   }

   public TransformerHandler newTransformerHandler(Source var1) throws TransformerConfigurationException {
      return this.delegate.newTransformerHandler(var1);
   }

   public TransformerHandler newTransformerHandler(Templates var1) throws TransformerConfigurationException {
      return this.delegate.newTransformerHandler(var1);
   }

   public XMLFilter newXMLFilter(Source var1) throws TransformerConfigurationException {
      return this.delegate.newXMLFilter(var1);
   }

   public XMLFilter newXMLFilter(Templates var1) throws TransformerConfigurationException {
      return this.delegate.newXMLFilter(var1);
   }

   public void setFeature(String var1, boolean var2) {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }
}
