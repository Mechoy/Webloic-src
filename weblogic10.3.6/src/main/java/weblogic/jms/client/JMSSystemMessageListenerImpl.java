package weblogic.jms.client;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import javax.jms.JMSException;
import javax.jms.Message;

public class JMSSystemMessageListenerImpl implements JMSSystemMessageListener {
   private LinkedList messages = new LinkedList();

   public void onMessage(Message var1) {
      this.addMessage(var1);
   }

   public Message receive(long var1) throws JMSException {
      synchronized(this) {
         return this.removeMessage();
      }
   }

   private synchronized void addMessage(Message var1) {
      this.messages.add(var1);
      this.notify();
   }

   private synchronized Message removeMessage() {
      try {
         return (Message)this.messages.remove();
      } catch (NoSuchElementException var2) {
         return null;
      }
   }
}
