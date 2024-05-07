package weblogic.jms.common;

import weblogic.logging.Loggable;

public class JMSException extends javax.jms.JMSException {
   static final long serialVersionUID = 8002367427279624380L;

   public JMSException(String var1) {
      super(var1);
   }

   public JMSException(Loggable var1) {
      super(var1.getMessage());
   }

   public JMSException(Throwable var1) {
      super(var1.toString());
      this.initCause(var1);
   }

   public JMSException(Loggable var1, String var2) {
      super(var1.getMessage(), var2);
   }

   public JMSException(String var1, String var2) {
      super(var1, var2);
   }

   public JMSException(Loggable var1, Throwable var2) {
      super(var1.getMessage());
      this.initCause(var2);
   }

   public JMSException(String var1, Throwable var2) {
      super(var1);
      this.initCause(var2);
   }

   public JMSException(Loggable var1, String var2, Throwable var3) {
      super(var1.getMessage(), var2);
      this.initCause(var3);
   }

   public JMSException(String var1, String var2, Throwable var3) {
      super(var1, var2);
      this.initCause(var3);
   }

   public final void setLinkedException(Exception var1) {
      setLinkedException(this, var1);
   }

   public final Exception getLinkedException() {
      return getLinkedException(this);
   }

   static Exception getLinkedException(javax.jms.JMSException var0) {
      try {
         return (Exception)var0.getCause();
      } catch (ClassCastException var2) {
         return new JMSException(var0);
      }
   }

   static void setLinkedException(javax.jms.JMSException var0, Throwable var1) {
      var0.initCause(var1);
   }

   public boolean isInformational() {
      return false;
   }
}
