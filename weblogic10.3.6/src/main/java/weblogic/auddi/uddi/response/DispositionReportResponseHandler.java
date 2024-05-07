package weblogic.auddi.uddi.response;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.Operator;
import weblogic.auddi.uddi.datastructure.OperatorHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandler;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;

public class DispositionReportResponseHandler extends UDDIXMLHandler {
   private static UDDIXMLHandlerMaker maker = null;

   private DispositionReportResponse getDispositionReport(Node var1) throws UDDIException {
      if (maker == null) {
         maker = UDDIXMLHandlerMaker.getInstance();
      }

      Object var2 = null;
      NodeList var3 = var1.getChildNodes();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.getLength(); ++var4) {
            if (var3.item(var4).getNodeName().equals("result")) {
               ResultHandler var5 = (ResultHandler)maker.makeHandler("result");
               Result var6 = (Result)var5.create(var3.item(var4));
               if (var6.getErrno() == 0) {
                  var2 = new SuccessDispositionReportResponse();
               }
            }
         }

         if (var2 == null) {
            var2 = new ErrorDispositionReportResponse();
         }
      }

      return (DispositionReportResponse)var2;
   }

   public Object create(Node var1) throws UDDIException {
      maker = UDDIXMLHandlerMaker.getInstance();
      DispositionReportResponse var2 = this.getDispositionReport(var1);
      Element var3 = (Element)var1;
      if (var3.getAttributeNode("operator") != null) {
         Attr var4 = var3.getAttributeNode("operator");
         OperatorHandler var5 = (OperatorHandler)maker.makeHandler("operator");
         Operator var6 = (Operator)var5.create(var4);
         var2.setOperator(var6);
      }

      NodeList var8 = var1.getChildNodes();
      if (var8 != null) {
         for(int var9 = 0; var9 < var8.getLength(); ++var9) {
            if (var8.item(var9).getNodeName().equals("result")) {
               ResultHandler var10 = (ResultHandler)maker.makeHandler("result");
               Result var7 = (Result)var10.create(var8.item(var9));
               var2.addResult(var7);
            }
         }
      }

      return var2;
   }
}
