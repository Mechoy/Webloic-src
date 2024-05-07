package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.TModelInstanceDetails;
import weblogic.uddi.client.structures.datatypes.TModelInstanceInfo;

public class TModelInstanceDetailsDOMBinder {
   public static TModelInstanceDetails fromDOM(Element var0) {
      TModelInstanceDetails var1 = new TModelInstanceDetails();
      Vector var2 = new Vector();
      NodeList var3 = var0.getElementsByTagName("tModelInstanceInfo");
      int var4 = var3.getLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.add(TModelInstanceInfoDOMBinder.fromDOM((Element)var3.item(var5)));
      }

      var1.setTModelInstanceInfoVector(var2);
      return var1;
   }

   public static Element toDOM(TModelInstanceDetails var0, Document var1) {
      Element var2 = var1.createElement("tModelInstanceDetails");
      Vector var3 = var0.getTModelInstanceInfoVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(TModelInstanceInfoDOMBinder.toDOM((TModelInstanceInfo)var3.elementAt(var4), var1));
      }

      return var2;
   }
}
