package weblogic.jms.common;

import weblogic.logging.Loggable;

public final class InvalidDestinationException extends javax.jms.InvalidDestinationException {
   static final long serialVersionUID = 4336734569809868546L;

   public InvalidDestinationException(String var1) {
      super(var1);
   }

   public InvalidDestinationException(Loggable var1) {
      super(var1.getMessage());
   }

   public InvalidDestinationException(String var1, String var2) {
      super(var1, var2);
   }

   public InvalidDestinationException(String var1, Throwable var2) {
      super(var1);
      JMSException.setLinkedException(this, var2);
   }

   public InvalidDestinationException(String var1, String var2, Throwable var3) {
      super(var1, var2);
      JMSException.setLinkedException(this, var3);
   }

   public void setLinkedException(Exception var1) {
      JMSException.setLinkedException(this, var1);
   }

   public Exception getLinkedException() {
      return JMSException.getLinkedException(this);
   }
}
