package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.ServiceInfo;
import weblogic.uddi.client.structures.datatypes.ServiceInfos;

public class ServiceInfosDOMBinder {
   public static ServiceInfos fromDOM(Element var0) {
      ServiceInfos var1 = new ServiceInfos();
      Vector var2 = new Vector();
      NodeList var3 = var0.getElementsByTagName("serviceInfo");
      int var4 = var3.getLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.add(ServiceInfoDOMBinder.fromDOM((Element)var3.item(var5)));
      }

      var1.setServiceInfoVector(var2);
      return var1;
   }

   public static Element toDOM(ServiceInfos var0, Document var1) {
      Element var2 = var1.createElement("serviceInfos");
      Vector var3 = var0.getServiceInfoVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(ServiceInfoDOMBinder.toDOM((ServiceInfo)var3.elementAt(var4), var1));
      }

      return var2;
   }
}
