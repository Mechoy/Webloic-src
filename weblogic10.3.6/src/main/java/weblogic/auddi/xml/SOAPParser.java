package weblogic.auddi.xml;

import java.io.IOException;
import java.io.StringReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Logger;

public class SOAPParser extends XMLParser {
   private DomBuilder m_parser;
   private boolean m_hasFault;
   private Node m_faultNode;

   public SOAPParser(DomBuilder var1) throws SchemaException {
      Logger.trace("+SOAPParser.CTOR()");
      this.m_parser = var1;
      Logger.trace("-SOAPParser.CTOR()");
   }

   public Document parseRequest(String var1) throws SchemaException {
      try {
         Logger.trace("+SOAPParser.parseRequest()");
         Document var2 = this.m_parser.parse(new InputSource(new StringReader(var1)));
         Logger.trace("-SOAPParser.parseRequest()");
         return var2;
      } catch (SAXException var3) {
         Logger.trace("-EXCEPTION(SchemaException) SOAPParser.parseRequest()");
         throw new SchemaException(var3);
      } catch (IOException var4) {
         Logger.trace("-EXCEPTION(IOException) SOAPParser.parseRequest()");
         throw new SchemaException(var4);
      } catch (IllegalArgumentException var5) {
         Logger.trace("-EXCEPTION(IllegalArgumentException) SOAPParser.parseRequest()");
         throw new SchemaException(var5.getMessage());
      }
   }

   public String getPayLoad(Document var1) {
      Logger.trace("+SOAPParser.getPayLoad()");
      Node var2 = this.getPayLoadNode(var1);
      String var3 = null;
      if (var2 != null) {
         var3 = XMLUtil.nodeToString(var2);
      }

      Logger.trace("-SOAPParser.getPayLoad()");
      return var3;
   }

   public Node getPayLoadNode(Document var1) {
      Logger.trace("+SOAPParser.getPayLoadNode()");
      NodeList var2 = var1.getElementsByTagNameNS("*", "Fault");
      Node var3;
      int var4;
      int var5;
      if (var2.getLength() == 0) {
         var2 = var1.getElementsByTagNameNS("*", "Body");
      } else {
         this.m_hasFault = true;
         var4 = 0;

         for(var5 = var2.getLength(); var4 < var5; ++var4) {
            var3 = var2.item(var4);
            if (var3.getLocalName().equals("Fault")) {
               this.m_faultNode = var3;
               break;
            }
         }

         var2 = var1.getElementsByTagNameNS("*", "detail");
      }

      if (var2.getLength() > 1) {
         throw new RuntimeException(UDDIMessages.get("error.soap.toomanychildren"));
      } else {
         var2 = var2.item(0).getChildNodes();
         var3 = null;
         var4 = 0;

         for(var5 = var2.getLength(); var4 < var5; ++var4) {
            var3 = var2.item(var4);
            if (var3.getNodeType() == 1) {
               break;
            }
         }

         Logger.trace("-SOAPParser.getPayLoadNode()");
         return var3;
      }
   }

   public boolean hasFault() {
      return this.m_hasFault;
   }

   public Node getFaultNode() {
      return this.m_faultNode;
   }

   public String getEncoding() {
      return this.m_parser.getEncoding();
   }
}
