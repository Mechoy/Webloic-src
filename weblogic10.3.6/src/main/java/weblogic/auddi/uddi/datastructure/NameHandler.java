package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.ValueNotAllowedException;

public class NameHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      Element var2 = (Element)var1;
      if (var2.getChildNodes().item(0) != null) {
         String var4 = var2.getChildNodes().item(0).getNodeValue();
         if (var4.trim().equals("")) {
            throw new ValueNotAllowedException(UDDIMessages.get("error.valueNotAllowed.empty", "name"));
         } else {
            Name var3 = new Name(var4);
            Attr var5 = var2.getAttributeNode("xml:lang");
            if (var5 != null) {
               var3.setLang(new Language(var5.getNodeValue()));
            }

            return var3;
         }
      } else {
         throw new ValueNotAllowedException(UDDIMessages.get("error.valueNotAllowed.empty", "name"));
      }
   }
}
