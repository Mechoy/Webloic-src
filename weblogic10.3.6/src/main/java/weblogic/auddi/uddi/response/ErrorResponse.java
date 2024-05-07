package weblogic.auddi.uddi.response;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.util.Util;

public class ErrorResponse extends FaultResponse {
   public ErrorResponse(Node var1) throws UDDIException {
      super(new FatalErrorException(getFaultContent(var1, "faultstring")), getFaultContent(var1, "faultcode"), getFaultContent(var1, "faultstring"));
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof ErrorResponse)) {
         return false;
      } else {
         ErrorResponse var2 = (ErrorResponse)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_fault, (Object)var2.m_fault);
         return var3;
      }
   }

   private static String getFaultContent(Node var0, String var1) {
      NodeList var2 = ((Element)var0).getElementsByTagNameNS("*", var1);
      int var3 = 0;

      for(int var4 = var2.getLength(); var3 < var4; ++var3) {
         Node var5 = var2.item(var3);
         if (var5.getNodeType() == 1) {
            return var5.getFirstChild().getNodeValue();
         }
      }

      return null;
   }
}
