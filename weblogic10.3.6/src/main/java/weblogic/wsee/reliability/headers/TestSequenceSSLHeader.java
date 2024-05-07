package weblogic.wsee.reliability.headers;

import javax.xml.namespace.QName;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.compat.SimpleElement;

public class TestSequenceSSLHeader extends WsrmHeader {
   private static final long serialVersionUID = -5994213896810460482L;
   public static final String LOCAL_NAME;
   public static final MsgHeaderType TYPE;
   private String sslSessionId;

   public TestSequenceSSLHeader() {
      this(WsrmConstants.RMVersion.latest());
   }

   public TestSequenceSSLHeader(WsrmConstants.RMVersion var1) {
      super(var1, LOCAL_NAME);
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public String getSSLSessionId() {
      return this.sslSessionId;
   }

   public void setSSLSessionId(String var1) {
      this.sslSessionId = var1;
   }

   public void readFromSimpleElement(SimpleElement var1) throws MsgHeaderException {
      super.setRmVersionFromSimpleElement(var1);
      String var2 = SimpleElement.getContentForChild(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.TEST_SEQUENCE_SSL_SESSION_ID.getElementName());
      this.sslSessionId = var2;
   }

   public SimpleElement writeToSimpleElement() throws MsgHeaderException {
      QName var1 = this.getName();
      SimpleElement var2 = new SimpleElement(var1);
      if (this.sslSessionId != null) {
         SimpleElement.addChild(var2, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.TEST_SEQUENCE_SSL_SESSION_ID.getQualifiedName(this.getRmVersion()), this.sslSessionId);
      }

      return var2;
   }

   public void writeTo(ContentHandler var1, ErrorHandler var2) throws SAXException {
      QName var3 = this.getName();
      String var4 = var3.getNamespaceURI();
      String var5 = var3.getLocalPart();
      var1.startPrefixMapping("", var4);
      var1.startElement(var4, var5, var5, EMPTY_ATTS);
      QName var6 = WsrmConstants.Element.TEST_SEQUENCE_SSL_SESSION_ID.getQName(this.getRmVersion());
      String var7 = var6.getNamespaceURI();
      String var8 = var6.getLocalPart();
      var1.startElement(var7, var8, var8, EMPTY_ATTS);
      var1.characters(this.getSSLSessionId().toCharArray(), 0, this.getSSLSessionId().length());
      var1.endElement(var7, var8, var8);
      var1.endElement(var4, var5, var5);
   }

   static {
      LOCAL_NAME = WsrmConstants.Element.TEST_SEQUENCE_SSL.getElementName();
      TYPE = new MsgHeaderType();
   }
}
