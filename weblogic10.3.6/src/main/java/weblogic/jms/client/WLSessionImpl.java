package weblogic.jms.client;

import java.io.Serializable;
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
import javax.jms.TransactionRolledBackException;
import org.w3c.dom.Document;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSPushEntry;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.extensions.WLAcknowledgeInfo;
import weblogic.jms.extensions.WLAsyncSession;
import weblogic.jms.extensions.WLMessageProducer;
import weblogic.jms.extensions.XMLMessage;
import weblogic.messaging.dispatcher.CompletionListener;

public class WLSessionImpl extends ReconnectController implements SessionInternal, WLAsyncSession {
   private WLConnectionImpl parent;

   public WLSessionImpl(JMSSession var1, WLConnectionImpl var2) {
      super(var2, var1);
      this.parent = var2;
   }

   protected ReconnectController getParent() {
      return this.parent;
   }

   protected Object getConnectionStateLock() {
      return this.parent.getConnectionStateLock();
   }

   protected WLConnectionImpl getWLConnectionImpl() {
      return this.parent.getWLConnectionImpl();
   }

   protected JMSConnection getPhysicalJMSConnection() {
      return this.parent.getPhysicalJMSConnection();
   }

   protected JMSSession getJMSSessionWaitForState() {
      return (JMSSession)this.getPhysicalWaitForState();
   }

   protected JMSSession getPhysicalJMSSession() {
      return (JMSSession)this.getPhysical();
   }

   public void acknowledge() throws JMSException {
      JMSSession var1 = this.getPhysicalJMSSession();
      var1.throwForAckRefreshedSessionRules();
      var1.acknowledge();
   }

   public void commit() throws JMSException {
      JMSSession var1 = this.getPhysicalJMSSession();
      if (var1.checkRefreshedWithPendingWork()) {
         throw new TransactionRolledBackException(JMSClientExceptionLogger.logLostServerConnectionLoggable().getMessage(), "ReservedRollbackOnly");
      } else {
         var1.commit();
      }
   }

   public void recover() throws JMSException {
      JMSSession var1 = this.getPhysicalJMSSession();
      var1.checkRefreshedWithPendingWork();
      var1.recover();
   }

   public void rollback() throws JMSException {
      JMSSession var1 = this.getPhysicalJMSSession();
      var1.checkRefreshedWithPendingWork();
      var1.rollback();
   }

   public void run() {
      this.getPhysicalJMSSession().run();
   }

   public void acknowledge(Message var1) throws JMSException {
      JMSSession var2 = this.getPhysicalJMSSession();
      var2.throwForAckRefreshedSessionRules();
      var2.acknowledge(var1);
   }

   public void acknowledge(WLAcknowledgeInfo var1) throws JMSException {
      JMSSession var2 = this.getPhysicalJMSSession();
      var2.throwForAckRefreshedSessionRules();
      var2.acknowledge(var1);
   }

   public long getLastSequenceNumber() {
      JMSSession var1 = this.getPhysicalJMSSession();
      return var1.getLastSequenceNumber();
   }

   public void setMMessageListener(MMessageListener var1) {
      JMSSession var2 = this.getPhysicalJMSSession();
      var2.setMMessageListener(var1);
   }

   public void close(long var1) throws JMSException {
      JMSSession var3 = this.getPhysicalJMSSession();
      var3.close(var1);
   }

   public void commit(long var1) throws JMSException {
      JMSSession var3 = this.getPhysicalJMSSession();
      var3.commit(var1);
   }

   public int rollback(long var1) throws JMSException {
      JMSSession var3 = this.getPhysicalJMSSession();
      var3.rollback(var1);
      return var3.getPipelineGenerationFromProxy();
   }

   public int recover(long var1) throws JMSException {
      JMSSession var3 = this.getPhysicalJMSSession();
      var3.recover(var1);
      return var3.getPipelineGenerationFromProxy();
   }

   public void removePendingWTMessage(long var1) throws JMSException {
      JMSSession var3 = this.getPhysicalJMSSession();
      var3.removePendingWTMessage(var1, true);
   }

   public int getPipelineGenerationFromProxy() throws JMSException {
      JMSSession var1 = this.getPhysicalJMSSession();
      return var1.getPipelineGenerationFromProxy();
   }

   public void acknowledge(Message var1, int var2, boolean var3) throws JMSException {
      JMSSession var4 = this.getPhysicalJMSSession();
      var4.throwForAckRefreshedSessionRules();
      var4.acknowledge((MessageImpl)var1, var2, var3);
   }

   public void pushMessage(MessageImpl var1, JMSPushEntry var2) {
      this.getPhysicalJMSSession().pushMessage(var1, var2);
   }

   public void checkSAFClosed() throws JMSException {
      this.getPhysicalJMSSession().checkSAFClosed();
   }

   public int consumersCount() {
      return this.getJMSSessionWaitForState().consumersCount();
   }

   public int producersCount() {
      return this.getJMSSessionWaitForState().producersCount();
   }

   public String getWLSServerName() {
      return this.getJMSSessionWaitForState().getWLSServerName();
   }

   public String getRuntimeMBeanName() {
      return this.getJMSSessionWaitForState().getRuntimeMBeanName();
   }

   public ClientRuntimeInfo getParentInfo() {
      return this.getJMSSessionWaitForState().getParentInfo();
   }

   public JMSID getJMSID() {
      return this.getJMSSessionWaitForState().getJMSID();
   }

   public void setPipelineGeneration(int var1) {
      this.getJMSSessionWaitForState().setPipelineGeneration(var1);
   }

   public XMLMessage createXMLMessage() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSSession var3 = this.getPhysicalJMSSession();

      try {
         return var3.createXMLMessage();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.computeJMSSession(var1, var3, var5).createXMLMessage();
      }
   }

   public XMLMessage createXMLMessage(String var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         return var4.createXMLMessage(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         return this.computeJMSSession(var2, var4, var6).createXMLMessage(var1);
      }
   }

   public XMLMessage createXMLMessage(Document var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         return var4.createXMLMessage(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         return this.computeJMSSession(var2, var4, var6).createXMLMessage(var1);
      }
   }

   public void setExceptionListener(ExceptionListener var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         var4.setExceptionListener(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         this.computeJMSSession(var2, var4, var6).setExceptionListener(var1);
      }

   }

   public int getMessagesMaximum() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSSession var3 = this.getPhysicalJMSSession();

      try {
         return var3.getMessagesMaximum();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.computeJMSSession(var1, var3, var5).getMessagesMaximum();
      }
   }

   public void setMessagesMaximum(int var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         var4.setMessagesMaximum(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         this.computeJMSSession(var2, var4, var6).setMessagesMaximum(var1);
      }

   }

   public int getOverrunPolicy() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSSession var3 = this.getPhysicalJMSSession();

      try {
         return var3.getOverrunPolicy();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.computeJMSSession(var1, var3, var5).getOverrunPolicy();
      }
   }

   public void setOverrunPolicy(int var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         var4.setOverrunPolicy(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         this.computeJMSSession(var2, var4, var6).setOverrunPolicy(var1);
      }

   }

   public long getRedeliveryDelay() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSSession var3 = this.getPhysicalJMSSession();

      try {
         return var3.getRedeliveryDelay();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.computeJMSSession(var1, var3, var5).getRedeliveryDelay();
      }
   }

   public void setRedeliveryDelay(long var1) throws JMSException {
      long var3 = System.currentTimeMillis();
      JMSSession var5 = this.getPhysicalJMSSession();

      try {
         var5.setRedeliveryDelay(var1);
      } catch (weblogic.jms.common.JMSException var7) {
         this.computeJMSSession(var3, var5, var7).setRedeliveryDelay(var1);
      }

   }

   public BytesMessage createBytesMessage() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSSession var3 = this.getPhysicalJMSSession();

      try {
         return var3.createBytesMessage();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.computeJMSSession(var1, var3, var5).createBytesMessage();
      }
   }

   public MapMessage createMapMessage() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSSession var3 = this.getPhysicalJMSSession();

      try {
         return var3.createMapMessage();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.computeJMSSession(var1, var3, var5).createMapMessage();
      }
   }

   public Message createMessage() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSSession var3 = this.getPhysicalJMSSession();

      try {
         return var3.createMessage();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.computeJMSSession(var1, var3, var5).createMessage();
      }
   }

   public ObjectMessage createObjectMessage() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSSession var3 = this.getPhysicalJMSSession();

      try {
         return var3.createObjectMessage();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.computeJMSSession(var1, var3, var5).createObjectMessage();
      }
   }

   public ObjectMessage createObjectMessage(Serializable var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         return var4.createObjectMessage(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         return this.computeJMSSession(var2, var4, var6).createObjectMessage(var1);
      }
   }

   public StreamMessage createStreamMessage() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSSession var3 = this.getPhysicalJMSSession();

      try {
         return var3.createStreamMessage();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.computeJMSSession(var1, var3, var5).createStreamMessage();
      }
   }

   public TextMessage createTextMessage() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSSession var3 = this.getPhysicalJMSSession();

      try {
         return var3.createTextMessage();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.computeJMSSession(var1, var3, var5).createTextMessage();
      }
   }

   public TextMessage createTextMessage(String var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         return var4.createTextMessage(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         return this.computeJMSSession(var2, var4, var6).createTextMessage(var1);
      }
   }

   public boolean getTransacted() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSSession var3 = this.getPhysicalJMSSession();

      try {
         return var3.getTransacted();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.computeJMSSession(var1, var3, var5).getTransacted();
      }
   }

   public int getAcknowledgeMode() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSSession var3 = this.getPhysicalJMSSession();

      try {
         return var3.getAcknowledgeMode();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.computeJMSSession(var1, var3, var5).getAcknowledgeMode();
      }
   }

   public MessageListener getMessageListener() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSSession var3 = this.getPhysicalJMSSession();

      try {
         return var3.getMessageListener();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.computeJMSSession(var1, var3, var5).getMessageListener();
      }
   }

   public void setMessageListener(MessageListener var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         var4.setMessageListener(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         this.computeJMSSession(var2, var4, var6).setMessageListener(var1);
      }

   }

   public MessageProducer createProducer(Destination var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         return var4.createProducer(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         return this.computeJMSSession(var2, var4, var6).createProducer(var1);
      }
   }

   public MessageConsumer createConsumer(Destination var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         return var4.createConsumer(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         return this.computeJMSSession(var2, var4, var6).createConsumer(var1);
      }
   }

   public MessageConsumer createConsumer(Destination var1, String var2) throws JMSException {
      long var3 = System.currentTimeMillis();
      JMSSession var5 = this.getPhysicalJMSSession();

      try {
         return var5.createConsumer(var1, var2);
      } catch (weblogic.jms.common.JMSException var7) {
         return this.computeJMSSession(var3, var5, var7).createConsumer(var1, var2);
      }
   }

   public MessageConsumer createConsumer(Destination var1, String var2, boolean var3) throws JMSException {
      long var4 = System.currentTimeMillis();
      JMSSession var6 = this.getPhysicalJMSSession();

      try {
         return var6.createConsumer(var1, var2, var3);
      } catch (weblogic.jms.common.JMSException var8) {
         return this.computeJMSSession(var4, var6, var8).createConsumer(var1, var2, var3);
      }
   }

   public Queue createQueue(String var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         return var4.createQueue(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         return this.computeJMSSession(var2, var4, var6).createQueue(var1);
      }
   }

   public QueueReceiver createReceiver(Queue var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         return var4.createReceiver(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         return this.computeJMSSession(var2, var4, var6).createReceiver(var1);
      }
   }

   public QueueReceiver createReceiver(Queue var1, String var2) throws JMSException {
      long var3 = System.currentTimeMillis();
      JMSSession var5 = this.getPhysicalJMSSession();

      try {
         return var5.createReceiver(var1, var2);
      } catch (weblogic.jms.common.JMSException var7) {
         return this.computeJMSSession(var3, var5, var7).createReceiver(var1, var2);
      }
   }

   public QueueSender createSender(Queue var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         return var4.createSender(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         return this.computeJMSSession(var2, var4, var6).createSender(var1);
      }
   }

   public QueueBrowser createBrowser(Queue var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         return var4.createBrowser(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         return this.computeJMSSession(var2, var4, var6).createBrowser(var1);
      }
   }

   public QueueBrowser createBrowser(Queue var1, String var2) throws JMSException {
      long var3 = System.currentTimeMillis();
      JMSSession var5 = this.getPhysicalJMSSession();

      try {
         return var5.createBrowser(var1, var2);
      } catch (weblogic.jms.common.JMSException var7) {
         return this.computeJMSSession(var3, var5, var7).createBrowser(var1, var2);
      }
   }

   public TemporaryQueue createTemporaryQueue() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSSession var3 = this.getPhysicalJMSSession();

      try {
         return var3.createTemporaryQueue();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.computeJMSSession(var1, var3, var5).createTemporaryQueue();
      }
   }

   public Topic createTopic(String var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         return var4.createTopic(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         return this.computeJMSSession(var2, var4, var6).createTopic(var1);
      }
   }

   public TopicSubscriber createSubscriber(Topic var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         return var4.createSubscriber(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         return this.computeJMSSession(var2, var4, var6).createSubscriber(var1);
      }
   }

   public TopicSubscriber createSubscriber(Topic var1, String var2, boolean var3) throws JMSException {
      long var4 = System.currentTimeMillis();
      JMSSession var6 = this.getPhysicalJMSSession();

      try {
         return var6.createSubscriber(var1, var2, var3);
      } catch (weblogic.jms.common.JMSException var8) {
         return this.computeJMSSession(var4, var6, var8).createSubscriber(var1, var2, var3);
      }
   }

   public TopicSubscriber createDurableSubscriber(Topic var1, String var2) throws JMSException {
      long var3 = System.currentTimeMillis();
      JMSSession var5 = this.getPhysicalJMSSession();

      try {
         return var5.createDurableSubscriber(var1, var2);
      } catch (weblogic.jms.common.JMSException var7) {
         return this.computeJMSSession(var3, var5, var7).createDurableSubscriber(var1, var2);
      }
   }

   public TopicSubscriber createDurableSubscriber(Topic var1, String var2, String var3, boolean var4) throws JMSException {
      long var5 = System.currentTimeMillis();
      JMSSession var7 = this.getPhysicalJMSSession();

      try {
         return var7.createDurableSubscriber(var1, var2, var3, var4);
      } catch (weblogic.jms.common.JMSException var9) {
         return this.computeJMSSession(var5, var7, var9).createDurableSubscriber(var1, var2, var3, var4);
      }
   }

   public TopicPublisher createPublisher(Topic var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         return var4.createPublisher(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         return this.computeJMSSession(var2, var4, var6).createPublisher(var1);
      }
   }

   public TemporaryTopic createTemporaryTopic() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSSession var3 = this.getPhysicalJMSSession();

      try {
         return var3.createTemporaryTopic();
      } catch (weblogic.jms.common.JMSException var5) {
         return this.computeJMSSession(var1, var3, var5).createTemporaryTopic();
      }
   }

   public void unsubscribe(String var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         var4.unsubscribe(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         this.computeJMSSession(var2, var4, var6).unsubscribe(var1);
      }

   }

   public void unsubscribe(Topic var1, String var2) throws JMSException {
      long var3 = System.currentTimeMillis();
      JMSSession var5 = this.getPhysicalJMSSession();

      try {
         var5.unsubscribe(var1, var2);
      } catch (weblogic.jms.common.JMSException var7) {
         this.computeJMSSession(var3, var5, var7).unsubscribe(var1, var2);
      }

   }

   public void associateTransaction(Message var1) throws JMSException {
      long var2 = System.currentTimeMillis();
      JMSSession var4 = this.getPhysicalJMSSession();

      try {
         var4.associateTransaction(var1);
      } catch (weblogic.jms.common.JMSException var6) {
         this.computeJMSSession(var2, var4, var6).associateTransaction(var1);
      }

   }

   public void acknowledgeAsync(WLAcknowledgeInfo var1, CompletionListener var2) {
      this.getPhysicalJMSSession().acknowledgeAsync(var1, var2);
   }

   public void sendAsync(MessageProducer var1, Message var2, CompletionListener var3) {
      ((WLProducerImpl)var1).sendAsync(var2, var3);
   }

   public void sendAsync(WLMessageProducer var1, Message var2, int var3, int var4, long var5, CompletionListener var7) {
      ((WLProducerImpl)var1).sendAsync(var2, var3, var4, var5, var7);
   }

   public void sendAsync(WLMessageProducer var1, Destination var2, Message var3, CompletionListener var4) {
      ((WLProducerImpl)var1).sendAsync(var2, var3, var4);
   }

   public void sendAsync(WLMessageProducer var1, Destination var2, Message var3, int var4, int var5, long var6, CompletionListener var8) {
      ((WLProducerImpl)var1).sendAsync(var2, var3, var4, var5, var6, var8);
   }

   public void receiveAsync(MessageConsumer var1, CompletionListener var2) {
      ((WLConsumerImpl)var1).receiveAsync(var2);
   }

   public void receiveAsync(MessageConsumer var1, long var2, CompletionListener var4) {
      ((WLConsumerImpl)var1).receiveAsync(var2, var4);
   }

   public void receiveNoWaitAsync(MessageConsumer var1, CompletionListener var2) {
      ((WLConsumerImpl)var1).receiveNoWaitAsync(var2);
   }
}
