package weblogic.jms.client;

import javax.jms.MessageListener;

public class JMSMessageContext extends JMSContext {
   private MessageListener listener = null;

   public JMSMessageContext() {
   }

   public JMSMessageContext(MessageListener var1) {
      if (var1 != null) {
         this.setClassLoader(var1.getClass().getClassLoader());
      }

      this.listener = var1;
   }

   public JMSMessageContext(MessageListener var1, ClassLoader var2) {
      this.setClassLoader(var2);
      this.listener = var1;
   }

   public void setMessageListener(MessageListener var1) {
      this.listener = var1;
   }

   public MessageListener getMessageListener() {
      return this.listener;
   }
}
