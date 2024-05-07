package weblogic.wsee.reliability.headers;

import com.bea.xbean.util.XsTypeConverter;
import java.util.Calendar;
import javax.xml.namespace.QName;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.compat.SimpleElement;

public class SequenceHeader extends WsrmHeader {
   private static final long serialVersionUID = 7442405656574388775L;
   public static final String LOCAL_NAME;
   public static final MsgHeaderType TYPE;
   private boolean lastMessage;
   private String sequenceId;
   private long messageNumber;
   private Calendar expires;

   public SequenceHeader() {
      this(WsrmConstants.RMVersion.latest());
   }

   public SequenceHeader(WsrmConstants.RMVersion var1) {
      this(var1, (String)null, 0L);
   }

   public SequenceHeader(WsrmConstants.RMVersion var1, String var2, long var3) {
      this(var1, var2, var3, false);
   }

   public SequenceHeader(WsrmConstants.RMVersion var1, String var2, long var3, boolean var5) {
      super(var1, LOCAL_NAME);
      this.lastMessage = false;
      this.sequenceId = null;
      this.messageNumber = 0L;
      this.sequenceId = var2;
      this.messageNumber = var3;
      this.lastMessage = var5;
      this.setMustUnderstand(true);
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
   }

   public void setLastMessage(boolean var1) {
      if (this.getRmVersion() != WsrmConstants.RMVersion.RM_10) {
         throw new IllegalStateException("Cannot set Sequence/LastMessage in Sequence header for WS-RM versions past version 1.0. WS-RM version in use is " + this.getRmVersion() + ". Use CloseSequence message instead");
      } else {
         this.lastMessage = var1;
      }
   }

   public boolean isLastMessage() {
      if (this.getRmVersion() != WsrmConstants.RMVersion.RM_10) {
         throw new IllegalStateException("Cannot use Sequence/LastMessage from Sequence header for WS-RM versions past version 1.0. WS-RM version in use is " + this.getRmVersion() + ". Use CloseSequence message instead");
      } else {
         return this.lastMessage;
      }
   }

   public Calendar getExpires() {
      return this.expires;
   }

   public void setExpires(Calendar var1) {
      this.expires = var1;
   }

   public void readFromSimpleElement(SimpleElement var1) throws MsgHeaderException {
      super.setRmVersionFromSimpleElement(var1);
      this.sequenceId = SimpleElement.getContentForChild(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.IDENTIFIER.getElementName());
      String var2 = SimpleElement.getContentForChild(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.MESSAGE_NUMBER.getElementName());
      this.messageNumber = Long.parseLong(var2);
      SimpleElement var3 = SimpleElement.getOptionalChild(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.LAST_MESSAGE.getElementName());
      if (var3 != null && this.getRmVersion() != WsrmConstants.RMVersion.RM_10) {
         throw new MsgHeaderException("Found Sequence/LastMessage in incoming Sequence header. Cannot use Sequence/LastMessage for WS-RM versions past version 1.0. WS-RM version in use is " + this.getRmVersion() + ". Use CloseSequence message instead");
      } else {
         this.lastMessage = var3 != null;
         String var4 = SimpleElement.getOptionalContentForChild(var1, "http://schemas.xmlsoap.org/ws/2002/07/utility", "Expires");
         if (var4 != null) {
            this.expires = XsTypeConverter.lexDateTime(var4);
         }

      }
   }

   public SimpleElement writeToSimpleElement() throws MsgHeaderException {
      QName var1 = this.getName();
      SimpleElement var2 = new SimpleElement(var1);
      if (this.sequenceId == null) {
         throw new MsgHeaderException("Sequence ID is not set");
      } else {
         SimpleElement.addChild(var2, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.IDENTIFIER.getQualifiedName(this.getRmVersion()), this.sequenceId);
         SimpleElement.addChild(var2, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.MESSAGE_NUMBER.getQualifiedName(this.getRmVersion()), Long.toString(this.messageNumber));
         if (this.lastMessage && this.getRmVersion() == WsrmConstants.RMVersion.RM_10) {
            SimpleElement.addChild(var2, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.LAST_MESSAGE.getQualifiedName(this.getRmVersion()), "");
         }

         if (this.expires != null) {
            SimpleElement.addChild(var2, "http://schemas.xmlsoap.org/ws/2002/07/utility", "wsu:Expires", XsTypeConverter.printDateTime(this.expires));
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
      this.writeToMessageNumberSubElement(var1, this.messageNumber);
      var1.endElement(var4, var5, var5);
   }

   static {
      LOCAL_NAME = WsrmConstants.Element.SEQUENCE.getElementName();
      TYPE = new MsgHeaderType();
   }
}
