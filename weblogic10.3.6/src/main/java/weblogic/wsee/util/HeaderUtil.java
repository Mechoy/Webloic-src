package weblogic.wsee.util;

import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import weblogic.wsee.message.MsgHeader;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class HeaderUtil {
   private static final boolean verbose = Verbose.isVerbose(HeaderUtil.class);

   public static void throwMustUnderstand(MessageContext var0) {
      if (var0 instanceof SOAPMessageContext) {
         SOAPMessageContext var1 = (SOAPMessageContext)var0;

         try {
            checkMustUnderstandHeader(var1.getMessage().getSOAPHeader());
         } catch (SOAPException var3) {
            throw new JAXRPCException("Failed to get soap message from context", var3);
         }
      }

   }

   public static void checkMustUnderstandHeader(SOAPHeader var0) {
      if (var0 != null) {
         Iterator var1 = var0.examineAllHeaderElements();

         SOAPHeaderElement var2;
         do {
            if (!var1.hasNext()) {
               return;
            }

            var2 = (SOAPHeaderElement)var1.next();
         } while(!var2.getMustUnderstand());

         String var3 = "MustUnderstand header not processed '" + var2.getElementName() + "'";
         throw new SOAPFaultException(new QName(var2.getNamespaceURI(), "MustUnderstand"), var3, (String)null, (Detail)null);
      }
   }

   public static void removeMustUnderstandFromHeader(MsgHeader var0, SOAPMessage var1) {
      try {
         if (var0.isMustUnderstand()) {
            SOAPHeader var2 = var1.getSOAPHeader();
            if (var2 == null) {
               return;
            }

            Element var3 = DOMUtils.getElementByTagNameNS(var2, var0.getName().getNamespaceURI(), var0.getName().getLocalPart());
            if (var3 == null) {
               return;
            }

            removeMustUnderstandFromHeader(var3);
         }

      } catch (SOAPException var4) {
         throw new JAXRPCException(var4);
      } catch (DOMProcessingException var5) {
         throw new JAXRPCException(var5);
      }
   }

   public static void removeMustUnderstandFromHeader(Element var0) {
      String var1 = null;
      var1 = var0.getAttributeNS("http://schemas.xmlsoap.org/soap/envelope/", "mustUnderstand");
      if (var1 != null && !var1.equals("")) {
         if (verbose) {
            Verbose.log((Object)("Setting " + var0.getLocalName() + "/" + "http://schemas.xmlsoap.org/soap/envelope/" + ": " + "mustUnderstand" + " to 0"));
         }

         var0.setAttributeNS("http://schemas.xmlsoap.org/soap/envelope/", "mustUnderstand", "0");
      } else {
         var1 = var0.getAttributeNS("http://www.w3.org/2003/05/soap-envelope", "mustUnderstand");
         if (var1 != null && !var1.equals("")) {
            if (verbose) {
               Verbose.log((Object)("Setting " + var0.getLocalName() + "/" + "http://www.w3.org/2003/05/soap-envelope" + ": " + "mustUnderstand" + " to 0"));
            }

            var0.setAttributeNS("http://www.w3.org/2003/05/soap-envelope", "mustUnderstand", "0");
         }
      }
   }
}
