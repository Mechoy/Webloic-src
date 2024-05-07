package weblogic.servlet.jsp.dd;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import weblogic.servlet.HTTPLogger;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.ResourceEntityResolver;
import weblogic.xml.jaxp.WebLogicDocumentBuilderFactory;

public final class JSPEntityResolver extends ResourceEntityResolver implements ErrorHandler {
   public static final String TLD_PUBLIC_ID_11 = "-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.1//EN";
   public static final String TLD_PUBLIC_ID_12 = "-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN";
   public boolean is12;

   public JSPEntityResolver() {
      this.addEntityResource("-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN", "taglib-1_2.dtd", this.getClass());
      this.addEntityResource("-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.1//EN", "taglib.dtd", this.getClass());
   }

   public void warning(SAXParseException var1) throws SAXParseException {
   }

   public void error(SAXParseException var1) throws SAXParseException {
      HTTPLogger.logMalformedWebDescriptor(var1.toString(), var1);
   }

   public void fatalError(SAXParseException var1) throws SAXParseException {
      HTTPLogger.logMalformedWebDescriptor(var1.toString(), var1);
      throw var1;
   }

   public InputSource resolveEntity(String var1, String var2) throws SAXException, IOException {
      if (var1 == null) {
         return null;
      } else {
         if (var1.equals("-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN")) {
            this.is12 = true;
         } else if (!var1.equals("-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.1//EN")) {
            throw new IOException("Invalid DTD for taglib: cannot resolve '" + var1 + "'");
         }

         return super.resolveEntity(var1, var2);
      }
   }

   public static TLDDescriptor load(Reader var0, ErrorHandler var1) throws IOException, ParserConfigurationException, SAXException, DOMProcessingException {
      WebLogicDocumentBuilderFactory var2 = new WebLogicDocumentBuilderFactory();
      var2.setValidating(true);
      DocumentBuilder var3 = var2.newDocumentBuilder();
      JSPEntityResolver var4 = new JSPEntityResolver();
      var3.setEntityResolver(var4);
      if (var1 == null) {
         var1 = var4;
      }

      var3.setErrorHandler((ErrorHandler)var1);
      InputSource var5 = new InputSource(var0);
      Document var6 = var3.parse(var5);
      TLDDescriptor var7 = new TLDDescriptor(var6);
      return var7;
   }

   public static TLDDescriptor load(Reader var0) throws IOException, ParserConfigurationException, SAXException, DOMProcessingException {
      return load(var0, (ErrorHandler)null);
   }

   public static TLDDescriptor load(Reader var0, String var1, String var2) throws IOException, ParserConfigurationException, SAXException, DOMProcessingException {
      JspcLogErrHandler var3 = new JspcLogErrHandler(var1, var2);
      return load(var0, var3);
   }

   public static void main(String[] var0) throws Exception {
      TLDDescriptor var1 = load(new FileReader(var0[0]), new StderrHandler());
      TagDescriptor[] var2 = (TagDescriptor[])((TagDescriptor[])var1.getTags());
      System.err.println("contains " + var2.length + " tags:");

      for(int var3 = 0; var3 < var2.length; ++var3) {
         System.err.println(" " + var2[var3]);
      }

   }
}
