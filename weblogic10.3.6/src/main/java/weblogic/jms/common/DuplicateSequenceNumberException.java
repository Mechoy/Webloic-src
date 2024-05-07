package weblogic.jms.common;

import weblogic.logging.Loggable;

public final class DuplicateSequenceNumberException extends JMSException {
   static final long serialVersionUID = 5131149317928539158L;

   public DuplicateSequenceNumberException(Loggable var1) {
      super(var1);
   }

   /** @deprecated */
   public DuplicateSequenceNumberException(String var1) {
      super(var1);
   }

   public DuplicateSequenceNumberException(Loggable var1, String var2) {
      super(var1, var2);
   }

   /** @deprecated */
   public DuplicateSequenceNumberException(String var1, String var2) {
      super(var1, var2);
   }

   public DuplicateSequenceNumberException(Loggable var1, Throwable var2) {
      super(var1.getMessage(), var2);
   }

   /** @deprecated */
   public DuplicateSequenceNumberException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public DuplicateSequenceNumberException(Loggable var1, String var2, Throwable var3) {
      super(var1, var2, var3);
   }

   /** @deprecated */
   public DuplicateSequenceNumberException(String var1, String var2, Throwable var3) {
      super(var1, var2, var3);
   }
}
