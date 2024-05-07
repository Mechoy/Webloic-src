package weblogic.wsee.reliability.faults;

import weblogic.wsee.reliability.WsrmConstants;

public class SecurityMismatchFaultMsg extends SequenceFaultMsg {
   public static final SequenceFaultMsgType TYPE = new SequenceFaultMsgType();
   boolean _ssl;

   public SecurityMismatchFaultMsg(WsrmConstants.RMVersion var1) {
      this((String)null, var1, true);
   }

   public SecurityMismatchFaultMsg(String var1, WsrmConstants.RMVersion var2) {
      this(var1, var2, true);
   }

   public SecurityMismatchFaultMsg(String var1, WsrmConstants.RMVersion var2, boolean var3) {
      super(var2, WsrmConstants.FaultCode.SENDER, "SecurityMismatch", getFaultString(var3), TYPE);
      this._ssl = var3;
      this.setSequenceId(var1);
   }

   static String getFaultString(boolean var0) {
      return "Mismatched " + (var0 ? "SSL" : "SCT") + " security information found";
   }

   public boolean isSsl() {
      return this._ssl;
   }

   public boolean isSct() {
      return !this.isSsl();
   }
}
