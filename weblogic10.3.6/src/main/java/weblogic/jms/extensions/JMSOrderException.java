package weblogic.jms.extensions;

import java.io.Serializable;
import weblogic.jms.common.JMSException;

public class JMSOrderException extends JMSException {
   Serializable member;

   public JMSOrderException(String var1) {
      super(var1);
   }

   public JMSOrderException(String var1, String var2) {
      super(var1, var2);
   }

   public JMSOrderException(String var1, Throwable var2) {
      super(var1);
      this.initCause(var2);
   }

   public void setMember(Serializable var1) {
      this.member = var1;
   }

   public Serializable getMember() {
      return this.member;
   }
}
