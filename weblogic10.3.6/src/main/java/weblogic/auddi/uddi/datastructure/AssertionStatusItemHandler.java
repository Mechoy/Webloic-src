package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class AssertionStatusItemHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      AssertionStatusItem var2 = new AssertionStatusItem();
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      if (var4.getAttributeNode("completionStatus") != null) {
         Attr var5 = var4.getAttributeNode("completionStatus");
         String var6 = var5.getNodeValue();
         var2.setCompletionStatus(var6);
         if ("status:complete".equals(var6)) {
            var2.setFromKeyAsserted(true);
            var2.setToKeyAsserted(true);
         } else if ("status:fromKey_incomplete".equals(var6)) {
            var2.setFromKeyAsserted(false);
            var2.setToKeyAsserted(true);
         } else if ("status:toKey_incomplete".equals(var6)) {
            var2.setFromKeyAsserted(true);
            var2.setToKeyAsserted(false);
         } else {
            var2.setFromKeyAsserted(false);
            var2.setToKeyAsserted(false);
         }
      }

      NodeList var9 = var4.getChildNodes();
      if (var9 == null) {
         return null;
      } else {
         for(int var10 = 0; var10 < var9.getLength(); ++var10) {
            if (var9.item(var10).getNodeType() != 8) {
               BusinessKeyHandler var7;
               BusinessKey var8;
               if (var9.item(var10).getNodeName().equals("fromKey")) {
                  var7 = (BusinessKeyHandler)var3.makeHandler("businessKey");
                  var8 = (BusinessKey)var7.create(var9.item(var10));
                  var2.setFromKey(var8);
               }

               if (var9.item(var10).getNodeName().equals("toKey")) {
                  var7 = (BusinessKeyHandler)var3.makeHandler("businessKey");
                  var8 = (BusinessKey)var7.create(var9.item(var10));
                  var2.setToKey(var8);
               }

               if (var9.item(var10).getNodeName().equals("keyedReference")) {
                  KeyedReferenceHandler var11 = (KeyedReferenceHandler)var3.makeHandler("keyedReference");
                  KeyedReference var13 = (KeyedReference)var11.create(var9.item(var10));
                  var2.setKeyedReference(var13);
               }

               if (var9.item(var10).getNodeName().equals("keysOwned")) {
                  NodeList var12 = var9.item(var10).getChildNodes();
                  if (var12 != null) {
                     for(int var14 = 0; var14 < var12.getLength(); ++var14) {
                        if (var12.item(var14).getNodeType() != 8) {
                           if (var12.item(var14).getNodeName().equals("fromKey")) {
                              var2.setFromKeyOwnership(true);
                           }

                           if (var12.item(var14).getNodeName().equals("toKey")) {
                              var2.setToKeyOwnership(true);
                           }
                        }
                     }
                  }
               }
            }
         }

         return var2;
      }
   }
}
