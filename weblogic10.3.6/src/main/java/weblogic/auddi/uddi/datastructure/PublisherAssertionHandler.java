package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class PublisherAssertionHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      PublisherAssertion var2 = new PublisherAssertion();
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      NodeList var4 = var1.getChildNodes();
      if (var4 != null) {
         for(int var5 = 0; var5 < var4.getLength(); ++var5) {
            if (var4.item(var5).getNodeType() != 8) {
               BusinessKeyHandler var6;
               BusinessKey var7;
               if (var4.item(var5).getNodeName().equals("fromKey")) {
                  var6 = (BusinessKeyHandler)var3.makeHandler("businessKey");
                  var7 = (BusinessKey)var6.create(var4.item(var5));
                  var2.setFromKey(var7);
               }

               if (var4.item(var5).getNodeName().equals("toKey")) {
                  var6 = (BusinessKeyHandler)var3.makeHandler("businessKey");
                  var7 = (BusinessKey)var6.create(var4.item(var5));
                  var2.setToKey(var7);
               }

               if (var4.item(var5).getNodeName().equals("keyedReference")) {
                  KeyedReferenceHandler var8 = (KeyedReferenceHandler)var3.makeHandler("keyedReference");
                  KeyedReference var9 = (KeyedReference)var8.create(var4.item(var5));
                  var2.setKeyedReference(var9);
               }
            }
         }

         return var2;
      } else {
         return null;
      }
   }
}
