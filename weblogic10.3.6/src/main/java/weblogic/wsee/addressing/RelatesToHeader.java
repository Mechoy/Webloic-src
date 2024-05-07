package weblogic.wsee.addressing;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class RelatesToHeader extends MsgHeader implements AddressingHeader {
   private static final long serialVersionUID = 1127182093303253908L;
   public QName name;
   public static final MsgHeaderType TYPE = new MsgHeaderType();
   private String relatedMessageId;
   private QName relationshipType;

   public RelatesToHeader(String var1, QName var2) {
      this.name = WSAddressingConstants.WSA_HEADER_RELATES_TO_10;
      this.relatedMessageId = var1;
      this.relationshipType = var2;
   }

   public RelatesToHeader(String var1, QName var2, QName var3) {
      this.name = WSAddressingConstants.WSA_HEADER_RELATES_TO_10;
      this.relatedMessageId = var1;
      this.relationshipType = var2;
      this.name = var3;
   }

   public RelatesToHeader() {
      this.name = WSAddressingConstants.WSA_HEADER_RELATES_TO_10;
   }

   public RelatesToHeader(QName var1) {
      this.name = WSAddressingConstants.WSA_HEADER_RELATES_TO_10;
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

   public QName getRelationshipType() {
      return this.relationshipType;
   }

   public void setRelationshipType(QName var1) {
      this.relationshipType = var1;
   }

   public String getRelatedMessageId() {
      return this.relatedMessageId;
   }

   public void setRelatedMessageId(String var1) {
      this.relatedMessageId = var1;
   }

   public void read(Element var1) throws MsgHeaderException {
      try {
         this.relatedMessageId = DOMUtils.getTextData(var1);
         String var2 = var1.getAttributeNS(this.name.getNamespaceURI(), "RelationshipType");
         int var3 = var2.indexOf(58);
         if (var3 != -1) {
            String var4 = var2.substring(0, var3);
            String var5 = var2.substring(var3 + 1);
            String var6 = DOMUtils.getNamespaceURI(var1, var4);
            this.relationshipType = new QName(var6, var5, var4);
         } else {
            this.relationshipType = QName.valueOf(var2);
         }

      } catch (DOMProcessingException var7) {
         throw new MsgHeaderException("Could not parse the relatesTo header", var7);
      }
   }

   public void write(Element var1) throws MsgHeaderException {
      DOMUtils.addTextData(var1, this.relatedMessageId);
      if (this.relationshipType == null && this.name != null && "http://www.w3.org/2005/08/addressing".equals(this.name.getNamespaceURI())) {
         DOMUtils.addNamespaceDeclaration(var1, "wsa", "http://www.w3.org/2005/08/addressing");
         var1.setAttributeNS(this.name.getNamespaceURI(), "wsa:RelationshipType", "http://www.w3.org/2005/08/addressing/reply");
      } else {
         DOMUtils.addNamespaceDeclaration(var1, this.relationshipType.getPrefix(), this.relationshipType.getNamespaceURI());
         var1.setAttributeNS(this.name.getNamespaceURI(), "wsa:RelationshipType", this.relationshipType.getPrefix() + ":" + this.relationshipType.getLocalPart());
      }

   }
}
