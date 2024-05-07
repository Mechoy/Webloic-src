package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.Result;

public class ResultDOMBinder {
   public static Result fromDOM(Element var0) {
      Result var1 = new Result();
      if (var0.hasAttribute("keyType")) {
         var1.setKeyType(var0.getAttribute("keyType"));
      }

      if (var0.hasAttribute("errno")) {
         var1.setErrno(var0.getAttribute("errno"));
      }

      NodeList var2 = var0.getElementsByTagName("errInfo");
      if (var2.getLength() > 0) {
         var1.setErrInfo(ErrInfoDOMBinder.fromDOM((Element)var2.item(0)));
      }

      return var1;
   }

   public static Element toDOM(Result var0, Document var1) {
      Element var2 = var1.createElement("result");
      if (var0.getKeyType() != null) {
         var2.setAttribute("keyType", var0.getKeyType());
      }

      if (var0.getErrno() != null) {
         var2.setAttribute("errno", var0.getErrno());
      }

      if (var0.getErrInfo() != null) {
         var2.appendChild(ErrInfoDOMBinder.toDOM(var0.getErrInfo(), var1));
      }

      return var2;
   }
}
