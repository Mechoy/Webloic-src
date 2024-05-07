package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.Address;
import weblogic.uddi.client.structures.datatypes.AddressLine;

public class AddressDOMBinder {
   public static Address fromDOM(Element var0) {
      Address var1 = new Address();
      if (var0.hasAttribute("useType")) {
         var1.setUseType(var0.getAttribute("useType"));
      }

      if (var0.hasAttribute("sortCode")) {
         var1.setSortCode(var0.getAttribute("sortCode"));
      }

      Vector var2 = new Vector();
      NodeList var3 = var0.getElementsByTagName("addressLine");
      int var4 = var3.getLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.add(AddressLineDOMBinder.fromDOM((Element)var3.item(var5)));
      }

      var1.setAddressLineVector(var2);
      return var1;
   }

   public static Element toDOM(Address var0, Document var1) {
      Element var2 = var1.createElement("address");
      if (var0.getUseType() != null) {
         var2.setAttribute("useType", var0.getUseType());
      }

      if (var0.getSortCode() != null) {
         var2.setAttribute("sortCode", var0.getSortCode());
      }

      Vector var3 = var0.getAddressLineVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(AddressLineDOMBinder.toDOM((AddressLine)var3.elementAt(var4), var1));
      }

      return var2;
   }
}
