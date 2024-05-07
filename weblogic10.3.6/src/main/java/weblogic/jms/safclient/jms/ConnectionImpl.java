package weblogic.jms.safclient.jms;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import weblogic.jms.common.IllegalStateException;
import weblogic.jms.safclient.ClientSAFDelegate;

public final class ConnectionImpl implements QueueConnection, TopicConnection {
   private ConnectionFactoryImpl connectionFactory;
   private HashMap sessions = new HashMap();
   private int currentSessionId;
   private int id;
   private boolean closed = false;
   private boolean started = false;
   private ExceptionListener listener;
   private String clientID;

   ConnectionImpl(ConnectionFactoryImpl var1, int var2) {
      this.connectionFactory = var1;
      this.id = var2;
   }

   public synchronized QueueSession createQueueSession(boolean var1, int var2) throws JMSException {
      return (QueueSession)this.createSession(var1, var2);
   }

   public ConnectionConsumer createConnectionConsumer(Queue var1, String var2, ServerSessionPool var3, int var4) throws JMSException {
      throw new JMSException("No consumer allowed in client SAF implementation");
   }

   public TopicSession createTopicSession(boolean var1, int var2) throws JMSException {
      return (TopicSession)this.createSession(var1, var2);
   }

   public ConnectionConsumer createConnectionConsumer(Topic var1, String var2, ServerSessionPool var3, int var4) throws JMSException {
      throw new JMSException("No consumer allowed in client SAF implementation");
   }

   public ConnectionConsumer createDurableConnectionConsumer(Topic var1, String var2, String var3, ServerSessionPool var4, int var5) throws JMSException {
      throw new JMSException("No consumer allowed in client SAF implementation");
   }

   public Session createSession(boolean var1, int var2) throws JMSException {
      this.checkClosed();
      SessionImpl var3 = new SessionImpl(this, this.currentSessionId, var1, var2);
      synchronized(this.sessions) {
         this.sessions.put(new Integer(this.currentSessionId), var3);
      }

      ++this.currentSessionId;
      return var3;
   }

   public String getClientID() throws JMSException {
      this.checkClosed();
      return this.clientID;
   }

   public void setClientID(String var1) throws JMSException {
      this.checkClosed();
      if (this.started) {
         throw new JMSException("ClientID cannot be set once the connection is started");
      } else {
         this.clientID = var1;
      }
   }

   public ConnectionMetaData getMetaData() {
      return new MetaData();
   }

   public ExceptionListener getExceptionListener() throws JMSException {
      this.checkClosed();
      return this.listener;
   }

   public void setExceptionListener(ExceptionListener var1) throws JMSException {
      this.checkClosed();
      this.listener = var1;
   }

   public void start() throws JMSException {
      this.checkClosed();
      this.started = true;
   }

   public void stop() throws JMSException {
      this.checkClosed();
      this.started = false;
   }

   public synchronized void close() throws JMSException {
      if (!this.closed) {
         this.closed = true;
         synchronized(this.sessions) {
            Collection var2 = this.sessions.values();
            Iterator var3 = var2.iterator();

            while(true) {
               if (!var3.hasNext()) {
                  this.sessions.clear();
                  break;
               }

               Session var4 = (Session)var3.next();
               var4.close();
            }
         }

         this.connectionFactory.connectionClosed(this.id);
      }
   }

   void preClose(JMSException var1) {
      HashMap var2;
      synchronized(this.sessions) {
         var2 = (HashMap)this.sessions.clone();
      }

      Iterator var3 = var2.values().iterator();

      while(var3.hasNext()) {
         SessionImpl var4 = (SessionImpl)var3.next();
         var4.preClose(var1);
      }

      if (this.listener != null) {
         try {
            this.listener.onException(var1);
         } catch (Throwable var8) {
            Throwable var5 = var8;

            for(int var6 = 0; var5 != null; var5 = var5.getCause()) {
               System.out.println("User onException listener threw an exception.  Level " + var6++);
               var5.printStackTrace();
            }
         }
      }

   }

   public ConnectionConsumer createConnectionConsumer(Destination var1, String var2, ServerSessionPool var3, int var4) throws JMSException {
      throw new JMSException("No consumer allowed in client SAF implementation");
   }

   void sessionClosed(int var1) {
      synchronized(this.sessions) {
         this.sessions.remove(new Integer(var1));
      }
   }

   private void checkClosed() throws JMSException {
      if (this.closed) {
         throw new IllegalStateException("The connection has been closed");
      }
   }

   String getDefaultTimeToDeliver() {
      return this.connectionFactory.getDefaultTimeToDeliver();
   }

   long getSendTimeout() {
      return this.connectionFactory.getSendTimeout();
   }

   String getDefaultUnitOfOrder() {
      return this.connectionFactory.getDefaultUnitOfOrder();
   }

   int getDefaultCompressionThreshold() {
      return this.connectionFactory.getDefaultCompressionThreshold();
   }

   String getDefaultDeliveryMode() {
      return this.connectionFactory.getDefaultDeliveryMode();
   }

   int getDefaultPriority() {
      return this.connectionFactory.getDefaultPriority();
   }

   long getDefaultTimeToLive() {
      return this.connectionFactory.getDefaultTimeToLive();
   }

   boolean getAttachJMSXUserId() {
      return this.connectionFactory.getAttachJMSXUserId();
   }

   ClientSAFDelegate getRoot() {
      return this.connectionFactory.getRoot();
   }

   public String toString() {
      return "ConnectionImpl(" + this.id + ")";
   }

   private static class MetaData implements ConnectionMetaData {
      private static final String JMS_PROVIDER_NAME = "BEA Systems, Inc.";
      private static final String PROVIDER_VERSION = "9.0.2";

      private MetaData() {
      }

      public String getJMSVersion() throws JMSException {
         return "1.0.2b";
      }

      public int getJMSMajorVersion() throws JMSException {
         return 1;
      }

      public int getJMSMinorVersion() throws JMSException {
         return 1;
      }

      public String getJMSProviderName() throws JMSException {
         return "BEA Systems, Inc.";
      }

      public String getProviderVersion() throws JMSException {
         return "9.0.2";
      }

      public int getProviderMajorVersion() throws JMSException {
         return 9;
      }

      public int getProviderMinorVersion() throws JMSException {
         return 2;
      }

      public Enumeration getJMSXPropertyNames() throws JMSException {
         Vector var1 = new Vector();
         var1.add("JMSXDeliveryCount");
         var1.add("JMSXGroupID");
         var1.add("JMSXGroupSeq");
         var1.add("JMSXUserID");
         return var1.elements();
      }

      // $FF: synthetic method
      MetaData(Object var1) {
         this();
      }
   }
}
