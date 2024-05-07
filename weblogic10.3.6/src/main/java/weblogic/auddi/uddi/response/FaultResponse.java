package weblogic.auddi.uddi.response;

import org.w3c.dom.Node;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.soap.FaultWrapper;
import weblogic.auddi.util.Util;

public class FaultResponse extends UDDIResponse {
   FaultWrapper m_fault = null;

   public FaultWrapper getFault() {
      return this.m_fault;
   }

   public DispositionReportResponse getDisposition() {
      return this.m_fault.getDisposition();
   }

   public FaultResponse(Node var1) throws UDDIException {
      this.m_fault = new FaultWrapper(var1);
   }

   FaultResponse(UDDIException var1, String var2, String var3) throws UDDIException {
      this.m_fault = new FaultWrapper(var1, var2, var3);
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof FaultResponse)) {
         return false;
      } else {
         FaultResponse var2 = (FaultResponse)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_fault, (Object)var2.m_fault);
         return var3;
      }
   }

   public String toXML() {
      return this.m_fault.toXML();
   }
}
