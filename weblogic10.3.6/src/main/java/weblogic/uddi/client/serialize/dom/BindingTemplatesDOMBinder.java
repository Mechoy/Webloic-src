package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.BindingTemplate;
import weblogic.uddi.client.structures.datatypes.BindingTemplates;

public class BindingTemplatesDOMBinder {
   public static BindingTemplates fromDOM(Element var0) {
      BindingTemplates var1 = new BindingTemplates();
      Vector var2 = new Vector();
      NodeList var3 = var0.getElementsByTagName("bindingTemplate");
      int var4 = var3.getLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.add(BindingTemplateDOMBinder.fromDOM((Element)var3.item(var5)));
      }

      var1.setBindingTemplateVector(var2);
      return var1;
   }

   public static Element toDOM(BindingTemplates var0, Document var1) {
      Element var2 = var1.createElement("bindingTemplates");
      Vector var3 = var0.getBindingTemplateVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(BindingTemplateDOMBinder.toDOM((BindingTemplate)var3.elementAt(var4), var1));
      }

      return var2;
   }
}
