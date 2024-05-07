package weblogic.wsee.reliability.handshake;

import org.w3c.dom.Element;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class SequenceOffer {
   private WsrmConstants.RMVersion rmVersion;
   private String sequenceId = null;
   private String expires = null;
   private EndpointReference endpoint;
   private WsrmConstants.IncompleteSequenceBehavior incompleteSequenceBehavior;

   public SequenceOffer(WsrmConstants.RMVersion var1) {
      this.rmVersion = var1;
   }

   public WsrmConstants.RMVersion getRmVersion() {
      return this.rmVersion;
   }

   public void setSequenceId(String var1) {
      this.sequenceId = var1;
   }

   public String getSequenceId() {
      return this.sequenceId;
   }

   public void setExpires(String var1) {
      this.expires = var1;
   }

   public String getExpires() {
      return this.expires;
   }

   public EndpointReference getEndpoint() {
      return this.endpoint;
   }

   public void setEndpoint(EndpointReference var1) {
      this.endpoint = var1;
   }

   public WsrmConstants.IncompleteSequenceBehavior getIncompleteSequenceBehavior() {
      return this.incompleteSequenceBehavior;
   }

   public void setIncompleteSequenceBehavior(WsrmConstants.IncompleteSequenceBehavior var1) {
      this.incompleteSequenceBehavior = var1;
   }

   public void read(Element var1) throws HandshakeMsgException {
      try {
         this.sequenceId = DOMUtils.getValueByTagNameNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.IDENTIFIER.getElementName());
         String var2 = DOMUtils.getOptionalValueByTagNameNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.EXPIRES.getElementName());
         if (var2 != null && !var2.equals("P0S")) {
            this.expires = var2;
         }

         if (this.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
            Element var3 = DOMUtils.getElementByTagNameNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.ENDPOINT.getElementName());
            this.endpoint = new EndpointReference();
            this.endpoint.read(var3);
            Element var4 = DOMUtils.getOptionalElementByTagNameNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.INCOMPLETE_SEQUENCE_BEHAVIOR.getElementName());
            if (var4 != null) {
               String var5 = DOMUtils.getTextContent(var4, true);
               this.incompleteSequenceBehavior = WsrmConstants.IncompleteSequenceBehavior.valueOf(var5);
            }
         }

      } catch (DOMProcessingException var6) {
         throw new HandshakeMsgException("Could not parse create sequence/offer message", var6);
      }
   }

   public void write(Element var1) throws HandshakeMsgException {
      if (this.sequenceId == null) {
         throw new HandshakeMsgException("Sequence ID is not set in the offer element of create sequence message");
      } else {
         DOMUtils.addValueNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.IDENTIFIER.getQualifiedName(this.getRmVersion()), this.sequenceId);
         Element var2;
         if (this.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
            if (this.endpoint == null) {
               throw new HandshakeMsgException("Endpoint is not set in create sequence Offer element");
            }

            var2 = var1.getOwnerDocument().createElementNS(this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.ENDPOINT.getQualifiedName(this.getRmVersion()));
            this.endpoint.write(var2);
            var1.appendChild(var2);
         }

         if (this.expires != null) {
            DOMUtils.addValueNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.EXPIRES.getQualifiedName(this.getRmVersion()), this.expires);
         }

         if (this.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11) && this.incompleteSequenceBehavior != null) {
            var2 = var1.getOwnerDocument().createElementNS(this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.INCOMPLETE_SEQUENCE_BEHAVIOR.getQualifiedName(this.getRmVersion()));
            DOMUtils.addTextData(var2, this.incompleteSequenceBehavior.name());
            var1.appendChild(var2);
         }

      }
   }
}
