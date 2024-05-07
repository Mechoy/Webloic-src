package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.IdentifierBag;
import weblogic.uddi.client.structures.datatypes.KeyedReference;

public class IdentifierBagDOMBinder {
   public static IdentifierBag fromDOM(Element var0) {
      IdentifierBag var1 = new IdentifierBag();
      Vector var2 = new Vector();
      NodeList var3 = var0.getElementsByTagName("keyedReference");
      int var4 = var3.getLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.add(KeyedReferenceDOMBinder.fromDOM((Element)var3.item(var5)));
      }

      var1.setKeyedReferenceVector(var2);
      return var1;
   }

   public static Element toDOM(IdentifierBag var0, Document var1) {
      Element var2 = var1.createElement("identifierBag");
      Vector var3 = var0.getKeyedReferenceVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(KeyedReferenceDOMBinder.toDOM((KeyedReference)var3.elementAt(var4), var1));
      }

      return var2;
   }
}
