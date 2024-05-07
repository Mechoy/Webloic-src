package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.auddi.uddi.UDDIException;

public class DescriptionHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      Element var2 = (Element)var1;
      Description var3;
      if (var2.getChildNodes().item(0) != null) {
         var3 = new Description(var2.getChildNodes().item(0).getNodeValue());
      } else {
         var3 = new Description("");
      }

      Attr var4 = var2.getAttributeNode("xml:lang");
      if (var4 != null) {
         var3.setLang(new Language(var4.getNodeValue()));
      }

      return var3;
   }
}
