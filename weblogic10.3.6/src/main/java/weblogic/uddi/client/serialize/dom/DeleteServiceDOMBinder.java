package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.ServiceKey;
import weblogic.uddi.client.structures.request.DeleteService;

public class DeleteServiceDOMBinder {
   public static Element toDOM(DeleteService var0, Document var1) {
      Element var2 = UpdateRequestDOMBinder.toDOM("delete_service", var0, var1);
      Vector var3 = var0.getServiceKeyVector();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.size(); ++var4) {
            var2.appendChild(ServiceKeyDOMBinder.toDOM((ServiceKey)var3.elementAt(var4), var1));
         }
      }

      return var2;
   }
}
