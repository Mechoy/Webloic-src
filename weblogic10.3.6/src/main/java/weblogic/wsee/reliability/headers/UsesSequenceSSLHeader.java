package weblogic.wsee.reliability.headers;

import javax.xml.namespace.QName;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.compat.SimpleElement;

public class UsesSequenceSSLHeader extends WsrmHeader {
   private static final long serialVersionUID = 4208222266024433659L;
   public static final String LOCAL_NAME;
   public static final MsgHeaderType TYPE;

   public UsesSequenceSSLHeader() {
      this(WsrmConstants.RMVersion.latest());
   }

   public UsesSequenceSSLHeader(WsrmConstants.RMVersion var1) {
      super(var1, LOCAL_NAME);
      this.setMustUnderstand(true);
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public void readFromSimpleElement(SimpleElement var1) throws MsgHeaderException {
      super.setRmVersionFromSimpleElement(var1);
   }

   public SimpleElement writeToSimpleElement() throws MsgHeaderException {
      QName var1 = this.getName();
      SimpleElement var2 = new SimpleElement(var1);
      return var2;
   }

   public void writeTo(ContentHandler var1, ErrorHandler var2) throws SAXException {
      QName var3 = this.getName();
      String var4 = var3.getNamespaceURI();
      String var5 = var3.getLocalPart();
      var1.startPrefixMapping("", var4);
      var1.startElement(var4, var5, var5, EMPTY_ATTS);
      var1.endElement(var4, var5, var5);
   }

   static {
      LOCAL_NAME = WsrmConstants.Element.USES_SEQUENCE_SSL.getElementName();
      TYPE = new MsgHeaderType();
   }
}
