package weblogic.wsee.reliability.handshake;

import org.w3c.dom.Element;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public abstract class EndOfLifeSequenceResponseMsg extends WsrmHandshakeMsg {
   private String sequenceId;

   public EndOfLifeSequenceResponseMsg(WsrmConstants.RMVersion var1, String var2, String var3) {
      super(var2, var1);
      this.sequenceId = var3;
   }

   public EndOfLifeSequenceResponseMsg(WsrmConstants.RMVersion var1, String var2) {
      this(var1, var2, (String)null);
   }

   public String getSequenceId() {
      return this.sequenceId;
   }

   public void setSequenceId(String var1) {
      this.sequenceId = var1;
   }

   public void read(Element var1) throws HandshakeMsgException {
      try {
         this.sequenceId = DOMUtils.getValueByTagNameNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.IDENTIFIER.getElementName());
      } catch (DOMProcessingException var3) {
         throw new HandshakeMsgException("Could not parse terminate sequence message", var3);
      }
   }

   public void write(Element var1) throws HandshakeMsgException {
      if (this.sequenceId == null) {
         throw new HandshakeMsgException("Sequence ID is not set");
      } else {
         DOMUtils.addNamespaceDeclaration(var1, "wsu", "http://schemas.xmlsoap.org/ws/2002/07/utility");
         DOMUtils.addValueNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.IDENTIFIER.getQualifiedName(this.getRmVersion()), this.sequenceId);
      }
   }
}
