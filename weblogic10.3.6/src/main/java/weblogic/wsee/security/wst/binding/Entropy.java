package weblogic.wsee.security.wst.binding;

import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.dom.marshal.MarshalException;

public class Entropy extends TrustDOMStructure {
   public static final String NAME = "Entropy";
   private BinarySecret binarySecret;

   public Entropy() {
   }

   public Entropy(String var1) {
      if (var1 != null) {
         this.namespaceUri = var1;
      }

   }

   public BinarySecret getBinarySecret() {
      return this.binarySecret;
   }

   public void setBinarySecret(BinarySecret var1) {
      this.binarySecret = var1;
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      if (this.binarySecret != null) {
         this.binarySecret.marshal(var1, (Node)null, var2);
      }

   }

   public void unmarshalContents(Element var1) throws MarshalException {
      Element var2 = getElementByTagName(var1, "BinarySecret", false);
      this.binarySecret = new BinarySecret(var2.getNamespaceURI());
      this.binarySecret.unmarshal(var2);
   }

   public String getName() {
      return "Entropy";
   }
}
