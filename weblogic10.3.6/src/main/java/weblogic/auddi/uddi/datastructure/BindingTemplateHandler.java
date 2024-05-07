package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class BindingTemplateHandler extends UDDIXMLHandler {
   public Object create(Node var1) throws UDDIException {
      BindingTemplate var2 = new BindingTemplate();
      UDDIXMLHandlerMaker var3 = UDDIXMLHandlerMaker.getInstance();
      Element var4 = (Element)var1;
      Attr var5;
      if (var4.getAttributeNode("bindingKey") != null) {
         var5 = var4.getAttributeNode("bindingKey");
         BindingKeyHandler var6 = (BindingKeyHandler)var3.makeHandler("bindingKey");
         BindingKey var7 = (BindingKey)var6.create(var5);
         var2.setBindingKey(var7);
      }

      if (var4.getAttributeNode("serviceKey") != null) {
         var5 = var4.getAttributeNode("serviceKey");
         ServiceKeyHandler var10 = (ServiceKeyHandler)var3.makeHandler("serviceKey");
         ServiceKey var12 = (ServiceKey)var10.create(var5);
         var2.setServiceKey(var12);
      }

      NodeList var9 = var1.getChildNodes();
      if (var9 != null) {
         for(int var11 = 0; var11 < var9.getLength(); ++var11) {
            if (var9.item(var11).getNodeType() != 8) {
               if (var9.item(var11).getNodeName().equals("description")) {
                  DescriptionHandler var13 = (DescriptionHandler)var3.makeHandler("description");
                  Description var8 = (Description)var13.create(var9.item(var11));
                  var2.addDescription(var8);
               }

               if (var9.item(var11).getNodeName().equals("accessPoint")) {
                  AccessPointHandler var14 = (AccessPointHandler)var3.makeHandler("accessPoint");
                  AccessPoint var15 = (AccessPoint)var14.create(var9.item(var11));
                  var2.setAccessPoint(var15);
               }

               if (var9.item(var11).getNodeName().equals("hostingRedirector")) {
                  HostingRedirectorHandler var16 = (HostingRedirectorHandler)var3.makeHandler("hostingRedirector");
                  HostingRedirector var17 = (HostingRedirector)var16.create(var9.item(var11));
                  var2.setHostingRedirector(var17);
               }

               if (var9.item(var11).getNodeName().equals("tModelInstanceDetails")) {
                  TModelInstanceDetailsHandler var18 = (TModelInstanceDetailsHandler)var3.makeHandler("tModelInstanceDetails");
                  TModelInstanceDetails var19 = (TModelInstanceDetails)var18.create(var9.item(var11));
                  var2.setTModelInstanceDetails(var19);
               }
            }
         }
      }

      return var2;
   }
}
