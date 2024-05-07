package weblogic.auddi.uddi.datastructure;

import java.net.MalformedURLException;
import java.net.URL;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class OverviewURLHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      URL var2 = null;
      Element var3 = (Element)var1;
      String var4 = "";
      if (var3.getChildNodes().item(0) != null) {
         var4 = var3.getChildNodes().item(0).getNodeValue();
      }

      if (var4.equals("")) {
         throw new FatalErrorException(UDDIMessages.get("error.valueNotAllowed.empty", "overviewURL"));
      } else {
         try {
            var2 = new URL(var4);
            return var2;
         } catch (MalformedURLException var6) {
            throw new FatalErrorException(UDDIMessages.get("error.fatalError.malformedURL", var4), var6);
         }
      }
   }
}
