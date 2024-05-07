package weblogic.auddi.soap;

import java.io.IOException;
import java.net.URL;
import org.w3c.dom.Element;
import weblogic.auddi.uddi.response.Result;
import weblogic.auddi.util.Logger;
import weblogic.auddi.xml.ParserFactory;
import weblogic.auddi.xml.SchemaException;

public class SOAPWrapper {
   public static final String HEADERVAL_CONTENT_TYPE_UTF8 = "text/xml;charset=utf-8";
   private static SOAPTransport s_soapTransport;

   public static String sendSOAPRequest(String var0, URL var1, String var2, int var3) throws IOException, SOAPWrapperException {
      try {
         Logger.trace("+SOAPWrapper.sendSOAPRequest(java.lang.String)");
         Element var4 = (new ParserFactory()).createSOAPParser().parseRequest(var0).getDocumentElement();
         Logger.trace("-SOAPWrapper.sendSOAPRequest(java.lang.String)");
         return sendSOAPRequest(var4, var1, var2, var3);
      } catch (SchemaException var5) {
         throw new SOAPWrapperException(var5, "");
      }
   }

   public static String sendSOAPRequest(Element var0, URL var1, String var2, int var3) throws IOException, SOAPWrapperException {
      Logger.trace("+SOAPWrapper.sendSOAPRequest(org.w3c.dom.Element)");
      String var4 = s_soapTransport.sendSOAPRequest(var0, var1, var2, var3);
      Logger.trace("-SOAPWrapper.sendSOAPRequest(org.w3c.dom.Element)");
      return var4;
   }

   public static String makeSOAPString(String var0) {
      Logger.trace("+SOAPWrapper.makeSOAPString");
      String var1 = s_soapTransport.makeSOAPString(var0);
      Logger.trace("-SOAPWrapper.makeSOAPString");
      return var1;
   }

   private static Element getSOAPContent(Element var0) throws SOAPWrapperException {
      Logger.trace("+SOAPWrapper.getSOAPContent");
      Element var1 = s_soapTransport.getSOAPContent(var0);
      Logger.trace("-SOAPWrapper.getSOAPContent");
      return var1;
   }

   public static String createFaultMessage(String var0, String var1, Result var2) {
      Logger.trace("+SOAPWrapper.createFaultMessage()");
      String var3 = s_soapTransport.createFaultMessage(var0, var1, var2);
      Logger.trace("-SOAPWrapper.createFaultMessage()");
      return var3;
   }

   static {
      Logger.trace("+SOAPWrapper.static initializer");
      s_soapTransport = SOAPTransportFactory.getSOAPTransport();
      Logger.trace("-SOAPWrapper.static initializer");
   }
}
