package weblogic.wsee.security.wst.binding;

import java.util.Map;
import org.w3c.dom.Element;
import weblogic.xml.dom.marshal.MarshalException;

public class AllowPostdating extends TrustDOMStructure {
   public static final String NAME = "AllowPostdating";

   public AllowPostdating() {
   }

   public AllowPostdating(String var1) {
      this.namespaceUri = var1;
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
   }

   public void unmarshalContents(Element var1) throws MarshalException {
   }

   public String getName() {
      return "AllowPostdating";
   }
}
