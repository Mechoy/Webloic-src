package weblogic.jms.safclient.jms;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSubscriber;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import org.w3c.dom.Document;
import weblogic.jms.common.IllegalStateException;
import weblogic.jms.extensions.JMSMessageFactoryImpl;
import weblogic.jms.extensions.WLAcknowledgeInfo;
import weblogic.jms.extensions.WLMessageFactory;
import weblogic.jms.extensions.WLQueueSession;
import weblogic.jms.extensions.WLTopicSession;
import weblogic.jms.extensions.XMLMessage;
import weblogic.jms.safclient.ClientSAFDelegate;
import weblogic.jms.safclient.agent.AgentManager;
import weblogic.jms.safclient.agent.DestinationImpl;
import weblogic.transaction.ClientTransactionManager;
import weblogic.transaction.TransactionHelper;

public final class SessionImpl implements WLQueueSession, WLTopicSession {
   private static final WLMessageFactory MESSAGEFACTORY = JMSMessageFactoryImpl.getFactory();
   private int id;
   private ConnectionImpl connection;
   private boolean closed = false;
   private ExceptionListener exceptionListener;
   private boolean transacted;
   private int acknowledgeMode;
   private int currentID;
   private HashMap producers = new HashMap();
   private boolean inTx = false;
   private Transaction transaction;

   public SessionImpl(ConnectionImpl var1, int var2, boolean var3, int var4) {
      this.connection = var1;
      this.id = var2;
      this.transacted = var3;
      this.acknowledgeMode = var4;
   }

   public void setExceptionListener(ExceptionListener var1) {
      this.exceptionListener = var1;
   }

   public int getMessagesMaximum() throws JMSException {
      throw new JMSException("No consumers allowed in client SAF");
   }

   public void setMessagesMaximum(int var1) throws JMSException {
      throw new JMSException("No consumers allowed in client SAF");
   }

   public int getOverrunPolicy() throws JMSException {
      throw new JMSException("No consumers allowed in client SAF");
   }

   public void setOverrunPolicy(int var1) throws JMSException {
      throw new JMSException("No consumers allowed in client SAF");
   }

   public long getRedeliveryDelay() throws JMSException {
      throw new JMSException("No consumers allowed in client SAF");
   }

   public void setRedeliveryDelay(long var1) throws JMSException {
      throw new JMSException("No consumers allowed in client SAF");
   }

   public void acknowledge() throws JMSException {
      throw new JMSException("No consumers allowed in the client SAF implementation");
   }

   public void acknowledge(Message var1) throws JMSException {
      throw new JMSException("No consumers allowed in the client SAF implementation");
   }

   public void acknowledge(WLAcknowledgeInfo var1) throws JMSException {
      throw new JMSException("No consumers allowed in the client SAF implementation");
   }

   public Topic createTopic(String var1) throws JMSException {
      DestinationImpl var2 = this.createDestination(var1);
      if (!var2.isTopic()) {
         throw new JMSException(var1 + " is a queue");
      } else {
         return var2;
      }
   }

   private DestinationImpl createDestination(String var1) throws JMSException {
      this.checkClosed();
      StringTokenizer var2 = new StringTokenizer(var1, "!");
      if (var2.countTokens() != 2) {
         throw new JMSException("Invalid format for createDestination.  Must beSAFImportedDestinationsName + '!' + DestinationName");
      } else {
         String var3 = var2.nextToken();
         String var4 = var2.nextToken();
         ClientSAFDelegate var5 = this.connection.getRoot();
         AgentManager var6 = var5.getAgentManager();
         return var6.getDestination(var3, var4);
      }
   }

   public TopicSubscriber createSubscriber(Topic var1) throws JMSException {
      throw new JMSException("No subscriptions allowed in the client SAF implementation");
   }

   public TopicSubscriber createSubscriber(Topic var1, String var2, boolean var3) throws JMSException {
      throw new JMSException("No subscriptions allowed in the client SAF implementation");
   }

   public TopicSubscriber createDurableSubscriber(Topic var1, String var2) throws JMSException {
      throw new JMSException("No subscriptions allowed in the client SAF implementation");
   }

   public TopicSubscriber createDurableSubscriber(Topic var1, String var2, String var3, boolean var4) throws JMSException {
      throw new JMSException("No subscriptions allowed in the client SAF implementation");
   }

   public TopicPublisher createPublisher(Topic var1) throws JMSException {
      return (TopicPublisher)this.createProducer(var1);
   }

   public TemporaryTopic createTemporaryTopic() throws JMSException {
      throw new JMSException("No temporary destination allowed in the client SAF implementation");
   }

   public void unsubscribe(String var1) throws JMSException {
      throw new JMSException("No subscriptions allowed in the client SAF implementation");
   }

   public void unsubscribe(Topic var1, String var2) throws JMSException {
      throw new JMSException("No subscriptions allowed in the client SAF implementation");
   }

   public Queue createQueue(String var1) throws JMSException {
      DestinationImpl var2 = this.createDestination(var1);
      if (!var2.isQueue()) {
         throw new JMSException(var1 + " is a topic");
      } else {
         return var2;
      }
   }

   public QueueReceiver createReceiver(Queue var1) throws JMSException {
      throw new JMSException("No receiver allowed in the client SAF implementation");
   }

   public QueueReceiver createReceiver(Queue var1, String var2) throws JMSException {
      throw new JMSException("No receiver allowed in the client SAF implementation");
   }

   public QueueSender createSender(Queue var1) throws JMSException {
      return (QueueSender)this.createProducer(var1);
   }

   public QueueBrowser createBrowser(Queue var1) throws JMSException {
      throw new JMSException("No browser allowed in the client SAF implementation");
   }

   public QueueBrowser createBrowser(Queue var1, String var2) throws JMSException {
      throw new JMSException("No browser allowed in the client SAF implementation");
   }

   public TemporaryQueue createTemporaryQueue() throws JMSException {
      throw new JMSException("No temporary destination allowed in the client SAF implementation");
   }

   public XMLMessage createXMLMessage() throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createXMLMessage();
   }

   public XMLMessage createXMLMessage(String var1) throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createXMLMessage(var1);
   }

   public XMLMessage createXMLMessage(Document var1) throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createXMLMessage(var1);
   }

   public BytesMessage createBytesMessage() throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createBytesMessage();
   }

   public MapMessage createMapMessage() throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createMapMessage();
   }

   public Message createMessage() throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createMessage();
   }

   public ObjectMessage createObjectMessage() throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createObjectMessage();
   }

   public ObjectMessage createObjectMessage(Serializable var1) throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createObjectMessage(var1);
   }

   public StreamMessage createStreamMessage() throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createStreamMessage();
   }

   public TextMessage createTextMessage() throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createTextMessage();
   }

   public TextMessage createTextMessage(String var1) throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createTextMessage(var1);
   }

   public boolean getTransacted() throws JMSException {
      this.checkClosed();
      return this.transacted;
   }

   public int getAcknowledgeMode() throws JMSException {
      this.checkClosed();
      return this.acknowledgeMode;
   }

   synchronized void beginOrResume(TransactionHelper var1) throws JMSException {
      try {
         ClientTransactionManager var2;
         if (!this.inTx) {
            var2 = var1.getTransactionManager();
            var2.begin();
            this.inTx = true;
         } else if (this.transaction != null) {
            var2 = var1.getTransactionManager();
            var2.resume(this.transaction);
            this.transaction = null;
         }

      } catch (NotSupportedException var3) {
         throw new weblogic.jms.common.JMSException(var3);
      } catch (SystemException var4) {
         throw new weblogic.jms.common.JMSException(var4);
      } catch (InvalidTransactionException var5) {
         throw new weblogic.jms.common.JMSException(var5);
      }
   }

   synchronized void suspend(TransactionHelper var1) throws JMSException {
      if (this.inTx) {
         if (this.transaction == null) {
            ClientTransactionManager var2 = var1.getTransactionManager();

            try {
               this.transaction = var2.suspend();
            } catch (SystemException var4) {
               throw new weblogic.jms.common.JMSException(var4);
            }
         }
      }
   }

   public synchronized void commit() throws JMSException {
      if (!this.transacted) {
         throw new JMSException("This is not a transacted session");
      } else {
         ClientSAFDelegate var1 = this.getRoot();
         TransactionHelper var2 = var1.getTransactionHelper();
         TransactionHelper.pushTransactionHelper(var2);

         try {
            this.beginOrResume(var2);
            ClientTransactionManager var3 = var2.getTransactionManager();

            try {
               var3.commit();
            } catch (RollbackException var11) {
               throw new weblogic.jms.common.JMSException(var11);
            } catch (HeuristicMixedException var12) {
               throw new weblogic.jms.common.JMSException(var12);
            } catch (HeuristicRollbackException var13) {
               throw new weblogic.jms.common.JMSException(var13);
            } catch (SystemException var14) {
               throw new weblogic.jms.common.JMSException(var14);
            }
         } finally {
            this.inTx = false;
            TransactionHelper.popTransactionHelper();
         }

      }
   }

   public synchronized void rollback() throws JMSException {
      if (!this.transacted) {
         throw new JMSException("This is not a transacted session");
      } else {
         ClientSAFDelegate var1 = this.getRoot();
         TransactionHelper var2 = var1.getTransactionHelper();
         TransactionHelper.pushTransactionHelper(var2);

         try {
            this.beginOrResume(var2);
            ClientTransactionManager var3 = var2.getTransactionManager();

            try {
               var3.rollback();
            } catch (SystemException var8) {
               throw new weblogic.jms.common.JMSException(var8);
            }
         } finally {
            this.inTx = false;
            TransactionHelper.popTransactionHelper();
         }

      }
   }

   public synchronized void close() throws JMSException {
      if (!this.closed) {
         this.closed = true;
         synchronized(this.producers) {
            Collection var2 = this.producers.values();
            Iterator var3 = var2.iterator();

            while(true) {
               if (!var3.hasNext()) {
                  this.producers.clear();
                  break;
               }

               MessageProducer var4 = (MessageProducer)var3.next();
               var4.close();
            }
         }

         this.connection.sessionClosed(this.id);
      }
   }

   void preClose(JMSException var1) {
      if (this.exceptionListener != null) {
         try {
            this.exceptionListener.onException(var1);
         } catch (Throwable var5) {
            Throwable var3 = var5;

            for(int var4 = 0; var3 != null; var3 = var3.getCause()) {
               System.out.println("User onException listener threw an exception.  Level " + var4++);
               var3.printStackTrace();
            }
         }
      }

   }

   public void recover() throws JMSException {
      throw new JMSException("Not yet implemented");
   }

   public MessageListener getMessageListener() throws JMSException {
      throw new JMSException("No listener allowed in client SAF implementation");
   }

   public void setMessageListener(MessageListener var1) throws JMSException {
      throw new JMSException("No listener allowed in client SAF implementation");
   }

   public void run() {
   }

   public MessageProducer createProducer(Destination var1) throws JMSException {
      this.checkClosed();
      if (var1 != null && !(var1 instanceof DestinationImpl)) {
         throw new JMSException("The destination passed into the client SAF implementation must be from the file context.  This destination is of type " + var1.getClass().getName());
      } else {
         MessageProducerImpl var2 = new MessageProducerImpl(this, this.currentID, (DestinationImpl)var1);
         synchronized(this.producers) {
            this.producers.put(new Integer(this.currentID), var2);
         }

         ++this.currentID;
         return var2;
      }
   }

   void closeProducer(int var1) {
      Integer var2 = new Integer(var1);
      synchronized(this.producers) {
         this.producers.remove(var2);
      }
   }

   public MessageConsumer createConsumer(Destination var1) throws JMSException {
      throw new JMSException("No consumer allowed in client SAF implementation");
   }

   public MessageConsumer createConsumer(Destination var1, String var2) throws JMSException {
      throw new JMSException("No consumer allowed in client SAF implementation");
   }

   public MessageConsumer createConsumer(Destination var1, String var2, boolean var3) throws JMSException {
      throw new JMSException("No consumer allowed in client SAF implementation");
   }

   private synchronized void checkClosed() throws JMSException {
      if (this.closed) {
         throw new IllegalStateException("The session has been closed");
      }
   }

   String getDefaultTimeToDeliver() {
      return this.connection.getDefaultTimeToDeliver();
   }

   long getSendTimeout() {
      return this.connection.getSendTimeout();
   }

   String getDefaultUnitOfOrder() {
      return this.connection.getDefaultUnitOfOrder();
   }

   int getDefaultCompressionThreshold() {
      return this.connection.getDefaultCompressionThreshold();
   }

   String getDefaultDeliveryMode() {
      return this.connection.getDefaultDeliveryMode();
   }

   int getDefaultPriority() {
      return this.connection.getDefaultPriority();
   }

   long getDefaultTimeToLive() {
      return this.connection.getDefaultTimeToLive();
   }

   boolean getAttachJMSXUserId() {
      return this.connection.getAttachJMSXUserId();
   }

   ClientSAFDelegate getRoot() {
      return this.connection.getRoot();
   }
}
