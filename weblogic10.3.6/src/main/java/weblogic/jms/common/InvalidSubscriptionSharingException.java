package weblogic.jms.common;

import weblogic.logging.Loggable;

public final class InvalidSubscriptionSharingException extends JMSException {
   public InvalidSubscriptionSharingException(String var1) {
      super(var1);
   }

   public InvalidSubscriptionSharingException(Loggable var1) {
      super(var1.getMessage());
   }

   public InvalidSubscriptionSharingException(String var1, String var2) {
      super(var1, var2);
   }

   public InvalidSubscriptionSharingException(String var1, Throwable var2) {
      super(var1);
      JMSException.setLinkedException(this, var2);
   }

   public InvalidSubscriptionSharingException(String var1, String var2, Throwable var3) {
      super(var1, var2);
      JMSException.setLinkedException(this, var3);
   }
}
