package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.response.DispositionReport;

public class DispositionReportDOMBinder {
   public static DispositionReport fromDOM(Element var0) {
      DispositionReport var1 = new DispositionReport();
      ListResponseDOMBinder.fromDOM(var1, var0);
      Vector var2 = new Vector();
      NodeList var3 = var0.getElementsByTagName("result");
      int var4 = var3.getLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.add(ResultDOMBinder.fromDOM((Element)var3.item(var5)));
      }

      var1.setResultVector(var2);
      return var1;
   }
}
