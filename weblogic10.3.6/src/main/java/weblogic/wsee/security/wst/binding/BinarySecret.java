package weblogic.wsee.security.wst.binding;

import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.Base64Encoding;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.dom.marshal.MarshalException;

public class BinarySecret extends TrustDOMStructure {
   private static final long serialVersionUID = 4725494712412800768L;
   public static final String NAME = "BinarySecret";
   public static final String XML_ATTRIB_BS_TYPE = "Type";
   private String id;
   private String type;
   private byte[] value;

   public BinarySecret() {
   }

   public BinarySecret(String var1) {
      if (var1 != null) {
         this.namespaceUri = var1;
      }

   }

   public void setType(String var1) {
      this.type = var1;
   }

   public String getType() {
      return this.type;
   }

   public byte[] getValue() {
      return this.value;
   }

   public void setValue(byte[] var1) {
      this.value = var1;
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      assert this.value != null;

      if (this.id == null) {
         this.id = DOMUtils.generateId("BinarySecret");
      }

      String var3 = (String)var2.get("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
      if (null == var3) {
         var3 = "wsu";
         var2.put("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", var3);
      }

      DOMUtils.addPrefixedAttribute(var1, WSSConstants.WSU_ID_QNAME, var3, this.id);
      DOMUtils.declareNamespace(var1, WSSConstants.WSU_ID_QNAME.getNamespaceURI(), var3, var2);
      if (this.type != null) {
         setAttribute(var1, "Type", this.type);
      }

      addTextContent(var1, (new Base64Encoding()).encode(this.value));
   }

   public void unmarshalContents(Element var1) throws MarshalException {
      this.id = getAttributeValueAsString(var1, WSSConstants.WSU_ID_QNAME);
      this.type = getAttributeValueAsString(var1, new QName("Type"));
      this.value = (new Base64Encoding()).decode(getTextContent(var1));
   }

   public String getName() {
      return "BinarySecret";
   }
}
