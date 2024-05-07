package weblogic.wsee.addressing;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class InterfaceNameHeader extends MsgHeader {
   public static final MsgHeaderType TYPE = new MsgHeaderType();
   private QName name;
   private String interfaceName;

   public InterfaceNameHeader() {
      this.name = WSAddressingConstants.WSA_HEADER_METADATA_INTERFACE_NAME_10;
   }

   public InterfaceNameHeader(String var1) {
      this.name = WSAddressingConstants.WSA_HEADER_METADATA_INTERFACE_NAME_10;
      this.interfaceName = var1;
   }

   public String getInterfaceName() {
      return this.interfaceName;
   }

   public void setInterfaceName(String var1) {
      this.interfaceName = var1;
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

   public void read(Element var1) throws MsgHeaderException {
      try {
         this.interfaceName = DOMUtils.getTextData(var1);
      } catch (DOMProcessingException var3) {
         throw new MsgHeaderException("Could not get Interface name", var3);
      }
   }

   public void write(Element var1) throws MsgHeaderException {
      DOMUtils.addTextData(var1, this.interfaceName);
   }
}
