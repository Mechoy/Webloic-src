package weblogic.wsee.addressing;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class ActionHeader extends MsgHeader implements AddressingHeader {
   private static final long serialVersionUID = 2313387877871567399L;
   private String actionURI;
   public static final MsgHeaderType TYPE = new MsgHeaderType();
   private QName name;

   public ActionHeader(String var1, QName var2) {
      this.name = WSAddressingConstants.WSA_HEADER_ACTION_10;
      this.actionURI = var1;
      this.name = var2;
   }

   public ActionHeader(String var1) {
      this.name = WSAddressingConstants.WSA_HEADER_ACTION_10;
      this.actionURI = var1;
   }

   public ActionHeader(QName var1) {
      this.name = WSAddressingConstants.WSA_HEADER_ACTION_10;
      this.name = var1;
   }

   public ActionHeader() {
      this.name = WSAddressingConstants.WSA_HEADER_ACTION_10;
   }

   public QName getName() {
      return this.name;
   }

   public void setName(QName var1) {
      this.name = var1;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public String getActionURI() {
      return this.actionURI;
   }

   public void setActionURI(String var1) {
      this.actionURI = var1;
   }

   public void read(Element var1) throws MsgHeaderException {
      try {
         this.actionURI = DOMUtils.getTextData(var1);
      } catch (DOMProcessingException var3) {
         throw new MsgHeaderException("Could not get actionURI", var3);
      }
   }

   public void write(Element var1) throws MsgHeaderException {
      DOMUtils.addTextData(var1, this.actionURI);
   }
}
