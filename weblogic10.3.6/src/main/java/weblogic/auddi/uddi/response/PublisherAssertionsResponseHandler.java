package weblogic.auddi.uddi.response;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.Operator;
import weblogic.auddi.uddi.datastructure.OperatorHandler;
import weblogic.auddi.uddi.datastructure.PublisherAssertion;
import weblogic.auddi.uddi.datastructure.PublisherAssertionHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class PublisherAssertionsResponseHandler extends UDDIXMLHandler {
   private static UDDIXMLHandlerMaker maker = null;

   public Object create(Node var1) throws UDDIException {
      PublisherAssertionsResponse var2 = new PublisherAssertionsResponse();
      maker = UDDIXMLHandlerMaker.getInstance();
      Element var3 = (Element)var1;
      Attr var4;
      String var5;
      if (var3.getAttributeNode("truncated") != null) {
         var4 = var3.getAttributeNode("truncated");
         var5 = var4.getNodeValue();
         var2.setTruncated(Boolean.valueOf(var5));
      }

      if (var3.getAttributeNode("authorizedName") != null) {
         var4 = var3.getAttributeNode("authorizedName");
         var5 = var4.getNodeValue();
         var2.setAuthorizedName(var5);
      }

      if (var3.getAttributeNode("operator") != null) {
         var4 = var3.getAttributeNode("operator");
         maker = UDDIXMLHandlerMaker.getInstance();
         OperatorHandler var9 = (OperatorHandler)maker.makeHandler("operator");
         Operator var6 = (Operator)var9.create(var4);
         var2.setOperator(var6);
      }

      NodeList var8 = var1.getChildNodes();
      if (var8 != null) {
         for(int var10 = 0; var10 < var8.getLength(); ++var10) {
            if (var8.item(var10).getNodeName().equals("publisherAssertion")) {
               PublisherAssertionHandler var11 = (PublisherAssertionHandler)maker.makeHandler("publisherAssertion");
               PublisherAssertion var7 = (PublisherAssertion)var11.create(var8.item(var10));
               var2.addPublisherAssertion(var7);
            }
         }
      }

      return var2;
   }
}
