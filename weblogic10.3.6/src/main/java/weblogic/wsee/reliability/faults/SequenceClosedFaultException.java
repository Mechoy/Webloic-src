package weblogic.wsee.reliability.faults;

public class SequenceClosedFaultException extends SequenceFaultException {
   private static final long serialVersionUID = 1L;

   public SequenceClosedFaultException(SequenceClosedFaultMsg var1) {
      super((SequenceFaultMsg)var1);
   }
}
