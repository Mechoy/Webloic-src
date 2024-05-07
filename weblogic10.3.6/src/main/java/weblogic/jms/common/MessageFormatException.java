package weblogic.jms.common;

public final class MessageFormatException extends javax.jms.MessageFormatException {
   static final long serialVersionUID = 3080563260492762026L;

   public MessageFormatException(String var1) {
      super(var1);
   }

   public MessageFormatException(String var1, String var2) {
      super(var1, var2);
   }

   public MessageFormatException(String var1, Throwable var2) {
      super(var1);
      JMSException.setLinkedException(this, var2);
   }

   public MessageFormatException(String var1, String var2, Throwable var3) {
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
