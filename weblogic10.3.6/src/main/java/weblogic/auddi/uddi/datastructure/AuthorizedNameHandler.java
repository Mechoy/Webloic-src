package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Node;
import weblogic.auddi.uddi.UDDIException;

public class AuthorizedNameHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      AuthorizedName var2 = null;
      String var3 = var1.getNodeValue();
      if (var3 != null && !var3.equals("")) {
         var2 = new AuthorizedName(var3);
      }

      return var2;
   }
}
