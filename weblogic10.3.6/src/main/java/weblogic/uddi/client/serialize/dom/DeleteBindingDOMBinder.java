package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.BindingKey;
import weblogic.uddi.client.structures.request.DeleteBinding;

public class DeleteBindingDOMBinder {
   public static Element toDOM(DeleteBinding var0, Document var1) {
      Element var2 = UpdateRequestDOMBinder.toDOM("delete_binding", var0, var1);
      Vector var3 = var0.getBindingKeyVector();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.size(); ++var4) {
            var2.appendChild(BindingKeyDOMBinder.toDOM((BindingKey)var3.elementAt(var4), var1));
         }
      }

      return var2;
   }
}
