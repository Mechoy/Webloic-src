package weblogic.jms.common;

import weblogic.logging.Loggable;

public final class OutOfSequenceRangeException extends JMSException {
   static final long serialVersionUID = 2320412392366430684L;

   public OutOfSequenceRangeException(Loggable var1) {
      super(var1);
   }

   /** @deprecated */
   public OutOfSequenceRangeException(String var1) {
      super(var1);
   }

   public OutOfSequenceRangeException(Loggable var1, String var2) {
      super(var1, var2);
   }

   /** @deprecated */
   public OutOfSequenceRangeException(String var1, String var2) {
      super(var1, var2);
   }

   public OutOfSequenceRangeException(Loggable var1, Throwable var2) {
      super(var1.getMessage(), var2);
   }

   /** @deprecated */
   public OutOfSequenceRangeException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public OutOfSequenceRangeException(Loggable var1, String var2, Throwable var3) {
      super(var1, var2, var3);
   }

   /** @deprecated */
   public OutOfSequenceRangeException(String var1, String var2, Throwable var3) {
      super(var1, var2, var3);
   }
}
