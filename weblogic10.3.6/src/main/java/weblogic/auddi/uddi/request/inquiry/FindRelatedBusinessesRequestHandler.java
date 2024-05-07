package weblogic.auddi.uddi.request.inquiry;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BusinessKey;
import weblogic.auddi.uddi.datastructure.BusinessKeyHandler;
import weblogic.auddi.uddi.datastructure.KeyedReference;
import weblogic.auddi.uddi.datastructure.KeyedReferenceHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class FindRelatedBusinessesRequestHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      FindRelatedBusinessesRequest var2 = new FindRelatedBusinessesRequest();
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      NodeList var5 = var1.getChildNodes();
      if (var5 != null) {
         for(int var6 = 0; var6 < var5.getLength(); ++var6) {
            if (var5.item(var6).getNodeName().equals("findQualifiers")) {
               FindQualifiersHandler var7 = (FindQualifiersHandler)var3.makeHandler("findQualifiers");
               FindQualifiers var8 = (FindQualifiers)var7.create(var5.item(var6));
               var2.setFindQualifiers(var8);
            }

            if (var5.item(var6).getNodeName().equals("businessKey")) {
               BusinessKeyHandler var9 = (BusinessKeyHandler)var3.makeHandler("businessKey");
               BusinessKey var11 = (BusinessKey)var9.create(var5.item(var6));
               var2.setBusinessKey(var11);
            }

            if (var5.item(var6).getNodeName().equals("keyedReference")) {
               KeyedReferenceHandler var10 = (KeyedReferenceHandler)var3.makeHandler("keyedReference");
               KeyedReference var12 = (KeyedReference)var10.create(var5.item(var6));
               var2.setKeyedReference(var12);
            }
         }
      }

      return var2;
   }
}
