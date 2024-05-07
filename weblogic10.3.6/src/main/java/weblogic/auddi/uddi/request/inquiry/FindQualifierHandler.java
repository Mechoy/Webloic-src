package weblogic.auddi.uddi.request.inquiry;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;

public class FindQualifierHandler extends UDDIXMLHandler {
   private NodeList children;

   public Object create(Node var1) throws UDDIException {
      Element var2 = (Element)var1;
      FindQualifier var3 = new FindQualifier(var2.getChildNodes().item(0).getNodeValue());
      return var3;
   }
}
