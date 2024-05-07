package weblogic.wsee.reliability.handshake;

import java.util.HashMap;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.xml.crypto.wss.SecurityTokenReferenceImpl;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;
import weblogic.xml.dom.marshal.MarshalException;

public final class CreateSequenceMsg extends WsrmHandshakeMsg {
   private EndpointReference acksTo;
   private Duration expires;
   private SequenceOffer offer;
   private SecurityTokenReference str;

   public CreateSequenceMsg(WsrmConstants.RMVersion var1) {
      super(WsrmConstants.Element.CREATE_SEQUENCE.getElementName(), var1);
   }

   public EndpointReference getAcksTo() {
      return this.acksTo;
   }

   public void setAcksTo(EndpointReference var1) {
      this.acksTo = var1;
   }

   public Duration getExpires() {
      return this.expires;
   }

   public void setExpires(Duration var1) {
      this.expires = var1;
   }

   public SequenceOffer getOffer() {
      return this.offer;
   }

   public void setOffer(SequenceOffer var1) {
      this.offer = var1;
   }

   public void setSecurityTokenReference(SecurityTokenReference var1) {
      this.str = var1;
   }

   public SecurityTokenReference getSecurityTokenReference() {
      return this.str;
   }

   public void read(Element var1) throws HandshakeMsgException {
      try {
         Element var2 = DOMUtils.getElementByTagNameNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.ACKS_TO.getElementName());
         this.acksTo = new EndpointReference();
         this.acksTo.read(var2);
         String var3 = DOMUtils.getOptionalValueByTagNameNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.EXPIRES.getElementName());
         if (var3 != null && !var3.equals("P0S")) {
            try {
               this.expires = DatatypeFactory.newInstance().newDuration(var3);
            } catch (DatatypeConfigurationException var8) {
               throw new HandshakeMsgException(var8.getMessage());
            } catch (IllegalArgumentException var9) {
               throw new HandshakeMsgException(var9.getMessage());
            }
         }

         Element var4 = DOMUtils.getOptionalElementByTagNameNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.OFFER.getElementName());
         if (var4 != null) {
            this.offer = new SequenceOffer(this.getRmVersion());
            this.offer.read(var4);
         }

         Element var5 = DOMUtils.getOptionalElementByTagNameNS(var1, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "SecurityTokenReference");
         if (var5 != null) {
            try {
               this.str = SecurityTokenReferenceImpl.createAndUnmarshal(var5);
            } catch (MarshalException var7) {
               throw new HandshakeMsgException(var7.getMessage(), var7);
            }
         }

      } catch (DOMProcessingException var10) {
         throw new HandshakeMsgException("Could not parse create sequence message", var10);
      } catch (MsgHeaderException var11) {
         throw new HandshakeMsgException("Could not parse create sequence message", var11);
      }
   }

   public void write(Element var1) throws HandshakeMsgException {
      if (this.acksTo == null) {
         throw new HandshakeMsgException("AcksTo is not set in create sequence message");
      } else {
         Element var2 = var1.getOwnerDocument().createElementNS(this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.ACKS_TO.getQualifiedName(this.getRmVersion()));
         this.acksTo.write(var2);
         var1.appendChild(var2);
         if (this.expires != null) {
            DOMUtils.addValueNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.EXPIRES.getQualifiedName(this.getRmVersion()), this.expires.toString());
         }

         if (this.offer != null) {
            Element var3 = var1.getOwnerDocument().createElementNS(this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.OFFER.getQualifiedName(this.getRmVersion()));
            this.offer.write(var3);
            var1.appendChild(var3);
         }

         if (this.str != null) {
            try {
               this.str.marshal(var1, (Node)null, new HashMap());
            } catch (MarshalException var4) {
               throw new HandshakeMsgException(var4.getMessage(), var4);
            }
         }

      }
   }
}
