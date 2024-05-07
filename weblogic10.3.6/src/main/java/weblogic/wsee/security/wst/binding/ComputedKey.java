package weblogic.wsee.security.wst.binding;

import java.util.Map;
import org.w3c.dom.Element;
import weblogic.xml.dom.marshal.MarshalException;

public class ComputedKey extends TrustDOMStructure {
   public static final String NAME = "ComputedKey";
   private String uri;

   public ComputedKey() {
   }

   public ComputedKey(String var1) {
      if (var1 != null) {
         this.namespaceUri = var1;
      }

   }

   public void setUri(String var1) {
      this.uri = var1;
   }

   public String getUri() {
      return this.uri;
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      if (this.uri == null) {
         throw new MarshalException("uri of ComputedKey can not be null");
      } else {
         addTextContent(var1, this.uri);
      }
   }

   public void unmarshalContents(Element var1) throws MarshalException {
      this.uri = getTextContent(var1);
   }

   public String getName() {
      return "ComputedKey";
   }
}
