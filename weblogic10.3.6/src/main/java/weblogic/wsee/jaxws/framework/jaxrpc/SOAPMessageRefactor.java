package weblogic.wsee.jaxws.framework.jaxrpc;

import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Messages;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class SOAPMessageRefactor {
   private Entry envEntry;
   private Entry headerEntry;
   private Entry bodyEntry;

   public void init(Message var1) {
      if (var1 != null) {
         try {
            SOAPMessage var2 = var1.readAsSOAPMessage();
            SOAPEnvelope var3 = var2.getSOAPPart().getEnvelope();
            SOAPHeader var4 = var3.getHeader();
            SOAPBody var5 = var3.getBody();
            this.envEntry = lookupAtts(var3);
            this.headerEntry = lookupAtts(var4);
            this.bodyEntry = lookupAtts(var5);
         } catch (SOAPException var6) {
            throw new WebServiceException(var6);
         }
      }
   }

   public Message refator(Message var1) {
      if (var1 == null) {
         return null;
      } else {
         try {
            SOAPMessage var2 = var1.readAsSOAPMessage();
            SOAPEnvelope var3 = var2.getSOAPPart().getEnvelope();
            SOAPHeader var4 = var3.getHeader();
            SOAPBody var5 = var3.getBody();
            setupAtts(var3, this.envEntry);
            setupAtts(var4, this.headerEntry);
            setupAtts(var5, this.bodyEntry);
            return Messages.create(var2);
         } catch (SOAPException var6) {
            throw new WebServiceException(var6);
         }
      }
   }

   private static Entry lookupAtts(Element var0) {
      NamedNodeMap var1 = null;
      Attr var2 = null;
      var1 = var0.getAttributes();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.getLength(); ++var3) {
            Attr var4 = (Attr)var1.item(var3);
            String var5 = var4.getLocalName();
            if ("http://www.w3.org/2000/xmlns/".equals(var4.getNamespaceURI()) && var5 != null && var5.equals(var0.getPrefix())) {
               var2 = var4;
               break;
            }
         }
      }

      return new Entry(var1, var2);
   }

   private static void setupAtts(Element var0, Entry var1) {
      NamedNodeMap var2 = var1.atts;
      Attr var3 = var1.excludeAtt;
      if (var2 != null) {
         for(int var4 = 0; var4 < var2.getLength(); ++var4) {
            Attr var5 = (Attr)var2.item(var4);
            if (var5 != var3) {
               var0.setAttributeNode((Attr)var0.getOwnerDocument().importNode(var5, true));
            }
         }
      }

   }

   private static class Entry {
      private NamedNodeMap atts = null;
      private Attr excludeAtt = null;

      public Entry(NamedNodeMap var1, Attr var2) {
         this.atts = var1;
         this.excludeAtt = var2;
      }
   }
}
