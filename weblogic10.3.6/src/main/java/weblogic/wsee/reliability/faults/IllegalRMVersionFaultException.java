package weblogic.wsee.reliability.faults;

import java.util.Arrays;
import java.util.List;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.exception.WsrmException;

public class IllegalRMVersionFaultException extends WsrmException {
   private static final long serialVersionUID = 5569653629987679780L;
   private String _seqId;
   private List<WsrmConstants.RMVersion> _allowed;
   private WsrmConstants.RMVersion _actual;

   public IllegalRMVersionFaultException(String var1, WsrmConstants.RMVersion var2, List<WsrmConstants.RMVersion> var3) {
      super(IllegalRMVersionFaultMsg.getFaultString(var2, var3));
      this._seqId = var1;
      this._allowed = var3;
      this._actual = var2;
   }

   public IllegalRMVersionFaultException(String var1, WsrmConstants.RMVersion var2, WsrmConstants.RMVersion var3) {
      this(var1, var2, Arrays.asList(var3));
   }

   public String getSequenceId() {
      return this._seqId;
   }

   public List<WsrmConstants.RMVersion> getAllowed() {
      return this._allowed;
   }

   public WsrmConstants.RMVersion getActual() {
      return this._actual;
   }
}
