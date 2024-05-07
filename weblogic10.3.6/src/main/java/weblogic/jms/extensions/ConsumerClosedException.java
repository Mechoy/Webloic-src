package weblogic.jms.extensions;

import javax.jms.MessageConsumer;
import weblogic.jms.common.JMSException;

public final class ConsumerClosedException extends JMSException {
   static final long serialVersionUID = -1819873727815556850L;
   private MessageConsumer consumer;

   public ConsumerClosedException(MessageConsumer var1, String var2, String var3) {
      super(var2, var3);
      this.consumer = var1;
   }

   public ConsumerClosedException(MessageConsumer var1, String var2) {
      super(var2);
      this.consumer = var1;
   }

   public MessageConsumer getConsumer() {
      return this.consumer;
   }

   public void setConsumer(MessageConsumer var1) {
      this.consumer = var1;
   }
}
