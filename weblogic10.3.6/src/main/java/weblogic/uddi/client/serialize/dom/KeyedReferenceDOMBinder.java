package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.KeyedReference;

public class KeyedReferenceDOMBinder {
   public static KeyedReference fromDOM(Element var0) {
      KeyedReference var1 = new KeyedReference();
      if (var0.hasAttribute("tModelKey")) {
         var1.setTModelKey(var0.getAttribute("tModelKey"));
      }

      if (var0.hasAttribute("keyName")) {
         var1.setKeyName(var0.getAttribute("keyName"));
      }

      if (var0.hasAttribute("keyValue")) {
         var1.setKeyValue(var0.getAttribute("keyValue"));
      }

      return var1;
   }

   public static Element toDOM(KeyedReference var0, Document var1) {
      Element var2 = var1.createElement("keyedReference");
      if (var0.getTModelKey() != null) {
         var2.setAttribute("tModelKey", var0.getTModelKey());
      }

      if (var0.getKeyName() != null) {
         var2.setAttribute("keyName", var0.getKeyName());
      }

      if (var0.getKeyValue() != null) {
         var2.setAttribute("keyValue", var0.getKeyValue());
      }

      return var2;
   }
}
