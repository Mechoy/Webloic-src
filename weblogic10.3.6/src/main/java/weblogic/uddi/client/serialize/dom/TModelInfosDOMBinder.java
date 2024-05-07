package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.TModelInfo;
import weblogic.uddi.client.structures.datatypes.TModelInfos;

public class TModelInfosDOMBinder {
   public static TModelInfos fromDOM(Element var0) {
      TModelInfos var1 = new TModelInfos();
      Vector var2 = new Vector();
      NodeList var3 = var0.getElementsByTagName("tModelInfo");
      int var4 = var3.getLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.add(TModelInfoDOMBinder.fromDOM((Element)var3.item(var5)));
      }

      var1.setTModelInfoVector(var2);
      return var1;
   }

   public static Element toDOM(TModelInfos var0, Document var1) {
      Element var2 = var1.createElement("tModelInfos");
      Vector var3 = var0.getTModelInfoVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(TModelInfoDOMBinder.toDOM((TModelInfo)var3.elementAt(var4), var1));
      }

      return var2;
   }
}
