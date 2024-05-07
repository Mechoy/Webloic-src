package weblogic.auddi.uddi.soap;

import java.io.IOException;
import java.net.URL;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.auddi.soap.SOAPWrapper;
import weblogic.auddi.soap.SOAPWrapperException;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.UnsupportedException;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;
import weblogic.auddi.uddi.request.UDDIRequest;
import weblogic.auddi.uddi.response.UDDIResponse;
import weblogic.auddi.uddi.xml.ParserWrapper;
import weblogic.auddi.uddi.xml.UDDIParser;
import weblogic.auddi.uddi.xml.UDDIParserFactory;
import weblogic.auddi.util.Logger;
import weblogic.auddi.xml.ParserFactory;
import weblogic.auddi.xml.SchemaException;
import weblogic.auddi.xml.XMLUtil;

public class UDDISOAPWrapper {
   private static UDDIParserFactory s_uddiParserFactory = new UDDIParserFactory();

   private UDDISOAPWrapper() {
   }

   public static String sendUDDIRequest(String var0, URL var1, String var2, int var3) throws IOException, FatalErrorException, SOAPWrapperException {
      Logger.Log(4, (String)"+weblogic.auddi.uddi.soap.UDDISOAPWrapper.sendUDDIRequest()");

      String var6;
      try {
         Document var4 = (new ParserFactory()).createSOAPParser().parseRequest(var0);
         String var5 = SOAPWrapper.sendSOAPRequest(var4.getDocumentElement(), var1, var2, var3);
         var6 = var5;
      } catch (SchemaException var10) {
         throw new FatalErrorException("Failed to create Request from xml" + getAdditionalMessage(var10), var10);
      } finally {
         Logger.Log(4, (String)"-weblogic.auddi.uddi.soap.UDDISOAPWrapper.sendUDDIRequest()");
      }

      return var6;
   }

   public static UDDIResponse createResponseFromSOAP(String var0) throws UDDIException {
      UDDIParser var1 = null;
      Logger.trace("+UDDISOAPWrapper.createResponseFromSOAP()");

      UDDIResponse var6;
      try {
         var1 = s_uddiParserFactory.getUDDIParser();
         Node var2 = var1.parseRequestIntoNode(var0);
         if (var2 != null) {
            Logger.Log(3, (String)"***** PROCESSING *****");
            UDDIResponse var12 = createResponse(var2);
            UDDIResponse var13 = var12;
            return var13;
         }

         if (!var1.hasFault()) {
            throw new FatalErrorException("No element found in the document");
         }

         UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
         UDDIXMLHandler var4 = var3.makeHandler("Error");
         UDDIResponse var5 = (UDDIResponse)var4.create(var1.getFaultNode());
         Logger.Log(3, (String)("handler : " + var4.getClass().getName()));
         Logger.Log(3, (String)("response : " + var5.getClass().getName()));
         var6 = var5;
      } catch (SchemaException var10) {
         throw new FatalErrorException("Failed to create Response from xml" + getAdditionalMessage(var10), var10);
      } finally {
         if (var1 != null) {
            s_uddiParserFactory.returnUDDIParser(var1);
         }

         Logger.trace("-UDDISOAPWrapper.createResponseFromSOAP()");
      }

      return var6;
   }

   public static UDDIResponse createResponseFromXML(String var0) throws UDDIException {
      Logger.trace("+UDDISOAPWrapper.createResponseFromXML()");

      UDDIResponse var4;
      try {
         Document var1 = ParserWrapper.parseRequest(var0, false);
         Element var2 = null;
         if (var1 == null || (var2 = var1.getDocumentElement()) == null) {
            throw new FatalErrorException("No element found in the document");
         }

         UDDIResponse var3 = createResponse(var2);
         var4 = var3;
      } catch (SchemaException var8) {
         throw new FatalErrorException("Failed to create Response from xml" + getAdditionalMessage(var8), var8);
      } finally {
         Logger.trace("-UDDISOAPWrapper.createResponseFromXML()");
      }

      return var4;
   }

   private static UDDIResponse createResponse(Node var0) throws UDDIException {
      Logger.trace("+weblogic.auddi.uddi.soap.UDDISOAPWrapper.createResponse()");
      UDDIXMLHandlerMaker var1 = UDDIXMLHandlerMaker.getInstance();
      String var2 = var0.getLocalName();
      UDDIXMLHandler var3 = var1.makeHandler(var2);
      UDDIResponse var4 = (UDDIResponse)var3.create(var0);
      Logger.Log(3, (String)("handler : " + var3.getClass().getName()));
      Logger.Log(3, (String)("response : " + var4.getClass().getName()));
      Logger.trace("-weblogic.auddi.uddi.soap.UDDISOAPWrapper.createResponse()");
      return var4;
   }

   public static UDDIRequest createRequest(String var0) throws UDDIException {
      UDDIParser var1 = null;

      UDDIRequest var5;
      try {
         Logger.trace("+UDDISOAPWrapper.createRequest(String soapRequest)");
         var1 = s_uddiParserFactory.getUDDIParser();
         Document var2 = var1.parseRequest(var0);
         if (var2 == null || var2.getDocumentElement() == null) {
            throw new FatalErrorException("No element found in the document");
         }

         Element var3 = var2.getDocumentElement();
         UDDIRequest var4 = createRequest((Node)var3);
         Logger.debug("SOAP Message was successfully parsed as a UDDI request");
         var5 = var4;
      } catch (SchemaException var9) {
         Logger.error("SOAP Message could not be parsed as a UDDI Request. Original request follows:");
         Logger.error("[" + var0 + "]");
         throw new FatalErrorException("Failed to create Request from xml" + getAdditionalMessage(var9), var9);
      } finally {
         if (var1 != null) {
            s_uddiParserFactory.returnUDDIParser(var1);
         }

         Logger.trace("-UDDISOAPWrapper.createRequest(String soapRequest)");
      }

      return var5;
   }

   private static String getAdditionalMessage(SchemaException var0) {
      Throwable var1 = var0.getCause();
      Throwable var2 = var0.getNestedException();
      String var3 = "";
      if (var1 != null) {
         var3 = var3 + " [ " + var1.getMessage() + " ]";
      } else if (var2 != null) {
         var3 = var3 + " [ " + var2.getMessage() + " ]";
      }

      return var3;
   }

   private static UDDIRequest createRequest(Node var0) throws UDDIException {
      UDDIRequest var7;
      try {
         Logger.trace("+UDDISOAPWrapper.createRequest(Node)");
         if (var0 == null) {
            throw new FatalErrorException(UDDIMessages.get("error.fatalError.node"));
         }

         XMLUtil.removeWhiteSpace(var0);
         Element var1 = (Element)var0;
         Attr var2 = var1.getAttributeNode("generic");
         Logger.trace("generic = " + var2);
         if (var2 != null && !var2.getNodeValue().toString().equals("1.0") && !var2.getNodeValue().toString().equals("2.0")) {
            throw new UnsupportedException(UDDIMessages.get("error.unsupported.generic"));
         }

         String var3 = var0.getNodeName();
         Logger.debug("operation = " + var3);
         UDDIXMLHandlerMaker var4 = UDDIXMLHandlerMaker.getInstance();
         UDDIXMLHandler var5 = var4.makeHandler(var3);
         UDDIRequest var6 = (UDDIRequest)var5.create(var0);
         Logger.debug("handler : " + var5.getClass().getName());
         Logger.debug("request : " + var6.getClass().getName());
         var7 = var6;
      } finally {
         Logger.trace("-UDDISOAPWrapper.createRequest(Node)");
      }

      return var7;
   }
}
