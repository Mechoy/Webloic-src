package weblogic.wsee.addressing;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class ToHeader extends MsgHeader implements AddressingHeader {
   private static final long serialVersionUID = -7635380952188297321L;
   public QName name;
   public static final MsgHeaderType TYPE = new MsgHeaderType();
   private String address;

   public ToHeader(String var1) {
      this.name = WSAddressingConstants.WSA_HEADER_TO_10;
      this.address = var1;
      this.setMustUnderstand(false);
   }

   public ToHeader(String var1, QName var2) {
      this(var1);
      this.name = var2;
   }

   public ToHeader() {
      this.name = WSAddressingConstants.WSA_HEADER_TO_10;
      this.setMustUnderstand(false);
   }

   public ToHeader(QName var1) {
      this();
      this.name = var1;
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

   public String getAddress() {
      return this.address;
   }

   public void setAddress(String var1) {
      this.address = var1;
   }

   public void read(Element var1) throws MsgHeaderException {
      try {
         this.address = DOMUtils.getTextData(var1);
      } catch (DOMProcessingException var3) {
         throw new MsgHeaderException("Could not get address", var3);
      }
   }

   public void write(Element var1) throws MsgHeaderException {
      DOMUtils.addTextData(var1, this.address);
   }
}
