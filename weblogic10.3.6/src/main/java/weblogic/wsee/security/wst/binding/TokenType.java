package weblogic.wsee.security.wst.binding;

import java.util.Map;
import org.w3c.dom.Element;
import weblogic.xml.dom.marshal.MarshalException;

public class TokenType extends TrustDOMStructure {
   public static final String NAME = "TokenType";
   private String tokenType;

   public TokenType() {
   }

   public TokenType(String var1) {
      if (var1 != null) {
         this.namespaceUri = var1;
      }

   }

   public void setTokenType(String var1) {
      this.tokenType = var1;
   }

   public String getTokenType() {
      return this.tokenType;
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      assert this.tokenType != null;

      addTextContent(var1, this.tokenType);
   }

   public void unmarshalContents(Element var1) throws MarshalException {
      this.tokenType = getTextContent(var1);
   }

   public String getName() {
      return "TokenType";
   }
}
