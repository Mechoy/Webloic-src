package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.FindQualifier;
import weblogic.uddi.client.structures.datatypes.FindQualifiers;

public class FindQualifiersDOMBinder {
   public static FindQualifiers fromDOM(Element var0) {
      FindQualifiers var1 = new FindQualifiers();
      Vector var2 = new Vector();
      NodeList var3 = var0.getElementsByTagName("findQualifier");
      int var4 = var3.getLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.add(FindQualifierDOMBinder.fromDOM((Element)var3.item(var5)));
      }

      var1.setFindQualifierVector(var2);
      return var1;
   }

   public static Element toDOM(FindQualifiers var0, Document var1) {
      Element var2 = var1.createElement("findQualifiers");
      Vector var3 = var0.getFindQualifierVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(FindQualifierDOMBinder.toDOM((FindQualifier)var3.elementAt(var4), var1));
      }

      return var2;
   }
}
