package weblogic.jms.common;

public final class InvalidClientIDException extends javax.jms.InvalidClientIDException {
   static final long serialVersionUID = 7405762579928968370L;

   public InvalidClientIDException(String var1) {
      super(var1);
   }

   public InvalidClientIDException(String var1, String var2) {
      super(var1, var2);
   }

   public InvalidClientIDException(String var1, Throwable var2) {
      super(var1);
      JMSException.setLinkedException(this, var2);
   }

   public InvalidClientIDException(String var1, String var2, Throwable var3) {
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
