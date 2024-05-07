package weblogic.wsee.mc.headers;

import javax.xml.namespace.QName;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import weblogic.wsee.mc.utils.McConstants;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.reliability2.compat.CommonHeader;
import weblogic.wsee.reliability2.compat.SimpleElement;

public class MessagePendingHeader extends CommonHeader {
   private static final long serialVersionUID = 1L;
   public static final String LOCAL_NAME;
   public static final MsgHeaderType TYPE;
   private final McConstants.McVersion _mcVersion;
   private boolean _pending;

   public MessagePendingHeader(McConstants.McVersion var1) {
      super(new QName(var1.getNamespaceUri(), LOCAL_NAME, var1.getPrefix()));
      this._pending = false;
      this._mcVersion = var1;
   }

   public MessagePendingHeader() {
      this(McConstants.McVersion.latest());
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public McConstants.McVersion getMcVersion() {
      return this._mcVersion;
   }

   public void setPending(boolean var1) {
      this._pending = var1;
   }

   public boolean getPending() {
      return this._pending;
   }

   public void readFromSimpleElement(SimpleElement var1) throws MsgHeaderException {
      this._pending = false;
      String var2 = var1.getAttr((String)null, McConstants.Element.PENDING.getElementName());
      if (var2 != null) {
         this._pending = Boolean.parseBoolean(var2);
      }

   }

   public SimpleElement writeToSimpleElement() throws MsgHeaderException {
      QName var1 = McConstants.Element.MESSAGE_PENDING.getQName(this.getMcVersion());
      SimpleElement var2 = new SimpleElement(var1);
      String var3 = Boolean.toString(this._pending);
      var2.setAttr((String)null, McConstants.Element.PENDING.getElementName(), var3);
      return var2;
   }

   public void writeTo(ContentHandler var1, ErrorHandler var2) throws SAXException {
   }

   static {
      LOCAL_NAME = McConstants.Element.MESSAGE_PENDING.getElementName();
      TYPE = new MsgHeaderType();
   }
}
