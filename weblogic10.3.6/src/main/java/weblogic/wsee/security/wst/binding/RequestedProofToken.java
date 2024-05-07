package weblogic.wsee.security.wst.binding;

import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.crypto.encrypt.api.keyinfo.EncryptedKey;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.dom.marshal.MarshalException;

public class RequestedProofToken extends TrustDOMStructure {
   public static final String NAME = "RequestedProofToken";
   private BinarySecret bs;
   private ComputedKey ck;
   private EncryptedKey ek;
   private SecurityToken st;

   public RequestedProofToken() {
   }

   public RequestedProofToken(String var1) {
      this.namespaceUri = var1;
   }

   public void setBinarySecret(BinarySecret var1) {
      this.bs = var1;
   }

   public BinarySecret getBinarySecret() {
      return this.bs;
   }

   public void setComputedKey(ComputedKey var1) {
      this.ck = var1;
   }

   public ComputedKey getComputedKey() {
      return this.ck;
   }

   public void setEncryptedKey(EncryptedKey var1) {
      this.ek = var1;
   }

   public EncryptedKey getEncryptedKey() {
      return this.ek;
   }

   public void setSecurityToken(SecurityToken var1) {
      this.st = var1;
   }

   public SecurityToken getSecurityToken() {
      return this.st;
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      if (this.bs != null) {
         this.bs.marshal(var1, (Node)null, var2);
      }

      if (this.ck != null) {
         this.ck.marshal(var1, (Node)null, var2);
      }

      if (this.ek != null) {
      }

      if (this.st != null) {
         this.st.marshal(var1, (Node)null, var2);
      }

   }

   public void unmarshalContents(Element var1) throws MarshalException {
      Element var2 = getElementByTagName(var1, "BinarySecret", true);
      if (var2 != null) {
         this.bs = new BinarySecret(var2.getNamespaceURI());
         this.bs.unmarshal(var2);
      }

      Element var3 = getElementByTagName(var1, "ComputedKey", true);
      if (var3 != null) {
         this.ck = new ComputedKey(var3.getNamespaceURI());
         this.ck.unmarshal(var3);
      }

   }

   public String getName() {
      return "RequestedProofToken";
   }
}
