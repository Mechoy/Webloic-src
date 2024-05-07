package weblogic.wsee.reliability.faults;

public class SequenceFaultException extends WsrmFaultException {
   private static final long serialVersionUID = -3396683388783245923L;
   private transient SequenceFaultMsg _msg = null;

   public SequenceFaultException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public SequenceFaultException(String var1) {
      super(var1);
   }

   public SequenceFaultException(SequenceFaultMsg var1) {
      super((WsrmFaultMsg)var1);
      this._msg = var1;
   }

   public SequenceFaultMsg getMsg() {
      return this._msg;
   }

   public String getSequenceId() {
      return this._msg != null ? this._msg.getSequenceId() : null;
   }
}
