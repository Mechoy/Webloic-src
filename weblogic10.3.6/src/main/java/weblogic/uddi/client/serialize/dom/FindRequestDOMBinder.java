package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.request.FindRequest;

public class FindRequestDOMBinder {
   public static Element toDOM(String var0, FindRequest var1, Document var2) {
      Element var3 = RequestDOMBinder.toDOM(var0, var1, var2);
      if (var1.getMaxRows() != 0) {
         var3.setAttribute("maxRows", "" + var1.getMaxRows());
      }

      if (var1.getFindQualifiers() != null) {
         var3.appendChild(FindQualifiersDOMBinder.toDOM(var1.getFindQualifiers(), var2));
      }

      return var3;
   }
}
