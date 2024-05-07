package weblogic.webservice.binding.jms;

import java.util.Hashtable;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.utils.collections.Pool;

/** @deprecated */
public class JMSConnection {
   private QueueConnectionFactory factory;
   private QueueConnection connection;
   private QueueSession session;
   private QueueSender sender;
   private TextMessage message;
   private TemporaryQueue responseQueue;
   private QueueReceiver receiver;
   private Pool pool;
   private JMSBindingInfo bindingInfo;
   public static final String JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";

   public JMSConnection(JMSBindingInfo var1) throws NamingException, JMSException {
      Hashtable var2 = new Hashtable();
      var2.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
      var2.put("java.naming.provider.url", "t3://" + var1.getHost() + ":" + var1.getPort());
      InitialContext var3 = new InitialContext(var2);
      this.factory = (QueueConnectionFactory)var3.lookup(var1.getFactoryName());
      this.connection = this.factory.createQueueConnection();
      this.session = this.connection.createQueueSession(false, 1);
      Queue var4 = (Queue)var3.lookup(var1.getQueueName());
      this.sender = this.session.createSender(var4);
      this.message = this.session.createTextMessage();
      this.responseQueue = this.session.createTemporaryQueue();
      this.receiver = this.session.createReceiver(this.responseQueue);
      this.connection.start();
   }

   QueueReceiver getReceiver() {
      return this.receiver;
   }

   Queue getResponseQueue() {
      return this.responseQueue;
   }

   public QueueSender getSender() {
      return this.sender;
   }

   public TextMessage getMessage() {
      return this.message;
   }

   void setPool(Pool var1) {
      this.pool = var1;
   }

   void release() {
      this.pool.add(this);
   }

   public void close() throws JMSException {
      this.sender.close();
      this.session.close();
      this.connection.close();
      this.receiver.close();
      this.responseQueue.delete();
   }
}
