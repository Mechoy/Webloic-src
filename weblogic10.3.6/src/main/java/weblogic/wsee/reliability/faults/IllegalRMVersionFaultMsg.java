package weblogic.wsee.reliability.faults;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.wsee.reliability.WsrmConstants;

public class IllegalRMVersionFaultMsg extends SequenceFaultMsg {
   public static final SequenceFaultMsgType TYPE = new SequenceFaultMsgType();
   private List<WsrmConstants.RMVersion> _allowed;
   private WsrmConstants.RMVersion _actual;

   public IllegalRMVersionFaultMsg(WsrmConstants.RMVersion var1) {
      this(var1, new ArrayList());
   }

   public IllegalRMVersionFaultMsg(WsrmConstants.RMVersion var1, List<WsrmConstants.RMVersion> var2) {
      super(var1, WsrmConstants.FaultCode.SENDER, "IllegalRMVersion", getFaultString(var1, var2), TYPE);
      this._actual = var1;
   }

   static String getFaultString(WsrmConstants.RMVersion var0, List<WsrmConstants.RMVersion> var1) {
      return "Illegal WS-RM version found '" + var0 + "'. Allowed versions are: " + getAllowedRMVersionStringFromList(var1);
   }

   private static StringBuffer getAllowedRMVersionStringFromList(List<WsrmConstants.RMVersion> var0) {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         var1.append(var2.next());
         if (var2.hasNext()) {
            var1.append(",");
         }
      }

      return var1;
   }

   public void setAllowed(List<WsrmConstants.RMVersion> var1) {
      this._allowed = var1;
   }

   public List<WsrmConstants.RMVersion> getAllowed() {
      return this._allowed;
   }

   public WsrmConstants.RMVersion getActual() {
      return this._actual;
   }
}
