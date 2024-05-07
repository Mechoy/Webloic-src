package weblogic.jms.common;

public final class InvalidSelectorException extends javax.jms.InvalidSelectorException {
   static final long serialVersionUID = -8059591219768508215L;

   public InvalidSelectorException(String var1) {
      super(var1);
   }

   public InvalidSelectorException(String var1, String var2) {
      super(var1, var2);
   }

   public InvalidSelectorException(String var1, Throwable var2) {
      super(var1);
      JMSException.setLinkedException(this, var2);
   }

   public InvalidSelectorException(String var1, String var2, Throwable var3) {
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
