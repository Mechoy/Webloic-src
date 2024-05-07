package weblogic.wsee.security.wssc;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.dom.marshal.MarshalException;

public abstract class SecurityTokenBase implements SecurityToken {
   private String id;

   protected abstract Element marshalInternal(Element var1, Node var2, Map var3) throws MarshalException;

   protected abstract Element unmarshalInternal(Node var1) throws MarshalException;

   public void marshal(Element var1, Node var2, Map var3) throws MarshalException {
      Element var4 = this.marshalInternal(var1, var2, var3);
      if (var4 != null && this.getId() != null && var3 != null) {
         String var5 = (String)var3.get("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
         if (var5 == null) {
            var5 = "wsu";
            var3.put("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", var5);
         }

         DOMUtils.addPrefixedAttribute(var4, WSSConstants.WSU_ID_QNAME, var5, this.getId());
      }

   }

   public void unmarshal(Node var1) throws MarshalException {
      Element var2 = this.unmarshalInternal(var1);
      if (var2 != null) {
         this.setId(DOMUtils.getAttributeValue(var2, WSSConstants.WSU_ID_QNAME));
      }

   }

   public String getId() {
      if (this.id == null) {
         this.id = DOMUtils.generateId();
      }

      return this.id;
   }

   public void setId(String var1) {
      this.id = var1;
   }

   public PrivateKey getPrivateKey() {
      return null;
   }

   public PublicKey getPublicKey() {
      return null;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public int hashCode() {
      return this.getId().hashCode();
   }

   public boolean equals(Object var1) {
      Class var2 = this.getClass();
      if (!var2.isInstance(var1)) {
         return false;
      } else {
         SecurityTokenBase var3 = (SecurityTokenBase)var1;
         return this.getId().equals(var3.getId());
      }
   }
}
