package weblogic.auddi.uddi.response;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BusinessKey;
import weblogic.auddi.uddi.datastructure.BusinessKeyHandler;
import weblogic.auddi.uddi.datastructure.Name;
import weblogic.auddi.uddi.datastructure.NameHandler;
import weblogic.auddi.uddi.datastructure.ServiceKey;
import weblogic.auddi.uddi.datastructure.ServiceKeyHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class ServiceInfoHandler extends UDDIXMLHandler {
   private static UDDIXMLHandlerMaker maker;
   private ServiceInfo serviceInfo = null;

   public Object create(Node var1) throws UDDIException {
      this.serviceInfo = new ServiceInfo();
      maker = UDDIXMLHandlerMaker.getInstance();
      Element var2 = (Element)var1;
      Attr var3;
      if (var2.getAttributeNode("serviceKey") != null) {
         var3 = var2.getAttributeNode("serviceKey");
         ServiceKeyHandler var4 = (ServiceKeyHandler)maker.makeHandler("serviceKey");
         ServiceKey var5 = (ServiceKey)var4.create(var3);
         this.serviceInfo.setServiceKey(var5);
      }

      if (var2.getAttributeNode("businessKey") != null) {
         var3 = var2.getAttributeNode("businessKey");
         BusinessKeyHandler var8 = (BusinessKeyHandler)maker.makeHandler("businessKey");
         BusinessKey var10 = (BusinessKey)var8.create(var3);
         this.serviceInfo.setBusinessKey(var10);
      }

      NodeList var7 = var1.getChildNodes();
      if (var7 != null) {
         for(int var9 = 0; var9 < var7.getLength(); ++var9) {
            if (var7.item(var9).getNodeName().equals("name")) {
               NameHandler var11 = (NameHandler)maker.makeHandler("name");
               Name var6 = (Name)var11.create(var7.item(var9));
               this.serviceInfo.addName(var6);
            }
         }
      }

      return this.serviceInfo;
   }
}
