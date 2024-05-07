package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.TModelBag;
import weblogic.uddi.client.structures.datatypes.TModelKey;

public class TModelBagDOMBinder {
   public static TModelBag fromDOM(Element var0) {
      TModelBag var1 = new TModelBag();
      Vector var2 = new Vector();
      NodeList var3 = var0.getElementsByTagName("tModelKey");
      int var4 = var3.getLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.add(TModelKeyDOMBinder.fromDOM((Element)var3.item(var5)));
      }

      var1.setTModelKeyVector(var2);
      return var1;
   }

   public static Element toDOM(TModelBag var0, Document var1) {
      Element var2 = var1.createElement("tModelBag");
      Vector var3 = var0.getTModelKeyVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(TModelKeyDOMBinder.toDOM((TModelKey)var3.elementAt(var4), var1));
      }

      return var2;
   }
}
