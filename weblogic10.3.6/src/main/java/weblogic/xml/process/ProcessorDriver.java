package weblogic.xml.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import javax.xml.parsers.SAXParser;
import org.xml.sax.AttributeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import weblogic.utils.AssertionError;
import weblogic.xml.dom.ResourceEntityResolver;
import weblogic.xml.jaxp.WebLogicSAXParserFactory;

public final class ProcessorDriver extends HandlerBase {
   private static final boolean debug = false;
   private static final boolean verboseSAX = false;
   private static final boolean verboseProcessor = false;
   private InProcessor processor;
   private ProcessingContext procTree;
   private ProcessingContext currentNode;
   private Parser parser;
   int level;

   public ProcessorDriver(InProcessor var1, String var2, String var3, boolean var4) {
      this.level = 0;
      this.processor = var1;
      this.setupSAXParser(var2, var3, var4);
   }

   public ProcessorDriver(InProcessor var1, boolean var2) {
      this(var1, (String)null, (String)null, var2);
   }

   public void setCustomErrorHandler(ErrorHandler var1) {
      if (var1 != null && this.parser != null) {
         this.parser.setErrorHandler(var1);
      }

   }

   public void startElement(String var1, AttributeList var2) throws SAXException {
      try {
         ProcessingContext var3 = this.currentNode();
         if (var3 != null && var3.isText()) {
            this.postProc();
            var3 = this.popNode();
         }

         var3 = this.pushNewNode(var1);
         this.setAttributes(var3, var2);
         this.preProc();
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

   }

   public void endElement(String var1) throws SAXException {
      ProcessingContext var2 = this.currentNode();
      if (var2.isText()) {
         try {
            this.postProc();
         } finally {
            var2 = this.popNode();
         }
      }

      try {
         this.postProc();
      } finally {
         var2 = this.popNode();
      }

   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      ProcessingContext var4 = this.currentNode();
      if (!var4.isText()) {
         var4 = this.pushNewTextNode();
         this.preProc();
      }

      var4.appendValue(var1, var2, var3);
   }

   public void process(String var1) throws IOException, XMLProcessingException, XMLParsingException {
      this.process(new File(var1));
   }

   public void process(File var1) throws IOException, XMLProcessingException, XMLParsingException {
      this.process((InputStream)(new FileInputStream(var1)));
   }

   public void process(InputStream var1) throws IOException, XMLProcessingException, XMLParsingException {
      try {
         InputSource var2 = new InputSource(var1);
         this.parser.parse(var2);
      } catch (SAXProcessorException var3) {
         throw new XMLProcessingException(var3);
      } catch (SAXException var4) {
         throw new XMLParsingException(var4);
      }
   }

   public void process(Reader var1) throws IOException, XMLProcessingException, XMLParsingException {
      try {
         InputSource var2 = new InputSource(var1);
         this.parser.parse(var2);
      } catch (SAXProcessorException var3) {
         throw new XMLProcessingException(var3);
      } catch (SAXException var4) {
         throw new XMLParsingException(var4);
      }
   }

   public void process(InputSource var1) throws IOException, XMLProcessingException, XMLParsingException {
      try {
         this.parser.parse(var1);
      } catch (SAXProcessorException var3) {
         throw new XMLProcessingException(var3);
      } catch (SAXException var4) {
         throw new XMLParsingException(var4);
      }
   }

   private void preProc() throws SAXException {
      ProcessingContext var1 = this.currentNode();

      try {
         this.processor.preProc(var1);
      } catch (SAXException var3) {
         this.popNode();
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      }
   }

   private void postProc() throws SAXException {
      ProcessingContext var1 = this.currentNode();

      try {
         this.processor.postProc(var1);
      } catch (SAXException var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      }
   }

   private ProcessingContext popNode() throws SAXException {
      ProcessingContext var1 = this.currentNode();
      ProcessingContext var2 = (ProcessingContext)var1.getParent();

      try {
         if (!var1.referenced()) {
            var1.release();
         }
      } catch (XMLProcessingException var4) {
         throw new AssertionError(var4);
      }

      if (var2 != null) {
         this.setCurrentNode(var2);
      }

      return var2;
   }

   private ProcessingContext pushNewTextNode() {
      ProcessingContext var1 = this.currentNode();
      ProcessingContext var2 = var1.newTextNode();
      this.setCurrentNode(var2);
      return var2;
   }

   private ProcessingContext pushNewNode(String var1) {
      ProcessingContext var2 = this.currentNode();
      ProcessingContext var3 = null;

      try {
         if (var2 != null) {
            var3 = var2.newElementNode(var1);
         } else {
            var3 = new ProcessingContext(var1);
         }
      } catch (XMLProcessingException var5) {
         throw new AssertionError(var5);
      }

      this.setCurrentNode(var3);
      return var3;
   }

   public ProcessingContext currentNode() {
      return this.currentNode;
   }

   private void setCurrentNode(ProcessingContext var1) {
      this.currentNode = var1;
   }

   private void setAttributes(ProcessingContext var1, AttributeList var2) {
      for(int var3 = 0; var3 < var2.getLength(); ++var3) {
         var1.setAttribute(var2.getName(var3), var2.getValue(var3));
      }

   }

   private void setupSAXParser(String var1, String var2, boolean var3) {
      try {
         WebLogicSAXParserFactory var4 = new WebLogicSAXParserFactory();
         var4.setFeature("http://apache.org/xml/features/allow-java-encodings", true);
         var4.setValidating(var3);
         SAXParser var5 = var4.newSAXParser();
         this.parser = var5.getParser();
      } catch (Exception var6) {
         throw new AssertionError(var6);
      }

      this.parser.setErrorHandler(new HandlerBase() {
         public void error(SAXParseException var1) throws SAXException {
            throw var1;
         }
      });
      if (var2 != null) {
         ResourceEntityResolver var7 = new ResourceEntityResolver();
         var7.setUnresolvedIsFatal(true);
         var7.addEntityResource(var1, var2);
         this.parser.setEntityResolver(var7);
      }

      this.parser.setDocumentHandler(this);
   }
}
