package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.auddi.uddi.UDDIException;

public class SearchNameHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      Element var2 = (Element)var1;
      String var4 = null;
      String var5 = null;
      Language var6 = null;
      if (var2.getChildNodes().item(0) != null) {
         var4 = var2.getChildNodes().item(0).getNodeValue();
         Attr var7 = var2.getAttributeNode("xml:lang");
         if (var7 != null) {
            var5 = var7.getNodeValue();
            var6 = new Language(var5);
         }

         Name var3 = new Name(var4, var6);
         return var3;
      } else {
         return null;
      }
   }
}
