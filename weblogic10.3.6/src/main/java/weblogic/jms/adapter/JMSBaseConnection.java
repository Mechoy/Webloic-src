package weblogic.jms.adapter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
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
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.jms.TransactionRolledBackException;
import javax.jms.XAConnection;
import javax.jms.XAConnectionFactory;
import javax.jms.XAQueueConnection;
import javax.jms.XAQueueConnectionFactory;
import javax.jms.XAQueueSession;
import javax.jms.XASession;
import javax.jms.XATopicConnection;
import javax.jms.XATopicConnectionFactory;
import javax.jms.XATopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.transaction.xa.XAResource;
import weblogic.common.internal.PeerInfo;
import weblogic.connector.exception.NoEnlistXAResourceException;
import weblogic.jms.bridge.AdapterConnectionMetaData;
import weblogic.jms.bridge.GenericMessage;
import weblogic.jms.bridge.LocalTransaction;
import weblogic.jms.bridge.NotificationListener;
import weblogic.jms.bridge.ResourceTransactionRolledBackException;
import weblogic.jms.bridge.SourceConnection;
import weblogic.jms.bridge.TargetConnection;
import weblogic.jms.bridge.TemporaryResourceException;
import weblogic.jms.bridge.internal.BridgeDebug;
import weblogic.jms.client.ConnectionInternal;
import weblogic.jms.client.XASessionInternal;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.extensions.JMSForwardHelper;
import weblogic.jms.extensions.MDBTransaction;
import weblogic.jms.extensions.WLMessageProducer;
import weblogic.security.SubjectUtils;
import weblogic.security.WLSPrincipals;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.transaction.internal.IgnoreXAResource;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public class JMSBaseConnection implements SourceConnection, TargetConnection, MessageListener, ExceptionListener {
   private JMSManagedConnectionFactory mcf;
   private JMSManagedConnection mc;
   protected ConnectionFactory cf;
   protected XAConnectionFactory xcf;
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
   private static AuthenticatedSubject kernelId;
   private AuthenticatedSubject subject;
   private boolean forwardMethodAvailable = false;
   private boolean preserveMsgProperty = false;
   private int logCount = 0;
   private static final boolean DEBUG = false;
   private static final String[] ISOLATING_PACKAGES = new String[]{"weblogic.*", "COM.rsa.*"};
   Object onExceptionLock = new Object();

   JMSBaseConnection(String var1, String var2, JMSManagedConnectionFactory var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, boolean var11, String var12, boolean var13) throws Exception {
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
      this.preserveMsgProperty = var13;
      if (var12 != null && var12.length() > 0) {
         this.classLoader = new IsolatingClassLoader("JMS Interop Adapter Class Loader", getClassPath(var12), ISOLATING_PACKAGES, true);
      }

      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   private static URL[] getClassPath(String var0) throws MalformedURLException {
      ArrayList var1 = new ArrayList();
      StringTokenizer var2 = new StringTokenizer(var0, File.pathSeparator);

      while(var2.hasMoreTokens()) {
         String var3 = var2.nextToken();
         if (var3.length() > 0) {
            var1.add((new File(var3)).toURL());
         }
      }

      return (URL[])((URL[])var1.toArray(new URL[0]));
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
         final Context var3 = null;

         try {
            AuthenticatedSubject var4 = SubjectUtils.getAnonymousSubject();
            var4.setQOS((byte)101);

            try {
               var3 = (Context)var4.doAs(kernelId, new PrivilegedExceptionAction() {
                  public Object run() throws Exception {
                     return JMSBaseConnection.this.getInitialContext();
                  }
               });
            } catch (PrivilegedActionException var22) {
               if (var22.getCause() instanceof NamingException) {
                  throw (NamingException)var22.getCause();
               }

               throw new AssertionError(var22);
            }

            final String var27;
            try {
               var27 = this.cfJNDI;
               var1 = this.subject.doAs(kernelId, new PrivilegedExceptionAction() {
                  public Object run() throws Exception {
                     return var3.lookup(var27);
                  }
               });
            } catch (PrivilegedActionException var21) {
               if (var21.getCause() instanceof NamingException) {
                  throw (NamingException)var21.getCause();
               }

               throw new AssertionError(var21);
            }

            try {
               var27 = this.destJNDI;
               var2 = this.subject.doAs(kernelId, new PrivilegedExceptionAction() {
                  public Object run() throws Exception {
                     return var3.lookup(var27);
                  }
               });
            } catch (PrivilegedActionException var20) {
               if (var20.getCause() instanceof NamingException) {
                  throw (NamingException)var20.getCause();
               }

               throw new AssertionError(var20);
            }

            if (var1 instanceof XAConnectionFactory) {
               this.xcf = (XAConnectionFactory)var1;
               this.isXA = true;
            } else {
               if (!(var1 instanceof ConnectionFactory)) {
                  throw new ResourceException("Adapter internal error -- connectionFactory object is not instanceof ConnectionFactory");
               }

               this.cf = (ConnectionFactory)var1;
            }

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
                     if (this.xcf instanceof XAQueueConnectionFactory) {
                        this.connection = ((XAQueueConnectionFactory)this.xcf).createXAQueueConnection(this.userName, this.password);
                     } else if (this.xcf instanceof XAConnectionFactory) {
                        this.connection = this.xcf.createXAConnection(this.userName, this.password);
                     }
                  } else if (this.xcf instanceof XAQueueConnectionFactory) {
                     this.connection = ((XAQueueConnectionFactory)this.xcf).createXAQueueConnection();
                  } else if (this.xcf instanceof XAConnectionFactory) {
                     this.connection = this.xcf.createXAConnection();
                  }

                  if (this.connection instanceof XAQueueConnection) {
                     this.xaSession = ((XAQueueConnection)this.connection).createXAQueueSession();
                     this.session = ((XAQueueSession)this.xaSession).getQueueSession();
                  } else {
                     this.xaSession = ((XAConnection)this.connection).createXASession();
                     this.session = this.xaSession.getSession();
                  }
               } else {
                  if (this.userName != null) {
                     if (this.cf instanceof QueueConnectionFactory) {
                        this.connection = ((QueueConnectionFactory)this.cf).createQueueConnection(this.userName, this.password);
                     } else {
                        this.connection = this.cf.createConnection(this.userName, this.password);
                     }
                  } else if (this.cf instanceof QueueConnectionFactory) {
                     this.connection = ((QueueConnectionFactory)this.cf).createQueueConnection();
                  } else {
                     this.connection = this.cf.createConnection();
                  }

                  if (this.connection instanceof QueueConnection) {
                     this.session = ((QueueConnection)this.connection).createQueueSession(this.transacted, this.ackMode);
                  } else {
                     this.session = this.connection.createSession(this.transacted, this.ackMode);
                  }
               }
            } else {
               if (this.destType != 1) {
                  throw new ResourceException("Adapter internal error -- Found non-JMS objcts");
               }

               this.destination = (Topic)var2;
               var27 = null;
               if (this.durable) {
                  var27 = "MessagingBridge." + this.name;
               }

               if (this.isXA) {
                  if (this.userName != null) {
                     if (this.xcf instanceof XATopicConnectionFactory) {
                        this.connection = ((XATopicConnectionFactory)this.xcf).createXATopicConnection(this.userName, this.password);
                     } else {
                        this.connection = this.xcf.createXAConnection(this.userName, this.password);
                     }
                  } else if (this.xcf instanceof XATopicConnectionFactory) {
                     this.connection = ((XATopicConnectionFactory)this.xcf).createXATopicConnection();
                  } else {
                     this.connection = this.xcf.createXAConnection();
                  }

                  if (this.durable && (this.connection.getClientID() == null || this.connection.getClientID().length() == 0)) {
                     this.connection.setClientID(var27);
                  }

                  if (this.connection instanceof XATopicConnection) {
                     this.xaSession = ((XATopicConnection)this.connection).createXATopicSession();
                     this.session = ((XATopicSession)this.xaSession).getTopicSession();
                  } else {
                     this.xaSession = ((XAConnection)this.connection).createXASession();
                     this.session = this.xaSession.getSession();
                  }
               } else {
                  if (this.userName != null) {
                     if (this.cf instanceof TopicConnectionFactory) {
                        this.connection = ((TopicConnectionFactory)this.cf).createTopicConnection(this.userName, this.password);
                     } else {
                        this.connection = this.cf.createConnection(this.userName, this.password);
                     }
                  } else if (this.cf instanceof TopicConnectionFactory) {
                     this.connection = ((TopicConnectionFactory)this.cf).createTopicConnection();
                  } else {
                     this.connection = this.cf.createConnection();
                  }

                  if (this.durable && (this.connection.getClientID() == null || this.connection.getClientID().length() == 0)) {
                     this.connection.setClientID(var27);
                  }

                  if (this.connection instanceof TopicConnection) {
                     this.session = ((TopicConnection)this.connection).createTopicSession(this.transacted, this.ackMode);
                  } else {
                     this.session = this.connection.createSession(this.transacted, this.ackMode);
                  }
               }
            }
         } catch (NamingException var23) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var23);
            }

            if (var3 == null) {
               this.throwResourceException("ConnectionFactory: failed to get initial context (InitialContextFactory =" + this.icFactory + ", url = " + this.url + ", user name = " + this.userName + ")", var23);
            }

            if (var1 == null) {
               if (this.logCount++ >= 2) {
                  this.throwResourceException("ConnectionFactory: " + this.cfJNDI + " not found", var23);
               } else {
                  this.throwTempResourceException();
               }
            }

            if (var2 == null) {
               if (this.logCount++ >= 2) {
                  this.throwResourceException("Destination: " + this.destJNDI + " not found", var23);
               } else {
                  this.throwTempResourceException();
               }
            }
         } catch (Throwable var24) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var24);
               Exception var5 = null;
               if (var24 instanceof JMSException) {
                  var5 = ((JMSException)var24).getLinkedException();
                  if (var5 != null) {
                     BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var5);
                  }
               }
            }

            this.throwResourceException("Failed to start the connection", var24);
         } finally {
            try {
               if (var3 != null) {
                  var3.close();
               }

               var3 = null;
               var1 = null;
               var2 = null;
            } catch (NamingException var19) {
            }

         }

         this.closed = false;
         if (this.connection instanceof ConnectionInternal) {
            PeerInfo var26 = ((ConnectionInternal)this.connection).getFEPeerInfo();
            if (var26.compareTo(PeerInfo.VERSION_DIABLO) >= 0) {
               this.forwardMethodAvailable = true;
            }
         }

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
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  JMSBaseConnection.this.closeInternal();
                  return null;
               }
            });
         } catch (PrivilegedActionException var9) {
            this.throwResourceException("Error closing connection", var9.getException());
            return;
         } finally {
            var1.setContextClassLoader(var2);
         }

      } else {
         try {
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  JMSBaseConnection.this.closeInternal();
                  return null;
               }
            });
         } catch (PrivilegedActionException var11) {
            this.throwResourceException("Error closing connection", var11.getException());
         }
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
      } catch (Exception var6) {
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

   void cleanup() throws JMSException, ResourceException {
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
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws JMSException {
                  JMSBaseConnection.this.cleanupInternal();
                  return null;
               }
            });
         } catch (PrivilegedActionException var9) {
            this.throwResourceException("Error cleanup connection", var9.getException());
            return;
         } finally {
            var1.setContextClassLoader(var2);
         }

      } else {
         try {
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws JMSException {
                  JMSBaseConnection.this.cleanupInternal();
                  return null;
               }
            });
         } catch (PrivilegedActionException var11) {
            this.throwResourceException("Error cleanup connection", var11.getException());
         }
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

   public void send(final Message var1) throws ResourceException {
      if (this.classLoader != null) {
         Thread var2 = Thread.currentThread();
         ClassLoader var3 = var2.getContextClassLoader();

         try {
            var2.setContextClassLoader(this.classLoader);
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  JMSBaseConnection.this.sendInternal(var1);
                  return null;
               }
            });
         } catch (PrivilegedActionException var9) {
            this.throwResourceException("Error sending message", var9.getException(), false);
            return;
         } finally {
            var2.setContextClassLoader(var3);
         }

      } else {
         final Message var12 = var1;

         try {
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  JMSBaseConnection.this.sendInternal(var12);
                  return null;
               }
            });
         } catch (PrivilegedActionException var11) {
            this.throwResourceException("Error sending message", var11.getException(), false);
         }
      }
   }

   private synchronized void sendInternal(Message var1) throws ResourceException {
      try {
         if (this.messageProducer == null) {
            this.ensureStarted();
            if (this.session == null || this.destination == null) {
               JMSManagedConnectionFactory.printError(this.mcf.getLogWriter(), this.name, "Internal error -- invalid state!");
               throw new ResourceAdapterInternalException("Bridge Adapter internal error -- invalid state!");
            }

            if (this.destType == 0 && this.session instanceof QueueSession) {
               this.messageProducer = ((QueueSession)this.session).createSender((Queue)this.destination);
            } else if (this.destType == 1 && this.session instanceof TopicSession) {
               this.messageProducer = ((TopicSession)this.session).createPublisher((Topic)this.destination);
            } else {
               this.messageProducer = this.session.createProducer(this.destination);
            }

            this.connection.start();
         }

         if (this.preserveMsgProperty) {
            if (this.forwardMethodAvailable && var1 instanceof MessageImpl) {
               ((MessageImpl)var1).setJMSXUserID((String)null);
               ((MessageImpl)var1).requestJMSXUserID(false);
               JMSForwardHelper.ForwardFromMessage((WLMessageProducer)this.messageProducer, var1, false);
            } else {
               long var2 = JMSForwardHelper.getRelativeTimeToLive(var1);
               if (var2 < 0L) {
                  var2 = 1L;
               }

               if (this.destType == 0) {
                  ((QueueSender)this.messageProducer).send(var1, var1.getJMSDeliveryMode(), var1.getJMSPriority(), var2);
               } else if (this.destType == 1) {
                  ((TopicPublisher)this.messageProducer).publish(var1, var1.getJMSDeliveryMode(), var1.getJMSPriority(), var2);
               }
            }
         } else {
            if (var1 instanceof MessageImpl) {
               ((MessageImpl)var1).setDeliveryCount(0);
            }

            if (this.destType == 0) {
               if (this.messageProducer instanceof QueueSender) {
                  ((QueueSender)this.messageProducer).send(var1);
               } else {
                  this.messageProducer.send(var1);
               }
            } else if (this.destType == 1) {
               if (this.messageProducer instanceof TopicPublisher) {
                  ((TopicPublisher)this.messageProducer).publish(var1);
               } else {
                  this.messageProducer.send(var1);
               }
            }
         }
      } catch (Throwable var4) {
         if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
            BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var4);
            Exception var3 = null;
            if (var4 instanceof JMSException) {
               var3 = ((JMSException)var4).getLinkedException();
               if (var3 != null) {
                  BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var3);
               }
            }
         }

         JMSManagedConnectionFactory.printError(this.mcf.getLogWriter(), this.name, "Failed to send a message");
         this.throwResourceException("Error creating producer or sending message", var4, false);
      }

   }

   public synchronized void send(GenericMessage var1) throws ResourceException {
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

   private Message receiveFromDestination(MessageConsumer var1, long var2) throws JMSException {
      if (var2 < 0L) {
         return var1.receive();
      } else {
         return var2 == 0L ? var1.receiveNoWait() : var1.receive(var2);
      }
   }

   private Message receiveCommon(final long var1) throws ResourceException {
      if (this.classLoader != null) {
         Thread var3 = Thread.currentThread();
         ClassLoader var4 = var3.getContextClassLoader();

         Message var7;
         try {
            var3.setContextClassLoader(this.classLoader);
            var7 = (Message)SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  return JMSBaseConnection.this.receiveInternal(var1);
               }
            });
         } catch (PrivilegedActionException var12) {
            this.throwResourceException("Error receiving message", var12.getException());
            return null;
         } finally {
            var3.setContextClassLoader(var4);
         }

         return var7;
      } else {
         final long var15 = var1;

         try {
            return (Message)SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  return JMSBaseConnection.this.receiveInternal(var15);
               }
            });
         } catch (PrivilegedActionException var14) {
            this.throwResourceException("Error receiving message", var14.getException());
            return null;
         }
      }
   }

   private synchronized Message receiveInternal(long var1) throws ResourceException {
      Exception var4;
      if (this.messageConsumer != null) {
         try {
            if (this.destType == 0) {
               if (this.messageConsumer instanceof QueueReceiver) {
                  return this.receiveFromQueue((QueueReceiver)this.messageConsumer, var1);
               }

               return this.receiveFromDestination(this.messageConsumer, var1);
            }

            if (this.destType == 1) {
               if (this.messageConsumer instanceof TopicSubscriber) {
                  return this.receiveFromTopic((TopicSubscriber)this.messageConsumer, var1);
               }

               return this.receiveFromDestination(this.messageConsumer, var1);
            }

            JMSManagedConnectionFactory.printError(this.mcf.getLogWriter(), this.name, "Internal error -- invalid state!");
            throw new ResourceException("Adapter internal error:  detect non-JMS objects in creating consumer or receiving message");
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
                     if (this.session instanceof TopicSession) {
                        this.messageConsumer = ((TopicSession)this.session).createDurableSubscriber((Topic)this.destination, this.name, this.selector, true);
                     } else {
                        this.messageConsumer = this.session.createDurableSubscriber((Topic)this.destination, this.name, this.selector, true);
                     }
                  } else if (this.session instanceof TopicSession) {
                     this.messageConsumer = ((TopicSession)this.session).createSubscriber((Topic)this.destination, this.selector, true);
                  } else {
                     this.messageConsumer = this.session.createConsumer(this.destination, this.selector, true);
                  }
               } else if (this.durable) {
                  if (this.session instanceof TopicSession) {
                     this.messageConsumer = ((TopicSession)this.session).createDurableSubscriber((Topic)this.destination, this.name);
                  } else {
                     this.messageConsumer = this.session.createDurableSubscriber((Topic)this.destination, this.name);
                  }
               } else if (this.session instanceof TopicSession) {
                  this.messageConsumer = ((TopicSession)this.session).createSubscriber((Topic)this.destination);
               } else {
                  this.messageConsumer = this.session.createConsumer(this.destination);
               }

               this.connection.start();
               if (this.messageConsumer instanceof TopicSubscriber) {
                  return this.receiveFromTopic((TopicSubscriber)this.messageConsumer, var1);
               }

               return this.receiveFromDestination(this.messageConsumer, var1);
            }

            if (this.selector != null && this.selector.trim().length() > 0) {
               if (this.session instanceof QueueSession) {
                  this.messageConsumer = ((QueueSession)this.session).createReceiver((Queue)this.destination, this.selector);
               } else {
                  this.messageConsumer = this.session.createConsumer(this.destination, this.selector);
               }
            } else if (this.session instanceof QueueSession) {
               this.messageConsumer = ((QueueSession)this.session).createReceiver((Queue)this.destination);
            } else {
               this.messageConsumer = this.session.createConsumer(this.destination);
            }

            this.connection.start();
            if (this.messageConsumer instanceof QueueReceiver) {
               return this.receiveFromQueue((QueueReceiver)this.messageConsumer, var1);
            }

            return this.receiveFromDestination(this.messageConsumer, var1);
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

   public void setMessageListener(final MessageListener var1) throws ResourceException {
      this.mlistener = var1;
      if (this.classLoader != null) {
         Thread var2 = Thread.currentThread();
         ClassLoader var3 = var2.getContextClassLoader();

         try {
            var2.setContextClassLoader(this.classLoader);
            if (this.messageConsumer == null) {
               SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
                  public Object run() throws ResourceException {
                     JMSBaseConnection.this.createConsumer();
                     return null;
                  }
               });
            }

            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  if (var1 == null) {
                     JMSBaseConnection.this.setMessageListenerInternal((MessageListener)null);
                  } else {
                     JMSBaseConnection.this.setMessageListenerInternal(JMSBaseConnection.this);
                  }

                  return null;
               }
            });
         } catch (PrivilegedActionException var10) {
            this.throwResourceException("Error setting message listener", var10.getException());
            return;
         } finally {
            var2.setContextClassLoader(var3);
         }

      } else {
         final MessageListener var13 = var1;
         final JMSBaseConnection var14 = this;

         try {
            if (this.messageConsumer == null) {
               SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
                  public Object run() throws ResourceException {
                     JMSBaseConnection.this.createConsumer();
                     return null;
                  }
               });
            }

            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  if (var13 == null) {
                     JMSBaseConnection.this.setMessageListenerInternal((MessageListener)null);
                  } else {
                     JMSBaseConnection.this.setMessageListenerInternal(var14);
                  }

                  return null;
               }
            });
         } catch (PrivilegedActionException var12) {
            this.throwResourceException("Error setting message listener", var12.getException());
         }
      }
   }

   private synchronized void setMessageListenerInternal(MessageListener var1) throws ResourceException {
      if (this.messageConsumer != null) {
         try {
            this.messageConsumer.setMessageListener(var1);
            JMSManagedConnectionFactory.printInfo(this.mcf.getLogWriter(), this.name, "MessageListener is set on " + this.destJNDI);
            return;
         } catch (Throwable var3) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var3);
            }

            JMSManagedConnectionFactory.printError(this.mcf.getLogWriter(), this.name, "Failed to set a message listener on " + this.destJNDI);
            this.throwResourceException("Error setting message listener", var3);
         }
      }

   }

   private synchronized void createConsumer() throws ResourceException {
      this.ensureStarted();
      if (this.session != null && this.destination != null) {
         try {
            if (this.destType == 0) {
               if (this.selector != null && this.selector.trim().length() > 0) {
                  if (this.session instanceof QueueSession) {
                     this.messageConsumer = ((QueueSession)this.session).createReceiver((Queue)this.destination, this.selector);
                  } else {
                     this.messageConsumer = this.session.createConsumer(this.destination, this.selector);
                  }
               } else if (this.session instanceof QueueSession) {
                  this.messageConsumer = ((QueueSession)this.session).createReceiver((Queue)this.destination);
               } else {
                  this.messageConsumer = this.session.createConsumer(this.destination);
               }
            } else if (this.destType == 1) {
               if (this.selector != null && this.selector.trim().length() > 0) {
                  if (this.durable) {
                     if (this.session instanceof TopicSession) {
                        this.messageConsumer = ((TopicSession)this.session).createDurableSubscriber((Topic)this.destination, this.name, this.selector, true);
                     } else {
                        this.messageConsumer = this.session.createDurableSubscriber((Topic)this.destination, this.name, this.selector, true);
                     }
                  } else if (this.session instanceof TopicSession) {
                     this.messageConsumer = ((TopicSession)this.session).createSubscriber((Topic)this.destination, this.selector, true);
                  } else {
                     this.messageConsumer = this.session.createConsumer(this.destination, this.selector, true);
                  }
               } else if (this.durable) {
                  if (this.session instanceof TopicSession) {
                     this.messageConsumer = ((TopicSession)this.session).createDurableSubscriber((Topic)this.destination, this.name);
                  } else {
                     this.messageConsumer = this.session.createDurableSubscriber((Topic)this.destination, this.name);
                  }
               } else if (this.session instanceof TopicSession) {
                  this.messageConsumer = ((TopicSession)this.session).createSubscriber((Topic)this.destination);
               } else {
                  this.messageConsumer = this.session.createConsumer(this.destination);
               }
            }

            this.connection.start();
            JMSManagedConnectionFactory.printInfo(this.mcf.getLogWriter(), this.name, "Consumer created on " + this.destJNDI);
            return;
         } catch (Throwable var2) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var2);
            }

            JMSManagedConnectionFactory.printError(this.mcf.getLogWriter(), this.name, "Failed to create consumer " + this.destJNDI);
            this.throwResourceException("Error creating asynchronous consumer ", var2);
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

   public void associateTransaction(final Message var1) throws ResourceException {
      if (this.classLoader != null) {
         Thread var2 = Thread.currentThread();
         ClassLoader var3 = var2.getContextClassLoader();

         try {
            var2.setContextClassLoader(this.classLoader);
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  JMSBaseConnection.this.associateTransactionInternal(var1);
                  return null;
               }
            });
         } catch (PrivilegedActionException var9) {
            this.throwResourceException("Error associating message with current transaction", var9.getException(), false);
            return;
         } finally {
            var2.setContextClassLoader(var3);
         }

      } else {
         try {
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  JMSBaseConnection.this.associateTransactionInternal(var1);
                  return null;
               }
            });
         } catch (PrivilegedActionException var11) {
            this.throwResourceException("Error associating message with current transaction", var11.getException(), false);
         }
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

   private void throwTempResourceException() throws ResourceException {
      this.mc.sendEvent(5, (Exception)null);
      throw new TemporaryResourceException();
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
            var3 = (XAResource)SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  return JMSBaseConnection.this.getXAResourceInternal();
               }
            });
         } catch (PrivilegedActionException var8) {
            if (var8.getException() instanceof NoEnlistXAResourceException) {
               throw (NoEnlistXAResourceException)var8.getException();
            }

            this.throwResourceException("Error getting XA resource", var8.getException());
            return null;
         } finally {
            var1.setContextClassLoader(var2);
         }

         return var3;
      } else {
         try {
            return (XAResource)SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  return JMSBaseConnection.this.getXAResourceInternal();
               }
            });
         } catch (PrivilegedActionException var10) {
            if (var10.getException() instanceof NoEnlistXAResourceException) {
               throw (NoEnlistXAResourceException)var10.getException();
            } else {
               this.throwResourceException("Error getting XA resource", var10.getException());
               return null;
            }
         }
      }
   }

   private synchronized XAResource getXAResourceInternal() throws ResourceException {
      if (this.xaSession != null && !(this.xaSession instanceof XASessionInternal)) {
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
      return this.xcf != null && this.xcf instanceof XAConnectionFactory;
   }

   public void setExceptionListener(final ExceptionListener var1) throws ResourceException {
      this.elistener = var1;
      if (this.classLoader != null) {
         Thread var2 = Thread.currentThread();
         ClassLoader var3 = var2.getContextClassLoader();

         try {
            var2.setContextClassLoader(this.classLoader);
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  JMSBaseConnection.this.setExceptionListenerInternal(var1);
                  return null;
               }
            });
         } catch (PrivilegedActionException var9) {
            this.throwResourceException("Error setting exception listener", var9.getException());
            return;
         } finally {
            var2.setContextClassLoader(var3);
         }

      } else {
         final ExceptionListener var12 = var1;

         try {
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  JMSBaseConnection.this.setExceptionListenerInternal(var12);
                  return null;
               }
            });
         } catch (PrivilegedActionException var11) {
            this.throwResourceException("Error setting exception listener", var11.getException());
         }
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

   public void setAcknowledgeMode(final int var1) throws ResourceException {
      if (this.classLoader != null) {
         Thread var2 = Thread.currentThread();
         ClassLoader var3 = var2.getContextClassLoader();

         try {
            var2.setContextClassLoader(this.classLoader);
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  JMSBaseConnection.this.setAcknowledgeModeInternal(var1);
                  return null;
               }
            });
         } catch (PrivilegedActionException var9) {
            this.throwResourceException("Error setting acknowledge mode", var9.getException());
            return;
         } finally {
            var2.setContextClassLoader(var3);
         }

      } else {
         final int var12 = var1;

         try {
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  JMSBaseConnection.this.setAcknowledgeModeInternal(var12);
                  return null;
               }
            });
         } catch (PrivilegedActionException var11) {
            this.throwResourceException("Error setting acknowledge mode", var11.getException());
         }
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
               if (this.destType == 0 && this.connection instanceof QueueConnection) {
                  this.session = ((QueueConnection)this.connection).createQueueSession(this.transacted, this.ackMode);
               } else if (this.destType == 1 && this.connection instanceof TopicConnection) {
                  this.session = ((TopicConnection)this.connection).createTopicSession(this.transacted, this.ackMode);
               } else {
                  this.session = this.connection.createSession(this.transacted, this.ackMode);
               }
            } catch (Throwable var4) {
               if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
                  BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var4);
               }

               this.throwResourceException("Error starting a transaction", var4);
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
            final Session var3 = this.session;
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  try {
                     var3.recover();
                  } catch (Throwable var2) {
                     if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
                        BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var2);
                     }

                     JMSBaseConnection.this.throwResourceException("Error recovering messages", var2, false);
                  }

                  return null;
               }
            });
         } catch (Throwable var10) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var10);
            }

            this.throwResourceException("Error recovering messages", var10, false);
            return;
         } finally {
            var1.setContextClassLoader(var2);
         }

      } else {
         try {
            final Session var13 = this.session;
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  try {
                     var13.recover();
                  } catch (Throwable var2) {
                     if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
                        BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var2);
                     }

                     JMSBaseConnection.this.throwResourceException("Error recovering messages", var2, false);
                  }

                  return null;
               }
            });
         } catch (Throwable var12) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var12);
            }

            this.throwResourceException("Error recovering messages", var12, false);
         }
      }
   }

   void createTransactedSession() throws ResourceException {
      if (this.classLoader != null) {
         Thread var1 = Thread.currentThread();
         ClassLoader var2 = var1.getContextClassLoader();

         try {
            var1.setContextClassLoader(this.classLoader);
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  JMSBaseConnection.this.createTransactedSessionInternal();
                  return null;
               }
            });
         } catch (PrivilegedActionException var8) {
            this.throwResourceException("Error creating transacted session", var8.getException());
            return;
         } finally {
            var1.setContextClassLoader(var2);
         }

      } else {
         try {
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  JMSBaseConnection.this.createTransactedSessionInternal();
                  return null;
               }
            });
         } catch (PrivilegedActionException var10) {
            this.throwResourceException("Error creating transacted session", var10.getException());
         }
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
               if (this.destType == 0 && this.connection instanceof QueueConnection) {
                  this.session = ((QueueConnection)this.connection).createQueueSession(true, this.ackMode);
               } else if (this.destType == 1 && this.connection instanceof TopicConnection) {
                  this.session = ((TopicConnection)this.connection).createTopicSession(true, this.ackMode);
               } else {
                  this.session = this.connection.createSession(true, this.ackMode);
               }

               if (this.mlistener != null) {
                  this.setMessageListener(this.mlistener);
               }
            } catch (Throwable var4) {
               if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
                  BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var4);
               }

               this.throwResourceException("Error beginning a transaction", var4);
            }

            this.transacted = true;
         }
      }
   }

   void commit() throws ResourceException {
      if (!this.transacted) {
         this.throwResourceException("Error committing a transaction -- not transacted", (Throwable)null);
      }

      if (this.classLoader != null) {
         Thread var1 = Thread.currentThread();
         ClassLoader var2 = var1.getContextClassLoader();

         try {
            var1.setContextClassLoader(this.classLoader);
            final Session var3 = this.session;
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  try {
                     var3.commit();
                  } catch (Throwable var2) {
                     if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
                        BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var2);
                     }

                     JMSBaseConnection.this.throwResourceException("Error committing a transaction", var2);
                  }

                  return null;
               }
            });
         } catch (Throwable var8) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var8);
            }

            this.throwResourceException("Error committing a transaction", var8);
            return;
         } finally {
            var1.setContextClassLoader(var2);
         }

      } else {
         try {
            final Session var11 = this.session;
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  try {
                     var11.commit();
                  } catch (Throwable var2) {
                     if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
                        BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var2);
                     }

                     JMSBaseConnection.this.throwResourceException("Error committing a transaction", var2);
                  }

                  return null;
               }
            });
         } catch (Throwable var10) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var10);
            }

            this.throwResourceException("Error committing a transaction", var10);
         }
      }
   }

   void rollback() throws ResourceException {
      if (!this.transacted) {
         this.throwResourceException("Error rolling back a transaction -- not transacted", (Throwable)null);
      }

      if (this.classLoader != null) {
         Thread var1 = Thread.currentThread();
         ClassLoader var2 = var1.getContextClassLoader();

         try {
            var1.setContextClassLoader(this.classLoader);
            final Session var3 = this.session;
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  try {
                     var3.rollback();
                  } catch (Throwable var2) {
                     if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
                        BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var2);
                     }

                     JMSBaseConnection.this.throwResourceException("Error committing a transaction", var2);
                  }

                  return null;
               }
            });
         } catch (Throwable var8) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var8);
            }

            this.throwResourceException("Error rolling back a transaction", var8);
            return;
         } finally {
            var1.setContextClassLoader(var2);
         }

      } else {
         try {
            final Session var11 = this.session;
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  try {
                     var11.rollback();
                  } catch (Throwable var2) {
                     if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
                        BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var2);
                     }

                     JMSBaseConnection.this.throwResourceException("Error committing a transaction", var2);
                  }

                  return null;
               }
            });
         } catch (Throwable var10) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var10);
            }

            this.throwResourceException("Error rolling back a  transaction", var10);
         }
      }
   }

   void setManagedConnection(JMSManagedConnection var1) {
      this.mc = var1;
      this.metaData = new AdapterConnectionMetaDataImpl(var1, this.mcf);
   }

   private InitialContext getInitialContext() throws NamingException {
      InitialContext var1 = null;
      if (this.url != null || this.userName != null && this.password != null) {
         Hashtable var2 = new Hashtable();
         if (this.userName != null && this.password != null) {
            var2.put("java.naming.security.principal", this.userName);
            var2.put("java.naming.security.credentials", this.password);
         }

         var2.put("java.naming.factory.initial", this.icFactory);
         if (this.url != null) {
            var2.put("java.naming.provider.url", this.url);
         }

         var1 = new InitialContext(var2);
      } else {
         var1 = new InitialContext();
      }

      this.subject = SecurityServiceManager.getCurrentSubject(kernelId);
      String var3 = SubjectUtils.getUsername(this.subject);
      if (WLSPrincipals.isKernelUsername(var3)) {
         this.subject = SubjectUtils.getAnonymousSubject();
         this.subject.setQOS((byte)101);
      }

      return var1;
   }

   private void ensureStarted() throws ResourceException {
      if (this.closed) {
         this.startInternal();
         this.closed = false;
      }

   }

   public void onMessage(final Message var1) {
      try {
         final Session var2 = this.session;
         final MessageListener var4 = this.mlistener;
         SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
            public Object run() {
               if (var4 != null) {
                  var4.onMessage(var1);
                  return null;
               } else {
                  if (var2 != null) {
                     try {
                        var2.recover();
                     } catch (Throwable var2x) {
                        if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
                           BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var2x);
                        }
                     }
                  }

                  return null;
               }
            }
         });
      } catch (final Throwable var5) {
         if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
            BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var5);
         }

         WorkManagerFactory.getInstance().getSystem().schedule(new WorkAdapter() {
            public void run() {
               JMSBaseConnection.this.onException(new weblogic.jms.common.JMSException("Fail to call runAs()", var5));
            }
         });
      }

   }

   public void onException(JMSException var1) {
      synchronized(this.onExceptionLock) {
         if (this.elistener != null) {
            this.elistener.onException(var1);
         }

      }
   }

   public void acknowledge(final Message var1) throws ResourceException {
      if (this.classLoader != null) {
         Thread var2 = Thread.currentThread();
         ClassLoader var3 = var2.getContextClassLoader();

         try {
            var2.setContextClassLoader(this.classLoader);
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  try {
                     var1.acknowledge();
                  } catch (Throwable var2) {
                     if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
                        BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var2);
                     }

                     JMSBaseConnection.this.throwResourceException("Error acknowledging messages", var2, false);
                  }

                  return null;
               }
            });
         } catch (Throwable var9) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var9);
            }

            this.throwResourceException("Error acknowledge messages", var9, false);
            return;
         } finally {
            var2.setContextClassLoader(var3);
         }

      } else {
         try {
            SecurityServiceManager.runAs(kernelId, this.subject, new PrivilegedExceptionAction() {
               public Object run() throws ResourceException {
                  try {
                     var1.acknowledge();
                  } catch (Throwable var2) {
                     if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
                        BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var2);
                     }

                     JMSBaseConnection.this.throwResourceException("Error acknowledging messages", var2, false);
                  }

                  return null;
               }
            });
         } catch (Throwable var11) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Exception:", var11);
            }

            this.throwResourceException("Error acknowledging messages", var11, false);
         }
      }
   }
}
