package weblogic.wsee.wstx.wsc.common;

import com.sun.xml.ws.util.DOMUtil;
import org.w3c.dom.Element;
import weblogic.wsee.wstx.wsat.WSATHelper;

public class WSCUtil {
   public static Element referenceElementTxId(String var0) {
      Element var1 = DOMUtil.createDom().createElementNS("http://weblogic.wsee.wstx.wsat/ws/2008/10/wsat", "wls-wsat:txId");
      var1.setTextContent(var0);
      return var1;
   }

   public static Element referenceElementBranchQual(String var0) {
      Element var1 = DOMUtil.createDom().createElementNS("http://weblogic.wsee.wstx.wsat/ws/2008/10/wsat", "wls-wsat:branchQual");
      var1.setTextContent(var0);
      return var1;
   }

   public static Element referenceElementRoutingInfo() {
      String var0 = WSATHelper.getInstance().getRoutingAddress();
      Element var1 = DOMUtil.createDom().createElementNS("http://weblogic.wsee.wstx.wsat/ws/2008/10/wsat", "wls-wsat:routing");
      var1.setTextContent(var0);
      return var1;
   }
}
