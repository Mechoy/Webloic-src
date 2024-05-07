package weblogic.auddi.soap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.Element;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.response.Result;
import weblogic.auddi.util.Logger;
import weblogic.auddi.util.PropertyManager;
import weblogic.webservice.core.soap.MessageFactoryImpl;
import weblogic.webservice.core.soap.SOAPConnectionFactoryImpl;
import weblogic.webservice.core.soap.SOAPFactoryImpl;

class SAAJTransport extends SOAPTransport {
   private static SOAPConnectionFactory s_soapConnectionFactory;
   private static MessageFactory s_messageFactory;
   private static SOAPFactory s_soapFactory;

   public SAAJTransport(String var1, String var2, String var3) {
      Logger.trace("+weblogic.auddi.soap.SAAJTransport.CTOR");
      Logger.trace("messageFactoryClass : " + var1);
      Logger.trace("connectionFactoryClass : " + var2);
      Logger.trace("soapFactoryClass : " + var3);
      if (s_messageFactory == null) {
         if (var1 != null && var1.length() != 0) {
            try {
               s_messageFactory = (MessageFactory)Class.forName(var1).newInstance();
               s_soapConnectionFactory = (SOAPConnectionFactory)Class.forName(var2).newInstance();
               s_soapFactory = (SOAPFactory)Class.forName(var3).newInstance();
            } catch (ClassNotFoundException var10) {
               var10.printStackTrace();
               if (s_messageFactory == null) {
                  throw new RuntimeException(UDDIMessages.get("error.class.notfound", var1));
               }

               if (s_soapConnectionFactory == null) {
                  throw new RuntimeException(UDDIMessages.get("error.class.notfound", var2));
               }

               throw new RuntimeException(UDDIMessages.get("error.class.notfound", var3));
            } catch (InstantiationException var11) {
               var11.printStackTrace();
               if (s_messageFactory == null) {
                  throw new RuntimeException(UDDIMessages.get("error.class.instantiation", var1));
               }

               if (s_soapConnectionFactory == null) {
                  throw new RuntimeException(UDDIMessages.get("error.class.instantiation", var2));
               }

               throw new RuntimeException(UDDIMessages.get("error.class.instantiation", var3));
            } catch (IllegalAccessException var12) {
               var12.printStackTrace();
               if (s_messageFactory == null) {
                  throw new RuntimeException(UDDIMessages.get("error.class.access", var1));
               }

               if (s_soapConnectionFactory == null) {
                  throw new RuntimeException(UDDIMessages.get("error.class.access", var2));
               }

               throw new RuntimeException(UDDIMessages.get("error.class.access", var3));
            }
         } else {
            s_messageFactory = new MessageFactoryImpl();
            s_soapConnectionFactory = new SOAPConnectionFactoryImpl();
            s_soapFactory = new SOAPFactoryImpl();
         }

         if (s_envelopeURI == null) {
            try {
               SOAPMessage var4 = s_messageFactory.createMessage();
               SOAPEnvelope var5 = var4.getSOAPPart().getEnvelope();
               SOAPBody var6 = var5.getBody();
               Name var7 = var5.getElementName();
               Name var8 = var6.getElementName();
               s_envelopeURI = var7.getURI();
               s_envelopePrefix = var7.getPrefix();
               s_envelopeQualifiedName = var7.getQualifiedName();
               s_bodyURI = var8.getURI();
               s_bodyQualifiedName = var8.getQualifiedName();
            } catch (SOAPException var9) {
               var9.printStackTrace();
               if (s_messageFactory == null) {
                  throw new RuntimeException(UDDIMessages.get("error.class.instantiation", var1));
               }

               if (s_soapConnectionFactory == null) {
                  throw new RuntimeException(UDDIMessages.get("error.class.instantiation", var2));
               }

               throw new RuntimeException(UDDIMessages.get("error.class.instantiation", var3));
            }
         }
      }

      Logger.trace("SAAJTransport.MessageFactoryClass: " + s_messageFactory.getClass().getName());
      Logger.trace("SAAJTransport.SOAPConnectionFactoryClass: " + s_soapConnectionFactory.getClass().getName());
      Logger.trace("SAAJTransport.SOAPFactoryClass: " + s_soapFactory.getClass().getName());
      Logger.trace("-weblogic.auddi.soap.SAAJTransport.CTOR");
   }

   public String createFaultMessage(String var1, String var2, Result var3) {
      Logger.trace("+SAAJTransport.createFaultMessage()");

      try {
         SOAPMessage var6 = s_messageFactory.createMessage();
         SOAPPart var4 = var6.getSOAPPart();
         SOAPEnvelope var8 = var4.getEnvelope();
         SOAPBody var9 = var8.getBody();
         SOAPFault var5 = var9.addFault();
         SOAPFactory var7 = SOAPFactory.newInstance();
         if (var2 == null) {
            var2 = "";
         }

         if (var1 == null) {
            var1 = s_envelopePrefix + ":Client";
         } else {
            int var10 = var1.lastIndexOf(58);
            if (var10 != -1) {
               var1 = s_envelopePrefix + var1.substring(var10);
            } else {
               var1 = s_envelopePrefix + ":" + var1;
            }
         }

         var5.setFaultCode(var1);
         var5.setFaultString(var2);
         Detail var18 = var5.addDetail();
         Name var11 = var4.getEnvelope().createName("dispositionReport");
         DetailEntry var12 = var18.addDetailEntry(var11);
         var12.addNamespaceDeclaration("", "urn:uddi-org:api_v2");
         var12.addAttribute(var7.createName("generic"), "2.0");
         var12.addAttribute(var7.createName("operator"), PropertyManager.getRuntimeProperty("auddi.siteoperator"));
         SOAPElement var13 = var12.addChildElement("result");
         var13.addAttribute(var7.createName("errno"), (new Integer(var3.getErrno())).toString());
         SOAPElement var14 = var13.addChildElement("errInfo");
         var14.addAttribute(var7.createName("errCode"), var3.getErrCode());
         var14.addTextNode(var3.getErrMsg());
         ByteArrayOutputStream var15 = new ByteArrayOutputStream();
         var6.writeTo(var15);
         Logger.trace("-SAAJTransport.createFaultMessage()");
         return var15.toString();
      } catch (SOAPException var16) {
         var16.printStackTrace();
         throw new RuntimeException(UDDIMessages.get("error.operation.failed", "createFaultMessage"));
      } catch (IOException var17) {
         var17.printStackTrace();
         throw new RuntimeException(UDDIMessages.get("error.operation.failed", "createFaultMessage"));
      }
   }

   public String sendSOAPRequest(Element var1, URL var2, String var3, int var4) throws SOAPWrapperException {
      SOAPConnection var5 = null;

      String var10;
      try {
         Logger.trace("+SAAJTransport.sendSOAPRequest(org.w3c.dom.Element)");
         if (var1 == null) {
            Logger.trace("-SAAJTransport.sendSOAPRequest(org.w3c.dom.Element)");
            throw new IllegalArgumentException("Parameter request is null");
         }

         if (var2 == null) {
            Logger.trace("-SAAJTransport.sendSOAPRequest(org.w3c.dom.Element)");
            throw new IllegalArgumentException("Parameter url is null");
         }

         var5 = s_soapConnectionFactory.createConnection();
         SOAPMessage var6 = s_messageFactory.createMessage();
         var6.getSOAPPart().setContent(new DOMSource(var1.getOwnerDocument()));
         SOAPMessage var7 = var5.call(var6, var2);
         ByteArrayOutputStream var8 = new ByteArrayOutputStream();

         try {
            var7.writeTo(var8);
         } catch (IOException var39) {
            throw new SOAPWrapperException(var39);
         }

         String var9 = var8.toString();
         if (var9.indexOf("Fault>") != -1 || var9.indexOf("fault>") != -1) {
            var9 = this.fixSOAP(var9);
         }

         if (!var9.trim().startsWith("<?xml")) {
            var9 = "<?xml version='1.0' encoding='UTF-8'?>" + var9;
         }

         var10 = var9;
      } catch (SOAPException var40) {
         throw new SOAPWrapperException(var40);
      } finally {
         try {
            var5.close();
         } catch (SOAPException var37) {
            Logger.debug("Exception while trying to close connection. Will be ignored:");
            Logger.debug((Throwable)var37);
         } finally {
            Logger.trace("-SAAJTransport.sendSOAPRequest(org.w3c.dom.Element)");
         }

      }

      return var10;
   }
}
