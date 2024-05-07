package weblogic.wsee.reliability.handshake;

import org.w3c.dom.Element;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public abstract class EndOfLifeSequenceMsg extends WsrmHandshakeMsg {
   private String sequenceId;
   private long lastMsgNumber;

   protected EndOfLifeSequenceMsg(WsrmConstants.RMVersion var1, String var2, String var3) {
      super(var2, var1);
      this.lastMsgNumber = -1L;
      this.sequenceId = var3;
   }

   protected EndOfLifeSequenceMsg(WsrmConstants.RMVersion var1, String var2) {
      this(var1, var2, (String)null);
   }

   public String getSequenceId() {
      return this.sequenceId;
   }

   public long getLastMsgNumber() {
      return this.lastMsgNumber;
   }

   public void setLastMsgNumber(long var1) {
      this.lastMsgNumber = var1;
   }

   public void read(Element var1) throws HandshakeMsgException {
      try {
         this.sequenceId = DOMUtils.getValueByTagNameNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.IDENTIFIER.getElementName());
         if (this.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
            String var2 = DOMUtils.getOptionalValueByTagNameNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.LAST_MSG_NUMBER.getElementName());
            if (var2 != null) {
               this.lastMsgNumber = Long.parseLong(var2);
            }
         }

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
         if (this.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11) && this.lastMsgNumber > 0L) {
            DOMUtils.addValueNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.LAST_MSG_NUMBER.getQualifiedName(this.getRmVersion()), Long.toString(this.lastMsgNumber));
         }

      }
   }
}
