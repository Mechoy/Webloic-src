package weblogic.jms.common;

import weblogic.logging.Loggable;

public class IllegalStateException extends javax.jms.IllegalStateException {
   static final long serialVersionUID = 2934913896351092779L;

   public IllegalStateException(Loggable var1) {
      super(var1.getMessage());
   }

   /** @deprecated */
   public IllegalStateException(String var1) {
      super(var1);
   }

   public IllegalStateException(Loggable var1, String var2) {
      super(var1.getMessage(), var2);
   }

   /** @deprecated */
   public IllegalStateException(String var1, String var2) {
      super(var1, var2);
   }

   public IllegalStateException(Loggable var1, Throwable var2) {
      super(var1.getMessage());
      JMSException.setLinkedException(this, var2);
   }

   /** @deprecated */
   public IllegalStateException(String var1, Throwable var2) {
      super(var1);
      JMSException.setLinkedException(this, var2);
   }

   public IllegalStateException(Loggable var1, String var2, Throwable var3) {
      super(var1.getMessage(), var2);
      JMSException.setLinkedException(this, var3);
   }

   /** @deprecated */
   public IllegalStateException(String var1, String var2, Throwable var3) {
      super(var1, var2);
      JMSException.setLinkedException(this, var3);
   }

   public void setLinkedException(Exception var1) {
      super.setLinkedException(var1);
      JMSException.setLinkedException(this, var1);
   }

   public Exception getLinkedException() {
      return JMSException.getLinkedException(this);
   }
}
