package weblogic.jms.safclient.jms;

import java.util.HashMap;
import java.util.Iterator;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import weblogic.jms.common.IllegalStateException;
import weblogic.jms.safclient.ClientSAFDelegate;
import weblogic.jms.safclient.jndi.Shutdownable;

public final class ConnectionFactoryImpl implements QueueConnectionFactory, TopicConnectionFactory, Shutdownable {
   private String name;
   private String defaultDeliveryMode = "Persistent";
   private String defaultTimeToDeliver = "0";
   private long defaultTimeToLive = 0L;
   private int defaultPriority = 4;
   private long sendTimeout = 10L;
   private int defaultCompressionThreshold = Integer.MAX_VALUE;
   private String defaultUnitOfOrder = null;
   private String clientID = null;
   private boolean attach = false;
   private ClientSAFDelegate root;
   private HashMap connections = new HashMap();
   private int currentConnectionId;

   public ConnectionFactoryImpl(String var1, ClientSAFDelegate var2) {
      this.name = var1;
      this.root = var2;
   }

   public Connection createConnection() throws JMSException {
      this.checkClosed();
      Integer var1 = new Integer(this.currentConnectionId++);
      ConnectionImpl var2 = new ConnectionImpl(this, var1);
      synchronized(this.connections) {
         this.connections.put(var1, var2);
      }

      if (this.clientID != null) {
         var2.setClientID(this.clientID);
      }

      return var2;
   }

   public Connection createConnection(String var1, String var2) throws JMSException {
      return this.createConnection();
   }

   public TopicConnection createTopicConnection() throws JMSException {
      return (TopicConnection)this.createConnection();
   }

   public TopicConnection createTopicConnection(String var1, String var2) throws JMSException {
      return (TopicConnection)this.createConnection();
   }

   public QueueConnection createQueueConnection() throws JMSException {
      return (QueueConnection)this.createConnection();
   }

   public QueueConnection createQueueConnection(String var1, String var2) throws JMSException {
      return (QueueConnection)this.createConnection();
   }

   void connectionClosed(int var1) {
      synchronized(this.connections) {
         this.connections.remove(new Integer(var1));
      }
   }

   private void checkClosed() throws JMSException {
      if (!this.root.isOpened()) {
         throw new IllegalStateException("The client SAF system is closed");
      }
   }

   public void setJNDIName(String var1) {
   }

   public void setLocalJNDIName(String var1) {
   }

   public void setDefaultDeliveryMode(String var1) {
      this.defaultDeliveryMode = var1;
   }

   String getDefaultDeliveryMode() {
      return this.defaultDeliveryMode;
   }

   public void setDefaultTimeToDeliver(String var1) {
      this.defaultTimeToDeliver = var1;
   }

   String getDefaultTimeToDeliver() {
      return this.defaultTimeToDeliver;
   }

   public void setDefaultTimeToLive(long var1) {
      this.defaultTimeToLive = var1;
   }

   long getDefaultTimeToLive() {
      return this.defaultTimeToLive;
   }

   public void setDefaultPriority(int var1) {
      this.defaultPriority = var1;
   }

   int getDefaultPriority() {
      return this.defaultPriority;
   }

   public void setDefaultRedeliveryDelay(long var1) {
   }

   public void setSendTimeout(long var1) {
      this.sendTimeout = var1;
   }

   long getSendTimeout() {
      return this.sendTimeout;
   }

   public void setDefaultCompressionThreshold(int var1) {
      this.defaultCompressionThreshold = var1;
   }

   int getDefaultCompressionThreshold() {
      return this.defaultCompressionThreshold;
   }

   public void setDefaultUnitOfOrder(String var1) {
      this.defaultUnitOfOrder = var1;
   }

   String getDefaultUnitOfOrder() {
      return this.defaultUnitOfOrder;
   }

   public void setClientId(String var1) {
      this.clientID = var1;
   }

   public void setAcknowledgePolicy(String var1) {
   }

   public void setAllowCloseInOnMessage(boolean var1) {
   }

   public void setMessagesMaximum(int var1) {
   }

   public void setMulticastOverrunPolicy(String var1) {
   }

   public void setSynchronousPrefetchMode(String var1) {
   }

   public void setXAConnectionFactoryEnabled(boolean var1) {
   }

   public void setAttachJMSXUserId(boolean var1) {
      this.attach = var1;
   }

   boolean getAttachJMSXUserId() {
      return this.attach;
   }

   ClientSAFDelegate getRoot() {
      return this.root;
   }

   public void shutdown(JMSException var1) {
      HashMap var2;
      synchronized(this.connections) {
         var2 = (HashMap)this.connections.clone();
      }

      Iterator var3 = var2.keySet().iterator();

      while(var3.hasNext()) {
         ConnectionImpl var4 = (ConnectionImpl)var2.get(var3.next());
         var4.preClose(var1);

         try {
            var4.close();
         } catch (JMSException var6) {
         }
      }

   }

   public String toString() {
      return "ConnectionFactory(" + this.name + "," + System.identityHashCode(this) + ")";
   }
}
