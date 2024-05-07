package weblogic.wsee.security.wst.binding;

import java.util.Map;
import org.w3c.dom.Element;
import weblogic.xml.dom.marshal.MarshalException;

public class KeySize extends TrustDOMStructure {
   public static final String NAME = "KeySize";
   private int size = 256;

   public KeySize() {
   }

   public KeySize(String var1) {
      if (var1 != null) {
         this.namespaceUri = var1;
      }

   }

   public int getSize() {
      return this.size;
   }

   public void setSize(int var1) {
      this.size = var1;
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      addTextContent(var1, String.valueOf(this.size));
   }

   public void unmarshalContents(Element var1) throws MarshalException {
      this.size = Integer.parseInt(getTextContent(var1));
   }

   public String getName() {
      return "KeySize";
   }
}
