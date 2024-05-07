package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Node;
import weblogic.auddi.uddi.UDDIException;

public class OperatorHandler extends UDDIXMLHandler {
   private Operator operator = null;

   public Object create(Node var1) throws UDDIException {
      String var2 = var1.getNodeValue();
      if (var2 != null && !var2.equals("")) {
         this.operator = new Operator(var2);
      }

      return this.operator;
   }
}
