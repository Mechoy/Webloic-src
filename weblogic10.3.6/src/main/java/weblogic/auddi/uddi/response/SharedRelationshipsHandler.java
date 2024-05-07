package weblogic.auddi.uddi.response;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.KeyedReference;
import weblogic.auddi.uddi.datastructure.KeyedReferenceHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class SharedRelationshipsHandler extends UDDIXMLHandler {
   private static UDDIXMLHandlerMaker maker = null;
   private SharedRelationships sharedRelationships = null;

   public Object create(Node var1) throws UDDIException {
      this.sharedRelationships = new SharedRelationships();
      maker = UDDIXMLHandlerMaker.getInstance();
      Element var2 = (Element)var1;
      NodeList var3 = var1.getChildNodes();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.getLength(); ++var4) {
            if (var3.item(var4).getNodeName().equals("keyedReference")) {
               KeyedReferenceHandler var5 = (KeyedReferenceHandler)maker.makeHandler("keyedReference");
               KeyedReference var6 = (KeyedReference)var5.create(var3.item(var4));
               this.sharedRelationships.add(var6);
            }
         }
      }

      Node var7 = var1.getAttributes().getNamedItem("direction");
      if (var7 != null) {
         this.sharedRelationships.setDirection(var7.getNodeValue().trim());
      }

      return this.sharedRelationships;
   }
}
