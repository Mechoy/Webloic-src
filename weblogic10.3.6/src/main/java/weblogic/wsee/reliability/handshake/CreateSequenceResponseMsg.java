package weblogic.wsee.reliability.handshake;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import org.w3c.dom.Element;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class CreateSequenceResponseMsg extends WsrmHandshakeMsg {
   private String sequenceId;
   private Duration expires;
   private WsrmConstants.IncompleteSequenceBehavior incompleteSequenceBehavior;
   private SequenceAccept accept;

   public CreateSequenceResponseMsg(WsrmConstants.RMVersion var1) {
      super(WsrmConstants.Element.CREATE_SEQUENCE_RESPONSE.getElementName(), var1);
   }

   public String getSequenceId() {
      return this.sequenceId;
   }

   public void setSequenceId(String var1) {
      this.sequenceId = var1;
   }

   public Duration getExpires() {
      return this.expires;
   }

   public void setExpires(Duration var1) {
      this.expires = var1;
   }

   public WsrmConstants.IncompleteSequenceBehavior getIncompleteSequenceBehavior() {
      return this.incompleteSequenceBehavior;
   }

   public void setIncompleteSequenceBehavior(WsrmConstants.IncompleteSequenceBehavior var1) {
      this.incompleteSequenceBehavior = var1;
   }

   public SequenceAccept getAccept() {
      return this.accept;
   }

   public void setAccept(SequenceAccept var1) {
      this.accept = var1;
   }

   public void read(Element var1) throws HandshakeMsgException {
      try {
         this.sequenceId = DOMUtils.getValueByTagNameNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.IDENTIFIER.getElementName());
         String var2 = DOMUtils.getOptionalValueByTagNameNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.EXPIRES.getElementName());
         if (var2 != null && !var2.equals("P0S")) {
            try {
               this.expires = DatatypeFactory.newInstance().newDuration(var2);
            } catch (DatatypeConfigurationException var6) {
               throw new HandshakeMsgException(var6.getMessage());
            }
         }

         Element var3 = DOMUtils.getOptionalElementByTagNameNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.ACCEPT.getElementName());
         if (var3 != null) {
            this.accept = new SequenceAccept(this.getRmVersion());
            this.accept.read(var3);
         }

         if (this.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
            Element var4 = DOMUtils.getOptionalElementByTagNameNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.INCOMPLETE_SEQUENCE_BEHAVIOR.getElementName());
            if (var4 != null) {
               String var5 = DOMUtils.getTextContent(var4, true);
               this.incompleteSequenceBehavior = WsrmConstants.IncompleteSequenceBehavior.valueOf(var5);
            }
         }

      } catch (DOMProcessingException var7) {
         throw new HandshakeMsgException("Could not parse create sequence response message", var7);
      }
   }

   public void write(Element var1) throws HandshakeMsgException {
      if (this.sequenceId == null) {
         throw new HandshakeMsgException("Sequence ID is not set");
      } else {
         DOMUtils.addValueNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.IDENTIFIER.getQualifiedName(this.getRmVersion()), this.sequenceId);
         if (this.expires != null) {
            DOMUtils.addValueNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.EXPIRES.getQualifiedName(this.getRmVersion()), this.expires.toString());
         }

         Element var2;
         if (this.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11) && this.incompleteSequenceBehavior != null) {
            var2 = var1.getOwnerDocument().createElementNS(this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.INCOMPLETE_SEQUENCE_BEHAVIOR.getQualifiedName(this.getRmVersion()));
            DOMUtils.addTextData(var2, this.incompleteSequenceBehavior.name());
            var1.appendChild(var2);
         }

         if (this.accept != null) {
            var2 = var1.getOwnerDocument().createElementNS(this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.ACCEPT.getQualifiedName(this.getRmVersion()));
            this.accept.write(var2);
            var1.appendChild(var2);
         }

      }
   }
}
