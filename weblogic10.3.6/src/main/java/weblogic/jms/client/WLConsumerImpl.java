package weblogic.jms.client;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Topic;
import weblogic.jms.common.JMSID;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.CompletionListener;
import weblogic.utils.expressions.ExpressionEvaluator;

public class WLConsumerImpl extends ReconnectController implements ConsumerInternal {
   private WLSessionImpl parent;
   private Object PROXY_ID_LOCK = new Object();
   private long proxyID;

   public WLConsumerImpl(JMSConsumer var1, WLSessionImpl var2) {
      super(var2, var1);
      this.parent = var2;
   }

   protected ReconnectController getParent() {
      return this.parent;
   }

   Object getConnectionStateLock() {
      return this.parent.getConnectionStateLock();
   }

   protected WLConnectionImpl getWLConnectionImpl() {
      return this.parent.getWLConnectionImpl();
   }

   protected void forget() {
   }

   protected JMSConnection getPhysicalJMSConnection() {
      return this.parent.getPhysicalJMSConnection();
   }

   private JMSConsumer physicalConsumer() {
      return (JMSConsumer)this.getPhysical();
   }

   public String getWLSServerName() {
      return ((ClientRuntimeInfo)this.getPhysicalWaitForState()).getWLSServerName();
   }

   public String getRuntimeMBeanName() {
      return ((ClientRuntimeInfo)this.getPhysicalWaitForState()).getRuntimeMBeanName();
   }

   public ClientRuntimeInfo getParentInfo() {
      return ((ClientRuntimeInfo)this.getPhysicalWaitForState()).getParentInfo();
   }

   public Queue getQueue() throws JMSException {
      return this.physicalConsumer().getQueue();
   }

   public Topic getTopic() throws JMSException {
      return ((JMSConsumer)this.getPhysicalWaitForState()).getTopic();
   }

   public boolean getNoLocal() throws JMSException {
      return ((JMSConsumer)this.getPhysicalWaitForState()).getNoLocal();
   }

   public void decrementWindowCurrent(boolean var1) throws JMSException {
      ((JMSConsumer)this.getPhysicalWaitForState()).decrementWindowCurrent(var1);
   }

   public Destination getDestination() {
      return this.physicalConsumer().getDestination();
   }

   public long getExpectedSequenceNumber() {
      return this.physicalConsumer().getExpectedSequenceNumber();
   }

   public ExpressionEvaluator getExpressionEvaluator() {
      return this.physicalConsumer().getExpressionEvaluator();
   }

   public ID getId() {
      return this.physicalConsumer().getId();
   }

   public JMSID getJMSID() {
      return this.physicalConsumer().getJMSID();
   }

   public JMSMessageContext getMessageListenerContext() {
      return this.physicalConsumer().getMessageListenerContext();
   }

   public JMSSession getSession() {
      return this.physicalConsumer().getSession();
   }

   public int getWindowCurrent() {
      return this.physicalConsumer().getWindowCurrent();
   }

   public int getWindowMaximum() {
      return this.physicalConsumer().getWindowMaximum();
   }

   public boolean isDurable() {
      return this.physicalConsumer().isDurable();
   }

   public boolean privateGetNoLocal() {
      return this.physicalConsumer().privateGetNoLocal();
   }

   public void removeDurableConsumer() {
      this.physicalConsumer().removeDurableConsumer();
   }

   public void setExpectedSequenceNumber(long var1) {
      this.physicalConsumer().setExpectedSequenceNumber(var1);
   }

   public void setExpectedSequenceNumber(long var1, boolean var3) {
      this.physicalConsumer().setExpectedSequenceNumber(var1, var3);
   }

   public final void setClosed(boolean var1) {
      this.physicalConsumer().setClosed(var1);
   }

   public void setId(JMSID var1) {
      this.physicalConsumer().setId(var1);
   }

   public void setRuntimeMBeanName(String var1) {
      this.physicalConsumer().setRuntimeMBeanName(var1);
   }

   public void setWindowCurrent(int var1) {
      this.physicalConsumer().setWindowCurrent(var1);
   }

   public boolean isClosed() {
      return super.isClosed();
   }

   public String getMessageSelector() throws JMSException {
      return this.physicalConsumer().getMessageSelector();
   }

   public MessageListener getMessageListener() throws JMSException {
      return ((JMSConsumer)this.getPhysicalWaitForState()).getMessageListener();
   }

   public void setMessageListener(MessageListener var1) throws JMSException {
      ((JMSConsumer)this.getPhysicalWaitForState()).setMessageListener(var1);
   }

   public void setMessageListener(MessageListener var1, long var2) throws JMSException {
      ((JMSConsumer)this.getPhysicalWaitForState()).setMessageListener(var1, var2);
   }

   public void close(long var1) throws JMSException {
      ((JMSConsumer)this.getPhysicalWaitForState()).close(var1);
   }

   public Message receive() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSConsumer var3 = null;

      try {
         var3 = this.computeJMSConsumer(var1, (Reconnectable)null, (weblogic.jms.common.JMSException)null);
         return var3.receive();
      } catch (weblogic.jms.common.JMSException var5) {
         var3 = this.computeJMSConsumer(var1, var3, var5);
         return var3.receive();
      }
   }

   public Message receive(long var1) throws JMSException {
      long var3 = System.currentTimeMillis();
      JMSConsumer var5 = null;

      try {
         var5 = this.computeJMSConsumer(var3, (Reconnectable)null, (weblogic.jms.common.JMSException)null);
         return var5.receive(var1);
      } catch (weblogic.jms.common.JMSException var7) {
         var5 = this.computeJMSConsumer(var3, var5, var7);
         return var5.receive(var1);
      }
   }

   public Message receiveNoWait() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSConsumer var3 = null;

      try {
         var3 = this.computeJMSConsumer(var1, (Reconnectable)null, (weblogic.jms.common.JMSException)null);
         return var3.receiveNoWait();
      } catch (weblogic.jms.common.JMSException var5) {
         var3 = this.computeJMSConsumer(var1, var3, var5);
         return var3.receiveNoWait();
      }
   }

   public void receiveAsync(CompletionListener var1) {
      try {
         long var2 = System.currentTimeMillis();
         JMSConsumer var4 = null;

         try {
            var4 = this.computeJMSConsumer(var2, (Reconnectable)null, (weblogic.jms.common.JMSException)null);
            var4.receiveInternal(Long.MAX_VALUE, var1);
         } catch (weblogic.jms.common.JMSException var6) {
            var4 = this.computeJMSConsumer(var2, var4, var6);
            var4.receiveInternal(Long.MAX_VALUE, var1);
         }
      } catch (Throwable var7) {
         var1.onException(var7);
      }

   }

   public void receiveAsync(long var1, CompletionListener var3) {
      try {
         long var4 = System.currentTimeMillis();
         JMSConsumer var6 = null;

         try {
            var6 = this.computeJMSConsumer(var4, (Reconnectable)null, (weblogic.jms.common.JMSException)null);
            var6.receiveInternal(var1, var3);
         } catch (weblogic.jms.common.JMSException var8) {
            var6 = this.computeJMSConsumer(var4, var6, var8);
            var6.receiveInternal(var1, var3);
         }
      } catch (Throwable var9) {
         var3.onException(var9);
      }

   }

   public void receiveNoWaitAsync(CompletionListener var1) {
      try {
         long var2 = System.currentTimeMillis();
         JMSConsumer var4 = null;

         try {
            var4 = this.computeJMSConsumer(var2, (Reconnectable)null, (weblogic.jms.common.JMSException)null);
            var4.receiveInternal(9223372036854775806L, var1);
         } catch (weblogic.jms.common.JMSException var6) {
            var4 = this.computeJMSConsumer(var2, var4, var6);
            var4.receiveInternal(9223372036854775806L, var1);
         }
      } catch (Throwable var7) {
         var1.onException(var7);
      }

   }

   public void setProxyID(long var1) {
      synchronized(this.PROXY_ID_LOCK) {
         this.proxyID = var1;
      }
   }

   long getProxyID() {
      synchronized(this.PROXY_ID_LOCK) {
         return this.proxyID;
      }
   }
}
