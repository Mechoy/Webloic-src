package weblogic.wsee.reliability.handshake;

import org.w3c.dom.Element;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class SequenceAccept {
   private WsrmConstants.RMVersion rmVersion;
   private EndpointReference acksTo = null;

   public SequenceAccept(WsrmConstants.RMVersion var1) {
      this.rmVersion = var1;
   }

   public WsrmConstants.RMVersion getRmVersion() {
      return this.rmVersion;
   }

   public void setAcksTo(EndpointReference var1) {
      this.acksTo = var1;
   }

   public EndpointReference getAcksTo() {
      return this.acksTo;
   }

   public void read(Element var1) throws HandshakeMsgException {
      try {
         Element var2 = DOMUtils.getElementByTagNameNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.ACKS_TO.getElementName());
         this.acksTo = new EndpointReference();
         this.acksTo.read(var2);
      } catch (DOMProcessingException var3) {
         throw new HandshakeMsgException("Could not parse create sequence response/accept message", var3);
      } catch (MsgHeaderException var4) {
         throw new HandshakeMsgException("Could not parse create sequence response/accept message", var4);
      }
   }

   public void write(Element var1) throws HandshakeMsgException {
      if (this.acksTo == null) {
         throw new HandshakeMsgException("AcksTo is not set in the accept element of create sequence response message");
      } else {
         Element var2 = var1.getOwnerDocument().createElementNS(this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.ACKS_TO.getQualifiedName(this.getRmVersion()));
         this.acksTo.write(var2);
         var1.appendChild(var2);
      }
   }
}
