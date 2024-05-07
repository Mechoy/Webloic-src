package weblogic.wsee.message.soap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.wsee.addressing.AddressingHeader;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderFactory;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.util.Verbose;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class SoapMsgHeaders implements MsgHeaders {
   private static final boolean verbose = Verbose.isVerbose(SoapMsgHeaders.class);
   private final SOAPMessage message;
   private Element soapHeader;
   private final Map parsedHeaders = new HashMap();

   public SoapMsgHeaders(Element var1) throws MsgHeaderException {
      this.message = null;
      this.soapHeader = var1;
      this.parseHeaders(this.soapHeader);
   }

   public SoapMsgHeaders(SOAPMessage var1) throws MsgHeaderException {
      this.message = var1;

      try {
         this.soapHeader = var1.getSOAPHeader();
         if (this.soapHeader != null) {
            this.parseHeaders(this.soapHeader);
         }

      } catch (SOAPException var3) {
         throw new MsgHeaderException("Could not access SOAP header", var3);
      }
   }

   public MsgHeader getHeader(MsgHeaderType var1) throws MsgHeaderException {
      return (MsgHeader)this.parsedHeaders.get(var1);
   }

   public void addHeader(MsgHeader var1) throws MsgHeaderException {
      this.addHeaderInternal(var1, false);
   }

   private void addHeaderInternal(MsgHeader var1, boolean var2) throws MsgHeaderException {
      if (!var2 && this.parsedHeaders.containsKey(var1.getType())) {
         throw new MsgHeaderException("SoapMsgHeaders already contains header of type " + var1.getName() + ". Cannot add another one.");
      } else {
         this.parsedHeaders.put(var1.getType(), var1);
         this.ensureSOAPHeader();
         Element var3 = var1.writeToParent(this.soapHeader);
         if (var1.getRole() != null) {
            this.setActor(var3, var1.getRole());
         }

         if (var1.isMustUnderstand()) {
            this.setMustUnderstand(var3, true);
         }

      }
   }

   public void replaceHeader(MsgHeader var1) throws MsgHeaderException {
      this.ensureSOAPHeader();
      QName var2 = var1.getName();
      NodeList var3 = this.soapHeader.getElementsByTagNameNS(var2.getNamespaceURI(), var2.getLocalPart());

      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         this.soapHeader.removeChild(var3.item(var4));
      }

      this.addHeaderInternal(var1, true);
   }

   public void addHeaders(NodeList var1) throws MsgHeaderException {
      this.ensureSOAPHeader();

      try {
         DOMUtils.copyNodes(this.soapHeader, var1);
      } catch (DOMProcessingException var3) {
         throw new MsgHeaderException("Could not copy headers", var3);
      }
   }

   private void ensureSOAPHeader() {
      if (this.soapHeader == null) {
         if (this.message == null) {
            throw new MsgHeaderException("Could not create SOAP header - not inside a message");
         }

         try {
            this.soapHeader = this.message.getSOAPHeader();
            if (this.soapHeader == null) {
               this.soapHeader = this.message.getSOAPPart().getEnvelope().addHeader();
            }
         } catch (SOAPException var2) {
            throw new MsgHeaderException("Could not add SOAP header", var2);
         }
      }

   }

   public boolean isEmpty() {
      return this.parsedHeaders.isEmpty();
   }

   public Iterator listHeaders() {
      return this.parsedHeaders.values().iterator();
   }

   private void parseHeaders(Element var1) {
      for(Node var2 = var1.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         if (var2 instanceof Element) {
            Element var3 = (Element)var2;
            if (var3.getNamespaceURI() != null) {
               QName var4 = new QName(var3.getNamespaceURI(), var3.getLocalName());
               MsgHeader var5 = MsgHeaderFactory.getInstance().createMsgHeader(var4);
               var5.read(var3);
               String var6 = this.getActor(var3);
               if (var6 != null && var6.length() > 0) {
                  var5.setRole(var6);
               }

               var5.setMustUnderstand(this.getMustUnderstand(var3));
               if (verbose) {
                  Verbose.log((Object)("Parsed header " + var4 + ":" + var5));
               }

               if (this.parsedHeaders.get(var5.getType()) != null) {
                  if (var5 instanceof AddressingHeader) {
                     var5.setDuplicated(true);
                  } else if (!var5.isMultiple()) {
                     throw new JAXRPCException("Duplicate header " + var4 + ":" + var5);
                  }
               }

               this.parsedHeaders.put(var5.getType(), var5);
            }
         }
      }

   }

   private String getSoapNamespace(Element var1) {
      Node var2 = var1.getParentNode();

      assert var2 != null;

      String var3 = var2.getNamespaceURI();
      return !"http://schemas.xmlsoap.org/soap/envelope/".equals(var3) && !"http://www.w3.org/2003/05/soap-envelope".equals(var3) ? null : var3;
   }

   public boolean getMustUnderstand(Element var1) {
      String var2 = this.getSoapNamespace(var1);
      if (var2 == null) {
         return false;
      } else {
         Attr var3 = var1.getAttributeNodeNS(var2, "mustUnderstand");
         if (var3 == null) {
            return false;
         } else {
            String var4 = var3.getValue();
            return "1".equals(var4) || "true".equals(var4);
         }
      }
   }

   public void setMustUnderstand(Element var1, boolean var2) {
      String var3 = this.getSoapNamespace(var1);
      if (var3 != null) {
         var1.setAttributeNS(var3, "env:mustUnderstand", var2 ? "1" : "0");
      }
   }

   public String getActor(Element var1) {
      String var2 = this.getSoapNamespace(var1);
      return var2 == null ? null : var1.getAttributeNS(var2, "actor");
   }

   public void setActor(Element var1, String var2) {
      String var3 = this.getSoapNamespace(var1);
      if (var3 != null) {
         var1.setAttributeNS(var3, "actor", var2);
      }
   }
}
