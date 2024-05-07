package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.exception.UDDIException;

public class UDDIExceptionDOMBinder {
   public static UDDIException fromDOM(Element var0) {
      UDDIException var1 = new UDDIException();
      NodeList var2 = var0.getElementsByTagName("faultcode");
      if (var2.getLength() > 0) {
         var1.setFaultCode(var2.item(0).getFirstChild().getNodeValue());
      }

      NodeList var3 = var0.getElementsByTagName("faultstring");
      if (var3.getLength() > 0) {
         var1.setFaultString(var3.item(0).getFirstChild().getNodeValue());
      }

      NodeList var4 = var0.getElementsByTagName("faultactor");
      if (var4.getLength() > 0) {
         var1.setFaultActor(var4.item(0).getFirstChild().getNodeValue());
      }

      NodeList var5 = var0.getElementsByTagName("detail");
      if (var5.getLength() > 0) {
         Element var6 = (Element)var5.item(0);
         NodeList var7 = var6.getElementsByTagName("dispositionReport");
         if (var7.getLength() > 0) {
            var1.setDispositionReport(DispositionReportDOMBinder.fromDOM((Element)var7.item(0)));
         }
      }

      return var1;
   }
}
