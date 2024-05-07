package weblogic.auddi.uddi.xml;

import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Logger;
import weblogic.auddi.xml.DomBuilder;
import weblogic.auddi.xml.SOAPParser;
import weblogic.auddi.xml.SchemaException;
import weblogic.auddi.xml.XMLParser;

public class UDDIParser extends XMLParser {
   public static final String XML_ENC = "UTF-8";
   public static final String IGNORE_ENCODING = "auddi.encoding.ignore";
   private DomBuilder m_v_parser;
   private SOAPParser m_SOAPParser;
   private boolean m_ignoreEncoding;

   public UDDIParser(SOAPParser var1, DomBuilder var2) throws SchemaException {
      Logger.trace("+UDDIParser.CTOR()");
      this.m_v_parser = var2;
      this.m_SOAPParser = var1;
      this.m_ignoreEncoding = Boolean.getBoolean(System.getProperty("auddi.encoding.ignore"));
      Logger.trace("-UDDIParser.CTOR()");
   }

   public Document parseRequest(String var1) throws SchemaException {
      Document var5;
      try {
         Logger.trace("+UDDIParser.parseRequest()");
         Document var2 = this.m_SOAPParser.parseRequest(var1);
         String var3 = this.m_SOAPParser.getPayLoad(var2);
         String var4;
         if (var3 == null) {
            var4 = null;
            return var4;
         }

         var2 = this.m_v_parser.parse(var3);
         var4 = this.m_SOAPParser.getEncoding();
         if (!this.m_ignoreEncoding && !"UTF-8".equals(var4) && var4 != null && var4.trim().length() != 0) {
            throw new SchemaException(UDDIMessages.get("error.fatalError.xmlEncoding", var4, "UTF-8"));
         }

         var5 = var2;
      } catch (SAXException var10) {
         Logger.trace("-EXCEPTION(SAXException) UDDIParser.parseRequest()");
         throw new SchemaException(var10);
      } catch (IOException var11) {
         Logger.trace("-EXCEPTION(IOException) UDDIParser.parseRequest()");
         throw new SchemaException(var11);
      } finally {
         Logger.trace("-UDDIParser.parseRequest()");
      }

      return var5;
   }

   public Node parseRequestIntoNode(String var1) throws SchemaException {
      Document var2 = this.m_SOAPParser.parseRequest(var1);
      Node var3 = this.m_SOAPParser.getPayLoadNode(var2);
      return var3;
   }

   public boolean hasFault() {
      return this.m_SOAPParser.hasFault();
   }

   public Node getFaultNode() {
      return this.m_SOAPParser.getFaultNode();
   }
}
