package weblogic.jms.common;

public final class TransactionInProgressException extends javax.jms.TransactionInProgressException {
   static final long serialVersionUID = -502217799400250711L;

   public TransactionInProgressException(String var1) {
      super(var1);
   }

   public TransactionInProgressException(String var1, String var2) {
      super(var1, var2);
   }

   public TransactionInProgressException(String var1, Throwable var2) {
      super(var1);
      JMSException.setLinkedException(this, var2);
   }

   public TransactionInProgressException(String var1, String var2, Throwable var3) {
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
