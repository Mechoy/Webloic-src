package weblogic.wsee.reliability.handshake;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.util.ToStringWriter;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public abstract class WsrmHandshakeMsg {
   private String elementName;
   private WsrmConstants.RMVersion rmVersion;
   private boolean mustUnderstand;
   private String role;

   protected WsrmHandshakeMsg(String var1, WsrmConstants.RMVersion var2) {
      this.elementName = var1;
      this.rmVersion = var2;
   }

   public String getElementName() {
      return this.elementName;
   }

   public WsrmConstants.RMVersion getRmVersion() {
      return this.rmVersion;
   }

   public QName getQName() {
      return new QName(this.rmVersion.getNamespaceUri(), this.elementName, this.rmVersion.getPrefix());
   }

   public abstract void read(Element var1) throws HandshakeMsgException;

   public abstract void write(Element var1) throws HandshakeMsgException;

   public Element writeMsg(SOAPMessage var1) throws HandshakeMsgException {
      if (var1 == null) {
         throw new HandshakeMsgException("Null SOAP message");
      } else {
         SOAPBody var2;
         try {
            var2 = var1.getSOAPBody();
         } catch (SOAPException var6) {
            throw new HandshakeMsgException("Error getting SOAP body", var6);
         }

         if (var2 == null) {
            throw new HandshakeMsgException("Null SOAP message body");
         } else {
            QName var3 = this.getQName();
            Element var4 = var2.getOwnerDocument().createElementNS(var3.getNamespaceURI(), var3.getPrefix() + ":" + var3.getLocalPart());
            DOMUtils.addNamespaceDeclaration(var2, var3.getPrefix(), var3.getNamespaceURI());
            this.write(var4);
            var2.appendChild(var4);
            boolean var5 = false;
            if (this.getRole() != null) {
               DOMUtils.addNamespaceDeclaration(var4, "soap", "http://schemas.xmlsoap.org/soap/envelope/");
               var5 = true;
               var4.setAttributeNS("http://schemas.xmlsoap.org/soap/envelope/", "actor", this.getRole());
            }

            if (this.isMustUnderstand()) {
               if (!var5) {
                  DOMUtils.addNamespaceDeclaration(var4, "soap", "http://schemas.xmlsoap.org/soap/envelope/");
               }

               var4.setAttributeNS("http://schemas.xmlsoap.org/soap/envelope/", "soap:mustUnderstand", "1");
            }

            return var4;
         }
      }
   }

   public Element readMsg(SOAPMessage var1) throws HandshakeMsgException {
      if (var1 == null) {
         throw new HandshakeMsgException("Null SOAP message");
      } else {
         SOAPBody var2;
         try {
            var2 = var1.getSOAPBody();
         } catch (SOAPException var7) {
            throw new HandshakeMsgException("Error getting SOAP body", var7);
         }

         if (var2 == null) {
            throw new HandshakeMsgException("Null SOAP message body");
         } else {
            QName var3 = this.getQName();

            Element var4;
            try {
               var4 = DOMUtils.getElementByTagNameNS(var2, var3.getNamespaceURI(), var3.getLocalPart());
            } catch (DOMProcessingException var6) {
               throw new HandshakeMsgException("Error reading the handshake message", var6);
            }

            this.read(var4);
            return var4;
         }
      }
   }

   public void setMustUnderstand(boolean var1) {
      this.mustUnderstand = var1;
   }

   public boolean isMustUnderstand() {
      return this.mustUnderstand;
   }

   public String getRole() {
      return this.role;
   }

   public void setRole(String var1) {
      this.role = var1;
   }

   public void toString(ToStringWriter var1) {
      var1.writeField("role", this.role);
      var1.writeField("mustUnderstand", this.mustUnderstand);
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }
}
