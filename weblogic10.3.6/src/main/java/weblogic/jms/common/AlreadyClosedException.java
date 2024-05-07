package weblogic.jms.common;

import weblogic.logging.Loggable;

public final class AlreadyClosedException extends IllegalStateException {
   static final long serialVersionUID = 2934913896305192779L;

   public AlreadyClosedException(Loggable var1) {
      super(var1.getMessage());
   }

   /** @deprecated */
   public AlreadyClosedException(String var1) {
      super(var1);
   }

   public AlreadyClosedException(Loggable var1, String var2) {
      super(var1.getMessage(), var2);
   }

   /** @deprecated */
   public AlreadyClosedException(String var1, String var2) {
      super(var1, var2);
   }

   public AlreadyClosedException(Loggable var1, Throwable var2) {
      super(var1.getMessage());
      JMSException.setLinkedException(this, var2);
   }

   /** @deprecated */
   public AlreadyClosedException(String var1, Throwable var2) {
      super(var1);
      JMSException.setLinkedException(this, var2);
   }

   public AlreadyClosedException(Loggable var1, String var2, Throwable var3) {
      super(var1.getMessage(), var2);
      JMSException.setLinkedException(this, var3);
   }

   /** @deprecated */
   public AlreadyClosedException(String var1, String var2, Throwable var3) {
      super(var1, var2);
      JMSException.setLinkedException(this, var3);
   }
}
