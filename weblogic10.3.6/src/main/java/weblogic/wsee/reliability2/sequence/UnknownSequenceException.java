package weblogic.wsee.reliability2.sequence;

import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.WseeRmTextTextFormatter;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.exception.WsrmException;

public class UnknownSequenceException extends WsrmException {
   private static final long serialVersionUID = 1L;
   private WsrmConstants.RMVersion _rmVersion;
   private boolean _sourceSide;
   private String _seqId;

   public UnknownSequenceException(WsrmConstants.RMVersion var1, boolean var2, String var3) {
      this((String)null, var1, var2, var3);
   }

   public UnknownSequenceException(String var1, WsrmConstants.RMVersion var2, boolean var3, String var4) {
      super(var1 != null ? var1 : WseeRmLogger.logUnknownSequenceLoggable(var4, var3 ? WseeRmTextTextFormatter.getInstance().sourceSideTerm() : WseeRmTextTextFormatter.getInstance().destinationSideTerm()).getMessage());
      this._rmVersion = var2;
      this._sourceSide = var3;
      this._seqId = var4;
   }

   public WsrmConstants.RMVersion getRmVersion() {
      return this._rmVersion;
   }

   public boolean isSourceSide() {
      return this._sourceSide;
   }

   public String getSequenceId() {
      return this._seqId;
   }
}
