package weblogic.jms.client;

import javax.jms.JMSException;
import javax.jms.Message;

public class JMSSystemMessageListenerImpl2 implements JMSSystemMessageListener {
   ConsumerInternal consumer;

   public JMSSystemMessageListenerImpl2(ConsumerInternal var1) {
      this.consumer = var1;
   }

   public void onMessage(Message var1) {
      throw new AssertionError("This should not happen");
   }

   public Message receive(long var1) throws JMSException {
      return this.consumer.getSession().getAsyncMessageForConsumer(this.consumer, var1);
   }
}
