package weblogic.auddi.soap;

import java.net.URL;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.response.Result;
import weblogic.auddi.util.Logger;
import weblogic.auddi.xml.DomBuilder;
import weblogic.auddi.xml.ParserFactory;
import weblogic.auddi.xml.SchemaException;
import weblogic.auddi.xml.XMLUtil;

abstract class SOAPTransport {
   protected static final String FAULT_CODE_CLIENT = ":Client";
   protected static String s_envelopeURI;
   protected static String s_bodyURI;
   protected static String s_envelopeQualifiedName;
   protected static String s_bodyQualifiedName;
   protected static String s_envelopePrefix;
   private static final String SAMPLE_XML = "<TempXml/>";
   private static String s_soapPrefix = null;
   private static String s_soapSuffix = null;

   public abstract String createFaultMessage(String var1, String var2, Result var3);

   public abstract String sendSOAPRequest(Element var1, URL var2, String var3, int var4) throws SOAPWrapperException;

   private String makeSOAPStringByParsing(String var1) {
      Logger.trace("+SOAPTransport.makeSOAPStringByParsing");

      try {
         Element var2 = (new ParserFactory()).createDOMParser().parseRequest(var1).getDocumentElement();
         Element var3 = this.makeSOAPElement(var2);
         var3.setAttribute("xmlns:" + s_envelopePrefix, s_envelopeURI);
         String var4 = XMLUtil.nodeToString(var3);
         Logger.trace("-SOAPTransport.makeSOAPStringByParsing");
         return var4;
      } catch (SchemaException var5) {
         var5.printStackTrace();
         Logger.trace("-SOAPTransport.makeSOAPStringByParsing");
         return null;
      }
   }

   public String makeSOAPString(String var1) {
      Logger.trace("+SOAPTransport.makeSOAPString");
      String var2;
      if (s_soapPrefix == null || s_soapSuffix == null) {
         var2 = this.makeSOAPStringByParsing("<TempXml/>");
         int var3 = var2.indexOf("<TempXml/>");
         s_soapPrefix = var2.substring(0, var3);
         s_soapSuffix = var2.substring(var3 + "<TempXml/>".length());
      }

      var2 = s_soapPrefix + var1 + s_soapSuffix;
      Logger.trace("-SOAPTransport.makeSOAPString");
      return var2;
   }

   protected Element makeSOAPElement(Element var1) {
      Logger.trace("+SOAPTransport.makeSOAPElement");
      Document var2 = var1.getOwnerDocument();
      Element var3 = var2.createElementNS(s_envelopeURI, s_envelopeQualifiedName);
      Element var4 = var2.createElementNS(s_bodyURI, s_bodyQualifiedName);
      var4.appendChild(var1);
      var3.appendChild(var4);
      Logger.trace("-SOAPTransport.makeSOAPElement");
      return var3;
   }

   protected Element getSOAPContent(Element var1) throws SOAPWrapperException {
      Logger.trace("+SOAPTransport.getSOAPContent");
      NodeList var2 = var1.getElementsByTagNameNS("*", "Body");
      if (var2.getLength() == 0) {
         XMLUtil.printXML(var1);
         throw new SOAPWrapperException(UDDIMessages.get("error.soap.base"));
      } else {
         var2 = var2.item(0).getChildNodes();
         Node var3 = null;
         Element var4 = null;
         int var5 = 0;

         for(int var6 = var2.getLength(); var5 < var6; ++var5) {
            var3 = var2.item(var5);
            if (var3.getNodeType() == 1) {
               var4 = (Element)var3;
               break;
            }
         }

         Logger.trace("-SOAPTransport.getSOAPContent");
         return var4;
      }
   }

   protected String fixSOAP(String var1) {
      Logger.trace("+SOAPTransport.fixSOAP(String soap)");

      Document var2;
      try {
         DomBuilder var3 = (new ParserFactory()).createDOMParserNS();
         var2 = var3.parseRequest(var1);
      } catch (SchemaException var11) {
         var11.printStackTrace();
         Logger.trace("-SOAPTransport.fixSOAP(String soap)");
         return var1;
      }

      this.fixPrefix(var2);
      NodeList var12 = var2.getElementsByTagNameNS("*", "Fault");
      if (var12.getLength() == 0) {
         return var1;
      } else {
         int var4 = 0;

         for(int var5 = var12.getLength(); var4 < var5; ++var4) {
            Node var6 = var12.item(var4);
            NodeList var7 = var6.getChildNodes();
            int var8 = 0;

            for(int var9 = var7.getLength(); var8 < var9; ++var8) {
               Node var10 = var7.item(var8);
               if (var10.getNodeType() == 1 && var10.getPrefix() != null && var10.getPrefix().length() != 0) {
                  var10.setPrefix("");
               }
            }
         }

         String var13 = XMLUtil.nodeToString(var2.getDocumentElement());
         Logger.trace("-SOAPTransport.fixSOAP(String soap)");
         return var13;
      }
   }

   private void fixPrefix(Document var1) {
      NodeList var2 = var1.getElementsByTagNameNS("*", "Envelope");
      int var3 = 0;

      int var4;
      for(var4 = var2.getLength(); var3 < var4; ++var3) {
         Element var5 = (Element)var2.item(var3);
         String var6 = var5.getPrefix();
         if (var6 != null && var6.length() != 0) {
            return;
         }

         var5.setPrefix("SOAP-ENV");
         var5.removeAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns");
         var5.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:SOAP-ENV", "http://schemas.xmlsoap.org/soap/envelope/");
      }

      var2 = var1.getElementsByTagNameNS("*", "Body");
      var3 = 0;

      for(var4 = var2.getLength(); var3 < var4; ++var3) {
         var2.item(var3).setPrefix("SOAP-ENV");
      }

      var2 = var1.getElementsByTagNameNS("*", "Fault");
      var3 = 0;

      for(var4 = var2.getLength(); var3 < var4; ++var3) {
         var2.item(var3).setPrefix("SOAP-ENV");
      }

   }
}
