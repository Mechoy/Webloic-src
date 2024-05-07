package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.auddi.uddi.UDDIException;

public class InstanceParmsHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      Element var2 = (Element)var1;
      InstanceParms var3 = null;
      if (var2.getChildNodes().item(0) != null) {
         var3 = new InstanceParms(var2.getChildNodes().item(0).getNodeValue());
      } else {
         var3 = new InstanceParms("");
      }

      return var3;
   }
}
