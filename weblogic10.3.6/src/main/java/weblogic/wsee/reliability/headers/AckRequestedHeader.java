package weblogic.wsee.reliability.headers;

import javax.xml.namespace.QName;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.compat.SimpleElement;

public class AckRequestedHeader extends WsrmHeader {
   private static final long serialVersionUID = -354937597205280227L;
   public static final String LOCAL_NAME;
   public static final MsgHeaderType TYPE;
   private String sequenceId;
   private long messageNumber;
   private boolean numSet;

   public AckRequestedHeader() {
      this(WsrmConstants.RMVersion.latest());
   }

   public AckRequestedHeader(WsrmConstants.RMVersion var1) {
      super(var1, LOCAL_NAME);
      this.sequenceId = null;
      this.messageNumber = 0L;
      this.numSet = false;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public String getSequenceId() {
      return this.sequenceId;
   }

   public void setSequenceId(String var1) {
      this.sequenceId = var1;
   }

   public long getMessageNumber() {
      return this.messageNumber;
   }

   public void setMessageNumber(long var1) {
      this.messageNumber = var1;
      this.numSet = true;
   }

   public void readFromSimpleElement(SimpleElement var1) throws MsgHeaderException {
      super.setRmVersionFromSimpleElement(var1);
      String var2 = null;

      try {
         this.sequenceId = SimpleElement.getContentForChild(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.IDENTIFIER.getElementName());
         var2 = SimpleElement.getOptionalContentForChild(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.MESSAGE_NUMBER.getElementName());
         if (var2 != null) {
            this.messageNumber = Long.parseLong(var2);
            if (this.messageNumber <= 0L) {
               throw new MsgHeaderException("Message Number is not a positive number: " + this.messageNumber);
            }

            this.numSet = true;
         }

      } catch (NumberFormatException var4) {
         throw new MsgHeaderException("Message number format error: " + var2, var4);
      }
   }

   public SimpleElement writeToSimpleElement() throws MsgHeaderException {
      QName var1 = this.getName();
      SimpleElement var2 = new SimpleElement(var1);
      if (this.sequenceId == null) {
         throw new MsgHeaderException("Sequence ID is not set");
      } else {
         SimpleElement.addChild(var2, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.IDENTIFIER.getQualifiedName(this.getRmVersion()), this.sequenceId);
         if (this.numSet) {
            if (this.messageNumber <= 0L) {
               throw new MsgHeaderException("Message number is not positive");
            }

            SimpleElement.addChild(var2, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.MESSAGE_NUMBER.getQualifiedName(this.getRmVersion()), Long.toString(this.messageNumber));
         }

         return var2;
      }
   }

   public void writeTo(ContentHandler var1, ErrorHandler var2) throws SAXException {
      QName var3 = this.getName();
      String var4 = var3.getNamespaceURI();
      String var5 = var3.getLocalPart();
      var1.startPrefixMapping("", var4);
      var1.startElement(var4, var5, var5, EMPTY_ATTS);
      this.writeToIdentifierSubElement(var1, this.sequenceId);
      if (this.numSet) {
         this.writeToMessageNumberSubElement(var1, this.messageNumber);
      }

      var1.endElement(var4, var5, var5);
   }

   static {
      LOCAL_NAME = WsrmConstants.Element.ACK_REQUESTED.getElementName();
      TYPE = new MsgHeaderType();
   }
}
