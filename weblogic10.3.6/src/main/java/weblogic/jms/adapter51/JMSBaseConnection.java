package weblogic.jms.adapter51;

import com.bea.interop.jms101_jms110.TopicConnectionFactory;
import com.bea.interop.jms101_jms110.TopicConnectionFactoryFrom110;
import java.lang.reflect.Constructor;
import java.util.Hashtable;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.jms.TransactionRolledBackException;
import javax.jms.XAConnectionFactory;
import javax.jms.XAQueueConnection;
import javax.jms.XAQueueConnectionFactory;
import javax.jms.XAQueueSession;
import javax.jms.XASession;
import javax.jms.XATopicConnection;
import javax.jms.XATopicConnectionFactory;
import javax.jms.XATopicSession;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.transaction.xa.XAResource;
import weblogic.connector.exception.NoEnlistXAResourceException;
import weblogic.jms.bridge.AdapterConnectionMetaData;
import weblogic.jms.bridge.GenericMessage;
import weblogic.jms.bridge.LocalTransaction;
import weblogic.jms.bridge.NotificationListener;
import weblogic.jms.bridge.ResourceTransactionRolledBackException;
import weblogic.jms.bridge.SourceConnection;
import weblogic.jms.bridge.TargetConnection;
import weblogic.jms.bridge.internal.BridgeDebug;
import weblogic.jms.client.JMSXASession;
import weblogic.jms.extensions.MDBTransaction;
import weblogic.transaction.internal.IgnoreXAResource;

public class JMSBaseConnection implements SourceConnection, TargetConnection, MessageListener, ExceptionListener {
   private JMSManagedConnectionFactory mcf;
   private JMSManagedConnection mc;
   protected ConnectionFactory cf;
   protected Connection connection;
   protected XASession xaSession;
   protected Session session;
   protected Destination destination;
   private MessageProducer messageProducer;
   private MessageConsumer messageConsumer;
   private MessageListener mlistener;
   private ExceptionListener elistener;
   private String name;
   private String userName;
   private String password;
   private String destJNDI;
   private int destType;
   private String cfJNDI;
   private String url;
   private String icFactory;
   private String selector;
   private AdapterConnectionMetaData metaData;
   private boolean transacted;
   private boolean durable;
   private boolean isXA;
   private int ackMode = 1;
   private boolean ignoreXA = false;
   static final int QUEUE = 0;
   static final int TOPIC = 1;
   private boolean closed = true;
   private ClassLoader classLoader;
   private static final boolean DEBUG = false;
   private static final String TOPIC_CONNECTION_TO_101_CLASSNAME = "com.bea.interop.jms101_jms110.TopicConnectionTo101";
   private static final String TOPIC_CONNECTION_FACTORY_TO_101_CLASSNAME = "com.bea.interop.jms101_jms110.TopicConnectionFactoryTo101";
   private static final String WLS_5_1_TOPIC_CONNECTION_FACTORY = "weblogic.jms.adapter51.wls51TopicConnectionFactory";

   JMSBaseConnection(String var1, String var2, JMSManagedConnectionFactory var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, boolean var11, ClassLoader var12) throws Exception {
      this.userName = var1;
      this.password = var2;
      this.mcf = var3;
      this.name = var4;
      this.url = var5;
      this.icFactory = var6;
      this.destJNDI = var8;
      if (var9 != null) {
         if (var9.equalsIgnoreCase("Queue")) {
            this.destType = 0;
         } else if (var9.equalsIgnoreCase("Topic")) {
            this.destType = 1;
         }
      } else {
         this.destType = 0;
      }

      this.cfJNDI = var7;
      this.selector = var10;
      this.durable = var11;
      this.classLoader = var12;
   }

   public void start() throws ResourceException {
      if (this.classLoader != null) {
         Thread var1 = Thread.currentThread();
         ClassLoader var2 = var1.getContextClassLoader();

         try {
            var1.setContextClassLoader(this.classLoader);
            this.startInternal();
         } finally {
            var1.setContextClassLoader(var2);
         }
      } else {
         this.startInternal();
      }

   }

   private synchronized void startInternal() throws ResourceException {
      if (this.connection == null || this.closed) {
         Object var1 = null;
         Object var2 = null;
         InitialContext var3 = null;

         try {
            var3 = this.getInitialContext();
            if (this.classLoader != null && this.destType != 0) {
               try {
                  var1 = var3.lookup("weblogic.jms.adapter51.wls51TopicConnectionFactory");
               } catch (NameNotFoundException var14) {
                  Class var17 = this.classLoader.loadClass("com.bea.interop.jms101_jms110.TopicConnectionTo101");
                  Class var6 = this.classLoader.loadClass("com.bea.interop.jms101_jms110.TopicConnectionFactoryTo101");
                  Constructor var7 = var6.getConstructors()[0];
                  Object[] var8 = new Object[]{var3.lookup(this.cfJNDI)};
                  TopicConnectionFactory var9 = (TopicConnectionFactory)var7.newInstance(var8);
                  Hashtable var10 = new Hashtable();
                  var10.put("weblogic.jndi.createIntermediateContexts", "true");
                  TopicConnectionFactoryFrom110 var11 = new TopicConnectionFactoryFrom110(var9);

                  try {
                     InitialContext var12 = new InitialContext(var10);
                     var12.unbind("weblogic.jms.adapter51.wls51TopicConnectionFactory");
                     var12.bind("weblogic.jms.adapter51.wls51TopicConnectionFactory", var11);
                  } catch (NamingException var13) {
                  }

                  var1 = var11;
               }
            } else {
               var1 = var3.lookup(this.cfJNDI);
            }

            var2 = var3.lookup(this.destJNDI);
            this.cf = (ConnectionFactory)var1;
            this.isXA = this.cf instanceof XAConnectionFactory;
            if (this.ignoreXA) {
               this.isXA = false;
            }

            if (var2 instanceof Queue && !(var2 instanceof Topic)) {
               this.destType = 0;
            } else if (!(var2 instanceof Queue) && var2 instanceof Topic) {
               this.destType = 1;
            }

            if (this.destType == 0) {
               this.destination = (Queue)var2;
               if (this.isXA) {
                  if (this.userName != null) {
                     this.connection = ((XAQueueConnectionFactory)this.cf).createXAQueueConnection(this.userName, this.password);
                  } else {
                     this.connection = ((XAQueueConnectionFactory)this.cf).createXAQueueConnection();
                  }

                  this.xaSession = ((XAQueueConnection)this.connection).createXAQueueSession();
                  this.session = ((XAQueueSession)this.xaSession).getQueueSession();
               } else {
                  if (this.userName != null) {
                     this.connection = ((QueueConnectionFactory)this.cf).createQueueConnection(this.userName, this.password);
                  } else {
                     this.connection = ((QueueConnectionFactory)this.cf).createQueueConnection();
                  }

                  this.session = ((QueueConnection)this.connection).createQueueSession(this.transacted, this.ackMode);
               }
            } else {
               if (this.destType != 1) {
                  throw new ResourceException("Adapter internal error -- Found non-JMS objcts");
               }

               this.destination = (Topic)var2;
               String var4 = null;
               if (this.durable) {
                  var4 = "MessagingBridge." + this.name;
               }

               if (this.isXA) {
                  if (this.userName != null) {
                     this.connection = ((XATopicConnectionFactory)this.cf).createXATopicConnection(this.userName, this.password);
                  } else {
                     this.connection = ((XATopicConnectionFactory)this.cf).createXATopicConnection();
                  }

                  if (this.durable && (this.connection.getClientID() == null || this.connection.getClientID().length() == 0)) {
                     this.connection.setClientID(var4);
                  }

                  this.xaSession = ((XATopicConnection)this.connection).createXATopicSession();
                  this.session = ((XATopicSession)this.xaSession).getTopicSession();
               } else {
                  if (this.userName != null) {
                     this.connection = ((javax.jms.TopicConnectionFactory)this.cf).createTopicConnection(this.userName, this.password);
                  } else {
                     this.connection = ((javax.jms.TopicConnectionFactory)this.cf).createTopicConnection();
                  }

                  if (this.durable && (this.connection.getClientID() == null || this.connection.getClientID().length() == 0)) {
                     this.connection.setClientID(var4);
                  }

                  this.session = ((TopicConnection)this.connection).createTopicSession(this.transacted, this.ackMode);
               }
            }
         } catch (NamingException var15) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var15);
            }

            if (var3 == null) {
               this.throwResourceException("ConnectionFactory: failed to get initial context (InitialContextFactory =" + this.icFactory + ", url = " + this.url + ", user name = " + this.userName + ")", var15);
            }

            if (var1 == null) {
               this.throwResourceException("ConnectionFactory: " + this.cfJNDI + " not found", var15);
            }

            if (var2 == null) {
               this.throwResourceException("Destination: " + this.destJNDI + " not found", var15);
            }
         } catch (Throwable var16) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var16);
               Exception var5 = null;
               if (var16 instanceof JMSException) {
                  var5 = ((JMSException)var16).getLinkedException();
                  if (var5 != null) {
                     BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var5);
                  }
               }
            }

            this.throwResourceException("Failed to start the connection", var16);
         }

         this.closed = false;
         JMSManagedConnectionFactory.printInfo(this.mcf.getLogWriter(), this.name, "Connection started to " + this.destJNDI);
      }
   }

   public void close() throws ResourceException {
      synchronized(this) {
         if (this.closed) {
            return;
         }
      }

      if (this.classLoader != null) {
         Thread var1 = Thread.currentThread();
         ClassLoader var2 = var1.getContextClassLoader();

         try {
            var1.setContextClassLoader(this.classLoader);
            this.closeInternal();
         } finally {
            var1.setContextClassLoader(var2);
            this.classLoader = null;
         }
      } else {
         this.closeInternal();
      }

   }

   private synchronized void closeInternal() throws ResourceException {
      JMSException var1 = null;
      this.closed = true;

      try {
         this.connection.stop();
      } catch (JMSException var5) {
         if (var1 == null) {
            var1 = var5;
         }
      } catch (IllegalStateException var6) {
      } catch (Exception var7) {
      }

      try {
         this.closeSession();
      } catch (JMSException var4) {
         if (var1 == null) {
            var1 = var4;
         }
      }

      if (this.connection != null) {
         try {
            this.connection.close();
         } catch (JMSException var3) {
            if (var1 == null) {
               var1 = var3;
            }
         }

         this.connection = null;
      }

      JMSManagedConnectionFactory.printInfo(this.mcf.getLogWriter(), this.name, "Connection closed to " + this.destJNDI);
      if (var1 != null) {
      }

   }

   void cleanup() throws JMSException {
      synchronized(this) {
         if (this.closed) {
            return;
         }
      }

      if (this.classLoader != null) {
         Thread var1 = Thread.currentThread();
         ClassLoader var2 = var1.getContextClassLoader();

         try {
            var1.setContextClassLoader(this.classLoader);
            this.cleanupInternal();
         } finally {
            var1.setContextClassLoader(var2);
         }
      } else {
         this.cleanupInternal();
      }

   }

   private synchronized void cleanupInternal() throws JMSException {
      JMSException var1 = null;
      if (this.messageProducer != null) {
         try {
            this.messageProducer.close();
         } catch (JMSException var4) {
            if (var1 == null) {
               var1 = var4;
            }
         }

         this.messageProducer = null;
      }

      if (this.messageConsumer != null) {
         try {
            this.messageConsumer.close();
         } catch (JMSException var3) {
            if (var1 == null) {
               var1 = var3;
            }
         }

         this.messageConsumer = null;
         this.mlistener = null;
      }

      if (var1 != null) {
         throw var1;
      }
   }

   private synchronized void closeSession() throws JMSException {
      JMSException var1 = null;
      if (this.messageProducer != null) {
         try {
            this.messageProducer.close();
         } catch (JMSException var6) {
            if (var1 == null) {
               var1 = var6;
            }
         }

         this.messageProducer = null;
      }

      if (this.messageConsumer != null) {
         try {
            this.messageConsumer.close();
         } catch (JMSException var5) {
            if (var1 == null) {
               var1 = var5;
            }
         }

         this.messageConsumer = null;
         this.mlistener = null;
      }

      if (this.xaSession != null) {
         try {
            this.xaSession.close();
         } catch (JMSException var4) {
            if (var1 == null) {
               var1 = var4;
            }
         }

         this.xaSession = null;
         this.session = null;
      } else if (this.session != null) {
         try {
            this.session.close();
         } catch (JMSException var3) {
            if (var1 == null) {
               var1 = var3;
            }
         }

         this.session = null;
      }

      if (var1 != null) {
         throw var1;
      }
   }

   public void pause() throws ResourceException {
      throw new NotSupportedException("pause() -- Not supported!");
   }

   public void resume() throws ResourceException {
      throw new NotSupportedException("resume() -- Not supported!");
   }

   public synchronized LocalTransaction getLocalTransaction() throws ResourceException {
      return new AdapterLocalTransactionImpl(this.mc);
   }

   public void send(Message var1) throws ResourceException {
      if (this.classLoader != null) {
         Thread var2 = Thread.currentThread();
         ClassLoader var3 = var2.getContextClassLoader();

         try {
            var2.setContextClassLoader(this.classLoader);
            this.sendInternal(var1);
         } finally {
            var2.setContextClassLoader(var3);
         }
      } else {
         this.sendInternal(var1);
      }

   }

   private synchronized void sendInternal(Message var1) throws ResourceException {
      if (this.messageProducer != null) {
         try {
            if (this.destType == 0) {
               ((QueueSender)this.messageProducer).send(var1);
            } else if (this.destType == 1) {
               ((TopicPublisher)this.messageProducer).publish(var1);
            }

            return;
         } catch (Throwable var5) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var5);
               Exception var3 = null;
               if (var5 instanceof JMSException) {
                  var3 = ((JMSException)var5).getLinkedException();
                  if (var3 != null) {
                     BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var3);
                  }
               }
            }

            JMSManagedConnectionFactory.printError(this.mcf.getLogWriter(), this.name, "Failed to send a message");
            this.throwResourceException("Error sending message", var5, false);
         }
      }

      this.ensureStarted();
      if (this.session != null && this.destination != null) {
         try {
            if (this.destType == 0) {
               this.messageProducer = ((QueueSession)this.session).createSender((Queue)this.destination);
               this.connection.start();
               ((QueueSender)this.messageProducer).send(var1);
            } else if (this.destType == 1) {
               this.messageProducer = ((TopicSession)this.session).createPublisher((Topic)this.destination);
               this.connection.start();
               ((TopicPublisher)this.messageProducer).publish(var1);
            }

            return;
         } catch (Throwable var4) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var4);
            }

            JMSManagedConnectionFactory.printError(this.mcf.getLogWriter(), this.name, "Failed to send a message");
            this.throwResourceException("Error creating producer or sending message", var4, false);
         }
      }

      JMSManagedConnectionFactory.printError(this.mcf.getLogWriter(), this.name, "Internal error -- invalid state!");
      throw new ResourceAdapterInternalException("Bridge Adapter internal error -- invalid state!");
   }

   public void send(GenericMessage var1) throws ResourceException {
      throw new NotSupportedException("Not implemented");
   }

   private Message receiveFromQueue(QueueReceiver var1, long var2) throws JMSException {
      if (var2 < 0L) {
         return var1.receive();
      } else {
         return var2 == 0L ? var1.receiveNoWait() : var1.receive(var2);
      }
   }

   private Message receiveFromTopic(TopicSubscriber var1, long var2) throws JMSException {
      if (var2 < 0L) {
         return var1.receive();
      } else {
         return var2 == 0L ? var1.receiveNoWait() : var1.receive(var2);
      }
   }

   private Message receiveCommon(long var1) throws ResourceException {
      if (this.classLoader != null) {
         Thread var3 = Thread.currentThread();
         ClassLoader var4 = var3.getContextClassLoader();

         Message var5;
         try {
            var3.setContextClassLoader(this.classLoader);
            var5 = this.receiveInternal(var1);
         } finally {
            var3.setContextClassLoader(var4);
         }

         return var5;
      } else {
         return this.receiveInternal(var1);
      }
   }

   private synchronized Message receiveInternal(long var1) throws ResourceException {
      Exception var4;
      if (this.messageConsumer != null) {
         try {
            if (this.destType == 0) {
               return this.receiveFromQueue((QueueReceiver)this.messageConsumer, var1);
            }

            if (this.destType == 1) {
               return this.receiveFromTopic((TopicSubscriber)this.messageConsumer, var1);
            }

            JMSManagedConnectionFactory.printError(this.mcf.getLogWriter(), this.name, "Internal error -- invalid state!");
            throw new ResourceException("Adapter internal error: detect non-JMS objects in creating consumer or receiving message");
         } catch (Throwable var6) {
            this.messageConsumer = null;
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var6);
               var4 = null;
               if (var6 instanceof JMSException) {
                  var4 = ((JMSException)var6).getLinkedException();
                  if (var4 != null) {
                     BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var4);
                  }
               }
            }

            JMSManagedConnectionFactory.printError(this.mcf.getLogWriter(), this.name, "Failed to receive a message!");
            this.throwResourceException("Error receiving message", var6);
         }
      }

      this.ensureStarted();
      if (this.session != null && this.destination != null) {
         try {
            if (this.destType != 0) {
               if (this.destType != 1) {
                  JMSManagedConnectionFactory.printError(this.mcf.getLogWriter(), this.name, "Internal error -- invalid state!");
                  throw new ResourceException("Error creating consumer or receiving message -- internal error");
               }

               if (this.selector != null && this.selector.trim().length() > 0) {
                  if (this.durable) {
                     this.messageConsumer = ((TopicSession)this.session).createDurableSubscriber((Topic)this.destination, this.name, this.selector, true);
                  } else {
                     this.messageConsumer = ((TopicSession)this.session).createSubscriber((Topic)this.destination, this.selector, true);
                  }
               } else if (this.durable) {
                  this.messageConsumer = ((TopicSession)this.session).createDurableSubscriber((Topic)this.destination, this.name);
               } else {
                  this.messageConsumer = ((TopicSession)this.session).createSubscriber((Topic)this.destination);
               }

               this.connection.start();
               return this.receiveFromTopic((TopicSubscriber)this.messageConsumer, var1);
            }

            if (this.selector != null && this.selector.trim().length() > 0) {
               this.messageConsumer = ((QueueSession)this.session).createReceiver((Queue)this.destination, this.selector);
            } else {
               this.messageConsumer = ((QueueSession)this.session).createReceiver((Queue)this.destination);
            }

            this.connection.start();
            return this.receiveFromQueue((QueueReceiver)this.messageConsumer, var1);
         } catch (Throwable var5) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var5);
               var4 = null;
               if (var5 instanceof JMSException) {
                  var4 = ((JMSException)var5).getLinkedException();
                  if (var4 != null) {
                     BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var4);
                  }
               }
            }

            JMSManagedConnectionFactory.printError(this.mcf.getLogWriter(), this.name, "Failed to receive a messag");
            this.throwResourceException("Error creating consumer or receiving message", var5);
         }
      }

      JMSManagedConnectionFactory.printError(this.mcf.getLogWriter(), this.name, "Internal error -- invalid state!");
      throw new ResourceException("Error receiving message -- internal error");
   }

   public GenericMessage receiveGenericMessage() throws ResourceException {
      throw new NotSupportedException("Not implemented");
   }

   public Message receive() throws ResourceException {
      return this.receiveCommon(-1L);
   }

   public Message receive(long var1) throws ResourceException {
      return this.receiveCommon(var1);
   }

   public GenericMessage receiveGenericMessage(long var1) throws ResourceException {
      throw new NotSupportedException("Not implemented");
   }

   public synchronized Message createMessage(Message var1) throws ResourceException {
      return var1;
   }

   public Message createMessage(GenericMessage var1) throws ResourceException {
      throw new NotSupportedException("Not implemented");
   }

   public GenericMessage createGenericMessage(Message var1) throws ResourceException {
      throw new NotSupportedException("Not implemented");
   }

   public GenericMessage createGenericMessage(GenericMessage var1) throws ResourceException {
      throw new NotSupportedException("Not implemented");
   }

   public void setMessageListener(MessageListener var1) throws ResourceException {
      this.mlistener = var1;
      if (this.classLoader != null) {
         Thread var2 = Thread.currentThread();
         ClassLoader var3 = var2.getContextClassLoader();

         try {
            var2.setContextClassLoader(this.classLoader);
            if (this.mlistener == null) {
               this.setMessageListenerInternal((MessageListener)null);
            } else {
               this.setMessageListenerInternal(this);
            }
         } finally {
            var2.setContextClassLoader(var3);
         }
      } else if (this.mlistener == null) {
         this.setMessageListenerInternal((MessageListener)null);
      } else {
         this.setMessageListenerInternal(this);
      }

   }

   private synchronized void setMessageListenerInternal(MessageListener var1) throws ResourceException {
      if (this.messageConsumer != null) {
         try {
            this.messageConsumer.setMessageListener(var1);
            JMSManagedConnectionFactory.printInfo(this.mcf.getLogWriter(), this.name, "MessageListener is set on " + this.destJNDI);
            return;
         } catch (Throwable var4) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var4);
            }

            JMSManagedConnectionFactory.printError(this.mcf.getLogWriter(), this.name, "Failed to set a message listener on " + this.destJNDI);
            this.throwResourceException("Error setting message listener", var4);
         }
      }

      this.ensureStarted();
      if (this.session != null && this.destination != null) {
         try {
            if (this.destType == 0) {
               if (this.selector != null && this.selector.trim().length() > 0) {
                  this.messageConsumer = ((QueueSession)this.session).createReceiver((Queue)this.destination, this.selector);
               } else {
                  this.messageConsumer = ((QueueSession)this.session).createReceiver((Queue)this.destination);
               }
            } else if (this.destType == 1) {
               if (this.selector != null && this.selector.trim().length() > 0) {
                  if (this.durable) {
                     this.messageConsumer = ((TopicSession)this.session).createDurableSubscriber((Topic)this.destination, this.name, this.selector, true);
                  } else {
                     this.messageConsumer = ((TopicSession)this.session).createSubscriber((Topic)this.destination, this.selector, true);
                  }
               } else if (this.durable) {
                  this.messageConsumer = ((TopicSession)this.session).createDurableSubscriber((Topic)this.destination, this.name);
               } else {
                  this.messageConsumer = ((TopicSession)this.session).createSubscriber((Topic)this.destination);
               }
            }

            if (var1 == null) {
               this.messageConsumer.setMessageListener((MessageListener)null);
            } else {
               this.messageConsumer.setMessageListener(this);
            }

            this.connection.start();
            JMSManagedConnectionFactory.printInfo(this.mcf.getLogWriter(), this.name, "MessageListener is set on " + this.destJNDI);
            return;
         } catch (Throwable var3) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var3);
            }

            JMSManagedConnectionFactory.printError(this.mcf.getLogWriter(), this.name, "Failed to set a message listener on " + this.destJNDI);
            this.throwResourceException("Error creating asynchronous consumer or setting message lisenter", var3);
         }
      }

      JMSManagedConnectionFactory.printError(this.mcf.getLogWriter(), this.name, "Internal error -- invalid state!");
      throw new ResourceAdapterInternalException("Bridge Adapter internal error -- invalid state!");
   }

   public void addNotificationListener(NotificationListener var1, int var2) throws ResourceException {
      throw new NotSupportedException("Not implemented");
   }

   public void removeNotificationListener(NotificationListener var1, int var2) throws ResourceException {
      throw new NotSupportedException("Not implemented");
   }

   public void associateTransaction(Message var1) throws ResourceException {
      if (this.classLoader != null) {
         Thread var2 = Thread.currentThread();
         ClassLoader var3 = var2.getContextClassLoader();

         try {
            var2.setContextClassLoader(this.classLoader);
            this.associateTransactionInternal(var1);
         } finally {
            var2.setContextClassLoader(var3);
         }
      } else {
         this.associateTransactionInternal(var1);
      }

   }

   private synchronized void associateTransactionInternal(Message var1) throws ResourceException {
      if (this.session instanceof MDBTransaction) {
         try {
            ((MDBTransaction)this.session).associateTransaction(var1);
         } catch (Throwable var3) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var3);
            }

            this.throwResourceException("Failed to associate a message with the current transaction", var3, false);
         }

      } else {
         throw new NotSupportedException("Not implemented");
      }
   }

   public void associateTransaction(GenericMessage var1) throws ResourceException {
      throw new NotSupportedException("Not implemented");
   }

   private void throwResourceException(String var1, Throwable var2) throws ResourceException {
      this.throwResourceException(var1, var2, true);
   }

   private void throwResourceException(String var1, Throwable var2, boolean var3) throws ResourceException {
      if (var3) {
         this.mc.sendEvent(5, (Exception)null);
      }

      Object var4 = null;
      if (var2 != null && var2 instanceof TransactionRolledBackException) {
         var4 = new ResourceTransactionRolledBackException("Transaction rolled back");
      } else {
         var4 = new ResourceException(var1);
      }

      if (var2 != null && var2 instanceof Exception) {
         ((ResourceException)var4).setLinkedException((Exception)var2);
      }

      throw var4;
   }

   public XAResource getXAResource() throws ResourceException {
      if (this.classLoader != null) {
         Thread var1 = Thread.currentThread();
         ClassLoader var2 = var1.getContextClassLoader();

         XAResource var3;
         try {
            var1.setContextClassLoader(this.classLoader);
            var3 = this.getXAResourceInternal();
         } finally {
            var1.setContextClassLoader(var2);
         }

         return var3;
      } else {
         return this.getXAResourceInternal();
      }
   }

   private synchronized XAResource getXAResourceInternal() throws ResourceException {
      if (this.xaSession != null && !(this.xaSession instanceof JMSXASession)) {
         XAResource var1 = this.xaSession.getXAResource();
         if (var1 instanceof IgnoreXAResource) {
            throw new NoEnlistXAResourceException("No need to enlist this resource");
         } else {
            return var1;
         }
      } else {
         throw new NoEnlistXAResourceException("No need to enlist this resource");
      }
   }

   public synchronized boolean implementsMDBTransaction() throws ResourceException {
      return this.session != null && this.session instanceof MDBTransaction;
   }

   public synchronized boolean isXAConnection() throws ResourceException {
      return this.cf != null && this.cf instanceof XAConnectionFactory;
   }

   public void setExceptionListener(ExceptionListener var1) throws ResourceException {
      this.elistener = var1;
      if (this.classLoader != null) {
         Thread var2 = Thread.currentThread();
         ClassLoader var3 = var2.getContextClassLoader();

         try {
            var2.setContextClassLoader(this.classLoader);
            this.setExceptionListenerInternal(this);
         } finally {
            var2.setContextClassLoader(var3);
         }
      } else {
         this.setExceptionListenerInternal(this);
      }

   }

   private synchronized void setExceptionListenerInternal(ExceptionListener var1) throws ResourceException {
      if (this.connection != null) {
         try {
            this.connection.setExceptionListener(var1);
         } catch (Throwable var3) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var3);
            }

            this.throwResourceException("Error setting exception listener", var3);
         }
      }

   }

   public void setAcknowledgeMode(int var1) throws ResourceException {
      if (this.classLoader != null) {
         Thread var2 = Thread.currentThread();
         ClassLoader var3 = var2.getContextClassLoader();

         try {
            var2.setContextClassLoader(this.classLoader);
            this.setAcknowledgeModeInternal(var1);
         } finally {
            var2.setContextClassLoader(var3);
         }
      } else {
         this.setAcknowledgeModeInternal(var1);
      }

   }

   private synchronized void setAcknowledgeModeInternal(int var1) throws ResourceException {
      if (var1 != this.ackMode) {
         this.ignoreXA = false;
         switch (var1) {
            case 1:
               this.ackMode = 1;
               break;
            case 2:
               this.ackMode = 2;
               break;
            case 3:
               this.ackMode = 3;
               break;
            default:
               this.ackMode = 1;
               this.ignoreXA = true;
         }

         if (this.connection != null) {
            try {
               this.closeSession();
            } catch (Exception var3) {
            }

            try {
               if (this.destType == 0) {
                  this.session = ((QueueConnection)this.connection).createQueueSession(this.transacted, this.ackMode);
               } else if (this.destType == 1) {
                  this.session = ((TopicConnection)this.connection).createTopicSession(this.transacted, this.ackMode);
               }
            } catch (Throwable var4) {
               if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
                  BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var4);
               }

               this.throwResourceException("Error starting a local transaction", var4);
            }

         }
      }
   }

   public AdapterConnectionMetaData getMetaData() throws ResourceException {
      return this.metaData;
   }

   public void recover() throws ResourceException {
      try {
         if (this.session.getTransacted()) {
            return;
         }
      } catch (JMSException var9) {
         this.throwResourceException("Error recovering messages", var9, false);
      }

      if (this.classLoader != null) {
         Thread var1 = Thread.currentThread();
         ClassLoader var2 = var1.getContextClassLoader();

         try {
            var1.setContextClassLoader(this.classLoader);
            this.session.recover();
         } catch (Throwable var11) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var11);
            }

            this.throwResourceException("Error recovering messages", var11, false);
         } finally {
            var1.setContextClassLoader(var2);
         }
      } else {
         try {
            this.session.recover();
         } catch (Throwable var10) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var10);
            }

            this.throwResourceException("Error recovering messages", var10, false);
         }
      }

   }

   void createTransactedSession() throws ResourceException {
      if (this.classLoader != null) {
         Thread var1 = Thread.currentThread();
         ClassLoader var2 = var1.getContextClassLoader();

         try {
            var1.setContextClassLoader(this.classLoader);
            this.createTransactedSessionInternal();
         } finally {
            var1.setContextClassLoader(var2);
         }
      } else {
         this.createTransactedSessionInternal();
      }

   }

   private synchronized void createTransactedSessionInternal() throws ResourceException {
      if (!this.transacted) {
         if (this.connection == null) {
            this.transacted = true;
         } else {
            MessageListener var1 = null;
            if (this.messageConsumer != null) {
               try {
                  var1 = this.messageConsumer.getMessageListener();
               } catch (Throwable var5) {
                  if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
                     BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var5);
                  }

                  this.throwResourceException("Error restore message listener", var5);
               }
            }

            try {
               this.closeSession();
            } catch (Exception var3) {
            }

            try {
               if (this.destType == 0) {
                  this.session = ((QueueConnection)this.connection).createQueueSession(true, this.ackMode);
               } else if (this.destType == 1) {
                  this.session = ((TopicConnection)this.connection).createTopicSession(true, this.ackMode);
               }

               if (this.mlistener != null) {
                  this.setMessageListener(this.mlistener);
               }
            } catch (Throwable var4) {
               if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
                  BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var4);
               }

               this.throwResourceException("Error beginning a local transaction", var4);
            }

            this.transacted = true;
         }
      }
   }

   void commit() throws ResourceException {
      if (!this.transacted) {
         this.throwResourceException("Error committing a local transaction -- not transacted", (Throwable)null);
      }

      if (this.classLoader != null) {
         Thread var1 = Thread.currentThread();
         ClassLoader var2 = var1.getContextClassLoader();

         try {
            var1.setContextClassLoader(this.classLoader);
            this.session.commit();
         } catch (Throwable var9) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var9);
            }

            this.throwResourceException("Error committing a local transaction", var9);
         } finally {
            var1.setContextClassLoader(var2);
         }
      } else {
         try {
            this.session.commit();
         } catch (Throwable var8) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var8);
            }

            this.throwResourceException("Error committing a local transaction", var8);
         }
      }

   }

   void rollback() throws ResourceException {
      if (!this.transacted) {
         this.throwResourceException("Error rolling back a local transaction -- not transacted", (Throwable)null);
      }

      if (this.classLoader != null) {
         Thread var1 = Thread.currentThread();
         ClassLoader var2 = var1.getContextClassLoader();

         try {
            var1.setContextClassLoader(this.classLoader);
            this.session.rollback();
         } catch (Throwable var9) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var9);
            }

            this.throwResourceException("Error rolling back a local transaction", var9);
         } finally {
            var1.setContextClassLoader(var2);
         }
      } else {
         try {
            this.session.rollback();
         } catch (Throwable var8) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var8);
            }

            this.throwResourceException("Error rolling back a local transaction", var8);
         }
      }

   }

   void setManagedConnection(JMSManagedConnection var1) {
      this.mc = var1;
      this.metaData = new AdapterConnectionMetaDataImpl(var1, this.mcf);
   }

   private InitialContext getInitialContext() throws NamingException {
      if (this.url == null) {
         return new InitialContext();
      } else {
         Hashtable var1 = new Hashtable();
         if (this.userName != null && this.password != null) {
            var1.put("java.naming.security.principal", this.userName);
            var1.put("java.naming.security.credentials", this.password);
         }

         var1.put("java.naming.factory.initial", this.icFactory);
         var1.put("java.naming.provider.url", this.url);
         return new InitialContext(var1);
      }
   }

   private void ensureStarted() throws ResourceException {
      if (this.closed) {
         this.startInternal();
         this.closed = false;
      }

   }

   public void onMessage(Message var1) {
      if (this.mlistener != null) {
         this.mlistener.onMessage(var1);
      } else {
         if (this.session != null) {
            try {
               this.session.recover();
            } catch (JMSException var3) {
            }
         }

      }
   }

   public void onException(JMSException var1) {
      if (this.elistener != null) {
         this.elistener.onException(var1);
      }

   }

   public void acknowledge(Message var1) throws ResourceException {
      if (this.classLoader != null) {
         Thread var2 = Thread.currentThread();
         ClassLoader var3 = var2.getContextClassLoader();

         try {
            var2.setContextClassLoader(this.classLoader);
            var1.acknowledge();
         } catch (Throwable var10) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var10);
            }

            this.throwResourceException("Error acknowledging messages", var10, false);
         } finally {
            var2.setContextClassLoader(var3);
         }
      } else {
         try {
            var1.acknowledge();
         } catch (Throwable var9) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var9);
            }

            this.throwResourceException("Error acknowledging messages", var9, false);
         }
      }

   }
}
