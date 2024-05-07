package weblogic.jms.common;

public final class MessageNotWriteableException extends javax.jms.MessageNotWriteableException {
   static final long serialVersionUID = -2186712139636772241L;

   public MessageNotWriteableException(String var1) {
      super(var1);
   }

   public MessageNotWriteableException(String var1, String var2) {
      super(var1, var2);
   }

   public MessageNotWriteableException(String var1, Throwable var2) {
      super(var1);
      JMSException.setLinkedException(this, var2);
   }

   public MessageNotWriteableException(String var1, String var2, Throwable var3) {
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
