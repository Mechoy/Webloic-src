package weblogic.xml.crypto.wss11.internal;

import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.dom.marshal.MarshalException;

public class SignatureConfirmationImpl implements SignatureConfirmation {
   private static final String ID_PREFIX = "sigconf";
   private String id = DOMUtils.generateId("sigconf");
   private String signatureValue = null;
   private Node signatureConfirmationNode;

   public SignatureConfirmationImpl() {
   }

   public SignatureConfirmationImpl(String var1) {
      this.signatureValue = var1;
   }

   public String getId() {
      return this.id;
   }

   public String getSignatureValue() {
      return this.signatureValue;
   }

   public Node getSignatureConfirmationNode() {
      return this.signatureConfirmationNode;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public void marshal(Element var1, Node var2, Map var3) throws MarshalException {
      String var4 = (String)var3.get("http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd");
      if (null == var4) {
         var4 = "wsse11";
      }

      String var5 = (String)var3.get("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
      Element var6 = DOMUtils.createElement(var1, WSS11Constants.SIG_CONF_QNAME, var4);
      DOMUtils.addPrefixedAttribute(var6, WSSConstants.WSU_ID_QNAME, var5, this.id);
      if (this.signatureValue != null) {
         DOMUtils.addAttribute(var6, WSS11Constants.VALUE_QNAME, this.signatureValue);
      }

      if (var2 != null) {
         var1.insertBefore(var6, var2);
      } else {
         var1.appendChild(var6);
      }

      this.signatureConfirmationNode = var6;
   }

   public void unmarshal(Node var1) throws MarshalException {
      Element var2 = (Element)var1;
      this.id = DOMUtils.getAttributeValue(var2, WSSConstants.WSU_ID_QNAME);
      this.signatureValue = DOMUtils.getAttributeValue(var2, WSS11Constants.VALUE_QNAME);
      this.signatureConfirmationNode = var1;
   }
}
