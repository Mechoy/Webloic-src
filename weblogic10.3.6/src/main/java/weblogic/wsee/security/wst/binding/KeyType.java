package weblogic.wsee.security.wst.binding;

import java.util.Map;
import org.w3c.dom.Element;
import weblogic.xml.dom.marshal.MarshalException;

public class KeyType extends TrustDOMStructure {
   public static final String NAME = "KeyType";
   private String keyType;

   public KeyType() {
   }

   public KeyType(String var1) {
      if (var1 != null) {
         this.namespaceUri = var1;
      }

   }

   public void setKeyType(String var1) {
      this.keyType = var1;
   }

   public String getKeyType() {
      return this.keyType;
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      assert this.keyType != null;

      addTextContent(var1, this.keyType);
   }

   public void unmarshalContents(Element var1) throws MarshalException {
      this.keyType = getTextContent(var1);
   }

   public String getName() {
      return "KeyType";
   }
}
