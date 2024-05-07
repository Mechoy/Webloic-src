package weblogic.xml.crypto.wss.policy;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.dom.DOMUtils;

public class ClaimsBuilder {
   public static String getClaimFromAttr(Node var0, String var1, String var2) {
      return getClaimFromAttr(var0, new QName(var1), new QName(var2));
   }

   public static String getClaimFromAttr(Node var0, QName var1, String var2) {
      return getClaimFromAttr(var0, var1, new QName(var2));
   }

   public static String getClaimFromAttr(Node var0, QName var1, QName var2) {
      Element var3 = DOMUtils.getFirstElement(var0, var1);
      return var3 != null ? DOMUtils.getAttributeValueAsString(var3, var2) : null;
   }

   public static String getClaimFromElt(Node var0, String var1) {
      return getClaimFromElt(var0, new QName(var1));
   }

   public static String getClaimFromElt(Node var0, QName var1) {
      Element var2 = DOMUtils.getFirstElement(var0, var1);
      return var2 != null ? DOMUtils.getTextContent(var2, true) : null;
   }
}
