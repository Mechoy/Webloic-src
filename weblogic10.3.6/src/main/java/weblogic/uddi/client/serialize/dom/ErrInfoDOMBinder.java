package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.ErrInfo;

public class ErrInfoDOMBinder {
   public static ErrInfo fromDOM(Element var0) {
      return var0.hasAttribute("errCode") ? new ErrInfo(var0.getAttribute("errCode"), TextDOMBinder.fromDOM(var0)) : new ErrInfo((String)null, TextDOMBinder.fromDOM(var0));
   }

   public static Element toDOM(ErrInfo var0, Document var1) {
      Element var2 = TextDOMBinder.toDOM("errInfo", var0.getValue(), var1);
      if (var0.getErrCode() != null) {
         var2.setAttribute("errCode", var0.getErrCode());
      }

      return var2;
   }
}
