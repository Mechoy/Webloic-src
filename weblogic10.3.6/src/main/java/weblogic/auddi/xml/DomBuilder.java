package weblogic.auddi.xml;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import weblogic.auddi.util.Logger;

public class DomBuilder extends XMLParser implements ErrorHandler {
   private static final int READ_AHEAD_LIMIT = 100;
   private SAXParseException m_SAXParseException;
   private DocumentBuilder m_parser;
   private String m_encoding;

   public DomBuilder(DocumentBuilder var1) {
      Logger.trace("+DomBuilder.CTOR()");
      Logger.debug("DomBuilder.Class: " + var1.getClass().getName());
      this.m_parser = var1;
      this.m_parser.setErrorHandler(this);
      Logger.trace("-DomBuilder.CTOR()");
   }

   public Document parseRequest(String var1) throws SchemaException {
      try {
         return this.parse(var1);
      } catch (SAXException var3) {
         throw new SchemaException(var3);
      } catch (IOException var4) {
         throw new SchemaException(var4);
      }
   }

   public void setEntityResolver(EntityResolver var1) {
      this.m_parser.setEntityResolver(var1);
   }

   public Document parse(String var1) throws SAXException, IOException {
      InputSource var2 = new InputSource(new StringReader(var1));
      return this.parse(var2);
   }

   public Document parse(InputSource var1) throws SAXException, IOException {
      Logger.trace("+DomBuilder.parse()");
      this.m_SAXParseException = null;
      this.m_encoding = this.readEncoding(var1);
      Document var2 = this.m_parser.parse(var1);
      if (this.m_SAXParseException != null) {
         throw this.m_SAXParseException;
      } else {
         XMLUtil.removeWhiteSpace(var2);
         Logger.trace("-DomBuilder.parse()");
         return var2;
      }
   }

   String getEncoding() {
      return this.m_encoding;
   }

   public void warning(SAXParseException var1) {
      Logger.trace("+DomBuilder.warning");
      Logger.trace("-DomBuilder.warning");
   }

   public void error(SAXParseException var1) {
      Logger.trace("+DomBuilder.error()");
      Logger.debug((Throwable)var1);
      this.m_SAXParseException = var1;
      Logger.trace("-DomBuilder.error()");
   }

   public void fatalError(SAXParseException var1) {
      Logger.trace("+DomBuilder.fatalError()");
      Logger.debug((Throwable)var1);
      this.m_SAXParseException = var1;
      Logger.trace("-DomBuilder.fatalError()");
   }

   private String readEncoding(InputSource var1) {
      String var2 = null;
      Reader var3 = null;

      Object var5;
      try {
         var3 = var1.getCharacterStream();
         var3.mark(100);
         char[] var4 = new char[100];
         var3.read(var4);
         var2 = new String(var4);
         var2 = var2.substring(0, var2.indexOf(62) + 1);
         int var18 = var2.indexOf("encoding");
         if (var18 == -1) {
            var2 = null;
         } else {
            var2 = var2.substring(var18 + 8);
            int var6 = var2.indexOf(34);
            int var7 = var2.indexOf(34, var6 + 1);
            if (var6 == -1) {
               var6 = var2.indexOf("'");
               var7 = var2.indexOf("'", var6 + 1);
            }

            var2 = var2.substring(var6 + 1, var7);
         }

         String var19 = var2;
         return var19;
      } catch (IOException var16) {
         var5 = null;
      } finally {
         try {
            var3.reset();
         } catch (IOException var15) {
            return null;
         }
      }

      return (String)var5;
   }
}
