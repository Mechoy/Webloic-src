package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.BindingTemplate;
import weblogic.uddi.client.structures.request.SaveBinding;

public class SaveBindingDOMBinder {
   public static Element toDOM(SaveBinding var0, Document var1) {
      Element var2 = UpdateRequestDOMBinder.toDOM("save_binding", var0, var1);
      Vector var3 = var0.getBindingTemplateVector();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.size(); ++var4) {
            var2.appendChild(BindingTemplateDOMBinder.toDOM((BindingTemplate)var3.elementAt(var4), var1));
         }
      }

      return var2;
   }
}
