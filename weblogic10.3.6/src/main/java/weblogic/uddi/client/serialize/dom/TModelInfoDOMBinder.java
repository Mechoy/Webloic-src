package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.TModelInfo;

public class TModelInfoDOMBinder {
   public static TModelInfo fromDOM(Element var0) {
      TModelInfo var3 = new TModelInfo();
      if (var0.hasAttribute("tModelKey")) {
         var3.setTModelKey(var0.getAttribute("tModelKey"));
      }

      NodeList var1 = var0.getElementsByTagName("name");
      if (var1.getLength() > 0) {
         var3.setName(NameDOMBinder.fromDOM((Element)var1.item(0)));
      }

      return var3;
   }

   public static Element toDOM(TModelInfo var0, Document var1) {
      Element var2 = var1.createElement("tModelInfo");
      if (var0.getTModelKey() != null) {
         var2.setAttribute("tModelKey", var0.getTModelKey());
      }

      if (var0.getName() != null) {
         var2.appendChild(NameDOMBinder.toDOM(var0.getName(), var1));
      }

      return var2;
   }
}
