package weblogic.wsee.reliability2.sequence;

import weblogic.wsee.reliability2.exception.WsrmException;

public class DuplicateSequenceException extends WsrmException {
   private static final long serialVersionUID = 1L;

   public DuplicateSequenceException(String var1) {
      super(var1);
   }

   public DuplicateSequenceException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
