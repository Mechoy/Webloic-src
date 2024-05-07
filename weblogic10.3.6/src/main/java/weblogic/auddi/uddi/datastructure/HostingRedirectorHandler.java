package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class HostingRedirectorHandler extends UDDIXMLHandler {
   private static UDDIXMLHandlerMaker maker = null;

   public Object create(Node var1) throws UDDIException {
      maker = UDDIXMLHandlerMaker.getInstance();
      NamedNodeMap var2 = var1.getAttributes();
      BindingKeyHandler var3 = (BindingKeyHandler)maker.makeHandler("bindingKey");
      BindingKey var4 = (BindingKey)var3.create(var2.getNamedItem("bindingKey"));
      if (var4 == null) {
         throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "bindingKey"));
      } else {
         HostingRedirector var5 = new HostingRedirector(var4);
         return var5;
      }
   }
}
