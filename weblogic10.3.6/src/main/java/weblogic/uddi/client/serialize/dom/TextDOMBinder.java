package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TextDOMBinder {
   public static String fromDOM(Element var0) {
      NodeList var1 = var0.getChildNodes();
      String var2 = "";

      for(int var3 = 0; var3 < var1.getLength(); ++var3) {
         if (var1.item(var3).getNodeType() == 3) {
            var2 = var2 + var1.item(var3).getNodeValue();
         }
      }

      return var2;
   }

   public static Element toDOM(String var0, String var1, Document var2) {
      Element var3 = var2.createElement(var0);
      if (var1 != null) {
         var3.appendChild(var3.getOwnerDocument().createTextNode(var1));
      }

      return var3;
   }
}
