package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class KeyedReferenceHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      UDDIXMLHandlerMaker var2 = UDDIXMLHandlerMaker.getInstance();
      NamedNodeMap var3 = var1.getAttributes();
      TModelKeyHandler var4 = (TModelKeyHandler)var2.makeHandler("tModelKey");
      Node var5 = var3.getNamedItem("tModelKey");
      TModelKey var6 = null;
      if (var5 != null) {
         var6 = (TModelKey)var4.create(var5);
      }

      Node var7 = var3.getNamedItem("keyName");
      String var8 = null;
      if (var7 != null) {
         var8 = var7.getNodeValue();
      }

      var7 = var3.getNamedItem("keyValue");
      if (var7 == null) {
         throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "keyValue"));
      } else {
         String var9 = var7.getNodeValue();
         KeyedReference var10 = new KeyedReference(var6, var8, var9);
         return var10;
      }
   }
}
