package weblogic.jms.common;

import weblogic.logging.Loggable;

public final class BadSequenceNumberException extends JMSException {
   static final long serialVersionUID = 9127369707336683070L;

   public BadSequenceNumberException(Loggable var1) {
      super(var1);
   }

   /** @deprecated */
   public BadSequenceNumberException(String var1) {
      super(var1);
   }

   public BadSequenceNumberException(Loggable var1, String var2) {
      super(var1, var2);
   }

   /** @deprecated */
   public BadSequenceNumberException(String var1, String var2) {
      super(var1, var2);
   }

   public BadSequenceNumberException(Loggable var1, Throwable var2) {
      super(var1.getMessage(), var2);
   }

   /** @deprecated */
   public BadSequenceNumberException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public BadSequenceNumberException(Loggable var1, String var2, Throwable var3) {
      super(var1, var2, var3);
   }

   /** @deprecated */
   public BadSequenceNumberException(String var1, String var2, Throwable var3) {
      super(var1, var2, var3);
   }
}
