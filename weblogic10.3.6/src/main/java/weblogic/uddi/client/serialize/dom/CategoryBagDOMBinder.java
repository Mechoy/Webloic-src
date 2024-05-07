package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.CategoryBag;
import weblogic.uddi.client.structures.datatypes.KeyedReference;

public class CategoryBagDOMBinder {
   public static CategoryBag fromDOM(Element var0) {
      CategoryBag var1 = new CategoryBag();
      Vector var2 = new Vector();
      NodeList var3 = var0.getElementsByTagName("keyedReference");
      int var4 = var3.getLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.add(KeyedReferenceDOMBinder.fromDOM((Element)var3.item(var5)));
      }

      var1.setKeyedReferenceVector(var2);
      return var1;
   }

   public static Element toDOM(CategoryBag var0, Document var1) {
      Element var2 = var1.createElement("categoryBag");
      Vector var3 = var0.getKeyedReferenceVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(KeyedReferenceDOMBinder.toDOM((KeyedReference)var3.elementAt(var4), var1));
      }

      return var2;
   }
}
