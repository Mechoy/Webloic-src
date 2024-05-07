package weblogic.wsee.reliability.faults;

import weblogic.wsee.reliability.WsrmConstants;

public class UnknownSequenceFaultException extends RuntimeException {
   private String _seqId;
   private WsrmConstants.RMVersion _rmVersion;

   public UnknownSequenceFaultException(String var1, WsrmConstants.RMVersion var2) {
      super("The value of wsrm:Identifier is not a known Sequence identifier: " + var1);
      this._seqId = var1;
      this._rmVersion = var2;
   }

   public String getSequenceId() {
      return this._seqId;
   }

   public WsrmConstants.RMVersion getRMVersion() {
      return this._rmVersion;
   }
}
